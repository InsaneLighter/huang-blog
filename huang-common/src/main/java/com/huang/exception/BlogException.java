package com.huang.exception;

public class BlogException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BlogException(String message){
		super(message);
	}

	public BlogException(Throwable cause)
	{
		super(cause);
	}

	public BlogException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
