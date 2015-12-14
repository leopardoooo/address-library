Ext.ns('Addr');

Addr ={
	browserSize:{
		width:document.body.clientWidth,
		height:document.body.clientHeight
	},
	MAX_TREE_LENTH: 6,
	itemsPerPage:25,
	leftTabPanel:null,//作恶的tab面板，包含地址树，光站树，维护人员树
	addrTree:null,//地址树
	gzTree:null,//光站树
	optrTree:null,//维护人员树
	
	mainPanel:null,//主面板,包含 查询表单，查询结果集
	queryForm:null,//查询表单
	queryResultGrid:null,//查询结果集
	
	rightPanel:null,//右侧面板，包含地址树详细信息
	detailPanel:null,//地址详细信息面板
	
	addrWinForm:null,//地址新增编辑窗口
	
	noticeWin:null,//通知公告窗口
	noticeGrid:null,//通知公告表格
	
	logWin:null,//操作日志窗口
	logGrid:null,//操作日志表格
	
	adjustWin:null,//审核窗口
	adjustGrid:null,//审核表格
	addrBlankStore:Ext.create("Ext.data.Store", {
		fields:[{"name":"id","type":"auto"}, {"name":"name","type":"auto"}],
		data:[{"id":"T","name":"是"}, {"id":"F","name":"否"}]
	}),
	addrUseTypeStore:Ext.create("Ext.data.Store", {
		fields:[{"name":"id","type":"auto"}, {"name":"name","type":"auto"}],
		data:[{"id":"","name":""},{"id":"CITY","name":"城市小区"}
		,{"id":"INDUSTRIAL_PARK","name":"工业园厂房"},{"id":"CITY_HOTEL","name":"城市酒店"},{"id":"SHOPS","name":"小区商铺"},{"id":"OTHERS","name":"其它"}]
	}),
	addrTypeStore:Ext.create("Ext.data.Store", {
		fields:[{"name":"id","type":"auto"}, {"name":"name","type":"auto"}],
		data:[{"id":"","name":""},{"id":"CITY","name":"城镇"}, {"id":"RURAL","name":"农村"}]
	}),
	addrFields:[
			{"name":"district","type":"auto"},
			{"name":"region","type":"auto"},
			{"name":"road","type":"auto"},
			{"name":"roadNum","type":"auto"},
			{"name":"village","type":"auto"},
			{"name":"building","type":"auto"},
			{"name":"street","type":"auto"},
			{"name":"department","type":"auto"},
			{"name":"houseNum","type":"auto"},
			{"name":"districtCode","type":"auto"},
			{"name":"regionCode","type":"auto"},
			{"name":"roadCode","type":"auto"},
			{"name":"roadNumCode","type":"auto"},
			{"name":"villageCode","type":"auto"},
			{"name":"buildingCode","type":"auto"},
			{"name":"streetCode","type":"auto"},
			{"name":"departmentCode","type":"auto"},
			{"name":"houseNumCode","type":"auto"},
            {"name":"collected","type":"auto"},{"name":"addrId","type":"auto"},{"name":"addrName","type":"auto"},{"name":"addrLevel","type":"auto"},{"name":"addrType","type":"auto"},{"name":"addrTypeText","type":"auto"},{"name":"addrUse","type":"auto"},{"name":"addrUseText","type":"auto"},{"name":"isBlank","type":"auto"},{"name":"isBlankText","type":"auto"},{"name":"addrParent","type":"auto"},{"name":"addrPrivateName","type":"auto"},{"name":"addrFullName","type":"auto"},{"name":"addrCode","type":"auto"},{"name":"countyId","type":"auto"},{"name":"createTime","type":"auto"},{"name":"createOptrId","type":"auto"},{"name":"createDoneCode","type":"auto"},{"name":"status","type":"auto"},{"name":"str1","type":"auto"},{"name":"str2","type":"auto"},{"name":"str3","type":"auto"},{"name":"str4","type":"auto"},{"name":"str5","type":"auto"}
        ],
        stdDevFields:["stdDevId","stdLevel","jdName","jdAddrId","jdAddrName","jdAddrFullName","jdAddrStr1","pid","countyId","createOptr","createTime","baseServeAddr","servAddrName","servAddrFullName","servAddrStr1"],
		addrLevelFields:[{name:'levelNum',type:'auto'}, {name:'levelName',type:'auto'} ],
		tmpVars:{
			desc:'存放一些临时变量'
		},
		citySelectWin:null,//分公司选择窗口
		getSearchParam:function(){
			var combo = Ext.getCmp('levelLabelCombo');
			var searchInput = Ext.getCmp('targetAddr');
			var sl = -1;
			if(combo){
				sl = combo.getValue() == null ? -1 :  combo.getValue();
			}
			var q = '';
			if(searchInput){
				q = searchInput.getValue();
			}
			return {sl:sl,q:q,pageSize:Addr.main.itemsPerPage};
		},
		searchBtnHandler:function(btn){
			var param = Addr.addrParentSearchStore.proxy.extraParams;
			var sl = -1;//级别
			var q = Addr.queryForm.getForm().findField('addrParent').getValue();
			var extra = {sl:sl,q:q,pageSize:Addr.itemsPerPage};
			for(var key in extra){
				param[key] = extra[key];
			}
			Addr.addrParentSearchStore.proxy.extraParams = param;
			Addr.addrParentSearchStore.load();
		}
		
}

//1.定义Model
Ext.define("Addr.model.Addr", {
    extend: "Ext.data.Model",idProperty:'addrId',
    fields: Addr.addrFields
});

 Ext.define("Addr.model.AddrLevel", {
    extend: "Ext.data.Model",
    fields: Addr.addrLevelFields
});

Ext.define("Addr.model.StdDev", {
    extend: "Ext.data.Model",idProperty:'stdDevId',
    fields: Addr.stdDevFields
});
 

Addr.util = {
	getSelectAbleCounties:function(){
		var countyId = Addr.loginUserInSession.companyOID;
		var children = Addr.allCountyMap[countyId];
		if(window.isDqgs){
			return children;
		}
		if(children && children.length>0){//应该是 4501
			var result = [];
			for(var index =0;index<children.length;index++){
				var child = children[index];
				var sub = Addr.allCountyMap[child.countyId];
				if(sub && sub.length > 0){
					result = result.concat(sub);
				}else{
					result.push(children[index]);
				}
			}
			return result;
		}else{
			return [Addr.singleCountyMap[countyId]];
		}
	},
	qtipValue : function(value) {
		if (Ext.isEmpty(value))
			return "";
		if (value == 'null')
			return "";
		return '<div data-qtitle="" data-qtip="' + value + '">' + value + '</div>';
	},
	req:function(url,params,callbackWithData,scope){
		Ext.Ajax.request({
			url : url,
			params:params,
			success:function(req){
				var data = Ext.decode(req.responseText);
				if(callbackWithData){
					callbackWithData.call(scope || window,data);
				}
			}
		})
	}
}

Addr.levelMap = {};
	for(var index =0;index<levesTmp.length;index++){
		var item = levesTmp[index];
		Addr.levelMap[''+item.levelNum] = item;
	}

/**
 * 
 * @param {} title  
 * @param {} message
 * @param {} callback
 * @param {} callbackArgs
 * @param {} scope
 */
window.Alert = function(title,message,callback,callbackArgs,scope){
	if(!title){
		throw new Error('参数错误');
	}
	if(!message){
		message = title;
		title = '提示';
	}
	Ext.Msg.alert(title,message,function(){
		if(callback){
			callback.call(scope|| window,callbackArgs)
		}
	});
}

window.Confirm = function( title, message, fn, scope){
	if(!title){
		throw new Error('参数错误');
	}
	if(!message){
		message = title;
		title = '提示';
	}
	Ext.Msg.confirm(title,message,fn,scope);
}