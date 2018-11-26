package com.yberry.dinehawaii.Customer.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.common.adapter.OrderFeedbackAdapter;
import com.yberry.dinehawaii.common.adapter.OrderFeedbackResponseAdapter;
import com.yberry.dinehawaii.common.models.FeedbackData;
import com.yberry.dinehawaii.common.models.FeedbackResponseData;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.Function.fieldRequired;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerFeedbackFragment extends Fragment {
    private static final String TAG = "CustomerFeedbackFrag";
    OrderFeedbackAdapter adapter;
    private RecyclerView mRecyclerView;
    public static ArrayList<FeedbackData> list = new ArrayList<FeedbackData>();
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    CustomTextView nofeed;
    private Context context;

    public CustomerFeedbackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_feedback, container, false);
        context = getActivity();
        initView(view);
        getOrderFeedback();
        return view;
    }

    private void getOrderFeedback() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.GETCUSTORDERFEED);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));// AppPreferencesBuss.getUserId(getActivity())
            Log.e(TAG, "getOrderFeedback: Request >> " + jsonObject.toString());
            feedbackApi(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void feedbackApi(JsonObject jsonObject) {
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
                Log.e(TAG, "onResponse: Resposne >> " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FeedbackData model = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), FeedbackData.class);
                            Log.e(TAG, "onResponse: model >> " + model);
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
    }

    private void initView(View rootView) {
        nofeed = (CustomTextView) rootView.findViewById(R.id.noreserv);
//        ((CustomButton) rootView.findViewById(R.id.tvAdd)).setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeCurrentRes);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_feed);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new OrderFeedbackAdapter(getActivity(), list, new OrderFeedbackAdapter.MyListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onReplyClick(View view, int position) {
                dialogReply(position);
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getOrderFeedback();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void dialogReply(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.reply_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        final EditText etText = (CustomEditText) dialog.findViewById(R.id.etText);
        final RadioGroup rgStatus = (RadioGroup) dialog.findViewById(R.id.rgStatus);
        CustomButton dsubmit = (CustomButton) dialog.findViewById(R.id.dsubmit);
        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        RecyclerView recycler_view = dialog.findViewById(R.id.recycler_view);


        final FeedbackData data = list.get(position);
        final List<FeedbackResponseData> list1 = data.getResponse();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        if (list1 != null)
            if (list1.contains(null))
                list1.remove(null);


        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        OrderFeedbackResponseAdapter adapter = new OrderFeedbackResponseAdapter(context, list1);
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etText.getText().toString())) {
                    etText.setError(fieldRequired);
                } else {
                    String status = "";
                    switch (rgStatus.getCheckedRadioButtonId()) {
                        case R.id.rbNone:
                            status = "";
                            break;
                        case R.id.rbSolution:
                            status = "Solution";
                            break;
                        case R.id.rbResolved:
                            status = "Resolved";
                            break;
                    }
                    dialog.hide();
                    if (Util.isNetworkAvailable(context)) {
                        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                // TODO Auto-generated method stub
                            }
                        });

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.SEND_REPLY_TO_ORDERS);
                        jsonObject.addProperty("business_id", AppPreferences.getCustomerid(context));// AppPreferencesBuss.getUserId(context)
                        jsonObject.addProperty("user_id", list1.get(position).getBusinessId());
                        jsonObject.addProperty("order_id", data.getOrderId());
                        jsonObject.addProperty("feedback_id", data.getFeedbackId());
                        jsonObject.addProperty("status", status);
                        jsonObject.addProperty("text_reply", etText.getText().toString());
                        jsonObject.addProperty("reply_by", "c");


                        Log.e(TAG, "replyApi: Request >> " + jsonObject.toString());

                        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
                        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);

                        call.enqueue(new Callback<JsonObject>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Log.e(TAG, "replyApi: Response >> " + response.body().toString());
                                String resp = response.body().toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(resp);
                                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                        getOrderFeedback();
                                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    progressHD.dismiss();
                                    Toast.makeText(context, "Server Not Responding", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                progressHD.dismiss();
                            }

                            @SuppressLint("LongLogTag")
                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                                progressHD.dismiss();
                                Toast.makeText(context, "Server Not Responding", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        dialog.show();

    }

    private void replyApi(JsonObject jsonObject) {

    }
}
