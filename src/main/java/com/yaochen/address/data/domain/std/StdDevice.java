/*
 * Copyright © 2015 nirack Corporation, All Rights Reserved.
 */
package com.yaochen.address.data.domain.std;

import java.util.Date;

import com.yaochen.address.common.BusiConstants;

public class StdDevice {
	/** 光节点ID */
	private Integer stdDevId;

	/** 级别 */
	private String stdLevel;
	
	private String stdLevelName;

	/** 名称 */
	private String jdName;

	/** 节点所在的地址 */
	private Integer jdAddrId;

	private String jdAddrName;
	private String jdAddrFullName;
	private String jdAddrStr1;

	/** 上级ID */
	private Integer pid;

	/** 所属分公司 */
	private String countyId;

	/** 录入操作员 */
	private String createOptr;

	/** 录入时间 */
	private Date createTime;

	public Integer getStdDevId() {
		return stdDevId;
	}

	public void setStdDevId(Integer stdDevId) {
		this.stdDevId = stdDevId;
	}

	public String getStdLevel() {
		return stdLevel;
	}

	public void setStdLevel(String stdLevel) {
		this.stdLevel = stdLevel == null ? null : stdLevel.trim();
		if(null != this.stdLevel){
			this.stdLevelName = BusiConstants.GzLevel.valueOf(stdLevel).getName();
		}
	}
	
	public String getJdName() {
		return jdName;
	}

	public void setJdName(String jdName) {
		this.jdName = jdName == null ? null : jdName.trim();
	}

	public Integer getJdAddrId() {
		return jdAddrId;
	}

	public void setJdAddrId(Integer jdAddrId) {
		this.jdAddrId = jdAddrId;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId == null ? null : countyId.trim();
	}

	public String getCreateOptr() {
		return createOptr;
	}

	public void setCreateOptr(String createOptr) {
		this.createOptr = createOptr == null ? null : createOptr.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getJdAddrName() {
		return jdAddrName;
	}

	public void setJdAddrName(String jdAddrName) {
		this.jdAddrName = jdAddrName;
	}

	public String getJdAddrFullName() {
		return jdAddrFullName;
	}

	public void setJdAddrFullName(String jdAddrFullName) {
		this.jdAddrFullName = jdAddrFullName;
	}

	public String getJdAddrStr1() {
		return jdAddrStr1;
	}

	public void setJdAddrStr1(String jdAddrStr1) {
		this.jdAddrStr1 = jdAddrStr1;
	}

	public String getStdLevelName() {
		return stdLevelName;
	}

}