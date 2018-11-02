package com.yberry.dinehawaii.Bussiness.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class OrderDetails implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("order_added_by")
    @Expose
    private String orderAddedBy;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("delivery_adderess")
    @Expose
    private String deliveryAdderess;
    @SerializedName("delivery_name")
    @Expose
    private String deliveryName;
    @SerializedName("delivery_contact_no")
    @Expose
    private String deliveryContactNo;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("wallet_amt")
    @Expose
    private String walletAmt;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("food_name")
    @Expose
    private String foodName;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("due_time")
    @Expose
    private String dueTime;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("customization")
    @Expose
    private String customization;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("loyalty_points")
    @Expose
    private String loyaltyPoints;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("e_gift_code")
    @Expose
    private String EGiftCode;
    @SerializedName("e_gift_amount")
    @Expose
    private String EGiftAmount;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("coupon_amount")
    @Expose
    private String couponAmount;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("vendor_business_name")
    @Expose
    private String vendorBusinessName;
    @SerializedName("vendor_contact_no")
    @Expose
    private String vendorContactNo;
    @SerializedName("order_assign_status")
    @Expose
    private String orderAssignStatus;
    @SerializedName("vender_assign_status")
    @Expose
    private String venderAssignStatus;
    @SerializedName("driver_assign_status")
    @Expose
    private String driverAssignStatus;
    @SerializedName("bus_lat")
    @Expose
    private String busLat;
    @SerializedName("bus_long")
    @Expose
    private String busLong;
    @SerializedName("cust_lat")
    @Expose
    private String custLat;
    @SerializedName("cust_long")
    @Expose
    private String custLong;
    @SerializedName("bus_address")
    @Expose
    private String busAddress;
    @SerializedName("cust_address")
    @Expose
    private String custAddress;
    @SerializedName("driver_lat")
    @Expose
    private String driverLat;
    @SerializedName("driver_long")
    @Expose
    private String driverLong;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("driver_contact_no")
    @Expose
    private String driverContactNo;
    @SerializedName("driver_id")
    @Expose
    private String driverId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderAddedBy() {
        return orderAddedBy;
    }

    public void setOrderAddedBy(String orderAddedBy) {
        this.orderAddedBy = orderAddedBy;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryAdderess() {
        return deliveryAdderess;
    }

    public void setDeliveryAdderess(String deliveryAdderess) {
        this.deliveryAdderess = deliveryAdderess;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryContactNo() {
        return deliveryContactNo;
    }

    public void setDeliveryContactNo(String deliveryContactNo) {
        this.deliveryContactNo = deliveryContactNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getWalletAmt() {
        return walletAmt;
    }

    public void setWalletAmt(String walletAmt) {
        this.walletAmt = walletAmt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCustomization() {
        return customization;
    }

    public void setCustomization(String customization) {
        this.customization = customization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEGiftCode() {
        return EGiftCode;
    }

    public void setEGiftCode(String EGiftCode) {
        this.EGiftCode = EGiftCode;
    }

    public String getEGiftAmount() {
        return EGiftAmount;
    }

    public void setEGiftAmount(String EGiftAmount) {
        this.EGiftAmount = EGiftAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorBusinessName() {
        return vendorBusinessName;
    }

    public void setVendorBusinessName(String vendorBusinessName) {
        this.vendorBusinessName = vendorBusinessName;
    }

    public String getVendorContactNo() {
        return vendorContactNo;
    }

    public void setVendorContactNo(String vendorContactNo) {
        this.vendorContactNo = vendorContactNo;
    }

    public String getVenderAssignStatus() {
        return venderAssignStatus;
    }

    public void setVenderAssignStatus(String venderAssignStatus) {
        this.venderAssignStatus = venderAssignStatus;
    }

    public String getDriverAssignStatus() {
        return driverAssignStatus;
    }

    public void setDriverAssignStatus(String driverAssignStatus) {
        this.driverAssignStatus = driverAssignStatus;
    }

    public String getBusLat() {
        return busLat;
    }

    public void setBusLat(String busLat) {
        this.busLat = busLat;
    }

    public String getBusLong() {
        return busLong;
    }

    public void setBusLong(String busLong) {
        this.busLong = busLong;
    }

    public String getCustLat() {
        return custLat;
    }

    public void setCustLat(String custLat) {
        this.custLat = custLat;
    }

    public String getCustLong() {
        return custLong;
    }

    public void setCustLong(String custLong) {
        this.custLong = custLong;
    }

    public String getBusAddress() {
        return busAddress;
    }

    public void setBusAddress(String busAddress) {
        this.busAddress = busAddress;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(String driverLat) {
        this.driverLat = driverLat;
    }

    public String getDriverLong() {
        return driverLong;
    }

    public void setDriverLong(String driverLong) {
        this.driverLong = driverLong;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContactNo() {
        return driverContactNo;
    }

    public void setDriverContactNo(String driverContactNo) {
        this.driverContactNo = driverContactNo;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getOrderAssignStatus() {
        return orderAssignStatus;
    }

    public void setOrderAssignStatus(String orderAssignStatus) {
        this.orderAssignStatus = orderAssignStatus;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", date='" + date + '\'' +
                ", orderAddedBy='" + orderAddedBy + '\'' +
                ", businessName='" + businessName + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", deliveryAdderess='" + deliveryAdderess + '\'' +
                ", deliveryName='" + deliveryName + '\'' +
                ", deliveryContactNo='" + deliveryContactNo + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", walletAmt='" + walletAmt + '\'' +
                ", userName='" + userName + '\'' +
                ", foodName='" + foodName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", dueTime='" + dueTime + '\'' +
                ", orderType='" + orderType + '\'' +
                ", customization='" + customization + '\'' +
                ", description='" + description + '\'' +
                ", loyaltyPoints='" + loyaltyPoints + '\'' +
                ", remark='" + remark + '\'' +
                ", EGiftCode='" + EGiftCode + '\'' +
                ", EGiftAmount='" + EGiftAmount + '\'' +
                ", couponCode='" + couponCode + '\'' +
                ", couponAmount='" + couponAmount + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", vendorBusinessName='" + vendorBusinessName + '\'' +
                ", vendorContactNo='" + vendorContactNo + '\'' +
                ", orderAssignStatus='" + orderAssignStatus + '\'' +
                ", venderAssignStatus='" + venderAssignStatus + '\'' +
                ", driverAssignStatus='" + driverAssignStatus + '\'' +
                ", busLat='" + busLat + '\'' +
                ", busLong='" + busLong + '\'' +
                ", custLat='" + custLat + '\'' +
                ", custLong='" + custLong + '\'' +
                ", busAddress='" + busAddress + '\'' +
                ", custAddress='" + custAddress + '\'' +
                ", driverLat='" + driverLat + '\'' +
                ", driverLong='" + driverLong + '\'' +
                ", driverName='" + driverName + '\'' +
                ", driverContactNo='" + driverContactNo + '\'' +
                ", driverId='" + driverId + '\'' +
                '}';
    }
}
