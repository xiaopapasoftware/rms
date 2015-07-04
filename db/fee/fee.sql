/* Drop Tables */
drop table if exists t_electric_fee;
drop table if exists t_normal_fee;


/* Create Tables */
create table t_electric_fee
(
   ID                   varchar(64) NOT NULL,
   RENT_CONTRACT_ID     varchar(64) comment '出租合同',
   SETTLE_TYPE          varchar(64) comment '结算类型',
   START_DATE           date comment '电费缴纳开始时间',
   END_DATE             date comment '电费缴纳开始时间',
   METER_VALUE          float comment '入住电表系数',
   PERSON_FEE           float comment '自用金额',
   MULITI_FEE           float comment '分摊金额',
   SETTLE_STATUS        varchar(64) comment '结算状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '电费结算信息';

create table t_normal_fee
(
   ID                   varchar(64) NOT NULL,
   RENT_CONTRACT_ID     varchar(64) comment '出租合同',
   FEE_TYPE             varchar(64) comment '费用类型',
   SETTLE_TYPE          varchar(64) comment '结算类型',
   START_DATE           date comment '电费缴纳开始时间',
   END_DATE             date comment '电费缴纳开始时间',
   METER_VALUE          float comment '表系数',
   PERSON_FEE           float comment '金额',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '一般费用结算信息';