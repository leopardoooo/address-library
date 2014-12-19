package com.yaochen.address.web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.service.TreeService;
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
	@RequestMapping("/findChildrens")
	@ResponseBody
	public Root<List<AdTree>> findChildrensByPid(@RequestParam("pid") Integer parentAddrId) throws Throwable{
		Pagination pager = treeService.findChildrensAndPagingByPid(parentAddrId, 0, 1000);
		List<AdTree> records = pager.getRecords();
		return ReturnValueUtil.getJsonRoot(records);
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
			@RequestParam("start") Integer start,
			@RequestParam("limit") Integer limit) throws Throwable{
		return ReturnValueUtil.getJsonRoot(treeService.findChildrensAndPagingByPid(parentAddrId, start, limit));
	}
	
	/**
	 * 查询当前用户有权访问的Level
	 */
	@RequestMapping("/findAuthLevel")
	@ResponseBody
	public Root<List<AdLevel>> findAuthLevelByCurrentUser()throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.findAuthLevelByCurrentUser());
	}
	
	/**
	 * 根据关键字进行搜索地址， 地址按照一定的规则进行排序
	 * 搜索时需要根据用户设置的作用域并结合startLevel进行搜索
	 * 
	 * @param startLevel 开始级别，如果为-1则为所有
	 * @param keyword
	 * @return 结果集进行分页
	 * @throws Throwable
	 */
	@RequestMapping("/search")
	@ResponseBody
	public Root<Pagination> doSearchAddress(@RequestParam("sl")Integer startLevel, 
			@RequestParam("q") String keyword, 
			@RequestParam("start") Integer start, 
			@RequestParam("limit") Integer limit)throws Throwable {
		
		Pagination pager = treeService.doSearchAddress(startLevel, keyword, start, limit);
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
	 * 添加地址
	 * 
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/addTrees")
	@ResponseBody
	public Root<List<Integer>> addTrees(AdTree tree,Integer startPosi,Integer endPosi)throws Throwable {
		return ReturnValueUtil.getJsonRoot(treeService.addTrees(tree, startPosi, endPosi ));
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
		
		// 当前用户能访问的级别级别
		params.put("levelList", tc.findAuthLevelByCurrentUser().getData());
		
		// 当前用户信息
		//params.put("session", );
		
		return params;
	}

	private static BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		TreeController.beanFactory = beanFactory;
	}
}
