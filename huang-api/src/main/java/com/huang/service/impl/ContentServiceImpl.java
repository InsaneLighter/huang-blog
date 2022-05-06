package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.*;
import com.huang.entity.enums.PostStatus;
import com.huang.entity.param.ContentParam;
import com.huang.event.BlogEvent;
import com.huang.mapper.*;
import com.huang.service.ContentService;
import com.huang.utils.*;
import io.minio.messages.Bucket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service("contentService")
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements ContentService {
    private static final Pattern summaryPattern = Pattern.compile("\t|\r|\n");
    private static final Pattern BLANK_PATTERN = Pattern.compile("\\s");

    @Autowired
    private ContentPatchLogMapper contentPatchLogMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostContentMapper postContentMapper;
    @Autowired
    private PostCategoryMapper postCategoryMapper;
    @Autowired
    private PostTagMapper postTagMapper;
    @Autowired
    private JournalMapper journalMapper;
    @Autowired
    private JournalPatchLogMapper journalPatchLogMapper;
    @Autowired
    private MinioUtil minioUtil;
    @Value("${minio.content.bucketName}")
    private String bucketName;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ContentEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<ContentEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticle(ContentParam contentParam) {
        if (contentParam.getStatus() == null) {
            contentParam.setStatus(PostStatus.DRAFT);
        }
        //content
        ContentEntity contentEntity = new ContentEntity();
        BeanUtils.copyProperties(contentParam,contentEntity);
        contentEntity.setWordCount(generateWordCount(contentEntity.getContent()));
        this.save(contentEntity);
        //contentPatch
        if (contentEntity.getStatus().getValue().equals(PostStatus.PUBLISHED.getValue())) {
            ContentPatchLogEntity contentPatchLogEntity = new ContentPatchLogEntity();
            contentPatchLogEntity.setOriginalContent(contentParam.getOriginContent());
            contentPatchLogEntity.setSourceId(contentEntity.getId());
            contentPatchLogMapper.insert(contentPatchLogEntity);
        }
        //post
        PostEntity postEntity = new PostEntity();
        BeanUtils.copyProperties(contentParam,postEntity);
        postEntity.setCreateTime(null);
        if (!StringUtils.hasText(contentParam.getSummary())){
            postEntity.setSummary(generateSummary(contentEntity.getContent()));
        }
        postMapper.insert(postEntity);
        applicationContext.publishEvent(new BlogEvent(this,postEntity));
        final String postEntityId = postEntity.getId();
        //post content
        PostContentEntity postContentEntity = new PostContentEntity();
        postContentEntity.setPostId(postEntityId);
        postContentEntity.setContentId(contentEntity.getId());
        postContentMapper.insert(postContentEntity);
        //category
        saveArticleCategories(contentParam, postEntityId);
        //tag
        saveArticleTags(contentParam, postEntityId);
        //journal
        saveJournalChanges("绞尽脑汁写了一篇文章: "+ contentParam.getTitle());
        //TODO 定时发布
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ContentParam contentParam) {
        String postId = contentParam.getId();
        //post
        PostEntity postEntity = new PostEntity();
        BeanUtils.copyProperties(contentParam,postEntity);
        contentParam.setId(null);
        if (!StringUtils.hasText(contentParam.getSummary())){
            postEntity.setSummary(generateSummary(contentParam.getContent()));
        }
        postMapper.updateById(postEntity);
        //content
        QueryWrapper<PostContentEntity> postContentWrapper = new QueryWrapper<>();
        postContentWrapper.eq("post_id",postId);
        PostContentEntity postContentEntity = postContentMapper.selectOne(postContentWrapper);
        ContentEntity contentEntity = new ContentEntity();
        contentEntity.setId(postContentEntity.getContentId());
        contentEntity.setContent(contentParam.getContent());
        contentEntity.setOriginContent(contentParam.getOriginContent());
        contentEntity.setTitle(contentParam.getTitle());
        contentEntity.setStatus(contentParam.getStatus());
        contentEntity.setWordCount(generateWordCount(contentParam.getContent()));
        this.updateById(contentEntity);
        //contentPatch
        ContentPatchLogEntity contentPatchLogEntity = new ContentPatchLogEntity();
        contentPatchLogEntity.setOriginalContent(contentEntity.getOriginContent());
        contentPatchLogEntity.setSourceId(contentEntity.getId());
        contentPatchLogMapper.insert(contentPatchLogEntity);
        //category
        QueryWrapper<PostCategoryEntity> postCategoryWrapper = new QueryWrapper<>();
        postCategoryWrapper.eq("post_id",postId);
        List<PostCategoryEntity> postCategoryEntities = postCategoryMapper.selectList(postCategoryWrapper);
        List<String> postCategoryIds = postCategoryEntities.stream().map(PostCategoryEntity::getId).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(postCategoryIds)) {
            postCategoryMapper.deleteBatchIds(postCategoryIds);
        }
        saveArticleCategories(contentParam,postId);
        //tag
        QueryWrapper<PostTagEntity> postTagWrapper = new QueryWrapper<>();
        postTagWrapper.eq("post_id",postId);
        List<PostTagEntity> postTagEntities = postTagMapper.selectList(postTagWrapper);
        List<String> postTagIds = postTagEntities.stream().map(PostTagEntity::getId).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(postTagIds)){
            postTagMapper.deleteBatchIds(postTagIds);
        }
        saveArticleTags(contentParam,postId);
        //journal
        saveJournalChanges("绞尽脑汁修改了一篇文章: "+ contentParam.getTitle());
    }

    @Override
    public String upload(MultipartFile file) {
        if(file == null ){
            throw new RuntimeException("上传附件为空！");
        }
        try {
            Optional<Bucket> bucket = minioUtil.getBucket(bucketName);
            if (!bucket.isPresent()) {
                minioUtil.createBucket(bucketName);
            }
            return minioUtil.uploadFile(file, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * @Description: 自动生成文章字数
     * @param content:
     * @return: java.lang.Integer
     **/
    private Integer generateWordCount(String content) {
        if (content == null) {
            return 0;
        }
        String cleanContent = CommonUtils.cleanHtmlTag(content);
        Matcher matcher = BLANK_PATTERN.matcher(cleanContent);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return cleanContent.length() - count;
    }

    /***
     * @Description: 自动生成摘要内容
     * @param content:
     * @return: java.lang.String
     **/
    private String generateSummary(String content) {
        if (!StringUtils.hasText(content)) {
            return "";
        }
        String text = CommonUtils.cleanHtmlTag(content);
        Matcher matcher = summaryPattern.matcher(text);
        text = matcher.replaceAll("");
        return text.substring(0, Math.min(text.length(), 150));
    }


    private void saveArticleCategories(ContentParam contentParam, String postEntityId) {
        Set<String> categoryIds = contentParam.getCategoryIds();
        if(CollectionUtils.isEmpty(categoryIds)){
            categoryIds = new HashSet<>(1);
            categoryIds.add("0");
        }
        categoryIds.forEach(categoryId -> {
            PostCategoryEntity postCategoryEntity = new PostCategoryEntity();
            postCategoryEntity.setPostId(postEntityId);
            postCategoryEntity.setCategoryId(categoryId);
            postCategoryMapper.insert(postCategoryEntity);
        });
    }

    private void saveArticleTags(ContentParam contentParam, String postEntityId) {
        Set<String> tagIds = contentParam.getTagIds();
        if(!CollectionUtils.isEmpty(tagIds)){
            tagIds.forEach(tagId -> {
                PostTagEntity postTagEntity = new PostTagEntity();
                postTagEntity.setPostId(postEntityId);
                postTagEntity.setTagId(tagId);
                postTagMapper.insert(postTagEntity);
            });
        }
    }

    private void saveJournalChanges(String journalContent) {
        //journal
        JournalEntity journalEntity = new JournalEntity();
        journalEntity.setContent(journalContent);
        journalEntity.setMood(Constant.SUNNY);
        journalEntity.setWeather(CommonUtils.getCurrentWeather());
        journalMapper.insert(journalEntity);
        applicationContext.publishEvent(new BlogEvent(this,journalEntity));
        //journalPatch
        JournalPatchLogEntity journalPatchLogEntity = new JournalPatchLogEntity();
        journalPatchLogEntity.setSourceId(journalEntity.getId());
        journalPatchLogEntity.setOriginalContent(journalEntity.getContent());
        journalPatchLogMapper.insert(journalPatchLogEntity);
    }
}