/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdRoleRes;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_ROLE_RES")
public interface AdRoleResMapper {
    int deleteByPrimaryKey(@Param("roleId") Integer roleId, @Param("resType") String resType, @Param("resCode") String resCode);

    int insert(AdRoleRes record);

    int insertSelective(AdRoleRes record);

	List<AdRoleRes> selectByRoleId(@Param("roleId") Integer roleId);
}