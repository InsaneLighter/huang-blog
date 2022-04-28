package com.huang.security.handler;

import cn.hutool.core.util.StrUtil;
import com.huang.entity.SysUserEntity;
import com.huang.entity.bean.SecurityProperties;
import com.huang.security.service.UserCacheClean;
import com.huang.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@AllArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private TokenProvider tokenProvider;
    private SecurityProperties properties;
    private SysUserService sysUserService;
    private UserCacheClean userCacheClean;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            SysUserEntity sysUserEntity = null;
            boolean cleanUserCache = false;
            try {
                sysUserEntity = sysUserService.getOne(properties.getOnlineKey() + token);
            } catch (Exception e) {
                log.error("当前登录状态过期 {}",e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(sysUserEntity)) {
                    userCacheClean.cleanUserCache(String.valueOf(tokenProvider.getClaims(token).get(TokenProvider.AUTHORITIES_KEY)));
                }
            }
            if (sysUserEntity != null && StringUtils.hasText(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                tokenProvider.checkRenewal(token);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(properties.getHeader());
        if (StringUtils.hasText(token) && token.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return token.replace(properties.getTokenStartWith(), "");
        } else {
            log.debug("非法Token：{}", token);
        }
        return null;
    }
}
