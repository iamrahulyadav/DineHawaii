package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaypalApprovedActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private CustomTextView tvPaypal;
    private static final String TAG = "PaypalActivity_21D";
    JSONObject object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal__approved_);
        init();
        try {
            object = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void init(){
        mContext = this;
        tvPaypal = (CustomTextView)findViewById(R.id.paypalReg);
        tvPaypal.setOnClickListener(this);
        setToolbar();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Payment Done");
        ImageView back = (ImageView) findViewById(R.id.back);
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

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.paypalReg){

            try {
                object.put("PayPal_TXN_ID","12345");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Util.isNetworkAvailable(mContext)) {
                try {
                    object.put("method", AppConstants.REGISTRATION.BUSINESSUSERREGISTRATION);
                    object.put("user_id", AppPreferencesBuss.getUserId(PaypalApprovedActivity.this));
                    object.put("business_id", AppPreferencesBuss.getBussiId(PaypalApprovedActivity.this));
                    object.put("pacakges", AppPreferencesBuss.getBussiPackagelist(PaypalApprovedActivity.this));
                    object.put("options", AppPreferencesBuss.getBusinessOptionList(PaypalApprovedActivity.this));
                    object.put("total_amount", AppPreferencesBuss.getPackageAmount(PaypalApprovedActivity.this));
                    object.put("terms_condition", "1");
                    object.put("PayPal_TXN_ID", AppPreferencesBuss.getBusinessTranctionId(PaypalApprovedActivity.this));
                    object.put("PayPal_TXN_STATUS", AppPreferencesBuss.getBusinessTranctionStatus(PaypalApprovedActivity.this));
                    Log.v(TAG, "checkdata420 :-" + object);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(object.toString()).getAsJsonObject();

                Log.e(TAG,jsonObject.toString());
                JsonCallMethod(jsonObject);
            } else {
                Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

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
                Log.e(TAG,response.body().toString());
                String s=response.body().toString();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if(jsonObject.getString("status").equalsIgnoreCase("200")) {

                        AppPreferencesBuss.setBussiPackagelist(PaypalApprovedActivity.this,object.getString("pacakges"));
                        AppPreferencesBuss.setBussiOptionlist(PaypalApprovedActivity.this,object.getString("options"));
                        AppPreferencesBuss.setUsertypeid(PaypalApprovedActivity.this, "2");
                        Intent intent=new Intent(PaypalApprovedActivity.this,BusinessNaviDrawer.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }else {
                        String msg=jsonObject.getString("message");
                        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e(TAG+"error",t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });
    }



}