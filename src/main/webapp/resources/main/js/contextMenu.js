//TODO 新增修改表单
Addr.AddrFormWin = Ext.create('Ext.window.Window',{
	title: '编辑地址', height: 320, width: 420, layout: 'fit',closeAction:'hide',frame:true,buttonAlign:'center',
	loadRecord:function(data,update){
		var form = this.down('form');
		form.getForm().reset();
		var isEmptyCmp = form.getForm().findField('isBlank');
		var disableEmptyCmp = false;
		var dataCopy = {};
		Addr.AddrFormWin.baseUrl = appBase + '/tree/';
		if(update == true){
			//是否留空不给修改
			disableEmptyCmp = true;
			for(var key in data){
				dataCopy[key] = Ext.isEmpty(data[key]) ? null:data[key];
			}
			var str1 = data.str1;
			var arr = str1.split('/');
			arr.pop();
			dataCopy.parentStr1 = arr.join('/');
			Addr.AddrFormWin.baseUrl += 'modTree';
		}else{
			dataCopy = {addrId:null,addrParent:data.addrId,addrUse:null,addrLevel:data.addrLevel + 1,countyId:data.countyId};
			dataCopy.parentStr1 = data.str1;
			Addr.AddrFormWin.baseUrl += 'addTree';
		}
		dataCopy.addrLevelName = dataCopy.addrLevel;
		isEmptyCmp.setDisabled(disableEmptyCmp);
		
		form.getForm().findField('addrName').setDisabled(dataCopy.isBlank == 'T');
		
		form.loadRecord(new Ext.data.Record(dataCopy));
	},
    items: {
				xtype:'form',bodyStyle: 'padding:20px 30px 30px 40px;',buttonAlign:'center',
				layout: 'anchor',defaults: {anchor: '90%'},
				defaults:{
					xtype:'combo',
					valueField:'id',
					displayField:'name'
				},
				items:[
					{fieldLabel: '是否留空', allowBlank:false,name: 'isBlank', store:Addr.addrBlankStore,typeAhead:false,editable:false,value:'F',
						listeners:{
							change:function(field, newValue, oldValue){
								var addrNameCpm = Addr.AddrFormWin.down('form').getForm().findField('addrName');
								if(newValue =='T'){
									addrNameCpm.setValue('');
									addrNameCpm.setDisabled(true);
								}else{
									addrNameCpm.setDisabled(false);
								}
							}
						},
				    	beforeLabelTextTpl: [
					        '<span style="color:red;font-weight:bold" data-qtip="必填选项">*</span>'  
					    ] } ,
						{fieldLabel: '地址名称',xtype:'textfield',name: 'addrName',allowBlank:false,
				    	beforeLabelTextTpl: [
					        '<span style="color:red;font-weight:bold" data-qtip="必填选项">*</span>'  
					    ],
							listeners:{
									change:function(field, newValue, oldValue){
										var form = this.up('form');
										var parentStr1 = form.getForm().findField('parentStr1');
										var str1 = form.getForm().findField('str1');
										str1.setValue(parentStr1.getValue() + '/' + newValue);
									}
								}
						},
						{fieldLabel: '地址级别',xtype:'displayfield',name: 'addrLevelName' },
						{fieldLabel: '分级名称',xtype:'displayfield',name: 'str1' },
						{fieldLabel: 'ignoreEmpty',xtype:'hidden',name: 'ignoreEmpty' ,value:true},
						{fieldLabel: '父级ID',xtype:'hidden',name: 'addrParent' },
						{fieldLabel: '分公司',xtype:'hidden',name: 'countyId' },
						{fieldLabel: '级别',xtype:'hidden',name: 'addrLevel' },
						{fieldLabel: '地址ID',xtype:'hidden',name: 'addrId' },
						{fieldLabel: '父级分级名称',xtype:'hidden',name: 'parentStr1' },
				    	{fieldLabel: '地址类型',name: 'addrType',allowBlank: true,store:Addr.addrTypeStore,typeAhead:false,editable:false,
				    	beforeLabelTextTpl: [
					        '<span style="color:red;font-weight:bold" data-qtip="必填选项">*</span>'  
					    ]},
					    {fieldLabel: '备注',name: 'addrUse',xtype:'textarea',maxLength:100, allowBlank: true }
				],
			    fbar: [{
			        text: '取消',glyph:0xf00d,
			        handler: function() {
			            this.up('form').getForm().reset();
			            Addr.AddrFormWin.hide();
			        }
			    }, {
			        text: '确定',glyph:0xf0c7,
			        formBind: true, //only enabled once the form is valid
			        handler: function() {
			            var form = this.up('form').getForm();
			            var values = form.getValues();
			            
			            if (this.up ('form').isValid()) {
			            	Addr.util.req(Addr.AddrFormWin.baseUrl ,values,function(data){
			            		if(data.code == 200 && data.message == 'success'){
			            			Alert('操作成功!');
			            			Addr.contextMenuHandler.refreshAfterOperation();
			            			Addr.AddrFormWin.hide();
			            		}else{
			            			Alert('操作失败',data.message);
			            		}
			            	})
			            }
			            
			        }
			    }]
			
    }

});

/**
 * 右键菜单的响应函数.
 * @type 
 */
Addr.contextMenuHandler={
	centerGrid:false,
	filterMenuItems:function(menu,record){
		var all = menu.query();
		if(!window.isDqgs){
			return;
		}
		var maxAllowed = Addr.loginUserInSession.maxLevelAllowed;
		var dataLevel = record.get('addrLevel');
		for(var index =0;index<all.length;index++){
			var item = all[index];
			var disableFlag = item.needFielterByFgs || false;
			if(disableFlag){
				if(item.addChildren == true){//添加子集
					disableFlag = maxAllowed -1 > dataLevel;
				}else{
					disableFlag = maxAllowed > dataLevel;
				}
			}
			item.setDisabled(disableFlag);
		}
	},
	refreshAfterOperation:function(){//各种操作执行之后,刷新表格的数据.
		Addr.queryResultGrid.store.load();
		Addr.treeStore.load();
		Addr.detailPanel.loadRecord(new Ext.data.Record({str1:null,addrId:null,isBlankText:null,addrTypeText:null,addrUse:null,status_name:null,addrLevel:null}));
	},
	/**
	 * 浏览数据
	 * @param {} addrId
	 */
	viewChildrenData:function(addrId){
//		Addr.queryForm.resetForm();
		Ext.Ajax.request({
			url : appBase + '/tree/findDirectChildrens',
			params:{pid:addrId},
			success:function(req){
				var data = Ext.decode(req.responseText);
				if(data.code != 200){
					Alert('加载数据出错！' + data.message);
					return;
				}
				var rawData = data.data || [] ;
				Addr.queryGridStore.loadRawData(data.data);
			}
		})
	},
	addrMerge:function(data){//地址合并
		Addr.mergeWin.loadRecords(data);
		Addr.mergeWin.show();
	},
	addNewAddr:function(data){//新增地址
		if(data.countyId == '4501'){
			Alert('当前级别地址不允许添加！');
			return;
		}
		Addr.AddrFormWin.loadRecord(data,false);
		Addr.AddrFormWin.show();
  	  	Addr.AddrFormWin.setTitle('新增地址');
	},
	addNewAddrBatch:function(data){//批量新增地址
		if(data.countyId == '4501'){
			Alert('当前级别地址不允许添加！');
			return;
		}
		Addr.AddrBatchAddWin.loadRecord(data);
		Addr.AddrBatchAddWin.show();
	},
	changeParent:function(data){//变更父级
		Addr.changeParentWin.loadRecord(data);
		Addr.changeParentWin.show();
	},
	updateAddr:function(data){//地址修改
		Addr.AddrFormWin.loadRecord(data,true);
		Addr.AddrFormWin.show();
  	  	Addr.AddrFormWin.setTitle('地址修改');
	},
	removeAddr:function(data){//删除地址
			Addr.tmpVars.id2del = data.data.addrId;
		function removeCallBack(data){
			if(data.code == 200){
				Addr.contextMenuHandler.refreshAfterOperation();
				Addr.tmpVars.id2del = null;
				Alert('操作成功');
				return ;
			}
			if(data.code == 312){//当前地址下仍有子节点地址
				Confirm('错误代码[' +data.code +']',data.message + '<br/> 是否强制删除？',function(yesNo) {
					if(yesNo == "yes"){
						var url = appBase + '/tree/delTreeForceCasecade';
						Addr.util.req(url,{addrId:Addr.tmpVars.id2del},removeCallBack);
					}
				} );
			}else{//其他错误
				Alert('错误代码[' +data.code +']',data.message);
			}
		}
		
		Confirm('确认删除地址?', '  是否删除 ”' + data.data.str1 + '” ？',function(yesNo) {
					if(yesNo == "yes"){
						var url = appBase + '/tree/delTree';
						Addr.util.req(url,{addrId:Addr.tmpVars.id2del},removeCallBack)
					}
				} );
		
		
	},
	viewLog:function(data){//节点日志
		Addr.logWin.loadSingle(data);
		Addr.logWin.show();
	},
	addrDevMatch:function(recs){//设备地址关联
    	Addr.matchWin.show();
		Addr.matchWin.loadRecords(recs,null);
	},
	findInTree:function(data){//资源树定位
		if(!data){
			return;
		}
		Addr.leftTabPanel.setActiveTab(0);
		Addr.treeGrid.collapseAll();
		var addrPrivateName = data.addrPrivateName;
		var recExists = Addr.treeStore.findRecord('addrId',data.addrId);
		if(recExists){
			Addr.treeGrid.expandPath(recExists.getPath());
			return;
		}
		
		var addrIds = addrPrivateName.split('/');
		if(Ext.isEmpty(addrIds[addrIds.length -1])){
			addrIds.pop();
		}
//		addrIds = addrIds.slice(0,Addr.MAX_TREE_LENTH);
		addrIds.reverse();
		if(addrIds[addrIds.length - 1 ] == '0'){
			var firstNode = Addr.treeGrid.store.getAt(0);
			if(null != firstNode && firstNode.get('addrId')!=0){
				addrIds.pop();
			}
		}
		Addr.tmpVars.nodesToBeExpand = addrIds;
		Addr.treeGrid.fireEvent('itemexpand',Addr.treeGrid);//激发expand 事件
	}
}