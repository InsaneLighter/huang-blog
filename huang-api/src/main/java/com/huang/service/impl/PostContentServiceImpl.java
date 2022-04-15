package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.PostContentMapper;
import com.huang.entity.PostContentEntity;
import com.huang.service.PostContentService;


@Service("postContentService")
public class PostContentServiceImpl extends ServiceImpl<PostContentMapper, PostContentEntity> implements PostContentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PostContentEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<PostContentEntity>()
        );
        return new PageUtils(page);
    }

}