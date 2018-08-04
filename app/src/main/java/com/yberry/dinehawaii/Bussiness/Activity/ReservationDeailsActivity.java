package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yberry.dinehawaii.Model.ReservationDetails;
import com.yberry.dinehawaii.Model.TableData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
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

public class ReservationDeailsActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "ReservationDetails";
    String tempRDate;
    ArrayList<TableData> tableDataList = new ArrayList<TableData>();
    private Context context;
    private ImageView back;
    private CustomTextView tvReservationID, tvDateTime, tvDateTime2, tvBookingTime, tvCustomerName, tvTableNo, tvPartySize, tvContactNo, tvEmail;
    private ReservationDetails model;
    private CustomTextView tvCheckin, tvWaitTime, tvTableReady, tvSeatedBy, tvReschedule, tvConfirmed, tvDeposit, tvClose, tvRestore;
    private String reservation_id;
    private String reservation_status = "";
    private String new_date = "", new_time = "";
    private CustomEditText inputReschedule;
    private LinearLayout llCloseResv, llRestore;
    private AlphaAnimation anim;
    private BroadcastReceiver tickReceiver;
    private SimpleDateFormat displayFormat;
    private SimpleDateFormat parseFormat;
    private Date afterAddingMins;
    private Date reserTime24hr;
    private Date currentTime24;
    private CustomTextView btnNoShow;
    private String[] tableDataListString;
    private String selected_table_id;
    private String combinetable = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_deails);
        setToolbar();
        init();
        initControls();
        displayFormat = new SimpleDateFormat("HH:mm:ss");
        parseFormat = new SimpleDateFormat("hh:mm a");

        if (getIntent().hasExtra("reservation_id"))
            reservation_id = getIntent().getStringExtra("reservation_id");
        if (getIntent().getAction().equalsIgnoreCase("PREVIOUS")) {
            ((CardView) findViewById(R.id.card_now_show)).setVisibility(View.GONE);
            ((CardView) findViewById(R.id.card_confirm)).setVisibility(View.GONE);
            ((CardView) findViewById(R.id.card_make_available)).setVisibility(View.GONE);
            ((FloatingActionButton) findViewById(R.id.fab_send_msg)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.llcheckin)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.llwait)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.lltableready)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.llseated)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.llreschedule)).setVisibility(View.GONE);
            llCloseResv.setVisibility(View.VISIBLE);
        }
        Log.e(TAG, "onCreate: reservation_id >> " + reservation_id);
        getResrvData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tickReceiver != null)
            unregisterReceiver(tickReceiver);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Reservation Details");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initControls() {
        tvCheckin = (CustomTextView) findViewById(R.id.tvCheckin);
        tvWaitTime = (CustomTextView) findViewById(R.id.tvWaitTime);
        tvTableReady = (CustomTextView) findViewById(R.id.tvTableReady);
        tvSeatedBy = (CustomTextView) findViewById(R.id.tvSeatedBy);
        tvReschedule = (CustomTextView) findViewById(R.id.tvReschedule);
        tvConfirmed = (CustomTextView) findViewById(R.id.tvConfirmed);
        tvDeposit = (CustomTextView) findViewById(R.id.tvDeposit);
        tvClose = (CustomTextView) findViewById(R.id.tvClose);
        tvRestore = (CustomTextView) findViewById(R.id.tvRestore);
        llCloseResv = (LinearLayout) findViewById(R.id.llCloseResv);
        llRestore = (LinearLayout) findViewById(R.id.llRestore);

        tvCheckin.setOnClickListener(this);
        tvWaitTime.setOnClickListener(this);
        tvTableReady.setOnClickListener(this);
        tvSeatedBy.setOnClickListener(this);
        tvReschedule.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        tvRestore.setOnClickListener(this);
    }

    private void init() {
        context = this;
        ((CustomTextView) findViewById(R.id.btnCloseR)).setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.btnCancel)).setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.btnConfirm)).setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.btnCancel1)).setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.btnAvailable)).setOnClickListener(this);


        ((FloatingActionButton) findViewById(R.id.fab_send_msg)).setOnClickListener(this);

        tvReservationID = (CustomTextView) findViewById(R.id.tvReservationID);
        tvDateTime = (CustomTextView) findViewById(R.id.tvDateTime);
        tvDateTime2 = (CustomTextView) findViewById(R.id.tvDateTime2);
        tvBookingTime = (CustomTextView) findViewById(R.id.tvBookingTime);
        tvCustomerName = (CustomTextView) findViewById(R.id.tvCustomerName);
        tvTableNo = (CustomTextView) findViewById(R.id.tvTableNo);
        tvPartySize = (CustomTextView) findViewById(R.id.tvPartySize);
        tvContactNo = (CustomTextView) findViewById(R.id.tvContactNo);
        tvEmail = (CustomTextView) findViewById(R.id.tvEmail);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_send_msg) {
            Intent intent = new Intent(context, SendMessageActivity.class);
            intent.putExtra("reservation_id", reservation_id);
            startActivity(intent);
        } else if (v.getId() == R.id.btnCancel) {
            cancelDialog();
        } else if (v.getId() == R.id.btnConfirm) {
            confirmReservation();
        } else if (v.getId() == R.id.btnCancel1) {
            cancelDialog();
        } else if (v.getId() == R.id.btnAvailable) {
            makeRservAvailable();
        } else if (v.getId() == R.id.tvCheckin) {
            openCheckinDialog();
        } else if (v.getId() == R.id.tvWaitTime) {
            openWaitTimeDialog();
        } else if (v.getId() == R.id.tvTableReady) {
            openTableReadyDialog();
        } else if (v.getId() == R.id.tvSeatedBy) {
            openSeatedByDialog();
        } else if (v.getId() == R.id.tvReschedule) {
            openRescheduleDialog();
        } else if (v.getId() == R.id.tvClose) {
            openCloseDialog();
        } else if (v.getId() == R.id.tvRestore) {
            getBusinessTables();
        } else if (v.getId() == R.id.btnCloseR) {
            closeReservation();
        } else if (v.getId() == R.id.btnNoShow) {
            makeNoShow();
        }
    }

    private void choseRestoreTable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationDeailsActivity.this);
        builder.setTitle("Restore Reservation");
        builder.setMessage("Choose a new table to reassign: ");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close api
                closeReservation();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void makeRservAvailable() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.WAIT_RESERV_CONFIRM);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        jsonObject.addProperty("reserv_id", reservation_id);
        Log.e(TAG, "make res ava Request >> " + jsonObject.toString());
        performAction(jsonObject);
    }

    private void getResrvData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.RESERV_DETAILS);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        jsonObject.addProperty("reservation_id", reservation_id);
        Log.e(TAG, "Request GET RESERVATION DETAILS" + jsonObject.toString());

        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response RESERVATION DETAILS >>  " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                        Gson gson = new Gson();
                        model = gson.fromJson(String.valueOf(jsonObject1), ReservationDetails.class);
                        Log.e(TAG, "getResrvData: Response >> " + model);

                        tvReservationID.setText(model.getReservationId());
                        tvDateTime.setText(model.getReservationBookedDate());
                        tvBookingTime.setText(model.getTime());
                        tvDateTime2.setText(model.getDate());
                        tvCustomerName.setText(model.getName());
                        tvTableNo.setText(model.getTableNumber());
                        tvPartySize.setText(model.getPartySize());
                        tvContactNo.setText(model.getMobile());
                        tvEmail.setText(model.getEmailId());
                        reservation_status = model.getReservationStatus();

                        if (getIntent().getAction().equalsIgnoreCase("NOSHOW") && model.getSetCheckedIn() != 0) {
                            ((CardView) findViewById(R.id.card_now_show)).setVisibility(View.VISIBLE);
                            ((CardView) findViewById(R.id.card_confirm)).setVisibility(View.GONE);
                            ((CardView) findViewById(R.id.card_make_available)).setVisibility(View.GONE);
                            ((CustomTextView) findViewById(R.id.btnCloseR)).setOnClickListener(ReservationDeailsActivity.this);
                            btnNoShow = (CustomTextView) findViewById(R.id.btnNoShow);
                            anim = new AlphaAnimation(0.0f, 1.0f);
                            anim.setDuration(200);
                            anim.setStartOffset(20);
                            anim.setRepeatMode(Animation.REVERSE);
                            anim.setRepeatCount(Animation.INFINITE);
                            //Create a broadcast receiver to handle change in time
                            tickReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                        Calendar c = Calendar.getInstance();
                                        String currentTime = sdf.format(c.getTime());
                                        try {
                                            currentTime24 = parseFormat.parse(currentTime);
                                            Log.e(TAG, "onCreate: currentTime24 >> " + displayFormat.format(currentTime24));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (reserTime24hr.compareTo(currentTime24) < 0) {
                                            Log.e(TAG, "reserTime24hr is before currentTime24");
                                            if (afterAddingMins.compareTo(currentTime24) > 0) {
                                                ((CustomTextView) findViewById(R.id.btnNoShow)).startAnimation(anim);
                                                btnNoShow.setOnClickListener(ReservationDeailsActivity.this);
                                                Log.e(TAG, "currentTime24 is before afterAddingMins");
                                                Log.e(TAG, "onCreate: IT'S NO-SHOW TIME");
                                            }
                                        }

                                        if (afterAddingMins.compareTo(currentTime24) < 0 || afterAddingMins.compareTo(currentTime24) == 0) {
                                            Log.e(TAG, "currentTime24 is after or equal to afterAddingMins");
                                            ((CustomTextView) findViewById(R.id.btnNoShow)).clearAnimation();
                                            btnNoShow.setEnabled(false);
                                        }
                                    }
                                }
                            };

                            //Register the broadcast receiver to receive TIME_TICK
                            registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
                            setNoShow();
                        }

                        if (reservation_status.equalsIgnoreCase("Booked")) {
                            if (!getIntent().getAction().equalsIgnoreCase("PREVIOUS"))
                                ((CardView) findViewById(R.id.card_confirm)).setVisibility(View.VISIBLE);
                            tvCheckin.setEnabled(false);
                            tvWaitTime.setEnabled(false);
                            tvTableReady.setEnabled(false);
                            tvSeatedBy.setEnabled(false);
                            tvReschedule.setEnabled(false);
                            llCloseResv.setVisibility(View.VISIBLE);
                            llRestore.setVisibility(View.GONE);
                        } else if (reservation_status.equalsIgnoreCase("Waiting")) {
                            tvConfirmed.setText("NO");
                            ((CardView) findViewById(R.id.card_confirm)).setVisibility(View.GONE);
                            ((CardView) findViewById(R.id.card_make_available)).setVisibility(View.VISIBLE);
                            ((CardView) findViewById(R.id.card_manage_res)).setVisibility(View.GONE);
                        } else if (reservation_status.equalsIgnoreCase("Cancelled")) {
                            tvConfirmed.setText("NA");
                            ((CardView) findViewById(R.id.card_confirm)).setVisibility(View.GONE);
                            ((CardView) findViewById(R.id.card_make_available)).setVisibility(View.GONE);
                            tvCheckin.setEnabled(false);
                            tvWaitTime.setEnabled(false);
                            tvTableReady.setEnabled(false);
                            tvSeatedBy.setEnabled(false);
                            tvReschedule.setEnabled(false);
                            llCloseResv.setVisibility(View.GONE);
                            llRestore.setVisibility(View.GONE);
                        } else if (reservation_status.contains("Confirm")) {
                            tvCheckin.setEnabled(true);
                            tvWaitTime.setEnabled(true);
                            tvTableReady.setEnabled(true);
                            tvSeatedBy.setEnabled(true);
                            tvReschedule.setEnabled(true);
                            tvConfirmed.setText("");
                            llRestore.setVisibility(View.GONE);
                            tvConfirmed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_green_24dp, 0, 0, 0);
                            ((CardView) findViewById(R.id.card_make_available)).setVisibility(View.GONE);
                            ((CardView) findViewById(R.id.card_confirm)).setVisibility(View.GONE);
                            llCloseResv.setVisibility(View.VISIBLE);
                        } else if (reservation_status.equalsIgnoreCase("Closed")) {
                            if (getIntent().getAction().equalsIgnoreCase("PREVIOUS"))
                                llRestore.setVisibility(View.GONE);
                            else llRestore.setVisibility(View.VISIBLE);
                            ((CardView) findViewById(R.id.card_confirm)).setVisibility(View.GONE);
                            ((CardView) findViewById(R.id.card_make_available)).setVisibility(View.GONE);
                            tvCheckin.setEnabled(false);
                            tvWaitTime.setEnabled(false);
                            tvTableReady.setEnabled(false);
                            tvSeatedBy.setEnabled(false);
                            tvReschedule.setEnabled(false);
                            llCloseResv.setVisibility(View.GONE);
                            tvConfirmed.setText("");
                            tvClose.setText("");
                            tvClose.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_green_24dp, 0, 0, 0);
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
                            tvWaitTime.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            tvWaitTime.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
                        }
                        if (!model.getSetSeatedBy().equalsIgnoreCase("")) {
                            tvSeatedBy.setText(model.getSetSeatedBy());
                            tvSeatedBy.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            tvSeatedBy.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
                        }
                        if (!model.getSetReschedule().equalsIgnoreCase("")) {
                            tvReschedule.setText(model.getSetReschedule());
                            tvReschedule.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            tvReschedule.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
                        }
                        tvDeposit.setText("$ " + model.getReservation_amount());
                        tvDeposit.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        tvDeposit.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(ReservationDeailsActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBusinessTables() {
        if (Util.isNetworkAvailable(ReservationDeailsActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_PARTY_SIZE_TABLE);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
            jsonObject.addProperty("party_size", "2");
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
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
                            tableDataListString = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String name = jsonObject1.getString("table_name");
                                combinetable = jsonObject1.getString("combine_table");

                                TableData table = new TableData();
                                table.setTable_id(id);
                                table.setTable_name(name);
                                Log.e("Table Data:", "" + table.toString());
                                tableDataList.add(table);
                                tableDataListString[i] = name;
                            }
                            Log.e("Table List:", "" + tableDataList);
                            new AlertDialog.Builder(ReservationDeailsActivity.this)
                                    .setTitle("Select Re-assign Table")
                                    .setSingleChoiceItems(tableDataListString, 0, null)
                                    .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.dismiss();
                                            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                            String selected_table = tableDataList.get(selectedPosition).getTable_name();
                                            selected_table_id = tableDataList.get(selectedPosition).getTable_id();

                                            Log.e(TAG, "onClick: selected_table >> " + selected_table);
                                            Log.e(TAG, "onClick: selected_table_id >> " + selected_table_id);
                                            restoreReservation();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();

                        } else if (jsonObject.getString("status").equalsIgnoreCase("300")) {
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            msg = jsonObject1.getString("msg");
                            Log.e("msg", msg);
                            new AlertDialog.Builder(context)
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .show();
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
            Toast.makeText(ReservationDeailsActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void restoreReservation() {
        if (Util.isNetworkAvailable(ReservationDeailsActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.RESTORE_RESERVATION);
            jsonObject.addProperty("reservation_id", reservation_id);
            jsonObject.addProperty("table_id", selected_table_id);
            jsonObject.addProperty("combine_table", combinetable);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
            Log.e(TAG, "restoreReservation : Request >> " + jsonObject.toString());
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "restoreReservation: Response >> " + response.body().toString());
                    String s = response.body().toString();

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        tableDataList.clear();
                        String msg;
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                            new AlertDialog.Builder(ReservationDeailsActivity.this)
                                    .setTitle("Message")
                                    .setMessage(msg)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            getResrvData();
                                        }
                                    })
                                    .show();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                            new AlertDialog.Builder(context)
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    })
                                    .show();
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
            Toast.makeText(ReservationDeailsActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void setNoShow() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentTime = sdf.format(c.getTime());
        tempRDate = model.getTime();
//        tempRDate = "3:50 PM";
        try {
            reserTime24hr = parseFormat.parse(tempRDate);
            currentTime24 = parseFormat.parse(currentTime);
            Log.e(TAG, "onCreate: reserTime24hr >> " + displayFormat.format(reserTime24hr));
            Log.e(TAG, "onCreate: currentTime24 >> " + displayFormat.format(currentTime24));

            long reserTime24hrAfter2Min = reserTime24hr.getTime();
            afterAddingMins = new Date(reserTime24hrAfter2Min + (2 * 60000));
            Log.e(TAG, "onCreate: afterAddingMins >> " + displayFormat.format(afterAddingMins));

            if (reserTime24hr.compareTo(currentTime24) < 0) {
                Log.e(TAG, "reserTime24hr is before currentTime24");
                if (afterAddingMins.compareTo(currentTime24) > 0) {
                    ((CustomTextView) findViewById(R.id.btnNoShow)).startAnimation(anim);
                    btnNoShow.setOnClickListener(ReservationDeailsActivity.this);
                    Log.e(TAG, "currentTime24 is before afterAddingMins");
                    Log.e(TAG, "onCreate: IT'S NO-SHOW TIME");
                }
            } else if (reserTime24hr.compareTo(currentTime24) > 0) {
                Log.e(TAG, "reserTime24hr is after currentTime24");
            } else if (reserTime24hr.compareTo(currentTime24) == 0) {
                Log.e(TAG, "reserTime24hr is equal to currentTime24");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void cancelReservation() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.CANCEL_RESERVATION);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        jsonObject.addProperty("reserv_id", reservation_id);
        Log.e(TAG, "cancelReservation: Request >> " + jsonObject.toString());
        performAction(jsonObject);
    }

    private void confirmReservation() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.CONFIRM_RESERVATION);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        jsonObject.addProperty("reserv_id", reservation_id);
        Log.e(TAG, "confirmReservation: Request >> " + jsonObject);
        performAction(jsonObject);

    }

    private void performAction(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "performAction: Response >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray resultArray = jsonObject.getJSONArray("result");
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        /*JSONObject jsonObject1 = resultArray.getJSONObject(0);
                        String message = jsonObject1.getString("msg");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();*/
                    }
                    ((CardView) findViewById(R.id.card_confirm)).setVisibility(View.GONE);
                    getResrvData();
                } catch (JSONException e) {
                    progressHD.dismiss();
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

    private void openCloseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationDeailsActivity.this);
        builder.setTitle("Close Reservation");
        builder.setMessage("Do you want to close this reservation?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close api
                closeReservation();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void closeReservation() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.ENDPOINT.CLOSE_RESERVATION);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        jsonObject.addProperty("reserv_id", reservation_id);
        Log.e(TAG, "closeReservation: Request >> " + jsonObject.toString());
        performCloseAction(jsonObject);
    }

    private void performCloseAction(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "closeReservation: Response >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray resultArray = jsonObject.getJSONArray("result");
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        ((CardView) findViewById(R.id.card_now_show)).setVisibility(View.GONE);
                        getIntent().setAction("SHOW");
                        getResrvData();
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
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    private void makeNoShow() {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.ENDPOINT.NOSHOWTIMEUPDATE);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        jsonObject.addProperty("reserv_id", reservation_id);
        Log.e(TAG, "makeNoShow: Request >> " + jsonObject.toString());


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "makeNoShow: Response >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray resultArray = jsonObject.getJSONArray("result");
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        ((CardView) findViewById(R.id.card_now_show)).setVisibility(View.GONE);
                        getResrvData();
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
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    private void cancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationDeailsActivity.this);
        builder.setTitle("Cancel Reservation");
        builder.setMessage("Do you want to cancel this reservation?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelReservation();
            }
        });
        builder.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void openRescheduleDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reschedule");
        builder.setCancelable(false);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input_contact_no, (ViewGroup) findViewById(android.R.id.content), false);
        inputReschedule = (CustomEditText) viewInflated.findViewById(R.id.input);
        inputReschedule.setHint("Select New Date");
        builder.setView(viewInflated);
        inputReschedule.setText(tvReschedule.getText().toString());
        inputReschedule.setFocusableInTouchMode(false);

        inputReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ReservationDeailsActivity.this,
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
                if (!new_time.isEmpty() && !new_date.isEmpty())
                    setReschedule();
                else {
                    Toast.makeText(context, "Reselect reschedule date & time", Toast.LENGTH_SHORT).show();
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

    private void setReschedule() {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.SETRESCHEDULE);
        jsonObject.addProperty("reserv_id", reservation_id);
        jsonObject.addProperty("date", new_date);
        jsonObject.addProperty("time", new_time);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        Log.e(TAG, "Request SET RESCHEDULE" + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_reservation_status_manage(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response RESCHEDULE >>  " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        getResrvData();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(ReservationDeailsActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openSeatedByDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seated By");
        builder.setCancelable(false);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input_contact_no, (ViewGroup) findViewById(android.R.id.content), false);
        final CustomEditText input = (CustomEditText) viewInflated.findViewById(R.id.input);
        input.setHint("Enter Wait Staff Name");
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(viewInflated);

        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String wait_staff_name = input.getText().toString();
                Log.e(TAG, "onClick: wait_staff_name >> " + wait_staff_name.length());
                dialog.dismiss();
                setTvSeatedBy(wait_staff_name);
            }
        });
        builder.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void openTableReadyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationDeailsActivity.this);
        builder.setTitle("Table Ready");
        builder.setMessage("Is customer table ready?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setTableReady();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();

    }

    private void openWaitTimeDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Waiting Time");
        builder.setCancelable(false);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input_contact_no, (ViewGroup) findViewById(android.R.id.content), false);
        final CustomEditText input = (CustomEditText) viewInflated.findViewById(R.id.input);
        input.setHint("Enter Time (in minutes)");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(viewInflated);
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String waiting_time = input.getText().toString();
                if (waiting_time.isEmpty()) {
                    input.setError("Enter Wait Time");
                } else {
                    Log.e(TAG, "onClick: waiting_time >> " + waiting_time.length());
                    setWaitTime(waiting_time + " mins");
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

    private void openCheckinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationDeailsActivity.this);
        builder.setTitle("Checked-in");
        builder.setMessage("Your customer is checked-in into restaurant?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setCheckIn();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void setCheckIn() {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.SETCHECKEDIN);
        jsonObject.addProperty("reserv_id", reservation_id);
        jsonObject.addProperty("checked_id", "1");
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        Log.e(TAG, "Request SET CHECK IN" + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_reservation_status_manage(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response CHECK IN >>  " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        getResrvData();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(ReservationDeailsActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTableReady() {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.SETTABLEREADY);
        jsonObject.addProperty("reserv_id", reservation_id);
        jsonObject.addProperty("table_ready", "1");
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        Log.e(TAG, "Request SET CHECK IN" + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_reservation_status_manage(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response CHECK IN >>  " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        getResrvData();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(ReservationDeailsActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTvSeatedBy(String seatedBy) {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.SETSEATEDBY);
        jsonObject.addProperty("reserv_id", reservation_id);
        jsonObject.addProperty("seated_by", seatedBy);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        Log.e(TAG, "Request SET CHECK IN" + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_reservation_status_manage(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response CHECK IN >>  " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        getResrvData();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(ReservationDeailsActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWaitTime(String wait_time) {
        final ProgressHUD progressHD = ProgressHUD.show(ReservationDeailsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.SETWAITTIME);
        jsonObject.addProperty("reserv_id", reservation_id);
        jsonObject.addProperty("waiting_time", wait_time);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ReservationDeailsActivity.this));
        jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferencesBuss.getUserId(ReservationDeailsActivity.this));
        Log.e(TAG, "Request SET WAIT TIME" + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_reservation_status_manage(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response WAIT TIME >>  " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        getResrvData();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray2 = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray2.getJSONObject(0);
                        Toast.makeText(ReservationDeailsActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ReservationDeailsActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
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
}
