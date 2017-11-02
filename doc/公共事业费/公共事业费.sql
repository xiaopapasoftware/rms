/**
* 1.房屋添加是否已租状态，可修改，便于计算公摊
* 2.整组账单模式计算要去上次抄表数计算得出
**/

/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/9/15 11:22:03                           */
/*==============================================================*/


drop table if exists fee_config;

drop table if exists fee_ele_charged_flow;

drop table if exists fee_ele_read_flow;

drop table if exists fee_electricity_bill;

drop table if exists fee_gas_bill;

drop table if exists fee_gas_charged_flow;

drop table if exists fee_gas_read_flow;

drop table if exists fee_order;

drop table if exists fee_order_account;

drop table if exists fee_other_bill;

drop table if exists fee_other_charged_flow;

drop table if exists fee_water_bill;

drop table if exists fee_water_charged_flow;

drop table if exists fee_water_read_flow;

drop table if exists process_flow;

/*==============================================================*/
/* Table: fee_config                                            */
/*==============================================================*/
create table fee_config
(
   id                   varchar(64) not null comment '内码',
   fee_type             int not null comment '费用类型 0：电费单价 1:电费谷单价 2:电费峰单价 3:宽带单价 4:有线电视费单价 5:水费单价 6：燃气费单价 ',
   config_type          int not null comment '配置范围 0：默认 1:公司 2:省份 3:地市 4:区县 5: 服务中心 6: 运营区域 7: 小区 8:楼宇 9：房号 10:房间',
   charge_method        int not null comment '收取方式 0:固定模式 1:账单模式',
   business_id          varchar(120) not null comment '相应范围ID',
   show_name            varchar(200) comment '范围名称',
   config_value         varchar(64) not null default '0' comment '配置的值',
   config_status        int not null default 0 comment '配置状态 0:启用 1停用',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_config comment '费用配置项';

/*==============================================================*/
/* Table: fee_ele_charged_flow                                  */
/*==============================================================*/
create table fee_ele_charged_flow
(
   id                   varchar(64) not null comment '内码',
   business_id          varchar(120) comment '关联业务ID',
   from_source          int not null default '0' comment '来源 0: 抄表1: 账单2:固定模式',
   order_no             varchar(120) comment '订单号',
   property_id          varchar(64) comment '物业ID',
   house_id             varchar(64) not null comment '房屋ID',
   room_id              varchar(64) comment '房号',
   rent_type            int comment '出租类型 0：整租 1：合租',
   house_ele_num        varchar(60) comment '户号',
   ele_calculate_date   datetime not null comment '计算时间',
   ele_amount           numeric(19,2) not null default 0 comment '收取金额',
   ele_peak_amount      numeric(19,2) not null default 0 comment '峰金额',
   ele_valley_amount    numeric(19,2) not null default 0 comment '谷金额',
   generate_order       int not null default 0 comment '是否已生成订单 0:是 -1:否 ',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_ele_charged_flow comment '电费收取流水';

/*==============================================================*/
/* Table: fee_ele_read_flow                                     */
/*==============================================================*/
create table fee_ele_read_flow
(
   id                   varchar(64) not null comment '内码',
   business_id          varchar(120) comment '关联业务ID',
   from_source          int not null default 0 comment '来源 0: 抄表1: 账单',
   property_id          varchar(120) comment '物业ID',
   house_id             varchar(120) not null comment '房屋ID',
   room_id              varchar(120) comment '房号',
   house_ele_num        varchar(60) comment '户号',
   ele_read_date        date not null comment '抄表日期',
   ele_degree           float default 0 comment '电表额度',
   ele_peak_degree      float default 0 comment '峰值数',
   ele_valley_degree    float default 0 comment '谷值数',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_ele_read_flow comment '抄电表流水';

/*==============================================================*/
/* Table: fee_electricity_bill                                  */
/*==============================================================*/
create table fee_electricity_bill
(
   id                   varchar(64) not null comment '内码',
   batch_no             varchar(120) comment '审核批次号',
   property_id          varchar(120) comment '物业ID',
   house_id             varchar(120) not null comment '房屋ID',
   house_ele_num        varchar(60) not null comment '户号',
   ele_bill_date        date not null comment '账单日期',
   ele_bill_amount      numeric(19,2) not null default 0 comment '账单金额',
   ele_peak_degree      float not null default 0 comment '峰值数',
   ele_valley_degree    float not null default 0 comment '谷值数',
   bill_status          int not null default 0 comment '状态 0:待提交1:待审核 2:审核通过 3:审核驳回',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_electricity_bill comment '电费账单表';

/*==============================================================*/
/* Table: fee_gas_bill                                          */
/*==============================================================*/
create table fee_gas_bill
(
   id                   varchar(64) not null comment '内码',
   batch_no             varchar(120) comment '审核批次号',
   property_id          varchar(120) comment '物业ID',
   house_id             varchar(120) not null comment '房屋ID',
   house_gas_num      varchar(60) not null comment '户号',
   gas_bill_date        date not null comment '账单日期',
   gas_bill_amount      numeric(19,2) not null default 0 comment '账单金额',
   gas_degree           float not null default 0 comment '仪表额度',
   bill_status          int not null default 0 comment '状态 0:待提交1:待审核 2:审核通过 3:审核驳回',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_gas_bill comment '燃气账单表';

/*==============================================================*/
/* Table: fee_gas_charged_flow                                  */
/*==============================================================*/
create table fee_gas_charged_flow
(
   id                   varchar(64) not null comment '内码',
   business_id          varchar(120) comment '关联业务ID',
   from_source          int not null default 0 comment '来源 0: 抄表1: 账单2:固定模式',
   order_no             varchar(120) comment '订单号',
   property_id          varchar(64) comment '物业ID',
   house_id             varchar(64) not null comment '房屋ID',
   room_id              varchar(64) comment '房号',
   rent_type            int comment '出租类型 0：整租 1：合租',
   house_gas_num        varchar(60) comment '户号',
   gas_calculate_date   datetime not null comment '计算时间',
   gas_amount           numeric(19,2) not null default 0 comment '收取金额',
   generate_order       int not null default 0 comment '是否已生成订单 0:是 -1:否 ',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_gas_charged_flow comment ' 煤气收取流水';

/*==============================================================*/
/* Table: fee_gas_read_flow                                     */
/*==============================================================*/
create table fee_gas_read_flow
(
   id                   varchar(64) not null comment '内码',
   business_id          varchar(120) comment '关联业务ID',
   from_source          int not null default 0 comment '来源 0: 抄表1: 账单',
   property_id          varchar(120) comment '物业ID',
   house_id             varchar(120) not null comment '房屋ID',
   house_gas_num        varchar(60) comment '户号',
   gas_read_date        datetime not null comment '抄表日期',
   gas_degree           float not null default 0 comment '仪表额度',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_gas_read_flow comment '抄燃气表流水';

/*==============================================================*/
/* Table: fee_order                                             */
/*==============================================================*/
create table fee_order
(
   order_no             varchar(120) not null comment '订单号',
   batch_no             varchar(120) comment '审核编号',
   property_id          varchar(64) comment '物业ID',
   house_id             varchar(64) not null comment '房屋ID',
   room_id              varchar(64) comment '房号',
   order_type           int not null default 0 comment '费用类型 0：电费 1：水费 2 燃气费
            3：宽带 4：电视 5:房租
            6:房租押金 7:定金 8:违约金
            ',
   order_date           datetime not null comment '订单时间',
   amount               numeric(19,2) not null default 0 comment '账单金额',
   order_status         int not null default 0 comment '状态 0:待审核1:待缴费2：已缴3：驳回 ',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (order_no)
);

alter table fee_order comment '订单';

/*==============================================================*/
/* Table: fee_order_account                                     */
/*==============================================================*/
create table fee_order_account
(
   order_no             varchar(120) not null comment '订单号',
   property_id          varchar(64) comment '物业ID',
   house_id             varchar(64) not null comment '房屋ID',
   room_id              varchar(64) comment '房号',
   order_type           int not null default 0 comment '费用类型 0：电费 1：水费 2 燃气费
            3：宽带 4：电视 5:房租
            6:房租押金 7:定金 8:违约金
            ',
   pay_date             datetime not null comment '支付时间',
   amount               numeric(19,2) not null default 0 comment '账单金额',
   order_status         int not null default 0 comment '状态 0:待审核1:待缴费2：已缴3：驳回 ',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记'
);

alter table fee_order_account comment '订单台账';

/*==============================================================*/
/* Table: fee_other_bill                                        */
/*==============================================================*/
create table fee_other_bill
(
   id                   varchar(64) not null comment '内码',
   batch_no             varchar(120) comment '审核批次号',
   property_id          varchar(120) comment '物业ID',
   house_id             varchar(120) comment '房屋ID',
   bill_date            datetime not null comment '账单日期',
   bill_unit            int not null default 0 comment '账单单位 0：月1：半年2:一年',
   bill_amount          numeric(19,2) not null default 0 comment '账单金额',
   Bill_type            float not null default 0 comment '仪表额度',
   bill_status          int not null default 0 comment '状态 0:待提交1:待审核 2:审核通过 3:审核驳回',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_other_bill comment '宽带、电视费、其他账单表';

/*==============================================================*/
/* Table: fee_other_charged_flow                                */
/*==============================================================*/
create table fee_other_charged_flow
(
   id                   varchar(64) not null comment '内码',
   order_no             varchar(120) comment '订单号',
   property_id          varchar(64) comment '物业ID',
   house_id             varchar(64) not null comment '房屋ID',
   room_id              varchar(64) comment '房号',
   calculate_date       datetime not null comment '计算时间',
   amount               numeric(19,2) not null default 0 comment '收取金额',
   type                 int not null default 0 comment '账单类型 0:宽带 1:电视 2:其它',
   generate_order       int not null default 0 comment '是否已生成订单 0:是 -1:否 ',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_other_charged_flow comment '宽带、电视费、其他账单收取流水';

/*==============================================================*/
/* Table: fee_water_bill                                        */
/*==============================================================*/
create table fee_water_bill
(
   id                   varchar(64) not null comment '内码',
   batch_no             varchar(120) comment '审核批次号',
   property_id          varchar(120) comment '物业ID',
   house_id             varchar(120) not null comment '房屋ID',
   house_water_num      varchar(60) not null comment '户号',
   water_bill_date      date not null comment '账单日期',
   water_bill_amount    numeric(19,2) not null default 0 comment '账单金额',
   water_degree         float not null default 0 comment '水表读数',
   bill_status          int not null default 0 comment '状态 0:待提交1:待审核 2:审核通过 3:审核驳回',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_water_bill comment '水费账单表';

/*==============================================================*/
/* Table: fee_water_charged_flow                                */
/*==============================================================*/
create table fee_water_charged_flow
(
   id                   varchar(64) not null comment '内码',
   business_id          varchar(120) comment '关联业务ID',
   from_source          int not null default 0 comment '来源 0: 抄表1: 账单2:固定模式',
   order_no             varchar(120) comment '订单号',
   property_id          varchar(64) comment '物业ID',
   house_id             varchar(64) not null comment '房屋ID',
   room_id              varchar(64) comment '房号',
   rent_type            int comment '出租类型 0：整租 1：合租',
   house_gas_num        varchar(60) comment '户号',
   water_calculate_date date not null comment '计算时间',
   water_amount         numeric(19,2) not null default 0 comment '收取金额',
   generate_order       bigint not null default 0 comment '是否已生成订单 0:是 -1:否 ',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_water_charged_flow comment '水收取流水';

/*==============================================================*/
/* Table: fee_water_read_flow                                   */
/*==============================================================*/
create table fee_water_read_flow
(
   id                   varchar(64) not null comment '内码',
   business_id          varchar(120) comment '关联业务ID',
   from_source          int not null default 0 comment '来源 0: 抄表1: 账单',
   property_id          varchar(120) comment '物业ID',
   house_id             varchar(120) not null comment '房屋ID',
   house_water_num      varchar(60) comment '户号',
   water_read_date      datetime not null comment '抄表日期',
   water_degree         float not null default 0 comment '仪表额度',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (id)
);

alter table fee_water_read_flow comment '抄水表流水';

/*==============================================================*/
/* Table: process_flow                                          */
/*==============================================================*/
create table process_flow
(
   business_id          varchar(120) not null comment '业务号',
   sort_no              int not null comment '序号',
   handler_date         datetime not null comment '处理时间',
   handler_result       int not null comment '处理结果 0:提交 1:通过 2:驳回',
   description          varchar(200) comment '处理说明',
   create_by            varchar(64) comment '创建者',
   create_date          datetime comment '创建时间',
   update_by            varchar(64) comment '更新者',
   update_date          timestamp comment '更新时间',
   remarks              varchar(255) comment '备注信息',
   del_flag             char(1) default '0' not null comment '删除标记',
   primary key (business_id, sort_no)
);

alter table process_flow comment '审核记录';

