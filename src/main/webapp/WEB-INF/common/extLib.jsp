<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ROOT2 = request.getContextPath(),
		extRes = ROOT2 + "/resources/ext/" ; %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- bootstrap -->
<%-- <link href="<%=extRes %>packages/ext-ux/build/MultiSelector.scss" rel="stylesheet"> --%>

<script src="<%=extRes %>ext.js"></script>

<script src="<%=extRes %>ext-all.js"></script>

<script src="<%=extRes %>packages/ext-ux/build/dataview-selector-plugin.js"></script>
<%-- <script src="<%=extRes %>packages/ext-ux/build/MultiSelect.js"></script> --%>
<script src="<%=extRes %>packages/ext-ux/build/ext-ux.js"></script>


<%-- 
<script src="<%=extRes %>packages/ext-aria/build/ext-aria.js"></script>
 --%>


<script src="<%=extRes %>packages/ext-locale/build/ext-locale-zh_CN.js"></script>
<%-- 
<link href="<%=extRes %>packages/ext-theme-neptune/build/resources/ext-theme-neptune-all.css" rel="stylesheet" />
<link href="<%=extRes %>packages/ext-theme-crisp/build/resources/ext-theme-crisp-all.css" rel="stylesheet" />
 --%>
 <link href="<%=extRes %>packages/ext-theme-classic/build/resources/ext-theme-classic-all.css" rel="stylesheet" />
<link href="<%=ROOT2 %>/resources/font-awesome/css/font-awesome.min.css" rel="stylesheet" />

 <style type="text/css">

       .x-grid-row
        {
            line-height:13px;vertical-align:top;padding:0 1px; -moz-user-select:text!important;-khtml-user-select:text!important;-webkit-user-select:text!important;
        }
        .x-grid-cell
        {
            overflow:hidden;font:normal 13px tahoma, arial, verdana, sans-serif;-moz-user-select:text!important;-khtml-user-select:text!important;-webkit-user-select:text!important;
        }
        .x-unselectable
        {
            -moz-user-select:text!important;-khtml-user-select:text!important;-webkit-user-select:text!important;
        }

</style>

<%-- <script src="<%=extRes %>packages/ext-aria/build/ext-aria.js"></script>
 --%>
 
<script type="text/javascript">
<!--
window.appBase = '<%=ROOT2 %>';
Ext.grid.View.prototype.unselectableCls= 'x-selectable';
Ext.override(Ext.Element, {
    unselectable : function(){
        var me = this;
        me.dom.unselectable = "";//1、把me.dom.unselectable = "on"修改为me.dom.unselectable = ""
        me.dom.selectable = "on";
        me.swallowEvent("selectstart", false);//2、把ture改成false
        me.removeCls(Element.unselectableCls);
        me.addCls('x-selectable');
        return me;
    }
});



//-->
</script>