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
    private String vendorBusName;
    @SerializedName("contact_no")
    @Expose
    private String vendorContact;
    @SerializedName("business_address")
    @Expose
    private String vendorBusAddress;
    @SerializedName("added_on")
    @Expose
    private String vendorAddedOn;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;


    @Override
    public String toString() {
        return "VendorModel{" +
                "vendorName='" + vendorName + '\'' +
                ", vendorBusName='" + vendorBusName + '\'' +
                ", vendorContact='" + vendorContact + '\'' +
                ", vendorBusAddress='" + vendorBusAddress + '\'' +
                ", vendorAddedOn='" + vendorAddedOn + '\'' +
                ", vendorId='" + vendorId + '\'' +
                '}';
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

    public String getVendorBusAddress() {
        return vendorBusAddress;
    }

    public void setVendorBusAddress(String vendorBusAddress) {
        this.vendorBusAddress = vendorBusAddress;
    }

    public String getVendorContact() {
        return vendorContact;
    }

    public void setVendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
    }

    public String getVendorBusName() {
        return vendorBusName;
    }

    public void setVendorBusName(String vendorBusName) {
        this.vendorBusName = vendorBusName;
    }

    public String getVendorAddedOn() {
        return vendorAddedOn;
    }

    public void setVendorAddedOn(String vendorAddedOn) {
        this.vendorAddedOn = vendorAddedOn;
    }

}