package com.yaochen.test.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants.AddrChangeType;
import com.yaochen.address.common.OfficeHelper;
import com.yaochen.address.data.domain.address.AdTreeChangeDetail;
import com.yaochen.address.data.mapper.address.AdTreeChangeDetailMapper;
import com.yaochen.address.dto.db.LogQueryForm;
import com.yaochen.test.SpringRunTest;

public class LogQueryTest extends SpringRunTest {
	@Autowired
	private AdTreeChangeDetailMapper dao;

	@Test
	public void testName() throws Exception {
		Integer start = 0 ;
		Integer limit = 20;
		
		LogQueryForm form = getQueryParam();
		
		Pagination pager = new Pagination(form, start, limit);
		List<AdTreeChangeDetail> rst = dao.queryLogDetail(pager);
		
		String[] firstLine = {"ID","全名","级别","变更类型","变更字段","变更前","变更后","变更时间","操作员","父节点ID"};
		String[] properties = {"addrId","str1","levelName","changeCause","columnDesc","oldValue","newValue","changeTime","changeOptrName","addrPid"};
		
		OfficeHelper.generateExcel(rst, properties, firstLine);
		
	}

	private LogQueryForm getQueryParam() {
		LogQueryForm form = new LogQueryForm();
		
		String addrName = "dd";
//		form.setAddrName(addrName);
//		form.setOldValue(addrName);
//		form.setPid(47277);
		
		Integer changeOptrId = 1;
		changeOptrId = 6;
//		form.setChangeOptrId(changeOptrId);
		AddrChangeType changeType = AddrChangeType.ADD;
		changeType = AddrChangeType.EDIT;
//		form.setChangeType(changeType);
		
		int level = 1;
		level = 3;
//		form.setLevel(level);
		
		return form;
	}

}
