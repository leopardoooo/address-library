<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 地址新增表单 -->
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="addModalLabel">添加下级地址</h4>
		</div>
		<div class="modal-body">
			<!-- 表单开始 -->
			<div id="addForm" class="panel-body">
				<div class="form-group">
					<label for="addFormParentAddrFullName">上级地址</label>
					<input type="text" class="form-control" id="addFormParentAddrFullName" readonly="readonly" value="">
					<input type="hidden" id="addFormCountyId">
				</div>
				<div class="container-fluid">
					<div class="row">
						<div class="col-xs-6 column">
							<div class="form-group"> 
								<label for="addFormIsBlank">是否留空</label>
								<select class="form-control" id="addFormIsBlank">
									<option value="T">是</option>
									<option value="F" selected="selected">否</option>
								</select> 
							</div>
						</div>
						<div class="col-xs-6 column end">
							<div class="form-group"> 
								<label for="addFormAddrLevel">地址级别</label>
								<input type="text" class="form-control"  id="addFormAddrLevel" readonly="readonly" value="5">
							</div>
						</div>
					</div>
				</div>
				<div class="container-fluid">
					<div class="row">
						<div class="col-xs-6 column">
							<div class="form-group"> 
								<label for="addFormAddrType">地址类型</label>
								<select class="form-control" id="addFormAddrType">
									<option value=""></option>
									<option value="CITY">城市地址</option>
									<option value="RURAL">农村地址</option>
								</select>
							</div>
						</div>
						<div class="col-xs-6 column end">
							<div class="form-group"> 
								<label for="addFormAddrUse">地址用途</label>
								<select class="form-control" id="addFormAddrUse">
									<option value=""></option>
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
					<ul class="nav nav-tabs" id="addFormTabs" role="tablist">
						<li role="presentation" class="active"><a href="#singleAddInAddForm" role="tab" data-toggle="tab">单一地址</a></li>
						<li role="presentation" id="batchAddInAddFormLi" ><a href="#batchAddInAddForm" role="tab" data-toggle="tab">多个地址（前缀+数字+后缀）</a></li>
					</ul> 
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="singleAddInAddForm">
							<div class="form-group">
								<input type="text" class="form-control" placeholder="地址名称" id="addFormAddrName">
								<span class="help-block" id="addFormAddrFullName"></span>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="batchAddInAddForm">
							<div class="container-fluid">
								<div class="row">
									<div class="col-xs-6 column">
										<div class="form-group"> 
											<input type="text" class="form-control" id="addFormAddrNamePreffix" placeholder="地址名称前缀">
										</div>
									</div>
									<div class="col-xs-6 column end">
										<div class="form-group"> 
											<input type="text" class="form-control" id="addFormAddrNameSuffix" placeholder="地址名称后缀">
										</div>
									</div>
								</div> <!-- /row -->
							</div>
							<div class="form-group">
								<div class="input-group num-range">
									<input type="text" class="form-control" id="addFormBatchStartNum" placeholder="开始位置，只能输入数字">
									<div class="input-group-addon"> <i>至</i> </div>
									<input type="text" class="form-control" id="addFormBatchEndNum" placeholder="结束位置，只能输入数字">
								</div><!-- /input-group -->
								<span class="help-block"></span>
							</div>
						</div>
					</div><!-- /tab-content -->
				</div><!-- /tabs -->
			</div><!-- 表单结束 -->
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" id="addFormSaveToNextBtn" title="将保存的地址作为上级地址">保存（并添加下级）</button>
			<button type="button" class="btn btn-default" id="addFormSaveContinue" title="将继续添加和当前同上级的地址">保存（并继续）</button>
			<button type="button" class="btn btn-primary" id="addFormOnlySave"  title="保存当前地址并关闭窗口">保存</button>
		</div>
	</div>
</div>
