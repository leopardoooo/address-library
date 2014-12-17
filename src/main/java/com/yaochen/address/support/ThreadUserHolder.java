package com.yaochen.address.support;

import com.yaochen.address.dto.UserInSession;

public class ThreadUserHolder {
	private static ThreadLocal<UserInSession> base=new ThreadLocal<UserInSession>();
	
	public void finalize() {
		base.remove();
	}

	public static UserInSession getOptr() {
		return base.get();
	}

	public static void setUserInSession(UserInSession user) {
		ThreadUserHolder.base.set(user);
	}
	
}
