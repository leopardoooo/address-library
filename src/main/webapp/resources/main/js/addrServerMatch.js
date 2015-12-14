Addr.matchAddrColumns=  [
	     { text: '地址编号',  flex: 1.5, hidden: true, dataIndex: 'addrId' }
	    ,{text: '地址名称',flex: 4,dataIndex: 'addrName',sortable: true ,renderer:Addr.util.qtipValue}
	    ,{text: '地址级别',flex: 2,dataIndex: 'addrLevel',sortable: true ,renderer:function(v){
	    	return '<div data-qtitle="" data-qtip="' + Addr.levelMap[''+v].levelDesc + '">' + Addr.levelMap[''+v].levelName + '</div>';
	    }}
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
	];

Addr.matchQueryGridStore=Ext.create("Ext.data.Store", {
        model: "Addr.model.Addr",
        autoLoad:false,pageSize:Addr.itemsPerPage,
        listeners:{
        	beforeLoad:function(store, operation, eOpts){
        		store.removeAll();
        		var param = store.proxy.extraParams;
        		var values = Addr.matchWin.down('form').getForm().getValues();
        		var addrLevel = Ext.isEmpty(values.addrLevel) ? [] : [values.addrLevel];
        		if(Ext.isEmpty(values.addrName) && ! values.addrLevel ){
        			return false;
        		}
        		Ext.apply(param, {
						"addrLevel" : addrLevel,
						"addrNameFlag" : Addr.matchWin.specifyQueryFlag ? 1 : 2,
						"addrName" : values.addrName
					});
        		store.proxy.extraParams = param;
        	}
        },
        proxy: {
            type: 'ajax',
            url: appBase + '/query/query',
             actionMethods: {
                create : 'POST',
                read   : 'POST', // by default GET
                update : 'POST',
                destroy: 'POST'
            },
            reader: {
                type: 'json',
                rootProperty: 'data.records',
                totalProperty: 'data.totalCount'
            }
        }
    });	

    
Addr.matchDevSearchStore = Ext.create("Ext.data.Store", {
	fields:['stdDevId','jdName'],
        autoLoad:false,
        listeners:{
        	beforeLoad:function(store, operation, eOpts){
        		store.removeAll();
        		var param = store.proxy.extraParams;
        		var values = Addr.matchWin.down('form').getForm().getValues();
        		Ext.apply(param, {
						"level" : values.stdLevel,
						"name" : values.stdDevId
					});
        		store.proxy.extraParams = param;
        	},
        	load:function(store){
        		var combo = Addr.matchWin.down('form').getForm().findField('stdDevId');
        		if(!combo){
        			Ext.Msg.show({
					    title: '错误提示',
					    message: '发现错误，请刷新后重新操作.如果仍出现这个问题，请联系管理员.',
					    multiline: false,buttons: Ext.Msg.OK,
					    icon: Ext.MessageBox.ERROR
					});
					return ;
        		}
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
            type: 'ajax',url: appBase + '/query/queryDev',
            actionMethods: { create : 'POST',  read   : 'POST',  update : 'POST', destroy: 'POST' },
            reader: {
                type: 'json',
                rootProperty: 'data'
            }
        }
    });

Addr.matchResultGridStore = Ext.create("Ext.data.Store", {
	fields:Addr.addrFields,autoLoad:false,
        listeners:{
        	beforeLoad:function(store, operation, eOpts){
        		store.removeAll();
        		var param = store.proxy.extraParams;
        		var values = Addr.matchWin.down('form').getForm().getValues();
        		if(!values.stdDevId){
        			return false;
        		}
        		Ext.apply(param, {
						"gzId" : values.stdDevId
					});
        		store.proxy.extraParams = param;
        	}
        },
        proxy: {
            type: 'ajax', url: appBase + '/query/queryRelatedAddrs',
             actionMethods: { create : 'POST',  read   : 'POST',  update : 'POST', destroy: 'POST' },
            reader: {
                type: 'json',
                rootProperty: 'data'
            }
        }
    });
    
Addr.matchWin=Ext.create('Ext.window.Window',{
	title:'设备地址关联',glyph:0xf0c1,width:900,height:600,closeAction:'hide',frame:false,border:false,layout: {type: 'vbox',pack: 'start',align: 'stretch'},
	defaults:{border:false,frame:false,flex:1},maximizable:true,
	loadRecords:function(addrs,gz){
		debugger;
		var grids = Addr.matchWin.queryBy(function(cmp){return (cmp.xtype== 'grid')});
		
		Ext.each(grids,function(grid){
			if(grid.idFlag == 'query'){
				Addr.matchWin.queryGrid = grid;
			}else if(grid.idFlag == 'result'){
				Addr.matchWin.resultGrid = grid;
			}
		});
		
		this.queryGrid.store.removeAll();
		this.resultGrid.store.removeAll();
		var form = this.down('form').getForm();
		form.reset();
		
		if(addrs && addrs.length>0){
			Ext.each(addrs,function(d){
				Addr.matchWin.queryGrid.store.add(d);
			});
		}
		
		if(gz){
			form.loadRecord(new Ext.data.Record(gz));
			form.findField('stdDevId').store.add(gz);
			form.findField('stdDevId').select(form.findField('stdDevId').store.getAt(0));
		}
	},
	listeners:{
		beforehide:function(win){
			Addr.logQueryForm.getForm().reset();
			Addr.logQueryStore.removeAll();
			var pagingBar = Addr.logWin.down('pagingtoolbar');
			pagingBar.moveFirst();
		}
	},
	items : [{
					xtype : 'form',title:'待关联信息',titleAlign:'center',bodyStyle:'padding:10px;',layout:'column',
					flex : 1,defaults:{anchor:'90%',columnWidth:.5},buttonAlign:'center',
					items:[
						{xtype:'combo',fieldLabel:'设备级别',displayField:'name',valueField:'id',
								editable:false,name:'stdLevel',hideLabel: false,multiSelect : false,allowBlank: false,
								store:Ext.create('Ext.data.JsonStore',{
									fields:['id','name'],
									data:[{"id":"JF","name":"\u673a\u623f/\u5206\u524d\u7aef"},{"id":"JJX","name":"\u4ea4\u63a5\u7bb1/\u5149\u5206\u8def\u5668"},{"id":"GZ","name":"\u5149\u7ad9/\u5149\u673a"}]
								})
							},
						{fieldLabel: '设备名称',xtype:'combo',name:'stdDevId',
							 store: Addr.matchDevSearchStore,anchor:'90%',
						        displayField: 'jdName',valueField: 'stdDevId',typeAhead: false, queryMode: 'local',
						        listeners:{
						        	specialkey: function(field, e){
						                switch(e.getKey()){
						                	case e.ENTER://回车
						                	Addr.matchDevSearchStore.load();
						                	break;
						                	default : 
						                	break;
						                }
						            },
						            change:function(combo, newValue, oldValue, eOpts){
						            	var rec = combo.store.findRecord('stdDevId',newValue);
						            	if(!rec){
						            		return;
						            	}
						            	var str = rec.data.jdAddrStr1;
						            	Addr.matchWin.down('form').getForm().findField('jdAddrStr1').setValue(str);
						            	Addr.matchResultGridStore.load();
						            }
						        },
						        triggerAction: 'all',
						        emptyText:'输入关键字敲“回车”查询地址...',
						        selectOnFocus:true
						        },
							{fieldLabel:'设备所处地址',name:'jdAddrStr1',xtype:'displayfield',columnWidth:.6},
							{xtype:'combo',fieldLabel:'地址级别',displayField:'levelName',valueField:'levelNum',
									editable:false,name:'addrLevel',hideLabel: false,multiSelect : false,allowBlank: false,
									store:Ext.create('Ext.data.JsonStore',{
										fields:Addr.addrLevelFields,
										data:levesTmp
									})
								},
								{fieldLabel:'地址名称',name:'addrName',xtype:'textfield'}
					],
					fbar:[
						{text:'匹配查询要关联的地址',glyph:0xf002,tooltip:'根据地址级别和名称匹配查询(地址名=查询条件).',handler:function(btn){
								Addr.matchWin.specifyQueryFlag = true;
								var values = Addr.matchWin.down('form').getForm().getValues();
				        		if(Ext.isEmpty(values.addrName) && ! values.addrLevel ){
				        			Alert('请至少填写地址级别或者地址名称的一项.');
				        			return false;
				        		}
				        		var pagingBar =Addr.matchWin.queryGrid.down('pagingtoolbar');
								pagingBar.moveFirst();
								Addr.matchQueryGridStore.load();
							}},
							{text:'模糊查询要关联的地址',glyph:0xf00e,tooltip:'根据地址级别和名称模糊查询(地址名称包含查询条件).',handler:function(btn){
								Addr.matchWin.specifyQueryFlag = false;
								var values = Addr.matchWin.down('form').getForm().getValues();
				        		if(Ext.isEmpty(values.addrName) && ! values.addrLevel ){
				        			Alert('请至少填写地址级别或者地址名称的一项.');
				        			return false;
				        		}
				        		var pagingBar =Addr.matchWin.queryGrid.down('pagingtoolbar');
								pagingBar.moveFirst();
								Addr.matchQueryGridStore.load();
							}},
							'->',
							{glyph:0xf0c7,text:'提交',handler:function(){
								debugger;
								var records = Addr.matchWin.resultGrid.store.queryRecords();
								if(!records || records.length==0){
									Alert('请至少选择一条记录拖拽到当前表格.');
									return false;
								}
								var form = Addr.matchWin.down('form').getForm();
								var values = form.getValues();
								if(!values.stdDevId){
									Alert('请选择要匹配的设备信息!');
									return false;
								}
								var arrays = [];
								Ext.each(records,function(rec){
									arrays.push(rec.get('addrId'));
								});
								
								var params = {
									stdDevId:values.stdDevId,
									addrIds:arrays
								};
								Addr.util.req(appBase+'/stdDev/matchAddr',params,function(data){
									if(data.code == 200 && data.message=='success'){
										Alert('操作成功！');
									}else{
										Alert('错误',data.message);
									}
									Addr.matchWin.hide();
								});
							}},
							{glyph:0xf00d,text:'关闭窗口',handler:function(){
								Addr.matchWin.hide();
							}}
					]
			},
			{flex : 3,
						layout : { type : 'hbox', pack : 'start', align : 'stretch' },defaults:{flex : 1},
						items : [{
									xtype : 'grid',title:'查询结果',titleAlign:'center',idFlag:'query',pageSize:Addr.itemsPerPage,
									selModel :new Ext.selection.CheckboxModel({ mode:'MULTI' }),
									dockedItems: [{
									    xtype: 'pagingtoolbar',
									    store: Addr.matchQueryGridStore,   // same store GridPanel is using
									    dock: 'bottom',
									    displayInfo: true
									}],
									viewConfig: {
						                plugins: {
						                    ptype: 'gridviewdragdrop', containerScroll: true,
						                    dragGroup: 'Addr.matchWin.group1',
						                    dropGroup: 'Addr.matchWin.group2'
						                },
						                listeners: {
						                    drop: function(node, data, dropRec, dropPosition) {
//						                        var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
						                    }
						                }
									},
									columns : Addr.matchAddrColumns,
									store:Addr.matchQueryGridStore
								}, 
						{
						title : '已关联地址',border:2,frame:false,bodyStyle:'paddint-left:10px',xtype:'grid',titleAlign:'center',idFlag:'result',
						layout : 'fit',selModel :new Ext.selection.CheckboxModel({ mode:'MULTI' }),
						columns : Addr.matchAddrColumns,
						viewConfig: {
				                plugins: {
				                    ptype: 'gridviewdragdrop',
				                    containerScroll: true,
				                    dragGroup: 'Addr.matchWin.group2',
				                    dropGroup: 'Addr.matchWin.group1'
				                },
				                listeners: {
				                    drop: function(node, data, dropRec, dropPosition) {
//				                        var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
				                    }
				                }
				            },
						store:Addr.matchResultGridStore
					}
					]
			}

		]
});