<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 编辑区 -->
<div id="edit" class="panel panel-default">
	<div class="panel-heading clearfix">
		<span class="text">地址编辑</span>
		<div class="pull-right">
			<button type="button" class="btn btn-default" title="收藏"> <i class="glyphicon glyphicon-star-empty"></i></button>
			<button type="button" class="btn btn-default" title="删除"> <i class="glyphicon glyphicon-trash"></i></button>
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#addAddressModal" title="添加下级地址"> <i class="glyphicon glyphicon-plus-sign"></i></button>
			<button type="button"  class="btn btn-default" title="保存"> <i class="glyphicon glyphicon-ok"></i></button>
		</div>
	</div>
	<div class="panel-body" id="editForm">
		<div class="form-group">
			<label for="currentLevelAddr">完整名称（1/2/3）</label>
			<input type="text" class="form-control" readonly="readonly" value="南宁市/青秀区/民族大道">
		</div>
		<div class="form-group"> 
			<label for="parentLevelAddr">地址代码</label>
			<input type="text" class="form-control" id="parentLevelAddr" readonly="readonly" placeholder="003201">
		</div>
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="parentLevelAddr">地址类型</label>
						<select class="form-control">
							<option>城市地址</option>
							<option>农村地址</option>
						</select>
					</div>
				</div>
				<div class="col-xs-6">
					<div class="form-group"> 
						<label for="parentLevelAddr">地址用途</label>
						<select class="form-control">
							<option>城市小区</option>
							<option>工业园厂房</option>
							<option>城市酒店</option>
							<option>小区商铺</option>
							<option>其它</option>
						</select>
					</div>
				</div>
			</div> <!-- /row -->
		</div>
		<div class="form-tabs"> 
			<ul class="nav nav-tabs" role="tablist">
				<li role="presentation" class="active"><a href="#normalEdit" role="tab" data-toggle="tab">修改名称</a></li>
				<li role="presentation"><a href="#mergeAddress" role="tab" data-toggle="tab">地址合并</a></li>
				<li role="presentation"><a href="#adjustLevel" role="tab" data-toggle="tab">级别调整</a></li>
			</ul>
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="normalEdit">
					<div class="form-group">
						<input type="text" class="form-control" id="currentLevelAddr" placeholder="所有下级地址的完整名称也会修改">
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
						<input type="text" class="form-control" placeholder="选择级别数">
					</div>
				</div>
			</div><!-- /tab-content -->
		</div><!-- /tabs -->
	</div>
</div><!-- 编辑区结束 -->