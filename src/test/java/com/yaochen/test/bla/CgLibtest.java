package com.yaochen.test.bla;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.easyooo.framework.common.util.CglibUtil;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.data.domain.address.AdTree;

public class CgLibtest {
	
	@Test
	public void test2() throws Exception {
		AdTree nt = init();
		String jsonString = JSON.toJSONString(nt);
		System.err.println(jsonString);
		Map map = JSON.parseObject(jsonString, Map.class);
		System.err.println(map);
		Object propertyValue = CglibUtil.getPropertyValue(nt, "createTime");
		CglibUtil.describe(nt);
		System.err.println(propertyValue);
	}
	
	@Test
	public void testDesc() throws Exception {
		AdTree old = null;
//		CglibUtil.describe(old);
		
		AdTree nt = init();
		long start = System.currentTimeMillis();
		for (int index = 0; index < 100; index++) {

			Map<String, Object> desc = extraAndAnaly(nt);
			
		}
		long end = System.currentTimeMillis();
		System.err.println("耗时：" + (end -start));
		
		@SuppressWarnings("unchecked")
		Map<String, Object> desc2 = CglibUtil.describe(new AdTree());
		System.err.println(desc2);
	}

	private Map<String, Object> extraAndAnaly(AdTree nt) {
		@SuppressWarnings("unchecked")
		Map<String, Object> desc = CglibUtil.describe(nt);
		return desc;
	}
	
	private AdTree init() {
		AdTree tree = new AdTree();
		String addrname = "addrname";
		tree.setAddrName(addrname);
		Integer addrlevel = 1;
		tree.setAddrLevel(addrlevel);
		Integer addrid = 1;
		tree.setAddrId(addrid);
		String addrtype = BusiConstants.AddrType.CITY.name();
		tree.setAddrType(addrtype);
		Integer addrparent = 0 ;
		tree.setAddrParent(addrparent);
		String addrcode = "addrcode";
		tree.setAddrCode(addrcode);
		String isblank = BusiConstants.Booleans.F.name();
		tree.setIsBlank(isblank);
		Date createtime = new Date();
		tree.setCreateTime(createtime);
		String status = BusiConstants.Status.ACTIVE.name();
		tree.setStatus(status);
		String str2 = "str2";
		tree.setStr2(str2);
		String str5 = "str5";
		tree.setStr5(str5);
		String str4 = "str4";
		tree.setStr4(str4);
		Integer collected = 0;
		tree.setCollected(collected);
		String countyid = "1";
		tree.setCountyId(countyid);
		String str1 = "str1";
		String str3 = "str3";
		
		tree.setStr1(str1);
		tree.setStr3(str3);
		
		String addruse = BusiConstants.AddrUsage.INDUSTRIAL_PARK.name();
		tree.setAddrUse(addruse);
		String addrtypetext = "addrtypetext";
		tree.setAddrTypeText(addrtypetext);
		String addrprivatename = "addrprivatename";
		tree.setAddrPrivateName(addrprivatename);
		String createoptrid = "0";
		tree.setCreateOptrId(createoptrid);
		String addrfullname = "addrfullname";
		tree.setAddrFullName(addrfullname);
		Integer createdonecode =  1;
		tree.setCreateDoneCode(createdonecode);
		return tree;
	}
	
}
