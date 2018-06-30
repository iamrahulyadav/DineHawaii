package com.yberry.dinehawaii.Customer.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.ChienesAdapter;
import com.yberry.dinehawaii.Customer.Adapter.MenuListAdapter;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.Model.MenuDetail;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurentDetailActivity extends AppCompatActivity {
    private static final String TAG = "RestaurentDetail";
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    public static MenuListAdapter adapter;
    CustomButton makeReservationButton, placeOrderButton;
    ImageView back;
    CustomTextView mName, mAddress, tvRecent_review, mContact;
    RatingBar myRatingBar;
    ArrayList<ListItem> listItems;
    String business_id;
    String business_name;
    String healt_status;
    String average_price;
    ArrayList<MenuDetail> menuArraylist = new ArrayList<>();
    ArrayList<String> arrayListMainMenu = new ArrayList<>();
    ArrayList<String> arrayListServices = new ArrayList<>();
    TextView nmenu, nservices;
    CustomTextView tvAverage, tvDeparment;
    LinearLayout viewLayout;
    Context context;
    private RecyclerView mRecyclerView, mRecyclerViewServices;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout registerlayout;
    private LinearLayout ll_section_food_type;
    private HorizontalScrollView ll_section_hours;
    private RelativeLayout view_food_plus, view_hours_plus, view_service_plus, viewmenu_list;
    private CustomTextView sunAD, sunBF, sunL, sunD, sunOther, monAD, monBF, monL, monD, monOther, tueAD, tueBF, tueL, tueD, tueOther,
            wedAD, wedBF, wedL, wedD, wedOther, thusAD, thusBF, thusL, thusD, thusOther,
            friAD, friBF, friL, friD, friOther, satAD, satBF, satL, satD, satOther;
    private LinearLayout ll_section_service;
    private ImageView view_food_image, view_hours_image, view_service_image;
    private TextView headet_text;
    private ChienesAdapter chineseadpater;
    private ArrayList<MenuDetail> arrayListCart;
    private CustomTextView tvCountBadge;

    private void getCounterData() {
        if (tvCountBadge != null)
            if (new DatabaseHandler(RestaurentDetailActivity.this).hasCartData()) {
                ArrayList<OrderItemsDetailsModel> cartItems = new DatabaseHandler(RestaurentDetailActivity.this).getCartItems(AppPreferences.getBusiID(RestaurentDetailActivity.this));  //database data
                Log.d("cartItems", String.valueOf(cartItems.size()));
                tvCountBadge.setText(String.valueOf(cartItems.size()));
            } else {
                tvCountBadge.setText("0");
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_detail);
        context = RestaurentDetailActivity.this;
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
        setToolbar();
        initView();
        initHours();
        initGui();

        setListener();
        listItems = new ArrayList<>();
        arrayListCart = new ArrayList<>();
        chineseadpater = new ChienesAdapter(this, arrayListCart);
        ListItem listItem = getIntent().getParcelableExtra("data");
        Log.e(TAG, "onCreate: Restaurant Data >> " + listItem);
        listItems.add(listItem);
        AppPreferences.setSelectedBusiLat(context, "" + listItem.getLatitude());
        AppPreferences.setSelectedBusiLong(context, "" + listItem.getLongitude());

        if (listItem.getType().equalsIgnoreCase("1")) {
            viewLayout.setVisibility(View.VISIBLE);
        } else if (listItem.getType().equalsIgnoreCase("0")) {
            viewLayout.setVisibility(View.GONE);
        }
        setValues(listItem);
        checkPackage();
        getMenus();
        getHours();
        getServices();
        getCounterData();

    }

    private void getServices() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GETBUSSINESSSERVICES);
        jsonObject.addProperty("business_id", business_id);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(this));
        Log.e(TAG, "Request GET SERVICES >> " + jsonObject.toString());
        getServiceJsonCall(jsonObject);
    }

    private void getServiceJsonCall(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(RestaurentDetailActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET SERVICES >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        nservices.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String service_name = jsonObject1.getString("service_name");
                            arrayListServices.add(service_name);
                        }

                        MenuListAdapter adapter1 = new MenuListAdapter(context, arrayListServices);
                        mRecyclerViewServices.setAdapter(adapter1);
                        adapter1.notifyDataSetChanged();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        nservices.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }


    private void initHours() {
        sunAD = ((CustomTextView) findViewById(R.id.txt_s1));
        sunBF = ((CustomTextView) findViewById(R.id.txt_s2));
        sunL = ((CustomTextView) findViewById(R.id.txt_s3));
        sunD = ((CustomTextView) findViewById(R.id.txt_s4));
        sunOther = ((CustomTextView) findViewById(R.id.txt_s5));
        monAD = ((CustomTextView) findViewById(R.id.txt_m1));
        monBF = ((CustomTextView) findViewById(R.id.txt_m2));
        monL = ((CustomTextView) findViewById(R.id.txt_m3));
        monD = ((CustomTextView) findViewById(R.id.txt_m4));
        monOther = ((CustomTextView) findViewById(R.id.txt_m5));
        tueAD = ((CustomTextView) findViewById(R.id.txt_tue1));
        tueBF = ((CustomTextView) findViewById(R.id.txt_tue2));
        tueL = ((CustomTextView) findViewById(R.id.txt_tue3));
        tueD = ((CustomTextView) findViewById(R.id.txt_tue4));
        tueOther = ((CustomTextView) findViewById(R.id.txt_tue5));
        wedAD = ((CustomTextView) findViewById(R.id.txt_w1));
        wedBF = ((CustomTextView) findViewById(R.id.txt_w2));
        wedL = ((CustomTextView) findViewById(R.id.txt_w3));
        wedD = ((CustomTextView) findViewById(R.id.txt_w4));
        wedOther = ((CustomTextView) findViewById(R.id.txt_w5));

        thusAD = ((CustomTextView) findViewById(R.id.txt_th1));
        thusBF = ((CustomTextView) findViewById(R.id.txt_th2));
        thusL = ((CustomTextView) findViewById(R.id.txt_th3));
        thusD = ((CustomTextView) findViewById(R.id.txt_th4));
        thusOther = ((CustomTextView) findViewById(R.id.txt_th5));

        friAD = ((CustomTextView) findViewById(R.id.txt_fri1));
        friBF = ((CustomTextView) findViewById(R.id.txt_fri2));
        friL = ((CustomTextView) findViewById(R.id.txt_fri3));
        friD = ((CustomTextView) findViewById(R.id.txt_fri4));
        friOther = ((CustomTextView) findViewById(R.id.txt_fri5));

        satAD = ((CustomTextView) findViewById(R.id.txt_sat1));
        satBF = ((CustomTextView) findViewById(R.id.txt_sat2));
        satL = ((CustomTextView) findViewById(R.id.txt_sat3));
        satD = ((CustomTextView) findViewById(R.id.txt_sat4));
        satOther = ((CustomTextView) findViewById(R.id.txt_sat5));
    }


    private void initGui() {
        view_food_plus = (RelativeLayout) findViewById(R.id.view_food_plus);
        // viewmenu_list = (RelativeLayout) findViewById(R.id.view_menu_list);
        ll_section_food_type = (LinearLayout) findViewById(R.id.ll_section_food_type);
        view_hours_plus = (RelativeLayout) findViewById(R.id.view_hours_plus);
        ll_section_hours = (HorizontalScrollView) findViewById(R.id.ll_section_hours);
        view_service_plus = (RelativeLayout) findViewById(R.id.view_service_plus);
        ll_section_service = (LinearLayout) findViewById(R.id.ll_section_service);

        view_food_image = (ImageView) findViewById(R.id.view_food_image);
        view_hours_image = (ImageView) findViewById(R.id.view_hours_image);
        view_service_image = (ImageView) findViewById(R.id.view_service_image);

        view_food_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_section_food_type.getVisibility() == View.VISIBLE) {
                    ll_section_food_type.setVisibility(View.GONE);
                    view_food_image.setImageResource(R.drawable.ic_keyboard_arrow_down_gray_24dp);
                } else {
                    ll_section_food_type.setVisibility(View.VISIBLE);
                    view_food_image.setImageResource(R.drawable.ic_keyboard_arrow_up_grey_24dp);
                }
            }
        });

        view_hours_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_section_hours.getVisibility() == View.VISIBLE) {
                    ll_section_hours.setVisibility(View.GONE);
                    view_hours_image.setImageResource(R.drawable.ic_keyboard_arrow_down_gray_24dp);
                } else {
                    ll_section_hours.setVisibility(View.VISIBLE);
                    view_hours_image.setImageResource(R.drawable.ic_keyboard_arrow_up_grey_24dp);
                }
            }
        });

        view_service_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_section_service.getVisibility() == View.VISIBLE) {
                    ll_section_service.setVisibility(View.GONE);
                    view_service_image.setImageResource(R.drawable.ic_keyboard_arrow_down_gray_24dp);

                } else {
                    ll_section_service.setVisibility(View.VISIBLE);
                    view_service_image.setImageResource(R.drawable.ic_keyboard_arrow_up_grey_24dp);
                }
            }
        });
    }

    private void getHours() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GETBUSSINESSTIME);
        jsonObject.addProperty("business_id", business_id);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(this));
        Log.e(TAG, "Request GET HOURS >> " + jsonObject.toString());
        getBusinessHours(jsonObject);

    }

    private void getBusinessHours(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET HOURS >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String monday_break = object.getString("monday_break");
                                String monday_lunch = object.getString("monday_lunch");
                                String monday_dinner = object.getString("monday_dinner");

                                if (monday_break.equalsIgnoreCase("-") || monday_break.equalsIgnoreCase(""))
                                    monBF.setText("NA");
                                else
                                    monBF.setText(monday_break);
                                if (monday_lunch.equalsIgnoreCase("-") || monday_lunch.equalsIgnoreCase(""))
                                    monL.setText("NA");
                                else
                                    monL.setText(monday_lunch);
                                if (monday_dinner.equalsIgnoreCase("-") || monday_dinner.equalsIgnoreCase(""))
                                    monD.setText("NA");
                                else
                                    monD.setText(monday_dinner);
                               /* monL.setText(monday_lunch);
                                monD.setText(monday_dinner);*/
                                /*========================================================================*/
                                String tuesday_start_break = object.getString("tuesday_break");
                                String tuesday_start_lunch = object.getString("tuesday_lunch");
                                String tuesday_start_dinner = object.getString("tuesday_dinner");
                                if (tuesday_start_break.equalsIgnoreCase("-") || tuesday_start_break.equalsIgnoreCase(""))
                                    tueBF.setText("NA");
                                else
                                    tueBF.setText(tuesday_start_break);
                                if (tuesday_start_lunch.equalsIgnoreCase("-") || tuesday_start_lunch.equalsIgnoreCase(""))
                                    tueL.setText("NA");
                                else
                                    tueL.setText(tuesday_start_lunch);
                                if (tuesday_start_dinner.equalsIgnoreCase("-") || tuesday_start_dinner.equalsIgnoreCase(""))
                                    tueD.setText("NA");
                                else
                                    tueD.setText(tuesday_start_dinner);
                               /* tueBF.setText(tuesday_start_break);
                                tueL.setText(tuesday_start_lunch);
                                tueD.setText(tuesday_start_dinner);*/
                                /*========================================================================*/
                                String wednesday_start_break = object.getString("wednesday_break");
                                String wednesday_start_lunch = object.getString("wednesday_lunch");
                                String wednesday_start_dinner = object.getString("wednesday_dinner");
                                if (wednesday_start_break.equalsIgnoreCase("-") || wednesday_start_break.equalsIgnoreCase(""))
                                    wedBF.setText("NA");
                                else
                                    wedBF.setText(wednesday_start_break);
                                if (wednesday_start_lunch.equalsIgnoreCase("-") || wednesday_start_lunch.equalsIgnoreCase(""))
                                    wedL.setText("NA");
                                else
                                    wedL.setText(wednesday_start_lunch);
                                if (wednesday_start_dinner.equalsIgnoreCase("-") || wednesday_start_dinner.equalsIgnoreCase(""))
                                    wedD.setText("NA");
                                else
                                    wedD.setText(wednesday_start_dinner);
                                /*wedBF.setText(wednesday_start_break);
                                wedL.setText(wednesday_start_lunch);
                                wedD.setText(wednesday_start_dinner);*/
                                /*========================================================================*/
                                String thursday_start_break = object.getString("thursday_break");
                                String thursday_start_lunch = object.getString("thursday_lunch");
                                String thursday_start_dinner = object.getString("thursday_dinner");

                                if (thursday_start_break.equalsIgnoreCase("-") || thursday_start_break.equalsIgnoreCase(""))
                                    thusBF.setText("NA");
                                else
                                    thusBF.setText(thursday_start_break);
                                if (thursday_start_lunch.equalsIgnoreCase("-") || thursday_start_lunch.equalsIgnoreCase(""))
                                    thusL.setText("NA");
                                else
                                    thusL.setText(thursday_start_lunch);
                                if (thursday_start_dinner.equalsIgnoreCase("-") || thursday_start_dinner.equalsIgnoreCase(""))
                                    thusD.setText("NA");
                                else
                                    thusD.setText(thursday_start_dinner);
                                /*thusBF.setText(thursday_start_break);
                                thusL.setText(thursday_start_lunch);
                                thusD.setText(thursday_start_dinner);*/
                                /*========================================================================*/
                                String friday_start_break = object.getString("friday_break");
                                String friday_start_lunch = object.getString("friday_lunch");
                                String friday_start_dinner = object.getString("friday_dinner");

                                if (friday_start_break.equalsIgnoreCase("-") || friday_start_break.equalsIgnoreCase(""))
                                    friBF.setText("NA");
                                else
                                    friBF.setText(friday_start_break);
                                if (friday_start_lunch.equalsIgnoreCase("-") || friday_start_lunch.equalsIgnoreCase(""))
                                    friL.setText("NA");
                                else
                                    friL.setText(friday_start_lunch);
                                if (friday_start_dinner.equalsIgnoreCase("-") || friday_start_dinner.equalsIgnoreCase(""))
                                    friD.setText("NA");
                                else
                                    friD.setText(friday_start_dinner);
                               /* friBF.setText(friday_start_break);
                                friL.setText(friday_start_lunch);
                                friD.setText(friday_start_dinner);*/
                                /*========================================================================*/
                                String saturday_start_break = object.getString("saturday_break");
                                String saturday_start_lunch = object.getString("saturday_lunch");
                                String saturday_start_dinner = object.getString("saturday_dinner");

                                if (saturday_start_break.equalsIgnoreCase("-") || saturday_start_break.equalsIgnoreCase(""))
                                    satBF.setText("NA");
                                else
                                    satBF.setText(saturday_start_break);
                                if (saturday_start_lunch.equalsIgnoreCase("-") || saturday_start_lunch.equalsIgnoreCase(""))
                                    satL.setText("NA");
                                else
                                    satL.setText(saturday_start_lunch);
                                if (saturday_start_dinner.equalsIgnoreCase("-") || saturday_start_dinner.equalsIgnoreCase(""))
                                    satD.setText("NA");
                                else
                                    satD.setText(saturday_start_dinner);
                                /*satBF.setText(saturday_start_break);
                                satL.setText(saturday_start_lunch);
                                satD.setText(saturday_start_dinner);*/
                                /*========================================================================*/
                                String sunday_start_break = object.getString("sunday_break");
                                String sunday_start_lunch = object.getString("sunday_lunch");
                                String sunday_start_dinner = object.getString("sunday_dinner");

                                if (sunday_start_break.equalsIgnoreCase("-") || sunday_start_break.equalsIgnoreCase(""))
                                    sunBF.setText("NA");
                                else
                                    sunBF.setText(sunday_start_break);
                                if (sunday_start_lunch.equalsIgnoreCase("-") || sunday_start_lunch.equalsIgnoreCase(""))
                                    sunL.setText("NA");
                                else
                                    sunL.setText(sunday_start_lunch);
                                if (sunday_start_dinner.equalsIgnoreCase("-") || sunday_start_dinner.equalsIgnoreCase(""))
                                    sunD.setText("NA");
                                else
                                    sunD.setText(sunday_start_dinner);
                               /* sunBF.setText(sunday_start_break);
                                sunL.setText(sunday_start_lunch);
                                sunD.setText(sunday_start_dinner);*/
                                /*========================================================================*/
                                String monday_all = object.getString("monday_all_break");
                                String tuesday_all = object.getString("tuesday_all_break");
                                String wednesday_all = object.getString("wednesday_all_break");
                                String thursday_all = object.getString("thursday_all_break");
                                String friday_all = object.getString("friday_all_break");
                                String saturday_all = object.getString("saturday_all_break");
                                String sun_all = object.getString("sunday_all_break");

                                if (monday_all.equalsIgnoreCase("-") || monday_all.equalsIgnoreCase(""))
                                    monAD.setText("NA");
                                else
                                    monAD.setText(monday_all);
                                if (tuesday_all.equalsIgnoreCase("-") || tuesday_all.equalsIgnoreCase(""))
                                    tueAD.setText("NA");
                                else
                                    tueAD.setText(tuesday_all);
                                if (wednesday_all.equalsIgnoreCase("-") || wednesday_all.equalsIgnoreCase(""))
                                    wedAD.setText("NA");
                                else
                                    wedAD.setText(wednesday_all);
                                if (thursday_all.equalsIgnoreCase("-") || thursday_all.equalsIgnoreCase(""))
                                    thusAD.setText("NA");
                                else
                                    thusAD.setText(thursday_all);
                                if (friday_all.equalsIgnoreCase("-") || friday_all.equalsIgnoreCase(""))
                                    friAD.setText("NA");
                                else
                                    friAD.setText(friday_all);
                                if (saturday_all.equalsIgnoreCase("-") || saturday_all.equalsIgnoreCase(""))
                                    satAD.setText("NA");
                                else
                                    satAD.setText(saturday_all);
                                if (sun_all.equalsIgnoreCase("-") || sun_all.equalsIgnoreCase(""))
                                    sunAD.setText("NA");
                                else
                                    sunAD.setText(sun_all);

                               /* sunAD.setText(sun_all);
                                monAD.setText(monday_all);
                                tueAD.setText(tuesday_all);
                                wedAD.setText(wednesday_all);
                                thusAD.setText(thursday_all);
                                friAD.setText(friday_all);
                                satAD.setText(saturday_all);*/
                                /*========================================================================*/
                                String mon_other_b = object.getString("monday_other_break");
                                String tues_other_b = object.getString("tuesday_other_break");
                                String wednes_other_b = object.getString("wednesday_other_break");
                                String thurs_other_b = object.getString("thursday_other_break");
                                String fri_other_b = object.getString("friday_other_break");
                                String sat_other_b = object.getString("saturday_other_break");
                                String sun_other_b = object.getString("sunday_other_break");

                                if (mon_other_b.equalsIgnoreCase("-") || mon_other_b.equalsIgnoreCase(""))
                                    monOther.setText("NA");
                                else
                                    monOther.setText(mon_other_b);
                                if (tues_other_b.equalsIgnoreCase("-") || tues_other_b.equalsIgnoreCase(""))
                                    tueOther.setText("NA");
                                else
                                    tueOther.setText(tues_other_b);
                                if (wednes_other_b.equalsIgnoreCase("-") || wednes_other_b.equalsIgnoreCase(""))
                                    wedOther.setText("NA");
                                else
                                    wedOther.setText(wednes_other_b);
                                if (thurs_other_b.equalsIgnoreCase("-") || thurs_other_b.equalsIgnoreCase(""))
                                    thusOther.setText("NA");
                                else
                                    thusOther.setText(thurs_other_b);
                                if (fri_other_b.equalsIgnoreCase("-") || fri_other_b.equalsIgnoreCase(""))
                                    friOther.setText("NA");
                                else
                                    friOther.setText(fri_other_b);
                                if (sat_other_b.equalsIgnoreCase("-") || sat_other_b.equalsIgnoreCase(""))
                                    satOther.setText("NA");
                                else
                                    satOther.setText(sat_other_b);
                                if (sun_other_b.equalsIgnoreCase("-") || sun_other_b.equalsIgnoreCase(""))
                                    sunOther.setText("NA");
                                else
                                    sunOther.setText(sun_other_b);

                               /* sunOther.setText(sun_other_b);
                                monOther.setText(mon_other_b);
                                tueOther.setText(tues_other_b);
                                wedOther.setText(wednes_other_b);
                                thusOther.setText(thurs_other_b);
                                satOther.setText(sat_other_b);
                                friOther.setText(fri_other_b);*/
                                /*========================================================================*/
                            }
                        } else {
                            Toast.makeText(RestaurentDetailActivity.this, "No Data found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG + "error", t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });

    }

    private void setValues(ListItem listItem) {
        headet_text.setText(listItem.getBusinessName());
        mName.setText(listItem.getBusinessName());
        mAddress.setText(listItem.getBusinessAddress());
        mContact.setText(listItem.getBusinessContactNo());
        myRatingBar.setRating(Float.parseFloat(listItem.getRating()));
        business_id = listItem.getId();
        business_name = listItem.getBusinessName();
        healt_status = listItem.getHealthCardStstus();
        average_price = listItem.getAvgPrice();
        Log.e(TAG, "setValues: average_price >> " + average_price);
        AppPreferencesBuss.setAveragePrice(context, listItem.getAvgPrice());
        tvDeparment.setText("Department of health report card : " + healt_status);
        tvAverage.setText("Average Price Range : " + average_price);
    }

    private void setListener() {
        tvRecent_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurentDetailActivity.this, CustomerResturantReviewAcivity.class);
                startActivity(intent);
            }
        });
        tvDeparment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://hi.healthinspections.us/hawaii/")));

            }
        });


        makeReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurentDetailActivity.this, MakingReservationActivity.class);
                System.out.println("BUSINESS ID " + " : " + business_id);
                intent.putExtra("business_id", business_id);
                intent.putExtra("business_name", business_name);
                startActivity(intent);
            }
        });

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RestaurentDetailActivity.this, PlaceAnOrder.class);
                in.putExtra("business_id", business_id);
                startActivity(in);

            }
        });
       /* viewmenu_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RestaurentDetailActivity.this, PlaceAnOrder.class);
                in.putExtra("business_id", business_id);
                startActivity(in);

            }
        });*/
    }

    private void checkPackage() {
        String package_list = AppPreferences.getBussPackageList(this);
        Log.e(TAG, "checkPackage: package_list >> " + package_list);
        if (package_list.contains("1") && package_list.contains("2"))
            makeReservationButton.setVisibility(View.VISIBLE);
        else
            makeReservationButton.setVisibility(View.GONE);
    }

    private void initView() {
        context = this;
        back = (ImageView) findViewById(R.id.back);
        tvRecent_review = (CustomTextView) findViewById(R.id.tvRecent_review);
        makeReservationButton = (CustomButton) findViewById(R.id.btnmake);
        placeOrderButton = (CustomButton) findViewById(R.id.tvPlace);
        mName = (CustomTextView) findViewById(R.id.mName);
        mAddress = (CustomTextView) findViewById(R.id.mAddress);
        mContact = (CustomTextView) findViewById(R.id.mContact);
        myRatingBar = (RatingBar) findViewById(R.id.myRatingBar);
        nmenu = (TextView) findViewById(R.id.nmenu);
        nservices = (TextView) findViewById(R.id.nservice);
        viewLayout = (LinearLayout) findViewById(R.id.viewlayout);
        tvAverage = (CustomTextView) findViewById(R.id.tvAverage);
        tvDeparment = (CustomTextView) findViewById(R.id.tvDeparment);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_menu);
        mRecyclerViewServices = (RecyclerView) findViewById(R.id.recycler_view_service);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        registerlayout = (LinearLayout) findViewById(R.id.registerlayout);
        ((CardView) findViewById(R.id.cardMenuList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RestaurentDetailActivity.this, PlaceAnOrder.class);
                in.putExtra("business_id", business_id);
                startActivity(in);
            }
        });
    }

    private void getMenus() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_RESURANT_MENU_DETAILS);
        jsonObject.addProperty("business_id", business_id);
        Log.e(TAG, "Request GET MENUS >> " + jsonObject.toString());
        getResurant_menu_details(jsonObject);
    }

    @SuppressLint("LongLogTag")
    private void getResurant_menu_details(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(RestaurentDetailActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET MENUS >> " + response.body().toString());
                String s = response.body().toString();
                MenuDetail menuDetail;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        nmenu.setVisibility(View.GONE);
                        registerlayout.setVisibility(View.VISIBLE);
                        JSONArray main_menus = jsonObject.getJSONArray("main_menus");
                        for (int m = 0; m < main_menus.length(); m++) {
                            String mainMenuNames = main_menus.getString(m);
                            Log.d(TAG, "Json Menu :- " + mainMenuNames);
                            arrayListMainMenu.add(mainMenuNames);
                            Log.d(TAG, arrayListMainMenu.toString());
                            JSONArray jsonArray_Details = jsonObject.getJSONArray("details");
                            Log.e(TAG + "<<<< JSON ARRAY DETAILS >>>", "" + jsonArray_Details.length());
                            for (int i = 0; i < jsonArray_Details.length(); i++) {
                                JSONObject jsonObject1 = jsonArray_Details.getJSONObject(i);
//                                JSONArray jsonArray_yogurt = jsonObject1.getJSONArray(arrayListMainMenu.get(m));
                                JSONArray jsonArray_yogurt = jsonObject1.getJSONArray(mainMenuNames);
                                Log.v(TAG, "Json Array of details :- " + jsonArray_yogurt.toString());
                                for (int j = 0; j < jsonArray_yogurt.length(); j++) {
                                    menuDetail = new MenuDetail();
                                    JSONObject frozenYogurt = jsonArray_yogurt.getJSONObject(j);
                                    String mainName = arrayListMainMenu.get(m);
                                    String id = frozenYogurt.getString("id");
                                    String name = frozenYogurt.getString("name");
                                    String bus_id = frozenYogurt.getString("bus_id");
                                    String cat_id = frozenYogurt.getString("cat_id");
                                    String price = frozenYogurt.getString("price");
                                    price = price.replaceAll(".00", " ");
                                    Log.v(TAG, "Main Menu Name :- " + mainMenuNames + "\n ~~~~~~~~~~ Id:- " + id + " bus id :- " + bus_id + " Name:- " + name + "Item Price :- " + price);
                                    menuDetail.setItemName(name);
                                    menuDetail.setItem_bus_id(bus_id);
                                    menuDetail.setItem_cat_id(cat_id);
                                    menuDetail.setItem_price(price.trim());
                                    menuDetail.setItemId(id);
                                    menuArraylist.add(menuDetail);
                                }
                            }
                        }
                        adapter = new MenuListAdapter(context, arrayListMainMenu);
                        Log.d("check90", arrayListMainMenu.toString());
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray resultError = jsonObject.getJSONArray("result");
                        JSONObject object = resultError.getJSONObject(0);
                        if (object.getString("msg").equalsIgnoreCase("Some Error Occured")) {
                            nmenu.setVisibility(View.VISIBLE);
                            registerlayout.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        headet_text = (TextView) findViewById(R.id.headet_text);
        headet_text.setText("Restaurant Details");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rest_details_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(menuItem, R.layout.cart_icon_layout);
        RelativeLayout actionView = (RelativeLayout) MenuItemCompat.getActionView(menuItem);
        ImageView imageView = (ImageView) actionView.findViewById(R.id.cartLayout);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, CartActivity.class));
            }
        });
        tvCountBadge = (CustomTextView) actionView.findViewById(R.id.counting);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        getCounterData();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCounterData();
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //   dial.setEnabled(true);
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.back) {
            finish();
        } else if (id == R.id.action_map) {
            Intent intent = new Intent(RestaurentDetailActivity.this, MapsActivity.class);
            intent.setAction("Restaurant");
            intent.putParcelableArrayListExtra("listItems", listItems);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_notification) {
            return true;
        } else if (id == R.id.action_cart) {
            Intent intent = new Intent(RestaurentDetailActivity.this, CartActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
