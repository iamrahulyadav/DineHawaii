package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PRINCE 9977123453 on 03-02-17.
 */

/* ~~~~~~~~~~~~~~~~~~~~~~~~  Screen No 43 ~~~~~~~~~~~~~~~~~~~~~~~~ */

public class BusinessInformationSubmitFragment extends Fragment {
    private static final String TAG = "BusinessInfoSubmtFrag";
    CustomEditText passcodeValue;
    RadioGroup radioGroup;
    RadioButton checkPrimaryContact, checkBusinessEmail;
    String value, passcodeGenerateValue;
    String CHECKED_VALUE = "business_email", admin_approval, data1;
    private CustomButton submitButton;
    private CustomTextView passcode, submit;
    private Context context;
    private View rootView;

    public BusinessInformationSubmitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_business_information_submit, container, false);
        ((LinearLayout) rootView.findViewById(R.id.maineLayout)).setVisibility(View.VISIBLE);
        init(rootView);
        LinearLayout mainView = (LinearLayout) rootView.findViewById(R.id.mainView);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        return rootView;
    }

    private void init(View view) {

        context = getContext();
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        checkBusinessEmail = (RadioButton) view.findViewById(R.id.checkBusinessEmail);
        checkPrimaryContact = (RadioButton) view.findViewById(R.id.checkPrimaryContact);

        passcode = (CustomTextView) view.findViewById(R.id.passcode);
        passcodeValue = (CustomEditText) view.findViewById(R.id.passcodeValue);
        submitButton = (CustomButton) view.findViewById(R.id.submit);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.checkBusinessEmail) {
                    CHECKED_VALUE = "business_email";
                    Log.v(TAG, "business_email Checked !!!!");
                } else if (checkedId == R.id.checkPrimaryContact) {
                    CHECKED_VALUE = "business_Contact";
                    Log.v(TAG, "business_Contact Checked !!!!");
                }
            }
        });

        passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.isNetworkAvailable(getActivity())) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.GENERATEPASSCODE);
                    jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                    jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                    jsonObject.addProperty("passcode_type", CHECKED_VALUE);
                    jsonObject.addProperty("passcode_type", CHECKED_VALUE);
                    jsonObject.addProperty("FCM_id", FirebaseInstanceId.getInstance().getToken());


                    passcodeGenerateMethod(jsonObject);

                    Log.v(TAG, "Request from generatePasscode :- " + jsonObject);

                } else {
                    Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.isNetworkAvailable(getActivity())) {

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.CHECKPASSCODE);
                    jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                    jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                    jsonObject.addProperty("passcode", passcodeValue.getText().toString());

                    JsonCallMethod(jsonObject);
                    Log.v(TAG, "Request from Submit button :- " + jsonObject);
                } else {
                    Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void JsonCallMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonElements = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonElements.getJSONObject(0);
                        admin_approval = jsonObject1.getString("admin_approval");
                        Log.d(TAG, "onResponse: admin_approval" + admin_approval);
                        if (admin_approval.equalsIgnoreCase("0")) {
                            Toast.makeText(getContext(), "After admin approval your bussiness detail will be display to all user", Toast.LENGTH_LONG).show();
                        } else {

                        }

                       /* AppPreferencesBuss.setRegistrationSno43(getActivity(), "Submit");
                        AppPreferencesBuss.setRegistrationSno42F(getActivity(), "");*/
                        ((FrameLayout) rootView.findViewById(R.id.layout_frame1)).setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getActivity(), BusinessNaviDrawer.class));
                            }
                        }, 500);
                    } else {
                        //
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg = jsonObject1.getString("msg");

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
            }
        });
    }

    private void passcodeGenerateMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    passcodeGenerateValue = jsonObject1.getString("Passcode");
                    passcodeValue.setText(passcodeGenerateValue);
                    Log.d(TAG, passcodeGenerateValue);
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
            }
        });
    }


}























