package com.huang.controller.admin;
import com.huang.entity.SysLogEntity;
import com.huang.service.SysLogService;
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
@RequestMapping("/log")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysLogService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		SysLogEntity sysLog = sysLogService.getById(id);
        return R.ok().put("sysLog", sysLog);
    }

    @PostMapping("/save")
    public R save(@RequestBody SysLogEntity sysLog){
		sysLogService.save(sysLog);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody SysLogEntity sysLog){
		sysLogService.updateById(sysLog);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String ...ids){
		sysLogService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
