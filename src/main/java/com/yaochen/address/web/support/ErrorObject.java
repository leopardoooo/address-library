package com.yaochen.address.web.support;

import com.yaochen.address.common.StatusCodeConstant;

/**
 * 错误对象封装
 *
 * @author Killer
 */
public class ErrorObject {
	private StatusCodeConstant statusCode;
	private Object message;
	private boolean success = true;
	public ErrorObject(){
	}
	
	public ErrorObject(StatusCodeConstant statusCode, Object message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}
	public StatusCodeConstant getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(StatusCodeConstant statusCode) {
		this.statusCode = statusCode;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
