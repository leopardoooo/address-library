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
	USER_INSUFFICIENT_AUTHORIZED(301, "权限不够,无法操作当前级别的地址"),
	LOGIN_FAILED(303, "未能成功登录"),
	WS_REQ_FAILURE(304, "webservice请求异常"),
	WS_CFG_ERROR(305, "webservice请求地址或需要调用的方法名配置错误"),
	//具体业务
	ADDR_NAME_INVALID(306, "地址名称不符合要求"),
	USER_NOT_AUTHORIZED(307, "用户未被授权本系统"),
	ADDR_NOT_EXISTS(308, "地址不存在"),
	ADDR_COLL_ALREADY_EXISTS(309, "已经收藏了该地址"),
	ADDR_COLL_NOT_EXISTS(310, "尚未收藏该地址"),
	USER_NOT_LOGGED(311, "没有已登录的操作员信息"),
	ADDR_HAS_CHILDREN(312, "当前地址下仍有子节点地址"),
	ADDR_ALREADY_EXISTS_THIS_LEVEL(313, "当前同级已有同名地址"),
	ADDR_NAME_IS_BLANK(313, "地址不能为空"),
	ADDR_NAME_CONTAIN_INVALID_CHARS(314, "地址不能有特殊字符"),
	ADDR_BASE_LEVEL_NOT_FOUND(315, "尚未设置当前操作员的地址级别"),
	BATCH_ADD_WRONG_RANGE_LENGTH_TOO_LONG(316, "名字的范围不正确,应为两个英文字符或两个数字"),
	BATCH_ADD_WRONG_RANGE_ORDER_WRONG(317, "名字范围不正确,起始位不应小于结束位"),
	BATCH_ADD_WRONG_RANGE_TYPE_MISTACH(318, "名字范围不正确,应都为数字,或者都为英文字母"),
	BATCH_ADD_RANGE_EMPTY(319, "名字范围必须都不能为空"),
	TOO_MANY_BLANK_ADDR(320, "同一父级的地址下只能有一个留空地址"),
	PARAM_MISSED_WHILE_CHECK_ADDR_NAME(321, "检查地址名的时候发现需要的属性不完整,至少需要地址名、是否留空、父级ID和当前地址级别"),
	NON_BLANK_ADDR_WITH_NO_NAME(322, "非留空地址的名字为空"),
	MERGE_ERROR_(323, "合并地址错误"),
	CHANGE_LEVEL_ERROR_(324, "地址变更父级错误"),
	MERGE_ERROR_DATA_FAULT(325, "合并地址的时候发现地址有断层"),
	MERGE_ERROR_SOME_ADDR_NOT_EXISTS(326, "要合并的两个地址必须都存在"),
	MERGE_ERROR_LEVEL_DISMATCH(327, "要合并的两个地址必须同级"),
	CHANGE_LEVEL_PARENT_LEVEL_WRONG(328, "更改上级时,上级地址的级别只能比当前地址级别高上一级"),
	NONE_BLANK_ADDRESS_WRONG_NAME(329, "非留空地址,名字不能使用“留空”"),
	
	
	//名字校验返回的错误 1005
	ADDR_NAME_CHECK_1005(1005,"五级地址不能出现#号"),
	ADDR_NAME_CHECK_1006(1005,"六级地址不能出现数字"),
	UNKNOWN_ERROR(999,"未知错误"),
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
	
	public static StatusCodeConstant parseCode(int code){
		StatusCodeConstant[] values = StatusCodeConstant.values();
		for(StatusCodeConstant scs: values){
			if(code == scs.getCode().intValue()){
				return scs;
			}
		}
		return null;
	}
}
