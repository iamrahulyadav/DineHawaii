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
import com.yberry.dinehawaii.Customer.Adapter.CouponAvailableAdapter;
import com.yberry.dinehawaii.Model.CustomerModel;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponOffersFragment extends Fragment {
    String TAG = "CouponOffersFragment";
    Context context;
    private View rootView;
    public static List<CustomerModel> list = new ArrayList<>();
    public static CouponAvailableAdapter adapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    FragmentIntraction intraction;
    CustomTextView nodata;
    public CouponOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_available_coupon, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Coupons");
        }
        initComponent();
        getAllCoupons();
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
        nodata = (CustomTextView) rootView.findViewById(R.id.nodata);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_coupon);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new CouponAvailableAdapter(context, list);
        mRecyclerView.setAdapter(adapter);


    }
    private void getAllCoupons() {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.ENDPOINT.GETALLCOUPONS);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
            jsonObject.addProperty("business_id", AppPreferences.getBusiID(context));
            Log.e(TAG, "available coupon json :- " + jsonObject.toString());
            getCoupon(jsonObject);

        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getCoupon(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_coupon_cust(jsonObject);


        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "available coupon Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CustomerModel customerModel = new CustomerModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            customerModel.setAmount(object.getString("coupon_amt_per"));
                            customerModel.setCoupon_code(object.getString("coupon_code"));
                            customerModel.setEnd_date(object.getString("coupon_e_date"));
                            customerModel.setStart_date(object.getString("coupon_s_date"));
                            customerModel.setCoupon_msg(object.getString("coupon_description"));
                            customerModel.setCoupon_status("");
                            list.add(customerModel);

                        }
                        adapter.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        nodata.setVisibility(View.VISIBLE);

                        //    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_LONG).show();
//                        Log.d("onResponse", jsonObject.getString("msg"));
                    }
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
                nodata.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
