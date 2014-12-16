/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import java.util.List;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_LEVEL")
public interface AdLevelMapper {
    int deleteByPrimaryKey(Integer levelNum);

    int insert(AdLevel record);

    int insertSelective(AdLevel record);

    AdLevel selectByPrimaryKey(Integer levelNum);

    int updateByPrimaryKeySelective(AdLevel record);

    int updateByPrimaryKey(AdLevel record);

	List<AdLevel> selectByMaxLevel(Integer levelNum);
}