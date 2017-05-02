ALTER TABLE `rms`.`t_property_project`
ADD COLUMN `AERA_ID` VARCHAR(64) NULL AFTER `T_MANAGEMENT_COMPANY_MAIN_ID`;

CREATE TABLE `sys_user_area` (
  `area_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '区域编号',
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '用户编号',
  PRIMARY KEY (`area_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='区域-菜用户';
