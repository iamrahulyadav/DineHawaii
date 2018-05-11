package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 04-02-17.
 */

public class FeedbackandReviewNextFragment extends Fragment implements View.OnClickListener {

    private Button btnSubmit, btnAddmore;
    private RadioGroup rgRateApp;
    private RadioButton rgAccess, rgCommended, ease_of_use, business_info, radioResponsive, radioBusinessName, radioRecomended;
    private CustomEditText etQues1, etQues2, etQues3, etQues4, etQues5, etOptionalMsg;
    private Context mContext;
    private LinearLayout linearCheckbox;
    private EditText etBusinessName;
    private String checkValue = "0", checkValue1 = "0", checkValue2 = "0", optionalValue = "0", questions, questions1, questions2, questions3, questions4, result;
    private CheckBox checkIos, checkAndroid, checkWebsite;

    private int Count = 1;

    private int radiovalue = 0;
    private int radiovalue1 = 0;
    private int radiovalue2 = 0;
    private int radiovalue3 = 0;
    private int radiovalue4 = 0;

    private int pos;
    private int pos1;

    private String TAG = "FEEDBACKFRAGMENT";
    FragmentIntraction intraction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_review, container, false);
        //headText.setText("FEEDBACK & REVIEWS");
//        headText.setText("FEEDBACK");
        if (intraction != null) {
            intraction.actionbarsetTitle("Feedback");
        }
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
        btnAddmore = (Button) view.findViewById(R.id.btnAddmore);
       /* etQues1 = (CustomEditText) view.findViewById(R.id.etQues1);
        etQues2 = (CustomEditText) view.findViewById(R.id.etQues2);
        etQues3 = (CustomEditText) view.findViewById(R.id.etQues3);
        etQues4 = (CustomEditText) view.findViewById(R.id.etQues4);
        etQues5 = (CustomEditText) view.findViewById(R.id.etQues5);
        etOptionalMsg = (CustomEditText) view.findViewById(R.id.etTextMsg);
        etBusinessName = (EditText) view.findViewById(R.id.etBusinessName);
        rgRateApp = (RadioGroup) view.findViewById(R.id.rgRateApp);
        rgAccess = (RadioButton) view.findViewById(R.id.radioAccess);
        rgCommended = (RadioButton) view.findViewById(R.id.radioRecomended);
        checkIos = (CheckBox) view.findViewById(R.id.cbIos);
        checkAndroid = (CheckBox) view.findViewById(R.id.cbAndroid);
        checkWebsite = (CheckBox) view.findViewById(R.id.cbWebsite);
        linearCheckbox = (LinearLayout) view.findViewById(R.id.linearCheckbox);


        ease_of_use = (RadioButton) view.findViewById(R.id.ease_of_use);
        business_info = (RadioButton) view.findViewById(R.id.business_info);
        radioResponsive = (RadioButton) view.findViewById(R.id.radioResponsive);
        radioBusinessName = (RadioButton) view.findViewById(R.id.radioBusinessName);
        radioRecomended = (RadioButton) view.findViewById(R.id.radioRecomended);*/

        //  private RadioButton ease_of_use,business_info,radioResponsive,radioBusinessName,radioRecomended;


        btnSubmit.setOnClickListener(this);
        btnAddmore.setOnClickListener(this);
        checkIos.setOnClickListener(this);

        rgCommended.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etOptionalMsg.setVisibility(View.VISIBLE);
                if (rgCommended.isChecked()) {
                    optionalValue = "1";

                }
            }
        });

        checkIos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkIos.isChecked()) {
                    checkValue = "1";
                }
            }
        });

        checkAndroid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkAndroid.isChecked()) {
                    checkValue1 = "1";
                }
            }
        });
        checkWebsite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkWebsite.isChecked()) {
                    checkValue2 = "1";
                }
            }
        });

        rgRateApp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                // Method 1 For Getting Index of RadioButton
                pos = rgRateApp.indexOfChild(view.findViewById(checkedId));

                pos1 = rgRateApp.indexOfChild(view.findViewById(rgRateApp.getCheckedRadioButtonId()));
                etBusinessName.setText("");
                Log.d(TAG + "POS", String.valueOf(pos1));

                switch (pos1) {
                    case 0:
                        linearCheckbox.setVisibility(View.VISIBLE);
                        radiovalue = 1;
                        radiovalue1 = 0;
                        radiovalue2 = 0;
                        radiovalue3 = 0;
                        radiovalue4 = 0;
                        Log.d("click1", String.valueOf(radiovalue));
                        break;
                    case 2:
                        linearCheckbox.setVisibility(View.GONE);
                        radiovalue1 = 1;
                        radiovalue2 = 0;
                        radiovalue3 = 0;
                        radiovalue4 = 0;
                        radiovalue = 0;
                        Log.d("click2", String.valueOf(radiovalue1));
                        break;
                    case 3:
                        linearCheckbox.setVisibility(View.GONE);
                        radiovalue2 = 1;
                        radiovalue = 0;
                        radiovalue1 = 0;
                        radiovalue3 = 0;
                        radiovalue4 = 0;
                        Log.d("click3", String.valueOf(radiovalue2));
                        break;
                    case 4:
                        linearCheckbox.setVisibility(View.GONE);
                        radiovalue3 = 1;
                        radiovalue = 0;
                        radiovalue1 = 0;
                        radiovalue2 = 0;
                        radiovalue4 = 0;
                        Log.d("click4", String.valueOf(radiovalue3));
                        break;
                    case 5:
                        etBusinessName.setEnabled(true);
                        linearCheckbox.setVisibility(View.GONE);
                        radiovalue4 = 1;
                        radiovalue = 0;
                        radiovalue1 = 0;
                        radiovalue2 = 0;
                        radiovalue3 = 0;
                        Log.d("click5", String.valueOf(radiovalue4));
                        break;

                    default:
                        //The default selection
                        radiovalue = 1;
                        radiovalue1 = 0;
                        radiovalue2 = 0;
                        radiovalue3 = 0;
                        radiovalue4 = 0;
                        Log.d("default", String.valueOf(radiovalue));
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddmore) {
            for (int i = 0; i <= Count; i++) {
                if (Count == 1) {
                    etQues2.setVisibility(View.VISIBLE);


                } else if (Count == 2) {

                    etQues3.setVisibility(View.VISIBLE);

                } else if (Count == 3) {

                    etQues4.setVisibility(View.VISIBLE);

                } else if (Count == 4) {

                    etQues5.setVisibility(View.VISIBLE);
                    btnAddmore.setVisibility(View.GONE);

                }
                Count++;
                break;
            }

        }

        if (v.getId() == R.id.submit) {

            questions = etQues1.getText().toString();
            questions1 = etQues2.getText().toString();
            questions2 = etQues3.getText().toString();
            questions3 = etQues4.getText().toString();
            questions4 = etQues5.getText().toString();

            if (questions.isEmpty()) {
                etQues1.setError("Please Enter your question #1");
            } else if (questions != null)
                result = questions;
            else if (questions1 != null)
                result += "," + questions1 + ",";
            else if (questions2 != null)
                result += questions2 + ",";
            else if (questions3 != null)
                result += questions3 + ",";
            else if (questions4 != null)
                result += questions4;

            if (Util.isNetworkAvailable(mContext)) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.BUSINESS_FEEDBACK_AND_MARKETING_API.BUSINESS_FEEDBACK_REVIEW);
                jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                jsonObject.addProperty("question_to_cusotmer", result);
                jsonObject.addProperty("rate_hawaii_app", radiovalue);
                jsonObject.addProperty("rate_hawaii_android", checkValue1);
                jsonObject.addProperty("rate_hawaii_ios", checkValue);
                jsonObject.addProperty("rate_hawaii_website", checkValue2);
                jsonObject.addProperty("ease_of_use", radiovalue1);
                jsonObject.addProperty("business_info", radiovalue2);
                jsonObject.addProperty("responsive_app", radiovalue3);
                jsonObject.addProperty("report_close_business", radiovalue4);
                jsonObject.addProperty("report_close_business_name", optionalValue);
                jsonObject.addProperty("improve_comment", etOptionalMsg.getText().toString());

                Log.d("JSONOBJECT:", jsonObject.toString());

                JsonCallMethod(jsonObject);
            } else {
                Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            }

            Fragment fragment = new MarketingOptionFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();

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
                Log.e(TAG + "onResponseFoodType", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
       /* if (Util.isNetworkAvailable(mContext)) {*/

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", "BusinessView56B");
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
          //  Log.e(TAG + "Response", jsonObject.toString());
            showDataFromServer(jsonObject);
            /*jsonObject.addProperty("business_id", "1");
            jsonObject.addProperty("user_id", "13");
            *///jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_ORDER_PAYMENT_PACKAGE);

            //   Log.e(TAG + "Response", jsonObject.toString());

        }/* else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
*/



    private void showDataFromServer(JsonObject jsonObject) {
      /*  final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });*/
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_table_system_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseDeliverService", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String rate_app = object.getString("rate_hawaii_app");
                                String rate_android = object.getString("rate_hawaii_android");
                                String rate_ios = object.getString("rate_hawaii_ios");
                                String rate_website = object.getString("rate_hawaii_website");
                                String ease_use = object.getString("ease_of_use");
                                String businessinfo = object.getString("business_info");
                                String responsive_app = object.getString("responsive_app");
                                String report_close_business = object.getString("report_close_business");
                                String report_close_business_name = object.getString("report_close_business_name");
                                String improve_comment = object.getString("improve_comment");
                                String rate_question = object.getString("rate_question");
                                if (rate_app.equalsIgnoreCase("1")) {
                                    rgAccess.setChecked(true);

                                } else if (rate_app.equalsIgnoreCase("0")) {
                                    rgAccess.setChecked(false);

                                } else if (rate_app.equalsIgnoreCase("")) {
                                    rgAccess.setChecked(false);

                                }
                                if (rate_ios.equalsIgnoreCase("1")) {
                                    checkIos.setChecked(true);

                                } else if (rate_ios.equalsIgnoreCase("0")) {
                                    checkIos.setChecked(false);

                                } else if (rate_ios.equalsIgnoreCase("")) {
                                    checkIos.setChecked(false);

                                }
                                if (rate_android.equalsIgnoreCase("1")) {
                                    checkAndroid.setChecked(true);

                                } else if (rate_android.equalsIgnoreCase("0")) {
                                    checkAndroid.setChecked(false);

                                } else if (rate_android.equalsIgnoreCase("")) {
                                    checkAndroid.setChecked(false);

                                }
                                if (rate_website.equalsIgnoreCase("1")) {
                                    checkWebsite.setChecked(true);

                                } else if (rate_website.equalsIgnoreCase("0")) {
                                    checkWebsite.setChecked(false);

                                } else if (rate_website.equalsIgnoreCase("")) {
                                    checkWebsite.setChecked(false);

                                }
                                if (ease_use.equalsIgnoreCase("1")) {
                                    ease_of_use.setChecked(true);

                                } else if (ease_use.equalsIgnoreCase("0")) {
                                    ease_of_use.setChecked(false);

                                } else if (ease_use.equalsIgnoreCase("")) {
                                    ease_of_use.setChecked(false);
                                }
                                if (businessinfo.equalsIgnoreCase("1")) {
                                    business_info.setChecked(true);

                                } else if (businessinfo.equalsIgnoreCase("0")) {
                                    business_info.setChecked(false);

                                } else if (businessinfo.equalsIgnoreCase("")) {
                                    business_info.setChecked(false);

                                }
                                if (responsive_app.equalsIgnoreCase("1")) {
                                    radioResponsive.setChecked(true);

                                } else if (responsive_app.equalsIgnoreCase("0")) {
                                    radioResponsive.setChecked(false);

                                } else if (responsive_app.equalsIgnoreCase("")) {
                                    radioResponsive.setChecked(false);

                                }   if (report_close_business.equalsIgnoreCase("1")) {
                                    radioBusinessName.setChecked(true);

                                } else if (report_close_business.equalsIgnoreCase("0")) {
                                    radioBusinessName.setChecked(false);

                                } else if (report_close_business.equalsIgnoreCase("")) {
                                    radioBusinessName.setChecked(false);

                                }



                                etBusinessName.setText(report_close_business_name);
                                etOptionalMsg.setText(improve_comment);


                            }
                        }

                        else {
                            Toast.makeText(getActivity(),"No Data found",Toast.LENGTH_SHORT).show();
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