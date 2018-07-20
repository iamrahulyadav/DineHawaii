package com.yberry.dinehawaii.Bussiness.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderDetailItemData {

    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("item_customization")
    @Expose
    private String itemCustomization;
    @SerializedName("iteam_message")
    @Expose
    private String iteamMessage;
    @SerializedName("food_name")
    @Expose
    private String foodName;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    @SerializedName("item_status")
    @Expose
    private String item_status;

    public String getFood_area_name() {
        return food_area_name;
    }

    public void setFood_area_name(String food_area_name) {
        this.food_area_name = food_area_name;
    }

    @SerializedName("food_area_name")
    @Expose
    private String food_area_name;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItemCustomization() {
        return itemCustomization;
    }

    public void setItemCustomization(String itemCustomization) {
        this.itemCustomization = itemCustomization;
    }

    public String getIteamMessage() {
        return iteamMessage;
    }

    public void setIteamMessage(String iteamMessage) {
        this.iteamMessage = iteamMessage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "OrderDetailItemData{" +
                "quantity='" + quantity + '\'' +
                ", itemCustomization='" + itemCustomization + '\'' +
                ", iteamMessage='" + iteamMessage + '\'' +
                ", foodName='" + foodName + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                ", food_area_name='" + food_area_name + '\'' +
                ", item_status='" + item_status + '\'' +
                '}';
    }
}