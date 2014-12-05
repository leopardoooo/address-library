package com.yaochen.address.web.support;

/**
 * 客户端响应对象封装
 * @author Killer
 */
public class Root<T> {
	private Integer code;
	private Object message;
	private T data;
	
	public Root(){
	}
	
	public Root(T data){
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
