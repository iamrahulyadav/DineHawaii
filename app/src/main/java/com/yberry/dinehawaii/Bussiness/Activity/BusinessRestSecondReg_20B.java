package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.model.BusinessDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.cpp.CountryCodePicker;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessRestSecondReg_20B extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BusiRestSecondReg_20B";
    JSONObject object;
    private CustomButton btnsubmit;
    private ImageView back;
    private CustomEditText userId, password, phoneNo;
    private String getUserId, getPassword, businessName, federalNo, radioButtonValue, bussiPhoneNo;
    private Context mContext;
    private RadioGroup radioGroup;
    String multisite;
    BusinessDetails businessDetails;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_restaurent_registration);
        setToolbar();
        if (getIntent().getAction().equalsIgnoreCase("searchButton")) {
            businessName = getIntent().getExtras().getString("bussiness_name");
            federalNo = getIntent().getExtras().getString("fein_Id");
            multisite = getIntent().getExtras().getString("multisite");
        } else if (getIntent().getAction().equalsIgnoreCase("submitButton")) {
            Intent intent = getIntent();
            String jsonString = intent.getStringExtra("data");
            Log.v(TAG, "Receive Json using intent :- " + jsonString);
            String profile = getIntent().getStringExtra("data");
            Log.v(TAG, "Receive Json using intent proooo:- " + profile);
            try {
                if (getIntent().getExtras() != null) {
                    object = new JSONObject(getIntent().getStringExtra("data"));
                    Log.d(TAG, "DineHawaii Object Data :- " + object.toString());
                    businessName = object.getString("business_name");
                    federalNo = object.getString("business_fein_no");
                    multisite = object.getString("business_level");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        init();
    }

    private void init() {
        mContext = this;
        businessDetails = new BusinessDetails();
        userId = (CustomEditText) findViewById(R.id.password);
        password = (CustomEditText) findViewById(R.id.name);
        phoneNo = (CustomEditText) findViewById(R.id.mobileNo);
        btnsubmit = (CustomButton) findViewById(R.id.submitButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.getSelectedCountryCode().toString().trim();
        AppPreferences.setcountrycode(BusinessRestSecondReg_20B.this, ccp);
        Log.d("codecountry", ccp.getSelectedCountryCode().toString().trim());
        btnsubmit.setOnClickListener(this);

        RelativeLayout mainView = (RelativeLayout) findViewById(R.id.activity_main);

        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes) {
                    AppPreferences.setSaveIdPass(BusinessRestSecondReg_20B.this,
                            userId.getText().toString(), password.getText().toString());
                } else {
                    AppPreferences.setSaveIdPass(BusinessRestSecondReg_20B.this, "", "");
                }
            }
        });
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submitButton) {
            getUserId = userId.getText().toString().trim();
            getPassword = password.getText().toString().trim();
            bussiPhoneNo = phoneNo.getText().toString().trim();

            if (getUserId.isEmpty()) {
                userId.setError("Enter user-id");
            } else if (Function.isEmailNotValid(userId)) {
                userId.setError("Enter email-id");
            } else if (getPassword.isEmpty()) {
                password.setError("Enter password");
            } else if (bussiPhoneNo.isEmpty()) {
                phoneNo.setError("Enter phone no");
            } else {
                if (Util.isNetworkAvailable(mContext)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("method", AppConstants.REGISTRATION.BUSINESSREGISTRATIONWITHID);
                    jsonObject.addProperty("business_name", businessName);
                    jsonObject.addProperty("business_fein_no", federalNo);
                    jsonObject.addProperty("business_level", multisite);
                    jsonObject.addProperty("email_id", getUserId);
                    jsonObject.addProperty("password", getPassword);
                    jsonObject.addProperty("phone_no", bussiPhoneNo);
                    jsonObject.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
                    Log.e(TAG, jsonObject.toString());
                    JsonCallMethod(jsonObject);
                } else {
                    Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void JsonCallMethod(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUrl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        AppPreferences.setUserType(BusinessRestSecondReg_20B.this, "BussinessUser");
                        AppPreferences.setCustomerid(BusinessRestSecondReg_20B.this, jsonObject1.getString("user_id"));
                        AppPreferencesBuss.setUserId(BusinessRestSecondReg_20B.this, jsonObject1.getString("user_id"));
                        AppPreferencesBuss.setBussiEmilid(BusinessRestSecondReg_20B.this, jsonObject1.getString("email_id"));
                        AppPreferencesBuss.setBussiName(BusinessRestSecondReg_20B.this, jsonObject1.getString("business_name"));
                        AppPreferencesBuss.setBussiFein(BusinessRestSecondReg_20B.this, jsonObject1.getString("business_fein"));
                        AppPreferencesBuss.setBussiId(BusinessRestSecondReg_20B.this, jsonObject1.getString("business_id"));
                        AppPreferencesBuss.setBussiPhoneNo(BusinessRestSecondReg_20B.this, jsonObject1.getString("phone_no"));

                        Intent intent = new Intent(BusinessRestSecondReg_20B.this, BusiSelectPackageActivity.class);
                        intent.setAction("register");
                        intent.putExtra("title", "BUSINESS INFORMATION");
                        startActivity(intent);
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error Message" + t.getMessage());
                progressHD.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Business Registration");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
