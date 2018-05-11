package com.yberry.dinehawaii.Bussiness.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.AddNewCouponActivity;
import com.yberry.dinehawaii.Bussiness.Adapter.CouponAdapter;
import com.yberry.dinehawaii.Bussiness.model.CouponModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
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

public class ManageCouponsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageCouponsFragment";
    Context context;
    ArrayList<CouponModel> list;
    FragmentIntraction intraction;
    private RecyclerView recycler_view;
    private CouponAdapter adapter;
    CustomTextView noCoupons;

    public ManageCouponsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_coupons, container, false);
        context = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("Manage Coupons");
        }
        list = new ArrayList<CouponModel>();
        init(view);
        setAdapter();
        return view;
    }

    private void setAdapter() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CouponAdapter(context, list);
        recycler_view.setAdapter(adapter);
        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), AddNewCouponActivity.class);
                intent.setAction("edit_coupon");
                intent.putExtra("data", list.get(position));
                getContext().startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void init(View view) {
        ((CustomButton) view.findViewById(R.id.addNewCoupon)).setOnClickListener(this);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        noCoupons = (CustomTextView) view.findViewById(R.id.noCoupons);
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

    private void getAllCoupons() {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.ALLCOUPONS);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "getAllCoupons: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_coupons_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "onResponse: Response >> " + resp);
                try {
                    list.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noCoupons.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            CouponModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), CouponModel.class);
                            list.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        noCoupons.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getActivity(), "no record found", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    noCoupons.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                noCoupons.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isNetworkAvailable(getActivity())) {
            getAllCoupons();
        } else
            Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewCoupon:
                Intent intent = new Intent(getActivity(), AddNewCouponActivity.class);
                intent.setAction("add_coupon");
                getContext().startActivity(intent);
                break;
            case R.id.addfoodtype:
                break;
            default:
                break;
        }
    }


}

