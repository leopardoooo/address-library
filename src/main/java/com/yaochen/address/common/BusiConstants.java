package com.yaochen.address.common;

public class BusiConstants {
	
	/**首页查询收藏的数目**/
	public static Integer COLLECTIONS_QUERY_COUNT = 6;
	
	public static interface StringConstants{
		/**webservice登录返回的XML根节点**/
		public static String WSDL_TARGET_NODE_NAME = "string";
		
		public static String REDIRECT_ACTION = "redirect:";
		
		public static String LOGIN_FAILURE_VIEW = "login";
		
		public static String LOGIN_SUCCESS_VIEW = "index";
		public static String USER_IN_SESSION = "loginUserInSession";
		/**存放在session里的关于登录的错误信息**/
		public static String LOGIN_ERROR_IN_SESSION = "LOGIN_ERROR_IN_SESSION";
		public static String SLASH = "/";
		/**顶级树的id**/
		public static String TOP_PID = "0";
		public static String GOLBEL_QUERY_PRECND = "GOLBEL_QUERY_PRECND";
		/** 查询的countyId限定 **/
		public static String GOLBEL_COUNTY_ID = "GOLBEL_COUNTY_ID";
		/** 顶级分公司ID **/
		public static String COUNTY_ALL = "4501";
		
		/**在设置了查询范围(user/setAddrScope)过滤之后的级别放在session**/
		public static String FILTERED_LEVELS_IN_SESSION = "FILTERED_LEVELS_IN_SESSION";
		/** 所有的等级 **/
		public static String ALL_LEVELS_IN_SESSION = "ALL_LEVELS_IN_SESSION";
		
		public static String GOLBEL_QUERY_SCOPE_TEXT = "GOLBEL_QUERY_SCOPE_TEXT";
		public static String BLANK_ADDR_NAME = "留空";
		/**新增的地址的初始状态,如果以后需要审核,修改这里**/
		public static String ADDR_INIT_STATUS = Status.ACTIVE.name();
		
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
		CITY("城镇"),
		//农村
		RURAL("农村"); 
		private String desc;

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		private AddrType(String desc) {
			this.desc = desc;
		}
	}
	
	public static enum AddrUsage{
//		城市小区
		CITY("城市小区"),
		//工业园厂房
		INDUSTRIAL_PARK("工业园厂房") ,
		//城市酒店
		CITY_HOTEL("城市酒店"),
		//小区商铺
		SHOPS("小区商铺"),
		// 其它
		OTHERS("其它");
		private String desc;

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		private AddrUsage(String desc) {
			this.desc = desc;
		}
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
		/**变更父级**/
		CHANGE_PARENT,
		/** 合并删除 */
		MERGE_DEL, 
		/** 审核失败 */
		AUDIT_FAILED,
		/** 审核成功 */
		AUDIT_SUCCESS
	}
	
	
}
