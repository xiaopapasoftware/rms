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
  `city_code` varchar(6) DEFAULT NULL COMMENT '城市编码',
  `city_name` varchar(30)  DEFAULT NULL COMMENT '城市名称',
  `district_code` varchar(6)  DEFAULT NULL COMMENT '行政区县编码',
  `district_name` varchar(30) DEFAULT NULL COMMENT '行政区县名称',
  `coordsys` tinyint(4) DEFAULT NULL COMMENT '坐标系 0:百度,1:高德',
  `comm_req_id` varchar(16)  DEFAULT NULL COMMENT '小区同步请求号',
  `alipay_status` tinyint(4) DEFAULT NULL COMMENT '小区同步状态',
   primary key (ID)
) comment = '物业项目';

create table T_BUILDING
(
   ID                   varchar(64) NOT NULL,
   T_PROPERTY_PROJECT_MAIN_ID      varchar(64) comment '物业项目',
   BUILDING_NAME    varchar(100) comment '楼宇名称',
   TOTAL_FLOOR_COUNT tinyint(4) DEFAULT NULL COMMENT '总楼层数',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   TOTAL_FLOOR_COUNT tinyint(4) DEFAULT NULL COMMENT '总楼层数',
  nick_name varchar(16) DEFAULT NULL COMMENT '公寓别名',
  min_amount varchar(16) DEFAULT NULL COMMENT '房源最小租金',
  max_amount varchar(16) DEFAULT NULL COMMENT '房源最大租金',
   primary key (ID)
) comment = '楼宇信息';

CREATE TABLE `t_house` (
  `ID` varchar(64) COLLATE utf8_estonian_ci NOT NULL,
  `PROPERTY_PROJECT_ID` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '物业项目',
  `BUILDING_ID` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '楼宇',
  `OWNER_ID` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '业主',
  `HOUSE_NO` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '房屋号',
  `HOUSE_FLOOR` int(11) DEFAULT NULL COMMENT '楼层',
  `HOUSE_SPACE` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '原始建筑面积',
  `DECORATION_SPANCE` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '装修建筑面积',
  `HOUSE_STATUS` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '房屋状态',
  `CREATE_BY` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '更新者',
  `UPDATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `REMARKS` varchar(255) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '备注信息',
  `DEL_FLAG` char(1) COLLATE utf8_estonian_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  `ORI_STRUC_ROOM_NUM` int(11) DEFAULT NULL,
  `ORI_STRUC_CUSSPAC_NUM` int(11) DEFAULT NULL,
  `ORI_STRUC_WASHRO_NUM` int(11) DEFAULT NULL,
  `DECORA_STRUC_ROOM_NUM` int(11) DEFAULT NULL,
  `DECORA_STRUC_CUSSPAC_NUM` int(11) DEFAULT NULL,
  `DECORA_STRUC_WASHRO_NUM` int(11) DEFAULT NULL,
  `house_Code` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL,
  `CERTIFICATE_NO` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL,
  `intent_mode` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '意向租赁类型',
  `is_feature` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '是否精选房源',
  `rental` float DEFAULT NULL COMMENT '意向租金',
  `short_desc` varchar(255) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '描述',
  `short_location` varchar(255) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '地址描述',
  `service_user` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '服务管家',
  `ele_account_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `water_account_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `gas_account_num` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `new_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(16) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '公寓类型',
  `share_area_config` varchar(32) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '公共区域物品配置',
  `rent_month_gap` tinyint(4) DEFAULT NULL COMMENT '支付间隔月数',
  `depos_month_count` tinyint(4) DEFAULT NULL COMMENT '押金月数',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `unique_id` (`new_id`)
) ENGINE=InnoDB AUTO_INCREMENT=955 DEFAULT CHARSET=utf8 COLLATE=utf8_estonian_ci COMMENT='房屋信息'
update t_house set is_feature='0' where is_feature is null;

CREATE TABLE `t_room` (
  `ID` varchar(64) COLLATE utf8_estonian_ci NOT NULL,
  `PROPERTY_PROJECT_ID` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '物业项目',
  `BUILDING_ID` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '楼宇',
  `HOUSE_ID` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '房屋',
  `ROOM_NO` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '房间号',
  `METER_NO` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '电表号',
  `ROOM_SPACE` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '房间面积',
  `ORIENTATION` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '朝向',
  `ROOM_STATUS` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '房间状态',
  `CREATE_BY` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '更新者',
  `UPDATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `REMARKS` varchar(255) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '备注信息',
  `DEL_FLAG` char(1) COLLATE utf8_estonian_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  `is_feature` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '是否精选房源',
  `rental` float DEFAULT NULL COMMENT '意向租金',
  `short_desc` varchar(255) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '描述',
  `short_location` varchar(255) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '地址描述',
  `new_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `room_config` varchar(32) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '物品配置',
  `rent_month_gap` tinyint(4) DEFAULT NULL COMMENT '支付间隔月数',
  `depos_month_count` tinyint(4) DEFAULT NULL COMMENT '押金月数',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `unique_id` (`new_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3048 DEFAULT CHARSET=utf8 COLLATE=utf8_estonian_ci COMMENT='房间信息'
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