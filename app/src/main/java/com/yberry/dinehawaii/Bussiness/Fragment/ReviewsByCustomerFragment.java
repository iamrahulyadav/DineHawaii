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
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.BusinessReviewAdapter;
import com.yberry.dinehawaii.Model.ReviewModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsByCustomerFragment extends Fragment {
    private static final String TAG = "ReviewsByCustomerFragment";
    public static BusinessReviewAdapter adapter;
    public static List<ReviewModel> list = new ArrayList<>();
    FragmentIntraction intraction;
    Context context;
    CustomTextView dataId;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomTextView tvSubmit;

    public ReviewsByCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews_by_customer, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Customer Reviews");
        }
        context = getActivity();
        init(view);
        dataAvailableReview();
        return view;
    }

    private void init(View view) {
        dataId = (CustomTextView) view.findViewById(R.id.dataId);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_reservation);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
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

    @SuppressLint("LongLogTag")
    private void dataAvailableReview() {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.ALL_RATING_REVIEW);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));   //AppPreferences.getBusiID(context)
            jsonObject.addProperty("user_id", ""); //AppPreferences.getReservationId(context)
            Log.e(TAG, "Request :- " + jsonObject.toString());
            getDataFromServerReview(jsonObject);

        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromServerReview(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ReviewModel reviewModel = new ReviewModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            reviewModel.setRating(object.getString("rating"));
                            reviewModel.setReview_message(object.getString("review"));
                            reviewModel.setReview_question(object.getString("customer_name"));
                            reviewModel.setCustomer_id(object.getString("customer_id"));
                            list.add(reviewModel);
                        }
                        adapter = new BusinessReviewAdapter(context, list);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                        dataId.setVisibility(View.VISIBLE);
                        dataId.setText(msg);
                    } else {
                        dataId.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressHD.dismiss();
                    Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
