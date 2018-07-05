package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.model.BusinessDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusiFirstReg_20A_1 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BusiFirstReg_20A_1";
    CustomButton submitButton;
    CustomTextView businessName;
    ImageView tvFederalIdNumb_pop, multiSite_txt;
    String permit_no = "", business_name = "";
    private ImageView back;
    private Context mContext;
    private CustomEditText etGeTaxNo;
    private RadioGroup radioGroupMulti;
    private String getBusinessName, getfederalNo, value = "0", multisite = "no";
    private RelativeLayout mainView;
    private CustomEditText fein_id_ET;
    private BusinessDetails businessDetails;
    private ArrayList<String> businessNameArrayList;
    private LinearLayout llCorporateEntry;
    private CustomEditText tvCoEntityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busi_first_reg_20a_1);
        setToolbar();
        if (getIntent().hasExtra("business_name"))
            business_name = getIntent().getStringExtra("business_name");
        if (getIntent().hasExtra("permit_no"))
            permit_no = getIntent().getStringExtra("permit_no");
        Log.e(TAG, "onCreate: business_name >> " + business_name);
        Log.e(TAG, "onCreate: permit_no >> " + permit_no);
        init();
        setListener();
    }

    private void init() {
        mContext = this;
        businessDetails = new BusinessDetails();
        etGeTaxNo = (CustomEditText) findViewById(R.id.etGeTaxNo);
        radioGroupMulti = (RadioGroup) findViewById(R.id.radioGroupMulti);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        submitButton = (CustomButton) findViewById(R.id.submitButton);
        businessName = (CustomTextView) findViewById(R.id.textViewBusiName);
        fein_id_ET = (CustomEditText) findViewById(R.id.enterFeinHere);
        tvFederalIdNumb_pop = (ImageView) findViewById(R.id.tvFederalIdNumb_pop);
        multiSite_txt = (ImageView) findViewById(R.id.multiSite_txt);
        // tvFederalIdNumb_pop.setOnClickListener(this);
        //multiSite_txt.setOnClickListener(this);
        businessNameArrayList = new ArrayList<>();
        businessName.setText(business_name);
        llCorporateEntry = (LinearLayout) findViewById(R.id.llCorporateEntry);
        tvCoEntityName = (CustomEditText) findViewById(R.id.tvCoEntityName);

    }

    private void setListener() {
        radioGroupMulti.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.yesradioMultiSiteBusiness) {
                    value = "1";
                    multisite = "yes";
                    llCorporateEntry.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.noradioMultiSiteBusiness) {
                    llCorporateEntry.setVisibility(View.GONE);
                    value = "0";
                    multisite = "no";
                }
            }
        });
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBusinessName = businessName.getText().toString().trim();
                getfederalNo = fein_id_ET.getText().toString().trim();

                if (TextUtils.isEmpty(getBusinessName))
                    businessName.setError("Enter Business Name");
                else if (TextUtils.isEmpty(fein_id_ET.getText()))
                    fein_id_ET.setError("Enter FEIN No.");
                else if (multisite.equalsIgnoreCase("yes") && TextUtils.isEmpty(tvCoEntityName.getText().toString())) {
                    fein_id_ET.setError("Enter Corporate Entity");
                } else if (Util.isNetworkAvailable(mContext)) {
                    search_submit_business();
                } else {
                    Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                }
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
        ((TextView) findViewById(R.id.headet_text)).setText(R.string.business_registration);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void search_submit_business() {
        String feinId = fein_id_ET.getText().toString().trim();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.SEARCH_SUBMIT_BUSINESS);
        jsonObject.addProperty("business_name", businessName.getText().toString().trim());
        jsonObject.addProperty("fein_number", feinId);
        jsonObject.addProperty("corporate_entity_name", tvCoEntityName.getText().toString());
        jsonObject.addProperty("multisite", value);
        jsonObject.addProperty("permit_no", permit_no);
        jsonObject.addProperty("type", "search");
        Log.d(TAG, "REQUEST SUBMIT BUTTON >> " + jsonObject.toString());
        search_submit_business_Task(jsonObject);
    }

    private void search_submit_business_Task(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "RESPONSE SUBMIT BUTTON >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        progressHD.dismiss();
                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject jsonObject1 = resultJsonArray.getJSONObject(i);
                            String id = jsonObject1.getString("id");
                            String bussiness_name = jsonObject1.getString("bname");
                            String fein_Id = jsonObject1.getString("fein");
                            String personal_contactNo = jsonObject1.getString("contact_per");
                            String address = jsonObject1.getString("address");
                            String city = jsonObject1.getString("city");
                            String state = jsonObject1.getString("state");
                            String island = jsonObject1.getString("Island");
                            String phone2 = jsonObject1.getString("Phone2");
                            String permit_Status = jsonObject1.getString("Permit_Status");
                            String facility_type = jsonObject1.getString("Facility_type");
                            String contact_Name = jsonObject1.getString("Contact_Name");
                            String mailAddr1 = jsonObject1.getString("MailAddr1");
                            String mailCity = jsonObject1.getString("MailCity");
                            String zip = jsonObject1.getString("Zip");
                            String expireDate = jsonObject1.getString("ExpireDate");
                            String permit_company = jsonObject1.getString("permit_company");

                            businessDetails.setId(id);
                            businessDetails.setBussiness_name(bussiness_name);
                            businessDetails.setFein_id(fein_Id);
                            businessDetails.setPersonal_contactNo(personal_contactNo);
                            businessDetails.setAddress(address);
                            businessDetails.setCity(city);
                            businessDetails.setState(state);
                            businessDetails.setIsland(island);
                            businessDetails.setPhone2(phone2);
                            businessDetails.setPermit_company(permit_Status);
                            businessDetails.setFacility_type(facility_type);
                            businessDetails.setContact_Name(contact_Name);
                            businessDetails.setMailAddr1(mailAddr1);
                            businessDetails.setMailCity(mailCity);
                            businessDetails.setZip(zip);
                            businessDetails.setExpireDate(expireDate);
                            businessDetails.setPermit_company(permit_company);
                        }

                        Log.d(TAG, "Name :---" + businessDetails.getBussiness_name());
                        Log.d(TAG, "```````````` Fein id :- " + businessDetails.getFein_id());

                        AppPreferencesBuss.setBussiName(BusiFirstReg_20A_1.this, getBusinessName);
                        AppPreferencesBuss.setBussiFein(BusiFirstReg_20A_1.this, getfederalNo);

                        Intent intent = new Intent(BusiFirstReg_20A_1.this, BusinessRestSecondReg_20B.class);
                        intent.setAction("searchButton");
                        intent.putExtra("bussiness_name", businessDetails.getBussiness_name());
                        intent.putExtra("fein_Id", businessDetails.getFein_id());
                        intent.putExtra("multisite", multisite);
                        startActivity(intent);

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressHD.dismiss();
                Log.e(TAG, "error" + t.getMessage());
//                    t.getMessage();
            }
        });

    }


    private void businessRegisration() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.BUSINESSREGISTRATION);
        jsonObject.addProperty("business_name", getBusinessName);
        jsonObject.addProperty("business_fein_no", getfederalNo);
        jsonObject.addProperty("type", "new");
        Log.e(TAG, jsonObject.toString());
        businessRegisrationTask(jsonObject);
    }

    private void businessRegisrationTask(JsonObject jsonObject) {

        Log.v(TAG, "Request Json Object Business Registration :- " + jsonObject.toString());

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

                Log.e(TAG, "Response from BusinessReg 1st :- " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject object = new JSONObject();
                        object.put("business_name", getBusinessName);
                        object.put("business_fein_no", getfederalNo);
                        object.put("business_level", multisite);

                        Log.v(TAG, "Json Sending object :- " + object);

                        AppPreferencesBuss.setBussiName(BusiFirstReg_20A_1.this, getBusinessName);
                        AppPreferencesBuss.setBussiFein(BusiFirstReg_20A_1.this, getfederalNo);

                        Intent intent = new Intent(BusiFirstReg_20A_1.this, BusinessRestSecondReg_20B.class);
                        intent.setAction("submitButton");
                        intent.putExtra("data", object.toString());
                        startActivity(intent);
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error" + t.getMessage());
//                    t.getMessage();
                progressHD.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

     /*   if (v.getId() == R.id.tvFederalIdNumb_pop) {
            Function.bottomToolTipDialogBox(null, BusiFirstReg_20A_1.this, "This package is already selected by you !!!" *//*+ "\n Package Details : " + datalist.getPackage_detail()*//*, tvFederalIdNumb_pop, null);
        } else if (v.getId() == R.id.multiSite_txt) {
            Function.bottomToolTipDialogBox(null, BusiFirstReg_20A_1.this, "This package is already selected by you !!!" *//*+ "\n Package Details : " + datalist.getPackage_detail()*//*, multiSite_txt, null);
        }*/
        /*tvFederalIdNumb_pop.setOnClickListener(this);
        multiSite_txt.setOnClickListener(this);
        name_pop.setOnClickListener(this);
        tv_pop.setOnClickListener(this);
        pop.setOnClickListener(this);*/

    }
}
