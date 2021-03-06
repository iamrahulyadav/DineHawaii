package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.yberry.dinehawaii.Util.Function;
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


public class ResvFutureFragment extends Fragment {
    private static final String TAG = "Future_Fragment";
    public ArrayList<ReservationDetails> reservList = new ArrayList<ReservationDetails>();
    LinearLayout spinnerLayout;
    Spinner spinnerDate;
    ArrayList<String> dateList = new ArrayList<>();
    ReservationListAdapter adapter;
    View rootView;
    FloatingActionButton imgContinue;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ReservationDetails reservationDetails;
    private CustomTextView tvNoOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recycler_view_future, container, false);
        init();
        setAdapterForDate(rootView);
        getFutureReservation();
        return rootView;
    }

    private void init() {
        tvNoOrders = (CustomTextView) rootView.findViewById(R.id.tvNoOrders);
        imgContinue = (FloatingActionButton) rootView.findViewById(R.id.imageview);
        imgContinue.setBackgroundResource(R.color.green);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new ReservationListAdapter(getActivity(), reservList, "future");
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getFutureReservation() {
        if (Util.isNetworkAvailable(getActivity())) {
            tvNoOrders.setVisibility(View.GONE);
            new GetDataTask().execute();
        } else {
            Toast.makeText(getActivity(), "Please connect your internet", Toast.LENGTH_LONG).show();
        }
    }

  /*  @Override
    public void onResume() {
        super.onResume();
        getFutureReservation();
    }*/

    private void setAdapterForDate(View rootView) {
        spinnerLayout = (LinearLayout) rootView.findViewById(R.id.spinnerLayout);
        spinnerLayout.setVisibility(View.VISIBLE);
        spinnerDate = (Spinner) rootView.findViewById(R.id.spinnerDate);
        dateList = Function.getFutureDate(3);
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, dateList);
        spinnerDate.setAdapter(dateAdapter);
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
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.FUTURE_RESERVATION_LIST);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG, "Request GET FUTURE RESERVATION >> " + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response GET FUTURE RESERVATION >> " + response.body().toString());
                    String s = response.body().toString();
                    reservList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray resultArray = jsonObject.getJSONArray("result");
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            for (int i = 0; i < resultArray.length(); i++) {
                                JSONObject resultjsonObject = resultArray.getJSONObject(i);
                                Gson gson = new Gson();
                                ReservationDetails model = gson.fromJson(String.valueOf(resultjsonObject), ReservationDetails.class);
                                model.setBlinked(false);
                                Log.e(TAG, "onResponse: >> " + model);
                                reservList.add(model);
                            }
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            tvNoOrders.setVisibility(View.VISIBLE);
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

            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            if (i == 400) {
                tvNoOrders.setVisibility(View.VISIBLE);
            }
        }
    }


}
