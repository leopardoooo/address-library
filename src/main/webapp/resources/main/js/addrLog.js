Addr.logQueryForm=new Ext.FormPanel({
	doQuery:function(){
		Addr.logQueryGrid.store.load();
	},
	exportResult:function(current){
		//导出结果集, current 为 true 的时候,导出当前的,否则导出所有符合条件的.
		var formValues = Addr.logQueryForm.getForm().getValues();
		var param = Addr.logQueryGrid.store.proxy.extraParams;
		for(var key in formValues){
			param[key] = formValues[key];
		}
		var paramStr = '?nonsenseParam=' + new Date().getTime();
		var start = 0;limit = 0;
		if(current == true){
			var pageData = Addr.logQueryGrid.down('pagingtoolbar').getPageData();
			var pageSize = Addr.logQueryGrid.store.pageSize;
			start = (pageData.currentPage - 1) * pageSize ;
			limit = pageSize;
		}else{
			limit = -1;
		}
		for (var name in param){
			var val = param[name];
			if(val != null){
				paramStr +=  '&' + name + '='+val ;
			}
		}
		var url = 'tree/exportLogs' + paramStr +  '&start='+start +  '&limit='+limit ;
		window.open(url);
	},
	buttons:[
		{text:'查询',tooltip:'查询',glyph:0xf002,handler:function(){
			Addr.logQueryForm.doQuery();
		}},
		{text:'导出当前',tooltip:'导出当前页面的结果',glyph:0xf0ed,handler:function(){
			Addr.logQueryForm.exportResult(true);
		}},
		{text:'导出所有',tooltip:'导出符合条件的所有页面的结果',glyph:0xf019,handler:function(){
			Addr.logQueryForm.exportResult();
		}},
		{text:'关闭窗口',glyph:0xf00d,tooltip:'点击这个按钮后者按下 Esc 关闭窗口',handler:function(){
			Addr.logWin.hide();
		}}
	],
	buttonAlign:'center',defaults:{padding:'5px',columnWidth:.3},bodyStyle:'padding:10px 20px 10px 10px;',
	layout:'column',items:[
			{name:'changeType',xtype:'combo',fieldLabel:'变更类型',displayField:'name',valueField:'id',editable:false,
			store:Ext.create('Ext.data.Store',{
				fields:['id','name'],
				data:[{id:'ADD',name:'新增'},{id:'ADD',name:'新增'},{id:'EDIT',name:'修改'},{id:'CHANGE_PARENT',name:'变更父节点'},{id:'MERGE_DEL',name:'合并'},{id:'',name:'不限'}]
			})},
			{name:'oldValue',xtype:'textfield',fieldLabel:'变更前的值'},
			{name:'newValue',xtype:'textfield',fieldLabel:'变更后的值'},
			{xtype:'combo',fieldLabel:'地址级别',displayField:'levelName',valueField:'levelNum',
					editable:false,name:'level',hideLabel: false,multiSelect : false,
					store:Ext.create('Ext.data.JsonStore',{
						fields:Addr.addrLevelFields,
						data:levesTmp
					})
				},
				{xtype:'datefield',name:'startDate',fieldLabel:'开始日期',format :'Y-m-d'},
				{xtype:'datefield',name:'endDate',fieldLabel:'结束日期',format :'Y-m-d'},
				{name:'addrName',xtype:'textfield',fieldLabel:'地址名称'},
				{name:'pid',xtype:'combo',fieldLabel:'上级地址',columnWidth:.6,
							 pageSize: Addr.itemsPerPage,
						        store: Ext.create("Ext.data.Store", {
								        model: "Addr.model.Addr",
								        autoLoad:false,pageSize:Addr.itemsPerPage,
								        listeners:{
								        	load:function(store){
								        		var combo = Addr.logQueryForm.getForm().findField('pid');
								        		if(1== store.getCount()){//只有一条记录,直接选中
								        			var record = store.getAt(0);
								        			combo.select(record);
								        		}else {
													setTimeout(function(){
														combo.expand();
													},500);
												}
								        	}
								        },
								        proxy: {
								            type: 'ajax', url: appBase + '/tree/queryParentTree',
								            actionMethods: {  create : 'POST', read   : 'POST', update : 'POST', destroy: 'POST' },
								            reader: {
								                type: 'json',
								                rootProperty: 'data.records',
								                totalProperty: 'data.totalCount'
								            }
								        }
								    }),anchor:'90%',
						        displayField: 'str1',valueField: 'addrId',typeAhead: false, queryMode: 'local',
						        listeners:{
						        	specialkey: function(field, e){
						                switch(e.getKey()){
						                	case e.ENTER://回车
						                		field.store.load({params:{"addrName" : field.getRawValue()}});
						                	break;
						                	default : 
						                	break;
						                }
						            }
						        },
						        triggerAction: 'all',
						        emptyText:'输入关键字敲“回车”查询地址...',
						        selectOnFocus:true},
	        {name:'changeOptrId',xtype:'combo',fieldLabel:'操作员',columnWidth:.6,
			        store: Ext.create("Ext.data.Store", {
			        	fields:['userid','username'],autoLoad:false,
					        listeners:{
					        	load:function(store){
					        		var combo = Addr.logQueryForm.getForm().findField('changeOptrId');
					        		if(1== store.getCount()){//只有一条记录,直接选中
					        			var record = store.getAt(0);
					        			combo.select(record);
					        		}else {
										setTimeout(function(){ combo.expand(); },500);
									}
					        	}
					        },
					        proxy: {
					            type: 'ajax', url: appBase + '/user/queryUsers',
					            actionMethods: {  create : 'POST', read   : 'POST', update : 'POST', destroy: 'POST' },
					            reader: {
					                type: 'json',rootProperty: 'data'
					            }
					        }
					    }),anchor:'90%',
			        displayField: 'username',valueField: 'userid',typeAhead: false, queryMode: 'local',
			        listeners:{
			        	specialkey: function(field, e){
			                switch(e.getKey()){
			                	case e.ENTER://回车
			                		field.store.load({params:{userName:field.getRawValue()}});
			                	break;
			                	default : 
			                	break;
			                }
			            }
			        },
			        triggerAction: 'all',
			        emptyText:'输入关键字敲“回车”查询操作员...',
			        selectOnFocus:true},
				{name:'addrId',xtype:'hidden',fieldLabel:'addrId'}
				
	]
});

Addr.logQueryStore = Ext.create('Ext.data.JsonStore',{
	fields:["changeSn", "changeCause", "addrName", "columnDesc", "oldValue", "newValue", "levelName", "addrPid", "changeOptrName"],
	listeners:{
		beforeLoad:function( store, operation, eOpts ){
    		store.removeAll();
    		var extra = Addr.logQueryForm.getForm().getValues();
    		var param = store.proxy.extraParams;
			for(var key in extra){
				param[key] = extra[key];
			}
			store.proxy.extraParams = param;
		}
	},
    proxy: {
        type: 'ajax', url : appBase + '/tree/queryAllLog', autoLoad:false,
        actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
        reader: {
        	rootProperty: 'data.records',
            totalProperty: 'data.totalCount'
        }
    }
})

Addr.logQueryGrid=new Ext.grid.GridPanel({
	layout:'fit',flex:4,glyph:0xf0ce,rowLines : true,
	store:Addr.logQueryStore,
	dockedItems: [{
	    xtype: 'pagingtoolbar',
	    store: Addr.logQueryStore,   // same store GridPanel is using
	    dock: 'bottom',
	    displayInfo: true
	}],
	columns: [
	     { text: '序号',  flex: 2, sortable: true, dataIndex: 'changeSn',renderer:Addr.util.qtipValue },
	     { text: '变更类型',  flex: 2, sortable: true, dataIndex: 'changeCause',renderer:Addr.util.qtipValue },
	     { text: '地址名称',  flex: 2, sortable: true, dataIndex: 'addrName',renderer:Addr.util.qtipValue },
	     {text: '变更属性',flex: 3,dataIndex: 'columnDesc',sortable: true ,renderer:Addr.util.qtipValue} ,
	     {text: '变更前',flex: 3,dataIndex: 'oldValue',sortable: true ,renderer:Addr.util.qtipValue},
	     {text: '变更后',flex: 3,dataIndex: 'newValue',sortable: true ,renderer:Addr.util.qtipValue},
	     {text: '地址级别',flex: 3,dataIndex: 'levelName',sortable: true ,renderer:Addr.util.qtipValue},
	     {text: '上级节点',flex: 3,dataIndex: 'addrPid',sortable: true ,renderer:Addr.util.qtipValue},
	     {text: '操作员',flex: 3,dataIndex: 'changeOptrName',sortable: true ,renderer:Addr.util.qtipValue}
	]
	
});

/**
 * 日志窗口.
 */
Addr.logWin = new Ext.Window({
	loadSingle:function(addrObj){
		Addr.logQueryForm.getForm().findField('addrId').setValue(addrObj.addrId);
		
		Addr.logQueryStore.removeAll();
		var pagingBar = Addr.logWin.down('pagingtoolbar');
		pagingBar.moveFirst();
		Addr.logQueryForm.doQuery();
	},
	listeners:{
		beforehide:function(win){
			Addr.logQueryForm.getForm().reset();
			Addr.logQueryStore.removeAll();
			var pagingBar = Addr.logWin.down('pagingtoolbar');
			pagingBar.moveFirst();
		},
		beforeshow:function(win){
			win.setSize(Addr.browserSize.width * 0.6,Addr.browserSize.height-100);
		},
		show:function(win){
//			win.toggleMaximize( );
		}
	},
	width:900,height:800,closeAction:'hide',frame:false,layout: {type: 'vbox',pack: 'start',align: 'stretch'},title:'操作日志',
	glyph:0xf02d,maximizable:true, defaults:{frame:false,flex:2},
	items:[
		Addr.logQueryForm,Addr.logQueryGrid
	]
});