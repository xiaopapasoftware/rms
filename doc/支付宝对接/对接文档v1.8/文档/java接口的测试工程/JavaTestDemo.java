package com.am.test;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.internal.util.json.JSONWriter;
import com.alipay.api.request.*;
import com.alipay.api.response.*;

import java.util.HashMap;
import java.util.Map;

public class TPJavaClient {
    private String TP_PRIVATEKEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKK0PXoLKnBkgtOl0kvyc9X2tUUdh/lRZr9RE1frjr2ZtAulZ+Moz9VJZFew1UZIzeK0478obY/DjHmD3GMfqJoTguVqJ2MEg+mJ8hJKWelvKLgfFBNliAw+/9O6Jah9Q3mRzCD8pABDEHY7BM54W7aLcuGpIIOa/qShO8dbXn+FAgMBAAECgYA8+nQ380taiDEIBZPFZv7G6AmT97doV3u8pDQttVjv8lUqMDm5RyhtdW4n91xXVR3ko4rfr9UwFkflmufUNp9HU9bHIVQS+HWLsPv9GypdTSNNp+nDn4JExUtAakJxZmGhCu/WjHIUzCoBCn6viernVC2L37NL1N4zrR73lSCk2QJBAPb/UOmtSx+PnA/mimqnFMMP3SX6cQmnynz9+63JlLjXD8rowRD2Z03U41Qfy+RED3yANZXCrE1V6vghYVmASYsCQQCoomZpeNxAKuUJZp+VaWi4WQeMW1KCK3aljaKLMZ57yb5Bsu+P3odyBk1AvYIPvdajAJiiikRdIDmi58dqfN0vAkEAjFX8LwjbCg+aaB5gvsA3t6ynxhBJcWb4UZQtD0zdRzhKLMuaBn05rKssjnuSaRuSgPaHe5OkOjx6yIiOuz98iQJAXIDpSMYhm5lsFiITPDScWzOLLnUR55HL/biaB1zqoODj2so7G2JoTiYiznamF9h9GuFC2TablbINq80U2NcxxQJBAMhw06Ha/U7qTjtAmr2qAuWSWvHU4ANu2h0RxYlKTpmWgO0f47jCOQhdC3T/RK7f38c7q8uPyi35eZ7S1e/PznY=";
    private String TP_OPENAPI_URL = "http://oepnapi.eco.dl.alipaydev.com/gateway.do";
    private String TP_APPID = "2015122300879608";//测试环境

    public static void main(String[] args) throws AlipayApiException {
        TPJavaClient client = new TPJavaClient();
        client.testState();
    }

    /**
     * 分散式房源同步
     *
     * @throws AlipayApiException
     */
    public void testDispersion() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        request.setBizContent("{\"bedroom_count\":6,\"community_code\":\"C0000192673888\",\"flat_area\":150.0,\"flat_building\":\"2\",\"flat_configs\":[\"3\",\"4\",\"5\",\"14\",\"8\",\"9\"],\"flat_unit\":\"4\",\"floor_count\":5,\"foregift_amount\":300.00,\"free_begin_date\":1504195200000,\"free_end_date\":4070880000000,\"images\":[\"https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/1/2017-09-12/1/410a08c298374b8b96b1f8bdab03f299/1_1505195885608.jpg\",\"https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/1/2017-09-12/1/f4bbe26729e94aa797de4ede3107d701/1_1505195885112.jpg\"],\"intro\":\"利嘉精品单间 一家专注于郑州高中档品质单间  方便省心 费用一目了然线上选房线下免费看房官方真是房源 杜绝虚假信息房源。\\r\\n 付款方式：付款方式灵活 月付 季付半年付 年付 提前30天支付下期租金  不关你是郑漂 ，还是土豪 都可以满足你的高品质住房需求，本套房源包含床空调 书桌 椅子 衣柜抽油烟机 燃气灶热水器 洗衣机 微波炉等设施。\\r\\n智能电表：电费透明 智能化电表 通过手机App可以实时查看用电情况 公共区域自动均摊  \\r\\n智能门锁：一房一锁  出门不用带钥匙 安全可靠。\\r\\n保洁服务：利嘉专属的保洁， 再也不用担心 公共区域的脏乱差。\\r\\n+快速维修  及时上门维修，无需请假在家等师傅。\\r\\n+专属客服   专业客服，利嘉全天候为您解忧。\\r\\n家电：所有家电 利嘉让您住房无忧。 \\r\\n品牌家具：根据房间量身定制 家具全新没有任何的味道 无甲醛环保 专业保洁  利嘉让您生活更省心。\",\"other_amount\":[{\"amount\":30.00,\"name\":\"保洁费\",\"unit\":\"元\"},{\"amount\":100.00,\"name\":\"物业费\",\"unit\":\"元\"},{\"amount\":365.00,\"name\":\"管理费\",\"unit\":\"元\"}],\"owners_name\":\"聂宝文\",\"owners_tel\":\"4000606868-30080\",\"parlor_count\":1,\"pay_type\":1,\"rent_status\":2,\"rent_type\":2,\"room_amount\":300.00,\"room_area\":10,\"room_code\":\"3621292\",\"room_configs\":[\"15\",\"2\",\"11\",\"13\",\"12\"],\"room_face\":2,\"room_name\":\"G\",\"room_num\":\"5东\",\"room_status\":0,\"toilet_count\":1,\"total_floor_count\":7}");
        AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }


    /**
     * 集中式房源同步
     * @throws AlipayApiException
     */
    public void testConcentration() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomConcentrationSyncRequest request = new AlipayEcoRenthouseRoomConcentrationSyncRequest();
        request.setBizContent("{\\\"bedroom_count\\\":1,\\\"community_code\\\":\\\"C0000248290\\\",\\\"flatTag\\\":2,\\\"foregift_amount\\\":9710.00,\\\"free_begin_date\\\":\\\"\\\",\\\"free_end_date\\\":\\\"\\\",\\\"fresh_room_status\\\":0,\\\"images\\\":[\\\"https://ecopublic.oss-cn-hangzhou.aliyuncs.com/eco/renthouse/1/2017-10-16/1/fd422ef688934308a2744c74a69857f7/1_1508123161808.jpg\\\"],\\\"intro\\\":\\\"很不错的放假 赞\\\",\\\"max_amount\\\":9710.00,\\\"mogo_score\\\":169,\\\"nick_name\\\":\\\"带阳台的阳光房\\\",\\\"other_amount\\\":[{\\\"amount\\\":77.00,\\\"name\\\":\\\"门卡押金\\\",\\\"unit\\\":\\\"元\\\"}],\\\"owners_name\\\":\\\"qwe\\\",\\\"owners_tel\\\":\\\"4000606868-91128\\\",\\\"parlor_count\\\":1,\\\"pay_type\\\":3,\\\"rent_status\\\":1,\\\"rent_type\\\":1,\\\"room_amount\\\":9710.00,\\\"room_area\\\":25.0,\\\"room_code\\\":\\\"176272\\\",\\\"room_configs\\\":[\\\"15\\\",\\\"6\\\",\\\"3\\\",\\\"4\\\",\\\"5\\\",\\\"13\\\",\\\"12\\\",\\\"14\\\"],\\\"room_status\\\":1,\\\"toilet_count\\\":1,\\\"total_floor_count\\\":23}");
        AlipayEcoRenthouseRoomConcentrationSyncResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 上下架房源
     * @throws AlipayApiException
     */
    /**
     * 上下架房源
     * @throws AlipayApiException
     */
    public void testState() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomStateSyncRequest request = new AlipayEcoRenthouseRoomStateSyncRequest();
        request.setBizContent("{" +
                "    \"flats_tag\": 1," +
                "    \"rent_status\": 1," +
                "    \"room_code\": \"24912\"," +
                "    \"room_status\": 0" +
                "}");
        AlipayEcoRenthouseRoomStateSyncResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 租约同步
     */
    public void testRenthouseLeaseOrderSync() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseLeaseOrderSyncRequest request = new AlipayEcoRenthouseLeaseOrderSyncRequest();
        request.setBizContent("{" +
                "    \"begin_date\": \"2017-03-10\"," +
                "    \"card_no\": \"370402198201254475\"," +
                "    \"card_type\": 0," +
                "    \"end_date\": \"2018-08-30\"," +
                "    \"flats_tag\": 1," +
                "    \"foregift_amount\": \"12.00\"," +
                "    \"free_deposit\": 1," +
                "    \"furniture_items\": \"[]\"," +
                "    \"lease_code\": \"1669896123\"," +
                "    \"lease_create_time\": \"2017-08-31 16:48:07\"," +
                "    \"lease_status\": 0," +
                "    \"other_fee_desc\": \"['门卡押金23元','家具押金1元']\"," +
                "    \"pay_type\": 3," +
                "    \"remark\": \"这是描述\"," +
                "    \"rent_day_desc\": \"每月8号收租\"," +
                "    \"rent_include_fee_desc\": [" +
                "        \"1\"," +
                "        \"2\"," +
                "        \"3\"," +
                "        \"4\"" +
                "    ]," +
                "    \"renter_name\": \"邵明基\"," +
                "    \"renter_phone\": \"13795436165\"," +
                "    \"room_code\": \"1234\"," +
                "    \"sale_amount\": \"23.12\"" +
                "}");

        System.out.println((new JSONWriter()).write(request.getBizModel(), true));
        AlipayEcoRenthouseLeaseOrderSyncResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }


    /**
     * 租约状态同步
     */
    public void testRenthouseLeaseState() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseLeaseStateSyncRequest request = new AlipayEcoRenthouseLeaseStateSyncRequest();
        request.setBizContent("{" +
                "    \"lease_ca_file\": \"\"," +
                "    \"lease_code\": \"4078921234\"," +
                "    \"lease_status\": 2" +
                "}");
        AlipayEcoRenthouseLeaseStateSyncResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 账单同步
     */
    public void testRenthouseBillOrder() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseBillOrderSyncRequest request = new AlipayEcoRenthouseBillOrderSyncRequest();
        request.setBizContent("{" +
                "    \"bill_number\": \"1\"," +
                "    \"bills\": [" +
                "        {" +
                "            \"bill_amount\": \"12.32\"," +
                "            \"bill_create_time\": \"2017-07-31 16:58:51\"," +
                "            \"bill_desc\": \"描述\"," +
                "            \"bill_no\": \"12.12\"," +
                "            \"bill_status\": 0," +
                "            \"bill_type\": \"10001\"," +
                "            \"deadline_date\": \"2017-08-04\"," +
                "            \"discount_amount\": \"12.56\"," +
                "            \"end_date\": \"2017-07-10\"," +
                "            \"lease_no\": \"123456789\"," +
                "            \"memo\": \"测试\"," +
                "            \"paid_amount\": \"12.32\"," +
                "            \"pay_lock\": 1," +
                "            \"pay_lock_memo\": \"支付房租\"," +
                "            \"pay_status\": 0," +
                "            \"pay_time\": \"2017-07-31 16:58:51\"," +
                "            \"start_date\": \"2017-01-02\"" +
                "        }" +
                "    ]," +
                "    \"trade_id\": \"A201705020000000002\"" +
                "}");
        AlipayEcoRenthouseBillOrderSyncResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 文件资源上传
     */
    public void testRenthouseCommonImageUpload() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseCommonImageUploadRequest request = new AlipayEcoRenthouseCommonImageUploadRequest();
        request.setBizContent("{" +
                "    \"file_base\": \"\"," + //图片base64
                "    \"file_type\": \"1\"," +
                "    \"is_public\": true" +
                "}");
        AlipayEcoRenthouseCommonImageUploadResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 公寓运营商基础信息维护
     */
    
    public void testRenthouseKaBaseinfoSync() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseKaBaseinfoSyncRequest request = new AlipayEcoRenthouseKaBaseinfoSyncRequest();
        //注意该接口使用的前提是，需要开通新的ISVappid权限，目前开发环境的测试账号已被使用
        request.setBizContent("{" +
                "    \"ka_name\": \"张三公寓\"" +
                "}");
        AlipayEcoRenthouseKaBaseinfoSyncResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 公寓运营商基础信息获取
     */
    public void testRenthouseKaBaseinfoQuery() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseKaBaseinfoQueryRequest request = new AlipayEcoRenthouseKaBaseinfoQueryRequest();
        request.setBizContent("{" +
                "\"ka_code\": \"1234\"" +
                "}");
        AlipayEcoRenthouseKaBaseinfoQueryResponse response = alipayClient.execute(request);
        System.out.println(response.getKaName());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    /**
     * 公寓运营商服务地址注册
     */
    public void testRenthouseKaServiceCreate() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseKaServiceCreateRequest request = new AlipayEcoRenthouseKaServiceCreateRequest();
        request.setBizContent("{" +
                "    \"address\": \"http://xxx/pay/pay.html\"," +
                "    \"ka_code\": \"1YqOEtXtWgsrhKjIc111\"," +
                "    \"type\": 1" +
                "}");
        AlipayEcoRenthouseKaServiceCreateResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}
