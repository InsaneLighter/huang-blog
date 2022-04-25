package com.huang.exception;

public class GlobalException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GlobalException(String message){
		super(message);
	}
	
	public GlobalException(Throwable cause)
	{
		super(cause);
	}
	
	public GlobalException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
