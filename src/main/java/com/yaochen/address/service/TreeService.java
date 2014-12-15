package com.yaochen.address.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.mapper.address.AdTreeMapper;

@Service
public class TreeService {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private AdTreeMapper adTreeMapper;
	
	public Integer addTree(AdTree tree) throws Throwable{
		
		return adTreeMapper.insertSelective(tree);
	}

	public void test() {
		AdTree selectByPrimaryKey = adTreeMapper.selectByPrimaryKey(1);
		logger.info("测试一下是否走的通    \n " + JSON.toJSONString(selectByPrimaryKey, true));
	}
	
}
