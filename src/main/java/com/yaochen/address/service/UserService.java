package com.yaochen.address.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.address.data.domain.address.AdSysUser;
import com.yaochen.address.data.mapper.address.AdSysUserMapper;
import com.yaochen.address.dto.UserInSession;

@Service
public class UserService {
	@Autowired private AdSysUserMapper adSysUserMapper;

	/**
	 * 更新用户在本系统中的信息.
	 * @param login
	 * @throws Throwable
	 */
	public void updateUserInfo(UserInSession login) throws Throwable{
		AdSysUser user = new AdSysUser();
		user.setCountyId(login.getCompanyOID());
		user.setUserid(login.getUserOID());
		user.setUsername(login.getUserName());
		user.setPassword(login.getPassword());
		user.setRoleId(login.getRoleIdInSys());
		AdSysUser exists = adSysUserMapper.selectByPrimaryKey(user.getUserid());
		if(null == exists){
			adSysUserMapper.insert(user);
		}else{
			adSysUserMapper.updateByPrimaryKey(user);
		}
	}
	
}
