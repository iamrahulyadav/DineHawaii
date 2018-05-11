package com.yberry.dinehawaii.vendor.Model;

import java.util.ArrayList;

public class VendorMasterData {
    String master_vendor_id;
    String master_vendor_name;
    String master_vendor_addedon;
    String master_item_id;
    String master_item_name;
    String sub_vendor_id;
    String sub_vendor_email;
    String sub_vendor_fn;
    String sub_vendor_ln;
    String sub_vendor_categ;
    String sub_vendor_busname;
    String sub_vendor_locality;
    String sub_vendor_contact;
    String other_vendor_name;
    String master_ven_prod_id;
    String master_item_value;
    String master_item_vend_id;
    String master_item_price;
    String master_item_vendor_name;

    public String getMaster_ven_prod_id() {
        return master_ven_prod_id;
    }

    public void setMaster_ven_prod_id(String master_ven_prod_id) {
        this.master_ven_prod_id = master_ven_prod_id;
    }

    public String getMaster_item_value() {
        return master_item_value;
    }

    public void setMaster_item_value(String master_item_value) {
        this.master_item_value = master_item_value;
    }

    public String getMaster_item_vend_id() {
        return master_item_vend_id;
    }

    public void setMaster_item_vend_id(String master_item_vend_id) {
        this.master_item_vend_id = master_item_vend_id;
    }

    public String getMaster_item_price() {
        return master_item_price;
    }

    public void setMaster_item_price(String master_item_price) {
        this.master_item_price = master_item_price;
    }

    public String getMaster_item_vendor_name() {
        return master_item_vendor_name;
    }

    public void setMaster_item_vendor_name(String master_item_vendor_name) {
        this.master_item_vendor_name = master_item_vendor_name;
    }

    ArrayList<OtherVendorModel> other_vendors_list;
    public ArrayList<OtherVendorModel> getOther_vendors_list() {
        return other_vendors_list;
    }

    public void setOther_vendors_list(ArrayList<OtherVendorModel> other_vendors_list) {
        this.other_vendors_list = other_vendors_list;
    }

    public String getOther_vendor_name() {
        return other_vendor_name;
    }

    public void setOther_vendor_name(String other_vendor_name) {
        this.other_vendor_name = other_vendor_name;
    }

    public String getOther_vendor_price() {
        return other_vendor_price;
    }

    public void setOther_vendor_price(String other_vendor_price) {
        this.other_vendor_price = other_vendor_price;
    }

    String other_vendor_price;
    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    boolean isSeleted=false;
    public String getSub_vendor_contact() {
        return sub_vendor_contact;
    }

    public void setSub_vendor_contact(String sub_vendor_contact) {
        this.sub_vendor_contact = sub_vendor_contact;
    }

    public String getSub_vendor_id() {
        return sub_vendor_id;
    }

    public void setSub_vendor_id(String sub_vendor_id) {
        this.sub_vendor_id = sub_vendor_id;
    }

    public String getSub_vendor_email() {
        return sub_vendor_email;
    }

    public void setSub_vendor_email(String sub_vendor_email) {
        this.sub_vendor_email = sub_vendor_email;
    }

    public String getSub_vendor_fn() {
        return sub_vendor_fn;
    }

    public void setSub_vendor_fn(String sub_vendor_fn) {
        this.sub_vendor_fn = sub_vendor_fn;
    }

    public String getSub_vendor_ln() {
        return sub_vendor_ln;
    }

    public void setSub_vendor_ln(String sub_vendor_ln) {
        this.sub_vendor_ln = sub_vendor_ln;
    }

    public String getSub_vendor_categ() {
        return sub_vendor_categ;
    }

    public void setSub_vendor_categ(String sub_vendor_categ) {
        this.sub_vendor_categ = sub_vendor_categ;
    }

    public String getSub_vendor_busname() {
        return sub_vendor_busname;
    }

    public void setSub_vendor_busname(String sub_vendor_busname) {
        this.sub_vendor_busname = sub_vendor_busname;
    }

    public String getSub_vendor_locality() {
        return sub_vendor_locality;
    }

    public void setSub_vendor_locality(String sub_vendor_locality) {
        this.sub_vendor_locality = sub_vendor_locality;
    }

    public String getMaster_item_id() {
        return master_item_id;
    }

    public void setMaster_item_id(String master_item_id) {
        this.master_item_id = master_item_id;
    }

    public String getMaster_item_name() {
        return master_item_name;
    }

    public void setMaster_item_name(String master_item_name) {
        this.master_item_name = master_item_name;
    }

    public String getMaster_vendor_name() {
        return master_vendor_name;
    }

    public void setMaster_vendor_name(String master_vendor_name) {
        this.master_vendor_name = master_vendor_name;
    }

    public String getMaster_vendor_id() {
        return master_vendor_id;
    }

    public void setMaster_vendor_id(String master_vendor_id) {
        this.master_vendor_id = master_vendor_id;
    }

    public String getMaster_vendor_addedon() {
        return master_vendor_addedon;
    }

    public void setMaster_vendor_addedon(String master_vendor_addedon) {
        this.master_vendor_addedon = master_vendor_addedon;
    }


    @Override
    public String toString() {
        return "VendorMasterData{" +
                "master_vendor_id='" + master_vendor_id + '\'' +
                ", master_vendor_name='" + master_vendor_name + '\'' +
                ", master_vendor_addedon='" + master_vendor_addedon + '\'' +
                ", master_item_id='" + master_item_id + '\'' +
                ", master_item_name='" + master_item_name + '\'' +
                ", sub_vendor_id='" + sub_vendor_id + '\'' +
                ", sub_vendor_email='" + sub_vendor_email + '\'' +
                ", sub_vendor_fn='" + sub_vendor_fn + '\'' +
                ", sub_vendor_ln='" + sub_vendor_ln + '\'' +
                ", sub_vendor_categ='" + sub_vendor_categ + '\'' +
                ", sub_vendor_busname='" + sub_vendor_busname + '\'' +
                ", sub_vendor_locality='" + sub_vendor_locality + '\'' +
                ", sub_vendor_contact='" + sub_vendor_contact + '\'' +
                ", other_vendor_name='" + other_vendor_name + '\'' +
                ", master_ven_prod_id='" + master_ven_prod_id + '\'' +
                ", master_item_value='" + master_item_value + '\'' +
                ", master_item_vend_id='" + master_item_vend_id + '\'' +
                ", master_item_price='" + master_item_price + '\'' +
                ", master_item_vendor_name='" + master_item_vendor_name + '\'' +
                ", other_vendors_list=" + other_vendors_list +
                ", other_vendor_price='" + other_vendor_price + '\'' +
                ", isSeleted=" + isSeleted +
                '}';
    }
}
