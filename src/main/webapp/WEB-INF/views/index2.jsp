<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ROOT = request.getContextPath(),
		RES = ROOT + "/resources" ; %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>广西广电网络公司标准地址库管理</title>

	<!-- bootstrap -->
    <link href="<%=RES %>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    
	<!-- font-awesome -->
    <link href="<%=RES %>/font-awesome/css/font-awesome.min.css" rel="stylesheet">
	
	<!-- main -->
	<link href="<%=RES %>/main/address-main.css" rel="stylesheet">
	
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="<%=RES %>/support/html5shiv.min.js"></script>
      <script src="<%=RES %>/support/respond.min.js"></script>
    <![endif]-->
  </head>
  <body> 
  	<nav id="topNav" class="navbar navbar-static-top" role="navigation">
	  <div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
		  <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#top-navbar-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		  </button>
		  <a class="navbar-brand" href="#">GuangXi LOGO</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="top-navbar-collapse">
		  <ul class="nav navbar-nav address-nav">
			<li>
				<p class="navbar-text">
					<span> <i class="glyphicon glyphicon-globe"></i> 中国 </span>
					<span class="middle"> <i class="glyphicon glyphicon-tint"></i> 
					广西 </span>
				</p>
			</li>
			<li><a href="#" data-toggle="modal" data-target="#switchCityModal"><i class="glyphicon glyphicon-plane"></i> 
				选择城市 <span class="fa fa-angle-down"></span></a> </li>
		  </ul>
		  <ul class="nav navbar-nav navbar-right">
			<li><a href="#"><i class="glyphicon glyphicon-map-marker" title="地址库"></i></a></li>
			<li><a href="#"><i class="glyphicon glyphicon-send" title="光纤管理"></i></a></li>
			<li><a href="#" class="admin-pic"> <img alt="" class="img-circle" src="./main/t2.jpg"> </a></li>
			<li class="dropdown">
				  <a href="#" class="dropdown-toggle admin-info" data-toggle="dropdown">
					Hi, Administrator <i class="fa fa-angle-down"></i></a>
				  <ul class="dropdown-menu" role="menu">
					<li><a href="#">Action</a></li>
					<li><a href="#">Another action</a></li>
					<li><a href="#">Something else here</a></li>
					<li class="divider"></li>
					<li><a href="#">修改资料</a></li>
				  </ul>
			</li>
		  </ul>
		</div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	<div id="search" class="container-fluid">
		<div class="row">
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="col-md-6" id="searchLeft">
				<div class="input-group">
					  <input type="text" class="form-control" placeholder="输入关键字搜索 “南宁市” 的信息 " autofocus="true" autocomplete="off"
							x-webkit-speech="" x-webkit-grammar="builtin:translate" >
					  <span class="input-group-btn">
						<button class="btn btn-primary" type="button"> <i class="glyphicon glyphicon-search"></i></button>
					  </span>
				</div><!-- /input-group -->
		    </div>
			<div class="col-md-6 container-fluid" id="searchRight">
				 <div class="row">
					<div class="col-md-4"><a href="#" class="btn ellipsis">南宁市景秀区民族大道..</a></div>
					<div class="col-md-4"><a href="#" class="btn ellipsis">南宁市景秀区民族大道..</a></div>
					<div class="col-md-4"><a href="#" class="btn ellipsis">南宁市景秀区民族大道..</a></div>
				 </div>
				 <div class="row">
					<div class="col-md-4"><a href="#" class="btn ellipsis">南宁市景秀区民族大道..</a></div>
					<div class="col-md-4"><a href="#" class="btn ellipsis">南宁市景秀区民族大道..</a></div>
					<div class="col-md-4"><a href="#" class="btn ellipsis">南宁市景秀区民族大道..</a></div>
				 </div>
			</div>
		</div>
		<div class="row search-footer">
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="col-md-6" id="resultDesc">
				 在“南宁市/景秀区”下，匹配 “南宁市景秀区民族大道32号” 共1,001条，耗时0.000132ms
			 </div>
			 <div class="col-md-6" id="tools">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#"><i class="glyphicon glyphicon-plus"></i></a></li>
					<li><a href="#"><i class="glyphicon glyphicon-import"></i></a></li>
				  </ul>
			 </div>
		</div>
	</div>
	
	<!--搜索内容 -->
	<section id="main" class="absolute">
		<ul id="level">
			<li class="start">
				<div class="article" id="level4">
					<div class="level-header clearfix">
						<p class="level-count"></p>
						<ul class="nav navbar-nav navbar-right">
							<li><a href="#" class="btn"><i class="fa fa-plus-circle"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-search"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-filter"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-refresh"></i></a></li>
						</ul>
					</div>
					<div class="list-group">
					  <a href="#" class="list-group-item">Cras justo odio <span class="badge">14</span></a>
					  <a href="#" class="list-group-item">Dapibus ac facilisis inlisis inlisis inlisis inlisis <span class="badge">14</span></a>
					  <a href="#" class="list-group-item">Morbi leo risuslisis inlisis inlisis in <span class="badge">14</span></a>
					  <a href="#" class="list-group-item">Porta ac consectetur ac <span class="badge">14</span></a>
					  <a href="#" class="list-group-item">Vestibulum at eros <span class="badge">14</span></a>
					</div>
				</div>
			</li>
			<li>
				<div class="article" id="level5">
					<div class="level-header clearfix">
						<p class="level-count"></p>
						<ul class="nav navbar-nav navbar-right">
							<li><a href="#" class="btn"><i class="fa fa-plus-circle"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-search"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-filter"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-refresh"></i></a></li>
						</ul>
					</div>
					<div class="list-group">
					</div>
				</div>
			</li>
			<li>
				<div class="article" id="level6">
					<div class="level-header clearfix">
						<p class="level-count"></p>
						<ul class="nav navbar-nav navbar-right">
							<li><a href="#" class="btn"><i class="fa fa-plus-circle"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-search"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-filter"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-refresh"></i></a></li>
						</ul>
					</div>
					<div class="list-group">
					</div>
				</div>
			</li>
			<li>
				<div class="article" id="level7"> 
					<div class="level-header clearfix">
						<p class="level-count"></p>
						<ul class="nav navbar-nav navbar-right">
							<li><a href="#" class="btn"><i class="fa fa-plus-circle"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-search"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-filter"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-refresh"></i></a></li>
						</ul>
					</div>
					<div class="list-group">
					</div>
				</article>
			</li>
			<li>
				<div class="article" id="level8">
					<div class="level-header clearfix">
						<p class="level-count"></p>
						<ul class="nav navbar-nav navbar-right">
							<li><a href="#" class="btn"><i class="fa fa-plus-circle"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-search"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-filter"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-refresh"></i></a></li>
						</ul>
					</div>
					<div class="list-group">
					</div>
				</article>
			</li>
			<li class="end">
				<div class="article" id="level9">
					<div class="level-header clearfix">
						<p class="level-count"></p>
						<ul class="nav navbar-nav navbar-right">
							<li><a href="#" class="btn"><i class="fa fa-plus-circle"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-search"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-filter"></i></a></li>
							<li><a href="#" class="btn"><i class="glyphicon glyphicon-refresh"></i></a></li>
						</ul>
					</div>
					<div class="list-group">
					</div>
				</div>
			</li>
		</ul>
	</section>

<!-- 带显示的内容 -->
<div class="modal fade" id="switchCityModal" tabindex="-1" role="dialog" aria-labelledby="switchCityModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="switchCityModalLabel">选择城市</h4>
      </div>
      <div class="modal-body">
        
		 ...
		
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary"></button>
      </div>
    </div>
  </div>
</div>

  </body>
 	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="<%=RES %>/support/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="<%=RES %>/bootstrap/js/bootstrap.min.js"></script>
</html>