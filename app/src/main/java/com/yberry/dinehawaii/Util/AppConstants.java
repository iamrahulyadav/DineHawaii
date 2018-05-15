package com.yberry.dinehawaii.Util;

/**
 * Created by MAX on 25-Jan-17.
 */

public class AppConstants {
    public static final String PAYPAL_CLIENT_ID = "AS9fpi-j9BCVs_-NoWAHkBhpMjEdtXh84ZJ6-nLv8cmJnIdkZo8rMkFI2Mbqr0mgokq_yojvl7F8iNe_";

    public static String KEY_METHOD = "method";
    public static String KEY_FNAME = "first_name";
    public static String KEY_LNAME = "last_name";
    public static String KEY_EMAILID = "email_id";
    public static String KEY_COUNTRYCODE = "country_type";
    public static String KEY_MOBILE = "contact_no";
    public static String KEY_PAGE = "page";
    public static String KEY_FOOD_TYPE_ID = "food_type_id";
    public static String KEY_SERVICE_TYPE_ID = "service_type_id";
    public static String KEY_EDIT_MENU = "EditBussFoodMenu";
    public static String KEY_ADD_MENU = "AddBussFoodMenu";
    public static String KEY_ADDRESS = "physical_address";
    public static String KEY_CITY = "city";
    public static String KEY_STATE = "state";
    public static String KEY_USERID = "user_id";
    public static String KEY_OTPTYPE = "otp_type";
    public static String KEY_OTP = "otp";
    public static String KEY_LATITUDE = "latitude";
    public static String KEY_LANGUAGE = "language";
    public static String KEY_PASSWORD = "password";
    public static String KEY_LONGITUDE = "longitude";
    public static String KEY_BUSINESS_NAME = "business_name";
    public static String KEY_LOCATION = "location";
    public static String KEY_DISTANCE = "distance";
    public static String KEY_LIMIT = "limit";
    public static String KEY_ADD_FOOD_TYPE = "add_food_category";
    public static String KEY_EDIT_FOOD_CAT = "edit_food_category";
    public static String KEY_MY_FOODS = "myFoodTypes";
    public static String KEY_DEL_FOOD_CAT = "delete_food_category";
    public static String KEY_IMPORT_FOOD = "ImportBussFoodCategory";
    public static String KEY_DELETE_FOOD_ITEM = "DeleteBussFoodMenu";
    public static String KEY_EGIFT_NEW = "sendEgiftNew";
    public static String KEY_EGIFT_APPLY = "getCheckCouponCode";
    public static String FOOD_PREP_TIME = "getBussinessOrderPaymentPackage";

    //http://truckslogistics.com/Projects-Work/Hawaii/web/APP/business_user/business_table_reaservation_api.php

    public interface BASEURL {
        //String URL = "http://truckslogistics.com/Projects-Work/Hawaii/web/APP/";
        String URL = "http://take007.co.in/Projects-Work/Hawaii/APP/";
        //String URL = "http://take007.co.in/Projects-Work/Hawaii/Beta-Test/APP/";
    }

    public interface ENDPOINT {
        String VENDOR_ORDER_API = "business_user/N_Business_vendors_Orders_Bid_Api.php";
        String REGISTRATION = "normal_user/register_log.php";
        String GENERALAPI = "normal_user/general_api.php";
        String NORMAL_BUSINESS_API = "normal_user/business_api.php";
        String NORMAL_USER_SEND_GIFT = "normal_user/user_send_gift_voucher_api.php";
        String BUSSINES_USERAPI = "business_user/register_log.php";
        String BUSSINES_USER_GENERALAPI = "business_user/general_api.php";
        String BUSSINES_USER_BUSINESSAPI = "business_user/business_api.php";
        String BUSINESS_FOOD_VENDOR_API = "business_user/business_food_vendor_api.php";
        String BUSINESS_TABLE_SYSTEM_API = "business_user/business_table_system_api.php";
        String BUSINESS_FEEDBACK_AND_MARKETING_API = "business_user/business_feedback_and_marketing_api.php";
        String DELIVERY_FOOD_SERVICE_API = "business_user/delivery_food_service_api.php";
        String BUSINESS_SECURITY_LEVEL_API = "business_user/business_security_level_api.php";
        String BUSINESS_TABLE_REASERVATION_API = "business_user/business_table_reaservation_api.php";
        String BUSINESS_REPORTS_API = "business_user/business_reports_api.php";
        String BUSINESS_SERIVCES_VIEW = "business_user/business_api_view.php";
        String BUSINESS_PROFILE = "business_user/register_log.php";
        String BUSINESS_TODAY_ORDER = "business_user/business_orders_api.php";
        String ORDER_DETAIL_API = "normal_user/order_detail_api.php";
        String RESERVATION_HISTORY = "normal_user/order_detail_api.php";
        String BUSINESS_OLD_ORDER = "business_user/business_orders_api.php";
        String PURCHASE_COUPON = "purchase_coupon_list";
        String DELETE_RESERVATION = "normal_user/order_detail_api.php";
        String CONFIRM_RESERVATION = "normal_user/order_detail_api.php";
        String CUSTOMIZATION_NORMALUSER = "normal_user/business_api.php";
        ;
        String SUBMIT_NEWORDER = "normal_user/business_api.php";
        String BUSSINES_USER_OLD_TAKE_OUT_FRAGMENT = "business_user/business_orders_api.php";
        String ALL_NOTIFICATION = "normal_user/business_api.php";
        ;
        String BUSINESS_ORDER_REPORTS = "business_user/business_reports_graph_api.php";
        String ADDEDIT_MENU_URL = "business_user/Add_Edit_Food_API.php";
        String BUSS_SERVICE_URL = "business_user/business_service_api.php";
        String BUSINESS_COUPONS_API = "business_user/N_business_coupons_API.php";
        String BUSINESS_RESERVATION_STATUS_MANAGE = "business_user/business_reservation_status_manage.php";
        String CLOSE_RESERVATION = "closeReservation";
        String NOSHOWTIMEUPDATE = "noShowTimeUpdate";
        String GETALLCOUPONS = "AllCoupons";
        String COUPONURL_CUST = "normal_user/N_business_coupon_Api.php";
        String BUSINESS_ADD_POS_API = "business_user/N_business_Emp_Dueties_API.php";
        String VENDOR_LIST_API = "business_user/N_Business_vendors_Api.php";
        String N_BUSINESS_NEW_API = "business_user/N_Business_New_Api.php";
        /**/
    }

    public interface REGISTRATION {
        String USER_SIGNUP = "user_signup";                                   //DONE
        String SENDOTP = "sendOtp";                                           //DONE
        String FINALSIGNUP = "final_signup";                                  //DONE
        String USERLOGIN = "user_login";                                      //DONE
        String BUSINESSREGISTRATION = "business_registration";                //DONE
        String BUSINESSREGISTRATIONWITHID = "check_business_user";            //DONE
        String GETALLPACKAGE = "getAllPackaes";                               //DONE
        String GETALLOPTION = "getAllOptions";                                //DONE
        String GETCHECKAMOUNT = "getCheckTotalAmount";                        //DONE
        String BUSINESSUSERREGISTRATION = "business_user_registration";       //DONE
        String BUSINESSUSERLOGIN = "businessUserLogin";                       //DONE
        String BUSINESSFORGETPASSWORD = "business_forgot_password";                       //DONE
        String NORMALFORGETPASSWORD = "normal_forgot_password";                       //DONE
        String UPDATE_PROFILE = "update_profile";                       //DONE
        String NORMALPASSWORD = "update_profile";                       //DONE
        String TAX = "getGeTax";                       //DONE
        String GETALLSELECTIONAMOUNT = "getAllSelectionAmount";
        String CHECK_PAYMENT_STATUS = "check_payment_status";
    }

    public interface GENERALAPI {
        String SENDLOYALTYPOINTS = "sendLoyaltyPoints";
        String GETALLBUSINESS = "getAllBusiness";
        String GETALLDUTIES = "getAllDuties";                         //DONE Screen no
        String GETALLPOSTITLE = "getAllPosTitle";                     //DONE Screen no
        String ALLGEOLOCATION = "allGeoLocations";                    //DONE Screen no
        String GETALLSERVICES = "allNewServices";                        //DONE Screen no 45.
        String ALLSERVICES = "allServices";                        //DONE Screen no 45.
        String ALL_FOOD_CATEGORIES = "allFoodCategories";             //DONE Screen no 45.
        String FOODTYPE = "allFoodTypes";                             //DONE Screen no 45.
        String UPLOADBUSSPHOTO = "UploadBussPhoto";                   //DONE Screen no 45.
        String UPLOADSERVICELOGO = "UploadServiceLogo";               //DONE Screen no 45.
        String UPLOADSERVICEPHOTO = "UploadServicePhoto";             //DONE Screen no 45.
        String SEARCH_BUSINESS = "search_business";                   //DONE
        String SEARCH_SUBMIT_BUSINESS = "search_submit_business";     //DONE
        String MYREVIEWS = "myreviews";     //DONE
        String MYLOYALTYPOINTS = "myLoyaltyPoints";     //DONE
        String MYGIFTCOUPONS = "myGiftCoupons";
        String EMPLOYREPORT = "employeeListReport";
        String BUSINESS_RESERVATION_REPORT = "businessReservationReports";
        String RESERVATIONREPORT = "businessReservationReports";
        String GETBUSSLOGOCOVER = "getBussLogoCover";
        String LOGOUT = "mobile_logout";
        String CHECK_COUPON = "CheckCoupon";
        String ADDEMPPOS = "add_emp_position";
        String ADDEMPDUTY = "add_emp_duty";
    }

    public interface BUSSINES_USER_BUSINESSAPI {
        String PlACEVENDORORDER = "New_Vendor_Order";
        String GETSERVICEDATA = "BusinessView53";
        String UPDATEBUSINESS42C = "updateBusiness42C";                  //DONE
        String UPDATEBUSINESS42D = "updateBusiness42D";                  //DONE
        String UPDATEBUSINESS42F = "updateBusiness42F";                  //DONE
        String GENERATEPASSCODE = "generatePasscode";                    //DONE
        String ALL_FOOD_MODIFY_CATEGORIES = "view_food_service_menu";
        String CHECKPASSCODE = "checkPasscode";                          //DONE
        String BUSINESSHOME = "businessHome";                            //DONE
        String ADD_SERURITY_EMP_DETAIL = "add_serurity_emp_detail";
        String BUSS_PACKAGE_MARKETING_DETAIL = "buss_package_marketing_detail";                            //DONE
        String BUSS_GET_TODAY_ORDERS = "today_orders";
        String BUSS_GET_TODAY_CLOSE_ORDERS = "old_future_takeout_orders";
        String GETUPDATEDUTIES = "getUpdateDuties";
        String GETALLEMPLOYEEDETAIL = "getAllEmployeeDetail";

        String BUSS_GET_OLD_ORDERS = "old_orders";
        String Buss_Mark = "BusinessView57";
        String FUTURE_TAKEOUT_ORDERS = "future_takeout_orders";
        String TODAY_TAKEOUT_ORDERS = "today_takeout_orders";
        String ALL_NOTIFICATION_BUSS = "All_notification_buss";
        String ORDER_REPORT = "businessOrderReports";
        String EMPLOYEE_REPORT = "employeeListReport";
        String RESERVATION_REPORT = "businessReservationReports";
        String HOME_DELIVERY_ORDERS = "home_delivery_orders";
        String ORDER_DETAIL = "order_detail";
        String UPDATE_ORDER = "update_order";
        String COMPLETE_HOME_DELIVERY_ORDERS = "complete_home_delivery_orders";
        String INHOUSE_COMPLETE_ORDERS = "inhouse_complete_orders";
        String COMPLETE_TAKEOUT_ORDERS = "complete_takeout_orders";
        String COMPLETE_FUTURE_TAKEOUT_ORDERS = "complete_future_takeout_orders";
        String IMPORT_SERVICE_TYPE = "add_update_business_service";
        String MYSERVICES = "myAllServices";
        String SOLDMENUITEM = "getMenuSoldOut";
        String GETORDERFEED = "business_feedback_order_list";
        String SETCHECKEDIN = "setCheckedIn";
        String SETTABLEREADY = "setTableReady";
        String SETSEATEDBY = "setSeatedBy";
        String SETWAITTIME = "setWaitTime";
        String SETRESCHEDULE = "setReschedule";
        String ALLBUSINESSREPORT = "AllBusinessReport";
        String CHANGEEMPLOYEESTATUS = "changeEmployeeStatus";
        String ADDNEWCOUPON = "AddNewCoupon";
        String ALLCOUPONS = "AllCoupons";
        String EDITCOUPON = "EditCoupon";
        String SETLEADTIME = "setLeadTime";
        String GETLEADTIMES = "getLeadTimes";
        String GETVENDORSLIST = "Vendors_List";
        String MASTERVENDORLIST = "Vendor_Master_Category_List";
        String GETVENDORSFOODLIST = "Vendor_Items_List";
        String GETVENDORSMASTERFOODLIST = "Vendor_Master_Item_List";
        String GETVENDORCAT = "getCheckCategoryVendorList";
        String AddVENDOR = "Add_New_Vendor";
        String ALL_DELIVERY_VENDORS = "All_Delivery_Vendors";
        String ASSIGN_ORDER_DELIVERY = "Assign_Order_Delivery";
        String ALLBUSAREA = "all_busi_area";
        String EDITBUSAREA = "edit_busi_area";
        String ADDBUSAREA = "add_busi_area";
    }

    public interface BUSINESS_FOOD_VENDOR_API {
        String ADD_FOOD_SERVICE = "add_food_service";                               //DONE
        String ADD_FOOD_DISTRIBUTION = "add_food_distribution";                     //DONE
        String ADD_SUPPLIER_VENDOR = "add_supplier_vendor";                     //DONE
        String UPLOADDISTRILOGO = "UploadDistriLogo";                               //DONE
        String UPLOADDISTRIPHOTO = "UploadDistriPhoto";                             //DONE
        String DELIVERY_SUPPLIER_VENDOR = "delivery_supplier_vendor";               //DONE
        String DRIVER_SUPPLIER_VENDOR = "driver_supplier_vendor";                   /// /DONE
        String VIEW_FOOD_DISTRIBUTOR_MENU_PRICE = "view_food_distributor_menu_price";
        String VIEW_FOOD_SUPPLIER_MENU_PRICE = "view_food_supplier_menu_price";                   //DONE
        String VIEW_BUSINESS_SERVICE = "business_user/business_api_view.php";                   //DONE
    }

    public interface BUSINESS_TABLE_SYSTEM_API {
        String ADD_BUSINESS_TABLE = "add_business_table";
        String ADD_BUSINESS_TABLE_NEW = "add_business_table_n";
        String ADD_COMBINE_BUSINESS_TABLE = "add_business_table_combination";                                             //DONE//DONE
        String ADD_BUSINESS_TABLE_COMBINATION = "add_business_table_combination";                   //DONE
        String ADD_BUSINESS_TABLE_TIME = "add_business_table_time";                                 //DONE
        String ADD_BUSINESS_ORDER_PAYMENT_PACKAGE = "add_business_order_payment_package";           //DONE
        String ADD_BUSINESS_VENDOR_DISCOUNT_SERVICE = "add_business_vendor_discount_service";       //DONE
        String CUURENT_RESERVATION_LIST = "cuurentReservationList";
        String FUTURE_RESERVATION_LIST = "futureReservationList";
        String CONFIRM_RESERVATION = "confirmReservation";
        String CANCEL_RESERVATION = "cancelReservation";
        String SENDMESSAGE = "sendMessage";
        String GET_BUSINESS_TABLE = "business_table_detail";
        String CREATE_TABLE_BOOKING = "create_table_booking";
        String EDIT_TABLE_DETAIL = "edit_table_detail";
        String WAITLISTRESERVATIONLIST = "waitListReservationList";
        String PREVIOUS_RESERV = "oldReservation";
        String WAIT_RESERV_CONFIRM = "waitListReservationConfirm";
        String GETCOMBINATIONTABLE = "getCombinationTable";
    }

    public interface BUSINESS_FEEDBACK_AND_MARKETING_API {
        String BUSINESS_FEEDBACK_REVIEW = "business_feedback_review";                                 //DONE
        String ADD_BUSINESS_MARKETING_OPTION = "add_business_marketing_option";
        String BUSS_PACKAGING_MARKETING_OPTION = "buss_package_marketing";
        String BUSINESS_RESERV_PACKAGE = "business_reserv_package";
    }

    public interface DELIVERY_FOOD_SERVICE_API {
        String DELIVERYFOODSERVICE = "deliveryFoodService";                               //DONE
        String DRIVERDELVFOODSERVICE = "driverDelvFoodService";                           //DONE
    }

    public interface BUSINESS_API_VIEW {
        String BUSINESSVIEW42C = "BusinessView42C";
        String BUSINESSVIEW42D = "BusinessView42D";
    }

    public interface CUSTOMER_USER {
        String GET_RESURANT_MENU_DETAILS = "getResurant_menu_details";
        String MAKE_BUSINESS_RESERVATION = "makeBusinessReservation";
        String COMPLETE_ORDERS = "complete_orders";
        String GETFAVOURITEORDERS = "getFavouriteOrders";
        String GET_CURRENT_RESERVATION = "getCurrentReservation";
        String GET_RESERVATION_HISTORY = "getReservationHistory";
        String CHECK_EMAILID = "checkEmailId";
        String SEND_EGIFT = "sendEgift";
        String PROFILE_SETTING = "profileSetting";
        String GET_ORDER_DETAILS = "getOrderDetails";
        String GET_AVAILABLE_COUPON_LIST = "couponsEgift";
        String GET_PAYMENT_DETAILS = "mob_coupon_purchase";
        String PURCHASE_COUPON = "purchase_coupon_list";
        String DELETE_RESERVATION = "getReservationDelete";
        String RESERVATION_FEEDBACK = "reservation_feedback";
        String RESERVATION_RATINGS = "reservation_ratings";
        String RESERVATION_WAITLIST = "getReservationWaitList";
        String GETRESERVATIONCONFIRM = "getReservationConfirm";
        String UPDATE_RESERVATION = "getReservationEdit";
        String GET_RESERVATION_CONFIRMATION = "getReservationConfirmation";
        String BUSSINESSCUSTOMIZATIONS = "bussinessCustomizations";
        String BUSSINESSAVAILABLELOYALTYPOINTS = "bussinessAvailableloyaltyPoints";
        String GETORDERDETAILS = "getOrderDetails";
        String GET_OLD_FUTURE_TAKEOUT_ORDER = "old_future_takeout_orders";                       // old_future_takeout_orders
        String GET_OLD_TODAY_TAKEOUT_ORDER = "old_today_takeout_orders";
        String GET_PARTY_SIZE_TABLE = "bussinessPartySizeTable";
        String UPDATE_INFO = "update_Reservation";// old_future_takeout_orders
        String ALL_NOTIFICATION = "All_notification";// show All notifications
        String RESERVATION_CONFIRM_93 = "reservation_confirm_93";
        String RESERVATION_REVIEW_LIST = "restaurantRecentReview";
        String TABLE_MANAGEMENT_52A = "table_management_52A";
        String GETBUSSINESSTIME = "getBussinessTime";
        String GETBUSSINESSSERVICES = "getBussinessServices";
        String PENDING_ORDERS = "Pending_Orders";
        String MY_EGIFT = "MyEgiftList";
        String SENT_EGIFT = "sendEgiftList";
        String ADDTOFAV = "updateFavouriteOrder";
        String REMOVEFAV = "RemoveFavourite";
        String ORDER_FEEDBACK = "order_feedback";
        String RESERV_DETAILS = "reservationDetailList";
    }

    public interface REPORT {
        String BUSINESSORDERREPORTS = "businessOrderReports";
        String BUSINESSORDERGRAPHREPORTS = "businessOrderGraphReports";

    }

    public interface CUSTOMERENDPOINT {
        String COUPONS = "normal_user/user_send_gift_voucher_api.php";
        String ORDER_DETAILS = "normal_user/business_api.php";
        String PROFILE = "normal_user/register_log.php";
        String WAITLIST_RESERVATION = "normal_user/order_detail_api.php";
        String SEND_GIFT = "normal_user/user_send_gift_voucher_api.php";
        String TAX = "normal_user/user_send_gift_voucher_api.php";

    }

    //  http://truckslogistics.com/Projects-Work/Hawaii/web/APP/normal_user/general_api.php
    // String URL = "http://truckslogistics.com/Projects-Work/Hawaii/web/APP/";

    public interface FCMUPDATE {
        String SENFCMTOKEN = "fcm_update";
        String PURCHASE_ORDER = "Purchase_Order";
        String Make_Reservation = "Make_Reservation";
        String CONFIRM_RESERVATION_BUSS = "Confirm_Reservation_Buss";
        String CANCEL_RESERVATION_BUSS = "Cancel_Reservation_Buss";
        String RESERVATION_ID = "reservation_id";
        String CANCEL_RESERVATION_USE = "Cancel_Reservation_Use";
        String CONFIRM_RESERVATION_USER = "Confirm_Reservation_User";
        String PURCHASE_COUPON = "Purchase_Coupon";
        String RECIVE_COUPON = "Recive_Coupon";
        String SEND_COUPON = "Send_Coupon";
    }

    public class BUSS_LOGIN_TYPE {
        public static final String VENDOR_USER = "VendorUser";
        public static final String CUSTOMER_USER = "CustomerUser";
        public static final String BUSINESS_USER = "BussinessUser";
        public static final String BUSSINESS_LOCAL_USER = "BussinessLocalUser";
    }
}
