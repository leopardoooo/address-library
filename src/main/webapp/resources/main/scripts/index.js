
/**
 * 选择城市
 * @param w
 */
SwitchCityModal = function(w){
	var lastActiveAddrId = null, F = {};
	var countryTpl = '<button class="btn" data-addr-id="#{addrId}" >#{addrName}</button>';
	var addrIdDesc = "data-addr-id",
		scopeText = null;
	
	F = {
		initialize: function(text){
			scopeText = text === "null" ? null : text;
			$('#switchCityModal').modal({
				show: !scopeText
			}).on("hide.bs.modal", function(e){
				if(!scopeText){
					Alert("必须选择一个城市!");
					e.preventDefault();
					e.stopPropagation();
					return false;
				}
			});
			
			// 按钮切换样式
			var desc = 'btn-success';
			$('#switchCityModal .item-list').click(function(e){
				if(!/button/i.test(e.target.tagName)) return;
				var activeBtn = $(this).find('.' + desc);
				
				activeBtn.removeClass(desc);
				$(e.target).addClass(desc);
				// text
				$(this).find("p>label").text($(e.target).text());
			});
			
			//点击城市
			$("#cityList").find("button").click(function(){
				var addrId = $(this).attr("data-addr-id");
				if(lastActiveAddrId === addrId){
					return;
				}
				lastActiveAddrId = addrId;
				F.doSubAddrList(lastActiveAddrId);
			});
			
			//确定按钮
			$('#switchCityModalOkBtn').click(function(){
				var $cityItems = $("#cityList").find('.' + desc);
				if($cityItems.length === 0){
					alert("必须选择一个城市");
					return ;
				}
				
				if($cityItems.length > 1){
					alert("只能选择一个城市");
					return ;
				}
				
				var $countryItems = $("#countyList").find('.' + desc);
				if($countryItems.length > 1){
					alert("只能选择一个城区或县");
					return ;
				}
				F.doSubmit($cityItems.attr(addrIdDesc), $cityItems
						.text(), $countryItems.attr(addrIdDesc), $countryItems.text());
			});
		},
		doSubmit: function(pid, pidText, subId, subIdText){
			var scopeText = pidText + "/" + subIdText;
			common.post("user/setAddrScope", {
				"pid": pid,
				"subId": subId || null,
				"scopeText": scopeText
			}, function(data){
				common.href("index");
			});
		},
		doSubAddrList: function(parentAddrId){
			common.post("tree/findChildrens", {
				"pid": parentAddrId
			}, function(data){
				$('#countyListLabel').text('');
				
				var links = "";
				for(var i = 0 ;i < data.length; i++){
					links += String.format(countryTpl, (data[i]));
				}
				if(!links){
					links = '<p class="empty">（没有数据）</p>';
				}
				$("#countyList").html(links);
			});
		}
	};
	
	return F;
}(window);


/***
 * 首页搜索封装，完全依赖页面的元素
 * @param W
 * @returns {___anonymous_Search}
 */
Search = function(W){
		
	var $input = $("#searchInput"), $parent = $input.parent(),
		$itemParent = $("#matchingResult"), desc = ">li>a";
	
	var hide = function(){ $parent.removeClass("open"); },
		show = function(){ $parent.addClass("open"); };
		
	var limit = 11;
	var linkTpl = '<li><a href="#" data-addr-index="#{index}">#{addrFullName}</a></li>';
	var pagingHeader = '<li class="dropdown-header">共#{offset}/#{totalCount}条相关的地址，按“←”或“→”方向键显示上下页内容</li>';
	var nopagingHeader = '<li class="dropdown-header">共#{totalCount}条相关的地址。</li>';
	
	return {
		initialize: function(){
			
			$('#searchLevelList' + desc).click(function(){
				$('#levelLabel').text($(this).text())
					.attr("data-level", $(this).attr("data-level"));
			});
		
			$itemParent.keydown(function(e){
				if (!/(37|38|39|40)/.test(e.which)) return;
				var $items = $(this).find(desc);
				if (!$items.length) return;

				var index = $items.index(e.target);
				
				if (e.which == 38) index-- ;  								// up
				if (e.which == 40 && index < $items.length - 1) index++ ;  // down
				if(index == -1){ 
					$input.trigger("focus");
					hide();
					return;
				}
				if (!~index)  index = 0 ;
				$items.eq(index).trigger('focus');
				
				if(e.which === 37) Search.doPage(-1); // <- 'prev'
				if(e.which === 39) Search.doPage(1); // -> 'next'
			});
			// selected
			$itemParent.click(function(e){
				var $items = $(this).find(desc);
				if (!$items.length) return;

				var index = $items.index(e.target);
				
				e.preventDefault();
				e.stopPropagation();
				
				if(index === -1) return;
				hide();
				$input.trigger("focus");
				// 
				Search.determineSearch($(e.target).attr("data-addr-index"));
			});
		
			$input.keydown(function(e){
				if (!/(13|40)/.test(e.which)) return;
				if(e.which === 13) Search.doSearch(0);
				if(e.which === 40) {
					show();
					Search.doSelectEqFirst();
				}
			});
			
			$("#indexSearchBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				Search.doSearch(0);
			});
			$(document).click(hide);
		},
		// 
		doPage: function(d){
			if(!Search.data ) return;
			var start = Search.data.offset, total = Search.data.totalCount;
			if(total <= limit) return;
			if(d < 0 && start - limit < 0) return; // pre
			if(d > 0 && start + limit > total) return;  // next
			start += d > 0 ? limit : -limit;
			
			Search.doSearch(start);
		},
		doSearch: function(start){
			Search.empty();
			show();
			var startLevel = $("#levelLabel").attr("data-level") || -1;
			var q = $("#searchInput").val();
			common.post("tree/search", {
				"sl": startLevel,
				"q": q,
				"start": start,
				"limit": limit
			}, function(data){
				Search.data = data;
				if(data.records.length === 0){
					Search.empty("没有匹配的相关地址!");
				}else{
					var links = "";
					for(var i= 0;i < data.records.length; i++){
						data.records[i]["index"] = i;
						links += String.format(linkTpl, data.records[i]);
					}
					links += '<li class="divider"></li>';
					links += String.format(data.totalCount > limit ? pagingHeader : nopagingHeader, data);
					$("#matchingResult").html(links);
					Search.doSelectEqFirst();
				}
			});
		},
		empty:function(str){
			$("#matchingResult").html('<li class="empty">' + (str || "加载中...") + '</li>');
		},
		doSelectEqFirst: function(){
			var $items = $itemParent.find(desc);
			if ($items.length > 0)
				$items.eq(0).trigger("focus");
		},
		// 选择一个结果集
		determineSearch: function(index){
			if(Search.data && Search.data.records.length > index){
				Address.doShowAddress(Search.data.records[index]);
			}
		}
	};
	
}(window);