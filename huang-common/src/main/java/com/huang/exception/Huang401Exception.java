package com.huang.exception;

public class Huang401Exception extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public Huang401Exception(String message){
		super(message);
	}

	public Huang401Exception(Throwable cause)
	{
		super(cause);
	}

	public Huang401Exception(String message, Throwable cause)
	{
		super(message,cause);
	}
}
