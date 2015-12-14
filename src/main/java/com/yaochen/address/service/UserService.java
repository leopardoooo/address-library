package com.yaochen.address.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.data.domain.address.AdSysUser;
import com.yaochen.address.data.mapper.address.AdSysUserMapper;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.ThreadUserParamHolder;

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

	public List<AdSysUser> queryUsers(String userName) {
		AdSysUser user = new AdSysUser();
		user.setUsername(userName);
		String countyId = ThreadUserParamHolder.getGlobeCountyId();
		if(!BusiConstants.StringConstants.COUNTY_ALL.equals(countyId)){
			user.setCountyId(countyId);
		}
		return adSysUserMapper.selectByUserName(user);
	}
	
}
