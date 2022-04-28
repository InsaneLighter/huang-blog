package com.huang.controller;

import com.huang.entity.SysUserEntity;
import com.huang.entity.dto.JwtUserDto;
import com.huang.entity.param.PwdParam;
import com.huang.security.utils.SecurityUtil;
import com.huang.service.SysUserService;
import com.huang.utils.EncryptUtils;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
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
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/getCurrentUser")
    public R getCurrentUser() {
        JwtUserDto currentUser = (JwtUserDto) SecurityUtil.getCurrentUser();
        SysUserEntity user = currentUser.getUser();
        user = sysUserService.getById(user.getId());
        return R.ok().put("user", user);
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        SysUserEntity user = sysUserService.getById(id);
        return R.ok().put("user", user);
    }

    @PostMapping("/save")
    public R save(@RequestBody SysUserEntity sysUser) {
        sysUserService.save(sysUser);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody SysUserEntity sysUser) {
        sysUserService.updateById(sysUser);
        return R.ok();
    }

    @PutMapping("/updatePwd")
    public R updatePwd(@RequestBody PwdParam pwdParam) {
        sysUserService.updatePwd(pwdParam);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody String... ids) {
        sysUserService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @GetMapping("/queryAllOnlineUser")
    public R queryOnlineUser(@RequestParam Map<String, Object> params){
        PageUtils page = sysUserService.getAll(params);
        return R.ok().put("data",page);
    }

    @DeleteMapping("/kickOut")
    public R deleteOnlineUser(@RequestBody Set<String> keys) {
        for (String key : keys) {
            // 解密Key
            sysUserService.kickOut(key);
        }
        return R.ok();
    }

}
