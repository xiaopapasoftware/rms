/* Drop Tables */
DROP TABLE IF EXISTS sys_user_area;
DROP TABLE IF EXISTS T_NEIGHBORHOOD;
DROP TABLE IF EXISTS T_MANAGEMENT_COMPANY;
DROP TABLE IF EXISTS T_PROPERTY_PROJECT;
DROP TABLE IF EXISTS T_BUILDING;
DROP TABLE IF EXISTS T_HOUSE;
DROP TABLE IF EXISTS T_ROOM;
DROP TABLE IF EXISTS T_DEVICES;
DROP TABLE IF EXISTS t_room_devices;
DROP TABLE IF EXISTS T_DEVICES_HIS;
DROP TABLE IF EXISTS T_HOUSE_OWNER;

CREATE TABLE `sys_user_area` (
  `area_id` VARCHAR(64) COLLATE utf8_bin NOT NULL
  COMMENT '区域编号',
  `user_id` VARCHAR(64) COLLATE utf8_bin NOT NULL
  COMMENT '用户编号',
  PRIMARY KEY (`area_id`, `user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin
  COMMENT ='区域-菜用户';

/* Create Tables */
CREATE TABLE T_NEIGHBORHOOD
(
  ID                VARCHAR(64)         NOT NULL,
  NEIGHBORHOOD_NAME VARCHAR(100) COMMENT '居委会名称',
  AREA_ID           VARCHAR(64) COMMENT '区域id',
  NEIGHBORHOOD_ADDR VARCHAR(100) COMMENT '居委会地址',
  CREATE_BY         VARCHAR(64) COMMENT '创建者',
  CREATE_DATE       DATETIME COMMENT '创建时间',
  UPDATE_BY         VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE       TIMESTAMP COMMENT '更新时间',
  REMARKS           VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG          CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '居委会';

CREATE TABLE T_MANAGEMENT_COMPANY
(
  ID           VARCHAR(64)         NOT NULL,
  COMPANY_NAME VARCHAR(100) COMMENT '物业公司名称',
  AREA_ID      VARCHAR(64) COMMENT '区域id',
  COMPANY_ADDR VARCHAR(100) COMMENT '物业公司地址',
  CREATE_BY    VARCHAR(64) COMMENT '创建者',
  CREATE_DATE  DATETIME COMMENT '创建时间',
  UPDATE_BY    VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE  TIMESTAMP COMMENT '更新时间',
  REMARKS      VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG     CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '物业公司';

CREATE TABLE T_PROPERTY_PROJECT
(
  ID                           VARCHAR(64)         NOT NULL,
  T_NEIGHBORHOOD_MAIN_ID       VARCHAR(64) COMMENT '居委会',
  T_MANAGEMENT_COMPANY_MAIN_ID VARCHAR(64) COMMENT '物业公司',
  AREA_ID                      VARCHAR(64) COMMENT '区域id',
  PROJECT_NAME                 VARCHAR(100) COMMENT '物业项目名称',
  PROJECT_SIMPLE_NAME          VARCHAR(100) COMMENT '物业项目拼音首字母',
  PROJECT_ADDR                 VARCHAR(100) COMMENT '物业项目地址',
  CREATE_BY                    VARCHAR(64) COMMENT '创建者',
  CREATE_DATE                  DATETIME COMMENT '创建时间',
  UPDATE_BY                    VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE                  TIMESTAMP COMMENT '更新时间',
  REMARKS                      VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG                     CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  `city_code`                  VARCHAR(6)  DEFAULT NULL
  COMMENT '城市编码',
  `city_name`                  VARCHAR(30) DEFAULT NULL
  COMMENT '城市名称',
  `district_code`              VARCHAR(6)  DEFAULT NULL
  COMMENT '行政区县编码',
  `district_name`              VARCHAR(30) DEFAULT NULL
  COMMENT '行政区县名称',
  `coordsys`                   TINYINT(4)  DEFAULT NULL
  COMMENT '坐标系 0:百度,1:高德',
  `comm_req_id`                VARCHAR(16) DEFAULT NULL
  COMMENT '小区同步请求号',
  `alipay_status`              TINYINT(4)  DEFAULT NULL
  COMMENT '小区同步状态',
  PRIMARY KEY (ID)
)
  COMMENT = '物业项目';

CREATE TABLE T_BUILDING
(
  ID                         VARCHAR(64)         NOT NULL,
  T_PROPERTY_PROJECT_MAIN_ID VARCHAR(64) COMMENT '物业项目',
  BUILDING_NAME              VARCHAR(100) COMMENT '楼宇名称',
  TOTAL_FLOOR_COUNT          TINYINT(4)                          DEFAULT NULL
  COMMENT '总楼层数',
  CREATE_BY                  VARCHAR(64) COMMENT '创建者',
  CREATE_DATE                DATETIME COMMENT '创建时间',
  UPDATE_BY                  VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE                TIMESTAMP COMMENT '更新时间',
  REMARKS                    VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG                   CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  TOTAL_FLOOR_COUNT          TINYINT(4)                          DEFAULT NULL
  COMMENT '总楼层数',
  nick_name                  VARCHAR(16)                         DEFAULT NULL
  COMMENT '公寓别名',
  min_amount                 VARCHAR(16)                         DEFAULT '0'
  COMMENT '房源最小租金',
  max_amount                 VARCHAR(16)                         DEFAULT '0'
  COMMENT '房源最大租金',
  type                       VARCHAR(1) COLLATE utf8_estonian_ci DEFAULT NULL
  COMMENT '公寓类型,1=分散式，2=集中式',
  PRIMARY KEY (ID)
)
  COMMENT = '楼宇信息';

CREATE TABLE `t_house` (
  `ID`                       VARCHAR(64) COLLATE utf8_estonian_ci NOT NULL,
  `PROPERTY_PROJECT_ID`      VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '物业项目',
  `BUILDING_ID`              VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '楼宇',
  `OWNER_ID`                 VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '业主',
  `HOUSE_NO`                 VARCHAR(100) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '房屋号',
  `HOUSE_FLOOR`              INT(11)                                       DEFAULT NULL
  COMMENT '楼层',
  `HOUSE_SPACE`              VARCHAR(100) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '原始建筑面积',
  `DECORATION_SPANCE`        VARCHAR(100) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '装修建筑面积',
  `HOUSE_STATUS`             VARCHAR(100) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '房屋状态',
  `CREATE_BY`                VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '创建者',
  `CREATE_DATE`              DATETIME                                      DEFAULT NULL
  COMMENT '创建时间',
  `UPDATE_BY`                VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '更新者',
  `UPDATE_DATE`              TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  `REMARKS`                  VARCHAR(255) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '备注信息',
  `DEL_FLAG`                 CHAR(1) COLLATE utf8_estonian_ci     NOT NULL DEFAULT '0'
  COMMENT '删除标记',
  `ORI_STRUC_ROOM_NUM`       INT(11)                                       DEFAULT NULL,
  `ORI_STRUC_CUSSPAC_NUM`    INT(11)                                       DEFAULT NULL,
  `ORI_STRUC_WASHRO_NUM`     INT(11)                                       DEFAULT NULL,
  `DECORA_STRUC_ROOM_NUM`    INT(11)                                       DEFAULT NULL,
  `DECORA_STRUC_CUSSPAC_NUM` INT(11)                                       DEFAULT NULL,
  `DECORA_STRUC_WASHRO_NUM`  INT(11)                                       DEFAULT NULL,
  `house_Code`               VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL,
  `CERTIFICATE_NO`           VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL,
  `intent_mode`              VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '意向租赁类型',
  `is_feature`               VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '是否精选房源',
  `rental`                   FLOAT                                         DEFAULT NULL
  COMMENT '意向租金',
  `short_desc`               VARCHAR(255) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '描述',
  `short_location`           VARCHAR(255) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '地址描述',
  `service_user`             VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '服务管家',
  `sales_user`               VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '跟进销售',
  `ele_account_num`          VARCHAR(64) CHARACTER SET utf8
  COLLATE utf8_bin                                                         DEFAULT NULL,
  `water_account_num`        VARCHAR(64) CHARACTER SET utf8
  COLLATE utf8_bin                                                         DEFAULT NULL,
  `gas_account_num`          VARCHAR(64) CHARACTER SET utf8
  COLLATE utf8_bin                                                         DEFAULT NULL,
  `new_id`                   BIGINT(20) UNSIGNED                  NOT NULL AUTO_INCREMENT,
  `share_area_config`        VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '公共区域物品配置 第1位：WIFI；第2位：空调；第3位：热水器；第4位：洗衣机；第5位：冰箱；第6位：电视；第7位：微波炉；第8位：燃气灶；第9位：抽油烟机；第10位：电磁炉；第11位：床；第12位：书桌；第13位：衣柜；第14位：沙发；第15位：阳台；第16位：独卫。',
  `rent_month_gap`           TINYINT(4)                                    DEFAULT NULL
  COMMENT '支付间隔月数',
  `depos_month_count`        TINYINT(4)                                    DEFAULT NULL
  COMMENT '押金月数',
  `alipay_status`            TINYINT(4)                                    DEFAULT NULL
  COMMENT '支付宝同步状态 0：同步处理中，1：同步成功，2：同步失败（审批拒绝）',
  `up`                       TINYINT(4)                                    DEFAULT NULL
  COMMENT '上下架 0:下架  1:上架',
  `reservation_phone`        VARCHAR(32)                                   DEFAULT NULL
  COMMENT '预约热线电话',
  `fee_config_info`          VARCHAR(1024)                                 DEFAULT NULL
  COMMENT '同步至支付宝租房的房源各种费用信息，形如： 费用描述1=123,费用描述2=123,费用描述3=123,费用描述4=123,费用描述5=134,费用描述6=212',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `unique_id` (`new_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 955
  DEFAULT CHARSET = utf8
  COLLATE = utf8_estonian_ci
  COMMENT ='房屋信息';
UPDATE t_house
SET is_feature = '0'
WHERE is_feature IS NULL;

CREATE TABLE `t_room` (
  `ID`                  VARCHAR(64) COLLATE utf8_estonian_ci NOT NULL,
  `PROPERTY_PROJECT_ID` VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '物业项目',
  `BUILDING_ID`         VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '楼宇',
  `HOUSE_ID`            VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '房屋',
  `ROOM_NO`             VARCHAR(100) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '房间号',
  `METER_NO`            VARCHAR(100) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '电表号',
  `ROOM_SPACE`          VARCHAR(100) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '房间面积',
  `ORIENTATION`         VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '朝向',
  `ROOM_STATUS`         VARCHAR(100) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '房间状态',
  `CREATE_BY`           VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '创建者',
  `CREATE_DATE`         DATETIME                                      DEFAULT NULL
  COMMENT '创建时间',
  `UPDATE_BY`           VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '更新者',
  `UPDATE_DATE`         TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  `REMARKS`             VARCHAR(255) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '备注信息',
  `DEL_FLAG`            CHAR(1) COLLATE utf8_estonian_ci     NOT NULL DEFAULT '0'
  COMMENT '删除标记',
  `is_feature`          VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '是否精选房源',
  `rental`              FLOAT                                         DEFAULT NULL
  COMMENT '意向租金',
  `short_desc`          VARCHAR(255) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '描述',
  `short_location`      VARCHAR(255) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '地址描述',
  `new_id`              BIGINT(20) UNSIGNED                  NOT NULL AUTO_INCREMENT,
  `sales_user`          VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '跟进销售',
  `room_config`         VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '物品配置 第1位：WIFI；第2位：空调；第3位：热水器；第4位：洗衣机；第5位：冰箱；第6位：电视；第7位：微波炉；第8位：燃气灶；第9位：抽油烟机；第10位：电磁炉；第11位：床；第12位：书桌；第13位：衣柜；第14位：沙发；第15位：阳台；第16位：独卫。',
  `rent_month_gap`      TINYINT(4)                                    DEFAULT NULL
  COMMENT '支付间隔月数',
  `depos_month_count`   TINYINT(4)                                    DEFAULT NULL
  COMMENT '押金月数',
  `alipay_status`       TINYINT(4)                                    DEFAULT NULL
  COMMENT '支付宝同步状态 0：同步处理中，1：同步成功，2：同步失败（审批拒绝）',
  `up`                  TINYINT(4)                                    DEFAULT NULL
  COMMENT '上下架 0:下架  1:上架',
  `reservation_phone`   VARCHAR(32)                                   DEFAULT NULL
  COMMENT '预约热线电话',
  `fee_config_info`     VARCHAR(1024)                                 DEFAULT NULL
  COMMENT '同步至支付宝租房的房源各种费用信息，形如： 费用描述1=123,费用描述2=123,费用描述3=123,费用描述4=123,费用描述5=134,费用描述6=212',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `unique_id` (`new_id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3048
  DEFAULT CHARSET = utf8
  COLLATE = utf8_estonian_ci
  COMMENT ='房间信息';
UPDATE t_room
SET is_feature = '0'
WHERE is_feature IS NULL;

CREATE TABLE T_DEVICES
(
  ID                  VARCHAR(64)         NOT NULL,
  DEVICE_ID           VARCHAR(64) COMMENT '设备编号',
  DISTR_SERL_NUM      VARCHAR(64) COMMENT '设备分配序号',
  DEVICE_NAME         VARCHAR(100) COMMENT '设备名称',
  DEVICE_MODEL        VARCHAR(100) COMMENT '设备型号',
  DEVICE_TYPE         VARCHAR(100) COMMENT '设备类型',
  DEVICE_PRICE        FLOAT COMMENT '设备采购价格',
  DEVICE_BRAND        VARCHAR(64) COMMENT '设备品牌',
  DEVICE_STATUS       VARCHAR(100) COMMENT '设备状态',
  DEVICES_CHOOSE_FLAG VARCHAR(16) COMMENT '设备分配状态',
  CREATE_BY           VARCHAR(64) COMMENT '创建者',
  CREATE_DATE         DATETIME COMMENT '创建时间',
  UPDATE_BY           VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE         TIMESTAMP COMMENT '更新时间',
  REMARKS             VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG            CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '设备信息';

CREATE TABLE t_room_devices
(
  ID                  VARCHAR(64)         NOT NULL,
  PROPERTY_PROJECT_ID VARCHAR(64) COMMENT '物业项目',
  BUILDING_ID         VARCHAR(64) COMMENT '楼宇',
  HOUSE_ID            VARCHAR(64) COMMENT '房屋',
  ROOM_ID             VARCHAR(64) COMMENT '房间 0代表公共区域',
  DEVICE_ID           VARCHAR(64) COMMENT '设备ID',
  CREATE_BY           VARCHAR(64) COMMENT '创建者',
  CREATE_DATE         DATETIME COMMENT '创建时间',
  UPDATE_BY           VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE         TIMESTAMP COMMENT '更新时间',
  REMARKS             VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG            CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '房屋设备关联信息';

CREATE TABLE T_DEVICES_HIS
(
  ID                  VARCHAR(64)         NOT NULL,
  PROPERTY_PROJECT_ID VARCHAR(64) COMMENT '物业项目',
  BUILDING_ID         VARCHAR(64) COMMENT '楼宇',
  HOUSE_ID            VARCHAR(64) COMMENT '房屋',
  ROOM_ID             VARCHAR(64) COMMENT '房间 0代表公共区域',
  OPER_TYPE           VARCHAR(64) COMMENT '行为（添加0/删除1）',
  DEVICE_ID           VARCHAR(64) COMMENT '设备ID',
  CREATE_BY           VARCHAR(64) COMMENT '创建者',
  CREATE_DATE         DATETIME COMMENT '创建时间',
  UPDATE_BY           VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE         TIMESTAMP COMMENT '更新时间',
  REMARKS             VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG            CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '设备变更信息';

CREATE TABLE T_HOUSE_OWNER
(
  ID          VARCHAR(64)         NOT NULL,
  HOUSE_ID    VARCHAR(64) COMMENT '房屋',
  OWNER_ID    VARCHAR(64) COMMENT '设备ID',
  CREATE_BY   VARCHAR(64) COMMENT '创建者',
  CREATE_DATE DATETIME COMMENT '创建时间',
  UPDATE_BY   VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE TIMESTAMP COMMENT '更新时间',
  REMARKS     VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG    CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '房屋业主关联信息';