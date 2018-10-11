package com.yberry.dinehawaii.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListItem implements Parcelable {

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

    @SerializedName("table_id")
    @Expose
    private String table_id;
    @SerializedName("table_name")
    @Expose
    private String table_name;
    private Boolean displayLogo;

    public ListItem() {
    }

    protected ListItem(Parcel in) {
        id = in.readString();
        avgPrice = in.readString();
        reservationPrice = in.readString();
        healthCardStstus = in.readString();
        if (in.readByte() == 0) {
            distance = null;
        } else {
            distance = in.readDouble();
        }
        coverImage = in.readString();
        logoImg = in.readString();
        businessName = in.readString();
        businessPackage = in.readString();
        businessOption = in.readString();
        businessAddress = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        businessContactNo = in.readString();
        rating = in.readString();
        type = in.readString();
        table_id = in.readString();
        table_name = in.readString();
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(avgPrice);
        parcel.writeString(reservationPrice);
        parcel.writeString(healthCardStstus);
        if (distance == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(distance);
        }
        parcel.writeString(coverImage);
        parcel.writeString(logoImg);
        parcel.writeString(businessName);
        parcel.writeString(businessPackage);
        parcel.writeString(businessOption);
        parcel.writeString(businessAddress);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(businessContactNo);
        parcel.writeString(rating);
        parcel.writeString(type);
        parcel.writeString(table_id);
        parcel.writeString(table_name);
    }

    @Override
    public String toString() {
        return "ListItem{" +
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
                ", table_id='" + table_id + '\'' +
                ", table_name='" + table_name + '\'' +
                '}';
    }

    public Boolean getDisplayLogo() {
        return displayLogo;
    }

    public void setDisplayLogo(Boolean displayLogo) {
        this.displayLogo = displayLogo;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
}