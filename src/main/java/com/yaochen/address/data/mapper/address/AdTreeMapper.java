/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.address;

import java.util.List;
import java.util.Map;

import com.easyooo.framework.sharding.annotation.Table;
import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_TREE")
public interface AdTreeMapper {
    int deleteByPrimaryKey(Integer addrId);

    int insert(AdTree record);

    int insertSelective(AdTree record);

    AdTree selectByPrimaryKey(Map<String, Object> map);

    int updateByPrimaryKeySelective(AdTree record);

    int updateByPrimaryKey(AdTree record);

	/**
	 * 根据关键字查询.
	 * @param pager
	 * @return
	 */
	List<AdTree> selectByKeyWord(Pagination pager);

	List<AdTree> selectByPid(Pagination pager);

	/**
	 * 查询收藏.
	 * @param pager
	 * @return
	 */
	List<AdLevel> selectUserCollection(Pagination pager);

	/**
	 * 更新地址全名,同时更新所有的下级.
	 * @param tree
	 */
	void updateFullNameAndChildren(AdTree tree);

	/**
	 * 根据关键字等条件,查询指定级别的地址.
	 * @param pager
	 * @return
	 */
	List<AdTree> selectByKeyWordLevel(Pagination pager);
}