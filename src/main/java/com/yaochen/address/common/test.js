var array = [ 'addr_id', 'addr_name', 'addr_level', 'addr_type', 'addr_use',
		'is_blank', 'addr_parent', 'addr_private_name', 'addr_full_name',
		'addr_code', 'county_id', 'create_time', 'create_optr_id',
		'create_done_code', 'status', 'str1', 'str2', 'str3', 'str4', 'str5' ]
for (var index = 0; index < array.length; index++) {
	var name = array[index];
	var fmt = ' Object ' + name + ' = rst.getObject("' + name + '");';
	console.log(fmt);
	console.log('map.put("' + name + '",' + name + ' )');
	
}