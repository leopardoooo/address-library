package com.yaochen.address.support;

import java.io.IOException;

import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该扩展类只为了抛出Mybatis parse sqlmap xml异常 
 *
 * @author Killer
 */
public class SelfishMyBatisFactoryBean extends SqlSessionFactoryBean{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 重写Mybatis Configuration，强制指定不使用缓存
	 */
	@Override
	protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
		SqlSessionFactory ssf = super.buildSqlSessionFactory();
		ssf.getConfiguration().setCacheEnabled(false);
		ssf.getConfiguration().setLocalCacheScope(LocalCacheScope.STATEMENT);
		return ssf;
	}
	
	/**
	 * 如果解析XML出错，则直接退出程序
	 */
	public void afterPropertiesSet() throws Exception {
		try {
			super.afterPropertiesSet();
		} catch (Throwable e) {
			logger.error("Mybatis Parse Sqlmap xml error", e);
			System.exit(-1);
			throw e;
		}
	}
}

