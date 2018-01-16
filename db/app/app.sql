/* Drop Tables */
drop table if exists T_APP_INTERFACE_LOG;
drop table if exists T_APP_CHECK_CODE;
drop table if exists T_APP_TOKEN;
drop table if exists T_APP_QUESTIONS;
drop table if exists T_MESSAGE;
drop table if exists t_house_ad;
drop table if exists T_REPAIR;
drop table if exists t_service_user_complain;

create table T_REPAIR
(
   ID                   varchar(64) NOT NULL,
   USER_ID    	 varchar(64) comment '报修人ID',
   USER_NAME    	 varchar(20) comment '报修人',
   USER_MOBILE           varchar(20) comment '报修电话',
   REPAIR_MOBILE	 VARCHAR(20) comment  '报修联系电话',
	EXPECT_REPAIR_TIME	VARCHAR(50) comment '期望维修时间',
   CONTRACT_ID	 VARCHAR(64) COMMENT  '合同号',
   ROOM_ID	 VARCHAR(64)  COMMENT  '房间号',
   DESCRIPTION	 VARCHAR(500) COMMENT  '报修描述',
   KEEPER	 VARCHAR(20) COMMENT  '管家',
   KEEPER_MOBILE	 VARCHAR(20) COMMENT  '管家电话',
   STATUS	 VARCHAR(10) COMMENT  '状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '报修表';

CREATE TABLE t_service_user_complain (
  ID varchar(64) NOT NULL,
  USER_ID varchar(64) COMMENT '投拆人',
  service_user varchar(64) COMMENT '被投拆人,服务管家',
  content varchar(500) COMMENT '投拆内容',
  CREATE_BY varchar(64) COMMENT '创建者',
  CREATE_DATE datetime COMMENT '创建时间',
  UPDATE_BY varchar(64) COMMENT '更新者',
  UPDATE_DATE timestamp COMMENT '更新时间',
  REMARKS varchar(255) COMMENT '备注信息',
  DEL_FLAG char(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (ID)
)  COMMENT='管家投拆记录';

create table t_house_ad
(
	ID                   varchar(64) NOT NULL,
	ad_name              varchar(64) comment '广告名称',
	ad_type              varchar(2) comment '广告类型 0:图片广告 1:房源链接式广告',
	PROPERTY_PROJECT_ID  varchar(64) comment '物业项目',
  BUILDING_ID          varchar(64) comment '楼宇',
  HOUSE_ID             varchar(64) comment '房屋',
  ROOM_ID              varchar(64) comment '房间',
	ad_value             varchar(64) comment '房源ID',
	ad_url               varchar(255) comment '广告图片地址',
	CREATE_BY            VARCHAR(64) COMMENT '创建者',
  CREATE_DATE          DATETIME 	COMMENT '创建时间',
  UPDATE_BY            VARCHAR(64) COMMENT '更新者',
  UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
  REMARKS              VARCHAR(255) COMMENT '备注信息',
  DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
	primary key (ID)
) comment = '广告';

create table T_MESSAGE
(
   ID                   varchar(64) NOT NULL,
   TITLE    	 varchar(200) comment '消息标题',
   CONTENT           varchar(500) comment '消息内容',
   TYPE	 VARCHAR(10) comment  '消息类型',
   SENDER	 VARCHAR(30) COMMENT  '发送人',
   RECEIVER	 VARCHAR(30)  COMMENT  '接收人',
   STATUS	 VARCHAR(10) COMMENT  '状态',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '消息表';

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
   ANSWER           	varchar(2000) comment '回答',
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