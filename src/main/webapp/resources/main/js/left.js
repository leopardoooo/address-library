/**
 * 左边地址树木.
 */
 
	Addr.treeStore = Ext.create("Ext.data.TreeStore", {
		model : 'Addr.model.Addr',
		pageSize:Addr.itemsPerPage,
		listeners : {
			load:function(store,records,successful, eOpts){
				//有一个奇葩的问题,只有一个节点的时候,不对这个记录处理一下,左边的树就不显示, hack的解决一下.
				//第一个条件保证只在第一次加载的时候有效.
				Ext.defer(function() {
					var count = Addr.treeStore.getCount();
					var cnt = records.length;
					if(cnt == 1  && Addr.detailPanel){
						var record = records[0];
		            	Addr.detailPanel.loadRecord(record);
					}
				}, 500, this);
				
			}
		},
        proxy: {
            type: 'ajax',
            url: appBase + '/query/queryChildren',
            reader: {
                type: 'json'
                ,rootProperty: 'data'
            }
        }
	});
	
	
	Addr.treeGrid =  Ext.create('Ext.tree.Panel', {
        title: '地址树',orderInTab:0,
        titleAlign :'left',singleExpand : false,//可以展开多个节点
        rowLines :true,columnLines :true ,
        pageSize:Addr.itemsPerPage,draggable:false,
        reserveScrollbar: true,floatable: false,
        loadMask: true,useArrows: false,rootVisible: false,store: Addr.treeStore,animate: false,
        viewConfig: {
	        listeners: {
	            render: function(view, eOpts){
	            	//首先展开第一个节点.
	            	Addr.treeGrid.expandRoot();
	            }
	        }
        },
        expandRoot:function(){
        	/*
        	Ext.defer(function() {
					console.log(this.name);
				}, 1000, this);
				
			*/
        	var rootNode = Addr.treeGrid.getRootNode();
        	if(Addr.treeGrid.getRootNode().childNodes.length > 0){
            	Addr.treeGrid.expandNode(Addr.treeGrid.getRootNode().childNodes[0]);
        	}
        },
        listeners:{
        	rowcontextmenu:function(grid, record, tr, rowIndex, event, eOpts){
        		//取消默认的浏览器右键事件  
        		if(event.preventDefault){
    				event.preventDefault();
    			}else if(event.stopPropagation){
    				event.stopPropagation();
    			}
    			Addr.contextMenuHandler.centerGrid = false;
    			Addr.treeGrid.contextMenu.showAt(event.getXY());
    			Addr.contextMenuHandler.filterMenuItems(Addr.treeGrid.contextMenu,record);
				return false;
        	},
        	itemclick:function( panel, record, item, index, e, eOpts ){
        		//点击左边小角 ,展开子集的时候,不触发 这个事件，但是会触发  rowclick  ,所以不用实现 rowclick  ,仅仅实现 itemclick 就可以满足
        		Addr.detailPanel.loadRecord(record);
        	},
        	itemexpand:function(panel){
        		var nodesToBeExpand = Addr.tmpVars.nodesToBeExpand; 
        		if(nodesToBeExpand && nodesToBeExpand.length > 0){
        			var addrId = nodesToBeExpand.pop();//每次 pop出最后一个,pop完了就不再继续展开
					var recExists = Addr.treeStore.findRecord('addrId',addrId);
					if(recExists){
						Addr.treeGrid.setSelection(recExists);
	        			Addr.treeGrid.expandPath(recExists.getPath());
					}
        		}
        	}
        }
        ,contextMenu:Ext.create('Ext.menu.Menu',{
		        items:[
		               {
				            text:'数据浏览(子集)',glyph:0xf103,
				            handler:function(){
				            	var rec = Addr.treeGrid.selModel.getSelection();
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
			                	var rec = Addr.treeGrid.selModel.getSelection();
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
			                	var rec = Addr.treeGrid.selModel.getSelection();
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
							text : '新增地址',needFielterByFgs:true, glyph : 0xf0c7,
							addChildren:true,
							handler : function() {
								var rec = Addr.treeGrid.selModel.getSelection();
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
			                text:'批量新增地址',glyph:0xf067,needFielterByFgs:true,addChildren:true,
			                handler:function(){
			                	var rec = Addr.treeGrid.selModel.getSelection();
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
								var rec = Addr.treeGrid.selModel.getSelection();
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
								var rec = Addr.treeGrid.selModel.getSelection();
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
								var rec = Addr.treeGrid.selModel.getSelection();
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
								var rec = Addr.treeGrid.selModel.getSelection();
				            	if(rec.length != 1){
				            		Alert('提示','请选择且仅选择一条记录！');
				            		return ;
				            	}
								Addr.contextMenuHandler.viewLog(rec[0].data);
							}
						}
		                ],
		        listeners:{
		        	blur:function(menu){
		        		menu.hide();
		        	}
		        }
		    }),
    	columns:[
		{
			/**this is so we know which column will show the tree**/
            xtype: 'treecolumn', 
            text: '地址名',  flex: 1.5, sortable: true, dataIndex: 'addrName' ,renderer:function(v){
            	return '<span style="font-size:14px; ">' + v + '</span>';
            }}
        ]
    });
	
	/**
	 * 右侧tab面板，目前是详细信息.
	 */
	 
	 Addr.leftTabPanel = Ext.create('Ext.tab.Panel',{
					glyph:0xf115,titleCollapse : true,split: true,frame: true,
					collapsible: true,floatable: false,region:'west',width:'20%',
					activeTabOrderly:function(goLeft){
						//切换标签,默认往右,如果参数不为空,往左
						var total = this.items.length;
						var now = this.currentTabItemNum;
						var next = (now + (Ext.isEmpty(goLeft) ? 1 : -1) + total) % total;
						this.setActiveTab(next);
					},
					activeLeft:function(){
						this.activeTabOrderly('left');
					},
					activeRight:function(){
						this.activeTabOrderly();
					},
					currentTabItemNum:0,
					listeners:{
						tabchange : function( tabPanel, newCard, oldCard, eOpts ){
							tabPanel.currentTabItemNum = newCard.orderInTab;
							if(newCard.expandRoot){
								newCard.expandRoot();
							}
						},
						render:function(tabPanel,eOpts ){
							//设置箭头点击的时候,切换tab
							var array = Addr.leftTabPanel.el.query('.x-box-scroller');
							var rightArraw = Ext.get(Addr.leftTabPanel.el.query('.x-box-scroller.x-box-scroller-right')[0]);
							var leftArraw =  Ext.get(Addr.leftTabPanel.el.query('.x-box-scroller.x-box-scroller-left')[0]);
							leftArraw.addListener( 'click', this.activeLeft, this);
							rightArraw.addListener( 'click', this.activeRight, this);
						}
					},
					items:[
					       Addr.treeGrid,
							Addr.stdDevPanel,
							Addr.stdOptrTreeGrid
					]
				});