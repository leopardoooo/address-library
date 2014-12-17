<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 地址新增表单 -->
<div id="addForm" class="panel-body">
	<div class="form-group">
		<label for="currentLevelAddr">上级地址（1/2/3）</label>
		<input type="text" class="form-control" readonly="readonly" value="南宁市/青秀区/民族大道">
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-6 column">
				<div class="form-group"> 
					<label for="parentLevelAddr">地址代码</label>
					<input type="text" class="form-control" value="003300">
				</div>
			</div>
			<div class="col-xs-6 column end">
				<div class="form-group"> 
					<label for="parentLevelAddr">地址级别</label>
					<select class="form-control">
						<option>5</option>
						<option>6</option>
						<option>7</option>
						<option>8</option>
					</select> 
				</div>
			</div>
		</div>
	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-6 column">
				<div class="form-group"> 
					<label for="parentLevelAddr">地址类型</label>
					<select class="form-control">
						<option>城市地址</option>
						<option>农村地址</option>
					</select>
				</div>
			</div>
			<div class="col-xs-6 column end">
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
			<li role="presentation" class="active"><a href="#singleAddInAddForm" role="tab" data-toggle="tab">单一地址</a></li>
			<li role="presentation"><a href="#batchAddInAddForm" role="tab" data-toggle="tab">多个地址（前缀+数字+后缀）</a></li>
			<li role="presentation"><a href="#fileImportInAddForm" role="tab" data-toggle="tab">文件导入</a> </li>
		</ul> 
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="singleAddInAddForm">
				<div class="form-group">
					<input type="text" class="form-control" placeholder="地址名称">
					<span class="help-block">南宁市/青秀区/民族大道/1298321</span>
				</div>
				
			</div>
			<div role="tabpanel" class="tab-pane" id="batchAddInAddForm">
				<div class="container-fluid">
					<div class="row">
						<div class="col-xs-6 column">
							<div class="form-group"> 
								<input type="text" class="form-control" placeholder="地址名称前缀">
							</div>
						</div>
						<div class="col-xs-6 column end">
							<div class="form-group"> 
								<input type="text" class="form-control" placeholder="地址名称后缀">
							</div>
						</div>
					</div> <!-- /row -->
				</div>
				<div class="form-group">
					<div class="input-group num-range">
						<input type="text" class="form-control" placeholder="开始位置，只能输入数字">
						<div class="input-group-addon"> <i>至</i> </div>
						<input type="text" class="form-control" placeholder="结束位置，只能输入数字">
					</div><!-- /input-group -->
					<span class="help-block">完整名称：</span>
				</div>
			</div>
			<div role="tabpanel" class="tab-pane" id="fileImportInAddForm">
				1111
			</div><!-- /tab panel -->
		</div><!-- /tab-content -->
	</div><!-- /tabs -->
</div><!-- 编辑区结束 -->