package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

public class ImmediateFeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FEEDBACK Customer";
    CustomButton btnSub;
    String rESV_ID, Business_ID, orderId;
    CustomEditText feedbackMsg, feedback_remark, feedback_title;
    boolean feedFrom = false;
    private ImageView back;
    private String type = "none";

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
        feedback_remark = (CustomEditText) findViewById(R.id.feedback_remark);
        feedback_title = (CustomEditText) findViewById(R.id.feedback_title);
        LinearLayout mainView = (LinearLayout) findViewById(R.id.main_view);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = ImmediateFeedbackActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) ImmediateFeedbackActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        btnSub.setOnClickListener(this);

    }


    private void reservFeedback() {
        if (Util.isNetworkAvailable(ImmediateFeedbackActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.ALL_FEEDBACK);
            jsonObject.addProperty("business_id", Business_ID);
            jsonObject.addProperty("order_id", rESV_ID);
            jsonObject.addProperty("review_message", feedbackMsg.getText().toString());
            jsonObject.addProperty("title", feedback_title.getText().toString());
            jsonObject.addProperty("remark", feedback_remark.getText().toString());
            jsonObject.addProperty("type", type);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(ImmediateFeedbackActivity.this));
            Log.e(TAG, "FeedbackJson :- " + jsonObject.toString());
            feedbackData(jsonObject);
        } else {
            Toast.makeText(ImmediateFeedbackActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void feedbackData(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(ImmediateFeedbackActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
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
                        Toast.makeText(ImmediateFeedbackActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ImmediateFeedbackActivity.this, CustomerNaviDrawer.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else if (jsonObject.getString("status").equals("400")) {
                        Toast.makeText(ImmediateFeedbackActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ImmediateFeedbackActivity.this, CustomerNaviDrawer.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    } else {
                        Toast.makeText(ImmediateFeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ImmediateFeedbackActivity.this, CustomerNaviDrawer.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }

                    progressHD.dismiss();

                } catch (JSONException e) {
                    progressHD.dismiss();
                    e.printStackTrace();
                    startActivity(new Intent(ImmediateFeedbackActivity.this, CustomerNaviDrawer.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(ImmediateFeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ImmediateFeedbackActivity.this, CustomerNaviDrawer.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSub) {
            if (TextUtils.isEmpty(feedbackMsg.getText().toString())) {
                feedbackMsg.setError("Enter Message");
            } else if (TextUtils.isEmpty(feedback_title.getText().toString())) {
                feedbackMsg.setError("Enter Title");
            } else {
                if (Util.isNetworkAvailable(ImmediateFeedbackActivity.this)) {
                    if (feedFrom) {
                        type = "order";
                        orderFeedback();
                    } else {
                        type = "reservation";
                        reservFeedback();
                    }
                } else {
                    Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void orderFeedback() {
        if (Util.isNetworkAvailable(ImmediateFeedbackActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.ALL_FEEDBACK);
            jsonObject.addProperty("business_id", Business_ID);
            jsonObject.addProperty("order_id", orderId);
            jsonObject.addProperty("title", feedback_title.getText().toString());
            jsonObject.addProperty("remark", feedback_remark.getText().toString());
            jsonObject.addProperty("type", type);
            jsonObject.addProperty("review_message", feedbackMsg.getText().toString());
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(ImmediateFeedbackActivity.this));
            Log.e(TAG, "OrderFeedbackJson :- " + jsonObject.toString());
            feedbackData(jsonObject);
        } else {
            Toast.makeText(ImmediateFeedbackActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }
}
