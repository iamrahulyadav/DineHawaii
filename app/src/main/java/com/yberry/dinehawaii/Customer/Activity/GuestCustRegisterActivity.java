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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.User;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.DialogManager;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.cpp.CountryCodePicker;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yberry.dinehawaii.Util.Function.errorMessage;
import static com.yberry.dinehawaii.Util.Function.fieldRequired;

public class GuestCustRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GuestCustRegister";
    String promoText = "";
    private Context mcContext;
    private CustomEditText mFName;
    private CustomEditText mLName;
    private CustomEditText mEmailId;
    private CustomEditText mMobile;
    private CustomEditText mAddress;
    private CustomEditText mCity;
    private CustomEditText mState, promoCode;
    private CustomButton btnContinue;
    private Spinner mCountryCode;
    private CountryCodePicker ccp;
    private ImageView id_physical, smart, txt_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activirty);

        mcContext = this;
        LinearLayout mainView = (LinearLayout) findViewById(R.id.mainView);
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
        initCompnet();
        listener();
        setToolbar();
    }

    private void initCompnet() {
        btnContinue = (CustomButton) findViewById(R.id.btnContinue);
        mState = (CustomEditText) findViewById(R.id.mState);
        mCity = (CustomEditText) findViewById(R.id.mCity);
        mAddress = (CustomEditText) findViewById(R.id.mAddress);
        mMobile = (CustomEditText) findViewById(R.id.mMobile);
        mEmailId = (CustomEditText) findViewById(R.id.mEmailId);
        mLName = (CustomEditText) findViewById(R.id.mLName);
        mFName = (CustomEditText) findViewById(R.id.mFName);
        promoCode = (CustomEditText) findViewById(R.id.promocode);
        id_physical = (ImageView) findViewById(R.id.id_physical);
        smart = (ImageView) findViewById(R.id.smart);
        txt_id = (ImageView) findViewById(R.id.txt_id);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);


        ccp.getSelectedCountryCode().toString();
        Log.d("country", ccp.getSelectedCountryCode().toString());

    }

    private void listener() {
        btnContinue.setOnClickListener(this);
        id_physical.setOnClickListener(this);
        smart.setOnClickListener(this);
        txt_id.setOnClickListener(this);

       /* String[] countryCode = getResources().getStringArray(R.array.countryCode);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GuestCustRegisterActivity.this,android.R.layout.simple_spinner_item,countryCode);
        //this.mCountryCode.setAdapter(arrayAdapter);
*/

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnContinue) {
            if (TextUtils.isEmpty(mFName.getText().toString())) {
                mFName.setError(fieldRequired);
            } else if (TextUtils.isEmpty(mLName.getText().toString())) {
                mLName.setError(fieldRequired);
            } else if (Function.isEmailNotValid(mEmailId)) {
                mEmailId.setError(errorMessage);
            }/*else if(mCountryCode.getSelectedItem().toString().equals("+0")){
                    Snackbar.make(findViewById(android.R.id.content),"Select your country code",Snackbar.LENGTH_LONG).show();
                }*/ /*else if (Function.isPhoneNumberNotValid(mMobile)) {
                mMobile.setError(errorMessage);*/ else if (TextUtils.isEmpty(mAddress.getText().toString())) {
                mAddress.setError(fieldRequired);
            } else if (TextUtils.isEmpty(mCity.getText().toString())) {
                mCity.setError(fieldRequired);
            } else if (TextUtils.isEmpty(mState.getText().toString())) {
                mState.setError(fieldRequired);
            } else {
                if (!TextUtils.isEmpty(promoCode.getText().toString())) {
                    promoText = promoCode.getText().toString();
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.REGISTRATION.USER_SIGNUP);
                jsonObject.addProperty(AppConstants.KEY_FNAME, mFName.getText().toString());
                jsonObject.addProperty(AppConstants.KEY_LNAME, mLName.getText().toString());
                jsonObject.addProperty(AppConstants.KEY_EMAILID, mEmailId.getText().toString());
                //       jsonObject.addProperty(AppConstants.KEY_COUNTRYCODE, mCountryCode.getSelectedItem().toString());
                jsonObject.addProperty(AppConstants.KEY_MOBILE, mMobile.getText().toString());
                jsonObject.addProperty(AppConstants.KEY_ADDRESS, mAddress.getText().toString());
                jsonObject.addProperty(AppConstants.KEY_CITY, mCity.getText().toString());
                jsonObject.addProperty(AppConstants.KEY_STATE, mState.getText().toString());
                jsonObject.addProperty("promo_code", promoText);
                registrationTask(jsonObject);
                Log.v(TAG, "Json Registration object Request :-" + jsonObject);
            }

        } else if (v.getId() == R.id.id_physical) {
            //Function.bottomToolTipDialogBox(null, GuestCustRegisterActivity.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/, id_physical, null);
        } else if (v.getId() == R.id.smart) {
            // Function.bottomToolTipDialogBox(null, GuestCustRegisterActivity.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/, smart, null);
        } else if (v.getId() == R.id.txt_id) {
            // Function.bottomToolTipDialogBox(null, GuestCustRegisterActivity.this, "This package is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/, txt_id, null);

        }


    }


    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Customer Registration");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void registrationTask(JsonObject jsonObject) {

        final DialogManager dialogManager = new DialogManager();
        dialogManager.showProcessDialog(GuestCustRegisterActivity.this, "Please Wait...");

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
                Log.v(TAG, "Json Registration object Response :-" + response);
                // Log.d("Response: Registraion 1", response.body().toString());
                JsonObject jsonObject = response.body();

                if (jsonObject.get("status").getAsString().equals("200")) {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    JsonObject result = jsonArray.get(0).getAsJsonObject();

                    User user1 = new Gson().fromJson(result, User.class);
                    Log.d("Response: user_id ", user1.toString());

                    Intent intent = new Intent(mcContext, GuestCustRegisterActivity2.class);
                    intent.putExtra("user_id", user1.getId());
                    startActivity(intent);
                    finish();
                } else {
                    JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                    JsonObject result = jsonArray.get(0).getAsJsonObject();

                    Log.v(TAG, "Else in register guest" + result.get("msg").getAsString());

                    Snackbar.make(findViewById(android.R.id.content), result.get("msg").getAsString(), Snackbar.LENGTH_LONG).show();

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
