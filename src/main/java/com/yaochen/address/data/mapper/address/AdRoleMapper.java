/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdRole;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_ROLE")
public interface AdRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(AdRole record);

    int insertSelective(AdRole record);

    AdRole selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(AdRole record);

    int updateByPrimaryKey(AdRole record);
}