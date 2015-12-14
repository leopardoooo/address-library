/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

import java.util.Date;

public class AdTreeChangeDetail {
    /** 主键ID */
    private Integer detailId;

    /** 变化SN */
    private Integer changeSn;

    /** 地址级别 */
    private Integer addrLevel;
    
    private String str1;
    private String addrName;
    
    private String levelName;
    
    private Integer addrPid;

    /** 内容更改、合并删除、审核失败 */
    private String changeType;
    
    /**  变更原因 */
    private String changeCause;

    /**  */
    private String changeOptrId;
    
    private String changeOptrName;

    /**  */
    private Integer changeDoneCode;

    /**  */
    private Date changeTime;

    /** 地址ID */
    private Integer addrId;

    /** 修改前的值 */
    private String oldValue;

    /** 修改后的值 */
    private String newValue;

    /** 列名 */
    private String columnName;

    /** 变更列说明 */
    private String columnDesc;

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Integer getChangeSn() {
        return changeSn;
    }

    public void setChangeSn(Integer changeSn) {
        this.changeSn = changeSn;
    }

    public Integer getAddrLevel() {
        return addrLevel;
    }

    public void setAddrLevel(Integer addrLevel) {
        this.addrLevel = addrLevel;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType == null ? null : changeType.trim();
    }

    public String getChangeCause() {
        return changeCause;
    }

    public void setChangeCause(String changeCause) {
        this.changeCause = changeCause == null ? null : changeCause.trim();
    }

    public String getChangeOptrId() {
        return changeOptrId;
    }

    public void setChangeOptrId(String changeOptrId) {
        this.changeOptrId = changeOptrId == null ? null : changeOptrId.trim();
    }

    public Integer getChangeDoneCode() {
        return changeDoneCode;
    }

    public void setChangeDoneCode(Integer changeDoneCode) {
        this.changeDoneCode = changeDoneCode;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getAddrId() {
        return addrId;
    }

    public void setAddrId(Integer addrId) {
        this.addrId = addrId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue == null ? null : oldValue.trim();
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }

    public String getColumnName() {
        return columnName;
    }

    public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc == null ? null : columnDesc.trim();
    }

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getChangeOptrName() {
		return changeOptrName;
	}

	public void setChangeOptrName(String changeOptrName) {
		this.changeOptrName = changeOptrName;
	}

	public Integer getAddrPid() {
		return addrPid;
	}

	public void setAddrPid(Integer addrPid) {
		this.addrPid = addrPid;
	}

}