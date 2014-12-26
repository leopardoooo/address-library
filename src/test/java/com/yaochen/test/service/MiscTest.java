package com.yaochen.test.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.easyooo.framework.common.util.CglibUtil;
import com.yaochen.address.common.CollectionHelper;
import com.yaochen.address.data.domain.address.AdTree;
import com.yaochen.address.data.mapper.address.AdRoleMapper;
import com.yaochen.address.data.mapper.address.AdTreeMapper;
import com.yaochen.address.dto.AddrDto;
import com.yaochen.address.service.TreeService;

public class MiscTest extends BaseTest{
	Integer source_id = 47073 ;
	Integer target_id = 47044 ;
	
	@Autowired
	private TreeService treeService;
	@Autowired
	private AdRoleMapper adRoleMapper;
	@Autowired
	private AdTreeMapper adTreeMapper;
	
	@Test
	public void testMerge() throws Throwable {
		source_id = 47073 ;
		target_id = 47044 ;
		
		AddrDto source = queryAndBuildTree(source_id);
		System.err.println(JSON.toJSONString(source, true));
		AddrDto target = queryAndBuildTree(target_id);
		AdTree parent = queryByKey(target.getAddrParent());
		System.err.println(JSON.toJSONString(target, true));
		List<Integer> ids = new ArrayList<Integer>();
		process(source, target,parent,ids);
		
	}
	
	/**
	 * source和targe同级.合并的对象.
	 * @param source
	 * @param target
	 * @param parent 
	 * @param ids2beDelete
	 * @throws Throwable
	 */
	private void process(AddrDto source, AddrDto target, AdTree parent, List<Integer> ids2beDelete)throws Throwable {
		List<AddrDto> srcSons = source.getChildren();
		srcSons = CollectionHelper.isEmpty(srcSons) ? new ArrayList<AddrDto>():srcSons;
		List<AddrDto> tarSons = target.getChildren();
		if(CollectionHelper.isEmpty(tarSons) ){//target 子节点为空,直接把srouce的子节点更改父级.到 target
			for (AddrDto srcSon : srcSons) {
				changeParent(srcSon, target);
			}
		}else{
			/**
			 * 如果 target 的子节点不为空,比较是否有重名的.
			 * 如果不重名,把子节点直接变更父级为 target.
			 * 如果有重名,递归调用这个方法.
			 */
			Map<String, AddrDto> tarMapById = CollectionHelper.converToMapSingle(tarSons, "addrId");
			for (AddrDto scSon : srcSons) {
				Integer addrId = scSon.getAddrId();
				String key = addrId.toString();
				AddrDto tarSon = tarMapById.get(key);
				if(null != tarSon){//如果有重名
					ids2beDelete.add(addrId);
					process(scSon, tarSon, target, ids2beDelete);
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
		List<AddrDto> children = adTreeMapper.selectAllPosterityForMerge(addr);
		Map<String, List<AddrDto>> map = CollectionHelper.converToMap(children , "addrParent");
		AddrDto dto = new AddrDto();
		CglibUtil.copy(addr, dto);
		buildTree(dto, map);
		return dto;
	}
	
	/**
	 * 构建一棵树.
	 * @param dto
	 * @param mapByPid
	 * @throws Throwable
	 */
	private void buildTree(AddrDto dto ,Map<String, List<AddrDto>> mapByPid ) throws Throwable{
		List<AddrDto> children = mapByPid.get(dto.getAddrId().toString());
		children = CollectionHelper.isEmpty(children) ? new ArrayList<AddrDto>():children;
		for (AddrDto child : children) {
			List<AddrDto> subChildren = mapByPid.get(child.getAddrId().toString());
			if(CollectionHelper.isNotEmpty(subChildren)){
				buildTree(child, mapByPid);
			}
		}
		dto.setChildren(children);
	}
	
	

	private void mergeAddrChildren(AdTree target, AdTree source,AdTree parent, List<Integer> ids)throws Throwable {
		ids.add(source.getAddrId());
		if(target.getAddrName().equals(source.getAddrName())){
			//如果重名,只用该 addrParent,private
			List<AdTree> targetChildren = findDirectChildren(target.getAddrId());
			List<AdTree> sourceChildren = findDirectChildren(source.getAddrId());
			
			if(CollectionHelper.isEmpty(targetChildren)){//目标没有子集
				//只需要把来源的子集变更父级.
				for (AdTree sourceChild : sourceChildren) {
					changeParent(sourceChild, target);
				}
			}else{//目标有子集
				//如果有重名,合并,否则,变更父级.
				Map<String, AdTree> targetMap = CollectionHelper.converToMapSingle(targetChildren, "addrName");
				for (AdTree sourceChild : sourceChildren) {
					AdTree targetChild = targetMap.get(sourceChild.getAddrName());
					if(null != targetChild){//冲突
						ids.add(targetChild.getAddrId());
						mergeAddrChildren(targetChild, sourceChild, target,ids);
					}else{
						changeParent(sourceChild, target);
					}
				}
			}
			
		}else{//如果不重名,则做变更上级操作.
			changeParent(target, parent);
		}
	}
	
	//查询直接下级
	private List<AdTree> findDirectChildren(Integer addrId) {
		return null;
	}


	private void changeParent(AdTree sourceChild,AdTree parent) throws Throwable{
		//update parent 
		//update fullname ,str1 ,privatename
	}
	private AdTree queryByKey(Integer targetId2) { return null; }
}
