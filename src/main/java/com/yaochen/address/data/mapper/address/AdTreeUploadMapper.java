/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.address.AdTreeUpload;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_TREE_UPLOAD")
public interface AdTreeUploadMapper {
    int deleteByPrimaryKey(Integer addrId);

    int insert(AdTreeUpload record);

    int insertSelective(AdTreeUpload record);

    AdTreeUpload selectByPrimaryKey(Integer addrId);

    int updateByPrimaryKeySelective(AdTreeUpload record);

    int updateByPrimaryKey(AdTreeUpload record);
}