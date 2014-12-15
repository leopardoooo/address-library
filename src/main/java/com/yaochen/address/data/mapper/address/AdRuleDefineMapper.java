/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdRuleDefine;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_RULE_DEFINE")
public interface AdRuleDefineMapper {
    int deleteByPrimaryKey(Integer ruleId);

    int insert(AdRuleDefine record);

    int insertSelective(AdRuleDefine record);

    AdRuleDefine selectByPrimaryKey(Integer ruleId);

    int updateByPrimaryKeySelective(AdRuleDefine record);

    int updateByPrimaryKey(AdRuleDefine record);
}