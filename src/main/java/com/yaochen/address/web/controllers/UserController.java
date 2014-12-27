package com.yaochen.address.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdOaCountyRef;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.mapper.address.AdOaCountyRefMapper;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.service.TreeService;
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
	@Autowired
	private TreeService treeService;
	@Autowired
	private AdOaCountyRefMapper adOaCountyRefMapper;
	
	@RequestMapping("/login")
	public String login(HttpServletRequest req)throws Throwable {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		UserInSession login = null;
		HttpSession session = req.getSession(true);
		try {
			login = loginWebServiceClient.login(email, password);
		} catch (Throwable e) {
			logger.info("登录错误: " + e.getMessage());
			session.setAttribute(BusiConstants.StringConstants.LOGIN_ERROR_IN_SESSION, e.getMessage());
		}
		if(login == null){
			return BusiConstants.StringConstants.LOGIN_FAILURE_VIEW;
		}
		
		//这里要保证OA传过来的countyId 是数字型
		AdOaCountyRef countyObj = adOaCountyRefMapper.selectByPrimaryKey(login.getCompanyOID());
		if(null == countyObj){
			session.setAttribute(BusiConstants.StringConstants.LOGIN_ERROR_IN_SESSION, "未能找到OA系统于地址库系统的分公司对应关系");
			return BusiConstants.StringConstants.LOGIN_FAILURE_VIEW;
		}
		login.setCompanyOID(countyObj.getCountyId());
		
		//登录的用户放到session和线程变量里
		int maxLevel = treeService.getMaxAllowedLevel(login);
		login.setMaxLevelAllowed(maxLevel);
		ThreadUserParamHolder.setUserInSession(login);
		session.setAttribute(BusiConstants.StringConstants.USER_IN_SESSION, login);
		String success = BusiConstants.StringConstants.REDIRECT_ACTION + BusiConstants.StringConstants.SLASH +  BusiConstants.StringConstants.LOGIN_SUCCESS_VIEW;
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
		session.removeAttribute(BusiConstants.StringConstants.USER_IN_SESSION);
		session.removeAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_PRECND);
		session.removeAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID);
		session.removeAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_SCOPE_TEXT);
		session.removeAttribute(BusiConstants.StringConstants.ALL_LEVELS_IN_SESSION);
		ThreadUserParamHolder.clearAll();
		return BusiConstants.StringConstants.REDIRECT_ACTION + BusiConstants.StringConstants.SLASH;
	}
	
	/**
	 * 重新加载验证规则的脚本.
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/reloadRuleScript")
	@ResponseBody
	public Root<Void> reloadRuleScript()throws Throwable {
		AddrNameChecker.setFileLoaded(false);
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
	public Root<Void> setAddrScopeForCurrentUser(@RequestParam("pid") Integer pid,@RequestParam("subId") Integer subId, 
			@RequestParam("scopeText") String scopeText, HttpSession session)throws Throwable {
		String slash = BusiConstants.StringConstants.SLASH;
		String str = BusiConstants.StringConstants.TOP_PID + slash + pid + slash;
		Integer lowestAddrId = pid;
		if(null != subId ){
			lowestAddrId = subId;
			str += subId + slash;
		}
		AdTree tree = treeService.queryByKey(lowestAddrId);
		
		session.setAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID, tree.getCountyId());
		session.setAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_PRECND, str);
		session.setAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_SCOPE_TEXT, scopeText);
		
		List<AdLevel> levelRaw = treeService.findAuthLevelByCurrentUser();
		List<AdLevel> levels = new ArrayList<AdLevel>();
		Integer addrLevel = tree.getAddrLevel();
		for (AdLevel level : levelRaw) {
			boolean ok = level.getLevelNum()>addrLevel;
			if(ok){
				levels.add(level);
			}
		}
		
		//设置了这个属性之后,  把 操作员的 权限级别  放入到session
		session.setAttribute(BusiConstants.StringConstants.FILTERED_LEVELS_IN_SESSION, levels);
		
		List<AdLevel> allLevels = treeService.findAllLevels();
		session.setAttribute(BusiConstants.StringConstants.ALL_LEVELS_IN_SESSION, allLevels);
		
		return ReturnValueUtil.getVoidRoot();
	}
	
	
}
