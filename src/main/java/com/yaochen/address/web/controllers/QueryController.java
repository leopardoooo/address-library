package com.yaochen.address.web.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easyooo.framework.common.util.CglibUtil;
import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.domain.std.StdDevice;
import com.yaochen.address.dto.Addr;
import com.yaochen.address.service.QueryService;
import com.yaochen.address.service.StdService;
import com.yaochen.address.service.TreeService;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/query")
public class QueryController {
	
	private static final String [] ADDR_TREE_GRID_PROPERTIES = new String  [] {"district", "region", "road", "roadNum", "village", "building", "street", "department", "houseNum"};
	
	@Autowired private QueryService queryService;
	
	@Autowired private StdService stdService;
	
	@Autowired private TreeService treeService;
	
	/**
	 * 关于第一个参数   addrLevel的说明，之前是直接传入  数值的，后来要求输入汉字，然后根据汉字查找。
	 * 但是之前定义的接口都是直接传值的，所以造成了混乱，
	 * 现在，如果直接传入数值，那就按照原来的直接按照值来处理。
	 * 
	 * 查找城市列表（第一级别的树节点）
	 * @throws Throwable 
	 */
	@RequestMapping(value="/query")
	@ResponseBody
	public Root<Pagination> query(String addrLevel,String addrLevelFlag,String addrNameFlag,String addrName, String addrParentFlag,
			String addrParent,String[] addrType,Integer start, Integer limit) throws Throwable{
		List<Integer> list = new ArrayList<Integer>();
		
		if(StringHelper.isNotEmpty(addrLevel)){
			List<AdLevel> levels = treeService.findAllLevels();
			Map<String, AdLevel> levMap = CollectionHelper.converToMapSingle(levels, "levelNum");
			boolean eq = "1".equals(addrLevelFlag);
			if( !eq ){
				getAddrLevels(list, addrLevel,levMap);
			}else{
				Integer levelIntVal = null;
				try {
					levelIntVal = Integer.parseInt(addrLevel);
				} catch (Exception e) {
					throw new MessageException(StatusCodeConstant.ADDR_LEVEL_NOT_WELL_FORMATTED);
				}
				if(levMap.containsKey(levelIntVal.toString())){
					list.add(levelIntVal);
				}
			}
			
			if(CollectionHelper.isEmpty(list)){
				throw new MessageException(StatusCodeConstant.ADDR_LEVEL_NOT_EXISTS);
			}
		}
		
		Pagination pager = queryService.query(list.toArray(new Integer[list.size()]),addrNameFlag,addrName,addrParentFlag,addrParent,addrType,start,limit);
		return ReturnValueUtil.getJsonRoot(pager);
	}

	private void getAddrLevels(List<Integer> list, String lev, Map<String, AdLevel> levMap) throws MessageException {
		if(lev.indexOf("-")>0){
			String[] array = lev.split("-");//只能有两个
			Integer start = Integer.parseInt(array[0]);
			Integer end = Integer.parseInt(array[1]);
			if(start >end){
				Integer tmp = end ;
				end = start;
				start = tmp;
			}
			//TODO 
			for (Integer index = start; index <= end; index++) {
				if(levMap.containsKey(index.toString())){
					list.add(index);
				}else{
					throw new MessageException(StatusCodeConstant.ADDR_LEVEL_NOT_WELL_FORMATTED);
				}
			}
			
		}else if(lev.indexOf(",")>0){
			String[] array = lev.split(",");
			for (String str : array) {
				Integer levNum = Integer.parseInt(str);
				if(levMap.containsKey(levNum.toString())){
					list.add(levNum);
				}else{
					throw new MessageException(StatusCodeConstant.ADDR_LEVEL_NOT_WELL_FORMATTED);
				}
			}
		}else{
			try {
				Integer levNum = Integer.parseInt(lev);
				if(-1== levNum){
					return;
				}
				if(levMap.containsKey(levNum.toString())){
					list.add(levNum);
				}
			} catch (NumberFormatException e) {
				throw new MessageException(StatusCodeConstant.ADDR_LEVEL_NOT_WELL_FORMATTED);
			}
			return;
		}
	}
	
	/**
	 * 查找城市列表（第一级别的树节点）
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryUnAudited")
	@ResponseBody
	public Root<Pagination> queryUnAudited(String startTime,String endTime,String status,Integer start, Integer limit) throws Throwable{
		Pagination pager = queryService.queryUnAudited(startTime,endTime,status,start,limit);
		return ReturnValueUtil.getJsonRoot(pager);
	}
	
	/**
	 * 查找城市列表（第一级别的树节点）
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryChildren")
	@ResponseBody
	public Root<List<Addr>> queryChildren(@RequestParam("node")String addrIdNode,HttpSession session) throws Throwable{
		Object attribute = session.getAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID);
		if(null == attribute){//首次尚未选择分公司
			return ReturnValueUtil.getJsonRoot(null);
		}
		List<AdTree> findDirectChildren = queryService.findDirectChildren(addrIdNode);
		return ReturnValueUtil.getJsonRoot(warpAddrTreeDto(findDirectChildren));
	}
	

	/**
	 * 查询光站树
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryStdDevTree")
	@ResponseBody
	public Root<List<StdDevice>> queryStdDevTree(@RequestParam("node")String devId,HttpSession session) throws Throwable{
		Object attribute = session.getAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID);
		if(null == attribute){//首次尚未选择分公司
			return ReturnValueUtil.getJsonRoot(null);
		}
		List<StdDevice> findDirectChildren = stdService.queryStdDevice(devId);
		return ReturnValueUtil.getJsonRoot(findDirectChildren);
	}
	
	/**
	 * 查询光站树
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryDev")
	@ResponseBody
	public Root<List<StdDevice>> queryDev(String level,String name) throws Throwable{
		List<StdDevice> findDirectChildren = stdService.queryDev(level,name);
		return ReturnValueUtil.getJsonRoot(findDirectChildren);
	}
	
	/**
	 * 查询光站关联的树.
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryRelatedAddrs")
	@ResponseBody
	public Root<List<AdTree>> queryRelatedAddrs(Integer gzId,Integer start,Integer limit) throws Throwable{
		List<AdTree> pager = queryService.queryRelatedAddrs(gzId);
		return ReturnValueUtil.getJsonRoot(pager);
	}
	
	/**
	 * 查询光站树
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryTopStdDevs")
	@ResponseBody
	public Root<List<StdDevice>> queryTopStdDevs() throws Throwable{
		List<StdDevice> findDirectChildren = stdService.queryTopStdDevs();
		return ReturnValueUtil.getJsonRoot(findDirectChildren);
	}
	
	/**
	 * 查询施工队施工人员树
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryServerTeamTree")
	@ResponseBody
	public Root<Pagination> queryServerTeamTree(@RequestParam("node")String keyWord,int start,int limit) throws Throwable{
		Pagination list = stdService.queryTeam(keyWord,start,limit);
		return ReturnValueUtil.getJsonRoot(list);
	}
	
	
	private List<Addr> warpAddrTreeDto(List<AdTree> records) {
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
	
}
