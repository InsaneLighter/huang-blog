package com.huang.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.CategoryEntity;
import com.huang.entity.PostCategoryEntity;
import com.huang.mapper.CategoryMapper;
import com.huang.mapper.PostCategoryMapper;
import com.huang.service.CategoryService;
import com.huang.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {
    @Autowired
    private PostCategoryMapper postCategoryMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );
        return new PageUtils(page);
    }

    private List<CategoryEntity> getChildren(CategoryEntity entity, List<CategoryEntity> entities) {
        return entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentId().equals(entity.getId()))
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, entities)))
                .collect(Collectors.toList());
    }

    @Override
    public PageUtils queryAll() {
        List<CategoryEntity> list = this.list();
        Page<CategoryEntity> page = new Page<>();
        page.setRecords(list);
        return new PageUtils(page);
    }

    @Override
    public Map<String,Object> queryByIds(Map<String, Object> params) {
        Map<String,Object> result = new HashMap<>();
        Object ids = params.get("ids");
        String realCategoryIds;
        if(ids instanceof ArrayList){
            ArrayList<String> categoryIds = (ArrayList<String>) params.get("ids");
            realCategoryIds = String.join(",", categoryIds);
        }else {
            realCategoryIds = (String) params.get("ids");
        }
        Page<PostCategoryEntity> page = new Page<>();
        boolean compare = false;
        if (StringUtils.hasText(realCategoryIds)) {
            //分类关联文章
            QueryWrapper<PostCategoryEntity> postCategoryWrapper = new QueryWrapper<>();
            List<String> categoryIds = Arrays.stream(realCategoryIds.split(",")).collect(Collectors.toList());
            postCategoryWrapper.in("category_id",categoryIds);
            List<PostCategoryEntity> postCategoryEntities = postCategoryMapper.selectList(postCategoryWrapper);
            page.setRecords(postCategoryEntities);

            //是否存在子节点
            QueryWrapper<CategoryEntity> categoryWrapper = new QueryWrapper<>();
            categoryWrapper.in("id",categoryIds).or().in("parent_id",categoryIds);
            List<CategoryEntity> categoryEntities = this.list(categoryWrapper);
            List<String> resultCategoryIds = categoryEntities.stream().map(CategoryEntity::getId).distinct().collect(Collectors.toList());
            compare = CommonUtils.compare(categoryIds, resultCategoryIds);
        }
        result.put("data",new PageUtils(page));
        result.put("hasChildren",compare);
        return result;
    }

    @Override
    public CategoryEntity queryByName(String name) {
        QueryWrapper<CategoryEntity> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.eq("name",name);
        return this.list(categoryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PageUtils queryAllTree() {
        List<CategoryEntity> list = this.list();
        Page<CategoryEntity> page = new Page<>();
        if (!CollectionUtils.isEmpty(list)) {
            List<CategoryEntity> entities = list.stream()
                    .filter(categoryEntity -> Constant.ROOT_CATEGORY_PARENT_ID.equals(categoryEntity.getParentId()))
                    .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, list)))
                    .collect(Collectors.toList());
            page.setRecords(entities);
        }
        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> getCategories(Map<String,Object> params) {
        QueryWrapper<CategoryEntity> categoryEntityQueryWrapper = new QueryWrapper<>();
        String keyword = (String) params.getOrDefault("keyword", "");
        if (StringUtils.hasText(keyword)) {
            categoryEntityQueryWrapper.like("name", keyword);
        }
        categoryEntityQueryWrapper.ne("id","0");
        return this.list(categoryEntityQueryWrapper);
    }

}