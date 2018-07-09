package com.yberry.dinehawaii.Customer.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yberry.dinehawaii.Model.ReservationDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerResDetailActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "CustomerReserv";
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 100;
    RelativeLayout statusLayout, histLayout;
    View view1, view2;
    FloatingActionButton callRestbtn;
    private CustomTextView tvReservationID, tvDateTime, tvBookingDate, tvBookingTime, tvCustomerName, tvTableNo, tvPartySize,
            tvContactNo, tvEmail, tvBusName, btnUpdate, btnConfirm, btnCancel, btnFeedback, btnReview;
    private CustomTextView tvCheckin, tvWaitTime, tvTableReady, tvSeatedBy, tvReschedule, tvConfirmed, tvDeposit, tvClose;
    private String reservation_id = "";
    private String reservation_status = "";
    private String buss_id = "";
    private CustomEditText inputReschedule;
    private String new_date, new_time;
    private ReservationDetails model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_res_detail);
        setToolbar();
        init();
        initControls();
        if (getIntent().getAction().equalsIgnoreCase("current_reserv")) {
            statusLayout.setVisibility(View.VISIBLE);
        }

        if (getIntent().getAction().equalsIgnoreCase("history_reserv")) {
            statusLayout.setVisibility(View.GONE);
            callRestbtn.setVisibility(View.GONE);
            histLayout.setVisibility(View.VISIBLE);
        }
        if (getIntent().hasExtra("reserv_id"))
            reservation_id = getIntent().getStringExtra("reserv_id");
        if (getIntent().hasExtra("buss_id"))
            buss_id = getIntent().getStringExtra("buss_id");
        if (getIntent().hasExtra("edit_status")) {
            if (getIntent().getStringExtra("edit_status").equalsIgnoreCase("0")) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                btnConfirm.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.GONE);
            }
        }
        if (getIntent().hasExtra("confirm_status")) {
            if (getIntent().getStringExtra("confirm_status").equalsIgnoreCase("111")) {
                btnConfirm.setText("Confirmed");
                btnConfirm.setEnabled(false);
            }

        }
        if (getIntent().hasExtra("cancel_status")) {
            if (getIntent().getStringExtra("cancel_status").equalsIgnoreCase("1")) {
                btnCancel.setVisibility(View.GONE);
            }
        }
        getResrvData();
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                  /*  Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "123456"));
                    startActivity(intent2);*/
                }
                return;
        }
    }

    private void init() {
        tvBusName = (CustomTextView) findViewById(R.id.tvBusName);
        tvReservationID = (CustomTextView) findViewById(R.id.tvReservationID);
        tvDateTime = (CustomTextView) findViewById(R.id.tvDateTime);
        tvBookingDate = (CustomTextView) findViewById(R.id.tvDateTime2);
        tvBookingTime = (CustomTextView) findViewById(R.id.tvBookingTime);
        tvCustomerName = (CustomTextView) findViewById(R.id.tvCustomerName);
        tvTableNo = (CustomTextView) findViewById(R.id.tvTableNo);
        tvPartySize = (CustomTextView) findViewById(R.id.tvPartySize);
        tvContactNo = (CustomTextView) findViewById(R.id.tvContactNo);
        tvEmail = (CustomTextView) findViewById(R.id.tvEmail);
        btnUpdate = (CustomTextView) findViewById(R.id.tvupdate);
        btnConfirm = (CustomTextView) findViewById(R.id.tvconfirm);
        btnCancel = (CustomTextView) findViewById(R.id.tvcancel);
        btnFeedback = (CustomTextView) findViewById(R.id.tvfeedback);
        btnReview = (CustomTextView) findViewById(R.id.tvreview);
        statusLayout = (RelativeLayout) findViewById(R.id.footerRelative);
        histLayout = (RelativeLayout) findViewById(R.id.histRelative);
        view1 = (View) findViewById(R.id.view);
        view2 = (View) findViewById(R.id.view1);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnFeedback.setOnClickListener(this);
        btnReview.setOnClickListener(this);
    }

    private void initControls() {
        callRestbtn = (FloatingActionButton) findViewById(R.id.fab_call_rest);
        tvCheckin = (CustomTextView) findViewById(R.id.tvCheckin);
        tvWaitTime = (CustomTextView) findViewById(R.id.tvWaitTime);
        tvTableReady = (CustomTextView) findViewById(R.id.tvTableReady);
        tvSeatedBy = (CustomTextView) findViewById(R.id.tvSeatedBy);
        tvReschedule = (CustomTextView) findViewById(R.id.tvReschedule);
        tvConfirmed = (CustomTextView) findViewById(R.id.tvConfirmed);
        tvDeposit = (CustomTextView) findViewById(R.id.tvDeposit);
        tvClose = (CustomTextView) findViewById(R.id.tvClose);

        callRestbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvupdate:
                openRescheduleDialog();
                break;
            case R.id.tvconfirm:
                confirmReserData();
                break;
            case R.id.tvcancel:
                cancelReservData();
                break;
            case R.id.tvfeedback:
                Intent intent = new Intent(CustomerResDetailActivity.this, CustomerFeedbackActivity.class);
                intent.putExtra("Resv_id", reservation_id);
                intent.putExtra("Bussiness_id", buss_id);
                startActivity(intent);
                break;
            case R.id.tvreview:
                /*Intent intent1 = new Intent(CustomerResDetailActivity.this, CustomerReviewActivity.class);
                intent1.putExtra("Resv_id", reservation_id);
                intent1.putExtra("Bussiness_id", buss_id);
                startActivity(intent1);*/
                Intent intent1 = new Intent(CustomerResDetailActivity.this, RatingActivity.class);
                intent1.putExtra("business_id", buss_id);
                startActivity(intent1);
                break;
            case R.id.fab_call_rest:
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                   /* Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "123456"));
                    startActivity(intent2);*/
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
                }
                break;
            default:
                break;
        }
    }

    private void cancelReservData() {
        if (Util.isNetworkAvailable(CustomerResDetailActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.DELETE_RESERVATION);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerResDetailActivity.this));
            jsonObject.addProperty("reservation_id", reservation_id);
            Log.e(TAG, "Request Delete Reservation :- " + jsonObject.toString());
            cancelReservApi(jsonObject);
        } else {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelReservApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(CustomerResDetailActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.delete_reservation(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, " Delete Making Reservation :- " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Toast.makeText(CustomerResDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent("current_reserv");
                        LocalBroadcastManager.getInstance(CustomerResDetailActivity.this).sendBroadcast(intent);
                        finish();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(CustomerResDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Confirm Reservation error :- ", Log.getStackTraceString(t));
                Toast.makeText(CustomerResDetailActivity.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                progressHD.dismiss();
            }
        });
    }

    private void confirmReserData() {
        if (Util.isNetworkAvailable(CustomerResDetailActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GETRESERVATIONCONFIRM);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerResDetailActivity.this));
            jsonObject.addProperty("reservation_id", reservation_id);
            Log.e(TAG, "Confirm ReservationJson :- " + jsonObject.toString());
            confirmReservApi(jsonObject);
        } else {
            Toast.makeText(CustomerResDetailActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void confirmReservApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(CustomerResDetailActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.order_detail_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, " Confirm Reservation :- " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        btnConfirm.setText("Confirmed");
                        btnConfirm.setEnabled(false);
                        Toast.makeText(CustomerResDetailActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        Toast.makeText(CustomerResDetailActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(CustomerResDetailActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Reservation Details");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void openRescheduleDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reschedule");
        builder.setCancelable(false);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input_contact_no, (ViewGroup) findViewById(android.R.id.content), false);
        inputReschedule = (CustomEditText) viewInflated.findViewById(R.id.input);
        inputReschedule.setHint("Select New Date");
        builder.setView(viewInflated);
        inputReschedule.setFocusableInTouchMode(false);

        inputReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CustomerResDetailActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setCancelColor(getResources().getColor(R.color.colorPrimary));
                dpd.setOkColor(getResources().getColor(R.color.colorPrimary));
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis() - 1000);
                dpd.setMinDate(c);
            }
        });

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (inputReschedule.getText().toString().isEmpty())
                    Toast.makeText(CustomerResDetailActivity.this, "Select reschedule date & time", Toast.LENGTH_SHORT).show();
                else {
                    String datetime = inputReschedule.getText().toString();
                    String[] newstring = datetime.split(",");
                    updateReservData(newstring[0], newstring[1]);
                }
            }
        });
        builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void updateReservData(String date, String time) {
        if (Util.isNetworkAvailable(CustomerResDetailActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(CustomerResDetailActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.UPDATE_RESERVATION);
            jsonObject.addProperty("reservation_id", reservation_id);
            jsonObject.addProperty("business_id", buss_id);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerResDetailActivity.this));
            jsonObject.addProperty("date", date);
            jsonObject.addProperty("time", time);
           /* jsonObject.addProperty("party_size", holder.editpartysize.getText().toString());
            jsonObject.addProperty("service_type1", model.getService_type1());
            jsonObject.addProperty("service_type2", model.getService_type2());*/
            Log.e(TAG, "Request update Reservation :- " + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.update_reservation(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, " Updating Reservation :- " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                getResrvData();
                                Toast.makeText(CustomerResDetailActivity.this, "Updated successfully", Toast.LENGTH_LONG).show();
                            }
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jarray = jsonObject.getJSONArray("result");
                            JSONObject jobj = jarray.getJSONObject(0);
                            Toast.makeText(CustomerResDetailActivity.this, "Unable to update", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("Confirm Reservation error :- ", Log.getStackTraceString(t));
                    progressHD.dismiss();
                }
            });
        } else {
            Toast.makeText(CustomerResDetailActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        new_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        Log.e(TAG, "new_date >> " + new_date);
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog timePickerDialog, int hourOfDay, int minute, int i2) {
                boolean isPM = (hourOfDay >= 12);
                new_time = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM");
                Log.e("new_time >> ", new_time);
                inputReschedule.setText(new_date + ", " + new_time);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show(getFragmentManager(), "timepickerdialog");
        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        timePickerDialog.setCancelColor(getResources().getColor(R.color.colorPrimary));
        timePickerDialog.setOkColor(getResources().getColor(R.color.colorPrimary));
    }

    private void getResrvData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.RESERV_DETAILS);
        jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferences.getCustomerid(CustomerResDetailActivity.this));
        jsonObject.addProperty("business_id", buss_id);
        jsonObject.addProperty("reservation_id", reservation_id);
        Log.e(TAG, "Request GET reser json" + jsonObject.toString());
        getReserDataApi(jsonObject);
    }

    private void getReserDataApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(CustomerResDetailActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        Gson gson = new Gson();
                        model = gson.fromJson(String.valueOf(jsonObject1), ReservationDetails.class);
                        tvBusName.setText(jsonObject1.getString("business_name"));
                        tvReservationID.setText(model.getReservationId());
                        tvDateTime.setText(model.getReservationBookedDate());
                        tvBookingTime.setText(model.getTime());
                        tvBookingDate.setText(model.getDate());
                        tvCustomerName.setText(model.getName());
                        tvTableNo.setText(model.getTableNumber());
                        tvPartySize.setText(model.getPartySize());
                        tvContactNo.setText(model.getMobile());
                        tvEmail.setText(model.getEmailId());
                        reservation_status = model.getReservationStatus();
                        if (reservation_status.equalsIgnoreCase("Booked")) {

                        } else if (reservation_status.equalsIgnoreCase("Waiting")) {
                            tvConfirmed.setText("YES");

                        } else if (reservation_status.equalsIgnoreCase("Cancelled")) {
                            tvConfirmed.setText("NA");
                        } else if (reservation_status.contains("Confirm")) {
                            tvConfirmed.setText("");
                            tvConfirmed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_green_24dp, 0, 0, 0);
                        }
                        if (model.getSetCheckedIn() == 1) {
                            tvCheckin.setText("");
                            tvCheckin.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_green_24dp, 0, 0, 0);
                        }
                        if (model.getSetTableReady() == 1) {
                            tvTableReady.setText("");
                            tvTableReady.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_green_24dp, 0, 0, 0);
                        }
                        if (!model.getSetWaitTime().equalsIgnoreCase("")) {
                            tvWaitTime.setText(model.getSetWaitTime());
                            tvWaitTime.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tvWaitTime.setTypeface(Typeface.createFromAsset(getAssets(), "Raleway-Bold.ttf"));
                        }
                        if (!model.getSetSeatedBy().equalsIgnoreCase("")) {
                            tvSeatedBy.setText(model.getSetSeatedBy());
                            tvSeatedBy.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tvSeatedBy.setTypeface(Typeface.createFromAsset(getAssets(), "Raleway-Bold.ttf"));
                        }
                        if (!model.getSetReschedule().equalsIgnoreCase("")) {
                            tvReschedule.setText(model.getSetReschedule());
                            tvReschedule.setTextColor(getResources().getColor(R.color.colorPrimary));
                            tvReschedule.setTypeface(Typeface.createFromAsset(getAssets(), "Raleway-Bold.ttf"));
                        }

                        tvDeposit.setText("$ " + model.getReservation_amount());
                        tvDeposit.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvDeposit.setTypeface(Typeface.createFromAsset(getAssets(), "Raleway-Bold.ttf"));
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(CustomerResDetailActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CustomerResDetailActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(CustomerResDetailActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
