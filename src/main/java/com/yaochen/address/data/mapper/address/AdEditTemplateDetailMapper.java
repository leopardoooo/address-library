/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import org.apache.ibatis.annotations.Param;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdEditTemplateDetail;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_EDIT_TEMPLATE_DETAIL")
public interface AdEditTemplateDetailMapper {
    int deleteByPrimaryKey(@Param("templateId") Integer templateId, @Param("levelNum") Integer levelNum);

    int insert(AdEditTemplateDetail record);

    int insertSelective(AdEditTemplateDetail record);

    AdEditTemplateDetail selectByPrimaryKey(@Param("templateId") Integer templateId, @Param("levelNum") Integer levelNum);

    int updateByPrimaryKeySelective(AdEditTemplateDetail record);

    int updateByPrimaryKey(AdEditTemplateDetail record);
}