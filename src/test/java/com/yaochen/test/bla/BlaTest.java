package com.yaochen.test.bla;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.support.MD5Util;

public class BlaTest {
	
	@Test
	public void testMath() throws Exception {
		for (int index = 0; index < 20; index++) {
			double random = Math.random();
			System.err.println(random / 1000);
		}
	}
	
	@Test
	public void testName() throws Exception {
		Field[] declaredFields = AdTree.class.getDeclaredFields();
		List<Bean> list = new ArrayList<BlaTest.Bean>();
		List<Model> ms = new ArrayList<Model>();
		List<String> names = new ArrayList<String>();
		for (Field field : declaredFields) {
			String name = field.getName();
			list.add(new Bean(name));
			ms.add(new Model(name));
			names.add(name);
		}
		
		System.err.println(JSON.toJSONString(list));
		System.err.println(JSON.toJSONString(ms));
		System.err.println(JSON.toJSONString(names));
	}
	
	@Test
	public void testPassword() throws Exception {
		System.err.println(MD5Util.EncodePassword("123"));
	}
	
	
	static class Model{
		String name;
		String type="auto";
		public Model(String name) {
			super();
			this.name = name;
			this.type="auto";
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	}
	
	static class Bean{
		private String text;
		private String dataIndex;
		
		public Bean(String text) {
			super();
			this.text = text;
			this.dataIndex = text;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getDataIndex() {
			return dataIndex;
		}
		public void setDataIndex(String dataIndex) {
			this.dataIndex = dataIndex;
		}
	}
	
}
