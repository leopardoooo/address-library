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
import com.yaochen.address.common.MessageException;
import com.yaochen.address.common.StatusCodeConstant;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.data.domain.address.AdDoneCode;
import com.yaochen.address.data.domain.address.AdLevel;
import com.yaochen.address.data.domain.address.AdRoleRes;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.mapper.address.AdDoneCodeMapper;
import com.yaochen.address.data.mapper.address.AdLevelMapper;
import com.yaochen.address.data.mapper.address.AdRoleResMapper;
import com.yaochen.address.data.mapper.address.AdTreeMapper;
import com.yaochen.address.dto.SystemFunction;
import com.yaochen.address.dto.UserInSession;
import com.yaochen.address.support.AddrNameChecker;
import com.yaochen.address.support.ThreadUserHolder;

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
	
	/**
	 * 新增地址.
	 * @param tree
	 * @return
	 * @throws Throwable
	 */
	public Integer addTree(AdTree tree) throws Throwable{
		UserInSession optr = ThreadUserHolder.getOptr();
		Date createTime = new Date();
		String optrId = optr.getUserOID();
		String countyId = optr.getDepartmentOID();//TODO 是不是这个字段？？
		
		tree.setCreateTime(createTime);
		tree.setCreateOptrId(optrId);
		
		tree.setCountyId(countyId);
		//验证
		if(!addrNameChecker.check(tree )){
			throw new MessageException(StatusCodeConstant.ADDR_NAME_INVALID);
		}
		
		int createDoneCode = createDoneCode(createTime, BusiCodeConstants.ADD_ADDR);
		
		tree.setCreateDoneCode(createDoneCode);
		
		return adTreeMapper.insertSelective(tree);
	}

	/**
	 * 插入流水.
	 * @param createTime
	 * @param optrId
	 * @param code
	 * @return
	 */
	private int createDoneCode(Date createTime, BusiCodeConstants code) {
		AdDoneCode doneCode = new AdDoneCode();
		doneCode.setBusiCode(code.name());
		doneCode.setCreateTime(createTime);
		doneCode.setOptrId(ThreadUserHolder.getOptr().getUserOID());
		int createDoneCode = adDoneCodeMapper.insertSelective(doneCode);
		return createDoneCode;
	}
	
	public List<Integer> addTrees(AdTree param,Integer startPosi,Integer endPosi) throws Throwable{
		//验证参数
		if(null == startPosi || null == endPosi){ //提示起始位置不能为空
			logger.info("参数为空");
			throw new MessageException(StatusCodeConstant.ADDR_NAME_INVALID);
		}
		if(endPosi < startPosi){ //后者不能小于前者
			throw new MessageException(StatusCodeConstant.ADDR_NAME_INVALID);
		}
		
		List<Integer> result = new ArrayList<Integer>();
		UserInSession optr = ThreadUserHolder.getOptr();
		Date createTime = new Date();
		String optrId = optr.getUserOID();
		String countyId = optr.getDepartmentOID();//TODO 是不是这个字段？？
		
		int createDoneCode = createDoneCode(createTime, BusiCodeConstants.ADD_ADDR_BATCH);
		
		for (Integer index = startPosi; index < endPosi; index++) {
			AdTree tree = new AdTree();
			CglibUtil.copy(param, tree);
			tree.setCreateDoneCode(createDoneCode);
			tree.setCreateTime(createTime);
			tree.setCountyId(countyId);
			tree.setCreateOptrId(optrId);
			tree.setAddrName(index.toString());
			if(!addrNameChecker.check(tree)){
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
		Pagination pager = new Pagination(param,start,limit);
		adTreeMapper.selectByKeyWord(pager);
		return pager;
	}

	/**
	 * 查询当前用户有权访问的Level
	 * @throws Throwable
	 */
	public List<AdLevel> findAuthLevelByCurrentUser() throws Throwable {
		UserInSession optr = ThreadUserHolder.getOptr();
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

	public void cancelCollectTree(Integer integer) throws Throwable {
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
		Pagination pager = new Pagination(param,start,limit);
		List<AdTree> list = adTreeMapper.selectByPid(pager);
		return pager;
	}


	public void findCollectTreeList(Integer integer) throws Throwable {
	}

	public void modTree(AdTree adtree) throws Throwable {
	}

	public void delTree(Integer integer) throws Throwable {
	}

	public void collectTree(Integer integer) throws Throwable {
	}
}
