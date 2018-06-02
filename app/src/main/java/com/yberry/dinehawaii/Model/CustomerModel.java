package com.yberry.dinehawaii.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hp on 15-06-2017.
 */

public class CustomerModel implements Parcelable{

    private String gift_id;
    private String coupon_tile;
    private String coupon_code;
    private String start_date;
    private String end_date;
    private String amount;
    private String business_name;
    private String business_id;
    private String order_quantity;
    private String order_ItemCustomization;
    private String order_date;
    private String order_id;
    private String total_price;
    private String food_name;
    private String cat_id;
    private String menu_id;
    private String item_customization;
    private String iteam_message;
    private String item_price;
    private String order;
    private String coupon_msg;
    private String coupon_status;
    private String avgPrice;


    private String purchased_coupon;
    private String purchased_coupon_tile;
    private String purchased_txn_id;
    private String purchased_amount;
    private String purchased_business_name;

    private String waitlist_id;
    private String waitlist_reservation_id;
    private String waitlist_business_id;
    private String waitlist_business_name;
    private String waitlist_party_size;
    private String waitlist_date;
    private String waitlist_time;
    private String waitlist_service_type1;
    private String waitlist_service_type2;
    private String notification_msg;

    private  String id;
    String fav_status;
    String offer_name;
    String offer_id;
    String offer_amount;
    String offer_type;
    String offer_decp;
    String minOrderAmt;
    String couponType;

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_amount() {
        return offer_amount;
    }

    public void setOffer_amount(String offer_amount) {
        this.offer_amount = offer_amount;
    }

    public String getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }

    public String getNotification_msg() {
        return notification_msg;
    }

    public void setNotification_msg(String notification_msg) {
        this.notification_msg = notification_msg;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(String notification_time) {
        this.notification_time = notification_time;
    }

    public String getNotification_image() {
        return notification_image;
    }

    public void setNotification_image(String notification_image) {
        this.notification_image = notification_image;
    }

    private String notification_type;
    private String notification_time;
    private String notification_image;

    protected CustomerModel(Parcel in) {
        gift_id = in.readString();
        coupon_tile = in.readString();
        coupon_code = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        amount = in.readString();
        business_name = in.readString();
        business_id = in.readString();
        order_quantity = in.readString();
        order_ItemCustomization = in.readString();
        order_date = in.readString();
        order_id = in.readString();
        total_price = in.readString();
        item_price = in.readString();
        food_name = in.readString();
        cat_id = in.readString();
        menu_id = in.readString();
        item_customization = in.readString();
        iteam_message = in.readString();
        order = in.readString();
        purchased_coupon = in.readString();
        purchased_coupon_tile = in.readString();
        purchased_txn_id = in.readString();
        purchased_amount = in.readString();
        purchased_business_name = in.readString();
        waitlist_id = in.readString();
        waitlist_reservation_id = in.readString();
        waitlist_business_id = in.readString();
        waitlist_business_name = in.readString();
        waitlist_party_size = in.readString();
        waitlist_date = in.readString();
        waitlist_time = in.readString();
        waitlist_service_type1 = in.readString();
        waitlist_service_type2 = in.readString();
        avgPrice = in.readString();
        coupon_msg = in.readString();
        fav_status = in.readString();
        id = in.readString();
        coupon_status = in.readString();
        offer_name = in.readString();
        offer_amount = in.readString();
        offer_type = in.readString();
        offer_id = in.readString();
        offer_decp = in.readString();
        minOrderAmt = in.readString();
        couponType = in.readString();
    }

    public static final Creator<CustomerModel> CREATOR = new Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel in) {
            return new CustomerModel(in);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public String toString() {
        return "CustomerModel{" +
                "gift_id='" + gift_id + '\'' +
                ", coupon_tile='" + coupon_tile + '\'' +
                ", coupon_code='" + coupon_code + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", amount='" + amount + '\'' +
                ", business_name='" + business_name + '\'' +
                ", business_id='" + business_id + '\'' +
                ", order_quantity='" + order_quantity + '\'' +
                ", order_ItemCustomization='" + order_ItemCustomization + '\'' +
                ", order_date='" + order_date + '\'' +
                ", order_id='" + order_id + '\'' +
                ", total_price='" + total_price + '\'' +
                ", food_name='" + food_name + '\'' +
                ", cat_id='" + cat_id + '\'' +
                ", menu_id='" + menu_id + '\'' +
                ", item_customization='" + item_customization + '\'' +
                ", iteam_message='" + iteam_message + '\'' +
                ", item_price='" + item_price + '\'' +
                ", order='" + order + '\'' +
                ", avgPrice='" + avgPrice + '\'' +
                ", purchased_coupon='" + purchased_coupon + '\'' +
                ", purchased_coupon_tile='" + purchased_coupon_tile + '\'' +
                ", purchased_txn_id='" + purchased_txn_id + '\'' +
                ", purchased_amount='" + purchased_amount + '\'' +
                ", purchased_business_name='" + purchased_business_name + '\'' +
                ", waitlist_id='" + waitlist_id + '\'' +
                ", waitlist_reservation_id='" + waitlist_reservation_id + '\'' +
                ", waitlist_business_id='" + waitlist_business_id + '\'' +
                ", waitlist_business_name='" + waitlist_business_name + '\'' +
                ", waitlist_party_size='" + waitlist_party_size + '\'' +
                ", waitlist_date='" + waitlist_date + '\'' +
                ", waitlist_time='" + waitlist_time + '\'' +
                ", waitlist_service_type1='" + waitlist_service_type1 + '\'' +
                ", waitlist_service_type2='" + waitlist_service_type2 + '\'' +
                ", notification_msg='" + notification_msg + '\'' +
                ", notification_type='" + notification_type + '\'' +
                ", notification_time='" + notification_time + '\'' +
                ", notification_image='" + notification_image + '\'' +
                ", coupon_msg='" + coupon_msg + '\'' +
                ", coupon_status='" + coupon_status + '\'' +
                ", fav_status='" + fav_status + '\'' +
                ", id='" + id + '\'' +
                ", offer_name='" + offer_name + '\'' +
                ", offer_id='" + offer_id + '\'' +
                ", offer_amount='" + offer_amount + '\'' +
                ", offer_type='" + offer_type + '\'' +
                ", offer_decp='" + offer_decp + '\'' +
                ", couponType='" + couponType + '\'' +
                ", minOrderAmt='" + minOrderAmt + '\'' +
                '}';
    }

    public String getOffer_decp() {
        return offer_decp;
    }

    public void setOffer_decp(String offer_decp) {
        this.offer_decp = offer_decp;
    }

    public String getMinOrderAmt() {
        return minOrderAmt;
    }

    public void setMinOrderAmt(String minOrderAmt) {
        this.minOrderAmt = minOrderAmt;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gift_id);
        parcel.writeString(coupon_tile);
        parcel.writeString(coupon_code);
        parcel.writeString(start_date);
        parcel.writeString(end_date);
        parcel.writeString(amount);
        parcel.writeString(business_name);
        parcel.writeString(business_id);
        parcel.writeString(order_quantity);
        parcel.writeString(order_ItemCustomization);
        parcel.writeString(order_date);
        parcel.writeString(order_id);
        parcel.writeString(total_price);
        parcel.writeString(item_price);
        parcel.writeString(food_name);
        parcel.writeString(cat_id);
        parcel.writeString(menu_id);
        parcel.writeString(item_customization);
        parcel.writeString(iteam_message);
        parcel.writeString(order);
        parcel.writeString(purchased_coupon);
        parcel.writeString(purchased_coupon_tile);
        parcel.writeString(purchased_txn_id);
        parcel.writeString(purchased_amount);
        parcel.writeString(purchased_business_name);
        parcel.writeString(waitlist_id);
        parcel.writeString(waitlist_reservation_id);
        parcel.writeString(waitlist_business_id);
        parcel.writeString(waitlist_business_name);
        parcel.writeString(waitlist_party_size);
        parcel.writeString(waitlist_date);
        parcel.writeString(waitlist_time);
        parcel.writeString(waitlist_service_type1);
        parcel.writeString(waitlist_service_type2);
        parcel.writeString(avgPrice);
        parcel.writeString(coupon_msg);
        parcel.writeString(fav_status);
        parcel.writeString(coupon_status);
        parcel.writeString(id);
        parcel.writeString(offer_name);
        parcel.writeString(offer_amount);
        parcel.writeString(offer_type);
        parcel.writeString(offer_id);
        parcel.writeString(offer_decp);
        parcel.writeString(minOrderAmt);
        parcel.writeString(couponType);
    }
    public CustomerModel() {

    }
    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getCoupon_tile() {
        return coupon_tile;
    }

    public void setCoupon_tile(String coupon_tile) {
        this.coupon_tile = coupon_tile;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(String order_quantity) {
        this.order_quantity = order_quantity;
    }

    public String getOrder_ItemCustomization() {
        return order_ItemCustomization;
    }

    public void setOrder_ItemCustomization(String order_ItemCustomization) {
        this.order_ItemCustomization = order_ItemCustomization;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getItem_customization() {
        return item_customization;
    }

    public void setItem_customization(String item_customization) {
        this.item_customization = item_customization;
    }

    public String getIteam_message() {
        return iteam_message;
    }

    public void setIteam_message(String iteam_message) {
        this.iteam_message = iteam_message;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPurchased_coupon() {
        return purchased_coupon;
    }

    public void setPurchased_coupon(String purchased_coupon) {
        this.purchased_coupon = purchased_coupon;
    }

    public String getPurchased_coupon_tile() {
        return purchased_coupon_tile;
    }

    public void setPurchased_coupon_tile(String purchased_coupon_tile) {
        this.purchased_coupon_tile = purchased_coupon_tile;
    }

    public String getPurchased_txn_id() {
        return purchased_txn_id;
    }

    public void setPurchased_txn_id(String purchased_txn_id) {
        this.purchased_txn_id = purchased_txn_id;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getPurchased_amount() {
        return purchased_amount;
    }

    public void setPurchased_amount(String purchased_amount) {
        this.purchased_amount = purchased_amount;
    }

    public String getPurchased_business_name() {
        return purchased_business_name;
    }

    public void setPurchased_business_name(String purchased_business_name) {
        this.purchased_business_name = purchased_business_name;
    }

    public String getWaitlist_id() {
        return waitlist_id;
    }

    public void setWaitlist_id(String waitlist_id) {
        this.waitlist_id = waitlist_id;
    }

    public String getWaitlist_reservation_id() {
        return waitlist_reservation_id;
    }

    public void setWaitlist_reservation_id(String waitlist_reservation_id) {
        this.waitlist_reservation_id = waitlist_reservation_id;
    }

    public String getWaitlist_business_id() {
        return waitlist_business_id;
    }

    public void setWaitlist_business_id(String waitlist_business_id) {
        this.waitlist_business_id = waitlist_business_id;
    }

    public String getWaitlist_business_name() {
        return waitlist_business_name;
    }

    public void setWaitlist_business_name(String waitlist_business_name) {
        this.waitlist_business_name = waitlist_business_name;
    }

    public String getWaitlist_party_size() {
        return waitlist_party_size;
    }

    public void setWaitlist_party_size(String waitlist_party_size) {
        this.waitlist_party_size = waitlist_party_size;
    }

    public String getWaitlist_date() {
        return waitlist_date;
    }

    public void setWaitlist_date(String waitlist_date) {
        this.waitlist_date = waitlist_date;
    }

    public String getWaitlist_time() {
        return waitlist_time;
    }

    public void setWaitlist_time(String waitlist_time) {
        this.waitlist_time = waitlist_time;
    }

    public String getWaitlist_service_type1() {
        return waitlist_service_type1;
    }

    public String getCoupon_msg() {
        return coupon_msg;
    }

    public void setCoupon_msg(String coupon_msg) {
        this.coupon_msg = coupon_msg;
    }

    public String getCoupon_status() {
        return coupon_status;
    }

    public void setCoupon_status(String coupon_status) {
        this.coupon_status = coupon_status;
    }

    public String getFav_status() {
        return fav_status;
    }

    public void setFav_status(String fav_status) {
        this.fav_status = fav_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public void setWaitlist_service_type1(String waitlist_service_type1) {
        this.waitlist_service_type1 = waitlist_service_type1;
    }

    public String getWaitlist_service_type2() {
        return waitlist_service_type2;
    }

    public void setWaitlist_service_type2(String waitlist_service_type2) {
        this.waitlist_service_type2 = waitlist_service_type2;
    }

    public static Creator<CustomerModel> getCREATOR() {
        return CREATOR;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }


}