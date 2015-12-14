Addr.devAddrComboStore = Ext.create("Ext.data.Store", {
        model: "Addr.model.Addr",
        autoLoad:false,pageSize:Addr.itemsPerPage,
        listeners:{
        	beforeLoad:function(store, operation, eOpts){
        		store.removeAll();
        		var param = store.proxy.extraParams;
        		
        		//jdAddrLevel
        		var values = Addr.stdDevFormWin.down('form').getForm().getValues();
				var startLevel = values.jdAddrLevel; 
				var sameParent = values.sameParentFlag || false;
				var q = values.jdAddrId;
				var currentId = values.addrId;
				var countyId = Addr.stdDevFormWin.down('form').getForm().findField('countyId').getValue();
        		Ext.apply(param, {
						"sl" : startLevel,
						"sameParent" : false,
						'countyId':countyId,
						"q" : q,
						"currentId" : currentId
					});
        		store.proxy.extraParams = param;
        	},
        	load:function(store){
        		var combo = Addr.stdDevFormWin.down('form').getForm().findField('jdAddrId');
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

Addr.stdDevFormWin = Ext.create('Ext.window.Window',{
	title: '编辑地址', height: 260, width: 480, layout: 'fit',closeAction:'hide',frame:true,buttonAlign:'center',baseUrl:appBase + '/stdDev/saveStdDev',
	loadRecord:function(data,update){
		if(data == null){
			update = false;
			data = {};
		}
		var form = this.down('form');
		form.getForm().reset();
		
		var dataCopy = {};
		var lev = this.getNextLevel(data.stdLevel );
		if(update == true){
			for(var key in data){
				dataCopy[key] = Ext.isEmpty(data[key]) ? null:data[key];
			}
			lev = this.getCurrentLevel(data.stdLevel );
			dataCopy.stdLevelName = lev.name;
			this.setTitle('编辑');
		}else{
			dataCopy = {stdDevId:null,pid:data.stdDevId || 0,jdName:null,stdLevel:lev.id,stdLevelName:lev.name,countyId:data.countyId};
			this.setTitle('新增');
		}
		//设置 countyId 
		var combo = form.getForm().findField('countyId2');
		var store = combo.store;
		store.removeAll();
		var countyIds = Addr.util.getSelectAbleCounties();
		store.add(countyIds);
		form.loadRecord(new Ext.data.Record(dataCopy));
		if(update == true){
			//给特别的几个field赋值
			var addrId = form.getForm().findField('jdAddrId');
			addrId.setValue(data.jdAddrId);
			addrId.setRawValue(data.jdAddrStr1);
			var jdAddrLevel = form.getForm().findField('jdAddrLevel');
			jdAddrLevel.setValue(data.jdAddrStr1.split('/').length);
		}
		if(dataCopy.countyId){
			var cnt = Addr.singleCountyMap[dataCopy.countyId];
			combo.setValue(cnt.countyId);
			combo.setRawValue(cnt.countyName);
			combo.setDisabled(true);
		}else{
			combo.setDisabled(false);
		}
	},
	levelAllMap:{
		JF:{id:'JF',name:'机房/分前端'},
		JJX:{id:'JJX',name:'交接箱/光分路器'},
		GZ:{id:'GZ',name:'光站/光机'}
	},
	getCurrentLevel:function(level){
		return this.levelAllMap[level];
	},
	getNextLevel:function(currentLevel){
		if(Ext.isEmpty(currentLevel)){
			return {id:'JF',name:'机房/分前端'};
		}
		if(currentLevel == 'JF'){
			return{id:'JJX',name:'交接箱/光分路器'};
		}
		if(currentLevel == 'JJX'){
			return {id:'GZ',name:'光站/光机'};
		}
	},
	doSearch:function(combo){
		combo.store.load();
	},
    items: {
				xtype:'form',bodyStyle: 'padding:20px 30px 30px 40px;',buttonAlign:'center',
				layout: 'anchor',defaults: {anchor: '90%'},
				items:[
						{fieldLabel: '名称',xtype:'textfield',allowBlank: false,name: 'jdName'},
						{fieldLabel: '光节点ID',xtype:'hidden',name: 'stdDevId' },
						{fieldLabel: '父级ID',xtype:'hidden',name: 'pid',value:0 },
						{fieldLabel: '级别',xtype:'hidden',allowBlank: false,name: 'stdLevel' },
						{fieldLabel: '级别',xtype:'displayfield',name: 'stdLevelName' },
						{fieldLabel: '分公司',xtype:'hidden',name: 'countyId' },
						{fieldLabel: '分公司',xtype:'combo',name: 'countyId2',displayField:'countyName',valueField:'cuntyId',multiSelect : false,allowBlank: false,editable:false,
						listeners:{
							change:function(combo, newValue, oldValue, eOpts ){
								var countyId = Addr.stdDevFormWin.down('form').getForm().findField('countyId');
								countyId.setValue(newValue);
							},
							select :function( combo, records, eOpts ){
								var countyId = Addr.stdDevFormWin.down('form').getForm().findField('countyId');
								countyId.setValue(records.get('countyId'));
							}
						},
						store: Ext.create('Ext.data.JsonStore',{
							fields:['cuntyId','countyName'],
							data:[]
						})
						},
						//'cuntyId','countyName'
						{xtype:'combo',fieldLabel:'所处地址级别',displayField:'levelName',valueField:'levelNum',
							editable:false,name:'jdAddrLevel',hideLabel: false,multiSelect : false,allowBlank: false,
							store:Ext.create('Ext.data.JsonStore',{
								fields:Addr.addrLevelFields,
								data:levesTmp,
								proxy:{
									type:'memory',
									reader:{
										
									}
								}
							})
						},
						
				    	{xtype:'combo',fieldLabel: '所处地址', name: 'jdAddrId', allowBlank: false,store:Addr.devAddrComboStore,typeAhead:false, displayField: 'str1',valueField: 'addrId',
				    	pageSize:Addr.itemsPerPage,anchor:'90%',
				    		listeners:{
						        	specialkey: function(field, e){
						                switch(e.getKey()){
						                	case e.ENTER://回车
						                		var countyId = Addr.stdDevFormWin.down('form').getForm().findField('countyId').getValue();
						                		if(!countyId){
						                			Alert('请先选择分公司！');
						                			return false;
						                		}
						                		debugger;
						                		Addr.stdDevFormWin.doSearch(field);
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
				],
			    fbar: [{
			        text: '取消',glyph:0xf00d,
			        handler: function() {
			            this.up('form').getForm().reset();
			            Addr.stdDevFormWin.hide();
			        }
			    }, {
			        text: '确定',glyph:0xf0c7,
			        formBind: true, //only enabled once the form is valid
			        handler: function() {
			            var form = this.up('form').getForm();
			            var values = form.getValues();
			            if (this.up ('form').isValid()) {
			            	Addr.util.req(Addr.stdDevFormWin.baseUrl ,values,function(data){
			            		if(data.code == 200 && data.message == 'success'){
			            			Alert('操作成功!');
			            			Addr.stdDevPanel.refreshTree();
			            			Addr.stdDevFormWin.hide();
			            		}else{
			            			Alert('操作失败',data.message);
			            		}
			            	})
			            }
			            
			        }
			    }]
			
    }
});

Addr.devTreeStore = Ext.create("Ext.data.TreeStore", {
	model : 'Addr.model.StdDev',
	pageSize:Addr.itemsPerPage,
	listeners:{
		load:function(store,records){
			Ext.each(records, function(rec) {
				if(rec.get('stdLevel') == 'GZ'){
					rec.set('leaf',true);
				}
				});
				var tmpArr = Addr.tmpVars.stdNode2Expand; 
				if(tmpArr && tmpArr.length > 0){
					Addr.stdDevPanel.findInTree(Addr.tmpVars.stdNode2Expand);
				}
		}
	},
    proxy: {
        type: 'ajax',
        url: appBase + '/query/queryStdDevTree',
        reader: {
            type: 'json'
            ,rootProperty: 'data'
        }
    }
});


Addr.stdDevPanel =  Ext.create('Ext.tree.Panel', {
	findInTree:function(data){//资源树定位
		if(!data){
			return;
		}
		Addr.stdDevPanel.fireEvent('itemexpand',Addr.stdDevPanel);//激发expand 事件
	},
	orderInTab:1,
    title:'光站设备',titleAlign :'left',singleExpand : true,//glyph:0xf19c,
    rowLines :true,columnLines :true ,
    pageSize:Addr.itemsPerPage,reserveScrollbar: true,
    loadMask: true,useArrows: false,rootVisible: false,store: Addr.devTreeStore,animate: false,
    expandRoot:function(){
    	if(Addr.stdDevPanel.getRootNode().childNodes.length > 0){
	            	Addr.stdDevPanel.expandNode(Addr.stdDevPanel.getRootNode().childNodes[0])
            	}
    },
    contextMenu:Ext.create('Ext.menu.Menu',{
        items:[
               {
		            text:'新增节点(下级)',glyph:0xf103,idFlag:'add',
		            handler:function(){
		            	var recs = Addr.stdDevPanel.selModel.getSelection();
		            	var data = null;
		            	if(recs.length == 1){
		            		data = recs[0].data;
		            	}else if(recs.length > 1){
		            		Alert('最多选择一条记录!');
		            		return;
		            	}
		            	Addr.stdDevFormWin.loadRecord(data);
		            	Addr.stdDevFormWin.show();
		            }
		        },
		        {
		            text:'修改',glyph:0xf044,idFlag:'update',
		            handler:function(){
		            	var recs = Addr.stdDevPanel.selModel.getSelection();
		            	var data = null;
		            	if(recs.length == 1){
		            		data = recs[0].data;
		            	}else if(recs.length > 1){
		            		Alert('最多选择一条记录!');
		            		return;
		            	}
		            	Addr.stdDevFormWin.loadRecord(data,true);
		            	Addr.stdDevFormWin.show();
		            }
		        },
		        {
		            text:'删除',glyph:0xf00d,idFlag:'remove',
		            handler:function(){
		            	var recs = Addr.stdDevPanel.selModel.getSelection();
		            	var data = null;
		            	if(recs.length == 1){
		            		data = recs[0].data;
		            	}else if(recs.length > 1){
		            		Alert('最多选择一条记录!');
		            		return;
		            	}
		            	
		            	var message = '是否确认删除 地址位于 "' + data.jdAddrStr1 + '" 的 ' + data.stdLevelName + ' <' + data.jdName + '>?';
		            	Addr.tmpVars.stdRemoveUrl = appBase + '/stdDev/removeStdDev';
		            	Addr.tmpVars.strRemoveParam =  {devId:data.stdDevId};
		            	
		            	Confirm('提示', message, function(txt) {
								if (txt == 'yes') {
									Addr.util.req(Addr.tmpVars.stdRemoveUrl,Addr.tmpVars.strRemoveParam,function(resp){
										if(resp.code ==200 && resp.message =='success'){
											Addr.stdDevFormWin.hide();
											Addr.stdDevPanel.refreshTree();
											Alert('操作成功!');
										}else{
											Alert('错误',resp.message);
										}
									});
								}
						}, this);
		            	
		            }
		        },
		        {
		            text:'关联地址',glyph:0xf0c1,idFlag:'view',
		            handler:function(){
		            	var recs = Addr.stdDevPanel.selModel.getSelection();
		            	var data = null;
		            	if(recs.length == 1){
		            		data = recs[0].data;
		            	}else if(recs.length > 1){
		            		Alert('最多选择一条记录!');
		            		return;
		            	}
		            	Addr.matchWin.loadRecords([],data);
		            	Addr.matchWin.show();
		            }
		        },
		        {
		            text:'刷新数据',glyph:0xf021,idFlag:'refresh',
		            handler:function(){
		            	Addr.stdDevPanel.refreshTree();
		            }
		        }
                ],
        listeners:{
        	blur:function(menu){
        		menu.hide();
        	}
        }
    }),
    refreshTree:function(){
    	Addr.devTreeStore.removeAll();
		Addr.devTreeStore.load();
    },
    listeners:{
    	containercontextmenu:function( grid, event, eOpts ){
			if(event.preventDefault){
				event.preventDefault();
			}else if(event.stopPropagation){
				event.stopPropagation();
			}
			
			Addr.stdDevPanel.selModel.deselectAll();//首先取消所有的选中的记录.
			Addr.stdDevPanel.contextMenu.showAt(event.getXY());
			Addr.tmpVars.stdNode2Expand = false;
			var all = Addr.stdDevPanel.contextMenu.query();
			for(var index =0;index<all.length;index++){
				var menuItem = all[index];
				var idFlag = menuItem.idFlag;
				var disableFlag = true;
				if((menuItem.idFlag == 'add' && !window.isDqgs ) || ( menuItem.idFlag == 'refresh' )){//只有新增和刷新可用
					disableFlag = false;
				}
				menuItem.setDisabled(disableFlag);
			}
			return false;
		},
		rowcontextmenu:function( grid, record, tr, rowIndex, event, eOpts  ){
			if(event.preventDefault){
				event.preventDefault();
			}else if(event.stopPropagation){
				event.stopPropagation();
			}
			Addr.stdDevPanel.contextMenu.showAt(event.getXY());//显示菜单
			Addr.stdDevPanel.getExpandIds(record.data);//为刷新后定位做准备
			var level = record.get('stdLevel');
			var all = Addr.stdDevPanel.contextMenu.query();
			for(var index =0;index<all.length;index++){
				var item = all[index];
				var disableFlag = false;
				var idFlag = item.idFlag;
				if(window.isDqgs && idFlag != 'refresh'){
						item.setDisabled(true);
						continue;
				}
				if(level =='GZ' && idFlag =='add'){
					disableFlag = true;
				}
				item.setDisabled(disableFlag);//如果最低等级的设备,不允许继续新增
			}
			
			return false;
		},
    	itemexpand:function(panel){
    		var stdNode2Expand = Addr.tmpVars.stdNode2Expand; 
    		if(stdNode2Expand && stdNode2Expand.length > 0){
    			var stdDevId = stdNode2Expand.pop();//每次 pop出最后一个,pop完了就不再继续展开
				var recExists = Addr.devTreeStore.findRecord('stdDevId',stdDevId);
				if(recExists){
					Addr.stdDevPanel.setSelection(recExists);
        			Addr.stdDevPanel.expandPath(recExists.getPath());
				}
    		}
    	}
    },
    getExpandIds:function(data){
    	Addr.tmpVars.stdNode2Expand= [data.stdDevId];
    	var pid = data.pid;
    	if(pid != 0){
    		Addr.tmpVars.stdNode2Expand.push(pid);
    		var idx = Addr.stdDevPanel.store.find('stdDevId',data.pid);
    		if(idx >-1){
    			var topData = Addr.stdDevPanel.store.getAt(idx);
    			if(topData.get('pid') != 0){
	    			Addr.tmpVars.stdNode2Expand.push(topData.get('pid'));
    			}
    		}
    	}
    }
    ,columns:[
	{xtype: 'treecolumn', 
        text: '名称',  flex: 2, sortable: true, dataIndex: 'jdName' ,renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
        	if (Ext.isEmpty(value) || value == 'null' ){
				return "";
        	}
        	cellmeta.tdAttr = 'data-qtip="'+value+'"';
        	var str = '';
        	switch(record.get('stdLevel')){
        		case 'JF' :
        		str = '<div style="color:#EA0000">' + value + '</div>'; break;
        		case 'JJX' :
        		str = '<div style="color:#000079">' + value + '</div>'; break;
        		default : str = value; break;
        	}
			return str;
        } }
        ,{text: '级别',  flex: 1.5, sortable: true, dataIndex: 'stdLevelName' ,renderer:Addr.util.qtipValue}
        ,{text: '所在地址',  flex: 1.5, sortable: true, dataIndex: 'jdAddrStr1' ,renderer:Addr.util.qtipValue}
    ]
});