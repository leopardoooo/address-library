<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 编辑区 -->
<div id="edit" class="panel panel-default" data-changed-flag="false" onmouseleave="$(this).trigger('mouseoutChain')">
	<div class="panel-heading clearfix">
		<span class="text">地址信息</span>
		<div class="pull-right">
			<button type="button" class="btn btn-default" id="editFormCollectBtn" title="收藏"> <i class="glyphicon glyphicon-eye-open"></i></button>
			<button type="button" class="btn btn-default" id="editFormDeleteBtn" title="删除"> <i class="glyphicon glyphicon-trash"></i></button>
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#addAddressModal" title="添加下级地址"> <i class="glyphicon glyphicon-plus-sign"></i></button>
			<button type="button" class="btn btn-default" id="editFormSaveBtn" title="保存"> <i class="glyphicon glyphicon-ok"></i></button>
		</div>
	</div>
	<div class="panel-body" id="editForm">
		<div class="form-group">
			<label for="currentLevelAddr">分级名称<b id="editFormFullLevel"></b></label>
			<label class="form-control" id="editFormFullAddrName" placeholder="完整的地址名称"></label>
		</div>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="parentLevelAddr">地址编号</label>
						<input type="text" class="form-control" id="editFormAddrId" readonly="readonly" placeholder="可通过编号直接搜索">
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="parentLevelAddr">是否留空</label>
						<input type="text" class="form-control" id="editFormIsBlank" readonly="readonly">
						<input type="hidden" id="editFormCountyId">
					</div>
				</div>
			</div> 
		</div>
		
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="editFormAddrType">地址类型</label>
						<select class="form-control" id="editFormAddrType">
							<option value="CITY">城镇</option>
							<option value="RURAL">农村</option>
						</select>
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="editFormAddrPurpose">地址用途</label>
						<select class="form-control" id="editFormAddrPurpose">
							<option value="CITY">城市小区</option>
							<option value="INDUSTRIAL_PARK">工业园厂房</option>
							<option value="CITY_HOTEL">城市酒店</option>
							<option value="SHOPS">小区商铺</option>
							<option value="OTHERS" selected="selected" >其它</option>
						</select>
					</div>
				</div>
			</div> <!-- /row -->
		</div>
		<div class="form-tabs"> 
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#normalEdit" role="tab" data-toggle="tab">修改名称</a></li>
				<li role="presentation"><a href="#mergeAddress" role="tab" data-toggle="tab">地址合并</a></li>
				<li role="presentation"><a href="#adjustLevel" role="tab" data-toggle="tab">变更上级</a></li>
			</ul>
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="normalEdit">
					<div class="form-group">
						<input type="text" class="form-control" id="editFormAddrName" placeholder="同时修改下级地址的完整名称">
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="mergeAddress">
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