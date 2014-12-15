/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdEditTemplate;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_EDIT_TEMPLATE")
public interface AdEditTemplateMapper {
    int deleteByPrimaryKey(Integer templateId);

    int insert(AdEditTemplate record);

    int insertSelective(AdEditTemplate record);

    AdEditTemplate selectByPrimaryKey(Integer templateId);

    int updateByPrimaryKeySelective(AdEditTemplate record);

    int updateByPrimaryKey(AdEditTemplate record);
}