package com.yaochen.address.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.service.TreeService;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/tree")
public class TreeController {
	@Autowired
	private TreeService treeService;
	
	
	/**
	 * 查找城市列表（第一级别的树节点）
	 */
	@RequestMapping(value="/findTopTrees")
	@ResponseBody
	public Root<List<AdTree>> findTopTreesByCurrentUser(){
		// TODO 
		return null;
	}
	
	/**
	 * 查找子节点根据当前节点编号
	 * 
	 * @param parentAddrId 父级地址编号
	 * @return 
	 */
	@RequestMapping("/findChildrens")
	@ResponseBody
	public Root<List<AdTree>> findChildrensByPid(@RequestParam("pid") Integer parentAddrId){
		
		// TODO 
		
		return null;
	}
	
	/**
	 * @see #findChildrensByPid(Integer)
	 * 
	 * @param parentAddrId 父级地址编号
	 * @return 并且分页
	 */
	@RequestMapping("/findChildrensAndPaging")
	@ResponseBody
	public Root<Pagination> findChildrensAndPagingByPid(
			@RequestParam("pid") Integer parentAddrId,
			@RequestParam("start") Integer start,
			@RequestParam("limit") Integer limit){
		
		// TODO 
		
		return null;
	}
	
	/**
	 * 查询当前用户有权访问的Level
	 */
	@RequestMapping("/findAuthLevel")
	@ResponseBody
	public Root<List<AdLevel>> findAuthLevelByCurrentUser()throws Throwable {
		// TODO 
		return null;
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
		
		// TODO
		
		return null;
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
	public Root<Void> addTree(AdTree tree)throws Throwable {
		// TODO
		return null;
	}
	
	/**
	 * 修改地址信息， 如果地址名称修改了，所有子节点的完整名称都需要修改
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/modTree")
	@ResponseBody
	public Root<Void> modTree(AdTree tree)throws Throwable {
		// TODO
		return null;
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
		
		// TODO
		
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 收藏地址（当前登录的用户）
	 */
	@RequestMapping("/collectTree")
	@ResponseBody
	public Root<Void> collectTree(@RequestParam("addrId") Integer addrId)throws Throwable {
		
		// TODO
		
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 取消收藏地址（当前登录的用户）
	 */
	@RequestMapping("/cancelCollectTree")
	@ResponseBody
	public Root<Void> cancelCollectTree(@RequestParam("addrId") Integer addrId)throws Throwable {
		
		// TODO
		
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
		
		return null;
	}
	
}
