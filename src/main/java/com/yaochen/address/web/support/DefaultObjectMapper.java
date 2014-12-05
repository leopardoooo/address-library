package com.yaochen.address.web.support;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Date类型返回json格式为自定义格式 
 * 
 * @author Killer
 */
public class DefaultObjectMapper extends ObjectMapper {

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public DefaultObjectMapper(){
       this(DEFAULT_DATE_FORMAT);
    } 
	
	public DefaultObjectMapper(final String dateFormat){
		// Disable DETECT GETTER
		this.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		this.setVisibility(JsonMethod.GETTER, Visibility.NONE);
		this.setDateFormat(new SimpleDateFormat(dateFormat));
    } 
	
}
