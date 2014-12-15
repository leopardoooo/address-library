/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

public class AdEditTemplateDetail {
    /** 模板ID */
    private Integer templateId;

    /** 层级 */
    private Integer levelNum;

    /** 层级定义类型 */
    private String configType;

    /** 层级展示明细 */
    private String configJson;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType == null ? null : configType.trim();
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson == null ? null : configJson.trim();
    }
}