/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdSysCfg;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_SYS_CFG")
public interface AdSysCfgMapper {
    int deleteByPrimaryKey(String paramId);

    int insert(AdSysCfg record);

    int insertSelective(AdSysCfg record);

    AdSysCfg selectByPrimaryKey(String paramId);

    int updateByPrimaryKeySelective(AdSysCfg record);

    int updateByPrimaryKey(AdSysCfg record);
}