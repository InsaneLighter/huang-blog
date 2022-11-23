package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.huang.entity.*;
import com.huang.entity.enums.PostStatus;
import com.huang.entity.param.BatchUpdateStatusParam;
import com.huang.entity.vo.ContentVo;
import com.huang.entity.vo.FrontPostVo;
import com.huang.entity.vo.PostVo;
import com.huang.mapper.*;
import com.huang.service.PostService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.utils.ServiceUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service("postService")
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private PostCategoryMapper postCategoryMapper;
    @Autowired
    private PostTagMapper postTagMapper;
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    protected PostContentMapper postContentMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //get post
        QueryWrapper<PostEntity> postWrapper = new QueryWrapper<>();
        String paramCategoryId = (String) params.getOrDefault("categoryId", "");
        Set<String> prePostIds = null;
        if(StringUtils.hasText(paramCategoryId)){
            QueryWrapper<PostCategoryEntity> preCategoryWrapper = new QueryWrapper<>();
            preCategoryWrapper.eq("category_id",paramCategoryId);
            List<PostCategoryEntity> postCategoryEntities = postCategoryMapper.selectList(preCategoryWrapper);
            prePostIds = ServiceUtils.fetchProperty(postCategoryEntities, PostCategoryEntity::getPostId);
        }

        if(prePostIds != null && !prePostIds.isEmpty()){
            postWrapper.in("id", prePostIds);
        }else if(StringUtils.hasText(paramCategoryId)){
            postWrapper.eq("id", "-1");
        }
        String title = (String) params.getOrDefault("title", "");
        if (StringUtils.hasText(title)) {
            postWrapper.like("title", title);
        }
        String status = (String) params.getOrDefault("status", "");
        if (StringUtils.hasText(status)) {
            postWrapper.eq("status", PostStatus.valueOf(status));
        }
        postWrapper.orderByDesc("top_priority");
        IPage<PostEntity> postEntityPage = this.page(
                new Query().getPage(params),
                postWrapper
        );
        List<PostEntity> records = postEntityPage.getRecords();
        Set<String> postIds = ServiceUtils.fetchProperty(records, PostEntity::getId);
        List<PostVo> postVos = null;
        if (!postIds.isEmpty()) {
            //get category
            QueryWrapper<PostCategoryEntity> postCategoryWrapper = new QueryWrapper<>();
            postCategoryWrapper.in("post_id", postIds);
            List<PostCategoryEntity> postCategoryEntities = postCategoryMapper.selectList(postCategoryWrapper);
            Set<String> categoryIds = ServiceUtils.fetchProperty(postCategoryEntities, PostCategoryEntity::getCategoryId);
            List<CategoryEntity> categoryEntities = null;
            if (!categoryIds.isEmpty()) {
                QueryWrapper<CategoryEntity> categoryWrapper = new QueryWrapper<>();
                categoryWrapper.in("id", categoryIds);
                categoryEntities = categoryMapper.selectList(categoryWrapper);
            }
            //get tag
            QueryWrapper<PostTagEntity> postTagsWrapper = new QueryWrapper<>();
            postTagsWrapper.in("post_id", postIds);
            List<PostTagEntity> postTagEntities = postTagMapper.selectList(postTagsWrapper);
            Set<String> tagIds = ServiceUtils.fetchProperty(postTagEntities, PostTagEntity::getTagId);
            List<TagEntity> tagEntities = null;
            if (!tagIds.isEmpty()) {
                QueryWrapper<TagEntity> tagWrapper = new QueryWrapper<>();
                tagWrapper.in("id", tagIds);
                tagEntities = tagMapper.selectList(tagWrapper);
            }

            List<CategoryEntity> finalCategoryEntities = categoryEntities;
            List<TagEntity> finalTagEntities = tagEntities;
            postVos = records.stream()
                    .map(postEntity -> {
                        PostVo postVo = new PostVo();
                        //set post
                        BeanUtils.copyProperties(postEntity, postVo);
                        String postEntityId = postEntity.getId();
                        //set category
                        List<CategoryEntity> categories = postCategoryEntities.stream()
                                .filter(postCategoryEntity -> postCategoryEntity.getPostId().equals(postEntityId))
                                .map(postCategoryEntity -> {
                                    String categoryId = postCategoryEntity.getCategoryId();
                                    return finalCategoryEntities.stream().filter(categoryEntity -> categoryEntity.getId().equals(categoryId)).findFirst().orElse(null);
                                })
                                .collect(Collectors.toList());
                        postVo.setCategories(categories);
                        //set tag
                        List<TagEntity> tags = postTagEntities.stream()
                                .filter(postTagEntity -> postTagEntity.getPostId().equals(postEntityId))
                                .map(postTagEntity -> {
                                    String tagId = postTagEntity.getTagId();
                                    return finalTagEntities.stream().filter(tagEntity -> tagEntity.getId().equals(tagId)).findFirst().orElse(null);
                                })
                                .collect(Collectors.toList());
                        postVo.setTags(tags);
                        return postVo;
                    })
                    .collect(Collectors.toList());
        }
        Page<PostVo> postVoPage = new Page<>();
        BeanUtils.copyProperties(postEntityPage, postVoPage);
        postVoPage.setRecords(postVos);
        return new PageUtils(postVoPage);
    }

    @Override
    public ContentVo getByPostId(String postId) {
        ContentVo contentVo = new ContentVo();
        PostEntity postEntity = this.getById(postId);
        BeanUtils.copyProperties(postEntity,contentVo);
        //category
        QueryWrapper<PostCategoryEntity> postCategoryWrapper = new QueryWrapper<>();
        postCategoryWrapper.eq("post_id",postId);
        List<PostCategoryEntity> postCategoryEntities = postCategoryMapper.selectList(postCategoryWrapper);
        List<String> categoryIds = postCategoryEntities.stream().map(PostCategoryEntity::getCategoryId).collect(Collectors.toList());
        contentVo.setCategoryIds(categoryIds);
        //tag
        QueryWrapper<PostTagEntity> postTagWrapper = new QueryWrapper<>();
        postTagWrapper.eq("post_id",postId);
        List<PostTagEntity> postTagEntities = postTagMapper.selectList(postTagWrapper);
        List<String> tagIds = postTagEntities.stream().map(PostTagEntity::getTagId).collect(Collectors.toList());
        contentVo.setTagIds(tagIds);
        //content
        QueryWrapper<PostContentEntity> postContentWrapper = new QueryWrapper<>();
        postContentWrapper.eq("post_id",postId);
        List<PostContentEntity> postContentEntities = postContentMapper.selectList(postContentWrapper);
        String contentId = postContentEntities.stream().map(PostContentEntity::getContentId).findFirst().orElse("");
        if(StringUtils.hasText(contentId)){
            ContentEntity contentEntity = contentMapper.selectById(contentId);
            String content = contentEntity.getContent();
            String originContent = contentEntity.getOriginContent();
            contentVo.setContent(content);
            contentVo.setOriginContent(originContent);
        }
        return contentVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //post
        List<PostEntity> postEntities = Arrays.stream(ids).map(id -> {
            PostEntity postEntity = new PostEntity();
            postEntity.setId(id);
            //直接删除 需要将状态置为RECYCLE
            postEntity.setStatus(PostStatus.RECYCLE);
            return postEntity;
        }).collect(Collectors.toList());
        this.updateBatchById(postEntities);
        this.removeByIds(Arrays.asList(ids));

        Arrays.stream(ids).forEach(id -> {
            //post tag
            QueryWrapper<PostTagEntity> postTagWrapper = new QueryWrapper<>();
            postTagWrapper.eq("post_id",id);
            postTagMapper.delete(postTagWrapper);
            //post category
            QueryWrapper<PostCategoryEntity> postCategoryWrapper = new QueryWrapper<>();
            postCategoryWrapper.eq("post_id",id);
            postCategoryMapper.delete(postCategoryWrapper);
            //post content
            QueryWrapper<PostContentEntity> postContentWrapper = new QueryWrapper<>();
            postContentWrapper.eq("post_id",id);
            PostContentEntity postContentEntity = postContentMapper.selectOne(postContentWrapper);
            postContentMapper.deleteById(postContentEntity);
            //content
            String contentId = postContentEntity.getContentId();
            ContentEntity contentEntity = new ContentEntity();
            contentEntity.setId(contentId);
            //将状态置为RECYCLE
            contentEntity.setStatus(PostStatus.RECYCLE);
            contentMapper.updateById(contentEntity);
        });


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusInBatch(BatchUpdateStatusParam batchUpdateStatusParam) {
        PostStatus status = batchUpdateStatusParam.getStatus();
        batchUpdateStatusParam.getIds().forEach(id -> {
            PostEntity postEntity = new PostEntity();
            postEntity.setId(id);
            postEntity.setStatus(status);
            this.updateById(postEntity);
        });
    }

    @Override
    public PageUtils queryByCondition(Map<String, Object> params) {
        MPJLambdaWrapper<FrontPostVo> mpjLambdaWrapper = new MPJLambdaWrapper<FrontPostVo>()
                .selectAll(PostEntity.class)
                .selectAs(CategoryEntity::getName, FrontPostVo::getCategory)
                .leftJoin(PostCategoryEntity.class, PostCategoryEntity::getPostId, PostEntity::getId)
                .leftJoin(CategoryEntity.class, CategoryEntity::getId, PostCategoryEntity::getCategoryId);
        String keyword = (String) params.getOrDefault("keyword", "");
        if (StringUtils.hasText(keyword)) {
            mpjLambdaWrapper.and(condition -> {
                condition.like(PostEntity::getTitle,keyword)
                .or().like(PostEntity::getSummary, keyword);
            });
        }
        String category = (String) params.getOrDefault("category", "");
        if (StringUtils.hasText(category)) {
            mpjLambdaWrapper.and(condition -> {
                condition.eq(CategoryEntity::getId,category);
            });
        }

        String startDate = (String) params.getOrDefault("startDate", "");
        String endDate = (String) params.getOrDefault("endDate", "");
        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            mpjLambdaWrapper.and(condition -> {
                condition.between(PostEntity::getCreateTime,startDate,endDate);
            });
        }

        IPage<FrontPostVo> page = postMapper.selectJoinPage(new Query().getPage(params), FrontPostVo.class, mpjLambdaWrapper);
        return new PageUtils(page);
    }

}