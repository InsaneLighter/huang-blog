package com.huang.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.SysUserEntity;
import com.huang.entity.param.PwdParam;
import com.huang.entity.param.UserParam;
import com.huang.exception.AuthenticationException;
import com.huang.exception.BlogException;
import com.huang.mapper.SysUserMapper;
import com.huang.security.config.bean.LoginCodeEnum;
import com.huang.security.config.bean.LoginProperties;
import com.huang.security.config.bean.SecurityProperties;
import com.huang.security.security.TokenProvider;
import com.huang.security.service.UserCacheClean;
import com.huang.security.service.dto.JwtUserDto;
import com.huang.service.SysUserService;
import com.huang.utils.*;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
    @Value("${rsa.privateKey}")
    private String privateKey;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SecurityProperties properties;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private UserCacheClean userCacheClean;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysUserEntity> page = this.page(
                new Query().getPage(params),
                new QueryWrapper<SysUserEntity>()
        );
        return new PageUtils(page);
    }

    /**
     * 保存在线用户信息
     * @param sysUserEntity /
     * @param token /
     * @param request /
     */
    public void save(SysUserEntity sysUserEntity, String token, HttpServletRequest request){
        String ip = CommonUtils.getIp(request);
        String browser = CommonUtils.getBrowser(request);
        String address = CommonUtils.getCityInfo(ip);
        try {
            sysUserEntity.setIp(ip);
            sysUserEntity.setBrowser(browser);
            sysUserEntity.setAddress(address);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        redisUtil.set(properties.getOnlineKey() + token, JSONObject.toJSON(sysUserEntity), properties.getTokenValidityInSeconds()/1000);
    }

    /**
     * 查询全部数据
     * @param filter /
     * @param pageable /
     * @return /
     */
    public PageUtils getAll(String filter, Pageable pageable){
        List<SysUserEntity> sysUserEntities = getAll(filter);
        Page<SysUserEntity> page = new Page<SysUserEntity>();
        page.setRecords(sysUserEntities);
        return new PageUtils(page);
    }

    /**
     * 查询全部数据，不分页
     * @param filter /
     * @return /
     */
    public List<SysUserEntity> getAll(String filter){
        List<String> keys = redisUtil.scan(properties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<SysUserEntity> sysUserEntities = new ArrayList<>();
        for (String key : keys) {
            SysUserEntity sysUserEntity = (SysUserEntity) redisUtil.get(key);
            if(StringUtils.hasText(filter)){
                if(sysUserEntity.toString().contains(filter)){
                    sysUserEntities.add(sysUserEntity);
                }
            } else {
                sysUserEntities.add(sysUserEntity);
            }
        }
        sysUserEntities.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return sysUserEntities;
    }

    /**
     * 踢出用户
     * @param key /
     */
    public void kickOut(String key){
        key = properties.getOnlineKey() + key;
        redisUtil.del(key);
    }

    /**
     * 退出登录
     * @param token /
     */
    public void logout(String token) {
        String key = properties.getOnlineKey() + token;
        redisUtil.del(key);
    }

    /**
     * 查询用户
     * @param key /
     * @return /
     */
    public SysUserEntity getOne(String key) {
        JSONObject jsonObject = (JSONObject) redisUtil.get(key);
        return jsonObject.toJavaObject(SysUserEntity.class);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken){
        List<SysUserEntity> sysUserEntities = getAll(userName);
        if(sysUserEntities ==null || sysUserEntities.isEmpty()){
            return;
        }
        for(SysUserEntity sysUserEntity : sysUserEntities){
            if(sysUserEntity.getUsername().equals(userName)){
                try {
                    String token = EncryptUtils.desDecrypt(sysUserEntity.getToken());
                    if(StringUtils.hasText(igoreToken)&&!igoreToken.equals(token)){
                        this.kickOut(token);
                    }else if(!StringUtils.hasText(igoreToken)){
                        this.kickOut(token);
                    }
                } catch (Exception e) {
                    log.error("checkUser is error",e);
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) {
        List<SysUserEntity> sysUserEntities = getAll(username);
        for (SysUserEntity sysUserEntity : sysUserEntities) {
            if (sysUserEntity.getUsername().equals(username)) {
                String token = EncryptUtils.desDecrypt(sysUserEntity.getToken());
                kickOut(token);
            }
        }
    }

    @Override
    public String login(UserParam authUser, HttpServletRequest request) {
        // 密码解密
        String password = RSAUtils.decryptByPrivateKey(authUser.getPassword(),privateKey);
        // 查询验证码
        String code = (String) redisUtil.get(authUser.getUuid());
        // 清除验证码
        redisUtil.del(authUser.getUuid());
        if (!StringUtils.hasText(code)) {
            throw new AuthenticationException("验证码不存在或已过期");
        }
        if (!StringUtils.hasText(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new AuthenticationException("验证码错误");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        SysUserEntity sysUserEntity = jwtUserDto.getUser();
        // 保存在线信息
        sysUserService.save(sysUserEntity, token, request);
        // 返回 token 与 用户信息
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            sysUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        return properties.getTokenStartWith() + token;
    }

    @Override
    public Map<String, Object> getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtil.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        return new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
    }

    @Override
    public SysUserEntity findByName(String username) {
        QueryWrapper<SysUserEntity> sysUserWrapper = new QueryWrapper<>();
        sysUserWrapper.eq("username",username);
        return this.list(sysUserWrapper).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(SysUserEntity sysUserEntity) {
        this.updateById(sysUserEntity);
        flushCache(sysUserEntity.getUsername());
    }

    @Override
    public Map<String, String> updateAvatar(MultipartFile file) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(SysUserEntity sysUserEntity) {
        this.updateById(sysUserEntity);
        flushCache(sysUserEntity.getUsername());
    }

    @Override
    public void updatePwd(PwdParam pwdParam) {
        String oldPassword = pwdParam.getOldPassword();
        String newPassword = pwdParam.getNewPassword();
        if(!StringUtils.hasText(oldPassword)
                || !StringUtils.hasText(newPassword)
                ){
            throw new BlogException("密码为空！");
        }
        if(newPassword.equals(oldPassword)){
            throw new BlogException("修改前后密码一致！");
        }
        String id = pwdParam.getId();
        SysUserEntity entity = this.getById(id);
        if (entity.getPassword().equals(oldPassword)) {
            entity.setPassword(newPassword);
            this.updateById(entity);
        }else {
            throw new BlogException("密码填写错误！");
        }
    }

    private void flushCache(String username) {
        userCacheClean.cleanUserCache(username);
    }
}