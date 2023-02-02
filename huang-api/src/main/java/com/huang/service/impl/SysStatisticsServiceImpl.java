package com.huang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.PostEntity;
import com.huang.entity.SysStatisticsEntity;
import com.huang.entity.vo.LogInfoVo;
import com.huang.entity.vo.VisitInfoVo;
import com.huang.mapper.PostMapper;
import com.huang.mapper.SysStatisticsMapper;
import com.huang.service.SysStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service("sysStatisticsService")
public class SysStatisticsServiceImpl extends ServiceImpl<SysStatisticsMapper, SysStatisticsEntity> implements SysStatisticsService {
    @Autowired
    private SysStatisticsMapper sysStatisticsMapper;
    @Autowired
    private PostMapper postMapper;

    @Override
    public SysStatisticsEntity getStatistics() {
        QueryWrapper<SysStatisticsEntity> statisticsWrapper = new QueryWrapper<>();
        statisticsWrapper.orderByDesc("create_time");
        SysStatisticsEntity entity = sysStatisticsMapper.selectList(statisticsWrapper).stream().findFirst().orElse(null);
        assert entity != null;
        long birthday = entity.getBirthday().getTime();
        long days = (System.currentTimeMillis() - birthday) / (1000 * 24 * 3600);
        entity.setEstablishDays((int) days);
        QueryWrapper<PostEntity> postWrapper = new QueryWrapper<>();
        postWrapper.select("sum(visit) as visit");
        PostEntity postEntity = postMapper.selectOne(postWrapper);
        entity.setViewPostCount(postEntity.getVisit());
        return entity;
    }

    @Override
    public Map<String, Object> visitStatistics() {
        Map<String, Object> map = new HashMap<>(2);
        QueryWrapper<SysStatisticsEntity> statisticsWrapper = new QueryWrapper<>();
        statisticsWrapper.orderByDesc("create_time");
        List<SysStatisticsEntity> sysStatisticsEntities = sysStatisticsMapper.selectList(statisticsWrapper);
        if (sysStatisticsEntities != null && !sysStatisticsEntities.isEmpty()) {
            SysStatisticsEntity entity = sysStatisticsEntities.get(0);
            Integer ipVisit = entity.getIpVisit();
            Integer visit = entity.getVisit();
            AtomicInteger totalCount = new AtomicInteger();
            sysStatisticsEntities.forEach(sysStatisticsEntity -> {
                totalCount.getAndAdd(sysStatisticsEntity.getVisit());
            });
            LogInfoVo logInfoVo = new LogInfoVo();
            logInfoVo.setTodayIp(ipVisit);
            logInfoVo.setTodayVisitCount(visit);
            logInfoVo.setTotalVisitCount(totalCount.get());

            List<VisitInfoVo> visitInfoVoList = new ArrayList<>();
            sysStatisticsEntities.stream().limit(5).forEach(sysStatisticsEntity -> {
                VisitInfoVo visitInfoVo = new VisitInfoVo();
                Date createTime = sysStatisticsEntity.getCreateTime();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                String date = sdf.format(createTime);
                Integer currentVisit = sysStatisticsEntity.getVisit();
                visitInfoVo.setType(date);
                visitInfoVo.setVisit(currentVisit);
                visitInfoVoList.add(visitInfoVo);
            });
            Collections.reverse(visitInfoVoList);
            map.put("logInfo",logInfoVo);
            map.put("visitInfo",visitInfoVoList);
        }
        return map;
    }

}