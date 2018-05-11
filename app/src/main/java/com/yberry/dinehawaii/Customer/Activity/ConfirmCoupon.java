package com.yberry.dinehawaii.Customer.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.activity.ThankYouCouponActivity;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class ConfirmCoupon extends AppCompatActivity implements View.OnClickListener{
    Button pay;
    String amount;
    ImageView back;



    private static final String TAG = "PaymentActivity";
   public static final int PAYPAL_REQUEST_CODE = 123;

    private String paymentAmount = "0.01";

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConstants.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_coupon);
        setToolbar();
        init();

    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((CustomTextView) findViewById(R.id.headet_text)).setText("Confirm");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void init() {
        Button pay = (Button) findViewById(R.id.pay);
        pay.setOnClickListener(this);
       // ((Button) findViewById(R.id.pay)).setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
         amount = extras.getString("total_amount");
       // AppPreferences.setCouponAmount(ConfirmCoupon.this,amount);
        AppPreferences.setCouponAmount(ConfirmCoupon.this,amount);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.pay){
            //startActivity(new Intent(ConfirmCoupon.this,ThankYouCouponActivity.class));
           // finish();
            getPayment(amount);
        }

    }

    private void getPayment(String paymentAmount) {
        //Getting the amountToBePaid from editText

//        paymentAmount = price;
        Log.v(TAG, "~~~~~~~~~~~~~~~~~~~~ Song Price outside iff :- " + paymentAmount);

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Purchase Fee\n",
                PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(ConfirmCoupon.this, com.paypal.android.sdk.payments.PaymentActivity.class);
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
                            Log.d("checking55", String.valueOf(jsonDetails.getJSONObject("response")));
                            AppPreferences.setTranctionId(ConfirmCoupon.this, jsonResponse.getString("id"));
                            AppPreferences.setPaymentApproved(ConfirmCoupon.this, jsonResponse.getString("state"));
                          //  state

                            // String transaction_ID = jsonResponse.getString("id");
                            String createTime = jsonResponse.getString("create_time");
                            String intent = jsonResponse.getString("intent");
                            String paymentState = jsonResponse.getString("state");
                            //   Log.i("paymentExample", "RESPONSE :- \n" + "Transaction_ID :- " + transaction_ID + "\nCreate Time :- " + createTime +
                            // "\nIntent :- " + intent + "\nPayment State :- " + paymentState);

                        } catch (JSONException e) {
                            Toast.makeText(ConfirmCoupon.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

//                        showAlertDialog(paymentAmount);

                        Intent in=new Intent(ConfirmCoupon.this,ThankYouCouponActivity.class);
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


}
