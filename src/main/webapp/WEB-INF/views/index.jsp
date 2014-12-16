<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
				<li><a href="#" data-toggle="modal" data-target="#fileImportModal"><i class="glyphicon glyphicon-plus"></i></a></li>
				<li><a href="#" data-toggle="modal" data-target="#fileImportModal"><i class="glyphicon glyphicon-import"></i></a></li>
				<li><a href="#" data-toggle="modal" data-target="#fileImportModal"><i class="glyphicon glyphicon-cog"></i></a></li>
			</ul>
		</div>
	</div>
	
	<!--搜索内容 -->
	<section id="main" class="absolute">
		<!-- 编辑区 -->
		<div id="edit" class="panel panel-default">
			<div class="panel-heading clearfix">
				<span class="text">地址编辑</span>
				<div class="pull-right">
					<button type="button" class="btn btn-default" title="收藏"> <i class="glyphicon glyphicon-star-empty"></i></button>
					<button type="button" class="btn btn-default" title="删除"> <i class="glyphicon glyphicon-trash"></i></button>
					<button type="button" class="btn btn-default" title="保存"> <i class="glyphicon glyphicon-ok"></i></button>
				</div>
			</div>
			<div class="panel-body" id="editForm">
				<div class="form-group">
					<label for="currentLevelAddr">完整名称（1/2/3）</label>
					<input type="text" class="form-control" readonly="readonly" value="南宁市/青秀区/民族大道">
				</div>
				<div class="form-group"> 
					<label for="parentLevelAddr">上级地址</label>
					<input type="text" class="form-control" id="parentLevelAddr" readonly="readonly" placeholder="南宁市/景秀区">
				</div>
				<div class="form-tabs"> 
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="dropdown active">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">修改地址 <span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li class="active"><a href="#normalEdit" role="tab" data-toggle="tab">修改名称</a></li>
								<li><a href="#mergeAddress" role="tab" data-toggle="tab">地址合并</a></li>
								<li><a href="#adjustLevel" role="tab" data-toggle="tab">级别调整</a></li>
							</ul>
						</li>
						<li role="presentation" class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">添加下级地址 <span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li><a href="#singleAdd" role="tab" data-toggle="tab">单一地址</a></li>
								<li><a href="#batchAdd" role="tab" data-toggle="tab">多个地址</a></li>
							</ul>
						</li>
					</ul>
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="normalEdit">
							<div class="form-group">
								<input type="text" class="form-control" id="currentLevelAddr" placeholder="修改后会影响到所有下级地址的完整名称">
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="mergeAddress">
							<div class="form-group">
								<div class="input-group">
									<input type="text" class="form-control" placeholder="搜索要合并到的地址">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
									</div><!-- /btn-group -->
								</div><!-- /input-group -->
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="adjustLevel">
							<div class="form-group">
								<div class="input-group">
									<input type="text" class="form-control" placeholder="level">
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="singleAdd">
							<div class="form-group">
								<div class="input-group">
									<input type="text" class="form-control" placeholder="路、街、巷、里、弄、大道">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">号 <span class="caret"></span></button>
										<ul class="dropdown-menu dropdown-menu-right" role="menu">
											<li><a href="#">Action</a></li>
											<li><a href="#">Another action</a></li>
											<li><a href="#">Something else here</a></li>
											<li class="divider"></li>
											<li><a href="#">Separated link</a></li>
										</ul>
									 </div><!-- /btn-group -->
								</div><!-- /input-group -->
							</div>
							<div class="form-group">
								<div class="input-group">
									<input type="text" class="form-control" placeholder="级别数">
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="batchAdd">
							<div class="form-group">
								<div class="input-group">
									<input type="text" class="form-control" placeholder="开始位置">
									<div class="input-group-addon"> 至 </div>
									<input type="text" class="form-control" placeholder="结束位置">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">号 <span class="caret"></span></button>
										<ul class="dropdown-menu dropdown-menu-right" role="menu">
											<li><a href="#">Action</a></li>
											<li><a href="#">Another action</a></li>
											<li><a href="#">Something else here</a></li>
											<li class="divider"></li>
											<li><a href="#">Separated link</a></li>
										</ul>
									</div><!-- /btn-group -->
								</div><!-- /input-group -->
							</div>
							<div class="form-group">
								<div class="input-group">
									<input type="text" class="form-control" placeholder="级别数">
								</div>
							</div>
						</div><!-- /tab panel -->
					</div><!-- /tab-content -->
				</div><!-- /tabs -->
			</div>
		</div><!-- 编辑区结束 -->
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
								<button class="btn btn-success">南宁市</button>
								<button class="btn">柳州市</button>
								<button class="btn">桂林市</button>
								<button class="btn">梧州市</button>
								<button class="btn">北海市</button>
								<button class="btn">防城港市</button>
								<button class="btn">钦州市</button>
								<button class="btn">贵港市</button>
								<button class="btn">玉林市</button>
								<button class="btn">百色市</button>
								<button class="btn">贺州市</button>
								<button class="btn">河池市</button>
								<button class="btn">来宾市</button>
								<button class="btn">崇左市</button>
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
	
	<!-- 文件导入 -->
	<div class="modal fade" id="fileImportModal" tabindex="-1" role="dialog" aria-labelledby="fileImportModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title" id="fileImportModalLabel">文件导入</h4>
				</div>
				<div class="modal-body">
					<form role="form">
  <div class="form-group">
    <label for="exampleInputEmail1">Email address</label>
    <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Enter email">
  </div>
  <div class="form-group">
    <label for="exampleInputPassword1">Password</label>
    <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
  </div>
  <div class="form-group">
    <label for="exampleInputFile">File input</label>
    <input type="file" id="exampleInputFile">
    <p class="help-block">Example block-level help text here.</p>
  </div>
  <div class="checkbox">
    <label>
      <input type="checkbox"> Check me out
    </label>
  </div>
  <button type="submit" class="btn btn-default">Submit</button>
</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary">确定</button>
				</div>
			</div>
		</div>
	</div>
</body>
<%@ include file="/WEB-INF/common/foot.jsp" %>
<script src="<%=RES %>/main/scripts/index.js"></script>
<script>
	$(document).ready(function(){
		$('#switchCityModal').modal("show");
		
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