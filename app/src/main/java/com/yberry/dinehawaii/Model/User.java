package com.yberry.dinehawaii.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MAX on 25-Jan-17.
 */

public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("method")
    public String Method;
    @SerializedName("first_name")
    public String FName;
    @SerializedName("last_name")
    public String LName;
    @SerializedName("email_id")
    public String EmailId;
    @SerializedName("contact_no")
    public String Mobile;
    @SerializedName("country_type")
    public String CountryCode;
    @SerializedName("physical_address")
    public String physicalAddress;
    @SerializedName("address")
    public String Address;
    @SerializedName("city")
    public String City;
    @SerializedName("state")
    public String State;
    @SerializedName("latitude")
    public String Address_Lat;
    @SerializedName("longitude")
    public String Address_Long;
    @SerializedName("user_image")
    public String UserImage;
    @SerializedName("user_id")
    int Id;

    public User() {

    }

    protected User(Parcel in) {
        Id = in.readInt();
        Method = in.readString();
        FName = in.readString();
        LName = in.readString();
        EmailId = in.readString();
        Mobile = in.readString();
        CountryCode = in.readString();
        physicalAddress = in.readString();
        Address = in.readString();
        City = in.readString();
        State = in.readString();
        Address_Lat = in.readString();
        Address_Long = in.readString();
        UserImage = in.readString();
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAddress_Lat() {
        return Address_Lat;
    }

    public void setAddress_Lat(String address_Lat) {
        Address_Lat = address_Lat;
    }

    public String getAddress_Long() {
        return Address_Long;
    }

    public void setAddress_Long(String address_Long) {
        Address_Long = address_Long;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Method);
        dest.writeString(FName);
        dest.writeString(LName);
        dest.writeString(EmailId);
        dest.writeString(Mobile);
        dest.writeString(CountryCode);
        dest.writeString(physicalAddress);
        dest.writeString(Address);
        dest.writeString(City);
        dest.writeString(State);
        dest.writeString(Address_Lat);
        dest.writeString(UserImage);
        dest.writeString(Address_Long);
    }

   /* @SerializedName("user_id")
    int Id;

    @SerializedName("method")
    public String Method;
    @SerializedName("first_name")
    public String FName;
    @SerializedName("last_name")
    public String LName;
    @SerializedName("email_id")
    public String EmailId;
    @SerializedName("contact_no")
    public String Mobile;
    @SerializedName("country_type")
    public String CountryCode;
    @SerializedName("physical_address")
    public String physicalAddress;
    @SerializedName("address")
    public String Address;
    @SerializedName("city")
    public String City;
    @SerializedName("state")
    public String State;

    protected User(Parcel in) {
        Id = in.readInt();
        Method = in.readString();
        FName = in.readString();
        LName = in.readString();
        EmailId = in.readString();
        Mobile = in.readString();
        CountryCode = in.readString();
        Address = in.readString();
        City = in.readString();
        State = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return Id;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }*/
}
