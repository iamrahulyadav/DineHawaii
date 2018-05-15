package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Fragment.ChienesFragment;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.Model.MenuDetail;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.adapter.PlaceAnOrderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceAnOrder extends AppCompatActivity {
    private static final String TAG = "PlaceAnOrder";
    public static ArrayList<OrderItemsDetailsModel> itemsDetailsModels;
    public ArrayList<ListItem> achiv = new ArrayList<ListItem>();
    ListView listView;
    ImageView back;
    PlaceAnOrderAdapter adapter;
    ArrayList<ArrayList<MenuDetail>> menuArraylist = new ArrayList<ArrayList<MenuDetail>>();
    ArrayList<String> arrayListMainMenu = new ArrayList<>();
    ViewPager viewPager;
    private Context mContext;
    private String business_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        mContext = this;
        setToolbar();
        setTablayout();
        itemsDetailsModels = new ArrayList<>();
        business_id = getIntent().getStringExtra("business_id");
        Log.v(TAG, "Business Id :- " + business_id);
        getAllMenus();
    }

    private void getAllMenus() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_RESURANT_MENU_DETAILS);
        jsonObject.addProperty("business_id", business_id);
        Log.e(TAG, "Request GET ALL MENU >> " + jsonObject.toString());
        getResurant_menu_details(jsonObject);
    }

    @SuppressLint("LongLogTag")
    private void getResurant_menu_details(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(PlaceAnOrder.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "Response GET ALL MENU >> " + response.body().toString());
                String s = response.body().toString();
                MenuDetail menuDetail;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray main_menus = jsonObject.getJSONArray("main_menus");
                        for (int m = 0; m < main_menus.length(); m++) {
                            String mainMenuNames = main_menus.getString(m);
                            Log.e(TAG, "Json Menu :- " + mainMenuNames);
                            arrayListMainMenu.add(mainMenuNames);
                            Log.e(TAG + "<<<<ARRAY>>>", "" + arrayListMainMenu.toString());
                            JSONArray jsonArray_Details = jsonObject.getJSONArray("details");
                            Log.e(TAG + "<<<< JSON ARRAY DETAILS >>>", "" + jsonArray_Details.length());
                            for (int i = 0; i < jsonArray_Details.length(); i++) {
                                JSONObject jsonObject1 = jsonArray_Details.getJSONObject(i);
                                JSONArray jsonArray_yogurt = jsonObject1.getJSONArray(mainMenuNames);
                                Log.e(TAG, "of details :- " + jsonArray_yogurt.toString());
                                ArrayList<MenuDetail> submenuArraylist = new ArrayList<>();
                                for (int j = 0; j < jsonArray_yogurt.length(); j++) {
                                    menuDetail = new MenuDetail();
                                    JSONObject frozenYogurt = jsonArray_yogurt.getJSONObject(j);
                                    String mainName = arrayListMainMenu.get(m);
                                    String id = frozenYogurt.getString("id");
                                    String name = frozenYogurt.getString("name");
                                    String bus_id = frozenYogurt.getString("bus_id");
                                    String cat_id = frozenYogurt.getString("cat_id");
                                    String price = frozenYogurt.getString("price");
                                    String service_type = frozenYogurt.getString("service_type");
                                    String half_price = frozenYogurt.getString("half_price");
                                    String details = frozenYogurt.getString("detail");
                                    //price = price.replaceAll(".00", " ");
                                    price = price.replaceAll("\\.00", "");
                                    Log.v(TAG, "Main Menu Name :- " + mainMenuNames + "\n ~~~~~~~~~~ Id:- " + id + " bus id :- " + bus_id + " Name:- " + name + "Item Price :- " + price);
                                    menuDetail.setItemName(name);
                                    menuDetail.setItem_bus_id(bus_id);
                                    menuDetail.setItem_cat_id(cat_id);
                                    menuDetail.setItemId(id);
                                    menuDetail.setItem_price(price.trim());
                                    menuDetail.setService_type(service_type);
                                    menuDetail.setItem_half_price(half_price);
                                    menuDetail.setDetails(details);
                                    submenuArraylist.add(menuDetail);
                                }

                                menuArraylist.add(submenuArraylist);
                                Log.e("CART09", menuArraylist.toString());
                                //  Log.e("CART091",menuDetail.toString());
                            }
                        }

                        adapter = new PlaceAnOrderAdapter(getSupportFragmentManager(), mContext);
                        for (int i = 0; i < arrayListMainMenu.size(); i++) {
                            adapter.addFragment(new ChienesFragment(PlaceAnOrder.this, menuArraylist.get(i)), arrayListMainMenu.get(i));
                        }
                        viewPager.setAdapter(adapter);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray resultError = jsonObject.getJSONArray("result");
                        JSONObject object = resultError.getJSONObject(0);
                        if (object.getString("msg").equalsIgnoreCase("Some Error Occured")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlaceAnOrder.this);
                            alertDialog.setMessage("NO RECORDS FOUND");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            alertDialog.show();
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
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Menu");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setTablayout() {
        TabLayout searchTabLayout = (TabLayout) findViewById(R.id.tablayout_search);
        viewPager = (ViewPager) findViewById(R.id.viewpager_search);
        searchTabLayout.setupWithViewPager(viewPager);

//         setupViewPager(viewPager);
    }


}
