/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.std;

import java.util.Date;

public class StdServiceTeam {
    /** 维护队ID */
    private Integer teamId;

    /** 维护队名称 */
    private String teamName;

    /** 创建的操作员 */
    private String createOptr;

    /** 创建时间 */
    private Date createTime;

    /** 所属分公司 */
    private String countyId;

    /** 服务的社区基本范围 */
    private Integer baseAddrId;
    
    private String servAddrName;
    private String servAddrFullName;
    private String servAddrStr1;

    /** 状态 */
    private String status;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName == null ? null : teamName.trim();
    }

    public String getCreateOptr() {
        return createOptr;
    }

    public void setCreateOptr(String createOptr) {
        this.createOptr = createOptr == null ? null : createOptr.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId == null ? null : countyId.trim();
    }

    public Integer getBaseAddrId() {
        return baseAddrId;
    }

    public void setBaseAddrId(Integer baseAddrId) {
        this.baseAddrId = baseAddrId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getServAddrName() {
		return servAddrName;
	}

	public void setServAddrName(String servAddrName) {
		this.servAddrName = servAddrName;
	}

	public String getServAddrFullName() {
		return servAddrFullName;
	}

	public void setServAddrFullName(String servAddrFullName) {
		this.servAddrFullName = servAddrFullName;
	}

	public String getServAddrStr1() {
		return servAddrStr1;
	}

	public void setServAddrStr1(String servAddrStr1) {
		this.servAddrStr1 = servAddrStr1;
	}
    
}