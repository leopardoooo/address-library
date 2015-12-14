/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.sys;

import java.util.List;
import java.util.Map;

import com.easyooo.framework.sharding.annotation.Table;
import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.data.domain.sys.SysNotice;
import com.yaochen.address.support.Repository;

@Repository
@Table("SYS_NOTICE")
public interface SysNoticeMapper {
    int deleteByPrimaryKey(Integer noticeId);

    int insert(SysNotice record);

    int insertSelective(SysNotice record);

    SysNotice selectByPrimaryKey(Integer noticeId);

    int updateByPrimaryKeySelective(SysNotice record);

    int updateByPrimaryKey(SysNotice record);

    List<SysNotice> queryUnchecked(Pagination pager);
    
    List<SysNotice> queryAllValid(Pagination pager);

	Integer countUnChecked(Map<String, String> map);

	List<SysNotice> queryAllValidForAdmin(Pagination pager);
}