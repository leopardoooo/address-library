Address = function(){
	//表格的模版
	var tpl = '<div class="item default" data-type="item" data-addr-index="#{index}">'
				+'<address>'
					+'<label class="label">#{addrLevel}</label> #{addrFullName}'
				+'</address>'
				+'<b class="down"></b>'
			+'</div>';
	
	var parentTpl = '<div class="item parent" data-type="parent" data-addr-index="#{index}">'
						+'<address>'
							+'<label class="label">#{addrLevel}</label> #{addrFullName}'
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
	
	var limit = 14;
	var that = null;
	var currentAddressDescTpl = " 已定位至 “#{str1}”，下级地址 “#{totalCount}” 个。";
	var $addrChildrenFilterTxt = $('#addrChildrenFilterTxt'),
	$addrChildrenFilterBtn = $('#addrChildrenFilterBtn');
	
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
			//结果过滤
			$addrChildrenFilterBtn.click(function(e){
				if(!that.data){
					return;
				}
				var start = that.data["offset"],total = that.data["totalCount"],
				totalPage = Math.floor(total / limit) + (total % limit > 0 ? 1 : 0);
				if(totalPage == 1 && $addrChildrenFilterBtn.attr('data-filtered') != 'false'){//取过滤标记
					Alert('数据无需过滤.');
					return;
				}
				
				var filter = $addrChildrenFilterTxt.val();
				var emptyFilter = !filter || filter.trim().length == 0;
				$addrChildrenFilterBtn.attr('data-filtered',emptyFilter);
				that.doShowAddress(that.lastAddrTreeObj, 0);
			});
		},
		doTriggerEvent: function(index, event, $parent){
			var addrTreeObj = (index == -1) ? that.lastAddrTreeObj : that.data.records[index];
			// 查看明细
			if(event === "detail"){
				that.toggleActive($parent);
				AddressDisplay.loadForm(addrTreeObj, "detail");
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
			AddressDisplay.loadForm(addrTreeObj);
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
		showBsGrid:function(addrTreeObj, start){
			addrTreeObj['addrUseText'] = (!addrTreeObj.addrUseText) ? '' : addrTreeObj.addrUseText;
			addrTreeObj['addrTypeText'] = (!addrTreeObj.addrTypeText) ? '' : addrTreeObj.addrTypeText;
			that.lastAddrTreeObj = addrTreeObj;
			start = start || 0;
			var filter = $addrChildrenFilterTxt.val();
			// show loading
			$("#resultBody").html(loadingTpl);
			// loading subtree
			var reqParam = {
					"pid": addrTreeObj["addrId"],
					"start": start,
					"filter":filter,
					"limit": limit
				};
			/*
			common.post("tree/findChildrensAndPaging", reqParam, function(data){
				$("#currentAddressLabel").text(String.format(currentAddressDescTpl, {
					addrFullName: addrTreeObj["addrFullName"],
					str1: addrTreeObj["str1"],
					totalCount: data["totalCount"]
				}));
				
			});
			*/
			
			var reqUrl = common.settings.path + "/" +"tree/findChildrensAndPaging2?1=1";
			for(var key in reqParam){
				reqUrl += '&'+key +'='+ reqParam[key];
			}
			    $("#main").bs_grid({
			 
			useFilters: false,
			  showRowNumbers: true,
			  showSortingIndicator: false,
			  useSortableLists: false,

			        ajaxFetchDataURL: reqUrl,
					dataRoot:'data',
			        row_primary_key: "treeId",
			 
			        columns: [
			            {field: "addrId", header: "编码", visible: "no"},
			            {field: "addrName", header: "地址名"},
			            {field: "str1", header: "全名"},
			            {field: "email", header: "Email", visible: "no", "sortable": "no"},
			            {field: "gender", header: "Gender"},
			            {field: "date_updated", header: "Date updated"}
			        ]
			 /*
			        sorting: [
			            {sortingName: "Code", field: "customer_id", order: "none"},
			            {sortingName: "Lastname", field: "lastname", order: "ascending"},
			            {sortingName: "Firstname", field: "firstname", order: "ascending"},
			            {sortingName: "Date updated", field: "date_updated", order: "none"}
			        ],
			 */
			
			/*
			        filterOptions: {
			            filters: [
			                {
			                    filterName: "Lastname", "filterType": "text", field: "lastname", filterLabel: "Last name",
			                    excluded_operators: ["in", "not_in"],
			                    filter_interface: [
			                        {
			                            filter_element: "input",
			                            filter_element_attributes: {"type": "text"}
			                        }
			                    ]
			                },
			                {
			                    filterName: "Gender", "filterType": "number", "numberType": "integer", field: "lk_genders_id", filterLabel: "Gender",
			                    excluded_operators: ["equal", "not_equal", "less", "less_or_equal", "greater", "greater_or_equal"],
			                    filter_interface: [
			                        {
			                            filter_element: "input",
			                            filter_element_attributes: {type: "checkbox"}
			                        }
			                    ],
			                    lookup_values: [
			                        {lk_option: "Male", lk_value: "1"},
			                        {lk_option: "Female", lk_value: "2", lk_selected: "yes"}
			                    ]
			                },
			                {
			                    filterName: "DateUpdated", "filterType": "date", field: "date_updated", filterLabel: "Datetime updated",
			                    excluded_operators: ["in", "not_in"],
			                    filter_interface: [
			                        {
			                            filter_element: "input",
			                            filter_element_attributes: {
			                                type: "text",
			                                title: "Set the date and time using format: dd/mm/yyyy hh:mm:ss"
			                            },
			                            filter_widget: "datetimepicker",
			                            filter_widget_properties: {
			                                dateFormat: "dd/mm/yy",
			                                timeFormat: "HH:mm:ss",
			                                changeMonth: true,
			                                changeYear: true,
			                                showSecond: true
			                            }
			                        }
			                    ],
			                    validate_dateformat: ["DD/MM/YYYY HH:mm:ss"],
			                    filter_value_conversion: {
			                        function_name: "local_datetime_to_UTC_timestamp",
			                        args: [
			                            {"filter_value": "yes"},
			                            {"value": "DD/MM/YYYY HH:mm:ss"}
			                        ]
			                    }
			                }
			            ]
			        }
			*/
			    });
		},
		doShowAddress: function(addrTreeObj, start){
			//TODO 
			that.showBsGrid(addrTreeObj, start);
			
			if(start  || !start ){
				return;
			}
			addrTreeObj['addrUseText'] = (!addrTreeObj.addrUseText) ? '' : addrTreeObj.addrUseText;
			addrTreeObj['addrTypeText'] = (!addrTreeObj.addrTypeText) ? '' : addrTreeObj.addrTypeText;
			that.lastAddrTreeObj = addrTreeObj;
			start = start || 0;
			var filter = $addrChildrenFilterTxt.val();
			// show loading
			$("#resultBody").html(loadingTpl);
			// loading subtree
			var reqParam = {
					"pid": addrTreeObj["addrId"],
					"start": start,
					"filter":filter,
					"limit": limit
				};
			common.post("tree/findChildrensAndPaging", reqParam, function(data){
				$("#currentAddressLabel").text(String.format(currentAddressDescTpl, {
					addrFullName: addrTreeObj["addrFullName"],
					str1: addrTreeObj["str1"],
					totalCount: data["totalCount"]
				}));
				
				that.data = data;
				
				// parent level tree 
				addrTreeObj["index"] = -1;
				addrTreeObj["addrFullNameFormat"] = addrTreeObj["addrFullNameFormat"]
					|| that.doFormatAddrName(addrTreeObj["str1"]);
				
				//TODO 
//				var links = String.format(parentTpl, addrTreeObj);
				
				var table = "<table class=\"table\" id=\"queryResultTable\" style=\"width: 860px;\">" +
						"<thead>   " +
						"<tr>      " +
						"<th>市</th>      " +
						"<th>区</th>      " +
						"<th>工业园</th>      " +
						"<th>路</th>      " +
						"<th>门牌号</th>      " +
						"<th>小区</th>      " +
						"<th>楼/栋</th>      " +
						"<th>单元</th>      " +
						"<th>层</th>      " +
						"<th>房号</th>   " +
						"</tr>" +
						"</thead>" +
						"<tbody>" +
						"</tbody>";
				
				var rowTpl = '<tr class="#{className}">'
					+'<td data-pid="#{addrPid}" border="1"> '
						+' <div><span style="padding-right:10px;">#{city}</span><span stype="float:right;" class="glyphicon glyphicon-plus"></span>'
					+'</div></td>'
					+'<td>#{county}</td>'
					+'<td>#{disc}</td>'
					+'<td>#{road}</td>'
					+'<td>#{streetNum}</td>'
					+'<td>#{society}</td>'
					+'<td>#{building}(#{addrLevel})</td>'
					+'<td>#{department}</td>'
					+'<td>#{floor}</td>'
					+'<td>#{doorNum}</td>'
					+'</tr>';
				
				var links = table;
				
				
				if(data.records.length === 0){
					links += String.format(emptyTpl, addrTreeObj);
				}else{
					for(var i = 0; i < data.records.length; i++){
						var o = data.records[i];
						o["index"] = i;
						o['addrUseText'] = (!o.addrUseText) ? '' : o.addrUseText;
						o['addrTypeText'] = (!o.addrTypeText) ? '' : o.addrTypeText;
						o["addrFullNameFormat"] = o["addrFullNameFormat"] 
							|| that.doFormatAddrName(o["str1"]);
						var arr = o.str1.split('/');
						console.log(arr);
						var param = {city:arr[0],county:arr[1],disc:arr[2],road:arr[3],streetNum:arr[4],society:arr[5],
								building:arr[6],department:arr[7],floor:arr[8],doorNum:arr[9]};
						
						CommonUtil.ApplyIf(param, o);
						links += String.format(rowTpl, param);
					}
				}
				
				links += "</table>";
				
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
		$addrBatchNamesArea = $('#addrBatchNamesArea'),
		$batchModeWithNames=false,
		$addFormBatchEndNum = $("#addFormBatchEndNum");
	
	var $win = $("#addAddressModal");
	
	var that = null;
	var parentAddressObj = null;
	var mode = "single"; // batch
	
	function toggleMode(m){
		mode = m;
	}
	function isBatchMode(){
		return mode === "batch";
	}
	
	return {
		initialize: function(){
			that = AddressAdd;
			$win.on("show.bs.modal", function(e){
				var treeObj = AddressDisplay.getLastAddrTreeObj();
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
			
			$('#addFormTabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				var el = $(e.target);
				toggleMode(el.attr('data-batch-mode'));
				$batchModeWithNames = el.attr('data-with-names') == 'true';
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
			
			var level =treeObj.addrLevel;
			if(treeObj.addrLevel == GlobalMaxLevelAllowed ){
				Alert('提示','当前要添加的地址级别低于已配置的最低级别,无法继续添加.',function(){
					$win.modal('hide');
					$win.attr('someDataChanged',false);
				});
				return ;
			}
			
			$parentFullNameInput.val(treeObj["addrFullName"]);
			$addrLevelInput.val(treeObj["addrLevel"] + 1);
			$addTypeInput.val(treeObj["addrType"]);
			$addrUseInput.val(treeObj["addrUse"]);
			$addrFullNameLabel.text(treeObj["addrFullName"]);
			$addrCountyId.val(treeObj.countyId);
			
			$addrNameInput.val("");
			$addFormAddrNamePreffix.val("");
			$addrBatchNamesArea.val("");
			$addFormAddrNameSuffix.val("");
			$addFormBatchStartNum.val("");
			$addFormBatchEndNum.val("");
		},
		doOnlySave: function(){
			that.doSave(function(data){
				$win.modal('hide');
				$win.attr('someDataChanged',false);
				Address.doShowAddressById(parentAddressObj.addrId);
			});
		},
		doSaveAndContinue: function(){
			$win.attr('someDataChanged',true);
			that.doSave(function(data){
				that.loadForm(parentAddressObj);
			});
		},
		doSaveAndToNext: function(){
			$win.attr('someDataChanged',true);
			that.doSave(function(data){
				that.loadForm(data);
			});
		},
		doSave: function(callback){
			if(isBatchMode()){
				debugger;
				if(!$batchModeWithNames && !$addFormBatchStartNum.val() && !$addFormBatchEndNum.val()){
					Alert("开始或结束数字不能为空");
					return;
				}else if($batchModeWithNames && (!$addrBatchNamesArea.val() || $addrBatchNamesArea.val().trim().length == 0) ){
					Alert("地址名不能为空");
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
				if($batchModeWithNames){
					data["batchNames"] = $addrBatchNamesArea.val();
					addrFullName = data["batchNames"];
					uri = "tree/addTreesWithNames";
				}else{
					data["addrNamePreffix"] = $addFormAddrNamePreffix.val();
					data["addrNameSuffix"] = $addFormAddrNameSuffix.val();
					data["start"] = $addFormBatchStartNum.val();
					data["end"] = $addFormBatchEndNum.val();
					uri = "tree/addTrees";
					addrFullName = parentAddressObj["addrFullName"] + data["addrNamePreffix"] 
					+ (data["start"] + "-" + data["end"]) + data["addrNameSuffix"] ;
				}
			}else{
				data["addrName"] = $addrNameInput.val();
				data["addrFullName"] = parentAddressObj["addrFullName"] + data["addrName"];
				uri = "tree/addTree";
				addrFullName = data["addrFullName"];
			}
			var message = String.format('确定要添加下级地址 "#{0}"吗', addrFullName);
			Confirm(message, {}, function(){
				//post
				common.post(uri, data, function(responseData){
					Alert("添加成功!");
					$addrNameInput.val();
					callback(responseData);
				});
			});
		}
	};
}();

/**
 * 地址编辑
 */
AddressDisplay = function(){
	
	var $fullLevel = $("#displayPanelFullLevel"),
		$fullAddrName = $("#displayPanelFullAddrName"),desc = ">li>a",
		$addrId = $("#displayPanelAddrId"),
		$isBlank=$('#displayPanelIsBlank'),
		$addrType = $("#displayPanelAddrType"),
		$addrPurpose = $("#displayPanelAddrPurpose"),
		$addrParent = $('#displayPanelAddrPid'),
		$addrCountyId = $('#displayPanelCountyId'),
		$display_panel=$('#display_panel'),
		$addrName = $("#displayPanelAddrName");
	var $collectBtn = $("#displayPanelCollectBtn");
	// 保存最后一条记录
	var lastAddrTreeObj = null; 
	var that = null;
	var mode = "childs";
	
	return {
		initialize: function(){
			that = AddressDisplay;
			
			$fullAddrName.click(function(e){
				if(!/a/i.test(e.target.tagName)) return ;
				var addrId = $(e.target).attr("addr-id");
				if(!addrId){ return; }
				//这个在编辑的panel内部,要特别提示一下
				var msgPlus = '\n 尚有正在编辑的地址没有保存，是否放弃保存？直接跳转?';
				if(confirm('是否要切换到 "' + $(e.target).attr("title") + '" ?'  )  ){
					Address.doShowAddressById(addrId);
				}
			});
			
			$("#displayPanelDeleteBtn").click(that.doDelete);
			$("#displayPanelCollectBtn").click(that.doCollect);
		},
		loadForm: function(addrTreeObj, __mode){
			mode = __mode || mode;
			lastAddrTreeObj = addrTreeObj;
			
			that.switchCollectClass();
			that.setValues(addrTreeObj);
			//如果是留空地址,不可以修改名字
			if(lastAddrTreeObj.isBlank == "T"){
				$('#displayPanelAddrName').attr("disabled", "disabled");
			}else{
				$('#displayPanelAddrName').removeAttr("disabled");
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
//				$collectBtn.attr("title", "收藏地址之后,可以方便的点击收藏条目直接定位到该地址.").find("i").attr("class", "glyphicon glyphicon glyphicon-eye-open");
				$collectBtn.text('收藏');
			}else{//已收藏
//				$collectBtn.attr("title", "收藏地址之后,可以方便的点击收藏条目直接定位到该地址.").find("i").attr("class", "glyphicon glyphicon glyphicon-eye-close");
				$collectBtn.text('取消收藏');
			}
		},
		setValues: function(addrTreeObj){
			$fullLevel.text("（" + (addrTreeObj["addrLevel"] || "") + "级地址）");
//			$fullAddrName.val(addrTreeObj["str1"]);
			var str1 = addrTreeObj["str1"];
			var privateName = addrTreeObj["addrPrivateName"];
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
			
			$fullAddrName.html(html);
			$addrId.val(addrTreeObj["addrId"]);
			$isBlank.val(addrTreeObj['isBlankText']),
			$addrType.val(addrTreeObj["addrTypeText"]);
			$addrPurpose.val(addrTreeObj["addrUseText"]);
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
			
			var confirmMessagePre = '收藏';
			if(lastAddrTreeObj.collected == 0){
				action = 'cancelCollectTree';//取消收藏
				confirmMessagePre = '取消收藏';
			}
			var message = String.format(confirmMessagePre + '“#{addrFullName}”?', lastAddrTreeObj);
			Confirm(message, {title:confirmMessagePre/*,yesTxt:confirmMessagePre*/}, function(){
				common.post("tree/"+action, {addrId: lastAddrTreeObj["addrId"]}, function(responseData){
					Alert("操作成功!");
					lastAddrTreeObj["collected"] = (lastAddrTreeObj.collected == 0 ? 1 : 0);
					that.switchCollectClass();
					Collections.doRender();
				});
			});
		},
		doDelete: function(){
			if(!lastAddrTreeObj)
				return ;
			var message = String.format('确定要删除“#{addrFullName}”?', lastAddrTreeObj);
			Confirm(message, {title:'提示',yesTxt:'确认删除',calcelTxt:'取消'}, function(){
				common.post("tree/delTree", {addrId: lastAddrTreeObj["addrId"]}, function(responseData){
					debugger;
					if(responseData && responseData["code"] == 312 ){
						Confirm('当前地址下仍有子节点地址,是否强制删除?',  {title:'提示',yesTxt:'确认强制删除',calcelTxt:'取消'}, function(){
							common.post("tree/delTreeForceCasecade", {addrId: lastAddrTreeObj["addrId"]}, function(responseData){
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
						});
					}else{
						Alert("删除成功!");
						Collections.doRender();
						Address.doShowAddressById(lastAddrTreeObj.addrParent);
						if(mode === "detail"){ 
							//清空表单
							that.resetForm();
						}else{
							Address.reloadAddress();
						}
					}
					
				});
			});
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
						"mergered": lastAddrTreeObj.addrId,
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

/**
 * 地址变更上级.
 */
AddressChangeLevel = function(){
	var $input = $("#searchInputForChangeLevel"), $parent = $input.parent(),
	$addrParent = $("#displayPanelAddrId"),
	$itemParent = $("#changeLevelSearchResult"), desc = ">li>a";
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
			that = AddressChangeLevel;
			
			$('#clRangeList' + desc).click(function(){
				$('#clRangeLabel').text($(this).text())
					.attr("data-level", $(this).attr("data-level"));
			});
			
			$('#clRangeBtn').click(function(){
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
			if(d > 0 && start + limit >= total) return;  // next
			start += d > 0 ? limit : -limit;
			
			AddressChangeLevel.doSearch(start);
		},
		doSearch: function(start){
			lastAddrTreeObj = AddressDisplay.getLastAddrTreeObj();
			if(!lastAddrTreeObj){
				return false;
			}
			AddressSingleMerge.empty();
			show();
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
			var sameParent = $("#clRangeLabel").attr("data-level") || true;
			common.post("tree/searchParentLevelAddrs", {
				"sl": startLevel,
				"sameParent": sameParent,
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
					var viewedCount = data.offset + data.records.length;
					data.viewedCount = viewedCount;
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
				
				var cid = $('#displayPanelCountyId').val();
				//是否要将
				var confirmMsg = '是否确定要将 "' + lastAddrTreeObj.addrFullName + '" 的上级变更为 "'+ selected.addrFullName +'" ? \n 此操作将会对上述两个地址的名字相同的子集合并!';
				Confirm(confirmMsg, {yesTxt:'确认变更'}, function(){
					common.post("tree/changeParent", {
						"pid": selected.addrId,
						"addrId": lastAddrTreeObj.addrId,
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


