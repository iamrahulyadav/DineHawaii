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
import com.yberry.dinehawaii.Model.TableData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    String partySizeS, userNameString, mobileNoString, emailString;
    String name, i;
    ArrayList<TableData> tableDataList;
    private String combinetable = "0";
    private BusReservationActivity context;
    private Calendar c;
    private String currTime;
    private SimpleDateFormat dateFormat;
    private String currDate;
    private CustomTextView btn_next;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_reservation);
        context = this;
        setToolbar();
        initViews();

        if (getIntent().hasExtra("table_id")) {
            selectedTableID = getIntent().getStringExtra("table_id");
            Log.e(TAG, "onCreate: selectedTableID >> " + selectedTableID);
        }

        if (getIntent().hasExtra("party_size")) {
            partySizeS = getIntent().getStringExtra("party_size");
            Log.e(TAG, "onCreate: partySizeS >> " + partySizeS);
        }
        if (getIntent().hasExtra("table_name")) {
            String table_name = getIntent().getStringExtra("table_name");
            Log.e(TAG, "onCreate: table_name >> " + table_name);
            businessName.setText("Table : " + table_name + " (Cap. " + partySizeS + ") ");
        }

        c = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currDate = dateFormat.format(c.getTime());
        dateFormat = new SimpleDateFormat("hh:mm a");
        currTime = dateFormat.format(c.getTime());
        Log.e(TAG, "onCreate: currDate >> " + currDate);
        Log.e(TAG, "onCreate: currTime >> " + currTime);
        btn_next.setText("Submit customer details to book this table at " + currDate + " | " + currTime);
    }

    private void initViews() {
        tableDataList = new ArrayList<TableData>();
        complete = (CustomButton) findViewById(R.id.complete);
        submit = (CustomButton) findViewById(R.id.submit1);
        businessName = (CustomTextView) findViewById(R.id.businessName);
        btn_next = (CustomTextView) findViewById(R.id.btn_next);
        userName = (CustomEditText) findViewById(R.id.userName);
        mobileNo = (CustomEditText) findViewById(R.id.textViewMobile);
        emailId = (CustomEditText) findViewById(R.id.textViewEmail);

        //keyboard dismiss on outside touch
        ((RelativeLayout) findViewById(R.id.touch_outside)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(context);
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
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
        else if (Function.isEmailNotValid(emailId))
            emailId.setError("Enter valid email-id");
        else if (selectedTableID.equalsIgnoreCase(""))
            Toast.makeText(this, "Select table", Toast.LENGTH_SHORT).show();
        else {
            userNameString = userName.getText().toString().trim();
            mobileNoString = mobileNo.getText().toString().trim();
            emailString = emailId.getText().toString().trim();
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            currDate = dateFormat.format(c.getTime());
            dateFormat = new SimpleDateFormat("hh:mm a");
            currTime = dateFormat.format(c.getTime());
            Log.e(TAG, "makeReservation: currDate >> " + currDate);
            Log.e(TAG, "makeReservation: currTime >> " + currTime);
            submitRequest();
        }
    }

    private void submitRequest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.MAKE_BUSINESS_RESERVATION);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
        jsonObject.addProperty("date", currDate);
        jsonObject.addProperty("time", currTime);  //time
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
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                            AppPreferencesBuss.setReservatId(context, reservation_id);
                            showThankYouAlert();
                           /* if (pre_amonut.equalsIgnoreCase("0") || isWaitList) {
                                showThankYouAlert();
                            } else {
                                *//*Intent in = new Intent(context, CustomerConfirmreservationActivity.class);
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Thank You!");
        builder.setCancelable(false);
        builder.setMessage("Your reservation has done successfully.");
        ImageView img = new ImageView(context);
        img.setImageResource(R.drawable.thanks);
        builder.setView(img);
        builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
