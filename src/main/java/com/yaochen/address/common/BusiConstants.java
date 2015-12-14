package com.yaochen.address.common;

import java.io.Serializable;


public class BusiConstants implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**首页查询收藏的数目**/
	public static Integer COLLECTIONS_QUERY_COUNT = 6;
	
	public static interface StringConstants{
		/**webservice登录返回的XML根节点**/
		public static String WSDL_TARGET_NODE_NAME = "string";
		
		public static String REDIRECT_ACTION = "redirect:";
		
		public static String LOGIN_FAILURE_VIEW = "login";
		
		public static String SSO_LOGIN_ACTION = "sso";
		
		public static String LOGIN_SUCCESS_VIEW = "index";
		public static String USER_IN_SESSION = "loginUserInSession";
		/**存放在session里的关于登录的错误信息**/
		public static String LOGIN_ERROR_IN_SESSION = "LOGIN_ERROR_IN_SESSION";
		public static String SLASH = "/";
		public static String SUCCESS = "success";
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
		/** 存放系统左侧树的最大级别的变量名 **/
		public static String MAX_TREE_LEVEL_PROP_NAME = "maxTreeLevel";
		
		public static String ADDR_SYS_FUN_CODE = "ADDR_SYS_FUN_CODE";
		
		/** 系统角色名的前缀,后面跟数字，比如： address_1 **/
		public static String ADDR_SYS_ROLE_PREFIX = "address_";
		
//		public static String ADDR_NOT_AUDITED_SUFFIX="(未审核)";
		public static String ADDR_NOT_AUDITED_SUFFIX="";//现在又要求通过地址库系统加入的都是ACTIVE
		
		public static String SYS_ADMIN_USER_NAME = "admin";
		
	}
	
	public static enum Booleans{
		T,F
	}
	
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
	
	public static enum TreeChangeFields{
		addrUseText("用处"),addrLevel("级别"),isBlankText("是否留空"),addrName("地址名"),addrParent("父节点"),addrTypeText("类型");
		private String desc;

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		private TreeChangeFields(String desc) {
			this.desc = desc;
		}
	}
	
	public static enum Status{
		NOT_AUDITED("未审核",0),
		REJECTED("审核不通过",2),
		REQ_APPROVED("审核通过",1),
		ACTIVE("正常",1),
		INVALID("作废",0);
		private int order;
		private String desc ;
		public String getDesc() {
			return desc;
		}
		
		private Status( String desc,int order) {
			this.order = order;
			this.desc = desc;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public static Status forName(String status) {
			for (Status item : Status.values()) {
				if(item.name().equals(status)){
					return item;
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 异动类型.
	 */
	public static enum AddrChangeType{
		/**新增**/
		ADD,
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
	
	public static enum GzLevel{
		JF("机房/分前端"),
		JJX("交接箱/光分路器"),
		GZ("光站/光机"); 
		
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		private GzLevel(String name) {
			this.name = name;
		}

	}
	
	
}
