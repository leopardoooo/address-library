
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