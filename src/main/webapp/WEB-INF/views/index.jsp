<%@page import="com.sun.tools.xjc.reader.dtd.TDTDReader"%>
<%@page import="com.yaochen.address.dto.UserInSession"%>
<%@page import="com.yaochen.address.common.BusiConstants"%>
<%@page import="com.yaochen.address.data.domain.address.AdLevel"%>
<%@page import="com.yaochen.address.data.domain.address.AdTree"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.yaochen.address.web.controllers.TreeController"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	Map<String, Object> params = TreeController.getCurrentIndexParams(session);
	List<AdTree> cityList = (List<AdTree>)params.get("cityList");
	List<AdLevel> levelList = (List<AdLevel>)params.get("levelList");
	List<AdTree> collections = (List<AdTree>)params.get("collections");
	Object city = session.getAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_SCOPE_TEXT);
	
	UserInSession user = (UserInSession)session.getAttribute(BusiConstants.StringConstants.USER_IN_SESSION);
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
				<span id="switchCityModalTargetlabel"><%=city == null ? "选择城市": city.toString() %></span>
				<i class="fa fa-angle-down"></i></a> </li>
		  </ul>
		  <ul class="nav navbar-nav navbar-right">
			<li><a href="#" title="地址库"><i class="glyphicon glyphicon-map-marker" ></i></a></li>
			<li><a href="#" title="光纤管理"><i class="glyphicon glyphicon-send"></i></a></li>
			<li><a href="#" class="admin-pic"> <img alt="" class="img-circle" src="<%=RES %>/main/t2.jpg"> </a></li>
			<li class="dropdown">
				  <a href="#" class="dropdown-toggle admin-info" data-toggle="dropdown">
					Hi, <%=user.getUserName() %> <i class="fa fa-angle-down"></i></a>
				  <ul class="dropdown-menu" role="menu">
					<li class="dropdown-header">来自 “<%=user.getCompanyName() + "/" + user.getDepartmentName() %>” 部门</li>
					<li class="divider"></li>
					<li><a href="<%=ROOT %>/user/logout">退出</a></li>
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
							<li><a href="#" data-level="-1">所有</a></li>
							<li class="divider"></li>
							<% for(AdLevel level: levelList){ %>
								<li><a href="#" data-level="<%=level.getLevelNum() %>">
									<%=level.getLevelName() %> (<%=level.getLevelNum() %>)</a></li>
							<%} %>
							<li class="divider"></li>
							<li class="dropdown-header">“选择一个级别开始搜索”</li>
						</ul>
					</div><!-- /btn-group -->
					<div class="input-container">
						<input type="text" class="form-control" id="searchInput" placeholder="输入关键字，搜索地址信息 " autocomplete="off"
							x-webkit-speech="" x-webkit-grammar="builtin:translate" >
						<ul id="matchingResult" class="dropdown-menu">
							<li class="empty">等待输入进行搜索..</li>
						</ul>
					</div>
					<span class="input-group-btn search-submit">
						<button class="btn btn-primary" id="indexSearchBtn" type="button"> <i class="glyphicon glyphicon-search"></i></button>
					</span>
				</div><!-- /input-group -->
		    </div>
			<div class="col-md-6 container-fluid" id="searchRight">
				<!--  -->
			</div>
		</div>
		<div class="row search-footer">
			<!-- Collect the nav links, forms, and other content for toggling -->
			<p id="resultDesc" class="pull-left">
				<i class="glyphicon glyphicon-map-marker"></i>
				<label id="currentAddressLabel">（无）</label>
			</p>
			<ul class="nav navbar-nav navbar-right" id="tools">
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
					<div class="pull-right" id="resultPagingTool">
						<!-- 
						<button type="button" class="btn btn-default" title="首页"> <i class="fa fa-angle-left"></i></button>
						<button type="button" class="btn btn-default"> 2 </button>
						<button type="button" class="btn btn-default"> 3 </button>
						<button type="button" class="btn btn-default"> 4 </button>
						<button type="button" class="btn btn-default"> ... </button>
						<button type="button" class="btn btn-default" title="末页"> <i class="fa fa-angle-right"></i></button>
						 -->
					</div>
				</div>
				<div class="panel-body" id="resultBody">
					<p class="empty">
						<i class="glyphicon glyphicon-search"></i>
						<span id="resultEmptyText">请使用搜索，定位上级地址！</span>
					</p>
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
								<button class="btn" data-addr-id="<%=tree.getAddrId() %>" ><%=tree.getAddrName() %></button>
								<% } %>
							</div>
						</div>
						<div class="item-list country">
							<p>可选城区、县，已选 “<label id="countyListLabel"></label>”</p>
							<div id="countyList" class="buttons">
							</div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="switchCityModalOkBtn">确定</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="addAddressModal" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
		<%@ include file="/WEB-INF/views/AddForm.jsp" %>
	</div>
</body>
<%@ include file="/WEB-INF/common/foot.jsp" %>
<script src="<%=RES %>/main/scripts/common.js"></script>
<script src="<%=RES %>/main/scripts/address.js"></script>
<script src="<%=RES %>/main/scripts/index.js"></script>
<script>
	/** 首页初始化函数 */
	Main = function(){
		return {
			initialize: function(){
				SwitchCityModal.initialize('<%=city %>');
				Search.initialize();
				Collections.initialize();
				Address.initialize();
				AddressEdit.initialize();
				AddressAdd.initialize();
			}
		};
	}();

	$(document).ready(function(){
		common.settings.path = '<%=ROOT %>';
		
		Main.initialize();
	});
</script>
</html>