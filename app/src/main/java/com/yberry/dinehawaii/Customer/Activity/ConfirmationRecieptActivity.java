package com.yberry.dinehawaii.Customer.Activity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.ReservationDataModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationRecieptActivity extends AppCompatActivity {
    ImageView back;
    String business_id;
    String reservation_id;
    String reservation_amount;
    String payment_type;
    String transaction_id;
    String payment_status;
    String min_hours;
    String time;
    String nofity_checkbox;
    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_reciept);
        buttonNext= (Button)findViewById(R.id.btn_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setToolbar();

        Intent in= getIntent();
        reservation_id=in.getStringExtra("reservation_id");
        business_id=in.getStringExtra("business_id");
        reservation_amount=in.getStringExtra("reservation_amount");
        payment_type=in.getStringExtra("payment_type");
        transaction_id=in.getStringExtra("transaction_id");
        payment_status=in.getStringExtra("payment_status");
        min_hours=in.getStringExtra("min_hours");
        time=in.getStringExtra("time");
        nofity_checkbox=in.getStringExtra("nofity_checkbox");

        confirmReservation();





    }
    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Reservation Confirmation");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    void confirmReservation() {

        if (Util.isNetworkAvailable(ConfirmationRecieptActivity.this))
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_RESERVATION_CONFIRMATION);
            jsonObject.addProperty("business_id", business_id);
            jsonObject.addProperty("reservation_id", reservation_id);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(ConfirmationRecieptActivity.this));//testing demo code
            jsonObject.addProperty("reservation_amount", reservation_amount);
            jsonObject.addProperty("paymentType", payment_type);
            jsonObject.addProperty("transaction_id", transaction_id);
            jsonObject.addProperty("payment_status", payment_status);
            jsonObject.addProperty("min_hours", min_hours);
            jsonObject.addProperty("time", time);
            jsonObject.addProperty("nofity_checkbox", nofity_checkbox);
            Log.e("Save Confirmation", "Package request :- " + jsonObject.toString());
            saveDataToServer(jsonObject);
        } else {
            Toast.makeText(this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void saveDataToServer(JsonObject jsonObject)
    {
        final ProgressHUD progressHD = ProgressHUD.show(ConfirmationRecieptActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_reservation_confirmation(jsonObject);

        call.enqueue(new Callback<JsonObject>()
        {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response)
            {
                Log.e("Save Confirmation", "Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                try
                {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200"))
                    {
                        jsonObject.getString("message");
                    }
                    else if (jsonObject.getString("status").equalsIgnoreCase("400"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1= jsonArray.getJSONObject(0);
                        String msg= jsonObject1.getString("msg");
                        Toast.makeText(ConfirmationRecieptActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t)
            {
                Log.e("Save Confirmation", "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(ConfirmationRecieptActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });




    }



}
