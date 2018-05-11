package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusiSelectPackageActivity;
import com.yberry.dinehawaii.Bussiness.Adapter.PackageMarketingAdapter;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 03-02-17.
 */

public class PackageAndMarketingFragment extends Fragment {
    private static final String TAG = "Package_MarketingFrag";
    EditText max_LeadTime, min_LeadTime, noShow_time;
    RadioGroup radioGroup1, radioGroup2;
    String radio1 = "1", radio2 = "1";
    ArrayList<CheckBoxPositionModel> packageList = new ArrayList<CheckBoxPositionModel>();
    PackageMarketingAdapter packageMarketingAdapter;
    RadioButton radioTrue, radioF, radioYes, radioNo;
    private ImageView nextImageView;
    private Button btnEdit;
    private RecyclerView mRecyclerView;
    FragmentIntraction intraction;


    public PackageAndMarketingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_package_and_markating_options, container, false);
//        headText.setText("PACKAGES & MARKETING OPTIONS");
        if (intraction != null) {
            intraction.actionbarsetTitle("Packages & Marketing Options");
        }
        showData();
        initViews(view);
        packageApiCalling();
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


    private void packageApiCalling() {

        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.BUSS_PACKAGE_MARKETING_DETAIL);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG, "Package request :- " + jsonObject.toString());
            packageMethodCall(jsonObject);

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }

    private void packageMethodCall(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Package Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                CheckBoxPositionModel model;
                try {
                    JSONObject jsonObject = new JSONObject(resp);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new CheckBoxPositionModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            Log.v(TAG, "Inside Package An Marketing !!!!");

                            String pachageName = jsonObject1.getString("package_name");
                            String pachageDetails = jsonObject1.getString("package_detail");

                            model.setName(pachageName);
                            model.setPackage_detail(pachageDetails);
                            packageList.add(model);

                        }

                        setAdapter(packageList);

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

    private void setAdapter(ArrayList<CheckBoxPositionModel> packageList) {

        packageMarketingAdapter = new PackageMarketingAdapter(getActivity(), packageList);
        mRecyclerView.setAdapter(packageMarketingAdapter);
        packageMarketingAdapter.notifyDataSetChanged();
    }

    private void initViews(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.packageRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        nextImageView = (ImageView) view.findViewById(R.id.imageview);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);

        min_LeadTime = (EditText) view.findViewById(R.id.min_LeadTime);
        max_LeadTime = (EditText) view.findViewById(R.id.max_LeadTime);
        noShow_time = (EditText) view.findViewById(R.id.noShow_time);

        radioGroup1 = (RadioGroup) view.findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) view.findViewById(R.id.radioGroup2);


        radioTrue = (RadioButton) view.findViewById(R.id.radioTrue);
        radioF = (RadioButton) view.findViewById(R.id.radioF);
        radioYes = (RadioButton) view.findViewById(R.id.radioYes);
        radioNo = (RadioButton) view.findViewById(R.id.radioNo);


        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.radioTrue) {
                    radio1 = "1";
                } else if (checkedId == R.id.radioF) {
                    radio1 = "0";
                }

            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.radioYes) {
                    radio2 = "1";
                } else if (checkedId == R.id.radioNo) {
                    radio2 = "0";
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BusiSelectPackageActivity.class);
                startActivity(intent);
            }
        });

        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                packageMarkeingOptions();

                Fragment fragment = new ReservationPackageFragment();//activity_package_and_markating_options
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();

            }
        });
    }

    private void packageMarkeingOptions() {


        String minTimeHours = min_LeadTime.getText().toString().trim();
        String maxTimeMonth = max_LeadTime.getText().toString().trim();
        String time_allowance_minute = noShow_time.getText().toString().trim();

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("method", AppConstants.BUSINESS_FEEDBACK_AND_MARKETING_API.BUSS_PACKAGING_MARKETING_OPTION);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("reserv_request_hours", minTimeHours);
        jsonObject.addProperty("reserv_request_month", maxTimeMonth);
        jsonObject.addProperty("time_allowance_minute", time_allowance_minute);
        jsonObject.addProperty("no_show_time_expired", radio1);
        jsonObject.addProperty("no_show_message_customer", radio2);

        Log.v(TAG, "Request json object :- " + jsonObject.toString());

        packageMarkeingOptionsTask(jsonObject);

    }

    private void packageMarkeingOptionsTask(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response json :- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                        Fragment fragment = new ReservationPackageFragment();//activity_package_and_markating_options
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack(null);



                        fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                        fragmentTransaction.commitAllowingStateLoss();

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

    private void showData() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "BusinessView49");
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        Log.v(TAG, "Request json object :- " + jsonObject.toString());
        showDataFromServer(jsonObject);

    }

    private void showDataFromServer(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.bussines_service_view(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response json :- " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String request_hours = object.getString("reserv_request_hours");
                            String request_month = object.getString("reserv_request_month");
                            String allowance_minute = object.getString("time_allowance_minute");
                            String no_show_time_expired = object.getString("no_show_time_expired");
                            String no_show_message_customer = object.getString("no_show_message_customer");
                            min_LeadTime.setText(request_hours);
                            max_LeadTime.setText(request_month);
                            noShow_time.setText(allowance_minute);
                            if (no_show_time_expired.equalsIgnoreCase("1")) {
                                radioTrue.setChecked(true);
                                radioF.setChecked(false);
                            } else if (no_show_time_expired.equalsIgnoreCase("0")) {
                                radioF.setChecked(true);
                                radioTrue.setChecked(false);
                            } else if (no_show_time_expired.equalsIgnoreCase("")) {
                                radioF.setChecked(false);
                                radioTrue.setChecked(false);
                            }
                            if (no_show_message_customer.equalsIgnoreCase("1")) {
                                radioYes.setChecked(true);
                                radioNo.setChecked(false);
                            } else if (no_show_message_customer.equalsIgnoreCase("0")) {
                                radioYes.setChecked(false);
                                radioNo.setChecked(true);
                            } else if (no_show_message_customer.equalsIgnoreCase("")) {
                                radioYes.setChecked(false);
                                radioNo.setChecked(false);
                            }
                        }
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
}
