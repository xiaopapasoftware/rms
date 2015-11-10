alter table t_house add intent_mode varchar(64) comment '意向租赁类型';

alter table t_house add is_feature varchar(64) comment '是否精选房源';
alter table t_room add is_feature varchar(64) comment '是否精选房源';

update t_house set is_feature='0' where is_feature is null;
update t_room set is_feature='0' where is_feature is null;