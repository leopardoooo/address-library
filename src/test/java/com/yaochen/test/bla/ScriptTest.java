package com.yaochen.test.bla;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Before;
import org.junit.Test;

import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.dto.SystemFunction;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.ThreadUserParamHolder;

public class ScriptTest {
	@Before
	public void before() {
		UserInSession user = new UserInSession();
		Random random = new Random();
		 user.setDuty("Duty_madeUp_部门经理");
		user.setHasWork("false");
		 user.setAreaOID("AreaOID_madeUp_" );
		 user.setAreaName("AreaName_madeUp");
		 user.setHumanName("HumanName_madeUp");
		 user.setCompanyOID("CompanyOID_madeUp");
		 user.setCompanyName("CompanyName_madeUp");
		 user.setDepartmentOID("DepartmentOID_madeUp");
		 user.setDepartmentName("DepartmentName_madeUp");
		 user.setPYCode("PYCode_madeUp");
		 user.setUserOID("UserOID_madeUp");
		 user.setUserName("UserName_madeUp");
		 user.setPassword("Password_madeUp");
		 user.setDelFlag("DelFlag_madeUp");
		 user.setImg("Img_madeUp");
		 user.setMobile("Mobile_madeUp");
		 user.setPublicMobile("PublicMobile_madeUp");
		 user.setOfficeTelphone("OfficeTelphone_madeUp");
		 user.setSignature("Signature_madeUp");
		 user.setImgUrl("ImgUrl_madeUp");
		 
		 SystemFunction fun = new SystemFunction();
		 fun.setFunctionName("地址库");
		 fun.setFunctionOID(123);
		 Integer roleId = Math.abs(random.nextInt()) % 3 + 1;
		 fun.setRoleOID(roleId);
		 
		 List<SystemFunction> list = new ArrayList<SystemFunction>();
		 list.add(fun);
		user.setSystemFunction(list );
		ThreadUserParamHolder.setUserInSession(user );
	}
	
	@Test
	public void testExecuteScript() throws Exception {
		try {
			ScriptEngineManager factory = new ScriptEngineManager();//step 1
			ScriptEngine engine = factory.getEngineByName("JavaScript");//Step 2
			String filePath = "validator.js";
			String script = getFileAsString(filePath).toString();
			engine.eval(script);
			AdTree tree = new AdTree();
			tree.setAddrLevel(6);
			tree.setAddrParent(1);
			tree.setAddrName("addrName_提供给JS调用123");
			engine.put("addr", tree);
			Invocable inv = (Invocable) engine;
			String res = (String)inv.invokeFunction("validate", "Scripting" );
			System.out.println("调用js函数返回的结果 \n \t:"+res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testGetFile() throws Exception {
		String filePath = "validator.js";
		StringBuffer sb = getFileAsString(filePath);
		System.err.println(sb.toString());
	}

	private StringBuffer getFileAsString(String filePath) throws Exception {
		StringBuffer sb = new StringBuffer();
		try (InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
				BufferedReader reader = new BufferedReader( new InputStreamReader(resourceAsStream));) {
			String line = null;
			while((line = reader.readLine()) != null){
				sb.append(line+"\n");
			}
			System.err.println(sb.toString());
		}catch (Exception e) {
			throw e;
		}
		return sb;
	}
	
}
