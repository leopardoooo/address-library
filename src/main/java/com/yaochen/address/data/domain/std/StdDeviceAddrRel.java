/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.std;

public class StdDeviceAddrRel {
    /** 关联表主键 */
    private Integer relId;

    /** 光节点ID */
    private Integer gjdId;
    
    private String addrName;
    private String addrFullName;
    private String addrStr1;

    /** 地址ID */
    private Integer addrId;

    public Integer getRelId() {
        return relId;
    }

    public void setRelId(Integer relId) {
        this.relId = relId;
    }

    public Integer getGjdId() {
        return gjdId;
    }

    public void setGjdId(Integer gjdId) {
        this.gjdId = gjdId;
    }

    public Integer getAddrId() {
        return addrId;
    }

    public void setAddrId(Integer addrId) {
        this.addrId = addrId;
    }

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	public String getAddrFullName() {
		return addrFullName;
	}

	public void setAddrFullName(String addrFullName) {
		this.addrFullName = addrFullName;
	}

	public String getAddrStr1() {
		return addrStr1;
	}

	public void setAddrStr1(String addrStr1) {
		this.addrStr1 = addrStr1;
	}
}