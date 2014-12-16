/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import java.util.List;

import com.easyooo.framework.sharding.annotation.Table;
import com.easyooo.framework.support.mybatis.Pagination;
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

	/**
	 * 根据关键字查询.
	 * @param pager
	 * @return
	 */
	List<AdTree> selectByKeyWord(Pagination pager);

	List<AdTree> selectByPid(Pagination pager);
}