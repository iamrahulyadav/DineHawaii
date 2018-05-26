package com.yberry.dinehawaii.Bussiness.Activity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.model.BusinessDetails;
import com.yberry.dinehawaii.Model.GeographicModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.SaveDataPreference;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.cpp.CountryCodePicker;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessRestSecondReg_20B extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BusiRestSecondReg_20B";
    JSONObject object;
    String multisite;
    BusinessDetails businessDetails;
    CountryCodePicker ccp;
    String exemptValue="0";
    LinearLayout llGetax;
    ArrayList<String> geographic_list;
    ArrayList<String> geographic_id_list;
    ArrayList<GeographicModel> geographicModel;
    ArrayAdapter<String> geographicAdapter;
    Spinner geographicArea;
    private CustomButton btnsubmit;
    private ImageView back;
    private CustomEditText userId, password, phoneNo, etGeTaxNo;
    private String getUserId, getPassword, businessName, federalNo, bussiPhoneNo;
    private Context mContext;
    private RadioGroup radioGroup, rgExemption;
    private String selected_geographic_location = "", selected_geographic_id = "";

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
        getGeographicArea();

    }

    @SuppressLint("LongLogTag")
    private void getGeographicArea() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.ALLGEOLOCATION);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(mContext));
        Log.e(TAG, jsonObject.toString());
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG + "onResponse", response.body().toString());
                String s = response.body().toString();
                geographic_list.clear();
                geographic_id_list.clear();
                geographicModel = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String name = jsonObject1.getString("geo_location_name");
                        String geo_id = jsonObject1.getString("id");
                        geographic_list.add(name);
                        geographic_id_list.add(geo_id);
                        GeographicModel m = new GeographicModel();
                        m.setGeo_id(geo_id);
                        m.setName(name);
                        geographicModel.add(m);
                    }
                    geographicAdapter = new ArrayAdapter<>(mContext, R.layout.row_spn, geographic_list);
                    geographicArea.setAdapter(geographicAdapter);
                    geographicArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
                            selected_geographic_location = geographic_list.get(position);
                            selected_geographic_id = geographic_id_list.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    Log.d("geographic_list", String.valueOf(geographic_list));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (progressHD != null && progressHD.isShowing())
                    progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                if (progressHD != null && progressHD.isShowing())
                    progressHD.dismiss();
            }
        });
    }

    private void init() {
        mContext = this;
        geographic_list = new ArrayList<>();
        geographic_id_list = new ArrayList<>();
        businessDetails = new BusinessDetails();
        userId = (CustomEditText) findViewById(R.id.password);
        rgExemption = (RadioGroup) findViewById(R.id.rgExemption);
        llGetax = (LinearLayout) findViewById(R.id.llGetax);
        password = (CustomEditText) findViewById(R.id.name);
        geographicArea = (Spinner) findViewById(R.id.geographic_area);
        etGeTaxNo = (CustomEditText) findViewById(R.id.etGeTaxNo);
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

        rgExemption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbexemptYes) {
                    exemptValue = "1";
                    llGetax.setVisibility(View.VISIBLE);
                } else {
                    exemptValue = "0";
                    llGetax.setVisibility(View.GONE);
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
            } else if (rgExemption.getCheckedRadioButtonId() == R.id.rdexemptYes && TextUtils.isEmpty(etGeTaxNo.getText().toString()))
                etGeTaxNo.setError("Enter GeTax no.");
            else if (selected_geographic_id.equalsIgnoreCase(""))
                Toast.makeText(mContext, "Select geographic area", Toast.LENGTH_SHORT).show();
            else {
                if (Util.isNetworkAvailable(mContext)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("method", AppConstants.REGISTRATION.BUSINESSREGISTRATIONWITHID);
                    jsonObject.addProperty("business_name", businessName);
                    jsonObject.addProperty("business_fein_no", federalNo);
                    jsonObject.addProperty("business_level", multisite);
                    jsonObject.addProperty("email_id", getUserId);
                    jsonObject.addProperty("password", getPassword);
                    jsonObject.addProperty("phone_no", bussiPhoneNo);
                    jsonObject.addProperty("business_get_tax_exemption", exemptValue);
                    jsonObject.addProperty("business_exemption_no", etGeTaxNo.getText().toString());
                    jsonObject.addProperty("geo_graphic_area",selected_geographic_id);
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
                        if (radioGroup.getCheckedRadioButtonId() == R.id.yes) {
                            SaveDataPreference.setBusRembEmailId(BusinessRestSecondReg_20B.this, jsonObject1.getString("email_id"));
                            SaveDataPreference.setBusRembPassword(BusinessRestSecondReg_20B.this, password.getText().toString());
                        } else {
                            SaveDataPreference.setBusRembEmailId(BusinessRestSecondReg_20B.this, "");
                            SaveDataPreference.setBusRembPassword(BusinessRestSecondReg_20B.this, "");
                        }
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
