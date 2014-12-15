package com.yaochen.address.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaochen.address.service.TreeService;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/tree")
public class TreeController {
	@Autowired
	private TreeService treeService;
	
	@RequestMapping("/addTree")
	@ResponseBody
	public Root<Integer> addTree( )throws Throwable {
		return ReturnValueUtil.getJsonRoot(1);
	}
	
	@RequestMapping("/addTrees")
	@ResponseBody
	public Root<Integer> addTrees( )throws Throwable {
		return ReturnValueUtil.getJsonRoot(1);
	}
	
	@RequestMapping("/queryTree")
	@ResponseBody
	public Root<Integer> queryTree( )throws Throwable {
		return ReturnValueUtil.getJsonRoot(1);
	}
	
	
	@RequestMapping("/test")
	@ResponseBody
	public Root<Integer> test( )throws Throwable {
		treeService.test();
		return ReturnValueUtil.getJsonRoot(1);
	}
	
	
	
}
