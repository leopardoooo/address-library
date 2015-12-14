package com.yaochen.address.data.mapper.sys;

import com.easyooo.framework.sharding.annotation.Table;
import com.yaochen.address.data.domain.sys.SysSeq;
import com.yaochen.address.support.Repository;

@Repository
@Table("SYS_SEQ")
public interface SysSeqMapper {
	public Integer getAddrSequence(SysSeq sysSeq);
}
