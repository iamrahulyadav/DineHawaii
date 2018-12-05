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
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.PlaceAnOrderAdapter;
import com.yberry.dinehawaii.Customer.Fragment.MenuFragment;
import com.yberry.dinehawaii.Model.MenuDetail;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.ProgressHUD;

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
    ImageView back;
    PlaceAnOrderAdapter adapter;
    ArrayList<ArrayList<MenuDetail>> menuArraylist = new ArrayList<ArrayList<MenuDetail>>();
    ArrayList<String> menuTypeList = new ArrayList<>();
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
        Log.e(TAG, "getAllMenus: Request >> " + jsonObject);

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
                try {
                    Log.e(TAG, "getAllMenus: Response >> " + response.body().toString());
                    String s = response.body().toString();
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray arrayMenuTypes = jsonObject.getJSONArray("main_menus");
                        for (int m = 0; m < arrayMenuTypes.length(); m++) {
                            menuTypeList.add(arrayMenuTypes.getString(m));
                            JSONArray arrayDetails = jsonObject.getJSONArray("details");
                            for (int i = 0; i < arrayDetails.length(); i++) {
                                JSONObject jsonObject1 = arrayDetails.getJSONObject(i);
                                JSONArray jsonArray = jsonObject1.getJSONArray(arrayMenuTypes.getString(m));
                                ArrayList<MenuDetail> menuList = new ArrayList<>();
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    MenuDetail data = new MenuDetail();
                                    JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                                    String id = jsonObject2.getString("id");
                                    String name = jsonObject2.getString("name");
                                    String bus_id = jsonObject2.getString("bus_id");
                                    String cat_id = jsonObject2.getString("cat_id");
                                    String price = jsonObject2.getString("price");
                                    String service_type = jsonObject2.getString("service_type");
                                    String half_price = jsonObject2.getString("half_price");
                                    String details = jsonObject2.getString("detail");
                                    String customization = "";
                                    if (jsonObject2.has("customization"))
                                        customization = jsonObject2.getString("customization");
                                    price = price.replaceAll("\\.00", "");
                                    Log.v(TAG, "Main Menu Name :- " + arrayMenuTypes.getString(m) + "\n ~~~~~~~~~~ Id:- " + id + " bus id :- " + bus_id + " Name:- " + name + "Item Price :- " + price);
                                    data.setItemName(name);
                                    data.setItem_bus_id(bus_id);
                                    data.setItem_cat_id(cat_id);
                                    data.setItemId(id);
                                    data.setItem_price(price.trim());
                                    data.setService_type(service_type);
                                    data.setItem_half_price(half_price);
                                    data.setDetails(details);
                                    data.setCustomizations(customization);
                                    menuList.add(data);
                                }

                                menuArraylist.add(menuList);
                                Log.e("CART09", menuArraylist.toString());
                            }
                        }

                        adapter = new PlaceAnOrderAdapter(getSupportFragmentManager(), mContext);
                        for (int i = 0; i < menuTypeList.size(); i++) {
                            adapter.addFragment(new MenuFragment(PlaceAnOrder.this, menuArraylist.get(i)), menuTypeList.get(i));
                        }
                        viewPager.setAdapter(adapter);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray resultError = jsonObject.getJSONArray("result");
                        JSONObject object = resultError.getJSONObject(0);
                        if (object.getString("msg").equalsIgnoreCase("Some Error Occured")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlaceAnOrder.this);
                            alertDialog.setMessage("Oops! No Records Found");
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
