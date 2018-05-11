package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerFeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FEEDBACK Customer";
    CustomButton btnSub;
    String rESV_ID, Business_ID, orderId;
    CustomEditText feedbackMsg;
    boolean feedFrom = false;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_feedback);
        setToolbar();
        getData();
        init();


    }

    private void getData() {
        if (getIntent().getStringExtra("Resv_id") != null) {
            rESV_ID = getIntent().getExtras().getString("Resv_id");
            Business_ID = getIntent().getExtras().getString("Bussiness_id");
            feedFrom = false;
        } else if (getIntent().getAction().equalsIgnoreCase("Order")) {
            if (getIntent().getStringExtra("orderid") != null && !getIntent().getStringExtra("orderid").equalsIgnoreCase("")) {
                feedFrom = true;
                orderId = getIntent().getStringExtra("orderid");
                Business_ID = getIntent().getExtras().getString("busid");
            }
        }


    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Feedback");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        btnSub = (CustomButton) findViewById(R.id.btnSub);
        feedbackMsg = (CustomEditText) findViewById(R.id.feedback_msg);
        LinearLayout mainView = (LinearLayout) findViewById(R.id.main_view);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = CustomerFeedbackActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) CustomerFeedbackActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        btnSub.setOnClickListener(this);

    }


    private void getFeedback() {
        if (Util.isNetworkAvailable(CustomerFeedbackActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.RESERVATION_FEEDBACK);
            jsonObject.addProperty("business_id", Business_ID);
            jsonObject.addProperty("reservation_id", rESV_ID);
            jsonObject.addProperty("review_message", feedbackMsg.getText().toString());
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerFeedbackActivity.this));
            Log.e(TAG, "FeedbackJson :- " + jsonObject.toString());
            feedbackData(jsonObject);

        } else {
            Toast.makeText(CustomerFeedbackActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }

    private void feedbackData(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(CustomerFeedbackActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "Feedback Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Toast.makeText(CustomerFeedbackActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (jsonObject.getString("status").equals("400")) {

                        Toast.makeText(CustomerFeedbackActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CustomerFeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CustomerFeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSub) {
            if (feedbackMsg.getText().toString().equalsIgnoreCase("")) {
                feedbackMsg.setError("Enter Message");
            } else {
                if (Util.isNetworkAvailable(CustomerFeedbackActivity.this)) {
                    if (feedFrom)
                        feedbackOrder();
                    else
                        getFeedback();
                } else {
                    Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void feedbackOrder() {
        if (Util.isNetworkAvailable(CustomerFeedbackActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.ORDER_FEEDBACK);
            jsonObject.addProperty("business_id", Business_ID);
            jsonObject.addProperty("order_id", orderId);
            jsonObject.addProperty("review_message", feedbackMsg.getText().toString());
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerFeedbackActivity.this));
            Log.e(TAG, "OrderFeedbackJson :- " + jsonObject.toString());
            feedbackData(jsonObject);

        } else {
            Toast.makeText(CustomerFeedbackActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }


}
