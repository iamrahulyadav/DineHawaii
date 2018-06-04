package com.yberry.dinehawaii.vendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BidDetailsModel {
    @SerializedName("bid_row_id")
    @Expose
    private String bidRowId;
    @SerializedName("bid_id")
    @Expose
    private String bidId;
    @SerializedName("bid_status")
    @Expose
    private String bidStatus;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;

    @Override
    public String toString() {
        return "BidDetailsModel{" +
                "bidRowId='" + bidRowId + '\'' +
                ", bidId='" + bidId + '\'' +
                ", bidStatus='" + bidStatus + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemAmount='" + itemAmount + '\'' +
                ", itemId='" + itemId + '\'' +
                ", vendorProductId='" + vendorProductId + '\'' +
                ", itemQuantity='" + itemQuantity + '\'' +
                ", businessBidAmt='" + businessBidAmt + '\'' +
                ", vendorBidAmount='" + vendorBidAmount + '\'' +
                ", vendorBidFinalAmount='" + vendorBidFinalAmount + '\'' +
                '}';
    }

    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_amount")
    @Expose
    private String itemAmount;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("vendor_product_id")
    @Expose
    private String vendorProductId;
    @SerializedName("item_quantity")
    @Expose
    private String itemQuantity;
    @SerializedName("business_bid_amt")
    @Expose
    private String businessBidAmt;
    @SerializedName("vendor_bid_amount")
    @Expose
    private String vendorBidAmount;
    @SerializedName("vendor_bid_final_amount")
    @Expose
    private String vendorBidFinalAmount;

    public String getBidRowId() {
        return bidRowId;
    }

    public void setBidRowId(String bidRowId) {
        this.bidRowId = bidRowId;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getBidStatus() {
        return bidStatus;
    }

    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(String vendorProductId) {
        this.vendorProductId = vendorProductId;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getBusinessBidAmt() {
        return businessBidAmt;
    }

    public void setBusinessBidAmt(String businessBidAmt) {
        this.businessBidAmt = businessBidAmt;
    }

    public String getVendorBidAmount() {
        return vendorBidAmount;
    }

    public void setVendorBidAmount(String vendorBidAmount) {
        this.vendorBidAmount = vendorBidAmount;
    }

    public String getVendorBidFinalAmount() {
        return vendorBidFinalAmount;
    }

    public void setVendorBidFinalAmount(String vendorBidFinalAmount) {
        this.vendorBidFinalAmount = vendorBidFinalAmount;
    }

}
