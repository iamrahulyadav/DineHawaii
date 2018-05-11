package com.yberry.dinehawaii.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by PRINCE 9977123453 on 08-02-17./
 */

public class CheckBoxPositionModel implements Parcelable {

    private String id;
    private String name;
    private String type;
    private String amount="0";
    private String package_detail;
    private boolean chckStatus;
    private String food_name;
    private String dish_name;
    private String food_ctagory_id;
    private boolean isSelected;
    private String totalOptionAmount;
    private String totalPackageAmount;
    private boolean selected;
    private String edit_status;

    public String getEdit_status() {
        return edit_status;
    }

    public void setEdit_status(String edit_status) {
        this.edit_status = edit_status;
    }

    ArrayList<String> foodNameList;

    public ArrayList<String> getFoodNameList() {
        return foodNameList;
    }

    public void setFoodNameList(ArrayList<String> foodNameList) {
        this.foodNameList = foodNameList;
    }

    public static Creator<CheckBoxPositionModel> getCREATOR() {
        return CREATOR;
    }

    public CheckBoxPositionModel(){}
    protected CheckBoxPositionModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        type = in.readString();
        amount = in.readString();
        chckStatus = in.readByte() != 0;
        food_name = in.readString();
        service_name = in.readString();
        edit_status = in.readString();
    }

    public static final Creator<CheckBoxPositionModel> CREATOR = new Creator<CheckBoxPositionModel>() {
        @Override
        public CheckBoxPositionModel createFromParcel(Parcel in) {
            return new CheckBoxPositionModel(in);
        }

        @Override
        public CheckBoxPositionModel[] newArray(int size) {
            return new CheckBoxPositionModel[size];
        }
    };

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    private String service_name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isChckStatus() {
        return chckStatus;
    }

    public void setChckStatus(boolean chckStatus) {
        this.chckStatus = chckStatus;
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public String getFood_ctagory_id() {
        return food_ctagory_id;
    }

    public void setFood_ctagory_id(String food_ctagory_id) {
        this.food_ctagory_id = food_ctagory_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTotalOptionAmount() {
        return totalOptionAmount;
    }

    public void setTotalOptionAmount(String totalOptionAmount) {
        this.totalOptionAmount = totalOptionAmount;
    }

    public String getTotalPackageAmount() {
        return totalPackageAmount;
    }

    public void setTotalPackageAmount(String totalPackageAmount) {
        this.totalPackageAmount = totalPackageAmount;
    }

    public String getPackage_detail() {
        return package_detail;
    }

    public void setPackage_detail(String package_detail) {
        this.package_detail = package_detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(amount);
        parcel.writeByte((byte) (chckStatus ? 1 : 0));
        parcel.writeString(food_name);
        parcel.writeString(service_name);
        parcel.writeString(edit_status);
    }

    @Override
    public String toString() {
        return "{" +
                "id:'" + id + '\'' +
                ", name:'" + name + '\'' +
                ", type:'" + type + '\'' +
                ", amount:'" + amount + '\'' +
                ", chckStatus:" + chckStatus +
                ", food_name:'" + food_name + '\'' +
                ", service_name:'" + service_name + '\'' +
                ", edit_status:'" + edit_status + '\'' +
                '}';
    }

}
