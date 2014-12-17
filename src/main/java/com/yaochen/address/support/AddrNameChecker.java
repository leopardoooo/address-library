package com.yaochen.address.support;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.stereotype.Component;

import com.yaochen.address.data.domain.address.AdTree;

@Component
public class AddrNameChecker {
	/**
	 * 文件是否已经被加载.
	 */
	private static boolean fileLoaded;
	
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
	public String check(AdTree tree,Object... otherArgs)throws Exception{
		ScriptEngineManager factory = new ScriptEngineManager();//初始化引擎工厂
		ScriptEngine engine = factory.getEngineByName("JavaScript");//取得JS引擎
		if(!fileLoaded){
			script = readJs();
			setFileLoaded(true);
		}
		engine.eval(script);
		tree.setAddrName("addrName_提供给JS调用");
		engine.put("addr", tree);
		Invocable inv = (Invocable) engine;
		String res = (String)inv.invokeFunction("validate", otherArgs );
		return res;
	}
	
	
	private String readJs() throws Exception{
		StringBuffer sb = new StringBuffer();
		try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
				BufferedReader reader = new BufferedReader( new InputStreamReader(resourceAsStream));) {
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line+"\n");
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
	
}
