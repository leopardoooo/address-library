package com.yaochen.address.web.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdOaCountyRef;
import com.yaochen.address.data.domain.address.AdSysUser;
import com.yaochen.address.data.mapper.address.AdOaCountyRefMapper;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.service.QueryService;
import com.yaochen.address.service.TreeService;
import com.yaochen.address.service.UserService;
import com.yaochen.address.support.AddrNameChecker;
import com.yaochen.address.support.LoginWebServiceClient;
import com.yaochen.address.support.MD5Util;
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
	private UserService userService;
	@Autowired
	private QueryService queryService;
	@Autowired
	private AdOaCountyRefMapper adOaCountyRefMapper;

	/**
	 *  说明：  server.xml配置了 编码格式为  GBK.所以这里需要把字符串转码为GBK格式的.
	 * @param req
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/sso")
	public String sso(HttpServletRequest req)throws Throwable {
		
		String password = req.getParameter("Password");

		/*
		String userAgent = req.getHeader("user-agent");
		boolean notIe = userAgent.indexOf("MSIE") <0;
*/
		
		String userName = req.getParameter("UserName");
		userName = getRequestUserName( req.getQueryString(),"UserName");
		
		String targetUrl = req.getParameter("Url");//暂时忽略这个
		if(logger.isDebugEnabled()){
			logger.debug("单点登录请求targetUrl : " + targetUrl);
		}
		String fromOa = req.getParameter("OA");
		UserInSession login = null;
		HttpSession session = req.getSession(true);
		
		if(!"1".equals(fromOa)){
			logger.warn("单点登录错误: 不是来自OA系统." );
			session.setAttribute(BusiConstants.StringConstants.LOGIN_ERROR_IN_SESSION, "不是来自OA的单点登录,拒绝登录.");
			return BusiConstants.StringConstants.LOGIN_FAILURE_VIEW;
		}
		
		
		try {
			login = loginWebServiceClient.login(userName, password);
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
		
		String countyId = countyObj.getCountyId();
		login.setCompanyOID(countyId);
		logger.info("当前登录用户  " + login.getUserName() + " 分公司 " + login.getCompanyName() + " countyId : " + login.getCompanyOID());
		//登录的用户放到session和线程变量里
		int maxLevel = treeService.getMaxAllowedLevel(login);
		login.setMaxLevelAllowed(maxLevel);
		ThreadUserParamHolder.setUserInSession(login);
		session.setAttribute(BusiConstants.StringConstants.USER_IN_SESSION, login);
		String success = BusiConstants.StringConstants.REDIRECT_ACTION + BusiConstants.StringConstants.SLASH +  BusiConstants.StringConstants.LOGIN_SUCCESS_VIEW;
		userService.updateUserInfo(login);
		resetCountyInfo(countyObj,session);
		resetLevelsInfo(maxLevel, session);
		return success;
	}
	
	
	@RequestMapping("/login")
	public String login(HttpServletRequest req)throws Throwable {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		UserInSession login = null;
		HttpSession session = req.getSession(true);
		try {
			login = loginWebServiceClient.login(email, MD5Util.EncodePassword(password));
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
		String countyId = countyObj.getCountyId();
		login.setCompanyOID(countyId);
		logger.info("当前登录用户  " + login.getUserName() + " 分公司 " + login.getCompanyName() + " countyId : " + login.getCompanyOID());
		//登录的用户放到session和线程变量里
		int maxLevel;
		try {
			maxLevel = treeService.getMaxAllowedLevel(login);
		} catch (MessageException e) {
			e.printStackTrace();
			session.setAttribute(BusiConstants.StringConstants.LOGIN_ERROR_IN_SESSION, e.getMessage());
			return BusiConstants.StringConstants.LOGIN_FAILURE_VIEW;
		}
		login.setMaxLevelAllowed(maxLevel);
		ThreadUserParamHolder.setUserInSession(login);
		session.setAttribute(BusiConstants.StringConstants.USER_IN_SESSION, login);
		String success = BusiConstants.StringConstants.REDIRECT_ACTION + BusiConstants.StringConstants.SLASH +  BusiConstants.StringConstants.LOGIN_SUCCESS_VIEW;
		userService.updateUserInfo(login);
		resetCountyInfo(countyObj,session);
		resetLevelsInfo(maxLevel, session);
		return success;
	}
	
	private void resetCountyInfo(AdOaCountyRef countyObj, HttpSession session) throws Throwable {
//		queryService.queryCounties(addrIdNode, optr);
		String countyId = countyObj.getCountyId();
		ThreadUserParamHolder.setGlobeCountyId(countyId);
		session.setAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID, countyId);
		
		List<AdOaCountyRef> list = adOaCountyRefMapper.selectAll();
		Map<String, List<AdOaCountyRef>> map = CollectionHelper.converToMap(list, "pid");
		ThreadUserParamHolder.setCountyChildrenMap(map);
		
		if(!BusiConstants.StringConstants.COUNTY_ALL.equals(countyId)){
			List<AdOaCountyRef> children = map.get(countyId);
			UserInSession user = ThreadUserParamHolder.getOptr();
			user.setDqgs(CollectionHelper.isNotEmpty(children));
			session.setAttribute(BusiConstants.StringConstants.USER_IN_SESSION, user);
			ThreadUserParamHolder.setUserInSession(user);
		}
		
		session.setAttribute("allCountyMap", map);
		Map<String, AdOaCountyRef> singleMap = CollectionHelper.converToMapSingle(list, "countyId");
		ThreadUserParamHolder.setCountyMap(singleMap);
		session.setAttribute("singleMap", singleMap);
		
		String pid = countyObj.getPid();
		logger.info("为首页设置当前地址做准备，当前分公司  --->> " + JSON.toJSONString(countyObj) + " " );
		AdOaCountyRef parent = singleMap.get(pid);
		String countyStr = "";
		if(parent!= null && !BusiConstants.StringConstants.COUNTY_ALL.equals(parent.getCompanyId())){
			logger.info("为首页设置当前地址做准备，当前分公司 的上级  --->> " + JSON.toJSONString(parent) + " " );
//			countyStr = parent.getCountyName() + "/";
		}
		countyStr += countyObj.getCountyName();
		session.setAttribute("countyStr", countyStr);
		
	}
	
	/**
	 * 查询用户列表...
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping("/queryUsers")
	@ResponseBody
	public Root<List<AdSysUser>> queryUsers(String userName)throws Throwable {
		return ReturnValueUtil.getJsonRoot(userService.queryUsers(userName));
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
		session.removeAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID);
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
	
	private void resetLevelsInfo(Integer maxLevel,HttpSession session)throws Throwable {
		List<AdLevel> levelRaw = treeService.findAuthLevelByCurrentUser();
		List<AdLevel> levels = new ArrayList<AdLevel>();
		for (AdLevel level : levelRaw) {
			boolean ok = level.getLevelNum()>maxLevel;
			if(ok){
				levels.add(level);
			}
		}
		//设置了这个属性之后,  把 操作员的 权限级别  放入到session
		session.setAttribute(BusiConstants.StringConstants.FILTERED_LEVELS_IN_SESSION, levels);
		List<AdLevel> allLevels = treeService.findAllLevels();
		session.setAttribute(BusiConstants.StringConstants.ALL_LEVELS_IN_SESSION, allLevels);
	}
	
	/**
	 * 之所以使用这么变态的方法是因为老是出现乱码.只好手工处理参数.
	 * @param notIe 
	 * @param queryString 
	 * @param paramName 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getRequestUserName(String queryString, String paramName)
			throws UnsupportedEncodingException {
		String utf = "UTF-8";
		
		String queryStringAsString = java.net.URLDecoder.decode(queryString, utf);
		
		if(queryStringAsString.indexOf("=")<0){
			return "";
		}
		String result = null;
		String[] params = queryStringAsString.split("&");
		for (String param : params) {
			String[] pair = param.split("=");
			String key = pair[0];
			if(key.equals(paramName)){
				String raw = pair[1];
				result = raw;
				break;
			}
		}
		
		return result;
	}
}
