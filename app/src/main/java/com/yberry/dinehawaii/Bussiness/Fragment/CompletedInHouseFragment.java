package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import com.yberry.dinehawaii.Bussiness.Adapter.InHouseOrdersAdapter;
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

/**
 * Created by Hvantage2 on 2018-02-14.
 */

public class CompletedInHouseFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    InHouseOrdersAdapter adapter;
    Context context;
    public static ArrayList<OrderDetails> list = new ArrayList<OrderDetails>();
    private static final String TAG = "CloseToday's Order";
    private View rootView;
    CustomTextView tvNoOrders;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("pppp", "dsafga");
        //Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_today_in_house, container, false);
        initComponent();
        getOrders();
        return rootView;
    }

    private void initComponent() {
        context = getActivity();
        tvNoOrders = (CustomTextView) rootView.findViewById(R.id.tvNoOrders);
        adapter = new InHouseOrdersAdapter(context, list);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        adapter = new InHouseOrdersAdapter(context, list);
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
            new GetDataTask().execute();
        } else {
            Toast.makeText(getActivity(), "Please connect your internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrders();
    }

    class GetDataTask extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.INHOUSE_COMPLETE_ORDERS);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));// AppPreferencesBuss.getUserId(getActivity())
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));// AppPreferencesBuss.getUserId(getActivity())
            Log.e(TAG, "Request GET DELIVERY ORDER >> " + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.today_orders(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response GET DELIVERY ORDER >> " + response.body().toString());
                    String resp = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            list.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                OrderDetails orderModel = new OrderDetails();
                                JSONObject object = jsonArray.getJSONObject(i);
                                orderModel.setId(object.getString("id"));
                                orderModel.setOrderId(object.getString("order_id")) ;
                                orderModel.setDate(object.getString("date"));
                                orderModel.setBusinessName(object.getString("business_name")) ;
                                orderModel.setTotalPrice("$" + object.getString("total_price"));
                                orderModel.setDeliveryAdderess(object.getString("delivery_adderess"));
                                orderModel.setDeliveryName(object.getString("delivery_name"));
                                orderModel.setDeliveryContactNo(object.getString("delivery_contact_no"));
                                orderModel.setOrderStatus(object.getString("order_status"));
                                orderModel.setUserName(object.getString("user_name"));
                                orderModel.setFoodName(object.getString("food_name"));
                                orderModel.setQuantity(object.getString("quantity"));
                                orderModel.setOrderType(object.getString("order_type"));
                                orderModel.setCustomization(object.getString("customization"));
                                orderModel.setDescription(object.getString("inhouse_table"));
                                orderModel.setOrderAddedBy(object.getString("order_added_by"));
                                list.add(orderModel);
                            }
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            Log.e("status", jsonObject.getString("status"));
                            JSONObject jobject = jsonObject.getJSONObject("result");
                            String msg = jobject.getString("msg");
                            Log.e("msg", msg);
                            publishProgress(400, msg);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        publishProgress(400, "Some error occured");
                        e.printStackTrace();
                    }
                    publishProgress(0, "");
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                    publishProgress(400, "Some error occured");
                }
            });
            return null;
        }

        private void publishProgress(int i, String msg) {
            refreshLayout.setRefreshing(false);
            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            if (adapter != null) {
                if (adapter.getItemCount() == 0)
                    tvNoOrders.setVisibility(View.VISIBLE);
                else
                    tvNoOrders.setVisibility(View.GONE);
            }
        }
    }


}
