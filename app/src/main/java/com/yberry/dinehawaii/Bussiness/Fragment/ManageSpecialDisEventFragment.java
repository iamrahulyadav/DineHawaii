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
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageSpecialDisEventFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageLeadTimeFragment";
    Context context;
    FragmentIntraction intraction;
    private CustomTextView btnSave;
    private ImageView helpdialog1, helpdialog2, helpdialog3;
    private CustomEditText etTitle;
    private CustomEditText etDesrc;

    public ManageSpecialDisEventFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_special_disevent_fragment, container, false);
        context = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("Post Discount and Events");
        }
        init(view);
        return view;
    }


    private void init(View view) {
        etTitle = (CustomEditText) view.findViewById(R.id.etTitle);
        etDesrc = (CustomEditText) view.findViewById(R.id.etDesrc);
        btnSave = (CustomTextView) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        helpdialog1 = ((ImageView) view.findViewById(R.id.helpdialog1));
        helpdialog2 = ((ImageView) view.findViewById(R.id.helpdialog2));

        helpdialog1.setOnClickListener(this);
        helpdialog2.setOnClickListener(this);
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

    private void postData() {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.POSTDISOUNTEVENT);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("business_name", AppPreferencesBuss.getBussiName(getActivity()));
        jsonObject.addProperty("title", etTitle.getText().toString());
        jsonObject.addProperty("desciption", etDesrc.getText().toString());
        Log.e(TAG, "postData: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.buss_service(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "onResponse: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
//                        JSONObject jsonObject1 = jsonObject.getJSONArray("result").getJSONObject(0);
                        etDesrc.setText("");
                        etTitle.setText("");
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
       /* if (Util.isNetworkAvailable(getActivity())) {
            getLeadTimes();
        } else
            Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (TextUtils.isEmpty(etTitle.getText().toString()))
                    etTitle.setError("Enter Title");
                else if (TextUtils.isEmpty(etDesrc.getText().toString()))
                    etDesrc.setError("Enter Description");
                else
                    postData();//api call
                break;
            case R.id.helpdialog1:
                Function.bottomToolTipDialogBox(null, getActivity(), "This is reservation lead time in minutes", helpdialog1, null);
                break;
            case R.id.helpdialog2:
                Function.bottomToolTipDialogBox(null, getActivity(), "This is reservation take-out lead time in minutes", helpdialog2, null);
                break;
        }
    }
}

