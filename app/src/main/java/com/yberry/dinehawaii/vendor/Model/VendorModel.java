package com.yberry.dinehawaii.vendor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VendorModel implements Serializable {
    @SerializedName("vendor_name")
    @Expose
    private String vendorName;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("vendor_favorite_status")
    @Expose
    private String vendorFavoriteStatus;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Override
    public String toString() {
        return "VendorModel{" +
                "vendorName='" + vendorName + '\'' +
                ", businessName='" + businessName + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", vendorFavoriteStatus='" + vendorFavoriteStatus + '\'' +
                '}';
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorFavoriteStatus() {
        return vendorFavoriteStatus;
    }

    public void setVendorFavoriteStatus(String vendorFavoriteStatus) {
        this.vendorFavoriteStatus = vendorFavoriteStatus;
    }
}