package com.yaochen.address.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyooo.framework.common.util.CglibUtil;
import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.data.domain.address.AdOaCountyRef;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.domain.address.AdTreeAudit;
import com.yaochen.address.data.mapper.address.AdSysUserMapper;
import com.yaochen.address.data.mapper.address.AdTreeAuditMapper;
import com.yaochen.address.data.mapper.address.AdTreeMapper;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.ThreadUserParamHolder;

@Service
public class QueryService {
	@Autowired private AdTreeMapper adTreeMapper;
	@Autowired private AdSysUserMapper adSysUserMapper;
	@Autowired private AdTreeAuditMapper adTreeAuditMapper;

	/**
	 * @param addrLevel
	 * @param addrNameFlag  是否精确查找 1,= ，2，like
	 * @param addrName
	 * @param addrParentFlag
	 * @param addrParent
	 * @param addrType
	 * @param start
	 * @param limit
	 * @return
	 */
	public Pagination query(Integer[] addrLevel, String addrNameFlag,
			String addrName, String addrParentFlag, String addrParent,
			String[] addrType, Integer start, Integer limit) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		if(addrLevel !=null && addrLevel.length >0){
			criteria.put("addrLevel", addrLevel);
		}
		
		if(StringHelper.isNotEmpty(addrName)){
			criteria.put("addrNameFlag", addrNameFlag);
			criteria.put("addrName", addrName);
		}
		if(StringHelper.isNotEmpty(addrParent)){
			criteria.put("addrParentFlag", addrParentFlag);
			criteria.put("addrParent", addrParent);
		}
		if(addrType != null && addrType.length > 0){
			criteria.put("addrType", addrType);
		}
		
		String globeCountyId = ThreadUserParamHolder.getGlobeCountyId();
		boolean comeFromQgs = BusiConstants.StringConstants.COUNTY_ALL.equals(globeCountyId);
		if(!comeFromQgs){
//			criteria.put("countyId", new String[]{globeCountyId});
			List<String> countyIds = getCountyIdScope(globeCountyId);
			criteria.put("countyIds", countyIds);
		}
		
		Pagination pager = new Pagination(criteria, start, limit);
		adTreeMapper.queryComplex(pager);
		return pager;
	}

	/**
	 * 查询未被审核过的地址.
	 * @param level
	 * @param specify 
	 * @param addrName 
	 * @param start
	 * @param limit2
	 * @return
	 */
	public Pagination queryUnAudited(String startTime,String endTime,String status,Integer start, Integer limit) {

		Map<String, Object> params = new HashMap<String, Object>();
		if(StringHelper.isNotEmpty(startTime)){
			params.put("startTime", startTime);
		}
		if(StringHelper.isNotEmpty(endTime)){
			params.put("endTime", endTime);
		}
		if(StringHelper.isNotEmpty(status)){
			params.put("status", status);
		}
		
		String globeCountyId = ThreadUserParamHolder.getGlobeCountyId();
		boolean comeFromQgs = !BusiConstants.StringConstants.COUNTY_ALL.equals(globeCountyId);
		if(comeFromQgs){
			params.put("countyId", ThreadUserParamHolder.getGlobeCountyId());
		}
		params.put("start", start !=null ? start : 0);
		params.put("limit", limit);
		
		Integer count = adTreeAuditMapper.countAuditInfo(params);
		Pagination pager = new Pagination(params, start, limit);
		pager.setTotalCount(count);

		ArrayList<AdTreeAudit> records = adTreeAuditMapper.queryAuditInfo(params);
		pager.setRecords(records);
		
		return pager;
	}

	
	/**
	 * 查询所有的直接下级子节点.
	 * @param parentAddrId 父级地址编号
	 * @return 并且分页
	 */
	public List<AdTree> findDirectChildren(String parentAddrId) throws Throwable {
		Integer addrId = null;
		String countyId = ThreadUserParamHolder.getGlobeCountyId();
		boolean comeFromQgs = BusiConstants.StringConstants.COUNTY_ALL.equals(countyId);
		
		try {
			addrId = Integer.parseInt(parentAddrId);
		} catch (Exception e) {
			//这里抛出异常,可能是Ext树传来的根节点的node ,为 root ,查找当前分公司在地址库里的最高级别的地址.
			Map<String, Object> param = new HashMap<String, Object>();
			//区公司的,或者县公司的,直接根据countyId查询.
			if(comeFromQgs ){
				param.put("countyIds", new String[]{countyId});
			}else{
				List<String> countyIds = getCountyIdScope(countyId);
				param.put("countyIds", countyIds.toArray());
			}
			return adTreeMapper.queryCountiesByCountyIds(param);
		}
		
		AdTree param = new AdTree();
		if(addrId !=0){
			param = queryAddrByKey(addrId);;
		}else{
			param.setAddrLevel(0);
			param.setAddrId(addrId);
			if(!comeFromQgs){
				param.setCountyId(countyId);
			}
		}
		return findDirectChildrenByCurrentAddr(param);
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
	
	private List<AdTree> findDirectChildrenByCurrentAddr(AdTree parent) {
		AdTree param =new AdTree();//新建一个对象,避免传入的参数收到污染，影响到在后续的操作中
		CglibUtil.copy(parent, param);
//		Integer addrLevel = parent.getAddrLevel();
//		param.setAddrLevel(addrLevel + 1);//为后续的查询做准备
		
		List<AdTree> selectChildrenByPid = adTreeMapper.selectDirectChildrenByPid(param);
		return selectChildrenByPid;
	}

	
	/**
	 * 根据主键查询.
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	public AdTree queryAddrByKey(Integer addrId)throws Throwable{
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("addrId", addrId);
		query.put("userid", getUserInSession().getUserOID());
		return adTreeMapper.selectByPrimaryKey(query);
	}
	
	
	/**
	 * 实际是获取session里的用户对象.在每次请求的时候,Timexxx拦截器负责把session里的用户对象放到线程变量里.
	 * @return
	 * @throws MessageException
	 */
	private UserInSession getUserInSession() throws MessageException{
		UserInSession optr = ThreadUserParamHolder.getOptr();
		if(optr == null){
			throw new MessageException(StatusCodeConstant.USER_NOT_LOGGED);
		}
		return optr;
	}
	

	public List<AdTree> queryRelatedAddrs(Integer gzId) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("gzId", gzId);
		return adTreeMapper.queryRelatedAddrs(criteria);
	}

}
