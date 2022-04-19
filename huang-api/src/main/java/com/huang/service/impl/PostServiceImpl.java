package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.*;
import com.huang.entity.enums.PostStatus;
import com.huang.mapper.*;
import com.huang.service.PostService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.utils.ServiceUtils;
import com.huang.vo.PostVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        }
        String title = (String) params.getOrDefault("title", "");
        if (StringUtils.hasText(title)) {
            postWrapper.like("title", title);
        }
        String status = (String) params.getOrDefault("status", "");
        if (StringUtils.hasText(status)) {
            postWrapper.eq("status", PostStatus.valueOf(status));
        }
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

}