package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.VendorBidDBHandler;
import com.yberry.dinehawaii.vendor.Adapter.VendorBidItemAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorBidItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidCartActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "BidCartActivity";
    CustomTextView noItems;
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
    private ArrayList<VendorBidItemModel> updatedcartItems;
    private CustomEditText etEndDate;
    private Dialog otherDetailsDialog;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_cart_bid);
        context = this;
        mydb = new VendorBidDBHandler(context);
        setToolbar();
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
            AppPreferences.setPrice(context, "" + totalPrice);
            Log.e(TAG, "onCreate: totalPrice >> " + totalPrice);
        } else {
            mainView.setVisibility(View.GONE);
            mainView2.setVisibility(View.GONE);
            noItems.setVisibility(View.VISIBLE);
        }
    }

    private void setCartAdapter() {
        adapter = new VendorBidItemAdapter(context, cartItems);
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
            showOtherDetailsDialog();
        }
    }

    private void showOtherDetailsDialog() {
        otherDetailsDialog = new Dialog(context);
        otherDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otherDetailsDialog.setCancelable(true);
        otherDetailsDialog.setContentView(R.layout.place_bid_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(otherDetailsDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        otherDetailsDialog.getWindow().setAttributes(lp);
        final CustomEditText etOrderFreq = (CustomEditText) otherDetailsDialog.findViewById(R.id.etOrderFreq);
        CustomEditText etStartDate = (CustomEditText) otherDetailsDialog.findViewById(R.id.etStartDate);
        etEndDate = (CustomEditText) otherDetailsDialog.findViewById(R.id.etEndDate);
        final CustomEditText etTermsCondt = (CustomEditText) otherDetailsDialog.findViewById(R.id.etTermsCondt);
        CustomButton btnSubmitBid = (CustomButton) otherDetailsDialog.findViewById(R.id.btnSubmitBid);

        etStartDate.setText(Function.getCurrentDateNew() + "");
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        BidCartActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setCancelColor(getResources().getColor(R.color.colorPrimary));
                dpd.setOkColor(getResources().getColor(R.color.colorPrimary));
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis() - 1000);
                dpd.setMinDate(c);
            }
        });
        btnSubmitBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etOrderFreq.getText().toString()))
                    Toast.makeText(context, "Enter Order Frequency", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(etEndDate.getText().toString()))
                    Toast.makeText(context, "Select End Date", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(etTermsCondt.getText().toString()))
                    Toast.makeText(context, "Enter your Terms & Conditions", Toast.LENGTH_SHORT).show();
                else
                    showBidAlert();
            }
        });
        if (!BidCartActivity.this.isFinishing())
            otherDetailsDialog.show();
    }

    private void showBidAlert() {
        AlertDialog.Builder order_alert = new AlertDialog.Builder(context);
        order_alert.setTitle("Place Order");
        order_alert.setMessage("Are you sure you want to place this Bid.");
        order_alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updatedcartItems = new VendorBidDBHandler(context).getfinalBidCartItems();  //database data
                Log.e(TAG, "onClick: updatedcartItems>>>>>" + updatedcartItems.toString());
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
        jsonObject.addProperty("method", AppConstants.BUSINESS_VENDOR_API.PLACEBID);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < updatedcartItems.size(); i++) {
            VendorBidItemModel model = updatedcartItems.get(i);
            JsonObject orderDetailsObject = new JsonObject();
            orderDetailsObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
            orderDetailsObject.addProperty("vendor_id", model.getVendor_id());
            orderDetailsObject.addProperty("vendor_product_id", model.getProduct_id());
            orderDetailsObject.addProperty("item_id", model.getItem_id());
            orderDetailsObject.addProperty("item_quantity", model.getVendor_item_qty());
            orderDetailsObject.addProperty("item_amount", model.getVendor_item_price());
            orderDetailsObject.addProperty("business_bid_amt", model.getBus_item_total_cost());
            orderDetailsObject.addProperty("vendor_bid_amount", model.getVendor_item_total_cost());
            jsonArray.add(orderDetailsObject);
            Log.e(TAG, orderDetailsObject.toString());
        }

        jsonObject.add("bidDetails", jsonArray);

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
                        mydb.deleteCartitem();
                        otherDetailsDialog.dismiss();
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
        Log.e(TAG, "You picked the following Date: " + date);
        etEndDate.setText(date);
    }

}
