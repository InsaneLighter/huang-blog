package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.SysLogEntity;
import com.huang.mapper.SysLogMapper;
import com.huang.service.SysLogService;
import com.huang.utils.PageUtils;
import com.huang.utils.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String keyword = (String) params.getOrDefault("keyword", "");
        String startDate = (String) params.getOrDefault("startDate", "");
        String endDate = (String) params.getOrDefault("endDate", "");
        QueryWrapper<SysLogEntity> sysLogWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(startDate)) {
            sysLogWrapper.ge("create_time", startDate + " 00:00:00");
        }
        if (StringUtils.hasText(endDate)) {
            sysLogWrapper.le("create_time", endDate + " 23:59:59");
        }
        if (StringUtils.hasText(keyword)) {
            sysLogWrapper.and(journalEntityQueryWrapper -> {
                journalEntityQueryWrapper.like("uri", keyword);
                journalEntityQueryWrapper.like("method", keyword);
                journalEntityQueryWrapper.like("request_ip", keyword);
                journalEntityQueryWrapper.like("address", keyword);
                journalEntityQueryWrapper.like("exception_detail", keyword);
            });
        }
        sysLogWrapper.orderByDesc("create_time");
        IPage<SysLogEntity> page = this.page(new Query().getPage(params), sysLogWrapper);
        return new PageUtils(page);
    }

}