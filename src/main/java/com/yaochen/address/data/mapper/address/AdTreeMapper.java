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
import com.yaochen.address.dto.AddrDto;
import com.yaochen.address.support.Repository;

@Repository
@Table("AD_TREE")
public interface AdTreeMapper {
    int deleteByPrimaryKey(Integer addrId);

    int insert(AdTree record);

    int insertSelective(AdTree record);

    AdTree selectByPrimaryKey(Map<String, Object> map);
    
    /**
	 * 根据主键查询.
	 * @param sourceId
	 * @return
	 */
	AdTree selectByPK(Integer sourceId);

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
	 * 根据关键字等条件,查询指定级别的地址.
	 * @param pager
	 * @return
	 */
	List<AdTree> selectByKeyWordLevel(Pagination pager);

	/**
	 * 查询直接下级的子节点.
	 * @param pager
	 * @return
	 */
	List<AdTree> selectDirectChildrenByPid(AdTree param);

	/**
	 * 变更直接子节点的父级.
	 * @param target
	 */
	void updateDirectChildrensParent(Map<String, Object> param);

	/**
	 * 更新其他属性.
	 * @param param
	 */
	void updateDirectChildrensOtherField(Map<String, Object> param);

	/**
	 * 查询指定ID的所有后代.只返回部分属性.
	 * @param sourceId
	 * @return
	 */
	List<AddrDto> selectAllPosterityForMerge(AdTree param);

	List<AdTree> queryAddrByName(Pagination pager);

	List<AdTree> selectAllInfoForPage(Map<String, Object> qm);

	/**
	 * 根据各种条件查询.
	 * @param startLevel
	 * @param endLevel
	 * @param addrName
	 * @param addrParent
	 * @param addrType
	 * @param start
	 * @param limit
	 * @return
	 */
	List<AdTree> queryComplex(Pagination pager);

	/**
	 * 审核不通过
	 * @param addrIds
	 */
	void deleteAddrNotProved(Integer[] addrIds);

	/**
	 * 审核通过
	 * @param addrIds
	 */
	void saveAddrProved(Integer[] addrIds);

	List<AdTree> queryRelatedAddrs(Map<String, Object> pager);

	List<AdTree> queryCountiesByCountyIds(Map<String, Object> param);

}