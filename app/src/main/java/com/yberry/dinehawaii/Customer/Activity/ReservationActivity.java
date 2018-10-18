package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.Model.TableData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomRadioButton;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    CustomButton complete, btnPay;
    CustomEditText partySize, userName, mobileNo, emailId, noOfAdults, noOfChilds, datePicker, timePicker;
    CustomTextView businessName;
    CheckBox child_high_chair, child_booster_chair;
    LinearLayout llTableBook;
    ImageView back;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConstants.PAYPAL_CLIENT_ID);
    Dialog dialog;
    CustomTextView btn_BookTable, tvPreChargesText, tvPaymentText;
    ArrayList<TableData> tableDataList;
    private boolean isWaitList = false;
    private String combinetable = "", reserve_lead_time, selecteDateToCompare = "",
            business_id = "0", wallet_amt = "0", reservation_id, pre_amonut,
            selectedTableID = "", child_booster = "0", child_high = "0", name,
            partySizeS, dateString, timeString, userNameString, mobileNoString,
            emailString, noofadultsString, noofchildString;
    private Context context;
    private ListItem data;
    private RadioGroup radioPaymode;
    private CustomRadioButton radio_wallet, radio_paypal;
    private double pre_charges = 0.0, pre_charges_base = 0.0;
    private DecimalFormat decimalFormat;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_reservation);
        context = this;
        setToolbar();
        initViews();

        data = (ListItem) getIntent().getParcelableExtra("data");
        business_id = data.getId();
        Log.e(TAG, "onCreate: data >> " + data);
        if (data.getReservationPrice().equalsIgnoreCase("0") || data.getReservationPrice().equalsIgnoreCase("")) {
            tvPreChargesText.setText("No pre-reservation charges required!");
            radioPaymode.setVisibility(View.GONE);
        } else {
            pre_charges = Double.parseDouble(data.getReservationPrice());
            pre_charges_base = Double.parseDouble(data.getReservationPrice());
            tvPreChargesText.setText("Pay $" + pre_charges + " as pre-reservation charges using:");
            radioPaymode.setVisibility(View.VISIBLE);
            btnPay.setText("Pay $" + pre_charges);
        }
        checkPackage();
        getFoodPrepTime();
        userName.setText(AppPreferences.getCustomername(context));
        mobileNo.setText(AppPreferences.getCustomerMobile(context));
        emailId.setText(AppPreferences.getEmailSetting(context));
//        wallet_amt = AppPreferences.getWalletAmt(context);
        wallet_amt = "5";
        radio_wallet.setText("Wallet Amount : $" + wallet_amt);
    }

    private void getFoodPrepTime() {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.FOOD_PREP_TIME);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
            jsonObject.addProperty("business_id", business_id);
            Log.e(TAG, "getFoodPrepTime: Request >> " + jsonObject);
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.requestGeneral(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        Log.e(TAG, "getFoodPrepTime: Response >> " + response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = resultJsonArray.getJSONObject(0);
                            reserve_lead_time = object.getString("reserve_lead_time");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "getFoodPrepTime: onFailure >> " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void checkPackage() {
        String package_list = AppPreferences.getBussPackageList(context);
        Log.e(TAG, "checkPackage: package_list >> " + package_list);

        if (package_list.equalsIgnoreCase("1")) llTableBook.setVisibility(View.GONE);
        else if (package_list.equalsIgnoreCase("1, 3")) llTableBook.setVisibility(View.GONE);
        else if (package_list.equalsIgnoreCase("1, 2")) llTableBook.setVisibility(View.VISIBLE);
        else if (package_list.equalsIgnoreCase("1, 2, 3")) llTableBook.setVisibility(View.VISIBLE);
        else if (package_list.equalsIgnoreCase("1, 2, 3, 4"))
            llTableBook.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        tableDataList = new ArrayList<TableData>();
        decimalFormat = new DecimalFormat("#.##");
        tvPaymentText = (CustomTextView) findViewById(R.id.tvPaymentText);

        btn_BookTable = (CustomTextView) findViewById(R.id.btn_bookTable);
        radio_wallet = (CustomRadioButton) findViewById(R.id.radio_wallet);
        radio_paypal = (CustomRadioButton) findViewById(R.id.radio_paypal);

        radioPaymode = (RadioGroup) findViewById(R.id.radioPaymode);
        tvPreChargesText = (CustomTextView) findViewById(R.id.tvPreChargesText);
        llTableBook = (LinearLayout) findViewById(R.id.llTableBook);
        complete = (CustomButton) findViewById(R.id.complete);
        btnPay = (CustomButton) findViewById(R.id.btnPay);
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
        radioPaymode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Log.e(TAG, "onCheckedChanged: ");
                setWallet();
            }
        });
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
                    Toast.makeText(context, "Select booking date", Toast.LENGTH_SHORT).show();
                else if (timePicker.getText().toString().equalsIgnoreCase(""))
                    Toast.makeText(context, "Select booking time", Toast.LENGTH_SHORT).show();
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

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWaitList = false;
                Log.e(TAG, "onClick: pre_charges >> " + pre_charges);
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
        if (child_booster_chair.isChecked()) {
            child_booster = "1";
        }
        if (child_high_chair.isChecked()) {
            child_high = "1";
        }

        if (datePicker.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(context, "Select booking date", Toast.LENGTH_SHORT).show();
        else if (timePicker.getText().toString().equalsIgnoreCase(""))
            Toast.makeText(context, "Select booking time", Toast.LENGTH_SHORT).show();
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
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.MAKE_BUSINESS_RESERVATION);
        jsonObject.addProperty("business_id", business_id);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
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

        Log.e(TAG, "submitRequest: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    String s = response.body().toString();
                    Log.e(TAG, "submitRequest: Response: >> " + s);
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        reservation_id = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            reservation_id = jsonObject1.getString("reservation_id");
                            AppPreferencesBuss.setReservatId(context, reservation_id);
                            if (pre_charges == 0.0 || pre_charges == 0 || isWaitList) {
                                //showThankYouAlert();
                                confirmReservation("wallet", "TRANSWAL0", "approved");
                            } else {
                                getPayment();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressHD != null && progressHD.isShowing())
                        progressHD.dismiss();
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
        dialog = new Dialog(context);
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
                btn_BookTable.setText("SELECTED TABLE : " + table.getTable_name());
                Log.d("selectedTableID", selectedTableID);
                gridView.setSelection(i);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getBusinessTables() {
        if (Util.isNetworkAvailable(context)) {
            final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_PARTY_SIZE_TABLE);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
            jsonObject.addProperty("party_size", partySize.getText().toString().trim());
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));
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
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void showWaitListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                Intent intent = new Intent(context, AlternateBusinessActivity.class);
                intent.putExtra("party_size", partySize.getText().toString());
                startActivityForResult(intent, 102);
            }
        });

        builder.show();
    }

    private void showNoTableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                Intent intent = new Intent(context, AlternateBusinessActivity.class);
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
            pre_charges = Double.parseDouble(data.getStringExtra("reserve_amt").replaceAll("\\$", "").replaceAll("\\s", ""));
            pre_charges = pre_charges_base;
            Log.e(TAG, "onActivityResult: business_id >> " + business_id);
            Log.e(TAG, "onActivityResult: selectedTableID >> " + selectedTableID);
            Log.e(TAG, "onActivityResult: pre_charges >> " + pre_charges);
            if (pre_charges == 0 || pre_charges == 0.0) {
                tvPreChargesText.setText("No pre-reservation charges required!");
                radioPaymode.setVisibility(View.GONE);
            } else {
                tvPreChargesText.setText("Pay $" + pre_charges + " as pre-reservation charges using:");
                radioPaymode.setVisibility(View.VISIBLE);
                btnPay.setText("Pay $" + pre_charges);
            }
            AppPreferences.setBusiID(this, business_id);
//            btnPay.performClick();
        } else if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentExample", paymentDetails);
                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);
                            JSONObject jsonResponse = jsonDetails.getJSONObject("response");
                            AppPreferences.setTranctionId(context, jsonResponse.getString("id"));
                            String transaction_ID = jsonResponse.getString("id");
                            String createTime = jsonResponse.getString("create_time");
                            String intent = jsonResponse.getString("intent");
                            String paymentState = jsonResponse.getString("state");
                            Log.e("paymentExample", "RESPONSE :- \n" + "Transaction_ID :- " + transaction_ID + "\nCreate Time :- " + createTime +
                                    "\nIntent :- " + intent + "\nPayment State :- " + paymentState);

                            if (paymentState.equalsIgnoreCase("approved")) {
                                confirmReservation("paypal", transaction_ID, paymentState);
                            } else {
                                Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("paymentExample", "The user canceled.");
                Toast.makeText(getApplicationContext(), "Sorry!! Payment cancelled by User", Toast.LENGTH_SHORT).show();
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void setWallet() {
//        wallet_amt = AppPreferences.getWalletAmt(context);
        wallet_amt = "5";
        radio_wallet.setText("Wallet Amount : $" + wallet_amt);
        if (radio_wallet.isChecked()) {
            if (!wallet_amt.equalsIgnoreCase("0") && !wallet_amt.equalsIgnoreCase("")) {
                if (pre_charges < Double.parseDouble(wallet_amt)) {
                    double wallet_remaining_amt = Double.parseDouble(wallet_amt) - pre_charges;
                    radio_wallet.setText("Wallet Amount : $" + decimalFormat.format(wallet_remaining_amt));
                    tvPaymentText.setText("Amount will be deduct from your wallet");
                    wallet_amt = String.valueOf(pre_charges);
                    pre_charges = 0;
                    btnPay.setText("Pay $" + pre_charges + "");
                } else {
                    double remaining_amt = pre_charges - Double.parseDouble(wallet_amt);
                    tvPaymentText.setText("$" + wallet_amt + " will be deduct from your wallet and remaining $" + decimalFormat.format(remaining_amt) + " will be done via paypal.");
                    pre_charges = remaining_amt;
                    btnPay.setText("Pay $" + decimalFormat.format(pre_charges));
                }
            } else {
                Toast.makeText(context, "Wallet is empty!", Toast.LENGTH_SHORT).show();
                radio_paypal.setChecked(true);
            }
        } else {
            pre_charges = pre_charges_base;
            btnPay.setText("Pay $" + decimalFormat.format(pre_charges));
            tvPaymentText.setText("Payment will be done via paypal!");
        }
    }

    private void getPayment() {
        Log.e(TAG, "getPayment: pre_charges >> " + pre_charges);

        if (pre_charges == 0.0) {
            Toast.makeText(this, "Payment amount can't be zero ", Toast.LENGTH_SHORT).show();
        } else {
            PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(pre_charges)), "USD", "Purchase Fee\n",
                    PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(context, com.paypal.android.sdk.payments.PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        }
    }

    void confirmReservation(String paymentType, String transaction_ID, String paymentState) {
        if (Util.isNetworkAvailable(context)) {
            final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_RESERVATION_CONFIRMATION);
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));
            jsonObject.addProperty("reservation_id", AppPreferencesBuss.getReservatId(context));
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));//testing demo code
            jsonObject.addProperty("reservation_amount", pre_charges);
            jsonObject.addProperty("wallet_amt", wallet_amt);
            jsonObject.addProperty("paymentType", paymentType);
            jsonObject.addProperty("transaction_id", transaction_ID);
            jsonObject.addProperty("payment_status", paymentState);
            jsonObject.addProperty("min_hours", "2");
            jsonObject.addProperty("time", Function.getCurrentDateTime());
            jsonObject.addProperty("nofity_checkbox", "0");
            Log.e(TAG, "confirmReservation: Request >> " + jsonObject);
            Log.d("SaveCon", AppPreferencesBuss.getReservatId(context));

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.get_reservation_confirmation(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        String resp = response.body().toString();
                        Log.e(TAG, "confirmReservation: Response >> " + resp);
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            jsonObject.getString("message");
                            showThankYouAlert();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String msg = jsonObject1.getString("msg");
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        progressHD.dismiss();
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "confirmReservation: onFailure >> " + t.getMessage());
                    progressHD.dismiss();
                    Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }
}
