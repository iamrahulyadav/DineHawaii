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
import android.widget.Button;
import android.widget.ImageView;
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
import com.yberry.dinehawaii.Util.Function;
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

public class BusiFirstReg_20A_2 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BusiFirstReg_20A_2";
    CustomButton submitButton;
    private ImageView back;
    private Context mContext;
    private RadioGroup radioGroupMulti;
    private String getBusinessName, getfederalNo, value = "0", multisite = "no", fein_id;
    private RelativeLayout mainView;
    private CustomEditText fein_id_ET;
    CustomEditText editTextBusiName;
    private BusinessDetails businessDetails;
    private ArrayList<String> businessNameArrayList;
    ImageView tvFederalIdNumb_pop, multiSite_txt;
    private String business_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busi_first_reg_20a_2);
        setToolbar();
        init();
        if (getIntent().hasExtra("business_name")) {
            business_name = getIntent().getStringExtra("business_name");
            editTextBusiName.setText(business_name);
            editTextBusiName.setSelection(editTextBusiName.getText().length());
        }

        Log.e(TAG, "onCreate: business_name >> " + business_name);
        setListener();
    }

    private void init() {
        mContext = this;
        businessDetails = new BusinessDetails();
        radioGroupMulti = (RadioGroup) findViewById(R.id.radioGroupMulti);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        submitButton = (CustomButton) findViewById(R.id.submitButton);
        editTextBusiName = (CustomEditText) findViewById(R.id.editTextBusiName);
        fein_id_ET = (CustomEditText) findViewById(R.id.enterFeinHere);
        tvFederalIdNumb_pop = (ImageView) findViewById(R.id.tvFederalIdNumb_pop);
        multiSite_txt = (ImageView) findViewById(R.id.multiSite_txt);
        tvFederalIdNumb_pop.setOnClickListener(this);
        multiSite_txt.setOnClickListener(this);
        businessNameArrayList = new ArrayList<>();
    }

    private void setListener() {
        radioGroupMulti.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.yesradioMultiSiteBusiness) {
                    value = "1";
                    multisite = "yes";
                } else if (checkedId == R.id.noradioMultiSiteBusiness) {
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
                getBusinessName = editTextBusiName.getText().toString().trim();
                getfederalNo = fein_id_ET.getText().toString().trim();
                Log.d("007value", value);
                Log.d("007value", multisite);
                if (getBusinessName.equalsIgnoreCase("")) {
                    editTextBusiName.setError("Enter business name");
                } else if (fein_id_ET.getText().toString().equalsIgnoreCase("")) {
                    fein_id_ET.setError("Enter FEIN No");
                } else if (Util.isNetworkAvailable(mContext)) {
                    businessRegisration();
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
        Log.v(TAG, "REQUEST SUBMIT BUTTON >> " + jsonObject.toString());
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

                Log.e(TAG, "RESPONSE SUBMIT BUTTON >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject object = new JSONObject();
                        object.put("business_name", getBusinessName);
                        object.put("business_fein_no", getfederalNo);
                        object.put("business_level", multisite);

                        Log.v(TAG, "Json Sending object :- " + object);

                        AppPreferencesBuss.setBussiName(BusiFirstReg_20A_2.this, getBusinessName);
                        AppPreferencesBuss.setBussiFein(BusiFirstReg_20A_2.this, getfederalNo);

                        Intent intent = new Intent(BusiFirstReg_20A_2.this, BusinessRestSecondReg_20B.class);
                        intent.setAction("submitButton");
                        intent.putExtra("data", object.toString());
                        startActivity(intent);
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(BusiFirstReg_20A_2.this, "Failed, try again later", Toast.LENGTH_SHORT).show();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error" + t.getMessage());
                Toast.makeText(BusiFirstReg_20A_2.this, "Failed, try again later", Toast.LENGTH_SHORT).show();

//                    t.getMessage();
                progressHD.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvFederalIdNumb_pop) {
            //Function.bottomToolTipDialogBox(null, BusiFirstReg_20A_2.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/, tvFederalIdNumb_pop, null);
        } else if (v.getId() == R.id.multiSite_txt) {
           // Function.bottomToolTipDialogBox(null, BusiFirstReg_20A_2.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/, multiSite_txt, null);
        }


        /*tvFederalIdNumb_pop.setOnClickListener(this);
        multiSite_txt.setOnClickListener(this);
        name_pop.setOnClickListener(this);
        tv_pop.setOnClickListener(this);
        pop.setOnClickListener(this);*/

    }
}
