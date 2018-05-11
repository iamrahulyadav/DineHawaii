package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.CurrrentReservationAdapter;
import com.yberry.dinehawaii.Customer.Adapter.HistoryAdapter;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.Model.ReservationDataModel;
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

public class ReservationHistoryFragment extends Fragment {
    private static final String TAG = "Reservation History";
    public static
    HistoryAdapter adapter;
    private RecyclerView mRecyclerView;
    Context context;
    public static ArrayList<ReservationDataModel> list = new ArrayList<ReservationDataModel>();
    CustomTextView nodata;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private View rootView;
    public ReservationHistoryFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.history_reservation, container, false);
        initComponent();
/*
        list.clear();
        ReservationDataModel model= new ReservationDataModel();
        model.setReservation_id("101");
        model.setService_type1("service 1");
        model.setTime("11.20");
        model.setParty_size("2");
        model.setBusiness_name("Hawaii");
        model.setDate("12/12/2016");
        list.add(model);
*/

        adapter.notifyDataSetChanged();

        dataTodaysOrder();
        return rootView;







    }
    private void initComponent() {
        context = getActivity();
        nodata = (CustomTextView) rootView.findViewById(R.id.nodata);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeHistRes);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_coupon);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new HistoryAdapter(context, list);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                dataTodaysOrder();
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void dataTodaysOrder()
    {
        if (Util.isNetworkAvailable(getActivity()))
        {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_RESERVATION_HISTORY);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));// AppPreferencesBuss.getUserId(getActivity() )
            Log.d(TAG, "History reservation request :- " + jsonObject.toString());
            getDataFromServer(jsonObject);
        }
        else
        {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromServer(JsonObject jsonObject)
    {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.reservation_history(jsonObject);

        call.enqueue(new Callback<JsonObject>()
        {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response)
            {
                Log.d(TAG, "History reservation Response :- " + response.body().toString());
                String resp = response.body().toString();
                try
                {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            ReservationDataModel model= new ReservationDataModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            model.setId(object.getString("id"));
                            model.setReservation_id(object.getString("reservation_id"));
                            model.setBusiness_id(object.getString("business_id"));
                            model.setBusiness_name(object.getString("business_name"));
                            model.setParty_size(object.getString("party_size"));
                            model.setDate(object.getString("date"));
                            model.setTime(object.getString("time"));
                            model.setService_type1(object.getString("service_type1"));
                            model.setService_type2(object.getString("service_type2"));

                            list.add(model);
                        }
                    }
                    else if (jsonObject.getString("status").equalsIgnoreCase("400"))
                    {
                        progressHD.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        nodata.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
                progressHD.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t)
            {
                Log.d(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });




    }

}
