package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.TakeOutAdapter;
import com.yberry.dinehawaii.Bussiness.model.OrderDetails;
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


public class PendingTakeoutOrdersFragment extends Fragment {
    private static final String TAG = "Today orderTakeout";
    public static ArrayList<OrderDetails> list = new ArrayList<OrderDetails>();
    TakeOutAdapter adapter;
    Context context;
    CustomTextView tvNoOrders;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View rootView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_today_in_house, container, false);
        init();
        getOrders();
        return rootView;
    }


    private void init() {
        context = getActivity();
        tvNoOrders = (CustomTextView) rootView.findViewById(R.id.tvNoOrders);
        adapter = new TakeOutAdapter(context, list);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        adapter = new TakeOutAdapter(context, list);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrders();
            }
        });
    }

    private void getOrders() {
        if (Util.isNetworkAvailable(getActivity())) {
            tvNoOrders.setVisibility(View.GONE);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.TODAY_TAKEOUT_ORDERS);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity())); //1
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity())); //13
            Log.e(TAG, "Request GET TAKE OUT ORDERS" + jsonObject.toString());
            getDataTodatTakeoutOrders(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataTodatTakeoutOrders(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.today_orders(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET TAKE OUT ORDERS >> " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            OrderDetails orderModel = new OrderDetails();
                            JSONObject object = jsonArray.getJSONObject(i);
                            String date = object.getString("date");
                            String duetime = object.getString("due_time");
                            orderModel.setDate(date);
                            //orderModel.setOrder_time(date);
                            orderModel.setDue_time(duetime);
                            orderModel.setId(object.getString("id"));
                            orderModel.setUser_name(object.getString("user_name"));
                            orderModel.setTotal_price("$" + object.getString("total_price"));
                            orderModel.setOrder_status(object.getString("order_status"));
                            orderModel.setDelivery_adderess(object.getString("delivery_adderess"));
                            orderModel.setOrder_id(object.getString("order_id"));
                            orderModel.setDelivery_contact_no(object.getString("delivery_contact_no"));
                            orderModel.setOrder_source(object.getString("order_added_by"));

                            list.add(orderModel);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONObject jobject = jsonObject.getJSONObject("result");
                        String msg = jobject.getString("msg");
                        Log.e("msgTakeout", msg);
                        tvNoOrders.setVisibility(View.VISIBLE);
                        refreshLayout.setRefreshing(false);
                        list.clear();
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressHD.dismiss();
                    tvNoOrders.setVisibility(View.VISIBLE);
                    refreshLayout.setRefreshing(false);
                }
                progressHD.dismiss();
                refreshLayout.setRefreshing(false);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                refreshLayout.setRefreshing(false);
                tvNoOrders.setVisibility(View.VISIBLE);
                progressHD.dismiss();
            }
        });
    }

}
