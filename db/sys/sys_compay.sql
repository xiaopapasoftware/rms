/* Drop Tables */
drop table if exists t_work_record;

/* Create Tables */
create table t_work_record
(
   ID                   varchar(64) NOT NULL,
   RECORD_TYPE          varchar(64) comment '记录类型',
   RECORD_TITLE         varchar(255) comment '记录标题',
   RECORD_CONTENT       text(32767) comment '记录内容',
   RECORD_STATUS        varchar(64) comment '状态',
   REVIEW_BY            varchar(64) comment '批阅人',
   REVIEW_DATE          DATETIME 	COMMENT '批阅时间',
   REVIEW_REMARKS       varchar(255) comment '批阅备注',
   CREATE_BY            VARCHAR(64) COMMENT '创建者',
   CREATE_DATE          DATETIME 	COMMENT '创建时间',
   UPDATE_BY            VARCHAR(64) COMMENT '更新者',
   UPDATE_DATE          TIMESTAMP COMMENT '更新时间',
   REMARKS              VARCHAR(255) COMMENT '备注信息',
   DEL_FLAG             CHAR(1) DEFAULT '0' NOT NULL COMMENT '删除标记',
   primary key (ID)
) comment = '工作记录';