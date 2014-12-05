<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ROOT = request.getContextPath(), RES = ROOT + "/resources"; %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap 101 Template</title>

    <!-- Bootstrap -->
    <link href="<%=RES %>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=RES %>/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="<%=RES%>/support/html5shiv.min.js"></script>
      <script src="<%=RES%>/support/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <h1>Hello, Bootstrap</h1>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=RES%>/support/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=RES %>/bootstrap/js/bootstrap.min.js"></script>
  </body>
</html>