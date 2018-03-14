<?php3
/**
 * 同步小区
 *
 */
header("Content-type: text/json;charset=UTF-8");

include_once dirname(__FILE__)."/classes/AopClient.php";
include_once dirname(__FILE__)."/classes/SignData.php";
include_once dirname(__FILE__)."/classes/AlipayEcoRenthouseCommunityInfoSyncRequest.php";

$c = new AopClient;
$c->charset= "GBK";
$request = new AlipayEcoRenthouseCommunityInfoSyncRequest();
$data['city_name'] = '郑州市'; //
$data['district_name'] = '郑东新区'; //行政区县
$data['community_name'] = '建业贰号城邦'; //小区名称
$data['community_locations'] = '113.672470|34.793300'; //113.672470|34.793300
$data['coordsys'] = 0;

$json_data = json_encode($data);
$request->setBizContent($json_data);
$response= $c->execute($request);
$response = json_encode($response);
var_dump($response);
exit;
