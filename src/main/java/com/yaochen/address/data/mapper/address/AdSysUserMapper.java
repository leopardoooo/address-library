package com.yaochen.address.data.mapper.address;

import java.util.List;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdSysUser;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_SYS_USER")
public interface AdSysUserMapper {
	int deleteByPrimaryKey(String userid);

    int insert(AdSysUser record);

    int insertSelective(AdSysUser record);

    AdSysUser selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(AdSysUser record);

    int updateByPrimaryKey(AdSysUser record);

	List<AdSysUser> selectByUserName(AdSysUser record);
}
