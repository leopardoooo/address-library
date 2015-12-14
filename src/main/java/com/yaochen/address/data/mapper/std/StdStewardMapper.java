/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.std;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.std.StdSteward;
import com.yaochen.address.support.Repository;

@Table("STD_STEWARD")
@Repository
public interface StdStewardMapper {
    int deleteByPrimaryKey(Integer stewardId);

    int insert(StdSteward record);

    int insertSelective(StdSteward record);

    StdSteward selectByPrimaryKey(Integer stewardId);

    int updateByPrimaryKeySelective(StdSteward record);

    int updateByPrimaryKey(StdSteward record);
}