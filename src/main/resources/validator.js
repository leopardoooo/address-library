var reg =/[0-9]/;
var validator={
		preValid:function(){
			return true;
		},
		lev1:function(addrName){
			return 0;
		},
		lev2:function(addrName){
			return 0;
		},
		lev3:function(addrName){
			return 0;
		},
		lev4:function(addrName){
			return 0;
		},
		lev5:function(addrName){
			if(addrName.indexOf('#')>=0){
				return 1005;
			};
			return 0;
		},
		lev6:function(addrName){
			if(reg.test(addrName)){
				return 1006;
			};
			return 0;
		},
		lev7:function(addrName){
			return 0;
		},
		lev8:function(addrName){
			
			return 0;
		},
		lev9:function(addrName){
			return 0;
		},
		lev10:function(addrName){
			return 0;
		}
		
};
/**
 * @param args 预留的参数位
 * @returns
 */
function validate(args) {
	var level = addr.getAddrLevel();
	var addrName = addr.getAddrName();
	if(validator['lev'+level]){
		return validator['lev'+level].call(this,addrName);
	}else{
		/**暂时没有设置的验证函数都返回为真.有其他要求再做修改.**/
		return 0;
	}
};