/**
 * 文件的说明.
 * JS的编写一定要严格符合JS的语法要求,
 * 另外尽量在每个语句后面加上分号,因为在执行的时候会压缩成一行代码.容易出现语法错误.
 * 可以写注释,但是必须以多行注释的方式写,否则可能会在JS引擎解释执行的时候出错,导致找不到要执行的方法.
 * 
 * 本文件是每次执行的时候读取一次,不用重启服务器,因此,如果规则改变,随时编辑好了新的规则,直接覆盖服务器上的版本即可.同时也要谨慎.
 * 
 * 每个级别定义一个函数, 如 lev1:function ,函数里面写具体的验证方法.
 * 需要被验证的地址,在执行前已经被绑定到执行当前脚本的引擎中,引用名为    addr ,
 * 这个对象是java对象,直接暴漏给JS引擎,需要取数据的时候,按照java的方式调用,比如取 地址名称： addr.getAddrName()
 * 验证方式,配置格式为 lev｛级别｝ 配置一个函数，验证通过则返回null,否则返回错误信息.
 * 
 */
var reg =/[0-9]/;/**阿拉伯数字校验**/
var validator={
		/**前置验证**/
		preValid:function(){
			return true;
		},
		lev1:function(addrName){
			return null;
		},
		lev2:function(addrName){
			return null;
		},
		lev3:function(addrName){
			return null;
		},
		lev4:function(addrName){
			return null;
		},
		lev5:function(addrName){
			if(addrName.indexOf('#')>=0){
				//不能出现#
				return '五级地址不允许出现 # ,请使用汉字 "号" 代替';
			};
			return null;
		},
		lev6:function(addrName){
			if(reg.test(addrName)){
				return '六级地址不允许出现 阿拉伯数字 ,请使用汉字代替';
			};
			return null;
		},
		lev7:function(addrName){
			return null;
		},
		lev8:function(addrName){
			
			return null;
		},
		lev9:function(addrName){
			return null;
		}
		
};
/**
 * 
 * @param args 预留的参数位
 * @returns
 */
function validate(args) {
	var level = addr.getAddrLevel();
	var addrName = addr.getAddrName();
	return validator['lev'+level].call(this,addrName);
}; 