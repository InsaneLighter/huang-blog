package com.huang.filter;

import com.huang.security.utils.RedisUtil;
import com.huang.utils.Constant;
import com.huang.utils.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Time 2022-05-06 14:49
 * Created by Huang
 * className: VisitFilter
 * Description:
 */
@Component
public class VisitFilter extends OncePerRequestFilter {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String remoteAddress = IPUtils.getClientIP(request);
        filterChain.doFilter(request, response);
        redisUtil.incr(Constant.VISIT_COUNT_PREFIX);
        redisUtil.sSet(Constant.VISIT_IP,remoteAddress);
    }
}
