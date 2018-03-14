<?php
/**
 * 上下架房源
* --------------------------------------------------
* liwei.xie@monph.com
* request api type:
*/

include_once dirname(__FILE__)."/classes/AopClient.php";
include_once dirname(__FILE__)."/classes/SignData.php";
include_once dirname(__FILE__)."/classes/AlipayEcoRenthouseRoomStateSyncRequest.php";


$c = new AopClient;
$c->charset= "GBK";

//录入分散式房源
$request = new AlipayEcoRenthouseRoomStateSyncRequest();

$data['room_code'] = '123456'; //公寓运营商内部存储的房源编号(ka系统的房源id)
$data['room_status'] = 1; //是否上架（0:下架，1：上架）
$data['rent_status'] = 1; //出租状态（1未租、2已租）
$data['flats_tag'] = 1; //房源类型（1:分散式 2：集中式）

$json_data = json_encode($data);
$request->setBizContent($json_data);
$response = $c->execute($request);
var_dump($response);exit;