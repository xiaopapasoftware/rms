/* Drop Tables */
drop table if exists T_NEIGHBORHOOD_CONTACT;


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