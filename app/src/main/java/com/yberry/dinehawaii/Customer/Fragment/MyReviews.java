package com.yberry.dinehawaii.Customer.Fragment;

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
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.MyReviewsAdapter;
import com.yberry.dinehawaii.Model.ReviewModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.FragmentIntraction;
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

public class MyReviews extends Fragment {
    private static final String TAG = "My Reviews";
    MyReviewsAdapter reviewsAdapter;
    ArrayList<ReviewModel> reviewModelArrayList;
    Context context;
    CustomTextView noreview;
    FragmentIntraction intraction;
    private View rootView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_my_reviews, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("My Reviews");
        }

        context = getActivity();
        initComponent();
        myReview();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIntraction) {
            intraction = (FragmentIntraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        intraction = null;
    }


    private void initComponent() {
        context = getActivity();
        reviewModelArrayList = new ArrayList<ReviewModel>();
        noreview = (CustomTextView) rootView.findViewById(R.id.noreviews);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.review_recycler);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        reviewsAdapter = new MyReviewsAdapter(context, reviewModelArrayList);
        mRecyclerView.setAdapter(reviewsAdapter);
        reviewsAdapter.notifyDataSetChanged();
    }

    private void myReview() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.MYREVIEWS);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));//8
            Log.e(TAG, "MyReviewJson :- " + jsonObject.toString());
            reviewData(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void reviewData(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e(TAG, "Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noreview.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            ReviewModel reviewModel = new ReviewModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            reviewModel.setRating(object.getString("rate"));
                            reviewModel.setReview_message(object.getString("review_message"));
                            reviewModel.setBussName(object.getString("Buss_name"));
                            reviewModel.setBusiness_id(object.getString("business_id"));
                            reviewModel.setDate(object.getString("date"));
                            reviewModelArrayList.add(reviewModel);
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                        noreview.setVisibility(View.VISIBLE);
                    } else {
                        noreview.setVisibility(View.VISIBLE);
                    }
                    reviewsAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressHD.dismiss();
                    Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
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
