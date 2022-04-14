package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.ContentMapper;
import com.huang.entity.ContentEntity;
import com.huang.service.ContentService;


@Service("contentService")
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements ContentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ContentEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<ContentEntity>()
        );
        return new PageUtils(page);
    }

}