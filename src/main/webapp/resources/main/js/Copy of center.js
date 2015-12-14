	/**
	 * 查询表单.
	 */
	Addr.queryForm = Ext.create('Ext.form.FormPanel',{
		resetForm:function(){
			this.getForm().reset();
			this.getForm().findField('addrLevel').store.clearFilter();
		},
		checkQueryForm:function(values){
			var valid = false;
			for (var key in values) {
				if(key.indexOf('Flag') < 0  && !Ext.isEmpty(values[key]) ){
					return true;
				}
			}
			return valid;
		},
		doQuery:function(){
			var values = this.getForm().getValues();
			var valid = this.checkQueryForm(values);
			
			Addr.queryGridStore.currentPage = 1;
			if(valid){
				Addr.queryGridStore.load();//加载参数的事情在store 的beforeload事件里处理.
			}else{
				Alert('提示','查询表单至少要填写一项');
			}
		},
		frame:false,region:'north',id:'queryForm',buttonAlign:'center',
		buttons:[
		         {text:'重置条件',glyph:0xf12d,scope:this,handler:function(){
		        	 Addr.queryForm.resetForm();
		         }},
		         {text:'查询地址',glyph:0xf002,scope:this,handler:function(){
		        	 Addr.queryForm.doQuery();
		         }}
		],
		bodyStyle:'text-align:center;padding:10px 50px 10px 50px;',
		align:'center',labelAlign:"center",
		layout:'column',defaults:{layout:'form',border:false,xtype:'panel',columnWidth:.9},
		items:[
		{
			columnWidth:.45,items:[
			                      {xtype:'combo',hideLabel: false,name:'addrLevelFlag',anchor:'90%',editable:false,
			                    	  fieldLabel:'地址级别',value:'2',
			                    	  displayField: 'name',valueField: 'id',typeAhead: false, queryMode: 'local',
			                    	  store:Ext.create('Ext.data.Store',{
			                    		  fields:['id','name'],
			                    		  data:[{id:'1',name:'等于    (=)'},
			                    		        {id:'2',name:'包含    (like)'}]
			                    	  })
			                      }
			                      ]
		},
		{columnWidth:.50,items:[
		      {xtype:'textfield',hideLabel: true,name:'addrLevel'}//fieldLabel:'地址级别',
		]},
		{
			columnWidth:.45,items:[
			                      {xtype:'combo',hideLabel: false,name:'addrNameFlag',anchor:'80%',editable:false,
			                    	  fieldLabel:'地址名称',
			                    	  displayField: 'name',valueField: 'id',typeAhead: false, queryMode: 'local',value:'1',
			                    	  store:Ext.create('Ext.data.Store',{
			                    		  fields:['id','name'],
			                    		  data:[{id:'1',name:'等于    (=)'},
			                    		        {id:'2',name:'包含    (like)'}]
			                    	  })
			                      }
			                      ]
		},
		{
			columnWidth:.50,items:[ {xtype:'textfield',hideLabel: true,name:'addrName',value:''} ]
		},
		{
			columnWidth:.45,items:[
			                      {xtype:'combo',hideLabel: false,name:'addrParentFlag',anchor:'80%',editable:false,
			                    	  fieldLabel:'上级地址',value:'1',
			                    	  displayField: 'name',valueField: 'id',typeAhead: false, queryMode: 'local',
			                    	  store:Ext.create('Ext.data.Store',{
			                    		  fields:['id','name'],
			                    		  data:[{id:'1',name:'等于    (=)'},
			                    		        {id:'2',name:'包含    (like)'}]
			                    	  })
			                      }
			                      ]
		},
		{columnWidth:.50,items:[
		      {xtype:'textfield',hideLabel: true,name:'addrParent'}//fieldLabel:'上级地址',
		]},
		{columnWidth:.95,items:[
		         {xtype:'combo',fieldLabel:'地址类型',hideLabel: false,multiSelect:true,name:'addrType',displayField:'name',valueField:'id',editable:false,store:Addr.addrTypeStore}
		]}
		
		]
	});

Addr.queryGridStore = Ext.create('Ext.data.JsonStore', {
	fields : Addr.addrFields,
	pageSize:Addr.itemsPerPage,
	actionMethods:'POST',
	listeners:{
		beforeLoad:function( store, operation, eOpts ){
    		store.removeAll();
    		var extra = Addr.queryForm.getForm().getValues();
    		var param = store.proxy.extraParams;
			for(var key in extra){
				param[key] = extra[key];
			}
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

Addr.queryResultGrid = Ext.create('Ext.grid.GridPanel',{
	bodyStyle:'padding:5px 0px 5px 0px;',glyph:0xf0ce,
	region:'center',titleAlign:'center',title:'查询结果',rowLines : true,
	store:Addr.queryGridStore,
	viewConfig:{
       listeners:{
       		render:function(v){
       			Ext.require(['resources.main.js.changeMerge',//变更父级,地址合并
								'resources.main.js.addBatch',//批量添加
								'resources.main.js.addrServerMatch',//设备地址关联
								'resources.main.js.addrLog'//日志
								]);//
       		}
       }
	},
	listeners:{
		containercontextmenu:function( grid, event, eOpts ){
			if(event.preventDefault){
				event.preventDefault();
			}else if(event.stopPropagation){
				event.stopPropagation();
			}
//			Addr.queryResultGrid.contextMenu.showAt(event.getXY());
		},
		rowcontextmenu:function( grid, record, tr, rowIndex, event, eOpts  ){
//			Alert('IE?',Ext.browser.userAgent + '    ' + Ext.browser.engineName);
			Addr.detailPanel.loadRecord(record);
			if(event.preventDefault){
				event.preventDefault();
			}else if(event.stopPropagation){
				event.stopPropagation();
			}
			Addr.contextMenuHandler.centerGrid = true;
			Addr.queryResultGrid.contextMenu.showAt(event.getXY());
			Addr.contextMenuHandler.filterMenuItems(Addr.queryResultGrid.contextMenu);
			return false;
		}
	},
	contextMenu:Ext.create('Ext.menu.Menu',{
        items:[
               {
		            text:'数据浏览(子集)',glyph:0xf103,
		            handler:function(){
		            	var rec = Addr.queryResultGrid.selModel.getSelection();
		            	if(rec.length != 1){
		            		Alert('提示','请选择且仅选择一条记录！');
		            		return ;
		            	}
		            	Addr.contextMenuHandler.viewChildrenData(rec[0].get('addrId'));
		            }
		        },
		        {
	                text:'地址合并',glyph:0xf0c1,needFielterByFgs:true,
	                handler:function(){//地址合并
	                	var rec = Addr.queryResultGrid.selModel.getSelection();
	                	if(rec.length < 1){
		            		Alert('提示','请至少选择一条记录！');
		            		return ;
		            	}
		            	for(var index =0;index<rec.length;index++){
		            		var r = rec[index];
		            		if(r.get('status')!='ACTIVE'){
		            			Confirm('提醒','有未通过审核的地址,是否确认继续操作？',function(txt){
		            				if(txt != 'yes'){
		            					return;
		            				}
		            				Addr.contextMenuHandler.addrMerge(rec);
		            			});
		            			return;
		            		}
		            	}
						Addr.contextMenuHandler.addrMerge(rec);
	                }
		        },
		        {
	                text:'变更父级',glyph:0xf113,needFielterByFgs:true,
	                handler:function(){//删除事件
	                	var rec = Addr.queryResultGrid.selModel.getSelection();
	                	if(rec.length != 1){
		            		Alert('提示','请选择且仅选择一条记录！');
		            		return ;
		            	}
		            	if(rec[0].data.status != 'ACTIVE'){
		            		Alert('提示','请先审核地址再做该操作！');
		            		return ;
		            	}
						Addr.contextMenuHandler.changeParent(rec[0]);
	                }
		        },
		        {
					text : '新增地址',needFielterByFgs:true,
					glyph : 0xf0c7,
					handler : function() {
						var rec = Addr.queryResultGrid.selModel.getSelection();
		            	if(rec.length != 1){
		            		Alert('提示','请选择且仅选择一条记录！');
		            		return ;
		            	}
		            	if(rec[0].data.status != 'ACTIVE'){
		            		Alert('提示','请先审核地址再做该操作！');
		            		return ;
		            	}
		            	Addr.contextMenuHandler.addNewAddr(rec[0].data);
					}
				},
		        {
	                text:'批量新增地址',glyph:0xf067,needFielterByFgs:true,
	                handler:function(){
	                	var rec = Addr.queryResultGrid.selModel.getSelection();
		            	if(rec.length != 1){
		            		Alert('提示','请选择且仅选择一条记录！');
		            		return ;
		            	}
		            	if(rec[0].data.status != 'ACTIVE'){
		            		Alert('提示','请先审核地址再做该操作！');
		            		return ;
		            	}
						Addr.contextMenuHandler.addNewAddrBatch(rec[0].data);
	                }
                },
                {
					text : '修改地址',needFielterByFgs:true,
					glyph : 0xf044,
					handler : function() {
						var rec = Addr.queryResultGrid.selModel.getSelection();
		            	if(rec.length != 1){
		            		Alert('提示','请选择且仅选择一条记录！');
		            		return ;
		            	}
		            	if(rec[0].data.status != 'ACTIVE'){
		            		Alert('提示','请先审核地址再做修改！');
		            		return ;
		            	}
						Addr.contextMenuHandler.updateAddr(rec[0].data);
					}
				},
                {
					text : '删除地址',needFielterByFgs:true,
					glyph : 0xf00d,
					handler : function() {
						var rec = Addr.queryResultGrid.selModel.getSelection();
		            	if(rec.length != 1){
		            		Alert('提示','请选择且仅选择一条记录！');
		            		return ;
		            	}
						Addr.contextMenuHandler.removeAddr(rec[0]);
					}
				},
                {
					text : '设备地址关联',needFielterByFgs:true,
					glyph : 0xf0c1,
					handler : function() {
						var rec = Addr.queryResultGrid.selModel.getSelection();
		            	if(rec.length < 1){
		            		Alert('提示','请选择至少一条记录！');
		            		return ;
		            	}
		            	for(var index =0;index<rec.length;index++){
		            		var r = rec[index];
		            		if(r.get('status')!='ACTIVE'){
		            			Confirm('提醒','有未通过审核的地址,是否确认继续操作？',function(txt){
		            				if(txt != 'yes'){
		            					return;
		            				}
		            				Addr.contextMenuHandler.addrDevMatch(rec);
		            			});
		            			return;
		            		}
		            	}
						Addr.contextMenuHandler.addrDevMatch(rec);
					}
				},
                {
					text : '节点日志',
					glyph : 0xf1da,
					handler : function() {
						var rec = Addr.queryResultGrid.selModel.getSelection();
		            	if(rec.length != 1){
		            		Alert('提示','请选择且仅选择一条记录！');
		            		return ;
		            	}
						Addr.contextMenuHandler.viewLog(rec[0].data);
					}
				},
                {
					text : '资源树定位',
					glyph : 0xf13d,
					handler : function() {
						var rec = Addr.queryResultGrid.selModel.getSelection();
		            	if(rec.length != 1){
		            		Alert('提示','请选择且仅选择一条记录！');
		            		return ;
		            	}
						
						Addr.contextMenuHandler.findInTree(rec[0].data);
					}
				}
                ],
        listeners:{
        	blur:function(menu){
        		menu.hide();
        	}
        }
    }),
	selModel :new Ext.selection.CheckboxModel({ mode:'SIMPLE' }),
	dockedItems: [{
	    xtype: 'pagingtoolbar',
	    store: Addr.queryGridStore,   // same store GridPanel is using
	    dock: 'bottom',
	    displayInfo: true
	}],
	columns: [
	     { text: '地址编号',align:'center' ,  flex: 1.5, sortable: true, dataIndex: 'addrId' }
	     ,{text: '上级地址',flex: 4,dataIndex: 'addrParent',sortable: true  ,
	    	 renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
	    		 var str1 = record.data.str1;
	    		 var arr = str1.split('/');
	    		 var rst = arr.length > 1 ? arr[arr.length -2] : '广西';
	    		 return Addr.util.qtipValue(rst);
		    }}
	    ,{text: '地址名称',flex: 4,dataIndex: 'addrName',sortable: true ,renderer:Addr.util.qtipValue}
	    ,{text: '地址级别',flex: 1,dataIndex: 'addrLevel',sortable: true ,align:'center'}
	    ,{text: '地址类型',flex: 2,dataIndex: 'addrType',sortable: true ,align:'center',renderer:function(v){
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

