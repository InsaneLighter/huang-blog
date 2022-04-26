package com.huang.exception;

import com.huang.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 * 
 * @Author scott
 * @Date 2019
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;
	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(GlobalException.class)
	public R<?> handleGlobalException(GlobalException e){
		log.error(e.getMessage(), e);
		return R.error(e.getMessage());
	}

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(BlogException.class)
	//@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public R<?> handleHuang401Exception(BlogException e){
		log.error(e.getMessage(), e);
		return R.error(e.getMessage());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public R<?> handlerNoFoundException(Exception e) {
		log.error(e.getMessage(), e);
		return R.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R<?> handleDuplicateKeyException(DuplicateKeyException e){
		log.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}

	@ExceptionHandler(Exception.class)
	public R<?> handleException(Exception e){
		log.error(e.getMessage(), e);
		return R.error("操作失败："+e.getMessage());
	}
	
	/**
	 * @Author 政辉
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public R<?> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
		StringBuffer sb = new StringBuffer();
		sb.append("不支持");
		sb.append(e.getMethod());
		sb.append("请求方法，");
		sb.append("支持以下");
		String [] methods = e.getSupportedMethods();
		if(methods!=null){
			for(String str:methods){
				sb.append(str);
				sb.append("、");
			}
		}
		log.error(sb.toString(), e);
		return R.error(405,sb.toString());
	}
	
	 /** 
	  * spring默认上传大小100MB 超出大小捕获异常MaxUploadSizeExceededException 
	  */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
    	log.error(e.getMessage(), e);
        return R.error(String.format("文件大小超出%s限制, 请压缩或降低文件质量! ", maxFileSize));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public R<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    	log.error(e.getMessage(), e);
        return R.error("字段太长,超出数据库字段的长度");
    }

    @ExceptionHandler(PoolException.class)
    public R<?> handlePoolException(PoolException e) {
    	log.error(e.getMessage(), e);
        return R.error("Redis 连接异常!");
    }

}
