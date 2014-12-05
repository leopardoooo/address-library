/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved
 */
package com.yaochen.address.common;

/**
 * 业务响应状态码
 *
 * @author Killer
 */
public enum StatusCodeConstant {
	
	RESPONSE_SUCCESS(200, "业务正常被执行，并成功响应完成"),
	SYSTEM_UNKNOW_EXCEPTION(500, "系统未知异常，不可预料的异常，发生该类异常标志着一个BUG"),
	SESSION_NOT_EXIST_OR_INVALID(501, "Session不存在或已失效"),
	FORM_PARAMS_ERROR(502, "表单参数错误，请检查参数个数、大小写、数据类型等"),
	FORM_VALID_FAILURE(503, "表单验证失败"),
	CONTROLLER_RETURN_ROOT(0, "控制器的返回值必须是Root类型!");
	
	private Integer code;
	
	private String desc;
	
	private StatusCodeConstant(int code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
