package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.CustomerResturantAdapter;
import com.yberry.dinehawaii.Model.ReviewModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerResturantReviewAcivity extends AppCompatActivity {
    private static final String TAG = "CustomerResturantReviewAcivity";

    public static CustomerResturantAdapter adapter;
    public static List<ReviewModel> list = new ArrayList<>();
    Context context;
    CustomTextView dataId;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomTextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_resturant_review_acivity);
        setToolbar();
        initComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataAvailableReview();

    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((CustomTextView) findViewById(R.id.headet_text)).setText("Reviews");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initComponent() {
        dataId = (CustomTextView) findViewById(R.id.dataId);
        tvSubmit = (CustomTextView) findViewById(R.id.tvSubmit);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_reservation);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (getIntent().hasExtra("business_name")) {
            tvSubmit.setText("Rate " + getIntent().getStringExtra("business_name"));
        }

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerResturantReviewAcivity.this, RatingActivity.class).putExtra("business_id", AppPreferences.getBusiID(CustomerResturantReviewAcivity.this)));
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void dataAvailableReview() {
        if (Util.isNetworkAvailable(CustomerResturantReviewAcivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.ALL_RATING_REVIEW);
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(CustomerResturantReviewAcivity.this));   //AppPreferences.getBusiID(CustomerResturantReviewAcivity.this)
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerResturantReviewAcivity.this)); //AppPreferences.getReservationId(CustomerResturantReviewAcivity.this)
            Log.e(TAG, "Request :- " + jsonObject.toString());
            getDataFromServerReview(jsonObject);

        } else {
            Toast.makeText(CustomerResturantReviewAcivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromServerReview(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(CustomerResturantReviewAcivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ReviewModel reviewModel = new ReviewModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            reviewModel.setRating(object.getString("rating"));
                            reviewModel.setReview_message(object.getString("review"));
                            reviewModel.setReview_question(object.getString("customer_name"));
                            list.add(reviewModel);
                        }
                        adapter = new CustomerResturantAdapter(context, list);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                        dataId.setVisibility(View.VISIBLE);
                        dataId.setText(msg);
                    } else {
                        dataId.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressHD.dismiss();
                    Toast.makeText(CustomerResturantReviewAcivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();

                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(CustomerResturantReviewAcivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

}









