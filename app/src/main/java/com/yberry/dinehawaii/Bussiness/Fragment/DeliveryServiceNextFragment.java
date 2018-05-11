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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yberry.dinehawaii.customview.CustomTextView;

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

public class DeliveryServiceNextFragment extends Fragment implements View.OnClickListener {
    private ImageView imageView;
    String fname, lname, dine_id, phone_number, maximum_milage;
    private CustomTextView tvAddMore, addMoreButton;
    private LinearLayout linearSecond, linearThird, linearLyaout3;
    private int Count = 1;
    private int where = 3;
    private CustomEditText  tvfirstname2,tvlastname2,tvDineppId2,tvSmartphNo2,tvbusinessName,
    etFirstName, etLastName, etDineId, etSmartPhno, etMaximumAllow, tvfirstname1, tvLastname1, tvDineAppid1, tvSmartphno1, tvMaximum1, tvfirstname23, tvLastname23, tvDineAppid23, tvSmartphno23, tvMaximum23;
    private Context mContext;
    private String TAG = "DELIVERY SERVICE NEXT";
    JsonArray elements;
    FragmentIntraction intraction;

    public DeliveryServiceNextFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_delivery_services_next, container, false);
        getJsonDate();
//        headText.setText("DISTRIBUTOR & FOOD SUPPLIERS");
        if (intraction != null) {
            intraction.actionbarsetTitle("Distributor & Food Suppliers");
        }
        LinearLayout mainView = (LinearLayout) view.findViewById(R.id.linearOne);
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

        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        mContext = this.getContext();
        elements = new JsonArray();
        imageView = (ImageView) view.findViewById(R.id.imageview);
        etFirstName = (CustomEditText) view.findViewById(R.id.tvfirstname);
        etLastName = (CustomEditText) view.findViewById(R.id.tvlastname);
        etDineId = (CustomEditText) view.findViewById(R.id.tvDineppId);
        etSmartPhno = (CustomEditText) view.findViewById(R.id.tvSmartphNo);
        etMaximumAllow = (CustomEditText) view.findViewById(R.id.tvMaximun);
        linearSecond = (LinearLayout) view.findViewById(R.id.secondlinear);
        linearThird = (LinearLayout) view.findViewById(R.id.thirdLinear);
        linearLyaout3 = (LinearLayout) view.findViewById(R.id.linearLyaout3);
        tvAddMore = (CustomTextView) view.findViewById(R.id.tvAddmore1);

        tvfirstname1 = (CustomEditText) view.findViewById(R.id.tvfirstname12);
        tvLastname1 = (CustomEditText) view.findViewById(R.id.tvlastname12);
        tvDineAppid1 = (CustomEditText) view.findViewById(R.id.tvDineppId12);
        tvSmartphno1 = (CustomEditText) view.findViewById(R.id.tvSmartphNo12);
        tvMaximum1 = (CustomEditText) view.findViewById(R.id.tvMaximun12);

        tvfirstname23 = (CustomEditText) view.findViewById(R.id.tvfirstname13);
        tvLastname23 = (CustomEditText) view.findViewById(R.id.tvlastname13);
        tvDineAppid23 = (CustomEditText) view.findViewById(R.id.tvDineppId13);
        tvSmartphno23 = (CustomEditText) view.findViewById(R.id.tvSmartphNo13);
        tvMaximum23 = (CustomEditText) view.findViewById(R.id.tvMaximun13);
        tvbusinessName = (CustomEditText) view.findViewById(R.id.tvbusinessName);





        tvfirstname2 = (CustomEditText) view.findViewById(R.id.tvfirstname2);
        tvlastname2 = (CustomEditText) view.findViewById(R.id.tvlastname2);
        tvDineppId2 = (CustomEditText) view.findViewById(R.id.tvDineppId2);
        tvSmartphNo2 = (CustomEditText) view.findViewById(R.id.tvSmartphNo2);

        addMoreButton = (CustomTextView) view.findViewById(R.id.addMoreButton);
        tvAddMore.setOnClickListener(this);

        imageView.setOnClickListener(this);

        addMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoreButton.setVisibility(View.GONE);
                linearLyaout3.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageview) {

            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject1.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject1.addProperty("first_name", etFirstName.getText().toString().trim());
            jsonObject1.addProperty("last_name", etLastName.getText().toString().trim());
            jsonObject1.addProperty("dine_app_id", etDineId.getText().toString().trim());
            jsonObject1.addProperty("smart_phone_number", etSmartPhno.getText().toString().trim());
            jsonObject1.addProperty("maximum_allowed_milage", etMaximumAllow.getText().toString().trim());
            elements.add(jsonObject1);
            Log.d("JSONARRAYVALUE", elements.toString());
            if (Util.isNetworkAvailable(mContext)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.BUSINESS_FOOD_VENDOR_API.DRIVER_SUPPLIER_VENDOR);
                getEditTextValue();
                jsonObject.add("totalDriver", elements.getAsJsonArray());
                Log.e("OBJECT", jsonObject.toString());
                JsonCallMethod(jsonObject);
                Fragment fragment = new DeliveryFoodServiceFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);


                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            } else {
                Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.tvAddmore1) {
            //  linearSecond.setVisibility(View.VISIBLE);
            for (int i = 0; i <= Count; i++) {
                if (Count == 1) {
                    linearSecond.setVisibility(View.VISIBLE);
                    where=7;

                } else if (Count == 2) {
                    tvAddMore.setVisibility(View.GONE);
                    linearThird.setVisibility(View.VISIBLE);
                    where=8;
                }
                Count++;
                break;
            }
        }


    }

    private void getEditTextValue() {
        if(where==7){
            getEditTextValueFirstLayout();

        }else if (where==8){
            getEditTextValueFirstLayout();
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject2.addProperty("user_id",  AppPreferencesBuss.getUserId(getActivity()));
            jsonObject2.addProperty("first_name", tvfirstname23.getText().toString().trim());
            jsonObject2.addProperty("last_name", tvLastname23.getText().toString().trim());
            jsonObject2.addProperty("dine_app_id", tvDineAppid23.getText().toString().trim());
            jsonObject2.addProperty("smart_phone_number", tvSmartphno23.getText().toString().trim());
            jsonObject2.addProperty("maximum_allowed_milage", tvMaximum23.getText().toString().trim());
            elements.add(jsonObject2);
            Log.d("Addmore2", jsonObject2.toString());

        }
    }

    private void getEditTextValueFirstLayout() {
        if(tvfirstname1.getText().toString().trim().isEmpty()){
            tvfirstname1.setError("please enter your first name");
        }else if (tvLastname1.getText().toString().trim().isEmpty()){
            tvLastname1.setError("please enter your last name");
        }else if(tvDineAppid1.getText().toString().trim().isEmpty()){
            tvDineAppid1.setError("please enter your dine-id");

        }else if(tvSmartphno1.getText().toString().trim().isEmpty()){
            tvSmartphno1.setError("please enter your smartphno.");
        }else if(tvMaximum1.getText().toString().trim().isEmpty()){
            tvMaximum1.setError("please enter Maximum Allowed Millage");
        }else {

            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject1.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject1.addProperty("first_name", tvfirstname1.getText().toString().trim());
            jsonObject1.addProperty("last_name", tvLastname1.getText().toString().trim());
            jsonObject1.addProperty("dine_app_id", tvDineAppid1.getText().toString().trim());
            jsonObject1.addProperty("smart_phone_number", tvSmartphno1.getText().toString().trim());
            jsonObject1.addProperty("maximum_allowed_milage", tvMaximum1.getText().toString().trim());
            elements.add(jsonObject1);
            Log.d("Addmore1", jsonObject1.toString());
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
        Call<JsonObject> call = apiService.business_food_vendor_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseDeliverService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg=jsonObject1.getString("msg");
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();

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
                Log.e(TAG , "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
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
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            if(i==0){
                                etFirstName.setText(jsonObject1.getString("first_name"));
                                etLastName.setText(jsonObject1.getString("last_name"));
                                etDineId.setText(jsonObject1.getString("dine_app_id"));
                                etSmartPhno.setText(jsonObject1.getString("smart_phone_number"));
                                etMaximumAllow.setText( jsonObject1.getString("maximum_allowed_milage"));
                                tvbusinessName.setText(jsonObject1.getString("business_name"));

                            }else if(i == 1){
                                tvfirstname2.setText(jsonObject1.getString("first_name"));
                                tvlastname2.setText(jsonObject1.getString("last_name"));
                                etDineId.setText(jsonObject1.getString("dine_app_id"));
                                tvDineppId2.setText(jsonObject1.getString("smart_phone_number"));
                                tvSmartphNo2.setText( jsonObject1.getString("maximum_allowed_milage"));
                                tvbusinessName.setText(jsonObject1.getString("business_name"));



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
                Log.e(TAG , "Error On failure :- " + Log.getStackTraceString(t));
                //progressHD.dismiss();
            }
        });
    }

    private void getJsonDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "BusinessView47B");
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));// AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));//, AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, jsonObject.toString());
        ShowJsonData(jsonObject);

    }

}
