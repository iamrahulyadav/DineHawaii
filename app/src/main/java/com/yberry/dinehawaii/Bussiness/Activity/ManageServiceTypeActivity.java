package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.CheckBoxOptionAdapter;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
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

public class ManageServiceTypeActivity extends AppCompatActivity {
    private static final String TAG = "ManageService";
    public static List<CheckBoxPositionModel> serviceList;
    RecyclerView recyclerView;
    CustomTextView noservice;
    CheckBoxOptionAdapter ad;
    ArrayAdapter<CheckBoxPositionModel> adapter;
    RelativeLayout layoutLinear;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    private String service_id_list = "", logintype = "";
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_service_type);
        setToolbar();
        init();
        getAllServices();
    }

    private void getAllServices() {
        if (Util.isNetworkAvailable(ManageServiceTypeActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.MYSERVICES);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ManageServiceTypeActivity.this));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ManageServiceTypeActivity.this));
            Log.e(TAG, "Request GET ALL SERVICES >> " + jsonObject.toString());
            getServiceApi(jsonObject);
        } else {
            Toast.makeText(ManageServiceTypeActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getServiceApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(ManageServiceTypeActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.buss_service(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    Log.e(TAG, "Response GET ALL SERVICES >> " + response.body().toString());
                    String s = response.body().toString();

                    serviceList.clear();
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            CheckBoxPositionModel model = new CheckBoxPositionModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            model.setName(object.getString("service_name"));
                            model.setId(object.getString("id"));
                            String status = object.getString("available");
                            if (status.equalsIgnoreCase("true")) {
                                model.setChckStatus(true);
                            } else if (status.equalsIgnoreCase("false")) {
                                model.setChckStatus(false);
                            } else {
                                model.setChckStatus(false);
                            }

                            serviceList.add(model);

                        }
                        CheckBoxOptionAdapter ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) serviceList, new CheckBoxOptionAdapter.setOnClickListener() {
                            @Override
                            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

                            }
                        });

                        recyclerView.setAdapter(ad);
                        ad.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equals("400")) {
                        noservice.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ManageServiceTypeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ManageServiceTypeActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();

                }
                progressHD.dismiss();
                swipeRefreshLayout.setRefreshing(false);

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                swipeRefreshLayout.setRefreshing(false);

                progressHD.dismiss();
                Toast.makeText(ManageServiceTypeActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            service_id_list = "";
            ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
            for (CheckBoxPositionModel listValue : checkBoxOptionList) {
                if (service_id_list.equalsIgnoreCase("")) {
                    service_id_list = listValue.getId();
                } else {
                    service_id_list = service_id_list + "," + listValue.getId();
                }
            }
            importServiceTypes();
            Log.d(TAG, "selected check box :- " + service_id_list);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void importServiceTypes() {
        if (Util.isNetworkAvailable(ManageServiceTypeActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.IMPORT_SERVICE_TYPE);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ManageServiceTypeActivity.this));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ManageServiceTypeActivity.this));
            jsonObject.addProperty("update_type", logintype);
            jsonObject.addProperty("service_id", service_id_list);
            Log.e(TAG, "import service request " + jsonObject);
            importServiceTypesApi(jsonObject);
        } else {
            Toast.makeText(ManageServiceTypeActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void importServiceTypesApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(ManageServiceTypeActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.buss_service(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "import service response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Toast.makeText(ManageServiceTypeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(ManageServiceTypeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ManageServiceTypeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ManageServiceTypeActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        if (AppPreferences.getUserType(ManageServiceTypeActivity.this).equalsIgnoreCase("BussinessLocalUser")) {
            logintype = "emp";
        } else if (AppPreferences.getUserType(ManageServiceTypeActivity.this).equalsIgnoreCase("BussinessUser")) {
            logintype = "buss";
        } else {
            logintype = "buss";
        }
        serviceList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshService);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ManageServiceTypeActivity.this));
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        noservice = (CustomTextView) findViewById(R.id.noservice);
        ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) serviceList, new CheckBoxOptionAdapter.setOnClickListener() {
            @Override
            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

            }
        });
        recyclerView.setAdapter(ad);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllServices();
            }
        });
    }


    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Service Type");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
