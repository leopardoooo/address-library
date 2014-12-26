package com.yaochen.address.common;

public enum BusiCodeConstants {
	ADD_ADDR("新增地址"),
	ADD_ADDR_BATCH("批量新增地址"),
	EDIT_ADDR("编辑地址"),
	DEL_ADDR("删除地址"),
	COLLECT_ADDR("收藏地址"),
	DE_COLLECT_ADDR("取消收藏地址"),
	MERGE("合并地址"),
	CHANGE_PARENT("变更上级"),
	EMPTY("无业务");
	
	private String desc;
	private BusiCodeConstants(String desc) {
		this.desc = desc;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}