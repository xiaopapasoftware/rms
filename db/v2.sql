alter table t_house add intent_mode varchar(64) comment '意向租赁类型';

alter table t_house add is_feature varchar(64) comment '是否精选房源';
alter table t_room add is_feature varchar(64) comment '是否精选房源';

update t_house set is_feature='0' where is_feature is null;
update t_room set is_feature='0' where is_feature is null;

alter table t_house add rental float comment '意向租金';
alter table t_room add rental float comment '意向租金';

alter table t_house add short_desc varchar(255) comment '描述';
alter table t_room add short_desc varchar(255) comment '描述';

alter table t_house add short_location varchar(255) comment '地址描述';
alter table t_room add short_location varchar(255) comment '地址描述';

alter table t_house add pay_way varchar(2) comment '付款方式';
alter table t_room add pay_way varchar(2) comment '付款方式';

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('356','0','付三押一','rent_fee_type','付款方式','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('357','1','付二押二','rent_fee_type','付款方式','2','0','1',now(),'1',now(),NULL,'0');

insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('314','113','0,1,113,','预约管理','50106','/contract/book',NULL,NULL,'1',NULL,'1',now(),'1',now(),NULL,'0');
insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('315','314','0,1,96,314','查看','30',NULL,NULL,NULL,'0','contract:contractBook:view','1',now(),'1',now(),NULL,'0');
insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('316','314','0,1,96,314','修改','60',NULL,NULL,NULL,'0','contract:contractBook:edit','1',now(),'1',now(),NULL,'0');

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('358','0','管家确认中','book_status','预约状态','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('359','1','预约成功','book_status','预约状态','2','0','1',now(),'1',now(),NULL,'0');

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('360','1','管理系统','data_source','数据来源','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('361','2','APP','data_source','数据来源','2','0','1',now(),'1',now(),NULL,'0');

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('362','1','未支付','order_status','订单状态','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('363','2','已支付','order_status','订单状态','2','0','1',now(),'1',now(),NULL,'0');

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('364','0','管家确认中','booked_status','预定状态','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('365','1','预定成功','booked_status','预定状态','2','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('366','2','已支付','booked_status','预定状态','3','0','1',now(),'1',now(),NULL,'0');

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('367','0','管家确认中','sign_status','签约状态','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('368','1','签约成功','sign_status','签约状态','2','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('369','2','已支付','sign_status','签约状态','3','0','1',now(),'1',now(),NULL,'0');

create table t_contract_book
(
	ID                   varchar(64) NOT NULL,
	user_id              varchar(64),
	house_id             varchar(64),
	room_id              varchar(64),
	user_name            varchar(64),
	user_phone           varchar(64),
	user_gender          varchar(64),
	book_date            DATETIME,
	book_status          varchar(64),
	sales_id             varchar(64),
	CREATE_BY            VARCHAR(64) COMMENT '创建者',
  CREATE_DATE          DATETIME 	COMMENT '创建时间',
  UPDATE_BY            VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
  REMARKS              VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_estonian_ci comment = '预约看房信息';

ALTER TABLE T_DEPOSIT_AGREEMENT ADD data_source VARCHAR(64) default '1' COMMENT '数据来源 管理系统/APP';
update T_DEPOSIT_AGREEMENT set data_source = '1';

create table t_payment_order
(
	ID                   varchar(64) NOT NULL,
	order_id             varchar(64),
	order_amount         float,
	order_date           DATETIME,
	order_status         varchar(2),
	trade_id             varchar(64) comment '账务交易ID',
	trans_id             varchar(64) comment '支付交易单号',
	trans_date           DATETIME,
	CREATE_BY            VARCHAR(64) COMMENT '创建者',
  CREATE_DATE          DATETIME 	COMMENT '创建时间',
  UPDATE_BY            VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
  REMARKS              VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	primary key (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_estonian_ci comment = '支付订单信息';

ALTER TABLE T_RENT_CONTRACT ADD data_source VARCHAR(64) default '1' COMMENT '数据来源 管理系统/APP';
update T_RENT_CONTRACT set data_source = '1';


alter table t_attachment add BIZ_ID varchar(64) comment '业务ID';

drop table if exists T_MESSAGE;
/* Create Tables */
create table T_MESSAGE
(
   ID                   varchar(64) NOT NULL,
   TITLE    	 varchar(200) comment '消息标题',
   CONTENT           varchar(500) comment '消息内容',
   TYPE	 VARCHAR(10) comment  '消息类型',
   SENDER	 VARCHAR(30) COMMENT  '发送人',
   RECEIVER	 VARCHAR(30)  COMMENT  '接收人',
   STATUS	 VARCHAR(10) COMMENT  '状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '消息表';

drop table if exists T_REPAIRS;
/* Create Tables */
create table T_REPAIRS
(
   ID                   varchar(64) NOT NULL,
   USER_ID    	 varchar(20) comment '用户ID, APP用户为注册手机号',
   USER_MOBILE          varchar(64) comment '报修填写的联系手机号',
   ROOM_ID	 VARCHAR(64) COMMENT '房间号',
   DESCRIPTION	 VARCHAR(18) comment  '描述',
   STEWARD      	 VARCHAR(1)  COMMENT  '管家',
   STEWARD_MOBILE	 VARCHAR(10) COMMENT  '管家电话',
   STATUS	 INT	 COMMENT  '报修状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '报修记录';
