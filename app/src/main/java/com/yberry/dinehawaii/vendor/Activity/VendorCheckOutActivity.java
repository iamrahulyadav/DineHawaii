/*
package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yberry.dinehawaii.Customer.Activity.CustomerNaviDrawer;
import com.yberry.dinehawaii.Customer.Activity.ImmediateFeedbackActivity;
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
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomCheckBox;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.Function.fieldRequired;

public class VendorCheckOutActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConstants.PAYPAL_CLIENT_ID);
    CustomTextView tvTotalPaidAmount, tvGETaxAmount, tvTotalAmt, tvTotalPaidAmount2,
            food_prepration_time, tvGETaxValue;
    double totalAmount = 0.0;
    double totalPaidAmount = 0.0;
    private static final String TAG = "VendorCheckOutActivity";
    RelativeLayout proceed, mainLayout;
    View view;
    AlertDialog.Builder builder;
    AlertDialog deliveryDialog;
    String timeInhouse = "", timeTakeout = "", timeCatering = "", timeDelivery = "", order_type = "0", radioValue = "0", minOrderValue = "0", takeOut_lead_time, catering_lead_days,
            egiftamount = "0", egiftcoupon = "", egiftid = "", order_timings = "", amount = "0", driverArrivalTime = "", coupon_type, coupon_name, coupon_id = "0", setDefault = "0",
            minDeliveryAmt = "", total_wallet_amt = "", usedWalletAmt = "";
    int loyalityTotal = 0;
    LinearLayout custNmLayout, custPhnLayout, custTimeLayout, custDtLayout, custAddLayout, custPreptimeLayout, llloyaltypt, llegiftbal;
    double pointsloyalty, amountofPoints;
    Context context;
    OffersAdapter couponAdapter;
    ArrayList<CustomerModel> egiftModelsList = new ArrayList<>();
    ArrayList<CustomerModel> couponsModelsList = new ArrayList<>();
    RecyclerView mRecyclerView;
    private ArrayList<OrderItemsDetailsModel> cartItems;
    private CustomCheckBox cbDefaultAddr;
    private CustomTextView tvDeliveryText, tvDriverTipText, tvDelChargeAmount;
    private double delChargeAmount = 0.0, geTaxAmount = 0.0, totalPaidAmountBase = 0.0, cust_latitude = 0.0, cust_longitude = 0.0;
    private DecimalFormat decimalFormat;
    private RadioGroup radioPaymode;
    private CustomTextView tvPaymentText;
    private double totalPaidAmountOrig = 0.0;
    private CustomEditText etRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        context = this;
        setToolbar();
        decimalFormat = new DecimalFormat("#.##");
        init();
        cartItems = new DatabaseHandler(context).getCartItems(AppPreferences.getBusiID(context));  //database data
        Log.e(TAG, "onCreate: cartItems >> " + cartItems);
        setCartAdapter();
        updateHomeDeliveryInfo();
        cust_latitude = Double.parseDouble(AppPreferences.getCustAddrLat(context));
        cust_longitude = Double.parseDouble(AppPreferences.getCustAddrLong(context));

        if (Util.isNetworkAvailable(context)) {
            getGETax();
            getFoodPrepTime();
        } else Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
    }

    private void setCartAdapter() {
        CheckOutItemAdapter adapter = new CheckOutItemAdapter(VendorCheckOutActivity.this, cartItems);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(VendorCheckOutActivity.this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);
    }

    private void getFoodPrepTime() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.FOOD_PREP_TIME);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
        jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));
        Log.e(TAG, "getFoodPrepTime: Request >> " + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String s = response.body().toString();
                Log.e(TAG, "getFoodPrepTime: Response >> " + s);
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
        tvTotalPaidAmount = (CustomTextView) findViewById(R.id.tvTotalPaidAmount);
        etRemark = (CustomEditText) findViewById(R.id.etRemark);
        tvPaymentText = (CustomTextView) findViewById(R.id.tvPaymentText);
        tvTotalAmt = (CustomTextView) findViewById(R.id.totalCost);
        tvTotalPaidAmount2 = (CustomTextView) findViewById(R.id.tvTotalPaidAmount2);
        if (!getIntent().getStringExtra("totalamount").equalsIgnoreCase("")) {
            totalAmount = Double.parseDouble(getIntent().getStringExtra("totalamount"));
            Log.e(TAG, "init: totalAmount" + totalAmount);
            tvTotalAmt.setText("$" + totalAmount);
        }
        tvGETaxAmount = (CustomTextView) findViewById(R.id.tvGETaxAmount);
        food_prepration_time = (CustomTextView) findViewById(R.id.prepTime);
        radioPaymode = (RadioGroup) findViewById(R.id.radioPaymode);
        proceed = (RelativeLayout) findViewById(R.id.proceedtopay);
        mainLayout = (RelativeLayout) findViewById(R.id.mainCheckout);
        custNmLayout = (LinearLayout) findViewById(R.id.cust1);
        custPhnLayout = (LinearLayout) findViewById(R.id.cust2);
        custAddLayout = (LinearLayout) findViewById(R.id.cust3);
        custTimeLayout = (LinearLayout) findViewById(R.id.cust4);
        custDtLayout = (LinearLayout) findViewById(R.id.cust5);
        custPreptimeLayout = (LinearLayout) findViewById(R.id.prepLayout);
        tvGETaxValue = (CustomTextView) findViewById(R.id.tvGETaxValue);
        llloyaltypt = (LinearLayout) findViewById(R.id.llloyalty);
        llegiftbal = (LinearLayout) findViewById(R.id.llegiftbal);
        tvDriverTipText = (CustomTextView) findViewById(R.id.tvDriverTipText);
        proceed.setOnClickListener(this);
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


        tvDeliveryText = (CustomTextView) findViewById(R.id.tvDeliveryText);
        tvDelChargeAmount = (CustomTextView) findViewById(R.id.tvDelChargeAmount);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.proceedtopay:
                proceedToPayment();
                break;
            case R.id.homedelivery_btn:
                if (!isFinishing()) {
                    deliveryDialog.show();
                }

                break;
            case R.id.take_way_btn:
                order_type = "11";
                break;
            case R.id.cateringradio:

                cateringOrder();
                break;
            case R.id.inhouseradio:
                order_type = "1";
                break;
            default:
                break;
        }
    }

    private void setFoodPrepTime(String type) {
        if (type.equalsIgnoreCase("1111")) {
            if (timeDelivery.compareTo(driverArrivalTime) > 0)
                food_prepration_time.setText(timeDelivery + " mins");
            else
                food_prepration_time.setText(driverArrivalTime + " mins");
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


    private void cateringOrder() {
        order_type = "111";
        AppPreferences.setOrderType(context, order_type);
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                VendorCheckOutActivity.this,
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
            }
        });
    }

    private void proceedToPayment() {
        if (order_type.equalsIgnoreCase("0")) {
            Toast.makeText(context, "Select your order type", Toast.LENGTH_SHORT).show();
        } else if (tvTotalPaidAmount.getText().toString().equalsIgnoreCase("00.0") || tvTotalPaidAmount.getText().toString().equalsIgnoreCase("0.0")) {
            placeOrder();
        } else {
            showPaymentDialog();
        }
    }

    private void getGETax() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.TAX);
        jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String s = response.body().toString();
                Log.e(TAG, "getGETax: Request >> " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = resultJsonArray.getJSONObject(0);
                        if (object.getString("business_get_tax_exemption").equalsIgnoreCase("1")) {
                            tvGETaxValue.setText("GE Tax");
                            tvTotalAmt.setText("$" + totalAmount);
                            tvTotalPaidAmount.setText("" + totalAmount);
                            tvTotalPaidAmount2.setText("$" + totalAmount);
                            tvGETaxAmount.setText("00");
                        } else {
                            tvGETaxValue.setText("GE Tax : " + object.getString("geTax_Percentage"));
                            double geTaxPer = Double.parseDouble(object.getString("geTax_Percentage").replace("%", ""));
                            Log.e(TAG, "getGETax: geTaxPer >> " + geTaxPer);

                            geTaxAmount = totalAmount * geTaxPer / 100;
                            Log.e(TAG, "getGETax: geTaxAmount >> " + geTaxAmount);

                            totalPaidAmount = geTaxAmount + totalAmount;
                            totalPaidAmountBase = totalPaidAmount;
                            Log.e(TAG, "getGETax: totalPaidAmount >> " + totalPaidAmount);

                            geTaxAmount = Double.valueOf(decimalFormat.format(geTaxAmount));
                            totalPaidAmount = Double.valueOf(decimalFormat.format(totalPaidAmount));

                            tvTotalPaidAmount.setText("" + totalPaidAmount);
                            tvTotalPaidAmount2.setText("$" + totalPaidAmount);
                            tvGETaxAmount.setText("$" + geTaxAmount);
                            amount = String.valueOf(totalPaidAmount);
                        }
                    } else {
                        tvGETaxValue.setText("GE Tax");
                        tvTotalAmt.setText("$" + totalAmount);
                        tvGETaxAmount.setText("00");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "getGETax: Exc >> " + e.getMessage());
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "getGETax: error >> " + t.getMessage());
                Toast.makeText(context, "Server Not responding", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, final int dayOfMonth) {
//        cateringDateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
//        Log.v(TAG, "onDateSet: cateringDateTime >> " + cateringDateTime);
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
//                    Date selectedTime24 = dateFormat.parse(cateringDateTime);
                    Log.e(TAG, "onCreate: currentdate24 >> " + dateFormat.format(currentTime24));
                    c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(catering_lead_days));
                    String newdate = dateFormat.format(c.getTime());
                    Log.e(TAG, "onTimeSet: new date>>>>>   " + newdate);
                    Date finalCurrentTime = dateFormat.parse(newdate);


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

        final EditText dname = (CustomEditText) view.findViewById(R.id.dname);
        final EditText dcontact = (CustomEditText) view.findViewById(R.id.dcontact);
        cbDefaultAddr = (CustomCheckBox) view.findViewById(R.id.cbDefaultAddr);
        ImageView close = (ImageView) view.findViewById(R.id.close);

        dname.setText(AppPreferences.getCustomername(context));
        dname.setSelection(dname.getText().length());
        dcontact.setText(AppPreferences.getCustomerMobile(context));
        dcontact.setSelection(dcontact.getText().length());

        CustomButton dsubmit = (CustomButton) view.findViewById(R.id.dsubmit);
        dsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dname.getText().toString())) {
                    dname.setError(fieldRequired);
                } else if (TextUtils.isEmpty(dcontact.getText().toString())) {
                    dcontact.setError(fieldRequired);
                } else {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    deliveryDialog.hide();
                    AppPreferences.setDeliveryName(context, dname.getText().toString());
                    AppPreferences.setDeliveryContact(context, dcontact.getText().toString());

                    double busi_latitude = Double.parseDouble(AppPreferences.getSelectedBusiLat(context));
                    double busi_longitude = Double.parseDouble(AppPreferences.getSelectedBusiLong(context));
                    Log.e(TAG, "updateHomeDeliveryInfo: cust_latitude >> " + cust_latitude);
                    Log.e(TAG, "updateHomeDeliveryInfo: cust_longitude >> " + cust_longitude);
                    Log.e(TAG, "updateHomeDeliveryInfo: busi_latitude >> " + busi_latitude);
                    Log.e(TAG, "updateHomeDeliveryInfo: busi_longitude >> " + busi_longitude);
                    AppPreferences.setDeliveryName(context, dname.getText().toString());
                    AppPreferences.setDeliveryContact(context, dcontact.getText().toString());
                    new GoogleMatrixRequest(busi_latitude, busi_longitude, cust_latitude, cust_longitude).execute();
                }
            }
        });

        cbDefaultAddr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    setDefault = "1";
                else
                    setDefault = "0";
            }
        });
        deliveryDialog.dismiss();
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
                    getPayment(tvTotalPaidAmount.getText().toString());
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

        if (!VendorCheckOutActivity.this.isFinishing())
            dialog.show();
    }

    private void getPayment(String amount) {
        Log.d("hellooo", amount);
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount + "0"), "USD", "Purchase Fee\n",
                PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(VendorCheckOutActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);
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
        if (requestCode == PAYPAL_REQUEST_CODE) {
            //If the result is OK i.e. user has not canceled the payment
            */
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
        *//*


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
                            AppPreferences.setTranctionId(VendorCheckOutActivity.this, jsonResponse.getString("id"));
                            AppPreferences.setPaymentApproved(VendorCheckOutActivity.this, jsonResponse.getString("state"));

                            // String transaction_ID = jsonResponse.getString("id");
                            String createTime = jsonResponse.getString("create_time");
                            String intent = jsonResponse.getString("intent");
                            String paymentState = jsonResponse.getString("state");
                            //   Log.i("paymentExample", "RESPONSE :- \n" + "Transaction_ID :- " + transaction_ID + "\nCreate Time :- " + createTime +
                            // "\nIntent :- " + intent + "\nPayment State :- " + paymentState);

                        } catch (JSONException e) {
                            Toast.makeText(VendorCheckOutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        placeOrder();
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");

                Toast.makeText(getApplicationContext(), "Sorry!! Payment cancelled", Toast.LENGTH_SHORT).show();

            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void placeOrder() {
        final ProgressHUD progressHD = ProgressHUD.show(VendorCheckOutActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_ORDER_DETAILS);
        JsonObject object = new JsonObject();
        object.addProperty("business_id", AppPreferences.getBusiID(VendorCheckOutActivity.this));
        object.addProperty("user_id", AppPreferences.getCustomerid(VendorCheckOutActivity.this));
        object.addProperty("old_order_id", AppPreferences.getOldOrderId(VendorCheckOutActivity.this));
        object.addProperty("delivery_name", AppPreferences.getDeliveryName(VendorCheckOutActivity.this));//, AppPreferencesBuss.getBussiId(getActivity()));
        object.addProperty("delivery_mobile", AppPreferences.getDeliveryContact(VendorCheckOutActivity.this));
        object.addProperty("adderess_save_status", AppPreferences.getRadioValue(VendorCheckOutActivity.this));
        object.addProperty("paymentType", "paypal");
        object.addProperty("txn_id", AppPreferences.getTranctionId(VendorCheckOutActivity.this));
        object.addProperty("payment_gross", AppPreferences.getPrice(VendorCheckOutActivity.this));
        object.addProperty("currency_code", "");
        object.addProperty("payment_status", "pending");
        object.addProperty("e_gift_balance", egiftamount);
        object.addProperty("e_gift_id", "0");
        object.addProperty("e_gift_wallet_amount", usedWalletAmt);
        object.addProperty("grandtotal", AppPreferencesBuss.getGrandTotal(context));
        object.addProperty("loyalty_points", (AppPreferencesBuss.getFinalLoyalityPoint(context)));
        object.addProperty("gratuity", (AppPreferencesBuss.getGratuity(context)));
        object.addProperty("credits", (AppPreferencesBuss.getCredit(context)));
        object.addProperty("order_type", AppPreferences.getOrderType(VendorCheckOutActivity.this)); //  AppPreferences.getOrderType(ThankYouScreenActivity.this)
        object.addProperty("today_time", AppPreferences.getOrderTime(VendorCheckOutActivity.this)); //order time
        object.addProperty("future_time", AppPreferences.getOrderTime(VendorCheckOutActivity.this));
        object.addProperty("address_lat", String.valueOf(cust_latitude));
        object.addProperty("address_long", String.valueOf(cust_longitude));
        object.addProperty("set_as_default", setDefault);
        object.addProperty("remark", etRemark.getText().toString());
        jsonObject.add("contactDetails", object);
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < cartItems.size(); i++) {
            OrderItemsDetailsModel model = cartItems.get(i);
            JsonObject orderDetailsObject = new JsonObject();
            orderDetailsObject.addProperty("menu_id", model.getMenu_id());
            orderDetailsObject.addProperty("cat_id", model.getCat_id());
            orderDetailsObject.addProperty("qty", model.getItemQuantity());
            orderDetailsObject.addProperty("price", model.getItemPrice());
            orderDetailsObject.addProperty("item_customization", model.getItemCustomiationList().replace("[", "").replace("]", ""));
            orderDetailsObject.addProperty("iteam_message", model.getMessage());
            jsonArray.add(orderDetailsObject);
            Log.e(TAG, orderDetailsObject.toString());
        }

        jsonObject.add("orderDetails", jsonArray);

        Log.e(TAG, "placeOrder: Request >> " + jsonObject);

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
                    progressHD.dismiss();
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String order_id = jsonObject1.getString("id");
                        DatabaseHandler mydb = new DatabaseHandler(VendorCheckOutActivity.this);
                        mydb.deleteCartitem();
                        showThankYouAlert(order_id);
                        Log.e(TAG, "onResponse: setDefault>>>>>>" + setDefault);
                        if (setDefault.equalsIgnoreCase("1")) {
                            AppPreferences.setCustAddrLat(context, String.valueOf(cust_latitude));
                            AppPreferences.setCustAddrLong(context, String.valueOf(cust_longitude));
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Log.e("onResponse", object.getString("msg"));
                        showErrorDialog(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    progressHD.dismiss();
                    e.printStackTrace();
                    showErrorDialog("Order Didn't Placed!");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressHD.dismiss();
                Log.e("ERROR", "Error On failure :- " + Log.getStackTraceString(t));
                showErrorDialog("Order Didn't Placed!");
            }
        });
    }

    private void showThankYouAlert(final String order_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thank You!");
        builder.setCancelable(false);
        builder.setMessage("Order Placed Successfully");
        ImageView img = new ImageView(VendorCheckOutActivity.this);
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
                Intent intent1 = new Intent(getApplicationContext(), ImmediateFeedbackActivity.class);
                intent1.setAction("Order");
                intent1.putExtra("orderid", order_id);
                intent1.putExtra("busid", AppPreferences.getBusiID(VendorCheckOutActivity.this));
                startActivity(intent1);
                finish();
            }
        });
        builder.show();
    }

    private void showErrorDialog(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VendorCheckOutActivity.this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
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

    public void getDeliveryInfo(final Double distance) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.GETDELIVERYINFO);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
        jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));
        Log.e(TAG, "getDeliveryInfo: Request >> " + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_user_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String s = response.body().toString();
                Log.e(TAG, "getDeliveryInfo: Response >> " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = resultJsonArray.getJSONObject(0);
//                        deliveryArea = Integer.parseInt(object.getString("delivery_area"));
                        driverArrivalTime = object.getString("driver_arrival_time");
                        minDeliveryAmt = object.getString("min_food_cost");
                        if (Double.parseDouble(object.getString("min_food_cost")) <= Double.parseDouble(tvTotalPaidAmount.getText().toString())) {
//                            if (distance <= deliveryArea) {
                            String cost_flat = object.getString("cost_flat");
                            String cost_range = object.getString("cost_range");
                            String cost_percent = object.getString("cost_percent");
                            String driver_tip = object.getString("driver_tip");
                            tvDeliveryText.setTextColor(getResources().getColor(R.color.blue));
                            if (driver_tip.equalsIgnoreCase("0")) {
                                tvDriverTipText.setVisibility(View.VISIBLE);
                                tvDriverTipText.setText("Please Tip driver generously.");
                            } else if (driver_tip.equalsIgnoreCase("1")) {
                                tvDriverTipText.setVisibility(View.VISIBLE);
                                tvDriverTipText.setText("Driver Tip included");
                                double tipAmt = Double.parseDouble(object.getString("driver_tip_amt"));
                                totalPaidAmount = tipAmt + totalPaidAmount;
                                totalPaidAmountBase = totalPaidAmount;
                                Log.e(TAG, "DriverTip: totalPaidAmount >> " + totalPaidAmount);

                                tipAmt = Double.valueOf(decimalFormat.format(tipAmt));


                                tvTotalPaidAmount.setText("" + totalPaidAmount);
                                tvTotalPaidAmount2.setText("$" + totalPaidAmountBase);
                            }
                            if (cost_flat.equalsIgnoreCase("1")) {
                                tvDeliveryText.setText("Delivery Fee: Flat $" + object.getString("flat_amt") + " on every order.");
                                delChargeAmount = Double.parseDouble(object.getString("flat_amt"));
                                tvDelChargeAmount.setText(String.valueOf(delChargeAmount));
                            } else if (cost_percent.equalsIgnoreCase("1")) {
                                tvDeliveryText.setText("Delivery Fee: " + object.getString("percent_amt") + "% on every order.");
                                delChargeAmount = totalAmount * Double.parseDouble(object.getString("percent_amt")) / 100;
                                tvDelChargeAmount.setText(String.valueOf(delChargeAmount));
                            } else if (cost_range.equalsIgnoreCase("1")) {
                                tvDeliveryText.setText("Delivery Fee: \n     1. $" + object.getString("min_food_cost") + " is the minimum cost for delivery.\n"
                                        + "     2. $" + object.getString("lessthan_amt") + " if total food cost is less than $" + object.getString("lessthan_value")
                                        + ".\n     3. $" + object.getString("between_amt") + " if total food cost is $" + object.getString("between_val1") + " to $" + object.getString("between_val2"));
                                if (totalAmount < Double.parseDouble(object.getString("lessthan_value"))) {
                                    delChargeAmount = Double.parseDouble(object.getString("lessthan_amt"));
                                } else if (totalAmount >= Double.parseDouble(object.getString("between_val1")) && totalAmount <= Double.parseDouble(object.getString("between_val2"))) {
                                    delChargeAmount = Double.parseDouble(object.getString("between_amt"));
                                } else {
                                    tvDeliveryText.setText("No Delivery Fee Applied");
                                    //delChargeAmount = Double.parseDouble(object.getString("min_food_cost"));
                                    delChargeAmount = 0.0;
                                }
                            }


                            order_type = "1111";
                            AppPreferences.setOrderType(context, order_type);


                            custNmLayout.setVisibility(View.VISIBLE);
                            custPhnLayout.setVisibility(View.VISIBLE);
                            custAddLayout.setVisibility(View.VISIBLE);
                            custTimeLayout.setVisibility(View.GONE);
                            custDtLayout.setVisibility(View.GONE);
                            custPreptimeLayout.setVisibility(View.VISIBLE);
                            setFoodPrepTime(order_type);

                            tvDelChargeAmount.setText("$" + String.valueOf(delChargeAmount));
                            totalPaidAmount = totalPaidAmount + delChargeAmount;
//                                totalPaidAmount = totalAmount + delChargeAmount;
                            totalPaidAmountBase = Double.parseDouble(decimalFormat.format(totalPaidAmount));
                            tvTotalPaidAmount.setText(decimalFormat.format(totalPaidAmount) + "");
                            tvTotalPaidAmount2.setText("$" + totalPaidAmountBase);

                           */
/* } else {
                                order_type = "0";
                                tvDeliveryText.setTextColor(Color.RED);
                                tvDeliveryText.setText("Delivery address is too far. Restaurant delivers within " + deliveryArea + " miles.");
                            }*//*

                        } else {
                            order_type = "0";
                            tvDeliveryText.setTextColor(Color.RED);
                            tvDeliveryText.setText("Minimum Order Amount for Delivery is $" + minDeliveryAmt);
                        }
                    }
                    if (progressHD != null) progressHD.dismiss();

                } catch (JSONException e) {
                    if (progressHD != null) progressHD.dismiss();
                    Log.e(TAG, "getDeliveryInfo: Exc >> " + e.getMessage());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (progressHD != null) progressHD.dismiss();
                Log.e(TAG, "getDeliveryInfo: onFailure >> " + t.getMessage());
            }
        });
    }


    public class GoogleMatrixRequest extends AsyncTask<Void, Double, Void> {

        private static final String API_KEY = AppConstants.GOOGLE_MATRIX_API_KEY;
        double origin_lat = 0.0, origin_long = 0.0;
        double dest_lat = 0.0, dest_long = 0.0;
        String url_request = "";
        OkHttpClient client = new OkHttpClient();

        public GoogleMatrixRequest(double origin_lat, double origin_long, double dest_lat, double dest_long) {
            this.origin_lat = origin_lat;
            this.origin_long = origin_long;
            this.dest_lat = dest_lat;
            this.dest_long = dest_long;
            url_request = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin_lat + "," + origin_long + "&destinations=" + dest_lat + "," + dest_long + "&mode=driving&units=imperial&key=" + API_KEY;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e(TAG, "GoogleMatrixRequest: doInBackground: Request >> " + url_request);
            double distance = 0.0;
            Request request = new Request.Builder()
                    .url(url_request)
                    .build();

            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                String response_str = response.body().string();
                Log.e(TAG, "GoogleMatrixRequest: doInBackground: Response >> " + response_str);
                JSONObject jsonObject = new JSONObject(response_str);
                JSONObject elementsObject = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                if (elementsObject.getString("status").equalsIgnoreCase("OK")) {
                    String[] str = elementsObject.getJSONObject("distance").getString("text").split("\\s");
                    if (str != null)
                        distance = Double.parseDouble(str[0]);
                    publishProgress(distance);
                } else if (elementsObject.getString("status").equalsIgnoreCase("ZERO_RESULTS")) {
                    publishProgress(distance);
                }

                */
/*order_type = "1111";
                AppPreferences.setOrderType(context, order_type);
                AppPreferences.setDeliveryName(context, dname.getText().toString());
                AppPreferences.setDeliveryContact(context, dcontact.getText().toString());
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
                getDeliveryInfo();*//*

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            super.onProgressUpdate(values);
            Log.e(TAG, "GoogleMatrixRequest: onProgressUpdate: distance >> " + values[0]);
            if (values[0] != 0.0)
                getDeliveryInfo(values[0]);
            else {
                order_type = "0";
                tvDeliveryText.setTextColor(Color.RED);
                tvDeliveryText.setText("Unable to locate delivery address, please reselect your delivery address.");
            }
        }
    }


}
*/
