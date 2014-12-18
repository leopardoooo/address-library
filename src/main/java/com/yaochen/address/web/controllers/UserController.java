package com.yaochen.address.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.AddrNameChecker;
import com.yaochen.address.support.LoginWebServiceClient;
import com.yaochen.address.support.ThreadUserParamHolder;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/user")
public class UserController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private LoginWebServiceClient loginWebServiceClient;
	@Autowired
	private AddrNameChecker addrNameChecker;
	
	@RequestMapping("/login")
	public String login(HttpServletRequest req)throws Throwable {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		UserInSession login = null;
		try {
			login = loginWebServiceClient.login(email, password);
		} catch (Throwable e) {
			logger.info("登录错误");
		}
		
		if(login == null){
			return BusiConstants.StringConstants.LOGIN_FAILURE_VIEW;
		}
		req.getSession(true).setAttribute(BusiConstants.StringConstants.USER_IN_SESSION, login);
		ThreadUserParamHolder.setUserInSession(login);
		String success ="redirect:/" +  BusiConstants.StringConstants.LOGIN_SUCCESS_VIEW;
		return success;
	}
	
	/**
	 * 用户登出（用户session超时也需要调用用户登出的接口）
	 * 
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session)throws Throwable {
		// TODO
		session.removeAttribute(BusiConstants.StringConstants.USER_IN_SESSION);
		session.removeAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_PRECND);
		return "redirect:/";
	}
	
	/**
	 * 重新加载验证规则的脚本.
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/reloadRuleScript")
	@ResponseBody
	public Root<Void> reloadRuleScript()throws Throwable {
		AddrNameChecker.setFileLoaded(true);
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
	public Root<Void> setAddrScopeForCurrentUser(@RequestParam("pid") String pid,@RequestParam("subId") String subId,HttpSession session)throws Throwable {
		String slash = BusiConstants.StringConstants.SLASH;
		String str = BusiConstants.StringConstants.TOP_PID + slash + pid;
		if(!StringHelper.isEmpty(subId)){
			str += slash + subId;
		}
		session.setAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_PRECND, str);
		return ReturnValueUtil.getVoidRoot();
	}
	
	
}
