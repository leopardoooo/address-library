/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

import java.util.Date;

public class AdDoneCode {
    /** 流水号 */
    private Integer doneCode;

    /** 业务代码 */
    private String busiCode;

    /** 操作时间 */
    private Date createTime;

    /** 操作员 */
    private String optrId;

    /** 备注 */
    private String remark;

    public Integer getDoneCode() {
        return doneCode;
    }

    public void setDoneCode(Integer doneCode) {
        this.doneCode = doneCode;
    }

    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode == null ? null : busiCode.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOptrId() {
        return optrId;
    }

    public void setOptrId(String optrId) {
        this.optrId = optrId == null ? null : optrId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}