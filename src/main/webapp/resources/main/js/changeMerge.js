 Addr.addrParentSearchStore = Ext.create("Ext.data.Store", {
        model: "Addr.model.Addr",
        autoLoad:false,pageSize:Addr.itemsPerPage,
        listeners:{
        	beforeLoad:function(store, operation, eOpts){
        		store.removeAll();
        		var param = store.proxy.extraParams;
        		var values = Addr.changeParentWin.down('form').getForm().getValues();
				var startLevel = Addr.changeParentWin.formerAddrData.addrLevel;//如果是变更上级,则 level -1 
				startLevel -= 1; 
				var sameParent = false;
				var q = values.addrName2;
				var currentId = values.addrId;
				
        		Ext.apply(param, {
						"sl" : startLevel,
						"sameParent" : sameParent,
						countyId:Addr.changeParentWin.formerAddrData.countyId,
						"q" : q,
						"currentId" : currentId
					});
        		store.proxy.extraParams = param;
        	},
        	load:function(store){
        		var combo = Addr.changeParentWin.down('form').getForm().findField('addrName2');
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
            type: 'ajax',
            url: appBase + '/tree/searchParentLevelAddrs',
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


/**
 * 变更父级和合并地址.
 */
Addr.changeParentWin = new Ext.Window({
			layout : 'fit',width : 480,height : 220,border:false,closeAction:'hide',buttonAlign:'center',title:'变更上级',
			bodyStyle:'padding:20px;',
			loadRecord : function(record,merge) {
				var form = this.down('form').getForm();
				var addr2 = form.findField('addrName2');
				form.reset();
				form.loadRecord(record);
				this.formerAddrData = record.data;
				this.mergeFlag = false;
				this.actionUrl = appBase + '/tree/changeParent' ;
			},
			doSearch:function(combo){
				combo.store.load();
			},
			doSave:function(){
				var values = this.down('form').getForm().getValues();
				var param = {};
				param[ 'addrId'] = values.addrId;
				param['pid'] = values.addrName2;
				Addr.util.req(this.actionUrl,param,function(data){
					var msg = '操作成功';
					if(data.code == 200){
						Addr.changeParentWin.hide();
						Addr.contextMenuHandler.refreshAfterOperation();
						Addr.treeGrid.expandRoot();
						Alert('操作成功!');
						return;
					}
					Alert('错误',data.message,function(){
						Addr.changeParentWin.hide();
					});
					
				});
			},
			doCancel:function(){
				var form = this.down('form').getForm();
				form.findField('addrName2').store.removeAll();
				form.reset();
				this.hide();
			},
			buttons:[
				{glyph:0xf0c7,text:'保存',handler:function(){Addr.changeParentWin.doSave()}},
				{glyph:0xf00d,text:'取消',handler:function(){Addr.changeParentWin.doCancel()}}
			],
			items : [{
						xtype : 'form',border:false,
						layout : 'form',
						items : [
						{fieldLabel: '地址ID',xtype:'hiddenfield',name:'addrId'},
						{fieldLabel: '地址名称',xtype:'displayfield',name:'addrName'},
						{fieldLabel: '新上级地址',xtype:'combo',name:'addrName2',
							 store: Addr.addrParentSearchStore,
						        pageSize:Addr.itemsPerPage,anchor:'90%',
						        displayField: 'str1',valueField: 'addrId',typeAhead: false, queryMode: 'local',
						        listeners:{
						        	specialkey: function(field, e){
						                switch(e.getKey()){
						                	case e.ENTER://回车
						                		Addr.changeParentWin.doSearch(field);
						                	break;
						                	default : 
						                	break;
						                }
						            }
						        },
						        triggerAction: 'all',
						        emptyText:'输入关键字敲“回车”查询地址...',
						        selectOnFocus:true
						        }
						]
					}]
		});
		
Addr.mergeSourceStore=Ext.create('Ext.data.JsonStore',{
	fields :  ['addrId','addrName','addrLevel','str1'],
	pageSize:Addr.itemsPerPage,
	actionMethods:'POST',
	listeners:{
		beforeLoad:function( store, operation, eOpts ){
    		store.removeAll();
    		var values = Addr.mergeWin.sourcePanel.down('form').getForm().getValues();
			values.addrLevel = [values.addrLevel];
			values.addrNameFlag = Addr.mergeWin.sourcePanel.down('form').blurQueryFlag ? 2 : 1;
    		var param = store.proxy.extraParams;
			for(var key in values){
				param[key] = values[key];
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

Addr.mergeTargetStore=Ext.create('Ext.data.JsonStore',{
	fields :  ['addrId','addrName','addrLevel','str1'],
	pageSize:Addr.itemsPerPage,
	actionMethods:'POST',
	listeners:{
		beforeLoad:function( store, operation, eOpts ){
    		store.removeAll();
    		var values = Addr.mergeWin.targetPanel.down('form').getForm().getValues();
    		values.addrLevel = [values.addrLevel];
			values.addrNameFlag = Addr.mergeWin.targetPanel.down('form').blurQueryFlag ? 2 : 1;
    		var param = store.proxy.extraParams;
			for(var key in values){
				param[key] = values[key];
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
		
Addr.mergeWin = Ext.create('Ext.Window',{
			layout : 'hbox',width : 480,height : 220,border:false,closeAction:'hide',buttonAlign:'center',title:'地址合并',
			defaults:{border:false,flex:2,frame:true,width:'100%',layout:'vbox',height:'100%'},
			bodyStyle:'padding:20px;',
			loadRecords : function(records) {
				this.actionUrl = appBase + '/tree/merge' ;
				if(records && records.length>0){
					this.recordsLoaded = records;
				}
			},
			listeners:{
				show:function(win){
					win.setSize(Addr.browserSize.width * 0.6,Addr.browserSize.height - 100);
					win.center();
					var first = win.items.first();
					var fs = first.sourceDataPanel;
					var second = first.nextSibling();
					var ss = second.sourceDataPanel;
					if(fs && ss){ throw new Error('代码错误.'); }
					win[fs ? 'sourcePanel' : 'targetPanel'] = first;
					win[ss ? 'sourcePanel' : 'targetPanel'] = second;
					first.down('grid').store.removeAll();
					second.down('grid').store.removeAll();
					
					if(this.recordsLoaded){
						win.sourcePanel.down('form').loadRecord(new Ext.data.Record({addrLevel:this.recordsLoaded[0].get('addrLevel')}));
						
//						win.sourcePanel.down('form').getForm().findField('addrLevel').disable();
						win.targetPanel.down('form').loadRecord(new Ext.data.Record({addrLevel:this.recordsLoaded[0].get('addrLevel')}));
//						win.targetPanel.down('form').getForm().findField('addrLevel').disable();
						win.sourcePanel.down('grid').store.add(this.recordsLoaded);
					}
					
				}
			},
			doSave:function(){
				var target = this.targetPanel.down('grid').selModel.getSelection();
				if(target.length!=1){
					Alert('请选择一个 ,且只能选择一个 合并的目标.');
					return;
				}
				target = target[0];
				var sources = this.sourcePanel.down('grid').store.queryRecords();
				if(sources.length<1){
					Alert('请至少选择一个地址来合并.');
					return ;
				}
				var targetLevel = target.get('addrLevel');
				var targetId = target.get('addrId');
				var mergered = [];
				for(var index =0;index<sources.length;index++){
					var src = sources[index];
					if(src.get('addrLevel')!=targetLevel ){
						Alert('合并的来源地址 第 ' + (index+1) + ' 行 与目标地址的级别不一致！' );
						return;
					}
					var sid = src.get('addrId');
					if(sid != targetId){//重复的数据自动过滤...
						mergered.push(sid);
					}
				}
				
				var param = {merger:targetId,mergered:mergered};
				Addr.util.req(this.actionUrl,param,function(data){
					var msg = '操作成功';
					if(data.code == 200){
						Addr.mergeWin.hide();
						Addr.contextMenuHandler.refreshAfterOperation();
						Addr.treeGrid.expandRoot();
						Alert(msg);
						return;
					}
					Alert('错误',data.message,function(){
						Addr.mergeWin.hide();
					});
					
				});
			},
			doCancel:function(){
				this.sourcePanel.down('form').getForm().reset();
				this.targetPanel.down('form').getForm().reset();
				this.sourcePanel.down('grid').store.removeAll();
				this.targetPanel.down('grid').store.removeAll();
				this.hide();
			},
			buttons:[
				{glyph:0xf0c7,text:'合并',handler:function(){Addr.mergeWin.doSave()}},
				{glyph:0xf00d,text:'取消',handler:function(){Addr.mergeWin.doCancel()}}
			],
			items : [
			{title:'被合并地址',defaults:{layout:'fit',width:'100%',frame:true,border:false,flex:2},titleAlign:'center',
				sourceDataPanel:true,
				items:[{
						xtype : 'form',layout : 'form',buttonAlign:'center',
						doSearch:function(blur){
							this.blurQueryFlag = blur;
							var searchWin = Ext.create('Ext.Window',{
								layout:'fit',border:false,defaults:{border:false},autoDestory:true,closeAction:'close',buttonAlign:'center',
								listeners:{
									show:function(win){
										win.setSize(Addr.browserSize.width * 0.5,Addr.browserSize.height - 150);
										win.center();
									}
								},
								buttons:[
								{text:'选中',handler:function(btn){
									var records = btn.up('window').down('grid').selModel.getSelection();
									if(!records || records.length< 1){
										Alert('请至少选择一条记录.');
										return;
									}
									
									debugger;
									var store = Addr.mergeWin.sourcePanel.down('grid').store; 
									for(var index =0;index<records.length;index++){
										var rec = new Ext.data.Record(records[index].data);
										var idx = store.find('addrId',rec.get('addrId'));
										if(idx < 0){
											store.add(rec);
										}
									}
									btn.up('window').close();
								}},
								{text:'取消',handler:function(btn){
									btn.up('window').close();
								}}
								],
								items:{
										xtype:'grid',layout:'fit',selModel :new Ext.selection.CheckboxModel({ mode:'MULTI' }),
										dockedItems: [{
										    xtype: 'pagingtoolbar',
										    store: Addr.mergeSourceStore,
										    dock: 'bottom',
										    displayInfo: true
										}],
										store:Addr.mergeSourceStore,
										columns:[
											     { text: '地址编号',  flex: 1, hidden: true, dataIndex: 'addrId' }
											     ,{text: '地址类型',flex: 2,dataIndex: 'addrType',sortable: true ,renderer:function(v){
											    	var str = v;
											    	Addr.addrTypeStore.each(function(rec) {
											    		if(v == rec.get('id')){
											    			str = rec.get('name');
											    		}
													})
													return str;
											    }}
											    ,{text: '地址名称',flex: 2,dataIndex: 'addrName',sortable: true ,renderer:Addr.util.qtipValue}
											    ,{text: '地址级别',flex: 2,dataIndex: 'addrLevel',sortable: true ,renderer:function(v){
											    	return '<div data-qtitle="" data-qtip="' + Addr.levelMap[''+v].levelDesc + '">' + Addr.levelMap[''+v].levelName + '</div>';
											    }}
									    ]
								}
							});
							searchWin.show();
							Addr.mergeSourceStore.load();
						},
						buttons:[{text:'匹配查询',glyph:0xf002,tooltip:'根据地址级别和名称匹配查询(地址名=查询条件).',handler:function(btn){
								btn.up('form').doSearch(false);
							}},
							{text:'模糊查询',glyph:0xf00e,tooltip:'根据地址级别和名称模糊查询(地址名称包含查询条件).',handler:function(btn){
								btn.up('form').doSearch(btn,true);
							}},
							{text:'删除',glyph:0xf00e,tooltip:'根据地址级别和名称模糊查询(地址名称包含查询条件).',handler:function(btn){
								var grid = Addr.mergeWin.sourcePanel.down('grid');
								var store = grid.store;
								var sels = grid.selModel.getSelection();
								store.remove(sels);
							}}
							],
						items : [
						{xtype:'combo',fieldLabel:'地址级别',displayField:'levelName',valueField:'levelNum',
								editable:false,name:'addrLevel',hideLabel: false,multiSelect : false,allowBlank: false,
								store:Ext.create('Ext.data.JsonStore',{
									fields:Addr.addrLevelFields,
									data:levesTmp
								})
							},
				        {fieldLabel: '地址名称',xtype:'textfield',name:'addrName'}
						]
					},
					{
						xtype:'grid',layout:'fit',flex:5,selModel :new Ext.selection.CheckboxModel({ mode:'MULTI' }),
						store:Ext.create('Ext.data.Store',{
							fields : ['addrId','addrName','addrLevel','str1'],
							data:[]
						}),
						columns:[
							     { text: '地址编号',  flex: 1, hidden: true, dataIndex: 'addrId' }
							     ,{text: '地址类型',flex: 2,dataIndex: 'addrType',sortable: true ,renderer:function(v){
							    	var str = v;
							    	Addr.addrTypeStore.each(function(rec) {
							    		if(v == rec.get('id')){
							    			str = rec.get('name');
							    		}
									})
									return str;
							    }}
							    ,{text: '地址名称',flex: 2,dataIndex: 'addrName',sortable: true ,renderer:Addr.util.qtipValue}
							    ,{text: '地址级别',flex: 2,dataIndex: 'addrLevel',sortable: true ,renderer:function(v){
							    	return '<div data-qtitle="" data-qtip="' + Addr.levelMap[''+v].levelDesc + '">' + Addr.levelMap[''+v].levelName + '</div>';
							    }}
					    ]
					}]},
					{title:'目标地址',defaults:{layout:'fit',width:'100%',frame:true,border:false,flex:2},titleAlign:'center',
					sourceDataPanel:false,
				items:[{
						xtype : 'form',layout : 'form',buttonAlign:'center',
						doSearch:function(blur){
							this.blurQueryFlag = blur;
							Addr.mergeTargetStore.load();
						},
						buttons:[{text:'匹配查询',glyph:0xf002,tooltip:'根据地址级别和名称匹配查询(地址名=查询条件).',handler:function(btn){
								btn.up('form').doSearch(false);
							}},
							{text:'模糊查询',glyph:0xf00e,tooltip:'根据地址级别和名称模糊查询(地址名称包含查询条件).',handler:function(btn){
								btn.up('form').doSearch(btn,true);
							}}],
						items : [
							{xtype:'combo',fieldLabel:'地址级别',displayField:'levelName',valueField:'levelNum',
								editable:false,name:'addrLevel',hideLabel: false,multiSelect : false,allowBlank: false,
								store:Ext.create('Ext.data.JsonStore',{
									fields:Addr.addrLevelFields,
									data:levesTmp
								})
							},
				        {fieldLabel: '地址名称',xtype:'textfield',name:'addrName'}
						]
					},
					{
						xtype:'grid',layout:'fit',flex:5,selModel :new Ext.selection.CheckboxModel(),
						dockedItems: [{
						    xtype: 'pagingtoolbar',
						    store: Addr.mergeTargetStore,
						    dock: 'bottom',
						    displayInfo: true
						}],
						store:Addr.mergeTargetStore,
						columns:[
							     { text: '地址编号',  flex: 1, hidden: true, dataIndex: 'addrId' }
							     ,{text: '地址类型',flex: 2,dataIndex: 'addrType',sortable: true ,renderer:function(v){
							    	var str = v;
							    	Addr.addrTypeStore.each(function(rec) {
							    		if(v == rec.get('id')){
							    			str = rec.get('name');
							    		}
									})
									return str;
							    }}
							    ,{text: '地址名称',flex: 2,dataIndex: 'addrName',sortable: true ,renderer:Addr.util.qtipValue}
							    ,{text: '地址级别',flex: 2,dataIndex: 'addrLevel',sortable: true ,renderer:function(v){
							    	return '<div data-qtitle="" data-qtip="' + Addr.levelMap[''+v].levelDesc + '">' + Addr.levelMap[''+v].levelName + '</div>';
							    }}
					    ]
					}]}
					
					]
});