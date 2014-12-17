package com.yaochen.address.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyooo.framework.common.util.CglibUtil;
import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiCodeConstants;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.data.domain.address.AdCollections;
import com.yaochen.address.data.domain.address.AdDoneCode;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdRoleRes;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.mapper.address.AdCollectionsMapper;
import com.yaochen.address.data.mapper.address.AdDoneCodeMapper;
import com.yaochen.address.data.mapper.address.AdLevelMapper;
import com.yaochen.address.data.mapper.address.AdRoleResMapper;
import com.yaochen.address.data.mapper.address.AdTreeMapper;
import com.yaochen.address.dto.SystemFunction;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.AddrNameChecker;

@Service
public class TreeService {
	private Logger logger = Logger.getLogger(getClass());
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
	
	/**
	 * 新增地址.
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	public Integer addTree(AdTree tree,UserInSession optr) throws Throwable{
		//验证同一级下名称是否重复
		Date createTime = new Date();
		String optrId = optr.getUserOID();
		String countyId = optr.getDepartmentOID();//TODO 是不是这个字段？？
		
		tree.setCreateTime(createTime);
		tree.setCreateOptrId(optrId);
		
		tree.setCountyId(countyId);
		//验证
		String check = addrNameChecker.check(tree );
		if(null!=check){
			throw new MessageException(StatusCodeConstant.ADDR_NAME_INVALID);
		}
		
		Integer addrParent = tree.getAddrParent();
		String addrName = tree.getAddrName();
		//检查同级别的有没有同名地址
		boolean exists = checkSameLevelAddrName(addrParent, addrName);
		if(exists){
			throw new MessageException(StatusCodeConstant.ADDR_ALREADY_EXISTS_THIS_LEVEL);
		}
		
		int createDoneCode = createDoneCode(createTime, BusiCodeConstants.ADD_ADDR,optr);
		
		tree.setCreateDoneCode(createDoneCode);
		
		return adTreeMapper.insertSelective(tree);
	}


	public List<Integer> addTrees(AdTree param,Integer startPosi,Integer endPosi,UserInSession optr) throws Throwable{
		//验证参数
		if(null == startPosi || null == endPosi){ //提示起始位置不能为空
			logger.info("参数为空");
			throw new MessageException(StatusCodeConstant.ADDR_NAME_INVALID);
		}
		if(endPosi < startPosi){ //后者不能小于前者
			throw new MessageException(StatusCodeConstant.ADDR_NAME_INVALID);
		}
		
		List<Integer> result = new ArrayList<Integer>();
		Date createTime = new Date();
		String optrId = optr.getUserOID();
		String countyId = optr.getDepartmentOID();//TODO 是不是这个字段？？
		
		//TODO 验证名称是否重复,重复则忽略
		
		int createDoneCode = createDoneCode(createTime, BusiCodeConstants.ADD_ADDR_BATCH,optr);
		
		Pagination childrenPager = findChildrensAndPagingByPid(param.getAddrParent(), 0, Integer.MAX_VALUE);
		List<AdTree> children = childrenPager.getRecords();
		
		for (Integer index = startPosi; index < endPosi; index++) {
			AdTree tree = new AdTree();
			CglibUtil.copy(param, tree);
			tree.setCreateDoneCode(createDoneCode);
			tree.setCreateTime(createTime);
			tree.setCountyId(countyId);
			tree.setCreateOptrId(optrId);
			String addrName = index.toString();
			boolean exists = checkSameLevelAddrName(addrName,children);
			if(exists){
				continue;
			}
			tree.setAddrName(addrName);
			
			String check = addrNameChecker.check(tree );
			if(null!=check){
				throw new MessageException(StatusCodeConstant.ADDR_NAME_INVALID);
			}
			int addrId = adTreeMapper.insertSelective(tree);
			result.add(addrId);
		}
		
		return result;
	}
	
	/**
	 * 根据关键字进行搜索地址， 地址按照一定的规则进行排序
	 * 搜索时需要根据用户设置的作用域并结合startLevel进行搜索
	 * 
	 * @param startLevel 开始级别，如果为-1则为所有
	 * @param keyword
	 * @return 结果集进行分页
	 * @throws Throwable
	 */
	public Pagination doSearchAddress(Integer startLevel, String keyword,
			Integer start, Integer limit) throws Throwable {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("addrLevel", startLevel);
		param.put("keyword", keyword);//全名
		param.put("status", BusiConstants.Status.ACTIVE.name());
		Pagination pager = new Pagination(param,start,limit);
		adTreeMapper.selectByKeyWord(pager);
		return pager;
	}

	/**
	 * 查询当前用户有权访问的Level
	 * @throws Throwable
	 */
	public List<AdLevel> findAuthLevelByCurrentUser(UserInSession optr) throws Throwable {
		List<SystemFunction> systemFunction = optr.getSystemFunction();
		Integer roleOID = null;
		for (SystemFunction fun : systemFunction) {
			Integer functionOID = fun.getFunctionOID();
			if(BusiConstants.ADDR_SYS_FUN_CODE.equals(""+functionOID)){
				roleOID = fun.getRoleOID();
				break;
			}
		}
		if(roleOID == null){
			throw new MessageException(StatusCodeConstant.USER_NOT_AUTHORIZED);
		}
		List<AdRoleRes> rrs = adRoleResMapper.selectByRoleId(roleOID);
		if(rrs==null || rrs.size() ==0 ){
			throw new MessageException(StatusCodeConstant.USER_NOT_AUTHORIZED);
		}
		//TODO 不十分科学,暂时这么搞.
		AdRoleRes rr = rrs.get(0);
		int maxLevel = Integer.parseInt(rr.getResCode());
		return adLevelMapper.selectByMaxLevel(maxLevel);
	}

	/**
	 * @see #findChildrensByPid(Integer)
	 * 
	 * @param parentAddrId 父级地址编号
	 * @return 并且分页
	 */
	public Pagination findChildrensAndPagingByPid(Integer parentAddrId, Integer start,
			Integer limit) throws Throwable {
		Map<String, Object>	param = new HashMap<String, Object>();
		param.put("addrParent", parentAddrId);
		param.put("status", BusiConstants.Status.ACTIVE.name());
		Pagination pager = new Pagination(param,start,limit);
		adTreeMapper.selectByPid(pager);
		return pager;
	}

	/**
	 * 编辑地址.
	 * @param tree
	 * @param ignoreEmpty 
	 * @throws Throwable
	 */
	public void modTree(AdTree tree, boolean ignoreEmpty,UserInSession optr) throws Throwable {
		AdTree oldTree = adTreeMapper.selectByPrimaryKey(tree.getAddrId());
		if(oldTree== null){
			throw new MessageException(StatusCodeConstant.ADDR_NOT_EXISTS);
		}
		Date createTime = new Date();
		createDoneCode(createTime, BusiCodeConstants.EDIT_ADDR,optr);
		
		if(ignoreEmpty){
			adTreeMapper.updateByPrimaryKeySelective(tree);
		}else{
			adTreeMapper.updateByPrimaryKey(tree);
		}
		
	}
	
	/**
	 * 根据主键查询.
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	public AdTree queryByKey(Integer addrId)throws Throwable{
		return adTreeMapper.selectByPrimaryKey(addrId);
	}

	public void delTree(Integer addrId,UserInSession optr) throws Throwable {
		//TODO 要不要做权限检查？
		AdTree tree = checkTreeExists(addrId);
		//只要有一个子节点都不给删除
		Pagination pager = findChildrensAndPagingByPid(addrId, 0, 1);
		List<Object> children = pager.getRecords();
		if(children != null && children.size() > 0){
			throw new MessageException(StatusCodeConstant.ADDR_HAS_CHILDREN);
		}
		
		tree.setStatus(BusiConstants.Status.INVALID.name());
		createDoneCode(new Date(), BusiCodeConstants.DEL_ADDR,optr);
		modTree(tree, true,optr);
	}

	/**
	 * 收藏
	 * @param addrId
	 * @throws Throwable
	 */
	public void saveCollectTree(Integer addrId,UserInSession optr) throws Throwable {
		checkTreeExists(addrId);
		String userId = optr.getUserOID();
		AdCollections coll = checkCollExists(addrId, userId);
		if(coll == null){
			coll = new AdCollections();
		}else{
			throw new MessageException(StatusCodeConstant.ADDR_COLL_ALREADY_EXISTS);
		}
		Date createTime = new Date();
		createDoneCode(createTime, BusiCodeConstants.COLLECT_ADDR,optr);
		coll.setAddrId(addrId);
		coll.setCreateTime(createTime);
		coll.setUserid(userId);
		adCollectionsMapper.insert(coll );
	}

	
	/**
	 * 取消收藏.
	 * @param addrId
	 * @throws Throwable
	 */
	public void saveCancelCollectTree(Integer addrId,UserInSession optr) throws Throwable {
		String userId = optr.getUserOID();
		AdCollections coll = checkCollExists(addrId, userId);
		if(coll == null){
			throw new MessageException(StatusCodeConstant.ADDR_COLL_NOT_EXISTS);
		}
		Date createTime = new Date();
		createDoneCode(createTime, BusiCodeConstants.DE_COLLECT_ADDR,optr);
		adCollectionsMapper.deleteByAddrAndUser(coll);
		
	}
	
	/**
	 * 查询收藏.
	 * @param limit
	 * @return
	 * @throws Throwable
	 */
	public List<AdLevel> findCollectTreeList( Integer limit,UserInSession optr) throws Throwable {
		String userId = optr.getUserOID();
		Map<String, String> param = new HashMap<String, String>();
		param.put("userid", userId);
		param.put("status", BusiConstants.Status.ACTIVE.name());
		Pagination pager = new Pagination(param,0,limit);
		List<AdLevel> list = adTreeMapper.selectUserCollection(pager);
		return list;
	}
	
	/**
	 * 要操作地址之前,先检查下是否存在.
	 * @param addrId
	 * @return
	 * @throws Throwable
	 * @throws MessageException
	 */
	private AdTree checkTreeExists(Integer addrId) throws Throwable,
			MessageException {
		AdTree tree = queryByKey(addrId);
		if(tree== null){
			throw new MessageException(StatusCodeConstant.ADDR_NOT_EXISTS);
		}
		return tree;
	}
	
	/**
	 * 检查地址是否已经被收藏.
	 * @param addrId
	 * @param userId
	 * @return
	 * @throws MessageException
	 */
	private AdCollections checkCollExists(Integer addrId, String userId)
			throws MessageException {
		AdCollections coll = new AdCollections();
		coll.setUserid(userId);
		coll.setAddrId(addrId);
		List<AdCollections> colls = adCollectionsMapper.selectByExample(coll);
		if(colls !=null && colls.size() > 0){
			for (AdCollections col : colls) {
				if(coll.getAddrId().equals(addrId) && coll.getUserid().equals(userId)){
					return col;
				}
			}
		}
		return null;
	}

	
	/**
	 * 插入流水.
	 * @param createTime
	 * @param optrId
	 * @param code
	 * @return
	 */
	private int createDoneCode(Date createTime, BusiCodeConstants code,UserInSession optr ) throws Throwable{
		AdDoneCode doneCode = new AdDoneCode();
		doneCode.setBusiCode(code.name());
		doneCode.setCreateTime(createTime);
		doneCode.setOptrId(optr.getUserOID());
		int createDoneCode = adDoneCodeMapper.insertSelective(doneCode);
		return createDoneCode;
	}
	
	private boolean checkSameLevelAddrName(Integer addrParent, String addrName) throws Throwable, MessageException {
		Pagination childrenPager = findChildrensAndPagingByPid(addrParent, 0, Integer.MAX_VALUE);
		List<AdTree> children = childrenPager.getRecords();
		return checkSameLevelAddrName(addrName, children);
	}

	private boolean checkSameLevelAddrName(String addrName,List<AdTree> children) {
		for (AdTree child : children) {
			if(child.getAddrName().equals(addrName)){
				return true;
			}
		}
		return false;
	}
}
