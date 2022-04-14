package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.PostMapper;
import com.huang.entity.PostEntity;
import com.huang.service.PostService;


@Service("postService")
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PostEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<PostEntity>()
        );
        return new PageUtils(page);
    }

}