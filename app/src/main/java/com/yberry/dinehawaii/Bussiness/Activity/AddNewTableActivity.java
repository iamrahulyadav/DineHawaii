package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.Model.TableData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.Bussiness.Adapter.DialogMultipleChoiceAdapter;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewTableActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomEditText tableno, tabledescp, tablecapacity, etMinutes, etSelectTables;
    private CustomTextView tvSelectService;
    private String service_id = "", table_id = "";
    private boolean combineUpdate = false, isUpdate = false;
    private String TAG = "AddNewTableActivity";
    private ArrayAdapter<CharSequence> reservTableAdapter;
    private RadioGroup rgChairType, rgCombine;
    private boolean combineAdd = false;
    private Spinner spinnerReserve;
    private LinearLayout llCombineTable, llCombineRadios, mainLinearLayout;
    private ArrayList<TableData> tableDataList;
    private TableData data;
    private String selectedTableIds;
    private AlertDialog.Builder dialog;
    private ArrayList<String> tempTableDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_table);
        tableDataList = new ArrayList<TableData>();
        tempTableDataList = new ArrayList<String>();
        init();
        setToolbar();
        if (getIntent().getAction() != null) {
            if (getIntent().getAction().equalsIgnoreCase("single_edit")) {
                isUpdate = true;
                combineUpdate = false;
                getEditData();
            } else if (getIntent().getAction().equalsIgnoreCase("combine_edit")) {
                isUpdate = true;
                combineUpdate = true;
                getEditCombineData();
                getCombineableTables();
            } else if (getIntent().getAction().equalsIgnoreCase("single_add")) {
                isUpdate = false;
                combineAdd = false;
            } else if (getIntent().getAction().equalsIgnoreCase("combine_add")) {
                isUpdate = false;
                combineAdd = true;
                llCombineTable.setVisibility(View.VISIBLE);
                llCombineRadios.setVisibility(View.GONE);
                tablecapacity.setHint("Select one or more table");
                tablecapacity.setFocusableInTouchMode(false);
                getCombineableTables();
            }
        }
    }

    private void getCombineableTables() {
        if (Util.isNetworkAvailable(AddNewTableActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(AddNewTableActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.GETCOMBINATIONTABLE);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(AddNewTableActivity.this));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(AddNewTableActivity.this));
            Log.e(TAG, "getCombineableTables: Request >> " + jsonObject);

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
                        Log.e(TAG, "getCombineableTables: Response >> " + s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("result");

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                TableData tableData = new TableData();
                                JSONObject object = jsonArray1.getJSONObject(i);
                                tableData.setTable_id(object.getString("id"));
                                tableData.setTable_name(object.getString("name"));
                                tableData.setTable_capacity(object.getString("capacity"));
                                tableDataList.add(tableData);
                            }

                            //to view old data
                            if (data != null) {
                                for (int i = 0; i < tableDataList.size(); i++) {
                                    String[] idArray = data.getTable_ids().split(",");
                                    for (int j = 0; j < idArray.length; j++) {
                                        if (idArray[j].equalsIgnoreCase(tableDataList.get(i).getTable_id())) {
                                            tableDataList.get(i).setSeleted(true);
                                            tempTableDataList.add(tableDataList.get(i).getTable_name());
                                        }
                                    }
                                }
                                etSelectTables.setText(TextUtils.join(", ", tempTableDataList));
                            }
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        }
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
        } else {
            Toast.makeText(AddNewTableActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void getEditData() {
        TableData data = (TableData) getIntent().getSerializableExtra("table_data");
        Log.e(TAG, "getEditData: data >> " + data);
        tableno.setText(data.getTable_name());
        tabledescp.setText(data.getTable_descp());
        service_id = data.getService_id();
        tablecapacity.setText(data.getTable_capacity());
        table_id = data.getTable_id();
        tvSelectService.setText(data.getService_name());
        etMinutes.setText(data.getAlot_min());
        if (data.getCombineStatus().equalsIgnoreCase("1"))
            rgCombine.check(R.id.rbCombineYes);
        else
            rgCombine.check(R.id.rbCombineNo);
        if (data.getIsHandicapped().equalsIgnoreCase("1"))
            rgChairType.check(R.id.rbChairHandicapped);
        else
            rgChairType.check(R.id.rbChairTypeNormal);

        for (int i = 0; i < spinnerReserve.getCount(); i++) {
            if (((String) spinnerReserve.getItemAtPosition(i)).equalsIgnoreCase(data.getReserv_priority())) {
                spinnerReserve.setSelection(i);
                break;
            }
        }
    }

    private void getEditCombineData() {
        data = (TableData) getIntent().getSerializableExtra("table_data");
        Log.e(TAG, "getEditCombineData: data >> " + data);
        tableno.setText(data.getTable_name());
        tabledescp.setText(data.getTable_descp());
        service_id = data.getService_id();
        tablecapacity.setText(data.getTable_capacity());
        table_id = data.getTable_id();
        tvSelectService.setText(data.getService_name());
        etMinutes.setText(data.getAlot_min());


        if (data.getIsHandicapped().equalsIgnoreCase("1"))
            rgChairType.check(R.id.rbChairHandicapped);
        else
            rgChairType.check(R.id.rbChairTypeNormal);

        for (int i = 0; i < spinnerReserve.getCount(); i++) {
            if (((String) spinnerReserve.getItemAtPosition(i)).equalsIgnoreCase(data.getReserv_priority())) {
                spinnerReserve.setSelection(i);
                break;
            }
        }


        llCombineTable.setVisibility(View.VISIBLE);
        llCombineRadios.setVisibility(View.GONE);
        tablecapacity.setHint("Select tables to get capacity");
//        tablecapacity.setEnabled(false);
        tablecapacity.setFocusableInTouchMode(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addfood:
                if (TextUtils.isEmpty(tableno.getText().toString())) {
                    tableno.setError("Enter table name");
                } else if (TextUtils.isEmpty(tablecapacity.getText().toString())) {
                    tablecapacity.setError("Enter table capacity");
                } else if (TextUtils.isEmpty(service_id)) {
                    Toast.makeText(this, "Select service type", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etMinutes.getText().toString())) {
                    Toast.makeText(this, "Enter time in minutes", Toast.LENGTH_SHORT).show();
                } else if (isUpdate == false) {
                    if (combineAdd == true) {
                        if (!TextUtils.isEmpty(etSelectTables.getText().toString()))
                            addCombineTable();
                        else
                            Toast.makeText(this, "Please select tables", Toast.LENGTH_SHORT).show();
                    } else
                        addSingleTable();
                } else {
                    if (combineUpdate == false)
                        updateApi();
                }

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateApi() {
        if (Util.isNetworkAvailable(AddNewTableActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(AddNewTableActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.EDIT_TABLE_DETAIL);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(AddNewTableActivity.this));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(AddNewTableActivity.this));
            jsonObject.addProperty("table_name", tableno.getText().toString());
            jsonObject.addProperty("table_id", table_id);
            jsonObject.addProperty("capacity", tablecapacity.getText().toString());
            jsonObject.addProperty("service_type_id", service_id.replace("[", "").replace("]", ""));
            jsonObject.addProperty("reser_priority", (String) spinnerReserve.getSelectedItem());
            jsonObject.addProperty("alot_hours", "0");
            jsonObject.addProperty("alot_minute", etMinutes.getText().toString());
            jsonObject.addProperty("description", (String) tabledescp.getText().toString());
            if (rgCombine.getCheckedRadioButtonId() == R.id.rbCombineYes)
                jsonObject.addProperty("combineStatus", "1");
            else
                jsonObject.addProperty("combineStatus", "0");
            if (rgChairType.getCheckedRadioButtonId() == R.id.rbChairHandicapped)
                jsonObject.addProperty("isHandicapped", "1");
            else
                jsonObject.addProperty("isHandicapped", "0");
            Log.e(TAG, "updateApi: Request >> " + jsonObject);

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.business_table_system_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "updateApi: Response>> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = jsonArray.getJSONObject(0);
                            Toast.makeText(getApplicationContext(), object.getString("msg"), Toast.LENGTH_LONG).show();
                            finish();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = jsonArray.getJSONObject(0);
                            Toast.makeText(getApplicationContext(), object.getString("msg"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(AddNewTableActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AddNewTableActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        tvSelectService = ((CustomTextView) findViewById(R.id.tvSelectService));
        tablecapacity = (CustomEditText) findViewById(R.id.edtablecapacity);
        tvSelectService.setOnClickListener(this);
        tableno = (CustomEditText) findViewById(R.id.edtableno);
        tabledescp = (CustomEditText) findViewById(R.id.edtabledesc);
        spinnerReserve = (Spinner) findViewById(R.id.reserveSpinner);
        etMinutes = (CustomEditText) findViewById(R.id.etMinutes);
        etSelectTables = (CustomEditText) findViewById(R.id.etSelectTables);
        mainLinearLayout = (LinearLayout) findViewById(R.id.mainViewTable);
        rgChairType = (RadioGroup) findViewById(R.id.rgChairType);
        rgCombine = (RadioGroup) findViewById(R.id.rgCombine);
        llCombineTable = (LinearLayout) findViewById(R.id.llCombineTable);
        llCombineRadios = (LinearLayout) findViewById(R.id.llCombineRadios);

        mainLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view = AddNewTableActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) AddNewTableActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        etSelectTables.setOnClickListener(this);
        setSpinnerAdapter();
    }

    private void addSingleTable() {
        if (Util.isNetworkAvailable(AddNewTableActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(AddNewTableActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_TABLE_NEW);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(AddNewTableActivity.this));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(AddNewTableActivity.this));
            jsonObject.addProperty("table_name", tableno.getText().toString());
            jsonObject.addProperty("capacity", tablecapacity.getText().toString());
            jsonObject.addProperty("service_type_id", service_id.replace("[", "").replace("]", ""));
            jsonObject.addProperty("reser_priority", (String) spinnerReserve.getSelectedItem());
            jsonObject.addProperty("alot_hours", "0");
            jsonObject.addProperty("alot_minute", etMinutes.getText().toString());
            jsonObject.addProperty("description", tabledescp.getText().toString());
            if (rgCombine.getCheckedRadioButtonId() == R.id.rbCombineYes)
                jsonObject.addProperty("combineStatus", "1");
            else
                jsonObject.addProperty("combineStatus", "0");
            if (rgChairType.getCheckedRadioButtonId() == R.id.rbChairHandicapped)
                jsonObject.addProperty("isHandicapped", "1");
            else
                jsonObject.addProperty("isHandicapped", "0");

            Log.e(TAG, "addSingleTable: Request >> " + jsonObject);

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.business_table_system_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "addSingleTable: Response>> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            Toast.makeText(AddNewTableActivity.this, "Table Added", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            String msg = jsonObject.getJSONObject("result").getString("msg");
                            Toast.makeText(AddNewTableActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddNewTableActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(AddNewTableActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void addCombineTable() {
        if (Util.isNetworkAvailable(AddNewTableActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(AddNewTableActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_TABLE_COMBINATION);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(AddNewTableActivity.this));
            jsonObject.addProperty("table_id", selectedTableIds);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(AddNewTableActivity.this));
            jsonObject.addProperty("table_name", tableno.getText().toString());
            jsonObject.addProperty("capacity", tablecapacity.getText().toString());
            jsonObject.addProperty("service_type_id", service_id.replace("[", "").replace("]", ""));
            jsonObject.addProperty("reser_priority", (String) spinnerReserve.getSelectedItem());
            jsonObject.addProperty("alot_hours", "0");
            jsonObject.addProperty("alot_minute", etMinutes.getText().toString());
            jsonObject.addProperty("description", tabledescp.getText().toString());
            if (rgChairType.getCheckedRadioButtonId() == R.id.rbChairHandicapped)
                jsonObject.addProperty("isHandicapped", "1");
            else
                jsonObject.addProperty("isHandicapped", "0");

            Log.e(TAG, "addCombineTable: Request >> " + jsonObject);

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.business_table_system_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "addCombineTable: Response>> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            Toast.makeText(AddNewTableActivity.this, "Table Added", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            String msg = jsonObject.getJSONObject("result").getString("msg");
                            Toast.makeText(AddNewTableActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddNewTableActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(AddNewTableActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void setSpinnerAdapter() {
        reservTableAdapter = ArrayAdapter.createFromResource(this, R.array.ReserveTable, R.layout.single_spinner_item);
        spinnerReserve.setAdapter(reservTableAdapter);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Add Table");
        if (getIntent().getAction() != null)
            if (getIntent().getAction().equalsIgnoreCase("edit"))
                ((TextView) findViewById(R.id.headet_text)).setText("Edit Table");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelectService:
                selectServiceData();
                break;
            case R.id.etSelectTables:
                openTableDialog();
                break;
        }
    }

    private void openTableDialog() {
        if (tableDataList != null && !tableDataList.isEmpty()) {
            final DialogMultipleChoiceAdapter adapter = new DialogMultipleChoiceAdapter(AddNewTableActivity.this, tableDataList);
            new AlertDialog.Builder(AddNewTableActivity.this).setTitle("Select Tables")
                    .setAdapter(adapter, null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedTableIds = TextUtils.join(",", adapter.getSelectedItemIds());
                            String selectedTableNames = TextUtils.join(", ", adapter.getSelectedItemNames());
                            int totalCapacity = adapter.getTotalCapacity();
                            Log.e(TAG, "onClick: selectedTableIds >> " + selectedTableIds);
                            Log.e(TAG, "onClick: selectedTableNames >> " + selectedTableNames);
                            Log.e(TAG, "onClick: totalCapacity >> " + totalCapacity);
                            etSelectTables.setText(selectedTableNames);
                            tablecapacity.setText(String.valueOf(totalCapacity));
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
        } else {
            Toast.makeText(this, "No tables found", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectServiceData() {
        if (Util.isNetworkAvailable(AddNewTableActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLSERVICES);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(AddNewTableActivity.this));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(AddNewTableActivity.this));
            Log.e(TAG, "Request GET ALL SERVICES >> " + jsonObject.toString());
            selectServiceApi(jsonObject);
        } else {
            Toast.makeText(AddNewTableActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void selectServiceApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(AddNewTableActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET ALL SERVICES >> " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(AddNewTableActivity.this, SelectServiceTypeActivity.class);
                        intent.putExtra("ServerType", s);
                        startActivityForResult(intent, 101);
                    }else{
                        Toast.makeText(AddNewTableActivity.this, "No services available please import services", Toast.LENGTH_LONG).show();
                    }
                    //  adapter.notifyDataSetChanged();
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
                Toast.makeText(AddNewTableActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("serviceFoodType");
                tvSelectService.setText(checkBoxPositionModel.getName());
                service_id = checkBoxPositionModel.getId();
            }
        }
    }

}
