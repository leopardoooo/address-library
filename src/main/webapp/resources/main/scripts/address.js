
Address = function(){
	var tpl = '<div class="item default" data-type="item" data-addr-index="#{index}">'
				+'<address>'
					+'<label class="label">#{addrLevel}</label> #{addrFullName}'
					+'<small><i class="type">#{addrType}</i><i class="use">#{addrUse}</i></small>'
				+'</address>'
				+'<b class="down"></b>'
			+'</div>';
	
	var parentTpl = '<div class="item parent" data-type="parent" data-addr-index="#{index}">'
						+'<address>'
							+'<label class="label">#{addrLevel}</label> #{addrFullName}'
							+'<small><i class="type">#{addrType}</i><i class="use">#{addrUse}</i></small>'
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
				alert("上一级，提供一个方法查找上一级的treeobj，然后调用");
				// TODO 
				common.post("tree/findTreeObjById", {
					"addrId": addrTreeObj["addrParent"],
				}, function(data){
					that.doShowAddress(addrTreeObj);
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
			$fullLevel.text("（" + (addrTreeObj["addrPrivateName"] || "") + "）");
			$fullAddrName.val(addrTreeObj["addrFullName"]);
			$addrId.val(addrTreeObj["addrId"]);
			$addrType.val(addrTreeObj["addrType"]);
			$addrPurpose.val(addrTreeObj["addrUse"]);
			$addrName.val(addrTreeObj["addrName"]);
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
	
	var $win = $("#addAddressModal");
	
	var that = null;
	var parentAddressObj = null;
	
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
			if($isBlankInput.val() === "F" && !$addrNameInput.val()){
				Alert("地址名称不能为空!");
			}
			var data = {
				addrName: $addrNameInput.val(),
				isBlank: $isBlankInput.val(),
				addrParent: parentAddressObj["addrId"],
				addrLevel: parentAddressObj["addrLevel"] + 1,
				addrType: $addTypeInput.val(),
				addrUse: $addrUseInput.val()
			};
			
			data["addrFullName"] = parentAddressObj["addrFullName"] + data["addrName"];
			
			if(!confirm(String.format('确定要为添加下级地址#{0}吗', data["addrFullName"] ))){
				return ;
			}
			
			//post
			common.post("tree/addTree", data, function(responseData){
				Alert("添加成功!");
				$addrNameInput.val();
				callback(responseData);
			});
		}
	};
}();