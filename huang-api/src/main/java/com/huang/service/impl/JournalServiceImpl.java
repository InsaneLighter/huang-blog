package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.JournalMapper;
import com.huang.entity.JournalEntity;
import com.huang.service.JournalService;


@Service("journalService")
public class JournalServiceImpl extends ServiceImpl<JournalMapper, JournalEntity> implements JournalService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<JournalEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<JournalEntity>()
        );
        return new PageUtils(page);
    }

}