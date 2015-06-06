/* Drop Tables */
drop table if exists T_NEIGHBORHOOD_CONTACT;
drop table if exists T_COMPANY_CONTACT;
drop table if exists T_CUSTOMER;
drop table if exists T_OWNER;


/* Create Tables */
create table T_NEIGHBORHOOD_CONTACT 
(
   ID                   varchar(64) NOT NULL,
   T_NEIGHBORHOOD_MAIN_ID      varchar(64) comment '居委会',
   CONTACT_NAME         VARCHAR(100) comment '姓名',
   CELL_PHONE           VARCHAR(100) comment '手机号',
   DESK_PHONE           VARCHAR(100) comment '座机号',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          TIMESTAMP COMMENT '创建时间',
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
   CREATE_DATE          TIMESTAMP COMMENT '创建时间',
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
   CREATE_DATE          TIMESTAMP COMMENT '创建时间',
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
   SOCIAL_NUMBER				VARCHAR(100) COMMENT '身份证号',
   CELL_PHONE           VARCHAR(100) comment '手机号',
   DESK_PHONE           VARCHAR(100) comment '座机号',
   ADDRESS              VARCHAR(300) comment '详细居住地址',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          TIMESTAMP COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '业主信息';