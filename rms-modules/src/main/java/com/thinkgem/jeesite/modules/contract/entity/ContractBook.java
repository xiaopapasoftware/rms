/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.person.entity.Customer;

import java.util.Date;
import java.util.List;

/**
 * 预约、预定、签约等都可能会用到的实体对象
 * 
 * @author huangsc
 * @author wangshujin
 */
public class ContractBook extends DataEntity<ContractBook> {
  private static final long serialVersionUID = 1L;
  private Customer customer;
  private String houseId; // house_id
  private String roomId; // room_id
  private String bookPhone; // user_phone
  private Date bookDate; // book_date
  private String bookStatus; // book_status
  private String salesId; // sales_id
  private String source;
  private String housingCode;
  private Integer housingType;

  private String projectName;
  private String buildingName;
  private String houseNo;
  private String roomNo;
  private String serviceUserName;
  private String salesName;
  private House house;
  private Room room;
  private List<String> customerIdList;
  private List<String> idList;

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getHouseId() {
    return houseId;
  }

  public void setHouseId(String houseId) {
    this.houseId = houseId;
  }

  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public String getBookPhone() {
    return bookPhone;
  }

  public void setBookPhone(String bookPhone) {
    this.bookPhone = bookPhone;
  }

  public Date getBookDate() {
    return bookDate;
  }

  public void setBookDate(Date bookDate) {
    this.bookDate = bookDate;
  }

  public String getBookStatus() {
    return bookStatus;
  }

  public void setBookStatus(String bookStatus) {
    this.bookStatus = bookStatus;
  }

  public String getSalesId() {
    return salesId;
  }

  public void setSalesId(String salesId) {
    this.salesId = salesId;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getBuildingName() {
    return buildingName;
  }

  public void setBuildingName(String buildingName) {
    this.buildingName = buildingName;
  }

  public String getHouseNo() {
    return houseNo;
  }

  public void setHouseNo(String houseNo) {
    this.houseNo = houseNo;
  }

  public String getRoomNo() {
    return roomNo;
  }

  public void setRoomNo(String roomNo) {
    this.roomNo = roomNo;
  }

  public String getServiceUserName() {
    return serviceUserName;
  }

  public void setServiceUserName(String serviceUserName) {
    this.serviceUserName = serviceUserName;
  }

  public String getSalesName() {
    return salesName;
  }

  public void setSalesName(String salesName) {
    this.salesName = salesName;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getHousingCode() {
    return housingCode;
  }

  public void setHousingCode(String housingCode) {
    this.housingCode = housingCode;
  }

  public Integer getHousingType() {
    return housingType;
  }

  public void setHousingType(Integer housingType) {
    this.housingType = housingType;
  }

  public House getHouse() {
    return house;
  }

  public void setHouse(House house) {
    this.house = house;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public List<String> getCustomerIdList() {
    return customerIdList;
  }

  public void setCustomerIdList(List<String> customerIdList) {
    this.customerIdList = customerIdList;
  }

  public List<String> getIdList() {
    return idList;
  }

  public void setIdList(List<String> idList) {
    this.idList = idList;
  }
}
