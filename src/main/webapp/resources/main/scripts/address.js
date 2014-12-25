Address = function(){
	var tpl = '<div class="item default" data-type="item" data-addr-index="#{index}">'
				+'<address>'
					+'<label class="label">#{addrLevel}</label> #{addrFullNameFormat}'
					+'<small><i class="type">#{addrTypeText}</i><i class="use">#{addrUseText}</i></small>'
				+'</address>'
				+'<b class="down"></b>'
			+'</div>';
	
	var parentTpl = '<div class="item parent" data-type="parent" data-addr-index="#{index}">'
						+'<address>'
							+'<label class="label">#{addrLevel}</label> #{addrFullNameFormat}'
							+'<small><i class="type">#{addrTypeText}</i><i class="use">#{addrUseText}</i></small>'
						+'</address>'
						+'<i class="up"></i>'
					+'</div>';
	
	var emptyTpl = '<p class="empty">'
						+'<i class="glyphicon glyphicon-map-marker"></i>'
						+'<span id="resultEmptyText">“#{addrFullName}” 还没有下级地址！</span>'
					+'</p>';
	
	var loadingTpl = '<p class="empty">'
				+'<i class="glyphicon glyphicon-cloud-download"></i>'
				+'<span id="resultEmptyText">查找中，稍等片刻...</span>'
			+'</p>';
	
	var pageConfig = {
		prevTpl: '<button class="btn btn-default" data-type="prev" title="首页"><b class="glyphicon glyphicon-chevron-left"></b></button> \n',
		nextTpl: '<button class="btn btn-default" data-type="next" title="末页"><b class="glyphicon glyphicon-chevron-right"></b></button> \n',
		numTPl: '<button class="btn btn-default" data-type="num" title="第#{0}页" data-num="#{0}" >#{0}</button> \n',
		activeTPl: '<button class="btn btn-default active" data-type="active" title="当前页">#{0}</button> \n',
		maxBlock: 9  
	};
	
	var limit = 13;
	var that = null;
	var currentAddressDescTpl = " 已定位至 “#{addrFullName}”，下级地址 “#{totalCount}” 个。";
	
	return {
		initialize: function(){
			that = Address;
			$("#resultBody").click(function(e){
				var $target = $(e.target);
				var $items = $target.closest("address", $(this));
				
				var $parent = null , event = null;
				if($items.length > 0){ // detail
					$parent = $items.parent();
					event = "detail";
				} else if(/div/i.test(e.target.tagName)){
					if($(e.target).hasClass("item")){
						$parent = $(e.target);
						event = "detail";
					}
				} else if(/(i|b)/i.test(e.target.tagName)){
					if($target.hasClass("up")){ // up
						$parent = $target.parent();
						event = "up";
					}else if($target.hasClass("down")){ // down
						$parent = $target.parent();
						event = "down";
					}
				}
				if(!event) return;
				var index = $parent.attr("data-addr-index");
				that.doTriggerEvent(index, event, $parent);
			}).scroll(function(e){
				var $hd = $("#resultHeading");
				if($(this).scrollTop() <= 0){
					$hd.removeClass("scroll");
				}else{
					if(!$hd.hasClass("scroll")){
						$hd.addClass("scroll");
					}
				}
			});
			
			// 分页条事件注册
			$("#resultPagingTool").click(function(e){
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
				var start = 0, currentStart = that.data["offset"], totalCount = that.data["totalCount"];
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
				// paging
				if(start != currentStart){
					that.doShowAddress(that.lastAddrTreeObj, start);
				}
			});
		},
		doTriggerEvent: function(index, event, $parent){
			var addrTreeObj = (index == -1) ? that.lastAddrTreeObj : that.data.records[index];
			// 查看明细
			if(event === "detail"){
				that.toggleActive($parent);
				AddressEdit.loadForm(addrTreeObj, "detail");
			}else if(event === "up"){
				if(addrTreeObj["addrParent"] == "0"){
					Alert("已经是顶级地址了!");
					return ;
				}
				common.post("tree/queryById", {
					"addrId": addrTreeObj["addrParent"],
				}, function(data){
					that.doShowAddress(data);
				});
			}else if(event === "down"){
				that.doShowAddress(addrTreeObj);
			}
		},
		reloadAddress: function(){
			that.doShowAddress(that.lastAddrTreeObj);
		},
		toggleActive: function($parent){
			if(that.lastActiveItem){
				if(that.lastActiveItem.attr("data-type") === "parent"){
					that.lastActiveItem.attr("class", "item parent");
				}else{
					that.lastActiveItem.attr("class", "item default");
				}
			}
			
			if($parent.attr("data-type") === "parent"){
				$parent.attr("class", "item parent-active");
			}else{
				$parent.attr("class", "item default-active");
			}
			that.lastActiveItem = $parent;
		},
		activeParent: function(addrTreeObj){
			var $items = $("#resultBody").find('div.item');
			that.toggleActive($items.eq(0));
			// 加载编辑表单
			AddressEdit.loadForm(addrTreeObj);
		},
		doShowAddressById: function(addrId){
			if(!addrId) return;
			// ajax post 
			common.post("tree/queryById", {
				"addrId": addrId,
			}, function(data){
				that.doShowAddress(data);
			});
		},
		doShowAddress: function(addrTreeObj, start){
			that.lastAddrTreeObj = addrTreeObj;
			
			// show loading
			$("#resultBody").html(loadingTpl);
			// loading subtree
			common.post("tree/findChildrensAndPaging", {
				"pid": addrTreeObj["addrId"],
				"start": start || 0,
				"limit": limit
			}, function(data){
				$("#currentAddressLabel").text(String.format(currentAddressDescTpl, {
					addrFullName: addrTreeObj["addrFullName"],
					totalCount: data["totalCount"]
				}));
				
				that.data = data;
				
				// parent level tree 
				addrTreeObj["index"] = -1;
				addrTreeObj["addrFullNameFormat"] = addrTreeObj["addrFullNameFormat"]
					|| that.doFormatAddrName(addrTreeObj["str1"]);
				var links = String.format(parentTpl, addrTreeObj);
				
				if(data.records.length === 0){
					links += String.format(emptyTpl, addrTreeObj);
				}else{
					for(var i = 0; i < data.records.length; i++){
						var o = data.records[i];
						o["index"] = i;
						o["addrFullNameFormat"] = o["addrFullNameFormat"] 
							|| that.doFormatAddrName(o["str1"]);
						links += String.format(tpl, o);
					}
				}
				$("#resultBody").html(links);
				
				// 激活ptree
				that.activeParent(addrTreeObj);
				
				// 渲染分页
				that.doRenderPaging();
			});
		},
		/**
		 * 传入带有分割线的地址完整名称
		 */
		doFormatAddrName: function(addrFullNameSplit){
			if(!addrFullNameSplit) return "";
			
			var strs = addrFullNameSplit.split("/");
			var formatString = "";
			for(var i = 0; i< strs.length; i++){
				if(i == strs.length - 1){
					formatString += "<b>" + strs[i] +"</b>";
				}else{
					formatString += strs[i];
				}
			}
			return formatString;
		},
		doRenderPaging: function(){
			var start = that.data["offset"], 
				total = that.data["totalCount"],
				currentPage = start / limit + 1,
				totalPage = Math.floor(total / limit) + (total % limit > 0 ? 1 : 0);
			if(total == 0){
				$("#resultPagingTool").html("");
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
			$("#resultPagingTool").html(links);
		}
	};
}();

/**
 * 添加下级地址
 */
AddressAdd = function(){
	
	var $parentFullNameInput = $("#addFormParentAddrFullName"),
		$isBlankInput = $("#addFormIsBlank"),
		$addrLevelInput = $("#addFormAddrLevel"),
		$addTypeInput = $("#addFormAddrType"),
		$addrUseInput = $("#addFormAddrUse"),
		$addrNameInput = $("#addFormAddrName"),
		$addrCountyId = $('#addFormCountyId'),
		$addrFullNameLabel = $("#addFormAddrFullName");
	var $addFormAddrNamePreffix = $("#addFormAddrNamePreffix"),
		$addFormAddrNameSuffix = $("#addFormAddrNameSuffix"),
		$addFormBatchStartNum = $("#addFormBatchStartNum"),
		$addFormBatchEndNum = $("#addFormBatchEndNum");
	
	var $win = $("#addAddressModal");
	
	var that = null;
	var parentAddressObj = null;
	var mode = "single"; // batch
	
	function toggleMode(){
		mode = mode == "single" ? "batch" : "single";
	}
	function isBatchMode(){
		return mode === "batch";
	}
	
	return {
		initialize: function(){
			that = AddressAdd;
			$win.on("show.bs.modal", function(e){
				var treeObj = AddressEdit.getLastAddrTreeObj();
				// 阻止显示
				if(!treeObj){ 
					e.preventDefault();
					e.stopPropagation();
				}else{
					that.loadForm(treeObj);
				}
			});
			
			$('#addFormTabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				toggleMode();
				if(isBatchMode()){
					$("#addFormSaveToNextBtn").hide();
					$("#addFormSaveContinue").hide();
				}else{
					$("#addFormSaveToNextBtn").show();
					$("#addFormSaveContinue").show();
				}
			});
			
			$isBlankInput.change(function(){
				if($(this).val() == "T"){
					$('#addFormTabs a[href="#singleAddInAddForm"]').tab('show');
					$("#batchAddInAddFormLi").hide();
					//留空禁用地址名
					$('#addFormAddrName').val('留空');
					$('#addFormAddrName').attr("disabled", "disabled");
					setTimeout(function(){
						$addrFullNameLabel.text(parentAddressObj["addrFullName"] + "/" + '留空' );
					}, 100);
				}else{
					$("#batchAddInAddFormLi").show();
					if('留空'== $('#addFormAddrName').val()){
						$('#addFormAddrName').val('');
						setTimeout(function(){
							$addrFullNameLabel.text(parentAddressObj["addrFullName"] + "/");
						}, 100);
					}
					$('#addFormAddrName').removeAttr("disabled");
				}
			});
			
			$addrNameInput.keyup(function(e){
				var __that = $(this);
				setTimeout(function(){
					$addrFullNameLabel.text(parentAddressObj["addrFullName"] + "/" + __that.val());
				}, 100);
			});
			
			$("#addFormOnlySave").click(that.doOnlySave);
			$("#addFormSaveContinue").click(that.doSaveAndContinue);
			$("#addFormSaveToNextBtn").click(that.doSaveAndToNext);
		},
		loadForm: function(treeObj){
			parentAddressObj = treeObj;
			
			$parentFullNameInput.val(treeObj["addrFullName"]);
			$addrLevelInput.val(treeObj["addrLevel"] + 1);
			$addTypeInput.val(treeObj["addrType"]);
			$addrUseInput.val(treeObj["addrUse"]);
			$addrFullNameLabel.text(treeObj["addrFullName"]);
			$addrCountyId.val(treeObj.countyId);
			
			$addrNameInput.val("");
			$addFormAddrNamePreffix.val("");
			$addFormAddrNameSuffix.val("");
			$addFormBatchStartNum.val("");
			$addFormBatchEndNum.val("");
		},
		doOnlySave: function(){
			that.doSave(function(data){
				$win.modal('hide');
				Address.doShowAddressById(parentAddressObj.addrId);
			});
		},
		doSaveAndContinue: function(){
			that.doSave(function(data){
				that.loadForm(parentAddressObj);
			});
		},
		doSaveAndToNext: function(){
			that.doSave(function(data){
				that.loadForm(data);
			});
		},
		doSave: function(callback){
			if(isBatchMode()){
				if(!$addFormBatchStartNum.val() && !$addFormBatchEndNum.val()){
					Alert("开始或结束数字不能为空");
					return;
				}
			}else{
				if($isBlankInput.val() === "F" && !$addrNameInput.val()){
					Alert("地址名称不能为空!");
					return;
				}
			}
			
			var data = {
				isBlank: $isBlankInput.val(),
				addrParent: parentAddressObj["addrId"],
				addrLevel: parentAddressObj["addrLevel"] + 1,
				addrType: $addTypeInput.val(),
				countyId:$addrCountyId.val(),
				addrUse: $addrUseInput.val()
			}, uri = null, addrFullName = null;
			
			// 如果是批量模式
			if(isBatchMode()){
				data["addrNamePreffix"] = $addFormAddrNamePreffix.val();
				data["addrNameSuffix"] = $addFormAddrNameSuffix.val();
				data["start"] = $addFormBatchStartNum.val();
				data["end"] = $addFormBatchEndNum.val();
				uri = "tree/addTrees";
				addrFullName = parentAddressObj["addrFullName"] + data["addrNamePreffix"] 
					+ (data["start"] + "-" + data["end"]) + data["addrNameSuffix"] ;
			}else{
				data["addrName"] = $addrNameInput.val();
				data["addrFullName"] = parentAddressObj["addrFullName"] + data["addrName"];
				uri = "tree/addTree";
				addrFullName = data["addrFullName"];
			}
			
			if(!confirm(String.format('确定要为添加下级地址 "#{0}"吗', addrFullName))){
				return ;
			}
			
			//post
			common.post(uri, data, function(responseData){
				Alert("添加成功!");
				$addrNameInput.val();
				callback(responseData);
			});
		}
	};
}();

/**
 * 地址编辑
 */
AddressEdit = function(){
	
	var $fullLevel = $("#editFormFullLevel"),
		$fullAddrName = $("#editFormFullAddrName"),
		$addrId = $("#editFormAddrId"),
		$addrType = $("#editFormAddrType"),
		$addrPurpose = $("#editFormAddrPurpose"),
		$addrParent = $('#editFormAddrPid'),
		$addrCountyId = $('#editFormCountyId'),
		$addrName = $("#editFormAddrName");
	var $collectBtn = $("#editFormCollectBtn");
	
	// 保存最后一条记录
	var lastAddrTreeObj = null; 
	var that = null;
	var mode = "childs";
	
	return {
		initialize: function(){
			that = AddressEdit;
			$("#editFormSaveBtn").click(that.doUpdate);
			$("#editFormDeleteBtn").click(that.doDelete);
			$("#editFormCollectBtn").click(that.doCollect);
		},
		loadForm: function(addrTreeObj, __mode){
			mode = __mode || mode;
			lastAddrTreeObj = addrTreeObj;
			
			that.switchCollectClass();
			that.setValues(addrTreeObj);
			//如果是留空地址,不可以修改名字
			if(lastAddrTreeObj.isBlank == "T"){
				$('#editFormAddrName').attr("disabled", "disabled");
			}else{
				$('#editFormAddrName').removeAttr("disabled");
			}
		},
		resetForm: function(){
			lastAddrTreeObj = null;
			that.setValues({
				addrUse: "OTHERS"
			});
		},
		switchCollectClass: function(){
			if(lastAddrTreeObj.collected == 1){//没收藏
				$collectBtn.attr("title", "收藏")
					.find("i").attr("class", "glyphicon glyphicon glyphicon-eye-open");
			}else{//已收藏
				$collectBtn.attr("title", "取消收藏") 
					.find("i").attr("class", "glyphicon glyphicon glyphicon-eye-close");
			}
		},
		setValues: function(addrTreeObj){
			$fullLevel.text("（" + (addrTreeObj["addrLevel"] || "") + "级地址）");
			$fullAddrName.val(addrTreeObj["str1"]);
			$addrId.val(addrTreeObj["addrId"]);
			$addrType.val(addrTreeObj["addrType"]);
			$addrPurpose.val(addrTreeObj["addrUse"]);
			$addrName.val(addrTreeObj["addrName"]);
			$addrParent.val(addrTreeObj["addrParent"]);
			$addrCountyId.val(addrTreeObj['countyId']);
		},
		doCollect: function(){
			if(!lastAddrTreeObj){
				Alert('没有选中地址');
				return ;
			}
			var action = 'collectTree';
			var confirmMessagePre = '确定要收藏';
			if(lastAddrTreeObj.collected == 0){
				action = 'cancelCollectTree';//取消收藏
				confirmMessagePre = '确定要取消收藏';
			}
			if(confirm(String.format(confirmMessagePre + '“#{addrFullName}”?', lastAddrTreeObj))){
				// post
				common.post("tree/"+action, {addrId: lastAddrTreeObj["addrId"]}, function(responseData){
					Alert("操作成功!");
					lastAddrTreeObj["collected"] = (lastAddrTreeObj.collected == 0 ? 1 : 0);
					that.switchCollectClass();
					Collections.doRender();
				});
			}
		},
		doDelete: function(){
			if(!lastAddrTreeObj)
				return ;
			
			if(confirm(String.format('确定要删除“#{addrFullName}”?', lastAddrTreeObj))){
				// post
				common.post("tree/delTree", {addrId: lastAddrTreeObj["addrId"]}, function(responseData){
					Alert("删除成功!");
					Collections.doRender();
					Address.doShowAddressById(lastAddrTreeObj.addrParent);
					if(mode === "detail"){ 
						//清空表单
						that.resetForm();
					}else{
						Address.reloadAddress();
					}
				});
			}
		},
		doUpdate: function(){
			if(!lastAddrTreeObj){
				return ;
			}
			
			var changeItems = that.compareChange();
			if(that.compareChange().length == 0){
				Alert("修改前与修改后的内容一致，无需提交！");
				return ;
			}
			
			var data = {
				ignoreEmpty: true,
				addrId: lastAddrTreeObj["addrId"]
			};
			for (var i = 0; i < changeItems.length; i++) {
				data[changeItems[i].name] = changeItems[i].input.val();
			}
			
			// post
			common.post("tree/modTree", data, function(responseData){
				Address.doShowAddressById(lastAddrTreeObj.addrParent);
				Alert("修改成功!");
			});
		},
		compareChange: function(){
			var changeProps = new Array();
			if($addrType.val() != lastAddrTreeObj["addrType"]){
				changeProps.push({name: 'addrType', input: $addrType});
			}
			if($addrPurpose.val() != lastAddrTreeObj["addrUse"]){
				changeProps.push({name: 'addrUse', input: $addrPurpose});
			}
			if($addrName.val() != lastAddrTreeObj["addrName"]){
				changeProps.push({name: 'addrName', input: $addrName});
			}
			
			return changeProps;
		},
		getLastAddrTreeObj: function(){
			return lastAddrTreeObj;
		}
	};
}();


/**
 * 地址单个地址合并.
 */
AddressSingleMerge = function(){
	var $input = $("#searchInputForSingleMerge"), $parent = $input.parent(),
	$addrParent = $("#editFormAddrId"),
	$itemParent = $("#singleMergeSearchResult"), desc = ">li>a";
	var hide = function(){ $parent.removeClass("open"); },
		show = function(){ $parent.addClass("open"); };
		
	var limit = 11;
	var linkTpl = '<li><a href="#" data-addr-index="#{index}" data-addr-id="#{addrId}">#{str1}</a></li>';
	var pagingHeader = '<li class="dropdown-header">共#{offset}/#{totalCount}条相关的地址，按“←”或“→”方向键显示上下页内容</li>';
	var nopagingHeader = '<li class="dropdown-header">共#{totalCount}条相关的地址。</li>';
	
	// 保存最后一条记录
	var lastAddrTreeObj = null; 
	var that = null;
	
	return {
		initialize: function(){
			that = AddressSingleMerge;
			
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
			if(d > 0 && start + limit > total) return;  // next
			start += d > 0 ? limit : -limit;
			
			AddressSingleMerge.doSearch(start);
		},
		doSearch: function(start){
			AddressSingleMerge.empty();
			show();
			
			lastAddrTreeObj = AddressEdit.getLastAddrTreeObj();
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
			common.post("tree/searchParentLevelAddrs", {
				"sl": startLevel,
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
				
				var cid = $('#editFormCountyId').val();
				var confirmMsg = '是否确定要将"' + lastAddrTreeObj.addrFullName + '" 合并到 "'+  selected.addrFullName +'" ? \n 此操作将会对当前第之下的所有子集都做出相应的修改!';
				if(confirm( confirmMsg )){
					common.post("tree/singleMerge", {
						"merger": selected.addrId,
						"mergered": lastAddrTreeObj.addrId,
					}, function(data){
						if(data && data.addrId){
							Address.doShowAddressById(data.addrId);
						}
					})
				}
			}
		}
	};
}();

/**
 * 地址变更上级.
 */
AddressChangeLevel = function(){
	var $input = $("#searchInputForChangeLevel"), $parent = $input.parent(),
	$addrParent = $("#editFormAddrId"),
	$itemParent = $("#changeLevelSearchResult"), desc = ">li>a";
	var hide = function(){ $parent.removeClass("open"); },
		show = function(){ $parent.addClass("open"); };
		
	var limit = 11;
	var linkTpl = '<li><a href="#" data-addr-index="#{index}" data-addr-id="#{addrId}">#{str1}</a></li>';
	var pagingHeader = '<li class="dropdown-header">共#{offset}/#{totalCount}条相关的地址，按“←”或“→”方向键显示上下页内容</li>';
	var nopagingHeader = '<li class="dropdown-header">共#{totalCount}条相关的地址。</li>';
	
	// 保存最后一条记录
	var lastAddrTreeObj = null; 
	var that = null;
	
	return {
		initialize: function(){
			that = AddressChangeLevel;
			
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
				
				if(e.which === 37) AddressChangeLevel.doPage(-1); // <- 'prev'
				if(e.which === 39) AddressChangeLevel.doPage(1); // -> 'next'
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
				AddressChangeLevel.determineSearch($(e.target).attr("data-addr-index"));
			});
		
			$input.keydown(function(e){
				if (!/(13|40)/.test(e.which)) return;
				if(e.which === 13) AddressChangeLevel.doSearch(0);
				if(e.which === 40) {
					show();
					AddressChangeLevel.doSelectEqFirst();
				}
			});
			
			$("#changeLevelSearchBtn").click(function(e){
				e.preventDefault();
				e.stopPropagation();
				AddressChangeLevel.doSearch(0);
			});
			$(document).click(hide);
			
		},
		// 
		doPage: function(d){
			if(!AddressChangeLevel.data ) return;
			var start = AddressChangeLevel.data.offset, total = AddressChangeLevel.data.totalCount;
			if(total <= limit) return;
			if(d < 0 && start - limit < 0) return; // pre
			if(d > 0 && start + limit > total) return;  // next
			start += d > 0 ? limit : -limit;
			
			AddressChangeLevel.doSearch(start);
		},
		doSearch: function(start){
			AddressChangeLevel.empty();
			show();
			
			lastAddrTreeObj = AddressEdit.getLastAddrTreeObj();
			var startLevel = lastAddrTreeObj.addrLevel;
			startLevel = startLevel -1;
			if(startLevel ==1){
				Alert('当前级别,无法变更上级.');
				hide();
				$input.trigger("focus");
				return;
			}
			
			var q = $("#searchInputForChangeLevel").val();
			//过滤特殊字符
			if(common.filterSpecialCharsIfPossible(q, $("#searchInputForChangeLevel"))){
				Alert('请不要使用特殊字符.');
				return;
			}
			common.post("tree/searchParentLevelAddrs", {
				"sl": startLevel,
				"q": q,
				"currentId": lastAddrTreeObj.addrParent,
				"start": start,
				"limit": limit
			}, function(data){
				AddressChangeLevel.data = data;
				if(data.records.length === 0){
					AddressChangeLevel.empty("没有匹配的相关地址!");
				}else{
					var links = "";
					for(var i= 0;i < data.records.length; i++){
						data.records[i]["index"] = i;
						links += String.format(linkTpl, data.records[i]);
					}
					links += '<li class="divider"></li>';
					links += String.format(data.totalCount > limit ? pagingHeader : nopagingHeader, data);
					$("#changeLevelSearchResult").html(links);
					AddressChangeLevel.doSelectEqFirst();
				}
			});
		},
		empty:function(str){
			$("#changeLevelSearchResult").html('<li class="empty">' + (str || "加载中...") + '</li>');
		},
		doSelectEqFirst: function(){
			var $items = $itemParent.find(desc);
			if ($items.length > 0)
				$items.eq(0).trigger("focus");
		},
		// 选择一个结果集
		determineSearch: function(index){
			if(AddressChangeLevel.data && AddressChangeLevel.data.records.length > index){
				var selected = AddressChangeLevel.data.records[index];
				if(!selected){
					throw new Error('未能正确的获取数据');
				}
				//选定的地址的 ID 和 全名
				
				var cid = $('#editFormCountyId').val();
				//是否要将
				var confirmMsg = '是否确定要将 "' + lastAddrTreeObj.addrFullName + '" 的上级变更为 "'+ selected.addrFullName +'" ? \n 此操作将会对当前第之下的所有子集都做出相应的修改!';
				if(confirm( confirmMsg )){
					common.post("tree/changeParent", {
						"pid": selected.addrId,
						"addrId": lastAddrTreeObj.addrId,
					}, function(data){
						if(data && data.addrId){
							Address.doShowAddressById(data.addrId);
						}
					})
				}
			}
		}
	};
}();