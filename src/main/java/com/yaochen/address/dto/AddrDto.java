package com.yaochen.address.dto;

import java.util.List;

import com.yaochen.address.data.domain.address.AdTree;

public class AddrDto extends AdTree {
	private List<AddrDto> children;

	public List<AddrDto> getChildren() {
		return children;
	}

	public void setChildren(List<AddrDto> children) {
		this.children = children;
	}
}
