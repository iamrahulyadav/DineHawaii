package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.BusinessListAdapter;
import com.yberry.dinehawaii.Bussiness.model.OtherBusinessModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBusinessListActivity extends AppCompatActivity {

    private static final String TAG = "MyBusinessListActivity";
    private MyBusinessListActivity context;
    private TextView tvTitle;
    private RecyclerView recycler_view;
    private ArrayList<OtherBusinessModel> list = new ArrayList<OtherBusinessModel>();
    private BusinessListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_list);
        setToolbar();
        context = this;
        setAdapter();
        getData();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setData() {
        list.add(new Gson().fromJson("{\"id\": \"1\",\"business_name\": \"Sayaji Indore\",\"business_email\": \"rk1@gmail.com\",\"business_contact_no\": \"14548787\",\"business_logo\": \"\",\"business_address\": \"Dewas Naka, Indore\"}", OtherBusinessModel.class));
        list.add(new Gson().fromJson("{\"id\": \"1\",\"business_name\": \"Sayaji Indore\",\"business_email\": \"mohit@gmail.com\",\"business_contact_no\": \"5655454545\",\"business_logo\": \"\",\"business_address\": \"Vijay Nagar, Indore\"}", OtherBusinessModel.class));
        adapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new BusinessListAdapter(MyBusinessListActivity.this, context, list);
        recycler_view.setAdapter(adapter);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle = (TextView) findViewById(R.id.headet_text);
        tvTitle.setText("My Business");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getData() {
        if (Util.isNetworkAvailable(MyBusinessListActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.GET_MY_BUSINESS);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(MyBusinessListActivity.this));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(MyBusinessListActivity.this));
            Log.e(TAG, "getData: Request >> " + jsonObject.toString());

            final ProgressHUD progressHD = ProgressHUD.show(MyBusinessListActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "getData: Response >> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                OtherBusinessModel model = new Gson().fromJson(String.valueOf(jsonObject1), OtherBusinessModel.class);
                                list.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                            Toast.makeText(MyBusinessListActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressHD.dismiss();
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, " Error :- " + Log.getStackTraceString(t));
                    progressHD.dismiss();
                }
            });

        } else {
            Toast.makeText(MyBusinessListActivity.this, "Please Connect Your Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
