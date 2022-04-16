package com.huang.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.PostEntity;
import com.huang.mapper.PostMapper;
import com.huang.service.PostService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("postService")
public class PostServiceImpl extends ServiceImpl<PostMapper, PostEntity> implements PostService {
    @Autowired
    private PostMapper mapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage page = new Query().getPage(params);
        QueryWrapper<PostVo> wrapper = new QueryWrapper<>();
        IPage<PostVo> resultPage = mapper.queryPage(page,wrapper);
        return new PageUtils(resultPage);
    }

}