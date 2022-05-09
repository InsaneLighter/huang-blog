package com.huang.interceptor;

import com.alibaba.fastjson.JSON;
import com.huang.entity.SysLogEntity;
import com.huang.mapper.SysLogMapper;
import com.huang.utils.CommonUtils;
import com.huang.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
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
    private final ThreadLocal<SysLogEntity> sysLogEntityThreadLocal = new ThreadLocal<>();
    @Autowired
    private SysLogMapper sysLogMapper;

    @Pointcut("execution(public * com.huang.controller.*.*Controller.*(..))")
    public void executeService() {
    }

    @Pointcut("execution(public * com.huang.controller.admin.SysLogController.*(..))")
    public void excludeSysLogControllerService() {
    }

    @Pointcut("execution(public * com.huang.controller.admin.SysUserController.getCurrentUserInfo())")
    public void excludeGetCurrentUserInfo() {
    }

    @Pointcut("executeService() && !excludeSysLogControllerService() && !excludeGetCurrentUserInfo()")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response = sra.getResponse();
        SysLogEntity sysLogEntity = new SysLogEntity();
        final String ip = IPUtils.getClientIP(request);
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String requestType = request.getMethod();
        Signature signature = pjp.getSignature();
        String param = getParam(request, requestType, pjp);
        log.info("Starting url: [{}], uri: [{}],requestType: [{}], method: [{}], params: [{}] ip: [{}]",
                url,
                uri,
                requestType,
                signature.toString(),
                param,
                ip);
        final long startTime = System.currentTimeMillis();
        String browser = CommonUtils.getBrowser(request);
        String address = CommonUtils.getCityInfo(ip);
        sysLogEntity.setAddress(address);
        sysLogEntity.setBrowser(browser);
        sysLogEntity.setUri(uri);
        sysLogEntity.setRequestType(requestType);
        sysLogEntity.setMethod(signature.toString());
        sysLogEntity.setParams(param);
        sysLogEntity.setRequestIp(ip);
        sysLogEntity.setStartTime(startTime);
        sysLogEntityThreadLocal.set(sysLogEntity);
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        long costTime = System.currentTimeMillis() - startTime;
        log.info("Ending url: [{}], uri: [{}], method: [{}], result: [{}], ip: [{}], status: [{}], usage: [{}] ms",
                url,
                uri,
                param,
                result,
                ip,
                response.getStatus(),
                costTime
        );
        sysLogEntity.setTime((int) costTime);
        //TODO 性能压测 and 异常信息处理
        sysLogMapper.insert(sysLogEntity);
        sysLogEntityThreadLocal.remove();
        return result;
    }

    @AfterThrowing(pointcut = "executeService()",
            throwing = "exception")
    public void handleControllerException(Exception exception) {
        log.info("handleControllerException: {}", exception.toString());
        SysLogEntity sysLogEntity = sysLogEntityThreadLocal.get();
        long startTime = sysLogEntity.getStartTime();
        long costTime = System.currentTimeMillis() - startTime;
        sysLogEntity.setTime((int) costTime);
        sysLogEntity.setExceptionDetail(exception.toString());
        sysLogMapper.insert(sysLogEntity);
        sysLogEntityThreadLocal.remove();
    }

    private String getParam(HttpServletRequest request, String requestType, JoinPoint pjp) {
        String contentType = request.getContentType();
        if(contentType == null || !contentType.contains("multipart/form-data")){
            Object[] arguments = pjp.getArgs();
            if (arguments != null && arguments.length > 0) {
                Object argument = arguments[0];
                return JSON.toJSONString(argument);
            }
        }
        return "";
    }
}
