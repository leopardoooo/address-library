/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdOaCountyRef;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_OA_COUNTY_REF")
public interface AdOaCountyRefMapper {
    int deleteByPrimaryKey(Integer companyId);

    int insert(AdOaCountyRef record);

    int insertSelective(AdOaCountyRef record);

    AdOaCountyRef selectByPrimaryKey(String companyId);

    int updateByPrimaryKeySelective(AdOaCountyRef record);

    int updateByPrimaryKey(AdOaCountyRef record);
}