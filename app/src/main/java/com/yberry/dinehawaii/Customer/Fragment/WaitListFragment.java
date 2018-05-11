package com.yberry.dinehawaii.Customer.Fragment;

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
import com.yberry.dinehawaii.Customer.Adapter.WaitlistAdapter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WaitListFragment extends Fragment {
    private static final String TAG = "Wait List";
    public static
    WaitlistAdapter adapter;
    public static List<CustomerModel> list = new ArrayList<>();
    Context context;
    CustomTextView nodata;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public WaitListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wait_list, container, false);
        initComponent();
        dataAvailableCoupon();
        return rootView;
    }


    private void initComponent() {
        context = getActivity();
        nodata = (CustomTextView) rootView.findViewById(R.id.nodata);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeWaitRes);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_coupon);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new WaitlistAdapter(context, list);
        mRecyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                dataAvailableCoupon();
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void dataAvailableCoupon() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.RESERVATION_WAITLIST);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));
            // jsonObject.addProperty("user_id", "8");// AppPreferencesBuss.getUserId(getActivity())
            Log.e(TAG, "Wait List request :- " + jsonObject.toString());
            getDataFromServerAvailableCoupon(jsonObject);

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromServerAvailableCoupon(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.wait(jsonObject);


        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Wait List Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CustomerModel customerModel = new CustomerModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            customerModel.setWaitlist_business_id(object.getString("business_id"));
                            customerModel.setWaitlist_business_name(object.getString("business_name"));
                            customerModel.setWaitlist_date(object.getString("date"));
                            customerModel.setWaitlist_id(object.getString("id"));
                            customerModel.setWaitlist_party_size(object.getString("party_size"));
                            customerModel.setWaitlist_reservation_id(object.getString("reservation_id"));
                            customerModel.setWaitlist_time(object.getString("time"));
                            customerModel.setWaitlist_service_type1(object.getString("service_type1"));
                            customerModel.setWaitlist_service_type2(object.getString("service_type2"));
                            list.add(customerModel);

                        }
                        adapter.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        progressHD.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        nodata.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    progressHD.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }
                progressHD.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
