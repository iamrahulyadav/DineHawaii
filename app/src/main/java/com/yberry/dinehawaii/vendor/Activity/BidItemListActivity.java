package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.yberry.dinehawaii.database.VendorBidDBHandler;
import com.yberry.dinehawaii.vendor.Adapter.BidItemListAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorBidItemModel;

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
    ArrayList<VendorBidItemModel> list;
    String category_id;
    private RecyclerView recycler_view;
    private BidItemListAdapter adapter;
    private CustomTextView tvCountBadge;
    private CartItemReciver reciver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_item_list);
        setToolbar();
        reciver = new CartItemReciver();
        LocalBroadcastManager.getInstance(this).registerReceiver(reciver, new IntentFilter("update_cart"));
        list = new ArrayList<VendorBidItemModel>();
        if (getIntent().hasExtra("vendor_id"))
            category_id = getIntent().getStringExtra("vendor_id");
        init();
        setAdapter();

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
                startActivity(new Intent(context, VendorBidCartActivity.class));
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

    private void getCounterData() {
        if (tvCountBadge != null) ;
        if (new VendorBidDBHandler(BidItemListActivity.this).hasCartData()) {
            ArrayList<VendorBidItemModel> cartItems = new VendorBidDBHandler(BidItemListActivity.this).getOrderCartItems();
            Log.e(TAG, "getCounterData: items >> " + String.valueOf(cartItems.size()));
            tvCountBadge.setText(String.valueOf(cartItems.size()));
        } else {
            tvCountBadge.setText("0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isNetworkAvailable(context)) {
            getAllVendorsFood();
        } else
            Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
        if (tvCountBadge != null)
            getCounterData();
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
                            VendorBidItemModel model = new VendorBidItemModel();
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            model.setItem_id(jsonObj.getString("item_id"));
                            model.setItem_name(jsonObj.getString("item_name"));
                            model.setProduct_id(jsonObj.getString("vendor_product_id"));
                            model.setVendor_item_price(jsonObj.getString("item_price"));
                            model.setVendor_item_total_cost(jsonObj.getString("item_price"));
                            model.setVendor_id(jsonObj.getString("vendor_id"));
                            model.setVendor_name(jsonObj.getString("vendor_name"));
                            model.setVendor_item_qty("1");
                            JSONArray jsonArrSubCat = jsonObj.getJSONArray("other_vendor");

                            ArrayList<VendorBidItemModel> other_vendors_list = new ArrayList<VendorBidItemModel>();
                            for (int j = 0; j < jsonArrSubCat.length(); j++) {
                                JSONObject jsonObj1 = jsonArrSubCat.getJSONObject(j);
                                VendorBidItemModel model2 = new VendorBidItemModel();

                                model2.setProduct_id(jsonObj1.getString("vendor_product_id"));
                                model2.setItem_id(jsonObj.getString("item_id"));
                                model2.setItem_name(jsonObj.getString("item_name"));
                                model2.setVendor_item_price(jsonObj1.getString("item_price"));
                                model2.setVendor_id(jsonObj1.getString("vendor_id"));
                                model2.setVendor_name(jsonObj1.getString("vendor_name"));
                                model2.setVendor_item_total_cost(jsonObj1.getString("item_price"));
                                model2.setVendor_item_qty("1");

                                other_vendors_list.add(model2);
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

    class CartItemReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: cartItemReciever ");
            getCounterData();
        }
    }
}
