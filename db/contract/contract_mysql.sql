/* Drop Tables */
DROP TABLE IF EXISTS T_LEASE_CONTRACT;
DROP TABLE IF EXISTS T_DEPOSIT_AGREEMENT;
DROP TABLE IF EXISTS T_RENT_CONTRACT;
DROP TABLE IF EXISTS T_ACCOUNTING;
DROP TABLE IF EXISTS T_AGREEMENT_CHANGE;
DROP TABLE IF EXISTS T_ATTACHMENT;
DROP TABLE IF EXISTS T_LEASE_CONTRACT_DTL;
DROP TABLE IF EXISTS T_AUDIT_HIS;
DROP TABLE IF EXISTS T_CONTRACT_TENANT;
DROP TABLE IF EXISTS t_contract_book;
DROP TABLE IF EXISTS t_lease_contract_owner;

CREATE TABLE T_LEASE_CONTRACT
(
  ID                    VARCHAR(64)         NOT NULL,
  PROPERTY_PROJECT_ID   VARCHAR(64) COMMENT '物业项目',
  BUILDING_ID           VARCHAR(64) COMMENT '楼宇',
  HOUSE_ID              VARCHAR(64) COMMENT '房屋',
  REMITTANCER_ID        VARCHAR(64) COMMENT '汇款人',
  CONTRACT_CODE         VARCHAR(64) COMMENT '承租合同编号',
  CONTRACT_NAME         VARCHAR(100) COMMENT '承租合同名称',
  EFFECTIVE_DATE        DATE COMMENT '合同生效时间',
  FIRST_REMITTANCE_DATE DATE COMMENT '首次打款日期',
  REMITTANCE_DATE       VARCHAR(64) COMMENT '打款日期',
  EXPIRED_DATE          DATE COMMENT '合同过期时间',
  CONTRACT_DATE         DATE COMMENT '合同签订时间',
  DEPOSIT               FLOAT COMMENT '承租押金',
  CONTRACT_STATUS       VARCHAR(64) COMMENT '合同审核状态',
  month_space           VARCHAR(64) COMMENT '打款月份间隔',
  CREATE_BY             VARCHAR(64) COMMENT '创建者',
  CREATE_DATE           DATETIME COMMENT '创建时间',
  UPDATE_BY             VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE           TIMESTAMP COMMENT '更新时间',
  REMARKS               VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG              CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '承租合同';

CREATE TABLE T_LEASE_CONTRACT_DTL
(
  ID                VARCHAR(64)         NOT NULL,
  LEASE_CONTRACT_ID VARCHAR(64) COMMENT '承租合同',
  START_DATE        DATE COMMENT '起始时间',
  END_DATE          DATE COMMENT '结束时间',
  DEPOSIT           FLOAT COMMENT '月承租价',
  CREATE_BY         VARCHAR(64) COMMENT '创建者',
  CREATE_DATE       DATETIME COMMENT '创建时间',
  UPDATE_BY         VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE       TIMESTAMP COMMENT '更新时间',
  REMARKS           VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG          CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '承租合同明细';

CREATE TABLE T_DEPOSIT_AGREEMENT
(
  ID                    VARCHAR(64)         NOT NULL,
  PROPERTY_PROJECT_ID   VARCHAR(64) COMMENT '物业项目',
  BUILDING_ID           VARCHAR(64) COMMENT '楼宇',
  HOUSE_ID              VARCHAR(64) COMMENT '房屋',
  ROOM_ID               VARCHAR(64) COMMENT '房间',
  RENT_MODE             VARCHAR(64) COMMENT '出租方式',
  USER_ID               VARCHAR(64) COMMENT '销售',
  AGREEMENT_CODE        VARCHAR(64) COMMENT '定金协议编号',
  AGREEMENT_NAME        VARCHAR(100) COMMENT '定金协议名称',
  START_DATE            DATE COMMENT '协议开始时间',
  EXPIRED_DATE          DATE COMMENT '协议结束时间',
  SIGN_DATE             DATE COMMENT '协议签订时间',
  REN_MONTHS            INT COMMENT '首付月数',
  DEPOSIT_MONTHS        INT COMMENT '房租押金月数',
  AGREEMENT_DATE        DATE COMMENT '约定合同签约时间',
  DEPOSIT_AMOUNT        FLOAT COMMENT '定金金额',
  HOUSING_RENT          FLOAT COMMENT '房屋租金',
  AGREEMENT_STATUS      VARCHAR(64) COMMENT '定金协议审核状态',
  AGREEMENT_BUSI_STATUS VARCHAR(64) COMMENT '定金协议业务状态',
  data_source           VARCHAR(64) DEFAULT '1'
  COMMENT '数据来源 管理系统/APP',
  CREATE_BY             VARCHAR(64) COMMENT '创建者',
  CREATE_DATE           DATETIME COMMENT '创建时间',
  UPDATE_BY             VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE           TIMESTAMP COMMENT '更新时间',
  REMARKS               VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG              CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '定金协议';
UPDATE T_DEPOSIT_AGREEMENT
SET data_source = '1';

CREATE TABLE T_CONTRACT_TENANT
(
  ID                   VARCHAR(64)         NOT NULL,
  DEPOSIT_AGREEMENT_ID VARCHAR(64) COMMENT '承租的定金协议ID',
  CONTRACT_ID          VARCHAR(64) COMMENT '入住的出租合同ID',
  LEASE_CONTRACT_ID    VARCHAR(64) COMMENT '承租的出租合同ID',
  AGREEMENT_CHANGE_ID  VARCHAR(64) COMMENT '入住的变更协议ID',
  LEASAGREM_CHANGE_ID  VARCHAR(64) COMMENT '承租的变更协议ID',
  TENANT_ID            VARCHAR(64) COMMENT '租客ID',
  CREATE_BY            VARCHAR(64) COMMENT '创建者',
  CREATE_DATE          DATETIME COMMENT '创建时间',
  UPDATE_BY            VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
  REMARKS              VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '合同租客关联信息';
CREATE INDEX lease_contract_id_IDX
  ON T_CONTRACT_TENANT (LEASE_CONTRACT_ID ASC);
CREATE INDEX CONTRACT_ID_INX
  ON T_CONTRACT_TENANT (CONTRACT_ID ASC);
CREATE INDEX TENANT_ID_INX
  ON T_CONTRACT_TENANT (TENANT_ID ASC);
CREATE INDEX DEL_FLAG_INX
  ON T_CONTRACT_TENANT (DEL_FLAG ASC);

CREATE TABLE T_RENT_CONTRACT
(
  ID                      VARCHAR(64)         NOT NULL,
  AGREEMENT_ID            VARCHAR(64) COMMENT '原定金协议',
  CONTRACT_ID             VARCHAR(64) COMMENT '原出租合同',
  CONTRACT_CODE           VARCHAR(100) COMMENT '合同编号',
  CONTRACT_NAME           VARCHAR(100) COMMENT '合同名称',
  RENT_MODE               VARCHAR(64) COMMENT '出租方式',
  PROPERTY_PROJECT_ID     VARCHAR(64) COMMENT '物业项目',
  BUILDING_ID             VARCHAR(64) COMMENT '楼宇',
  HOUSE_ID                VARCHAR(64) COMMENT '房屋',
  ROOM_ID                 VARCHAR(64) COMMENT '房间',
  USER_ID                 VARCHAR(64) COMMENT '销售',
  PARTNER_ID              VARCHAR(64) COMMENT '合作人',
  CONTRACT_SOURCE         VARCHAR(64) COMMENT '合同来源',
  RENTAL                  FLOAT COMMENT '月租金',
  START_DATE              DATE COMMENT '合同生效时间',
  EXPIRED_DATE            DATE COMMENT '合同过期时间',
  SIGN_DATE               DATE COMMENT '合同签订时间',
  return_Date             DATE COMMENT '实际退租日期',
  SIGN_TYPE               VARCHAR(64) COMMENT '合同签订类型',
  HAS_TV                  VARCHAR(64) COMMENT '是否开通有线电视',
  TV_FEE                  FLOAT COMMENT '有线电视每月费用',
  HAS_NET                 VARCHAR(64) COMMENT '是否开通宽带',
  NET_FEE                 FLOAT COMMENT '每月宽带费用',
  WATER_FEE               FLOAT COMMENT '合租每月水费',
  GAS_FEE                 FLOAT COMMENT '每月燃气费用',
  SERVICE_FEE             FLOAT COMMENT '服务费比例',
  REN_MONTHS              INT COMMENT '首付月数',
  DEPOSIT_MONTHS          INT COMMENT '房租押金月数',
  DEPOSIT_AMOUNT          FLOAT COMMENT '房租押金金额',
  DEPOSIT_ELECTRIC_AMOUNT FLOAT COMMENT '水电押金金额',
  ELE_RECHARGE_AMOUNT     FLOAT COMMENT '智能电表充值金额',
  HAS_VISA                VARCHAR(64) COMMENT '是否需办理居住证及落户',
  METER_VALUE             FLOAT COMMENT '入住分电表系数',
  TOTAL_METER_VALUE       FLOAT COMMENT '入住总电表系数',
  PEAK_METER_VALUE        FLOAT COMMENT '入住峰电系数',
  VALLEY_METER_VALUE      FLOAT COMMENT '入住谷电系数',
  COAL_VALUE              FLOAT COMMENT '入住煤表系数',
  WATER_VALUE             FLOAT COMMENT '入住水表系数',
  REMIND_TIME             DATE COMMENT '续租提醒时间',
  CONTRACT_STATUS         VARCHAR(64) COMMENT '合同状态',
  CONTRACT_BUSI_STATUS    VARCHAR(64) COMMENT '合同业务状态',
  CHARGE_TYPE             VARCHAR(64) COMMENT '付费方式，分为预付费、后付费、综合付费',
  return_remark           VARCHAR(64) COMMENT '退租备注',
  renew_count             INT         DEFAULT 0
  COMMENT '续签次数',
  data_source             VARCHAR(64) DEFAULT '1'
  COMMENT '数据来源 管理系统/APP',
  CREATE_BY               VARCHAR(64) COMMENT '创建者',
  CREATE_DATE             DATETIME COMMENT '创建时间',
  UPDATE_BY               VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE             TIMESTAMP COMMENT '更新时间',
  REMARKS                 VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG                CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  HAS_FREE                CHAR(1)     DEFAULT '0'
  COMMENT '是否返租促销',
  FREE_MONTHS             INT(11)     DEFAULT 0
  COMMENT '返租促销减免房租月数',
  derate_Rent_Flag        CHAR(1)     DEFAULT '0'
  COMMENT '是否房租全免',
  lease_term_months       INT COMMENT '房源租赁周期（月）'
    PRIMARY KEY (ID)
)
  COMMENT = '出租合同';
CREATE INDEX USER_ID_IDX
  ON T_RENT_CONTRACT (USER_ID ASC);
CREATE INDEX CONTRACT_ID_INX
  ON T_RENT_CONTRACT (CONTRACT_ID ASC);
CREATE INDEX AGREEMENT_ID_INX
  ON T_RENT_CONTRACT (AGREEMENT_ID ASC);
CREATE INDEX RENT_MODE_INX
  ON T_RENT_CONTRACT (RENT_MODE ASC);
CREATE INDEX PROPERTY_PROJECT_ID_INX
  ON T_RENT_CONTRACT (PROPERTY_PROJECT_ID ASC);
CREATE INDEX BUILDING_ID_INX
  ON T_RENT_CONTRACT (BUILDING_ID ASC);
CREATE INDEX HOUSE_ID_INX
  ON T_RENT_CONTRACT (HOUSE_ID ASC);
CREATE INDEX ROOM_ID_INX
  ON T_RENT_CONTRACT (ROOM_ID ASC);
CREATE INDEX CONTRACT_STATUS_INX
  ON T_RENT_CONTRACT (CONTRACT_STATUS ASC);
CREATE INDEX CONTRACT_BUSI_STATUS_INX
  ON T_RENT_CONTRACT (CONTRACT_BUSI_STATUS ASC);
CREATE INDEX DEL_FLAG_INX
  ON T_RENT_CONTRACT (DEL_FLAG ASC);
CREATE INDEX CONTRACT_CODE_INX
  ON T_RENT_CONTRACT (CONTRACT_CODE ASC);
CREATE INDEX CONTRACT_NAME_INX
  ON T_RENT_CONTRACT (CONTRACT_CODE ASC);
CREATE INDEX START_DATE_INX
  ON T_RENT_CONTRACT (START_DATE ASC);
CREATE INDEX EXPIRED_DATE_INX
  ON T_RENT_CONTRACT (EXPIRED_DATE ASC);

CREATE TABLE T_ACCOUNTING
(
  ID               VARCHAR(64)         NOT NULL,
  RENT_CONTRACT_ID VARCHAR(64) COMMENT '出租合同',
  ACCOUNTING_TYPE  VARCHAR(64) COMMENT '核算类型',
  FEE_DIRECTION    VARCHAR(64) COMMENT '核算费用方向',
  FEE_TYPE         VARCHAR(64) COMMENT '核算费用类别',
  FEE_AMOUNT       FLOAT COMMENT '核算金额',
  USER_ID          VARCHAR(100) COMMENT '核算人',
  FEE_DATE         DATETIME COMMENT '核算时间',
  payment_trans_id VARCHAR(32) COMMENT '款项ID',
  CREATE_BY        VARCHAR(64) COMMENT '创建者',
  CREATE_DATE      DATETIME COMMENT '创建时间',
  UPDATE_BY        VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE      TIMESTAMP COMMENT '更新时间',
  REMARKS          VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG         CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '退租核算';

CREATE TABLE T_AGREEMENT_CHANGE
(
  ID                    VARCHAR(64)         NOT NULL,
  RENT_CONTRACT_ID      VARCHAR(64) COMMENT '出租合同',
  AGREEMENT_CHANGE_NAME VARCHAR(64) COMMENT '合同变更协议名称',
  START_DATE            DATE COMMENT '协议生效时间',
  REN_MONTHS            INT COMMENT '首付月数',
  DEPOSIT_MONTHS        INT COMMENT '房租押金月数',
  RENT_MODE             VARCHAR(64) COMMENT '出租方式',
  AGREEMENT_STATUS      VARCHAR(64) COMMENT '协议审核状态',
  USER_ID               VARCHAR(100) COMMENT '核算人',
  CREATE_BY             VARCHAR(64) COMMENT '创建者',
  CREATE_DATE           DATETIME COMMENT '创建时间',
  UPDATE_BY             VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE           TIMESTAMP COMMENT '更新时间',
  REMARKS               VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG              CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '协议变更';

CREATE TABLE T_ATTACHMENT
(
  ID                  VARCHAR(64)         NOT NULL,
  LEASE_CONTRACT_ID   VARCHAR(64) COMMENT '承租合同',
  RENT_CONTRACT_ID    VARCHAR(64) COMMENT '出租合同',
  DEPOSIT_AGREEM_ID   VARCHAR(64) COMMENT '定金协议',
  PROPERTY_PROJECT_ID VARCHAR(64) COMMENT '物业项目',
  BUILDING_ID         VARCHAR(64) COMMENT '楼宇',
  HOUSE_ID            VARCHAR(64) COMMENT '房屋',
  ROOM_ID             VARCHAR(64) COMMENT '房间',
  ATTACHMENT_NAME     VARCHAR(64) COMMENT '附件名称',
  ATTACHMENT_TYPE     VARCHAR(64) COMMENT '附件类型',
  ATTACHMENT_PATH     VARCHAR(4000) COMMENT '附件地址',
  trading_accounts_id VARCHAR(64) COMMENT '账务交易ID',
  BIZ_ID              VARCHAR(64) COMMENT '业务ID',
  CREATE_BY           VARCHAR(64) COMMENT '创建者',
  CREATE_DATE         DATETIME COMMENT '创建时间',
  UPDATE_BY           VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE         TIMESTAMP COMMENT '更新时间',
  REMARKS             VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG            CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '附件';
CREATE INDEX rent_contract_id_idx
  ON t_attachment (RENT_CONTRACT_ID ASC);
CREATE INDEX house_id_idx
  ON t_attachment (HOUSE_ID ASC);
CREATE INDEX room_id_idx
  ON t_attachment (ROOM_ID ASC);
CREATE INDEX lease_contract_id_idx
  ON t_attachment (LEASE_CONTRACT_ID ASC);
CREATE INDEX deposit_agm_id_idx
  ON t_attachment (DEPOSIT_AGREEM_ID ASC);
CREATE INDEX tra_acnt_idx
  ON t_attachment (trading_accounts_id ASC);

CREATE TABLE T_AUDIT
(
  ID          VARCHAR(64)         NOT NULL,
  OBJECT_TYPE VARCHAR(64) COMMENT '审核类型',
  OBJECT_ID   VARCHAR(64) COMMENT '审核对象ID',
  NEXT_ROLE   VARCHAR(100) COMMENT '下一级审核角色',
  CREATE_BY   VARCHAR(64) COMMENT '创建者',
  CREATE_DATE DATETIME COMMENT '创建时间',
  UPDATE_BY   VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE TIMESTAMP COMMENT '更新时间',
  REMARKS     VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG    CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '审核表';

CREATE TABLE T_AUDIT_HIS
(
  ID           VARCHAR(64)         NOT NULL,
  OBJECT_TYPE  VARCHAR(64) COMMENT '审核类型',
  OBJECT_ID    VARCHAR(64) COMMENT '审核对象ID',
  AUDIT_USER   VARCHAR(100) COMMENT '审核人',
  AUDIT_TIME   DATETIME COMMENT '审核时间',
  AUDIT_STATUS VARCHAR(100) COMMENT '审核状态',
  AUDIT_MSG    VARCHAR(100) COMMENT '审核意见',
  CREATE_BY    VARCHAR(64) COMMENT '创建者',
  CREATE_DATE  DATETIME COMMENT '创建时间',
  UPDATE_BY    VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE  TIMESTAMP COMMENT '更新时间',
  REMARKS      VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG     CHAR(1) DEFAULT '0' NOT NULL
  COMMENT '删除标记',
  PRIMARY KEY (ID)
)
  COMMENT = '审核历史表';

CREATE TABLE `t_contract_book` (
  `ID`           VARCHAR(64) COLLATE utf8_estonian_ci NOT NULL,
  `customer_id`  VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '用户信息id',
  `house_id`     VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL,
  `room_id`      VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL,
  `book_phone`   VARCHAR(64) COLLATE utf8_estonian_ci NOT NULL
  COMMENT '预约联系电话',
  `book_date`    DATETIME                                      DEFAULT NULL,
  `book_status`  VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL,
  `sales_id`     VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL,
  `CREATE_BY`    VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '创建者',
  `CREATE_DATE`  DATETIME                                      DEFAULT NULL
  COMMENT '创建时间',
  `UPDATE_BY`    VARCHAR(64) COLLATE utf8_estonian_ci          DEFAULT NULL
  COMMENT '更新者',
  `UPDATE_DATE`  TIMESTAMP                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  `REMARKS`      VARCHAR(255) COLLATE utf8_estonian_ci         DEFAULT NULL
  COMMENT '备注信息',
  `DEL_FLAG`     CHAR(1) COLLATE utf8_estonian_ci     NOT NULL DEFAULT '0'
  COMMENT '删除标记',
  `source`       CHAR(1) COLLATE utf8_estonian_ci              DEFAULT NULL
  COMMENT '预约来源 1支付宝租房平台',
  `housing_code` BIGINT(20) UNSIGNED                           DEFAULT NULL
  COMMENT '房源编号',
  `housing_type` CHAR(1) COLLATE utf8_estonian_ci              DEFAULT NULL
  COMMENT '房源类型 房间-1 房屋-0',
  PRIMARY KEY (`ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_estonian_ci
  COMMENT ='预约看房信息'