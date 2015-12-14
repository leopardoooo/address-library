/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import java.util.ArrayList;
import java.util.Map;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.domain.address.AdTreeAudit;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_TREE_AUDIT")
public interface AdTreeAuditMapper {
    int deleteByPrimaryKey(Integer addrId);

    int insert(AdTreeAudit record);

    int insertSelective(AdTreeAudit record);

    /**
	 * 根据主键查询.
	 * @param sourceId
	 * @return
	 */
    AdTreeAudit selectByPK(Integer sourceId);

    int updateByPrimaryKeySelective(AdTree record);

    int updateByPrimaryKey(AdTree record);

	/**
	 * 根据关键字查询.
	 * @param pager
	 * @return
	 */
	ArrayList<AdTreeAudit> queryAuditInfo(Map<String, Object> params);

	void audit(Map<String, Object> param );

	int countAuditInfo(Map<String, Object> params);
}