package com.yaochen.address.support;

import org.springframework.stereotype.Component;

import com.yaochen.address.data.domain.address.AdTree;

@Component
public class AddrNameChecker {

	/**
	 * 返回null ,表示通过,否则返回错误信息.
	 * @param tree
	 * @param otherArgs
	 * @return
	 */
	public String check(AdTree tree,Object... otherArgs){
		//TODO 土鳖扛铁牛
		return null;
	}
	
}
