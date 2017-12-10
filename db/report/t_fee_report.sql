/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : rms

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-12-10 22:57:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_fee_report
-- ----------------------------
DROP TABLE IF EXISTS `t_fee_report`;
CREATE TABLE `t_fee_report` (
  `ID` varchar(64) COLLATE utf8_estonian_ci NOT NULL,
  `ROOM_ID` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '房间',
  `FEE_NO` varchar(100) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '业务号如电表号',
  `FEE_TYPE` varchar(16) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '费用类型',
  `REMAIN_FEE` float DEFAULT NULL COMMENT '剩余费用',
  `SMS_RECORD` varchar(16) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '短信发送记录',
  `FEE_TIME` datetime DEFAULT NULL COMMENT '费用统计截止时间',
  `CREATE_BY` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '创建者',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_BY` varchar(64) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '更新者',
  `UPDATE_DATE` datetime NOT NULL COMMENT '更新时间',
  `REMARKS` varchar(255) COLLATE utf8_estonian_ci DEFAULT NULL COMMENT '备注信息',
  `DEL_FLAG` char(1) COLLATE utf8_estonian_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_estonian_ci COMMENT='费用统计报表';
