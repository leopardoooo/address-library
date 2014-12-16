/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved
 */
package com.yaochen.test.commons;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring with Junit abstract classes
 * 在测试运行实现用例时，需要在本地启动MySQL Databases
 * 具体请参考Spring XML
 * 
 * @author Killer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class AbstractSpringUnitTest {
	
	public static Logger logger = LoggerFactory
			.getLogger(AbstractSpringUnitTest.class);
	
}
