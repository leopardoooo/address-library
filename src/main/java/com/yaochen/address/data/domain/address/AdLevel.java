/*
 * Copyright © 2014 YAOCHEN Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.address;

public class AdLevel {
    /** 层级 */
    private Integer levelNum;

    /** 层级名称 */
    private String levelName;

    /** 层级描述 */
    private String levelDesc;

    /** 层级配置示例 */
    private String levelExample;

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName == null ? null : levelName.trim();
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc == null ? null : levelDesc.trim();
    }

    public String getLevelExample() {
        return levelExample;
    }

    public void setLevelExample(String levelExample) {
        this.levelExample = levelExample == null ? null : levelExample.trim();
    }
}