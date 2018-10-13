package com.yberry.dinehawaii.Bussiness.Fragment;


import android.annotation.SuppressLint;
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
import com.yberry.dinehawaii.Bussiness.Adapter.ResrvFeedbackAdapter;
import com.yberry.dinehawaii.Bussiness.model.OrderDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResrvFeedbackFragment extends Fragment {
    private static final String TAG = "ResrvFeedbackFragment";
    public static
    ResrvFeedbackAdapter adapter;
    public static ArrayList<OrderDetails> list = new ArrayList<OrderDetails>();
    CustomTextView nofeed;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ResrvFeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_feedback, container, false);
        initView(view);
        getReservFeedback();
        return view;
    }

    private void getReservFeedback() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.GETRESERVEFEED);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            Log.e(TAG, "getReservFeedback request>>. " + jsonObject.toString());
            list.clear();
            final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });


            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.business_feedback_and_marketing_api(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "getReservFeedback Response :- " + response.body().toString());
                    String resp = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            //list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                OrderDetails model = new OrderDetails();
                                JSONObject object = jsonArray.getJSONObject(i);
                                model.setOrder_id(object.getString("id"));
                                model.setUser_name(object.getString("user"));
                                model.setReviewmsg(object.getString("review_message"));
                                list.add(model);
                            }
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            progressHD.dismiss();
                            nofeed.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        adapter.notifyDataSetChanged();

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
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void initView(View rootView) {
        nofeed = (CustomTextView) rootView.findViewById(R.id.noreserv);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeCurrentRes);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_feed);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new ResrvFeedbackAdapter(getActivity(), list);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ((CustomButton) rootView.findViewById(R.id.tvAdd)).setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getReservFeedback();
                adapter.notifyDataSetChanged();
            }
        });
    }

}
