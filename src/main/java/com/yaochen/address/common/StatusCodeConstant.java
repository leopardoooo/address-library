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
	//登录相关
	LOGIN_FAILED(303, "未能成功登录"),
	WS_REQ_FAILURE(304, "webservice请求异常"),
	WS_CFG_ERROR(305, "webservice请求地址或需要调用的方法名配置错误"),
	//具体业务
	ADDR_NAME_INVALID(306, "地址名称不符合要求"),
	USER_NOT_AUTHORIZED(307, "用户未被授权本系统"),
	ADDR_NOT_EXISTS(308, "要操作的地址不存在"),
	ADDR_COLL_ALREADY_EXISTS(309, "已经收藏了该地址"),
	ADDR_COLL_NOT_EXISTS(310, "尚未收藏该地址"),
	
	//系统
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
