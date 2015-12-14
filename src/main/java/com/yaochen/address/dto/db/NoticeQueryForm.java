package com.yaochen.address.dto.db;

import java.util.Date;

public class NoticeQueryForm {
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
    private String effDate;

    /** 失效日期 */
    private String invalidDate;

    /** 状态 */
    private String status;
    
    private String read;//是否已读
    
    private String countyId;//用于查询
    
    private String optr;//操作员,用于查询

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
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
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
		this.createOptr = createOptr;
	}

	public String getEffDate() {
		return effDate;
	}

	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}

	public String getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getOptr() {
		return optr;
	}

	public void setOptr(String optr) {
		this.optr = optr;
	}
	
}
