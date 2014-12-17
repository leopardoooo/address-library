
/**
 * 选择城市
 * @param w
 */
SwitchCityModal = function(w){
	var lastActiveAddrId = null, F = {};
	var countryTpl = '<button class="btn" data-addr-id="#{addrId}" >#{addrName}</button>';
	var addrIdDesc = "data-addr-id";
	
	F = {
		initialize: function(){
			$('#switchCityModal').modal({
				show: true
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
			common.post("user/setAddrScope", {
				"pid": pid,
				"subId": subId || null
			}, function(data){
				$('#switchCityModal').modal("hide");
				alert(pidText + "/" + subIdText);
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
	
	return {
		initialize: function(){
			
			$('#searchLevelList' + desc).click(function(){
				$('#levelLabel').text($(this).text());
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
				
				// 左键
				if(e.keyCode === 37){
					alert("上一页");
				}
				// 右键
				if(e.keyCode === 39){
					alert("下一页");
				}
			});
			
			// selected
			$itemParent.click(function(e){
				var $items = $(this).find(desc);
				if (!$items.length) return;

				var index = $items.index(e.target);
				
				e.preventDefault();
				e.stopPropagation();
				
				if(index === -1) return;
				
				$input.val($(e.target).text());
				hide();
				$input.trigger("focus");
			});
		
			$input.keydown(function(e){
				if (!/(13|40)/.test(e.which)) return;
				var $items = $itemParent.find(desc);
				if ($items.length > 0)
					$items.eq(0).trigger("focus");
				show();
			});
			
			//$input.focus(show);
			$input.blur(function(e){
				hide();
			});
		}
	};
	
}(window);