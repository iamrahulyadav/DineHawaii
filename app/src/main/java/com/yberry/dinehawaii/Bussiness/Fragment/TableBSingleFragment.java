package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
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
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TableBSingleFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TableSingleFragment";
    Context context;
    CustomTextView notable;
    private RecyclerView mRecyclerView;
    private ManageTableAdapter tableAdapter;
    private CustomTextView addNewTable;
    public ArrayList<TableData> tableList;
    private View view;

    public TableBSingleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.single_table_fragment, container, false);
        context = getActivity();
        tableList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_viewtable);
        notable = (CustomTextView) view.findViewById(R.id.notable);
        addNewTable = (CustomTextView) view.findViewById(R.id.addNewTable);
        addNewTable.setOnClickListener(this);
        addNewTable.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(tableAdapter);
        tableAdapter = new ManageTableAdapter(context, tableList, "blocked", new ManageTableAdapter.ManageTableAdapterListener() {
            @Override
            public void blockTable(View view, int position, boolean block_status) {
                if (!block_status) {
                    unblockApi(tableList.get(position).getTable_id(), "n");
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
        getBlockedSingleTables();
    }

    private void getBlockedSingleTables() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.GETBLOCKEDSINGLETABLES);
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

                try {
                    Log.e(TAG + "onResponseTable", response.body().toString());
                    String s = response.body().toString();
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        notable.setVisibility(View.GONE);
//                        tableList = new ArrayList<>();
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
                            tableAdapter.notifyDataSetChanged();
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        notable.setVisibility(View.VISIBLE);
                        tableAdapter.notifyDataSetChanged();
                        Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    progressHD.dismiss();
                    e.printStackTrace();
                    tableAdapter.notifyDataSetChanged();
                    Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
                }
                progressHD.dismiss();

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                notable.setVisibility(View.VISIBLE);
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
                tableAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
    }

//    private void getBlockedSingleTables() {
//        if (Util.isNetworkAvailable(getActivity())) {
//            final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    // TODO Auto-generated method stub
//                }
//            });
//
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.GETBLOCKEDSINGLETABLES);
//            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
//            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
//            Log.e(TAG, "getBlockedSingleTables: Request >> " + jsonObject.toString());
//
//            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
//            Call<JsonObject> call = apiService.get_business_table(jsonObject);
//            call.enqueue(new Callback<JsonObject>() {
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    Log.e(TAG, "getBlockedSingleTables: Response >> " + response.body().toString());
//                    String s = response.body().toString();
//                    try {
//                        JSONObject jsonObject = new JSONObject(s);
//                        blockedList = new ArrayList<>();
//                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
//                            notable.setVisibility(View.GONE);
//                            JSONArray jsonArray1 = jsonObject.getJSONArray("result");
//                            for (int i = 0; i < jsonArray1.length(); i++) {
//                                TableData tableData = new TableData();
//                                JSONObject object = jsonArray1.getJSONObject(i);
//                                tableData.setTable_id(object.getString("id"));
//                                tableData.setTable_name(object.getString("name"));
//                                tableData.setTable_capacity(object.getString("capacity"));
//                                tableData.setTable_descp(object.getString("description"));
//                                tableData.setReserv_priority(object.getString("reser_priority"));
//                                tableData.setService_id(object.getString("service_id"));
//                                tableData.setService_name(object.getString("service_name"));
//                                tableData.setAlot_hours(object.getString("alot_hours"));
//                                tableData.setAlot_min(object.getString("alot_minute"));
//                                tableData.setIsHandicapped(object.getString("isHandicapped"));
//                                tableData.setTable_type(object.getString("table_type"));
////                                tableData.setTable_type("single");
//                                blockedList.add(tableData);
//                                tableAdapter.notifyDataSetChanged();
//                            }
//                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
//                            notable.setVisibility(View.VISIBLE);
//                        }
//                        Log.e(TAG, "onResponse: blockedList.size >> " + blockedList.size());
//                        progressHD.dismiss();
//                    } catch (JSONException e) {
//                        progressHD.dismiss();
//                        e.printStackTrace();
//                    }
//                }
//
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    notable.setVisibility(View.VISIBLE);
//                    Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
//                    progressHD.dismiss();
//                    Toast.makeText(context, "Failed, try again.", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } else {
//            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
//        }
//    }

    private void unblockApi(String table_id, String table_type) {
        if (Util.isNetworkAvailable(getActivity())) {
            final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.UNBLOCKTABLES);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("table_id", table_id);
            jsonObject.addProperty("table_type", table_type);
            Log.e(TAG, "unblockApi: Request >> " + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.get_business_table(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "unblockApi: Response >> " + response.body().toString());
                    String s = response.body().toString();

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            Toast.makeText(context, "Unblocked", Toast.LENGTH_SHORT).show();
                            getBlockedSingleTables();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            Toast.makeText(context, "Failed, try again.", Toast.LENGTH_SHORT).show();
                        }
                        progressHD.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressHD.dismiss();
                        Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                    progressHD.dismiss();
                    Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

}
