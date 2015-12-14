/** 避免查询造成麻烦 **/
update ad_tree t set t.addr_parent = -1 where t.addr_id = 0; 
CREATE TABLE `std_device` (
  `std_dev_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '光节点ID',
  `std_level` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '级别',
  `jd_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '名称',
  `jd_addr_id` int(11) NOT NULL COMMENT '节点所在的地址',
  `pid` int(11) NOT NULL DEFAULT '0' COMMENT '上级ID',
  `county_id` varchar(16) COLLATE utf8_bin NOT NULL COMMENT '所属分公司',
  `create_optr` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '录入操作员',
  `create_time` datetime NOT NULL COMMENT '录入时间',
  PRIMARY KEY (`std_dev_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='标准设备信息';


CREATE TABLE `std_device_addr_rel` (
  `rel_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '关联表主键',
  `gjd_id` int(11) NOT NULL COMMENT '光节点ID',
  `addr_id` int(11) NOT NULL COMMENT '地址ID',
  PRIMARY KEY (`rel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='光节点与地址的关联';

CREATE TABLE `std_service_team` (
  `team_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '维护队ID',
  `team_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '维护队名称',
  `create_optr` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建的操作员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `county_id` varchar(16) COLLATE utf8_bin NOT NULL COMMENT '所属分公司',
  `base_serv_addr` int(11) NOT NULL COMMENT '服务的社区基本范围',
  `status` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '状态',
  PRIMARY KEY (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='标准维护队信息';

CREATE TABLE `std_steward` (
  `steward_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '维护人员ID',
  `steward_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '维护人员ID',
  `team_id` int(11) NOT NULL COMMENT '所属服务队的ID',
  PRIMARY KEY (`steward_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='标准维护人员';

