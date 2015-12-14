/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.std;

import java.util.List;
import java.util.Map;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.std.StdDevice;
import com.yaochen.address.support.Repository;

@Table("STD_DEVICE")
@Repository
public interface StdDeviceMapper {
    int deleteByPrimaryKey(Integer stdDevId);

    int insert(StdDevice record);

    int insertSelective(StdDevice record);

    StdDevice selectByPrimaryKey(Integer stdDevId);

    int updateByPrimaryKeySelective(StdDevice record);

    int updateByPrimaryKey(StdDevice record);

	List<StdDevice> queryChildren(Map<String, Object> param);
}