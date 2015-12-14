package com.yaochen.address.web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.OfficeHelper;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.dto.db.LogQueryForm;
import com.yaochen.address.service.TreeService;
import com.yaochen.address.support.ThreadUserParamHolder;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/tree")
public class TreeController implements BeanFactoryAware{
	@Autowired
	private TreeService treeService;
	
	/**
	 * 查找城市列表（第一级别的树节点）
	 * @throws Throwable 
	 */
	@RequestMapping(value="/findTopTrees")
	@ResponseBody
	public Root<List<AdTree>> findTopTreesByCurrentUser() throws Throwable{
		Pagination pager = treeService.findChildrensAndPagingByPid(0, 0, 1000);
		List<AdTree> records = pager.getRecords();
		return ReturnValueUtil.getJsonRoot(records);
	}
	
	/**
	 * 查找父节点.进根据addrName 查询, 不查询全名.
	 * @param start 
	 * @param limit 
	 * @throws Throwable 
	 */
	@RequestMapping(value="/queryParentTree")
	@ResponseBody
	public Root<Pagination> queryParentTree(String addrName, Integer start, Integer limit) throws Throwable{
		if(null == start){
			start = 0;
		}
		Pagination pager = treeService.queryAddrByName(addrName, start, limit);
		return ReturnValueUtil.getJsonRoot(pager);
	}
	
	/**
	 * 检查地址名.
	 * 如果不通过,填写错误的信息,如果通过,则为空.
	 * 参数至少要有  addrName ,addrLevel ,IsBlank 和  AddrParent 这四个属性
	 * @throws Throwable 
	 */
	@RequestMapping(value="/checkAddrName")
	@ResponseBody
	public Root<String> checkAddrName(AdTree addr) throws Throwable{
		String message = null;
		try {
			treeService.checkAddrName(addr);
		} catch (Throwable e) {
			message = e.getMessage();
		}
		return ReturnValueUtil.getJsonRoot(message);
	}
	
	/**
	 * 查找子节点根据当前节点编号
	 * 
	 * @param parentAddrId 父级地址编号
	 * @return 
	 * @throws Throwable 
	 */
	@RequestMapping("/findDirectChildrens")
	@ResponseBody
	public Root<List<AdTree>> findDirectChildrens(@RequestParam("pid") Integer parentAddrId) throws Throwable{
		return ReturnValueUtil.getJsonRoot(treeService.findDirectChildren(parentAddrId));
	}
	
	
	/**
	 * @see #findChildrensByPid(Integer)
	 * 
	 * @param parentAddrId 父级地址编号
	 * @return 并且分页
	 * @throws Throwable 
	 */
	@RequestMapping("/findChildrensAndPaging")
	@ResponseBody
	public Root<Pagination> findChildrensAndPagingByPid(
			@RequestParam("pid") Integer parentAddrId,
			@RequestParam("filter") String filter,
			@RequestParam("start") Integer start,
			@RequestParam("limit") Integer limit) throws Throwable{
		return ReturnValueUtil.getJsonRoot(treeService.findChildrensAndPagingByPid(parentAddrId, start, limit,filter));
	}
	
	
	/**
	 * @see #findChildrensByPid(Integer)
	 * 
	 * @param parentAddrId 父级地址编号
	 * @return 并且分页
	 * @throws Throwable 
	 */
	@RequestMapping("/findChildrensAndPaging2")
	@ResponseBody
	public Root<Map<String, Object>> findChildrensAndPagingByPid2(
			@RequestParam("pid") Integer parentAddrId,
			@RequestParam("filter") String filter,
			@RequestParam("start") Integer start,
			@RequestParam("limit") Integer limit) throws Throwable{
		Pagination pager = treeService.findChildrensAndPagingByPid(parentAddrId, start, limit,filter);
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("total_rows", pager.getTotalCount());
		map.put("page_data", pager.getRecords());
		
		return ReturnValueUtil.getJsonRoot(map);
	}
	
	/**
	 * 查询当前用户有权访问的Level
	 * 现在不用这个了,因为查询的时候不要限制了.
	 */
	@RequestMapping("/findAuthLevel")
	@ResponseBody
	@Deprecated
	public Root<List<AdLevel>> findAuthLevelByCurrentUser()throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.findAuthLevelByCurrentUser());
	}
	
	
	/**
	 * 获取所有的level.
	 */
	@RequestMapping("/findAllLevelInSession")
	@ResponseBody
	public Root<List<AdLevel>> findAllLevelInSession(HttpSession session)throws Throwable {
		Object levelsRaw = session.getAttribute(BusiConstants.StringConstants.ALL_LEVELS_IN_SESSION);
		if(levelsRaw == null){
			List<AdLevel> levels = new ArrayList<AdLevel>(0);
			return ReturnValueUtil.getJsonRoot(levels);
		}else{
			@SuppressWarnings("unchecked")
			List<AdLevel> list = (List<AdLevel>) levelsRaw;
			return ReturnValueUtil.getJsonRoot(list);
		}
	}
	
	/**
	 * 根据关键字进行搜索地址， 地址按照一定的规则进行排序
	 * 搜索时需要根据用户设置的作用域并结合startLevel进行搜索
	 * 
	 * @param targetLevel 开始级别，如果为-1则为所有
	 * @param keyword
	 * @return 结果集进行分页
	 * @throws Throwable
	 */
	@RequestMapping("/search")
	@ResponseBody
	public Root<Pagination> doSearchAddress(@RequestParam("sl")Integer targetLevel, 
			@RequestParam("q") String keyword, 
			@RequestParam("start") Integer start, 
			@RequestParam("limit") Integer limit)throws Throwable {
		
		Pagination pager = treeService.doSearchAddress(targetLevel, keyword, start, limit);
		return ReturnValueUtil.getJsonRoot(pager);
	}
	
	/**
	 * 根据关键字进行搜索 指定级别的 地址， 地址按照一定的规则进行排序
	 * 搜索时需要根据用户设置的作用域并结合startLevel进行搜索
	 * 
	 * @param startLevel 开始级别，如果为-1则为所有
	 * @param keyword
	 * @return 结果集进行分页
	 * @throws Throwable
	 */
	@RequestMapping("/searchParentLevelAddrs")
	@ResponseBody
	public Root<Pagination> searchParentLevelAddrs(@RequestParam("sl")Integer startLevel, 
			@RequestParam("q") String keyword,
			@RequestParam("countyId") String countyId,
			@RequestParam("sameParent") boolean sameParent,
			@RequestParam("start") Integer start,
			@RequestParam("currentId") Integer currentId,
			@RequestParam("limit") Integer limit)throws Throwable {
		
		Pagination pager = treeService.searchParentLevelAddrs(startLevel, keyword, countyId,currentId,sameParent,start, limit);
		return ReturnValueUtil.getJsonRoot(pager);
	}
	
	/**
	 * 根据主键查询.
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/queryById")
	@ResponseBody
	public Root<AdTree> queryById(Integer addrId)throws Throwable {
		if(null == addrId || BusiConstants.StringConstants.TOP_PID.equals(addrId.toString())){
			throw new MessageException(StatusCodeConstant.ADDR_NOT_EXISTS);
		}
		AdTree queryByKey = treeService.queryByKey(addrId);
		return ReturnValueUtil.getJsonRoot(queryByKey);
	}
	
	/**
	 * 添加地址
	 * 
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/addTree")
	@ResponseBody
	public Root<AdTree> addTree(AdTree tree)throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.addTree(tree));
	}
	
	/**
	 * 审核地址
	 * 
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/audit")
	@ResponseBody
	public Root<Void> audit(Integer [] addrIds,boolean proved)throws Throwable {
		treeService.saveAudit(addrIds,proved);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 批量添加地址.
	 * @param tree 本次新增地址的通用参数.
	 * @param addrNamePreffix	地址名 前缀.
	 * @param addrNameSuffix	地址名 后缀.
	 * @param start		起始位置	可以是英文字符,也可以是数字,但开始和结束的类型必须一致,都是字符或者都是数字.
	 * @param end		结束位置	可以是英文字符,也可以是数字,但开始和结束的类型必须一致,都是字符或者都是数字.
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/addTrees")
	@ResponseBody
	public Root<List<Integer>> addTrees(AdTree tree,String start,String end,String addrNamePreffix,String addrNameSuffix)throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.addTrees(tree,addrNamePreffix,addrNameSuffix, start, end ));
	}
	
	/**
	 * 批量添加地址.
	 * @param tree 本次新增地址的通用参数.
	 * @param addrNamePreffix	地址名 前缀.
	 * @param addrNameSuffix	地址名 后缀.
	 * @param start		起始位置	可以是英文字符,也可以是数字,但开始和结束的类型必须一致,都是字符或者都是数字.
	 * @param end		结束位置	可以是英文字符,也可以是数字,但开始和结束的类型必须一致,都是字符或者都是数字.
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/addTreesWithNames")
	@ResponseBody
	public Root<List<Integer>> addTreesWithNames(AdTree tree,String batchNames)throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.addTrees(tree,batchNames ));
	}
	
	/**
	 * 修改地址信息， 如果地址名称修改了，所有子节点的完整名称都需要修改
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/modTree")
	@ResponseBody
	public Root<Void> modTree(AdTree tree,boolean ignoreEmpty)throws Throwable {
		treeService.modTree(tree,ignoreEmpty);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 删除子节点，如果删除的子节点有下级子节点，那么需要先删除下级子子节点才给予删除
	 * 
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/delTree")
	@ResponseBody
	public Root<Void> delTree(@RequestParam("addrId") Integer addrId)throws Throwable {
		treeService.delTree(addrId);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 根据节点查询操作日志.
	 * @param addrId 
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/exportLogs")
	public void exportLogs(LogQueryForm form, Integer start, Integer limit,HttpServletResponse response)throws Throwable {
		if(limit == null || limit <1){
			start = 0 ;
			limit = Integer.MAX_VALUE;
		}
		String[] firstLine = {"ID","全名","级别","变更类型","变更字段","变更前","变更后","变更时间","操作员","父节点ID"};
		String[] properties = {"addrId","str1","levelName","changeCause","columnDesc","oldValue","newValue","changeTime","changeOptrName","addrPid"};
		
		Pagination pager = treeService.queryAllLog(form,start,limit);
		response.setHeader("Content-Disposition", "attachment; filename=optrLog.xls");  
		response.setContentType("application/octet-stream; charset=utf-8");  

		HSSFWorkbook file = OfficeHelper.generateExcel(CollectionHelper.makesureNotNull(pager.getRecords()), properties, firstLine);
		file.write(response.getOutputStream());
	}
	
	/** 
	 * 根据节点查询操作日志.
	 * @param addrId 
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/queryAllLog")
	@ResponseBody
	public Root<Pagination> queryAllLog(LogQueryForm form, Integer start, Integer limit)throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.queryAllLog(form,start,limit));
	}
	
	/**
	 * 删除子节点，如果删除的子节点有下级子节点，那么需要先删除下级子子节点才给予删除
	 * 
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/delTreeForceCasecade")
	@ResponseBody
	public Root<Void> delTreeForceCasecade(@RequestParam("addrId") Integer addrId)throws Throwable {
		treeService.delTreeForceCasecade(addrId);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 这对一个地址进行合并.
	 * 
	 * @param merger	合并后的地址.
	 * @param mergered 被合并的地址.
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/singleMerge")
	@ResponseBody
	@Deprecated
	public Root<AdTree> singleMerge(@RequestParam("merger") Integer merger,@RequestParam("mergered") Integer mergered)throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.saveSingleMerge(merger,mergered));
	}
	
	/**
	 * 
	 * @param merger	合并后的地址.
	 * @param mergered 被合并的地址.
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/merge")
	@ResponseBody
	public Root<AdTree> merge(@RequestParam("merger") Integer merger,@RequestParam("mergered") Integer[] mergered)throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.saveMerge(merger,mergered));
	}
	
	/**
	 * 变更地址上级.
	 * 
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/changeParent")
	@ResponseBody
	public Root<AdTree> changeParent(@RequestParam("addrId") Integer addrId,@RequestParam("pid") Integer pid)throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.saveChangeParent(addrId,pid));
	}
	
	
	
	/**
	 * 收藏地址（当前登录的用户）
	 */
	@RequestMapping("/collectTree")
	@ResponseBody
	public Root<Void> collectTree(@RequestParam("addrId") Integer addrId)throws Throwable {
		treeService.saveCollectTree(addrId);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 取消收藏地址（当前登录的用户）
	 */
	@RequestMapping("/cancelCollectTree")
	@ResponseBody
	public Root<Void> cancelCollectTree(@RequestParam("addrId") Integer addrId)throws Throwable {
		treeService.saveCancelCollectTree(addrId);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 查询我的收藏地址列表，按收藏时间倒序排序
	 * 
	 * @param limit 指定的条数
	 * @return
	 */
	@RequestMapping("/findCollects")
	@ResponseBody
	public Root<List<AdLevel>> findCollectTreeList(@RequestParam("limit") Integer limit)throws Throwable {
		// TODO
		return ReturnValueUtil.getJsonRoot(treeService.findCollectTreeList(limit));
	}
	
	/**
	 * 获取首页的初始化参数
	 * @return
	 * @throws Throwable
	 */
	public static Map<String, Object> getCurrentIndexParams(HttpSession session)throws Throwable {
		Map<String, Object> params = new HashMap<>();
		
		TreeController tc = beanFactory.getBean(TreeController.class);
		
		// 城市列表
		params.put("cityList", tc.findTopTreesByCurrentUser().getData());
		UserInSession optr = ThreadUserParamHolder.getOptr();
		
		boolean canEditNotice = false;
		if(optr != null){
			String userName = optr.getUserName();
			if(BusiConstants.StringConstants.SYS_ADMIN_USER_NAME.equalsIgnoreCase(userName)){
				canEditNotice = true;
			}
		}
		params.put("canEditNotice", canEditNotice);
		// 当前用户能访问的级别级别
//		params.put("levelList", tc.findAuthLevelInSession(session).getData());
		//查询不要限制,增删改才需要
		params.put("levelList", tc.findAllLevelInSession(session).getData());
		return params;
	}

	private static BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		TreeController.beanFactory = beanFactory;
	}
}
