package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends AppCompatActivity {

    private RatingActivity context;
    private String business_name;
    private String business_id;
    private String TAG = "RatingActivity";
    private RatingBar ratingBar;
    private CustomEditText etReview;
    private CustomButton btnRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        context = this;
        setToolbar();
        if (getIntent().hasExtra("business_id"))
            business_id = getIntent().getStringExtra("business_id");
        Log.e(TAG, "onCreate: business_id >> " + business_id);
        init();
    }

    private void init() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        etReview = (CustomEditText) findViewById(R.id.etReview);
        btnRating = (CustomButton) findViewById(R.id.btnSubmit);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkAvailable(context)) {
                    submitRating();
                } else {
                    Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void submitRating() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.SUBMIT_RATING);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
        jsonObject.addProperty("business_id", business_id);
        jsonObject.addProperty("rating", String.valueOf(ratingBar.getRating()));
        jsonObject.addProperty("review", etReview.getText().toString());
        Log.e(TAG, "Request: submitRating >> " + jsonObject.toString());


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_user_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "submitRating: Response >> " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = resultJsonArray.getJSONObject(0);
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Server not responding", Toast.LENGTH_SHORT).show();
                    }
                    progressHD.dismiss();
                } catch (JSONException e) {
                    progressHD.dismiss();
                    Toast.makeText(context, "Server not responding", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server not responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Submit Rating");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
