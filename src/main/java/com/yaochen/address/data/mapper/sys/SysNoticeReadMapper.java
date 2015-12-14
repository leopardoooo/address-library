/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.sys;

import org.apache.ibatis.annotations.Param;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.sys.SysNoticeRead;
import com.yaochen.address.support.Repository;

@Repository
@Table("SYS_NOTICE_READ")
public interface SysNoticeReadMapper {
    int deleteByPrimaryKey(@Param("noticeId") Integer noticeId, @Param("optrId") String optrId);

    int insert(SysNoticeRead record);

    int insertSelective(SysNoticeRead record);

    SysNoticeRead selectByPrimaryKey(@Param("noticeId") Integer noticeId, @Param("optrId") String optrId);

    int updateByPrimaryKeySelective(SysNoticeRead record);

    int updateByPrimaryKey(SysNoticeRead record);
}