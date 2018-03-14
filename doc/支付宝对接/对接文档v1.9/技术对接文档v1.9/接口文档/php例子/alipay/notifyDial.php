<?php
/**
 * 拨号记录通知
* --------------------------------------------------
* liwei.xie@monph.com
* request api type:
*/

include_once dirname(__FILE__)."/classes/AopClient.php";
include_once dirname(__FILE__)."/classes/AopEncrypt.php";
include_once dirname(__FILE__)."/classes/SignData.php";

if(isset($_POST) && !empty($_POST)){
    $c = new AopClient;
    $sign_res = $c->rsaCheckV3($_POST);
    if(!$sign_res){        
        $r['code'] = 0;
        $r['message'] = '验签失败';
        echo json_encode($r);exit;
    }
	//身份证号解密
    $cardNo = decrypt($_POST['cardNo'], $c->encryptKey);
    
    $r['code'] = 1;
    $r['message'] = '成功';
    echo json_encode($r);exit;

}



