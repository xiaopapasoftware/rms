/* Drop Tables */
drop table if exists T_NEIGHBORHOOD_CONTACT;
drop table if exists T_COMPANY_CONTACT;
drop table if exists T_CUSTOMER;
drop table if exists T_OWNER;
drop table if exists T_TENANT;
drop table if exists T_REMITTANCER;
drop table if exists T_PARTNER;
drop table if exists T_COMPANY;
drop table if exists T_COMPANY_LINKMAN;


/* Create Tables */
create table T_NEIGHBORHOOD_CONTACT 
(
   ID                   varchar(64) NOT NULL,
   T_NEIGHBORHOOD_MAIN_ID      varchar(64) comment '居委会',
   CONTACT_NAME         VARCHAR(100) comment '姓名',
   CELL_PHONE           VARCHAR(100) comment '手机号',
   DESK_PHONE           VARCHAR(100) comment '座机号',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '居委会联系人';

create table T_COMPANY_CONTACT 
(
   ID                   varchar(64) NOT NULL,
   MANAGEMENT_COMPANY_ID      varchar(64) comment '物业公司',
   CONTACT_NAME         VARCHAR(100) comment '姓名',
   CELL_PHONE           VARCHAR(100) comment '手机号',
   DESK_PHONE           VARCHAR(100) comment '座机号',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '物业公司联系人';

create table T_CUSTOMER
(
   ID                   varchar(64) NOT NULL,
   USER_ID      varchar(64) comment '销售',
   CONTACT_NAME         VARCHAR(100) comment '姓名',
   GENDER								VARCHAR(2) COMMENT '性别',
   CELL_PHONE           VARCHAR(100) comment '手机号',
   IS_TENANT            VARCHAR(2) comment '是否转租客',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '客户信息';

create table T_OWNER
(
   ID                   varchar(64) NOT NULL,
   NAME                 VARCHAR(100) comment '姓名',
   SOCIAL_NUMBER	 	VARCHAR(100) COMMENT '身份证号',
   CELL_PHONE           VARCHAR(100) comment '手机号',
   DESK_PHONE           VARCHAR(100) comment '座机号',
   ADDRESS              VARCHAR(300) comment '详细居住地址',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '业主信息';

create table T_TENANT
(
   ID                   varchar(64) NOT NULL,
   DEPOSIT_AGREEMENT_ID VARCHAR(100) comment '定金协议',
   LEASE_CONTRACT_ID  	VARCHAR(100) COMMENT '承租合同',
   RENT_CONTRACT        VARCHAR(100) comment '出租合同',
   LEASE_CONTRACT_CHANGE_ID  VARCHAR(100) comment '承租合同变更协议',
   RENT_CONTRACT_CHANGE_ID   VARCHAR(300) comment '出租合同变更协议',
   TENANT_TYPE          VARCHAR(64) COMMENT '租客类型',
   COMPANY_ID           VARCHAR(64) COMMENT '企业',
   TENANT_NAME          VARCHAR(100) COMMENT '姓名',
   GENDER               VARCHAR(100) COMMENT '性别',
   ID_TYPE              VARCHAR(100) COMMENT '证件类型',
   ID_NO                VARCHAR(100) COMMENT '证件号码',
   BIRTHDAY             date COMMENT '出生日期',
   DEGREES              VARCHAR(64) COMMENT '学历',
   CELL_PHONE           VARCHAR(64) COMMENT '手机号码',
   EMAIL                VARCHAR(64) COMMENT '电子邮箱',
   HOUSE_REGISTER       VARCHAR(64) COMMENT '户籍所在地',
   POSITION             VARCHAR(64) COMMENT '职位',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '租客信息';

create table T_REMITTANCER
(
   ID                   varchar(64) NOT NULL,
   USER_NAME            VARCHAR(100) comment '开户人姓名',
   BANK_NAME				VARCHAR(100) COMMENT '开户行名称',
   BANK_ACCOUNT            VARCHAR(100) comment '开户行账号',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '汇款人信息';

create table T_PARTNER
(
   ID                   varchar(64) NOT NULL,
   PARTNER_TYPE         VARCHAR(100) comment '合作人类型',
   PARTNER_NAME         VARCHAR(100) comment '姓名',
   CELL_PHONE           VARCHAR(100) comment '手机号',
   DESK_PHONE           VARCHAR(100) comment '座机号',
   USER_NAME            VARCHAR(100) comment '开户人姓名',
   BANK_NAME				    VARCHAR(100) COMMENT '开户行名称',
   BANK_ACCOUNT         VARCHAR(100) comment '开户行账号',
   COMMISSION_PERCENT   float comment '佣金百分比',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '合作人信息';

create table T_COMPANY
(
   ID                   varchar(64) NOT NULL,
   COMPANY_NAME         VARCHAR(100) comment '企业名称',
   TELL_PHONE           VARCHAR(100) comment '企业电话',
   ID_TYPE              VARCHAR(100) comment '证件类型',
   ID_NO                VARCHAR(100) comment '证件号码',
   COMPANY_ADRESS       VARCHAR(100) comment '企业注册地址',
   BUSINESS_ADRESS	    VARCHAR(100) COMMENT '企业营业地址',
   BANK_NAME				    VARCHAR(100) COMMENT '开户行名称',
   BANK_ACCOUNT         VARCHAR(100) comment '开户行账号',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '企业信息';

create table T_COMPANY_LINKMAN
(
   ID                   varchar(64) NOT NULL,
   COMPANY_ID           VARCHAR(100) comment '企业',
   PERSON_NAME          VARCHAR(100) comment '姓名',
   CELL_PHONE           VARCHAR(100) comment '手机号码',
   TELL_PHONE           VARCHAR(100) comment '座机号码',
   EMAIL                VARCHAR(100) comment '邮箱',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '企业联系人';