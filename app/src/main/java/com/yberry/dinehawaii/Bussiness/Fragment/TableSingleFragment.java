package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.AddNewTableActivity;
import com.yberry.dinehawaii.Bussiness.Adapter.ManageTableAdapter;
import com.yberry.dinehawaii.Model.TableData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TableSingleFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TableSingleFragment";
    public static ArrayList<TableData> tableList = new ArrayList<TableData>();
    Context context;
    CustomTextView notable;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ManageTableAdapter tableAdapter;
    private CustomTextView addNewTable;
    private TextView tvTimePickup;
    private TextView tvTimeDropoff;
    private TextView tvDatePickup;
    private TextView tvDateDropoff;
    private DatePickerDialog datePickerPickup;
    private DatePickerDialog datePickerDropoff;
    private boolean isPickupDate = false;

    public TableSingleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_table_fragment, container, false);
        context = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_viewtable);
        notable = (CustomTextView) view.findViewById(R.id.notable);
        addNewTable = (CustomTextView) view.findViewById(R.id.addNewTable);
        addNewTable.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(tableAdapter);
        tableAdapter = new ManageTableAdapter(context, tableList, "single", new ManageTableAdapter.ManageTableAdapterListener() {
            @Override
            public void blockTable(View view, int position, boolean block_status) {
                if (block_status)
                    dialogSelectDateTime(position);
                else {
                    //unblock api
                }
            }
        });
        mRecyclerView.setAdapter(tableAdapter);
        tableAdapter.notifyDataSetChanged();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (tableList != null)
            tableList.clear();
        getAllTables();
    }

    private void getAllTables() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.GET_BUSINESS_TABLE);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG, "tables Request json :- " + jsonObject.toString());
            getTablesApi(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getTablesApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_business_table(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseTable", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        notable.setVisibility(View.GONE);
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            TableData tableData = new TableData();
                            JSONObject object = jsonArray1.getJSONObject(i);
                            tableData.setTable_id(object.getString("id"));
                            tableData.setTable_name(object.getString("name"));
                            tableData.setTable_capacity(object.getString("capacity"));
                            tableData.setTable_descp(object.getString("description"));
                            tableData.setReserv_priority(object.getString("reser_priority"));
                            tableData.setService_id(object.getString("service_id"));
                            tableData.setService_name(object.getString("service_name"));
                            tableData.setAlot_hours(object.getString("alot_hours"));
                            tableData.setAlot_min(object.getString("alot_minute"));
                            tableData.setIsHandicapped(object.getString("isHandicapped"));
                            tableData.setCombineStatus(object.getString("combineStatus"));
                            tableData.setTable_type("single");
                            tableList.add(tableData);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        notable.setVisibility(View.VISIBLE);
                    }
                    progressHD.dismiss();
                    tableAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    progressHD.dismiss();
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                notable.setVisibility(View.VISIBLE);
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewTable:
                openDialog();
                break;
            default:
                break;
        }
    }

    private void openDialog() {
        final String grpname[] = {"Add Single Table", "Add Combined Table"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
        alt_bld.setTitle("Select Table Type");
        alt_bld.setSingleChoiceItems(grpname, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (item == 0) {
                    startActivity(new Intent(getActivity(), AddNewTableActivity.class).setAction("single_add"));
                } else if (item == 1) {
                    startActivity(new Intent(getActivity(), AddNewTableActivity.class).setAction("combine_add"));
                }
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void dialogSelectDateTime(final int position) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Block Schedule");
        dialog.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.input_date_time_layout, null);
        dialog.setView(dialogView);
        tvTimePickup = (TextView) dialogView.findViewById(R.id.tvTimePickup);
        tvTimeDropoff = (TextView) dialogView.findViewById(R.id.tvTimeDropoff);
        tvDatePickup = (TextView) dialogView.findViewById(R.id.tvDatePickup);
        tvDateDropoff = (TextView) dialogView.findViewById(R.id.tvDateDropoff);

        dialog.setPositiveButton("Block", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(tvDatePickup.getText().toString()) || TextUtils.isEmpty(tvDatePickup.getText().toString())
                        || TextUtils.isEmpty(tvDatePickup.getText().toString()) || TextUtils.isEmpty(tvDatePickup.getText().toString())) {
                    Toast.makeText(context, "Select Date & Time", Toast.LENGTH_SHORT).show();
                    dialogSelectDateTime(position);
                } else {
                    blockApi(tvDatePickup.getText().toString(),
                            tvTimePickup.getText().toString(),
                            tvDateDropoff.getText().toString(),
                            tvTimeDropoff.getText().toString(), position);
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final Calendar mcurrentTime = Calendar.getInstance();

        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON1).setEnabled(false);

        datePickerPickup = new DatePickerDialog(context, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDropoff = new DatePickerDialog(context, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        tvDatePickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickupDate = true;
                datePickerPickup.show();
            }
        });


        tvTimePickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tvDatePickup.getText().toString())) {
                    tvDatePickup.setError("Select Date");
                } else {
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hours, int mins) {
                            String timeSet = "";
                            if (hours > 12) {
                                hours -= 12;
                                timeSet = "PM";
                            } else if (hours == 0) {
                                hours += 12;
                                timeSet = "AM";
                            } else if (hours == 12)
                                timeSet = "PM";
                            else
                                timeSet = "AM";
                            String minutes = "";
                            if (mins < 10)
                                minutes = "0" + mins;
                            else
                                minutes = String.valueOf(mins);
                            tvTimePickup.setText(hours + ":" + minutes + " " + timeSet);

                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Pickup Time");
                    mTimePicker.show();
                }
            }
        });

        tvDateDropoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tvTimePickup.getText().toString())) {
                    tvTimePickup.setError("Select Time");
                } else {
                    isPickupDate = false;
                    datePickerDropoff.show();
                }
            }
        });

        tvTimeDropoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tvDateDropoff.getText().toString())) {
                    tvDateDropoff.setError("Select Date");
                } else {
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hours, int mins) {
                            String timeSet = "";
                            if (hours > 12) {
                                hours -= 12;
                                timeSet = "PM";
                            } else if (hours == 0) {
                                hours += 12;
                                timeSet = "AM";
                            } else if (hours == 12)
                                timeSet = "PM";
                            else
                                timeSet = "AM";
                            String minutes = "";
                            if (mins < 10)
                                minutes = "0" + mins;
                            else
                                minutes = String.valueOf(mins);
                            tvTimeDropoff.setText(hours + ":" + minutes + " " + timeSet);
                            alertDialog.getButton(AlertDialog.BUTTON1).setEnabled(true);
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Drop-off Time");
                    mTimePicker.show();
                }
            }
        });
    }

    private void blockApi(String start_date, String start_time, String end_date, String end_time, int position) {
        if (Util.isNetworkAvailable(getActivity())) {
            final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.BLOCKTABLES);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("table_id", tableList.get(position).getTable_id());
            jsonObject.addProperty("start_date", start_date);
            jsonObject.addProperty("start_time", start_time);
            jsonObject.addProperty("end_date", end_date);
            jsonObject.addProperty("end_time", end_time);
            Log.e(TAG, "blockApi: Request >> " + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.get_business_table(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "blockApi: Response >> " + response.body().toString());
                    String s = response.body().toString();

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            Toast.makeText(context, "Blocked", Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            Toast.makeText(context, "Failed, try again.", Toast.LENGTH_SHORT).show();
                        }
                        progressHD.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressHD.dismiss();
                        Toast.makeText(context, "Failed, try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                    progressHD.dismiss();
                    Toast.makeText(context, "Failed, try again.", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (isPickupDate) {
                tvDatePickup.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                datePickerDropoff.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
            } else {
                tvDateDropoff.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }
        }
    };

}
