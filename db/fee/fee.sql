/* Drop Tables */
drop table if exists t_electric_fee;

/* Create Tables */
create table t_electric_fee
(
   ID                   varchar(64) NOT NULL,
   RENT_CONTRACT_ID     varchar(64) comment '出租合同ID',
   PAYMENT_TRANS_ID      varchar(64) comment '对应充值款项ID',
   CHARGE_DATE			date comment '充值时间',
   CHARGE_AMOUNT		float comment '充值金额',
   CHARGE_STATUS        varchar(64) comment '充值状态,0=充值中,1=充值成功,2=充值失败',
   SETTLE_STATUS		varchar(64) comment '结算状态，0=待结算；0=待结算；2=审核拒绝；3=审核通过',
   CHARGE_ID            varchar(64) comment '电表系统返回的充值ID',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '电费充值记录表';