package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedbackandReviewFragment extends Fragment implements View.OnClickListener {
    FragmentIntraction intraction;
    private Button btnSubmit;
    private CustomEditText etQues1, etOptionalMsg,etEaseOfUse,etbusInfo,etresponsive,etrepbuss;
    RadioGroup accessGroup;
    private Context mContext;
    private LinearLayout linearCheckbox;
    private String checkValue = "";
    private RadioButton checkIos, checkAndroid;
    private String TAG = "FEEDBACKFRAGMENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_review, container, false);
//        headText.setText("FEEDBACK");
        if (intraction != null) {
            intraction.actionbarsetTitle("Feedback");
        }
        LinearLayout mainView = (LinearLayout) view.findViewById(R.id.linearmain);
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

        showdata();
        init(view);
        return view;
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

    private void init(final View view) {
        mContext = this.getContext();
        btnSubmit = (Button) view.findViewById(R.id.submit);
        etQues1 = (CustomEditText) view.findViewById(R.id.etQues1);
        etOptionalMsg = (CustomEditText) view.findViewById(R.id.etTextMsg);
        etEaseOfUse = (CustomEditText) view.findViewById(R.id.easeuse);
        etresponsive = (CustomEditText) view.findViewById(R.id.respapp);
        etbusInfo = (CustomEditText) view.findViewById(R.id.businfo);
        etrepbuss = (CustomEditText) view.findViewById(R.id.etBusinessName);
        checkIos = (RadioButton) view.findViewById(R.id.cbIos);
        checkAndroid = (RadioButton) view.findViewById(R.id.cbAndroid);
        linearCheckbox = (LinearLayout) view.findViewById(R.id.linearCheckbox);
        accessGroup = (RadioGroup)view.findViewById(R.id.radiogrp);

        btnSubmit.setOnClickListener(this);
        checkIos.setOnClickListener(this);


        accessGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cbIos){
                    checkValue = "ios";
                }else if (checkedId == R.id.cbAndroid){
                    checkValue = "android";
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.submit) {

                if (Util.isNetworkAvailable(mContext)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("method", AppConstants.BUSINESS_FEEDBACK_AND_MARKETING_API.BUSINESS_FEEDBACK_REVIEW);
                    jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                    jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                    jsonObject.addProperty("question", etQues1.getText().toString());
                    jsonObject.addProperty("app_access", "android");
                    jsonObject.addProperty("ease_of_use", etEaseOfUse.getText().toString());
                    jsonObject.addProperty("business_info", etbusInfo.getText().toString());
                    jsonObject.addProperty("responsive_app", etresponsive.getText().toString());
                    jsonObject.addProperty("report_buss", etrepbuss.getText().toString());
                    jsonObject.addProperty("improve_comment", etOptionalMsg.getText().toString());
                    Log.d("JSONOBJECT:", jsonObject.toString());
                    JsonCallMethod(jsonObject);
                } else {
                    Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            }
          
        }
    }

    private void JsonCallMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_feedback_and_marketing_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponse", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                        Fragment fragment = new BusinessHomeFragment41();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack(null);



                        fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    //  adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG + "error", t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });
    }


    private void showdata() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", "BusinessView56B");
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG + "Response", jsonObject.toString());
            showDataFromServer(jsonObject);
        } else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }


    }


    private void showDataFromServer(JsonObject jsonObject) {
      /*  final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });*/
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.bussines_service_view(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseFeedbackview", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if (jsonArray.length() != 0) {
                                JSONObject object = jsonArray.getJSONObject(0);
                                String ease_use = object.getString("ease_of_use");
                                String businessinfo = object.getString("business_info");
                                String responsive_app = object.getString("responsive_app");
                                String improve_comment = object.getString("improve_comment");
                                String rate_question = object.getString("question");
                                etQues1.setText(rate_question);
                                etEaseOfUse.setText(ease_use);
                                etbusInfo.setText(businessinfo);
                                etresponsive.setText(responsive_app);
                                etOptionalMsg.setText(improve_comment);

                        } else {
                            Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //  adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG + "error", t.getMessage());
                t.getMessage();
                //   progressHD.dismiss();
            }
        });
    }

}
