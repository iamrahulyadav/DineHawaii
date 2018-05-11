package com.yberry.dinehawaii.vendor.Model;

import java.util.ArrayList;

/**
 * Created by Peter on 11-Apr-17.
 */

public class VendorBidItemModel {

    String id = "", product_id = "", item_id = "", item_name = "", vendor_id = "", vendor_name = "", vendor_item_price = "", vendor_item_qty = "", vendor_item_total_cost = "";
    ArrayList<VendorBidItemModel> other_vendors_list;
    private boolean isSelected = false;

    public ArrayList<VendorBidItemModel> getOther_vendors_list() {
        return other_vendors_list;
    }

    public void setOther_vendors_list(ArrayList<VendorBidItemModel> other_vendors_list) {
        this.other_vendors_list = other_vendors_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getVendor_item_price() {
        return vendor_item_price;
    }

    public void setVendor_item_price(String vendor_item_price) {
        this.vendor_item_price = vendor_item_price;
    }

    public String getVendor_item_qty() {
        return vendor_item_qty;
    }

    public void setVendor_item_qty(String vendor_item_qty) {
        this.vendor_item_qty = vendor_item_qty;
    }

    public String getVendor_item_total_cost() {
        return vendor_item_total_cost;
    }

    public void setVendor_item_total_cost(String vendor_item_total_cost) {
        this.vendor_item_total_cost = vendor_item_total_cost;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public String toString() {
        return "VendorBidItemModel{" +
                "id='" + id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", item_id='" + item_id + '\'' +
                ", item_name='" + item_name + '\'' +
                ", vendor_id='" + vendor_id + '\'' +
                ", vendor_name='" + vendor_name + '\'' +
                ", vendor_item_price='" + vendor_item_price + '\'' +
                ", vendor_item_qty='" + vendor_item_qty + '\'' +
                ", vendor_item_total_cost='" + vendor_item_total_cost + '\'' +
                ", other_vendors_list=" + other_vendors_list +
                ", isSelected=" + isSelected +
                '}';
    }
}