package com.yberry.dinehawaii.vendor.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Peter on 11-Apr-17.
 */

public class VendorOrderItemsDetailsModel implements Parcelable {

    public static final Creator<VendorOrderItemsDetailsModel> CREATOR = new Creator<VendorOrderItemsDetailsModel>() {
        @Override
        public VendorOrderItemsDetailsModel createFromParcel(Parcel in) {
            return new VendorOrderItemsDetailsModel(in);
        }

        @Override
        public VendorOrderItemsDetailsModel[] newArray(int size) {
            return new VendorOrderItemsDetailsModel[size];
        }
    };
    String itemName;
    String itemId;
    String vendorId;
    String itemPrice;
    String itemQuan;
    String id;
    String itemTotalCost;
    String productId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VendorOrderItemsDetailsModel() {

    }
    public VendorOrderItemsDetailsModel(Parcel in) {
        itemName = in.readString();
        itemId = in.readString();
        vendorId = in.readString();
        itemPrice = in.readString();
        itemQuan = in.readString();
        id = in.readString();
        itemTotalCost = in.readString();
        productId = in.readString();

    }

    public static Creator<VendorOrderItemsDetailsModel> getCREATOR() {
        return CREATOR;
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQuan() {
        return itemQuan;
    }

    public void setItemQuan(String itemQuan) {
        this.itemQuan = itemQuan;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "VendorOrderItemsDetailsModel{" +
                "itemName='" + itemName + '\'' +
                ", itemId='" + itemId + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", itemQuan='" + itemQuan + '\'' +
                ", itemTotalCost='" + itemTotalCost + '\'' +
                ", productId='" + productId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getItemTotalCost() {
        return itemTotalCost;
    }

    public void setItemTotalCost(String itemTotalCost) {
        this.itemTotalCost = itemTotalCost;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeString(itemId);
        dest.writeString(vendorId);
        dest.writeString(itemPrice);
        dest.writeString(itemQuan);
        dest.writeString(itemTotalCost);
        dest.writeString(id);
        dest.writeString(productId);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}