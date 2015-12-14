package com.yaochen.address.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.data.domain.address.AdCollections;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.mapper.address.AdCollectionsMapper;
import com.yaochen.address.data.mapper.address.AdDoneCodeMapper;
import com.yaochen.address.data.mapper.address.AdLevelMapper;
import com.yaochen.address.data.mapper.address.AdOaCountyRefMapper;
import com.yaochen.address.data.mapper.address.AdRoleResMapper;
import com.yaochen.address.data.mapper.address.AdSysUserMapper;
import com.yaochen.address.data.mapper.address.AdTreeChangeDetailMapper;
import com.yaochen.address.data.mapper.address.AdTreeChangeMapper;
import com.yaochen.address.data.mapper.address.AdTreeMapper;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.AddrNameChecker;
import com.yaochen.address.support.ThreadUserParamHolder;

@Service
public class TestService {
	@Autowired
	private AdTreeMapper adTreeMapper;
	@Autowired
	private AddrNameChecker addrNameChecker;
	@Autowired
	private AdDoneCodeMapper adDoneCodeMapper;
	@Autowired
	private AdLevelMapper adLevelMapper;
	@Autowired
	private AdRoleResMapper adRoleResMapper;
	@Autowired
	private AdCollectionsMapper adCollectionsMapper;
	@Autowired
	private AdTreeChangeMapper adTreeChangeMapper;
	@Autowired
	private AdTreeChangeDetailMapper adTreeChangeDetailMapper;
	@Autowired
	private AdOaCountyRefMapper adOaCountyRefMapper;
	@Autowired private AdSysUserMapper adSysUserMapper;
	
	/**
	 * 查询当前用户有权访问的Level
	 * @throws Throwable
	 */
	public List<AdLevel> findAuthLevelByCurrentUser() throws Throwable {
		int maxLevel = getUserInSession().getMaxLevelAllowed();
		return adLevelMapper.selectByMaxLevel(maxLevel);
	}

	/**
	 * @see #findChildrensByPid(Integer)
	 * 
	 * @param parentAddrId 父级地址编号
	 * @param filter 
	 * @return 并且分页
	 */
	public Pagination findChildrensAndPagingByPid(Integer parentAddrId, Integer start,
			Integer limit, String... filter) throws Throwable {
		Map<String, Object>	param = new HashMap<String, Object>();
		param.put("addrParent", parentAddrId);
		param.put("userid", getUserInSession().getUserOID());
		String countyId = getUserInSession().getCompanyOID();
		if(!BusiConstants.StringConstants.COUNTY_ALL.equals(countyId)){
			param.put("countyId", countyId);
		}
		if(null != filter && filter.length ==1){//可变参数只是为了其他地方调用方便,实际上只允许一个元素
			String value = filter[0];
			if(StringHelper.isNotEmpty(value)){
				param.put("filter", value);
			}
		}
		
		param.put("status", BusiConstants.Status.ACTIVE.name());
		Pagination pager = new Pagination(param,start,limit);
		List<AdTree> selectByKeyWord = adTreeMapper.selectByPid(pager);
		
		AdCollections coll = new AdCollections();
		coll.setUserid(getUserInSession().getUserOID());
		List<AdCollections> colls = adCollectionsMapper.selectByExample(coll);
		Map<String, List<AdCollections>> map = CollectionHelper.converToMap(colls, "addrId");
		for (AdTree tree : selectByKeyWord) {
			List<AdCollections> list = map.get(tree.getAddrId().toString());
			Integer collected = 0;
			if(CollectionHelper.isEmpty(list)){
				collected = 1;
			}
			tree.setCollected(collected);
		}
		pager.setRecords(selectByKeyWord);
		return pager;
	}

	/**
	 * 一次查出首页需要的所有数据.
	 * @param parentAddrId 父级地址编号
	 * @return 并且分页
	 */
	public List<AdTree> queryAllDataForHomePage(AdTree currentNode)
			throws Exception {
		Map<String, Object> qm = new HashMap<String, Object>();
		//TODO 后台取树的最大级别  设置一个常量
		Integer treeLevel =  5;
		try {
			treeLevel = Integer.parseInt(System.getProperty(BusiConstants.StringConstants.MAX_TREE_LEVEL_PROP_NAME ));
		} catch (Exception e) {
			e.printStackTrace();
		}
		qm.put("treeLevel", treeLevel  );
		qm.put("childLevel", currentNode.getAddrLevel()  +1 );
		qm.put("countyId", currentNode.getCountyId() );
		String[] split = currentNode.getAddrPrivateName().split("/");
		List<String> asList = Arrays.asList(split);
		asList.subList(0, asList.size() - 1);
		qm.put("pids", asList  );
		return adTreeMapper.selectAllInfoForPage(qm);
	}
	
	/**
	 * 根据主键查询.
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	public AdTree queryByKey(Integer addrId)throws Throwable{
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
	
}
