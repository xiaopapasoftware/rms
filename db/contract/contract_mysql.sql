/* Drop Tables */
drop table if exists T_LEASE_CONTRACT;


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