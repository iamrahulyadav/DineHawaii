package com.yberry.dinehawaii.vendor.Model;

public class VendorMasterData {
    String master_vendor_id;
    String master_vendor_name;
    String master_vendor_addedon;
    String sub_vendor_id;
    String sub_vendor_email;
    String sub_vendor_fn;
    String sub_vendor_ln;
    String sub_vendor_categ;
    String sub_vendor_busname;
    String sub_vendor_locality;
    String sub_vendor_contact;
    String vendor_assign_status;
    String favorite_del_status;

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

    public String getMaster_vendor_name() {
        return master_vendor_name;
    }

    @Override
    public String toString() {
        return "VendorMasterData{" +
                "master_vendor_id='" + master_vendor_id + '\'' +
                ", master_vendor_name='" + master_vendor_name + '\'' +
                ", master_vendor_addedon='" + master_vendor_addedon + '\'' +
                ", sub_vendor_id='" + sub_vendor_id + '\'' +
                ", sub_vendor_email='" + sub_vendor_email + '\'' +
                ", sub_vendor_fn='" + sub_vendor_fn + '\'' +
                ", sub_vendor_ln='" + sub_vendor_ln + '\'' +
                ", sub_vendor_categ='" + sub_vendor_categ + '\'' +
                ", sub_vendor_busname='" + sub_vendor_busname + '\'' +
                ", sub_vendor_locality='" + sub_vendor_locality + '\'' +
                ", sub_vendor_contact='" + sub_vendor_contact + '\'' +
                ", vendor_assign_status='" + vendor_assign_status + '\'' +
                ", favorite_del_status='" + favorite_del_status + '\'' +
                '}';
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


    public String getFavorite_del_status() {
        return favorite_del_status;
    }

    public void setFavorite_del_status(String favorite_del_status) {
        this.favorite_del_status = favorite_del_status;
    }

    public String getVendor_assign_status() {
        return vendor_assign_status;
    }

    public void setVendor_assign_status(String vendor_assign_status) {
        this.vendor_assign_status = vendor_assign_status;
    }
}
