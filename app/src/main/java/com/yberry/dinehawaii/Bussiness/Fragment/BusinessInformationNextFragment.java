package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.SelectLanguageActivity;
import com.yberry.dinehawaii.Model.GeographicModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by PRINCE 9977123453 on 03-02-17.
 */

/* ~~~~~~~~~~~~~~~~~~~~~~~~  Screen No 42 D ~~~~~~~~~~~~~~~~~~~~~~~~*/

public class BusinessInformationNextFragment extends Fragment {

    private static final String TAG = "BusinessInfoNextFrag";
    CustomEditText feinNo, aboutBusiness, businessType, etGeTaxNo;
    Spinner geographicArea;
    ImageView nextView;
    String value;
    CustomEditText edittextLanguage;
    ArrayAdapter<String> geographicAdapter;
    ArrayList<String> geographic_list;
    ArrayList<String> geographic_id_list;
    ArrayList<GeographicModel> geographicModel;
    RadioGroup rgExemption;
    LinearLayout llGeTax;
    String exemptValue;
    private String selected_geographic_location = "", selected_geographic_id = "";

    public BusinessInformationNextFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_business_information_next, container, false);

        etGeTaxNo = (CustomEditText) view.findViewById(R.id.etGeTaxNo);
        edittextLanguage = (CustomEditText) view.findViewById(R.id.edittext_language);
        feinNo = (CustomEditText) view.findViewById(R.id.fein_no);
        aboutBusiness = (CustomEditText) view.findViewById(R.id.about_business);
        businessType = (CustomEditText) view.findViewById(R.id.business_type);
        geographicArea = (Spinner) view.findViewById(R.id.geographic_area);
        nextView = (ImageView) view.findViewById(R.id.imageview);
        llGeTax = (LinearLayout) view.findViewById(R.id.llGetax);
        rgExemption = (RadioGroup) view.findViewById(R.id.rgExemption);
        geographic_list = new ArrayList<>();
        geographic_id_list = new ArrayList<>();
        init();
        LinearLayout mainView = (LinearLayout) view.findViewById(R.id.activity_security);
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

        feinNo.setText(AppPreferencesBuss.getBussiFein(getActivity()));
        getGeographicArea();

        rgExemption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rdexemptYes) {
                    exemptValue = "1";
                    llGeTax.setVisibility(View.VISIBLE);
                } else {
                    exemptValue = "0";
                    llGeTax.setVisibility(View.GONE);
                }
            }
        });

        getData();
        return view;
    }

    private void getData() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_API_VIEW.BUSINESSVIEW42D);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "getData: Request >> " + jsonObject.toString());

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.bussines_service_view(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, response.body().toString());
                String s = response.body().toString();
                Log.d(TAG, "response" + s);
                Log.e(TAG, "getData: Response >>" + response.body().toString());

                JsonObject jsonObject = response.body();
                if (jsonObject.get("status").getAsString().equalsIgnoreCase("200")) {
                    JsonObject jsonObject1 = jsonObject.get("result").getAsJsonObject();
                    String location_name = jsonObject1.get("geo_location_name").getAsString();
                    String about = jsonObject1.get("business_about").getAsString();
                    String lang = jsonObject1.get("language").getAsString();

                    for (int i = 0; i < geographic_list.size(); i++) {
                        if (geographic_list.get(i).equalsIgnoreCase(location_name)) {
                            geographicArea.setSelection(i);
                        }
                    }

                    aboutBusiness.setText(about);
                    edittextLanguage.setText(lang);

                } else {
                    String msg = jsonObject.get("status").getAsString();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }

                progressHD.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error:- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }


    private void init() {

        edittextLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(getActivity(), SelectLanguageActivity.class);
                startActivityForResult(i1, 100);
            }
        });


        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "onClick: selected_geographic_location >> " + selected_geographic_location);
                Log.e(TAG, "onClick: selected_geographic_id >> " + selected_geographic_id);

                if (selected_geographic_id.equalsIgnoreCase(""))
                    Toast.makeText(getActivity(), "Select geographic area", Toast.LENGTH_SHORT).show();
                else if (aboutBusiness.getText().toString().length() < 10)
                    aboutBusiness.setError("Describe your business");
                else if (rgExemption.getCheckedRadioButtonId() == R.id.rbexemptYes && TextUtils.isEmpty(etGeTaxNo.getText().toString()))
                    etGeTaxNo.setError("Enter Ge Tax No");
                else if (edittextLanguage.getText().toString().equalsIgnoreCase(""))
                    Toast.makeText(getActivity(), "Select language", Toast.LENGTH_SHORT).show();
                else {
                    saveData();
                }
            }
        });
    }

    private void saveData() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.UPDATEBUSINESS42D);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("business_fein_no", AppPreferencesBuss.getBussiFein(getActivity()));
            jsonObject.addProperty("geo_graphic_area", selected_geographic_id);
            jsonObject.addProperty("about_business", aboutBusiness.getText().toString().trim());
            jsonObject.addProperty("business_type", businessType.getText().toString().trim());
            jsonObject.addProperty("add_bussiness_website", "");
            jsonObject.addProperty("website_link", "");
            jsonObject.addProperty("select_language", edittextLanguage.getText().toString().trim());

            Log.d(TAG, "Request saveData >> " + jsonObject);
            JsonCallMethod(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG, "Inside on Activity result ");
        String languageName = null;
        if (requestCode == 100) {
            try {
                languageName = data.getStringExtra("language");
                Log.e(TAG, "Language selected :- " + languageName);
            } catch (Exception e) {
                Log.e(TAG, "Exception in selecting Language :-" + e.getMessage());
            }
            edittextLanguage.setText(languageName);
        }
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
                Log.e(TAG, "Response of updateBusiness 42D :- " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
/*
                        AppPreferencesBuss.setRegistrationSno42D(getActivity(), "Submit");
                        AppPreferencesBuss.setRegistrationSno42C(getActivity(), "");
*/

                        Fragment fragment = new BusinessServiceHoursFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                        fragmentTransaction.commitAllowingStateLoss();
                    } else {
                        String msg = jsonObject.getString("status");
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
                Log.e(TAG, "error:- " + Log.getStackTraceString(t));
                t.getMessage();
                progressHD.dismiss();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void getGeographicArea() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.ALLGEOLOCATION);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        Log.e(TAG, jsonObject.toString());
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponse", response.body().toString());
                String s = response.body().toString();
                geographic_list.clear();
                geographic_id_list.clear();
                geographicModel = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String name = jsonObject1.getString("geo_location_name");
                        String geo_id = jsonObject1.getString("id");
                        geographic_list.add(name);
                        geographic_id_list.add(geo_id);
                        GeographicModel m = new GeographicModel();
                        m.setGeo_id(geo_id);
                        m.setName(name);
                        geographicModel.add(m);
                    }
                    geographicAdapter = new ArrayAdapter<>(getActivity(), R.layout.row_spn, geographic_list);
                    //geographicAdapter.setDropDownViewResource(R.layout.row_spn_dropdown);
                    geographicArea.setAdapter(geographicAdapter);
                    geographicArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selected_geographic_location = geographic_list.get(position);
                            selected_geographic_id = geographic_id_list.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    Log.d("geographic_list", String.valueOf(geographic_list));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (progressHD != null && progressHD.isShowing())
                    progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                if (progressHD != null && progressHD.isShowing())
                    progressHD.dismiss();
            }
        });
    }
}
