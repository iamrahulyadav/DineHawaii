package com.yberry.dinehawaii.vendor.Model;

/**
 * Created by abc on 8/11/2017.
 */

public class VendorlistDataModel {
    String productId;
    String itemId;
    String itemName;
    String Price;
    String ava_quan;
    String desc;
    String added_on;
    String minimum_vol;
    String maximum_vol;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "VendorlistDataModel{" +
                "productId='" + productId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", Price='" + Price + '\'' +
                ", ava_quan='" + ava_quan + '\'' +
                ", desc='" + desc + '\'' +
                ", added_on='" + added_on + '\'' +
                ", minimum_vol='" + minimum_vol + '\'' +
                ", maximum_vol='" + maximum_vol + '\'' +
                '}';
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAva_quan() {
        return ava_quan;
    }

    public void setAva_quan(String ava_quan) {
        this.ava_quan = ava_quan;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAdded_on() {
        return added_on;
    }

    public void setAdded_on(String added_on) {
        this.added_on = added_on;
    }

    public String getMinimum_vol() {
        return minimum_vol;
    }

    public void setMinimum_vol(String minimum_vol) {
        this.minimum_vol = minimum_vol;
    }

    public String getMaximum_vol() {
        return maximum_vol;
    }

    public void setMaximum_vol(String maximum_vol) {
        this.maximum_vol = maximum_vol;
    }
}
