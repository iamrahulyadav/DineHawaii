package com.yberry.dinehawaii.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferencesBuss {
    public static final String MBPREFERENCES = "bussi_pre";
    public static final String USER_ID = "user_id";
    public static final String BUSSI_ID = "bussi_id";
    public static final String BUSSI_NAME = "bussi_name";
    public static final String BUSSI_FEIN = "bussi_fein";
    public static final String BUSSI_EMILID = "bussi_emilid";
    public static final String BUSSI_PACKAGELIST = "bussi_packagelist";
    public static final String BUSSI_OPTIONLIST = "bussi_optionlist";
    public static final String BUSSI_PHONENO = "bussi_phoneno";
    public static final String RESTURANT_CONTACT = "resturant_contact";
    public static final String CUSTOMER_GIFT_ID = "gift_id";
    public static final String BUSSI_SAVEID = "bussi_saveid";
    public static final String BUSSI_SAVEPASS = "savepass";
    public static final String REGISTRATION_SNO_42A = "registration_sno_42a";
    public static final String REGISTRATION_SNO_42C = "registration_sno_42c";
    public static final String REGISTRATION_SNO_42D = "registration_sno_42d";
    public static final String REGISTRATION_SNO_42F = "registration_sno_42f";
    public static final String REGISTRATION_SNO_43 = "registration_sno_43";
    public static final String PROFILE_NAME = "profile_image";
    public static final String PROFILE_EMAIL = "profile_email";
    public static final String PROFILE_MOBILE = "profile_mobile";
    public static final String PROFILE_IMAGE = "profile_image";
    public static final String COUPON_AMOUNT = "coupon_amount";
    public static final String COUPON_NAME = "coupon_name";
    public static final String BUSSINESS_PACKAGE_TOTAL_AMOUNT = "package_amount";
    public static final String BUSSINESS_TRANCTION_ID = "business_traction_id";
    public static final String BUSSINESS_TRANCTION_STATUS = "business_traction_state";
    public static final String BUSSINESS_OPTION_LIST = "option_list";
    public static final String BUSSINESS_ORDER_TYPE = "order_type";
    public static final String BUSSINESS_ORDER_STATUS = "order_status";
    public static final String OPEN = "open";
    public static final String CLOSE = "close";
    public static final String FIRST_NAME = "first";
    public static final String BUSINESSSIMAGE = "image_business";
    public static final String RESERVATION_ID = "re_id";
    public static final String LOYALITY_POINTS = "loyality_points";
    public static final String EGIFT_POINTS = "egift_points";
    public static final String FINAL_EGIFT_POINTS = "total_egift_points";
    public static final String FINAL_LOYALTY_POINTS = "total_loyalty_points";
    public static final String GRAND_TOTAL = "grand_total";
    public static final String GRATUITY = "gratuity";
    public static final String CREDIT = "credit";
    public static final String START_MONTH = "start_month";
    public static final String END_MONTH = "end_month";
    public static final String MAP_LATITUDE = "map_latitude";
    public static final String MAP_LONGITUTE = "map_longitutde";
    public static final String AVERAGE_PRICE = "average_price";
    public static final String RESPONSE_IMAGE = "response_image";
    public static final String VERIFIED_STATUS = "verified_status";
    public static final String ALLOTTED_DUTIES = "allotted_duties";
    private static final String VENDORURL = "vendor_url";
    private static final String EMP_POSITION_NAME = "emp_position_name";
    private static final String EMP_POSITION = "emp_position";
    private static final String USERTYPEID = "user_type_id";
    private static final String IS_MULTISITE = "is_multisite";
    private static final String IS_SUB_BUSINESS = "is_sub_business";
    private static final String MAIN_BUSINESS_EMAIL = "main_business_email";
    private final SharedPreferences sharedPreferences;
    private final Editor editor;

    public AppPreferencesBuss(Context context) {
        String prefsFile = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public static String getReservatId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(RESERVATION_ID, "");
    }


    public static String setReservatId(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(RESERVATION_ID, id);
        editor.commit();
        return id;
    }

    public static String getUsertypeid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(USERTYPEID, "");
    }


    public static String setUsertypeid(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USERTYPEID, id);
        editor.commit();
        return id;
    }

    public static String getVendorUrl(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(VENDORURL, "");
    }


    public static String setVendorUrl(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(VENDORURL, id);
        editor.commit();
        return id;
    }


    public static String getAveragePrice(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(AVERAGE_PRICE, "");
    }


    public static String setAveragePrice(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(AVERAGE_PRICE, value);
        editor.commit();
        return value;
    }

    public static String setAllottedDuties(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(ALLOTTED_DUTIES, id);
        editor.commit();
        return id;
    }


    public static String getAllottedDuties(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(ALLOTTED_DUTIES, "");
    }

/*
    public static String setResponseImage(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(RESPONSE_IMAGE, "");
    }


    public static String getResponseImage(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(RESPONSE_IMAGE, id);
        editor.commit();
        return id;
    }
*/


    public static String getDp(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(RESPONSE_IMAGE, "");
    }


    public static String setDp(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(RESPONSE_IMAGE, id);
        editor.commit();
        return id;
    }


    public static String getProfileImage(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(PROFILE_IMAGE, "");
    }


    public static String setProfileImage(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PROFILE_IMAGE, id);
        editor.commit();
        return id;
    }

    public static String getProfileMobile(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(PROFILE_MOBILE, "0");
    }

    public static String setProfileMobile(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PROFILE_MOBILE, id);
        editor.commit();
        return id;
    }

    public static String getProfileEmail(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(PROFILE_EMAIL, "0");
    }

    public static String setProfileEmail(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PROFILE_EMAIL, id);
        editor.commit();
        return id;
    }

    public static String getProfileName(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(PROFILE_NAME, "0");
    }

    public static String setProfileName(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(PROFILE_NAME, id);
        editor.commit();
        return id;
    }

    public static String getResturantContact(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(RESTURANT_CONTACT, "0");
    }

    public static String setResturantContact(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(RESTURANT_CONTACT, id);
        editor.commit();
        return id;
    }


    public static String getBussiPackagelist(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSI_PACKAGELIST, "");
    }

    public static void setBussiPackagelist(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSI_PACKAGELIST, id);
        editor.commit();
    }

    public static String getBussiOptionlist(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSI_OPTIONLIST, "");
    }

    public static void setBussiOptionlist(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSI_OPTIONLIST, id);
        editor.commit();
    }

    public static String getUserId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(USER_ID, "0");
    }

    public static void setUserId(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(USER_ID, id);
        editor.commit();
    }

    public static String getBussiId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSI_ID, "");
    }

    public static void setBussiId(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSI_ID, name);
        editor.commit();
    }

    public static String getBussiName(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSI_NAME, "Guest");
    }

    public static void setBussiName(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSI_NAME, name);
        editor.commit();
    }

    public static String getBussiFein(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSI_FEIN, "");
    }

    public static void setBussiFein(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSI_FEIN, name);
        editor.commit();
    }

    public static String getBussiPhoneNo(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSI_PHONENO, "");
    }

    public static void setBussiPhoneNo(Context context, String phoneNo) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSI_PHONENO, phoneNo);
        editor.commit();
    }

    public static String getBussiEmilid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSI_EMILID, "");
    }

    public static void setBussiEmilid(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSI_EMILID, name);
        editor.commit();
    }

    public static void setSaveIdPass(Context context, String id, String pass) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSI_SAVEID, id);
        editor.putString(BUSSI_SAVEPASS, pass);
        editor.commit();
    }

    public static String getSaveid(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(BUSSI_SAVEID, "");
    }

    public static String getSavepass(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(BUSSI_SAVEPASS, "");
    }

    public static void setRegistrationSno42A(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(REGISTRATION_SNO_42A, submit);
        editor.commit();
    }

    public static String getRegistrationSno42A(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(REGISTRATION_SNO_42A, "");
    }

    public static void setRegistrationSno42C(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(REGISTRATION_SNO_42C, submit);
        editor.commit();
    }

    public static String getRegistrationSno42C(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(REGISTRATION_SNO_42C, "");
    }

    public static void setRegistrationSno42D(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(REGISTRATION_SNO_42D, submit);
        editor.commit();
    }

    public static String getRegistrationSno42D(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(REGISTRATION_SNO_42D, "");
    }

    public static void setRegistrationSno42F(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(REGISTRATION_SNO_42F, submit);
        editor.commit();
    }

    public static String getRegistrationSno42F(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(REGISTRATION_SNO_42F, "");
    }


    public static void setRegistrationSno43(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(REGISTRATION_SNO_43, submit);
        editor.commit();
    }

    public static String getRegistrationSno43(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(REGISTRATION_SNO_43, "");
    }


    // CUSTOMER_GIFT_ID


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


    public static void setCouponAmount(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(COUPON_AMOUNT, submit);
        editor.commit();
    }

    public static String getCouponAmount(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(COUPON_NAME, "");
    }
    //setCustomerGiftId

    public static void setLoyalityPoints(Context context, String submit1) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(LOYALITY_POINTS, submit1);
        editor.commit();
    }

    public static String getLoyalityPoint(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(LOYALITY_POINTS, "");
    }

    public static void setEgiftPoints(Context context, String submit2) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(EGIFT_POINTS, submit2);
        editor.commit();
    }

    public static String getEgiftPoint(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(EGIFT_POINTS, "");
    }

    public static void setfinalEgiftPoints(Context context, String submit3) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(FINAL_EGIFT_POINTS, submit3);
        editor.commit();
    }

    public static String getFinalEgiftPoint(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(FINAL_EGIFT_POINTS, "");
    }

    public static void setfinalLoylityPoints(Context context, String submit4) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(FINAL_LOYALTY_POINTS, submit4);
        editor.commit();
    }

    public static String getFinalLoyalityPoint(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(FINAL_LOYALTY_POINTS, "");
    }

    public static void setGrandTotal(Context context, String submit5) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(GRAND_TOTAL, submit5);
        editor.commit();
    }

    public static String getGrandTotal(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(GRAND_TOTAL, "");
    }

    public static void setGratuity(Context context, String submit6) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(GRATUITY, submit6);
        editor.commit();
    }

    public static String getGratuity(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(GRATUITY, "");
    }

    public static void setCredit(Context context, String submit7) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CREDIT, submit7);
        editor.commit();
    }

    public static String getCredit(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(CREDIT, "");
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


    public static String getBusinessOptionList(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSINESS_OPTION_LIST, "0");
    }


    public static void setBusinessOptionList(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSINESS_OPTION_LIST, submit);
        editor.commit();
    }

    public static void setBusinessTranctionStatus(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSINESS_TRANCTION_STATUS, submit);
        editor.commit();
    }

    public static String getBusinessTranctionStatus(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSINESS_TRANCTION_STATUS, "");
    }

    public static void setPackageAmount(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSINESS_PACKAGE_TOTAL_AMOUNT, submit);
        editor.commit();
    }

    public static String getPackageAmount(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSINESS_PACKAGE_TOTAL_AMOUNT, "");
    }

    public static void setBusinessTranctionId(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSINESS_TRANCTION_ID, submit);
        editor.commit();
    }

    public static String getBusinessTranctionId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSINESS_TRANCTION_ID, "");
    }

    public static void setBusinessOrderType(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSINESS_ORDER_TYPE, submit);
        editor.commit();
    }

    public static String getBusinessOrderType(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSINESS_ORDER_TYPE, "");
    }

    public static void setBusinessOrderStatus(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSSINESS_ORDER_STATUS, submit);
        editor.commit();
    }

    public static String getBusinessOrderStatus(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSSINESS_ORDER_STATUS, "");
    }


    public static void setOpen(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(OPEN, submit);
        editor.commit();
    }

    public static String getopen(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(OPEN, "");
    }

    public static void setClose(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(CLOSE, submit);
        editor.commit();
    }

    public static String getClose(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(CLOSE, "");
    }
    //FIRST_NAME

    public static void setfirstname(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(FIRST_NAME, submit);
        editor.commit();
    }

    public static String getfirstname(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(FIRST_NAME, "");
    }


    public static void setEndtMonth(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(END_MONTH, submit);
        editor.commit();
    }

    public static String getEndtMonth(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(END_MONTH, "");
    }

    public static void setStartMonth(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(START_MONTH, submit);
        editor.commit();
    }

    public static String getStartMonth(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(START_MONTH, "");
    }

    public static void setBussImage(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(BUSINESSSIMAGE, submit);
        editor.commit();
    }

    public static String getBussImage(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(BUSINESSSIMAGE, "");
    }


    public static void setMapLatitude(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(MAP_LATITUDE, submit);
        editor.commit();
    }

    public static String getMapLatitude(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(MAP_LATITUDE, "");
    }


    public static void setMapLongitute(Context context, String submit) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(MAP_LONGITUTE, submit);
        editor.commit();
    }

    public static String getMapLongitute(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(MAP_LONGITUTE, "");
    }


    public static void setVerified_status(Context context, boolean verified_status) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(VERIFIED_STATUS, verified_status);
        editor.commit();
    }

    public static boolean getVerifiedStatus(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getBoolean(VERIFIED_STATUS, false);
    }

    public static void clearPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setEmpPosition(Context context, String emp_position_name) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(EMP_POSITION_NAME, emp_position_name);
        editor.commit();
    }

    public static String getEmpPosition(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(EMP_POSITION_NAME, "");
    }

    public static void setEmpPositionId(Context context, String emp_position) {
        SharedPreferences preferences = context.getSharedPreferences(MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(EMP_POSITION, emp_position);
        editor.commit();
    }

    public static String getEmpPositionId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(MBPREFERENCES, 0);
        return pereference.getString(EMP_POSITION, "");
    }

    public static void setIsMultisite(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(IS_MULTISITE, value);
        editor.commit();
    }

    public static boolean getIsMultisite(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(IS_MULTISITE, false);
    }

    public static void setIsSubBusiness(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putBoolean(IS_SUB_BUSINESS, value);
        editor.commit();
    }

    public static boolean getIsSubBusiness(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getBoolean(IS_SUB_BUSINESS, false);
    }

    public static void setMainBusinessEmail(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MBPREFERENCES, 0);
        Editor editor = preferences.edit();
        editor.putString(MAIN_BUSINESS_EMAIL, value);
        editor.commit();
    }

    public static String getMainBusinessEmail(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(
                MBPREFERENCES, 0);
        return pereference.getString(MAIN_BUSINESS_EMAIL, "");
    }
}






















