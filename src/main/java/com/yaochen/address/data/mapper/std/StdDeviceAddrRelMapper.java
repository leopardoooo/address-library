/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.std;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.std.StdDeviceAddrRel;
import com.yaochen.address.support.Repository;

@Table("STD_DEVICE_ADDR_REL")
@Repository
public interface StdDeviceAddrRelMapper {
    int deleteByPrimaryKey(Integer relId);

    int insert(StdDeviceAddrRel record);

    int insertSelective(StdDeviceAddrRel record);

    StdDeviceAddrRel selectByPrimaryKey(Integer relId);

    int updateByPrimaryKeySelective(StdDeviceAddrRel record);

    int updateByPrimaryKey(StdDeviceAddrRel record);

	void deleteByDevId(Integer gjdId);
}