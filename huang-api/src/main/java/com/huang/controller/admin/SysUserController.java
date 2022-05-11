package com.huang.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.huang.entity.SysUserEntity;
import com.huang.entity.param.PwdParam;
import com.huang.service.SysUserService;
import com.huang.utils.PageUtils;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-15 10:19:09
 */
@RestController
@RequestMapping("/admin/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPage(params);
        return R.ok().put("data", page);
    }

    @GetMapping("/getCurrentUser")
    public R getCurrentUserInfo() {
        Map<String,Object> result = sysUserService.getCurrentUserInfo();
        return R.ok().put("data", result);
    }

    @PostMapping
    public R save(@RequestBody SysUserEntity sysUser) {
        sysUserService.saveUser(sysUser);
        return R.ok();
    }

    @PutMapping
    public R update(@RequestBody SysUserEntity sysUser) {
        sysUserService.update(sysUser);
        return R.ok();
    }

    @PutMapping("/updatePwd")
    public R updatePwd(@RequestBody PwdParam pwdParam) {
        sysUserService.updatePwd(pwdParam);
        return R.ok();
    }

    @DeleteMapping
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
            sysUserService.kickOut(key);
        }
        return R.ok();
    }

    @DeleteMapping("/kickOutByUserName")
    public R deleteOnlineUserByUserName(@RequestBody JSONObject params) {
        String username = params.getString("username");
        sysUserService.kickOutForUsername(username);
        return R.ok();
    }

    @PutMapping("/resetPwd")
    public R resetPwd(@RequestBody JSONObject params) {
        String id = params.getString("id");
        sysUserService.resetPwd(id);
        return R.ok();
    }

    @PostMapping("/upload")
    public R uploadImages(@RequestPart("file") MultipartFile multipartFile){
        String url = sysUserService.upload(multipartFile);
        return R.ok().put("url",url);
    }

}
