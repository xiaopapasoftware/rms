<?php
/**
 * 同步分散式房源
* --------------------------------------------------
* liwei.xie@monph.com
* request api type:
*/

include_once dirname(__FILE__)."/classes/AopClient.php";
include_once dirname(__FILE__)."/classes/SignData.php";
include_once dirname(__FILE__)."/classes/AlipayEcoRenthouseRoomDispersionSyncRequest.php";
include_once dirname(__FILE__)."/classes/AlipayEcoRenthouseCommonImageUploadRequest.php";


$json_data = '{"community_code":"C0000154560","room_code":"5278","floor_count":17,"total_floor_count":34,"flat_building":"8","flat_unit":"2","room_num":17204,"room_name":"D","room_face":"2","bedroom_count":4,"parlor_count":1,"toilet_count":1,"flat_area":112.86,"room_area":24.52,"rent_status":1,"intro":"内敛的颜色，时尚的装修风格，合理的布局，每一处无不展现魔飞公寓的用心和关怀。","flat_configs":["1","3","4","5","7","8","9"],"room_configs":["2","11","12","13"],"pay_type":1,"room_amount":899,"foregift_amount":899,"free_begin_date":"","free_end_date":"","images":["https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/497bcc284b4b408da74d6d0e91efa4c111.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/5a37d25997bb433f8206ab4a66a93ec1680.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/8fc37581af2b4ae6a0e9ecc6cab94e1a519.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/899d6a7b1e0e4304879816267b4cf221896.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/ee89b845e7f640d2bcb4bf5d20edfb52343.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/4268f1b5f220486a8cdf0f5e77cc3f89404.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/95af98be7d3a40328c73575e1d199d81505.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/7103d13de8324b34937d1d15ac6832d0350.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/77745c39dc834ab19fb0cb3a0eddbb02894.jpg","https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/5/2017-12-19/1/baf8278bdec44fad91a1cc0fc8785d45552.jpg"],"owners_name":"蘑菇公寓","owners_tel":"4000371851","checkin_time":"2017-12-19","room_status":1,"rent_type":2}';
$request->setBizContent($json_data);
$response= $c->execute($request);
var_dump($response);exit;

