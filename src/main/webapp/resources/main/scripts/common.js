
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
		if(!data.$ajaxRequestWiredFlagName){
			data.$ajaxRequestWiredFlagName = true;//配合后台过滤器,标记是ajax请求
		}
		return $.post(url, data, function(responseData){
			if(responseData && responseData["code"] == 200){
				success(responseData["data"]);
			}else if(responseData.code == '307'){//没有  授权
				common.href('');
			}else if(responseData.code == '311'){//没有登录
				common.href('');
			}else{
//				alert("Ajax Error! code: " + responseData["code"] + ", message: " + responseData["message"]);
//				Alert("Ajax Error!</br> code: " + responseData["code"] + "</br> message: " + responseData["message"]);
				Alert(responseData["message"]);
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
//		var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]");
		var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]");
		if(pattern.test(q)){
			if(input && input.val){
				input.val('');
			}
			return true;
		}
		return false;
	}
};

CommonUtil={
		isNumber : function(v) {
			return typeof v === 'number' && isFinite(v);
		},
		/**
		 * 拷贝属性,只拷贝目标对象里没有的.
		 * @param target
		 * @param source
		 * @returns
		 */
		ApplyIf:function(target,source){
			if(!source || !target || typeof source != 'object')return target;
			for(var field in source){
				if(!target[field]) target[field] = source[field];
			}
			return target;
		},
		/**
		 * 拷贝属性,目标对象里有,则使用source里的覆盖.
		 * @param target
		 * @param source
		 * @returns
		 */
		Apply:function(target,source){
			if(!source || !target || typeof source != 'object')return target;
			for(var field in source) target[field] = source[field];
			return target;
		}
}

CommonUtil.Apply(Function.prototype, {

    /**
    * Creates an interceptor function. The passed function is called before the original one. If it returns false,
    * the original one is not called. The resulting function returns the results of the original function.
    * The passed function is called with the parameters of the original function. Example usage:
    * <pre><code>
var sayHi = function(name){
   alert('Hi, ' + name);
}

sayHi('Fred'); // alerts "Hi, Fred"

//create a new function that validates input without
//directly modifying the original function:
var sayHiToFriend = sayHi.createInterceptor(function(name){
   return name == 'Brian';
});

sayHiToFriend('Fred');  // no alert
sayHiToFriend('Brian'); // alerts "Hi, Brian"
</code></pre>
    * @param {Function} fcn The function to call before the original
    * @param {Object} scope (optional) The scope (<code><b>this</b></code> reference) in which the passed function is executed.
    * <b>If omitted, defaults to the scope in which the original function is called or the browser window.</b>
    * @return {Function} The new function
    */
   createInterceptor : function(fcn, scope){
       var method = this;
       return !Ext.isFunction(fcn) ?
               this :
               function() {
                   var me = this,
                       args = arguments;
                   fcn.target = me;
                   fcn.method = method;
                   return (fcn.apply(scope || me || window, args) !== false) ?
                           method.apply(me || window, args) :
                           null;
               };
   },

    /**
    * Creates a callback that passes arguments[0], arguments[1], arguments[2], ...
    * Call directly on any function. Example: <code>myFunction.createCallback(arg1, arg2)</code>
    * Will create a function that is bound to those 2 args. <b>If a specific scope is required in the
    * callback, use {@link #createDelegate} instead.</b> The function returned by createCallback always
    * executes in the window scope.
    * <p>This method is required when you want to pass arguments to a callback function.  If no arguments
    * are needed, you can simply pass a reference to the function as a callback (e.g., callback: myFn).
    * However, if you tried to pass a function with arguments (e.g., callback: myFn(arg1, arg2)) the function
    * would simply execute immediately when the code is parsed. Example usage:
    * <pre><code>
var sayHi = function(name){
   alert('Hi, ' + name);
}

//clicking the button alerts "Hi, Fred"
new Ext.Button({
   text: 'Say Hi',
   renderTo: Ext.getBody(),
   handler: sayHi.createCallback('Fred')
});
</code></pre>
    * @return {Function} The new function
   */
   createCallback : function(/*args...*/){
       // make args available, in function below
       var args = arguments,
           method = this;
       return function() {
           return method.apply(window, args);
       };
   },

   /**
    * Creates a delegate (callback) that sets the scope to obj.
    * Call directly on any function. Example: <code>this.myFunction.createDelegate(this, [arg1, arg2])</code>
    * Will create a function that is automatically scoped to obj so that the <tt>this</tt> variable inside the
    * callback points to obj. Example usage:
    * <pre><code>
var sayHi = function(name){
   // Note this use of "this.text" here.  This function expects to
   // execute within a scope that contains a text property.  In this
   // example, the "this" variable is pointing to the btn object that
   // was passed in createDelegate below.
   alert('Hi, ' + name + '. You clicked the "' + this.text + '" button.');
}

var btn = new Ext.Button({
   text: 'Say Hi',
   renderTo: Ext.getBody()
});

//This callback will execute in the scope of the
//button instance. Clicking the button alerts
//"Hi, Fred. You clicked the "Say Hi" button."
btn.on('click', sayHi.createDelegate(btn, ['Fred']));
</code></pre>
    * @param {Object} scope (optional) The scope (<code><b>this</b></code> reference) in which the function is executed.
    * <b>If omitted, defaults to the browser window.</b>
    * @param {Array} args (optional) Overrides arguments for the call. (Defaults to the arguments passed by the caller)
    * @param {Boolean/Number} appendArgs (optional) if True args are appended to call args instead of overriding,
    * if a number the args are inserted at the specified position
    * @return {Function} The new function
    */
   createDelegate : function(obj, args, appendArgs){
       var method = this;
       return function() {
           var callArgs = args || arguments;
           if (appendArgs === true){
               callArgs = Array.prototype.slice.call(arguments, 0);
               callArgs = callArgs.concat(args);
           }else if (CommonUtil.isNumber(appendArgs)){
               callArgs = Array.prototype.slice.call(arguments, 0); // copy arguments first
               var applyArgs = [appendArgs, 0].concat(args); // create method call params
               Array.prototype.splice.apply(callArgs, applyArgs); // splice them in
           }
           return method.apply(obj || window, callArgs);
       };
   },
   /**
    * Calls this function after the number of millseconds specified, optionally in a specific scope. Example usage:
    * <pre><code>
var sayHi = function(name){ alert('Hi, ' + name); }
sayHi.defer(2000, this, ['Fred']);
</code></pre>
    * @param {Number} millis The number of milliseconds for the setTimeout call (if less than or equal to 0 the function is executed immediately)
    * @param {Object} scope (optional) The scope (<code><b>this</b></code> reference) in which the function is executed.
    * <b>If omitted, defaults to the browser window.</b>
    * @param {Array} args (optional) Overrides arguments for the call. (Defaults to the arguments passed by the caller)
    * @param {Boolean/Number} appendArgs (optional) if True args are appended to call args instead of overriding,
    * if a number the args are inserted at the specified position
    * @return {Number} The timeout id that can be used with clearTimeout
    */
   defer : function(millis, obj, args, appendArgs){
       var fn = this.createDelegate(obj, args, appendArgs);
       if(millis > 0){
           return setTimeout(fn, millis);
       }
       fn();
       return 0;
   }
});

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
		callback.defer(500, callbackScope || {},callBackArgs );
	}
};

/**
 * 确认框.
 * 
 * @param message 提示的内容
 * @param cfg:object <ul>配置信息
 * <li>title:顶部的标题,</li> 
 * <li>yesTxt 确认按钮的文字,</li>
 * <li>cancelTxt:取消按钮的文字,</li>
 * <li>backdrop:点击其他区域的时候是否隐藏,可选 static ,或者 false,</li>
 * </ul>
 * @param callback:function	回调函数
 * @param callbackScope:object	回调函数的作用域.
 */
Confirm=function(message,cfg,callback,callbackScope){
	var defaultCfg = {title:'提醒',yesTxt:'保存',cancelTxt:'取消',backdrop:'static'};
	cfg = cfg ||{};
	CommonUtil.ApplyIf(cfg,defaultCfg);
	var el = $('#confirmModal'), $confirmModalLabel = $('#confirmModalLabel'), $confirmBody = $('#confirmBody'), $confirmCancelBtn = $('#confirmCancelBtn'), $confirmYesBtn = $('#confirmYesBtn');
	$confirmModalLabel.text(cfg.title);
	$confirmBody.html(message);
	$confirmYesBtn.text(cfg.yesTxt);
	$confirmCancelBtn.text(cfg.cancelTxt);
	
	$confirmYesBtn.unbind('click');
	callbackScope = callbackScope || {};
	$confirmYesBtn.bind('click',function(){
		if(callback) callback.createDelegate(callbackScope).call(callbackScope);
		el.modal('hide');
	});
	el.modal({
		backdrop:cfg.backdrop
	});//渲染
}
/**trim**/
String.prototype.trim = String.prototype.trim || function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}


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

