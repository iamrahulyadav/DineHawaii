package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.OnClickListener;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.adapter.CheckBoxOptionAdapter;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessPositionActivity extends AppCompatActivity implements OnClickListener {
    CheckBoxOptionAdapter ad;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private String TAG = "BUSPOSITION";
    private ArrayList<CheckBoxPositionModel> list;
    private ArrayList<String> selectedDuty;
    private String[] duty_list;
    CustomTextView tvaddduty;
    Dialog popup;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_position);
        setToolbar();
        init();
        if (getIntent().getAction().equalsIgnoreCase("update_duties")) ;
        {
            String emp_duties = getIntent().getStringExtra("emp_duties");
            Log.e(TAG, "onCreate: emp_duties>> " + emp_duties);
            if (!emp_duties.equalsIgnoreCase("")) {
                duty_list = emp_duties.split(",");
                Log.e(TAG, "onCreate: duty_list >> " + duty_list.length);
            }
        }
        getAllDuties();
    }

    private void init() {
        context = this;
        list = new ArrayList<>();
        selectedDuty = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvaddduty = (CustomTextView) findViewById(R.id.tvaddduty);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(BusinessPositionActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) list, new CheckBoxOptionAdapter.setOnClickListener() {
            @Override
            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

            }
        });
        mRecyclerView.setAdapter(ad);
        tvaddduty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDutyDialog();
            }
        });
    }

    private void showAddDutyDialog() {
        popup = new Dialog(context);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.food_category_dialog);
        final CustomEditText foodtype = (CustomEditText) popup.findViewById(R.id.new_category);
        final CustomTextView subtitle = (CustomTextView) popup.findViewById(R.id.addcategory);
        final CustomTextView title = (CustomTextView) popup.findViewById(R.id.foodTitle);
        title.setText("Employee Duty");
        subtitle.setText("Add Employee Duty");
        foodtype.setHint("Enter duty");
        popup.findViewById(R.id.popupclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.findViewById(R.id.cat_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(foodtype.getText().toString())) {
                    foodtype.setError("Enter Position");
                } else {
                    if (Util.isNetworkAvailable(context)) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.GENERALAPI.ADDEMPDUTY);
                        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
                        jsonObject.addProperty("duty", foodtype.getText().toString());
                        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
                        Log.e(TAG, "add emppos json" + jsonObject.toString());
                        addNewDutyApi(jsonObject);
                    } else {
                        Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        popup.show();
    }

    private void addNewDutyApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_add_pos_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "get addpos response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        popup.dismiss();
                        list.clear();
                        getAllDuties();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        popup.dismiss();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        popup.dismiss();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllDuties() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLDUTIES);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(BusinessPositionActivity.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(BusinessPositionActivity.this));
        Log.e(TAG, "Request GET ALLDUTIES >> " + jsonObject.toString());
        JsonCallMethod(jsonObject);
    }

    private void JsonCallMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(BusinessPositionActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET ALLDUTIES >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CheckBoxPositionModel model = new CheckBoxPositionModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            model.setId(object.getString("duty_id"));
                            model.setName(object.getString("duty"));
                            model.setSelected(false);
                            //model.setSelected(object.getBoolean("select"));
                            if (duty_list != null)
                                if (duty_list.length > 0) {
                                    for (int j = 0; j < duty_list.length; j++) {
                                        if (duty_list[j].equalsIgnoreCase(model.getId())) {
                                            model.setChckStatus(true);
                                            selectedDuty.add(model.getId());
                                            Log.e(TAG, "onResponse: model >> " + model);
                                        }
                                    }
                                }
                            list.add(model);

                        }

                        CheckBoxOptionAdapter adapter = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) list, new CheckBoxOptionAdapter.setOnClickListener() {
                            @Override
                            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

                            }
                        });

                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else if (jsonObject.getString("status").equalsIgnoreCase("400")){
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(BusinessPositionActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_food, menu);
        return true;
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Duties");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addfood:
                ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("jobduties", checkBoxOptionList);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String s, int i) {
        if (i == 1) {
            selectedDuty.add(s);
        } else {
            selectedDuty.remove(s);
        }
        Log.e(TAG, "SelectedDuty : " + selectedDuty.toString());
    }
}