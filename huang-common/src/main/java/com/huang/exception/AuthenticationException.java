package com.huang.exception;

/**
 * @Time 2022-05-11 20:17
 * Created by Huang
 * className: AuthenticationException
 * Description:
 */
public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
