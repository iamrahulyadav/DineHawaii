package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.JustifiedTextView;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.SaveDataPreference;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomCheckBox;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.Function.errorMessage;

public class BusinessLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BusinessLoginActivity";
    Context mContext;
    CustomTextView forgetPass, forgetId;
    ImageView user_pop, pass_pop;
    CustomCheckBox rememberMe;
    private CustomButton loginBotton;
    private CustomEditText edittext_id, edittext_pass;
    private ImageView back;
    private String admin_approval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_business_login);
        LinearLayout mainView = (LinearLayout) findViewById(R.id.activity_business__login_);

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
        init();


    }

    private void init() {
        mContext = this;
        loginBotton = (CustomButton) findViewById(R.id.loginBotton);
        edittext_id = (CustomEditText) findViewById(R.id.edittext_id);
        edittext_pass = (CustomEditText) findViewById(R.id.edittext_pass);
        forgetPass = (CustomTextView) findViewById(R.id.forgetPass);
        rememberMe = (CustomCheckBox) findViewById(R.id.rememberme);
        user_pop = (ImageView) findViewById(R.id.user_pop);
        pass_pop = (ImageView) findViewById(R.id.pass_pop);
        //forgetId = (CustomTextView) findViewById(R.id.forgetId);
        edittext_id.setText(AppPreferences.getSaveid(BusinessLoginActivity.this));
        edittext_pass.setText(AppPreferences.getSavepass(BusinessLoginActivity.this));

        loginBotton.setOnClickListener(this);
        edittext_id.setOnClickListener(this);
        edittext_pass.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
        user_pop.setOnClickListener(this);
        pass_pop.setOnClickListener(this);
        setToolbar();

        if (!SaveDataPreference.getBusRembEmailId(BusinessLoginActivity.this).equalsIgnoreCase("")) {
            rememberMe.setChecked(true);
            edittext_id.setText(SaveDataPreference.getBusRembEmailId(BusinessLoginActivity.this));
            edittext_id.setSelection(edittext_id.getText().toString().length());
        }

        if (!SaveDataPreference.getBusRembPassword(BusinessLoginActivity.this).equalsIgnoreCase("")) {
            edittext_pass.setText(SaveDataPreference.getBusRembPassword(BusinessLoginActivity.this));
            edittext_pass.setSelection(edittext_pass.getText().toString().length());
        }

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SaveDataPreference.setBusRembEmailId(BusinessLoginActivity.this, edittext_id.getText().toString());
                    SaveDataPreference.setBusRembPassword(BusinessLoginActivity.this, edittext_pass.getText().toString());
                } else {
                    SaveDataPreference.setBusRembEmailId(BusinessLoginActivity.this, "");
                    SaveDataPreference.setBusRembPassword(BusinessLoginActivity.this, "");
                }
            }
        });
    }


    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((CustomTextView) findViewById(R.id.headet_text)).setText("Business/Restaurant Login");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBotton) {
            String email = edittext_id.getText().toString();
            String password = edittext_pass.getText().toString();
            if (email.isEmpty()) {
                edittext_id.setError("Enter username");
            } else if (Function.isEmailNotValid(edittext_id)) {
                edittext_id.setError(errorMessage);
            } else if (password.isEmpty()) {
                edittext_pass.setError("Enter password");
            } else if (!rememberMe.isChecked()) {
                SaveDataPreference.setBusRembEmailId(BusinessLoginActivity.this, "");
                SaveDataPreference.setBusRembPassword(BusinessLoginActivity.this, "");
                loginApi();
            } else {
                loginApi();
                SaveDataPreference.setBusRembEmailId(BusinessLoginActivity.this, edittext_id.getText().toString());
                SaveDataPreference.setBusRembPassword(BusinessLoginActivity.this, edittext_pass.getText().toString());
            }
        } else if (v.getId() == R.id.forgetPass) {
            Intent i = new Intent(BusinessLoginActivity.this, BusinessForgetPassword.class);
            startActivity(i);
        } else if (v.getId() == R.id.pass_pop) {
            // Function.bottomToolTipDialogBox(null, BusinessLoginActivity.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/, pass_pop, null);
        } else if (v.getId() == R.id.user_pop) {
            // Function.bottomToolTipDialogBox(null, BusinessLoginActivity.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/, user_pop, null);
        }
    }

    private void loginApi() {
        if (Util.isNetworkAvailable(mContext)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.REGISTRATION.BUSINESSUSERLOGIN);
            jsonObject.addProperty("email_id", edittext_id.getText().toString());
            jsonObject.addProperty("password", edittext_pass.getText().toString());
            jsonObject.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
            Log.e(TAG, "Request BUSINESS LOGIN >> " + jsonObject.toString());
            JsonCallMethod(jsonObject);

        } else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_SHORT).show();

        }
    }

    private void JsonCallMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUrl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response BUSINESS LOGIN >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        final JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        final Dialog paymentDialog = new Dialog(BusinessLoginActivity.this);
                        paymentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        paymentDialog.setCancelable(true);
                        paymentDialog.setContentView(R.layout.business_t_n_c);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(paymentDialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.gravity = Gravity.CENTER;
                        paymentDialog.getWindow().setAttributes(lp);

                        JustifiedTextView justifytxt = (JustifiedTextView) paymentDialog.findViewById(R.id.justifytxt);
                        final CustomButton accept = (CustomButton) paymentDialog.findViewById(R.id.accept);
                        final CustomButton reject = (CustomButton) paymentDialog.findViewById(R.id.reject);
                        justifytxt.setText(getResources().getString(R.string.text_21B) + " " + getResources().getString(R.string.text_21B));

                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {

                                    AppPreferencesBuss.setUserId(BusinessLoginActivity.this, jsonObject1.getString("user_id"));
                                    AppPreferencesBuss.setBussiEmilid(BusinessLoginActivity.this, jsonObject1.getString("email_id"));
                                    AppPreferencesBuss.setBussiName(BusinessLoginActivity.this, jsonObject1.getString("bussiness_name"));
                                    AppPreferencesBuss.setBussiFein(BusinessLoginActivity.this, jsonObject1.getString("bussiness_fein"));
                                    AppPreferencesBuss.setBussiId(BusinessLoginActivity.this, jsonObject1.getString("bussiness_id"));
                                    AppPreferencesBuss.setBussiPackagelist(BusinessLoginActivity.this, jsonObject1.getString("package_id"));
                                    AppPreferencesBuss.setBussiOptionlist(BusinessLoginActivity.this, jsonObject1.getString("option_id"));
                                    AppPreferencesBuss.setfirstname(BusinessLoginActivity.this, jsonObject1.getString("first_name"));
                                    AppPreferencesBuss.setBussiPhoneNo(BusinessLoginActivity.this, jsonObject1.getString("contact_no"));
                                    AppPreferencesBuss.setUsertypeid(BusinessLoginActivity.this, jsonObject1.getString("user_type"));

                                    if (jsonObject1.getString("multisite").equalsIgnoreCase("1"))
                                        AppPreferencesBuss.setIsMultisite(BusinessLoginActivity.this, true);
                                    else
                                        AppPreferencesBuss.setIsMultisite(BusinessLoginActivity.this, false);

                                    AppPreferences.setUserType(mContext, AppConstants.BUSS_LOGIN_TYPE.BUSINESS_USER);

                                    if (jsonObject1.getString("admin_approval").equalsIgnoreCase("1"))
                                        AppPreferencesBuss.setVerified_status(BusinessLoginActivity.this, true);
                                    else
                                        AppPreferencesBuss.setVerified_status(BusinessLoginActivity.this, false);

                                    if (jsonObject1.getString("user_image").length() == 0) {
                                        AppPreferencesBuss.setProfileImage(BusinessLoginActivity.this, "");
                                    } else {
                                        AppPreferencesBuss.setProfileImage(BusinessLoginActivity.this, jsonObject1.getString("user_image"));
                                    }


                                    admin_approval = jsonObject1.getString("admin_approval");
                                    if (admin_approval.equalsIgnoreCase("0")) {
                                        Toast.makeText(getApplicationContext(), "After admin approval, your business details will be displayed to the users", Toast.LENGTH_LONG).show();
                                    } else {
                                    }


                                    AppPreferencesBuss.setSaveIdPass(BusinessLoginActivity.this, edittext_id.getText().toString(), edittext_pass.getText().toString());
                                    Intent intent = new Intent(getApplicationContext(), BusinessNaviDrawer.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    if (AppPreferencesBuss.getIsMultisite(BusinessLoginActivity.this))
                                        intent.putExtra("multisite", "yes");
                                    startActivity(intent);

                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (paymentDialog.isShowing()) paymentDialog.dismiss();
                            }
                        });
                        paymentDialog.show();

                        /*if (!isFinishing())
                            paymentDialog.show();
*/

                        //AppPreferences.setCustomerid(BusinessLoginActivity.this, jsonObject1.getString("user_id"));


                    } else if (jsonObject.getString("status").equalsIgnoreCase("300")) {
                       /* if (jsonObject1.getString("email_id").equalsIgnoreCase("test12345@gmail.com")) {
                            AppPreferences.setUserType(mContext, "BussinessLocalUser");
                            AppPreferencesBuss.setAllottedDuties(mContext, "4");
                        } else {
                            AppPreferences.setUserType(mContext, "BussinessUser");
                        }*/
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        Log.e(TAG, "onResponse: " + jsonArray);

                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        AppPreferencesBuss.setfirstname(BusinessLoginActivity.this, jsonObject1.getString("emp_name"));
                        AppPreferencesBuss.setBussiId(BusinessLoginActivity.this, jsonObject1.getString("emp_business"));
                        AppPreferencesBuss.setAllottedDuties(BusinessLoginActivity.this, jsonObject1.getString("emp_duties"));
                        AppPreferencesBuss.setBussiPackagelist(BusinessLoginActivity.this, jsonObject1.getString("package_id"));
                        AppPreferencesBuss.setBussiOptionlist(BusinessLoginActivity.this, jsonObject1.getString("package_id"));
                        AppPreferencesBuss.setUserId(BusinessLoginActivity.this, jsonObject1.getString("user_id"));
                        AppPreferencesBuss.setBussiEmilid(BusinessLoginActivity.this, jsonObject1.getString("emp_email"));
                        AppPreferencesBuss.setBussiPhoneNo(BusinessLoginActivity.this, jsonObject1.getString("emp_contact"));
                        AppPreferencesBuss.setBussiFein(BusinessLoginActivity.this, jsonObject1.getString("emp_dine_app_id"));
                        AppPreferences.setUserType(mContext, AppConstants.BUSS_LOGIN_TYPE.BUSSINESS_LOCAL_USER);
                        AppPreferencesBuss.setEmpPosition(mContext, jsonObject1.getString("emp_position_name"));
                        AppPreferencesBuss.setEmpPositionId(mContext, jsonObject1.getString("emp_position"));
                        AppPreferencesBuss.setUsertypeid(BusinessLoginActivity.this, jsonObject1.getString("user_type"));
                        AppPreferencesBuss.setSaveIdPass(BusinessLoginActivity.this, edittext_id.getText().toString(), edittext_pass.getText().toString());
                        Intent intent1 = new Intent(getApplicationContext(), BusinessNaviDrawer.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject12 = jsonArray.getJSONObject(0);
                        Log.d("onResponse", jsonObject12.getString("msg"));
                        Toast.makeText(BusinessLoginActivity.this, jsonObject12.getString("msg"), Toast.LENGTH_SHORT).show();
                    } /*else if (jsonObject.getString("status").equalsIgnoreCase("700")) {
                        AppPreferences.setUserType(mContext, AppConstants.BUSS_LOGIN_TYPE.VENDOR_USER);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        AppPreferencesBuss.setfirstname(BusinessLoginActivity.this, jsonObject1.getString("first_name") + " " + jsonObject1.getString("last_name"));
                        AppPreferencesBuss.setUsertypeid(BusinessLoginActivity.this, jsonObject1.getString("user_type"));
                        AppPreferencesBuss.setVendorUrl(BusinessLoginActivity.this, jsonObject1.getString("VENDOR_ADMIN_Url"));
                        AppPreferencesBuss.setUserId(BusinessLoginActivity.this, jsonObject1.getString("user_id"));
                        Intent intent = new Intent(getApplicationContext(), VendorLoginWebViewActivity.class);
                        intent.putExtra("vendor_url", jsonObject1.getString("VENDOR_ADMIN_Url"));
                        startActivity(intent);
                    } */ else {
                        Intent intent = new Intent(getApplicationContext(), BusinessLoginError.class);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}