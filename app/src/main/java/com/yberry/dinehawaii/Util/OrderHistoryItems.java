package com.yberry.dinehawaii.Util;

/**
 * Created by Hvantage2 on 2018-02-12.
 */

public class OrderHistoryItems {
    String item,qty,price;

    @Override
    public String toString() {
        return "Item{" +
                "item='" + item + '\'' +
                ", qty='" + qty + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public OrderHistoryItems(String item, String qty, String price) {
        this.item = item;
        this.qty = qty;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

}
