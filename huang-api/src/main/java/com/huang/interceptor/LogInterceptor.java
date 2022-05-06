package com.huang.interceptor;

import com.huang.entity.SysLogEntity;
import com.huang.mapper.SysLogMapper;
import com.huang.utils.CommonUtils;
import com.huang.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Time 2022-05-06 16:02
 * Created by Huang
 * className: LogInterceptor
 * Description:
 */
@Slf4j
@Aspect
@Component
public class LogInterceptor {
    @Autowired
    private SysLogMapper sysLogMapper;
    // 定义切点Pointcut
    @Pointcut("execution(public * com.huang.controller.*.*Controller.*(..))")
    public void executeService() {
    }

    @Around("executeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response = sra.getResponse();

        SysLogEntity sysLogEntity = new SysLogEntity();
        final String ip = IPUtils.getClientIP(request);

        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        log.info("Starting url: [{}], uri: [{}], method: [{}], params: [{}] ip: [{}]",
                url,
                uri,
                method,
                queryString,
                ip);
        final long startTime = System.currentTimeMillis();
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        long costTime = System.currentTimeMillis() - startTime;
        log.info("Ending url: [{}], uri: [{}], method: [{}], result: [{}], ip: [{}], status: [{}], usage: [{}] ms",
                url,
                uri,
                method,
                result,
                ip,
                response.getStatus(),
                costTime
                );
        String browser = CommonUtils.getBrowser(request);
        String address = CommonUtils.getCityInfo(ip);
        sysLogEntity.setAddress(address);
        sysLogEntity.setBrowser(browser);
        sysLogEntity.setExceptionDetail("");
        sysLogEntity.setMethod(method);
        sysLogEntity.setParams(queryString);
        sysLogEntity.setRequestIp(ip);
        sysLogEntity.setTime((int) costTime);
        //TODO 性能压测 and 异常信息处理
        sysLogMapper.insert(sysLogEntity);
        return result;
    }
}
