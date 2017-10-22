/* Drop Tables */
drop table if exists sys_user_area;
drop table if exists T_NEIGHBORHOOD;
drop table if exists T_MANAGEMENT_COMPANY;
drop table if exists T_PROPERTY_PROJECT;
drop table if exists T_BUILDING;
drop table if exists T_HOUSE;
drop table if exists T_ROOM;
drop table if exists T_DEVICES;
drop table if exists t_room_devices;
drop table if exists T_DEVICES_HIS;
drop table if exists T_HOUSE_OWNER;

CREATE TABLE `sys_user_area` (
  `area_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '区域编号',
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户编号',
  PRIMARY KEY (`area_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='区域-菜用户';

/* Create Tables */
create table T_NEIGHBORHOOD
(
   ID                   varchar(64) NOT NULL,
   NEIGHBORHOOD_NAME    varchar(100) comment '居委会名称',
   AREA_ID 				VARCHAR(64)  COMMENT '区域id',
   NEIGHBORHOOD_ADDR    VARCHAR(100) comment '居委会地址',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '居委会';

create table T_MANAGEMENT_COMPANY
(
   ID                   varchar(64) NOT NULL,
   COMPANY_NAME   		varchar(100) comment '物业公司名称',
   AREA_ID 				VARCHAR(64)  COMMENT '区域id',
   COMPANY_ADDR    		VARCHAR(100) comment '物业公司地址',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '物业公司';

create table T_PROPERTY_PROJECT
(
   ID                   varchar(64) NOT NULL,
   T_NEIGHBORHOOD_MAIN_ID      varchar(64) comment '居委会',
   T_MANAGEMENT_COMPANY_MAIN_ID varchar(64) comment '物业公司',
   AREA_ID 				VARCHAR(64)  COMMENT '区域id',
   PROJECT_NAME    varchar(100) comment '物业项目名称',
   PROJECT_SIMPLE_NAME    varchar(100) comment '物业项目拼音首字母',
   PROJECT_ADDR    VARCHAR(100) comment '物业项目地址',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '物业项目';

create table T_BUILDING
(
   ID                   varchar(64) NOT NULL,
   T_PROPERTY_PROJECT_MAIN_ID      varchar(64) comment '物业项目',
   BUILDING_NAME    varchar(100) comment '楼宇名称',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '楼宇信息';

create table T_HOUSE
(
   ID                   varchar(64) NOT NULL,
   PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
   BUILDING_ID          varchar(64) comment '楼宇',
   OWNER_ID             varchar(64) comment '业主',
   HOUSE_CODE			varchar(64) comment '系统生成的唯一房屋编码',
   HOUSE_NO             varchar(100) comment '房屋号',
   HOUSE_FLOOR          int comment '楼层',
   HOUSE_SPACE          varchar(100) comment '原始建筑面积',
   DECORATION_SPANCE    varchar(100) comment '装修建筑面积',
   ORI_STRUC_ROOM_NUM    	int comment 	'原始房屋结构房数',
   ORI_STRUC_CUSSPAC_NUM    int comment 	'原始房屋结构厅数',
   ORI_STRUC_WASHRO_NUM     int comment		'原始房屋结构卫数',
   DECORA_STRUC_ROOM_NUM    	int comment 	'装修房屋结构房数',
   DECORA_STRUC_CUSSPAC_NUM    int comment 		'装修房屋结构厅数',
   DECORA_STRUC_WASHRO_NUM     int comment		'装修房屋结构卫数',
   CERTIFICATE_NO			varchar(64) comment '房屋产权证号',
   ele_account_num			VARCHAR(64) comment '电户号',
   water_account_num		VARCHAR(64) comment '水户号',
   gas_account_num			VARCHAR(64) comment '煤气户号',
   intent_mode 				varchar(64) comment '意向租赁类型';
   HOUSE_STATUS         varchar(100) comment '房屋状态',
   service_user 		varchar(64) comment '服务管家',
   is_feature 			varchar(64) comment '是否精选房源',
   rental 				float comment '意向租金',
   short_desc 			varchar(255) comment '描述',
   short_location 		varchar(255) comment '地址描述',
   pay_way 				varchar(2) comment '付款方式',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '房屋信息';
update t_house set is_feature='0' where is_feature is null;

create table T_ROOM
(
   ID                   varchar(64) NOT NULL,
   PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
   BUILDING_ID          varchar(64) comment '楼宇',
   HOUSE_ID             varchar(64) comment '房屋',
   ROOM_NO              varchar(100) comment '房间号',
   METER_NO             varchar(100) comment '电表号',
   ROOM_SPACE           varchar(100) comment '房间面积',
   ORIENTATION          varchar(64) comment '朝向',
   STRUCTURE            varchar(64) comment '附属结构',
   ROOM_STATUS          varchar(100) comment '房间状态',
   is_feature 			varchar(64) comment '是否精选房源',
   rental 				float 		comment '意向租金',
   short_desc 			varchar(255) comment '描述',
   short_location 		varchar(255) comment '地址描述',
   pay_way 				varchar(2) 	comment '付款方式',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '房间信息';
update t_room set is_feature='0' where is_feature is null;

create table T_DEVICES
(
   ID                   varchar(64) NOT NULL,
   DEVICE_ID            varchar(64) comment '设备编号',
   DISTR_SERL_NUM		varchar(64) comment '设备分配序号',
   DEVICE_NAME          varchar(100) comment '设备名称',
   DEVICE_MODEL         varchar(100) comment '设备型号',
   DEVICE_TYPE          varchar(100) comment '设备类型',
   DEVICE_PRICE         float comment '设备采购价格',
   DEVICE_BRAND         varchar(64) comment '设备品牌',
   DEVICE_STATUS        varchar(100) comment '设备状态',
   DEVICES_CHOOSE_FLAG	varchar(16) comment '设备分配状态', 
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '设备信息';

create table t_room_devices
(
	ID                   varchar(64) NOT NULL,
	PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
  BUILDING_ID          varchar(64) comment '楼宇',
  HOUSE_ID             varchar(64) comment '房屋',
  ROOM_ID              varchar(64) comment '房间 0代表公共区域',
  DEVICE_ID            varchar(64) comment '设备ID',
	CREATE_BY            VARCHAR(64) COMMENT '创建者',
  CREATE_DATE          DATETIME COMMENT '创建时间',
  UPDATE_BY            VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
  REMARKS              VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
  primary key (ID)
) comment = '房屋设备关联信息';

create table T_DEVICES_HIS
(
   ID                   varchar(64) NOT NULL,
   PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
   BUILDING_ID          varchar(64) comment '楼宇',
   HOUSE_ID             varchar(64) comment '房屋',
   ROOM_ID              varchar(64) comment '房间 0代表公共区域',
   OPER_TYPE            varchar(64) comment '行为（添加0/删除1）',
   DEVICE_ID            varchar(64) comment '设备ID',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '设备变更信息';

create table T_HOUSE_OWNER
(
	ID                   varchar(64) NOT NULL,
  HOUSE_ID             varchar(64) comment '房屋',
  OWNER_ID             varchar(64) comment '设备ID',
	CREATE_BY            VARCHAR(64) COMMENT '创建者',
  CREATE_DATE          DATETIME COMMENT '创建时间',
  UPDATE_BY            VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
  REMARKS              VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
  primary key (ID)
) comment = '房屋业主关联信息';