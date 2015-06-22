/* Drop Tables */
drop table if exists T_LEASE_CONTRACT;
drop table if exists T_DEPOSIT_AGREEMENT;
drop table if exists T_RENT_CONTRACT;
drop table if exists T_ACCOUNTING;
drop table if exists T_AGREEMENT_CHANGE;
drop table if exists T_ATTACHMENT;
drop table if exists T_LEASE_CONTRACT_DTL;
drop table if exists T_AUDIT;
drop table if exists T_AUDIT_HIS;
drop table if exists T_CONTRACT_TENANT;


/* Create Tables */
create table T_LEASE_CONTRACT
(
   ID                   varchar(64) NOT NULL,
   PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
   BUILDING_ID  varchar(64) comment '楼宇',
   HOUSE_ID  varchar(64) comment '房屋',
   REMITTANCER_ID  varchar(64) comment '汇款人',
   CONTRACT_NAME         VARCHAR(100) comment '承租合同名称',
   EFFECTIVE_DATE       date comment '合同生效时间',
   FIRST_REMITTANCE_DATE       date comment '首次打款日期',
   REMITTANCE_DATE      varchar(64) comment '打款日期',
   EXPIRED_DATE         date comment '合同过期时间',
   CONTRACT_DATE        date comment '合同签订时间',
   DEPOSIT              float comment '承租押金',
   CONTRACT_STATUS      VARCHAR(64) COMMENT '合同审核状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '承租合同';

create table T_LEASE_CONTRACT_DTL
(
   ID                   varchar(64) NOT NULL,
   LEASE_CONTRACT_ID    varchar(64) comment '承租合同',
   START_DATE           date comment '起始时间',
   END_DATE             date comment '结束时间',
   DEPOSIT              float comment '月承租价',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '承租合同明细';

create table T_DEPOSIT_AGREEMENT
(
	 ID                   varchar(64) NOT NULL,
	 PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
   BUILDING_ID          varchar(64) comment '楼宇',
   HOUSE_ID             varchar(64) comment '房屋',
   ROOM_ID              varchar(64) comment '房间',
   RENT_MODE            varchar(64) comment '出租方式',
   USER_ID              varchar(64) comment '销售',
   AGREEMENT_NAME       varchar(100) comment '定金协议名称',
   START_DATE           date comment '协议开始时间',
   EXPIRED_DATE         date comment '协议结束时间',
   SIGN_DATE            date comment '协议签订时间',
   REN_MONTHS           int comment '首付房租月数',
   DEPOSIT_MONTHS       int comment '房租押金月数',
   AGREEMENT_DATE       date comment '约定合同签约时间',
   DEPOSIT_AMOUNT       float comment '定金金额',
   HOUSING_RENT         float comment '房屋租金',
   AGREEMENT_STATUS  varchar(64) comment '定金协议审核状态',
   AGREEMENT_BUSI_STATUS  varchar(64) comment '定金协议业务状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '定金协议';

create table T_CONTRACT_TENANT
(
	 ID                   varchar(64) NOT NULL,
	 DEPOSIT_AGREEMENT_ID VARCHAR(64) comment '定金协议',
	 CONTRACT_ID          varchar(64) comment '出租合同',	
	 LEASE_CONTRACT_ID    varchar(64) comment '承租合同',	
	 TENANT_ID            varchar(64) comment '租客',
	 CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '合同租客关联信息';

create table T_RENT_CONTRACT
(
	 ID                   varchar(64) NOT NULL,
	 CONTRACT_ID          varchar(64) comment '原出租合同',
	 CONTRACT_NAME        varchar(100) comment '合同名称',
   RENT_MODE            varchar(64) comment '出租方式',
   PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
   BUILDING_ID          varchar(64) comment '楼宇',
   HOUSE_ID             varchar(64) comment '房屋',
   ROOM_ID              varchar(64) comment '房间',
   USER_ID              varchar(64) comment '销售',
   CONTRACT_SOURCE      varchar(64) comment '合同来源',
   PARNTER              varchar(64) comment '合作人',
   RENTAL               float comment '月租金',
   START_DATE           date comment '合同生效时间',
   EXPIRED_DATE         date comment '合同过期时间',
   SIGN_DATE            date comment '合同签订时间',
   SIGN_TYPE            varchar(64) comment '合同签订类型',
   HAS_TV               varchar(64) comment '是否开通有线电视',
   TV_FEE               float comment '有线电视每月费用',
   HAS_NET              varchar(64) comment '是否开通宽带',
   NET_FEE              float comment '每月宽带费用',
   WATER_FEE            float comment '合租每月水费',
   SERVICE_FEE          float comment '服务费比例',
   REN_MONTHS           int comment '首付房租月数',
   DEPOSIT_MONTHS       int comment '房租押金月数',
   DEPOSIT_AMOUNT       float comment '房租押金金额',
   DEPOSIT_ELECTRIC_AMOUNT       float comment '水电押金金额',
   HAS_VISA              varchar(64) comment '是否需办理居住证及落户',
   METER_VALUE         float comment '入住分电表系数',
   TOTAL_METER_VALUE         float comment '入住总电表系数',
   PEAK_METER_VALUE         float comment '入住峰电系数',
   FLAT_METER_VALUE         float comment '入住平电系数',
   VALLEY_METER_VALUE         float comment '入住谷电系数',
   COAL_VALUE          float comment '入住煤表系数',
   WATER_VALUE         float comment '入住水表系数',
   REMIND_TIME         date comment '续租提醒时间',
   CONTRACT_STATUS      VARCHAR(64) COMMENT '合同状态',
   CONTRACT_BUSI_STATUS      VARCHAR(64) COMMENT '合同业务状态',
   CHARGE_TYPE          VARCHAR(64) COMMENT '付费方式',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '出租合同';


create table T_ACCOUNTING
(
	 ID                   varchar(64) NOT NULL,
	 RENT_CONTRACT_ID     varchar(64) comment '出租合同',
   ACCOUNTING_TYPE      varchar(64) comment '核算类型',
   FEE_DIRECTION        varchar(64) comment '核算费用方向',
   FEE_TYPE             varchar(64) comment '核算费用类别',
   FEE_AMOUNT           float comment '核算金额',
   USER_ID              varchar(100) comment '核算人',
   FEE_DATE             TIMESTAMP comment '核算时间',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '退租核算';

create table T_AGREEMENT_CHANGE
(
	 ID                   varchar(64) NOT NULL,
	 RENT_CONTRACT_ID     varchar(64) comment '出租合同',
   AGREEMENT_CHANGE_NAME      varchar(64) comment '合同变更协议名称',
   START_DATE           date comment '协议生效时间',
   RENT_MODE            varchar(64) comment '出租方式',
   AGREEMENT_STATUS     varchar(64) comment '协议审核状态',
   USER_ID              varchar(100) comment '核算人',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '协议变更';

create table T_ATTACHMENT
(
   ID                   varchar(64) NOT NULL,
   LEASE_CONTRACT_ID    varchar(64) comment '承租合同',
   RENT_CONTRACT_ID     varchar(64) comment '出租合同',
   PROPERTY_PROJECT_ID	varchar(64) comment '物业项目',
   BUILDING_ID			varchar(64) comment '楼宇',
   HOUSE_ID				varchar(64) comment '房屋',
   ROOM_ID				varchar(64) comment '房间',
   ATTACHMENT_NAME      varchar(64) comment '附件名称',
   ATTACHMENT_TYPE      varchar(64) comment '附件类型',
   ATTACHMENT_PATH      varchar(4000) comment '附件地址',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '附件';

create table T_AUDIT
(
	 ID                   varchar(64) NOT NULL,
	 OBJECT_TYPE          varchar(64) comment '审核类型',
	 OBJECT_ID            varchar(64) comment '审核对象ID',
   NEXT_ROLE            varchar(100) comment '下一级审核角色',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '审核表';

create table T_AUDIT_HIS
(
	 ID                   varchar(64) NOT NULL,
	 OBJECT_TYPE          varchar(64) comment '审核类型',
	 OBJECT_ID            varchar(64) comment '审核对象ID',
   AUDIT_USER           varchar(100) comment '审核人',
   AUDIT_TIME           datetime comment '审核时间',
   AUDIT_STATUS         varchar(100) comment '审核状态',
   AUDIT_MSG            varchar(100) comment '审核意见',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '审核历史表';