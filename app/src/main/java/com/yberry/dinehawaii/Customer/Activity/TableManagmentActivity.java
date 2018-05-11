package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.yberry.dinehawaii.Util.Util;

import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hvantage2 on 7/14/2017.
 */

public class  TableManagmentActivity extends AppCompatActivity implements View.OnClickListener {
    public static TableAdapter adapter;
    public static final String TAG = "TAG";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    CustomEditText customerName, dineEmail, etime, partySize, esmartPhno, edate, smartPhno, table_no;
    String order_id, id, name, time, partysize, date, phone, table_name, e_name, e_id, e_time, e_date, e_table_no, e_cell, e_party, table_id;
    Dialog dialog, dialog1;
    ArrayList<TableData> tableDataList = new ArrayList<TableData>();
    String selectedTableID = "";
    String business_id, pre_amount;
    Button btn_BookTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_table_managment);
        setToolbar();
        checkPackage();
        getData();
        initComponent();
    }

    private void checkPackage() {
        table_no = (CustomEditText) findViewById(R.id.table_no);
        Log.e("Activity", "Making Registration");
        String package_list = AppPreferences.getBussPackageList(TableManagmentActivity.this);
        Log.e("Sel-Business Package", package_list);
        if (package_list.equalsIgnoreCase("1")) table_no.setVisibility(View.GONE);
        else if (package_list.equalsIgnoreCase("1, 3")) table_no.setVisibility(View.GONE);
        else if (package_list.equalsIgnoreCase("1, 2")) table_no.setVisibility(View.VISIBLE);
        else if (package_list.equalsIgnoreCase("1, 2, 3")) table_no.setVisibility(View.VISIBLE);
        else if (package_list.equalsIgnoreCase("1, 2, 3, 4"))
            table_no.setVisibility(View.VISIBLE);
    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((CustomTextView) findViewById(R.id.headet_text)).setText("Customer Details");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getData()
    {
        if (getIntent().getExtras() != null)
        {
            Bundle extras = getIntent().getExtras();
            order_id = extras.getString("order_id");
            id = extras.getString("id");
            name = extras.getString("name");
            time = extras.getString("time");
            partysize = extras.getString("partysize");
            date = extras.getString("date");
            phone = extras.getString("phone");
            table_name = extras.getString("tablename");
            business_id=extras.getString("business_id");
            pre_amount=extras.getString("pre_amount");

        } else {
            Toast.makeText(this, "Currently no reservation found", Toast.LENGTH_SHORT).show();
        }

    }


    private void initComponent() {
        customerName = (CustomEditText) findViewById(R.id.customerName);
        customerName.setText(name);
        e_name = customerName.getText().toString();
        dineEmail = (CustomEditText) findViewById(R.id.dineEmail);
        dineEmail.setText(id);


        etime = (CustomEditText) findViewById(R.id.time);
        etime.setText(time);


        partySize = (CustomEditText) findViewById(R.id.partySize);
        partySize.setText(partysize);


        smartPhno = (CustomEditText) findViewById(R.id.smartPhno);
        smartPhno.setText(phone);

        edate = (CustomEditText) findViewById(R.id.date);
        edate.setText(date);

        table_no = (CustomEditText) findViewById(R.id.table_no);
        table_no.setText(table_name);


        ((TextView) findViewById(R.id.tvSubmit)).setOnClickListener(this);
        table_no.setOnClickListener(this);



    }

    private void updateInfoServer(JsonObject jsonObject) {
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response Making  :- " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject object = resultJsonArray.getJSONObject(i);
                            //String r_id = object.getString("id");
                            //Toast.makeText(TableManagmentActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);
                            String msg = object1.getString("msg");
                            Toast.makeText(TableManagmentActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
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

    private void updateInfo() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.UPDATE_INFO);
        jsonObject.addProperty("reservation_id", AppPreferences.getReservationId(TableManagmentActivity.this));
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(TableManagmentActivity.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(TableManagmentActivity.this));
        jsonObject.addProperty("date", edate.getText().toString());
        jsonObject.addProperty("time", etime.getText().toString());
        jsonObject.addProperty("party_size", partySize.getText().toString());
        jsonObject.addProperty("customer_name", customerName.getText().toString());
        jsonObject.addProperty("customer_hawaii_id", dineEmail.getText().toString());
        jsonObject.addProperty("customer_smartphone_no", smartPhno.getText().toString());
        jsonObject.addProperty("table_id", table_no.getText().toString());
        Log.d("check_reservation_id", AppPreferences.getReservationId(TableManagmentActivity.this));
        updateInfoServer(jsonObject);
    }


    private void customTableDialog(ArrayList<TableData> list) {

        dialog = new Dialog(TableManagmentActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_table);
        // dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TableAdapter adapter = new TableAdapter(dialog.getContext(), list);
        adapter.notifyDataSetChanged();
        final GridView gridView = (GridView) dialog.findViewById(R.id.gridAvailableTables);
        ImageView btnCancel = (ImageView) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        Log.e("table-list", list + "");

        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TableData table = tableDataList.get(i);
                table_id = table.getTable_id();
                selectedTableID = table_id;
                String table_name = table.getTable_name();
                table_no.setText(table_name);

                // table_no.setText("Booking Table : " + table.getTable_name());
                Log.d("tableId", table_id);
                gridView.setSelection(i);
                dialog.dismiss();
            }
        });
        dialog.show();
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

    private void getBusinessTables() {
        if (Util.isNetworkAvailable(TableManagmentActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_PARTY_SIZE_TABLE);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(TableManagmentActivity.this));
            jsonObject.addProperty("party_size", partySize.getText().toString().trim());
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(TableManagmentActivity.this));
            Log.e(TAG, "Request Get Tables:- " + jsonObject.toString());
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.get_party_size_tables(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response Get Tables:- " + response.body().toString());
                    String s = response.body().toString();

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        tableDataList.clear();
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                name = jsonObject1.getString("table_name");
                                TableData table = new TableData();
                                table.setTable_id(id);
                                table.setTable_name(name);
                                Log.e("Table Data:", "" + table.toString());

                                tableDataList.add(table);
                            }
                            Log.e("Table List:", "" + tableDataList);
                            customTableDialog(tableDataList);
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String msg = jsonObject1.getString("msg");
                            Log.e("msg", msg);
                            Toast.makeText(TableManagmentActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                }
            });
        } else {
            Toast.makeText(TableManagmentActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvSubmit)
        {
            updateInfo();
            Intent in = new Intent(TableManagmentActivity.this, CustomerConfirmreservationActivity.class);
            in.putExtra("business_id",business_id);
            in.putExtra("reservation_id",order_id);
            in.putExtra("reserve_amt",pre_amount);
            startActivity(in);
        } else if (v.getId() == R.id.table_no) {
            getBusinessTables();
        }

    }
}
