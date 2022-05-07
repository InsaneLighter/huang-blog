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
        if (StringUtils.hasText(keyword)) {
            sysLogWrapper.like("uri", keyword)
                    .or().like("method", keyword)
                    .or().like("request_ip", keyword)
                    .or().like("address", keyword)
                    .or().like("exception_detail", keyword);
        }
        if(StringUtils.hasText(startDate)){
            sysLogWrapper.ge("create_time",startDate);
        }
        if(StringUtils.hasText(endDate)){
            sysLogWrapper.le("create_time",endDate);
        }
        sysLogWrapper.orderByDesc("create_time");
        IPage<SysLogEntity> page = this.page(new Query().getPage(params), sysLogWrapper);
        return new PageUtils(page);
    }

}