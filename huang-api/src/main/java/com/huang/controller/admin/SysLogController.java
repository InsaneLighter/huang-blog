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
@RequestMapping("/admin/log")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    @PostMapping
    public R list(@RequestBody Map<String, Object> params){
        PageUtils page = sysLogService.queryPage(params);
        return R.ok().put("data", page);
    }

    @PostMapping
    public R save(@RequestBody SysLogEntity sysLog){
		sysLogService.save(sysLog);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody SysLogEntity sysLog){
		sysLogService.updateById(sysLog);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody String ...ids){
		sysLogService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
