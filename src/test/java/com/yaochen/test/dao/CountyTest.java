package com.yaochen.test.dao;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.data.domain.address.AdOaCountyRef;
import com.yaochen.address.data.mapper.address.AdOaCountyRefMapper;
import com.yaochen.test.SpringRunTest;

public class CountyTest extends SpringRunTest {
	@Autowired
	private AdOaCountyRefMapper dao;

	@Test
	public void testName() throws Exception {
		List<AdOaCountyRef> list = dao.selectAll();
		Map<String, AdOaCountyRef> singleMap = CollectionHelper.converToMapSingle(list, "countyId");
		Map<String, List<AdOaCountyRef>> map = CollectionHelper.converToMap(list, "pid");
		
		AdOaCountyRef county = singleMap.get(BusiConstants.StringConstants.COUNTY_ALL);
		
		AdOaCountyRef copy = buildTree(county,map);
		System.err.println(JSON.toJSONString(copy));
	}

	private AdOaCountyRef buildTree(AdOaCountyRef county, Map<String, List<AdOaCountyRef>> map) {
		String cid = county.getCountyId();
		List<AdOaCountyRef> children = map.get(cid);
		if(CollectionHelper.isEmpty(children)){
			county.setChildren(children);
			return county;
		}
		
		for (AdOaCountyRef child : children) {
			buildTree(child, map);
		}
		county.setChildren(children);
		return county;
	}

}
