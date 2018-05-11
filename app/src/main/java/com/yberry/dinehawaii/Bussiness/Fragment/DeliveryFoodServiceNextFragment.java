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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.JsonArray;
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
 * Created by PRINCE 9977123453 on 03-02-17.
 */

public class DeliveryFoodServiceNextFragment extends Fragment implements View.OnClickListener {

    JsonArray elements, elements1;
    String driver_id, minutes_per_mile, fname, lname, dine_id, phone_number, maximum_miles, minute_order,business_name;
    private ImageView imageView;
    private Context mContext;
    private Button btnAddmore, btnAdd2;
    private RadioButton radioMinPerMills;
    private CustomEditText business_id,etFirstName, etLastName, etDineId, etSmartPhno, etMaximumAllow, etMills, etPerMilles, etAllOrder, etFirstName2, etLastName2, etDineId2, etSmartPhno2, etMills2, etPerMilles2, etFirstName3, etLastName3, etDineId3, etSmartPhno3, etMills3, etPerMilles3;
    private LinearLayout linear1, linear2, linear3, linearRadio1, linearRadio2, linearRadio3;
    private int count = 1, flag = 1;
    private int where = 0, where1 = 0;
    private String TAG = "DELIRYFOODNEXT", optionalValue = "0";
    FragmentIntraction intraction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_delivery_food_next, container, false);
        //headText.setText("SERVICE-DELIVERY");
    //    headText.setText("SERVICE-DELIVERY FOOD SERVICE");
        if (intraction != null) {
            intraction.actionbarsetTitle("Service-Delivery Food Service");
        }
        getJsonDate();
        initComponent(view);

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

    private void initComponent(View view) {
        elements = new JsonArray();
        elements1 = new JsonArray();
        mContext = this.getContext();

        imageView = (ImageView) view.findViewById(R.id.imageview);
        business_id = (CustomEditText) view.findViewById(R.id.business_id);
        btnAddmore = (Button) view.findViewById(R.id.btnAddmore);
        btnAdd2 = (Button) view.findViewById(R.id.btnAdd2);
        radioMinPerMills = (RadioButton) view.findViewById(R.id.radioMinPerMills);

        linear1 = (LinearLayout) view.findViewById(R.id.parentlinear1);
        linear2 = (LinearLayout) view.findViewById(R.id.parentlinear2);
        linear3 = (LinearLayout) view.findViewById(R.id.parentlinear3);

        linearRadio1 = (LinearLayout) view.findViewById(R.id.radiolinear1);
        linearRadio2 = (LinearLayout) view.findViewById(R.id.radiolinear2);
        linearRadio3 = (LinearLayout) view.findViewById(R.id.radiolinear3);

        etFirstName = (CustomEditText) view.findViewById(R.id.tvfirstname);
        etLastName = (CustomEditText) view.findViewById(R.id.tvlastname);
        etDineId = (CustomEditText) view.findViewById(R.id.tvDineppId);
        etSmartPhno = (CustomEditText) view.findViewById(R.id.tvSmartphNo);
        etMaximumAllow = (CustomEditText) view.findViewById(R.id.tvMaximun);
        etMills = (CustomEditText) view.findViewById(R.id.etMills);
        etPerMilles = (CustomEditText) view.findViewById(R.id.etPerMilles);
        etAllOrder = (CustomEditText) view.findViewById(R.id.etAllOrder);

        etFirstName2 = (CustomEditText) view.findViewById(R.id.tvfirstname2);
        etLastName2 = (CustomEditText) view.findViewById(R.id.tvlastname2);
        etDineId2 = (CustomEditText) view.findViewById(R.id.tvDineppId2);
        etSmartPhno2 = (CustomEditText) view.findViewById(R.id.tvSmartphNo2);
        etMills2 = (CustomEditText) view.findViewById(R.id.etMills2);
        etPerMilles2 = (CustomEditText) view.findViewById(R.id.etPerMilles2);

        etFirstName3 = (CustomEditText) view.findViewById(R.id.tvfirstname3);
        etLastName3 = (CustomEditText) view.findViewById(R.id.tvlastname3);
        etDineId3 = (CustomEditText) view.findViewById(R.id.tvDineppId3);
        etSmartPhno3 = (CustomEditText) view.findViewById(R.id.tvSmartphNo3);
        etMills3 = (CustomEditText) view.findViewById(R.id.etMills3);
        etPerMilles3 = (CustomEditText) view.findViewById(R.id.etPerMilles3);


        imageView.setOnClickListener(this);
        btnAddmore.setOnClickListener(this);
        btnAdd2.setOnClickListener(this);

        radioMinPerMills.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (radioMinPerMills.isChecked()) {
                    optionalValue = "1";

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageview) {
            if (Util.isNetworkAvailable(mContext)) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.DELIVERY_FOOD_SERVICE_API.DRIVERDELVFOODSERVICE);
                getFirstetValue();
                jsonObject.add("totalDriver", elements.getAsJsonArray());
                secondArrayValue();
                jsonObject.add("totalDriverMinMile", elements1.getAsJsonArray());
                Log.e("OBJECT", jsonObject.toString());
                JsonCallMethod(jsonObject);

                Fragment fragment = new ServiceContractorFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);



                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            } else {
                Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

            }
        }

        if (v.getId() == R.id.btnAddmore) {
            for (int i = 0; i <= count; i++) {
                if (count == 1) {
                    linear2.setVisibility(View.VISIBLE);
                    where = 5;

                } else if (count == 2) {
                    btnAddmore.setVisibility(View.GONE);
                    linear3.setVisibility(View.VISIBLE);
                    where = 8;
                }
                count++;
                break;
            }

        }
        if (v.getId() == R.id.btnAdd2) {
            for (int i = 0; i <= flag; i++) {
                if (flag == 1) {
                    linearRadio2.setVisibility(View.VISIBLE);
                    where1 = 9;

                } else if (flag == 2) {
                    btnAdd2.setVisibility(View.GONE);
                    linearRadio3.setVisibility(View.VISIBLE);
                    where1 = 10;
                }
                flag++;
                break;
            }

        }
    }

    private void secondArrayValue() {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("miles", etMills.getText().toString());
        jsonObject2.addProperty("minutes", etPerMilles.getText().toString());
        elements1.add(jsonObject2);
        if (where1 == 9) {
            getSecondArrayValue();
        }
        if (where1 == 10) {
            getSecondArrayValue();
            getThirdArrayValue();
        }
    }

    private void getThirdArrayValue() {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("miles", etMills3.getText().toString());
        jsonObject2.addProperty("minutes", etPerMilles3.getText().toString());
        elements1.add(jsonObject2);
    }

    private void getSecondArrayValue() {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("miles", etMills2.getText().toString());
        jsonObject2.addProperty("minutes", etPerMilles2.getText().toString());
        elements1.add(jsonObject2);
    }


    private void getEditTextSecondValue() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject1.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject1.addProperty("first_name", etFirstName2.getText().toString().trim());
        jsonObject1.addProperty("last_name", etLastName2.getText().toString().trim());
        jsonObject1.addProperty("dine_app_id", etDineId2.getText().toString().trim());
        jsonObject1.addProperty("smart_phone_number", etSmartPhno2.getText().toString().trim());
        jsonObject1.addProperty("maximum_allowed_milage", "50");
        jsonObject1.addProperty("maximum_allowed_miles", etMaximumAllow.getText().toString().trim());
        jsonObject1.addProperty("minutes_per_mile", optionalValue);//radiobutton value
        jsonObject1.addProperty("minute_for_all_order", etAllOrder.getText().toString().trim());
        elements.add(jsonObject1);
        Log.d("JSONARRAYVALUE2", elements.toString());


    }

    private void getEditTextThirdValue() {

        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject1.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject1.addProperty("first_name", etFirstName3.getText().toString().trim());
        jsonObject1.addProperty("last_name", etLastName3.getText().toString().trim());
        jsonObject1.addProperty("dine_app_id", etDineId3.getText().toString().trim());
        jsonObject1.addProperty("smart_phone_number", etSmartPhno3.getText().toString().trim());
        jsonObject1.addProperty("maximum_allowed_milage", "50");
        jsonObject1.addProperty("maximum_allowed_miles", etMaximumAllow.getText().toString().trim());
        jsonObject1.addProperty("minutes_per_mile", optionalValue);//radiobutton value
        jsonObject1.addProperty("minute_for_all_order", etAllOrder.getText().toString().trim());
        elements.add(jsonObject1);
        Log.d("JSONARRAYVALUE3", elements.toString());
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
        Call<JsonObject> call = apiService.delivery_food_service_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseDelrServiceNext", response.body().toString());
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
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    private void getFirstetValue() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject1.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject1.addProperty("first_name", etFirstName.getText().toString().trim());
        jsonObject1.addProperty("last_name", etLastName.getText().toString().trim());
        jsonObject1.addProperty("dine_app_id", etDineId.getText().toString().trim());
        jsonObject1.addProperty("smart_phone_number", etSmartPhno.getText().toString().trim());
        jsonObject1.addProperty("maximum_allowed_milage", "50");
        jsonObject1.addProperty("maximum_allowed_miles", etMaximumAllow.getText().toString().trim());
        jsonObject1.addProperty("minutes_per_mile", optionalValue);//radiobutton value
        jsonObject1.addProperty("minute_for_all_order", etAllOrder.getText().toString().trim());
        elements.add(jsonObject1);
        Log.d("JSONARRAYVALUE1", elements.toString());

        if (where == 5) {
            getEditTextSecondValue();
        } else if (where == 8) {
            getEditTextSecondValue();
            getEditTextThirdValue();
        }
    }

    private void ShowJsonData(JsonObject jsonObject) {
       /* final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG + "onDeliverService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("drivers");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            driver_id = jsonObject1.getString("driver_id");
                            fname = jsonObject1.getString("first_name");
                            lname = jsonObject1.getString("last_name");
                            dine_id = jsonObject1.getString("dine_app_id");
                            phone_number = jsonObject1.getString("smart_phone_number");
                            maximum_miles = jsonObject1.getString("maximum_allowed_miles");
                            minutes_per_mile = jsonObject1.getString("minutes_per_mile");
                            minute_order = jsonObject1.getString("minute_for_all_order");
                            business_name = jsonObject1.getString("business_name");

                            etFirstName.setText(fname);
                            etLastName.setText(lname);
                            etDineId.setText(dine_id);
                            etSmartPhno.setText(phone_number);
                            etMaximumAllow.setText(maximum_miles);
                            etAllOrder.setText(minute_order);
                            business_id.setText(business_name);
                            etPerMilles.setText(minutes_per_mile);
                            if (minutes_per_mile.equalsIgnoreCase("1")) {
                                radioMinPerMills.setChecked(true);
                            } else if (minutes_per_mile.equalsIgnoreCase("0")) {
                                radioMinPerMills.setChecked(false);
                            } else {
                                radioMinPerMills.setChecked(false);
                            }

                        }

                        JSONArray jsonArray1 = jsonObject.getJSONArray("details");
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject object = jsonArray1.getJSONObject(j);
                            JSONArray jsonArray2 = object.getJSONArray("29");
                            for (int k = 0; k < jsonArray2.length(); k++) {
                                JSONObject object1 = jsonArray2.getJSONObject(k);
                                object1.getString("id");
                                String miles = object1.getString("miles");
                                String permills = object1.getString("minutes");

                                etMills.setText(miles);
                                etPerMilles.setText(permills);

                            }

                        }


                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_LONG).show();
                        Log.d("onResponse", jsonObject1.getString("msg"));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                //progressHD.dismiss();
            }
        });
    }

    private void getJsonDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "BusinessView47D");
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));//, AppPreferencesBuss.getBussiId(getActivity()));
      /*  jsonObject.addProperty("business_id", "1");
        jsonObject.addProperty("user_id", "13");
*/
        Log.e(TAG, jsonObject.toString());
        ShowJsonData(jsonObject);

    }
}