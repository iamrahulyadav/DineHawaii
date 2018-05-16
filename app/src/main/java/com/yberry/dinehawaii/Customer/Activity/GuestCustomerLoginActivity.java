package com.yberry.dinehawaii.Customer.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.User;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.DialogManager;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.SaveDataPreference;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomCheckBox;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yberry.dinehawaii.Util.Function.fieldRequired;

public class GuestCustomerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GusetCustLoginActivity";
    private ImageView back;
    CustomEditText mUserName,mPassword;
    CustomTextView mLoasPassword,mSignup;
    ImageView txt,txt1;
    CustomButton btnLogin;
    CustomCheckBox rememberMeCust;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_custumer_login_acivity);
        LinearLayout mainView = (LinearLayout) findViewById(R.id.activity_main);
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
        setToolbar();
        init();


        listener();
    }
    private void init() {
        back = (ImageView)findViewById(R.id.back);
        mUserName = (CustomEditText) findViewById(R.id.mUserName);
        mPassword = (CustomEditText) findViewById(R.id.mPassword);
        btnLogin = (CustomButton) findViewById(R.id.btnLogin);
        mSignup = (CustomTextView) findViewById(R.id.mSignup);
        mLoasPassword = (CustomTextView) findViewById(R.id.mLoasPassword);
        rememberMeCust = (CustomCheckBox)findViewById(R.id.remembermecust);
        txt = (ImageView) findViewById(R.id.txt);
        txt1 = (ImageView) findViewById(R.id.txt1);
        mSignup.setOnClickListener(this);


        if (!SaveDataPreference.getCustRembEmailId(GuestCustomerLoginActivity.this).equalsIgnoreCase("")) {
            rememberMeCust.setChecked(true);
            mUserName.setText(SaveDataPreference.getCustRembEmailId(GuestCustomerLoginActivity.this));
            mUserName.setSelection(mUserName.getText().toString().length());
        }

        if (!SaveDataPreference.getCustRembPassword(GuestCustomerLoginActivity.this).equalsIgnoreCase("")) {
            mPassword.setText(SaveDataPreference.getCustRembPassword(GuestCustomerLoginActivity.this));
            mPassword.setSelection(mPassword.getText().toString().length());
        }

        rememberMeCust.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SaveDataPreference.setCustRembEmailId(GuestCustomerLoginActivity.this,mUserName.getText().toString());
                    SaveDataPreference.setCustRembPassword(GuestCustomerLoginActivity.this,mPassword.getText().toString());
                }else{
                    SaveDataPreference.setCustRembEmailId(GuestCustomerLoginActivity.this,"");
                    SaveDataPreference.setCustRembPassword(GuestCustomerLoginActivity.this,"");
                }
            }
        });
    }
    private void listener(){
        back.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        mLoasPassword.setOnClickListener(this);
        txt.setOnClickListener(this);
        txt1.setOnClickListener(this);
    }
    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Guest/Customer Login");
//        back.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
        }else if(v.getId() == R.id.mSignup){
            Intent intent = new Intent(getApplicationContext(), GuestCustRegisterActivity.class);
            startActivity(intent);
        }

        else if(v.getId() == R.id.mLoasPassword){
            Intent intent = new Intent(getApplicationContext(), CustomerForgotPassword.class);
            startActivity(intent);
        } else if(v.getId() == R.id.txt){
            //Function.bottomToolTipDialogBox(null, GuestCustomerLoginActivity.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/ , txt, null);
        }else if(v.getId() == R.id.txt1){
            // Function.bottomToolTipDialogBox(null, GuestCustomerLoginActivity.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/ , txt1, null);
        }


        else if (v.getId() == R.id.btnLogin) {
           /* Intent intent=new Intent(getApplicationContext(),CustomerNaviDrawer.class);
            startActivity(intent);
            finish();*/
            if(TextUtils.isEmpty(mUserName.getText().toString())){
                mUserName.setError(fieldRequired);
            }else if(TextUtils.isEmpty(mPassword.getText().toString())){
                mPassword.setError(fieldRequired);
            }else if (!rememberMeCust.isChecked()){
                loginApi();
                SaveDataPreference.setCustRembEmailId(GuestCustomerLoginActivity.this,"");
                SaveDataPreference.setCustRembPassword(GuestCustomerLoginActivity.this,"");
            }else{
                loginApi();
                SaveDataPreference.setCustRembEmailId(GuestCustomerLoginActivity.this,mUserName.getText().toString());
                SaveDataPreference.setCustRembPassword(GuestCustomerLoginActivity.this,mPassword.getText().toString());
            }
        }
    }

    private void loginApi() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD,AppConstants.REGISTRATION.USERLOGIN);
        jsonObject.addProperty(AppConstants.KEY_EMAILID, mUserName.getText().toString());
        jsonObject.addProperty(AppConstants.KEY_PASSWORD, mPassword.getText().toString());
        jsonObject.addProperty("user_type", "3");
        jsonObject.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
        loginTask(jsonObject);
        Log.v(TAG, "Guest Login Request :-" + jsonObject);
    }

    private void loginTask(JsonObject jsonObject) {

        final DialogManager dialogManager = new DialogManager();
        dialogManager.showProcessDialog(GuestCustomerLoginActivity.this,"Please Wait...");
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(AppConstants.BASEURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);

        Call<JsonObject> call = apiService.request(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               // Log.d("Response: request 1", call.request().toString());
//                Log.d("Response: Registraion 1", response.body().toString());

            //    Log.v(TAG, "Guest Login Request :-" + response.body().toString());

                JsonObject jsonObject = response.body();
                Log.d("status", String.valueOf(jsonObject));
                if(jsonObject.get("status").getAsString().equals("200")){
                    JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    JsonObject result = jsonArray.get(0).getAsJsonObject();
                    User user = new Gson().fromJson(result,User.class);
                    Log.d("Response: user_id ", user.toString());

                    /*:[{"user_id":"8","first_name":"ashu","last_name":"kh","email_id":"ashu@gmail.com","contact_no":"9713891879",
                    "country_type":"+91","address":"Zanjeer wala square","city":"Inodre","state":"MP"}]*/
                    AppPreferences.setUserType(GuestCustomerLoginActivity.this,"CustomerUser");
                    AppPreferences.setCustomerid(GuestCustomerLoginActivity.this, String.valueOf(user.getId()));
                    AppPreferences.setEmailSetting(GuestCustomerLoginActivity.this,String.valueOf(user.getEmailId()));
                    AppPreferences.setCustomername(GuestCustomerLoginActivity.this, String.valueOf(user.getFName() +" "+user.getLName()));
                    AppPreferences.setCustomerMobile(GuestCustomerLoginActivity.this, String.valueOf(user.getMobile()));
                    AppPreferences.setCustomerAddress(GuestCustomerLoginActivity.this, String.valueOf(user.getAddress()) + ","+ user.getCity());
                    AppPreferences.setCustAddrLat(GuestCustomerLoginActivity.this, String.valueOf(user.getAddress_Long()));
                    AppPreferences.setCustAddrLong(GuestCustomerLoginActivity.this, String.valueOf(user.getUserImage()));
                    if(user.getUserImage().length()==0){
                        AppPreferences.setCustomerPic(GuestCustomerLoginActivity.this, "");
                    }else {
                        AppPreferences.setCustomerPic(GuestCustomerLoginActivity.this, user.getUserImage());
                        Log.d("pic02", user.getUserImage());
                    }


                    Intent intent=new Intent(getApplicationContext(),CustomerNaviDrawer.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("data", user);
                    startActivity(intent);

                }else {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    JsonObject result = jsonArray.get(0).getAsJsonObject();
                    Snackbar.make(findViewById(android.R.id.content),result.get("msg").getAsString(),Snackbar.LENGTH_LONG).show();
                }

                dialogManager.stopProcessDialog();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Response: request 1", call.request().toString());
                Log.d("Response: onFailure 1", call.toString());
                dialogManager.stopProcessDialog();
            }
        });
    }
}
