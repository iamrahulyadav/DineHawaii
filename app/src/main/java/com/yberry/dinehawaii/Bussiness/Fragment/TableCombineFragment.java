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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TableCombineFragment extends Fragment {
    private static final String TAG = "TableSingleFragment";
    public static ArrayList<TableData> tableList = new ArrayList<TableData>();
    Context context;
    CustomTextView notable;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ManageTableAdapter tableAdapter;

    public TableCombineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_table_fragment, container, false);
        context = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_viewtable);
        notable = (CustomTextView) view.findViewById(R.id.notable);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(tableAdapter);
        tableAdapter = new ManageTableAdapter(context, tableList);
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
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result_combine");
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
                            tableData.setTable_ids(object.getString("table_id"));
                            tableData.setTable_type("combine");
                            tableList.add(tableData);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        notable.setVisibility(View.VISIBLE);
                    }
                    progressHD.dismiss();
                    tableAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressHD.dismiss();
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
}
