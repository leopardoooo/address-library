package com.yaochen.address.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyooo.framework.support.mybatis.Pagination;
import com.yaochen.address.common.BusiConstants;
import com.yaochen.address.common.BusiConstants.Status;
import com.yaochen.address.common.DateHelper;
import com.yaochen.address.data.domain.sys.SysNotice;
import com.yaochen.address.data.domain.sys.SysNoticeCounty;
import com.yaochen.address.data.domain.sys.SysNoticeRead;
import com.yaochen.address.data.mapper.sys.SysNoticeCountyMapper;
import com.yaochen.address.data.mapper.sys.SysNoticeMapper;
import com.yaochen.address.data.mapper.sys.SysNoticeReadMapper;
import com.yaochen.address.dto.db.NoticeQueryForm;
import com.yaochen.address.support.ThreadUserParamHolder;

@Service
public class NoticeService {

	@Autowired private SysNoticeMapper sysNoticeMapper;
	@Autowired private SysNoticeCountyMapper sysNoticeCountyMapper;
	@Autowired private SysNoticeReadMapper sysNoticeReadMapper;
	
	public Pagination query(NoticeQueryForm notice, Integer start, Integer limit) {
		
		String countyId = ThreadUserParamHolder.getGlobeCountyId();
		
		notice.setOptr(ThreadUserParamHolder.getOptr().getUserOID());
		Pagination pager = new Pagination(notice,start,limit);
		if(BusiConstants.Booleans.T.name().equals(notice.getRead())){
			notice.setCountyId(countyId);
			sysNoticeMapper.queryUnchecked(pager);
		}else{
			if(!BusiConstants.StringConstants.COUNTY_ALL.equals(countyId)){
				notice.setCountyId(countyId);
				sysNoticeMapper.queryAllValid(pager);
			}else{
				sysNoticeMapper.queryAllValidForAdmin(pager);
			}
		}
		
		return pager;
	}

	public void save(NoticeQueryForm form, String[] countyIds) throws Throwable {
		
		SysNotice notice = new SysNotice();
		BeanUtils.copyProperties(form, notice);
		Date effDate = DateHelper.parseDate(form.getEffDate(), DateHelper.FORMAT_YMD);
		Date invalidDate = DateHelper.parseDate(form.getEffDate(), DateHelper.FORMAT_YMD);
		Date createTime = null;
		try {
			createTime = DateHelper.parseDate(form.getEffDate(), DateHelper.FORMAT_YMD);
		} catch (Exception e) {
		}
		notice.setCreateTime(createTime);
		notice.setEffDate(effDate);
		notice.setInvalidDate(invalidDate);
		
		
		Integer noticeId = notice.getNoticeId();
		if(null ==noticeId){
			notice.setCreateOptr(ThreadUserParamHolder.getOptr().getUserOID());
			notice.setCreateTime(new Date());
			notice.setStatus(Status.ACTIVE.name());
			sysNoticeMapper.insert(notice);
			noticeId = notice.getNoticeId();
		}else{
			sysNoticeMapper.updateByPrimaryKeySelective(notice);
		}
		
		sysNoticeCountyMapper.deleteByNoticeId(noticeId);
		
		if(countyIds==null){
			return;
		}
		
		for (String countyId : countyIds) {
			SysNoticeCounty nc = new SysNoticeCounty();
			nc.setCountyId(countyId);
			nc.setNoticeId(noticeId);
			sysNoticeCountyMapper.insert(nc);
		}
		
	}

	public void remove(Integer noticeId) {
		SysNotice not = new SysNotice();
		not.setNoticeId(noticeId);
		not.setStatus(Status.INVALID.name());
		sysNoticeMapper.updateByPrimaryKeySelective(not);
	}

	public void saveCheckRead(Integer noticeId) {
		String userId = ThreadUserParamHolder.getOptr().getUserOID();
		SysNoticeRead read = new SysNoticeRead();
		read.setMarkTime(new Date());
		read.setNoticeId(noticeId);
		read.setOptrId(userId);
		sysNoticeReadMapper.insert(read);
	}

	public Integer countUnChecked() {
		String userId = ThreadUserParamHolder.getOptr().getUserOID();
		String countyId = ThreadUserParamHolder.getGlobeCountyId();
		Map<String, String> map = new HashMap<String, String>();
		map.put("countyId", countyId);
		map.put("userID", userId);
		Integer countUnChecked = sysNoticeMapper.countUnChecked(map);
		return countUnChecked;
	}

	public List<String> queryCities(Integer noticeId) {
		List<SysNoticeCounty> cts = sysNoticeCountyMapper.selectByNoticeId(noticeId);
		List<String> list = new ArrayList<String>();
		if(cts==null){
			return list;
		}
		for(SysNoticeCounty sc:cts){
			list.add(sc.getCountyId());
		}
		return list;
	}

}
