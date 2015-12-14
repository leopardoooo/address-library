<%@page import="com.yaochen.address.common.CollectionHelper"%>
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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	Object city = session.getAttribute("countyStr");
	UserInSession user = (UserInSession)session.getAttribute(BusiConstants.StringConstants.USER_IN_SESSION);
	String env = System.getProperty("runEnv");
%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<!-- 引入 jquery 和 bootstrapt  -->
	<%@ include file="/WEB-INF/common/head.jsp" %>
	<script type="text/javascript">
		var selectableCompanies = <%=JSON.toJSONString(cityList) %>;
		//允许操作的最低级的地址级别
		var GlobalMaxLevelAllowed = <%=maxLevel%>;
		var GlobalCountyId = '<%=session.getAttribute(BusiConstants.StringConstants.GOLBEL_COUNTY_ID)%>';
		var levelsRange = [];
		var levesTmp = <%= JSON.toJSONString(levelList) %>;
		for(var index =0;index<levesTmp.length;index++){
			var lev = levesTmp[index];
			lev.levelName = lev.levelName+'(' + lev.levelNum +')';
			levelsRange.push(lev);
		}
		levelsRange.push({levelNum:-1,levelName:'不限'});
		window.canEditNotice = <%=params.get("canEditNotice")%>;
		window.isDqgs = <%=user.isDqgs()%>;
	</script>
</head>
<body>
<span></span>
<!-- //TODO  修改背景色  -->
	<nav id="topNav" class="navbar navbar-static-top" role="navigation" style="background-color: #157fcc;">
	  <div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
		  <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#top-navbar-collapse">
			<span class="sr-only">Toggle navigation</span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
			<span class="icon-bar"></span>
		  </button>
		  <% if("test".equals(env) ){ %>
		  <a class="navbar-brand" style="color: #FF2D2D;" href="#">地址库测试系统</a>
		  <% }else{ %>
		  <a class="navbar-brand" href="#">地址库系统</a>
		  <% } %>
		  
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="top-navbar-collapse">
		  <ul class="nav navbar-nav address-nav">
			<li>
				<p class="navbar-text">
					<span> 中国 </span>
					<span> 广西 </span>
					<span> <%=city == null ? "选择城市": city.toString() %> </span>
				</p>
			</li>
		  </ul>
		  <ul class="nav navbar-nav navbar-right">
		  <li ><a href="#" target="_self" onclick="javascript:Addr.noticeListWin.show();" title="通知公告" notice="notice"><span class="badge" style="background-color: red;padding-right: 10px;mar">0</span>  &nbsp;&nbsp;&nbsp;通知公告</a></li>
		  <li><a href="#" onclick="javascript:Addr.logWin.show()" target="_self" title="操作日志">操作日志</a></li>
		  <li><a href="resources/help.doc" target="_blank" title="帮助文档">帮助文档</a></li>
		  <li><a href="#" onclick="javascript:Addr.addrAuditWin.showWin()" target="_self" title="审核">审核</a></li>
			  
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

		<%@ include file="/WEB-INF/common/extLib.jsp" %>
		
		<script src="<%=RES %>/main/scripts/common.js"></script>
		
		<script type="text/javascript">
		common.settings.path = '<%=ROOT %>';
		</script>
			
			<script src="<%=RES %>/main/js/init.js"> </script>
			
			<script src="<%=RES %>/main/js/stdDev.js"> </script>
			<script src="<%=RES %>/main/js/stdOptr.js"> </script>
			<script src="<%=RES %>/main/js/top.js"> </script>
			<script src="<%=RES %>/main/js/left.js"> </script>
			<script src="<%=RES %>/main/js/center.js"> </script>
			<script src="<%=RES %>/main/js/right.js"> </script>
			
			<script src="<%=RES %>/main/js/contextMenu.js"> </script>
			
			<script src="<%=RES %>/main/js/index.js"> </script>
			<script src="<%=RES %>/main/js/addrAudit.js"> </script>
			
			<script src="<%=RES %>/main/js/notice.js"> </script>
			
			<%-- <script src="<%=RES %>/ext/pages/index.js"> </script> --%>

			<!-- 引入 ueditor -->
			<script src="<%=RES %>/ueditor/ueditor.config.js"> </script>
			<script src="<%=RES %>/ueditor/ueditor.all.js"> </script>
			
	<div id="mainViewPort"></div>
		
</body>
<%@ include file="/WEB-INF/common/foot.jsp" %>
<script type="text/javascript">
	
	if(!Addr){
		Ext.ns('Addr');
	}
	Addr.allCountyMap = <%=JSON.toJSONString(session.getAttribute("allCountyMap")) %>;
	Addr.singleCountyMap = <%=JSON.toJSONString(session.getAttribute("singleMap")) %>;
	
	Addr.loginUserInSession = <%=JSON.toJSONString(user) %>;
	Addr.util.selectAbleCounties = [];
	
	
	 var checkNotice = function () {
		 	
		 	Addr.util.req(appBase+'/notice/countUnRead',{},function(data){
		 		var html = '';
		 		if(data.data>0){
		 			html = '<span class="badge" style="background-color: red;">' + data.data +'</span>&nbsp;&nbsp;&nbsp;通知公告' ;
		 		}else{
		 			html = '通知公告';
		 		}
		 		var noticeEl = Ext.query('a[notice=notice]');
			 	noticeEl = noticeEl[0];
			 	noticeEl.innerHTML=html;
		 	});
		 	
		 };

		 var taskChecker = new Ext.util.TaskRunner();
		 var task = taskChecker.start({
					run : checkNotice,
					interval : 600000//十分钟
					//interval : 60000//30秒,测试
				});
</script>
</html>