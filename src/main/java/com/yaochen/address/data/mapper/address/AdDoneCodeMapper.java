/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdDoneCode;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_DONE_CODE")
public interface AdDoneCodeMapper {
    int deleteByPrimaryKey(Integer doneCode);

    int insert(AdDoneCode record);

    int insertSelective(AdDoneCode record);

    AdDoneCode selectByPrimaryKey(Integer doneCode);

    int updateByPrimaryKeySelective(AdDoneCode record);

    int updateByPrimaryKey(AdDoneCode record);
}