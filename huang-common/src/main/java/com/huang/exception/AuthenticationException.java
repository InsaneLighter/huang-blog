package com.huang.exception;

import org.springframework.http.HttpStatus;

/**
 * Authentication exception.
 *
 * @author johnniang
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
