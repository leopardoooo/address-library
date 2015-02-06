package com.yaochen.address.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.yaochen.address.common.BusiConstants.AddrChangeType;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.common.StringHelper;
import com.yaochen.address.data.domain.address.AdCollections;
import com.yaochen.address.data.domain.address.AdDoneCode;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdRoleRes;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.domain.address.AdTreeChange;
import com.yaochen.address.data.mapper.address.AdCollectionsMapper;
import com.yaochen.address.data.mapper.address.AdDoneCodeMapper;
import com.yaochen.address.data.mapper.address.AdLevelMapper;
import com.yaochen.address.data.mapper.address.AdOaCountyRefMapper;
import com.yaochen.address.data.mapper.address.AdRoleResMapper;
import com.yaochen.address.data.mapper.address.AdTreeChangeMapper;
import com.yaochen.address.data.mapper.address.AdTreeMapper;
import com.yaochen.address.dto.AddrDto;
import com.yaochen.address.dto.SystemFunction;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.AddrNameChecker;
import com.yaochen.address.support.ThreadUserParamHolder;
import com.yaochen.address.web.controllers.UserController;

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
	@Autowired
	private AdTreeChangeMapper adTreeChangeMapper;
	@Autowired
	private AdOaCountyRefMapper adOaCountyRefMapper;
	
	/**
	 * 新增地址.
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	public AdTree addTree(AdTree tree) throws Throwable{
		UserInSession optr = getUserInSession();
		//验证同一级下名称是否重复
		checkRole(tree.getAddrLevel());
		Date createTime = new Date();
		String optrId = optr.getUserOID();
		
		tree.setCreateTime(createTime);
		tree.setCreateOptrId(optrId);
		
		//验证
		boolean isBlank = tree.getIsBlank().equals(BusiConstants.Booleans.T.name());
		if(isBlank){
			tree.setAddrName(BusiConstants.StringConstants.BLANK_ADDR_NAME);
		}else{
			String addrName = tree.getAddrName();
			if(!isBlank && BusiConstants.StringConstants.BLANK_ADDR_NAME.equals(addrName)){
				throw new MessageException(StatusCodeConstant.NONE_BLANK_ADDRESS_WRONG_NAME);
			}
		}
		String addrName = checkAddrName(tree);
		
		int createDoneCode = createDoneCode(createTime, BusiCodeConstants.ADD_ADDR);
		tree.setAddrName(addrName);
		
		tree.setCreateDoneCode(createDoneCode);
		AdTree parentNode = queryByKey(tree.getAddrParent());
		String fullName = null;
		String str1 = null;
		String slash = BusiConstants.StringConstants.SLASH;
		if(null != parentNode){
			str1 = parentNode.getStr1() +( slash + addrName );
			fullName = parentNode.getAddrFullName() + (isBlank ? "":addrName);
		}else{//如果没有上级,这里一定是一级地址
			str1 = addrName;
			fullName = addrName;
		}
		tree.setAddrFullName(fullName);
		tree.setStr1(str1);
		tree.setStatus(BusiConstants.StringConstants.ADDR_INIT_STATUS);
		//新增的树的ID
		adTreeMapper.insertSelective(tree);
		Integer newAddedAddrId = tree.getAddrId();
		
		String addrPrivateName = BusiConstants.StringConstants.TOP_PID + slash + newAddedAddrId + slash ;
		if(null != parentNode){
			addrPrivateName = parentNode.getAddrPrivateName() + newAddedAddrId + slash ;
		}
		tree.setAddrPrivateName(addrPrivateName);
		adTreeMapper.updateByPrimaryKeySelective(tree);
		return queryByKey(tree.getAddrId());
	}

	/**
	 * 检查地址名是否合法,
	 * @param tree 	地址的对象,至少包含四个属性  addrName ,addrLevel ,IsBlank 和  AddrParent .
	 * @param children	同父级的所有地址集合(数组),可不输入,不输入单个验证的时候会重新查询.
	 * @return
	 * @throws MessageException
	 * @throws Exception
	 * @throws Throwable
	 */
	public String checkAddrName(AdTree tree,AdTree ... children) throws MessageException,
			Exception, Throwable {
		logger.info("检测地址名...");
		String addrName = tree.getAddrName();
		String isBlank = tree.getIsBlank();
		boolean numNull = null == tree.getAddrParent() || null == tree.getAddrLevel();
		boolean blankIsNull = StringHelper.isEmpty(isBlank);
		if(numNull || blankIsNull){
			throw new MessageException(StatusCodeConstant.PARAM_MISSED_WHILE_CHECK_ADDR_NAME);
		}else if(BusiConstants.Booleans.F.name().equals(isBlank) && StringHelper.isEmpty(addrName)){
			throw new MessageException(StatusCodeConstant.NON_BLANK_ADDR_WITH_NO_NAME);
		}
		addrName = literalCheckAddrName(tree, addrName);
		Integer check = addrNameChecker.checkBusiRule(tree );
		if(check != null && 0 != check){
			StatusCodeConstant code = StatusCodeConstant.parseCode(check);
			throw new MessageException(code);
		}
		//检查同级别的有没有同名地址
		boolean exists = false;
		tree.setAddrName(addrName);
		if(null != children && children.length > 0 ){
			List<AdTree> list = Arrays.asList(children);
			exists = checkSameLevelAddrName(tree,list);
		}else{
			exists = checkSameLevelAddrName(tree);
		}
		if(exists){
			throw new MessageException(StatusCodeConstant.ADDR_ALREADY_EXISTS_THIS_LEVEL);
		}
		return addrName;
	}
	
	/**
	 * 批量添加地址.
	 * @param param 本次通用参数.
	 * @param addrNamePreffix	地址名 前缀.
	 * @param addrNameSuffix	地址名 后缀.
	 * @param start		起始位置	可以是英文字符,也可以是数字,但开始和结束的类型必须一致,都是字符或者都是数字.
	 * @param end		结束位置	可以是英文字符,也可以是数字,但开始和结束的类型必须一致,都是字符或者都是数字.
	 * @return		数组,第一个 成功的个数,第二个,失败的个数.
	 * @throws Throwable
	 */
	public List<Integer> addTrees(AdTree param,String addrNamePreffix, String addrNameSuffix, String start,String end) throws Throwable{
		checkRole(param.getAddrLevel());
		UserInSession optr = getUserInSession();
		//验证参数
		if(StringHelper.isEmpty(end) || StringHelper.isEmpty(start)){ //提示起始位置不能为空
			throw new MessageException(StatusCodeConstant.BATCH_ADD_RANGE_EMPTY);
		}
		//TODO 批量添加的时候,不允许批量留空地址
		if(BusiConstants.Booleans.T.name().equals(param.getIsBlank())){
			throw new MessageException(StatusCodeConstant.TOO_MANY_BLANK_ADDR);
		}
		
		Integer endPosi = null;
		Integer startPosi = null;
		
		start = start.trim();
		end = end.trim();
		
		boolean bothNum = false;//都是数字
		boolean bothEngChar = false;//都是字母
		if(start.length() == 1 && end.length() ==1){//只有范围的的字符串都是一位的时候才判断是否为字母,否则超过一位的肯定不是字母.
			bothEngChar = StringHelper.isAlphabet(start) && StringHelper.isAlphabet(end);
		}
		if(!bothEngChar){//如果确定是字母了,肯定不是数字了.
			bothNum = StringHelper.isNumeric(start) && StringHelper.isNumeric(end);
		}
		if(!bothEngChar && !bothNum){//两者都不是,抛出异常
			throw new MessageException(StatusCodeConstant.BATCH_ADD_WRONG_RANGE_TYPE_MISTACH);
		}
		if(bothNum){//前面的检查确保了这里不会抛错
			startPosi = Integer.parseInt(start);
			endPosi = Integer.parseInt(end);
		}
		if(bothEngChar){
			startPosi = (int) start.charAt(0);
			endPosi = (int) end.charAt(0);
		}
		
		if(endPosi < startPosi){ //范围后者不能小于前者
			throw new MessageException(StatusCodeConstant.BATCH_ADD_WRONG_RANGE_ORDER_WRONG);
		}
		
		List<Integer> result = new ArrayList<Integer>();
		Integer succNum = 0;
		Integer failedNum = 0;
		
		Date createTime = new Date();
		String optrId = optr.getUserOID();
		
		int createDoneCode = createDoneCode(createTime, BusiCodeConstants.ADD_ADDR_BATCH);
		Pagination childrenPager = findChildrensAndPagingByPid(param.getAddrParent(), 0, Integer.MAX_VALUE);
		List<AdTree> children = childrenPager.getRecords();
		AdTree parentNode = queryByKey(param.getAddrParent());
		addrNamePreffix = StringHelper.isEmpty(addrNamePreffix) ? "":addrNamePreffix;
		addrNameSuffix = StringHelper.isEmpty(addrNameSuffix) ? "":addrNameSuffix;
		String slash = BusiConstants.StringConstants.SLASH;
		
		for (int index = startPosi; index <= endPosi; index++) {
			AdTree tree = new AdTree();
			CglibUtil.copy(param, tree);
			tree.setCreateDoneCode(createDoneCode);
			tree.setCreateTime(createTime);
			tree.setCreateOptrId(optrId);
			
			String addrName = addrNamePreffix;
			if(bothNum){
				addrName += index + addrNameSuffix;
			}else if(bothEngChar){
				addrName += (char)index + addrNameSuffix;
			}
			tree.setAddrName(addrName);
			try {
				addrName = checkAddrName(tree,children.toArray(new AdTree[children.size()]));
			} catch (Throwable e) {
				//批量添加的时候,地址名检验不通过,直接忽略.
				failedNum ++;
				continue;
			}
			tree.setAddrName(addrName);
			boolean isBlank = param.getIsBlank().equals(BusiConstants.Booleans.T.name());
			String str1 = null;
			String addrFullName = null;
			if(null != parentNode){
				str1 = parentNode.getStr1() +slash + addrName;
				addrFullName = parentNode.getAddrFullName() + ( isBlank ? "": addrName );
			}else{//如果没有上级,这里一定是一级地址
				str1 = addrName;
				addrFullName = ( isBlank ? "": addrName );
			}
			tree.setStr1(str1);
			tree.setAddrFullName(addrFullName);
			tree.setStatus(BusiConstants.StringConstants.ADDR_INIT_STATUS);
			adTreeMapper.insertSelective(tree);
			Integer addrId = tree.getAddrId();
			
			String addrPrivateName = BusiConstants.StringConstants.TOP_PID + addrId + slash ;
			if(null != parentNode){
				addrPrivateName = parentNode.getAddrPrivateName()  + addrId + slash ;
			}
			tree.setAddrPrivateName(addrPrivateName);
			adTreeMapper.updateByPrimaryKeySelective(tree);
			succNum ++;
		}
		result.add(succNum);
		result.add(failedNum);
		return result;
	}
	
	/**
	 * 根据关键字进行搜索地址， 地址按照一定的规则进行排序
	 * 搜索时需要根据用户设置的作用域并结合startLevel进行搜索
	 * 
	 * @param targetLevel 开始级别，如果为-1则为所有
	 * @param keyword
	 * @return 结果集进行分页
	 * @throws Throwable
	 */
	public Pagination doSearchAddress(Integer targetLevel, String keyword,
			Integer start, Integer limit) throws Throwable {
		Map<String, Object> param = new HashMap<String, Object>();
		String baseScope = getBaseScope();
		param.put("baseScope", baseScope);
		UserInSession userInSession = getUserInSession();
		String globeCountyId = getGlobeCountyId();
		int total = 0;
		if(targetLevel > 0){
			param.put("addrLevel", targetLevel);
		}else if(targetLevel == 0){//等于0的时候,直接根据ID查询
			Pagination pager = new Pagination(param,0,total);
			List<AdTree> list = new ArrayList<AdTree>();
			int targetId = 0;
			try {
				targetId = Integer.parseInt(keyword);
			} catch (Exception e) {
				throw new MessageException(StatusCodeConstant.PARAM_ERROR_WHEN_QUERY_BY_ID);
			}
			
			AdTree addr = queryByKey(targetId);
			boolean countyAll = userInSession.getCompanyOID().equals(BusiConstants.StringConstants.COUNTY_ALL);
			if(countyAll || globeCountyId.equals(addr.getCountyId())){
				list.add(addr);
				total = 1;
			}
			pager.setTotalCount(total);
			pager.setRecords(list);
			return pager;
		}
		param.put("countyId", globeCountyId);
		param.put("userid", userInSession.getUserOID());
		param.put("keyword", keyword);//全名
		param.put("status", BusiConstants.Status.ACTIVE.name());
		Pagination pager = new Pagination(param,start,limit);
		
		List<AdTree> selectByKeyWord = adTreeMapper.selectByKeyWord(pager);
		AdCollections coll = new AdCollections();
		coll.setUserid(userInSession.getUserOID());
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
		return pager;
	}
	
	/**
	 * 根据关键字进行搜索 指定级别的 地址， 地址按照一定的规则进行排序
	 * 
	 * @param level 指定的搜索的级别.
	 * @param keyword
	 * @param currentAddrId 
	 * @return 结果集进行分页
	 * @throws Throwable
	 */
	public Pagination searchParentLevelAddrs(Integer level, String keyword, Integer currentAddrId,boolean sameParent,
			Integer start, Integer limit) throws Throwable {
		Map<String, Object> param = new HashMap<String, Object>();
		AdTree tree = queryByKey(currentAddrId);
		if(sameParent){
			Integer addrParent = null;
			try {
				addrParent = tree.getAddrParent();
			} catch (Exception e) {
				e.printStackTrace();
			}
			param.put("addrParent", addrParent);
		}else{
			
		}
		UserInSession user = getUserInSession();
		param.put("countyId", user.getCompanyOID());
		param.put("addrLevel", level);
		param.put("currentAddrId", currentAddrId);
		param.put("userid", user.getUserOID());
		param.put("countyId", getGlobeCountyId());
		param.put("keyword", keyword);//全名
		param.put("status", BusiConstants.Status.ACTIVE.name());
		Pagination pager = new Pagination(param,start,limit);
		adTreeMapper.selectByKeyWordLevel(pager);
		return pager;
	}

	public List<AdLevel> findAllLevels() throws Throwable {
		return adLevelMapper.selectByMaxLevel( -1 );
	}
	
	/**
	 * 查询当前用户有权访问的Level
	 * @throws Throwable
	 */
	public List<AdLevel> findAuthLevelByCurrentUser() throws Throwable {
		int maxLevel = getUserInSession().getMaxLevelAllowed();
		return adLevelMapper.selectByMaxLevel(maxLevel);
	}

	/**
	 * 获取当前用户能操作的最高的级别.
	 * @param userInSession 
	 * @param systemFunction
	 * @return
	 * @throws MessageException
	 */
	public int getMaxAllowedLevel(UserInSession optr) throws MessageException {
		
		List<SystemFunction> systemFunction = optr.getSystemFunction();
		Integer roleOID = null;
		for (SystemFunction fun : systemFunction) {
			Integer functionOID = fun.getFunctionOID();
			String code = System.getProperty(BusiConstants.StringConstants.ADDR_SYS_FUN_CODE);
			if(code.equals(""+functionOID)){
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
		return maxLevel;
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
	 * 查询所有的直接下级子节点.
	 * @param parentAddrId 父级地址编号
	 * @return 并且分页
	 */
	public List<AdTree> findDirectChildren(Integer parentAddrId) throws Throwable {
		AdTree param = new AdTree();
		if(parentAddrId !=0){
			param = queryByKey(parentAddrId);
		}else{
			param.setAddrLevel(0);
			param.setAddrId(parentAddrId);
			param.setCountyId(ThreadUserParamHolder.getGlobeCountyId());
		}
		return findDirectChildrenByCurrentAddr(param);
	}

	private List<AdTree> findDirectChildrenByCurrentAddr(AdTree parent) {
		Integer addrLevel = parent.getAddrLevel();
		AdTree param =new AdTree();//新建一个对象,避免传入的参数收到污染，影响到在后续的操作中
		CglibUtil.copy(parent, param);
		param.setAddrLevel(addrLevel + 1);//为后续的查询做准备
		param.setStatus(BusiConstants.Status.ACTIVE.name());
		List<AdTree> selectChildrenByPid = adTreeMapper.selectDirectChildrenByPid(param);
		return selectChildrenByPid;
	}

	/**
	 * 编辑地址.
	 * @param tree
	 * @param ignoreEmpty 
	 * @throws Throwable
	 */
	public void modTree(AdTree tree, boolean ignoreEmpty) throws Throwable {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("addrId", tree.getAddrId());
		query.put("userid", getUserInSession().getUserOID());
		AdTree oldTree = adTreeMapper.selectByPrimaryKey(query);
		if(oldTree== null){
			throw new MessageException(StatusCodeConstant.ADDR_NOT_EXISTS);
		}
		checkRole(oldTree.getAddrLevel());
		Date createTime = new Date();
		String oldAddrName = oldTree.getAddrName();
		String addrName = tree.getAddrName();
		boolean nameChanged = StringHelper.isNotEmpty(addrName) && !StringHelper.bothEmptyOrEquals(oldAddrName,addrName);
		if(nameChanged){
			AdTree checker = new AdTree();
			CglibUtil.copy(oldTree, checker);
			checker.setAddrName(addrName);
			addrName = checkAddrName(checker);
			tree.setAddrName(addrName);
		}
		
		if(ignoreEmpty){
			adTreeMapper.updateByPrimaryKeySelective(tree);
		}else{
			adTreeMapper.updateByPrimaryKey(tree);
		}
		
		//需要修改地址名
		if(nameChanged){
			AdTree parent = queryByKey(oldTree.getAddrParent());
			AdTree target = new AdTree();
			target.setAddrId(oldTree.getAddrId());
			target.setAddrFullName(parent.getAddrFullName()+tree.getAddrName());
			target.setAddrPrivateName(oldTree.getAddrPrivateName());
			target.setStr1(parent.getStr1() + BusiConstants.StringConstants.SLASH +tree.getAddrName());
			
			updateAllChildrensFullNamePrivateNameAndStr1(target, oldTree);
			
		}
		
		AdTreeChange change = new AdTreeChange();
		CglibUtil.copy(oldTree, change);
		change.setChangeTime(createTime);
		change.setChangeCause("修改");
		change.setChangeDoneCode(createDoneCode(createTime, BusiCodeConstants.EDIT_ADDR));
		change.setChangeType(AddrChangeType.EDIT.name());
		change.setChangeOptrId(ThreadUserParamHolder.getOptr().getUserOID());
		adTreeChangeMapper.insert(change);
		
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
	 * 变更地址的父级. 变更上级的时候允许重名,因此不做额外的事情,直接修改父级ID，批量修改所有子集的三个属性.
	 * @param addrId 当前地址的ID.
	 * @param pid		新的父级ID.
	 * @return
	 * @throws Throwable
	 */
	public AdTree saveChangeParent(Integer addrId,Integer pid) throws Throwable{
		//准备参数、校验
		AdTree sourceChild = queryByKey(addrId);
		AdTree sourceCopy = new AdTree();
		CglibUtil.copy(sourceChild, sourceCopy);
		
		checkRole(sourceChild.getAddrLevel());
		AdTree parent = queryByKey(pid);
		if(parent.getAddrLevel() +1 != sourceChild.getAddrLevel() ){
			throw new MessageException(StatusCodeConstant.CHANGE_LEVEL_PARENT_LEVEL_WRONG);
		}
		Date createTime = new Date();
		//干活
		changeParent(sourceChild, parent);
		
		AdTreeChange change = new AdTreeChange();
		CglibUtil.copy(sourceCopy, change);
		change.setChangeTime(new Date());
		change.setChangeDoneCode(createDoneCode(createTime, BusiCodeConstants.CHANGE_PARENT));
		change.setChangeOptrId(getUserInSession().getUserOID());
		change.setChangeCause("变更父级 => " + parent.getStr1() + "( " + pid + " )"   );
		change.setChangeType(AddrChangeType.CHANGE_PARENT.name());
		adTreeChangeMapper.insert(change);
		return sourceChild;
	}
	
	private void changeParent(AdTree sourceChild,AdTree parent) throws Throwable{
		if(parent.getAddrLevel() +1  != sourceChild.getAddrLevel()){
			throw new MessageException(StatusCodeConstant.CHANGE_LEVEL_PARENT_LEVEL_WRONG);
		}
		//准备参数
		String newFullNamePrefix = parent.getAddrFullName();
		String newPnPrefix = parent.getAddrPrivateName();
		String newStr1Prefix = parent.getStr1();
		
		AdTree newValue = new AdTree();
		newValue.setAddrFullName(newFullNamePrefix + sourceChild.getAddrName());//州市龙圩区广平镇大地
		String slash = BusiConstants.StringConstants.SLASH;
		newValue.setAddrPrivateName(newPnPrefix + sourceChild.getAddrId() + slash );//0/4/47040/
		newValue.setStr1(newStr1Prefix +slash+ sourceChild.getAddrName() );//梧州市/龙圩区/广平镇/留空/留空/大地
		newValue.setAddrId(sourceChild.getAddrId());
		newValue.setAddrParent(parent.getAddrId());
		
		//修改父ID，
		adTreeMapper.updateByPrimaryKeySelective(newValue);
		//修改其他三个属性
		updateAllChildrensFullNamePrivateNameAndStr1(newValue, sourceChild);;
	}
	
	/**
	 * 合并地址.
	 * @param targetId 合并后的地址.
	 * @param sourceId	被合并的地址.
	 * @return
	 * @throws Throwable
	 */
	public AdTree saveSingleMerge(Integer targetId, Integer sourceId) throws Throwable{
		//TODO 土鳖扛铁牛
		AddrDto target = queryAndBuildTree(targetId);
		AddrDto source = queryAndBuildTree(sourceId);
		checkRole(source.getAddrLevel());
		if(!target.getAddrLevel().equals(source.getAddrLevel())){
			throw new MessageException(StatusCodeConstant.MERGE_ERROR_LEVEL_DISMATCH);
		}
		AdTree targetParent = queryByKey(target.getAddrParent());
		int createDoneCode = createDoneCode(new Date(), BusiCodeConstants.MERGE);
		List<Map<Boolean, Integer>> ids2beDelete = new ArrayList<Map<Boolean,Integer>>();
		//true : source false : target
		Map<Boolean, Integer> map = new HashMap<Boolean, Integer>();
		map.put(true, sourceId);
		map.put(false, targetId);
		ids2beDelete.add(map);
		mergeAddress(source, target, targetParent, ids2beDelete);
		delMergeredTree(ids2beDelete,createDoneCode);
		return target;
	}
	
	/**
	 * 删除被合并掉的地址.
	 * @param list
	 * @param doneCode 
	 */
	private void delMergeredTree(List<Map<Boolean, Integer>> list, Integer doneCode)throws Throwable {
		if(CollectionHelper.isEmpty(list)){
			return;
		}
		Date createTime = new Date();
		String userOID = ThreadUserParamHolder.getOptr().getUserOID();
		String status = BusiConstants.Status.INVALID.name();
		for (Map<Boolean, Integer> map : list) {
			Integer addrId = map.get(true);
			
			AdTree tree = checkTreeExists(addrId);
			checkRole(tree.getAddrLevel());
			//TODO 判断有没有被BOSS系统引用，有没有被光站系统引用   
			this.addrNameChecker.usedByOtherSystem(tree);
			
			tree.setStatus(status);
			adTreeMapper.deleteByPrimaryKey(tree.getAddrId());
			AdTreeChange change = new AdTreeChange();
			CglibUtil.copy(tree, change);
			change.setChangeTime(createTime);
			change.setChangeCause("合并删除");
			change.setMergeAddrId(map.get(false));
			change.setChangeDoneCode(doneCode);
			change.setChangeType(AddrChangeType.MERGE_DEL.name());
			change.setChangeOptrId(userOID);
			
			List<AdCollections> colls = checkAddressIsCollected(addrId);
			colls = CollectionHelper.isEmpty(colls) ? new ArrayList<AdCollections>():colls;
			for (AdCollections coll : colls) {
				adCollectionsMapper.deleteByAddrAndUser(coll);
			}
			adTreeChangeMapper.insert(change);
		}
	}

	/**
	 * source和targe同级.合并的对象.
	 * @param source
	 * @param target
	 * @param parent 
	 * @param ids2beDelete
	 * @throws Throwable
	 */
	private void mergeAddress(AddrDto source, AddrDto target, AdTree parent, List<Map<Boolean, Integer>> ids2beDelete)throws Throwable {
		List<AddrDto> srcSons = source.getChildren();
		srcSons = CollectionHelper.isEmpty(srcSons) ? new ArrayList<AddrDto>():srcSons;
		List<AddrDto> tarSons = target.getChildren();
		if(CollectionHelper.isEmpty(tarSons) ){//target 子节点为空,直接把srouce的子节点更改父级.到 target
			for (AddrDto srcSon : srcSons) {
				changeParent(srcSon, target);
			}
		}else{//target 子节点不为空 
			/**
			 * 如果 target 的子节点不为空,比较是否有重名的.
			 * 如果不重名,把子节点直接变更父级为 target.
			 * 如果有重名,递归调用这个方法.
			 */
			Map<String, AddrDto> tarMapById = CollectionHelper.converToMapSingle(tarSons, "addrName");
			for (AddrDto scSon : srcSons) {
				String addrName = scSon.getAddrName();
				AddrDto tarSon = tarMapById.get(addrName);
				if(null != tarSon){//如果有重名
					Map<Boolean, Integer> map = new HashMap<Boolean, Integer>();
					map.put(true, scSon.getAddrId());
					map.put(false, tarSon.getAddrId());
					ids2beDelete.add(map);
					
					mergeAddress(scSon, tarSon, target, ids2beDelete);
				}else{
					changeParent(scSon, target);
				}
			}
			
		}
		
	}
	
	/**
	 * 查询地址以及它的子节点,并构建成树.
	 * @param addrId
	 * @return
	 * @throws Throwable
	 */
	private AddrDto queryAndBuildTree(Integer addrId) throws Throwable{
		AdTree addr = adTreeMapper.selectByPK(addrId);
		if(null == addr){
			throw new MessageException(StatusCodeConstant.MERGE_ERROR_SOME_ADDR_NOT_EXISTS);
		}
		List<AddrDto> children = adTreeMapper.selectAllPosterityForMerge(addr);
		children = CollectionHelper.isEmpty(children)? new ArrayList<AddrDto>():children;
		Map<String, List<AddrDto>> map = CollectionHelper.converToMap(children , "addrParent");
		AddrDto dto = new AddrDto();
		CglibUtil.copy(addr, dto);
		CollectionHelper.buildTree(dto, map);
		return dto;
	}
	
	/**
	 * 变更所有子节点的 三个属性.
	 * @param target
	 * @param source
	 */
	private void updateAllChildrensFullNamePrivateNameAndStr1(AdTree target, AdTree source) {
		Map<String, Object> param = new HashMap<String, Object>();
		String oldStr1 = source.getStr1();
		String oldFullName = source.getAddrFullName();
		String oldAddrPrivateName = source.getAddrPrivateName();
		
		String newStr1 = target.getStr1();
		String newAddrFullName = target.getAddrFullName();
		String newAddrPrivateName = target.getAddrPrivateName();
		
		//fullname
		param.put("oldStr1", oldStr1);
		param.put("newStr1", newStr1);
		//privateName
		param.put("oldFullName", oldFullName);
		param.put("newAddrFullName", newAddrFullName);
		//str1
		param.put("oldAddrPrivateName", oldAddrPrivateName);
		param.put("newAddrPrivateName", newAddrPrivateName);
		
		adTreeMapper.updateDirectChildrensOtherField(param);
	}

	/**
	 * 删除地址.
	 * @param addrId
	 * @throws Throwable
	 */
	public void delTree(Integer addrId) throws Throwable {
		int createDoneCode = createDoneCode(new Date(), BusiCodeConstants.DEL_ADDR);
		//以上留 空的接口
		delTree(addrId, createDoneCode,false);
	}

	/**
	 * @param addrId 要删除的ID.
	 * @param doneCode 
	 * @param force 是否强制删除(合并的时候,需要强制删除.因为加入数组的顺序问题.其实子节点也在删除的列表里,但是放在了后面导致会抛错)
	 * @throws Throwable
	 * @throws MessageException
	 */
	private void delTree(Integer addrId,  int doneCode, boolean force) throws Throwable, MessageException {
		Date createTime = new Date();
		AdTree tree = checkTreeExists(addrId);
		checkRole(tree.getAddrLevel());
		//TODO 判断有没有被BOSS系统引用
		//TODO 判断有没有被光站系统引用   
		this.addrNameChecker.usedByOtherSystem(tree);
		//只要有一个子节点都不给删除
		Pagination pager = findChildrensAndPagingByPid(addrId, 0, 1);
		List<Object> children = pager.getRecords();
		if(children != null && children.size() > 0 && ! force){
			throw new MessageException(StatusCodeConstant.ADDR_HAS_CHILDREN);
		}
		tree.setStatus(BusiConstants.Status.INVALID.name());
		adTreeMapper.deleteByPrimaryKey(tree.getAddrId());
		AdTreeChange change = new AdTreeChange();
		CglibUtil.copy(tree, change);
		change.setChangeTime(createTime);
		change.setChangeCause("删除");
		change.setChangeDoneCode(doneCode);
		change.setChangeType(AddrChangeType.MERGE_DEL.name());
		change.setChangeOptrId(ThreadUserParamHolder.getOptr().getUserOID());
		
		AdCollections coll = checkCollExists(addrId, getUserInSession().getUserOID());
		if(null != coll){
			adCollectionsMapper.deleteByAddrAndUser(coll);
		}
		
		adTreeChangeMapper.insert(change);
	}

	/**
	 * 收藏
	 * @param addrId
	 * @throws Throwable
	 */
	public void saveCollectTree(Integer addrId) throws Throwable {
		UserInSession optr = getUserInSession();
		checkTreeExists(addrId);
		String userId = optr.getUserOID();
		AdCollections coll = checkCollExists(addrId, userId);
		if(coll == null){
			coll = new AdCollections();
		}else{
			throw new MessageException(StatusCodeConstant.ADDR_COLL_ALREADY_EXISTS);
		}
		Date createTime = new Date();
		createDoneCode(createTime, BusiCodeConstants.COLLECT_ADDR);
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
	public void saveCancelCollectTree(Integer addrId) throws Throwable {
		UserInSession optr = getUserInSession();
		String userId = optr.getUserOID();
		AdCollections coll = checkCollExists(addrId, userId);
		if(coll == null){
			throw new MessageException(StatusCodeConstant.ADDR_COLL_NOT_EXISTS);
		}
		Date createTime = new Date();
		createDoneCode(createTime, BusiCodeConstants.DE_COLLECT_ADDR);
		adCollectionsMapper.deleteByAddrAndUser(coll);
		
	}
	
	/**
	 * 查询收藏.
	 * @param limit
	 * @return
	 * @throws Throwable
	 */
	public List<AdLevel> findCollectTreeList( Integer limit) throws Throwable {
		UserInSession optr = ThreadUserParamHolder.getOptr();
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
	 * 检查地址是否已经被收藏.
	 * @param addrId
	 * @param userId
	 * @return
	 * @throws MessageException
	 */
	private List<AdCollections> checkAddressIsCollected(Integer addrId){
		AdCollections coll = new AdCollections();
		coll.setAddrId(addrId);
		List<AdCollections> colls = adCollectionsMapper.selectByExample(coll);
		return colls;
	}

	
	/**
	 * 插入流水.
	 * @param createTime
	 * @param optrId
	 * @param code
	 * @return
	 */
	private int createDoneCode(Date createTime, BusiCodeConstants code ) throws Throwable{
		UserInSession optr = getUserInSession();
		AdDoneCode doneCode = new AdDoneCode();
		doneCode.setBusiCode(code.name());
		doneCode.setCreateTime(createTime);
		doneCode.setOptrId(optr.getUserOID());
		int createDoneCode = adDoneCodeMapper.insertSelective(doneCode);
		return createDoneCode;
	}
	
	/**
	 * 检查名字是否重复以及是否有多个留空地址.
	 * @param addrParent
	 * @param tree 至少保证 addrParent,addrName,isBlank 不为空
	 * @return
	 * @throws Throwable
	 */
	private boolean checkSameLevelAddrName(AdTree tree) throws Throwable {
		Pagination childrenPager = findChildrensAndPagingByPid(tree.getAddrParent(), 0, Integer.MAX_VALUE);
		List<AdTree> children = childrenPager.getRecords();
		return checkSameLevelAddrName(tree, children);
	}

	/**
	 * 检查名字是否重复以及是否有多个留空地址.如果重名,返回true,如果有多个留空地址,抛出异常
	 * @param tree
	 * @param children
	 * @return
	 * @throws MessageException
	 */
	private boolean checkSameLevelAddrName(AdTree tree,List<AdTree> children) throws MessageException {
		String addrName = tree.getAddrName();
		String isBlank = tree.getIsBlank();
		if(StringHelper.isEmpty(isBlank)){
			throw new MessageException(StatusCodeConstant.PARAM_MISSED_WHILE_CHECK_ADDR_NAME);
		}
		for (AdTree child : children) {
			if(child.getIsBlank().equals(isBlank) && BusiConstants.Booleans.T.name().equals(isBlank)){
				throw new MessageException(StatusCodeConstant.TOO_MANY_BLANK_ADDR);
			}
			if(child.getAddrName().equals(addrName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 检查地址的字面,并进行必要的修改.
	 * 包括,
	 * 非留空地址,地名不能为空.
	 * 去掉地址里的所有空格,全角字符自动转为半角,如果有特殊字符,抛出异常.
	 * @param tree
	 * @param addrName
	 * @return
	 * @throws MessageException
	 */
	private String literalCheckAddrName(AdTree tree, String addrName)
			throws MessageException {
		if(tree.getIsBlank().equals(BusiConstants.Booleans.T.name())){
			addrName = BusiConstants.StringConstants.BLANK_ADDR_NAME;
		}
		//地址不能为空 留空为 T 的例外
		if(StringHelper.isEmpty(addrName)){
			throw new MessageException(StatusCodeConstant.ADDR_NAME_IS_BLANK);
		}
		//地址去空格(包括里面的)  a 3
		addrName = StringHelper.replaceAllEmpty(addrName);
		//全角数字，标点  转半角
		addrName = StringHelper.full2Half(addrName);
		
		if(StringHelper.containSpecialCharacter(addrName)){
			throw new MessageException(StatusCodeConstant.ADDR_NAME_CONTAIN_INVALID_CHARS);
		}
		return addrName;
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
	
	/**
	 * 获取当前操作员的地址级别.
	 * @see UserController#setAddrScopeForCurrentUser(String, String, javax.servlet.http.HttpSession)
	 * @return
	 * @throws MessageException
	 */
	private String getBaseScope() throws MessageException{
		String baseQueryScope = ThreadUserParamHolder.getBaseQueryScope();
		return baseQueryScope;
	}
	
	
	/**
	 * 获取当前操作员的地址级别.
	 * @see UserController#setAddrScopeForCurrentUser(String, String, javax.servlet.http.HttpSession)
	 * @return
	 * @throws MessageException
	 */
	private String getGlobeCountyId() throws MessageException{
		String baseQueryScope = ThreadUserParamHolder.getGlobeCountyId();
		return baseQueryScope;
	}
	
	private void checkRole(Integer level)throws MessageException{
		Integer maxAllowedLevel = getUserInSession().getMaxLevelAllowed();
		if(maxAllowedLevel > level){
			throw new MessageException(StatusCodeConstant.USER_INSUFFICIENT_AUTHORIZED);
		}
	}
}
