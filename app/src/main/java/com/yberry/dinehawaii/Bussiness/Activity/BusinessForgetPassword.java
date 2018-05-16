package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.Function.errorMessage;

public class BusinessForgetPassword extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BusinessForgetPassword";

    private CustomEditText mEmail_id;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_forget_password);
        setToolbar();
        init();
    }

    private void init() {
        mEmail_id = (CustomEditText) findViewById(R.id.mEmail_id);
        //  helpDialog = (ImageView) findViewById(R.id.help);
        ((CustomButton) findViewById(R.id.btn_password)).setOnClickListener(this);
       /* helpDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Function.bottomToolTipDialogBox(null, BusinessForgetPassword.this, "This package is already selected by you !!!" *//*+ "\n Package Details : " + datalist.getPackage_detail()*//* , helpDialog, null);

            }
        });*/
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

        final ProgressHUD progressHD = ProgressHUD.show(BusinessForgetPassword.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUrl(jsonObject);

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
                            Toast.makeText(BusinessForgetPassword.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);

                        }


                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            String msg = object1.getString("msg");
                            Toast.makeText(BusinessForgetPassword.this, msg, Toast.LENGTH_SHORT).show();
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
                Log.e(TAG, "error" + t.getMessage());
                Toast.makeText(BusinessForgetPassword.this, "Server Not Responding.", Toast.LENGTH_SHORT).show();
//                    t.getMessage();
            }
        });
    }

    private void callPassword() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.BUSINESSFORGETPASSWORD);
        jsonObject.addProperty("email_id", mEmail_id.getText().toString());
        jsonObject.addProperty("user_type", "2");
        Log.e(TAG, "forget json" + jsonObject.toString());
        changePassword(jsonObject);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_password) {
            String mpassword = mEmail_id.getText().toString();
            if (mpassword.isEmpty()) {
                mEmail_id.setError("Enter user-id");
            } else if (Function.isEmailNotValid(mEmail_id)) {
                mEmail_id.setError(errorMessage);
            } else {
                callPassword();
            }


        }


    }


}

