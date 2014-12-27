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
		}
		
};
/**
 * @param args 预留的参数位
 * @returns
 */
function validate(args) {
	var level = addr.getAddrLevel();
	var addrName = addr.getAddrName();
	return validator['lev'+level].call(this,addrName);
};