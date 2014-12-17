package com.yaochen.address.common;

public class StringHelper {
	
	//地址去空格(包括里面的)  a 3
	//全角数字，标点  转半角
	//地址不能为空 留空为 T 的例外
	
	//有特殊字符的，报错
	
	public static boolean isEmpty(String raw){
		if(null == raw || raw.trim().length() ==0 || "null".equals(raw.trim())){
			return true;
		}
		return false;
	}
	
	
}
