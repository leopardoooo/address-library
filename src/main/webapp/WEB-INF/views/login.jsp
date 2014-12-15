<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ROOT = request.getContextPath(), RES = ROOT + "/resources"; %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>广西广电网络公司标准地址库管理</title>

	<!-- bootstrap -->
    <link href="<%=RES %>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    
	<!-- main -->
	<link href="<%=RES %>/main/login.css" rel="stylesheet">
	
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="<%=RES%>/support/html5shiv.min.js"></script>
      <script src="<%=RES%>/support/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
  	<div class="login-panel wd">
		<div class="pull-left welcome-text">
			<h1>欢迎使用 Adress Library。</h1>
			<p>构建全省标准的地址规范。</p>
		</div>
		<div class="pull-right form">
			<form name="loginForm" class="form-inline well"
				 action="user.html" method="POST" onSubmit="return login(this)">
				<div class="control-group">
					<div class="controls">
						<input type="text" id="inputUser" autofocus="true" style="width: 266px" placeholder="用户名">
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<input type="password" id="inputPassword" style="width: 213px;" placeholder="密码">
						<button type="submit" class="submit-btn">登录</button>
					</div>
					<div class="controls">
						<label class="checkbox">
							<input type="checkbox"> 记住我
						</label>
					</div>
				</div>
			</form>
		</div>
	</div>
       <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
		<script src="<%=RES%>/support/jquery.min.js"></script>
		<!-- Include all compiled plugins (below), or include individual files as needed -->
		<script src="<%=RES %>/bootstrap/js/bootstrap.min.js"></script>
	
       <script type="text/javascript">
		function openWithFullScreen(href, name){
			var h = screen.availHeight - 61,
				w = screen.availWidth - 15;
			
			var props = "height="+ h +",width="+ w +", top=0, left=0, toolbar=no,"
					+"menubar=no,scrollbars=no,resizable=no,location=no, status=no";
			window.open(href, name, props) ;
		}
		function login(){
			//openWithFullScreen("user.html", "boss-window");
			//window.close();
			return true;
		}
       </script>
  </body>
 	
</html>