package com.yberry.dinehawaii.vendor.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorOrderDetailItem implements Parcelable {
    @SerializedName("vendor_product_id")
    @Expose
    private String vendorProductId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_name")
    @Expose
    private String item_name;
    @SerializedName("item_quantity")
    @Expose
    private String itemQuantity;
    @SerializedName("item_amount")
    @Expose
    private String itemAmount;

    public String getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(String vendorProductId) {
        this.vendorProductId = vendorProductId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.vendorProductId);
        dest.writeString(this.itemId);
        dest.writeString(this.item_name);
        dest.writeString(this.itemQuantity);
        dest.writeString(this.itemAmount);
    }

    public VendorOrderDetailItem() {
    }

    protected VendorOrderDetailItem(Parcel in) {
        this.vendorProductId = in.readString();
        this.itemId = in.readString();
        this.item_name = in.readString();
        this.itemQuantity = in.readString();
        this.itemAmount = in.readString();
    }

    public static final Parcelable.Creator<VendorOrderDetailItem> CREATOR = new Parcelable.Creator<VendorOrderDetailItem>() {
        @Override
        public VendorOrderDetailItem createFromParcel(Parcel source) {
            return new VendorOrderDetailItem(source);
        }

        @Override
        public VendorOrderDetailItem[] newArray(int size) {
            return new VendorOrderDetailItem[size];
        }
    };

    @Override
    public String toString() {
        return "VendorOrderDetailItem{" +
                "vendorProductId='" + vendorProductId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", item_name='" + item_name + '\'' +
                ", itemQuantity='" + itemQuantity + '\'' +
                ", itemAmount='" + itemAmount + '\'' +
                '}';
    }
}
