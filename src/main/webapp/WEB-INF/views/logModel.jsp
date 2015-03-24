<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 地址新增表单 -->
<div class="modal-dialog" style="width: 900px;">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" style="text-align: center;" id="logModalTitle">操作日志</h4>
		</div>
		<div class="modal-body">
			<!-- 表格开始 -->
			<table class="table" id="logModelTable" style="width: 860px;">
			   <thead>
			      <tr>
			         <th>全名</th>
			         <th>变更类型</th>
			         <th>变更原因</th>
			         <th>变更时间</th>
			         <th>变更操作员</th>
			         <th>父级ID</th>
			         <th>流水</th>
			      </tr>
			   </thead>
			   <tbody>
			   </tbody>
			</table>
			
		</div>
		<div class="modal-footer">
			<div class="pull-right" id="logPagingTool"></div>
		</div>
	</div>
</div>
