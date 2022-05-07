package com.huang.controller.admin;

import com.huang.entity.SysStatisticsEntity;
import com.huang.service.SysStatisticsService;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 04-15 10:19:09
 */
@RestController
@RequestMapping("/statistics")
public class SysStatisticsController {
    @Autowired
    private SysStatisticsService sysStatisticsService;

    @GetMapping
    public R getStatistics(){
        SysStatisticsEntity sysStatisticsEntity = sysStatisticsService.getStatistics();
        return R.ok().put("data", sysStatisticsEntity);
    }

    @GetMapping("/visit")
    public R visitStatistics(){
        Map<String,Object> result = sysStatisticsService.visitStatistics();
        return R.ok().put("data", result);
    }
}
