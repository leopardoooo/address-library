/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.mapper.std;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.std.StdServiceTeam;
import com.yaochen.address.support.Repository;

@Table("STD_SERVICE_TEAM")
@Repository
public interface StdServiceTeamMapper {
    int deleteByPrimaryKey(Integer teamId);

    int insert(StdServiceTeam record);

    int insertSelective(StdServiceTeam record);

    StdServiceTeam selectByPrimaryKey(Integer teamId);

    int updateByPrimaryKeySelective(StdServiceTeam record);

    int updateByPrimaryKey(StdServiceTeam record);
}