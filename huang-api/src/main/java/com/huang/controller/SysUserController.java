package com.huang.controller;
import com.huang.entity.SysUserEntity;
import com.huang.service.SysUserService;
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
@RequestMapping("/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysUserService.queryPage(params);
        return R.ok().put("page", page);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
		SysUserEntity sysUser = sysUserService.getById(id);
        return R.ok().put("sysUser", sysUser);
    }

    @PostMapping("/save")
    public R save(@RequestBody SysUserEntity sysUser){
		sysUserService.save(sysUser);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody SysUserEntity sysUser){
		sysUserService.updateById(sysUser);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String[] ids){
		sysUserService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
