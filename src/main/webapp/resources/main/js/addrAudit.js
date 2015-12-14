Addr.auditGridFields =[
			{"name":"district","type":"auto"}, {"name":"region","type":"auto"},　{"name":"road","type":"auto"}, {"name":"roadNum","type":"auto"},　{"name":"village","type":"auto"}, {"name":"building","type":"auto"},　{"name":"street","type":"auto"}, {"name":"department","type":"auto"},　{"name":"houseNum","type":"auto"}, {"name":"districtCode","type":"auto"},　{"name":"regionCode","type":"auto"}, {"name":"roadCode","type":"auto"},　{"name":"roadNumCode","type":"auto"}, {"name":"villageCode","type":"auto"},　{"name":"buildingCode","type":"auto"}, {"name":"streetCode","type":"auto"},　{"name":"departmentCode","type":"auto"}, {"name":"houseNumCode","type":"auto"},　{"name":"collected","type":"auto"},{"name":"addrId","type":"auto"},{"name":"addrName","type":"auto"},{"name":"addrLevel","type":"auto"},{"name":"addrType","type":"auto"},{"name":"addrTypeText","type":"auto"},{"name":"addrUse","type":"auto"},{"name":"addrUseText","type":"auto"},{"name":"isBlank","type":"auto"},{"name":"isBlankText","type":"auto"},{"name":"addrParent","type":"auto"},{"name":"addrPrivateName","type":"auto"},{"name":"addrFullName","type":"auto"},{"name":"addrCode","type":"auto"},{"name":"countyId","type":"auto"},{"name":"createTime","type":"auto"},{"name":"createOptrId","type":"auto"},{"name":"createDoneCode","type":"auto"},{"name":"status","type":"auto"},{"name":"str1","type":"auto"},{"name":"str2","type":"auto"},{"name":"str3","type":"auto"},{"name":"str4","type":"auto"},{"name":"str5","type":"auto"}
            ,{name:'checked4audit'}
       ]; 
Addr.auditGridStore = Ext.create('Ext.data.JsonStore', {
	fields : Addr.auditGridFields,
	pageSize:Addr.itemsPerPage,
	actionMethods:'POST',
	listeners:{
		load:function( store, records, successful, eOpts  ){
			var selModel = Addr.auditGrid.selModel;
			for(var index = 0;index<records.length;index++){
				var rec = records[index];
				var addrId = rec.get('addrId');
				var flag = Addr.addrAuditWin.cachedDatas[addrId];
				if(flag ==true){
					rec.set('checked4audit',true);
					rec.commit();
				}
			}
		},
		beforeload : function( store, operation, eOpts ){
			var param = store.proxy.extraParams;
			var values = Addr.addrAuditWin.getQueryParam();
			Ext.apply(param, values);
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

Addr.auditCompareGridStore = Ext.create('Ext.data.JsonStore', {
	fields : Addr.auditGridFields,
	pageSize:Addr.itemsPerPage,
	actionMethods:'POST',
	listeners:{
		beforeload : function( store, operation, eOpts ){
			var param = store.proxy.extraParams;
			var values = Addr.auditCompareGrid.getQueryParam();
			Ext.apply(param, values);
    		store.proxy.extraParams = param;
		}
	},
    proxy: {
        type: 'ajax', url : appBase + '/query/query', autoLoad:false,
        actionMethods: {create : 'POST',read   : 'POST',update : 'POST',destroy: 'POST'},
        reader: {
        	rootProperty: 'data.records',
            totalProperty: 'data.totalCount'
        }
    }
});

Addr.auditCompareLevels = [{levelNum:null,levelName:'所有',levelDesc:'所有'}].concat(levesTmp);
Addr.auditCompareGrid = Ext.create('Ext.grid.GridPanel',{
	bodyStyle:'padding:5px 0px 5px 0px;',glyph:0xf0ce,layout:'fit',
	region:'center',titleAlign:'center',rowLines : true,//title:'待审核地址比较',
	store:Addr.auditCompareGridStore,
	tbar:['&nbsp;','-',
	{xtype:'combo',fieldLabel:'地址级别',displayField:'levelName',valueField:'levelNum',
		emptyText:'地址级别...', id:'addrAuditCompareLevelParam',
					editable:false,name:'level',hideLabel: true,multiSelect : false,
					store:Ext.create('Ext.data.JsonStore',{
						fields:Addr.addrLevelFields,
						data:Addr.auditCompareLevels
					})
				},
	{xtype:'hidden',hideLabel: true,id:'addrAuditCompareNameFlagParam'},//是否模糊查询
	{xtype:'textfield',name:'addrName',hideLabel: true,id:'addrAuditCompareNameParam',emptyText:'地址名称...',fieldLabel:'地址名称'},'->',
		{glyph:0xf002,text:'匹配查询',scope:this,handler:function(){
			Ext.getCmp('addrAuditCompareNameFlagParam').setValue(1);
			var pagingBar = Addr.auditCompareGrid.down('pagingtoolbar');
			pagingBar.moveFirst();
		}},'-'
		,{glyph:0xf002,text:'模糊查询',scope:this,handler:function(){
			Ext.getCmp('addrAuditCompareNameFlagParam').setValue(0);
			var pagingBar = Addr.auditCompareGrid.down('pagingtoolbar');
			pagingBar.moveFirst();
		}}
	],
	getQueryParam:function(){
		var addrLevel = Ext.getCmp('addrAuditCompareLevelParam').getValue();
		var addrName = Ext.getCmp('addrAuditCompareNameParam').getValue();
		var addrNameFlag = Ext.getCmp('addrAuditCompareNameFlagParam').getValue();
		var param = {addrName:addrName,addrNameFlag:addrNameFlag};
		param.addrLevelFlag = 1;
		if(addrLevel && addrLevel > 0){
			param.addrLevel = addrLevel;
		}else{
			param.addrLevel=-1;
		}
		param.addrLevel=addrLevel;
		return param;
	},
	dockedItems: [{
	    xtype: 'pagingtoolbar',
	    store: Addr.auditCompareGridStore, 
	    dock: 'bottom',
	    displayInfo: true
	}],
	columns: [
	     { text: '地址编号',  flex: 1.5, sortable: true, dataIndex: 'addrId' ,hidden:false}
	     ,{text: '分级地址',flex: 4,dataIndex: 'str1'}
	     ,{text: '地址级别',flex: 1,dataIndex: 'addrLevel',sortable: true ,renderer:Addr.util.qtipValue}
	    ,{text: '地址全名',flex: 5,dataIndex: 'addrFullName',sortable: true ,renderer:Addr.util.qtipValue}
	    ,{text: '地址类型',flex: 2,dataIndex: 'addrType',sortable: true ,renderer:function(v){
	    	var str = v;
	    	Addr.addrTypeStore.each(function(rec) {
	    		if(v == rec.get('id')){
	    			str = rec.get('name');
	    		}
			})
			return str;
	    }}
	    
	]
});

Addr.auditCompareWin = Ext.create('Ext.Window',{
	width:600,height:400,closeAction:'close',autoDestory:true,frame:false,layout:'border',buttonAlign:'center',modal: true,
	items:Addr.auditCompareGrid,
	resetQueryParam:function(){
		Ext.getCmp('addrAuditCompareLevelParam').setValue(null);
		Ext.getCmp('addrAuditCompareNameParam').setValue(null);
		Ext.getCmp('addrAuditCompareNameFlagParam').setValue(1);
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
	checkRecord:function(cmp,index){
		var record = Addr.auditGridStore.getAt(index);
		record.set('checked4audit',cmp.checked);
		record.commit();
		var addrId = ''+record.get('addrId');
		Addr.addrAuditWin.cachedDatas[addrId] = cmp.checked ;
	},
//	selModel :new Ext.selection.CheckboxModel({ mode:'SIMPLE' }),
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
	    ,{text: '操作',flex: 1,dataIndex: 'checked4audit',renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
	    	if(record.get('status')!= '0'){
	    		return '';
	    	}
	    	var id = 'addr_audit_row_check_'+record.get('addrId') ;
	    	if(value){
	    		setTimeout(function(){
	    			Ext.get(id).dom.checked = value == true;//IE8以及以下必须得append之后才能操作.
	    		},100)
	    	}
	    	return '<input id ="' + id + '" type="checkbox" onclick="Addr.auditGrid.checkRecord(this,' + rowIndex + ')">';
	    }}
	    ,{text: '审核状态',flex: 5,dataIndex: 'status',sortable: true ,renderer:function(v){
	    	var txt = '未审核';//默认为未审核
	    	if(v =='1'){
	    		txt = '审核通过';
	    	}else if (v =='2'){
	    		txt = '审核不通过';
	    	}
	    	return txt;
	    }}
	    ,{text: '地址全名',flex: 5,dataIndex: 'addrFullName',sortable: true ,renderer:Addr.util.qtipValue}
	]
});

Addr.addrAuditWin=Ext.create('Ext.window.Window',{
	width:900,height:800,closeAction:'close',autoDestory:true,frame:false,glyph:0xf00c,title:'审核地址',layout:'border',buttonAlign:'center',
	defaults:{frame:false,border:false,region:'center'},resizable:true,maximizable : true,
	checkedRecords:{},
	getQueryParam:function(){
		var start = Ext.getCmp('addr_audit_start_time').getValue();
		var end = Ext.getCmp('addr_audit_end_time').getValue();
		var status = Ext.getCmp('addr_audit_audit_status').getValue();
		var values = {
			startTime:start ? start.toJSON().substr(0,10) : null,
			endTime:end ? end.toJSON().substr(0,10) :null,
			status:(!status || status.indexOf('ext') >=0) ? null:status
		};
		return values;
	},
	resetQueryParam:function(){
		Ext.getCmp('addr_audit_start_time').setValue(null);
		Ext.getCmp('addr_audit_end_time').setValue(null);
		Ext.getCmp('addr_audit_audit_status').setValue('');
	},
	reloadGridData:function(){
		var pagingBar = Addr.addrAuditWin.down('pagingtoolbar');
		pagingBar.moveFirst();
	},
	tbar:['&nbsp;','-',
	{xtype:'datefield',hideLabel:true,format : 'Y-m-d',emptyText:'开始日期',id:'addr_audit_start_time',listeners:{
		change:function(){
			Addr.addrAuditWin.reloadGridData();
		}
	}},
	{xtype:'datefield',hideLabel:true,format : 'Y-m-d',emptyText:'结束日期',id:'addr_audit_end_time',listeners:{
		change:function(){
			Addr.addrAuditWin.reloadGridData();
		}
	}},'-',
	{xtype:'combo',hideLabel:true,emptyText:'审核状态',id:'addr_audit_audit_status',
	store:new Ext.data.JsonStore({
		fields:['id','name'],
		data:[{id:'0',name:'未审核'},{id:'1',name:'审核通过'},{id:'2',name:'审核不通过'},{id:'',name:'全部'}]
	}),displayField:'name',valueField:'id',editable:false,
	listeners:{
		change:function(){
			Addr.addrAuditWin.reloadGridData();
		}
	}},'->',
		{glyph:0xf002,text:'地址查询',scope:this,handler:function(){
			Addr.addrAuditWin.queryForCompare();
		}}
	],
	buttons:[
			{text:'审核通过',glyph:0xf00c,tooltip:'选中的地址通过审核,成为正式地址.',handler:function(){
				Addr.addrAuditWin.doSaveResult(true);
			}}
			,{text:'审核不通过',glyph:0xf00d,tooltip:'选中的地址不能通过审核.',handler:function(){
				Addr.addrAuditWin.doSaveResult(false);
			}}
			
		],
	items:[
		Addr.auditGrid
	],cachedDatas:{},
	queryForCompare:function(){
		Addr.auditCompareWin.show();
		Addr.auditCompareWin.resetQueryParam();
		Addr.auditCompareGridStore.removeAll();
	},
	showWin:function(){
		Addr.addrAuditWin.cachedDatas = {};
		Addr.auditGridStore.removeAll();
		var pagingBar = Addr.addrAuditWin.down('pagingtoolbar');
		Addr.addrAuditWin.resetQueryParam();
		Addr.addrAuditWin.setSize(Addr.browserSize.width * 0.6,Addr.browserSize.height-100);
		Addr.addrAuditWin.show();
		pagingBar.moveFirst();
	},
	doSaveResult:function(result){
		
		var url = appBase + '/tree/audit';
		var arr = [];
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