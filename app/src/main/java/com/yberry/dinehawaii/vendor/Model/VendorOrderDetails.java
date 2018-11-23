package com.yberry.dinehawaii.vendor.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VendorOrderDetails implements Parcelable {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("order_unique_id")
    @Expose
    private String orderUniqueId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("vendor_contact_no")
    @Expose
    private String vendorContactNo;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_quantity")
    @Expose
    private String itemQuantity;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("order_frequency")
    @Expose
    private String orderFrequency;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("sub_total")
    @Expose
    private Double subTotal;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("delivery_fee")
    @Expose
    private Double deliveryFee;
    @SerializedName("ge_tax")
    @Expose
    private Double geTax;
    @SerializedName("orderDetails")
    @Expose
    private List<VendorOrderDetailItem> orderDetails = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderUniqueId() {
        return orderUniqueId;
    }

    public void setOrderUniqueId(String orderUniqueId) {
        this.orderUniqueId = orderUniqueId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorContactNo() {
        return vendorContactNo;
    }

    public void setVendorContactNo(String vendorContactNo) {
        this.vendorContactNo = vendorContactNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderFrequency() {
        return orderFrequency;
    }

    public void setOrderFrequency(String orderFrequency) {
        this.orderFrequency = orderFrequency;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getGeTax() {
        return geTax;
    }

    public void setGeTax(Double geTax) {
        this.geTax = geTax;
    }

    public List<VendorOrderDetailItem> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<VendorOrderDetailItem> orderDetails) {
        this.orderDetails = orderDetails;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.orderUniqueId);
        dest.writeString(this.amount);
        dest.writeString(this.vendorName);
        dest.writeString(this.vendorContactNo);
        dest.writeString(this.itemName);
        dest.writeString(this.itemQuantity);
        dest.writeString(this.dateTime);
        dest.writeString(this.deliveryDate);
        dest.writeString(this.orderType);
        dest.writeString(this.orderFrequency);
        dest.writeString(this.remark);
        dest.writeString(this.paymentStatus);
        dest.writeValue(this.subTotal);
        dest.writeString(this.totalAmount);
        dest.writeValue(this.deliveryFee);
        dest.writeValue(this.geTax);
        dest.writeTypedList(this.orderDetails);
    }

    public VendorOrderDetails() {
    }

    protected VendorOrderDetails(Parcel in) {
        this.orderId = in.readString();
        this.orderUniqueId = in.readString();
        this.amount = in.readString();
        this.vendorName = in.readString();
        this.vendorContactNo = in.readString();
        this.itemName = in.readString();
        this.itemQuantity = in.readString();
        this.dateTime = in.readString();
        this.deliveryDate = in.readString();
        this.orderType = in.readString();
        this.orderFrequency = in.readString();
        this.remark = in.readString();
        this.paymentStatus = in.readString();
        this.subTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.totalAmount = in.readString();
        this.deliveryFee = (Double) in.readValue(Double.class.getClassLoader());
        this.geTax = (Double) in.readValue(Double.class.getClassLoader());
        this.orderDetails = in.createTypedArrayList(VendorOrderDetailItem.CREATOR);
    }

    public static final Creator<VendorOrderDetails> CREATOR = new Creator<VendorOrderDetails>() {
        @Override
        public VendorOrderDetails createFromParcel(Parcel source) {
            return new VendorOrderDetails(source);
        }

        @Override
        public VendorOrderDetails[] newArray(int size) {
            return new VendorOrderDetails[size];
        }
    };

    @Override
    public String toString() {
        return "VendorOrderDetails{" +
                "orderId='" + orderId + '\'' +
                ", orderUniqueId='" + orderUniqueId + '\'' +
                ", amount='" + amount + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", vendorContactNo='" + vendorContactNo + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemQuantity='" + itemQuantity + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderFrequency='" + orderFrequency + '\'' +
                ", remark='" + remark + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", subTotal=" + subTotal +
                ", totalAmount='" + totalAmount + '\'' +
                ", deliveryFee=" + deliveryFee +
                ", geTax=" + geTax +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
