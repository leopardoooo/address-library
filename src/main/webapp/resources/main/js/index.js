/**
 * 问题：
 * 1. 地址搜索框，直接敲回车，有时候 下拉框没有渲染，(数据已经有了)
 * 有时候，select 事件无效 
 */


Ext.onReady(function () {
	
	Ext.QuickTips.init();
    Ext.setGlyphFontFamily('FontAwesome');
    
      Addr.browserSize = {
		width:document.body.clientWidth,
		height:document.body.clientHeight
	};
	
    //主面板
    Addr.mainPanel = Ext.create('Ext.Panel',{
		layout:'border',width:'100%',
		height:Ext.getBody().getHeight() - 68,
		renderTo:Ext.get('mainViewPort'),
		items:[
		       Addr.leftTabPanel,//左侧tab面板
			{
				xtype:'panel',title:'地址列表',frame:false,region:'center',width:'50%',titleAlign:'center',
				glyph:0xf115,
				frame:true,
				layout:'border',
				items:[Addr.queryForm,Addr.queryResultGrid]
				},
			Addr.detailPanel
		]
	});

	Addr.treeComboData = {text:'',leaf:false,children:[]};
	var children = false;
	if(window.canEditNotice){//admin用户
		children = [{
			text:'广西',addrId:0,addrName:'广西',addrParent:'-1',leaf:false,
			children:selectableCompanies
		}];
	}else{
		children = selectableCompanies
	}
	Addr.treeComboData.children = children;
	
});