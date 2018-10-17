package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "ReservationActivity";
    CustomButton complete, submit;
    String business_id, business_name, reservation_id, date, time, pre_amonut;
    CustomEditText partySize, userName, mobileNo, emailId, noOfAdults, noOfChilds;
    CustomEditText datePicker, timePicker;
    CustomTextView businessName;
    CheckBox child_high_chair, child_booster_chair;
    String child_booster = "0", child_high = "0";
    LinearLayout llTableBook;
    ImageView back;
    Dialog dialog;
    CustomTextView btn_BookTable;
    String selectedTableID = "";
    String partySizeS, dateString, timeString, userNameString, mobileNoString, emailString, noofadultsString, noofchildString;
    String name, i, selected_table;
    ArrayList<TableData> tableDataList;
    private boolean isWaitList = false;
    private String combinetable = "";
    private String reserve_lead_time;
    private String selecteDateToCompare = "";

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_reservation);
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
        checkPackage();
        getFoodPrepTime();


        userName.setText(AppPreferences.getCustomername(ReservationActivity.this));
        mobileNo.setText(AppPreferences.getCustomerMobile(ReservationActivity.this));
        emailId.setText(AppPreferences.getEmailSetting(ReservationActivity.this));
    }

    private void getFoodPrepTime() {
        if (Util.isNetworkAvailable(ReservationActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.FOOD_PREP_TIME);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(ReservationActivity.this));
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(ReservationActivity.this));
            Log.e(TAG, "get food prep time" + jsonObject.toString());
            getFoodTimeApi(jsonObject);
        } else {
            Toast.makeText(ReservationActivity.this, "Please Connect Internet", Toast.LENGTH_LONG).show();
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
                        reserve_lead_time = object.getString("reserve_lead_time");

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

    private void checkPackage() {
        Log.e("Activity", "Making Registration");
        String package_list = AppPreferences.getBussPackageList(ReservationActivity.this);
        Log.d("SelActivity", AppPreferences.getBussPackageList(ReservationActivity.this));

        if (package_list.equalsIgnoreCase("1")) llTableBook.setVisibility(View.GONE);
        else if (package_list.equalsIgnoreCase("1, 3")) llTableBook.setVisibility(View.GONE);
        else if (package_list.equalsIgnoreCase("1, 2")) llTableBook.setVisibility(View.VISIBLE);
        else if (package_list.equalsIgnoreCase("1, 2, 3")) llTableBook.setVisibility(View.VISIBLE);
        else if (package_list.equalsIgnoreCase("1, 2, 3, 4"))
            llTableBook.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        tableDataList = new ArrayList<TableData>();
        btn_BookTable = (CustomTextView) findViewById(R.id.btn_bookTable);
        llTableBook = (LinearLayout) findViewById(R.id.llTableBook);
        complete = (CustomButton) findViewById(R.id.complete);
        submit = (CustomButton) findViewById(R.id.submit1);
        businessName = (CustomTextView) findViewById(R.id.businessName);
        partySize = (CustomEditText) findViewById(R.id.partySize);
        datePicker = (CustomEditText) findViewById(R.id.datePicker);
        timePicker = (CustomEditText) findViewById(R.id.timePicker);
        userName = (CustomEditText) findViewById(R.id.userName);
        mobileNo = (CustomEditText) findViewById(R.id.textViewMobile);
        emailId = (CustomEditText) findViewById(R.id.textViewEmail);
        noOfAdults = (CustomEditText) findViewById(R.id.textViewNoofAdults);
        noOfChilds = (CustomEditText) findViewById(R.id.textViewNoofChilds);
        child_high_chair = (CheckBox) findViewById(R.id.child_high_chair);
        child_booster_chair = (CheckBox) findViewById(R.id.child_booster_chair);

        //keyboard dismiss on outside touch
        ((RelativeLayout) findViewById(R.id.touch_outside)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(ReservationActivity.this);
                return false;
            }
        });

        btn_BookTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datePicker.getText().toString().equalsIgnoreCase(""))
                    Toast.makeText(ReservationActivity.this, "Select booking date", Toast.LENGTH_SHORT).show();
                else if (timePicker.getText().toString().equalsIgnoreCase(""))
                    Toast.makeText(ReservationActivity.this, "Select booking time", Toast.LENGTH_SHORT).show();
                else if (userName.getText().toString().equalsIgnoreCase(""))
                    userName.setError("Enter your full name");
                else if (mobileNo.getText().toString().equalsIgnoreCase(""))
                    mobileNo.setError("Enter your mobile no.");
                else if (emailId.getText().toString().equalsIgnoreCase(""))
                    emailId.setError("Enter your email-id");
                else if (partySize.getText().toString().equalsIgnoreCase(""))
                    partySize.setError("Enter party size");
                else if (noOfChilds.getText().toString().equalsIgnoreCase(""))
                    noOfChilds.setError("Enter no of children");
                else if (noOfAdults.getText().toString().equalsIgnoreCase(""))
                    noOfAdults.setError("Enter no of adults");
                else
                    getBusinessTables();
            }
        });


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ReservationActivity.this,
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
               // c.add(Calendar.DATE, 1);
                dpd.setMinDate(c);
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePicker.getText().toString().isEmpty()) {
                    datePicker.setError("First select date");
                } else {


                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePickerDialog timePickerDialog, int hourOfDay, int minute, int i2) {
                            boolean isPM = (hourOfDay >= 12);
                            String selectedTime = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM");
                            Log.d("selectedTime", selectedTime);

                            //check for date
                            String mytime = datePicker.getText().toString();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date selectedDate = null, currentDate = null;
                            Calendar c = Calendar.getInstance();
                            String currentTimeStr = dateFormat.format(c.getTime());
                            try {
                                selectedDate = dateFormat.parse(mytime);
                                currentDate = dateFormat.parse(currentTimeStr);
                                Log.e(TAG, "onTimeSet: selectedDate >> " + selectedDate);
                                Log.e(TAG, "onTimeSet: currentDate >> " + currentDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (currentDate.compareTo(selectedDate) == 0) {
                                Log.e(TAG, "onTimeSet: currentDate is equal to selectedDate");
                                SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
                                SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                                String currentTime = parseFormat.format(c.getTime());

                                Log.e(TAG, "onTimeSet: selecteDateToCompare >> " + selecteDateToCompare);

                                try {
                                    Date currentTime24 = parseFormat.parse(currentTime);
                                    Date selectedTime24 = parseFormat.parse(selectedTime);
                                    Log.e(TAG, "onCreate: currentTime24 >> " + displayFormat.format(currentTime24));
                                    Log.e(TAG, "onCreate: selectedTime24 >> " + displayFormat.format(selectedTime24));
                                    c.add(Calendar.MINUTE, Integer.parseInt(reserve_lead_time));

                                    String newTime = parseFormat.format(c.getTime());
                                    Date finalCurrentTime = parseFormat.parse(newTime);
                                    Log.e(TAG, "onCreate: finalCurrentTime >> " + displayFormat.format(finalCurrentTime));

                                    if (finalCurrentTime.compareTo(selectedTime24) > 0) {
                                        Log.e(TAG, "onTimeSet: selected time is greater than current time");
                                        showAlertDialog("Reservation requires minimum " + reserve_lead_time + " mins before reservation time");
                                    } else if (finalCurrentTime.compareTo(selectedTime24) < 0) {
                                        timePicker.setText(selectedTime);
                                        Log.e(TAG, "onTimeSet: selected time is less than currect time");
                                    } else if (finalCurrentTime.compareTo(selectedTime24) == 0) {
                                        timePicker.setText(selectedTime);
                                        Log.e(TAG, "onTimeSet: current time is equal selected time");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                timePicker.setText(selectedTime);
                            }

                            //check for time

                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show(getFragmentManager(), "timepickerdialog");

                    timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
                    timePickerDialog.setCancelColor(getResources().getColor(R.color.colorPrimary));
                    timePickerDialog.setOkColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWaitList = false;
                makeReservation();
            }
        });
    }

    private void showAlertDialog(String msg) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ReservationActivity.this);
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
        if (child_booster_chair.isChecked()) {
            child_booster = "1";
        }
        if (child_high_chair.isChecked()) {
            child_high = "1";
        }

        if (datePicker.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(ReservationActivity.this, "Select booking date", Toast.LENGTH_SHORT).show();
        else if (timePicker.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(ReservationActivity.this, "Select booking time", Toast.LENGTH_SHORT).show();
        else if (userName.getText().toString().equalsIgnoreCase(""))
            userName.setError("Enter your full name");
        else if (mobileNo.getText().toString().equalsIgnoreCase(""))
            mobileNo.setError("Enter your mobile no.");
        else if (emailId.getText().toString().equalsIgnoreCase(""))
            emailId.setError("Enter your email-id");
        else if (partySize.getText().toString().equalsIgnoreCase(""))
            partySize.setError("Enter party size");
        else if (noOfChilds.getText().toString().equalsIgnoreCase(""))
            noOfChilds.setError("Enter no of children");
        else if (noOfAdults.getText().toString().equalsIgnoreCase(""))
            noOfAdults.setError("Enter no of adults");
        else if (selectedTableID.equalsIgnoreCase(""))
            Toast.makeText(this, "Select table", Toast.LENGTH_SHORT).show();
        else {
            partySizeS = partySize.getText().toString().trim();
            dateString = datePicker.getText().toString().trim();
            timeString = timePicker.getText().toString().trim();
            userNameString = userName.getText().toString().trim();
            mobileNoString = mobileNo.getText().toString().trim();
            emailString = emailId.getText().toString().trim();
            noofadultsString = noOfAdults.getText().toString().trim();
            noofchildString = noOfChilds.getText().toString().trim();
            submitRequest(partySizeS, dateString, timeString, userNameString, mobileNoString, emailString, noofadultsString, noofchildString, selectedTableID);
        }
    }

    private void submitRequest(String partySizeS, String date, String time, String name, String phone_no, String email, String adults, String childs, String table_id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.MAKE_BUSINESS_RESERVATION);
        jsonObject.addProperty("business_id", business_id);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(ReservationActivity.this));
        jsonObject.addProperty("date", date);
        jsonObject.addProperty("time", time);  //time
        jsonObject.addProperty("party_size", partySizeS);
        jsonObject.addProperty("child_high_chair", child_high);
        jsonObject.addProperty("child_booster_chair", child_booster);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("mobile", phone_no);
        jsonObject.addProperty("no_of_adults", adults);
        jsonObject.addProperty("no_of_childers", childs);
        jsonObject.addProperty("table_id", table_id);
        jsonObject.addProperty("combine_table", combinetable);//
        Log.e(TAG, "Request MAKING RESERVATION >> " + jsonObject.toString());
        make_business_reservation(jsonObject);
    }

    private void make_business_reservation(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                            AppPreferencesBuss.setReservatId(ReservationActivity.this, reservation_id);
                            if (pre_amonut.equalsIgnoreCase("0") || isWaitList) {
                                showThankYouAlert();
                            } else {
                                Intent in = new Intent(ReservationActivity.this, CustomerConfirmreservationActivity.class);
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
                                finish();
                            }
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("Thank You!");
        builder.setCancelable(false);
        builder.setMessage("Your reservation has done successfully.");
        ImageView img = new ImageView(ReservationActivity.this);
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        Log.e(TAG, "You picked the following Date: " + date);
        datePicker.setText(date);
        datePicker.setError(null);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = hourOfDay + ":" + minute + ":" + second;
        Log.v(TAG, "You picked the following time: " + time);
        timePicker.setText(time);
    }

    private void customTableDialog(ArrayList<TableData> list, String msg) {
        dialog = new Dialog(ReservationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_table);
        dialog.setCanceledOnTouchOutside(false);
        TableAdapter adapter = new TableAdapter(dialog.getContext(), list);
        adapter.notifyDataSetChanged();
        final GridView gridView = (GridView) dialog.findViewById(R.id.gridAvailableTables);
        CustomTextView tvMessage = (CustomTextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(msg);
        ImageView btnCancel = (ImageView) dialog.findViewById(R.id.btnCancel);
        Log.e("table-list", list + "");
        gridView.setAdapter(adapter);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TableData table = tableDataList.get(i);

                selectedTableID = table.getTable_id();
                selected_table = table.getTable_name();
                btn_BookTable.setText("SELECTED TABLE : " + table.getTable_name());
                Log.d("selectedTableID", selectedTableID);
                gridView.setSelection(i);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getBusinessTables() {
        if (Util.isNetworkAvailable(ReservationActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(ReservationActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_PARTY_SIZE_TABLE);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(ReservationActivity.this));
            jsonObject.addProperty("party_size", partySize.getText().toString().trim());
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(ReservationActivity.this));
            Log.e(TAG, "Request GET TABLES >> " + jsonObject.toString());
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.get_party_size_tables(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response GET TABLES >> " + response.body().toString());
                    String s = response.body().toString();

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        tableDataList.clear();
                        String msg;
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                name = jsonObject1.getString("table_name");
                                combinetable = jsonObject1.getString("combine_table");
                                TableData table = new TableData();
                                table.setTable_id(id);
                                table.setTable_name(name);
                                Log.e("Table Data:", "" + table.toString());
                                tableDataList.add(table);
                            }
                            Log.e("Table List:", "" + tableDataList);
                            msg = jsonObject.getString("message");
                            customTableDialog(tableDataList, msg);
                        } else if (jsonObject.getString("status").equalsIgnoreCase("300")) {
                            showWaitListDialog();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            msg = jsonObject1.getString("msg");
                            Log.e("msg", msg);
                            showNoTableDialog();
                        }
                        if (progressHD != null && progressHD.isShowing())
                            progressHD.dismiss();
                    } catch (JSONException e) {
                        if (progressHD != null && progressHD.isShowing())
                            progressHD.dismiss();
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (progressHD != null && progressHD.isShowing())
                        progressHD.dismiss();
                    Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                }
            });
        } else {
            Toast.makeText(ReservationActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void showWaitListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("Add To Wait-List");
        builder.setMessage("We are sorry, but your request at " + datePicker.getText().toString() + ", " + timePicker.getText().toString() + " for the party size " + partySize.getText().toString() + " is unavailable.\n\nDo you want to add it to wait list?");
        builder.setPositiveButton("ADD TO WAIT-LIST", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedTableID = "0";
                isWaitList = true;
                makeReservation();
            }
        });


        builder.setNegativeButton("Choose Alternate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ReservationActivity.this, AlternateBusinessActivity.class);
                intent.putExtra("party_size", partySize.getText().toString());
                startActivityForResult(intent, 102);
            }
        });

        builder.show();
    }

    private void showNoTableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("Tables Not Available");
        builder.setMessage("We are sorry, there are no table available for reservations");

        builder.setNegativeButton("CALL RESTAURANT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.setNegativeButton("Choose Alternate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ReservationActivity.this, AlternateBusinessActivity.class);
                intent.putExtra("party_size", partySize.getText().toString());
                startActivityForResult(intent, 102);
            }
        });

        builder.show();
    }

    class TableAdapter extends ArrayAdapter {
        ArrayList<TableData> tableList = new ArrayList<TableData>();
        Context context;

        TableAdapter(Context context, ArrayList<TableData> tableList) {
            super(context, R.layout.grid_item_layout, tableList);
            this.context = context;
            this.tableList = tableList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.grid_item_layout, parent, false);
            TextView textViewTableName = (TextView) v.findViewById(R.id.textViewTableName);
            TableData table = tableList.get(position);
            textViewTableName.setText(table.getTable_name());
            return v;
        }
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
