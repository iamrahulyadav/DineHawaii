package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class BusinessInformationFragment extends Fragment {
    private static final String TAG = "BusinessInfoFragment";
    ArrayList<String> mailstateList = new ArrayList<String>();
    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> mailcountries = new ArrayList<String>();
    ArrayList<String> countries = new ArrayList<String>();
    ArrayList<String> mailcityList = new ArrayList<String>();
    ArrayList<String> cityList = new ArrayList<String>();
    FragmentIntraction intraction;
    private LinearLayout linearLayout;
    private CustomEditText businessName, businessEmail, street, mailStreet, zipCode, mailZipCode, bussPhoneNo, primaryName, otherPhoneNo, smartPhoneNo, nearestLocality;
    private Spinner spinner_country, spinner_state, spinner_city, mailCountry, mailState, mailCity;
    private CustomTextView edit_bussinessNumber, businessNameTV;
    private ImageView nextView;
    private String phy_country = "", phy_state = "", phy_city = "";
    private String mail_country = "", mail_state = "", mail_city = "";
    private int streetType = 0;
    private String latitude = "0.0", longitude = "0.0";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public BusinessInformationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_business_information, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Business Information");
        }
        initViews(view);
        setListerner();
        physicalAddress();
        mailingAddress();
        if (!AppPreferencesBuss.getBussiPhoneNo(getActivity()).equalsIgnoreCase("")) {
            bussPhoneNo.setText(AppPreferencesBuss.getBussiPhoneNo(getActivity()));
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_API_VIEW.BUSINESSVIEW42C);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "Request GET DATA >> " + jsonObject.toString());
        getData(jsonObject);
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


    private void initViews(View view) {
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
        businessName = (CustomEditText) view.findViewById(R.id.business_name);
        nearestLocality = (CustomEditText) view.findViewById(R.id.locality);
        businessNameTV = (CustomTextView) view.findViewById(R.id.businessNameTV);
        edit_bussinessNumber = (CustomTextView) view.findViewById(R.id.edit_buss_phone);
        businessEmail = (CustomEditText) view.findViewById(R.id.business_email);
        spinner_country = (Spinner) view.findViewById(R.id.country);
        spinner_state = (Spinner) view.findViewById(R.id.state);
        spinner_city = (Spinner) view.findViewById(R.id.city);
        street = (CustomEditText) view.findViewById(R.id.street);
        mailCountry = (Spinner) view.findViewById(R.id.mail_country);
        mailState = (Spinner) view.findViewById(R.id.mail_state);
        mailCity = (Spinner) view.findViewById(R.id.mail_city);
        mailStreet = (CustomEditText) view.findViewById(R.id.mail_street);
        zipCode = (CustomEditText) view.findViewById(R.id.zipCode);
        mailZipCode = (CustomEditText) view.findViewById(R.id.mail_zipCode);
        bussPhoneNo = (CustomEditText) view.findViewById(R.id.business_phoneno);
        primaryName = (CustomEditText) view.findViewById(R.id.primary_name);
        otherPhoneNo = (CustomEditText) view.findViewById(R.id.othephone_no);
        smartPhoneNo = (CustomEditText) view.findViewById(R.id.smartphone_no);
        nextView = (ImageView) view.findViewById(R.id.imageview);
        //headText.setText("BUSINESS INFORMATION");
        businessName.setText(AppPreferencesBuss.getBussiName(getActivity()));
        businessName.setEnabled(false);
        businessName.setActivated(false);
        businessName.setFocusableInTouchMode(false);
        businessName.setFocusable(false);
       /* businessNameTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= businessNameTV.getRight() - businessNameTV.getTotalPaddingRight()) {
                        Function.bottomToolTipDialogBox(null, getActivity(), "You can edit your Business name by clicking on ALLOW EDIT Button !!!", null, v);
                        return true;
                    }
                }
                return true;
            }
        });*/

        street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streetType = 1;
                findPlace();
            }
        });

        mailStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streetType = 2;
                findPlace();
            }
        });

        businessEmail.setText(AppPreferencesBuss.getBussiEmilid(getActivity()));
        Log.d(TAG, AppPreferencesBuss.getBussiName(getActivity()));

        LinearLayout mainView = (LinearLayout) view.findViewById(R.id.linear_layout);
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
    }


    public void findPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, "findPlace: Exception >> " + e.getMessage());
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "findPlace: Exception >> " + e.getMessage());
            // TODO: Handle the error.
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.e(TAG, "onActivityResult: Place >> " + place.getName() + place.getPhoneNumber());
                Log.e(TAG, "onActivityResult: LatLng >> " + place.getLatLng());

                if (streetType == 1) {
                    street.setText(place.getName() + ", " + place.getAddress());
                    latitude = String.valueOf(place.getLatLng().latitude);
                    longitude = String.valueOf(place.getLatLng().longitude);
                    Log.e(TAG, "onActivityResult: latitude >> " + latitude);
                    Log.e(TAG, "onActivityResult: longitude >> " + longitude);
                } else if (streetType == 2) {
                    mailStreet.setText(place.getName() + ", " + place.getAddress());
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "onActivityResult: result_error >> " + status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }


    private void physicalAddress() {
        ArrayAdapter<String> countryadapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, countries);
        spinner_country.setAdapter(countryadapter);
        final ArrayAdapter<String> stateadapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, stateList);
        spinner_state.setAdapter(stateadapter);
        final ArrayAdapter<String> cityadapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, cityList);
        spinner_city.setAdapter(cityadapter);

        try {
            JSONObject obj = new JSONObject(Function.loadJSONFromAsset(getActivity(), "country.json"));
            JSONArray m_jArry = obj.getJSONArray("country_name");
            countries.clear();
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jsonObject = m_jArry.getJSONObject(i);
                countries.add(jsonObject.getString("name"));
            }
            countryadapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: Selected Country >> " + spinner_country.getSelectedItem().toString());
                try {
                    JSONObject obj = new JSONObject(Function.loadJSONFromAsset(getActivity(), "state.json"));
                    JSONObject stateJson = obj.getJSONObject(spinner_country.getSelectedItem().toString());
                    JSONArray state_jArry = stateJson.getJSONArray("name");
                    stateList.clear();
                    for (int i = 0; i < state_jArry.length(); i++) {
                        stateList.add(state_jArry.get(i).toString());
                        stateadapter.notifyDataSetChanged();
                        if (state_jArry.get(i).toString().equalsIgnoreCase(phy_state))
                            spinner_state.setSelection(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.e(TAG, "onItemSelected: Selected State >> " + spinner_state.getSelectedItem().toString());
                        try {
                            JSONObject obj = new JSONObject(Function.loadJSONFromAsset(getActivity(), "city.json"));
                            JSONObject cityJObj = obj.getJSONObject(spinner_state.getSelectedItem().toString());
                            JSONArray city_jArry = cityJObj.getJSONArray("name");
                            cityList.clear();
                            for (int i = 0; i < city_jArry.length(); i++) {
                                cityList.add(city_jArry.get(i).toString());
                                cityadapter.notifyDataSetChanged();
                                if (city_jArry.get(i).toString().equalsIgnoreCase(phy_city))
                                    spinner_city.setSelection(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void mailingAddress() {

        ArrayAdapter<String> countryadapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, mailcountries);
        mailCountry.setAdapter(countryadapter);

        final ArrayAdapter<String> stateadapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, mailstateList);
        mailState.setAdapter(stateadapter);

        final ArrayAdapter<String> cityadapter = new ArrayAdapter<String>(getActivity(), R.layout.row_spn, mailcityList);
        mailCity.setAdapter(cityadapter);

        try {
            JSONObject obj = new JSONObject(Function.loadJSONFromAsset(getActivity(), "country.json"));
            JSONArray m_jArry = obj.getJSONArray("country_name");
            mailcountries.clear();
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jsonObject = m_jArry.getJSONObject(i);
                mailcountries.add(jsonObject.getString("name"));
                countryadapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mailCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onItemSelected: Selected Country >> " + mailCountry.getSelectedItem().toString());
                Log.e(TAG, "onItemSelected: Set Country >> " + mail_country);
                    try {
                        JSONObject obj = new JSONObject(Function.loadJSONFromAsset(getActivity(), "state.json"));
                        JSONObject stateJson = obj.getJSONObject(mailCountry.getSelectedItem().toString());
                        JSONArray state_jArry = stateJson.getJSONArray("name");
                        mailstateList.clear();
                        for (int i = 0; i < state_jArry.length(); i++) {
                            mailstateList.add(state_jArry.get(i).toString());
                            stateadapter.notifyDataSetChanged();
                            if (state_jArry.get(i).toString().equalsIgnoreCase(mail_state))
                                mailState.setSelection(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                mailState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.e(TAG, "onItemSelected: Selected State >> " + mailState.getSelectedItem().toString());
                        Log.e(TAG, "onItemSelected: Set State >> " + mail_state);

                        try {
                            JSONObject obj = new JSONObject(Function.loadJSONFromAsset(getActivity(), "city.json"));
                            JSONObject cityJObj = obj.getJSONObject(mailState.getSelectedItem().toString());
                            JSONArray city_jArry = cityJObj.getJSONArray("name");
                            mailcityList.clear();
                            for (int i = 0; i < city_jArry.length(); i++) {
                                mailcityList.add(city_jArry.get(i).toString());
                                cityadapter.notifyDataSetChanged();
                                if (city_jArry.get(i).toString().equalsIgnoreCase(mail_city))
                                    mailCity.setSelection(i);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void setListerner() {

        edit_bussinessNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = bussPhoneNo.length();
                bussPhoneNo.setEnabled(true);
                bussPhoneNo.setActivated(true);
                bussPhoneNo.setFocusable(true);
                bussPhoneNo.setFocusableInTouchMode(true);
                bussPhoneNo.requestFocus();
                bussPhoneNo.requestFocus(position);
                bussPhoneNo.requestFocusFromTouch();
            }
        });

        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(getActivity())) {
                    //check validation for physical address && mailing address
                    /*if (businessName.getText().toString().equalsIgnoreCase(""))
                        Toast.makeText(getActivity(), "Provide business name", Toast.LENGTH_SHORT).show();
                    else */
                    if (spinner_country.getSelectedItem().toString().equalsIgnoreCase("Select Country"))
                        Toast.makeText(getActivity(), "Select country for physical address", Toast.LENGTH_SHORT).show();
                    else if (spinner_state.getSelectedItem().toString().equalsIgnoreCase("Select State"))
                        Toast.makeText(getActivity(), "Select state for physical address", Toast.LENGTH_SHORT).show();
                    else if (spinner_city.getSelectedItem().toString().equalsIgnoreCase("Select City"))
                        Toast.makeText(getActivity(), "Select city for physical address", Toast.LENGTH_SHORT).show();
                    else if (nearestLocality.getText().toString().equalsIgnoreCase(""))
                        Toast.makeText(getActivity(), "Provide locality", Toast.LENGTH_SHORT).show();
                    else if (street.getText().toString().equalsIgnoreCase(""))
                        Toast.makeText(getActivity(), "Provide street for physical address", Toast.LENGTH_SHORT).show();
                    else if (zipCode.getText().toString().equalsIgnoreCase("") || zipCode.getText().toString().equalsIgnoreCase("0"))
                        Toast.makeText(getActivity(), "Provide zip-code for physical address", Toast.LENGTH_SHORT).show();
                    else if (mailCountry.getSelectedItem().toString().equalsIgnoreCase("Select Country"))
                        Toast.makeText(getActivity(), "Select country for mailing address", Toast.LENGTH_SHORT).show();
                    else if (mailState.getSelectedItem().toString().equalsIgnoreCase("Select State"))
                        Toast.makeText(getActivity(), "Select state for mailing address", Toast.LENGTH_SHORT).show();
                    else if (mailCity.getSelectedItem().toString().equalsIgnoreCase("Select City"))
                        Toast.makeText(getActivity(), "Select city for mailing address", Toast.LENGTH_SHORT).show();
                    else if (mailStreet.getText().toString().equalsIgnoreCase(""))
                        Toast.makeText(getActivity(), "Provide street for mailing address", Toast.LENGTH_SHORT).show();
                    else if (mailZipCode.getText().toString().equalsIgnoreCase("") || mailZipCode.getText().toString().equalsIgnoreCase("0"))
                        Toast.makeText(getActivity(), "Provide zip-code for mailing address", Toast.LENGTH_SHORT).show();
                    else if (bussPhoneNo.getText().toString().equalsIgnoreCase(""))
                        Toast.makeText(getActivity(), "Provide business phone no.", Toast.LENGTH_SHORT).show();
                    else if (primaryName.getText().toString().equalsIgnoreCase(""))
                        Toast.makeText(getActivity(), "Provide primary contact name.", Toast.LENGTH_SHORT).show();
                    else if (smartPhoneNo.getText().toString().equalsIgnoreCase(""))
                        Toast.makeText(getActivity(), "Provide primary contact no.", Toast.LENGTH_SHORT).show();
                    else
                        saveData();
                } else {
                    Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.UPDATEBUSINESS42C);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_name", businessName.getText().toString().trim());
        jsonObject.addProperty("locality", nearestLocality.getText().toString());
        jsonObject.addProperty("street", street.getText().toString());
        jsonObject.addProperty("city", spinner_city.getSelectedItem().toString());
        jsonObject.addProperty("state", spinner_state.getSelectedItem().toString());
        jsonObject.addProperty("country", spinner_country.getSelectedItem().toString());
        jsonObject.addProperty("zipCode", zipCode.getText().toString());
        jsonObject.addProperty("mail_street", mailStreet.getText().toString());
        jsonObject.addProperty("mail_city", mailCity.getSelectedItem().toString());
        jsonObject.addProperty("mail_state", mailState.getSelectedItem().toString());
        jsonObject.addProperty("mail_country", mailCountry.getSelectedItem().toString());
        jsonObject.addProperty("mail_zipCode", mailZipCode.getText().toString());
        jsonObject.addProperty("business_contact_no", bussPhoneNo.getText().toString().trim());
        jsonObject.addProperty("business_email", businessEmail.getText().toString().trim());
        jsonObject.addProperty("contact_person_name", primaryName.getText().toString().trim());
        jsonObject.addProperty("contact_person_no", smartPhoneNo.getText().toString().trim());
        jsonObject.addProperty("other_phone_number", otherPhoneNo.getText().toString().trim());
        jsonObject.addProperty("latitude", latitude);
        jsonObject.addProperty("longitude", longitude);
        JsonCallMethod(jsonObject);
        Log.d(TAG, "Request from updateBusiness 42C :- " + jsonObject);
    }

    @SuppressLint("LongLogTag")
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
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, response.body().toString());
                String s = response.body().toString();
                Log.d(TAG + "response", s);
                Log.e(TAG, "Response of updateBusiness 42C :- " + response.body().toString());

                //JSONObject jsonObject = new JSONObject(s);
                JsonObject jsonObject = response.body();
                if (jsonObject.get("status").getAsString().equalsIgnoreCase("200")) {
                      /*  AppPreferencesBuss.setRegistrationSno42C(getActivity(), "Submit");
                        AppPreferencesBuss.setRegistrationSno42A(getActivity(), "");
                       */
                    Fragment fragment = new BusinessInformationNextFragment();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                    fragmentTransaction.commitAllowingStateLoss();
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

    @SuppressLint("LongLogTag")
    private void getData(JsonObject jsonObject) {

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
                Log.e(TAG, "Response of GETDATA >> " + response.body().toString());

                //JSONObject jsonObject = new JSONObject(s);
                JsonObject jsonObject = response.body();
                if (jsonObject.get("status").getAsString().equalsIgnoreCase("200")) {
                    JsonObject jsonObject1 = jsonObject.get("result").getAsJsonObject();
                    street.setText(jsonObject1.get("street").getAsString());
                    if (!jsonObject1.get("mail_street").getAsString().equalsIgnoreCase("0"))
                        mailStreet.setText(jsonObject1.get("mail_street").getAsString());
                    zipCode.setText(jsonObject1.get("zipCode").getAsString());
                    if (!jsonObject1.get("mail_zipCode").getAsString().equalsIgnoreCase("0"))
                        mailZipCode.setText(jsonObject1.get("mail_zipCode").getAsString());
                    primaryName.setText(jsonObject1.get("contact_person_name").getAsString());
                    smartPhoneNo.setText(jsonObject1.get("contact_person_no").getAsString());
                    otherPhoneNo.setText(jsonObject1.get("other_phone_number").getAsString());
                    businessEmail.setText(jsonObject1.get("business_email").getAsString());
                    bussPhoneNo.setText(jsonObject1.get("business_contact_no").getAsString());
                    nearestLocality.setText(jsonObject1.get("locality").getAsString());
                    phy_country = jsonObject1.get("country").getAsString();
                    phy_state = jsonObject1.get("state").getAsString();
                    phy_city = jsonObject1.get("city").getAsString();

                    mail_country = jsonObject1.get("mail_country").getAsString();
                    mail_state = jsonObject1.get("mail_state").getAsString();
                    mail_city = jsonObject1.get("mail_city").getAsString();

                    latitude = jsonObject1.get("latitude").getAsString();
                    longitude = jsonObject1.get("longitude").getAsString();

                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).equalsIgnoreCase(phy_country)) {
                            spinner_country.setSelection(i);
                            break;
                        }
                    }

                    for (int i = 0; i < stateList.size(); i++) {
                        if (stateList.get(i).equalsIgnoreCase(phy_state)) {
                            spinner_state.setSelection(i);
                            break;
                        }
                    }

                    for (int i = 0; i < cityList.size(); i++) {
                        if (cityList.get(i).equalsIgnoreCase(phy_city)) {
                            spinner_city.setSelection(i);
                            break;
                        }
                    }

                    //for mailing address
                    for (int i = 0; i < mailcountries.size(); i++) {
                        if (mailcountries.get(i).equalsIgnoreCase(mail_country)) {
                            mailCountry.setSelection(i);
                            break;
                        }
                    }

                    for (int i = 0; i < mailstateList.size(); i++) {
                        if (mailstateList.get(i).equalsIgnoreCase(mail_state)) {
                            mailState.setSelection(i);
                            break;
                        }
                    }

                    for (int i = 0; i < mailstateList.size(); i++) {
                        if (mailstateList.get(i).equalsIgnoreCase(mail_city)) {
                            mailCity.setSelection(i);
                            break;
                        }
                    }
                    progressHD.dismiss();
                } else {
                    progressHD.dismiss();
                    String msg = jsonObject.get("status").getAsString();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error:- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }
}
