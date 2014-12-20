
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
			}else{
				alert("Ajax Error! code: " + responseData["code"]
						+ ", message: " + responseData["message"]);
			}
		}, dataType);
	},
	href: function(url){
		if(common.settings.path){
			url = common.settings.path + "/" + url;
		}
		window.location.href = url;
	}
};

Alert = function(msg){
	alert(msg);
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
