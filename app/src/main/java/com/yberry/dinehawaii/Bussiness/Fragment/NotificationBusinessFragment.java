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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.NotificationBusinessAdapter;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hvantage2 on 8/2/2017.
 */

public class NotificationBusinessFragment extends Fragment {
    private static final String TAG = "All notification";
    public static List<CustomerModel> list = new ArrayList<>();
    Context context;
    NotificationBusinessAdapter adapter;
    private RecyclerView mRecyclerView;
    private TextView noNotification;
    private LinearLayoutManager mLayoutManager;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        getAllNotifications();
        init();
        return rootView;
    }


    private void init() {
        context = getActivity();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        noNotification = (TextView) rootView.findViewById(R.id.noNotifications);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new NotificationBusinessAdapter(context, list);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getAllNotifications() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ALL_NOTIFICATION_BUSS);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));//13
            Log.e(TAG, "notificationBus :- " + jsonObject.toString());
            showNotifications(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void showNotifications(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "notificationResp :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                CustomerModel customerModel = new CustomerModel();
                                JSONObject object = jsonArray.getJSONObject(i);
                                customerModel.setNotification_msg(object.getString("message"));
                                customerModel.setNotification_type(object.getString("notification_type"));
                                customerModel.setNotification_time(object.getString("time"));
                                customerModel.setNotification_image(object.getString("image"));

                                list.add(customerModel);


                            }


                        }
                    } else {
                        noNotification.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
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
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
