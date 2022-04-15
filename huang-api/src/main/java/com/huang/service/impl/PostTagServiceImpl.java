package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.PostTagMapper;
import com.huang.entity.PostTagEntity;
import com.huang.service.PostTagService;


@Service("postTagService")
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTagEntity> implements PostTagService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PostTagEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<PostTagEntity>()
        );
        return new PageUtils(page);
    }

}