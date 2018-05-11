package com.yberry.dinehawaii.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.yberry.dinehawaii.Customer.Activity.ThankYouScreenActivity;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentActivity extends AppCompatActivity {
    private static final String TAG = "PaymentActivity";
    TextView makePaymentButton;
    CheckBox paypalCheckBox;
    ArrayList<OrderItemsDetailsModel> cartItems;
    JsonObject contactDetails, otherDetObj;
    JsonArray orderDetails;
    OrderItemsDetailsModel detailsModel;
    public static final int PAYPAL_REQUEST_CODE = 123;

    private String paymentAmount = "0",coupon_code,coupon_id="0",coupon_amount="0";
    ArrayList<OrderItemsDetailsModel> arrayList;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConstants.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setToolbar();

        Intent intent = new Intent(PaymentActivity.this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);

        if (!getIntent().getExtras().getString("grandtotal").equalsIgnoreCase("")){
            paymentAmount = getIntent().getExtras().getString("grandtotal");
            arrayList= getIntent().getParcelableArrayListExtra("place_order");
            coupon_code = getIntent().getStringExtra("couponCode");
            coupon_id = getIntent().getStringExtra("couponId");
            coupon_amount = getIntent().getStringExtra("couponAmount");
        }/*else {

            Toast.makeText(this, "Amount cant be zero", Toast.LENGTH_SHORT).show();
            paymentAmount = "0";
        }*/
        //Log.v(TAG, "Payment Amount to be paid using paypal :- " + paymentAmount);
//        Log.v(TAG, "Order to be paid using paypal :- " + arrayList.toString());

        initViews();
    }

    private void initViews() {

        paypalCheckBox =  (CheckBox) findViewById(R.id.paypalCheckBox);
        makePaymentButton =(TextView)findViewById(R.id.makePayment);
//        makePaymentButton.setEnabled(false);
        makePaymentButton.setBackgroundColor(getResources().getColor(R.color.offwhite));

        paypalCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    makePaymentButton.setEnabled(true);
                    makePaymentButton.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                } else {
//                    makePaymentButton.setEnabled(false);
                    makePaymentButton.setBackgroundColor(getResources().getColor(R.color.offwhite));
                }
            }
        });

        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (! paypalCheckBox.isChecked()){

                    Log.v(TAG, "Inside check box not checked");
                    Toast.makeText(getApplicationContext(), "Please check the above check box !!!! ", Toast.LENGTH_SHORT).show();

                } else {
                    Log.v(TAG, "Inside check box checked");

//                    String paymentAmount = getIntent().getStringExtra("totalPayment");
//                    paymentAmount = "0.01";
                    getPayment(paymentAmount);
                }

                /*Intent in=new Intent(PaymentActivity.this,ThankYouScreenActivity.class);
                startActivity(in);
                finish();*/
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ((TextView) findViewById(R.id.headet_text)).setText("Payment");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    

    private void getPayment(String paymentAmount) {
        Log.d("hellooo",paymentAmount);
        //Getting the amountToBePaid from editText

//        paymentAmount = price;
        Log.v(TAG, "~~~~~~~~~~~~~~~~~~~~ Song Price outside iff :- " + paymentAmount);

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount+"0"), "USD", "Purchase Fee\n",
                PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(PaymentActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);
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
                            AppPreferences.setTranctionId(PaymentActivity.this, jsonResponse.getString("id"));
                            AppPreferences.setPaymentApproved(PaymentActivity.this, jsonResponse.getString("state"));

                            // String transaction_ID = jsonResponse.getString("id");
                            String createTime = jsonResponse.getString("create_time");
                            String intent = jsonResponse.getString("intent");
                            String paymentState = jsonResponse.getString("state");
                            //   Log.i("paymentExample", "RESPONSE :- \n" + "Transaction_ID :- " + transaction_ID + "\nCreate Time :- " + createTime +
                            // "\nIntent :- " + intent + "\nPayment State :- " + paymentState);

                        } catch (JSONException e) {
                            Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

//                        showAlertDialog(paymentAmount);

                        Intent in=new Intent(PaymentActivity.this,ThankYouScreenActivity.class);
                        in.putExtra("arrayList",arrayList);
                        in.putExtra("couponCode",coupon_code);
                        in.putExtra("couponId",coupon_id);
                        in.putExtra("couponAmount",coupon_amount);
                        startActivity(in);
                        finish();

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

    private void showAlertDialog(String paymentAmount) {

        new SweetAlertDialog(PaymentActivity.this)
                .setTitleText("Payment of " + paymentAmount + " done Successfully !!!")
                .show();

    }
    private void sendRequestSubmit() {

        detailsModel = new OrderItemsDetailsModel();
        contactDetails = new JsonObject();
        otherDetObj = new JsonObject();

        contactDetails.addProperty("business_id", AppPreferences.getBusiID(PaymentActivity.this));
        contactDetails.addProperty("user_id", AppPreferences.getCustomerid(PaymentActivity.this));
        contactDetails.addProperty("delivery_name", AppPreferences.getCustomername(PaymentActivity.this));
        contactDetails.addProperty("delivery_address", AppPreferences.getCustomerAddress(PaymentActivity.this));
        contactDetails.addProperty("delivery_mobile", AppPreferences.getCustomerMobile(PaymentActivity.this));
        contactDetails.addProperty("adderess_save_status", "1");
        contactDetails.addProperty("paymentType", "paypal");
        contactDetails.addProperty("txn_id",AppPreferences.getTranctionId(PaymentActivity.this));
        contactDetails.addProperty("payment_gross",detailsModel.getItemTotalCost());
        contactDetails.addProperty("currency_code","91");
        contactDetails.addProperty("payment_status",AppPreferences.getPaymentApproved(PaymentActivity.this));
//        contactDetails.addProperty("e_gift_balance",detailsModel.getE);
        contactDetails.addProperty("e_gift_id", "paypal");
        contactDetails.addProperty("loyalty_points", "paypal");
        contactDetails.addProperty("gratuity", "paypal");
        contactDetails.addProperty("credits", "paypal");
        contactDetails.addProperty("order_type", "new");

        Log.v(TAG, "Contact Details Object :-" + contactDetails.toString());


        for (OrderItemsDetailsModel orderModel : cartItems) {
            otherDetObj.addProperty("cat_id", orderModel.getCat_id());
            otherDetObj.addProperty("menu_id", orderModel.getMenu_id());
            otherDetObj.addProperty("qty", orderModel.getItemQuantity());
            otherDetObj.addProperty("price", orderModel.getItemTotalCost());
            otherDetObj.addProperty("item_customization", orderModel.getItemCustomiationList());
            otherDetObj.addProperty("iteam_message", orderModel.getMessage());
        }

        Log.v(TAG, "OrderDetails Array object :-" + otherDetObj);
        orderDetails = new JsonArray();
        orderDetails.add(otherDetObj);

        contactDetails.add("orderDetails",orderDetails);
        Log.v(TAG, "OrderDetails Array object :-" + orderDetails);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.GET_ORDER_DETAILS);
        jsonObject.add("contactDetails", new Gson().toJsonTree(contactDetails));
        /*
        "orderDetails": [
        {
            "cat_id":"1",
                "menu_id":"1",
                "qty":"1",
                "price":"26.00"
        },
        {
            "cat_id":"1",
                "menu_id":"2",
                "qty":"2",
                "price":"56.00"
        } ]*/


        /*
         *  {
            "method":"getOrderDetails",
                "contactDetails":{
            "business_id":"1",
                    "user_id":"8",
                    "delivery_name":"Ashutosh Khare",
                    "delivery_address":"saket nagar indore",
                    "delivery_mobile":"9993243209",
                    "adderess_save_status":"1",
                    "paymentType":"paypal"
        }, "orderDetails": [
            {
                "cat_id":"1",
                    "menu_id":"1",
                    "qty":"1",
                    "price":"26.00"
            },
            {
                "cat_id":"1",
                    "menu_id":"2",
                    "qty":"2",
                    "price":"56.00"
            }
            ]
        }
        */
        //   sendSubmitTask(jsonObject);
    }

    @SuppressLint("LongLogTag")
    private void sendSubmitTask(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(PaymentActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.submit_neworder(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();
                try {
                    JSONObject object = new JSONObject(s);
                    String status = object.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        Log.e(TAG + "<<<< New Order  >>>", object.getString("message"));
                    } else if (status.equalsIgnoreCase("400")) {
                        Log.e(TAG + "<<<< New Order  >>>", object.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG + "EORRO", t.getMessage());
                progressHD.dismiss();
            }
        });

        Log.v(TAG, "Request Json Object :- " + jsonObject);
    }
}
