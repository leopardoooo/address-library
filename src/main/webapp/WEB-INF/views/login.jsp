<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/common/head.jsp"%>
<style>
	HTML,BODY{background: #fff; }
	.container{width: 600px; height: 400px; background: #ddd; border-radius: 4px; padding-top: 50px;}
</style>
</head>
<body>
	<div class="container">
		<form role="form" action="<%=ROOT %>/user/login" method="POST" >
			<div class="form-group">
				<label for="exampleInputEmail1">User Name</label> <input
					type="text" name="email" class="form-control" id="exampleInputEmail1" placeholder="Enter UserName">
			</div>
			<div class="form-group">
				<label for="exampleInputPassword1">Password</label> <input
					type="password" name="password" class="form-control" id="exampleInputPassword1"
					placeholder="Password">
			</div>
			<div class="checkbox">
				<label> <input type="checkbox"> Check me out
				</label>
			</div>
			<button type="submit" class="btn btn-default">Submit</button>
		</form>
	</div>
</body>
</html>