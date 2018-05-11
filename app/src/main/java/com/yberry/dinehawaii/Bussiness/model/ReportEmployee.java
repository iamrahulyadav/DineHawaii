package com.yberry.dinehawaii.Bussiness.model;

/**
 * Created by abc on 8/11/2017.
 */

public class ReportEmployee {


    String business_name;
    String dine_app_id;
    String added_by;
    String email;
    String mobile;
    String name;
    String posiiton;
    String date;
    String time;
    String party_size;
    String table_name;
    String total_prcie;
    String delivery_adderess;
    String delivery_contact_no;
    String delivery_name;
    String food_name;
    String status;


    @Override
    public String toString() {
        return "ReportEmployee{" +
                "business_name='" + business_name + '\'' +
                ", dine_app_id='" + dine_app_id + '\'' +
                ", added_by='" + added_by + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", posiiton='" + posiiton + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", party_size='" + party_size + '\'' +
                ", table_name='" + table_name + '\'' +
                ", total_prcie='" + total_prcie + '\'' +
                ", delivery_adderess='" + delivery_adderess + '\'' +
                ", delivery_contact_no='" + delivery_contact_no + '\'' +
                ", delivery_name='" + delivery_name + '\'' +
                ", food_name='" + food_name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }



    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getDine_app_id() {
        return dine_app_id;
    }

    public void setDine_app_id(String dine_app_id) {
        this.dine_app_id = dine_app_id;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosiiton() {
        return posiiton;
    }

    public void setPosiiton(String posiiton) {
        this.posiiton = posiiton;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getParty_size() {
        return party_size;
    }

    public void setParty_size(String party_size) {
        this.party_size = party_size;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTotal_prcie() {
        return total_prcie;
    }

    public void setTotal_prcie(String total_prcie) {
        this.total_prcie = total_prcie;
    }

    public String getDelivery_adderess() {
        return delivery_adderess;
    }

    public void setDelivery_adderess(String delivery_adderess) {
        this.delivery_adderess = delivery_adderess;
    }

    public String getDelivery_contact_no() {
        return delivery_contact_no;
    }

    public void setDelivery_contact_no(String delivery_contact_no) {
        this.delivery_contact_no = delivery_contact_no;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
