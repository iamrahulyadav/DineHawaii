package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yberry.dinehawaii.Customer.Adapter.CheckOutItemAdapter;
import com.yberry.dinehawaii.Customer.Adapter.OffersAdapter;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomCheckBox;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomRadioButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.AppPreferences.getBusiID;
import static com.yberry.dinehawaii.Util.AppPreferences.getCustomerid;
import static com.yberry.dinehawaii.Util.AppPreferences.setRadioValue;
import static com.yberry.dinehawaii.Util.Function.fieldRequired;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConstants.PAYPAL_CLIENT_ID);
    CustomTextView totalPrice, loylityBal, getax, CustPhn, CustAddr, CustOrTime, CustOrDate, tvTotalAmt, TvTotalWithTax, food_prepration_time;
    CustomEditText loyality_apply, giftcouponcode, custName, couponCodeText;
    CustomButton applygiftbtn, removeegift, applyLoyaltyPoints, removePoints, apply_coupon, remove_coupon;
    String totalAmount = "0", cateringDateTime = "", TAG = "CheckOutActivity";
    RelativeLayout proceed, mainLayout;
    LinearLayout rlloyalty, rlegiftcard, rlcoupons;
    View view;
    AlertDialog.Builder builder;
    AlertDialog deliveryDialog;
    CustomRadioButton yesRadioButton, noRadioButton;
    CustomCheckBox homedelivery_btn, take_way_btn, inhouse_btn, catering_btn;
    RadioGroup radioGroup, radioCredits;
    String timeInhouse = "", timeTakeout = "", timeCatering = "", timeDelivery = "";
    String order_type = "0", radioValue = "0", egiftamount = "0", egiftcoupon = "", egiftid = "", order_timings = "", amount = "0";
    int loyalityTotal = 0;
    ArrayList<OrderItemsDetailsModel> cartItemsList;
    LinearLayout custNmLayout, custPhnLayout, custTimeLayout, custDtLayout, custAddLayout, custPreptimeLayout, llloyaltypt;
    double pointsloyalty, amountofPoints;
    Context context;
    OffersAdapter couponAdapter;
    ArrayList<CustomerModel> egiftModelsList = new ArrayList<>();
    ArrayList<CustomerModel> couponsModelsList = new ArrayList<>();
    RadioButton rd_loylty, rd_egift, rd_coupon;
    RecyclerView mRecyclerView;
    private String coupon_type;
    private String coupon_name, coupon_id = "0";
    private String minOrderValue = "0", takeOut_lead_time, catering_lead_days;
    private ArrayList<OrderItemsDetailsModel> cartItems;
    private EditText daddress;
    private String latitude = "0.0";
    private String longitude = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        context = this;
        setToolbar();
        init();
        getGETax();
        checkPackage();
        cartItems = new DatabaseHandler(context).getCartItems(AppPreferences.getBusiID(context));  //database data
        Log.d("CARTITEMS", cartItems.toString());
        setCartAdapter();
        setLoyalityPoints();
        updateHomeDeliveryInfo();
        getFoodPrepTime();
        custName.requestFocus();
    }

    private void setCartAdapter() {
        CheckOutItemAdapter adapter = new CheckOutItemAdapter(CheckOutActivity.this, cartItems);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CheckOutActivity.this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);
    }

    private void getFoodPrepTime() {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.FOOD_PREP_TIME);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));
            Log.e(TAG, "get food prep time" + jsonObject.toString());
            getFoodTimeApi(jsonObject);
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getFoodTimeApi(JsonObject jsonObject) {
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String s = response.body().toString();
                Log.e(TAG, "get food prep time resp" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = resultJsonArray.getJSONObject(0);
                        timeInhouse = object.getString("inhouse_min");
                        timeCatering = object.getString("catering_min");
                        timeDelivery = object.getString("takeout_del_min");
                        timeTakeout = object.getString("takeout_min");
                        takeOut_lead_time = object.getString("takeout_lead_time");
                        catering_lead_days = object.getString("catering_days");

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error" + t.getMessage());
            }
        });
    }

    private void init() {
        cartItemsList = new ArrayList<>();
        cartItemsList.clear();
        totalPrice = (CustomTextView) findViewById(R.id.totalAmount);
        tvTotalAmt = (CustomTextView) findViewById(R.id.totalCost);
        TvTotalWithTax = (CustomTextView) findViewById(R.id.totalCostTax);
        if (!getIntent().getStringExtra("totalamount").equalsIgnoreCase("")) {
            totalAmount = getIntent().getStringExtra("totalamount");
            tvTotalAmt.setText("$" + totalAmount);
        }
        if (getIntent().getParcelableArrayListExtra("place_order") != null) {
            cartItemsList = getIntent().getParcelableArrayListExtra("place_order");
        }
        couponCodeText = (CustomEditText) findViewById(R.id.couponCodeText);
        getax = (CustomTextView) findViewById(R.id.getax);
        food_prepration_time = (CustomTextView) findViewById(R.id.prepTime);
        loylityBal = (CustomTextView) findViewById(R.id.loylityBal);
        loyality_apply = (CustomEditText) findViewById(R.id.loyaliy_apply);
        giftcouponcode = (CustomEditText) findViewById(R.id.couponApplied);
        applygiftbtn = (CustomButton) findViewById(R.id.applygift);
        removeegift = (CustomButton) findViewById(R.id.removeEgift);
        applyLoyaltyPoints = (CustomButton) findViewById(R.id.applypoints);
        removePoints = (CustomButton) findViewById(R.id.removepoints);
        apply_coupon = (CustomButton) findViewById(R.id.applycoupon);
        remove_coupon = (CustomButton) findViewById(R.id.removecoupon);
        radioGroup = (RadioGroup) findViewById(R.id.radioGrp);
        radioCredits = (RadioGroup) findViewById(R.id.radioCredits);
        take_way_btn = (CustomCheckBox) findViewById(R.id.take_way_btn);
        homedelivery_btn = (CustomCheckBox) findViewById(R.id.homedelivery_btn);
        proceed = (RelativeLayout) findViewById(R.id.proceedtopay);
        mainLayout = (RelativeLayout) findViewById(R.id.mainCheckout);
        yesRadioButton = (CustomRadioButton) findViewById(R.id.yesRadioButton);
        noRadioButton = (CustomRadioButton) findViewById(R.id.noRadioButton);
        inhouse_btn = (CustomCheckBox) findViewById(R.id.inhouseradio);
        catering_btn = (CustomCheckBox) findViewById(R.id.cateringradio);
        custNmLayout = (LinearLayout) findViewById(R.id.cust1);
        custPhnLayout = (LinearLayout) findViewById(R.id.cust2);
        custAddLayout = (LinearLayout) findViewById(R.id.cust3);
        custTimeLayout = (LinearLayout) findViewById(R.id.cust4);
        custDtLayout = (LinearLayout) findViewById(R.id.cust5);
        custPreptimeLayout = (LinearLayout) findViewById(R.id.prepLayout);
        custName = (CustomEditText) findViewById(R.id.custname);
        CustPhn = (CustomTextView) findViewById(R.id.custContact);
        CustAddr = (CustomTextView) findViewById(R.id.custAddress);
        CustOrTime = (CustomTextView) findViewById(R.id.custTime);
        CustOrDate = (CustomTextView) findViewById(R.id.custDate);
        llloyaltypt = (LinearLayout) findViewById(R.id.llloyalty);
        rlloyalty = (LinearLayout) findViewById(R.id.relloyalty);
        rlegiftcard = (LinearLayout) findViewById(R.id.relegift);
        rlcoupons = (LinearLayout) findViewById(R.id.relcoupon);
        rd_egift = (RadioButton) findViewById(R.id.radio_egift);
        rd_loylty = (RadioButton) findViewById(R.id.radio_loyalty);
        rd_coupon = (RadioButton) findViewById(R.id.radio_coupon);
        homedelivery_btn.setOnClickListener(this);
        take_way_btn.setOnClickListener(this);
        inhouse_btn.setOnClickListener(this);
        catering_btn.setOnClickListener(this);
        removeegift.setOnClickListener(this);
        applygiftbtn.setOnClickListener(this);
        proceed.setOnClickListener(this);
        applyLoyaltyPoints.setOnClickListener(this);
        removePoints.setOnClickListener(this);
        apply_coupon.setOnClickListener(this);
        remove_coupon.setOnClickListener(this);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = getCurrentFocus();
                if (v != null) {
                    hideKeyboard();
                }
                return false;
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.yesRadioButton) {
                    radioValue = "1";
                } else if (checkedId == R.id.noRadioButton) {
                    radioValue = "0";
                }
                setRadioValue(context, radioValue);
            }
        });

        radioCredits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_loyalty) {
                    llloyaltypt.setVisibility(View.VISIBLE);
                    rlloyalty.setVisibility(View.VISIBLE);
                    giftcouponcode.setText("");
                    couponCodeText.setText("");
                    rlcoupons.setVisibility(View.GONE);
                    rlegiftcard.setVisibility(View.GONE);
                } else if (checkedId == R.id.radio_egift) {
                    rlegiftcard.setVisibility(View.VISIBLE);
                    llloyaltypt.setVisibility(View.GONE);
                    rlloyalty.setVisibility(View.GONE);
                    loyality_apply.setText("");
                    couponCodeText.setText("");
                    rlcoupons.setVisibility(View.GONE);
                } else if (checkedId == R.id.radio_coupon) {
                    rlcoupons.setVisibility(View.VISIBLE);
                    rlegiftcard.setVisibility(View.GONE);
                    giftcouponcode.setText("");
                    loyality_apply.setText("");
                    llloyaltypt.setVisibility(View.GONE);
                    rlloyalty.setVisibility(View.GONE);
                }
            }
        });
        getAllEgifts();
        getAllCoupons();
        giftcouponcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (egiftModelsList.isEmpty() || egiftModelsList == null) {
                    Toast.makeText(context, "No E-Gift Cards Available", Toast.LENGTH_SHORT).show();
                } else {
                    offersDialog();
                    couponAdapter = new OffersAdapter(context, egiftModelsList);
                    mRecyclerView.setAdapter(couponAdapter);
                }
            }
        });
        couponCodeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (couponsModelsList.isEmpty() || couponsModelsList == null) {
                    Toast.makeText(context, "No Coupons Available", Toast.LENGTH_SHORT).show();
                } else {
                    offersDialog();
                    couponAdapter = new OffersAdapter(context, couponsModelsList);
                    mRecyclerView.setAdapter(couponAdapter);
                }
            }
        });
    }

    public void offersDialog() {
        final Dialog mDialog;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.offers_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.gravity = Gravity.BOTTOM;
        mDialog.getWindow().setAttributes(lp);
        LinearLayoutManager mLayoutManager;
        mRecyclerView = (RecyclerView) mDialog.findViewById(R.id.offers_recycler);
        ImageView cancel = (ImageView) mDialog.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CustomerModel model;
                if (radioCredits.getCheckedRadioButtonId() == R.id.radio_egift) {
                    model = egiftModelsList.get(position);
                    giftcouponcode.setText(model.getOffer_name());
                } else if (radioCredits.getCheckedRadioButtonId() == R.id.radio_coupon) {
                    model = couponsModelsList.get(position);
                    minOrderValue = model.getMinOrderAmt();

                    if (Double.parseDouble(amount) >= Double.parseDouble(minOrderValue))
                        couponCodeText.setText(model.getOffer_name());
                    else
                        Snackbar.make(findViewById(android.R.id.content), "This coupon can't be applied min order amount must be $" + minOrderValue, Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                }
                mDialog.cancel();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceedtopay:
                proceedToPayment();
                break;
            case R.id.homedelivery_btn:
                homedelivery_btn.setChecked(true);
                inhouse_btn.setChecked(false);
                catering_btn.setChecked(false);
                take_way_btn.setChecked(false);
                deliveryDialog.show();
                break;
            case R.id.take_way_btn:
                order_type = "11";
                take_way_btn.setChecked(true);
                inhouse_btn.setChecked(false);
                catering_btn.setChecked(false);
                homedelivery_btn.setChecked(false);
                AppPreferences.setOrderType(context, order_type);
                setTimePicker();
                break;
            case R.id.cateringradio:
                catering_btn.setChecked(true);
                inhouse_btn.setChecked(false);
                homedelivery_btn.setChecked(false);
                take_way_btn.setChecked(false);
                cateringOrder();
                break;
            case R.id.inhouseradio:
                inhouse_btn.setChecked(true);
                homedelivery_btn.setChecked(false);
                catering_btn.setChecked(false);
                take_way_btn.setChecked(false);
                order_type = "1";
                AppPreferences.setDeliveryName(context, AppPreferences.getCustomername(context));
                AppPreferences.setDeliveryContact(context, AppPreferences.getCustomerMobile(context));
                AppPreferences.setDeliveryAddress(context, "In-House Order");
                AppPreferences.setOrderType(context, order_type);
                setTimePicker();
                break;
            case R.id.applygift:
                applyEgift();
                break;
            case R.id.removeEgift:
                removeEgiftMethod();
                break;
            case R.id.applypoints:
                applyPointsMethod();
                break;
            case R.id.removepoints:
                removepoints();
                break;
            case R.id.applycoupon:
                applyCouponCode();
                break;
            case R.id.removecoupon:
                removeCouponCode();
                break;
            default:
                break;
        }
    }

    private void removeCouponCode() {
        couponCodeText.setText("");
        couponCodeText.setEnabled(true);
        apply_coupon.setVisibility(View.VISIBLE);
        remove_coupon.setVisibility(View.GONE);
        rd_loylty.setEnabled(true);
        rd_egift.setEnabled(true);
        totalPrice.setText(amount);
    }

    private void applyCouponCode() {
        if (TextUtils.isEmpty(couponCodeText.getText().toString())) {
            Toast.makeText(context, "Select Coupon Code", Toast.LENGTH_LONG).show();
        } else {
            if (Util.isNetworkAvailable(context)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.GENERALAPI.CHECK_COUPON);
                jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
                jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));
                jsonObject.addProperty("coupon_code", couponCodeText.getText().toString());
                Log.e(TAG, "coupon apply json" + jsonObject.toString());
                applyCouponCodeApi(jsonObject);
            } else {
                Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setFoodPrepTime(String type) {
        if (type.equalsIgnoreCase("1111")) {
            food_prepration_time.setText(timeDelivery + " mins");
        } else if (type.equalsIgnoreCase("1")) {
            food_prepration_time.setText(timeInhouse + " mins");
        } else if (type.equalsIgnoreCase("11")) {
            food_prepration_time.setText(timeTakeout + " mins");
        } else if (type.equalsIgnoreCase("111")) {
            food_prepration_time.setText(timeCatering + " mins");
        } else {
            food_prepration_time.setText("");
        }
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Checkout");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void removepoints() {
        int points = Integer.parseInt(loyality_apply.getText().toString());
        int balance = Integer.parseInt(loylityBal.getText().toString()) + points;
        double total = Double.parseDouble(totalPrice.getText().toString()) + points;
        Log.e(TAG, " remove points" + points);
        Log.e(TAG, "balance" + balance);
        Log.e(TAG, "total" + total);

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        double grandtotal = Double.valueOf(twoDForm.format(total));
        totalPrice.setText(amount);
        loylityBal.setText(balance + "");
        loyality_apply.setText("");
        applyLoyaltyPoints.setVisibility(View.VISIBLE);
        removePoints.setVisibility(View.GONE);
        rd_egift.setEnabled(true);
        rd_coupon.setEnabled(true);
    }

    private void applyPointsMethod() {
        if (TextUtils.isEmpty(loyality_apply.getText().toString())) {
            Toast.makeText(context, "Enter points", Toast.LENGTH_LONG).show();
        } else {
            if (Double.parseDouble(loyality_apply.getText().toString()) < Double.parseDouble(totalPrice.getText().toString())) {
                int points = Integer.parseInt(loyality_apply.getText().toString());
                int balance = loyalityTotal - points;
                double totalloyalAmount = Double.parseDouble(loyality_apply.getText().toString()) / pointsloyalty;
                double finalprice = totalloyalAmount * amountofPoints;
                double total = Double.parseDouble(totalPrice.getText().toString()) - finalprice;
                Log.e(TAG, " apply points " + pointsloyalty);
                Log.e(TAG, "totalloyalAmount" + totalloyalAmount);
                Log.e(TAG, "finalprice" + finalprice);
                Log.e(TAG, "total" + total);
                DecimalFormat twoDForm = new DecimalFormat("#.##");
                double total_amount = Double.valueOf(twoDForm.format(total));
                totalPrice.setText(total_amount + "");
                loylityBal.setText(balance + "");
                applyLoyaltyPoints.setVisibility(View.GONE);
                removePoints.setVisibility(View.VISIBLE);
                rd_egift.setEnabled(false);
                rd_coupon.setEnabled(false);
            } else {
                Toast.makeText(this, "Your Total amount is less than points applied", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void applyEgift() {
        if (TextUtils.isEmpty(giftcouponcode.getText().toString())) {
            Toast.makeText(context, "Select E-Gift Card", Toast.LENGTH_LONG).show();
        } else {
            if (Util.isNetworkAvailable(context)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.KEY_EGIFT_APPLY);
                jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
                jsonObject.addProperty("coupon_code", giftcouponcode.getText().toString());
                Log.e(TAG, "egift apply json" + jsonObject.toString());
                applyCouponApi(jsonObject);
            } else {
                Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void applyCouponCodeApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_coupon_cust(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        rd_loylty.setEnabled(false);
                        rd_egift.setEnabled(false);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        String coupon_amount = jsonObject1.getString("coupon_amt_per");
                        coupon_type = jsonObject1.getString("coupon_type");
                        coupon_name = jsonObject1.getString("coupon_code");
                        coupon_id = jsonObject1.getString("coupon_id");
                        if (Double.parseDouble(coupon_amount) < Double.parseDouble(totalPrice.getText().toString())) {
                            couponCodeText.setText(coupon_name + "( $" + coupon_amount + " )");
                            couponCodeText.setEnabled(false);
                            remove_coupon.setVisibility(View.VISIBLE);
                            apply_coupon.setVisibility(View.GONE);
                            if (coupon_type.equalsIgnoreCase("Discount Percentage")) {
                                double couponTotal = Double.parseDouble(coupon_amount.replace("%", ""));
                                couponCodeText.setText(coupon_name + "( " + coupon_amount + "% )");
                                Log.d("demo", String.valueOf(couponTotal));
                                Log.d("couponTotal", String.valueOf(couponTotal));

                                double totalamt = Double.parseDouble(amount) * couponTotal / 100;
                                double finalamt = totalamt;
                                Log.d("taxdemo1", String.valueOf(finalamt));


                                double admin_amount = Double.parseDouble(amount) - finalamt;
                                double admin_per = admin_amount;

                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                double grand_amount = Double.valueOf(twoDForm.format(admin_per));
                                totalPrice.setText(grand_amount + "");
                            } else if (coupon_type.equalsIgnoreCase("Discount Amount")) {
                                double amt = Double.parseDouble(totalPrice.getText().toString()) - Double.parseDouble(coupon_amount);
                                DecimalFormat twoDForm = new DecimalFormat("#.##");
                                double total_amount = Double.valueOf(twoDForm.format(amt));
                                totalPrice.setText(total_amount + "");
                            }

                            Snackbar.make(findViewById(android.R.id.content), "Your Coupon Code " + coupon_name + " applied successfully", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Your Coupon Code can't be applied", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(context, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cateringOrder() {
        order_type = "111";
        AppPreferences.setOrderType(context, order_type);
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                CheckOutActivity.this,
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
        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                order_type = "0";
                order_timings = "";
                catering_btn.setChecked(false);
            }
        });
    }

    private void proceedToPayment() {
        if (order_type.equalsIgnoreCase("0")) {
            Toast.makeText(context, "Select your order type", Toast.LENGTH_SHORT).show();
        } else if (totalPrice.getText().toString().equalsIgnoreCase("00.0") || totalPrice.getText().toString().equalsIgnoreCase("0.0")) {
            Toast.makeText(this, "Amount can't be zero", Toast.LENGTH_SHORT).show();
        } else {
            /*Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
            AppPreferencesBuss.setfinalLoylityPoints(context, loyality_apply.getText().toString());
            AppPreferences.setDeliveryName(context, custName.getText().toString());
            AppPreferencesBuss.setGrandTotal(context, totalPrice.getText().toString());
            AppPreferences.setPrepTime(context, food_prepration_time.getText().toString());
            setRadioValue(context, radioValue);
            intent.putExtra("grandtotal", totalPrice.getText().toString());
            intent.putExtra("couponCode", egiftcoupon);
            intent.putExtra("couponId", egiftid);
            intent.putExtra("couponAmount", egiftamount);
            intent.putExtra("place_order", cartItemsList);
            intent.putExtra("offercoupon", coupon_id);
            startActivity(intent);

            finish();*/
            showPaymentDialog();
        }
    }

    private void getGETax() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.TAX);
        geTaxApi(jsonObject);
        Log.d(TAG, " Request Tax :- " + jsonObject.toString());

    }

    private void geTaxApi(JsonObject jsonObject) {
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Log.v(TAG, "Inside response 200 ");
                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject object = resultJsonArray.getJSONObject(i);
                            double geTaxTotal = Double.parseDouble(object.getString("geTax").replace("%", ""));
                            Log.d("demo", String.valueOf(geTaxTotal));
                            getax.setText(String.valueOf(geTaxTotal) + "%");
                            Log.d("geTaxTotal1", String.valueOf(geTaxTotal));

                            double totaltax = Double.parseDouble(totalAmount) * geTaxTotal / 100;
                            double finaltotaltax = totaltax;
                            Log.d("taxdemo1", String.valueOf(finaltotaltax));


                            double admin_amount = finaltotaltax + Double.parseDouble(totalAmount);
                            double admin_per = admin_amount;
                            double admin_total = admin_per;     /// final amount

                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                            double grand_amount = Double.valueOf(twoDForm.format(admin_total));
                            double finaltotaltax1 = Double.valueOf(twoDForm.format(totaltax));
                            totalPrice.setText(grand_amount + "");
                            TvTotalWithTax.setText("$" + grand_amount + "");
                            getax.setText("$" + finaltotaltax1 + "");
                            amount = String.valueOf(grand_amount);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error" + t.getMessage());
                Toast.makeText(context, "Server Not responding", Toast.LENGTH_SHORT).show();
//                    t.getMessage();
            }
        });
    }

    private void removeEgiftMethod() {
        giftcouponcode.setText("");
        giftcouponcode.setEnabled(true);
        removeegift.setVisibility(View.GONE);
        applygiftbtn.setVisibility(View.VISIBLE);
        totalPrice.setText(Double.parseDouble(totalPrice.getText().toString()) + Double.parseDouble(egiftamount) + "");
        rd_loylty.setEnabled(true);
        rd_coupon.setEnabled(true);
    }

    private void applyCouponApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        rd_loylty.setVisibility(View.GONE);
                        rd_coupon.setVisibility(View.GONE);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        egiftamount = jsonObject1.getString("coupon_amount");
                        egiftcoupon = jsonObject1.getString("coupon_code");
                        egiftid = jsonObject1.getString("id");
                        if (Double.parseDouble(egiftamount) < Double.parseDouble(totalPrice.getText().toString())) {
                            giftcouponcode.setText(egiftcoupon + "( $" + egiftamount + " )");
                            giftcouponcode.setEnabled(false);
                            removeegift.setVisibility(View.VISIBLE);
                            applygiftbtn.setVisibility(View.GONE);
                            double amt = Double.parseDouble(totalPrice.getText().toString()) - Double.parseDouble(egiftamount);
                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                            double total_amount = Double.valueOf(twoDForm.format(amt));
                            totalPrice.setText(total_amount + "");
                            Snackbar.make(findViewById(android.R.id.content), "Egift Card " + egiftcoupon + " applied successfully", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "Egift Card can't be applied", Snackbar.LENGTH_LONG).setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            }).show();
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(context, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoyalityPoints() {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.BUSSINESSAVAILABLELOYALTYPOINTS);
            jsonObject.addProperty("user_id", getCustomerid(context));//8
            jsonObject.addProperty("business_id", getBusiID(context));//8
            Log.e(TAG, "LoyalityJson :- " + jsonObject.toString());
            loyalityData(jsonObject);

        } else {
            Toast.makeText(getApplicationContext(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }

    private void loyalityData(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.order_details(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            loylityBal.setText(object.getString("totalPoints"));
                            loyalityTotal = Integer.parseInt(object.getString("totalPoints"));
                            pointsloyalty = Integer.parseInt(object.getString("loyalty_points"));
                            amountofPoints = Integer.parseInt(object.getString("loyalty_amount"));
                            loylityBal.setText(String.valueOf(loyalityTotal));
                            if (amountofPoints == 0 || pointsloyalty == 0) {
                                applyLoyaltyPoints.setEnabled(false);
                                loyality_apply.setEnabled(false);
                            } else {
                                applyLoyaltyPoints.setEnabled(true);
                                loyality_apply.setEnabled(true);
                            }
                            AppPreferencesBuss.setLoyalityPoints(context, object.getString("totalPoints"));
                            loyality_apply.setFilters(new InputFilter[]{new InputFilterMinMax("1", "" + loyalityTotal)});
                            // loyality_apply.setFilters(new InputFilter[]{new InputFilterMinMax(String.valueOf(pointsloyalty), "" + loyalityTotal)});
                        }
                    } else {
                        loylityBal.setText("0");
                        applyLoyaltyPoints.setEnabled(false);
                        loyality_apply.setEnabled(false);
                        loyalityTotal = 0;
                        loylityBal.setText(String.valueOf(loyalityTotal));
                        AppPreferencesBuss.setLoyalityPoints(context, String.valueOf(loyalityTotal));
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
                Toast.makeText(getApplicationContext(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, final int dayOfMonth) {
        cateringDateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        Log.v(TAG, "onDateSet: cateringDateTime >> " + cateringDateTime);
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog, int hourOfDay, int minute, int i2) {
                boolean isPM = (hourOfDay >= 12);
                String selectedTime = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                String currentTime = dateFormat.format(c.getTime());
                try {
                    Date currentTime24 = dateFormat.parse(currentTime);
                    Date selectedTime24 = dateFormat.parse(cateringDateTime);
                    Log.e(TAG, "onCreate: currentdate24 >> " + dateFormat.format(currentTime24));
                    c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(catering_lead_days));
                    String newdate = dateFormat.format(c.getTime());
                    Log.e(TAG, "onTimeSet: new date>>>>>   " + newdate);
                    Date finalCurrentTime = dateFormat.parse(newdate);


                    Log.e(TAG, "onTimeSet: final date>>>>>   " + finalCurrentTime);
                    if (!catering_lead_days.equalsIgnoreCase("") && !catering_lead_days.equalsIgnoreCase("0")) {
                        if (finalCurrentTime.compareTo(selectedTime24) < 0) {

                            Log.e(TAG, "onTimeSet: current time is greater than selected time");
                            order_timings = cateringDateTime + "," + selectedTime;
                            AppPreferences.setOrderTime(context, order_timings);
                            custPhnLayout.setVisibility(View.GONE);
                            custAddLayout.setVisibility(View.GONE);
                            custDtLayout.setVisibility(View.VISIBLE);
                            custPreptimeLayout.setVisibility(View.VISIBLE);
                            custNmLayout.setVisibility(View.VISIBLE);
                            custName.setText(AppPreferences.getCustomername(context));
                            custName.setSelection(custName.length());
                            CustOrTime.setText(selectedTime);
                            CustOrDate.setText(cateringDateTime);
                            setFoodPrepTime(order_type);

                        } else if (finalCurrentTime.compareTo(selectedTime24) > 0 || finalCurrentTime.compareTo(selectedTime24) == 0) {

                            timePickerDialog.dismiss();
                            order_type = "0";
                            order_timings = "";
                            catering_btn.setChecked(false);
                            showAlertDialog("Catering Order Requires " + catering_lead_days + " days prior booking");
                            Log.e(TAG, "onTimeSet: current time is less than/equals selected time");

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, mHour, mMinute, false);
        timePickerDialog.show(getFragmentManager(), "timepickerdialog");
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        timePickerDialog.setCancelColor(getResources().getColor(R.color.colorPrimary));
        timePickerDialog.setOkColor(getResources().getColor(R.color.colorPrimary));
    }

    private void showAlertDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Order Time");
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void setTimePicker() {

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        boolean isPM = (hourOfDay >= 12);
                        String selectedTime = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM");
                        AppPreferences.setOrderTime(context, selectedTime);

                        order_timings = AppPreferences.getOrderTime(context);
                        Log.d("ORDERTIME>>", order_timings);
                        custTimeLayout.setVisibility(View.VISIBLE);
                        custPhnLayout.setVisibility(View.GONE);
                        custAddLayout.setVisibility(View.GONE);
                        custDtLayout.setVisibility(View.GONE);
                        custPreptimeLayout.setVisibility(View.VISIBLE);
                        custNmLayout.setVisibility(View.VISIBLE);
                        custName.setText(AppPreferences.getCustomername(context));
                        custName.setSelection(custName.length());
                        CustOrTime.setText(order_timings);
                        setFoodPrepTime(order_type);

                        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                        Calendar c = Calendar.getInstance();
                        String currentTime = parseFormat.format(c.getTime());
                        try {
                            Date currentTime24 = parseFormat.parse(currentTime);
                            Date selectedTime24 = parseFormat.parse(selectedTime);
                            Log.e(TAG, "onCreate: currentTime24 >> " + displayFormat.format(currentTime24));
                            Log.e(TAG, "onCreate: selectedTime24 >> " + displayFormat.format(selectedTime24));
                            c.add(Calendar.MINUTE, Integer.parseInt(takeOut_lead_time));

                            String newTime = parseFormat.format(c.getTime());
                            Date finalCurrentTime = parseFormat.parse(newTime);
                            Log.e(TAG, "onCreate: finalCurrentTime >> " + displayFormat.format(finalCurrentTime));

                            if (finalCurrentTime.compareTo(selectedTime24) > 0) {
                                Log.e(TAG, "onTimeSet: selected time is greater than current time");
                                order_type = "0";
                                order_timings = "";
                                inhouse_btn.setChecked(false);
                                take_way_btn.setChecked(false);
                                showAlertDialog("Take-out order requires minimum " + takeOut_lead_time + " mins before pickup time");
                            } else if (finalCurrentTime.compareTo(selectedTime24) < 0) {
                                Log.e(TAG, "onTimeSet: selected time is less than currect time");
                            } else if (finalCurrentTime.compareTo(selectedTime24) == 0) {
                                Log.e(TAG, "onTimeSet: current time is equal selected time");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, mHour, mMinute, false);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                order_type = "0";
                order_timings = "";
                inhouse_btn.setChecked(false);
                take_way_btn.setChecked(false);
            }
        });
        timePickerDialog.show();
    }

    @SuppressLint("RestrictedApi")
    public void updateHomeDeliveryInfo() {
        builder = new AlertDialog.Builder(context);
        LayoutInflater _inflater = LayoutInflater.from(context);
        view = _inflater.inflate(R.layout.pop_up_dialog, null);
        builder.setView(view, 50, 50, 50, 50);
        deliveryDialog = builder.create();
        deliveryDialog.setCancelable(true);
        deliveryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deliveryDialog.setCanceledOnTouchOutside(false);

        final EditText dname = (EditText) view.findViewById(R.id.dname);
        final EditText dcontact = (EditText) view.findViewById(R.id.dcontact);
        daddress = (EditText) view.findViewById(R.id.daddress);
        daddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPlace();
            }
        });
        ImageView close = (ImageView) view.findViewById(R.id.close);

        dname.setText(AppPreferences.getCustomername(context));
        dname.setSelection(dname.getText().length());
        dcontact.setText(AppPreferences.getCustomerMobile(context));
        dcontact.setSelection(dcontact.getText().length());
        daddress.setText(AppPreferences.getCustomerAddress(context));
        daddress.setSelection(daddress.getText().length());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_way_btn.setChecked(false);
                homedelivery_btn.setChecked(false);
                deliveryDialog.dismiss();
                order_type = "0";
            }
        });

        Button dsubmit = (Button) view.findViewById(R.id.dsubmit);
        dsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dname.getText().toString())) {
                    dname.setError(fieldRequired);
                } else if (TextUtils.isEmpty(dcontact.getText().toString())) {
                    dcontact.setError(fieldRequired);
                } else if (TextUtils.isEmpty(daddress.getText().toString())) {
                    daddress.setError(fieldRequired);
                } else {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    deliveryDialog.hide();
                    order_type = "1111";
                    AppPreferences.setOrderType(context, order_type);
                    AppPreferences.setDeliveryName(context, dname.getText().toString());
                    AppPreferences.setDeliveryContact(context, dcontact.getText().toString());
                    AppPreferences.setDeliveryAddress(context, daddress.getText().toString());
                    custNmLayout.setVisibility(View.VISIBLE);
                    custPhnLayout.setVisibility(View.VISIBLE);
                    custAddLayout.setVisibility(View.VISIBLE);
                    custTimeLayout.setVisibility(View.GONE);
                    custDtLayout.setVisibility(View.GONE);
                    custPreptimeLayout.setVisibility(View.VISIBLE);
                    custName.setText(dname.getText().toString());
                    custName.setSelection(custName.length());
                    CustPhn.setText(dcontact.getText().toString());
                    CustAddr.setText(daddress.getText().toString());
                    setFoodPrepTime(order_type);
                }
            }
        });
        deliveryDialog.dismiss();
    }

    private void checkPackage() {
        String package_list = AppPreferences.getBussPackageList(context);
        if (package_list.contains("3")) {
            take_way_btn.setVisibility(View.VISIBLE);
            catering_btn.setVisibility(View.VISIBLE);
            inhouse_btn.setVisibility(View.VISIBLE);
        } else {
            take_way_btn.setVisibility(View.GONE);
            catering_btn.setVisibility(View.GONE);
            inhouse_btn.setVisibility(View.GONE);
        }
    }

    private void getAllCoupons() {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.ENDPOINT.GETALLCOUPONS);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));
            Log.e(TAG, "available coupon json :- " + jsonObject.toString());
            getCoupon(jsonObject);

        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getCoupon(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_coupon_cust(jsonObject);


        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "available coupon Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        couponsModelsList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CustomerModel customerModel = new CustomerModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            customerModel.setOffer_amount(object.getString("coupon_amt_per"));
                            customerModel.setOffer_name(object.getString("coupon_code"));
                            customerModel.setOffer_decp(object.getString("coupon_description"));
                            customerModel.setMinOrderAmt(object.getString("min_order_amt"));
                            couponsModelsList.add(customerModel);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);

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
                // Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllEgifts() {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.MY_EGIFT);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
            jsonObject.addProperty("curr_date", Function.getCurrentDate());
            Log.e(TAG, "available coupon json :- " + jsonObject.toString());
            getDataFromServerAvailableCoupon(jsonObject);

        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromServerAvailableCoupon(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.available_coupon(jsonObject);


        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "available coupon Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        egiftModelsList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CustomerModel customerModel = new CustomerModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            customerModel.setOffer_amount(object.getString("amount"));
                            customerModel.setOffer_name(object.getString("coupon"));
                            customerModel.setOffer_decp(object.getString("message"));
                            egiftModelsList.add(customerModel);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        //Toast.makeText(context, object.getString("msg"), Toast.LENGTH_LONG).show();
//                        Log.d("onResponse", jsonObject.getString("msg"));
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

    public void findPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(CheckOutActivity.this);
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "findPlace: Exception >> " + e.getMessage());
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "findPlace: Exception >> " + e.getMessage());
            // TODO: Handle the error.
        }
    }

    private void showPaymentDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.paypal_order_payment_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        final CustomButton pay = (CustomButton) dialog.findViewById(R.id.pay);
        CustomCheckBox checkBox = (CustomCheckBox) dialog.findViewById(R.id.checkbox);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkAvailable(context)) {
                    dialog.dismiss();
                    getPayment(totalPrice.getText().toString());
                } else
                    Toast.makeText(context, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    pay.setVisibility(View.VISIBLE);
                else
                    pay.setVisibility(View.GONE);
            }
        });

        if (!CheckOutActivity.this.isFinishing())
            dialog.show();
    }

    private void getPayment(String amount) {
        Log.d("hellooo", amount);
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount + "0"), "USD", "Purchase Fee\n",
                PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(CheckOutActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //Puting paypal payment to the intent
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(CheckOutActivity.this, data);
                Log.e(TAG, "onActivityResult: Place >> " + place.getName() + place.getPhoneNumber());
                Log.e(TAG, "onActivityResult: LatLng >> " + place.getLatLng());

                daddress.setText(place.getName() + ", " + place.getAddress());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                Log.e(TAG, "onActivityResult: latitude >> " + latitude);
                Log.e(TAG, "onActivityResult: longitude >> " + longitude);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(CheckOutActivity.this, data);
                Log.e(TAG, "onActivityResult: result_error >> " + status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
            }
        } else if (requestCode == PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            /*
            {
                "client": {
                "environment": "sandbox",
                        "paypal_sdk_version": "2.0.0",
                        "platform": "iOS",
                        "product_name": "PayPal iOS SDK;"
            },
                "response": {
                "create_time": "2014-02-12T22:29:49Z",
                        "id": "PAY-564191241M8701234KL57LXI",
                        "intent": "sale",
                        "state": "approved"
            },
                "response_type": "payment"
            }
        */

            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);
                            JSONObject jsonResponse = jsonDetails.getJSONObject("response");
                            AppPreferences.setTranctionId(CheckOutActivity.this, jsonResponse.getString("id"));
                            AppPreferences.setPaymentApproved(CheckOutActivity.this, jsonResponse.getString("state"));

                            // String transaction_ID = jsonResponse.getString("id");
                            String createTime = jsonResponse.getString("create_time");
                            String intent = jsonResponse.getString("intent");
                            String paymentState = jsonResponse.getString("state");
                            //   Log.i("paymentExample", "RESPONSE :- \n" + "Transaction_ID :- " + transaction_ID + "\nCreate Time :- " + createTime +
                            // "\nIntent :- " + intent + "\nPayment State :- " + paymentState);

                        } catch (JSONException e) {
                            Toast.makeText(CheckOutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        placeOrder();
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");

                Toast.makeText(getApplicationContext(), "Sorry!! Payment cancelled by User", Toast.LENGTH_SHORT).show();

            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void placeOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_ORDER_DETAILS);
        JsonObject object = new JsonObject();
        object.addProperty("business_id", AppPreferences.getBusiID(CheckOutActivity.this));
        object.addProperty("user_id", AppPreferences.getCustomerid(CheckOutActivity.this));
        object.addProperty("delivery_name", AppPreferences.getDeliveryName(CheckOutActivity.this));//, AppPreferencesBuss.getBussiId(getActivity()));
        object.addProperty("delivery_address", AppPreferences.getDeliveryAddress(CheckOutActivity.this));
        object.addProperty("delivery_mobile", AppPreferences.getDeliveryContact(CheckOutActivity.this));
        object.addProperty("adderess_save_status", AppPreferences.getRadioValue(CheckOutActivity.this));
        object.addProperty("paymentType", "paypal");
        object.addProperty("txn_id", AppPreferences.getTranctionId(CheckOutActivity.this));
        object.addProperty("payment_gross", AppPreferences.getPrice(CheckOutActivity.this));
        object.addProperty("currency_code", "");
        object.addProperty("payment_status", "pending");
        object.addProperty("e_gift_balance", egiftamount);
        object.addProperty("e_gift_id", egiftid);
        object.addProperty("grandtotal", AppPreferencesBuss.getGrandTotal(context));
        object.addProperty("loyalty_points", (AppPreferencesBuss.getFinalLoyalityPoint(context)));
        object.addProperty("gratuity", (AppPreferencesBuss.getGratuity(context)));
        object.addProperty("credits", (AppPreferencesBuss.getCredit(context)));
        object.addProperty("order_type", AppPreferences.getOrderType(CheckOutActivity.this)); //  AppPreferences.getOrderType(ThankYouScreenActivity.this)
        object.addProperty("today_time", AppPreferences.getOrderTime(CheckOutActivity.this)); //order time
        object.addProperty("future_time", AppPreferences.getOrderTime(CheckOutActivity.this));
        jsonObject.add("contactDetails", object);
        Log.e("Place order request", object.toString());
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < cartItemsList.size(); i++) {
            OrderItemsDetailsModel model = cartItemsList.get(i);
            JsonObject orderDetailsObject = new JsonObject();
            orderDetailsObject.addProperty("menu_id", model.getMenu_id());
            orderDetailsObject.addProperty("cat_id", model.getCat_id());
            orderDetailsObject.addProperty("qty", model.getItemQuantity());
            orderDetailsObject.addProperty("price", model.getItemPrice());
            orderDetailsObject.addProperty("item_customization", model.getItemCustomiationList());
            orderDetailsObject.addProperty("iteam_message", model.getMessage());
            jsonArray.add(orderDetailsObject);
            Log.e(TAG, orderDetailsObject.toString());
        }

        jsonObject.add("orderDetails", jsonArray);

        Log.e(TAG, "placeOrder: Request >> " + jsonObject);
        placeOrderTask(jsonObject);
    }

    private void placeOrderTask(JsonObject jsonObject) {
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.order_details(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e(TAG, "placeOrder: Response >> " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String order_id = jsonObject1.getString("id");
                        DatabaseHandler mydb = new DatabaseHandler(CheckOutActivity.this);
                        mydb.deleteCartitem();
                        showThankYouAlert(order_id);

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Log.e("onResponse", object.getString("msg"));
                        showErrorDialog(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorDialog("Order Didn't Placed!");

                }
                // progressHD.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ERROR", "Error On failure :- " + Log.getStackTraceString(t));
                showErrorDialog("Order Didn't Placed!");
            }
        });
    }

    private void showThankYouAlert(final String order_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thank You!");
        builder.setMessage("Order Placed Successfully");
        ImageView img = new ImageView(CheckOutActivity.this);
        img.setImageResource(R.drawable.thanks);
        builder.setView(img);
        builder.setPositiveButton("GO TO HOME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppPreferences.setDeliveryName(context, "");
                AppPreferences.setDeliveryContact(context, "");
                AppPreferences.setDeliveryAddress(context, "");

                Intent intent = new Intent(getApplicationContext(), CustomerNaviDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNeutralButton("FEEDBACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent1 = new Intent(getApplicationContext(), CustomerFeedbackActivity.class);
                intent1.setAction("Order");
                intent1.putExtra("orderid", order_id);
                intent1.putExtra("busid", AppPreferences.getBusiID(CheckOutActivity.this));
                startActivity(intent1);
                finish();
            }
        });
        builder.show();
    }

    private void showErrorDialog(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckOutActivity.this);
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.drawable.ic_launcher_app);
        alertDialog.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                placeOrder();
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public class InputFilterMinMax implements InputFilter {

        private double min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Double.parseDouble(min);
            this.max = Double.parseDouble(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(double a, double b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }


}
