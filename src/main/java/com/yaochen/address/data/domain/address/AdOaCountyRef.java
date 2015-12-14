/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdOaCountyRef implements Serializable{
    /** OA系统的分公司ID */
    private String companyId;

    /** 地址库的 */
    private String countyId;
    
    private String pid;//上级ID

    /** 分公司名称 */
    private String countyName;

    /** 描述信息 */
    private String remark;
    
    private List<AdOaCountyRef> children = new ArrayList<AdOaCountyRef>();

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName == null ? null : countyName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public List<AdOaCountyRef> getChildren() {
		return children;
	}

	public void setChildren(List<AdOaCountyRef> children) {
		this.children = children;
	}
    
}