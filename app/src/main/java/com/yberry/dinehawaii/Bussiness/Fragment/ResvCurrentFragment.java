package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.ReservationListAdapter;
import com.yberry.dinehawaii.Model.ReservationDetails;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PRINCE 9977123453 on 31-12-16.
 */
public class ResvCurrentFragment extends Fragment {
    private static final String TAG = "ResvCurrentFragment";
    private static final long REFRESH_INTERVAL = 1 * 1000 * 60;//1/2 min
    public ArrayList<ReservationDetails> reservList = new ArrayList<ReservationDetails>();
    ReservationListAdapter adapter;
    View rootView;
    private FloatingActionButton imgContinue;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomTextView tvNoOrders;
    private SwipeRefreshLayout refreshLayout;
    private Handler handler;
    private BroadcastReceiver tickReceiver;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recycler_view, container, false);
        init();
        return rootView;
    }

    private void init() {
        tvNoOrders = (CustomTextView) rootView.findViewById(R.id.tvNoOrders);
        imgContinue = (FloatingActionButton) rootView.findViewById(R.id.imageview);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        imgContinue.setBackgroundResource(R.color.green);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new ReservationListAdapter(getActivity(), reservList, "today");
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        adapter.notifyDataSetChanged();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getCurrentReservation();
            }
        });

        getCurrentReservation();
        tickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    getCurrentReservation();
                }
            }
        };
        //Register the broadcast receiver to receive TIME_TICK
        getActivity().registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    private void getCurrentReservation() {
        if (Util.isNetworkAvailable(getActivity())) {
            new GetDataTask().execute();
        } else {
            Toast.makeText(getActivity(), "Please connect your internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tickReceiver == null) {
            getActivity().registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tickReceiver == null) {
//            getActivity().registerReceiver(tickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        } else {
            getActivity().unregisterReceiver(tickReceiver);
            tickReceiver = null;
        }
    }

    class GetDataTask extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvNoOrders.setVisibility(View.GONE);
            mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
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
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.CUURENT_RESERVATION_LIST);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG, "Request GET CURRENT RESERVATION >> " + jsonObject.toString());
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    try {
                        Log.e(TAG, "Response GET CURRENT RESERVATION >> " + response.body().toString());
                        String s = response.body().toString();
                        reservList.clear();
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray resultArray = jsonObject.getJSONArray("result");
                        Log.d("STATUS>>", jsonObject.getString("status"));
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject resultjsonObject = resultArray.getJSONObject(i);
                                Gson gson = new Gson();
                                ReservationDetails model = gson.fromJson(String.valueOf(resultjsonObject), ReservationDetails.class);
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
                                SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                                String currentTime = parseFormat.format(c.getTime());
                                String tempRDate = model.getTime();
                                Date reserTime24hr = parseFormat.parse(tempRDate);
                                Date currentTime24hr = parseFormat.parse(currentTime);
                                Log.e(TAG, "reserTime24hr >> " + displayFormat.format(reserTime24hr));
                                Log.e(TAG, "currentTime24 >> " + displayFormat.format(currentTime24hr));

                                long reserTime24hrAfter2Min = reserTime24hr.getTime();
                                Date afterAddingMins = new Date(reserTime24hrAfter2Min + (2 * 60000));
                                Log.e(TAG, "afterAddingMins >> " + displayFormat.format(afterAddingMins));

                                if (reserTime24hr.compareTo(currentTime24hr) < 0) {
                                    Log.e(TAG, "reserTime24hr is before currentTime24");
                                    if (!model.getReservationStatus().equalsIgnoreCase("Cancelled") && !model.getReservationStatus().equalsIgnoreCase("Closed") && !model.getReservationStatus().equalsIgnoreCase("Booked") && model.getSetCheckedIn() == 0)
                                        model.setBlinked(true);
                                    else
                                        model.setBlinked(false);
                                }
                                reservList.add(model);
                            }
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            publishProgress(400, "No Records available");
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
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
//            refreshLayout.setRefreshing(false);
            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            if (i == 400) {
                tvNoOrders.setVisibility(View.VISIBLE);
            }
        }
    }


}