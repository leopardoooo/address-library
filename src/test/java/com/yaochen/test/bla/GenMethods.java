package com.yaochen.test.bla;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.yaochen.address.web.controllers.TreeController;

public class GenMethods {
	private Logger logger = Logger.getLogger(getClass());
	
	Class<?> clazz = TreeController.class;
	
	@Test
	public void testGenMethods() throws Exception {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			String name = method.getName();
			if(name.indexOf("addTree") ==0){
				continue;
			}
			Class<?>[] parameterTypes = method.getParameterTypes();
			String newName = "public void " + name + "(%s) throws Throwable{}";
			String params = "";
			for (Class<?> param : parameterTypes) {
				String simpleName = param.getSimpleName();
				params += simpleName + " " + simpleName.toLowerCase() + ", " ; 
			}
			if(params.length() >0 ){
				params = params.substring(0, params.length() -1);
			}
			
			System.err.println(String.format(newName, params));
		}
		
	}
	
}
