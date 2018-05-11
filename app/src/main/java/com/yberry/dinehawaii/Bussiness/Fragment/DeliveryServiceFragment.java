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

public class DeliveryServiceFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "DeliveryService";
    JsonArray totalCost;
    Context context;
    JsonArray jsonArray;
    JsonObject jsonObject;
    RadioButton radio1, radio2, radio3, radio4;
    private ImageView imageView;
    private RadioGroup radioGroup;
    private LinearLayout llChose, llChooseB, llChooseC, llChooseD, llChooseE;
    private CustomEditText etFlat, etPercent, etRate, et_from_km, et_to_km, et_total_cost, et_E_from_km, et_E_to_km, et_E_total_cost,
            et_D_from_km, et_D_to_km, et_D_total_cost, et_C_from_km, et_C_to_km, et_C_total_cost, et_B_from_km, et_B_to_km, et_B_total_cost, etFlat3;
    private Button btnAddmore;
    private int count = 1;
    private int where = 5;
    private CustomTextView serviceType, min;
    private int id;
    private String valueService;
    private Context mContext;
    FragmentIntraction intraction;

    public DeliveryServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_delivery_service, container, false);
//        headText.setText("DISTRIBUTOR & FOOD SUPPLIERS");
        if (intraction != null) {
            intraction.actionbarsetTitle("Distributor & Food Suppliers");
        }
       // headText.setText("DELIVERYSERVICE-DISTRIBUTOR & FOOD SUPPLIERS");
        getDeliveryJsonDate();
        context = getContext();
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
                llChooseB.setVisibility(View.GONE);
                llChooseC.setVisibility(View.GONE);
                llChooseD.setVisibility(View.GONE);
                llChooseE.setVisibility(View.GONE);

                //  apiCallig();
                if (id == R.id.radio1) {

                    etFlat.setEnabled(true);
                    etFlat.setActivated(true);
                    etFlat.setFocusable(true);
                    etFlat.setFocusableInTouchMode(true);
                    etFlat.requestFocus();
                    etFlat.requestFocusFromTouch();

                    etPercent.setEnabled(false);
                    etRate.setEnabled(false);
                    Log.d(TAG, etFlat.getText().toString());
                } else if (id == R.id.radio2) {

                    etPercent.setEnabled(true);
                    etPercent.setActivated(true);
                    etPercent.setFocusable(true);
                    etPercent.setFocusableInTouchMode(true);
                    etPercent.requestFocus();
                    etPercent.requestFocusFromTouch();

                    etFlat.setEnabled(false);
                    etRate.setEnabled(false);

                    Log.d(TAG, etPercent.getText().toString());

                } else if (id == R.id.radio3) {

                    etRate.setEnabled(true);
                    etRate.setActivated(true);
                    etRate.setFocusable(true);
                    etRate.setFocusableInTouchMode(true);
                    etRate.requestFocus();
                    etRate.requestFocusFromTouch();


                    etPercent.setEnabled(false);
                    etPercent.setEnabled(false);
                    Log.d(TAG + "rate", etRate.getText().toString());

                } else if (id == R.id.radio4) {

                    llChose.setVisibility(View.VISIBLE);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCallig();
                Fragment fragment = new DeliveryServiceNextFragment();
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
        if (valueService != null) {
            if (Util.isNetworkAvailable(getActivity())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.BUSINESS_FOOD_VENDOR_API.DELIVERY_SUPPLIER_VENDOR);
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));// AppPreferencesBuss.getUserId(getActivity()));
                jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));//, AppPreferencesBuss.getBussiId(getActivity()));
                jsonObject.addProperty("service_types_id", valueService.trim());// valueService);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("delivery_fee", "20");//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("rate_for_all_order", etFlat.getText().toString().trim());//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("percent_food_order_amount", etPercent.getText().toString().trim());//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("rate_one_way_mile", etRate.getText().toString().trim());//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("scale_order_size", "1");//AppPreferencesBuss.getUserId(getApplicationContext()));
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
        Call<JsonObject> call = apiService.business_food_vendor_api(jsonObject);
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
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
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
        etFlat3 = (CustomEditText) view.findViewById(R.id.etFlat3);
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

        radio1 = (RadioButton) view.findViewById(R.id.radio1);
        radio2 = (RadioButton) view.findViewById(R.id.radio2);
        radio3 = (RadioButton) view.findViewById(R.id.radio3);
        radio4 = (RadioButton) view.findViewById(R.id.radio4);

        llChose = (LinearLayout) view.findViewById(R.id.llChooseA);
        llChooseB = (LinearLayout) view.findViewById(R.id.llChooseB);
        llChooseC = (LinearLayout) view.findViewById(R.id.llChooseC);
        llChooseD = (LinearLayout) view.findViewById(R.id.llChooseD);
        llChooseE = (LinearLayout) view.findViewById(R.id.llChooseE);
        btnAddmore = (Button) view.findViewById(R.id.btnAddmore);
        serviceType = (CustomTextView) view.findViewById(R.id.serviceType);
        min = (CustomTextView) view.findViewById(R.id.min);
        totalCost = new JsonArray();
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnAddmore) {
            Log.d(TAG, "RadioId" + String.valueOf(id));
            if (id == R.id.radio4) {
                addMoreValue();
            } else {
                Toast.makeText(getContext(), "Click To Sliding Scale Option", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.serviceType) {
            if (Util.isNetworkAvailable(mContext)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLSERVICES);
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
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
                Log.e(TAG + "error", t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });
    }

    private void addMoreValue() {
        for (int i = 0; i <= count; i++) {
            if (count == 1) {
                llChooseB.setVisibility(View.VISIBLE);
                Log.d(TAG + "Count=1", String.valueOf(count));
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

    private void ShowDeliveryJsonData(JsonObject jsonObject) {
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
                Log.e(TAG + "onService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            serviceType.setText(jsonObject1.getString("service_name"));
                            min.setText(jsonObject1.getString("delivery_fee"));
                            etFlat.setText(jsonObject1.getString("rate_for_all_order"));
                            etPercent.setText(jsonObject1.getString("percent_food_order_amount"));
                            etRate.setText(jsonObject1.getString("rate_one_way_mile"));
                            etFlat3.setText(jsonObject1.getString("scale_order_size"));
                            String radio_rate_order = jsonObject1.getString("radio_rate_for_all_order");
                            String radio_percent_order_amount = jsonObject1.getString("radio_percent_food_order_amount");
                            String radio_rate_mile = jsonObject1.getString("radio_rate_one_way_mile");
                            String radio_order_size = jsonObject1.getString("radio_scale_order_size");
                            Log.d("radio_rate_order",radio_rate_order);
                            Log.d("radio_percent_order_amount",radio_percent_order_amount);
                            Log.d("radio_rate_mile",radio_rate_mile);
                            Log.d("radio_order_size",radio_order_size);

                            if (radio_rate_order.equalsIgnoreCase("1")) {
                                radio1.setChecked(true);
                            } else if (radio_rate_order.equalsIgnoreCase("0")) {
                                radio1.setChecked(false);
                           }else if (radio_percent_order_amount.equalsIgnoreCase("1")) {
                                radio2.setChecked(true);
                            } else if (radio_rate_order.equalsIgnoreCase("0")) {
                                radio2.setChecked(false);
                            }




                            if (radio_rate_mile.equalsIgnoreCase("1")) {
                                 radio3.setChecked(true);
                                } else if (radio_rate_mile.equalsIgnoreCase("0")) {
                                  radio3.setChecked(false);
                             }

                            if (radio_order_size.equalsIgnoreCase("1")) {
                                 radio4.setChecked(true);
                            } else if (radio_order_size.equalsIgnoreCase("0")) {
                                  radio4.setChecked(false);
                            }
                        }
                        JSONArray jsonArray1 = jsonObject.getJSONArray("other_detail");
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);

                            if (j == 0) {
                                llChose.setVisibility(View.VISIBLE);
                                et_from_km.setText(jsonObject2.getString("from_km"));
                                et_to_km.setText(jsonObject2.getString("to_km"));
                                et_total_cost.setText(jsonObject2.getString("total_cost"));
                            } else if (j == 1) {
                                llChooseB.setVisibility(View.VISIBLE);
                                et_B_from_km.setText(jsonObject2.getString("from_km"));
                                et_B_to_km.setText(jsonObject2.getString("to_km"));
                                et_B_total_cost.setText(jsonObject2.getString("total_cost"));
                            } else if (j == 2) {
                                llChooseC.setVisibility(View.VISIBLE);
                                et_C_from_km.setText(jsonObject2.getString("from_km"));
                                et_C_to_km.setText(jsonObject2.getString("to_km"));
                                et_C_total_cost.setText(jsonObject2.getString("total_cost"));
                            } else if (j == 3) {
                                llChooseD.setVisibility(View.VISIBLE);
                                et_D_from_km.setText(jsonObject2.getString("from_km"));
                                et_D_to_km.setText(jsonObject2.getString("to_km"));
                                et_D_total_cost.setText(jsonObject2.getString("total_cost"));
                            } else if (j == 4) {
                                llChooseE.setVisibility(View.VISIBLE);
                                et_E_from_km.setText(jsonObject2.getString("from_km"));
                                et_E_to_km.setText(jsonObject2.getString("to_km"));
                                et_E_total_cost.setText(jsonObject2.getString("total_cost"));
                            }


                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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

    private void getDeliveryJsonDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "BusinessView47A");
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
/*
        jsonObject.addProperty("user_id", "13");// AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id","1");//, AppPreferencesBuss.getBussiId(getActivity()));
*/
        Log.e(TAG, jsonObject.toString());
        ShowDeliveryJsonData(jsonObject);

    }
}
