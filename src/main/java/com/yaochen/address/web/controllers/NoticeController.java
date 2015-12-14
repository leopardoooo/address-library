package com.yaochen.address.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.dto.db.NoticeQueryForm;
import com.yaochen.address.service.NoticeService;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;


@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired private NoticeService noticeService;
	
	/**
	 * @throws Throwable 
	 */
	@RequestMapping(value="/query")
	@ResponseBody
	public Root<Pagination> query(NoticeQueryForm notice,Integer start,Integer limit) throws Throwable{
		return ReturnValueUtil.getJsonRoot(noticeService.query(notice,start,limit));
	}
	
	/**
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryCities")
	@ResponseBody
	public Root<List<String>> queryCities(Integer noticeId) throws Throwable{
		return ReturnValueUtil.getJsonRoot(noticeService.queryCities(noticeId));
	}
	
	/**
	 * @throws Throwable 
	 */
	@RequestMapping(value="/countUnRead")
	@ResponseBody
	public Root<Integer> countUnRead() throws Throwable{
		Integer countUnChecked = noticeService.countUnChecked();
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("cnt", countUnChecked);
		return ReturnValueUtil.getJsonRoot(countUnChecked);
	}
	
	/**
	 * @throws Throwable 
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Root<Void> save(NoticeQueryForm notice,String [] countyIds) throws Throwable{
		noticeService.save(notice,countyIds);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * @param noticeId 
	 * @throws Throwable 
	 */
	@RequestMapping(value="/remove")
	@ResponseBody
	public Root<Void> remove(Integer noticeId) throws Throwable{
		noticeService.remove(noticeId);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * @throws Throwable 
	 */
	@RequestMapping(value="/checkRead")
	@ResponseBody
	public Root<Void> checkRead(Integer noticeId) throws Throwable{
		noticeService.saveCheckRead(noticeId);
		return ReturnValueUtil.getVoidRoot();
	}
	
}
