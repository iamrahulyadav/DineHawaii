package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerConfirmreservationActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static final String TAG = "Making_Reservation_NEXT";
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConstants.PAYPAL_CLIENT_ID);
    CustomTextView resName, resTime, resDate, resAmount;
    CustomButton btndone;
    private ImageView back;
    private String business_id, reservation_id, amount = "", name, time, date, partysize, tablename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_confirmreservation);
        setToolbar();
        init();
    }

    private void init() {
        amount = AppPreferences.getReservationAmount(CustomerConfirmreservationActivity.this);
        Log.d("viniiii", AppPreferences.getReservationAmount(this));
        if (getIntent().hasExtra("business_id"))
            business_id = getIntent().getStringExtra("business_id");
        if (getIntent().hasExtra("reservation_id"))
            reservation_id = getIntent().getStringExtra("reservation_id");
        if (getIntent().hasExtra("reserve_amt"))
            amount = getIntent().getStringExtra("reserve_amt");
        if (getIntent().hasExtra("name"))
            name = getIntent().getStringExtra("name");
        if (getIntent().hasExtra("time"))
            time = getIntent().getStringExtra("time");
        if (getIntent().hasExtra("date"))
            date = getIntent().getStringExtra("date");
        if (getIntent().hasExtra("tablename"))
            tablename = getIntent().getStringExtra("tablename");
        if (getIntent().hasExtra("partysize"))
            partysize = getIntent().getStringExtra("partysize");

        Log.e("busi_id", business_id);
        Log.e("res_id", reservation_id);
        Log.e("amount", amount);

        resName = (CustomTextView) findViewById(R.id.rescustname);
        resTime = (CustomTextView) findViewById(R.id.restime);
        resDate = (CustomTextView) findViewById(R.id.resdate);
        resAmount = (CustomTextView) findViewById(R.id.resamt);
        btndone = (CustomButton) findViewById(R.id.btnDone);
        btndone.setOnClickListener(this);


        resName.setText(name);
        resDate.setText(date);
        resTime.setText(time);
        resAmount.setText(amount + "$");
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((CustomTextView) findViewById(R.id.headet_text)).setText("Confirm Reservation");

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDone) {
            getPayment(amount);
        }

    }

    private void getPayment(String amount) {
        // amount="10";
        if (amount.equalsIgnoreCase("0")) {
            Toast.makeText(this, "Payment amount can't be zero ", Toast.LENGTH_SHORT).show();
        } else {
            //Getting the amountToBePaid from editText
            Log.v(TAG, "~~~~~~~~~~~~~~~~~~~~ Song Price outside iff :- " + amount);
            Log.d("amount", "" + amount);

            //Creating a paypalpayment
            PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Purchase Fee\n",
                    PayPalPayment.PAYMENT_INTENT_SALE);
            //Creating Paypal Payment activity intent
            Intent intent = new Intent(CustomerConfirmreservationActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);
            //putting the paypal configuration to the intent
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            //Puting paypal payment to the intent
            intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
            //Starting the intent activity for result
            //the request code will be used on the method onActivityResult
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        }
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
                        Log.e("paymentExample", paymentDetails);

                        try {

                            JSONObject jsonDetails = new JSONObject(paymentDetails);
                            JSONObject jsonResponse = jsonDetails.getJSONObject("response");
                            AppPreferences.setTranctionId(CustomerConfirmreservationActivity.this, jsonResponse.getString("id"));


                            String transaction_ID = jsonResponse.getString("id");
                            String createTime = jsonResponse.getString("create_time");
                            String intent = jsonResponse.getString("intent");
                            String paymentState = jsonResponse.getString("state");
                            String nofity_checkbox = "0";
                            Log.e("paymentExample", "RESPONSE :- \n" + "Transaction_ID :- " + transaction_ID + "\nCreate Time :- " + createTime +
                                    "\nIntent :- " + intent + "\nPayment State :- " + paymentState);


                            if (paymentState.equalsIgnoreCase("approved")) {
                                /* Intent in = new Intent(CustomerConfirmreservationActivity.this, ThanksReservationActivity.class);
                                 *//*  in.putExtra("reservation_id", reservation_id);
                                in.putExtra("business_id", business_id);
                             *//*   //in.putExtra("user_id", AppPreferences.getCustomerid(CustomerConfirmreservationActivity.this));
                                in.putExtra("reservation_amount", amount);
                                in.putExtra("payment_type", "paypal");
                                in.putExtra("transaction_id", transaction_ID);
                                in.putExtra("payment_status", paymentState);
                                in.putExtra("min_hours", "02");
                                in.putExtra("time", "11:30");
                                if (chk_Confirm.isChecked())
                                    nofity_checkbox="1";
                                else
                                    nofity_checkbox="0";
                                startActivity(in);
                                finish();*/
                                confirmReservation(transaction_ID, paymentState, nofity_checkbox);


                            } else {
                                Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(CustomerConfirmreservationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

//                        showAlertDialog(amount);


                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("paymentExample", "The user canceled.");

                Toast.makeText(getApplicationContext(), "Sorry!! Payment cancelled by User", Toast.LENGTH_SHORT).show();

            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

    }


    void confirmReservation(String transaction_ID, String paymentState, String nofity_checkbox) {
        if (Util.isNetworkAvailable(CustomerConfirmreservationActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_RESERVATION_CONFIRMATION);
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(CustomerConfirmreservationActivity.this));
            jsonObject.addProperty("reservation_id", AppPreferencesBuss.getReservatId(CustomerConfirmreservationActivity.this));
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerConfirmreservationActivity.this));//testing demo code
            jsonObject.addProperty("reservation_amount", amount);
            jsonObject.addProperty("paymentType", "paypal");
            jsonObject.addProperty("transaction_id", transaction_ID);
            jsonObject.addProperty("payment_status", paymentState);
            jsonObject.addProperty("min_hours", "2");
            jsonObject.addProperty("time", Function.getCurrentDateTime());
            jsonObject.addProperty("nofity_checkbox", nofity_checkbox);
            Log.e(TAG, "Request CONFIRM RESERVATION >> " + jsonObject.toString());
            Log.d("SaveCon", AppPreferencesBuss.getReservatId(CustomerConfirmreservationActivity.this));
            saveDataToServer(jsonObject);
        } else {
            Toast.makeText(this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void saveDataToServer(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(CustomerConfirmreservationActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_reservation_confirmation(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("Save", "Response CONFIRM RESERVATION >> " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        jsonObject.getString("message");
                        showThankYouAlert();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(CustomerConfirmreservationActivity.this, msg, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Save Confirmation", "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(CustomerConfirmreservationActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void showThankYouAlert () {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(CustomerConfirmreservationActivity.this);
        builder.setTitle("Thank You!");
        builder.setCancelable(false);
        builder.setMessage("Your reservation has done successfully.");
        ImageView img = new ImageView(CustomerConfirmreservationActivity.this);
        img.setImageResource(R.drawable.thanks);
        builder.setView(img);
        builder.setPositiveButton("GO TO HOME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), CustomerNaviDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }
}
