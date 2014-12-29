<%@page import="com.alibaba.fastjson.JSON"%>
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
	int maxLevel = 0;
	for(AdLevel lev:levelList){
		if(lev.getLevelNum() > maxLevel){
			maxLevel = lev.getLevelNum();
		}
	}
	Object city = session.getAttribute(BusiConstants.StringConstants.GOLBEL_QUERY_SCOPE_TEXT);
	
	UserInSession user = (UserInSession)session.getAttribute(BusiConstants.StringConstants.USER_IN_SESSION);
%>
<html>
<head> 
	<%@ include file="/WEB-INF/common/head.jsp" %>
	<script type="text/javascript">
		var selectableCompanies = <%=JSON.toJSONString(cityList) %>;
		//允许操作的最低级的地址级别
		var GlobalMaxLevelAllowed = <%=maxLevel%>;
		var GlobalCountyId = '<%=session.getAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID)%>';
	</script>
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
		  <li><a href="resources/help.doc" target="_blank" title="帮助文档"><i class="glyphicon glyphicon-book" ></i></a></li>
			<li><a href="#" title="地址库"><i class="glyphicon glyphicon-map-marker" ></i></a></li>
			<li><a href="#" title="光纤管理"><i class="glyphicon glyphicon-send"></i></a></li>
			<li><a href="#" class="admin-pic"> <img alt="" class="img-circle" src="<%=RES %>/main/t2.jpg"> </a></li>
			<li class="dropdown">
				  <a href="#" class="dropdown-toggle admin-info" data-toggle="dropdown">
					Hi, <%=user.getUserName() %> <i class="fa fa-angle-down"></i></a>
				  <ul class="dropdown-menu" role="menu">
					<li class="dropdown-header"><%=user.getAreaName() + "/" + user.getCompanyName() %></li>
					<li class="divider"></li>
					<li><a href="<%=ROOT %>/user/logout"> <i class="glyphicon glyphicon-off"></i> 退出系统</a></li>
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
							<li class="divider"></li>
							<li><a href="#" data-level="0">根据ID查询</a>
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
				<div class="panel-heading clearfix" id="resultHeading">
					<span class="text">地址列表</span>
					<div class="pull-right" id="resultPagingTool"></div>
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
							<%int count = cityList.size();int totalPage = count / 11 + (( count % 11 ==0 ) ? 0 : 1); %>
							<p>分公司列表,共 <%=totalPage %>页，当前已选中 “<label></label>” 
								&nbsp;&nbsp;&nbsp;&nbsp;<input id="companyFilter" value="" placeholder="输入关键字，敲回车过滤">
							</p>
								<div id="companyPagerToolBar">
									<ol class="breadcrumb">
									<%for(int index = 1;index<=totalPage;index++){ %>
									<li current-page="<%=index %>" style="color: blue;"><a current-page="<%=index %>" href="#">第<%=index %>页</a></li>
									<%} %>
									</ol>
								</div>
							<div id="cityList" class="buttons" total-page="<%=totalPage %>">
								<%
									int rows = 0;
									int cells = 0;
									for (AdTree tree : cityList) {
										String addrName = tree.getAddrName();
										String nameSub = addrName.length() > 3 ? 
												addrName.substring(0,3) + ".." : addrName;
										cells ++;
										boolean dataShow = rows < 2;
								%>
								<button class="btn" data-show="<%=dataShow %>" title="<%=addrName %>" data-addr-id="<%=tree.getAddrId() %>" ><%=nameSub %></button>
								<%
									if(cells % 6 ==0){
										rows ++;
									}
								} %>
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
	
	<div class="modal fade" id="alertModal" tabindex="-1" role="dialog" aria-labelledby=""alertModalLabel"" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title" id="alertModalLabel">提示</h4>
				</div>
				<div class="modal-body" id="alertBody"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="alertModalOkBtn">确定</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog"
		data-backdrop="static" aria-labelledby="confirmModalLabel"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="confirmModalLabel">模态框（Modal）标题</h4>
				</div>
				<div id="confirmBody" class="modal-body">在这里添加一些文本</div>
				<div class="modal-footer">
					<button type="button" id="confirmCancelBtn" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button type="button" id="confirmYesBtn" class="btn btn-primary">提交更改</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal -->
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
				AddressChangeLevel.initialize();
				AddressSingleMerge.initialize();
			}
		};
	}();

	$(document).ready(function(){
		common.settings.path = '<%=ROOT %>';
		
		Main.initialize();
	});
</script>
</html>