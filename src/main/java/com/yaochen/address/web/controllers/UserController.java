package com.yaochen.address.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/login")
	@ResponseBody
	public Root<Integer> login( )throws Throwable {
		return ReturnValueUtil.getJsonRoot(1);
	}

}
