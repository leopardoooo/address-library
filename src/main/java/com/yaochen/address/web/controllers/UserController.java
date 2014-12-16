package com.yaochen.address.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/login")
	@ResponseBody
	public Root<Integer> login()throws Throwable {
		return ReturnValueUtil.getJsonRoot(1);
	}
	
	/**
	 * 用户登出（用户session超时也需要调用用户登出的接口）
	 * 
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public Root<Void> logout()throws Throwable {

		// TODO
		
		return ReturnValueUtil.getVoidRoot();
	}
	
	
	/**
	 * 设置当前操作员的地址级别
	 * 
	 * @param levelPath 如：1/2/3
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/setAddrScope")
	@ResponseBody
	public Root<Void> setAddrScopeForCurrentUser(@RequestParam("levelPath") String levelPath)throws Throwable {
		
		// TODO 
		
		return ReturnValueUtil.getVoidRoot();
	}

}
