package com.huang.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import com.huang.mapper.SysStatisticsMapper;
import com.huang.entity.SysStatisticsEntity;
import com.huang.service.SysStatisticsService;


@Service("sysStatisticsService")
public class SysStatisticsServiceImpl extends ServiceImpl<SysStatisticsMapper, SysStatisticsEntity> implements SysStatisticsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysStatisticsEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<SysStatisticsEntity>()
        );
        return new PageUtils(page);
    }

}