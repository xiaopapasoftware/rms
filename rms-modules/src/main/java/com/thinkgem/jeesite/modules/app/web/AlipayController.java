package com.thinkgem.jeesite.modules.app.web;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayEcoRenthouseKaBaseinfoQueryRequest;
import com.alipay.api.request.AlipayEcoRenthouseKaBaseinfoSyncRequest;
import com.alipay.api.request.AlipayEcoRenthouseKaServiceCreateRequest;
import com.alipay.api.response.AlipayEcoRenthouseKaBaseinfoQueryResponse;
import com.alipay.api.response.AlipayEcoRenthouseKaBaseinfoSyncResponse;
import com.alipay.api.response.AlipayEcoRenthouseKaServiceCreateResponse;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 支付宝Controller
 * 
 * @author xiao
 * @version 2018-01-02
 */
@Controller
@RequestMapping(value = "${adminPath}/app/alipay")
public class AlipayController extends BaseController {

  private String TP_PRIVATEKEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKK0PXoLKnBkgtOl0kvyc9X2tUUdh/lRZr9RE1frjr2ZtAulZ+Moz9VJZFew1UZIzeK0478obY/DjHmD3GMfqJoTguVqJ2MEg+mJ8hJKWelvKLgfFBNliAw+/9O6Jah9Q3mRzCD8pABDEHY7BM54W7aLcuGpIIOa/qShO8dbXn+FAgMBAAECgYA8+nQ380taiDEIBZPFZv7G6AmT97doV3u8pDQttVjv8lUqMDm5RyhtdW4n91xXVR3ko4rfr9UwFkflmufUNp9HU9bHIVQS+HWLsPv9GypdTSNNp+nDn4JExUtAakJxZmGhCu/WjHIUzCoBCn6viernVC2L37NL1N4zrR73lSCk2QJBAPb/UOmtSx+PnA/mimqnFMMP3SX6cQmnynz9+63JlLjXD8rowRD2Z03U41Qfy+RED3yANZXCrE1V6vghYVmASYsCQQCoomZpeNxAKuUJZp+VaWi4WQeMW1KCK3aljaKLMZ57yb5Bsu+P3odyBk1AvYIPvdajAJiiikRdIDmi58dqfN0vAkEAjFX8LwjbCg+aaB5gvsA3t6ynxhBJcWb4UZQtD0zdRzhKLMuaBn05rKssjnuSaRuSgPaHe5OkOjx6yIiOuz98iQJAXIDpSMYhm5lsFiITPDScWzOLLnUR55HL/biaB1zqoODj2so7G2JoTiYiznamF9h9GuFC2TablbINq80U2NcxxQJBAMhw06Ha/U7qTjtAmr2qAuWSWvHU4ANu2h0RxYlKTpmWgO0f47jCOQhdC3T/RK7f38c7q8uPyi35eZ7S1e/PznY=";
  private String TP_OPENAPI_URL = "http://oepnapi.eco.dl.alipaydev.com/gateway.do";
  private String TP_APPID = "2015122300879608";//测试环境

  /**
   * 基础信息维护
   */
  @RequestMapping(value = "baseInfoSync/{name}")
  @ResponseBody
  public String baseInfoSync(@PathVariable("name") String name) throws AlipayApiException {
    AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
    AlipayEcoRenthouseKaBaseinfoSyncRequest request = new AlipayEcoRenthouseKaBaseinfoSyncRequest();
    //注意该接口使用的前提是，需要开通新的ISVappid权限，目前开发环境的测试账号已被使用
    request.setBizContent("{" +
            "    \"ka_name\": \" "+ name + " \"" +
            "}");
    AlipayEcoRenthouseKaBaseinfoSyncResponse response = alipayClient.execute(request);
    if (response.isSuccess()) {
      return response.getKaCode();
    } else {
      return response.getMsg();
    }
  }

  /**
   * 基础信息获取
   */
  @RequestMapping(value = "baseInfoQuery/{kaCode}")
  @ResponseBody
  public String baseInfoQuery(@PathVariable("kaCode") String kaCode) throws AlipayApiException {
    AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
    AlipayEcoRenthouseKaBaseinfoQueryRequest request = new AlipayEcoRenthouseKaBaseinfoQueryRequest();
    request.setBizContent("{" + "\"ka_code\": \"" + kaCode  + "\"" + "}");
    AlipayEcoRenthouseKaBaseinfoQueryResponse response = alipayClient.execute(request);
    if (response.isSuccess()) {
      return response.getValid();
    } else {
      return response.getMsg();
    }
  }

  /**
   * 公寓运营商服务地址注册
   */
  @RequestMapping(value = "serviceCreate/{type}")
  @ResponseBody
  public String serviceCreate(@PathVariable("type") String type) throws AlipayApiException {
    PropertiesLoader loader = new PropertiesLoader("jeesite.properties");
    String kaCode =  loader.getProperty("alipay.kacode");
    String url = "";
    if ("1".equals(type)) {
      url = loader.getProperty("alipay.url.reservation");
    } else if ("2".equals(type)) {
      url = loader.getProperty("alipay.url.affirm");
    } else if ("3".equals(type)) {
      url = loader.getProperty("alipay.url.call");
    }
    AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
    AlipayEcoRenthouseKaServiceCreateRequest request = new AlipayEcoRenthouseKaServiceCreateRequest();
    request.setBizContent("{" +
            "    \"address\": \"" + url + "\"," +
            "    \"ka_code\": \"" + kaCode + "\"," +
            "    \"type\": " + type +
            "}");
    AlipayEcoRenthouseKaServiceCreateResponse response = alipayClient.execute(request);
    if (response.isSuccess()) {
      return "success";
    } else {
      return response.getMsg();
    }
  }

}
