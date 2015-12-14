<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ROOT2 = request.getContextPath(),
		extRes = ROOT2 + "/resources/ext/" ; %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- bootstrap -->

<script src="<%=extRes %>ext.js"></script>

<script src="<%=extRes %>ext-all.js"></script>

<script src="<%=extRes %>packages/ext-ux/build/dataview-selector-plugin.js"></script>
<script src="<%=extRes %>packages/ext-ux/build/ext-ux.js"></script>


<%-- 
<script src="<%=extRes %>packages/ext-aria/build/ext-aria.js"></script>
 --%>


<script src="<%=extRes %>packages/ext-locale/build/ext-locale-zh_CN.js"></script>
<link href="<%=extRes %>packages/ext-theme-neptune/build/resources/ext-theme-neptune-all.css" rel="stylesheet" />
<link href="<%=ROOT2 %>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet" />

<%-- <script src="<%=extRes %>packages/ext-aria/build/ext-aria.js"></script>
 --%>
<script type="text/javascript">
<!--
window.appBase = '<%=ROOT2 %>';
//-->
</script>