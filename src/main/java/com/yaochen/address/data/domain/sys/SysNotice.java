/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.sys;

import java.util.Date;

public class SysNotice {
    /** 主键 */
    private Integer noticeId;

    /** 标题 */
    private String noticeTitle;

    /** 内容 */
    private String noticeContent;

    /** 创建时间 */
    private Date createTime;

    /** 创建者操作员 */
    private String createOptr;

    /** 生效日期 */
    private Date effDate;

    /** 失效日期 */
    private Date invalidDate;

    /** 状态 */
    private String status;
    
    private String read;//是否已读
    
    private String countyId;//用于查询

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle == null ? null : noticeTitle.trim();
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent == null ? null : noticeContent.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateOptr() {
        return createOptr;
    }

    public void setCreateOptr(String createOptr) {
        this.createOptr = createOptr == null ? null : createOptr.trim();
    }

    public Date getEffDate() {
        return effDate;
    }

    public void setEffDate(Date effDate) {
        this.effDate = effDate;
    }

    public Date getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(Date invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
}