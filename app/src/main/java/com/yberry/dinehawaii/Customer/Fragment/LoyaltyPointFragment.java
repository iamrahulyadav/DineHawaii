package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.LoyaltyPointAdapter;
import com.yberry.dinehawaii.Model.LoyaltyPointModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoyaltyPointFragment extends Fragment{
    private static final String TAG = "Loyalty point";
    public static LoyaltyPointAdapter adapter;
    CustomTextView nopoints;
    ArrayList<LoyaltyPointModel> loyaltyList;
    View rootView;
    Context context;
    LinearLayout layout;
    FragmentIntraction intraction;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_loyalty_point, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Loyalty Points");
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new LoyaltyBroadCast(), new IntentFilter("loyaltyPoints"));

        init();
        loyaltyPoint(container.getContext());
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

    private void init() {
        context = getActivity();
        nopoints = (CustomTextView) rootView.findViewById(R.id.nopointsav);
        layout = (LinearLayout) rootView.findViewById(R.id.activity_loyalty_point);
        loyaltyList = new ArrayList<LoyaltyPointModel>();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new LoyaltyPointAdapter(context, loyaltyList);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = getActivity().getCurrentFocus();
                if (v != null) {
                    hideKeyboard();
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        //LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(LoyaltyBroadCast);
        super.onDestroy();
    }

    private void loyaltyPoint(Context context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.MYLOYALTYPOINTS);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
        Log.d(TAG, "LoyaltyPointJson Request :- " + jsonObject.toString());
        myLoyaltyPoints(jsonObject);
    }

    private void myLoyaltyPoints(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    loyaltyList.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            LoyaltyPointModel model = new LoyaltyPointModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            model.setBussId(object.getString("buss_id"));
                            model.setBussName(object.getString("Buss_name"));
                            model.setDate(object.getString("date"));
                            model.setTotalPoints(object.getString("total_points"));
                            loyaltyList.add(model);

                        }
                        adapter.notifyDataSetChanged();

                    } else if (jsonObject.getString("status").equals("400")) {
                        progressHD.dismiss();
                        nopoints.setVisibility(View.VISIBLE);
                    } else {
                        progressHD.dismiss();
                        nopoints.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    progressHD.dismiss();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                nopoints.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    class LoyaltyBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loyaltyPoint(context);
        }
    }
}


