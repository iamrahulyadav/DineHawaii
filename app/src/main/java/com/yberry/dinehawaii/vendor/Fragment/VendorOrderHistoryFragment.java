package com.yberry.dinehawaii.vendor.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Activity.VendorOrderDetailActivity;
import com.yberry.dinehawaii.vendor.Adapter.VendorOrderHistoryAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorOrderDetails;

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
public class VendorOrderHistoryFragment extends Fragment {

    private static final String TAG = "VendorOrderHistoryFragment";
    public static ArrayList<VendorOrderDetails> list = new ArrayList<VendorOrderDetails>();
    VendorOrderHistoryAdapter adapter;
    Context context;
    CustomTextView tvNoOrders;
    FragmentIntraction intraction;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout refreshLayout;

    public VendorOrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_order_history, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Order History");
        }
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrders();
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

    private void init(View rootView) {
        context = getActivity();
        tvNoOrders = (CustomTextView) rootView.findViewById(R.id.tvNoOrders);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        adapter = new VendorOrderHistoryAdapter(context, list, new VendorOrderHistoryAdapter.MyListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, VendorOrderDetailActivity.class);
                intent.putExtra("data", list.get(position));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrders();
            }
        });
    }

    private void getOrders() {
        if (Util.isNetworkAvailable(getActivity())) {
            tvNoOrders.setVisibility(View.GONE);
            new GetDataTask1().execute();
        } else {
            Toast.makeText(getActivity(), "Please connect your internet", Toast.LENGTH_LONG).show();
        }
    }

    class GetDataTask1 extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHD1 = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_VENDOR_API.VENDORORDERHISTORY);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG, "Request GET ORDER >> " + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response GET ORDER >> " + response.body().toString());
                    String resp = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            list.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                VendorOrderDetails data = new Gson().fromJson(jsonArray.getJSONObject(i).toString(), VendorOrderDetails.class);

                                Log.e(TAG, "onResponse: data >> " + data);
                              /*  VendorOrderHistoryModel orderModel = new VendorOrderHistoryModel();
                                JSONObject object = jsonArray.getJSONObject(i);
                                orderModel.setOrderId(object.getString("order_id"));
                                orderModel.setOrderUniqueId(object.getString("order_unique_id"));
                                orderModel.setAmount(object.getString("amount"));
                                orderModel.setVendorName(object.getString("vendor_name"));
                                orderModel.setItemName(object.getString("item_name"));
                                orderModel.setItemQuantity(object.getString("item_quantity"));
                                orderModel.setDateTime(object.getString("date_time"));
                                List<String> items = Arrays.asList(object.getString("item_name").split("\\s*,\\s*"));
                                Log.e(TAG, "onResponse: items" + items.toString());
*/
                                list.add(data);
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
            refreshLayout.setRefreshing(false);
            if (progressHD1 != null && progressHD1.isShowing())
                progressHD1.dismiss();
            if (adapter != null) {
                if (adapter.getItemCount() == 0)
                    tvNoOrders.setVisibility(View.VISIBLE);
                else
                    tvNoOrders.setVisibility(View.GONE);
            }
        }
    }
}
