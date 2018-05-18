package com.yberry.dinehawaii.RetrofitClasses;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Util.AppConstants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by MAX on 25-Jan-17.
 */

public interface MyApiEndpointInterface {

    @POST(AppConstants.ENDPOINT.REGISTRATION)
    Call<JsonObject> request(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSS_SERVICE_URL)
    Call<JsonObject> buss_service(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.COUPONURL_CUST)
    Call<JsonObject> get_coupon_cust(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.VENDOR_ORDER_API)
    Call<JsonObject> vendorOrderUrl(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.GENERALAPI)
    Call<JsonObject> requestGeneral(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSSINES_USERAPI)
    Call<JsonObject> requestBusinessUrl(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSSINES_USER_GENERALAPI)
    Call<JsonObject> requestBusinessUserGeneralurl(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.ADDEDIT_MENU_URL)
    Call<JsonObject> addeditmenu(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_PROFILE)
    Call<JsonObject> businessprofile(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSSINES_USER_OLD_TAKE_OUT_FRAGMENT)
    Call<JsonObject> buisness_take_our(@Body JsonObject jsonObject);

    @POST(AppConstants.CUSTOMERENDPOINT.PROFILE)
    Call<JsonObject> customerprofile(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSSINES_USER_BUSINESSAPI)
    Call<JsonObject> businessUserBusinessApi(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_FOOD_VENDOR_API)
    Call<JsonObject> business_food_vendor_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.N_BUSINESS_NEW_API)
    Call<JsonObject> business_new_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_ADD_POS_API)
    Call<JsonObject> business_add_pos_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.VENDOR_LIST_API)
    Call<JsonObject> vendors_list_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_TABLE_SYSTEM_API)
    Call<JsonObject> business_table_system_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_FEEDBACK_AND_MARKETING_API)
    Call<JsonObject> business_feedback_and_marketing_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.DELIVERY_FOOD_SERVICE_API)
    Call<JsonObject> delivery_food_service_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.NORMAL_BUSINESS_API)
    Call<JsonObject> normalUserBusinessApi(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.NORMAL_USER_SEND_GIFT)
    Call<JsonObject> normalUserSendGift(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_SECURITY_LEVEL_API)
    Call<JsonObject> businessSecurityLevelApi(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_TABLE_REASERVATION_API)
    Call<JsonObject> business_table_reaservation_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_REPORTS_API)
    Call<JsonObject> business_reports_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_SERIVCES_VIEW)
    Call<JsonObject> bussines_service_view(@Body JsonObject jsonObject);

    @POST(AppConstants.BUSINESS_VENDOR_API.VIEW_BUSINESS_SERVICE)
    Call<JsonObject> view_service(@Body JsonObject jsonObject);

    @POST(AppConstants.CUSTOMERENDPOINT.COUPONS)
    Call<JsonObject> available_coupon(@Body JsonObject jsonObject);

    @POST(AppConstants.CUSTOMERENDPOINT.ORDER_DETAILS)
    Call<JsonObject> order_details(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_TODAY_ORDER)
    Call<JsonObject> today_orders(@Body JsonObject jsonObject);


    @POST(AppConstants.ENDPOINT.ORDER_DETAIL_API)
    Call<JsonObject> order_detail_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_OLD_ORDER)
    Call<JsonObject> old_orders(@Body JsonObject jsonObject);

    //  PAYPAL_COUPON  WAITLIST_RESERVATION
    @POST(AppConstants.ENDPOINT.RESERVATION_HISTORY)
    Call<JsonObject> reservation_history(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.PURCHASE_COUPON)
    Call<JsonObject> purchase_coupon(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.DELETE_RESERVATION)
    Call<JsonObject> delete_reservation(@Body JsonObject jsonObject);

    @POST(AppConstants.CUSTOMERENDPOINT.WAITLIST_RESERVATION)
    Call<JsonObject> wait(@Body JsonObject jsonObject);

    @POST(AppConstants.CUSTOMERENDPOINT.SEND_GIFT)
    Call<JsonObject> send_gift(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.ORDER_DETAIL_API)
    Call<JsonObject> update_reservation(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.CONFIRM_RESERVATION)
    Call<JsonObject> confirm_reservation(@Body JsonObject jsonObject);


    @POST(AppConstants.ENDPOINT.NORMAL_BUSINESS_API)
    Call<JsonObject> get_reservation_confirmation(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_TABLE_SYSTEM_API)
    Call<JsonObject> get_business_table(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_TABLE_SYSTEM_API)
    Call<JsonObject> business_combine_table_system_api(@Body JsonObject jsonObject);


    @POST(AppConstants.ENDPOINT.CUSTOMIZATION_NORMALUSER)
    Call<JsonObject> getcustomization_normaluser(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.SUBMIT_NEWORDER)
    Call<JsonObject> submit_neworder(@Body JsonObject jsonObject);


    @POST(AppConstants.ENDPOINT.NORMAL_BUSINESS_API)
    Call<JsonObject> get_party_size_tables(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.NORMAL_BUSINESS_API)
    Call<JsonObject> get_business_date_time(@Body JsonObject jsonObject);


    @POST(AppConstants.ENDPOINT.ALL_NOTIFICATION)
    Call<JsonObject> get_all_notification(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_ORDER_REPORTS)
    Call<JsonObject> business_order_reports(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_RESERVATION_STATUS_MANAGE)
    Call<JsonObject> business_reservation_status_manage(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.BUSINESS_COUPONS_API)
    Call<JsonObject> business_coupons_api(@Body JsonObject jsonObject);

    @POST(AppConstants.ENDPOINT.N_BUSINESS_NEW_API)
    Call<JsonObject> n_business_new_api(@Body JsonObject jsonObject);


}
