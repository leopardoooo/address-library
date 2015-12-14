

Addr.stdOptrStore = Ext.create("Ext.data.TreeStore", {
	model : 'Addr.model.StdDev',
	pageSize:Addr.itemsPerPage,
	listeners : {
		beforeload:function( store, operation, eOpts ){
			//TODO 
		},
		load:function(store,records){
			
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


Addr.stdOptrTreeGrid =  Ext.create('Ext.panel.Panel', {
    title:'维护人员',singleExpand : true,orderInTab:2,
//    glyph:0xf183,
    rowLines :true,columnLines :true ,
    pageSize:Addr.itemsPerPage,draggable:true,
    reserveScrollbar: true,
    loadMask: true,useArrows: true,rootVisible: false,
//    store: Addr.stdOptrStore,animate: false,
    expandRoot:function(){
    	/*
    	if(Addr.stdOptrTreeGrid.getRootNode().childNodes.length > 0){
	            	Addr.stdOptrTreeGrid.expandNode(Addr.stdOptrTreeGrid.getRootNode().childNodes[0])
            	}
            	*/
    },
    listeners:{
    	rowcontextmenu:function(grid, record, tr, rowIndex, event, eOpts){
    		//取消默认的浏览器右键事件  
    		if(event.preventDefault){
				event.preventDefault();
			}else if(event.stopPropagation){
				event.stopPropagation();
			}
    	}
    }
//        ,columns: Addr.treeColumns
    ,columns:[
	{
		/**this is so we know which column will show the tree**/
        xtype: 'treecolumn', 
        text: '节点名称',  flex: 1.5, sortable: true, dataIndex: 'jdName' }
    ]
});