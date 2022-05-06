package com.huang.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.SysStatisticsEntity;

import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-15 10:19:09
 */
public interface SysStatisticsService extends IService<SysStatisticsEntity> {
    SysStatisticsEntity getStatistics();

    Map<String, Object> visitStatistics();
}

