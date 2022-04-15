package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.PostCategoryMapper;
import com.huang.entity.PostCategoryEntity;
import com.huang.service.PostCategoryService;


@Service("postCategoryService")
public class PostCategoryServiceImpl extends ServiceImpl<PostCategoryMapper, PostCategoryEntity> implements PostCategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PostCategoryEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<PostCategoryEntity>()
        );
        return new PageUtils(page);
    }

}