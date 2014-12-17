package com.yaochen.address.support;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xerces.dom.DocumentImpl;
import org.codehaus.xfire.client.Client;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSON;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.dto.UserInSession;

public class LoginWebServiceClient {
	private Client client;
	private String wsdlUrl;
	String loginMethod ;
	
	private Object[] remoteInvokeRaw(String methodName, Object... params)
			throws Exception, MalformedURLException {
		if(this.client == null){
			client = new Client(new URL(this.wsdlUrl));
		}
		Object[] results = client.invoke(methodName, params);
		return results;
	}

	public String getWsdlUrl() {
		return wsdlUrl;
	}

	public void setWsdlUrl(String wsdlUrl) {
		this.wsdlUrl = wsdlUrl;
	}
	
	public UserInSession login(String username,String password) throws Throwable{
		if(this.wsdlUrl == null || this.wsdlUrl.trim().length()== 0 || 
				this.loginMethod == null || loginMethod.trim().length() == 0){
			throw new MessageException(StatusCodeConstant.WS_CFG_ERROR);
		}
		Object[] remoteInvokeRaw = this.remoteInvokeRaw(loginMethod, username,password);
		
		if(remoteInvokeRaw == null || remoteInvokeRaw.length ==0){
			throw new MessageException(StatusCodeConstant.WS_REQ_FAILURE);
		}
		DocumentImpl di = (DocumentImpl) remoteInvokeRaw[0];
		if(di ==null){
			throw new MessageException(StatusCodeConstant.LOGIN_FAILED);
		}
		NodeList childNodes = di.getChildNodes();

		//TODO 如下
		/**
		 * 根据给出的文档,返回的正确的response body 应该
		 * 只有一个元素,就是string,内容是json格式的字符串,错误的结果,比如用户不存在
		 * ,密码不正确等等,不知道返回到结过是怎样,知道后再做处理
		 **/
		Node extractTarget = extractTarget(childNodes, BusiConstants.WSDL_TARGET_NODE_NAME);
		
		if(extractTarget == null) {
			throw new MessageException(StatusCodeConstant.LOGIN_FAILED);
		}
		String json = extractTarget.getTextContent();
		UserInSession parseObject = JSON.parseObject(json,UserInSession.class);
		
		return parseObject;
	}
	
	public static void main(String[] args) throws Exception{
		LoginWebServiceClient client = new LoginWebServiceClient();
		String url = "http://localhost/login/services/LoginAction?wsdl";
		client.setWsdlUrl(url);
		  String method = "login";
//		rs = client.remoteInvoke(method, "tr123", 67);
		
		Object[] remoteInvokeRaw = client.remoteInvokeRaw(method, "tr123", 67);
		if(remoteInvokeRaw == null || remoteInvokeRaw.length ==0){
			//TODO cuow错误处理
		}
		DocumentImpl di = (DocumentImpl) remoteInvokeRaw[0];
		
		NodeList childNodes = di.getChildNodes();
		
		Node extractTarget = extractTarget(childNodes, "string");
		
		if(extractTarget == null) {
			System.err.println("出错");
		}
		String json = extractTarget.getTextContent();
		
		UserInSession parseObject = JSON.parseObject(json,UserInSession.class);
		
		System.err.println(JSON.toJSONString(parseObject, true));
		
	}
	
	public static Node extractTarget(NodeList childNodes,String nodeName) {
		int length = childNodes.getLength();
		for (int index = 0; index < length; index++) {
			Node node = childNodes.item(index);
			String name = node.getNodeName();
			if(name.equals(nodeName)){
				return node;
			}
			NodeList sub = node.getChildNodes();
			if(sub==null){
				return null;
			}
			Node extractTarget = extractTarget(sub, nodeName);
			if(null != extractTarget){
				return extractTarget;
			}
		}
		
		
		return null;
	}

	public void setLoginMethod(String loginMethod) {
		this.loginMethod = loginMethod;
	}
	
}