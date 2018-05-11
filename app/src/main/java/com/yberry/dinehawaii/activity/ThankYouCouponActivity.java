package com.yberry.dinehawaii.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThankYouCouponActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    Button done;
    public static final String TAG="ThankYouCouponActivity";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_coupon);
        setToolbar();
        getJsonDate();
       init();
    }

    private void init() {
        ((Button) findViewById(R.id.done)).setOnClickListener(this);
    }


    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
       getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((CustomTextView) findViewById(R.id.headet_text)).setText("Thank You");
        back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    private void getJsonDate() {
        JsonObject object =new JsonObject();
        object.addProperty("method", AppConstants.CUSTOMER_USER.GET_PAYMENT_DETAILS);
        object.addProperty("business_id", AppPreferences.getCustomerBusinessId(ThankYouCouponActivity.this));
        object.addProperty("user_id", AppPreferences.getCustomerid(ThankYouCouponActivity.this));
        object.addProperty("coupon_id", AppPreferences.getCustomerGiftId(ThankYouCouponActivity.this));//, AppPreferencesBuss.getBussiId(getActivity()));
        object.addProperty("txn_id", AppPreferences.getTranctionId(ThankYouCouponActivity.this));
        object.addProperty("item_name", AppPreferences.getCustomerCoupon(ThankYouCouponActivity.this) );
        object.addProperty("amount", AppPreferences.getCouponAmount(ThankYouCouponActivity.this));
        object.addProperty("currency_code", "91");
        object.addProperty("payment_status", AppPreferences.getPaymentApproved(ThankYouCouponActivity.this));
        ShowJsonData(object);
        Log.v(TAG, "check parameter :-" + object);


    }


    private void ShowJsonData(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(ThankYouCouponActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.send_gift(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
 //                Log.e(TAG + "onDeliverService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Toast.makeText(ThankYouCouponActivity.this, "Transaction has been completed", Toast.LENGTH_SHORT).show();



                        }



                    }

                    else if (jsonObject.getString("status").equalsIgnoreCase("400"))
                    {
                        Log.e("status", jsonObject.getString("status"));
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1= jsonArray.getJSONObject(0);
                        String msg= jsonObject1.getString("msg");
                        Log.e("msg", msg);
                        Toast.makeText(ThankYouCouponActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }progressHD.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
if(v.getId() == R.id.done){
    finish();
}
    }
}
