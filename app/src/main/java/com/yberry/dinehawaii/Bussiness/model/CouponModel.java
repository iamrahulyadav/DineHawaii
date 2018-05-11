package com.yberry.dinehawaii.Bussiness.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CouponModel implements Serializable{

    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("business_id")
    @Expose
    private String businessId;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("coupon_type")
    @Expose
    private String couponType;
    @SerializedName("coupon_title")
    @Expose
    private String couponTitle;
    @SerializedName("coupon_amt_per")
    @Expose
    private String couponAmtPer;
    @SerializedName("coupon_description")
    @Expose
    private String couponDescription;
    @SerializedName("coupon_s_date")
    @Expose
    private String couponSDate;

    @SerializedName("coupon_e_date")
    @Expose
    private String couponEDate;

    @SerializedName("min_order_amt")
    @Expose
    private String minOrderAmt;

    @SerializedName("usage_per_user")
    @Expose
    private String usagPerUser;

    public String getMinOrderAmt() {
        return minOrderAmt;
    }

    public void setMinOrderAmt(String minOrderAmt) {
        this.minOrderAmt = minOrderAmt;
    }

    public String getUsagPerUser() {
        return usagPerUser;
    }

    public void setUsagPerUser(String usagPerUser) {
        this.usagPerUser = usagPerUser;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponAmtPer() {
        return couponAmtPer;
    }

    public void setCouponAmtPer(String couponAmtPer) {
        this.couponAmtPer = couponAmtPer;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getCouponSDate() {
        return couponSDate;
    }

    public void setCouponSDate(String couponSDate) {
        this.couponSDate = couponSDate;
    }

    public String getCouponEDate() {
        return couponEDate;
    }

    public void setCouponEDate(String couponEDate) {
        this.couponEDate = couponEDate;
    }

    @Override
    public String toString() {
        return "CouponModel{" +
                "couponId='" + couponId + '\'' +
                ", businessId='" + businessId + '\'' +
                ", couponCode='" + couponCode + '\'' +
                ", couponType='" + couponType + '\'' +
                ", couponTitle='" + couponTitle + '\'' +
                ", couponAmtPer='" + couponAmtPer + '\'' +
                ", couponDescription='" + couponDescription + '\'' +
                ", couponSDate='" + couponSDate + '\'' +
                ", couponEDate='" + couponEDate + '\'' +
                ", minOrderAmt='" + minOrderAmt + '\'' +
                ", usagPerUser='" + usagPerUser + '\'' +
                '}';
    }
}