/**
 * 右侧tab面板，目前是详细信息.
 */
 Addr.detailPanel = Ext.create('Ext.FormPanel',{
 	loadRecord:function(record){
 		if(!record || !record.data){
 			return;
 		}
 		var rec = new Ext.data.Record({});
 		for(var key in record.data){
 			rec.set(key,record.get(key));
 		}
 		if(rec.get('addrLevel')){
	 		rec.set('addrLevel',Addr.levelMap[rec.get('addrLevel')].levelDesc);
 		}
 		this.getForm().loadRecord(rec);
 	},
	 region:'east',title:'详细信息',titleAlign:'center',split: true,titleCollapse : true,split: true,frame: true,collapsible: true,floatable: false,
	 glyph:0xf039,
					xtype:'form',bodyStyle: 'padding:40px 20px 40px;',
					layout: 'anchor',defaults: {anchor: '90%'},
					defaults:{
						xtype:'displayfield',
						valueField:'id',
						displayField:'name'
					},
					items:[
							{fieldLabel: '分级名称',xtype:'displayfield',name: 'str1' },
					    	{ fieldLabel: '地址编号', name: 'addrId' },
					    	{fieldLabel: '是否留空', name: 'isBlankText'} ,
					    	{fieldLabel: '地址类型',name: 'addrTypeText' },
					    	{fieldLabel: '状态',name: 'status_name'},
					    	//Addr.levelMap
					    	{fieldLabel: '级别描述',name: 'addrLevel', xtype:'textarea',hideLabel: false,height:150}
					]
				}
 );
 
 
 