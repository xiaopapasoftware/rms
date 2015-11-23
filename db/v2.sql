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