package com.yaochen.address.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easyooo.framework.common.util.CglibUtil;
import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.dto.Addr;
import com.yaochen.address.service.TestService;
import com.yaochen.address.service.TreeService;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/test2")
public class TestController {
	private static final String [] ADDR_TREE_GRID_PROPERTIES = new String  [] {"district", "region", "road", "roadNum", "village", "building", "street", "department", "houseNum"};
	
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private TreeService treeService;
	@Autowired
	private TestService testService;
	

	@RequestMapping(value = "/view/{page}")
	public String view(@PathVariable String page) throws Throwable {
		logger.info(page);
		return page;
	}
	
	/**
	 * 查找城市列表（第一级别的树节点）
	 * @throws Throwable 
	 */
	@RequestMapping(value="/findTopTrees")
	@ResponseBody
	public Root<List<AdTree>> findTopTreesByCurrentUser() throws Throwable{
		Pagination pager = new Pagination();
		List<AdTree> records = pager.getRecords();
		return ReturnValueUtil.getJsonRoot(records);
	}

	/**
	 * 查找城市列表（第一级别的树节点）
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryAllLevels")
	@ResponseBody
	public Root<List<AdLevel>> queryAllLevels() throws Throwable{
		List<AdLevel>  allLevels= new ArrayList<AdLevel>();
		
		List<AdLevel> findAllLevels = treeService.findAllLevels();
		AdLevel all = new AdLevel();
		all.setLevelNum(-1);
		all.setLevelName("所有");
		allLevels.add(all);
		allLevels.addAll(findAllLevels);
		AdLevel id = new AdLevel();
		id.setLevelNum(0);
		id.setLevelName("根据ID查询");
		allLevels.add(id);
		return ReturnValueUtil.getJsonRoot(allLevels);
	}
	
	
	/**
	 * 查找城市列表（第一级别的树节点）
	 * @throws Throwable 
	 */
	@RequestMapping(value="/search")
	@ResponseBody
	public Root<Pagination> search(@RequestParam("sl")Integer targetLevel, 
			@RequestParam("q") String keyword, 
			@RequestParam("start") Integer start, 
			@RequestParam("limit") Integer limit) throws Throwable{
		Pagination pager = treeService.doSearchAddress(targetLevel, keyword, start, limit);
		List<AdTree> records = pager.getRecords();
		List<Addr> res = warpTreeDto(records);
		pager.setRecords(res);
		return ReturnValueUtil.getJsonRoot(pager);
	}
	
	private List<Addr> warpTreeDto(List<AdTree> records) {
		List<Addr> result = new ArrayList<Addr>();
		if(CollectionHelper.isEmpty(records)){
			return result;
		}
		for (AdTree addr1 : records) {
			Addr addr = wrapTreeDto(addr1);
			result.add(addr);
		}
		return result;
	}

	private Addr wrapTreeDto(AdTree addr1) {
		Addr addr = new Addr(addr1);
		Integer level = addr.getAddrLevel();
		if(level >1 ){
			for(int index =0;index <level-1;index++){
				CglibUtil.setPropertyValue(addr, ADDR_TREE_GRID_PROPERTIES[index], "");
			}
		}
		return addr;
	}

	
	/**
	 * 查找城市列表（第一级别的树节点）
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryAllDataForHomePage2")
	@ResponseBody
	public Addr queryAllDataForHomePage2(@RequestParam("node")String addrIdNode) throws Throwable{
		Integer addrId = 0;
		try {
			addrId = Integer.parseInt(addrIdNode);
		} catch (Exception e) {
		}
		
		AdTree currentNode = testService.queryByKey(addrId);
		
		List<AdTree> list = testService.queryAllDataForHomePage(currentNode);
		
		Map<String, List<AdTree>> mapByPid = CollectionHelper.converToMap(list, "addrParent");
		
		List<AdTree> top = mapByPid.get(BusiConstants.StringConstants.TOP_PID);
		if(CollectionHelper.isEmpty(top)){
			throw new MessageException(StatusCodeConstant.UNKNOWN_ERROR);
		}
		
		Addr firstNode = wrapTreeDto(top.get(0));
		
		mapByPid.get(firstNode.getAddrId());
		firstNode =buildTree(firstNode,mapByPid);

		return firstNode;
	}
	
	public static void main(String[] args) {
	}

	private Addr buildTree(Addr node, Map<String, List<AdTree>> mapByPid) {
		Integer addrId = node.getAddrId();
		List<Addr> children = warpTreeDto(mapByPid.get(addrId.toString()));
		for (Addr addr : children) {
			buildTree(addr, mapByPid);
		}
		node.setChildren(children);
		return node;
	}
	
	
	
}
