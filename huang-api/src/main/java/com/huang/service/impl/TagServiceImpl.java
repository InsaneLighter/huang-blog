package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.TagEntity;
import com.huang.mapper.TagMapper;
import com.huang.service.TagService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, TagEntity> implements TagService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TagEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<TagEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryAll(Map<String, Object> params) {
        QueryWrapper<TagEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("name");
        List<TagEntity> list = this.list(wrapper);
        Page<TagEntity> page = new Page<>();
        page.setRecords(list);
        return new PageUtils(page);
    }

}