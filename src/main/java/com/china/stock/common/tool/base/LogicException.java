package com.china.stock.common.tool.base;

public class LogicException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Integer errorCode = -999;
    private String errorMsg;
    
	public String getErrorMsg() {
		return errorMsg;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public LogicException() {
		super();
	}

	public LogicException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super();
	}

	public LogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogicException(String message) {
		super(message);
	}

	public LogicException(Throwable cause) {
		super(cause);
	}

	public LogicException(Integer errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

}
