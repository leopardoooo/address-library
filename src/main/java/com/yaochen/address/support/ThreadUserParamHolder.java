package com.yaochen.address.support;

import java.util.List;
import java.util.Map;

import com.yaochen.address.data.domain.address.AdOaCountyRef;
import com.yaochen.address.dto.UserInSession;

public class ThreadUserParamHolder {
	private static ThreadLocal<UserInSession> base=new ThreadLocal<UserInSession>();
	private static ThreadLocal<String> globeCountyId = new ThreadLocal<String>();
	
	private static ThreadLocal<Map<String, List<AdOaCountyRef>>> countyChildrenMap = new ThreadLocal<Map<String,List<AdOaCountyRef>>>();
	private static ThreadLocal<Map<String, AdOaCountyRef>> countyMap = new ThreadLocal<Map<String,AdOaCountyRef>>();
	
	public void finalize() {
		base.remove();
	}

	public static UserInSession getOptr() {
		return base.get();
	}

	public static void setUserInSession(UserInSession user) {
		ThreadUserParamHolder.base.set(user);
	}

	public static String getGlobeCountyId() {
		return globeCountyId.get();
	}

	public static void setGlobeCountyId(String globeCountyId) {
		ThreadUserParamHolder.globeCountyId.set(globeCountyId);;
	}
	
	public static Map<String, List<AdOaCountyRef>> getCountyChildrenMap() {
		return ThreadUserParamHolder.countyChildrenMap.get();
	}

	public static void setCountyChildrenMap(
			Map<String, List<AdOaCountyRef>> countyChildrenMap) {
		ThreadUserParamHolder.countyChildrenMap.set(countyChildrenMap);;
	}

	public static Map<String, AdOaCountyRef> getCountyMap() {
		return ThreadUserParamHolder.countyMap.get();
	}

	public static void setCountyMap(
			Map<String, AdOaCountyRef> countyMap) {
		ThreadUserParamHolder.countyMap.set(countyMap);;
	}

	public static void clearAll() {
		ThreadUserParamHolder.base.remove();
		ThreadUserParamHolder.globeCountyId.remove();
		ThreadUserParamHolder.countyChildrenMap.remove();
		ThreadUserParamHolder.countyMap.remove();
	}
	
}
