
// 通用处理函数与业务无关
common = {
	version: 0.1,
	settings: {
		// 上下问路径
		path: null
	},
	// ajax post
	post: function(url, data, success, dataType){
		if(common.settings.path){
			url = common.settings.path + "/" + url;
		}
		dataType = dataType || "json";
		return $.post(url, data, function(responseData){
			if(responseData && responseData["code"] == 200){
				success(responseData["data"]);
			}else if(responseData.code == '307'){//没有  登录
				common.href('');
				debugger;
			}else{
//				alert("Ajax Error! code: " + responseData["code"] + ", message: " + responseData["message"]);
				Alert("Ajax Error!</br> code: " + responseData["code"]
				+ "</br> message: " + responseData["message"]);
			}
		}, dataType);
	},
	href: function(url){
		if(common.settings.path){
			url = common.settings.path + "/" + url;
		}
		window.location.href = url;
	},
	/**检查是否有特殊字符,如果传入了 input 对象,则直接将其置空**/
	filterSpecialCharsIfPossible:function(q,input){
		var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]");
		if(pattern.test(q)){
			if(input && input.val){
				input.val('');
			}
			return true;
		}
		return false;
	}
};

/**
 * 如果只有一个参数,这个参数就是消息(msg),此时标题为默认的"提示".
 * 如果有两个以上,依次是如下
 * @param title 标题, 默认的是 提示.
 * @param msg	消息
 * @param callback 回调.
 * @param callbackScope 回调的scope.
 * @param callBackArgs 回调的参数,以数组形式传入.
 */
Alert = function(){
	var argLength = arguments.length;
	if(argLength == 0){
		throw new Error('调用"Alert"函数参数错误,至少需要一个参数.');
	}
	var title,msg,callback,callbackScope,callBackArgs;
	//推定参数
	if(argLength ==1){//只有一个参数的时候,arg[0]就是消息内容.
		msg = arguments[0];
		title = "提示";
	}else{
		title = arguments[0];
		msg = arguments[1];
		callback = arguments[2];
		callbackScope = arguments[3];
		callBackArgs = arguments[4];
	}
	//开始执行
	var desc = "alertModal";
	if(document.getElementById(desc)){
		$('#alertModalLabel').html(title);//标题
		$("#alertBody").html(msg);//内容
		var modalObj = $('#' + desc).modal("show");//渲染
	}else{
		alert(msg);
	}
	if(callback){
		callback.call( callbackScope || {},callBackArgs );
	}
};

/**
 * opts参数为“Object”时，替换目标字符串中的#{property name}部分。<br> 
 * opts为“string...”时，替换目标字符串中的#{0}、#{1}...部分。 
 */
String.format = String.format || function(source, opts) {
	source = String(source);  
    var data = Array.prototype.slice.call(arguments, 1), toString = Object.prototype.toString;  
    if (data.length) {  
        data = data.length == 1 ?  
        /* ie 下 Object.prototype.toString.call(null) == '[object Object]' */  
        (opts !== null && (/\[object Array\]|\[object Object\]/.test(toString  
                        .call(opts))) ? opts : data) : data;  
        return source.replace(/#\{(.+?)\}/g, function(match, key) {  
            var replacer = data[key];  
            // chrome 下 typeof /a/ == 'function'  
            if ('[object Function]' == toString.call(replacer)) {  
                replacer = replacer(key);  
            }  
            return ('undefined' == typeof replacer ? '' : replacer);  
        });  
    }  
    return source; 
};

