/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.sys.SysNoticeCounty;
import com.yaochen.address.support.Repository;

@Repository
@Table("SYS_NOTICE_COUNTY")
public interface SysNoticeCountyMapper {
    int deleteByPrimaryKey(@Param("noticeId") Integer noticeId, @Param("countyId") String countyId);

    int insert(SysNoticeCounty record);

    int insertSelective(SysNoticeCounty record);

	void deleteByNoticeId(Integer noticeId);

	List<SysNoticeCounty> selectByNoticeId(Integer noticeId);
}