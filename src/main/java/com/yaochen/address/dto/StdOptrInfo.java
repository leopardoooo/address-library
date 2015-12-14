package com.yaochen.address.dto;

import java.util.List;

import com.yaochen.address.data.domain.std.StdServiceTeam;
import com.yaochen.address.data.domain.std.StdSteward;

public class StdOptrInfo extends StdServiceTeam {
	private List<StdSteward> optrs;

	public List<StdSteward> getOptrs() {
		return optrs;
	}

	public void setOptrs(List<StdSteward> optrs) {
		this.optrs = optrs;
	}
}
