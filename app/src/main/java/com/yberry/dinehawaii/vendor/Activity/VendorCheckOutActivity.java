package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
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
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
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
import com.yberry.dinehawaii.database.VendorOrderDBHandler;
import com.yberry.dinehawaii.vendor.Adapter.VendorCheckOutItemAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    RelativeLayout proceed;
    LinearLayout mainLayout;
    View view;
    Context context;
    RecyclerView mRecyclerView;
    private ArrayList<VendorOrderItemsDetailsModel> cartItems;
    private CustomTextView tvDeliveryText, tvDelChargeAmount;
    private double delChargeAmount = 0.0, geTaxAmount = 0.0, totalPaidAmountBase = 0.0;
    private DecimalFormat decimalFormat;
    private CustomTextView tvDeliveryDate, tvDeliveryDays;
    private final static String TAG = "VendorCheckOutActivity";
    private String vendor_id = "";
    private RadioGroup rgOrderType;
    private String amount = "0";
    private VendorOrderDBHandler mydb;
    private CustomEditText etRemark, etOrderFrequency;
    private String preparation_time = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_check_out);
        context = this;
        setToolbar();
        decimalFormat = new DecimalFormat("#.##");
        init();
        if (getIntent().hasExtra("vendor_id"))
            vendor_id = getIntent().getStringExtra("vendor_id");
        if (!getIntent().getStringExtra("totalamount").equalsIgnoreCase("")) {
            totalAmount = Double.parseDouble(getIntent().getStringExtra("totalamount"));
        }
        mydb = new VendorOrderDBHandler(context);
        cartItems = mydb.getOrderCartItems(vendor_id);  //database data
        Log.e(TAG, "onCreate: cartItems >> " + cartItems);
        setCartAdapter();

        if (Util.isNetworkAvailable(context)) {
            getGETax();
            getDeliveryInfo();
        } else Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
        tvTotalAmt.setText("$" + totalAmount);
    }

    private void setCartAdapter() {
        VendorCheckOutItemAdapter adapter = new VendorCheckOutItemAdapter(VendorCheckOutActivity.this, cartItems);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(VendorCheckOutActivity.this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);
    }

    private void init() {
        tvTotalPaidAmount = (CustomTextView) findViewById(R.id.tvTotalPaidAmount);
        tvDeliveryDate = findViewById(R.id.tvDeliveryDate);
        tvDeliveryDays = findViewById(R.id.tvDeliveryDays);
        tvTotalAmt = (CustomTextView) findViewById(R.id.totalCost);
        tvTotalPaidAmount2 = (CustomTextView) findViewById(R.id.tvTotalPaidAmount2);

        tvGETaxAmount = (CustomTextView) findViewById(R.id.tvGETaxAmount);
        food_prepration_time = (CustomTextView) findViewById(R.id.prepTime);
        proceed = (RelativeLayout) findViewById(R.id.proceedtopay);
        mainLayout = findViewById(R.id.mainCheckout);
        tvGETaxValue = (CustomTextView) findViewById(R.id.tvGETaxValue);
        rgOrderType = findViewById(R.id.rgOrderType);
        etRemark = findViewById(R.id.etRemark);
        etOrderFrequency = findViewById(R.id.etOrderFrequency);

        proceed.setOnClickListener(this);
        tvDeliveryDate.setOnClickListener(this);
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

            case R.id.tvDeliveryDate:
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
                break;

            default:
                break;
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


    private void proceedToPayment() {
        if (tvTotalPaidAmount.getText().toString().equalsIgnoreCase("00.0") || tvTotalPaidAmount.getText().toString().equalsIgnoreCase("0.0")) {
//            placeOrder();
        } else {
            showPaymentDialog();
        }
    }

    private void getGETax() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.TAX);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));

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
        tvDeliveryDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
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
            /*{
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
            }*/


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

//    private void placeOrder() {
//        final ProgressHUD progressHD = ProgressHUD.show(VendorCheckOutActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//            }
//        });
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_ORDER_DETAILS);
//        JsonObject object = new JsonObject();
//        object.addProperty("business_id", AppPreferences.getBusiID(VendorCheckOutActivity.this));
//        object.addProperty("user_id", AppPreferences.getCustomerid(VendorCheckOutActivity.this));
//        object.addProperty("old_order_id", AppPreferences.getOldOrderId(VendorCheckOutActivity.this));
//        object.addProperty("delivery_name", AppPreferences.getDeliveryName(VendorCheckOutActivity.this));//, AppPreferencesBuss.getBussiId(getActivity()));
//        object.addProperty("delivery_mobile", AppPreferences.getDeliveryContact(VendorCheckOutActivity.this));
//        object.addProperty("adderess_save_status", AppPreferences.getRadioValue(VendorCheckOutActivity.this));
//        object.addProperty("paymentType", "paypal");
//        object.addProperty("txn_id", AppPreferences.getTranctionId(VendorCheckOutActivity.this));
//        object.addProperty("payment_gross", AppPreferences.getPrice(VendorCheckOutActivity.this));
//        object.addProperty("currency_code", "");
//        object.addProperty("payment_status", "pending");
//        object.addProperty("e_gift_balance", egiftamount);
//        object.addProperty("e_gift_id", "0");
//        object.addProperty("e_gift_wallet_amount", usedWalletAmt);
//        object.addProperty("grandtotal", AppPreferencesBuss.getGrandTotal(context));
//        object.addProperty("loyalty_points", (AppPreferencesBuss.getFinalLoyalityPoint(context)));
//        object.addProperty("gratuity", (AppPreferencesBuss.getGratuity(context)));
//        object.addProperty("credits", (AppPreferencesBuss.getCredit(context)));
//        object.addProperty("order_type", AppPreferences.getOrderType(VendorCheckOutActivity.this)); //  AppPreferences.getOrderType(ThankYouScreenActivity.this)
//        object.addProperty("today_time", AppPreferences.getOrderTime(VendorCheckOutActivity.this)); //order time
//        object.addProperty("future_time", AppPreferences.getOrderTime(VendorCheckOutActivity.this));
//        object.addProperty("address_lat", String.valueOf(cust_latitude));
//        object.addProperty("address_long", String.valueOf(cust_longitude));
//        object.addProperty("set_as_default", setDefault);
//        object.addProperty("remark", etRemark.getText().toString());
//        jsonObject.add("contactDetails", object);
//        JsonArray jsonArray = new JsonArray();
//        for (int i = 0; i < cartItems.size(); i++) {
//            VendorOrderItemsDetailsModel model = cartItems.get(i);
//            JsonObject orderDetailsObject = new JsonObject();
//            orderDetailsObject.addProperty("menu_id", model.getMenu_id());
//            orderDetailsObject.addProperty("cat_id", model.getCat_id());
//            orderDetailsObject.addProperty("qty", model.getItemQuantity());
//            orderDetailsObject.addProperty("price", model.getItemPrice());
//            orderDetailsObject.addProperty("item_customization", model.getItemCustomiationList().replace("[", "").replace("]", ""));
//            orderDetailsObject.addProperty("iteam_message", model.getMessage());
//            jsonArray.add(orderDetailsObject);
//            Log.e(TAG, orderDetailsObject.toString());
//        }
//
//        jsonObject.add("orderDetails", jsonArray);
//
//        Log.e(TAG, "placeOrder: Request >> " + jsonObject);
//
//        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
//        Call<JsonObject> call = apiService.order_details(jsonObject);
//        call.enqueue(new Callback<JsonObject>() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                Log.e(TAG, "placeOrder: Response >> " + response.body().toString());
//                String s = response.body().toString();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    progressHD.dismiss();
//                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("result");
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                        String order_id = jsonObject1.getString("id");
//                        DatabaseHandler mydb = new DatabaseHandler(VendorCheckOutActivity.this);
//                        mydb.deleteCartitem();
//                        showThankYouAlert(order_id);
//                        Log.e(TAG, "onResponse: setDefault>>>>>>" + setDefault);
//                        if (setDefault.equalsIgnoreCase("1")) {
//                            AppPreferences.setCustAddrLat(context, String.valueOf(cust_latitude));
//                            AppPreferences.setCustAddrLong(context, String.valueOf(cust_longitude));
//                        }
//                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("result");
//                        JSONObject object = jsonArray.getJSONObject(0);
//                        Log.e("onResponse", object.getString("msg"));
//                        showErrorDialog(object.getString("msg"));
//                    }
//                } catch (JSONException e) {
//                    progressHD.dismiss();
//                    e.printStackTrace();
//                    showErrorDialog("Order Didn't Placed!");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                progressHD.dismiss();
//                Log.e("ERROR", "Error On failure :- " + Log.getStackTraceString(t));
//                showErrorDialog("Order Didn't Placed!");
//            }
//        });
//    }

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

                Intent intent = new Intent(getApplicationContext(), BusinessNaviDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

    public void getDeliveryInfo() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.GET_VENDOR_DELIVERY_INFO);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
        jsonObject.addProperty("vendor_id", vendor_id);
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
                        preparation_time = object.getString("preparation_time");
                        String schedule_days = object.getString("schedule_days");
                        String cost_flat = object.getString("cost_flat");
                        String cost_range = object.getString("cost_range");
                        String cost_percent = object.getString("cost_percent");
                        tvDeliveryText.setTextColor(getResources().getColor(R.color.blue));
                        tvDeliveryDays.setText(schedule_days);

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
                                delChargeAmount = 0.0;
                            }
                        }
                        tvDelChargeAmount.setText("$" + String.valueOf(delChargeAmount));
                        totalPaidAmount = totalPaidAmount + delChargeAmount;
//                                totalPaidAmount = totalAmount + delChargeAmount;
                        totalPaidAmountBase = Double.parseDouble(decimalFormat.format(totalPaidAmount));
                        tvTotalPaidAmount.setText(decimalFormat.format(totalPaidAmount) + "");
                        tvTotalPaidAmount2.setText("$" + totalPaidAmountBase);

                    }
                    if (progressHD != null) progressHD.dismiss();

                } catch (JSONException e)

                {
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

    private void placeOrder() {
        JsonObject object = new JsonObject();
        object.addProperty("method", AppConstants.BUSINESS_VENDOR_API.PlACEVENDORORDER);
        object.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        object.addProperty("vendor_id", vendor_id);
        object.addProperty("delivery_date", tvDeliveryDate.getText().toString());
        if (rgOrderType.getCheckedRadioButtonId() == R.id.radioSingle)
            object.addProperty("order_type", "single");
        else
            object.addProperty("order_type", "repeat");
        object.addProperty("order_frequency", etOrderFrequency.getText().toString());
        object.addProperty("remark", etRemark.getText().toString());
        object.addProperty("paymentType", "paypal");
        object.addProperty("txn_id", AppPreferences.getTranctionId(VendorCheckOutActivity.this));
        object.addProperty("payment_status", "done");
        object.addProperty("sub_total", totalAmount);
        object.addProperty("total_amount", tvTotalPaidAmount.getText().toString());
        object.addProperty("delivery_fee", delChargeAmount);
        object.addProperty("ge_tax", geTaxAmount);

        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < cartItems.size(); i++) {
            VendorOrderItemsDetailsModel model = cartItems.get(i);
            JsonObject orderDetailsObject = new JsonObject();
            orderDetailsObject.addProperty("vendor_product_id", model.getProductId());
            orderDetailsObject.addProperty("item_id", model.getItemId());
            orderDetailsObject.addProperty("item_quantity", model.getItemQuan());
            orderDetailsObject.addProperty("item_amount", model.getItemTotalCost());
            jsonArray.add(orderDetailsObject);
            Log.e(TAG, orderDetailsObject.toString());
        }
        object.add("orderDetails", jsonArray);
        Log.e(TAG, "placeOrder: Request >> " + object.toString());
        placeOrderTask(object);
    }

    private void placeOrderTask(JsonObject jsonObject) {
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
                        mydb = mydb;
                        mydb.deleteVendorCartTtem(vendor_id);
                        showThankYouAlert("Your order placed successfully and the order id is " + object.getString("order_id"));
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Log.e("onResponse", object.getString("msg"));
                        showErrorDialog(object.getString("msg"));
                    }
                } catch (JSONException e) {
                    progressHD.dismiss();
                    e.printStackTrace();
                    showErrorDialog("Something went wrong");

                }
                progressHD.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ERROR", "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                showErrorDialog("Something went wrong");
            }
        });
    }
}
