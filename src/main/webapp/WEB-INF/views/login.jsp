<%@page import="com.yaochen.address.common.BusiConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/common/head.jsp"%>
<% Object errMsgObj = session.getAttribute(BusiConstants.StringConstants.LOGIN_ERROR_IN_SESSION); %>
<style>
	HTML,BODY{background: #fff; }
	.body{width: 500px; height: 260px; background: #ddd; border-radius: 4px; padding-top: 50px; margin-top: 100px;}
	.header{background: #3b5598;}
	.container{width: 1000px;}
	.header .container{font-size: 40px; color: #fff; font-weight: bold; padding: 30px 0 10px 0; text-shadow: 0 1px 0 #fff;}
	#loginForm{width: 450px; border-radius: 4px; border:1px solid #ccc; padding: 20px;}
	#loginForm{margin: 50px 0 0 0;} 
	#loginForm .title{font-size: 16px; padding: 10px 0 16px 0; margin-bottom: 20px;  border-bottom: 1px solid #ccc;}
	#loginForm .btn{background: #3b5598; border-radius: 0; border-color:  #0f3596;}
</style>
</head> 
<body>
	<div class="header">
		<div class="container">
			<p>广西广电网络公司</p>
		</div>
	</div>
	<div class="container">
		<form id="loginForm" role="form" action="<%=ROOT %>/user/login" method="POST" >
			<p class="title"><b>标准地址库</b> 登录</p>
			<div class="form-group"> 
				<label for="exampleInputEmail1">用户名</label> <input
					type="text" name="email" class="form-control" id="exampleInputEmail1" placeholder="目前支持OA系统的用户名">
			</div> 
			<div class="form-group">
				<label for="exampleInputPassword1">密码</label> <input
					type="password" name="password" class="form-control" id="exampleInputPassword1"
					placeholder="输入密码">
			</div> 
			<button type="submit" class="btn btn-primary">登录</button>
		</form>
	</div> 
	
	<% if(null != errMsgObj){ String errMsg = null == errMsgObj? "": errMsgObj.toString();
			%>
			<div style="height: 80px;" id="loginErrMsg" >
				<span style="font-size: 32px;text-align: center;position: relative;left: 30%;">
				<%= errMsgObj %>
				</span>
			</div>
			
			<script type="text/javascript">
				$('#loginErrMsg').fadeOut(5000);
			</script>
			
			<% 		
		}
	%>
	
</body>
</html>