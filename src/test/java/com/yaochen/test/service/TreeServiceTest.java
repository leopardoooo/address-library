package com.yaochen.test.service;

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
		treeService.addTree(tree,ThreadUserHolder.getOptr());
	}
	
	/**
	 * 增加一个用来测试其他业务的地址
	 * @throws Throwable
	 */
	@Test
	@Ignore
	public void testAddTreeForOtherBusiTest() throws Throwable {
		AdTree tree = new AdTree();
		tree.setAddrCode("1234");
		tree.setAddrFullName("广西南宁市新民路");
		tree.setAddrLevel(2);
		tree.setAddrName("新民路");
		tree.setAddrParent(1);
		tree.setAddrPrivateName("新民路");
		tree.setAddrType(BusiConstants.AddrType.CITY.name());
		tree.setAddrUse(BusiConstants.AddrUsage.CITY.name());
		tree.setIsBlank("F");
		tree.setStatus(BusiConstants.Status.ACTIVE.name());
		//TODO 
		treeService.addTree(tree,ThreadUserHolder.getOptr());
	}
	
	@Test
	@Ignore
	public void testAddTreeAddAllCity() throws Throwable {
		
		String [] cities = new String []{"南宁","柳州","桂林","梧州","北海","防城港",
		"钦州","贵港","玉林","百色","贺州","河池","来宾","崇左"		
		};
		
		for (int index = 0; index < cities.length; index++) {
			String city = cities[index];
			
			AdTree tree = new AdTree();
			String addrCode = "0000";
			addrCode = addrCode + (index + 1);
			if(addrCode.length() > 5){
				addrCode = addrCode.substring(1,addrCode.length());
			}
			tree.setAddrCode(addrCode);
			tree.setAddrFullName("广西" + city + "市");
			tree.setAddrLevel(1);
			tree.setAddrName(city+"市");
			tree.setAddrParent(0);
			tree.setAddrPrivateName(city);
			tree.setAddrType(BusiConstants.AddrType.CITY.name());
			tree.setAddrUse(BusiConstants.AddrUsage.CITY.name());
			tree.setIsBlank("F");
			tree.setStatus(BusiConstants.Status.ACTIVE.name());
			treeService.addTree(tree,ThreadUserHolder.getOptr());
		}
		
	}
	
	@Test
	public void testModTree() throws Throwable {
		//TODO 这里发现一个问题,修改地址名的时候，全名也要修改,前台输入还是后台计算？？？？
		AdTree tree = treeService.queryByKey(15);
		logger.info(JSON.toJSONString(tree, true));
		tree.setAddrName("treeNewBee");
		treeService.modTree(tree, true,ThreadUserHolder.getOptr());
		tree = treeService.queryByKey(15);
		logger.info(JSON.toJSONString(tree, true));
	}
	
	@Test
	public void testDelTree() throws Throwable {
		treeService.delTree(15,ThreadUserHolder.getOptr());
		AdTree tree = treeService.queryByKey(15);
		logger.info(JSON.toJSONString(tree, true));
	}
	
	/**
	 * 测试批量添加.
	 * @throws Throwable
	 */
	@Test
	public void testAddTreeBatch() throws Throwable {
		//TODO addrCode 怎么确定/????
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
		treeService.addTrees(tree,1,10,ThreadUserHolder.getOptr());
	}
	
	
	@Test
	public void testFindChildrensByPid() throws Throwable {
		try {
			Pagination pager = treeService.findChildrensAndPagingByPid(0, 0, 1000);
			logger.info(JSON.toJSONString(pager, true));
			Pagination pager2 = treeService.findChildrensAndPagingByPid(0, 0, 100);
			logger.info(JSON.toJSONString(pager2, true));
			
			List<AdLevel> levels = treeService.findAuthLevelByCurrentUser(ThreadUserHolder.getOptr());
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
		treeService.addTrees(param, startPosi, endPosi,null);
	}
	
	
	/**
	 * 查询树.
	 * @throws Throwable
	 */
	@Test
	public void testSaveCollectoin() throws Throwable{
		//TODO
		treeService.saveCollectTree(1,ThreadUserHolder.getOptr());
		treeService.saveCollectTree(2,ThreadUserHolder.getOptr());
		treeService.saveCollectTree(3,ThreadUserHolder.getOptr());
		treeService.saveCollectTree(4,ThreadUserHolder.getOptr());
		List<AdLevel> list = treeService.findCollectTreeList(100,ThreadUserHolder.getOptr());
		System.err.println(JSON.toJSONString(list, true));
	}
	
	@Test
	public void testCancelCollectoin() throws Throwable{
		//TODO
		treeService.saveCancelCollectTree(1,ThreadUserHolder.getOptr());
	}
	
	
}
