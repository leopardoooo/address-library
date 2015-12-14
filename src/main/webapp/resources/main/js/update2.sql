CREATE TABLE `sys_notice` (
  `notice_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_title` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '标题',
  `notice_content` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_optr` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建者操作员',
  `eff_date` datetime NOT NULL COMMENT '生效日期',
  `invalid_date` datetime NOT NULL COMMENT '失效日期',
  `status` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统通知消息';



CREATE TABLE `sys_notice_county` (
  `notice_id` int(11) NOT NULL,
  `county_id` varchar(8) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`notice_id`,`county_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='通知适用的分公司信息';


CREATE TABLE `sys_notice_read` (
  `notice_id` int(11) NOT NULL COMMENT '通知ID',
  `optr_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '操作员ID',
  `mark_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '标记已读的时间',
  PRIMARY KEY (`notice_id`,`optr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='通知已读记录';

