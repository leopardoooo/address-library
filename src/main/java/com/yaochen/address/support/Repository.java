/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved
 */
package com.yaochen.address.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被标注了该注解的Mapper才能被MyBatis代理
 * 
 * @author Killer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Repository {

}

