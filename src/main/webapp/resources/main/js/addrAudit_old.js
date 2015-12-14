Addr.auditGridStore = Ext.create('Ext.data.JsonStore', {
	fields : Addr.addrFields,
	pageSize:Addr.itemsPerPage,
	actionMethods:'POST',
	listeners:{
		load:function( store, records, successful, eOpts  ){
			var selModel = Addr.auditGrid.selModel;
//			selModel.selectAll();
			var arr = [];
			for(var index = 0;index<records.length;index++){
				var rec = records[index];
				var addrId = rec.get('addrId');
				var flag = Addr.addrAuditWin.cachedDatas[addrId];
				if(flag ==true){
					arr.push(rec);
				}
			}
			selModel.select(arr);
		},
		beforeload : function( store, operation, eOpts ){
			Addr.auditGrid.cacheSelectedData();
			var param = store.proxy.extraParams;
			var values = Addr.addrAuditWin.down('form').getForm().getValues();
			Ext.apply(param, values);
			param.specify = Addr.addrAuditWin.specifyQueryFlag;
    		store.proxy.extraParams = param;
		}
	},
    proxy: {
        type: 'ajax', url : appBase + '/query/queryUnAudited', autoLoad:false,
        actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
        reader: {
        	rootProperty: 'data.records',
            totalProperty: 'data.totalCount'
        }
    }
});

Addr.auditGrid = Ext.create('Ext.grid.GridPanel',{
	bodyStyle:'padding:5px 0px 5px 0px;',glyph:0xf0ce,layout:'fit',
	region:'center',titleAlign:'center',rowLines : true,//title:'待审核地址',
	store:Addr.auditGridStore,
	listeners:{
		containercontextmenu:function( grid, e, eOpts ){
			if(event.preventDefault){
				event.preventDefault();
			}else if(event.stopPropagation){
				event.stopPropagation();
			}
		}
	},
	cacheSelectedData:function(){
			var selModel = Addr.auditGrid.selModel;
			var records = selModel.getSelection();
			var tmp = {};
			Ext.each(records,function(rec){tmp[''+rec.get('addrId')] = true;});
			Addr.auditGridStore.getData().each(function(record, index, total) {
				var addrId = ''+record.get('addrId');
				Addr.addrAuditWin.cachedDatas[addrId] = tmp['' + addrId ] || false ;
							});
	},
	selModel :new Ext.selection.CheckboxModel({ mode:'SIMPLE' }),
	dockedItems: [{
	    xtype: 'pagingtoolbar',
	    store: Addr.auditGridStore,   // same store GridPanel is using
	    dock: 'bottom',
	    displayInfo: true
	}],
	columns: [
	     { text: '地址编号',  flex: 1.5, sortable: true, dataIndex: 'addrId' ,hidden:true}
	     ,{text: '地址级别',flex: 1,dataIndex: 'addrLevel',sortable: true ,renderer:Addr.util.qtipValue}
	     ,{text: '上级地址',flex: 4,dataIndex: 'addrParent',sortable: true  ,
	    	 renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
	    		 var str1 = record.data.str1;
	    		 var arr = str1.split('/');
	    		 var rst = arr.length > 1 ? arr[arr.length -2] : '广西';
	    		 return Addr.util.qtipValue(rst);
		    }}
	    ,{text: '地址名称',flex: 4,dataIndex: 'addrName',sortable: true ,renderer:Addr.util.qtipValue}
	    ,{text: '地址类型',flex: 2,dataIndex: 'addrType',sortable: true ,renderer:function(v){
	    	var str = v;
	    	Addr.addrTypeStore.each(function(rec) {
	    		if(v == rec.get('id')){
	    			str = rec.get('name');
	    		}
			})
			return str;
	    }}
	    ,{text: '地址全名',flex: 5,dataIndex: 'addrFullName',sortable: true ,renderer:Addr.util.qtipValue}
	]
});

Addr.addrAuditWin=Ext.create('Ext.window.Window',{
	width:900,height:800,closeAction:'close',autoDestory:true,frame:false,glyph:0xf00c,title:'审核地址',layout:'border',
	defaults:{frame:false,border:false,region:'center'},resizable:true,maximizable : true,specifyQueryFlag:true,
	items:[
		{
		buttons:[
			{text:'匹配查询',glyph:0xf002,tooltip:'根据地址级别和名称匹配查询(地址名=查询条件).',handler:function(){
					Addr.addrAuditWin.specifyQueryFlag = true;
					var pagingBar = Addr.addrAuditWin.down('pagingtoolbar');
					pagingBar.moveFirst();
					Addr.auditGridStore.load();
				}},
				{text:'模糊查询',glyph:0xf00e,tooltip:'根据地址级别和名称模糊查询(地址名称包含查询条件).',handler:function(){
					Addr.addrAuditWin.specifyQueryFlag = false;
					var pagingBar = Addr.addrAuditWin.down('pagingtoolbar');
					pagingBar.moveFirst();
					Addr.auditGridStore.load();
				}}
				,{text:'审核通过',glyph:0xf00c,tooltip:'选中的地址通过审核,成为正式地址.',handler:function(){
					Addr.addrAuditWin.doSaveResult(true);
				}}
				,{text:'审核不通过',glyph:0xf00d,tooltip:'选中的地址不能通过审核.',handler:function(){
					Addr.addrAuditWin.doSaveResult(false);
				}}
				
			],
		xtype:'form',frame:false,border:false,layout:'hbox',region:'north',width:'20%',buttonAlign:'center',
		bodyStyle:'margin :10px;padding:10px',
		defaults:{frame:false,border:false,layout:'form',flex:1},
		items:[
			{xtype:'combo',fieldLabel:'地址级别',displayField:'levelName',valueField:'levelNum',
					editable:false,name:'level',hideLabel: false,multiSelect : false,
					store:Ext.create('Ext.data.JsonStore',{
						fields:Addr.addrLevelFields,
						data:levesTmp
					})
				},
			{xtype:'textfield',name:'addrName',fieldLabel:'地址名称'},
			{xtype:'checkbox',name:'addrName',fieldLabel:'未审核'}
		]},
		Addr.auditGrid
	],cachedDatas:{},
	showWin:function(){
		Addr.addrAuditWin.cachedDatas = {};
		Addr.auditGridStore.removeAll();
		var pagingBar = Addr.addrAuditWin.down('pagingtoolbar');
		pagingBar.moveFirst();
		Addr.addrAuditWin.down('form').getForm().reset();
		Addr.addrAuditWin.setSize(Addr.browserSize.width * 0.6,Addr.browserSize.height-100);
		Addr.addrAuditWin.show();
	},
	doSaveResult:function(result){
		
		var url = appBase + '/tree/audit';
		var arr = [];
		Addr.auditGrid.cacheSelectedData();
		for(var key in  Addr.addrAuditWin.cachedDatas){
			if(Addr.addrAuditWin.cachedDatas[key] == true ){
				arr.push(parseInt(key));
			}
		}
		if(arr.length ==0){
			Alert('前选择要审核的地址！');
			return false;
		}
		var params = {proved:result,addrIds:arr}
		var wait = Ext.Msg.wait('操作中','请稍候');
		Addr.util.req(url,params,function(data){
			wait.hide();
			if(data.code ==200 && data.message =='success'){
				Alert('操作成功！');
				Addr.contextMenuHandler.refreshAfterOperation();
				Addr.addrAuditWin.hide();
			}else{
				Alert('操作失败！',data.message);
			}
			
		});
	}
});