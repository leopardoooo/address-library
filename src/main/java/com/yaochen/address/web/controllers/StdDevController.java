package com.yaochen.address.web.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yaochen.address.data.domain.std.StdDevice;
import com.yaochen.address.data.domain.std.StdServiceTeam;
import com.yaochen.address.data.domain.std.StdSteward;
import com.yaochen.address.service.StdService;
import com.yaochen.address.web.support.ReturnValueUtil;
import com.yaochen.address.web.support.Root;

@Controller
@RequestMapping("/stdDev")
public class StdDevController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired private StdService stdService;
	
	/**
	 * 查找服务节点
	 * @throws Throwable 
	 */
	@RequestMapping(value="/saveStdDev")
	@ResponseBody
	public Root<Void> saveStdDev(StdDevice dev) throws Throwable{
		logger.info("message");
		stdService.saveOrUpdateDevice(dev);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 删除服务节点.
	 * @throws Throwable 
	 */
	@RequestMapping(value="/removeStdDev")
	@ResponseBody
	public Root<Void> removeStdDev(Integer devId) throws Throwable{
		logger.info("message");
		stdService.removeDevice(devId);
		return ReturnValueUtil.getVoidRoot();
	}
	
	
	/**
	 * 保存修改服务队信息.
	 * @throws Throwable 
	 */
	@RequestMapping(value="/saveServerTeam")
	@ResponseBody
	public Root<Void> saveServerTeam(StdServiceTeam team) throws Throwable{
		logger.info("message");
		stdService.saveServerTeam(team);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 保存修改服务队信息.
	 * @throws Throwable 
	 */
	@RequestMapping(value="/matchAddr")
	@ResponseBody
	public Root<Void> matchAddr(Integer[] addrIds,Integer stdDevId) throws Throwable{
		logger.info("message");
		stdService.saveMatchAddr(stdDevId,addrIds);
		return ReturnValueUtil.getVoidRoot();
	}
	
	
	
	/**
	 * 删除服务队.
	 * @throws Throwable 
	 */
	@RequestMapping(value="/removeServerTeam")
	@ResponseBody
	public Root<Void> removeServerTeam(Integer teamId) throws Throwable{
		logger.info("message");
		stdService.removeServerTeam(teamId);
		return ReturnValueUtil.getVoidRoot();
	}
	
	
	/**
	 * 保存修改维护人员信息.
	 * @throws Throwable 
	 */
	@RequestMapping(value="/saveSteward")
	@ResponseBody
	public Root<Void> saveSteward(StdSteward server) throws Throwable{
		logger.info("message");
		stdService.saveSteward(server);
		return ReturnValueUtil.getVoidRoot();
	}
	
	/**
	 * 删除服务人员.
	 * @throws Throwable 
	 */
	@RequestMapping(value="/removeSteward")
	@ResponseBody
	public Root<Void> removeSteward(Integer optrId) throws Throwable{
		logger.info("message");
		stdService.removeSteward(optrId);
		return ReturnValueUtil.getVoidRoot();
	}
	
}
