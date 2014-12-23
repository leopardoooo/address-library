package com.yaochen.address.support;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.yaochen.address.data.domain.address.AdTree;

@Component
public class AddrNameChecker {
	private Logger logger = Logger.getLogger(getClass());
	/**
	 * 文件是否已经被加载.
	 */
	private static boolean fileLoaded;
	
	private static enum NameCheckError{
		/** 验证通过,0 **/
		SUCCESS(0,""),
		UNKNOWN_ERROR(999,"未知错误"),
		LEV5_WITH_SHARP(5,"五级地址不能出现#号"),
		LEV6_WITH_NUM(6,"五级地址不能出现#号");
		private String desc;
		private Integer code;
		public String getDesc() {
			return desc;
		}
		@SuppressWarnings("unused")
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public Integer getCode() {
			return code;
		}
		@SuppressWarnings("unused")
		public void setCode(Integer code) {
			this.code = code;
		}
		private NameCheckError(Integer code,String desc) {
			this.desc = desc;
			this.code = code;
		}
		
		public static NameCheckError parseCode(Integer num){
			if(null == num){
				return null;
			}
			for (NameCheckError item : NameCheckError.values()) {
				if(item.getCode().equals(num.intValue())){
					return item;
				}
			}
			return null;
		}
	}
	
	/**
	 * 要执行的文件的脚本.
	 */
	private static String script;
	
	private String fileName;

	/**
	 * 返回null ,表示通过,否则返回错误信息.
	 * @param tree
	 * @param otherArgs
	 * @return
	 */
	public String checkBusiRule(AdTree tree,Object... otherArgs)throws Exception{
		ScriptEngineManager factory = new ScriptEngineManager();//初始化引擎工厂
		ScriptEngine engine = factory.getEngineByName("JavaScript");//取得JS引擎
		if(!fileLoaded){
			script = readJs();
			setFileLoaded(true);
		}
		logger.debug("-------------------加载到的JS--------------------------");
		logger.debug(script);
		logger.debug("---------------------------------------------");
		engine.eval(script);
		engine.put("addr", tree);
		Invocable inv = (Invocable) engine;
		Double result = null;
		try {
			result = (Double)inv.invokeFunction("validate", otherArgs );
		} catch (Exception e) {
			logger.info("执行验证的JS的时候出错,错误信息如下：\r\n " + e.getMessage() );
		}
		NameCheckError parseCode = NameCheckError.parseCode(result.intValue());
		if(null == parseCode){
			parseCode = NameCheckError.LEV5_WITH_SHARP;
		}
		return parseCode.getDesc();
	}
	
	
	private String readJs() throws Exception{
		StringBuffer sb = new StringBuffer();
		try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
				BufferedReader reader = new BufferedReader( new InputStreamReader(resourceAsStream));) {
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line+"\r\n");
			}
		}catch (Exception e) {
			throw e;
		}
		return sb.toString();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public static void setFileLoaded(boolean fileLoaded) {
		AddrNameChecker.fileLoaded = fileLoaded;
	}


	/**
	 * 如果被别的的系统引用,则直接抛出异常.
	 * 目前该方法留空,有需要的时候再实现.
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	public void usedByOtherSystem(AdTree tree) throws Throwable{
		//TODO 判断当前地址是否被别的系统引用....
	}
	
}
