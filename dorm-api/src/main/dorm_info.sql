/*
 Navicat Premium Data Transfer

 Source Server         : 本地MySQL8
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : dorm_info

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 16/07/2024 08:48:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `employee_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职工号',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  `role` int NULL DEFAULT NULL COMMENT '角色：1为系统管理员，2为宿舍管理员，3维修师',
  `building_id` int NULL DEFAULT NULL COMMENT '所在楼宇',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'S190231', '李雪梅3', '13812093312', '女', '123456', 1, 2, 1);
INSERT INTO `admin` VALUES (2, 'S181731', '张仁聪', '13199028761', '男', '123456', 1, 2, 2);
INSERT INTO `admin` VALUES (4, 'S161232', '张荷', '13488974763', '女', '1234', -1, 2, 3);
INSERT INTO `admin` VALUES (5, NULL, '姚燕', '13760613080', '女', '123456', 1, 1, NULL);
INSERT INTO `admin` VALUES (6, '190211', '刘意', '13899091234', '女', '123456', 1, 2, 3);
INSERT INTO `admin` VALUES (7, '189021', '李大国', '18798822109', '', '123456', 1, 3, NULL);
INSERT INTO `admin` VALUES (8, '201127', '王长贵', '13312098878', '男', '123456', 1, 3, NULL);

-- ----------------------------
-- Table structure for admin_modify
-- ----------------------------
DROP TABLE IF EXISTS `admin_modify`;
CREATE TABLE `admin_modify`  (
  `admin_id` int NULL DEFAULT NULL COMMENT '宿舍管理员ID',
  `modify_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `employee_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职工号',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  `role` int NULL DEFAULT NULL COMMENT '角色：1为系统管理员，2为宿舍管理员',
  `building_id` int NULL DEFAULT NULL COMMENT '所在楼宇',
  `app_status` int NULL DEFAULT NULL COMMENT '审核状态：0-待审核，1-同意，2-拒绝',
  PRIMARY KEY (`modify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_modify
-- ----------------------------
INSERT INTO `admin_modify` VALUES (1, 7, 'S190231', '李雪梅3', '13812093312', '女', '123456', 1, 2, 1, 1);
INSERT INTO `admin_modify` VALUES (7, 8, '189021', '李大国5', '18798822109', '', '123456', NULL, 3, NULL, 0);

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building`  (
  `building_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `building_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '楼宇名称',
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '位置',
  `floor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '总楼层',
  `romms_count` int NULL DEFAULT NULL COMMENT '总宿舍数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`building_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of building
-- ----------------------------
INSERT INTO `building` VALUES (1, '南区5栋', '南生活区', '2', 12, '旧楼', 1);
INSERT INTO `building` VALUES (2, 'B栋', '南生活区', '2', 32, '研究生宿舍', 1);
INSERT INTO `building` VALUES (3, '学生公寓主楼', '北生活区', '18', 480, '本科生宿舍', 1);
INSERT INTO `building` VALUES (4, 'A栋', '南生活区', '5', 320, '本科生宿舍', 1);
INSERT INTO `building` VALUES (5, '北18栋', '北生活区', '3', 26, NULL, -1);

-- ----------------------------
-- Table structure for dormitory
-- ----------------------------
DROP TABLE IF EXISTS `dormitory`;
CREATE TABLE `dormitory`  (
  `dormitory_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `building_id` int NULL DEFAULT NULL COMMENT '楼宇ID',
  `floor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '楼层',
  `room_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '宿舍号',
  `max_capacity` int NULL DEFAULT NULL COMMENT '最大可住人数',
  `occupied` int NULL DEFAULT NULL COMMENT '已入住人数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`dormitory_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dormitory
-- ----------------------------
INSERT INTO `dormitory` VALUES (1, 1, '2', '201', 6, 1, '上床下桌，1卫1浴', 1);
INSERT INTO `dormitory` VALUES (2, 1, '2', '202', 6, 2, '上床下桌，1卫1浴', 1);
INSERT INTO `dormitory` VALUES (3, 2, '3', '301', 4, 2, '研究生宿舍', 1);
INSERT INTO `dormitory` VALUES (4, 2, '3', '302', 6, 0, '上床下桌，有阳台', 1);
INSERT INTO `dormitory` VALUES (5, 3, '4', '402', 6, 0, '卫生间损坏，本学期不安排入住', -1);
INSERT INTO `dormitory` VALUES (6, 3, '2', '201', 4, 1, '', 1);
INSERT INTO `dormitory` VALUES (7, 3, '2', '202', 4, 1, '', 1);
INSERT INTO `dormitory` VALUES (8, 3, '2', '203', 4, 1, '', 1);
INSERT INTO `dormitory` VALUES (9, 3, '2', '204', 4, 0, '', 1);
INSERT INTO `dormitory` VALUES (10, 3, '2', '205', 4, 0, '', 1);
INSERT INTO `dormitory` VALUES (11, 3, '3', '301', 4, 4, '', 1);
INSERT INTO `dormitory` VALUES (12, 4, '1', '101', 8, 1, '', 1);
INSERT INTO `dormitory` VALUES (13, 3, '3', '302', 4, 0, '', 1);
INSERT INTO `dormitory` VALUES (14, 3, '3', '303', 4, 2, '', 1);
INSERT INTO `dormitory` VALUES (15, 3, '3', '304', 4, 4, '', 1);
INSERT INTO `dormitory` VALUES (16, 3, '3', '305', 4, 1, '', 1);
INSERT INTO `dormitory` VALUES (17, 3, '3', '306', 4, 0, '', 1);
INSERT INTO `dormitory` VALUES (18, 3, '3', '307', 4, 0, '', 1);
INSERT INTO `dormitory` VALUES (19, 3, '3', '308', 4, 0, '', 1);
INSERT INTO `dormitory` VALUES (20, 3, '8', '801', 4, 3, '', 1);
INSERT INTO `dormitory` VALUES (21, 3, '8', '802', 4, 4, '', 1);
INSERT INTO `dormitory` VALUES (22, 3, '8', '803', 4, 1, '', 1);
INSERT INTO `dormitory` VALUES (23, 3, '8', '804', 4, 1, '', 1);
INSERT INTO `dormitory` VALUES (24, 3, '8', '805', 4, 1, '', 1);

-- ----------------------------
-- Table structure for fee
-- ----------------------------
DROP TABLE IF EXISTS `fee`;
CREATE TABLE `fee`  (
  `fee_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `fee_type` int NULL DEFAULT NULL COMMENT '费用类型，1为电费，2为水费',
  `bill_year` int NULL DEFAULT NULL COMMENT '账单所属年份',
  `bill_month` int NULL DEFAULT NULL COMMENT '账单所属月份',
  `usages` double NULL DEFAULT NULL COMMENT '水电用量，电费为度，水费为吨',
  `unit_price` double NULL DEFAULT NULL COMMENT '单价',
  `amount` double NULL DEFAULT NULL COMMENT '总费用',
  `dorm_id` int NULL DEFAULT NULL COMMENT '宿舍ID',
  `building_id` int NULL DEFAULT NULL COMMENT '楼宇ID',
  `status` int NULL DEFAULT NULL COMMENT '状态：0待缴费，1已缴费',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`fee_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fee
-- ----------------------------
INSERT INTO `fee` VALUES (1, 1, 2024, 1, 185, 0.72, 133.2, 1, 1, 0, NULL);
INSERT INTO `fee` VALUES (2, 2, 2024, 1, 18.6, 7.5, 139.5, 1, 1, 0, NULL);
INSERT INTO `fee` VALUES (3, 1, 2024, 2, 288, 0.75, 216, 6, 3, 0, NULL);
INSERT INTO `fee` VALUES (4, 2, 2024, 2, 12.7, 7.5, 95.25, 6, 3, 0, NULL);
INSERT INTO `fee` VALUES (5, 1, 2024, 2, 286, 0.75, 214.5, 7, 3, 1, '2024-03-14 22:35:15');
INSERT INTO `fee` VALUES (6, 2, 2024, 2, 12.5, 7.5, 93.75, 7, 3, 1, '2024-03-16 17:21:44');
INSERT INTO `fee` VALUES (7, 1, 2024, 2, 103, 0.75, 77.25, 11, 3, 1, '2024-03-14 22:35:12');
INSERT INTO `fee` VALUES (8, 1, 2024, 1, 122, 0.75, 91.5, 8, 3, 0, NULL);
INSERT INTO `fee` VALUES (9, 1, 2024, 1, 98, 0.75, 73.5, 3, 2, 0, NULL);
INSERT INTO `fee` VALUES (10, 2, 2024, 1, 3.1, 7.5, 23.25, 1, 1, 0, NULL);
INSERT INTO `fee` VALUES (11, 1, 2024, 1, 28, 0.75, 21, 12, 4, 0, NULL);
INSERT INTO `fee` VALUES (12, 2, 2024, 1, 1.8, 7.5, 13.5, 12, 4, 0, NULL);
INSERT INTO `fee` VALUES (13, 1, 2024, 1, 178, 0.72, 128.16, 7, 3, 1, '2024-03-16 22:41:07');
INSERT INTO `fee` VALUES (14, 2, 2024, 1, 4.9, 7.5, 36.75, 7, 3, 1, '2024-03-18 09:18:34');
INSERT INTO `fee` VALUES (15, 1, 2023, 12, 289, 0.72, 208.08, 7, 3, 1, '2024-03-16 22:41:57');
INSERT INTO `fee` VALUES (16, 1, 2024, 1, 201, 0.75, 150.75, 13, 3, 0, NULL);
INSERT INTO `fee` VALUES (17, 2, 2024, 1, 12.5, 7.5, 93.75, 13, 3, 0, NULL);
INSERT INTO `fee` VALUES (18, 1, 2023, 11, 288, 0.75, 216, 14, 3, 0, NULL);
INSERT INTO `fee` VALUES (19, 2, 2023, 11, 12.5, 7.5, 93.75, 14, 3, 1, '2024-03-18 15:09:07');
INSERT INTO `fee` VALUES (20, 1, 2023, 12, 195, 0.75, 146.25, 14, 3, 1, '2024-04-06 16:52:09');
INSERT INTO `fee` VALUES (21, 2, 2023, 12, 10.6, 7.5, 79.5, 14, 3, 1, '2024-04-06 16:58:05');
INSERT INTO `fee` VALUES (22, 1, 2024, 1, 187, 0.75, 140.25, 14, 3, 0, NULL);
INSERT INTO `fee` VALUES (23, 2, 2024, 1, 9.8, 7.5, 73.5, 14, 3, 0, NULL);
INSERT INTO `fee` VALUES (24, 1, 2024, 2, 102, 0.75, 76.5, 14, 3, 1, '2024-04-06 17:06:50');
INSERT INTO `fee` VALUES (25, 2, 2024, 2, 5.8, 7.5, 43.5, 14, 3, 1, '2024-03-18 15:17:17');
INSERT INTO `fee` VALUES (26, 1, 2024, 3, 275, 0.75, 206.25, 14, 3, 0, NULL);

-- ----------------------------
-- Table structure for fix
-- ----------------------------
DROP TABLE IF EXISTS `fix`;
CREATE TABLE `fix`  (
  `fix_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `student_id` int NULL DEFAULT NULL COMMENT '报修学生ID',
  `dorm_id` int NULL DEFAULT NULL COMMENT '宿舍ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '报修时间',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '详情',
  `fix_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维修结果',
  `fixed_time` datetime NULL DEFAULT NULL COMMENT '维修时间',
  `confirm_pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维修图片',
  `pics` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `building_id` int NULL DEFAULT NULL COMMENT '楼宇ID',
  `worker_id` int NULL DEFAULT NULL COMMENT '维修人员ID',
  PRIMARY KEY (`fix_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fix
-- ----------------------------
INSERT INTO `fix` VALUES (1, 1, 2, '2024-03-08 22:50:31', ' 厕所灯坏了', '厕所灯有时不亮，有时会一闪一闪的', '更换灯泡与开关后，故障解决。', '2024-03-10 10:55:00', NULL, 'http://118.25.89.125:18080/images/20230410152503e4uk2.jpg', '1', 1, 7);
INSERT INTO `fix` VALUES (3, 2, 2, '2024-03-10 15:21:27', '水龙头漏水', '阳台洗手盆的水龙头发生漏水，请尽快安排师傅维修。', '已更换新的水龙头', '2024-03-15 09:02:23', 'http://118.25.89.125:18080/images/20240315/090222c4xta.jpg', 'http://118.25.89.125:18080/images/20230410152121skdb9.jpg', '1', 1, 7);
INSERT INTO `fix` VALUES (4, 8, 7, '2024-03-16 15:20:00', '厕所堵塞', '厕所无法下水', NULL, NULL, NULL, 'http://118.25.89.125:18080/images/20240316/151959b6zqm.jpg', '2', 3, 8);
INSERT INTO `fix` VALUES (5, 8, 7, '2024-03-16 15:21:47', '晾衣杆断了', '阳台晾衣格杆断裂，无法晾晒衣服', NULL, NULL, NULL, '', '0', 3, 7);
INSERT INTO `fix` VALUES (7, 0, 22, '2024-04-03 15:58:41', 'RRRRRR', 'WWWWWWWWWWWW', NULL, NULL, NULL, 'http://118.25.89.125:18080/images/20240403/155839m7wva.jpg', '0', 3, 8);
INSERT INTO `fix` VALUES (8, 19, 15, '2024-04-03 23:30:14', '更换阳台灯', '阳台的灯不亮了', NULL, NULL, NULL, '', '0', 3, 7);

-- ----------------------------
-- Table structure for leave_info
-- ----------------------------
DROP TABLE IF EXISTS `leave_info`;
CREATE TABLE `leave_info`  (
  `leave_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `student_id` int NULL DEFAULT NULL COMMENT '学生ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `leave_type` int NULL DEFAULT NULL COMMENT '1离校、2留校',
  `leave_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '离校、留校时间',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原因',
  `pics` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请假单照片',
  `dorm_id` int NULL DEFAULT NULL COMMENT '宿舍ID',
  `building_id` int NULL DEFAULT NULL COMMENT '楼栋ID',
  PRIMARY KEY (`leave_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of leave_info
-- ----------------------------
INSERT INTO `leave_info` VALUES (1, 19, '2024-03-18 11:54:43', 1, '2024-03-25至2024-05-29', '去实习', '', 15, 3);
INSERT INTO `leave_info` VALUES (2, 8, '2024-03-18 13:27:26', 1, '2024-03-27至2024-03-29', '请假回家', '', 7, 3);
INSERT INTO `leave_info` VALUES (3, 2, '2024-03-18 13:33:09', 2, '2024-05-01至2024-05-06', '五一假期留校住宿', '', 2, 1);
INSERT INTO `leave_info` VALUES (4, 8, '2024-03-18 13:34:04', 1, '2024-05-13至2024-05-15', '请病假', 'http://118.25.89.125:18080/images/20240318/133402cp4dk.jpg', 7, 3);
INSERT INTO `leave_info` VALUES (5, 16, '2024-03-18 15:19:30', 1, '2024-04-15至2024-04-17', '请假回家', 'http://118.25.89.125:18080/images/20240318/151929msr1e.jpg', 14, 3);
INSERT INTO `leave_info` VALUES (6, 43, '2024-04-04 19:47:31', 1, '2024-04-04至2024-04-06', '请假回家', '', 21, 3);
INSERT INTO `leave_info` VALUES (7, 43, '2024-04-04 19:49:04', 1, '2024-04-24至2024-04-25', '请病假', '', 21, 3);
INSERT INTO `leave_info` VALUES (8, 40, '2024-04-04 19:53:17', 1, '2024-04-15至2024-04-26', '到分校区上课', '', 20, 3);

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg`  (
  `msg_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `student_id` int NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `msg_time` datetime NULL DEFAULT NULL,
  `is_read` int NULL DEFAULT NULL,
  `fee_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of msg
-- ----------------------------
INSERT INTO `msg` VALUES (37, '12月水费催缴通知', 16, '同学您好，12月的水费账单已经生成，使用度量为10.6吨，合计79.5元，请及时缴纳！', '2024-04-06 16:57:44', 1, 21);
INSERT INTO `msg` VALUES (38, '12月水费催缴通知', 17, '同学您好，12月的水费账单已经生成，使用度量为10.6吨，合计79.5元，请及时缴纳！', '2024-04-06 16:57:44', 1, 21);
INSERT INTO `msg` VALUES (39, '11月电费催缴通知', 16, '同学您好，11月的电费账单已经生成，使用度量为288.0度，合计216.0元，请及时缴纳！', '2024-04-06 16:57:46', 0, 18);
INSERT INTO `msg` VALUES (40, '11月电费催缴通知', 17, '同学您好，11月的电费账单已经生成，使用度量为288.0度，合计216.0元，请及时缴纳！', '2024-04-06 16:57:46', 0, 18);
INSERT INTO `msg` VALUES (41, '3月电费催缴通知', 16, '同学您好，3月的电费账单已经生成，使用度量为275.0度，合计206.25元，请及时缴纳！', '2024-04-06 17:06:06', 0, 26);
INSERT INTO `msg` VALUES (42, '3月电费催缴通知', 17, '同学您好，3月的电费账单已经生成，使用度量为275.0度，合计206.25元，请及时缴纳！', '2024-04-06 17:06:06', 0, 26);
INSERT INTO `msg` VALUES (43, '2月电费催缴通知', 16, '同学您好，2月的电费账单已经生成，使用度量为102.0度，合计76.5元，请及时缴纳！', '2024-04-06 17:06:27', 1, 24);
INSERT INTO `msg` VALUES (44, '2月电费催缴通知', 17, '同学您好，2月的电费账单已经生成，使用度量为102.0度，合计76.5元，请及时缴纳！', '2024-04-06 17:06:27', 1, 24);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT,
  `publish_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '2024-03-06 13:57:04', '南区17栋停电通知', '通知', '<p>2023年4月11日，为配合电力公司检修电力设备，将对南区17栋实施停电，停电时间为9:00~11:30，请同学们做好时间安排。</p><p><br></p>');
INSERT INTO `notice` VALUES (2, '2024-03-06 13:58:17', '4月8日南区篮球场举行创意市集活动', '活动', '<p>4月8日南区篮球场举行创意市集活动，欢迎大家前往参加！</p>');
INSERT INTO `notice` VALUES (3, '2024-03-10 15:29:41', '饭堂营业时间变更通知', '通知', '<p>5月1日起，学校南区饭堂营业时间将变更为<span style=\"font-weight: bold; color: rgb(249, 150, 59);\">6:30~22:30</span>，请同学们知悉，做好就餐安排。</p>');
INSERT INTO `notice` VALUES (4, '2024-03-15 09:43:02', '2024年第一次卫生检查通告', '公告', '<p>本次卫生检查，共有3个宿舍不合格：</p><p>1、南区17栋301</p><p>2、南区17楼202</p><p>3、南区8栋502</p><p>请以各位同学继续做好内务，达到学校要求的标准。</p>');
INSERT INTO `notice` VALUES (5, '2024-03-15 09:42:30', '3月16日停水通知', '通知', '<p>各位同学：</p><p>本周六（3月16日）14:00至14:30南生活区因更换主水管阀门将会暂停供水，请各位同学提前做好储水，如有不便敬请谅解。</p>');
INSERT INTO `notice` VALUES (6, '2024-03-15 09:42:30', '3月16日停水通知', '通知', '<p>各位同学：</p><p>本周六（3月16日）14:00至14:30南生活区因更换主水管阀门将会暂停供水，请各位同学提前做好储水，如有不便敬请谅解。</p>');

-- ----------------------------
-- Table structure for occupation
-- ----------------------------
DROP TABLE IF EXISTS `occupation`;
CREATE TABLE `occupation`  (
  `occupation_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `student_id` int NULL DEFAULT NULL COMMENT '学生ID',
  `operator_id` int NULL DEFAULT NULL COMMENT '操作人ID',
  `check_in_time` datetime NULL DEFAULT NULL COMMENT '入住时间',
  `check_out_time` datetime NULL DEFAULT NULL COMMENT '搬离时间',
  `curr_status` int NULL DEFAULT NULL COMMENT '当前状态',
  `dorm_id` int NULL DEFAULT NULL COMMENT '宿舍ID',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `building_id` int NULL DEFAULT NULL COMMENT '楼宇ID',
  PRIMARY KEY (`occupation_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of occupation
-- ----------------------------
INSERT INTO `occupation` VALUES (1, 4, NULL, '2023-04-06 16:28:30', '2024-03-14 11:27:59', -1, 1, '已申请休学', 1);
INSERT INTO `occupation` VALUES (2, 2, 1, '2023-04-06 18:25:33', '2024-03-14 11:25:54', 1, 2, '新学年更新宿舍#入住新宿舍自动迁出。', 1);
INSERT INTO `occupation` VALUES (3, 3, 1, '2023-03-02 18:26:15', '2023-04-06 18:32:09', -1, 1, '学生自行申请更换宿舍', 1);
INSERT INTO `occupation` VALUES (4, 1, 1, '2023-04-06 18:30:02', NULL, 1, 2, '新学年更新宿舍', 1);
INSERT INTO `occupation` VALUES (5, 1, 1, '2022-09-01 14:42:37', '2023-02-13 10:42:54', -1, 5, '原宿舍维修，安排更换宿舍', 3);
INSERT INTO `occupation` VALUES (7, 6, 1, '2024-02-26 11:25:24', NULL, 1, 8, '', 3);
INSERT INTO `occupation` VALUES (8, 7, 1, '2024-02-26 11:25:26', NULL, 1, 6, '', 3);
INSERT INTO `occupation` VALUES (9, 2, 6, '2024-03-14 11:25:54', NULL, 1, 1, '', 1);
INSERT INTO `occupation` VALUES (10, 8, 6, '2024-03-14 13:35:15', NULL, 1, 7, '', 3);
INSERT INTO `occupation` VALUES (11, 10, 6, '2024-03-17 17:16:53', NULL, 1, 11, '', 3);
INSERT INTO `occupation` VALUES (12, 11, 6, '2024-03-17 17:17:02', '2024-03-17 19:43:52', -1, 13, '申请更换宿舍', 3);
INSERT INTO `occupation` VALUES (13, 12, 6, '2024-03-17 17:17:22', NULL, 1, 11, '', 3);
INSERT INTO `occupation` VALUES (14, 13, 6, '2024-03-17 17:17:32', NULL, 1, 11, '', 3);
INSERT INTO `occupation` VALUES (15, 14, 6, '2024-03-17 17:17:51', NULL, 1, 11, '', 3);
INSERT INTO `occupation` VALUES (16, 15, 6, '2024-03-17 17:21:34', '2024-03-17 19:31:01', -1, 14, '申请校外住宿', 3);
INSERT INTO `occupation` VALUES (17, 16, 6, '2024-03-17 17:22:36', NULL, 1, 14, '', 3);
INSERT INTO `occupation` VALUES (18, 17, 6, '2024-03-17 17:22:50', NULL, 1, 14, '', 3);
INSERT INTO `occupation` VALUES (19, 18, 6, '2024-03-17 17:22:59', NULL, 1, 15, '', 3);
INSERT INTO `occupation` VALUES (20, 19, 6, '2024-03-17 17:23:10', NULL, 1, 15, '', 3);
INSERT INTO `occupation` VALUES (21, 20, 6, '2024-03-17 17:23:21', NULL, 1, 15, '', 3);
INSERT INTO `occupation` VALUES (22, 21, 6, '2024-03-17 17:23:34', NULL, 1, 15, '', 3);
INSERT INTO `occupation` VALUES (23, 40, 6, '2024-03-17 19:39:33', NULL, 1, 20, '', 3);
INSERT INTO `occupation` VALUES (24, 41, 6, '2024-03-17 19:40:03', NULL, 1, 20, '', 3);
INSERT INTO `occupation` VALUES (25, 42, 6, '2024-03-17 19:40:12', NULL, 1, 20, '', 3);
INSERT INTO `occupation` VALUES (26, 43, 6, '2024-03-17 19:40:21', NULL, 1, 21, '', 3);
INSERT INTO `occupation` VALUES (27, 44, 6, '2024-03-17 19:40:31', NULL, 1, 21, '', 3);
INSERT INTO `occupation` VALUES (28, 53, 6, '2024-03-17 19:41:01', '2024-03-17 19:41:45', -1, 2, '#入住新宿舍自动迁出。', 1);
INSERT INTO `occupation` VALUES (29, 63, 6, '2024-03-17 19:41:15', NULL, 1, 23, '', 3);
INSERT INTO `occupation` VALUES (30, 54, 6, '2024-03-17 19:41:25', NULL, 1, 24, '', 3);
INSERT INTO `occupation` VALUES (31, 53, 6, '2024-03-17 19:41:45', NULL, 1, 22, '', 3);
INSERT INTO `occupation` VALUES (32, 26, 6, '2024-03-18 15:47:46', NULL, 1, 16, '', 3);
INSERT INTO `occupation` VALUES (33, 45, 6, '2024-03-18 15:48:08', NULL, 1, 21, '', 3);
INSERT INTO `occupation` VALUES (34, 48, 6, '2024-03-18 15:48:38', NULL, 1, 21, '', 3);
INSERT INTO `occupation` VALUES (35, 90, 6, '2024-04-03 17:50:39', NULL, 1, 3, '', 2);
INSERT INTO `occupation` VALUES (36, 91, 6, '2024-04-03 17:50:50', NULL, 1, 3, '', 2);
INSERT INTO `occupation` VALUES (37, 74, 6, '2024-04-03 17:51:06', NULL, 1, 12, '', 4);

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `student_id` int NOT NULL AUTO_INCREMENT COMMENT '物理ID',
  `student_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `gender` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '性别',
  `grade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级',
  `college` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '院系',
  `major_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业班级',
  `mobile` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`student_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 114 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '20231022314', '单慧', '女', '2023', '计算机学院', '软件工程2302', '18798909987', 1, '123456');
INSERT INTO `student` VALUES (2, '20231022302', '刘健霜', '女', '2023', '计算机学院', '软件工程2302', NULL, 1, NULL);
INSERT INTO `student` VALUES (3, '20231022303', '冯苇婷', '女', '2023', '计算机学院', '软件工程2302', NULL, -1, NULL);
INSERT INTO `student` VALUES (4, '20221022305', '王礼娜', '女', '2022', '计算机学院', '软件工程2202', NULL, 0, NULL);
INSERT INTO `student` VALUES (5, '20221022306', '卢秀珠', '女', '2022', '计算机学院', '软件工程2202', NULL, -1, NULL);
INSERT INTO `student` VALUES (6, '20220319105', '陆作虎', '男', '2022', '艺术学院', '视觉2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (7, '20220319106', '崔锡嘉', '男', '2022', '艺术学院', '视觉2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (8, '20231022103', '沈杨', '男', '2023', '计算机学院', '软件工程2302', '18677871234', 1, '123456');
INSERT INTO `student` VALUES (10, '20231021101', '王一博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (11, '20231021102', '王二博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (12, '20231021103', '王三博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (13, '20231021104', '王四博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (14, '20231021105', '王五博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (15, '20231021106', '王六博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (16, '20231021107', '张英才', '男', '2023', '计算机学院', '软件工程2301', '18199091234', 1, '123456');
INSERT INTO `student` VALUES (17, '20231021108', '王八博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (18, '20231021109', '王九博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (19, '20231021110', '王十博', '男', '2023', '计算机学院', '软件工程2301', '18290901234', 1, '123456');
INSERT INTO `student` VALUES (20, '20231021111', '李一博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (21, '20231021112', '李二博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (22, '20231021113', '李三博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (23, '20231021114', '李四博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (24, '20231021115', '李五博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (25, '20231021116', '李六博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (26, '20231021117', '李七博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (27, '20231021118', '李八博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (28, '20231021119', '李九博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (29, '20231021120', '李十博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (30, '20231021121', '赵一博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (31, '20231021122', '赵二博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (32, '20231021123', '赵三博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (33, '20231021124', '赵四博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (34, '20231021125', '赵五博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (35, '20231021126', '赵六博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (36, '20231021127', '赵七博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (37, '20231021128', '赵八博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (38, '20231021129', '赵九博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (39, '20231021130', '赵十博', '男', '2023', '计算机学院', '软件工程2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (40, '20231032101', '刘一倩', '女', '2022', '艺术系', '视觉设计2201', '18222098787', 1, '123456');
INSERT INTO `student` VALUES (41, '20231032102', '刘二倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (42, '20231032103', '刘三倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (43, '20231032104', '刘四倩', '女', '2022', '艺术系', '视觉设计2201', '18190991210', 1, '123456');
INSERT INTO `student` VALUES (44, '20231032105', '刘五倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (45, '20231032106', '刘六倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (46, '20231032107', '刘七倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (47, '20231032108', '刘八倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (48, '20231032109', '刘九倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (49, '20231032110', '刘十倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (50, '20231032111', '李一倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (51, '20231032112', '李二倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (52, '20231032113', '李三倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (53, '20231032114', '李四倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (54, '20231032115', '李五倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (55, '20231032116', '李六倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (56, '20231032117', '李七倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (57, '20231032118', '李八倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (58, '20231032119', '李九倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (59, '20231032120', '李十倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (60, '20231032121', '赵一倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (61, '20231032122', '赵二倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (62, '20231032123', '赵三倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (63, '20231032124', '赵四倩', '女', '2022', '艺术系', '视觉设计2201', NULL, 0, NULL);
INSERT INTO `student` VALUES (64, '20231032125', '赵五倩', '女', '2023', '艺术系', '视觉设计2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (65, '20231032126', '赵六倩', '女', '2023', '外语系', '日语2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (66, '20231032127', '赵七倩', '女', '2023', '外语系', '日语2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (67, '20231032128', '赵八倩', '女', '2023', '外语系', '日语2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (68, '20231032129', '赵九倩', '女', '2023', '艺术系', '视觉设计2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (69, '20231032130', '赵十倩', '女', '2023', '艺术系', '视觉设计2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (70, '20237021101', '刘一倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (71, '20237021102', '刘二倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (72, '20237021103', '刘三倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (73, '20237021104', '刘四倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (74, '20237021105', '刘五倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (75, '20237021106', '刘六倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (76, '20237021107', '刘七倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (77, '20237021108', '刘八倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (78, '20237021109', '刘九倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (79, '20237021110', '刘十倩', '女', '2023', '园艺园林学院', '园艺2301', NULL, 0, NULL);
INSERT INTO `student` VALUES (80, '20237021201', '宋一倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (81, '20237021202', '宋二倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (82, '20237021203', '宋三倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (83, '20237021204', '宋四倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (84, '20237021205', '宋五倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (85, '20237021206', '宋六倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (86, '20237021207', '宋七倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (87, '20237021208', '宋八倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (88, '20237021209', '宋九倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (89, '20237021210', '宋十倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (90, '20237021211', '张一倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (91, '20237021212', '张二倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (92, '20237021213', '张三倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (93, '20237021214', '张四倩', '女', '2023', '园艺园林学院', '园艺2302', NULL, 0, NULL);
INSERT INTO `student` VALUES (94, '20233301101', '罗一诺', '男', '2023', '信息学院', '通讯工程', '18712341234', 0, '123456');
INSERT INTO `student` VALUES (95, '20233301102', '罗二诺', '男', '2023', '信息学院', '通讯工程', '18712341235', 0, '123456');
INSERT INTO `student` VALUES (96, '20233301103', '罗三诺', '男', '2023', '信息学院', '通讯工程', '18712341236', 0, '123456');
INSERT INTO `student` VALUES (97, '20233301104', '罗四诺', '男', '2023', '信息学院', '通讯工程', '18712341237', 0, '123456');
INSERT INTO `student` VALUES (98, '20233301105', '罗五诺', '男', '2023', '信息学院', '通讯工程', '18712341238', 0, '123456');
INSERT INTO `student` VALUES (99, '20233301106', '罗六诺', '男', '2023', '信息学院', '通讯工程', '18712341239', 0, '123456');
INSERT INTO `student` VALUES (100, '20233301107', '罗七诺', '男', '2023', '信息学院', '通讯工程', '18712341240', 0, '123456');
INSERT INTO `student` VALUES (101, '20233301108', '罗八诺', '男', '2023', '信息学院', '通讯工程', '18712341241', 0, '123456');
INSERT INTO `student` VALUES (102, '20233301109', '罗九诺', '男', '2023', '信息学院', '通讯工程', '18712341242', 0, '123456');
INSERT INTO `student` VALUES (103, '20233301110', '罗十诺', '男', '2023', '信息学院', '通讯工程', '18712341243', 0, '123456');
INSERT INTO `student` VALUES (104, '20233301111', '张一诺', '女', '2023', '机电工程学院', '自动化', '18712341244', 0, '123456');
INSERT INTO `student` VALUES (105, '20233301112', '张二诺', '女', '2023', '机电工程学院', '自动化', '18712341245', 0, '123456');
INSERT INTO `student` VALUES (106, '20233301113', '张三诺', '女', '2023', '机电工程学院', '自动化', '18712341246', 0, '123456');
INSERT INTO `student` VALUES (107, '20233301114', '张四诺', '女', '2023', '机电工程学院', '自动化', '18712341247', 0, '123456');
INSERT INTO `student` VALUES (108, '20233301115', '张五诺', '女', '2023', '机电工程学院', '自动化', '18712341248', 0, '123456');
INSERT INTO `student` VALUES (109, '20233301116', '张六诺', '女', '2023', '机电工程学院', '自动化', '18712341249', 0, '123456');
INSERT INTO `student` VALUES (110, '20233301117', '张七诺', '女', '2023', '机电工程学院', '自动化', '18712341250', 0, '123456');
INSERT INTO `student` VALUES (111, '20233301118', '张八诺', '女', '2023', '机电工程学院', '自动化', '18712341251', 0, '123456');
INSERT INTO `student` VALUES (112, '20233301119', '张九诺', '女', '2023', '机电工程学院', '自动化', '18712341252', 0, '123456');
INSERT INTO `student` VALUES (113, '20233301120', '张十诺', '女', '2023', '机电工程学院', '自动化', '18712341253', 0, '123456');

SET FOREIGN_KEY_CHECKS = 1;
