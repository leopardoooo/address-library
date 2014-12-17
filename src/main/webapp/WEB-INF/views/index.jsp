<%@page import="com.yaochen.address.data.domain.address.AdTree"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.yaochen.address.web.controllers.TreeController"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	TreeController tc = WebApplicationContextUtils.getWebApplicationContext(application).getBean(TreeController.class);
	Map<String, Object> params = tc.getCurrentIndexParams().getData();
	List<AdTree> cityList = (List<AdTree>)params.get("cityList");
%>
<html>
<head> 
	<%@ include file="/WEB-INF/common/head.jsp" %>
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
					<span> 中国 </span>
					<span> 广西 </span>
				</p>
			</li>
			<li><a href="#" data-toggle="modal" data-target="#switchCityModal">
				选择城市 <i class="fa fa-angle-down"></i></a> </li>
		  </ul>
		  <ul class="nav navbar-nav navbar-right">
			<li><a href="#" title="地址库"><i class="glyphicon glyphicon-map-marker" ></i></a></li>
			<li><a href="#" title="光纤管理"><i class="glyphicon glyphicon-send"></i></a></li>
			<li><a href="#" class="admin-pic"> <img alt="" class="img-circle" src="<%=RES %>/main/t2.jpg"> </a></li>
			<li class="dropdown">
				  <a href="#" class="dropdown-toggle admin-info" data-toggle="dropdown">
					Hi, Administrator <i class="fa fa-angle-down"></i></a>
				  <ul class="dropdown-menu" role="menu">
					<li class="dropdown-header">来自 “xx” 部门</li>
					<li class="divider"></li>
					<li><a href="#"><i class=""></i> 退出</a></li>
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
					<div class="input-group-btn condition">
						<button type="button" class="btn btn-default dropdown-toggle" id="searchLevelBtn" data-toggle="dropdown"><span id="levelLabel">所有</span> <span class="caret"></span></button>
						<ul class="dropdown-menu" id="searchLevelList" role="menu">
							<li><a href="#">所有</a></li>
							<li class="divider"></li>
							<li><a href="#">4 级</a></li>
							<li><a href="#">5 级</a></li>
							<li><a href="#">6 级</a></li>
							<li><a href="#">7 级</a></li>
							<li><a href="#">8 级</a></li>
							<li><a href="#">9 级</a></li>
							<li class="divider"></li>
							<li class="dropdown-header">“选择一个级别进行”</li>
						</ul>
					</div><!-- /btn-group -->
					<div class="input-container">
						<input type="text" class="form-control" id="searchInput" placeholder="输入关键字搜索 “南宁市” 的信息 " autocomplete="off"
							x-webkit-speech="" x-webkit-grammar="builtin:translate" >
						<ul id="matchingResult" class="dropdown-menu">
							<li><a href="#">好大一个结果集啊1</a></li>
							<li><a href="#">好大一个结果集啊2</a></li>
							<li><a href="#">好大一个结果集啊3</a></li>
							<li><a href="#">好大一个结果集啊4</a></li>
							<li><a href="#">好大一个结果集啊5</a></li>
							<li><a href="#">好大一个结果集啊6</a></li>
							<li><a href="#">好大一个结果集啊7</a></li>
							<li><a href="#">好大一个结果集啊8</a></li>
							<li><a href="#">好大一个结果集啊9</a></li>
							<li><a href="#">好大一个结果集啊10</a></li>
							<li class="divider"></li>
							<li class="dropdown-header">共1000条相关的结果，按“←”或“→”方向键显示上下页内容</li>
						</ul>
					</div>
					<span class="input-group-btn search-submit">
						<button class="btn btn-primary" type="button"> <i class="glyphicon glyphicon-search"></i></button>
					</span>
				</div><!-- /input-group -->
		    </div>
			<div class="col-md-6 container-fluid" id="searchRight">
				 <div class="row">
					<div class="col-md-4"><a href="#" class="btn btn-link ellipsis">南宁市景秀区民族大道..</a></div>
					<div class="col-md-4"><a href="#" class="btn btn-link ellipsis">南宁市景秀区民族大道..</a></div>
					<div class="col-md-4"><a href="#" class="btn btn-link ellipsis">南宁市景秀区民族大道..</a></div>
				 </div>
				 <div class="row">
					<div class="col-md-4"><a href="#" class="btn btn-link ellipsis">南宁市景秀区民族大道..</a></div>
					<div class="col-md-4"><a href="#" class="btn btn-link ellipsis">南宁市景秀区民族大道..</a></div>
					<div class="col-md-4"><a href="#" class="btn btn-link ellipsis">南宁市景秀区民族大道..</a></div>
				 </div>
			</div>
		</div>
		<div class="row search-footer">
			<!-- Collect the nav links, forms, and other content for toggling -->
			<p id="resultDesc" class="pull-left">
				 已定位至 “中国广西南宁市西乡塘区友爱南路8号”，下级地址共 “9,000” 个，含直接下级 “21” 个。
			</p>
			<ul class="nav navbar-nav navbar-right" id="tools">
				<li><a href="#" data-toggle="modal" data-target="#addModal"><i class="glyphicon glyphicon-plus"></i></a></li>
				<li><a href="#" data-toggle="modal" data-target="#fileImportModal"><i class="glyphicon glyphicon-import"></i></a></li>
				<li><a href="#" data-toggle="modal" data-target="#fileImportModal"><i class="glyphicon glyphicon-cog"></i></a></li>
			</ul>
		</div>
	</div>
	
	<!--搜索内容 -->
	<section id="main" class="absolute">
		<%@ include file="/WEB-INF/views/EditForm.jsp" %>
		<div id="resultList">
			<!-- 搜索结果集 -->
			<div class="panel panel-default">
				<div class="panel-heading clearfix">
					<span class="text">地址列表</span>
					<div class="pull-right">
					  <button type="button" class="btn btn-default" title="首页"> <i class="fa fa-angle-left"></i></button>
					  <button type="button" class="btn btn-default"> 2 </button>
					  <button type="button" class="btn btn-default"> 3 </button>
					  <button type="button" class="btn btn-default"> 4 </button>
					  <button type="button" class="btn btn-default"> ... </button>
					  <button type="button" class="btn btn-default" title="末页"> <i class="fa fa-angle-right"></i></button>
					</div>
				</div>
				<div class="panel-body container-fluid">
					<div class="col-md-6 result-body" id="resultBody">
						<div class="item level3">
							<p class="left">3</p>
							<address>
								<strong>南宁西乡塘区友爱南路8号</strong>
								<small><i class="glyphicon glyphicon-map-marker"></i><label>3,000</label></small>
							</address>
							<p class="right"><i class="fa fa-chevron-right"></i></p>
						</div>
					</div>
					<div class="col-md-6 result-body" id="subResultBody">
						<div class="item level4">
							<p class="left">4</p>
							<address>
								<strong>南宁西乡塘区友爱南路8号</strong>
								<small><i class="glyphicon glyphicon-map-marker"></i><label>3,000</label></small>
							</address>
							<p class="right"><i class="fa fa-chevron-right"></i></p>
						</div>
						<div class="item level4">
							<p class="left">4</p>
							<address>
								<strong>南宁西乡塘区友爱南路8号</strong>
								<small><i class="glyphicon glyphicon-map-marker"></i><label>3,000</label></small>
							</address>
							<p class="right"><i class="fa fa-chevron-right"></i></p>
						</div>
						<div class="item level4">
							<p class="left">4</p>
							<address>
								<strong>南宁西乡塘区友爱南路8号</strong>
								<small><i class="glyphicon glyphicon-map-marker"></i><label>3,000</label></small>
							</address>
							<p class="right"><i class="fa fa-chevron-right"></i></p>
						</div>
					</div>
				</div>
			</div><!-- 搜索结果集结束 -->
		</div>
	</section>

	<!-- 模式窗口 -->
	<div class="modal fade" id="switchCityModal" tabindex="-1" role="dialog" aria-labelledby="switchCityModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title" id="switchCityModalLabel">选择城市</h4>
				</div>
				<div class="modal-body">
					<div class="switch-city-body">
						<div class="item-list city">
							<p>可选城市列表，已选 “<label></label>”</p>
							<div id="cityList" class="buttons">
								<%  for (AdTree tree : cityList){ %>
								<button class="btn"><%=tree.getAddrName() %></button>
								<% } %>
							</div>
						</div>
						<div class="item-list country">
							<p>可选城区、县，已选 “<label></label>”</p>
							<div id="countyList" class="buttons">
								<button class="btn btn-success">青秀区</button>
								<button class="btn">兴宁区</button>
								<button class="btn">江南区</button>
								<button class="btn">良庆区</button>
								<button class="btn">邕宁区</button>
								<button class="btn">西乡塘区</button>
								<button class="btn">武鸣县</button>
								<button class="btn">隆安县</button>
								<button class="btn">马山县</button>
								<button class="btn">上林县</button>
								<button class="btn">宾阳县</button>
								<button class="btn">横县</button>
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary">确定</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="addAddressModal" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title" id="addModalLabel">添加下级地址</h4>
				</div>
				<div class="modal-body">
					<%@ include file="/WEB-INF/views/AddForm.jsp" %>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" title="将保存的地址作为上级地址">保存（并添加下级）</button>
					<button type="button" class="btn btn-default" title="将继续添加和当前同上级的地址">保存（并继续）</button>
					<button type="button" class="btn btn-primary" title="保存当前地址并关闭窗口">保存关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
<%@ include file="/WEB-INF/common/foot.jsp" %>
<script src="<%=RES %>/main/scripts/index.js"></script>
<script>
	$(document).ready(function(){
		$('#switchCityModal').modal({
			show: false
		});
		
		$('#addAddressModal').modal({
			show: true
		});
		
		(function(){
			var desc = 'btn-success';
			$('#switchCityModal .item-list button').click(function(){
				var activeBtn = $(this).parent().find('.' + desc);
				activeBtn.removeClass(desc);
				$(this).addClass(desc);
				
				// text
				$(this).parent().parent().find(">p>label").text($(this).text());
			});
		})();
	
		Search.initialize();
	});
</script>
</html>