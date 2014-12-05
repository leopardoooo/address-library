package com.yaochen.address.web.support;


/**
 * 返回值工具类
 *
 * @author Killer
 */
public final class ReturnValueUtil {
	
	/**
	 * 如果是Null，返回一个可识别的Null类型的对象
	 * @param obj
	 * @return
	 */
	public static <T> Root<T> getJsonRoot(T obj){
		return new Root<T>(obj);
	}
	
	/**
	 * 当控制器无返回值时，使用该方法返回一个Void类型的Object
	 * 
	 * @return
	 */
	public static Root<Void> getVoidRoot(){
		return new Root<Void>(null);
	}
}
