package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Adapter.BidItemListAdapter;
import com.yberry.dinehawaii.vendor.Model.OtherVendorModel;
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidItemListActivity extends AppCompatActivity {
    private static final String TAG = "VendorFoodList";
    Context context;
    ArrayList<VendorMasterData> list;
    String category_id;
    private RecyclerView recycler_view;
    private BidItemListAdapter adapter;
    private CustomTextView counting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_item_list);
        setToolbar();
        list = new ArrayList<VendorMasterData>();
        if (getIntent().hasExtra("vendor_id"))
            category_id = getIntent().getStringExtra("vendor_id");
        init();
        setAdapter();
        if (Util.isNetworkAvailable(context)) {
            getAllVendorsFood();
        } else
            Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Bid Items");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rest_details_menu, menu);
        MenuItem menuItem1 = menu.findItem(R.id.action_map);
        menuItem1.setVisible(false);
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(menuItem, R.layout.cart_icon_layout);
        RelativeLayout actionView = (RelativeLayout) MenuItemCompat.getActionView(menuItem);
        ImageView imageView = (ImageView) actionView.findViewById(R.id.cartLayout);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, VendorCartActivity.class));
            }
        });
        counting = (CustomTextView) actionView.findViewById(R.id.counting);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart)
            startActivity(new Intent(context, VendorCartActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private void getAllVendorsFood() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.GETVENDORSMASTERFOODLIST);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        jsonObject.addProperty("category_id", category_id);
        Log.e(TAG, "getAllVendorsFood: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendors_list_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "onResponse: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        list.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            VendorMasterData model = new VendorMasterData();
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            model.setMaster_item_id(jsonObj.getString("item_id"));
                            model.setMaster_item_name(jsonObj.getString("item_name"));
                            model.setMaster_ven_prod_id(jsonObj.getString("vendor_product_id"));
                            model.setMaster_item_price(jsonObj.getString("item_price"));
                            model.setMaster_item_value(jsonObj.getString("item_value"));
                            model.setMaster_item_vend_id(jsonObj.getString("vendor_id"));
                            model.setMaster_item_vendor_name(jsonObj.getString("vendor_name"));
                            JSONArray jsonArrSubCat = jsonObj.getJSONArray("other_vendor");
                            ArrayList<OtherVendorModel> other_vendors_list = new ArrayList<OtherVendorModel>();
                            for (int j = 0; j < jsonArrSubCat.length(); j++) {
                                JSONObject jsonObj1 = jsonArrSubCat.getJSONObject(j);
                                OtherVendorModel model1 = new OtherVendorModel();
                                model1.setVendorName(jsonObj1.getString("vendor_name"));
                                model1.setItemPrice(jsonObj1.getString("item_price"));
                                other_vendors_list.add(model1);
                            }
                            model.setOther_vendors_list(other_vendors_list);
                            Log.e(TAG, "onResponse: model >> " + model.toString());
                            list.add(model);
                            Log.e(TAG, "onResponse: list >> " + list.toString());
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        context = this;
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private void setAdapter() {
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new BidItemListAdapter(context, list, category_id);
        recycler_view.setAdapter(adapter);
    }

}
