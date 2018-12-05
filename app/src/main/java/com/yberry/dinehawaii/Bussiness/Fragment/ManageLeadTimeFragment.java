package com.yberry.dinehawaii.Bussiness.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageLeadTimeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageLeadTimeFragment";
    Context context;
    FragmentIntraction intraction;
    private CustomEditText etResTime, etTakeOutTime, etCatTime;
    private CustomTextView btnSave;
    private ImageView helpdialog1, helpdialog2, helpdialog3;

    public ManageLeadTimeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_lead_time, container, false);
        context = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("Lead Time");
        }
        init(view);
        return view;
    }


    private void init(View view) {
        etResTime = (CustomEditText) view.findViewById(R.id.etResTime);
        etTakeOutTime = (CustomEditText) view.findViewById(R.id.etTakeOutTime);
        etCatTime = (CustomEditText) view.findViewById(R.id.etCatTime);
        btnSave = (CustomTextView) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        helpdialog1 = ((ImageView) view.findViewById(R.id.helpdialog1));
        helpdialog2 = ((ImageView) view.findViewById(R.id.helpdialog2));
        helpdialog3 = ((ImageView) view.findViewById(R.id.helpdialog3));

        helpdialog1.setOnClickListener(this);
        helpdialog2.setOnClickListener(this);
        helpdialog3.setOnClickListener(this);


        //Function.bottomToolTipDialogBox(null, getActivity(), "Welcome to Dine Hawaii, proceed ahead by login or register.", helpDialog, null);

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

    private void getLeadTimes() {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.GETLEADTIMES);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "getLeadTimes: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.buss_service(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    String resp = response.body().toString();
                    Log.e(TAG, "onResponse: Response >> " + resp);
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONArray("result").getJSONObject(0);
                        etResTime.setText(jsonObject1.getString("reserve_lead_time"));
                        etTakeOutTime.setText(jsonObject1.getString("takeout_lead_time"));
                        etCatTime.setText(jsonObject1.getString("catering_days"));
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(getActivity(), "no record found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    progressHD.dismiss();
                    Toast.makeText(getActivity(), "Internal server error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Internal server error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isNetworkAvailable(getActivity())) {
            getLeadTimes();
        } else
            Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (TextUtils.isEmpty(etResTime.getText().toString()) || etResTime.getText().toString().equalsIgnoreCase("0"))
                    etResTime.setError("Set Time");
                else if (TextUtils.isEmpty(etTakeOutTime.getText().toString()) || etTakeOutTime.getText().toString().equalsIgnoreCase("0"))
                    etTakeOutTime.setError("Set Time");
                else if (TextUtils.isEmpty(etCatTime.getText().toString()) || etCatTime.getText().toString().equalsIgnoreCase("0"))
                    etCatTime.setError("Set Time");
                else
                    setLeadTimes();//api call
                break;
            case R.id.helpdialog1:
                Function.bottomToolTipDialogBox(null, getActivity(), "This is reservation lead time in minutes", helpdialog1, null);
                break;
            case R.id.helpdialog2:
                Function.bottomToolTipDialogBox(null, getActivity(), "This is reservation take-out lead time in minutes", helpdialog2, null);
                break;
            case R.id.helpdialog3:
                Function.bottomToolTipDialogBox(null, getActivity(), "This is catering lead time in days", helpdialog3, null);
                break;
        }
    }

    private void setLeadTimes() {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.SETLEADTIME);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("reserve_lead_time", etResTime.getText().toString());
        jsonObject.addProperty("takeout_lead_time", etTakeOutTime.getText().toString());
        jsonObject.addProperty("catering_days", etCatTime.getText().toString());
        Log.e(TAG, "setLeadTimes: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.buss_service(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();
                Log.e(TAG, "setLeadTimes Response >> " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Toast.makeText(getActivity(), "Changes saved ", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

