package com.yaochen.address.common;

public class BusiConstants {
	public static String WSDL_TARGET_NODE_NAME = "string";
	public static String LOGIN_FAILURE_VIEW = "login";
	public static String LOGIN_SUCCESS_VIEW = "index";
	public static String USER_IN_SESSION = "loginUserInSession";
	
	/**
	 * 操作地址库的功能代码.这里做测试设置为  123....
	 */
	public static String ADDR_SYS_FUN_CODE = "123";
	
	
	public static enum AddrType{
		//城镇
		CITY,
		//农村
		RURAL 
	}
	
	public static enum AddrUsage{
//		城市小区
		CITY,
		//工业园厂房
		INDUSTRIAL_PARK ,
		//城市酒店
		CITY_HOTEL,
		//小区商铺
		SHOPS
	}
	
	public static enum Status{
		ACTIVE,
		INVALID
	}
	
}
