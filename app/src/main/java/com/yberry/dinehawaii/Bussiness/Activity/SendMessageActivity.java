package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imag, imgCross;
    CustomEditText sendMsg;
    String reserv_id;
    private static final String TAG = "SendMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        RelativeLayout mainView = (RelativeLayout) findViewById(R.id.activity_resarvation_detail);
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
        reserv_id= getIntent().getExtras().getString("reservation_id");
        Log.e("reserv_id",reserv_id);
    }

    private void init() {
        imag = (ImageView) findViewById(R.id.ivSend);
        imgCross = (ImageView) findViewById(R.id.ivcross);
        sendMsg = (CustomEditText) findViewById(R.id.typemessage);
        imag.setOnClickListener(SendMessageActivity.this);
        imgCross.setOnClickListener(SendMessageActivity.this);
    }

    private void sendMessage() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.SENDMESSAGE);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(SendMessageActivity.this));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(SendMessageActivity.this));
        jsonObject.addProperty("reserv_id", reserv_id);
        jsonObject.addProperty("message", sendMsg.getText().toString());
        dataSend(jsonObject);
        Log.e(TAG, "Request SEND MESSAGE >> "+jsonObject.toString());
    }

    private void dataSend(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(SendMessageActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response SEND MESSAGE >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray resultArray = jsonObject.getJSONArray("result");
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        finish();
                        Toast.makeText(getApplicationContext(), "Sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Failed, try again", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivSend) {
            if(TextUtils.isEmpty(sendMsg.getText().toString())){
                sendMsg.setError("Please Enter Message.");
            }
            else
                sendMessage();
        }
        if (v.getId() == R.id.ivcross) {
            onBackPressed();
        }
    }
}
