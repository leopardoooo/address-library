Addr.noticeListStore =Ext.create('Ext.data.JsonStore',{
	fields:['noticeId','noticeName','createTime','createOptr','read','noticeContent','effDate','invalidDate'],
	autoLoad:true,pageSize:Addr.itemsPerPage,
    listeners:{
    	beforeLoad:function(store, operation, eOpts){
    		store.removeAll();
    		var param = store.proxy.extraParams;
    		Ext.apply(param, {read:Addr.noticeListWin.queryOnlyUnChecked});
    		store.proxy.extraParams = param;
    	}
    },
    proxy: {
        type: 'ajax',
        url: appBase + '/notice/query',
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

Addr.noticeDetailWin = Ext.create('Ext.window.Window',{
	glyph:0xf129,title:'公告内容',layout:'fit',width:1000,height:600,buttonAlign:'center',titleAlign:'center',
	buttons:[{glyph:0xf00d,text:'关闭窗口',handler:function(){Addr.noticeDetailWin.hide()}}],
	closeAction:'hide',
	loadHtml:function(html){
		Addr.noticeDetailWin.setHtml(html);
	},
	html:''
});

Addr.noticeListWin = Ext.create('Ext.window.Window',{
	queryOnlyUnChecked:'F',
	listeners:{
		render:function(win){
			if(window.canEditNotice != true){
				Ext.getCmp('addNoticeBtn').hide();
				Ext.getCmp('modNoticeBtn').hide();
				Ext.getCmp('removeNoticeBtn').hide();
			}
		}
	},
	tbar:[
	{glyph:0xf067,text:'添加',id:'addNoticeBtn',handler:function(){
		Addr.noticeEditWin.loadEditData();
		Addr.noticeEditWin.show();
	}},
	{glyph:0xf044,text:'修改',id:'modNoticeBtn',handler:function(){
		var sel = Addr.noticeListWin.down('grid').selModel;
		var rs = sel.getSelection()
		if(rs.length !=1){
			Alert('请选择一套记录!');
			return;
		}
		Addr.noticeEditWin.loadEditData(rs[0]);
		Addr.noticeEditWin.show();
	}},
	{glyph:0xf12d,text:'删除',id:'removeNoticeBtn',handler:function(){
		var sel = Addr.noticeListWin.down('grid').selModel;
		var rs = sel.getSelection()
		if(rs.length !=1){
			Alert('请选择一套记录!');
			return;
		}
		Confirm('是否确认删除', '确认删除标题为 “' +
				data.data.noticeTitle +
				'”的通知？', function(txt) {
										if (txt == 'yes') {
											Addr.util.req(appBase+'/notice/remove',{noticeId:data.data.noticeId},function(rd){
												Addr.noticeListStore.load();
											});
										}
									});
	}},
	'->',
	{glyph:0xf03a,text:'只看未读',handler:function(){
		Addr.noticeListWin.queryOnlyUnChecked = 'T';
		Addr.noticeListStore.load();
	}},
	{glyph:0xf03a,text:'查看全部',handler:function(){
		Addr.noticeListWin.queryOnlyUnChecked = 'F';
		Addr.noticeListStore.load();
	}},
	{glyph:0xf00d,text:'关闭窗口',handler:function(){Addr.noticeListWin.hide()}}
	],
	title:'公告通知',layout:'fit',width:800,height:600,glyph:0xf03a,buttonAlign:'center',titleAlign:'center',
	closeAction:'hide',
	items:[
	{xtype:'grid', dockedItems: [{ xtype: 'pagingtoolbar', store: Addr.noticeListStore,  dock: 'bottom', displayInfo: true }],
		store:Addr.noticeListStore,
		columns:[
				
	     { text: '地址编号',  flex: 1,  dataIndex: 'noticeId',hidden:true }
	     ,{text: '标题',flex: 2,dataIndex: 'noticeTitle',sortable: true ,renderer:Addr.util.qtipValue}
	     ,{text: '生效日期',flex: 2,dataIndex: 'effDate',sortable: true ,renderer:Addr.util.qtipValue}
	     ,{text: '失效日期',flex: 2,dataIndex: 'invalidDate',sortable: true ,renderer:Addr.util.qtipValue}
	     ,{text: '状态',flex: 2,dataIndex: 'read',sortable: true ,renderer:function(v){ return v =='T' ? '已读':'未读'; }}
	     ,{text: '操作',flex: 1,dataIndex: 'noticeId',sortable: true ,renderer:function(value, cellmeta, record, rowIndex, columnIndex, store){
	     	return '<a href="#" onclick="Addr.noticeListWin.viewContent(' + value + ')">查看详细</a>';
	     }}
		]
	}
	],
	viewContent:function(id){
		var data = Addr.noticeListStore.queryRecords('noticeId',id)[0];
		//do sth
		Addr.noticeDetailWin.loadHtml(data.data.noticeContent);
		Addr.noticeDetailWin.setTitle(data.get('noticeTitle') );
		Addr.noticeDetailWin.show();
		if(data.data.read == 'F'){
			this.checkNotice(data);
		}else{
			rec.set('read','T');
		}
	},
	checkNotice:function(data){
		Addr.util.req(appBase+'/notice/checkRead',{noticeId:data.data.noticeId},function(rd){
			Addr.noticeListStore.load();
			});
	}
});

Addr.noticeEditWin = Ext.create('Ext.window.Window',{
	title:'编辑公告',layout:'border',width:800,height:600,glyph:0xf044,closeAction:'hide',buttonAlign:'center',titleAlign:'center',
	htmlEditor:null,
	loadEditData:function(data){
		this.htmlEditor = UE.getEditor('htmlEditor');
		var form = this.down('form').getForm();
		form.reset();
		this.htmlEditor.ready(function(){
			
			var countyIdsCmp = form.findField('countyIds');
			countyIdsCmp.store.removeAll();
			countyIdsCmp.store.add( Addr.util.getSelectAbleCounties()) ;
			
			if(!data){
				Addr.noticeEditWin.htmlEditor.setContent('');
				return;
			}
			data.data.invalidDate = data.data.invalidDate.substring(0,10);
			data.data.effDate= data.data.effDate.substring(0,10);
			form.loadRecord(data);
			Addr.noticeEditWin.htmlEditor.setContent(data.get('noticeContent'));
			
			Addr.util.req(appBase+'/notice/queryCities',{noticeId:data.data.noticeId},function(rd){
				Addr.noticeEditWin.down('combo').setValue(rd.data);
			});
			
		});
		
	},
	items:[
	{xtype:'form',region:'north',bodyStyle:'text-align:center;padding:10px 50px 10px 50px;',layout:'column',
	defaults:{columnWidth:.3},
		items:[
				{xtype:'hidden',fieldLabel:'ID',name:'noticeId'},
				{xtype:'textfield',fieldLabel:'标题',name:'noticeTitle'},
				{xtype:'datefield',fieldLabel:'失效日期',name:'invalidDate',editable:false,format : 'Y-m-d',minValue:Ext.util.Format.date(new Date(),'Y-m-d')},
				{xtype:'datefield',fieldLabel:'生效日期',name:'effDate',editable:false,format : 'Y-m-d',minValue:Ext.util.Format.date(new Date(),'Y-m-d')},
				{xtype:'combo',fieldLabel:'适用分公司',displayField:'addrName',valueField:'countyId',
					editable:false,name:'countyIds',hideLabel: false,multiSelect : true,columnWidth:.9,displayField:'countyName',valueField:'countyId',
					store:Ext.create('Ext.data.JsonStore',{
						fields:['countyId','countyName'],
						data:[]
					})
				}
				
		]
	},
	{xtype:'panel',width:'780',height:'300',region:'center',
				html:'<div style="width:780px;height:300px;" id="htmlEditor"></div>'}
	],
	buttons:[
		{glyph:0xf0c7,text:'保存',handler:function(){Addr.noticeEditWin.doSave()}},
		{glyph:0xf00d,text:'取消',handler:function(){Addr.noticeEditWin.doCancel()}}
	],
	doSave:function(){
		var form = Addr.noticeEditWin.down('form').getForm();
		var values = form.getValues();
		var content = Addr.noticeEditWin.htmlEditor.getContent();
		values.noticeContent = content;
		
		Addr.util.req(appBase+'/notice/save',values,function(data){
			if(data.code == 200 && data.message=='success'){
				Alert('操作成功!');
				Addr.noticeListStore.load();
				Addr.noticeEditWin.hide();
			}else{
				Alert('操作失败!',data.message);
			}
		});
	},
	doCancel:function(){
		Addr.noticeEditWin.hide();
	}
});

