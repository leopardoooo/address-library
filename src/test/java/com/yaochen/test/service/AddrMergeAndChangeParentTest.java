package com.yaochen.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yaochen.address.data.mapper.address.AdRoleMapper;
import com.yaochen.address.data.mapper.address.AdTreeMapper;
import com.yaochen.address.service.TreeService;

public class AddrMergeAndChangeParentTest extends BaseTest {
	Integer source_id = 47073;
	Integer target_id = 47044;

	@Autowired
	private TreeService treeService;
	@Autowired
	private AdRoleMapper adRoleMapper;
	@Autowired
	private AdTreeMapper adTreeMapper;

	@Test
	public void testMerge() throws Throwable {
		source_id = 47073;
		target_id = 47044;
		treeService.saveSingleMerge(target_id, source_id);
		
	}
}
