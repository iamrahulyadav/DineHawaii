package com.yberry.dinehawaii.Model;

/**
 * Created by Peter on 07-Apr-17.
 */

public class MenuDetail {

    String mainMenuName;
    String itemId;
    String itemName;
    String item_cat_id;
    String item_bus_id;
    String item_price;
    String service_type="";
    String item_half_price="";
    String details="";

    public String getItem_half_price() {
        return item_half_price;
    }

    public void setItem_half_price(String item_half_price) {
        this.item_half_price = item_half_price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    String menu;

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    String totalItems;

    public String getMainMenuName() {
        return mainMenuName;
    }

    public void setMainMenuName(String mainMenuName) {
        this.mainMenuName = mainMenuName;
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

    public String getItem_cat_id() {
        return item_cat_id;
    }

    public void setItem_cat_id(String item_cat_id) {
        this.item_cat_id = item_cat_id;
    }

    public String getItem_bus_id() {
        return item_bus_id;
    }

    public void setItem_bus_id(String item_bus_id) {
        this.item_bus_id = item_bus_id;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }


    @Override
    public String toString() {
        return "MenuDetail{" +
                "mainMenuName='" + mainMenuName + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", item_cat_id='" + item_cat_id + '\'' +
                ", item_bus_id='" + item_bus_id + '\'' +
                ", item_price='" + item_price + '\'' +
                ", service_type='" + service_type + '\'' +
                ", item_half_price='" + item_half_price + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
