package com.yaochen.address.common;

import java.util.Map;

import com.easyooo.framework.common.util.MapUtil;


/**
 * 消息异常转换错误
 *
 * @author Killer
 */
@SuppressWarnings("serial")
public class MessageException extends Throwable{
	
	private Map<String, Object> msgContext;
	
	private StatusCodeConstant statusCode;
	
	public MessageException(StatusCodeConstant statusCode){
		super("code: " + statusCode.getCode() + ", desc: " + statusCode.getDesc());
		this.statusCode = statusCode;
	}
	
	public MessageException(StatusCodeConstant statusCode, Object...contextValues){
		super("code: " + statusCode.getCode() + ", desc: " + statusCode.getDesc());
		this.statusCode = statusCode;
		msgContext = MapUtil.gmap(contextValues);
	}

	public StatusCodeConstant getStatusCode() {
		return statusCode;
	}

	public Map<String, Object> getMsgContext() {
		return msgContext;
	}
	
}
