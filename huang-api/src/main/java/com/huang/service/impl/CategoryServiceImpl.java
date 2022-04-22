package com.huang.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.CategoryEntity;
import com.huang.mapper.CategoryMapper;
import com.huang.service.CategoryService;
import com.huang.utils.Constant;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

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
    public PageUtils queryAll(Map<String, Object> params) {
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


}