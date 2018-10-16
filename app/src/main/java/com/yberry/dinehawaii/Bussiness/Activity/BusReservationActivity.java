package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Activity.CustomerNaviDrawer;
import com.yberry.dinehawaii.Model.TableData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusReservationActivity extends AppCompatActivity {
    private static final String TAG = "BusReservationActivity";
    CustomButton complete, submit;
    String business_id, business_name, reservation_id, date, time, pre_amonut;
    CustomEditText userName, mobileNo, emailId;
    CustomTextView businessName;
    String child_booster = "0", child_high = "0";
    ImageView back;
    String selectedTableID = "";
    String partySizeS,  userNameString, mobileNoString, emailString;
    String name, i;
    ArrayList<TableData> tableDataList;
    private String combinetable = "";

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_reservation);
        setToolbar();
        initViews();

        if (getIntent().hasExtra("business_id")) {
            business_id = getIntent().getStringExtra("business_id");
            Log.v(TAG, "Business Id :- " + business_id);
        }

        if (getIntent().hasExtra("business_name")) {
            business_name = getIntent().getStringExtra("business_name");
            businessName.setText(business_name);
            Log.v(TAG, "Business Name :- " + business_name);
        }

        Log.e(TAG, "onCreate: business_id >> " + business_id);
        Log.e(TAG, "onCreate: business_name >> " + business_name);
        getFoodPrepTime();


        userName.setText(AppPreferences.getCustomername(BusReservationActivity.this));
        mobileNo.setText(AppPreferences.getCustomerMobile(BusReservationActivity.this));
        emailId.setText(AppPreferences.getEmailSetting(BusReservationActivity.this));
    }

    private void getFoodPrepTime() {
        if (Util.isNetworkAvailable(BusReservationActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.FOOD_PREP_TIME);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(BusReservationActivity.this));
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(BusReservationActivity.this));
            Log.e(TAG, "get food prep time" + jsonObject.toString());
            getFoodTimeApi(jsonObject);
        } else {
            Toast.makeText(BusReservationActivity.this, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getFoodTimeApi(JsonObject jsonObject) {
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String s = response.body().toString();
                Log.e(TAG, "get food prep time resp" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = resultJsonArray.getJSONObject(0);

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error" + t.getMessage());
            }
        });
    }


    private void initViews() {
        tableDataList = new ArrayList<TableData>();
        complete = (CustomButton) findViewById(R.id.complete);
        submit = (CustomButton) findViewById(R.id.submit1);
        businessName = (CustomTextView) findViewById(R.id.businessName);
        userName = (CustomEditText) findViewById(R.id.userName);
        mobileNo = (CustomEditText) findViewById(R.id.textViewMobile);
        emailId = (CustomEditText) findViewById(R.id.textViewEmail);

        //keyboard dismiss on outside touch
        ((RelativeLayout) findViewById(R.id.touch_outside)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(BusReservationActivity.this);
                return false;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeReservation();
            }
        });
    }

    private void showAlertDialog(String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BusReservationActivity.this);
        builder.setTitle("Reservation Time");
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    private void makeReservation() {

        if (userName.getText().toString().equalsIgnoreCase(""))
            userName.setError("Enter your full name");
        else if (mobileNo.getText().toString().equalsIgnoreCase(""))
            mobileNo.setError("Enter your mobile no.");
        else if (emailId.getText().toString().equalsIgnoreCase(""))
            emailId.setError("Enter your email-id");
        else if (selectedTableID.equalsIgnoreCase(""))
            Toast.makeText(this, "Select table", Toast.LENGTH_SHORT).show();
        else {
            userNameString = userName.getText().toString().trim();
            mobileNoString = mobileNo.getText().toString().trim();
            emailString = emailId.getText().toString().trim();
            submitRequest();
        }
    }

    private void submitRequest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.MAKE_BUSINESS_RESERVATION);
        jsonObject.addProperty("business_id", business_id);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(BusReservationActivity.this));
        jsonObject.addProperty("date", date);
        jsonObject.addProperty("time", time);  //time
        jsonObject.addProperty("party_size", partySizeS);
        jsonObject.addProperty("child_high_chair", child_high);
        jsonObject.addProperty("child_booster_chair", child_booster);
        jsonObject.addProperty("name", userNameString);
        jsonObject.addProperty("email", emailString);
        jsonObject.addProperty("mobile", mobileNoString);
        jsonObject.addProperty("no_of_adults", "0");
        jsonObject.addProperty("no_of_childers", "0");
        jsonObject.addProperty("table_id", selectedTableID);
        jsonObject.addProperty("combine_table", combinetable);//
        Log.e(TAG, "Request MAKING RESERVATION >> " + jsonObject.toString());
        make_business_reservation(jsonObject);
    }

    private void make_business_reservation(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(BusReservationActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response MAKING RESERVATION >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        reservation_id = "";
                        date = "";
                        time = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            reservation_id = jsonObject1.getString("reservation_id");
                            date = jsonObject1.getString("date");
                            time = jsonObject1.getString("time");
                            pre_amonut = jsonObject1.getString("reservation_pre_amout");
                            Log.e(TAG, "onResponse: reservation_id >> " + reservation_id);
                            Log.e(TAG, "onResponse: reservation_pre_amout >> " + pre_amonut);
                            AppPreferencesBuss.setReservatId(BusReservationActivity.this, reservation_id);
                            showThankYouAlert();
                           /* if (pre_amonut.equalsIgnoreCase("0") || isWaitList) {
                                showThankYouAlert();
                            } else {
                                *//*Intent in = new Intent(BusReservationActivity.this, CustomerConfirmreservationActivity.class);
                                in.putExtra("reservation_id", reservation_id);
                                in.putExtra("name", userNameString);
                                in.putExtra("time", timePicker.getText().toString());
                                in.putExtra("partysize", partySizeS);
                                in.putExtra("date", date);
                                in.putExtra("phone", mobileNoString);
                                in.putExtra("tablename", selected_table);
                                in.putExtra("business_id", business_id);
                                in.putExtra("reserve_amt", pre_amonut);
                                startActivity(in);
                                finish();*//*
                            }*/
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (progressHD != null && progressHD.isShowing())
                    progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                if (progressHD != null && progressHD.isShowing())
                    progressHD.dismiss();
            }
        });
    }

    private void showThankYouAlert() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BusReservationActivity.this);
        builder.setTitle("Thank You!");
        builder.setCancelable(false);
        builder.setMessage("Your reservation has done successfully.");
        ImageView img = new ImageView(BusReservationActivity.this);
        img.setImageResource(R.drawable.thanks);
        builder.setView(img);
        builder.setPositiveButton("GO TO HOME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), CustomerNaviDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Making Reservation");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: requestCode >> " + requestCode + ", resultCode >> " + resultCode);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            business_id = data.getStringExtra("busi_id");
            selectedTableID = data.getStringExtra("table_id");
            pre_amonut = data.getStringExtra("reserve_amt").replaceAll("\\$", "").replaceAll("\\s", "");
            Log.e(TAG, "onActivityResult: business_id >> " + business_id);
            Log.e(TAG, "onActivityResult: selectedTableID >> " + selectedTableID);
            Log.e(TAG, "onActivityResult: pre_amonut >> " + pre_amonut);
            AppPreferences.setBusiID(this, business_id);
            submit.performClick();
        }
    }
}
