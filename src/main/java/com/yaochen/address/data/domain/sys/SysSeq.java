package com.yaochen.address.data.domain.sys;

public class SysSeq {
	private Integer seqValue;
	private String seqKey;

	public Integer getSeqValue() {
		return seqValue;
	}

	public void setSeqValue(Integer seqValue) {
		this.seqValue = seqValue;
	}

	public String getSeqKey() {
		return seqKey;
	}

	public void setSeqKey(String seqKey) {
		this.seqKey = seqKey;
	}

	public SysSeq(Integer seqValue, String seqKey) {
		super();
		this.seqValue = seqValue;
		this.seqKey = seqKey;
	}

	public SysSeq() {
		super();
	}

}
