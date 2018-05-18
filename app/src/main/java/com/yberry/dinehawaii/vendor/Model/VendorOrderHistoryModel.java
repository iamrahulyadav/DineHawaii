package com.yberry.dinehawaii.vendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorOrderHistoryModel {

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
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_quantity")
    @Expose
    private String itemQuantity;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

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
}
