package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.ContentPatchLogMapper;
import com.huang.entity.ContentPatchLogEntity;
import com.huang.service.ContentPatchLogService;


@Service("contentPatchLogService")
public class ContentPatchLogServiceImpl extends ServiceImpl<ContentPatchLogMapper, ContentPatchLogEntity> implements ContentPatchLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ContentPatchLogEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<ContentPatchLogEntity>()
        );
        return new PageUtils(page);
    }

}