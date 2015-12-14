/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdTreeChange;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_TREE_CHANGE")
public interface AdTreeChangeMapper {
    int deleteByPrimaryKey(Integer changeSn);

    int insert(AdTreeChange record);

    int insertSelective(AdTreeChange record);

    AdTreeChange selectByPrimaryKey(Integer changeSn);

    int updateByPrimaryKeySelective(AdTreeChange record);

    int updateByPrimaryKey(AdTreeChange record);

}