package com.yaochen.address.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageViewController {
	private Logger logger = Logger.getLogger(getClass());
	
	@RequestMapping(value="/view/{page}")
	public String view(@PathVariable String page) throws Throwable{
		logger.info(page);
		return page;
	}
	
}
