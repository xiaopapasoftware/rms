<?php
/**
 * 验签
*/

include_once dirname(__FILE__)."/classes/AopClient.php";
include_once dirname(__FILE__)."/classes/AopEncrypt.php";
include_once dirname(__FILE__)."/classes/SignData.php";
$c = new AopClient;

$json = '{"charset":"UTF-8","lookTime":"2017-12-16 12:00","bookSex":"1","cardType":"0","sign":"kYVSATH/vSIlN0f+xD7BQJPUtbrk7hNpQQeoaZAg1fUU/Su7/za+MtveQlRrZ7Dh9adi1SmssieQRArk83lhDk0m5dJvCPMzbrGAtI6QhsrcmPZeeuf9SiXlNyDcZseTy2z8yZ+YrQsYTkxdGsMsqkCDqbm6IPilkvQyDPa4iqc=","remark":"","roomCode":"4937","cardNo":"EzCTU+vQaz7rR/nQaEMOXNdTbw752a3BaeEWzWCX6LY=","bookName":"谷宏达","aliUserId":"2088112524921494","bookPhone":"wnXi9rZfwThtF/6FNW8s/Q==","service":"alipay.industry.car.appid.invoke","flatsTag":"1","sign_type":"RSA","_request_path":"http://api.monph.com/alipay/notifyInvitation/"}';
$r= $c->rsaCheckV3(json_decode($json, true));
var_dump($r);