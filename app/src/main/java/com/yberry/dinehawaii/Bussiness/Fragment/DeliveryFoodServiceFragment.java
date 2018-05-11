package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.Bussiness.Activity.SelectServiceTypeActivity;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 03-02-17.
 */

public class DeliveryFoodServiceFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "DeliveryService";
    JsonArray totalCost;
    Context context;
    JsonArray jsonArray;
    JsonObject jsonObject;
    String flatString = "0", percentString = "0", rateString = "0";
    private RadioGroup radioGroup;
    private LinearLayout llChose, llChooseB, llChooseC, llChooseD, llChooseE;
    private CustomEditText etFlat, etPercent, etRate, et_from_km, et_to_km, et_total_cost, et_E_from_km, et_E_to_km, et_E_total_cost,
            et_D_from_km, et_D_to_km, et_D_total_cost, et_C_from_km, et_C_to_km, et_C_total_cost, et_B_from_km, et_B_to_km, et_B_total_cost, etFlat3;
    private Button btnAddmore;
    private int count = 1;
    private int where = 5;
    private CustomTextView serviceType,min,totaltckt,gratuty_amt,drivername;
    private int id;
    private String valueService, radioButtonDreiver, valueTotalTicket;
    private Context mContext;
    private ImageView imageView;
    private RadioGroup rgDriverName, rgTotalTicket;
    private RadioButton del_yes,del_no,radio_order_size,radiobox_rate_order,radiobox_percent_order_amount,radiobox_one_way_mile,grat_yes,grat_no;

    FragmentIntraction intraction;

    public DeliveryFoodServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_delivery_food, container, false);
       // headText.setText("SERVICE-DELIVERY");
        if (intraction != null) {
            intraction.actionbarsetTitle("Service-Delivery");
        }
        getDeliveryJsonDate();
        LinearLayout mainView = (LinearLayout) view.findViewById(R.id.mainView);
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
        mContext = getContext();
        init(view);
        jsonObject = new JsonObject();
        clicklListners(view);

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

    private void clicklListners(final View view) {
        btnAddmore.setOnClickListener(this);
        serviceType.setOnClickListener(this);
//rgDriverName,rgTotalTicket
        rgDriverName.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                radioButtonDreiver = radioButton.getText().toString();
            }
        });
        rgTotalTicket.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                valueTotalTicket = radioButton.getText().toString();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                id = radioGroup.getCheckedRadioButtonId();
                Log.d(TAG, String.valueOf(id));
                RadioButton button = (RadioButton) view.findViewById(id);
                etFlat.setText("");
                etPercent.setText("");
                etRate.setText("");

                llChose.setVisibility(View.GONE);
                //  apiCallig();
                if (id == 5) {
                    etFlat.setEnabled(true);
                    etPercent.setEnabled(false);
                    etRate.setEnabled(false);
                    flatString = etFlat.getText().toString();
                    Log.d(TAG, "Flat rate :- " + etFlat.getText().toString());
                    Log.d(TAG, "Flat rate String :- " + flatString);
                } else if (id == 6) {
                    etPercent.setEnabled(true);
                    etFlat.setEnabled(false);
                    etRate.setEnabled(false);

                    percentString = etPercent.getText().toString();
                    Log.d(TAG, "percen rate :- " + etPercent.getText().toString());
                    Log.d(TAG, "percen rate String :- " + percentString);

                } else if (id == 7) {
                    etRate.setEnabled(true);
                    etPercent.setEnabled(false);
                    etPercent.setEnabled(false);

                    rateString = etRate.getText().toString();
                    Log.d(TAG, "Rate String :- " + rateString);
                    Log.d(TAG + "Rate", etRate.getText().toString());


                } else if (id == 8) {
                    llChose.setVisibility(View.VISIBLE);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallig();
                Fragment fragment = new DeliveryFoodServiceNextFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);


                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

    }

    private void apiCallig() {
        DeliveryService();
    }

    private void DeliveryService() {

        flatString = etFlat.getText().toString();
        Log.d(TAG, "Flat rate DeliveryService :- " + etFlat.getText().toString());
        Log.d(TAG, "DeliveryService Flat rate String :- " + flatString);

        if (valueService != null) {
            if (Util.isNetworkAvailable(getActivity())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.DELIVERY_FOOD_SERVICE_API.DELIVERYFOODSERVICE);
                jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                jsonObject.addProperty("service_type_id", valueService.trim());// valueService);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("delivery_fee", "20");//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("rate_for_all_order", flatString);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("percent_food_order_amount", percentString);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("rate_one_way_mile", rateString);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("scale_order_size", "1");//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("driver_ticket_cost", valueTotalTicket);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("driver_gratuity_cost", "15");//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("delv_driver_name", radioButtonDreiver);//AppPreferencesBuss.getUserId(getApplicationContext()));
                getEditTextValue();
                jsonObject.add("totalCost", totalCost.getAsJsonArray());//AppPreferencesBuss.getUserId(getApplicationContext()));
                Log.e("response", jsonObject.toString());
                Log.d(TAG + "Edit", etFlat.getText().toString());
                Log.d(TAG + "ValueS", valueService);
                JsonApiCallingDeliveryService(jsonObject);
            }
        } else {
            Toast.makeText(getContext(), "Please select any option", Toast.LENGTH_LONG).show();
        }
    }

    private void getEditTextValue() {
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("from_km", et_from_km.getText().toString());
        jsonObject2.addProperty("to_km", et_to_km.getText().toString());
        jsonObject2.addProperty("total_cost", et_total_cost.getText().toString());
        totalCost.add(jsonObject2);
        Log.d(TAG + "EDITText", et_from_km.getText().toString());
        if (where == 7) {
            editTextValueB();
        }
        if (where == 8) {
            editTextValueB();
            editTextValueC();
        } else if (where == 9) {
            editTextValueB();
            editTextValueC();
            editTextValueD();
        } else if (where == 10) {
            editTextValueB();
            editTextValueC();
            editTextValueD();
            editTextValueE();

        }
    }

    private void editTextValueE() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_km", et_E_from_km.getText().toString());
        jsonObject.addProperty("to_km", et_E_to_km.getText().toString());
        jsonObject.addProperty("total_cost", et_E_total_cost.getText().toString());
        totalCost.add(jsonObject);
        Log.d(TAG + "Count=4", String.valueOf(count));

    }

    private void editTextValueD() {
        JsonObject jsonObject5 = new JsonObject();
        jsonObject5.addProperty("from_km", et_D_from_km.getText().toString());
        jsonObject5.addProperty("to_km", et_D_to_km.getText().toString());
        jsonObject5.addProperty("total_cost", et_D_total_cost.getText().toString());
        totalCost.add(jsonObject5);
        Log.d(TAG + "Count=3", String.valueOf(count));

    }

    private void editTextValueC() {
        JsonObject jsonObject4 = new JsonObject();
        jsonObject4.addProperty("from_km", et_C_from_km.getText().toString());
        jsonObject4.addProperty("to_km", et_C_to_km.getText().toString());
        jsonObject4.addProperty("total_cost", et_C_total_cost.getText().toString());
        Log.d(TAG + "Count=2", String.valueOf(count));
        totalCost.add(jsonObject4);

    }

    private void editTextValueB() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_km", et_B_from_km.getText().toString());
        jsonObject.addProperty("to_km", et_B_to_km.getText().toString());
        jsonObject.addProperty("total_cost", et_B_total_cost.getText().toString());
        totalCost.add(jsonObject);

    }


    private void JsonApiCallingDeliveryService(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.delivery_food_service_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();

                Log.e(TAG, "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonArray = jsonObject.getJSONObject("result");
                        String message = jsonArray.getString("msg");
                        Log.d("message45", message);
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

    private void init(View view) {
        imageView = (ImageView) view.findViewById(R.id.imageview);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupDelivery);
        etFlat = (CustomEditText) view.findViewById(R.id.etFlat);
        etPercent = (CustomEditText) view.findViewById(R.id.etFlat1);//,,
        etRate = (CustomEditText) view.findViewById(R.id.etFlat2);
        et_from_km = (CustomEditText) view.findViewById(R.id.et_from_km);
        et_to_km = (CustomEditText) view.findViewById(R.id.et_to_km);
        et_total_cost = (CustomEditText) view.findViewById(R.id.et_total_cost);

        et_B_from_km = (CustomEditText) view.findViewById(R.id.et_B_from_km);
        et_B_to_km = (CustomEditText) view.findViewById(R.id.et_B_to_km);
        et_B_total_cost = (CustomEditText) view.findViewById(R.id.et_B_total_cost);

        et_E_from_km = (CustomEditText) view.findViewById(R.id.et_E_from_km);
        et_E_to_km = (CustomEditText) view.findViewById(R.id.et_E_to_km);
        et_E_total_cost = (CustomEditText) view.findViewById(R.id.et_E_total_cost);

        et_C_from_km = (CustomEditText) view.findViewById(R.id.et_C_from_km);
        et_C_to_km = (CustomEditText) view.findViewById(R.id.et_C_to_km);
        et_C_total_cost = (CustomEditText) view.findViewById(R.id.et_C_total_cost);

        et_D_from_km = (CustomEditText) view.findViewById(R.id.et_D_from_km);
        et_D_to_km = (CustomEditText) view.findViewById(R.id.et_D_to_km);
        et_D_total_cost = (CustomEditText) view.findViewById(R.id.et_D_total_cost);
        etFlat3 = (CustomEditText) view.findViewById(R.id.etFlat3);


        llChose = (LinearLayout) view.findViewById(R.id.llChooseA);
        llChooseB = (LinearLayout) view.findViewById(R.id.llChooseB);
        llChooseC = (LinearLayout) view.findViewById(R.id.llChooseC);
        llChooseD = (LinearLayout) view.findViewById(R.id.llChooseD);
        llChooseE = (LinearLayout) view.findViewById(R.id.llChooseE);
        btnAddmore = (Button) view.findViewById(R.id.btnAddmore);
        serviceType = (CustomTextView) view.findViewById(R.id.serviceType);
        min = (CustomTextView) view.findViewById(R.id.min);
        totaltckt = (CustomTextView) view.findViewById(R.id.totaltckt);
        gratuty_amt = (CustomTextView) view.findViewById(R.id.gratuty_amt);
        drivername = (CustomTextView) view.findViewById(R.id.drivername);

        //rgDriverName,rgTotalTicket
        rgDriverName = (RadioGroup) view.findViewById(R.id.rgDriverName);
        rgTotalTicket = (RadioGroup) view.findViewById(R.id.rgTotalTicket);


        radio_order_size = (RadioButton) view.findViewById(R.id.radio_order_size);
        radiobox_rate_order = (RadioButton) view.findViewById(R.id.radiobox_rate_order);
        radiobox_percent_order_amount = (RadioButton) view.findViewById(R.id.radiobox_percent_order_amount);
        radiobox_one_way_mile = (RadioButton) view.findViewById(R.id.radiobox_one_way_mile);
        grat_yes = (RadioButton) view.findViewById(R.id.grat_yes);
        grat_no = (RadioButton) view.findViewById(R.id.grat_no);


        del_yes = (RadioButton) view.findViewById(R.id.del_yes);
        del_no = (RadioButton) view.findViewById(R.id.del_no);



        totalCost = new JsonArray();
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnAddmore) {
            Log.d(TAG + "RadioId", String.valueOf(id));
            if (id == 4) {
                addMoreValue();
            } else {
                Toast.makeText(getContext(), "Click To Sliding Scale Option", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.serviceType) {
            if (Util.isNetworkAvailable(mContext)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLSERVICES);
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                Log.d(TAG + "Userid", AppPreferencesBuss.getUserId(getActivity()));
                Log.e(TAG, jsonObject.toString());
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
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseFoodType", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(mContext, SelectServiceTypeActivity.class);
                        intent.putExtra("ServerType", s);
                        startActivityForResult(intent, 101);
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

    private void addMoreValue() {

        for (int i = 0; i <= count; i++) {
            if (count == 1) {
                llChooseB.setVisibility(View.VISIBLE);
                Log.d(TAG + "Coun", String.valueOf(count));
                where = 7;
            } else if (count == 2) {
                llChooseC.setVisibility(View.VISIBLE);
                where = 8;
            } else if (count == 3) {
                llChooseD.setVisibility(View.VISIBLE);
                where = 9;
            } else if (count == 4) {
                llChooseE.setVisibility(View.VISIBLE);
                where = 10;
            }
            count++;
            break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                //  Log.e(TAG + "ServiceType", "OnActivtyResultFoodType");
                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("serviceFoodType");
//                Log.d("FoodServiceIntentValue", checkBoxPositionModel.getId());
                serviceType.setText(checkBoxPositionModel.getName());

                //jsonObject.put("serviceName", checkBoxPositionModel.getName());
                valueService = checkBoxPositionModel.getId();

            }


        }
    }

    @SuppressLint("LongLogTag")
   /* private void ShowDeliveryJsonData(JsonObject jsonObject) {
       *//* final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });*//*
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.bussines_service_view(jsonObject);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                         //   String service_name=jsonObject1.getString("service_name");
                           // String delivery_fee=jsonObject1.getString("delivery_fee");
                            String order=jsonObject1.getString("rate_for_all_order");
                            String order_amount=jsonObject1.getString("percent_food_order_amount");
                            String mile=jsonObject1.getString("rate_one_way_mile");
                            String size= jsonObject1.getString("scale_order_size");
                            String cost= jsonObject1.getString("driver_ticket_cost");
                            String gratuity_cost= jsonObject1.getString("driver_gratuity_cost");
                            String driver_name= jsonObject1.getString("delv_driver_name");

                            *//*AppPreferencesBuss.setServiceName(getActivity(),jsonObject1.getString("service_name"));

                            AppPreferencesBuss.setDeliverFee(getActivity(),jsonObject1.getString("delivery_fee"));


                           AppPreferencesBuss.setOrderAmount(getActivity(),jsonObject1.getString("rate_for_all_order"));


                           AppPreferencesBuss.setpercentage(getActivity(),jsonObject1.getString("percent_food_order_amount"));


*//*
                         //   Log.d("name",service_name);
                          //  Log.d("name1",delivery_fee);
                          *//*  Log.d("name2",order);
                            Log.d("name3",order_amount);
                            Log.d("name4",mile);
                            Log.d("name5",size);
                            Log.d("name6",cost);
                            Log.d("name7",gratuity_cost);
                            Log.d("name8",driver_name);

                        *//* //   serviceType.setText(String.valueOf(service_name));
                           // min.setText(String.valueOf(delivery_fee));


                          //  etFlat.setText(String.valueOf(order));
                            etPercent.setText(String.valueOf(order_amount));
                            etRate.setText(String.valueOf(mile));
                            etFlat3.setText(String.valueOf(size));
                            totaltckt.setText(String.valueOf(cost));
                            gratuty_amt.setText(String.valueOf(gratuity_cost));
                            drivername.setText(String.valueOf(driver_name));

                           *//* etFlat.setText(order);
                            etPercent.setText(order_amount);
                            etRate.setText(mile);
                            etFlat3.setText(size);
                            totaltckt.setText(cost);
                            gratuty_amt.setText(gratuity_cost);
                            drivername.setText(driver_name);*//*

                           *//* etFlat.setText(jsonObject1.getString("rate_for_all_order"));
                            etPercent.setText(jsonObject1.getString("percent_food_order_amount"));
                            etRate.setText(jsonObject1.getString("rate_one_way_mile"));
                            etFlat3.setText(jsonObject1.getString("scale_order_size"));
                            totaltckt.setText(jsonObject1.getString("driver_ticket_cost"));
                            gratuty_amt.setText(jsonObject1.getString("driver_gratuity_cost"));
                            drivername.setText(jsonObject1.getString("delv_driver_name"));
                       *//*     // radio_order_size,radiobox_rate_order,radiobox_percent_order_amount,radiobox_one_way_mile

String order_size = jsonObject1.getString("radiobox_rate_for_all_order");
String percent_order = jsonObject1.getString("radiobox_percent_food_order_amount");
String one_mile = jsonObject1.getString("radiobox_rate_one_way_mile");
String scale_order_size = jsonObject1.getString("radio_scale_order_size");


                            if(order_size.equalsIgnoreCase("1")){
                                radio_order_size.setChecked(true);
                            }else{
                                radio_order_size.setChecked(false);
                            }



                            if(percent_order.equalsIgnoreCase("1")){
                                radiobox_rate_order.setChecked(true);
                            }else{
                                radiobox_rate_order.setChecked(false);
                            }



                            if(one_mile.equalsIgnoreCase("1")){
                                radiobox_percent_order_amount.setChecked(true);
                            }else{
                                radiobox_percent_order_amount.setChecked(false);
                            }


                            if(scale_order_size.equalsIgnoreCase("1")){
                                radiobox_one_way_mile.setChecked(true);
                            }else{
                                radiobox_one_way_mile.setChecked(false);
                            }


                        }

                        JSONArray jsonArray1 = jsonObject.getJSONArray("DeliceryFee");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                               et_from_km.setText(jsonObject2.getString("from_km"));
                            et_to_km.setText(jsonObject2.getString("to_km"));
                            et_total_cost.setText(jsonObject2.getString("total_cost"));


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
*/
    private void getDeliveryJsonDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "BusinessView47C");
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));// AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));//, AppPreferencesBuss.getBussiId(getActivity()));

      /*  jsonObject.addProperty("user_id", "13");// AppPreferencesBuss.getUserId(getActivity()));13
        jsonObject.addProperty("business_id","1");//, AppPreferencesBuss.getBussiId(getActivity()));1
*/
        Log.e(TAG, jsonObject.toString());
        ShowDeliveryJsonData(jsonObject);

    }


    private void ShowDeliveryJsonData(JsonObject jsonObject) {
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.bussines_service_view(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            //  Log.e(TAG + "onService", response.body().toString());

                Log.d("onService", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String order = jsonObject1.getString("rate_for_all_order");
                            String food = jsonObject1.getString("percent_food_order_amount");
                            String rate = jsonObject1.getString("rate_one_way_mile");
                            String scale = jsonObject1.getString("scale_order_size");
                            String name = jsonObject1.getString("service_name");
                            String delivery = jsonObject1.getString("delivery_fee");
                            String gratuty = jsonObject1.getString("driver_gratuity_cost");

                            String radiobox_order =jsonObject1.getString("radiobox_rate_for_all_order");
                            if(radiobox_order.equalsIgnoreCase("1")){
                                radiobox_rate_order.setChecked(true);
                            }else{
                                radiobox_rate_order.setChecked(false);
                            }

                            String percent_amount =jsonObject1.getString("radiobox_percent_food_order_amount");
                            if(percent_amount.equalsIgnoreCase("1")){
                                radiobox_percent_order_amount.setChecked(true);
                            }else{
                                radiobox_percent_order_amount.setChecked(false);
                            }

                            String one_mile = jsonObject1.getString("radiobox_rate_one_way_mile");

                            if(one_mile.equalsIgnoreCase("1")){
                                radiobox_one_way_mile.setChecked(true);
                            }else{
                                radiobox_one_way_mile.setChecked(false);
                            }

                            String scale_order_size = jsonObject1.getString("radio_scale_order_size");
                            if(scale_order_size.equalsIgnoreCase("1")){
                                radio_order_size.setChecked(true);
                            }else{
                                radio_order_size.setChecked(false);
                            }


                            String tckt_cost =jsonObject1.getString("driver_ticket_cost");
                            if(tckt_cost.equalsIgnoreCase("yes")){
                                grat_yes.setChecked(true);
                              grat_no.setChecked(false);
                            }else{
                                grat_yes.setChecked(false);
                                grat_no.setChecked(false);

                            }

                            String driver_cost =jsonObject1.getString("delv_driver_name");
                            if(driver_cost.equalsIgnoreCase("yes")){
                                del_yes.setChecked(true);
                                del_no.setChecked(false);

                            }else{
                                del_no.setChecked(false);
                                del_yes.setChecked(false);

                            }


                           /* "radiobox_rate_for_all_order": "1",
                                    "radiobox_percent_food_order_amount": "0",
                                    "radiobox_rate_one_way_mile": "1",
                                    "radio_scale_order_size": "0"
*/
                            etFlat.setText(order);
                            etPercent.setText(food);
                            etRate.setText(rate);
                            etFlat3.setText(scale);
                            serviceType.setText(name);
                            min.setText(delivery);
                            gratuty_amt.setText(gratuty);

                        }

                        JSONArray jsonArray1 = jsonObject.getJSONArray("DeliceryFee");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                            et_from_km.setText(jsonObject2.getString("from_km"));
                            et_to_km.setText(jsonObject2.getString("to_km"));
                            et_total_cost.setText(jsonObject2.getString("total_cost"));


                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_LONG).show();
                        Log.d("onResponse", jsonObject1.getString("msg"));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));

            }
        });
    }


   /* @Override
    public void onResume() {
        super.onResume();
        serviceType.setText(AppPreferencesBuss.getServiceName(getActivity()));
        min.setText(AppPreferencesBuss.getDeliverFee(getActivity()));
        etFlat.setText(AppPreferencesBuss.getOrderAmount(getActivity()));
        etPercent.setText(AppPreferencesBuss.getpercentage(getActivity()));

    }*/
}
