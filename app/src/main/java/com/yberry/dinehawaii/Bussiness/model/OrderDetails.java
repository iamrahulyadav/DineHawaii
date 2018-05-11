package com.yberry.dinehawaii.Bussiness.model;

import java.io.Serializable;

/**
 * Created by hvantage3 on 6/29/2017.
 */

public class OrderDetails implements Serializable
{


    String id;
    String order_id;
    String date;
    String business_name;
    String total_price;
    String delivery_adderess;
    String delivery_name;
    String delivery_contact_no;
    String order_status;
    String user_name;
    String food_name;
    String quantity;
    String order_type;
    String customization;
    String description;
    String due_time;
    String order_time;
    String reviewmsg;
    String loyalty_points;
    String e_gift_code;
    String e_gift_amount;
    String coupon_code;
    String coupon_amount;

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }


    public OrderDetails() {
    }


    public String getLoyalty_points() {
        return loyalty_points;
    }

    public void setLoyalty_points(String loyalty_points) {
        this.loyalty_points = loyalty_points;
    }

    public String getE_gift_code() {
        return e_gift_code;
    }

    public void setE_gift_code(String e_gift_code) {
        this.e_gift_code = e_gift_code;
    }

    public String getE_gift_amount() {
        return e_gift_amount;
    }

    public void setE_gift_amount(String e_gift_amount) {
        this.e_gift_amount = e_gift_amount;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(String coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    public OrderDetails(String id, String order_id, String date, String business_name, String total_price, String delivery_adderess, String delivery_name, String delivery_contact_no, String order_status, String user_name,
                        String food_name, String quantity, String order_type, String customization, String description, String loyalty_points,
                        String e_gift_code, String e_gift_amount, String coupon_code, String coupon_amount) {
        this.id = id;
        this.order_id = order_id;
        this.date = date;
        this.business_name = business_name;
        this.total_price = total_price;
        this.delivery_adderess = delivery_adderess;
        this.delivery_name = delivery_name;
        this.delivery_contact_no = delivery_contact_no;
        this.order_status = order_status;
        this.user_name = user_name;
        this.food_name = food_name;
        this.quantity = quantity;
        this.order_type = order_type;
        this.customization = customization;
        this.description = description;
        this.loyalty_points = loyalty_points;
        this.e_gift_code = e_gift_code;
        this.e_gift_amount = e_gift_amount;
        this.coupon_code = coupon_code;
        this.coupon_amount = coupon_amount;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDelivery_adderess() {
        return delivery_adderess;
    }

    public void setDelivery_adderess(String delivery_adderess) {
        this.delivery_adderess = delivery_adderess;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_contact_no() {
        return delivery_contact_no;
    }

    public void setDelivery_contact_no(String delivery_contact_no) {
        this.delivery_contact_no = delivery_contact_no;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getCustomization() {
        return customization;
    }

    public void setCustomization(String customization) {
        this.customization = customization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReviewmsg() {
        return reviewmsg;
    }

    public void setReviewmsg(String reviewmsg) {
        this.reviewmsg = reviewmsg;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "id='" + id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", date='" + date + '\'' +
                ", business_name='" + business_name + '\'' +
                ", total_price='" + total_price + '\'' +
                ", delivery_adderess='" + delivery_adderess + '\'' +
                ", delivery_name='" + delivery_name + '\'' +
                ", delivery_contact_no='" + delivery_contact_no + '\'' +
                ", order_status='" + order_status + '\'' +
                ", user_name='" + user_name + '\'' +
                ", food_name='" + food_name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", order_type='" + order_type + '\'' +
                ", customization='" + customization + '\'' +
                ", reviewmsg='" + reviewmsg + '\'' +
                ", description='" + description + '\'' +
                ", loyalty_points='" + loyalty_points + '\'' +
                ", e_gift_code='" + e_gift_code + '\'' +
                ", e_gift_amount='" + e_gift_amount + '\'' +
                ", coupon_code='" + coupon_code + '\'' +
                ", coupon_amount='" + coupon_amount + '\'' +
                '}';
    }
}
