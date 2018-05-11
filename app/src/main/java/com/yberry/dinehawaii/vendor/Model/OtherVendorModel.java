package com.yberry.dinehawaii.vendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherVendorModel {

    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

}