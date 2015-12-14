package com.yaochen.address.data.domain.address;

import java.util.Date;

public class AdTreeAudit  extends AdTree{
	/**
	 * 审核时间.
	 */
	private Date auditTime;

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
}
