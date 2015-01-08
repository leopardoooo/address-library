AddressEdit = function(){
	
	var $isBlankInput = $("#editFormIsBlank"),
		$addrLevelInput = $("#editFormAddrLevel"),
		$editFormAddrId = $('#editFormAddrId'),
		$editTypeInput = $("#editFormAddrType"),
		$addrUseInput = $("#editFormAddrUse"),
		$editFormAddrName = $("#editFormAddrName"),
		$editFormAddrFullName=$('#editFormAddrFullName'),
		$editFullNameInput = $('#editFormFullAddrName');
		$addrCountyId = $('#editFormCountyId');
	
	var $win = $("#modAddressModal");
	var lastAddrTreeObj = null;
	var that = null;
	var mode = "single"; // batch
	
	function toggleMode(){
		mode = mode == "single" ? "batch" : "single";
	}
	function isBatchMode(){
		return mode === "batch";
	}
	
	return {
		initialize: function(){
			that = AddressEdit;
			$win.on("show.bs.modal", function(e){
				var treeObj = AddressDisplay.getLastAddrTreeObj();
				lastAddrTreeObj = treeObj;
				// 阻止显示
				if(!treeObj){ 
					e.preventDefault();
					e.stopPropagation();
				}else{
					that.loadForm(treeObj);
				}
			});
			//关闭模态窗口的时候，刷新地址
			$win.on("hide.bs.modal", function(e){
				var treeObj = AddressDisplay.getLastAddrTreeObj();
				if($win.attr('someDataChanged') == 'true'){
					Address.doShowAddressById(treeObj.addrId);
					$win.attr('someDataChanged',false);
				}
			});
			
			$editFormAddrName.keyup(function(e){
				var __that = $(this);
				setTimeout(function(){
					var str1 = lastAddrTreeObj.str1;
					var arr = str1.split('/');
					arr.reverse().shift();
					arr.reverse();
					$editFormAddrFullName.html(arr.join('') + "" + __that.val());
				}, 100);
			});
			
			$isBlankInput.change(function(){
				if($(this).val() == "T"){
					//留空禁用地址名
					$('#editFormAddrName').val('留空');
					$('#editFormAddrName').attr("disabled", "disabled");
				}else{
					if('留空'== $('#editFormAddrName').val()){
						$('#editFormAddrName').val('');
					}
					$('#editFormAddrName').removeAttr("disabled");
				}
			});
			
			$("#editFormOnlySave").click(that.doOnlySave);
		},
		loadForm: function(treeObj){
			lastAddrTreeObj = treeObj;
			var level =treeObj.addrLevel;
			$editTypeInput.val(treeObj["addrType"]);
			$addrUseInput.val(treeObj["addrUse"]);
			$addrCountyId.val(treeObj.countyId);
			$editFormAddrId.val(treeObj.addrId);
			$isBlankInput.val(treeObj.isBlankText);
			
			$editFormAddrName.val(treeObj.addrName);
			$editFormAddrFullName.html(treeObj.addrFullName);
			$('#editFormFullAddrNameLabel').html('分级名称'+"（" + (treeObj["addrLevel"] || "") + "级地址）");
			
			var str1 = treeObj["str1"];
			var privateName = treeObj["addrPrivateName"];
			var names = str1.split('/');
			var ids= privateName.split('/');
			var arr = [];
			
			var fullName = '';
			for (var index = 1; index < ids.length-1; index++) {
				var id = ids[index];
				var name = names[index-1];
				if('' == fullName){
					fullName = name;
				}else{
					fullName += '/' + name ;
				}
				var obj = {id:id,name:name,fullName:fullName};
				arr.push(obj);
				if(index < ids.length -2){
					var slash = {slash:true};
					arr.push(slash);
				}
			}
			var html = '';
			var eachLink = '<a addr-id="#{id}" title="#{fullName}" style="cursor:pointer;color:blue;" > #{name}</a>';
			for (var idx = 0; idx < arr.length; idx++) {
				var item = arr[idx];
				if(item.slash){
					html += ' / ';
				}else{
					html += String.format(eachLink,item);
				}
			}
			$editFullNameInput.html(html);
		},
		doOnlySave: function(){
			that.doSave(function(data){
				$win.modal('hide');
				$win.attr('someDataChanged',false);
				Address.doShowAddressById(lastAddrTreeObj.addrId);
			});
		},
		doSave: function(callback){
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
			if(!data.addrName || data.addrName.trim() ==''){
				Alert('地址名字不能为空!');
				return ;
			}
			// post
			common.post("tree/modTree", data, function(responseData){
				Address.doShowAddressById(lastAddrTreeObj.addrId);
				Alert("修改成功!");
				$win.modal('hide');
				$win.attr('someDataChanged',false);
			});
			
		},
		compareChange: function(){
			var changeProps = new Array();
			if(!lastAddrTreeObj){
				return changeProps;
			}
			if($editTypeInput.val() != lastAddrTreeObj["addrType"]){
				changeProps.push({name: 'addrType', input: $editTypeInput});
			}
			var addrPurpose = $addrUseInput.val();
			var addrUse = lastAddrTreeObj["addrUse"];
			if((addrPurpose || '') != (addrUse || '')){
				changeProps.push({name: 'addrUse', input: $addrUseInput});
			}
			if($editFormAddrName.val() != lastAddrTreeObj["addrName"]){
				changeProps.push({name: 'addrName', input: $editFormAddrName});
			}
			return changeProps;
		}
	};
}();
