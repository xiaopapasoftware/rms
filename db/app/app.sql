/* Drop Tables */
drop table if exists T_APP_INTERFACE_LOG;
drop table if exists T_APP_CHECK_CODE;
drop table if exists T_APP_USER;
drop table if exists T_APP_TOKEN;


/* Create Tables */
create table T_APP_USER
(
   ID                   varchar(64) NOT NULL,
   PHONE    			varchar(20) comment '手机号码',
   PASSWORD           	varchar(20) comment '密码',
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
