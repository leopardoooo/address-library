package com.yaochen.test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * Spring 启动测试
 * 
 * @author Killer
 */
public class SpringRunTest extends AbstractSpringUnitTest{

	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	public void testGetApplicationContext(){
		assertThat(applicationContext, notNullValue());
	}
	
	public <T> T getBean(Class<T> klass){
		return applicationContext.getBean(klass);
	}
}
