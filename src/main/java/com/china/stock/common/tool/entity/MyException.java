package com.china.stock.common.tool.entity;
public class MyException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int errorCode;
	public int getErrorCode() {
		return errorCode;
	}
	
	public MyException() 
	{
		super();
	}
	public MyException(String msg) 
	{
		super(msg);
	}
	
	public MyException(int errorCode,String msg) 
	{
		super(msg);
		this.errorCode = errorCode;
	}
	
	public MyException(String msg, Throwable cause) 
	{
		super(msg, cause);
	}
	public MyException(Throwable cause) 
	{
		super(cause);
	}
}
