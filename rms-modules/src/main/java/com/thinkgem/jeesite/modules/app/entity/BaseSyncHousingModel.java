package com.thinkgem.jeesite.modules.app.entity;

import java.util.List;

/**
 * 支付宝房源同步对象
 */
public class BaseSyncHousingModel {
    protected String comm_req_id;//小区同步请求号
    protected String room_code;//传递给支付宝租房的房源编号
    protected String floor_count;//所在楼层
    protected String total_floor_count;//房屋总楼层
    protected String bedroom_count;//户型-房数
    protected String parlor_count;//户型-厅数
    protected String toilet_count;//户型-卫数
    protected String room_area;//房间面积
    protected String rent_status;//出租状态，1-未租，2-已租
    protected String pay_type;//房租付几个月的方式
    protected String intro;//房源描述
    protected String room_amount;//租金
    protected String foregift_amount;//押金
    protected String[] images;//房源图片
    protected String owners_name;//管家姓名
    protected String owners_tel;//管家电话（预约看房用）
    protected String checkin_time;//可入住时间
    protected String room_status;// 房源初始上下架状态 1：上架 0：下架
    protected String rent_type;//出租类型 1：整租，2：合租
    protected String flat_area;//公寓面积
    protected String flat_building;//楼号
    protected String room_num;//房屋号-201
    protected String[] room_configs;//分散式房间物品配置
    protected String[] flat_configs;//分散式合租公共区域物品配置，分散式整租不用设置
    protected String room_name;//房间号，A-Z
    protected String room_face;//房间朝向
    protected AlipayEcoRenthouseOtherAmount[] other_amount;//其他费用
    private String nick_name;
    private String max_amount;
    private String room_store_no;//房源对应的店铺编号

    public class AlipayEcoRenthouseOtherAmount {
        String name;//费用名称：门卡费
        String amount;//费用金额 30
        String unit = "元";//费用单位 元

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public String getComm_req_id() {
        return comm_req_id;
    }

    public void setComm_req_id(String comm_req_id) {
        this.comm_req_id = comm_req_id;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getFloor_count() {
        return floor_count;
    }

    public void setFloor_count(String floor_count) {
        this.floor_count = floor_count;
    }

    public String getTotal_floor_count() {
        return total_floor_count;
    }

    public void setTotal_floor_count(String total_floor_count) {
        this.total_floor_count = total_floor_count;
    }

    public String getBedroom_count() {
        return bedroom_count;
    }

    public void setBedroom_count(String bedroom_count) {
        this.bedroom_count = bedroom_count;
    }

    public String getParlor_count() {
        return parlor_count;
    }

    public void setParlor_count(String parlor_count) {
        this.parlor_count = parlor_count;
    }

    public String getToilet_count() {
        return toilet_count;
    }

    public void setToilet_count(String toilet_count) {
        this.toilet_count = toilet_count;
    }


    public String getRoom_area() {
        return room_area;
    }

    public void setRoom_area(String room_area) {
        this.room_area = room_area;
    }

    public String getRent_status() {
        return rent_status;
    }

    public void setRent_status(String rent_status) {
        this.rent_status = rent_status;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getRoom_amount() {
        return room_amount;
    }

    public void setRoom_amount(String room_amount) {
        this.room_amount = room_amount;
    }

    public String getForegift_amount() {
        return foregift_amount;
    }

    public void setForegift_amount(String foregift_amount) {
        this.foregift_amount = foregift_amount;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getOwners_name() {
        return owners_name;
    }

    public void setOwners_name(String owners_name) {
        this.owners_name = owners_name;
    }

    public String getOwners_tel() {
        return owners_tel;
    }

    public void setOwners_tel(String owners_tel) {
        this.owners_tel = owners_tel;
    }

    public String getCheckin_time() {
        return checkin_time;
    }

    public void setCheckin_time(String checkin_time) {
        this.checkin_time = checkin_time;
    }

    public String getRoom_status() {
        return room_status;
    }

    public void setRoom_status(String room_status) {
        this.room_status = room_status;
    }

    public String getRent_type() {
        return rent_type;
    }

    public void setRent_type(String rent_type) {
        this.rent_type = rent_type;
    }

    public String getFlat_area() {
        return flat_area;
    }

    public void setFlat_area(String flat_area) {
        this.flat_area = flat_area;
    }

    public String getFlat_building() {
        return flat_building;
    }

    public void setFlat_building(String flat_building) {
        this.flat_building = flat_building;
    }

    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public String[] getRoom_configs() {
        return room_configs;
    }

    public void setRoom_configs(String[] room_configs) {
        this.room_configs = room_configs;
    }

    public String[] getFlat_configs() {
        return flat_configs;
    }

    public void setFlat_configs(String[] flat_configs) {
        this.flat_configs = flat_configs;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_face() {
        return room_face;
    }

    public void setRoom_face(String room_face) {
        this.room_face = room_face;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getMax_amount() {
        return max_amount;
    }

    public void setMax_amount(String max_amount) {
        this.max_amount = max_amount;
    }

    public AlipayEcoRenthouseOtherAmount[] getOther_amount() {
        return other_amount;
    }

    public void setOther_amount(AlipayEcoRenthouseOtherAmount[] other_amount) {
        this.other_amount = other_amount;
    }

    public String getRoom_store_no() {
        return room_store_no;
    }

    public void setRoom_store_no(String room_store_no) {
        this.room_store_no = room_store_no;
    }
}
