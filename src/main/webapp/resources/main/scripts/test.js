/**
 * 地址单个地址合并.
 */
AddressSingleMerge = function(){
	var $input = $("#searchInputForSingleMerge"), $parent = $input.parent(),
	$addrParent = $("#displayPanelAddrId"),
	$itemParent = $("#singleMergeSearchResult"), desc = ">li>a";
	var hide = function(){ $parent.removeClass("open"); },
		show = function(){ $parent.addClass("open"); };
		
	var limit = 11;
	var linkTpl = '<li><a href="#" data-addr-index="#{index}" data-addr-id="#{addrId}">#{str1}</a></li>';
	var pagingHeader = '<li class="dropdown-header">共#{viewedCount}/#{totalCount}条相关的地址，按“←”或“→”方向键显示上下页内容</li>';
	var nopagingHeader = '<li class="dropdown-header">共#{totalCount}条相关的地址。</li>';
	
	// 保存最后一条记录
	var lastAddrTreeObj = null; 
	var that = null;
	
	return {
		initialize: function(){
			that = AddressSingleMerge;
			
			//范围
			$('#mergeParentRangeList' + desc).click(function(){
				hide();
				$('#mergeParentRangeLabel').text($(this).text())
					.attr("data-level", $(this).attr("data-level"));
			});
			$('#mergeParentRangeBtn').click(function(){
				hide();
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
				
				if(e.which === 37) AddressSingleMerge.doPage(-1); // <- 'prev'
				if(e.which === 39) AddressSingleMerge.doPage(1); // -> 'next'
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
				AddressSingleMerge.determineSearch($(e.target).attr("data-addr-index"));
			});
		
			$input.keydown(function(e){
				if (!/(13|40)/.test(e.which)) return;
				if(e.which === 13) AddressSingleMerge.doSearch(0);
				if(e.which === 40) {
					show();
					AddressSingleMerge.doSelectEqFirst();
				}
			});
			
			$("#singleMergeSearchBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				AddressSingleMerge.doSearch(0);
			});
			$(document).click(hide);
			
		},
		// 
		doPage: function(d){
			if(!AddressSingleMerge.data ) return;
			var start = AddressSingleMerge.data.offset, total = AddressSingleMerge.data.totalCount;
			if(total <= limit) return;
			if(d < 0 && start - limit < 0) return; // pre
			if(d > 0 && start + limit >= total) return;  // next
			start += d > 0 ? limit : -limit;
			
			AddressSingleMerge.doSearch(start);
		},
		doSearch: function(start){
			lastAddrTreeObj = AddressDisplay.getLastAddrTreeObj();
			if(!lastAddrTreeObj){
				return false;
			}
			AddressSingleMerge.empty();
			show();
			var startLevel = lastAddrTreeObj.addrLevel;
			if(startLevel ==1){
				Alert('已经是最顶级,无法合并.');
				hide();
				$input.trigger("focus");
				return;
			}
			var q = $("#searchInputForSingleMerge").val();
			//过滤特殊字符
			if(common.filterSpecialCharsIfPossible(q, $("#searchInputForSingleMerge"))){
				Alert('请不要使用特殊字符.');
				return;
			}
			var sameParent = $("#mergeParentRangeLabel").attr("data-level") || true;
			common.post("tree/searchParentLevelAddrs", {
				"sl": startLevel,
				"sameParent": sameParent,
				"q": q,
				"currentId": lastAddrTreeObj.addrId,
				"start": start,
				"limit": limit
			}, function(data){
				AddressSingleMerge.data = data;
				if(data.records.length === 0){
					AddressSingleMerge.empty("没有匹配的相关地址!");
				}else{
					var links = "";
					for(var i= 0;i < data.records.length; i++){
						data.records[i]["index"] = i;
						links += String.format(linkTpl, data.records[i]);
					}
					links += '<li class="divider"></li>';
					var viewedCount = data.offset + data.records.length;
					data.viewedCount = viewedCount;
					links += String.format(data.totalCount > limit ? pagingHeader : nopagingHeader, data);
					$("#singleMergeSearchResult").html(links);
					AddressSingleMerge.doSelectEqFirst();
				}
			});
		},
		empty:function(str){
			$("#singleMergeSearchResult").html('<li class="empty">' + (str || "加载中...") + '</li>');
		},
		doSelectEqFirst: function(){
			var $items = $itemParent.find(desc);
			if ($items.length > 0)
				$items.eq(0).trigger("focus");
		},
		// 选择一个结果集
		determineSearch: function(index){
			if(AddressSingleMerge.data && AddressSingleMerge.data.records.length > index){
				var selected = AddressSingleMerge.data.records[index];
				if(!selected){
					throw new Error('未能正确的获取数据');
				}
				//选定的地址的 ID 和 全名
				
				var cid = $('#displayPanelCountyId').val();
				var confirmMsg = '是否确定要将"' + lastAddrTreeObj.addrFullName + '" 合并到 "'+  selected.addrFullName +'" ? \n 此操作将会对当前第之下的所有子集都做出相应的修改!';
				Confirm(confirmMsg, {yesTxt:'合并'}, function(){
					common.post("tree/singleMerge", {
						"merger": selected.addrId,
						"mergered": lastAddrTreeObj.addrId
					}, function(data){
						if(data && data.addrId){
							Address.doShowAddressById(data.addrId);
						}
					})
				});
			}
		}
	};
}();

var targetFields =["logSearchpid","logSearchstartDate","logSearchendDate","logSearchaddrName","logSearchchangeType","logSearcholdValue","logSearchnewValue","logSearchchangeOptrId","logSearchlevel"];
for(var index =0;index < targetFields.length;index ++){
	var name = targetFields[index];
	console.log(name + ' value is ' + $('#'+ name).val() );
	$('#'+ name).val('');
}



