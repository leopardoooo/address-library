
Addr.AddrBatchAddWin = Ext.create('Ext.window.Window',{
	title: '批量添加地址', height: 360, width: 420, layout: 'fit',closeAction:'hide',frame:true,buttonAlign:'center',glyph:0xf067,
	baseUrl : appBase + '/tree/',
	loadRecord:function(data,update){
		var form = this.down('form');
		form.getForm().reset();
		var isEmptyCmp = form.getForm().findField('isBlank');
		var disableEmptyCmp = false;
		var dataCopy = {addrId:null,addrParent:data.addrId,addrUse:null,addrLevel:data.addrLevel + 1,countyId:data.countyId,parentStr1 : data.str1};
		dataCopy.addrLevelName = dataCopy.addrLevel;
		form.loadRecord(new Ext.data.Record(dataCopy));
	},
	listeners:{
		beforehide:function( window, eOpts ){
			var form = window.down('form');
			form.getForm().reset();
		},
		show:function( window, eOpts ){
			window.down('tabpanel').setActiveItem(0);
		}
	},
    items: {
				xtype:'form',bodyStyle: 'padding:20px 30px 30px 40px;',buttonAlign:'center',
				defaults:{ frame:false,border:false },
				items:[
					{xtype:'form',layout:'form',
					defaults:{
						xtype:'combo',
						valueField:'id',
						layout:'form',
						displayField:'name'
					},
					items:[
						{fieldLabel: 'ignoreEmpty',xtype:'hidden',name: 'ignoreEmpty' ,value:true},
							{fieldLabel: '父级ID',xtype:'hidden',name: 'addrParent' },
							{fieldLabel: 'countyId',xtype:'hidden',name: 'countyId' },
							{fieldLabel: '是否留空',xtype:'hidden',name: 'isBlank',value:'F'},
							{fieldLabel: '级别',xtype:'hidden',name: 'addrLevel' },
							{fieldLabel: '父级分级名称',xtype:'hidden',name: 'parentStr1' },
					    	{fieldLabel: '地址类型',name: 'addrType',allowBlank: false,store:Addr.addrTypeStore,typeAhead:false,editable:false,
				    	beforeLabelTextTpl: [
					        '<span style="color:red;font-weight:bold" data-qtip="必填选项">*</span>'  
					    ]},
					    {fieldLabel: '地址级别',xtype:'displayfield',name: 'addrLevelName' }
					]
					},
					{xtype:'tabpanel',bodyStyle:'padding:5px 5px 5px 5px;',
						defaults:{
							frame:false,border:false,xtype:'form',layout:'form'
						},
						items:[
						{title:'地址名称区间',defaults:{padding:'5px'},urlSuffix:'addTrees',
							items:[
							{fieldLabel: '名称前缀',name: 'addrNamePreffix',xtype:'textfield'},
							{fieldLabel: '名称后缀',name: 'addrNameSuffix',xtype:'textfield'},
							{fieldLabel: '从',name: 'start',xtype:'textfield'},
							{fieldLabel: '至',name: 'end',xtype:'textfield'}
							]
						},
						{title:'自定义地址名称',urlSuffix:'addTreesWithNames',items:[
							{fieldLabel: '地址名称',xtype:'textarea',name: 'batchNames',
							emptyText : '请输入要添加的地址名,以逗号分隔.逗号(仅仅都好本身)不区分全半角',
				    	beforeLabelTextTpl: [
					        '<span style="color:red;font-weight:bold" data-qtip="必填选项">*</span>'  
					    ]}
						]}
						]
					}
				]
			
    },
	    fbar: [{
	        text: '取消',
	        handler: function() {
	            this.up('window').down('form').getForm().reset();
	            Addr.AddrBatchAddWin.hide();
	        }
	    }, {
	        text: '确定',
	        formBind: true, //only enabled once the form is valid
	        handler: function() {
	            var form = this.up('window').down('form').getForm();
	            var values = form.getValues();
	            var tab = Addr.AddrBatchAddWin.down('tabpanel');
	            var urlSuffix = tab.getActiveTab().urlSuffix;
	            
	            if(Ext.isEmpty(values.addrNamePreffix) && Ext.isEmpty(values.addrNameSuffix) && Ext.isEmpty(values.batchNames)){
	            	Alert('请完整填写表单的必填项目！');
	            	return false;
	            }
	            
	            if (this.up('window').down('form').isValid()) {
						var wait = Ext.Msg.wait('操作中','请稍候');
						wait.show();
	            	Addr.util.req(Addr.AddrBatchAddWin.baseUrl + urlSuffix ,values,function(data){
	            		if(data.code == 200 && data.message == 'success'){
	            			wait.hide();
	            			Alert('操作结果!','成功 [' +
	            					data.data[0] +
	            					'] 个，失败 [' +
	            					data.data[1] +
	            					'] 个' );
	            			Addr.contextMenuHandler.refreshAfterOperation();
	            			Addr.AddrBatchAddWin.hide();
	            		}else{
	            			wait.hide();
	            			Alert('操作失败',data.message);
	            		}
	            	})
	            }
	            
	        }
	    }]

});