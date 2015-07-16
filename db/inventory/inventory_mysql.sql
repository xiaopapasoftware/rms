/* Drop Tables */
drop table if exists T_NEIGHBORHOOD;
drop table if exists T_MANAGEMENT_COMPANY;
drop table if exists T_PROPERTY_PROJECT;
drop table if exists T_BUILDING;
drop table if exists T_HOUSE;
drop table if exists T_ROOM;
drop table if exists T_DEVICES;
drop table if exists t_room_devices;
drop table if exists T_DEVICES_HIS;


/* Create Tables */
create table T_NEIGHBORHOOD
(
   ID                   varchar(64) NOT NULL,
   NEIGHBORHOOD_NAME    varchar(100) comment '居委会名称',
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
   COMPANY_NAME    varchar(100) comment '物业公司名称',
   COMPANY_ADDR    VARCHAR(100) comment '物业公司地址',
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
   PROJECT_NAME    varchar(100) comment '物业项目名称',
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
   HOUSE_NO             varchar(100) comment '房屋号',
   HOUSE_FLOOR          int comment '楼层',
   HOUSE_SPACE          varchar(100) comment '原始建筑面积',
   DECORATION_SPANCE    varchar(100) comment '装修建筑面积',
   HOUSE_STRUCTURE   	varchar(100) comment '原始房屋结构',
   DECORATION_STRUCTURE varchar(100) comment '装修房屋结构',
   HOUSE_STATUS         varchar(100) comment '房屋状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '房屋信息';

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
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '房间信息';

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