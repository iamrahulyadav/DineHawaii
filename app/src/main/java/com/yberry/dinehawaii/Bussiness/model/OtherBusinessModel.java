package com.yberry.dinehawaii.Bussiness.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherBusinessModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("business_email")
    @Expose
    private String businessEmail;
    @SerializedName("business_contact_no")
    @Expose
    private String businessContactNo;
    @SerializedName("business_logo")
    @Expose
    private String businessLogo;
    @SerializedName("business_address")
    @Expose
    private String businessAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getBusinessContactNo() {
        return businessContactNo;
    }

    public void setBusinessContactNo(String businessContactNo) {
        this.businessContactNo = businessContactNo;
    }

    public String getBusinessLogo() {
        return businessLogo;
    }

    public void setBusinessLogo(String businessLogo) {
        this.businessLogo = businessLogo;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

}