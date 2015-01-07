<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 编辑区 -->
<div id="edit" class="panel panel-default" data-changed-flag="false">
	<div class="panel-heading clearfix">
		<span class="text">地址信息</span>
		<div class="pull-right">
			<button type="button" class="btn btn-default" id="displayPanelCollectBtn" title="收藏"> <i class="glyphicon glyphicon-eye-open"></i></button>
			<button type="button" class="btn btn-default" id="displayPanelDeleteBtn" title="删除"> <i class="glyphicon glyphicon-trash"></i></button>
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#addAddressModal" title="添加下级地址"> <i class="glyphicon glyphicon-plus-sign"></i></button>
			<button type="button" class="btn btn-default" data-toggle="modal" id="displayPanelBtn" data-target="#modAddressModal" title="修改"> <i class="glyphicon glyphicon-edit"></i></button>
		</div>
	</div>
	<div class="panel-body" id="displayPanel">
		<div class="form-group">
			<label for="currentLevelAddr">分级名称<b id="displayPanelFullLevel"></b></label>
			<label class="form-control" id="displayPanelFullAddrName" placeholder="完整的地址名称"></label>
		</div>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="parentLevelAddr">地址编号</label>
						<input type="text" class="form-control" id="displayPanelAddrId" readonly="readonly" placeholder="可通过编号直接搜索">
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="parentLevelAddr">是否留空</label>
						<input type="text" class="form-control" id="displayPanelIsBlank" readonly="readonly">
						<input type="hidden" id="displayPanelCountyId">
					</div>
				</div>
			</div> 
		</div>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="displayPanelAddrType">地址类型</label>
						<input type="text" class="form-control" id="displayPanelAddrType" readonly="readonly">
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="displayPanelAddrPurpose">地址用途</label>
						<input type="text" class="form-control" id="displayPanelAddrPurpose" readonly="readonly">
					</div>
				</div>
			</div> <!-- /row -->
		</div>
		<div class="form-tabs"> 
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#mergeAddress" role="tab" data-toggle="tab">地址合并</a></li>
				<li role="presentation"><a href="#adjustLevel" role="tab" data-toggle="tab">变更上级</a></li>
			</ul>
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="mergeAddress">
					<div class="form-group">
						<div class="input-group dropup">
							<div class="input-group-btn condition">
								<button type="button" class="btn btn-default dropdown-toggle" id="mergeParentRangeBtn" data-toggle="dropdown"><span id="mergeParentRangeLabel">范围</span> <span class="caret"></span></button>
								<ul class="dropdown-menu" id="mergeParentRangeList" role="menu">
									<li><a href="#" data-level="false">所有</a></li>
									<li><a href="#" data-level="true">同父</a></li>
									<li class="divider"></li>
									<li class="dropdown-header">“选择范围开始搜索”</li>
								</ul>
							</div>
							<input type="text" class="form-control" id="searchInputForSingleMerge" placeholder="搜索要合并到的地址">
							<ul id="singleMergeSearchResult" class="dropdown-menu">
								<li class="empty">等待输入进行搜索..</li>
							</ul>
							<div class="input-group-btn">
								<button type="button" id="singleMergeSearchBtn" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
							</div><!-- /btn-group -->
						</div><!-- /input-group -->
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="adjustLevel">
					<div class="form-group dropup">
						<div class="input-group">
							<div class="input-group-btn condition">
								<button type="button" class="btn btn-default dropdown-toggle" id="clRangeBtn" data-toggle="dropdown"><span id="clRangeLabel">范围</span> <span class="caret"></span></button>
								<ul class="dropdown-menu" id="clRangeList" role="menu">
									<li><a href="#" data-level="false">所有</a></li>
									<li><a href="#" data-level="true">同父</a></li>
									<li class="divider"></li>
									<li class="dropdown-header">“选择范围开始搜索”</li>
								</ul>
							</div>
							<input type="text" class="form-control" id="searchInputForChangeLevel" placeholder="选择级别数">
							
							<ul id="changeLevelSearchResult" class="dropdown-menu">
								<li class="empty">等待输入进行搜索..</li>
							</ul>
							<div class="input-group-btn">
								<button type="button" id="changeLevelSearchBtn" class="btn btn-default"><i class="glyphicon glyphicon-search"></i></button>
							</div><!-- /btn-group -->
						</div>
					</div>
				</div>
			</div><!-- /tab-content -->
		</div><!-- /tabs -->
	</div>
</div><!-- 编辑区结束 -->