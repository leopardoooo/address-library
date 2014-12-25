package com.yaochen.test.ws;

import java.net.URL;

import org.codehaus.xfire.client.Client;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.LoginWebServiceClient;

public class OaLoginTest {
	private String username = "test";
	private String password = "123456";
	private String url = "http://10.1.1.78:8082/login.asmx?WSDL";
	private String loginMethod = "LoginFromWebService";
	
	private Client client;
	
	
	@Test
	public void testLogin() throws Throwable {
		client = new Client(new URL(this.url));
		password = "123456";
		Object[] results = client.invoke(loginMethod,new String[]{username,password});
		String jsonStr = results[0].toString();
		UserInSession parseObject = JSON.parseObject(jsonStr, UserInSession.class);
		
        System.out.println(JSON.toJSONString(parseObject, true));
	}
	
	@Test
	public void testLogin2() throws Throwable {
		LoginWebServiceClient cli = new LoginWebServiceClient();
		cli.setWsdlUrl(url);
		cli.setLoginMethod(loginMethod);
		UserInSession login = cli.login(username, password);
		System.err.println(JSON.toJSONString(login, true));
	}
	
}
