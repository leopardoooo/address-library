package com.yaochen.test.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.dto.SystemFunction;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.service.TreeService;
import com.yaochen.address.support.ThreadUserHolder;
import com.yaochen.test.commons.SpringRunTest;


public class TreeServiceTest extends SpringRunTest{
	
	@Autowired
	private TreeService treeService;
	
	@Before
	public void before() {
		UserInSession user = new UserInSession();
		Random random = new Random();
		 user.setDuty("Duty_madeUp_部门经理");
		user.setHasWork("false");
		 user.setAreaOID("AreaOID_madeUp_" );
		 user.setAreaName("AreaName_madeUp");
		 user.setHumanName("HumanName_madeUp");
		 user.setCompanyOID("CompanyOID_madeUp");
		 user.setCompanyName("CompanyName_madeUp");
		 user.setDepartmentOID("DepartmentOID_madeUp");
		 user.setDepartmentName("DepartmentName_madeUp");
		 user.setPYCode("PYCode_madeUp");
		 user.setUserOID("UserOID_madeUp");
		 user.setUserName("UserName_madeUp");
		 user.setPassword("Password_madeUp");
		 user.setDelFlag("DelFlag_madeUp");
		 user.setImg("Img_madeUp");
		 user.setMobile("Mobile_madeUp");
		 user.setPublicMobile("PublicMobile_madeUp");
		 user.setOfficeTelphone("OfficeTelphone_madeUp");
		 user.setSignature("Signature_madeUp");
		 user.setImgUrl("ImgUrl_madeUp");
		 
		 SystemFunction fun = new SystemFunction();
		 fun.setFunctionName("地址库");
		 fun.setFunctionOID(123);
		 Integer roleId = Math.abs(random.nextInt()) % 3 + 1;
		 fun.setRoleOID(roleId);
		 
		 List<SystemFunction> list = new ArrayList<SystemFunction>();
		 list.add(fun);
		user.setSystemFunction(list );
		ThreadUserHolder.setUserInSession(user );
	}
	
	/**
	 * 增加地址.
	 * @throws Throwable
	 */
	@Test
	@Ignore
	public void testAddTree() throws Throwable {
		AdTree tree = new AdTree();
		tree.setAddrCode("1234");
		tree.setAddrFullName("广西南宁市新民路 25号");
		tree.setAddrLevel(2);
		tree.setAddrName("25号");
		tree.setAddrParent(2);
		tree.setAddrPrivateName("25号");
		tree.setAddrType(BusiConstants.AddrType.CITY.name());
		tree.setAddrUse(BusiConstants.AddrUsage.CITY.name());
		tree.setIsBlank("F");
		tree.setStatus(BusiConstants.Status.ACTIVE.name());
		//TODO 
		treeService.addTree(tree);
	}
	
	@Test
	public void testFindChildrensByPid() throws Throwable {
		try {
			Pagination pager = treeService.findChildrensAndPagingByPid(0, 0, 1000);
			logger.info(JSON.toJSONString(pager, true));
			Pagination pager2 = treeService.findChildrensAndPagingByPid(0, 0, 100);
			logger.info(JSON.toJSONString(pager2, true));
			
			List<AdLevel> levels = treeService.findAuthLevelByCurrentUser();
			logger.info(JSON.toJSONString(levels, true));
			String keyword = "南宁";
			Pagination doSearchAddress = treeService.doSearchAddress(3, keyword, 0, 100);
			logger.info(JSON.toJSONString(doSearchAddress, true));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 批量增加.
	 * @throws Throwable
	 */
	@Test
	@Ignore
	public void testAddTrees() throws Throwable{
		Integer startPosi = 1;
		Integer endPosi = 10 ;
		AdTree param = new AdTree();
		//TODO
		treeService.addTrees(param, startPosi, endPosi);
	}
	
	
	/**
	 * 查询树.
	 * @throws Throwable
	 */
	@Test
	public void testQueryTree() throws Throwable{
		//TODO
	}
	
}
