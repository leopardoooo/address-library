<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/common/head.jsp"%>
<style>
	HTML,BODY{background: #fff; }
	.container{width: 500px; height: 260px; background: #ddd; border-radius: 4px; padding-top: 50px; margin-top: 100px;}
</style>
</head>
<body>
	<div class="container">
		<form role="form" action="<%=ROOT %>/user/login" method="POST" >
			<div class="form-group">
				<label for="exampleInputEmail1">用户名</label> <input
					type="text" name="email" class="form-control" id="exampleInputEmail1" placeholder="请输入OA系统的用户名">
			</div> 
			<div class="form-group">
				<label for="exampleInputPassword1">密码</label> <input
					type="password" name="password" class="form-control" id="exampleInputPassword1"
					placeholder="请输入密码">
			</div>
			<button type="submit" class="btn btn-default">登录</button>
		</form>
	</div> 
</body>
</html>