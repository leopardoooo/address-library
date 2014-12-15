/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_TREE")
public interface AdTreeMapper {
    int deleteByPrimaryKey(Integer addrId);

    int insert(AdTree record);

    int insertSelective(AdTree record);

    AdTree selectByPrimaryKey(Integer addrId);

    int updateByPrimaryKeySelective(AdTree record);

    int updateByPrimaryKey(AdTree record);
}