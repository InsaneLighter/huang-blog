package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.JournalPatchLogMapper;
import com.huang.entity.JournalPatchLogEntity;
import com.huang.service.JournalPatchLogService;


@Service("journalPatchLogService")
public class JournalPatchLogServiceImpl extends ServiceImpl<JournalPatchLogMapper, JournalPatchLogEntity> implements JournalPatchLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<JournalPatchLogEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<JournalPatchLogEntity>()
        );
        return new PageUtils(page);
    }

}