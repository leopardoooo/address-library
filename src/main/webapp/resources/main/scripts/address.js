
Address = function(){
	var tpl = '<div class="item default" data-type="item" data-addr-index="#{index}">'
				+'<address>'
					+'<label class="label">#{addrLevel}</label> #{addrFullName}'
					+'<small><i class="type">#{addrTypeText}</i><i class="use">#{addrUseText}</i></small>'
				+'</address>'
				+'<b class="down"></b>'
			+'</div>';
	
	var parentTpl = '<div class="item parent" data-type="parent" data-addr-index="#{index}">'
						+'<address>'
							+'<label class="label">#{addrLevel}</label> #{addrFullName}'
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
	
	var limit = 10;
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
		doShowAddress: function(addrTreeObj){
			that.lastAddrTreeObj = addrTreeObj;
			
			// show loading
			$("#resultBody").html(loadingTpl);
			// loading subtree
			common.post("tree/findChildrensAndPaging", {
				"pid": addrTreeObj["addrId"],
				"start": 0,
				"limit": limit
			}, function(data){
				$("#currentAddressLabel").text(String.format(currentAddressDescTpl, {
					addrFullName: addrTreeObj["addrFullName"],
					totalCount: data["totalCount"]
				}));
				
				$("#resultPagingTool").html("一共" + data["totalCount"] + "条.");
				
				that.data = data;
				
				// parent level tree 
				addrTreeObj["index"] = -1;
				var links = String.format(parentTpl, addrTreeObj);
				
				if(data.records.length === 0){
					links += String.format(emptyTpl, addrTreeObj);
				}else{
					for(var i = 0; i < data.records.length; i++){
						var o = data.records[i];
						o["index"] = i;
						links += String.format(tpl, o);
					}
				}
				$("#resultBody").html(links);
				
				// 激活ptree
				that.activeParent(addrTreeObj);
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
		$addrName = $("#editFormAddrName");
	
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
			that.setValues(addrTreeObj);
		},
		resetForm: function(){
			lastAddrTreeObj = null;
			that.setValues({
				addrUse: "OTHERS"
			});
		},
		setValues: function(addrTreeObj){
			$fullLevel.text("（" + (addrTreeObj["addrLevel"] || "") + "级地址）");
			$fullAddrName.val(addrTreeObj["str1"]);
			$addrId.val(addrTreeObj["addrId"]);
			$addrType.val(addrTreeObj["addrType"]);
			$addrPurpose.val(addrTreeObj["addrUse"]);
			$addrName.val(addrTreeObj["addrName"]);
		},
		doCollect: function(){
			alert("收藏");
		},
		doDelete: function(){
			if(!lastAddrTreeObj)
				return ;
			
			if(confirm(String.format('确定要删除“#{addrFullName}”?', lastAddrTreeObj))){
				// post
				common.post("tree/delTree", {addrId: lastAddrTreeObj["addrId"]}, function(responseData){
					Alert("删除成功!");
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
 * 添加下级地址
 */
AddressAdd = function(){
	
	var $parentFullNameInput = $("#addFormParentAddrFullName"),
		$isBlankInput = $("#addFormIsBlank"),
		$addrLevelInput = $("#addFormAddrLevel"),
		$addTypeInput = $("#addFormAddrType"),
		$addrUseInput = $("#addFormAddrUse"),
		$addrNameInput = $("#addFormAddrName"),
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
					$("#batchAddInAddFormLi").hide();
				}else{
					$("#batchAddInAddFormLi").show();
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
			
			$addrNameInput.val("");
			$addFormAddrNamePreffix.val("");
			$addFormAddrNameSuffix.val("");
			$addFormBatchStartNum.val("");
			$addFormBatchEndNum.val("");
		},
		doOnlySave: function(){
			that.doSave(function(data){
				$win.modal('hide');
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