package com.yberry.dinehawaii.Model;

import java.io.Serializable;

/**
 * Created by hvantage3 on 6/30/2017.
 */

public class ReservationDataModel implements Serializable
{
    String id;
    String reservation_id;
    String business_id;
    String business_name;
    String party_size;
    String date;
    String time;
    String service_type1;
    String service_type2;
    String confirm_status;
    String edit_status;
    String delete_status;

    public String getPre_amount() {
        return pre_amount;
    }

    public void setPre_amount(String pre_amount) {
        this.pre_amount = pre_amount;
    }

    String pre_amount;

    public ReservationDataModel() {
    }
    //constructor for current reservation
    public ReservationDataModel(String id, String reservation_id, String business_id, String business_name, String party_size, String date, String time, String service_type1, String service_type2) {
        this.id = id;
        this.reservation_id = reservation_id;
        this.business_id = business_id;
        this.business_name = business_name;
        this.party_size = party_size;
        this.date = date;
        this.time = time;
        this.service_type1 = service_type1;
        this.service_type2 = service_type2;
    }

    //constructor for reservation history
    public ReservationDataModel(String id, String reservation_id, String business_id, String business_name, String party_size, String date, String time, String service_type1, String service_type2, String confirm_status, String edit_status, String delete_status) {
        this.id = id;
        this.reservation_id = reservation_id;
        this.business_id = business_id;
        this.business_name = business_name;
        this.party_size = party_size;
        this.date = date;
        this.time = time;
        this.service_type1 = service_type1;
        this.service_type2 = service_type2;
        this.confirm_status = confirm_status;
        this.edit_status = edit_status;
        this.delete_status = delete_status;
    }


    public String getConfirm_status() {
        return confirm_status;
    }

    public void setConfirm_status(String confirm_status) {
        this.confirm_status = confirm_status;
    }

    public String getEdit_status() {
        return edit_status;
    }

    public void setEdit_status(String edit_status) {
        this.edit_status = edit_status;
    }

    public String getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(String delete_status) {
        this.delete_status = delete_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getParty_size() {
        return party_size;
    }

    public void setParty_size(String party_size) {
        this.party_size = party_size;
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

    public String getService_type1() {
        return service_type1;
    }

    public void setService_type1(String service_type1) {
        this.service_type1 = service_type1;
    }

    public String getService_type2() {
        return service_type2;
    }

    public void setService_type2(String service_type2) {
        this.service_type2 = service_type2;
    }

    @Override
    public String toString() {
        return "ReservationDataModel{" +
                "id='" + id + '\'' +
                ", reservation_id='" + reservation_id + '\'' +
                ", business_id='" + business_id + '\'' +
                ", business_name='" + business_name + '\'' +
                ", party_size='" + party_size + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", service_type1='" + service_type1 + '\'' +
                ", service_type2='" + service_type2 + '\'' +
                '}';
    }
}
