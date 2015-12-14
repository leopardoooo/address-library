/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import java.util.List;

import com.easyooo.framework.sharding.annotation.Table;
import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.data.domain.address.AdTreeChangeDetail;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_TREE_CHANGE_DETAIL")
public interface AdTreeChangeDetailMapper {
    int deleteByPrimaryKey(Integer detailId);

    int insert(AdTreeChangeDetail record);

    int insertSelective(AdTreeChangeDetail record);

    AdTreeChangeDetail selectByPrimaryKey(Integer detailId);

    int updateByPrimaryKeySelective(AdTreeChangeDetail record);

    int updateByPrimaryKey(AdTreeChangeDetail record);

	List<AdTreeChangeDetail> queryLogDetail(Pagination pager);
}