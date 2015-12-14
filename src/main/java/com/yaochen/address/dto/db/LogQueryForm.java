package com.yaochen.address.dto.db;

import com.yaochen.address.common.BusiConstants.AddrChangeType;

public class LogQueryForm {
	
	/** 上级节点 **/
	private Integer pid;
	/** 修改日期开始时间 **/
	private String startDate;
	/** 修改日期结束时间 **/
	private String endDate;
	/** 名称,模糊查询 **/
	private String addrName;
	private Integer addrId;
	private AddrChangeType changeType;
	private String oldValue;
	private String newValue;
	/** 操作员 **/
	private Integer changeOptrId;
	/** 地址级别 **/
	private Integer level;
	
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getAddrName() {
		return addrName;
	}
	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}
	public AddrChangeType getChangeType() {
		return changeType;
	}
	public void setChangeType(AddrChangeType changeType) {
		this.changeType = changeType;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public Integer getChangeOptrId() {
		return changeOptrId;
	}
	public void setChangeOptrId(Integer changeOptrId) {
		this.changeOptrId = changeOptrId;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getAddrId() {
		return addrId;
	}
	public void setAddrId(Integer addrId) {
		this.addrId = addrId;
	}
	
}
