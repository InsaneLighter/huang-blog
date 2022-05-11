package com.huang.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huang.entity.*;
import com.huang.entity.bean.LoginCodeEnum;
import com.huang.entity.bean.LoginProperties;
import com.huang.entity.bean.SecurityProperties;
import com.huang.entity.dto.JwtUserDto;
import com.huang.entity.param.PwdParam;
import com.huang.entity.param.UserParam;
import com.huang.exception.AuthenticationException1;
import com.huang.exception.BlogException;
import com.huang.mapper.*;
import com.huang.security.handler.TokenProvider;
import com.huang.security.utils.RedisUtil;
import com.huang.security.utils.SecurityUtil;
import com.huang.service.SysUserService;
import com.huang.utils.*;
import com.wf.captcha.base.Captcha;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
    @Value("${rsa.privateKey}")
    private String privateKey;
    @Value("${login.default-password}")
    private String DEFAULT_PWD;
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
    private PasswordEncoder encoder;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private JournalMapper journalMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Value("${minio.user.bucketName}")
    private String bucketName;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String keyword = (String) params.getOrDefault("keyword", "");
        QueryWrapper<SysUserEntity> sysUserWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            sysUserWrapper.like("username", keyword)
                    .or().like("email", keyword)
                    .or().like("description", keyword);
        }
        IPage<SysUserEntity> page = this.page(new Query().getPage(params), sysUserWrapper);
        return new PageUtils(page);
    }

    /**
     * 保存在线用户信息
     *
     * @param sysUserEntity /
     * @param token         /
     * @param request       /
     */
    public void save(SysUserEntity sysUserEntity, String token, HttpServletRequest request) {
        String ip = CommonUtils.getIp(request);
        String browser = CommonUtils.getBrowser(request);
        String address = CommonUtils.getCityInfo(ip);
        try {
            sysUserEntity.setIp(ip);
            sysUserEntity.setBrowser(browser);
            sysUserEntity.setAddress(address);
            sysUserEntity.setLoginTime(new Date());
            sysUserEntity.setToken(token);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        redisUtil.set(properties.getOnlineKey() + token, JSONObject.toJSON(sysUserEntity), properties.getTokenValidityInSeconds() / 1000);
    }

    /**
     * 查询全部数据
     *
     * @return /
     */
    public PageUtils getAll(Map<String, Object> params) {
        String keyword = (String) params.getOrDefault("keyword", "");
        List<SysUserEntity> sysUserEntities = getAll(keyword);
        Page<SysUserEntity> page = toPage(params, sysUserEntities);
        return new PageUtils(page);
    }

    private Page<SysUserEntity> toPage(Map<String, Object> params, List<SysUserEntity> sysUserEntities) {
        int page = Integer.parseInt((String) params.getOrDefault("page", "1"));
        int size = Integer.parseInt((String) params.getOrDefault("size", "10"));
        Page<SysUserEntity> result = new Page<>();
        List<SysUserEntity> resultEntities;
        int fromIndex = page * size;
        int toIndex = page * size + size;
        if (fromIndex > sysUserEntities.size()) {
            resultEntities = new ArrayList();
        } else if (toIndex >= sysUserEntities.size()) {
            resultEntities = sysUserEntities.subList(fromIndex, sysUserEntities.size());
        } else {
            resultEntities = sysUserEntities.subList(fromIndex, toIndex);
        }
        result.setRecords(resultEntities);
        result.setCurrent(page);
        result.setSize(size);
        result.setTotal(sysUserEntities.size());
        return result;
    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<SysUserEntity> getAll(String filter) {
        List<String> keys = redisUtil.scan(properties.getOnlineKey() + "*");
        Collections.reverse(keys);
        List<SysUserEntity> sysUserEntities = new ArrayList<>();
        for (String key : keys) {
            JSONObject jsonObject = (JSONObject) redisUtil.get(key);
            if (jsonObject != null) {
                SysUserEntity sysUserEntity = jsonObject.toJavaObject(SysUserEntity.class);
                sysUserEntity.setPassword("******");
                if (StringUtils.hasText(filter)) {
                    if (sysUserEntity.toString().toLowerCase().contains(filter.toLowerCase())) {
                        sysUserEntities.add(sysUserEntity);
                    }
                } else {
                    sysUserEntities.add(sysUserEntity);
                }
            }
        }
        return sysUserEntities;
    }

    /**
     * 踢出用户
     *
     * @param key /
     */
    public void kickOut(String key) {
        key = properties.getOnlineKey() + key;
        redisUtil.del(key);
    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String token) {
        String key = properties.getOnlineKey() + token;
        redisUtil.del(key);
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public SysUserEntity getOne(String key) {
        JSONObject jsonObject = (JSONObject) redisUtil.get(key);
        return jsonObject.toJavaObject(SysUserEntity.class);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String ignoreToken) {
        List<SysUserEntity> sysUserEntities = getAll(userName);
        if (sysUserEntities == null || sysUserEntities.isEmpty()) {
            return;
        }
        for (SysUserEntity sysUserEntity : sysUserEntities) {
            String token = sysUserEntity.getToken();
            if (sysUserEntity.getUsername().equalsIgnoreCase(userName) && !token.equals(ignoreToken)) {
                try {
                    this.kickOut(token);
                } catch (Exception e) {
                    log.error("checkUser is error", e);
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    @Override
    public void kickOutForUsername(String username) {
        List<SysUserEntity> sysUserEntities = getAll(username);
        for (SysUserEntity sysUserEntity : sysUserEntities) {
            if (sysUserEntity.getUsername().equals(username)) {
                String token = sysUserEntity.getToken();
                kickOut(token);
            }
        }
    }

    @Override
    public SysUserEntity login(UserParam authUser, HttpServletRequest request) {
        // 密码解密
        String password = RSAUtils.decryptByPrivateKey(authUser.getPassword(), privateKey);
        // 查询验证码
        String code = (String) redisUtil.get(authUser.getUuid());
        // 清除验证码
        redisUtil.del(authUser.getUuid());
        if (!StringUtils.hasText(code)) {
            throw new AuthenticationException1("验证码不存在或已过期");
        }
        if (!StringUtils.hasText(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new AuthenticationException1("验证码错误");
        }
        //账号禁用判断
        QueryWrapper<SysUserEntity> sysUserWrapper = new QueryWrapper<>();
        sysUserWrapper.eq("username", authUser.getUsername());
        SysUserEntity entity = this.getOne(sysUserWrapper);
        if (entity != null) {
            Integer status = entity.getStatus();
            if (status == null || status == 1) {
                throw new AuthenticationException1("账号已被禁用！");
            }
        }else {
            throw new AuthenticationException1("用户不存在！");
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
        SysUserEntity resultEntity = new SysUserEntity();
        BeanUtils.copyProperties(sysUserEntity,resultEntity);
        resultEntity.setToken(properties.getTokenStartWith() + token);
        resultEntity.setPassword(null);
        return resultEntity;
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
        sysUserWrapper.eq("username", username);
        return this.list(sysUserWrapper).stream().findFirst().orElse(null);
    }


    @Override
    public void updatePwd(PwdParam pwdParam) {
        String oldPassword = RSAUtils.decryptByPrivateKey(pwdParam.getOldPassword(), privateKey);
        String newPassword = RSAUtils.decryptByPrivateKey(pwdParam.getNewPassword(), privateKey);
        if (!StringUtils.hasText(oldPassword)
                || !StringUtils.hasText(newPassword)
        ) {
            throw new BlogException("密码为空！");
        }
        if (newPassword.equals(oldPassword)) {
            throw new BlogException("修改前后密码一致！");
        }
        String id = pwdParam.getId();
        SysUserEntity entity = this.getById(id);

        if (encoder.matches(oldPassword, entity.getPassword())) {
            entity.setPassword(encoder.encode(newPassword));
            this.updateById(entity);
        } else {
            throw new BlogException("密码填写错误！");
        }
    }

    @Override
    public void saveUser(SysUserEntity sysUser) {
        String username = sysUser.getUsername();
        QueryWrapper<SysUserEntity> sysUserWrapper = new QueryWrapper<>();
        sysUserWrapper.eq("username", username);
        SysUserEntity sysUserEntity = this.list(sysUserWrapper).stream().findFirst().orElse(null);
        if (sysUserEntity == null) {
            String password = RSAUtils.decryptByPrivateKey(sysUser.getPassword(), privateKey);
            sysUser.setPassword(encoder.encode(password));
            this.save(sysUser);
        } else {
            throw new BlogException("已存在相同用户名的用户！");
        }
    }

    @Override
    public void update(SysUserEntity sysUser) {
        UserDetails currentUser = SecurityUtil.getCurrentUser();
        String username = currentUser.getUsername();
        QueryWrapper<SysUserEntity> sysUserWrapper = new QueryWrapper<>();
        sysUserWrapper.eq("id", "0");
        SysUserEntity sysUserEntity = this.getOne(sysUserWrapper);
        if(sysUserEntity.getUsername().equals(username)){
            this.updateById(sysUser);
        }else {
            throw new AuthenticationException1("当前用户没有权限更改用户数据！");
        }
    }

    @Override
    public void resetPwd(String id) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setId(id);
        sysUserEntity.setPassword(encoder.encode(DEFAULT_PWD));
        this.updateById(sysUserEntity);
    }

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            return minioUtil.uploadFile(multipartFile, bucketName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getCurrentUserInfo() {
        Map<String, Object> result = new HashMap<>(2);
        JwtUserDto currentUser = (JwtUserDto) SecurityUtil.getCurrentUser();
        SysUserEntity user = currentUser.getUser();
        user = this.getById(user.getId());
        Date createTime = user.getCreateTime();
        long days = (System.currentTimeMillis() - createTime.getTime()) / (1000 * 24 * 3600);
        String username = user.getUsername();
        QueryWrapper<PostEntity> postWrapper = new QueryWrapper<>();
        postWrapper.eq("create_by",username);
        Long postCount = postMapper.selectCount(postWrapper);
        QueryWrapper<JournalEntity> journalWrapper = new QueryWrapper<>();
        journalWrapper.eq("create_by",username);
        Long journalCount = journalMapper.selectCount(journalWrapper);
        QueryWrapper<CategoryEntity> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.eq("create_by",username);
        Long categoryCount = categoryMapper.selectCount(categoryWrapper);
        QueryWrapper<TagEntity> tagWrapper = new QueryWrapper<>();
        tagWrapper.eq("create_by",username);
        Long tagCount = tagMapper.selectCount(tagWrapper);
        SysStatisticsEntity sysStatisticsEntity = new SysStatisticsEntity();

        sysStatisticsEntity.setEstablishDays((int) days);
        sysStatisticsEntity.setPostCount(Math.toIntExact(postCount));
        sysStatisticsEntity.setJournalCount(Math.toIntExact(journalCount));
        sysStatisticsEntity.setCategoryCount(Math.toIntExact(categoryCount));
        sysStatisticsEntity.setTagCount(Math.toIntExact(tagCount));

        result.put("user",user);
        result.put("statistics",sysStatisticsEntity);
        return result;
    }
}