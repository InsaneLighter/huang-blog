package com.huang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huang.entity.SysUserEntity;
import com.huang.entity.param.PwdParam;
import com.huang.entity.param.UserParam;
import com.huang.utils.PageUtils;
import org.springframework.data.domain.Pageable;
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

    PageUtils getAll(String filter, Pageable pageable);

    List<SysUserEntity> getAll(String filter);

    void kickOut(String key);

    void logout(String token);

    SysUserEntity getOne(String key);

    void checkLoginOnUser(String userName, String igoreToken);

    void kickOutForUsername(String username);

    String login(UserParam authUser, HttpServletRequest request);

    Map<String, Object> getCode();

    SysUserEntity findByName(String userName);

    void updatePass(SysUserEntity sysUserEntity);

    Map<String, String> updateAvatar(MultipartFile file);

    void updateEmail(SysUserEntity sysUserEntity);

    void updatePwd(PwdParam pwdParam);
}

