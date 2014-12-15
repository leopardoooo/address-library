/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import org.apache.ibatis.annotations.Param;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdItemValue;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_ITEM_VALUE")
public interface AdItemValueMapper {
    int deleteByPrimaryKey(@Param("itemKey") String itemKey, @Param("itemValue") String itemValue);

    int insert(AdItemValue record);

    int insertSelective(AdItemValue record);

    AdItemValue selectByPrimaryKey(@Param("itemKey") String itemKey, @Param("itemValue") String itemValue);

    int updateByPrimaryKeySelective(AdItemValue record);

    int updateByPrimaryKey(AdItemValue record);
}