package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerForgotPassword extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "TAG";
    private CustomEditText mEmail_id;
    private ImageView back;/*helpDialog*/
    private Button btn_password;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_forgot_password);
        setToolbar();
        init();
    }

    private void init() {
        mEmail_id = (CustomEditText) findViewById(R.id.mEmail_id);
        btn_password = (Button) findViewById(R.id.btn_password);
        btn_password.setOnClickListener(this);

    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((CustomTextView) findViewById(R.id.headet_text)).setText("Forget Password");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void changePassword(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(CustomerForgotPassword.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.request(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Log.v(TAG, "Inside response 200 ");
                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject object = resultJsonArray.getJSONObject(i);
                            Toast.makeText(CustomerForgotPassword.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();

//


                                }
                            }, 2000);

                        }


                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            String msg = object1.getString("msg");
                            Toast.makeText(CustomerForgotPassword.this, msg, Toast.LENGTH_SHORT).show();

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressHD.dismiss();
                Toast.makeText(CustomerForgotPassword.this, "Server Not Responding.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "error" + t.getMessage());
//                    t.getMessage();
            }
        });
    }

    private void callPassword() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.NORMALFORGETPASSWORD);
        jsonObject.addProperty("email_id", mEmail_id.getText().toString());
        jsonObject.addProperty("user_type", 3);
        changePassword(jsonObject);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_password) {
            checkData();
        }


    }

    private void checkData() {
        if (TextUtils.isEmpty(mEmail_id.getText().toString())) {
            mEmail_id.setError("Enter email-id");
        } else {
            if (Util.isNetworkAvailable(CustomerForgotPassword.this)) {
                callPassword();
            }else{
                Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
