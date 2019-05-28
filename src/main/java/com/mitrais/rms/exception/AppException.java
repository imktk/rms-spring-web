package com.mitrais.rms.exception;

public class AppException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public AppException(String code)
	{
		super(code);
	}
}
