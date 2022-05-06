package com.huang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.SysUserEntity;
import com.huang.entity.param.PwdParam;
import com.huang.entity.param.UserParam;
import com.huang.utils.PageUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Huang
 * @email mail@huanghong.top
 * @date 2022-04-15 10:19:09
 */
public interface SysUserService extends IService<SysUserEntity> {
    PageUtils queryPage(Map<String, Object> params);

    void save(SysUserEntity sysUserEntity, String token, HttpServletRequest request);

    PageUtils getAll(Map<String, Object> params);

    List<SysUserEntity> getAll(String filter);

    void kickOut(String key);

    void logout(String token);

    SysUserEntity getOne(String key);

    void checkLoginOnUser(String userName, String ignoreToken);

    void kickOutForUsername(String username);

    SysUserEntity login(UserParam authUser, HttpServletRequest request);

    Map<String, Object> getCode();

    SysUserEntity findByName(String userName);

    void updatePwd(PwdParam pwdParam);

    void saveUser(SysUserEntity sysUser);

    void update(SysUserEntity sysUser);

    void resetPwd(String id);

    String upload(MultipartFile multipartFile);

    Map<String, Object> getCurrentUserInfo();

}

