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
import com.yaochen.address.data.mapper.address.AdRoleResMapper;
import com.yaochen.address.data.mapper.address.AdTreeChangeMapper;
import com.yaochen.address.data.mapper.address.AdTreeMapper;
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
	
	/**
	 * 新增地址.
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	public AdTree addTree(AdTree tree) throws Throwable{
		UserInSession optr = getUserInSession();
		
		//验证同一级下名称是否重复
		Date createTime = new Date();
		String optrId = optr.getUserOID();
		String countyId = optr.getDepartmentOID();//TODO 是不是这个字段？？
		
		tree.setCreateTime(createTime);
		tree.setCreateOptrId(optrId);
		
		tree.setCountyId(countyId);
		//验证
		String addrName = checkAddrName(tree);
		boolean isBlank = tree.getIsBlank().equals(BusiConstants.Booleans.T.name());
		int createDoneCode = createDoneCode(createTime, BusiCodeConstants.ADD_ADDR);
		
		tree.setAddrName(addrName);
		tree.setCreateDoneCode(createDoneCode);
		AdTree parentNode = queryByKey(tree.getAddrParent());
		String str1 = null;
		if(null != parentNode){
			str1 = parentNode.getStr1() +( isBlank ? "": BusiConstants.StringConstants.SLASH + addrName );
		}else{//如果没有上级,这里一定是一级地址
			str1 = ( isBlank ? "": addrName );
		}
		tree.setStr1(str1);
		tree.setStatus(BusiConstants.StringConstants.ADDR_INIT_STATUS);
		//新增的树的ID
		adTreeMapper.insertSelective(tree);
		Integer newAddedAddrId = tree.getAddrId();
		
		String addrPrivateName = BusiConstants.StringConstants.TOP_PID + BusiConstants.StringConstants.SLASH + newAddedAddrId ;
		if(null != parentNode){
			addrPrivateName = parentNode.getAddrPrivateName() +   BusiConstants.StringConstants.SLASH + newAddedAddrId ;
		}
		tree.setAddrPrivateName(addrPrivateName);
		adTreeMapper.updateByPrimaryKeySelective(tree);
		return adTreeMapper.selectByPrimaryKey(newAddedAddrId);
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
		String check = addrNameChecker.checkBusiRule(tree );
		if(null!=check){
			throw new MessageException(StatusCodeConstant.ADDR_NAME_INVALID);
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
	 * @return
	 * @throws Throwable
	 */
	public List<Integer> addTrees(AdTree param,String addrNamePreffix, String addrNameSuffix, String start,String end) throws Throwable{
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
		Date createTime = new Date();
		String optrId = optr.getUserOID();
		String countyId = optr.getDepartmentOID();//TODO 是不是这个字段？？
		
		int createDoneCode = createDoneCode(createTime, BusiCodeConstants.ADD_ADDR_BATCH);
		Pagination childrenPager = findChildrensAndPagingByPid(param.getAddrParent(), 0, Integer.MAX_VALUE);
		List<AdTree> children = childrenPager.getRecords();
		AdTree parentNode = queryByKey(param.getAddrParent());
		addrNamePreffix = StringHelper.isEmpty(addrNamePreffix) ? "":addrNamePreffix;
		addrNameSuffix = StringHelper.isEmpty(addrNameSuffix) ? "":addrNameSuffix;
		
		for (int index = startPosi; index <= endPosi; index++) {
			AdTree tree = new AdTree();
			CglibUtil.copy(param, tree);
			tree.setCreateDoneCode(createDoneCode);
			tree.setCreateTime(createTime);
			tree.setCountyId(countyId);
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
				continue;
			}
			tree.setAddrName(addrName);
			boolean isBlank = param.getIsBlank().equals(BusiConstants.Booleans.T.name());
			String str1 = null;
			String addrFullName = null;
			if(null != parentNode){
				str1 = parentNode.getStr1() +BusiConstants.StringConstants.SLASH + addrName;
				addrFullName = parentNode.getAddrFullName() + ( isBlank ? "": BusiConstants.StringConstants.SLASH + addrName );
			}else{//如果没有上级,这里一定是一级地址
				str1 = addrName;
				addrFullName = ( isBlank ? "": addrName );
			}
			tree.setStr1(str1);
			tree.setAddrFullName(addrFullName);
			tree.setStatus(BusiConstants.StringConstants.ADDR_INIT_STATUS);
			adTreeMapper.insertSelective(tree);
			Integer addrId = tree.getAddrId();
			
			String addrPrivateName = BusiConstants.StringConstants.TOP_PID + BusiConstants.StringConstants.SLASH + addrId ;
			if(null != parentNode){
				addrPrivateName = parentNode.getAddrPrivateName() +   BusiConstants.StringConstants.SLASH + addrId ;
			}
			tree.setAddrPrivateName(addrPrivateName);
			adTreeMapper.updateByPrimaryKeySelective(tree);
			
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
		String baseScope = getBaseScope();
		param.put("baseScope", baseScope);
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
	public List<AdLevel> findAuthLevelByCurrentUser() throws Throwable {
		UserInSession optr = getUserInSession();
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
//		String baseScope = getBaseScope();
//		System.err.println(baseScope);
		param.put("addrParent", parentAddrId);
		String baseScope = getBaseScope();
		param.put("baseScope", baseScope);
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
	public void modTree(AdTree tree, boolean ignoreEmpty) throws Throwable {
		AdTree oldTree = adTreeMapper.selectByPrimaryKey(tree.getAddrId());
		if(oldTree== null){
			throw new MessageException(StatusCodeConstant.ADDR_NOT_EXISTS);
		}
		Date createTime = new Date();
		String oldAddrName = oldTree.getAddrName();
		String addrName = tree.getAddrName();
		if(StringHelper.isNotEmpty(addrName) && !StringHelper.bothEmptyOrEquals(oldAddrName,addrName)){
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
		if(StringHelper.isNotEmpty(addrName) && !StringHelper.bothEmptyOrEquals(oldAddrName,addrName)){
			//这里只是为了方便传参数
			tree.setStr1(oldAddrName);
			tree.setAddrPrivateName(oldTree.getAddrPrivateName());
			adTreeMapper.updateFullNameAndChildren(tree);
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
		return adTreeMapper.selectByPrimaryKey(addrId);
	}

	public void delTree(Integer addrId) throws Throwable {
		//以上留 空的接口
		AdTree tree = checkTreeExists(addrId);
		//TODO 判断有没有被BOSS系统引用
		//TODO 判断有没有被光站系统引用   
		this.addrNameChecker.usedByOtherSystem(tree);
		//只要有一个子节点都不给删除
		Pagination pager = findChildrensAndPagingByPid(addrId, 0, 1);
		List<Object> children = pager.getRecords();
		if(children != null && children.size() > 0){
			throw new MessageException(StatusCodeConstant.ADDR_HAS_CHILDREN);
		}
		tree.setStatus(BusiConstants.Status.INVALID.name());
		Date createTime = new Date();
		adTreeMapper.deleteByPrimaryKey(tree.getAddrId());
		AdTreeChange change = new AdTreeChange();
		CglibUtil.copy(tree, change);
		change.setChangeTime(createTime);
		change.setChangeCause("删除");
		change.setChangeDoneCode(createDoneCode(createTime, BusiCodeConstants.DEL_ADDR));
		change.setChangeType(AddrChangeType.MERGE_DEL.name());
		change.setChangeOptrId(ThreadUserParamHolder.getOptr().getUserOID());
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
			if(child.getIsBlank().equals(isBlank)){
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
	
}
