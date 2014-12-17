/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import java.util.List;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdCollections;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_COLLECTIONS")
public interface AdCollectionsMapper {
    int insert(AdCollections record);
	
    int insertSelective(AdCollections record);

	List<AdCollections> selectByExample(AdCollections coll);

	void deleteByAddrAndUser(AdCollections coll);
}