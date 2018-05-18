package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.VendorBidDBHandler;
import com.yberry.dinehawaii.database.VendorOrderDBHandler;
import com.yberry.dinehawaii.vendor.Adapter.VendorBidItemAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorBidItemModel;
import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorBidCartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "VendorBidCartActivity";
    CustomTextView total_amount, noItems;
    VendorBidItemAdapter adapter;
    CustomButton btnPlaceBid;
    ArrayList<VendorBidItemModel> cartItems;
    Context context;
    double amount, totalPrice = 0;
    LinearLayout mainView;
    RecyclerView mRecyclerView;
    RelativeLayout mainView2;
    private VendorBidDBHandler mydb;
    private LinearLayoutManager mLayoutManager;
    BroadcastReceiver updatePrice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (cartItems != null) {
                cartItems.clear();
            }
            getCartData();
        }
    };
    private String vendor_id = "0";
    private ArrayList<VendorBidItemModel> updatedcartItems;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_cart_bid);
        context = this;
        mydb = new VendorBidDBHandler(context);
        setToolbar();
        if (getIntent().hasExtra("vendor_id"))
            vendor_id = getIntent().getStringExtra("vendor_id");
        initViews();
        LocalBroadcastManager.getInstance(context).registerReceiver(updatePrice, new IntentFilter("updateTotalprice"));
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cartItems != null) {
            cartItems.clear();
        }
        getCartData();
    }

    private void getCartData() {
        if (mydb.hasCartData()) {
            cartItems = new VendorBidDBHandler(context).getBidCartItems();  //database data
            Log.e(TAG, "onCreate: cartItems >> " + cartItems);
            setCartAdapter();
            amount = Double.parseDouble(new VendorBidDBHandler(context).getOrderCartTotal());
            totalPrice = Double.parseDouble(new VendorBidDBHandler(context).getOrderCartTotal());
            total_amount.setText("" + totalPrice);
            AppPreferences.setPrice(context, "" + totalPrice);
            Log.e(TAG, "onCreate: totalPrice >> " + totalPrice);
        } else {
            mainView.setVisibility(View.GONE);
            mainView2.setVisibility(View.GONE);
            noItems.setVisibility(View.VISIBLE);
        }
    }

    private void setCartAdapter() {
        adapter = new VendorBidItemAdapter(context, cartItems, total_amount);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("LongLogTag")
    private void initViews() {
        btnPlaceBid = (CustomButton) findViewById(R.id.btnPlaceBid);
        btnPlaceBid.setOnClickListener(this);
        total_amount = (CustomTextView) findViewById(R.id.total_amount);
        noItems = (CustomTextView) findViewById(R.id.noitems);
        mainView2 = (RelativeLayout) findViewById(R.id.mainView2);
        mainView = (LinearLayout) findViewById(R.id.main_view);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Cart");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlaceBid) {
            showOrderAlert();
        } else if (v.getId() == R.id.fabadditem) {
            finish();
        }
    }

    private void showOrderAlert() {
        AlertDialog.Builder order_alert = new AlertDialog.Builder(context);
        order_alert.setTitle("Place Order");
        order_alert.setMessage("Are you sure you want to place this Bid.");
        order_alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updatedcartItems = new VendorBidDBHandler(context).getfinalBidCartItems();  //database data
                Log.e(TAG, "onClick: updatedcartItems>>>>>"+updatedcartItems.toString() );
                placeBid();
            }
        });
        order_alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        order_alert.show();
    }

    private void placeBid() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_VENDOR_API.PlACEVENDORORDER);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < updatedcartItems.size(); i++) {
            VendorBidItemModel model = updatedcartItems.get(i);
            JsonObject orderDetailsObject = new JsonObject();
            orderDetailsObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
            orderDetailsObject.addProperty("vendor_id", vendor_id);
            orderDetailsObject.addProperty("vendor_product_id", model.getProduct_id());
            orderDetailsObject.addProperty("item_id", model.getItem_id());
            orderDetailsObject.addProperty("item_quantity", model.getVendor_item_qty());
            orderDetailsObject.addProperty("item_amount", model.getVendor_item_price());
            orderDetailsObject.addProperty("business_bid_amt", model.getBus_item_total_cost());
            orderDetailsObject.addProperty("vendor_bid_amount", model.getVendor_item_total_cost());
            jsonArray.add(orderDetailsObject);
            Log.e(TAG, orderDetailsObject.toString());
        }

        jsonObject.add("orderDetails", jsonArray);

        Log.e(TAG, "Request Place bid >>> " + jsonObject.toString());

        placeBidTask(jsonObject);
    }

    private void placeBidTask(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendorOrderUrl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e(TAG, "Response Place Order >>> " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        mydb = new VendorBidDBHandler(context);
                       // mydb.deleteVendorCartTtem(vendor_id);
                        showThankYouAlert("Your bid placed successfully.");
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Log.e("onResponse", object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ERROR", "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    private void showThankYouAlert(String msg) {
        AlertDialog.Builder th_alert = new AlertDialog.Builder(context);
        th_alert.setTitle("Thank You!");
        th_alert.setMessage(msg);
        th_alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, BusinessNaviDrawer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        th_alert.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(updatePrice);
    }
}
