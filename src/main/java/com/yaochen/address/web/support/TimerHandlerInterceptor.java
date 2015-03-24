package com.yaochen.address.web.support;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.ThreadUserParamHolder;

/**
 * 时间统计拦截器
 *
 * @author Killer
 */
public class TimerHandlerInterceptor implements HandlerInterceptor{
	
	private final String TIMER_HANDLER_START_KEY = "_timer_handler_start";
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug(String.format("A new request for %s", request.getRequestURI()));
		}
		String servletPath = request.getServletPath();
		String contextPath = request.getContextPath();
		String login = BusiConstants.StringConstants.SLASH + BusiConstants.StringConstants.LOGIN_FAILURE_VIEW;
		String ssoLogin = BusiConstants.StringConstants.SLASH + BusiConstants.StringConstants.SSO_LOGIN_ACTION;
		HttpSession session = request.getSession();
		Object userInsession = session.getAttribute(BusiConstants.StringConstants.USER_IN_SESSION);
		if(null !=userInsession && userInsession.getClass().equals(UserInSession.class)){
			ThreadUserParamHolder.setUserInSession((UserInSession)userInsession);
		}else{
			//这里多做了点事情,不用重新搞个filter
			boolean notLogin = !"/user/login".equals(servletPath) && ! login.equals(servletPath);
			boolean notSsoLogin =!"/user/sso".equals(servletPath) && !ssoLogin.equals(servletPath);
			notLogin = notLogin && notSsoLogin;
			if(notLogin){
//				String ajaxFlag = request.getParameter("$ajaxRequestWiredFlagName");
				String requestType = request.getHeader("X-Requested-With");
				boolean isAjax = "XMLHttpRequest".equals(requestType);
				if(!isAjax){
					response.sendRedirect(contextPath );
					return false;
				}else{//如果不是AJAX请求
					String msg = "{\"code\":%d,\"message\":\"%s\",\"data\":null}";
					StatusCodeConstant code = StatusCodeConstant.USER_NOT_LOGGED;
					response.setCharacterEncoding("UTF-8");
					PrintWriter writer = response.getWriter();
					writer.write(String.format(msg, code.getCode(),code.getDesc()));
					writer.flush();
					return true;
				}
			}
			
		}
		Object baseScope = session.getAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_PRECND);
		if(baseScope!=null && StringHelper.isNotEmpty(baseScope.toString().trim())){
			ThreadUserParamHolder.setBaseQueryScope(baseScope.toString());
		}
		Object countyId = session.getAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID);
		if(countyId!=null && StringHelper.isNotEmpty(countyId.toString().trim())){
			ThreadUserParamHolder.setGlobeCountyId(countyId.toString());
		}
		request.setAttribute(TIMER_HANDLER_START_KEY, System.currentTimeMillis());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Object obj = request.getAttribute(TIMER_HANDLER_START_KEY);
		long task = System.currentTimeMillis() - (long)obj;
		logger.info(String.format("The request takes %d ms.", task));
	}
}
