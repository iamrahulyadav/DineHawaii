package com.yberry.dinehawaii.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Peter on 11-Apr-17.
 */

public class OrderItemsDetailsModel implements Parcelable {

    public static final Creator<OrderItemsDetailsModel> CREATOR = new Creator<OrderItemsDetailsModel>() {
        @Override
        public OrderItemsDetailsModel createFromParcel(Parcel in) {
            return new OrderItemsDetailsModel(in);
        }

        @Override
        public OrderItemsDetailsModel[] newArray(int size) {
            return new OrderItemsDetailsModel[size];
        }
    };
    public String ItemName;
    public String ItemPrice;
    public String ItemQuantity;
    public String ItemTotalCost;
    public String avgPrice;
    public String QuantityType;
    public String itemImage;
    public String ServiceType;
    public String ItemEditId;
    public String food_type_id;
    public String service_type_id;
    public String details;
    public String half_price;
    public String item_category;
    public String Menu_id;
    public String Cat_id;
    public String Buss_id;
    public String Message;
    public String Date;
    public String Status;
    public String Name;
    public String DeliveryAddress;
    public String DineHawaiiID;
    public String SmartphoneNo;
    public String WaitStaffName;
    public String ItemCustomiationList;
    String homeDelFoodTime;
    String inhouseDelFoodTime;
    String takeoutDelFoodTime;
    String catFoodTime;
    String menuItemStatus;
    String busAreaId;
    String busAreaName;
    private String old_today_take_out_id;
    private String old_today_take_out_order_id;
    private String old_today_take_out_date;
    private String old_today_take_out_business_name;
    private String old_today_take_out_total_price;
    private String old_today_take_out_delivery_adderess;
    private String old_today_take_out_total_delivery_name;
    private String old_today_take_out_total_delivery_contact_no;
    private String old_today_take_out_total_order_status;
    private String old_today_take_out_user_name;
    private String old_today_take_out_food_name;
    private String old_today_take_out_quantity;
    private String old_future_take_out_id;
    private String old_future_take_out_order_id;
    private String old_future_take_out_date;
    private String old_future_take_out_business_name;
    private String old_future_take_out_total_price;
    private String old_future_take_out_delivery_adderess;
    private String old_futurey_take_out_total_delivery_name;
    private String old_future_take_out_total_delivery_contact_no;
    private String old_future_take_out_total_order_status;
    private String old_future_take_out_user_name;
    private String old_future_take_out_food_name;
    private String old_future_take_out_quantity;
    private String OrderType;
    private String item_customization;
    public OrderItemsDetailsModel() {

    }
    public OrderItemsDetailsModel(Parcel in) {
        ItemName = in.readString();
        ItemPrice = in.readString();
        ItemQuantity = in.readString();
        ItemTotalCost = in.readString();
        Menu_id = in.readString();
        Cat_id = in.readString();
        Buss_id = in.readString();
        Message = in.readString();
        Date = in.readString();
        Status = in.readString();
        Name = in.readString();
        DeliveryAddress = in.readString();
        DineHawaiiID = in.readString();
        SmartphoneNo = in.readString();
        WaitStaffName = in.readString();
        old_today_take_out_id = in.readString();
        old_today_take_out_order_id = in.readString();
        old_today_take_out_date = in.readString();
        old_today_take_out_business_name = in.readString();
        old_today_take_out_total_price = in.readString();
        old_today_take_out_delivery_adderess = in.readString();
        old_today_take_out_total_delivery_name = in.readString();
        old_today_take_out_total_delivery_contact_no = in.readString();
        old_today_take_out_total_order_status = in.readString();
        old_today_take_out_user_name = in.readString();
        old_today_take_out_food_name = in.readString();
        old_today_take_out_quantity = in.readString();
        old_future_take_out_id = in.readString();
        old_future_take_out_order_id = in.readString();
        old_future_take_out_date = in.readString();
        old_future_take_out_business_name = in.readString();
        old_future_take_out_total_price = in.readString();
        old_future_take_out_delivery_adderess = in.readString();
        old_futurey_take_out_total_delivery_name = in.readString();
        old_future_take_out_total_delivery_contact_no = in.readString();
        old_future_take_out_total_order_status = in.readString();
        old_future_take_out_user_name = in.readString();
        old_future_take_out_food_name = in.readString();
        old_future_take_out_quantity = in.readString();
        OrderType = in.readString();
        avgPrice = in.readString();
        item_category = in.readString();
        food_type_id = in.readString();
        half_price = in.readString();
        details = in.readString();
        itemImage = in.readString();
        ServiceType = in.readString();
        ItemEditId = in.readString();
        service_type_id = in.readString();
        QuantityType = in.readString();
        homeDelFoodTime = in.readString();
        inhouseDelFoodTime = in.readString();
        takeoutDelFoodTime = in.readString();
        menuItemStatus = in.readString();
        catFoodTime = in.readString();
        busAreaId = in.readString();
        busAreaName = in.readString();
        item_customization = in.readString();
    }

    public static Creator<OrderItemsDetailsModel> getCREATOR() {
        return CREATOR;
    }

    public String getItemEditId() {
        return ItemEditId;
    }

    public void setItemEditId(String itemEditId) {
        ItemEditId = itemEditId;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(String service_type_id) {
        this.service_type_id = service_type_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getHalf_price() {
        return half_price;
    }

    public void setHalf_price(String half_price) {
        this.half_price = half_price;
    }

    public String getItem_category() {
        return item_category;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ItemName);
        dest.writeString(ItemPrice);
        dest.writeString(ItemQuantity);
        dest.writeString(ItemTotalCost);
        dest.writeString(Menu_id);
        dest.writeString(Cat_id);
        dest.writeString(Buss_id);
        dest.writeString(Message);
        dest.writeString(Date);
        dest.writeString(Status);
        dest.writeString(Name);
        dest.writeString(DeliveryAddress);
        dest.writeString(DineHawaiiID);
        dest.writeString(SmartphoneNo);
        dest.writeString(WaitStaffName);
        dest.writeString(old_today_take_out_id);
        dest.writeString(old_today_take_out_order_id);
        dest.writeString(old_today_take_out_date);
        dest.writeString(old_today_take_out_business_name);
        dest.writeString(old_today_take_out_total_price);
        dest.writeString(old_today_take_out_delivery_adderess);
        dest.writeString(old_today_take_out_total_delivery_name);
        dest.writeString(old_today_take_out_total_delivery_contact_no);
        dest.writeString(old_today_take_out_total_order_status);
        dest.writeString(old_today_take_out_user_name);
        dest.writeString(old_today_take_out_food_name);
        dest.writeString(old_today_take_out_quantity);
        dest.writeString(old_future_take_out_id);
        dest.writeString(old_future_take_out_order_id);
        dest.writeString(old_future_take_out_date);
        dest.writeString(old_future_take_out_business_name);
        dest.writeString(old_future_take_out_total_price);
        dest.writeString(old_future_take_out_delivery_adderess);
        dest.writeString(old_futurey_take_out_total_delivery_name);
        dest.writeString(old_future_take_out_total_delivery_contact_no);
        dest.writeString(old_future_take_out_total_order_status);
        dest.writeString(old_future_take_out_user_name);
        dest.writeString(old_future_take_out_food_name);
        dest.writeString(old_future_take_out_quantity);
        dest.writeString(OrderType);
        dest.writeString(avgPrice);
        dest.writeString(item_category);
        dest.writeString(food_type_id);
        dest.writeString(half_price);
        dest.writeString(details);
        dest.writeString(itemImage);
        dest.writeString(ServiceType);
        dest.writeString(ItemEditId);
        dest.writeString(service_type_id);
        dest.writeString(QuantityType);
        dest.writeString(homeDelFoodTime);
        dest.writeString(inhouseDelFoodTime);
        dest.writeString(takeoutDelFoodTime);
        dest.writeString(catFoodTime);
        dest.writeString(menuItemStatus);
        dest.writeString(busAreaId);
        dest.writeString(busAreaName);
        dest.writeString(item_customization);
    }

    public String getBusAreaId() {
        return busAreaId;
    }

    public void setBusAreaId(String busAreaId) {
        this.busAreaId = busAreaId;
    }

    public String getBusAreaName() {
        return busAreaName;
    }

    public void setBusAreaName(String busAreaName) {
        this.busAreaName = busAreaName;
    }

    public String getItem_customization() {
        return item_customization;
    }

    public void setItem_customization(String item_customization) {
        this.item_customization = item_customization;
    }

    public String getHomeDelFoodTime() {

        return homeDelFoodTime;
    }

    public void setHomeDelFoodTime(String homeDelFoodTime) {
        this.homeDelFoodTime = homeDelFoodTime;
    }

    @Override
    public String toString() {
        return "OrderItemsDetailsModel{" +
                "ItemName='" + ItemName + '\'' +
                ", ItemPrice='" + ItemPrice + '\'' +
                ", ItemQuantity='" + ItemQuantity + '\'' +
                ", ItemTotalCost='" + ItemTotalCost + '\'' +
                ", Menu_id='" + Menu_id + '\'' +
                ", Cat_id='" + Cat_id + '\'' +
                ", Buss_id='" + Buss_id + '\'' +
                ", Message='" + Message + '\'' +
                ", Date='" + Date + '\'' +
                ", Status='" + Status + '\'' +
                ", avgPrice='" + avgPrice + '\'' +
                ", Name='" + Name + '\'' +
                ", DeliveryAddress='" + DeliveryAddress + '\'' +
                ", DineHawaiiID='" + DineHawaiiID + '\'' +
                ", SmartphoneNo='" + SmartphoneNo + '\'' +
                ", WaitStaffName='" + WaitStaffName + '\'' +
                ", old_today_take_out_id='" + old_today_take_out_id + '\'' +
                ", old_today_take_out_order_id='" + old_today_take_out_order_id + '\'' +
                ", old_today_take_out_date='" + old_today_take_out_date + '\'' +
                ", old_today_take_out_business_name='" + old_today_take_out_business_name + '\'' +
                ", old_today_take_out_total_price='" + old_today_take_out_total_price + '\'' +
                ", old_today_take_out_delivery_adderess='" + old_today_take_out_delivery_adderess + '\'' +
                ", old_today_take_out_total_delivery_name='" + old_today_take_out_total_delivery_name + '\'' +
                ", old_today_take_out_total_delivery_contact_no='" + old_today_take_out_total_delivery_contact_no + '\'' +
                ", old_today_take_out_total_order_status='" + old_today_take_out_total_order_status + '\'' +
                ", old_today_take_out_user_name='" + old_today_take_out_user_name + '\'' +
                ", old_today_take_out_food_name='" + old_today_take_out_food_name + '\'' +
                ", old_today_take_out_quantity='" + old_today_take_out_quantity + '\'' +
                ", old_future_take_out_id='" + old_future_take_out_id + '\'' +
                ", old_future_take_out_order_id='" + old_future_take_out_order_id + '\'' +
                ", old_future_take_out_date='" + old_future_take_out_date + '\'' +
                ", old_future_take_out_business_name='" + old_future_take_out_business_name + '\'' +
                ", old_future_take_out_total_price='" + old_future_take_out_total_price + '\'' +
                ", old_future_take_out_delivery_adderess='" + old_future_take_out_delivery_adderess + '\'' +
                ", old_futurey_take_out_total_delivery_name='" + old_futurey_take_out_total_delivery_name + '\'' +
                ", old_future_take_out_total_delivery_contact_no='" + old_future_take_out_total_delivery_contact_no + '\'' +
                ", old_future_take_out_total_order_status='" + old_future_take_out_total_order_status + '\'' +
                ", old_future_take_out_user_name='" + old_future_take_out_user_name + '\'' +
                ", old_future_take_out_food_name='" + old_future_take_out_food_name + '\'' +
                ", old_future_take_out_quantity='" + old_future_take_out_quantity + '\'' +
                ", OrderType='" + OrderType + '\'' +
                ", item_category ='" + item_category + '\'' +
                ", food_type_id ='" + food_type_id + '\'' +
                ", half_price ='" + half_price + '\'' +
                ", details ='" + details + '\'' +
                ", itemImage ='" + itemImage + '\'' +
                ", ServiceType ='" + ServiceType + '\'' +
                ", ItemEditId ='" + ItemEditId + '\'' +
                ", service_type_id ='" + service_type_id + '\'' +
                ", QuantityType ='" + QuantityType + '\'' +
                ", homeDelFoodTime ='" + homeDelFoodTime + '\'' +
                ", inhouseDelFoodTime ='" + inhouseDelFoodTime + '\'' +
                ", catFoodTime ='" + catFoodTime + '\'' +
                ", menuItemStatus ='" + menuItemStatus + '\'' +
                ", takeoutDelFoodTime ='" + takeoutDelFoodTime + '\'' +
                ", busAreaId ='" + busAreaId + '\'' +
                ", busAreaName ='" + busAreaName + '\'' +
                ", item_customization ='" + item_customization + '\'' +
                '}';
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemQuantity() {
        return ItemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        ItemQuantity = itemQuantity;
    }

    public String getItemTotalCost() {
        return ItemTotalCost;
    }

    public void setItemTotalCost(String itemTotalCost) {
        ItemTotalCost = itemTotalCost;
    }

    public String getMenu_id() {
        return Menu_id;
    }

    public void setMenu_id(String menu_id) {
        Menu_id = menu_id;
    }

    public String getCat_id() {
        return Cat_id;
    }

    public void setCat_id(String cat_id) {
        Cat_id = cat_id;
    }

    public String getBuss_id() {
        return Buss_id;
    }

    public void setBuss_id(String buss_id) {
        Buss_id = buss_id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getDineHawaiiID() {
        return DineHawaiiID;
    }

    public void setDineHawaiiID(String dineHawaiiID) {
        DineHawaiiID = dineHawaiiID;
    }

    public String getSmartphoneNo() {
        return SmartphoneNo;
    }

    public void setSmartphoneNo(String smartphoneNo) {
        SmartphoneNo = smartphoneNo;
    }

    public String getWaitStaffName() {
        return WaitStaffName;
    }

    public void setWaitStaffName(String waitStaffName) {
        WaitStaffName = waitStaffName;
    }

    public String getItemCustomiationList() {
        return ItemCustomiationList;
    }

    public void setItemCustomiationList(String itemCustomiationList) {
        ItemCustomiationList = itemCustomiationList;
    }

    public String getOld_today_take_out_id() {
        return old_today_take_out_id;
    }

    public void setOld_today_take_out_id(String old_today_take_out_id) {
        this.old_today_take_out_id = old_today_take_out_id;
    }

    public String getOld_today_take_out_order_id() {
        return old_today_take_out_order_id;
    }

    public void setOld_today_take_out_order_id(String old_today_take_out_order_id) {
        this.old_today_take_out_order_id = old_today_take_out_order_id;
    }

    public String getOld_today_take_out_date() {
        return old_today_take_out_date;
    }

    public void setOld_today_take_out_date(String old_today_take_out_date) {
        this.old_today_take_out_date = old_today_take_out_date;
    }

    public String getOld_today_take_out_business_name() {
        return old_today_take_out_business_name;
    }

    public void setOld_today_take_out_business_name(String old_today_take_out_business_name) {
        this.old_today_take_out_business_name = old_today_take_out_business_name;
    }

    public String getOld_today_take_out_total_price() {
        return old_today_take_out_total_price;
    }

    public void setOld_today_take_out_total_price(String old_today_take_out_total_price) {
        this.old_today_take_out_total_price = old_today_take_out_total_price;
    }

    public String getOld_today_take_out_delivery_adderess() {
        return old_today_take_out_delivery_adderess;
    }

    public void setOld_today_take_out_delivery_adderess(String old_today_take_out_delivery_adderess) {
        this.old_today_take_out_delivery_adderess = old_today_take_out_delivery_adderess;
    }

    public String getOld_today_take_out_total_delivery_name() {
        return old_today_take_out_total_delivery_name;
    }

    public void setOld_today_take_out_total_delivery_name(String old_today_take_out_total_delivery_name) {
        this.old_today_take_out_total_delivery_name = old_today_take_out_total_delivery_name;
    }

    public String getOld_today_take_out_total_delivery_contact_no() {
        return old_today_take_out_total_delivery_contact_no;
    }

    public void setOld_today_take_out_total_delivery_contact_no(String old_today_take_out_total_delivery_contact_no) {
        this.old_today_take_out_total_delivery_contact_no = old_today_take_out_total_delivery_contact_no;
    }

    public String getOld_today_take_out_total_order_status() {
        return old_today_take_out_total_order_status;
    }

    public void setOld_today_take_out_total_order_status(String old_today_take_out_total_order_status) {
        this.old_today_take_out_total_order_status = old_today_take_out_total_order_status;
    }

    public String getOld_today_take_out_user_name() {
        return old_today_take_out_user_name;
    }

    public void setOld_today_take_out_user_name(String old_today_take_out_user_name) {
        this.old_today_take_out_user_name = old_today_take_out_user_name;
    }

    public String getOld_today_take_out_food_name() {
        return old_today_take_out_food_name;
    }

    public void setOld_today_take_out_food_name(String old_today_take_out_food_name) {
        this.old_today_take_out_food_name = old_today_take_out_food_name;
    }

    public String getOld_today_take_out_quantity() {
        return old_today_take_out_quantity;
    }

    public void setOld_today_take_out_quantity(String old_today_take_out_quantity) {
        this.old_today_take_out_quantity = old_today_take_out_quantity;
    }

    public String getOld_future_take_out_id() {
        return old_future_take_out_id;
    }

    public void setOld_future_take_out_id(String old_future_take_out_id) {
        this.old_future_take_out_id = old_future_take_out_id;
    }

    public String getOld_future_take_out_order_id() {
        return old_future_take_out_order_id;
    }

    public void setOld_future_take_out_order_id(String old_future_take_out_order_id) {
        this.old_future_take_out_order_id = old_future_take_out_order_id;
    }

    public String getOld_future_take_out_date() {
        return old_future_take_out_date;
    }

    public void setOld_future_take_out_date(String old_future_take_out_date) {
        this.old_future_take_out_date = old_future_take_out_date;
    }

    public String getOld_future_take_out_business_name() {
        return old_future_take_out_business_name;
    }

    public void setOld_future_take_out_business_name(String old_future_take_out_business_name) {
        this.old_future_take_out_business_name = old_future_take_out_business_name;
    }

    public String getOld_future_take_out_total_price() {
        return old_future_take_out_total_price;
    }

    public void setOld_future_take_out_total_price(String old_future_take_out_total_price) {
        this.old_future_take_out_total_price = old_future_take_out_total_price;
    }

    public String getOld_future_take_out_delivery_adderess() {
        return old_future_take_out_delivery_adderess;
    }

    public void setOld_future_take_out_delivery_adderess(String old_future_take_out_delivery_adderess) {
        this.old_future_take_out_delivery_adderess = old_future_take_out_delivery_adderess;
    }

    public String getOld_futurey_take_out_total_delivery_name() {
        return old_futurey_take_out_total_delivery_name;
    }

    public void setOld_futurey_take_out_total_delivery_name(String old_futurey_take_out_total_delivery_name) {
        this.old_futurey_take_out_total_delivery_name = old_futurey_take_out_total_delivery_name;
    }

    public String getOld_future_take_out_total_delivery_contact_no() {
        return old_future_take_out_total_delivery_contact_no;
    }

    public void setOld_future_take_out_total_delivery_contact_no(String old_future_take_out_total_delivery_contact_no) {
        this.old_future_take_out_total_delivery_contact_no = old_future_take_out_total_delivery_contact_no;
    }

    public String getOld_future_take_out_total_order_status() {
        return old_future_take_out_total_order_status;
    }

    public void setOld_future_take_out_total_order_status(String old_future_take_out_total_order_status) {
        this.old_future_take_out_total_order_status = old_future_take_out_total_order_status;
    }

    public String getOld_future_take_out_user_name() {
        return old_future_take_out_user_name;
    }

    public void setOld_future_take_out_user_name(String old_future_take_out_user_name) {
        this.old_future_take_out_user_name = old_future_take_out_user_name;
    }

    public String getOld_future_take_out_food_name() {
        return old_future_take_out_food_name;
    }

    public void setOld_future_take_out_food_name(String old_future_take_out_food_name) {
        this.old_future_take_out_food_name = old_future_take_out_food_name;
    }

    public String getOld_future_take_out_quantity() {
        return old_future_take_out_quantity;
    }

    public void setOld_future_take_out_quantity(String old_future_take_out_quantity) {
        this.old_future_take_out_quantity = old_future_take_out_quantity;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getFood_type_id() {
        return food_type_id;
    }

    public void setFood_type_id(String food_type_id) {
        this.food_type_id = food_type_id;
    }

    public String getQuantityType() {
        return QuantityType;
    }

    public void setQuantityType(String quantityType) {
        QuantityType = quantityType;
    }

    public String getMenuItemStatus() {
        return menuItemStatus;
    }

    public void setMenuItemStatus(String menuItemStatus) {
        this.menuItemStatus = menuItemStatus;
    }



    @Override
    public int describeContents() {
        return 0;
    }
}