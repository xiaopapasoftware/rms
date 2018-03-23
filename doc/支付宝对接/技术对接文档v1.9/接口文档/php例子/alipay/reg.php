<?php
/**
 * 同步公寓信息
* --------------------------------------------------
* liwei.xie@monph.com
* request api type:
*/
include_once dirname(__FILE__)."/classes/AopClient.php";
include_once dirname(__FILE__)."/classes/SignData.php";
include_once dirname(__FILE__)."/classes/AlipayEcoRenthouseKaBaseinfoQueryRequest.php";
include_once dirname(__FILE__)."/classes/AlipayEcoRenthouseKaBaseinfoSyncRequest.php";
include_once dirname(__FILE__)."/classes/AlipayEcoRenthouseKaServiceCreateRequest.php";

$c = new AopClient;
$c->charset= "GBK";

//同步公寓名称
//$request = new AlipayEcoRenthouseKaBaseinfoSyncRequest();
//$data['ka_name'] = '蘑菇公寓'; //公寓运营商名称
//$json_data = json_encode($data);
//$request->setBizContent($json_data);
//$response= $c->execute($request);


//查询公寓基础信息
//$request = new AlipayEcoRenthouseKaBaseinfoQueryRequest();
//$data['ka_code'] = 'C6AoF5HSASiFKr'; //公寓运营商code 
//$json_data = json_encode($data);
//$request->setBizContent($json_data);
//$response= $c->execute($request);


//注册回调地址
//$request = new AlipayEcoRenthouseKaServiceCreateRequest();
//$data['ka_code'] = 'C6AoF5H4VZiFKr'; //公寓运营商code
//$data['type'] = 1; //1:预约看房 2:确认租约 3:拨号记录 4:支付页面url
//$data['address'] = 'https://XXXXXXXXXXX/notifyInvitation/';
//$json_data = json_encode($data);
//$request->setBizContent($json_data);
//$response= $c->execute($request);


var_dump($response);exit;