package com.huang.controller;
import com.huang.entity.SysStatisticsEntity;
import com.huang.service.SysStatisticsService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 
 *
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-15 10:19:09
 */
@RestController
@RequestMapping("/statistics")
public class SysStatisticsController {
    @Autowired
    private SysStatisticsService sysStatisticsService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysStatisticsService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		SysStatisticsEntity sysStatistics = sysStatisticsService.getById(id);
        return R.ok().put("sysStatistics", sysStatistics);
    }

    @PostMapping("/save")
    public R save(@RequestBody SysStatisticsEntity sysStatistics){
		sysStatisticsService.save(sysStatistics);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody SysStatisticsEntity sysStatistics){
		sysStatisticsService.updateById(sysStatistics);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String[] ids){
		sysStatisticsService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
