package com.yaochen.address.web.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;

/**
 *
 * 系统异常处理类
 *
 * @author Killer
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ErrorObject ajaxError(Throwable ex,
			HttpServletRequest request, HttpServletResponse response) {
		
		Throwable error = ex;
		// system message exception
		if(error.getCause() != null && error.getCause() instanceof MessageException){
			error = error.getCause();
		}
		
		ErrorObject eo = new ErrorObject();
		
		if(error instanceof HttpMessageNotReadableException){
			StatusCodeConstant scc = StatusCodeConstant.FORM_PARAMS_ERROR;
			eo.setStatusCode(scc);
			eo.setMessage(scc.getDesc());
		}else if(error instanceof MessageException){
			StatusCodeConstant scc = ((MessageException) error).getStatusCode();
			eo.setStatusCode(scc);
			eo.setMessage(scc.getDesc());
		}else{
			eo.setStatusCode(StatusCodeConstant.SYSTEM_UNKNOW_EXCEPTION);
			eo.setMessage(error.getMessage());
		}
		// log print
		logError(eo, request.getRequestURL().toString(), ex);
		
		return eo;
	}
	
	private void logError(ErrorObject eo, String url, Throwable ex){
		switch (eo.getStatusCode()) {
			case FORM_VALID_FAILURE:
				logger.error("\n A form validate unsuccessful in the " + url + ", message: " + eo.getMessage());
				break;
			case SYSTEM_UNKNOW_EXCEPTION:
				logger.error("A system exception occurs in the " + url, ex);
				break;
			case FORM_PARAMS_ERROR:
				logger.error("A form parameter exception in the " + url, ex);
				break;
			default:
				logger.error("A message exception["+ eo.getMessage() +"] occurs in the " + url);
				break;
		}
	}
	
}