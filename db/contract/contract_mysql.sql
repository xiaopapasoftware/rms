/* Drop Tables */
drop table if exists T_LEASE_CONTRACT;
drop table if exists T_DEPOSIT_AGREEMENT;


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
   CONTRACT_DATE         date comment '合同签订时间',
   DEPOSIT              float comment '承租押金',
   CONTRACT_STATUS      VARCHAR(64) COMMENT '合同审核状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          TIMESTAMP COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '承租合同';

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
   CREATE_DATE          TIMESTAMP COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '定金协议';