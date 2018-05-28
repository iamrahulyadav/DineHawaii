package com.yberry.dinehawaii.Customer.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.User;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.DialogManager;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.GPSTracker;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yberry.dinehawaii.Util.Function.fieldRequired;


public class GuestCustRegisterActivity2 extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{

    private static final String TAG = "GuestCustRegister2";
    private ImageView back;
    private RadioGroup mSelectPasscode;
    private CustomEditText mPasscode,mPassword, mConfirmPass;
    //private Spinner mSelectLanguage;
    private CheckBox mTermCondition;
    private CustomButton btnSubmit;
    private int userId;
    GPSTracker gpsTracker;
    private double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guest_or_custumer_reg);
        RelativeLayout mainView = (RelativeLayout) findViewById(R.id.mainView);
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

        userId = getIntent().getIntExtra("user_id",0);
        gpsTracker = new GPSTracker(GuestCustRegisterActivity2.this);

        if(gpsTracker.canGetLocation()){

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.v(TAG, " Latitude :- " + latitude  + " & Longitude:- " +  longitude);

            // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \n Lat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
//           Toast.makeText(getApplicationContext(), "GPS not enabled", Toast.LENGTH_LONG).show();

            gpsTracker.showSettingsAlert();

        }


        setToolbar();
        init();
        listener();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Customer Registration");

    }

    private void init() {
        back = (ImageView) findViewById(R.id.back);
        mSelectPasscode = (RadioGroup) findViewById(R.id.mSelectPasscode);
        this.btnSubmit = (CustomButton) findViewById(R.id.btnSubmit);
       // this.mSelectLanguage = (Spinner) findViewById(R.id.mSelectLanguage);
        this.mPasscode = (CustomEditText) findViewById(R.id.mPasscode);
        this.mPassword = (CustomEditText) findViewById(R.id.mPassword);
        this.mConfirmPass = (CustomEditText) findViewById(R.id.mConfirmPass);
        this.mTermCondition = (CheckBox) findViewById(R.id.mTermCondition);

        /*mSelectLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }
    private void listener(){

        String[] language = getResources().getStringArray(R.array.language);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GuestCustRegisterActivity2.this,R.layout.row_spn,language);
       // arrayAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        //this.mSelectLanguage.setAdapter(arrayAdapter);
        back.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        mSelectPasscode.setOnCheckedChangeListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
        }else if (v.getId() == R.id.btnSubmit) {
            /*Intent intent=new Intent(getApplicationContext(),GuestCustomerLoginActivity.class);
            startActivity(intent);
            finish();*/
            if(TextUtils.isEmpty(mPasscode.getText().toString())){
                mPasscode.setError(fieldRequired);
            }/*else if(mSelectLanguage.getSelectedItem().toString().equalsIgnoreCase("Select Language")){
                Snackbar.make(findViewById(android.R.id.content),"Select your language",Snackbar.LENGTH_LONG).show();
            }*/else if(TextUtils.isEmpty(mPassword.getText().toString())){
                mPassword.setError(fieldRequired);
            }
            else if(TextUtils.isEmpty(mConfirmPass.getText().toString())){
                mConfirmPass.setError(fieldRequired);
            }
            else if(!Function.confirmPassword(mPassword.getText().toString(),mConfirmPass.getText().toString())){
                mConfirmPass.setError("Password does match");
            }
            else if(!mTermCondition.isChecked()){
                Snackbar.make(findViewById(android.R.id.content),"Accept Terms & Conditions",Snackbar.LENGTH_LONG).show();
            }else {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(AppConstants.KEY_METHOD,AppConstants.REGISTRATION.FINALSIGNUP);
                jsonObject.addProperty(AppConstants.KEY_USERID, userId);
                jsonObject.addProperty(AppConstants.KEY_OTP, mPasscode.getText().toString());
                //jsonObject.addProperty(AppConstants.KEY_LANGUAGE, mSelectLanguage.getSelectedItem().toString());
                jsonObject.addProperty(AppConstants.KEY_LANGUAGE, "English");
                jsonObject.addProperty(AppConstants.KEY_PASSWORD, mPassword.getText().toString());
                jsonObject.addProperty(AppConstants.KEY_LATITUDE, latitude);
                jsonObject.addProperty(AppConstants.KEY_LONGITUDE, longitude);
                jsonObject.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
                finalRegistrationTask(jsonObject);
                Log.v(TAG, "Final Registration Request :-" + jsonObject);
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD,AppConstants.REGISTRATION.SENDOTP);
        jsonObject.addProperty(AppConstants.KEY_USERID, userId);
        if(checkedId == R.id.mSmartph){
            jsonObject.addProperty(AppConstants.KEY_OTPTYPE, "mobile");
        }else{
            jsonObject.addProperty(AppConstants.KEY_OTPTYPE, "email");
        }

        sendOtpTask(jsonObject);
    }

    private void sendOtpTask(JsonObject jsonObject) {
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
                Log.d("Response: request 1", call.request().toString());
                Log.d("Response: Registraion 1", response.body().toString());
                Log.v(TAG, "Final Registration Response :-" + response.toString());
                JsonObject jsonObject = response.body();

                if(jsonObject.get("status").getAsString().equals("200")){
                    JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    JsonObject result = jsonArray.get(0).getAsJsonObject();
                    userId = Integer.parseInt(result.get("user_id").getAsString());

                    mPasscode.setText(result.get("otp").getAsString());


                }else {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    JsonObject result = jsonArray.get(0).getAsJsonObject();

                    Snackbar.make(findViewById(android.R.id.content),result.get("msg").getAsString(),Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Response: request 1", call.request().toString());
                Log.d("Response: onFailure 1", call.toString());
            }
        });

    }

    private void finalRegistrationTask(JsonObject jsonObject) {
        final DialogManager dialogManager = new DialogManager();
        dialogManager.showProcessDialog(GuestCustRegisterActivity2.this,"Please Wait...");
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
                Log.d("Response: request 1", call.request().toString());
                Log.d("Response: Registraion 1", response.body().toString());

                JsonObject jsonObject = response.body();

                if(jsonObject.get("status").getAsString().equals("200")){
                    JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    JsonObject result = jsonArray.get(0).getAsJsonObject();
                    User user = new Gson().fromJson(result,User.class);
                    Log.d("Response: user_id ", user.toString());

                    Toast.makeText(GuestCustRegisterActivity2.this, "You have registered, please login", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),GuestCustomerLoginActivity.class);
                    intent.putExtra("data", user);
                    startActivity(intent);
                    finish();

                } else if(jsonObject.get("status").equals("400")) {

                    JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    JsonObject result = jsonArray.get(0).getAsJsonObject();
                    Log.v(TAG, "Else in register guest_icon final" +result.get("msg").getAsString());
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
