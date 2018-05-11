package com.yberry.dinehawaii.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.yberry.dinehawaii.cpp.CountryCodePicker;

public class AppPreferences {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String MBPREFERENCES = "customer_pre";
    public static final String CUSTOMERID = "customerid";
    public static final String CUSTOMERNAME = "customername";
    public static final String CUSTOMER_MOBILE = "customermobile";
    public static final String CUSTOMER_ADDRESS = "customeraddress";
    public static final String CUSTOMER_PIC = "customerpic";
    public static final String SAVEID = "saveid";
    public static final String SAVEPASS = "savepass";
    public static final String EMAIL_SETTING = "email_setting";
    public static final String PRIVACY_SETTING = "privacy_setting";
    public static final String NOTIFICATION_SETTING = "notification_setting";
    public static final String APPUPDATE_SETTING = "appupdate_setting";
    public static final String TRANCTION_ID = "tranction_id";
    public static final String MENU_ID = "menu_id";
    public static final String CAT_ID = "cat_id";
    public static final String PRICE = "price_id";
    public static final String CUSTOMER_ORDERTIME = "customer_ordertime";
    public static final String BUSNID = "busnid";
    public static final String QTY = "qty";
    public static final String RADIOVALUE = "radio";
    public static final String PAYMENT_APPROVED = "payment_approved";
    public static final String TOKEN_ID = "_TOKEN_ID";
    public static final String SELECTED_CHBK = "selected_chbk";
    public static final String USER_TYPE = "user_type";
    public static final String CUSTOMER_GIFT_ID = "gift_id";
    public static final String COUPON_NAME = "coupon_name";
    public static final String COUPON_BUSINESS_ID = "busness_id";
    public static final String COUPON_AMOUNT = "coupon_amount";
    public static final String BUSS_PACKAGE_LIST = "buss_package_list";
    public static final String CUSTOMER_TODAY_TIME = "today_time";
    public static final String CUSTOMER_FUTURE_TIME = "future_time";
    public static final String CUSTOMER_OUT_TIME = "out_time";
    public static final String DELIVERY_NAME = "d_name";
    public static final String DELIVERY_CONTACT = "d_contact";
    public static final String DELIVERY_ADDRESS = "d_address";
    public static final String DELIVERY_CITY = "d_city";
    public static final String DELIVERY_STATE = "d_state";
    public static final String DELIVERY_ZIP_CODE = "d_zip_code";
    public static final String DELIVERY_COUNTRY = "d_name";
    public static final String DELIVERY_CUSTOMIZATION = "d_customiztion";
    public static final String DELIVERY_MESSAGE = "d_msg";
    public static final String ORDER_TYPE = "o_type";
    public static final String IMAGE = "image";
    public static final String COUNTRYCODE = "code";
    public static final String RESERVATION_ID = "reservation";
    public static final String RESERVATION_AMOUNT = "reservation_amount";
    public static final String ORDER_MENU_ID = "menu_id_order";
    public static final String PREP_TIME = "prep_time";




    private final SharedPreferences sharedPreferences;
    private final Editor editor;
    public static String getLatitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(LATITUDE, "");
    }

    public static void setLatitude(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(LATITUDE, id);
        editor.commit();
    }

    public static String getLongitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(LONGITUDE, "");
    }

    public static void setLongitude(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(LONGITUDE, id);
        editor.commit();
    }
    public AppPreferences(Context context) {
        String prefsFile = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static String getCustomerPic(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_PIC, "");
    }

    public static void setCustomerPic(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_PIC, id);
        editor.commit();
    }

    public static String getCustomerid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CUSTOMERID, "0");
    }

    public static void setCustomerid(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMERID, id);
        editor.commit();
    }

    public static String getCustomername(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CUSTOMERNAME, "Guest");
    }

    public static void setCustomername(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMERNAME, name);
        editor.commit();
    }

    public static void setSaveIdPass(Context context, String id, String pass) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SAVEID, id);
        editor.putString(SAVEPASS, pass);
        editor.commit();
    }

    public static String getSaveid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SAVEID, "");
    }


    public static String getSavepass(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SAVEPASS, "");
    }


    public static String getPrivacySetting(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(PRIVACY_SETTING, "");
    }

    public static void setPrivacySetting(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PRIVACY_SETTING, email);
        editor.commit();
    }


    public static String getEmailSetting(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(EMAIL_SETTING, "");
    }

    public static void setEmailSetting(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(EMAIL_SETTING, email);
        editor.commit();
    }
 public static String getPrepTime(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(PREP_TIME, "");
    }

    public static void setPrepTime(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PREP_TIME, email);
        editor.commit();
    }


    public static String getNotificationSetting(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(NOTIFICATION_SETTING, "");
    }

    public static void setNotificationSetting(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(NOTIFICATION_SETTING, email);
        editor.commit();
    }


    public static String getAppUpdateSetting(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(APPUPDATE_SETTING, "");
    }

    public static void setAppUpdateSetting(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(APPUPDATE_SETTING, email);
        editor.commit();
    }

    public static String getCustomerMobile(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_MOBILE, "");
    }

    public static void setCustomerMobile(Context context, String mobile) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_MOBILE, mobile);
        editor.commit();
    }

    public static String getCustomerAddress(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_ADDRESS, "");
    }

    public static void setCustomerAddress(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_ADDRESS, address);
        editor.commit();
    }

    public static String getTranctionId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(TRANCTION_ID, "");
    }

    public static void setTranctionId(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(TRANCTION_ID, address);
        editor.commit();
    }

    public static void setCustomerCoupon(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(COUPON_NAME, submit);
        editor.commit();
    }

    public static String getCustomerCoupon(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(COUPON_NAME, "");
    }


    public static String getQty(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(QTY, "");
    }

    public static void setQty(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(QTY, address);
        editor.commit();
    }

    public static String getPrice(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(PRICE, "");
    }

    public static void setPrice(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PRICE, address);
        editor.commit();
    }

    public static String getBusiID(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(BUSNID, "");
    }

    public static void setBusiID(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSNID, address);
        editor.commit();
    }


    public static String getCatId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(CAT_ID, "");
    }

    public static void setCatId(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CAT_ID, address);
        editor.commit();
    }


    public static String getMenuId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(MENU_ID, "");
    }

    public static void setMenuId(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(MENU_ID, address);
        editor.commit();
    }


    public static String getRadioValue(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(RADIOVALUE, "1");
    }

    public static void setRadioValue(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(RADIOVALUE, address);
        editor.commit();
    }


    public static String getPaymentApproved(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(PAYMENT_APPROVED, "");
    }

    //    PAYMENT_APPROVED
    public static void setPaymentApproved(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PAYMENT_APPROVED, address);
        editor.commit();
    }

    public static String getTOKENID(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(TOKEN_ID, "");
    }

    //    PAYMENT_APPROVED
    public static void setTOKENID(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(TOKEN_ID, address);
        editor.commit();
    }

    public static String getSelectedChbk(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(SELECTED_CHBK, "");
    }

    //    PAYMENT_APPROVED
    public static void setSelectedChbk(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(SELECTED_CHBK, address);
        editor.commit();
    }


    public static String getUserType(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(USER_TYPE, "");
    }


    public static void setUserType(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USER_TYPE, address);
        editor.commit();
    }

    public static void setCustomerGiftId(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_GIFT_ID, submit);
        editor.commit();
    }

    public static String getCustomerGiftId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_GIFT_ID, "");
    }

    public static void setCustomerBusinessId(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(COUPON_BUSINESS_ID, submit);
        editor.commit();
    }

    public static String getCustomerBusinessId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(COUPON_BUSINESS_ID, "");
    }

    public static void setCouponAmount(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(COUPON_AMOUNT, submit);
        editor.commit();
    }

    public static String getCouponAmount(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(COUPON_AMOUNT, "");


    }


    public static void setBussPackageList(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSS_PACKAGE_LIST, package_list);
        editor.commit();
    }


    public static String getBussPackageList(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSS_PACKAGE_LIST, "");
    }


    public static void setCustomerTodayTime(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_TODAY_TIME, package_list);
        editor.commit();
    }


    public static String getCustomerTodayTime(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_TODAY_TIME, "");
    }

    public static void setCustomerFutureTime(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_FUTURE_TIME, package_list);
        editor.commit();
    }


    public static String getCustomerFutureTime(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_FUTURE_TIME, "");
    }


    public static boolean setDeliveryName(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_NAME, package_list);
        editor.commit();
        return false;
    }


    public static String getDeliveryName(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_NAME, "");
    }

    public static void setDeliveryContact(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_CONTACT, package_list);
        editor.commit();
    }


    public static String getDeliveryContact(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_CONTACT, "");
    }

    public static void setDeliveryAddress(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_ADDRESS, package_list);
        editor.commit();
    }


    public static String getDeliveryAddress(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_ADDRESS, "");
    }

    public static void setDeliveryCity(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_CITY, package_list);
        editor.commit();
    }


    public static String getDeliveryCity(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_CITY, "");
    }


    public static void setDeliveryState(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_STATE, package_list);
        editor.commit();
    }


    public static String getDeliveryState(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_STATE, "");
    }


    public static void setDeliveryZipCode(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_ZIP_CODE, package_list);
        editor.commit();
    }


    public static String getDeliveryZipCode(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_ZIP_CODE, "");
    }

    public static void setDeliveryCountry(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_COUNTRY, package_list);
        editor.commit();
    }


    public static String getDeliveryCountry(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_COUNTRY, "");
    }


    public static void setCustomizationDelivery(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_CUSTOMIZATION, package_list);
        editor.commit();
    }


    public static String getCustomizationDelivery(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_CUSTOMIZATION, "");
    }
    // DELIVERY_MESSAGE   CUSTOMER_OUT_TIME

    public static void setCustomizationMsg(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(DELIVERY_MESSAGE, package_list);
        editor.commit();
    }


    public static String getCustomizationMsg(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(DELIVERY_MESSAGE, "");
    }

    public static void setCustomerOut_Time(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_OUT_TIME, package_list);
        editor.commit();
    }


    public static String getCustomerOut_Time(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_OUT_TIME, "");
    }

//ORDER_TYPE


    public static void setOrderType(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(ORDER_TYPE, package_list);
        editor.commit();
    }


    public static String getOrderType(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(ORDER_TYPE, "");
    }

    public static void setImage(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(IMAGE, package_list);
        editor.commit();
    }


    public static String getImage(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(IMAGE, "");
    }

    public static void setcountrycode(Context context, CountryCodePicker package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(COUNTRYCODE, String.valueOf(package_list));
        editor.commit();
    }


    public static String getcountrycode(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(COUNTRYCODE, "");
    }






    public static void setReservationId(Context context, String package_list) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(RESERVATION_ID, package_list);
        editor.commit();
    }


    public static String getReservationId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(RESERVATION_ID, "");
    }
    public static void setReservationAmount(Context context, String amount) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(RESERVATION_AMOUNT, amount);
        editor.commit();
    }


    public static String getReservationAmount(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(RESERVATION_AMOUNT, "");
    }



    public static String getMenuOrderId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(ORDER_MENU_ID, "");
    }

    public static void setMenuOrderId(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(ORDER_MENU_ID, email);
        editor.commit();
    }

    public static void setOrderTime(Context context, String time) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CUSTOMER_ORDERTIME, time);
        editor.commit();
    }


    public static String getOrderTime(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(CUSTOMER_ORDERTIME, "");
    }

    public static void clearPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


}
