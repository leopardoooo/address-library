package com.yaochen.address.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.data.domain.address.AdOaCountyRef;
import com.yaochen.address.data.domain.std.StdDevice;
import com.yaochen.address.data.domain.std.StdDeviceAddrRel;
import com.yaochen.address.data.domain.std.StdServiceTeam;
import com.yaochen.address.data.domain.std.StdSteward;
import com.yaochen.address.data.mapper.std.StdDeviceAddrRelMapper;
import com.yaochen.address.data.mapper.std.StdDeviceMapper;
import com.yaochen.address.data.mapper.std.StdServiceTeamMapper;
import com.yaochen.address.data.mapper.std.StdStewardMapper;
import com.yaochen.address.support.ThreadUserParamHolder;

@Service
public class StdService {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired private StdDeviceMapper stdDeviceMapper;
	@Autowired private StdDeviceAddrRelMapper stdDeviceAddrRelMapper;
	@Autowired private StdServiceTeamMapper stdServiceTeamMapper;
	@Autowired private StdStewardMapper stdStewardMapper;
	
	public List<StdDevice> queryStdDevice(String rootId) {
		String globeCountyId = ThreadUserParamHolder.getGlobeCountyId();
		logger.info("rootId : " + rootId + "  globeCountyId " + globeCountyId);

		Map<String, Object> param = new HashMap<String, Object>();
		
		if(!BusiConstants.StringConstants.COUNTY_ALL.equals(globeCountyId)){
			List<String> countyIdScope = getCountyIdScope(globeCountyId);
			param.put("countyIds", countyIdScope);
		}
		try {
			int root = Integer.parseInt(rootId);
			param.put("pid", root);
		} catch (NumberFormatException e) {
//			e.printStackTrace();
			param.put("pid", 0);
		}
		List<StdDevice> children = stdDeviceMapper.queryChildren(param);
		return children;
	}
	

	public List<StdDevice> queryDev(String level, String name) {
		String globeCountyId = ThreadUserParamHolder.getGlobeCountyId();
		Map<String, Object> param = new HashMap<String, Object>();
		if(!BusiConstants.StringConstants.COUNTY_ALL.equals(globeCountyId)){
			List<String> countyIdScope = getCountyIdScope(globeCountyId);
			param.put("countyIds", countyIdScope);
		}
		
		if(StringHelper.isNotEmpty(level)){
			param.put("stdLevel", level);
		}
		if(StringHelper.isNotEmpty(name)){
			param.put("jdName", name);
		}
		return stdDeviceMapper.queryChildren(param);
	}
	
	/**
	 * 只查询当前分公司,第一级别的光站.
	 * @return
	 */
	public List<StdDevice> queryTopStdDevs() {
		String globeCountyId = ThreadUserParamHolder.getGlobeCountyId();
		Map<String, Object> param = new HashMap<String, Object>();
		
		if(!BusiConstants.StringConstants.COUNTY_ALL.equals(globeCountyId)){
			List<String> countyIdScope = getCountyIdScope(globeCountyId);
			param.put("countyIds", countyIdScope);
		}
		param.put("pid", 0);
		
		List<StdDevice> children = stdDeviceMapper.queryChildren(param);
		
		return children;
	}
	
	public void saveOrUpdateDevice(StdDevice device){
		Integer stdDevId = device.getStdDevId();
		if(stdDevId ==null){
			device.setCreateTime(new Date());
			device.setCreateOptr(ThreadUserParamHolder.getOptr().getUserOID());
			stdDeviceMapper.insert(device);
		}else{
			stdDeviceMapper.updateByPrimaryKeySelective(device);
		}
	}
	
	public void removeDevice(Integer devId) throws MessageException{
		String globeCountyId = ThreadUserParamHolder.getGlobeCountyId();
		Map<String, Object> param = new HashMap<String, Object>();
		
		if(!BusiConstants.StringConstants.COUNTY_ALL.equals(globeCountyId)){
			List<String> countyIdScope = getCountyIdScope(globeCountyId);
			param.put("countyIds", countyIdScope);
		}
		param.put("pid", devId);
		
		List<StdDevice> children = stdDeviceMapper.queryChildren(param);
		if(CollectionHelper.isNotEmpty(children)){
			throw new MessageException(StatusCodeConstant.STD_DEV_HAS_CHILDREN);
		}
		//TODO 已经关联地址的不能删除
//		stdDeviceAddrRelMapper.selectByPrimaryKey(relId);
		stdDeviceMapper.deleteByPrimaryKey(devId);
		stdDeviceAddrRelMapper.deleteByDevId(devId); 
	}
	
	/**
	 * 查询服务队信息
	 * @param keyWord
	 * @param start
	 * @param limit
	 * @return
	 */
	public Pagination queryTeam(String keyWord, int start, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 保存修改服务队信息.
	 * @param team
	 */
	public void saveServerTeam(StdServiceTeam team) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 删除服务队信息.
	 * @param teamId
	 */
	public void removeServerTeam(Integer teamId) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 保存修改维护人员信息.
	 * @param server
	 */
	public void saveSteward(StdSteward server) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 删除维护人员.
	 * @param optrId
	 */
	public void removeSteward(Integer optrId) {
		// TODO Auto-generated method stub
		
	}


	public void saveMatchAddr(Integer gjdId, Integer[] addrIds) {
		stdDeviceAddrRelMapper.deleteByDevId(gjdId);
		for (Integer addrId : addrIds) {
			StdDeviceAddrRel rel = new StdDeviceAddrRel();
			rel.setAddrId(addrId);
			rel.setGjdId(gjdId);
			stdDeviceAddrRelMapper.insert(rel);
		}
	}
	
	private List<String> getCountyIdScope(String countyId) {
		Map<String, List<AdOaCountyRef>> childrenMap = ThreadUserParamHolder.getCountyChildrenMap();
		List<String> countyIds = new ArrayList<String>();
		List<AdOaCountyRef> children = childrenMap.get(countyId);
		if(CollectionHelper.isEmpty(children)){
			countyIds.add(countyId);
		}else{
			for (AdOaCountyRef c : children) {
				countyIds.add(c.getCountyId());
			}
		}
		return countyIds;
	}

}
