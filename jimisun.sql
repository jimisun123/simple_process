/*
 Navicat Premium Data Transfer

 Source Server         : 得实Mysql
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 27.50.142.211:63306
 Source Schema         : jimisun

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 11/02/2022 15:33:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for process
-- ----------------------------
DROP TABLE IF EXISTS `process`;
CREATE TABLE `process` (
  `process_id` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `bus_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_template_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `current_node_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `current_node_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `current_node_execute_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `current_node_distribute_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_status` tinyint(4) DEFAULT NULL,
  `process_data` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`process_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of process
-- ----------------------------
BEGIN;
INSERT INTO `process` (`process_id`, `bus_key`, `process_template_key`, `current_node_key`, `current_node_name`, `current_node_execute_type`, `current_node_distribute_type`, `process_status`, `process_data`) VALUES ('4c5b3ea4149f4a7bbf009796906202a3', 'BS111111', 'template1', 'bus1', '业务节点1', 'ANY', 'USER', 1, NULL);
INSERT INTO `process` (`process_id`, `bus_key`, `process_template_key`, `current_node_key`, `current_node_name`, `current_node_execute_type`, `current_node_distribute_type`, `process_status`, `process_data`) VALUES ('8eff8ee2e7464b089d87297dd5bf0e15', 'BS433243445', 'template1', 'start', '开始节点', '', '', 1, NULL);
INSERT INTO `process` (`process_id`, `bus_key`, `process_template_key`, `current_node_key`, `current_node_name`, `current_node_execute_type`, `current_node_distribute_type`, `process_status`, `process_data`) VALUES ('b2cbbc4d84fc4fe18d6fba9af5536293', 'BS43324324', 'template1', 'bus1', '业务节点1', 'ANY', 'USER', 1, NULL);
INSERT INTO `process` (`process_id`, `bus_key`, `process_template_key`, `current_node_key`, `current_node_name`, `current_node_execute_type`, `current_node_distribute_type`, `process_status`, `process_data`) VALUES ('e1f7b54cfe194c6aaf14885aed78384a', 'BS43324324', 'template1', 'end', '结束节点', 'ANY', 'USER', 2, NULL);
COMMIT;

-- ----------------------------
-- Table structure for process_node
-- ----------------------------
DROP TABLE IF EXISTS `process_node`;
CREATE TABLE `process_node` (
  `process_node_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_template_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_node_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `next_process_node_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `node_execute_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `node_distribute_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `actors` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `ext` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_node_id` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `form_url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`process_node_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of process_node
-- ----------------------------
BEGIN;
INSERT INTO `process_node` (`process_node_key`, `process_template_key`, `process_node_name`, `next_process_node_key`, `node_execute_type`, `node_distribute_type`, `actors`, `ext`, `process_node_id`, `form_url`) VALUES ('start', 'template1', '开始节点', 'bus1', '', '', NULL, NULL, '1', '');
INSERT INTO `process_node` (`process_node_key`, `process_template_key`, `process_node_name`, `next_process_node_key`, `node_execute_type`, `node_distribute_type`, `actors`, `ext`, `process_node_id`, `form_url`) VALUES ('bus1', 'template1', '业务节点1', 'end', 'ANY', 'USER', 'zhangsan,lisi', NULL, '2', 'http://4234234/4324');
INSERT INTO `process_node` (`process_node_key`, `process_template_key`, `process_node_name`, `next_process_node_key`, `node_execute_type`, `node_distribute_type`, `actors`, `ext`, `process_node_id`, `form_url`) VALUES ('end', 'template1', '结束节点', '', NULL, NULL, NULL, NULL, '3', '');
COMMIT;

-- ----------------------------
-- Table structure for process_task
-- ----------------------------
DROP TABLE IF EXISTS `process_task`;
CREATE TABLE `process_task` (
  `process_task_id` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `process_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_node_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_node_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_node_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `ascription` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `operator` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `process_task_status` tinyint(4) DEFAULT NULL,
  `opinion` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `task_data` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `operator_time` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `form_url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `task_agree_address` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `task_reject_address` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`process_task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of process_task
-- ----------------------------
BEGIN;
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('128d981cdb6d4892bd3434a6aaa08999', 'b2cbbc4d84fc4fe18d6fba9af5536293', '2', 'bus1', '业务节点1', 'lisi', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('20fcde8bae7843aba2373c42b31a9803', 'e1f7b54cfe194c6aaf14885aed78384a', '2', 'bus1', '业务节点1', 'lisi', 'jimisun', 2, '我同意了', NULL, 'Fri Feb 11 13:31:52 CST 2022', NULL, NULL, NULL);
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('55894749986c417598e648b42a97fde5', '8eff8ee2e7464b089d87297dd5bf0e15', '2', 'bus1', '业务节点1', 'zhangsan', 'jimisun', 4, NULL, NULL, 'Fri Feb 11 14:53:04 CST 2022', NULL, NULL, NULL);
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('6965f52d4fde4ef1acb23b4d7b035f0d', 'e1f7b54cfe194c6aaf14885aed78384a', '2', 'bus1', '业务节点1', 'zhangsan', NULL, 3, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('7a7a6abcdc604c7d99f22f53bcc3ca90', '4c5b3ea4149f4a7bbf009796906202a3', '2', 'bus1', '业务节点1', 'zhangsan', NULL, 1, NULL, NULL, NULL, 'http://4234234/4324', '此处是执行任务的url地址', '此处是驳回任务的url地址');
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('7ad4aa706f674822a95f8302843bc2db', 'b2cbbc4d84fc4fe18d6fba9af5536293', '2', 'bus1', '业务节点1', 'zhangsan', NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('8e53dd8f177e4146b34a76e2693ecc16', '8eff8ee2e7464b089d87297dd5bf0e15', '2', 'bus1', '业务节点1', 'zhangsan', NULL, 3, NULL, NULL, 'Fri Feb 11 14:51:05 CST 2022', NULL, NULL, NULL);
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('9ff9e0530460448eb631687b7c284e1c', '4c5b3ea4149f4a7bbf009796906202a3', '2', 'bus1', '业务节点1', 'lisi', 'jimisun', 4, NULL, NULL, 'Fri Feb 11 15:32:46 CST 2022', NULL, '此处是执行任务的url地址', '此处是驳回任务的url地址');
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('a40d4ce6db824f269ecc0ab0950b13f5', '4c5b3ea4149f4a7bbf009796906202a3', '2', 'bus1', '业务节点1', 'zhangsan', 'jimisun', 4, NULL, NULL, 'Fri Feb 11 15:32:46 CST 2022', NULL, '此处是执行任务的url地址', '此处是驳回任务的url地址');
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('d6f73fe3448147d9a116c96ec3273a75', '8eff8ee2e7464b089d87297dd5bf0e15', '2', 'bus1', '业务节点1', 'lisi', 'jimisun', 1, NULL, NULL, 'Fri Feb 11 14:53:04 CST 2022', NULL, NULL, NULL);
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('d759798195e9404ba7e51ea2e78e90af', '4c5b3ea4149f4a7bbf009796906202a3', '2', 'bus1', '业务节点1', 'lisi', NULL, 1, NULL, NULL, NULL, 'http://4234234/4324', '此处是执行任务的url地址', '此处是驳回任务的url地址');
INSERT INTO `process_task` (`process_task_id`, `process_id`, `process_node_id`, `process_node_key`, `process_node_name`, `ascription`, `operator`, `process_task_status`, `opinion`, `task_data`, `operator_time`, `form_url`, `task_agree_address`, `task_reject_address`) VALUES ('ef5519e260d04c4ea013e835a3b2313e', '8eff8ee2e7464b089d87297dd5bf0e15', '2', 'bus1', '业务节点1', 'lisi', 'zhangsan', 2, '意见', NULL, 'Fri Feb 11 14:51:05 CST 2022', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for process_template
-- ----------------------------
DROP TABLE IF EXISTS `process_template`;
CREATE TABLE `process_template` (
  `process_template_key` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `process_status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`process_template_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of process_template
-- ----------------------------
BEGIN;
INSERT INTO `process_template` (`process_template_key`, `process_status`) VALUES ('template1', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
