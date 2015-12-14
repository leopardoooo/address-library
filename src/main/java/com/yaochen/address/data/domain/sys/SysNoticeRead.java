/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.sys;

import java.util.Date;

public class SysNoticeRead {
    /** 通知ID */
    private Integer noticeId;

    /** 操作员ID */
    private String optrId;

    /** 标记已读的时间 */
    private Date markTime;

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public String getOptrId() {
        return optrId;
    }

    public void setOptrId(String optrId) {
        this.optrId = optrId;
    }

    public Date getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Date markTime) {
        this.markTime = markTime;
    }
}