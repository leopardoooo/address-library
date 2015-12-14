LogCmp=function(){
	var $win = $('#logModel');
	/*操作员*/
	var $logOptrSearchBtn = $('#logOptrSearchBtn'),$logOptrSearchResult=$('#logOptrSearchResult'),
	/*上级节点*/
	$logPidSearchBtn = $('#logPidSearchBtn'),$logPidSearchResult = $('#logPidSearchResult'),$formFieldPrefix='logSearch'
		/** 地址级别组件 **/
		$logSearchlevel=$('#logSearchlevel'), desc = ">li>a"
		;
	var userLinkTpl = '<li><a href="#" data-addr-index="#{index}" data-optr-id="#{userid}">#{username}</a></li>';
	var addrLinkTpl =  '<li><a href="#" data-addr-index="#{index}" data-addr-id="#{addrId}">#{str1}</a></li>';
	
	var pagingHeader = '<li class="dropdown-header">共#{viewedCount}/#{totalCount}条相关的地址，按“←”或“→”方向键显示上下页内容</li>';
	var nopagingHeader = '<li class="dropdown-header">共#{totalCount}条相关的地址。</li>';
	
	
	var pageConfig = {
			prevTpl: '<button class="btn btn-default" data-type="prev" title="首页"><b class="glyphicon glyphicon-chevron-left"></b></button> \n',
			nextTpl: '<button class="btn btn-default" data-type="next" title="末页"><b class="glyphicon glyphicon-chevron-right"></b></button> \n',
			numTPl: '<button class="btn btn-default" data-type="num" title="第#{0}页" data-num="#{0}" >#{0}</button> \n',
			activeTPl: '<button class="btn btn-default active" data-type="active" title="当前页">#{0}</button> \n',
			maxBlock: 9  
		};
	//要取值的field
	var targetFields =["logSearchpid","logSearchstartDate","logSearchendDate","logSearchaddrName","logSearchchangeType","logSearcholdValue","logSearchnewValue","logSearchchangeOptrId","logSearchlevel"];
	var limit = 10;
	
	return {
		/**
		 * 分页查询父节点.
		 */
		doPageAddrPid:function(d){
			if(!LogCmp.addrParentData ) return;
			var start = LogCmp.addrParentData.offset, total = LogCmp.addrParentData.totalCount;
			if(total <= limit) return;
			if(d < 0 && start - limit < 0) return; // pre
			if(d > 0 && start + limit >= total) return;  // next
			start += d > 0 ? limit : -limit;
			LogCmp.doSearchAddrParent(start);
		},
		/**
		 * 初始化各个查询组件...
		 */
		initQueryCmps:function(){
			
			var win = $('.modal-dialog');
//			win.width( $(window).width() * 0.8 );//resize窗口
			//初始化时间组件
			$('.form_date').datetimepicker({
		        language:  'zh-CN', todayBtn:  1,
				autoclose: 1,todayHighlight: 1,
				startView: 2,minView: 2,forceParse: 0
		    }).datetimepicker('reset');
			//初始化地址级别
			common.post('tree/findAllLevelInSession', {}, function(data){
				data = data || [];
				var raw = '<option value="" selected="selected">请选择地址级别</option>';
				$logSearchlevel.html(raw);
				for(var index = 0;index<data.length;index++){
					var lev = data[index];
					$logSearchlevel.append( '<option value="'+ lev.levelNum +'" >'+ lev.levelName +'('+ lev.levelNum +')'+'</option>');
				}
			});
			
			//初始化tooltip
			$("[data-toggle='tooltip']").tooltip(); 
			
			for(var index =0;index < targetFields.length;index ++){
				var name = targetFields[index];
				$('#'+ name).val('');
			}
			
			$('#' + $formFieldPrefix + 'changeOptrId').attr('data-optr-id','');
			$('#' + $formFieldPrefix + 'pid').attr('data-addr-pid','');
			
		},
		initialize:function(){
			/*首先初始化各个组件的监听函数*/
			/*操作员查询组件*/
			$logOptrSearchBtn.click(function(e){
				e.preventDefault();
				e.stopPropagation();
				LogCmp.doSearchOptr();
			});
			/*父节点查询组件*/
			$logPidSearchBtn.click(function(e){
				e.preventDefault();
				e.stopPropagation();
				LogCmp.doSearchAddrParent();
			});
			
			$win.on("show.bs.modal", function(e){
				if(e.target.id == "logModel"){
					LogCmp.initQueryCmps();
				}
			});
			//关闭模态窗口的时候，刷新地址
			$win.on("hide.bs.modal", function(e){
				//
			});
			
			// 分页条事件注册
			$("#logPagingTool").click(function(e){
				var tag = e.target.tagName, $target = null;
				
				if(/button/i.test(tag)){
					$target = $(e.target);
				}else if(/b/i.test(tag)){
					$target = $(e.target).parent();
				}else{
					return;
				}
				
				var type = $target.attr("data-type");
				if(type === "active") return;
				var start = 0, currentStart = LogCmp.data["offset"], totalCount = LogCmp.data["totalCount"];
				switch(type){
				case "next": 
					start = (totalCount % limit > 0) ? (totalCount - totalCount % limit) : (totalCount - limit); 
					break;
				case "prev":
					start = 0;
					break;
				default: 
					start = (parseInt($target.attr("data-num")) - 1) * limit;
				}
				if(start != currentStart){
					LogCmp.showLog( start);
				}
			});
		},
		empty:function(el,str){
			if($(el).html) $(el).html('<li class="empty">' + (str || "加载中...") + '</li>');
			if($(el).val) $(el).val('');
		},
		doSelectEqFirst: function($itemParent){
			var $items = $itemParent.find(desc);
			if ($items.length > 0)
				$items.eq(0).trigger("focus");
		},
		doSearchOptr:function(){//查询操作员
			var q = $("#logSearchchangeOptrId").val();
			//过滤特殊字符
			if(common.filterSpecialCharsIfPossible(q, $("#logSearchchangeOptrId"))){
				Alert('请不要使用特殊字符.');
				return;
			}
			if(q.trim().length == 0){
				Alert('请至少输入一个字符.');
				return;
			}
			
			LogCmp.empty($("#logSearchchangeOptrId"));
			LogCmp.showMenu($("#logSearchchangeOptrId").parent());
			common.post('user/queryUsers', {userName:q}, function(data){
				data = data ||[];
				if(data.length === 0){
					LogCmp.empty($("#logOptrSearchResult"),"没有找到对应的操作员信息!");
					setTimeout(function(){
						LogCmp.hideMenu( $("#logSearchchangeOptrId").parent());
					}, 1000);
				}else{
					var links = "";
					for(var i= 0;i < data.length; i++){
						data[i]["index"] = i;
						links += String.format(userLinkTpl, data[i]);
					}
					links += '<li class="divider"></li>';
					$logOptrSearchResult.html(links);
					
					$('#logOptrSearchResult' + desc).click(function(){
						LogCmp.hideMenu($('#logSearchchangeOptrId').parent());
						$('#logSearchchangeOptrId').val($(this).text())
							.attr("data-optr-id", $(this).attr("data-optr-id"));
					});
					
					LogCmp.doSelectEqFirst($logOptrSearchResult);
				}
			});
		},
		doSearchAddrParent:function(start){//查询上级节点
			if(!start) start = 0;
			var qinp = $('#logSearchpid'); 
			var q = qinp.val();
			//过滤特殊字符
			if(common.filterSpecialCharsIfPossible(q,qinp)){
				Alert('请不要使用特殊字符.');
				return;
			}
			if(q.trim().length == 0){
				Alert('请至少输入一个字符.');
				return;
			}
			
			LogCmp.empty(qinp);
			LogCmp.showMenu(qinp.parent());
			common.post('tree/queryParentTree', {addrName:q,"start": start,"limit": limit}, function(data){
				data = data ||{records:[]};
				LogCmp.addrParentData = data;
				if(data.records.length === 0){
					
					LogCmp.empty($("#logPidSearchResult"),"没有找到对应的父节点!");
					setTimeout(function(){
						LogCmp.hideMenu(qinp.parent());
					}, 1000);
					
					LogCmp.empty(qinp,"没有找到对应的父节点!");
				}else{
					var links = "";
					for(var i= 0;i < data.records.length; i++){
						data.records[i]["index"] = i;
						links += String.format(addrLinkTpl, data.records[i]);
					}
					links += '<li class="divider"></li>';
					var viewedCount = data.offset + data.records.length;
					data.viewedCount = viewedCount;
					links += String.format(data.totalCount > limit ? pagingHeader : nopagingHeader, data);
					
					$logPidSearchResult.html(links);
					
					$('#logPidSearchResult' + desc).click(function(){
						LogCmp.hideMenu($('#logSearchpid').parent());
						$('#logSearchpid').val($(this).text())
							.attr("data-addr-pid", $(this).attr("data-addr-id"));
					});
					
					$logPidSearchResult.keydown(function(e){
						if (!/(37|38|39|40)/.test(e.which)) return;
						var $items = $(this).find(desc);
						if (!$items.length) return;

						var index = $items.index(e.target);
						
						if (e.which == 38) index-- ;  								// up
						if (e.which == 40 && index < $items.length - 1) index++ ;  // down
						if(index == -1){ 
							$('#logSearchpid').trigger("focus");
							LogCmp.hideMenu($('#logSearchpid').parent());
							return;
						}
						if (!~index)  index = 0 ;
						$items.eq(index).trigger('focus');
						
						if(e.which === 37) LogCmp.doPageAddrPid(-1); // <- 'prev'
						if(e.which === 39) LogCmp.doPageAddrPid(1); // -> 'next'
					});
					
					LogCmp.doSelectEqFirst($logPidSearchResult);
				}
			});

		},
		showLogWin:function(){
			$win.attr('userLog',true);
			$win.modal('show');
		},
		showMenu:function(item){
			item.addClass("open"); 
		},
		hideMenu:function(item){
			item.removeClass("open"); 
		},
		getFormValues:function(){
			var values = {};
			for(var index =0;index < targetFields.length;index ++){
				var name = targetFields[index];
				var rawVal = $('#'+ name).val();
				values[name.replace($formFieldPrefix, '')] = rawVal;
			}
			values.changeOptrId = $('#' + $formFieldPrefix + 'changeOptrId').attr('data-optr-id');
			values.pid = $('#' + $formFieldPrefix + 'pid').attr('data-addr-pid');
			if(!$('#logSearchpid').val()){
				values.pid = null;
			}
			if(!$('#logSearchchangeOptrId').val()){
				values.changeOptrId = null;
			}
			return values;
		},
		exportCurrentPage:function(){//导出当前页面
			var params = LogCmp.getFormValues();
			if(LogCmp.data){
				params.start = LogCmp.data["offset"] ||0;
			}else{
				params.start = 0;
			}
			params.limit = limit;
			var paramStr = '?1=1';
			var index =0;
			for (var name in params){
				var val = params[name];
				if(val != null){
					paramStr +=  '&' + name + '='+val ;
				}
			}
			var url = 'tree/exportLogs' + paramStr;
			window.open(url);
		},
		exportAllPages : function() {// 导出当前页面
			//TODO 进度条先不要
			/*
			var pb = 
			'<div class="progress progress-striped active" style="100%">'
				+'<div class="progress-bar progress-bar-success" role="progressbar"' 
				   +'aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" '
			    	  +'   style="width: 100%;">'
			+'<span class="sr-only">40% 完成</span>'
			      +'</div>'
			+'</div>';
			LogCmp.currentPosition = 0;
			$("#logModelTable").parent().append(pb);
			LogCmp.runProgress();
			*/
			var params = LogCmp.getFormValues();
			params.start = 0;
			params.limit = -1;
			var paramStr = '?1=1';
			var index =0;
			for (var name in params){
				var val = params[name];
				if(val != null){
					paramStr +=  '&' + name + '='+val ;
				}
			}
			var url = 'tree/exportLogs' + paramStr;
			window.open(url);
			
		},
		runProgress:function(){
			LogCmp.currentPosition+=10;
            $(".progress-bar").css("width",LogCmp.currentPosition+"%");
            if(LogCmp.currentPosition<100){
                var timer=setTimeout("LogCmp.runProgress()",500);
            }else{
                alert("加载完毕！");
            }
		},
		showLog:function(start){
			//获取需要的参数.
			var params = LogCmp.getFormValues();
			start = start || 0;
			var url = 'tree/queryAllLog';
			params.start = start;
			params.limit = limit;
			
			common.post(url, params, function(responseData){
				LogCmp.data = responseData;
				LogCmp.renderer(responseData);
			});
		},
		shinkAndAddTip:function(str){//增加提示
			return '<a href="javascript:void()" class="tooltip-hide" data-toggle="tooltip"' +  
		      'data-placement="bottom" title="' + str + '">' + String.hideMiddle(str,14); + '</a>';
		},
		renderer:function(responseData){
			var table = $('#logModelTable');
			var records = responseData.records;
			var html = '';
			var className = 'success';
			//序号 	变更类型 	地址名 	变更属性 	变更前 	变更后 	地址级别 	上级节点 	操作员
			var rowTpl = '<tr class="#{className}">'
				+'<td>#{orderIndex}</td>'					
				+'<td>#{changeCause}</td>'					
				+'<td>#{addrName}</td>'						
				+'<td>#{columnDesc}</td>'
				+'<td>#{oldValue}</td>'
				+'<td>#{newValue}</td>'
				+'<td>#{levelName}(#{addrLevel})</td>'
				+'<td>#{addrPid}</td>'
				+'<td>#{changeOptrName}</td>'
				+'</tr>';
			for (var index = 0; index < records.length; index++) {
				className = index % 2 ==1 ? 'info': 'success';
				var rec = records[index];
				rec.str1 = LogCmp.shinkAndAddTip(rec.str1);
				rec.changeCause = LogCmp.shinkAndAddTip(rec.changeCause);
				rec.className = className;
				rec.orderIndex = (1 + index);
				html += String.format(rowTpl,rec);
			}
			table.children('tbody').html(html);
			LogCmp.doRenderPaging();
		},
		doRenderPaging: function(){
			
			var start = LogCmp.data["offset"], 
				total = LogCmp.data["totalCount"],
				currentPage = start / limit + 1,
				totalPage = Math.floor(total / limit) + (total % limit > 0 ? 1 : 0);
			if(total == 0){
				$("#logModelTable").children('tbody').html("没有数据");
				$('#logPagingTool').html("");
				return;
			}
			
			var halfNum = Math.floor(pageConfig.maxBlock / 2),
			leftBlock = currentPage - halfNum,
			rightBlock = currentPage + pageConfig.maxBlock - halfNum; 
			
			if(totalPage <= pageConfig.maxBlock){
				leftBlock = 1;
				rightBlock = totalPage;
			}else{
				if(leftBlock <= 0 ){ 
					rightBlock += -leftBlock;
					leftBlock = 1;
				}else{
					rightBlock --;
				} 
				if(rightBlock > totalPage){
					leftBlock -= rightBlock - totalPage;
					rightBlock = totalPage;
				}
			}
			
			var links = String.format(pageConfig.prevTpl) ;
			for(var i = leftBlock; i <= rightBlock; i++){
				links += String.format(pageConfig[currentPage === i?"activeTPl":"numTPl"], i);
			}
			links += String.format(pageConfig.nextTpl); 
			$("#logPagingTool").html(links);
		}
	}
}();
