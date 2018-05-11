package com.yberry.dinehawaii.Bussiness.Fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.yberry.dinehawaii.Bussiness.Adapter.PreviousResListAdapter;
import com.yberry.dinehawaii.Bussiness.Adapter.ReservationListAdapter;
import com.yberry.dinehawaii.Model.ReservationDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResvPreviousFragment extends Fragment {
    private static final String TAG = "Previous_Fragment";
    public ArrayList<ReservationDetails> reservList = new ArrayList<ReservationDetails>();
    PreviousResListAdapter adapter;
    private FloatingActionButton imgContinue;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomTextView tvNoOrders;
    private SwipeRefreshLayout refreshLayout;

    public ResvPreviousFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        init(view);
        getPreviousReservation();
        return view;
    }

    private void init(View view) {
        tvNoOrders = (CustomTextView) view.findViewById(R.id.tvNoOrders);
        imgContinue = (FloatingActionButton) view.findViewById(R.id.imageview);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        imgContinue.setBackgroundResource(R.color.green);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new PreviousResListAdapter(getActivity(), reservList, "previous");
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getPreviousReservation();
            }
        });
    }

    private void getPreviousReservation() {
        if (Util.isNetworkAvailable(getActivity())) {
            new GetDataTask().execute();
        } else {
            Toast.makeText(getActivity(), "Please connect your internet", Toast.LENGTH_LONG).show();
        }
    }

    class GetDataTask extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvNoOrders.setVisibility(View.GONE);
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
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.PREVIOUS_RESERV);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG, "Request get Previous RESERVATION >> " + jsonObject.toString());
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response get Previous RESERVATION >> " + response.body().toString());
                    String s = response.body().toString();
                    reservList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray resultArray = jsonObject.getJSONArray("result");
                        Log.d("STATUS>>", jsonObject.getString("status"));
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject resultjsonObject = resultArray.getJSONObject(i);
                                Gson gson = new Gson();
                                ReservationDetails model = gson.fromJson(String.valueOf(resultjsonObject), ReservationDetails.class);
                                reservList.add(model);
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
                    }/* catch (ParseException e) {
                        e.printStackTrace();
                    }*/
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
