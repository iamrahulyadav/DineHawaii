package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.TableLayoutGridAdapter;
import com.yberry.dinehawaii.Bussiness.model.TableLayoutData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.GridSpacingItemDecoration;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
import com.yberry.dinehawaii.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableLayoutActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TableLayoutActivity";
    private ImageView back;
    private int spacing = 10, spanCount = 3;
    private boolean includeEdge = true;
    private TableLayoutGridAdapter adapter;
    private RecyclerView recylcer_view;
    private ArrayList<TableLayoutData> list;
    private TableLayoutActivity context;
    private SwipeRefreshLayout refreshLayout;
    private BroadcastReceiver tickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: tickReceiver");
            getTableMetrixData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_layout);
        context = this;
        list = new ArrayList<>();
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getTableMetrixData();
            }
        });
        setToolbar();
        setRecyclerView();
        //setStaticData();
        getTableMetrixData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tickReceiver != null)
            unregisterReceiver(tickReceiver);
    }


    private void setStaticData() {
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"1\",\n" +
                "\t\"reservation_id\": \"1\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T1\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"2\",\n" +
                "\t\"reservation_id\": \"1\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T2\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"3\",\n" +
                "\t\"reservation_id\": \"1\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T3\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"4\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T4\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"5\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T5\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"4\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T4\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"5\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T5\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"3\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T3\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"4\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T4\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"3\",\n" +
                "\t\"reservation_id\": \"1\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T3\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"4\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T4\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        adapter.notifyDataSetChanged();
        if (list.size() <= 50)
            setStaticData();
    }

    private void setRecyclerView() {
        recylcer_view = (RecyclerView) findViewById(R.id.recycler_view);
        recylcer_view.setLayoutManager(new GridLayoutManager(context, spanCount));
        recylcer_view.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recylcer_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recylcer_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (list.get(position).getStatus().equalsIgnoreCase("Free")) {
                    Intent intent = new Intent(getApplicationContext(), BusReservationActivity.class);
                    intent.putExtra("table_id", list.get(position).getTableId());
                    intent.putExtra("party_size", list.get(position).getTableSize());
                    intent.putExtra("table_name", list.get(position).getTableName());
                    startActivity(intent);
                    /*new AlertDialog.Builder(context)
                            .setMessage("Do you want to book this table for currect date & time?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();*/

                } else {
                    Intent intent = new Intent(getApplicationContext(), ReservationDetailsActivity.class);
                    intent.putExtra("reservation_id", list.get(position).getReservationId());
                    intent.setAction("NOSHOW");
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
        adapter = new TableLayoutGridAdapter(context, context, list);
        recylcer_view.setAdapter(adapter);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Tables Layout");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onClick(View v) {
    }

    ProgressHUD progressHD = null;

    private void getTableMetrixData() {
        if (Util.isNetworkAvailable(context)) {
            if (!refreshLayout.isRefreshing()) {
                progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                    }
                });
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.GETTABLEMATRIX);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
            Log.e(TAG, "getTableMetrixData: Request >> " + jsonObject.toString());
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.table_layout_api(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        Log.e(TAG, "getTableMetrixData: Response >> " + response.body().toString());
                        String resp = response.body().toString();
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            list.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                TableLayoutData item = new Gson().fromJson(String.valueOf(jsonObject1), TableLayoutData.class);
                                list.add(item);
                                adapter.notifyDataSetChanged();

                            }
                        } else if (jsonObject.getString("status").equals("400")) {
                            Toast.makeText(context, jsonObject.getJSONArray("result").getJSONObject(0).getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
                        }
                        if (progressHD != null)
                            progressHD.dismiss();
                        refreshLayout.setRefreshing(false);
                    } catch (JSONException e) {
                        if (progressHD != null)
                            progressHD.dismiss();
                        refreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    } catch (Exception e) {
                        if (progressHD != null)
                            progressHD.dismiss();
                        refreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                    if (progressHD != null)
                        progressHD.dismiss();
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            refreshLayout.setRefreshing(false);
        }
    }
}
