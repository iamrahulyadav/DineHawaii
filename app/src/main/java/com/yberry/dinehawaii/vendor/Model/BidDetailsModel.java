package com.yberry.dinehawaii.vendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BidDetailsModel {
    @SerializedName("bid_id")
    @Expose
    private String bidId;
    @SerializedName("bid_status")
    @Expose
    private String bidStatus;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("item_name")
    @Expose
    private String itemName;
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
