package com.yaochen.address.web.support;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import com.yaochen.address.common.StatusCodeConstant;

/**
 * 在JSON返回之前，包装一些额外固定的格式
 * 
 * @author Killer
 */
public class StandardResponseHttpMessageConverter extends
		MappingJacksonHttpMessageConverter {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	static final String SUCCESS = "success";
	
	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return super.canWrite(clazz, mediaType);
	}
	
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		JavaType javaType = getJavaType(type, contextClass);
		
		if(logger.isDebugEnabled()){
			logHeader(inputMessage.getHeaders());
		}
		
		try {
			byte[] buffer = IOUtils.toByteArray(inputMessage.getBody());
			if(logger.isDebugEnabled()){
				logger.debug("The request data: " + new String(buffer));
			}
			return getObjectMapper().readValue(buffer, javaType);
		}
		catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * 输出请求头信息
	 * @param headers
	 */
	private void logHeader(HttpHeaders headers){
		if(headers == null || headers.size() == 0){
			logger.debug("No HTTP request header information.");
		}
		
		Map<String, String> headerMap = headers.toSingleValueMap();
		StringBuffer buffer = new StringBuffer("The Http request header: [");
		for (Entry<String, String> entry : headerMap.entrySet()) {
			buffer.append(entry.getKey() + ": " + entry.getValue());
		}
		buffer.append("]");
		logger.debug(buffer.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		Root<Object> response = null;
		// 系统异常的处理
		if(object instanceof ErrorObject){
			response = new Root<Object>();
			ErrorObject eo = (ErrorObject) object;
			response.setSuccess(eo.isSuccess());
			response.setCode(eo.getStatusCode().getCode());
			response.setMessage(eo.getMessage());
		}else if(object instanceof Root){
			response = (Root<Object>) object;
			// 成功响应
			response.setCode(StatusCodeConstant.RESPONSE_SUCCESS.getCode());
			response.setMessage(SUCCESS);
		}else{
			/*
			response = new Root<Object>();
			StatusCodeConstant scc = StatusCodeConstant.CONTROLLER_RETURN_ROOT;
			response.setCode(scc.getCode());
			response.setMessage(scc.getDesc());
			*/
			super.writeInternal(object, outputMessage);
			return;
		}
		
		super.writeInternal(response, outputMessage);
	}
}
