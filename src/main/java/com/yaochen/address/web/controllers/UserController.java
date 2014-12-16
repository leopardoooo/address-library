package com.yaochen.address.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.LoginWebServiceClient;
import com.yaochen.address.support.ThreadUserHolder;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/user")
public class UserController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private LoginWebServiceClient loginWebServiceClient;
	
	@RequestMapping("/login")
	public String login(String email, String password )throws Throwable {
		UserInSession login = null;
		try {
			login = loginWebServiceClient.login(email, password);
		} catch (Throwable e) {
			logger.info("登录错误");
//			e.printStackTrace();
		}
		
		if(login == null){
			return BusiConstants.LOGIN_FAILURE_VIEW;
		}
		ThreadUserHolder.setUserInSession(login);
		String success = BusiConstants.LOGIN_SUCCESS_VIEW;
		return success;
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
