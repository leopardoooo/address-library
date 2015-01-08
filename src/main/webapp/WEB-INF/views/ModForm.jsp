<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 地址新增表单 -->
<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="addModalLabel">编辑地址</h4>
		</div>
		<div class="modal-body">
			<!-- 表单开始 -->
			<div class="panel-body" id="editForm">
			<div class="form-group">
				<label for="editFormFullAddrName" id="editFormFullAddrNameLabel">分级名称</label>
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
		
		<div class="form-group"> 
			<label for="editFormAddrPurpose22">修改名称</label>
			<input type="text" class="form-control" id="editFormAddrName" placeholder="同时修改下级地址的完整名称">
			<label class="help-block" id="editFormAddrFullName"></label>
		</div>
		
	</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-primary" data-dismiss="modal" id="editFormOnlyCancel"  title="保存当前地址并关闭窗口">取消</button>
			<button type="button" class="btn btn-primary" id="editFormOnlySave"  title="保存当前地址并关闭窗口">保存</button>
		</div>
	</div>
</div>
