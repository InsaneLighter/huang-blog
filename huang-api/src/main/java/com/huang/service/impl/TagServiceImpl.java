package com.huang.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.TagEntity;
import com.huang.mapper.TagMapper;
import com.huang.service.TagService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.stereotype.Service;
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

}