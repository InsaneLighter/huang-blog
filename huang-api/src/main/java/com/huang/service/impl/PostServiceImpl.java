package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.mapper.PostMapper;
import com.huang.entity.PostEntity;
import com.huang.service.PostService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Map;


@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {
    @Autowired
    private PostMapper postMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PostEntity> wrapper = new QueryWrapper<>();
        String title = (String) params.getOrDefault("title", "");
        String status = (String) params.getOrDefault("status", "");
        String category = (String) params.getOrDefault("category", "");
        if(StringUtils.hasText(title)){
            wrapper.like("title",title);
        }
        if(StringUtils.hasText(title)){
            wrapper.eq("status",status);
        }
        if(StringUtils.hasText(title)){
            wrapper.eq("category",category);
        }

        IPage page = this.page(new Query().getPage(params), wrapper);
        return new PageUtils(page);
    }

}