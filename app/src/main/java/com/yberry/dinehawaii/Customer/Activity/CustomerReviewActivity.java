package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RatingBar;
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

public class CustomerReviewActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final String TAG = "REVIEW Customer";

    private ImageView back;
    private  RatingBar ratingBar;
    private  String ratedValue,rESV_ID1,Business_ID1;
    private  CustomEditText etQuestion;
    private CustomButton btnSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_review);
        LinearLayout mainView = (LinearLayout)findViewById(R.id.activity_reviews_cus);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = CustomerReviewActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) CustomerReviewActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        setToolbar();
        init();

        if (getIntent().getStringExtra("Resv_id") != null) {
            rESV_ID1 = getIntent().getStringExtra("Resv_id");
            Business_ID1 = getIntent().getExtras().getString("Bussiness_id");
        }
    }

    private void init() {
        back = (ImageView) findViewById(R.id.back);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        etQuestion = (CustomEditText) findViewById(R.id.etQues1);
        btnSub = (CustomButton) findViewById(R.id.btnSub);
        back.setOnClickListener(this);
        btnSub.setOnClickListener(this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser) {
                ratedValue = String.valueOf(ratingBar.getRating());
            }
        });}

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Review");}

    private void getReview() {
        if (Util.isNetworkAvailable(CustomerReviewActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.RESERVATION_RATINGS);
            jsonObject.addProperty("business_id",Business_ID1);   //Business_ID1
            jsonObject.addProperty("reservation_id", rESV_ID1);   //rESV_ID1
            jsonObject.addProperty("review_question", etQuestion.getText().toString());
            jsonObject.addProperty("ratings", ratedValue);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerReviewActivity.this));
            Log.e(TAG, "ReviewJson :- " + jsonObject.toString());
            customerRatting(jsonObject);
        } else {
            Toast.makeText(CustomerReviewActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void customerRatting(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(CustomerReviewActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                        Toast.makeText(CustomerReviewActivity.this,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }else if (jsonObject.getString("status").equals("400")){

                        Toast.makeText(CustomerReviewActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(CustomerReviewActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(CustomerReviewActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.back){
            onBackPressed();
        }
        if(view.getId()==R.id.btnSub){
            if (TextUtils.isEmpty(ratedValue)){
                Toast.makeText(this, "Please select rating", Toast.LENGTH_SHORT).show();
            }else {
                getReview();
            }
        }
    }


}
