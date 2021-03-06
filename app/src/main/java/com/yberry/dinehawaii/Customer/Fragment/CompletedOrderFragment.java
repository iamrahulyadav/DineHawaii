package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.OrderHistoryAdapter;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompletedOrderFragment extends Fragment {
    private static final String TAG = "CompletedOrderFragment";
    public static OrderHistoryAdapter adapter;
    public static List<CustomerModel> list = new ArrayList<>();
    Context context;
    private CustomTextView nodata;
    BroadcastReceiver favReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            repeatOrder();
            //Toast.makeText(context, "Marked as Favorite", Toast.LENGTH_SHORT).show();
        }
    };
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View rootView;

    public CompletedOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_completed, container, false);
        context = getActivity();
        initComponent();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(favReceiver, new IntentFilter("markedFav"));
        repeatOrder();
        return rootView;
    }


    private void initComponent() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_repeatOrder);
        nodata = (CustomTextView) rootView.findViewById(R.id.noData);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new OrderHistoryAdapter(context, "completed", list);
        mRecyclerView.setAdapter(adapter);
    }

    private void repeatOrder() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.COMPLETE_ORDERS);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));//8
            Log.e(TAG, "Request GET COMPLETED" + jsonObject.toString());
            getRepeatData(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(favReceiver);
    }

    private void getRepeatData(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.order_detail_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    Log.e(TAG, "Response GET COMPLETED >> " + response.body().toString());
                    String resp = response.body().toString();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CustomerModel customerModel = new CustomerModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            customerModel.setOrder_id(object.getString("order_id"));
                            customerModel.setTotal_price(object.getString("total_price"));
                            customerModel.setFood_name(object.getString("food_name"));
                            customerModel.setOrder_quantity(object.getString("quantity"));
                            customerModel.setOrder_ItemCustomization(object.getString("item_customization"));
                            customerModel.setOrder_date(object.getString("order_date"));
                            customerModel.setOrder(object.getString("order"));
                            customerModel.setCat_id(object.getString("cat_id"));
                            customerModel.setMenu_id(object.getString("menu_id"));
                            customerModel.setItem_customization(object.getString("item_customization"));
                            customerModel.setIteam_message(object.getString("iteam_message"));
                            customerModel.setItem_price(object.getString("item_price"));
                            customerModel.setBusiness_id(object.getString("business_id"));
                            customerModel.setAvgPrice(object.getString("avgPrice"));
                            customerModel.setId(object.getString("id"));
                            customerModel.setFav_status(object.getString("Favourite"));

                            customerModel.setBus_lattitude(object.getString("latitude"));
                            customerModel.setBus_longitude(object.getString("longitude"));
                            customerModel.setBus_package(object.getString("business_package"));
                            customerModel.setOrder_status(object.getString("order_status"));
                            Log.d("BUSINESSID", object.getString("business_id"));
                            if (i >= 20) {
                                break;
                            }
                            list.add(customerModel);
                            Log.v(TAG, "Inside Complete Fragment!!!!");
                            Log.d("FOOD NMAE:", object.getString("food_name"));
                        }
                        adapter.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        nodata.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    progressHD.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    list.clear();
                    adapter.notifyDataSetChanged();
                    progressHD.dismiss();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                list.clear();
                adapter.notifyDataSetChanged();
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
