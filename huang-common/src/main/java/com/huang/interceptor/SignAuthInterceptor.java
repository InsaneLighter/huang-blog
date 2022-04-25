package com.huang.interceptor;


import com.alibaba.fastjson.JSON;
import com.huang.interceptor.util.BodyReaderHttpServletRequestWrapper;
import com.huang.interceptor.util.HttpUtils;
import com.huang.interceptor.util.SignUtil;
import com.huang.utils.Constant;
import com.huang.utils.DateUtils;
import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.SortedMap;

/**
 * 签名拦截器
 * @author qinfeng
 */
@Slf4j
public class SignAuthInterceptor implements HandlerInterceptor {
    /**
     * 5分钟有效期
     */
    private final static long MAX_EXPIRE = 5 * 60;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("request URI = " + request.getRequestURI());
        HttpServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
        //获取全部参数(包括URL和body上的)
        SortedMap<String, String> allParams = HttpUtils.getAllParams(requestWrapper);
        //对参数进行签名验证
        String headerSign = request.getHeader(Constant.X_SIGN);
        String timesTamp = request.getHeader(Constant.X_TIMESTAMP);

        //1.校验时间有消息
        try {
            DateUtils.parseDate(timesTamp, "yyyyMMddHHmmss");
        } catch (Exception e) {
            throw new IllegalArgumentException("签名验证失败:X-TIMESTAMP格式必须为:yyyyMMddHHmmss");
        }
        Long clientTimestamp = Long.parseLong(timesTamp);
        //判断时间戳 timestamp=201808091113
        if ((DateUtils.getCurrentTimestamp() - clientTimestamp) > MAX_EXPIRE) {
            throw new IllegalArgumentException("签名验证失败:X-TIMESTAMP已过期");
        }

        //2.校验签名
        boolean isSigned = SignUtil.verifySign(allParams,headerSign);

        if (isSigned) {
            log.debug("Sign 签名通过！Header Sign : {}",headerSign);
            return true;
        } else {
            log.error("request URI = " + request.getRequestURI());
            log.error("Sign 签名校验失败！Header Sign : {}",headerSign);
            //校验失败返回前端
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = response.getWriter();
            R<?> result = R.error("Sign签名校验失败！");
            out.print(JSON.toJSON(result));
            return false;
        }
    }

}
