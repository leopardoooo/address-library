package com.yaochen.address.support;

import com.yaochen.address.dto.UserInSession;

public class ThreadUserParamHolder {
	private static ThreadLocal<UserInSession> base=new ThreadLocal<UserInSession>();
	private static ThreadLocal<String> baseQueryScope = new ThreadLocal<String>();
	
	public void finalize() {
		base.remove();
	}

	public static UserInSession getOptr() {
		return base.get();
	}

	public static void setUserInSession(UserInSession user) {
		ThreadUserParamHolder.base.set(user);
	}

	public static String getBaseQueryScope() {
		return ThreadUserParamHolder.baseQueryScope.get();
	}

	public static void setBaseQueryScope(String baseQueryScope) {
		ThreadUserParamHolder.baseQueryScope.set(baseQueryScope);;
	}
}
