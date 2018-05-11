package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusiSelectPackageActivity;
import com.yberry.dinehawaii.Bussiness.Activity.ManageServiceTypeActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Activity.DeliveryVendorDetailActivity;
import com.yberry.dinehawaii.vendor.Fragment.ManageVendorsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessHomeFragment41 extends Fragment {

    private static final String TAG = "BusinessHomeFragment41";
    CustomTextView reservation_btn, table_btn, order_btn, feed_btn, info_btn, packagebtn, maketingoption, staff, wholestaff, dinemsg, delivery_charge,
            service_btn, logo_btn, schedule_btn, manage_table, serviceChargesbtn, employlist, manage_service, tvVendors, vendor_orders_hist, vendor_bid_hist;
    FragmentIntraction intraction;
    private LinearLayout messageLinearLayout, business_setting_tab, other_setting_tab, manage_operation_tab, security_tab, vendors_tab;
    private ImageView oprationExpand, settingExpand, otherSettingExpand, service_minus, message_minus, securityExpand, vendorsExpand;
    private String packages = "0";
    private Context context;
    private CustomTextView manage_menu;
    private CustomTextView packageAndMarketing, securityAccess;
    private CustomTextView manage_coupons;
    private CustomTextView tvLeadTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_home_fragment41, container, false);
        LinearLayout mainView = (LinearLayout) view.findViewById(R.id.activity_bussiness_home);

        if (intraction != null) {
            intraction.actionbarsetTitle("Home");
        }

        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        init(view);
        clickListner();

        Log.e(TAG, "checkLoggedInUser: user_type >> " + AppPreferences.getUserType(getActivity()));
        if (AppPreferences.getUserType(getActivity()).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.BUSSINESS_LOCAL_USER))
            checkDuties();
        checkPackageOptions();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "onCreateView: Refreshed Token" + refreshedToken);
        sendTokenOnServer(refreshedToken);
        return view;
    }

    private void checkDuties() {
        String duties = AppPreferencesBuss.getAllottedDuties(getActivity());
        Log.e(TAG, "checkLoggedInUser: duties >> " + duties);
        if (AppPreferences.getUserType(getActivity()).equalsIgnoreCase("BussinessLocalUser")) {
            if (!TextUtils.isEmpty(duties)) {
                order_btn.setEnabled(false);
                reservation_btn.setEnabled(false);
                feed_btn.setEnabled(false);
                info_btn.setEnabled(false);
                service_btn.setEnabled(false);
                manage_menu.setEnabled(false);
                manage_table.setEnabled(false);
                manage_service.setEnabled(false);
                logo_btn.setEnabled(false);
                serviceChargesbtn.setEnabled(false);
                table_btn.setEnabled(false);
                schedule_btn.setEnabled(false);
                packageAndMarketing.setEnabled(false);
                staff.setEnabled(false);
                wholestaff.setEnabled(false);
                dinemsg.setEnabled(false);
                securityAccess.setEnabled(false);

                if (duties.contains("1")) ;

                if (duties.contains("2"))
                    securityAccess.setEnabled(true);
                if (duties.contains("3"))
                    securityAccess.setEnabled(true);
                if (duties.contains("4"))
                    manage_menu.setEnabled(true);

                if (duties.contains("5")) ;
                manage_menu.setEnabled(true);
                if (duties.contains("6")) ;

                if (duties.contains("7"))
                    reservation_btn.setEnabled(true);
                if (duties.contains("8"))
                    reservation_btn.setEnabled(true);
                if (duties.contains("9"))
                    reservation_btn.setEnabled(true);
                if (duties.contains("10"))
                    manage_table.setEnabled(true);

                if (duties.contains("11")) ;
                if (duties.contains("12")) ;
                if (duties.contains("13")) ;
                if (duties.contains("14")) ;
                if (duties.contains("15")) ;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIntraction) {
            intraction = (FragmentIntraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        intraction = null;
    }


    private void sendTokenOnServer(String s) {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.FCMUPDATE.SENFCMTOKEN);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("fcm_id", s);
            Log.e(TAG, "Request sendTokenOnServer >> " + jsonObject.toString());
            JsonCallMethod(jsonObject);
        }
    }

    @SuppressLint("LongLogTag")
    private void JsonCallMethod(JsonObject jsonObject) {
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUrl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response sendTokenOnServer >> " + response.body().toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    private void checkPackageOptions() {
        packages = AppPreferencesBuss.getBussiPackagelist(getActivity());
        Log.e(TAG, "checkPackage: " + packages);
        Log.e(TAG, "checkOptions: " + AppPreferencesBuss.getBussiOptionlist(getActivity()));
        if (!packages.contains("1")) {
            reservation_btn.setText("Manage Reservation \n (This feature is included in package 1)");
            reservation_btn.setEnabled(false);
        }

        if (!packages.contains("2")) {
            table_btn.setText("Add & Combine Tables\n (This feature is included in package 3)");
            manage_table.setText("Manage Tables\n (This feature is included in package 3)");
            table_btn.setEnabled(false);
            manage_table.setEnabled(false);
        }
        if (!packages.contains("4")) {
            tvVendors.setText("All Vendors/Suppliers\n (This feature is included in package 4)");
            vendor_orders_hist.setText("Order History\n (This feature is included in package 4)");
            vendor_bid_hist.setText("Bid History\n (This feature is included in package 4)");
            vendor_bid_hist.setEnabled(false);
            tvVendors.setEnabled(false);
            vendor_orders_hist.setEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppPreferences.getUserType(getActivity()).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.BUSSINESS_LOCAL_USER))
            checkDuties();
        checkPackageOptions();
    }

    private void clickListner() {
        oprationExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manage_operation_tab.getVisibility() == View.VISIBLE) {
                    oprationExpand.setImageResource(R.drawable.plus);
                    manage_operation_tab.setVisibility(View.GONE);
                } else {
                    oprationExpand.setImageResource(R.drawable.minus);
                    manage_operation_tab.setVisibility(View.VISIBLE);
                }
            }
        });
        vendorsExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vendors_tab.getVisibility() == View.VISIBLE) {
                    vendorsExpand.setImageResource(R.drawable.plus);
                    vendors_tab.setVisibility(View.GONE);
                } else {
                    vendorsExpand.setImageResource(R.drawable.minus);
                    vendors_tab.setVisibility(View.VISIBLE);
                }
            }
        });

        settingExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (business_setting_tab.getVisibility() == View.VISIBLE) {
                    settingExpand.setImageResource(R.drawable.plus);
                    business_setting_tab.setVisibility(View.GONE);
                } else {
                    settingExpand.setImageResource(R.drawable.minus);
                    business_setting_tab.setVisibility(View.VISIBLE);
                }

            }
        });

        otherSettingExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (other_setting_tab.getVisibility() == View.VISIBLE) {
                    otherSettingExpand.setImageResource(R.drawable.plus);
                    other_setting_tab.setVisibility(View.GONE);
                } else {
                    otherSettingExpand.setImageResource(R.drawable.minus);
                    other_setting_tab.setVisibility(View.VISIBLE);
                }
            }
        });

        message_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageLinearLayout.getVisibility() == View.VISIBLE) {
                    message_minus.setImageResource(R.drawable.plus);
                    messageLinearLayout.setVisibility(View.GONE);
                } else {
                    message_minus.setImageResource(R.drawable.minus);
                    messageLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        reservation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TableManagmentFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BusinessInformationFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        manage_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TableManagmentFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        table_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TableManagmentPackageFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ManageOrdersFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();


            }
        });

        feed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FeedbackandReviewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();


            }
        });

        manage_coupons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ManageCouponsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();


            }
        });

        manage_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ManageMenuFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BusinessServiceHoursFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();


            }
        });

        logo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UploadPhotoLogoFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FoodServiceFragment45();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        manage_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ManageTableFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });


        packageAndMarketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BusiSelectPackageActivity.class);
                startActivity(intent);

            }
        });
        serviceChargesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ServiceChargesAndPaymentFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        securityAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EmployeeListFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        tvLeadTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ManageLeadTimeFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        tvVendors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ManageVendorsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        manage_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ManageServiceTypeActivity.class));
            }
        });
        delivery_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DeliveryVendorDetailActivity.class));
            }
        });
    }

    private void init(View view) {
        oprationExpand = (ImageView) view.findViewById(R.id.oprationExpand);
        settingExpand = (ImageView) view.findViewById(R.id.settingExpand);
        otherSettingExpand = (ImageView) view.findViewById(R.id.otherSettingExpand);
        service_minus = (ImageView) view.findViewById(R.id.service_minus);
        message_minus = (ImageView) view.findViewById(R.id.message_minus);
        vendorsExpand = (ImageView) view.findViewById(R.id.VendorsExpand);

        manage_menu = (CustomTextView) view.findViewById(R.id.manage_menu);
        manage_table = (CustomTextView) view.findViewById(R.id.manage_table);
        business_setting_tab = (LinearLayout) view.findViewById(R.id.business_setting_tab);
        other_setting_tab = (LinearLayout) view.findViewById(R.id.other_setting_tab);
        manage_operation_tab = (LinearLayout) view.findViewById(R.id.manage_operation_tab);
//        serviceLinearLayout = (LinearLayout) view.findViewById(R.id.serviceLinearLayout);
        messageLinearLayout = (LinearLayout) view.findViewById(R.id.messageLinearLayout);
        reservation_btn = (CustomTextView) view.findViewById(R.id.reservation_btn);
        tvLeadTime = (CustomTextView) view.findViewById(R.id.tvLeadTime);
        table_btn = (CustomTextView) view.findViewById(R.id.table_btn);
        order_btn = (CustomTextView) view.findViewById(R.id.order_btn);
        feed_btn = (CustomTextView) view.findViewById(R.id.feed_btn);
        info_btn = (CustomTextView) view.findViewById(R.id.info_btn);
        service_btn = (CustomTextView) view.findViewById(R.id.service_btn);
        logo_btn = (CustomTextView) view.findViewById(R.id.logo_btn);
        schedule_btn = (CustomTextView) view.findViewById(R.id.schedule_btn);
        serviceChargesbtn = (CustomTextView) view.findViewById(R.id.service_charges);
        employlist = (CustomTextView) view.findViewById(R.id.employeelist);
        packagebtn = (CustomTextView) view.findViewById(R.id.packages);
        maketingoption = (CustomTextView) view.findViewById(R.id.marketing);
        staff = (CustomTextView) view.findViewById(R.id.staff);
        wholestaff = (CustomTextView) view.findViewById(R.id.wholestaff);
        dinemsg = (CustomTextView) view.findViewById(R.id.msgdine);
        security_tab = (LinearLayout) view.findViewById(R.id.security_layout);
        manage_service = (CustomTextView) view.findViewById(R.id.manage_service);
        packageAndMarketing = (CustomTextView) view.findViewById(R.id.packageAndMarketing);
        securityAccess = (CustomTextView) view.findViewById(R.id.securityAccess);
        securityExpand = (ImageView) view.findViewById(R.id.message_minus1);

        manage_coupons = (CustomTextView) view.findViewById(R.id.manage_coupons);
        vendor_orders_hist = (CustomTextView) view.findViewById(R.id.vendor_orders_hist);
        vendor_bid_hist = (CustomTextView) view.findViewById(R.id.vendor_bid_hist);
        tvVendors = (CustomTextView) view.findViewById(R.id.tvVendors);
        delivery_charge = (CustomTextView) view.findViewById(R.id.tvDelivery);
        vendors_tab = (LinearLayout) view.findViewById(R.id.vendors_tab);
    }
}




