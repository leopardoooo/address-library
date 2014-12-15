/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

import java.util.Date;

public class AdEditTemplate {
    /** 模板ID */
    private Integer templateId;

    /** 模板名称 */
    private String templateName;

    /** 模板描述 */
    private String templateDesc;

    /** 模板适用分公司ID(填4501表示全区通用) */
    private String countyId;

    /**  */
    private Date createTime;

    /**  */
    private String createOptrId;

    /** 状态 */
    private String status;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName == null ? null : templateName.trim();
    }

    public String getTemplateDesc() {
        return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
        this.templateDesc = templateDesc == null ? null : templateDesc.trim();
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId == null ? null : countyId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateOptrId() {
        return createOptrId;
    }

    public void setCreateOptrId(String createOptrId) {
        this.createOptrId = createOptrId == null ? null : createOptrId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}