drop table if exists T_TRADING_ACCOUNTS;
drop table if exists T_PAYMENT_TRANS;
drop table if exists T_RECEIPT;
drop table if exists T_INVOICE;
drop table if exists T_PAYMENT_TRADE;
DROP TABLE IF EXISTS t_payment_order;

create table T_TRADING_ACCOUNTS
(
   ID                   varchar(64) NOT NULL,
   TRADE_ID             varchar(64) comment '账务交易对象',
   TRADE_TYPE           varchar(64) comment '账务交易类型',
   TRADE_DIRECTION      varchar(64) comment '账务交易方向',
   TRADE_AMOUNT         float comment '交易金额',
   PAYEE_NAME           varchar(100) comment '收款人名称',
   PAYEE_TYPE           varchar(64) comment '收款人类型',
   TRADE_STATUS         VARCHAR(64) COMMENT '账务状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '账务交易';
CREATE INDEX TRADE_ID_INX  ON T_TRADING_ACCOUNTS ( TRADE_ID ASC);
CREATE INDEX TRADE_TYPE_INX ON T_TRADING_ACCOUNTS ( TRADE_TYPE ASC);
CREATE INDEX TRADE_DIRECTION_INX  ON T_TRADING_ACCOUNTS ( TRADE_DIRECTION ASC);
CREATE INDEX TRADE_STATUS_INX  ON T_TRADING_ACCOUNTS ( TRADE_STATUS ASC);
CREATE INDEX DEL_FLAG_INX  ON T_TRADING_ACCOUNTS ( DEL_FLAG ASC);

create table T_PAYMENT_TRANS
(
   ID                   varchar(64) NOT NULL,
   TRADE_TYPE           varchar(64) comment '交易类型',
   PAYMENT_TYPE         varchar(64) comment '款项类型',
   TRANS_ID             varchar(64) comment '交易对象',
   TRADE_DIRECTION      varchar(64) comment '交易款项方向',
   START_DATE           date comment '交易款项开始时间',
   EXPIRED_DATE         date comment '交易款项到期时间',
   TRADE_AMOUNT         float comment '应该交易金额',
   TRANS_AMOUNT         float comment '实际交易金额',
   LAST_AMOUNT          float comment '剩余交易金额',
   TRANS_STATUS         VARCHAR(64) COMMENT '交易款项状态',
   TRANSFER_DEPOSIT_AMOUNT FLOAT COMMENT '已转定金金额',
   postpaid_fee_id		varchar(64) '后付费记录ID',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '款项交易';
alter table t_payment_trans add index idx_trade_type(trade_type);
alter table t_payment_trans add index idx_payment_type(payment_type);
alter table t_payment_trans add index idx_trans_id(trans_id);
alter table t_payment_trans add index idx_start_date(start_date);
alter table t_payment_trans add index idx_complex_pt(del_flag,payment_type,start_date);
alter table t_payment_trans add index idx_del_flag(del_flag);

create table T_PAYMENT_TRADE
(
   ID                   varchar(64) NOT NULL,
   TRANS_ID             varchar(64) comment '款项ID',
   TRADE_ID             varchar(64) comment '账务ID',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '款项账务关联表';
CREATE INDEX TRANS_ID_INX ON T_PAYMENT_TRADE (TRANS_ID ASC);
CREATE INDEX TRADE_ID_INX ON T_PAYMENT_TRADE (TRADE_ID ASC);
CREATE INDEX DEL_FLAG_INX ON T_PAYMENT_TRADE (DEL_FLAG ASC);

create table T_RECEIPT
(
   ID                   varchar(64) NOT NULL,
   TRADING_ACCOUNTS_ID  varchar(64) comment '账务交易',
   RECEIPT_NO           varchar(100) comment '收据号码',
   TRADE_MODE           varchar(64) comment '交易方式',
   PAYMENT_TYPE         varchar(64) comment '款项类型',
   RECEIPT_DATE         DATETIME comment '收据日期',
   trans_Begin_Date_Desc varchar(64) comment '款项开始日期',
   trans_End_Date_Desc 	varchar(64) comment '款项结束日期',
   RECEIPT_AMOUNT       float comment '收据金额',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '账务收据';

create table T_INVOICE
(
   ID                   varchar(64) NOT NULL,
   TRADING_ACCOUNTS_ID  varchar(64) comment '账务交易',
   INVOICE_TYPE         varchar(64) comment '开票类型',
   INVOICE_NO           varchar(64) comment '发票号码',
   INVOICE_TITLE        varchar(64) comment '发票抬头',
   INVOICE_DATE         DATE comment '开票日期',
   INVOICE_AMOUNT       float comment '发票金额',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '发票信息';

create table t_payment_order
(
	ID                   varchar(64) NOT NULL,
	order_id             varchar(64),
	order_amount         float,
	order_date           DATETIME,
	order_status         varchar(2),
	trade_id             varchar(64) comment '账务交易ID',
	trans_id             varchar(64) comment '支付交易单号',
	trans_date           DATETIME,
	house_id 			 varchar(64),
	CREATE_BY            VARCHAR(64) COMMENT '创建者',
  CREATE_DATE          DATETIME 	COMMENT '创建时间',
  UPDATE_BY            VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
  REMARKS              VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	primary key (ID)
) comment = '支付订单信息';