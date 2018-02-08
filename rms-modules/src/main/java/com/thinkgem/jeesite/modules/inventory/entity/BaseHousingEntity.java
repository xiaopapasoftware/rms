package com.thinkgem.jeesite.modules.inventory.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * 房源基础实体对象
 *
 * @author wangj
 */
public class BaseHousingEntity<T> extends DataEntity<T> {
    protected PropertyProject propertyProject; // 物业项目
    protected Building building; // 楼宇
    protected String shortDesc;//房源描述
    protected String shortLocation;//地址描述
    protected String isFeature;//是否精选房源
    protected String attachmentPath;// 房源图片
    protected String choose;
    protected String orientation;// 房间朝向     0东 1南 2西 3北 4东南 5东北 6西南 7西北 8 9
    protected Double rental;//意向租金
    protected Long newId;
    protected Integer rentMonthGap;//房租支付间隔月数
    protected Integer deposMonthCount;//押金月数
    protected Integer alipayStatus;//支付宝同步状态
    protected Integer up;
    protected String reservationPhone;
    /**
     * 支付宝租房同步用
     */
    protected String feeConfigInfo;//用于同步支付宝的各种费用，配置形如：feeName1=feeAmt1,feeName2=feeAmt2,feeName3=feeAmt3,feeName4=feeAmt4,feeName5=feeAmt5
    protected String feeDesc1;
    protected String feeAmt1;
    protected String feeDesc2;
    protected String feeAmt2;
    protected String feeDesc3;
    protected String feeAmt3;
    protected String feeDesc4;
    protected String feeAmt4;
    protected String feeDesc5;
    protected String feeAmt5;

    public BaseHousingEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public BaseHousingEntity(String id) {
        super(id);
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    @NotNull(message = "物业项目不能为空")
    public PropertyProject getPropertyProject() {
        return propertyProject;
    }

    public void setPropertyProject(PropertyProject propertyProject) {
        this.propertyProject = propertyProject;
    }

    @NotNull(message = "楼宇不能为空")
    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortLocation() {
        return shortLocation;
    }

    public void setShortLocation(String shortLocation) {
        this.shortLocation = shortLocation;
    }

    public String getIsFeature() {
        return isFeature;
    }

    public void setIsFeature(String isFeature) {
        this.isFeature = isFeature;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public Double getRental() {
        return rental;
    }

    public void setRental(Double rental) {
        this.rental = rental;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    public Integer getRentMonthGap() {
        return rentMonthGap;
    }

    public void setRentMonthGap(Integer rentMonthGap) {
        this.rentMonthGap = rentMonthGap;
    }

    public Integer getDeposMonthCount() {
        return deposMonthCount;
    }

    public void setDeposMonthCount(Integer deposMonthCount) {
        this.deposMonthCount = deposMonthCount;
    }

    public Integer getAlipayStatus() {
        return alipayStatus;
    }

    public void setAlipayStatus(Integer alipayStatus) {
        this.alipayStatus = alipayStatus;
    }

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public String getReservationPhone() {
        return reservationPhone;
    }

    public void setReservationPhone(String reservationPhone) {
        this.reservationPhone = reservationPhone;
    }

    public String getFeeConfigInfo() {
        return feeConfigInfo;
    }

    public void setFeeConfigInfo(String feeConfigInfo) {
        this.feeConfigInfo = feeConfigInfo;
    }

    public String getFeeDesc1() {
        return feeDesc1;
    }

    public void setFeeDesc1(String feeDesc1) {
        this.feeDesc1 = feeDesc1;
    }

    public String getFeeAmt1() {
        return feeAmt1;
    }

    public void setFeeAmt1(String feeAmt1) {
        this.feeAmt1 = feeAmt1;
    }

    public String getFeeDesc2() {
        return feeDesc2;
    }

    public void setFeeDesc2(String feeDesc2) {
        this.feeDesc2 = feeDesc2;
    }

    public String getFeeAmt2() {
        return feeAmt2;
    }

    public void setFeeAmt2(String feeAmt2) {
        this.feeAmt2 = feeAmt2;
    }

    public String getFeeDesc3() {
        return feeDesc3;
    }

    public void setFeeDesc3(String feeDesc3) {
        this.feeDesc3 = feeDesc3;
    }

    public String getFeeAmt3() {
        return feeAmt3;
    }

    public void setFeeAmt3(String feeAmt3) {
        this.feeAmt3 = feeAmt3;
    }

    public String getFeeDesc4() {
        return feeDesc4;
    }

    public void setFeeDesc4(String feeDesc4) {
        this.feeDesc4 = feeDesc4;
    }

    public String getFeeAmt4() {
        return feeAmt4;
    }

    public void setFeeAmt4(String feeAmt4) {
        this.feeAmt4 = feeAmt4;
    }

    public String getFeeDesc5() {
        return feeDesc5;
    }

    public void setFeeDesc5(String feeDesc5) {
        this.feeDesc5 = feeDesc5;
    }

    public String getFeeAmt5() {
        return feeAmt5;
    }

    public void setFeeAmt5(String feeAmt5) {
        this.feeAmt5 = feeAmt5;
    }
}
