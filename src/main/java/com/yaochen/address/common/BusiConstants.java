package com.yaochen.address.common;

public class BusiConstants {
	
	public static interface StringConstants{
		public static String WSDL_TARGET_NODE_NAME = "string";
		
		public static String REDIRECT_ACTION = "redirect:";
		
		public static String LOGIN_FAILURE_VIEW = "login";
		
		public static String LOGIN_SUCCESS_VIEW = "index";
		public static String USER_IN_SESSION = "loginUserInSession";
		public static String SLASH = "/";
		/**顶级树的id**/
		public static String TOP_PID = "0";
		public static String GOLBEL_QUERY_PRECND = "GOLBEL_QUERY_PRECND";
		
		/**在设置了查询范围(user/setAddrScope)过滤之后的级别放在session**/
		public static String FILTERED_LEVELS_IN_SESSION = "FILTERED_LEVELS_IN_SESSION";
		
		public static String GOLBEL_QUERY_SCOPE_TEXT = "GOLBEL_QUERY_SCOPE_TEXT";
		public static String BLANK_ADDR_NAME = "留空";
	}
	
	public static enum Booleans{
		T,F
	}
	
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
		SHOPS,
		// 其它
		OTHERS
	}
	
	public static enum Status{
		NOT_AUDITED,
		ACTIVE,
		INVALID
	}
	
	/**
	 * 异动类型.
	 */
	public static enum AddrChangeType{
		/**编辑 */
		EDIT, 
		/** 合并删除 */
		MERGE_DEL, 
		/** 审核失败 */
		AUDIT_FAILED,
		/** 审核成功 */
		AUDIT_SUCCESS
	}
	
	
}
