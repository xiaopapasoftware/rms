/* Drop Tables */
drop table if exists T_APP_INTERFACE_LOG;
drop table if exists T_APP_CHECK_CODE;
drop table if exists T_APP_USER;
drop table if exists T_APP_TOKEN;
drop table if exists　T_APP_QUESTIONS;


/* Create Tables */
create table T_APP_USER
(
   ID                   varchar(64) NOT NULL,
   PHONE    			varchar(20) comment '手机号码',
   PASSWORD           	varchar(64) comment '密码',
   ID_CARD_NO				VARCHAR(18) comment  '身份证号',
   NAME					VARCHAR(30) COMMENT  '姓名',
   SEX					VARCHAR(1)  COMMENT  '性别1-男 2-女',
   BIRTH				VARCHAR(10) COMMENT  '生日',
   AGE					INT			COMMENT  '年龄',
   PROFESSION			VARCHAR(100) COMMENT '职业',
   CORP					VARCHAR(200) COMMENT '公司',
   AVATAR				VARCHAR(64)  COMMENT '头像,附件ID',
   ID_CARD_PHOTO＿FRONT		VARCHAR(64)comment  '身份证正面,附件ID',
   ID_CARD_PHOTO＿BACK		VARCHAR(64)comment  '身份证反面,附件ID',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = 'APP用户';

create table T_APP_CHECK_CODE
(
   ID                   varchar(64) NOT NULL,
   PHONE		  		varchar(20) comment '手机号码',
   CODE          		varchar(64) comment '验证码',
   EXPRIE             	TIMESTAMP comment '过期时间',
   primary key (ID)
) comment = '验证码';


create table T_APP_TOKEN
(
   ID                   varchar(64) NOT NULL,
   PHONE		  		varchar(20) comment '手机号码',
   TOKEN          		varchar(200) comment 'TOKEN',
   EXPRIE             	TIMESTAMP comment '过期时间',
   primary key (ID)
) comment = 'TOKEN';

create table T_APP_INTERFACE_LOG
(
	 ID                   varchar(64) NOT NULL,
	 INTERFACE			  VARCHAR(200) comment '接口名',
	 REQUEST          	  varchar(1000) comment 'request',	
	 RESPONSE             varchar(1000) comment 'response',	
	 IS_SUCCESS  			CHAR(1) comment '是否调用成功',
	 INVOKE_TIME            TIMESTAMP COMMENT '创建者',
   	RETURN_TIME          TIMESTAMP 	COMMENT '创建时间',
   primary key (ID)
) comment = '接口调用日志';

create table T_APP_QUESTIONS
(
   ID                   varchar(64) NOT NULL,
   QUESTION    			varchar(2000) comment '问题',
   ANSWER           	varchar(2000) comment '回签',
   TYPE					VARCHAR(1) COMMENT  '类型',
   SORT					INT  COMMENT  '排序',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '常见问题';
