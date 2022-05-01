package com.huang.controller;

import com.huang.entity.SysUserEntity;
import com.huang.entity.param.UserParam;
import com.huang.security.handler.TokenProvider;
import com.huang.service.SysUserService;
import com.huang.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Time 2022-04-26 12:47
 * Created by Huang
 * className: LoginController
 * Description:
 */
@RestController
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping(value = "/login")
    public R login(@Validated @RequestBody UserParam authUser, HttpServletRequest request) {
        SysUserEntity sysUserEntity = sysUserService.login(authUser,request);
        return R.ok().put("user",sysUserEntity);
    }

    @GetMapping(value = "/code")
    public R getCode() {
        Map<String,Object> imgResult = sysUserService.getCode();
        return R.ok(imgResult);
    }

    @DeleteMapping(value = "/logout")
    public R logout(HttpServletRequest request) {
        sysUserService.logout(tokenProvider.getToken(request));
        return R.ok();
    }

}
