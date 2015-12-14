/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.std;

public class StdSteward {
    /** 维护人员ID */
    private Integer stewardId;

    /** 维护人员ID */
    private String stewardName;

    /** 所属服务队的ID */
    private Integer teamId;

    public Integer getStewardId() {
        return stewardId;
    }

    public void setStewardId(Integer stewardId) {
        this.stewardId = stewardId;
    }

    public String getStewardName() {
        return stewardName;
    }

    public void setStewardName(String stewardName) {
        this.stewardName = stewardName == null ? null : stewardName.trim();
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
}