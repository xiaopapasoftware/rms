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

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('500','0','付三押一','rent_fee_type','付款方式','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('501','1','付二押二','rent_fee_type','付款方式','2','0','1',now(),'1',now(),NULL,'0');

insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('314','113','0,1,113,','预约管理','50106','/contract/book',NULL,NULL,'1',NULL,'1',now(),'1',now(),NULL,'0');
insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('315','314','0,1,96,314','查看','30',NULL,NULL,NULL,'0','contract:contractBook:view','1',now(),'1',now(),NULL,'0');
insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('316','314','0,1,96,314','修改','60',NULL,NULL,NULL,'0','contract:contractBook:edit','1',now(),'1',now(),NULL,'0');

insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('330','1','0,1,','门锁管理','810','','','','1','','1',now(),'1',now(),'','0');
insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('331','330','0,1,330,','门锁管理','30',NULL,NULL,NULL,'1',NULL,'1',now(),'1',now(),NULL,'0');
insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('332','331','0,1,330,331','科技侠门锁钥匙管理','30','/lock/scienerLockKey',NULL,NULL,'1',NULL,'1',now(),'1',now(),NULL,'0');

delete from sys_dict where type='book_status';
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('372','0','等待管家确认','book_status','预约状态','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('373','1','预约成功','book_status','预约状态','2','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('374','2','用户取消预约','book_status','预约状态','3','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('375','3','管家取消预约','book_status','预约状态','4','0','1',now(),'1',now(),NULL,'0');

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('502','1','管理系统','data_source','数据来源','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('503','2','APP','data_source','数据来源','2','0','1',now(),'1',now(),NULL,'0');

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('504','1','未支付','order_status','订单状态','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('505','2','已支付','order_status','订单状态','2','0','1',now(),'1',now(),NULL,'0');

delete from sys_dict where type = 'booked_status';
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('364','0','等待管家确认','booked_status','预定状态','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('365','1','预定成功等待支付','booked_status','预定状态','2','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('366','2','定金支付成功','booked_status','预定状态','3','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('379','3','管家确认成功请您核实','booked_status','预定状态','4','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('380','4','管家取消预定','booked_status','预定状态','5','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('381','5','用户取消预定','booked_status','预定状态','6','0','1',now(),'1',now(),NULL,'0');

delete from sys_dict where type = 'sign_status';
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('367','0','等待管家确认','sign_status','签约状态','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('368','1','在线签约成功等待支付','sign_status','签约状态','2','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('369','2','在线签约支付成功','sign_status','签约状态','3','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('376','3','管家取消在线签约','sign_status','签约状态','4','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('377','4','管家确认成功请您核实','sign_status','签约状态','5','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('378','5','用户取消在线签约','sign_status','签约状态','6','0','1',now(),'1',now(),NULL,'0');

DROP TABLE IF EXISTS t_contract_book;
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
) comment = '预约看房信息';

ALTER TABLE T_DEPOSIT_AGREEMENT ADD data_source VARCHAR(64) default '1' COMMENT '数据来源 管理系统/APP';
update T_DEPOSIT_AGREEMENT set data_source = '1';

DROP TABLE IF EXISTS t_payment_order;
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
) comment = '支付订单信息';

alter table t_payment_order add house_id varchar(64);

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
   CONTRACT_ID  VARCHAR(64) comment '合同号',
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


insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('320','105','0,1,105,','广告活动管理','20107','/inventory/ad',NULL,NULL,'1',NULL,'1',now(),'1',now(),NULL,'0');
insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('321','320','0,1,105,320','查看','30',NULL,NULL,NULL,'0','inventory:ad:view','1',now(),'1',now(),NULL,'0');
insert into sys_menu (id, parent_id, parent_ids, name, sort, href, target, icon, is_show, permission, create_by, create_date, update_by, update_date, remarks, del_flag) values('322','320','0,1,105,320','修改','60',NULL,NULL,NULL,'0','inventory:ad:edit','1',now(),'1',now(),NULL,'0');

drop table if exists t_house_ad;
create table t_house_ad
(
	ID                   varchar(64) NOT NULL,
	ad_name              varchar(64) comment '广告名称',
	ad_type              varchar(2) comment '广告类型 0:图片广告 1:房源链接式广告',
	PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
  BUILDING_ID          varchar(64) comment '楼宇',
  HOUSE_ID             varchar(64) comment '房屋',
  ROOM_ID              varchar(64) comment '房间',
	ad_value             varchar(64) comment '房源ID',
	ad_url               varchar(255) comment '广告图片地址',
	CREATE_BY            VARCHAR(64) COMMENT '创建者',
  CREATE_DATE          DATETIME 	COMMENT '创建时间',
  UPDATE_BY            VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
  REMARKS              VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	primary key (ID)
) comment = '广告';

insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('370','0','图片广告','ad_type','广告类型','1','0','1',now(),'1',now(),NULL,'0');
insert into sys_dict (id, value, label, type, description, sort, parent_id, create_by, create_date, update_by, update_date, remarks, del_flag) values('371','1','房源链接式广告','ad_type','广告类型','2','0','1',now(),'1',now(),NULL,'0');

alter table t_house add service_user varchar(64) comment '服务管家';

drop table if exists T_REPAIR;
/* Create Tables */
create table T_REPAIR
(
   ID                   varchar(64) NOT NULL,
   USER_ID    	 varchar(20) comment '报修人ID',
   USER_NAME    	 varchar(20) comment '报修人',
   USER_MOBILE           varchar(20) comment '报修电话',
   REPAIR_MOBILE	 VARCHAR(20) comment  '报修联系电话',
	EXPECT_REPAIR_TIME	VARCHAR(50) comment '期望维修时间',
   CONTRACT_ID	 VARCHAR(64) COMMENT  '合同号',
   ROOM_ID	 VARCHAR(64)  COMMENT  '房间号',
   DESCRIPTION	 VARCHAR(500) COMMENT  '报修描述',
   KEEPER	 VARCHAR(20) COMMENT  '管家',
   KEEPER_MOBILE	 VARCHAR(20) COMMENT  '管家电话',
   STATUS	 VARCHAR(10) COMMENT  '状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '报修表';