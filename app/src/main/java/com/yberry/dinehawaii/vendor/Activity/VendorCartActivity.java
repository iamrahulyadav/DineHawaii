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
import com.yberry.dinehawaii.database.VendorOrderDBHandler;
import com.yberry.dinehawaii.vendor.Adapter.VendorCartItemAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorCartActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CartActivity";
    CustomTextView total_amount, noItems;
    VendorCartItemAdapter adapter;
    CustomButton btnplaceOrder;
    ArrayList<VendorOrderItemsDetailsModel> cartItems;
    Context context;
    double amount, totalPrice = 0;
    LinearLayout mainView;
    RecyclerView mRecyclerView;
    RelativeLayout mainView2;
    private VendorOrderDBHandler mydb;
    private LinearLayoutManager mLayoutManager;
    private String vendor_id = "0";
    BroadcastReceiver updatePrice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            amount = Double.parseDouble(new VendorOrderDBHandler(context).getOrderCartTotal(vendor_id));
            totalPrice = Double.parseDouble(new VendorOrderDBHandler(context).getOrderCartTotal(vendor_id));
            total_amount.setText("" + totalPrice);
            AppPreferences.setPrice(context, "" + totalPrice);
            Log.e(TAG, "onReceive: totalPrice >> " + totalPrice);
            if (amount == 0.0 || amount == 0) {
                mainView.setVisibility(View.GONE);
                mainView2.setVisibility(View.GONE);
                noItems.setVisibility(View.VISIBLE);
            }
        }
    };
    private ArrayList<VendorOrderItemsDetailsModel> updatedcartItems;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_cart);
        context = this;
        mydb = new VendorOrderDBHandler(context);
        setToolbar();
        initViews();
        if (getIntent().hasExtra("vendor_id"))
            vendor_id = getIntent().getStringExtra("vendor_id");
        else
            vendor_id = "806";
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

        if (mydb.hasCartData(vendor_id)) {
            cartItems = new VendorOrderDBHandler(context).getOrderCartItems(vendor_id);  //database data
            Log.e(TAG, "onCreate: cartItems >> " + cartItems);
            setCartAdapter();
            amount = Double.parseDouble(new VendorOrderDBHandler(context).getOrderCartTotal(vendor_id));
            totalPrice = Double.parseDouble(new VendorOrderDBHandler(context).getOrderCartTotal(vendor_id));
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
        adapter = new VendorCartItemAdapter(context, cartItems, total_amount);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("LongLogTag")
    private void initViews() {
        btnplaceOrder = (CustomButton) findViewById(R.id.btnPlaceOrder);
        btnplaceOrder.setOnClickListener(this);
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
        if (v.getId() == R.id.btnPlaceOrder) {
            //showOrderAlert();
            Intent intent = new Intent(getApplicationContext(), VendorCheckOutActivity.class);
            intent.putExtra("totalamount", total_amount.getText().toString());
            intent.putExtra("vendor_id", vendor_id);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(updatePrice);
    }
}
