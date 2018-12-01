package com.yberry.dinehawaii.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LIstItemAlternate {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("avgPrice")
    @Expose
    private String avgPrice;
    @SerializedName("reservation_price")
    @Expose
    private String reservationPrice;
    @SerializedName("healthCardStstus")
    @Expose
    private String healthCardStstus;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("logoImg")
    @Expose
    private String logoImg;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("business_package")
    @Expose
    private String businessPackage;
    @SerializedName("business_option")
    @Expose
    private String businessOption;
    @SerializedName("business_address")
    @Expose
    private String businessAddress;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("business_contact_no")
    @Expose
    private String businessContactNo;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("table_name")
    @Expose
    private String tableName;
    @SerializedName("table_id")
    @Expose
    private String tableId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(String reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public String getHealthCardStstus() {
        return healthCardStstus;
    }

    public void setHealthCardStstus(String healthCardStstus) {
        this.healthCardStstus = healthCardStstus;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessPackage() {
        return businessPackage;
    }

    public void setBusinessPackage(String businessPackage) {
        this.businessPackage = businessPackage;
    }

    public String getBusinessOption() {
        return businessOption;
    }

    public void setBusinessOption(String businessOption) {
        this.businessOption = businessOption;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBusinessContactNo() {
        return businessContactNo;
    }

    public void setBusinessContactNo(String businessContactNo) {
        this.businessContactNo = businessContactNo;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }


    @Override
    public String toString() {
        return "LIstItemAlternate{" +
                "id='" + id + '\'' +
                ", avgPrice='" + avgPrice + '\'' +
                ", reservationPrice='" + reservationPrice + '\'' +
                ", healthCardStstus='" + healthCardStstus + '\'' +
                ", distance=" + distance +
                ", coverImage='" + coverImage + '\'' +
                ", logoImg='" + logoImg + '\'' +
                ", businessName='" + businessName + '\'' +
                ", businessPackage='" + businessPackage + '\'' +
                ", businessOption='" + businessOption + '\'' +
                ", businessAddress='" + businessAddress + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", businessContactNo='" + businessContactNo + '\'' +
                ", rating='" + rating + '\'' +
                ", type='" + type + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableId='" + tableId + '\'' +
                '}';
    }
}