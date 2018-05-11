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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
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

public class OnlineOrderingAndPaymentFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "OnlineOrdering";
    private ImageView imageView;
    private String data1;
    private CustomEditText etInHouse, etTakeOut, etCatering, etDelivery, etTakeoutOrder, etTakeoutDelivery, etInHouseBreakFast,
            etInHouseLunch, etInHouseDinner, etTakeOutBreakFast, etTakeOutLunch, etTakeOutDinner, etTakoutDeliveryBreakFast,
            etTakoutDeliveryLunch, etTakoutDeliveryDinner, etCateringBreakfast, etCateringLunch, etCateringDinner,
            etCatringPayment, etDeliveryVendor, etDeliveryVendor1, etDeliveryVendor2;
    private RadioGroup rgInHouse, rgTakeOut, rgCatering, rgDelivery, rgTakeOutOrder, rgTakeOutDeliveryOrder, rgCatringPayment, rgDeliveryServiceAvilable, rgDeliveryServiceAvilable1,
            rgDeliveryServiceAvilable2;
    private Context mContext;
    private String radioInHouseValue, radioTakeOut, radioCatring, radioDelivery, radioTakeOutOrder, radioTakeOutDeliveryOrder,
            radioCatringPayment, radioDeliveryServiceAvilable, radioDeliveryServiceAvilable1, radioDeliveryServiceAvilable2;
    private Button addMoreButton;
    private JsonArray jsonArray;
    private int cout = 1;
    private int where = 0;
    private RadioButton pre_order_no,pre_order_yes,pre_delvery_no,pre_delvery_yes,pre_catering_no,pre_catering_yes, service_yes, service_no, catering_yes, catering_no, take_out_yes, take_in_no, take_in, take_out, noradioMultiSiteBusiness, yesradioMultiSiteBusiness, deliver_out_yes, deliver_out_no, takeout_no, takeout_yes;
    FragmentIntraction intraction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_online_ordering_and_online_package, container, false);
//        headText.setText("ONLINE ORDERING & PAYMENT");
        if (intraction != null) {
            intraction.actionbarsetTitle("Online Ordering & Payment");
        }
        mContext = getActivity();
        showdata();
        init(view);
        onClickListenerRadioGroup(view);
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

    private void init(View view) {
        imageView = (ImageView) view.findViewById(R.id.imageview);
        etInHouse = (CustomEditText) view.findViewById(R.id.etInHouse);
        etTakeOut = (CustomEditText) view.findViewById(R.id.etTakeOut);
        etCatering = (CustomEditText) view.findViewById(R.id.etCatering);
        etDelivery = (CustomEditText) view.findViewById(R.id.etDelivery);
        etTakeoutOrder = (CustomEditText) view.findViewById(R.id.etTakeoutOrder);
        etTakeoutDelivery = (CustomEditText) view.findViewById(R.id.etTakeoutDelivery);
        etInHouseBreakFast = (CustomEditText) view.findViewById(R.id.etInHouseBreakFast);
        etInHouseLunch = (CustomEditText) view.findViewById(R.id.etInHouseLunch);
        etInHouseDinner = (CustomEditText) view.findViewById(R.id.etInHouseDinner);
        etTakeOutBreakFast = (CustomEditText) view.findViewById(R.id.etTakeOutBreakFast);
        etTakeOutLunch = (CustomEditText) view.findViewById(R.id.etTakeOutLunch);
        etTakeOutDinner = (CustomEditText) view.findViewById(R.id.etTakeOutDinner);
        etTakoutDeliveryBreakFast = (CustomEditText) view.findViewById(R.id.etTakoutDeliveryBreakFast);
        etTakoutDeliveryLunch = (CustomEditText) view.findViewById(R.id.etTakoutDeliveryLunch);
        etTakoutDeliveryDinner = (CustomEditText) view.findViewById(R.id.etTakoutDeliveryDinner);
        etCateringBreakfast = (CustomEditText) view.findViewById(R.id.etCateringBreakfast);
        etCateringLunch = (CustomEditText) view.findViewById(R.id.etCateringLunch);
        etCateringDinner = (CustomEditText) view.findViewById(R.id.etCateringDinner);
        etCatringPayment = (CustomEditText) view.findViewById(R.id.etCatringPayment);
        etDeliveryVendor = (CustomEditText) view.findViewById(R.id.etDeliveryVendor);
        etDeliveryVendor1 = (CustomEditText) view.findViewById(R.id.etDeliveryVendor1);
        etDeliveryVendor2 = (CustomEditText) view.findViewById(R.id.etDeliveryVendor2);

        rgInHouse = (RadioGroup) view.findViewById(R.id.rgInHouse);
        rgTakeOut = (RadioGroup) view.findViewById(R.id.rgTakeOut);
        rgCatering = (RadioGroup) view.findViewById(R.id.rgCatering);
        rgDelivery = (RadioGroup) view.findViewById(R.id.rgDelivery);
        rgTakeOutOrder = (RadioGroup) view.findViewById(R.id.rgTakeOutOrder);
        rgTakeOutDeliveryOrder = (RadioGroup) view.findViewById(R.id.rgTakeOutDeliveryOrder);
        rgCatringPayment = (RadioGroup) view.findViewById(R.id.rgCatringPayment);
        rgDeliveryServiceAvilable = (RadioGroup) view.findViewById(R.id.rgDeliveryServiceAvilable);
        rgDeliveryServiceAvilable1 = (RadioGroup) view.findViewById(R.id.rgDeliveryServiceAvilable1);
        rgDeliveryServiceAvilable2 = (RadioGroup) view.findViewById(R.id.rgDeliveryServiceAvilable2);
        service_yes = (RadioButton) view.findViewById(R.id.service_yes);
        service_no = (RadioButton) view.findViewById(R.id.service_no);
      /*  take_in = (RadioButton) view.findViewById(R.id.take_in);
        take_out = (RadioButton) view.findViewById(R.id.take_out);
      */  noradioMultiSiteBusiness = (RadioButton) view.findViewById(R.id.noradioMultiSiteBusiness);
        yesradioMultiSiteBusiness = (RadioButton) view.findViewById(R.id.yesradioMultiSiteBusiness);
        take_out_yes = (RadioButton) view.findViewById(R.id.take_out_yes);
        take_in_no = (RadioButton) view.findViewById(R.id.take_in_no);
        catering_no = (RadioButton) view.findViewById(R.id.catering_no);
        catering_yes = (RadioButton) view.findViewById(R.id.catering_yes);


        pre_order_no = (RadioButton) view.findViewById(R.id.pre_order_no);
        pre_order_yes = (RadioButton) view.findViewById(R.id.pre_order_yes);
        pre_delvery_no = (RadioButton) view.findViewById(R.id.pre_delvery_no);
        pre_delvery_yes = (RadioButton) view.findViewById(R.id.pre_delvery_yes);
        pre_catering_no = (RadioButton) view.findViewById(R.id.pre_catering_no);
        pre_catering_yes = (RadioButton) view.findViewById(R.id.pre_catering_yes);
        addMoreButton = (Button) view.findViewById(R.id.btnAddmMore);
        jsonArray = new JsonArray();
    }

    private void onClickListenerRadioGroup(final View view) {
        imageView.setOnClickListener(this);
        addMoreButton.setOnClickListener(this);
        rgInHouse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgInHouse.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioInHouseValue = "1";
                } else {
                    radioInHouseValue = "0";
                }
                Log.d(TAG + "RadioVAlu", s);
                Log.d(TAG + "RadioInH", radioInHouseValue);
            }
        });
        rgTakeOut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgTakeOut.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioTakeOut = "1";
                } else {
                    radioTakeOut = "0";
                }
                Log.d(TAG + "RadioTake", s);
                Log.d(TAG + "RadioTake", radioTakeOut);
            }
        });
        rgCatering.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgCatering.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioCatring = "1";
                } else {
                    radioCatring = "0";
                }
                Log.d(TAG + "RadioCat", s);
                Log.d(TAG + "RadioCat", radioCatring);
            }
        });
        rgDelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgDelivery.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioDelivery = "1";
                } else {
                    radioDelivery = "0";
                }
                Log.d(TAG + "RadioDeli", s);
                Log.d(TAG + "RadioDeli", radioDelivery);
            }
        });
        rgTakeOutOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgTakeOutOrder.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioTakeOutOrder = "1";
                } else {
                    radioTakeOutOrder = "0";
                }
                Log.d(TAG + "RadioOut", s);
                Log.d(TAG + "Radioout", radioTakeOutOrder);
            }
        });
        rgTakeOutDeliveryOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgTakeOutDeliveryOrder.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioTakeOutDeliveryOrder = "1";
                } else {
                    radioTakeOutDeliveryOrder = "0";
                }
                Log.d(TAG + "RadioOrd", s);
                Log.d(TAG + "Radioord", radioTakeOutDeliveryOrder);
            }
        });
        rgCatringPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgCatringPayment.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioCatringPayment = "1";
                } else {
                    radioCatringPayment = "0";
                }
                Log.d(TAG + "RadioPay", s);
                Log.d(TAG + "RadioPAy", radioCatringPayment);
            }
        });
        rgDeliveryServiceAvilable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgDeliveryServiceAvilable.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String radioText = radioButton.getText().toString();
                if (radioText.equalsIgnoreCase("Yes")) {
                    radioDeliveryServiceAvilable = "yes";
                } else {
                    radioDeliveryServiceAvilable = "no";
                }
                Log.d(TAG + "RadioServ", radioText);
                Log.d(TAG + "RadioServ", radioDeliveryServiceAvilable);
            }
        });
        rgDeliveryServiceAvilable1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgDeliveryServiceAvilable1.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioDeliveryServiceAvilable1 = "yes";
                } else {
                    radioDeliveryServiceAvilable1 = "no";
                }
                Log.d(TAG + "RadioSer1", s);
                Log.d(TAG + "RadioSer1", radioDeliveryServiceAvilable1);
            }
        });
        rgDeliveryServiceAvilable2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgDeliveryServiceAvilable2.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    radioDeliveryServiceAvilable2 = "yes";
                } else {
                    radioDeliveryServiceAvilable2 = "no";
                }
                Log.d(TAG + "RadioSer2", s);
                Log.d(TAG + "RadioSer2", radioDeliveryServiceAvilable2);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageview) {
            apiCalling();

        } else if (view.getId() == R.id.btnAddmMore) {
            for (int i = 0; i <= cout; i++) {
                if (cout == 1) {
                    rgDeliveryServiceAvilable1.setVisibility(View.VISIBLE);
                    etDeliveryVendor1.setVisibility(View.VISIBLE);
                    where = 1;
                } else if (cout == 2) {
                    rgDeliveryServiceAvilable2.setVisibility(View.VISIBLE);
                    etDeliveryVendor2.setVisibility(View.VISIBLE);
                    where = 2;
                    addMoreButton.setVisibility(View.GONE);
                }

                cout++;
                break;
            }
        }
    }

    private void apiCalling() {
        if (Util.isNetworkAvailable(mContext)) {

            JsonElement jsonElement_order_package = jsonArray.getAsJsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_ORDER_PAYMENT_PACKAGE);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("in_house_option", radioInHouseValue);
            jsonObject.addProperty("in_house_cost", etInHouse.getText().toString().trim());
            jsonObject.addProperty("take_out_option", radioTakeOut);
            jsonObject.addProperty("take_out_cost", etTakeOut.getText().toString().trim());
            jsonObject.addProperty("catering_option", radioCatring);
            jsonObject.addProperty("catering_cost", etCatering.getText().toString().trim());
            jsonObject.addProperty("delivery_option", radioDelivery);
            jsonObject.addProperty("delivery_cost", etDelivery.getText().toString().trim());
            jsonObject.addProperty("payment_take_out_option", radioTakeOut);
            jsonObject.addProperty("payment_take_out_cost", etTakeoutOrder.getText().toString().trim());
            jsonObject.addProperty("payment_delivery_option", radioTakeOutDeliveryOrder);
            jsonObject.addProperty("payment_delivery_cost", etTakeoutDelivery.getText().toString().trim());
            jsonObject.addProperty("paymentcatering_option", radioCatringPayment);
            jsonObject.addProperty("paymentcatering_cost", etCatringPayment.getText().toString());
            jsonObject.addProperty("in_house_breakfast_minute", etInHouseBreakFast.getText().toString().trim());
            jsonObject.addProperty("in_house_lunch_minute", etInHouseLunch.getText().toString().trim());
            jsonObject.addProperty("in_house_dinner_minute", etInHouseDinner.getText().toString().trim());
            jsonObject.addProperty("take_out_breakfast_minute", etTakeOutBreakFast.getText().toString());
            jsonObject.addProperty("take_out_lunch_minute", etTakeOutLunch.getText().toString().trim());
            jsonObject.addProperty("take_out_dinner_minute", etTakeOutDinner.getText().toString().trim());
            jsonObject.addProperty("take_out_delv_breakfast_minute", etTakoutDeliveryBreakFast.getText().toString().trim());
            jsonObject.addProperty("take_out_delv_lunch_minute", etTakoutDeliveryLunch.getText().toString().trim());
            jsonObject.addProperty("take_out_delv_dinner_minute", etTakoutDeliveryDinner.getText().toString().trim());
            jsonObject.addProperty("catering_breakfast_minute", etCateringBreakfast.getText().toString().trim());
            jsonObject.addProperty("catering_lunch_minute", etCateringLunch.getText().toString().trim());
            jsonObject.addProperty("catering_dinner_minute", etCateringDinner.getText().toString().trim());
            ValueOfDeliveryService();
            jsonObject.add("order_package", jsonElement_order_package);
            Log.e(TAG + "Response", jsonObject.toString());
            JsonMethodOnlineAndPaymnet(jsonObject);
        } else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }


    private void JsonMethodOnlineAndPaymnet(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
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
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                        Fragment fragment = new VendorDiscountFragment();
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

    private void ValueOfDeliveryService() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("delivery_vendor_name", etDeliveryVendor.getText().toString().trim());
        jsonObject.addProperty("delivery_option", radioDeliveryServiceAvilable);
        jsonArray.add(jsonObject);
        if (where == 1) {
            ValueWhere1();
        }
        if (where == 2) {
            ValueWhere1();
            ValueWhere2();
        }
    }

    private void ValueWhere1() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("delivery_vendor_name", etDeliveryVendor1.getText().toString().trim());
        jsonObject.addProperty("delivery_option", radioDeliveryServiceAvilable1);
        jsonArray.add(jsonObject);
    }

    private void ValueWhere2() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("delivery_vendor_name", etDeliveryVendor2.getText().toString().trim());
        jsonObject.addProperty("delivery_option", radioDeliveryServiceAvilable2);
        jsonArray.add(jsonObject);

    }

    @SuppressLint("LongLogTag")
    public void PacakgeData() {
        String packageNo = AppPreferencesBuss.getBussiPackagelist(mContext);
        String[] data = packageNo.split(",");

        Log.d(TAG, "onCreate: " + data);
        for (int i = 0; i < data.length; i++) {
            data1 = data[i];
            Log.d(TAG, "onCreate: " + data1);
            if (data1.contains("4")) {
                Fragment fragment = new FoodServiceFragment45();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);


                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            } else if (data1.contains(" ")) {
                Intent intent = new Intent(mContext, BusinessNaviDrawer.class);
                startActivity(intent);
            }
        }
    }


    private void showdata() {
        if (Util.isNetworkAvailable(mContext)) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", "BusinessView53");
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));

         /*   jsonObject.addProperty("business_id", "1");
            jsonObject.addProperty("user_id", "13");
         */   //jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_ORDER_PAYMENT_PACKAGE);

           /* jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
           */  //   Log.e(TAG + "Response", jsonObject.toString());
            showDataFromServer(jsonObject);
        } else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }


    private void showDataFromServer(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
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
                                String inhouse_option = object.getString("sc_inhouse_option");
                                String inhouse_percent = object.getString("sc_inhouse_cost_percent");
                                String takeout_option = object.getString("sc_takeout_option");
                                String takeout_cost = object.getString("sc_takeout_cost");
                                String catering_option = object.getString("sc_catering_option");
                                String catering_cost = object.getString("sc_catering_cost");
                                String delivery_cost = object.getString("sc_delivery_cost");
                                String takeout_order_option = object.getString("ppr_takeout_order_option");
                                String takeout_order_cost = object.getString("ppr_takeout_order_cost");
                                String takeour_delv_order_option = object.getString("ppr_takeour_delv_order_option");
                                String takeour_delv_order_cost = object.getString("ppr_takeour_delv_order_cost");
                                String catering_order_option = object.getString("ppr_catering_order_option");
                                String catering_order_cost = object.getString("ppr_catering_order_cost");
                                etInHouse.setText(inhouse_percent);
                                etTakeOut.setText(takeout_cost);
                                etDelivery.setText(delivery_cost);
                                etCatering.setText(catering_cost);
                                etTakeoutOrder.setText(takeout_order_cost);
                                etTakeoutDelivery.setText(takeour_delv_order_cost);
                                etCatringPayment.setText(catering_order_cost);

                                if (inhouse_option.equalsIgnoreCase("1")) {
                                    yesradioMultiSiteBusiness.setChecked(true);
                                    noradioMultiSiteBusiness.setChecked(false);
                                } else if (inhouse_option.equalsIgnoreCase("0")) {
                                    noradioMultiSiteBusiness.setChecked(true);
                                    yesradioMultiSiteBusiness.setChecked(false);

                                } else if (inhouse_option.equalsIgnoreCase("")) {
                                    yesradioMultiSiteBusiness.setChecked(false);
                                    noradioMultiSiteBusiness.setChecked(false);
                                }


                                if (takeout_option.equalsIgnoreCase("1")) {
                                    take_out_yes.setChecked(true);
                                    take_in_no.setChecked(false);
                                } else if (takeout_option.equalsIgnoreCase("0")) {
                                    take_in_no.setChecked(true);
                                    take_out_yes.setChecked(false);

                                } else if (takeout_option.equalsIgnoreCase("")) {
                                    take_in_no.setChecked(false);
                                    take_out_yes.setChecked(false);
                                }

                              /*    pre_catering_no = (RadioButton) view.findViewById(R.id.pre_catering_no);
                                pre_catering_yes = (RadioButton) view.findViewById(R.id.pre_catering_yes);

*/

                                if (takeour_delv_order_option.equalsIgnoreCase("1")) {
                                    pre_delvery_yes.setChecked(true);
                                    pre_delvery_no.setChecked(false);
                                } else if (takeour_delv_order_option.equalsIgnoreCase("0")) {
                                    pre_delvery_no.setChecked(true);
                                    pre_delvery_yes.setChecked(false);

                                } else if (takeour_delv_order_option.equalsIgnoreCase("")) {
                                    pre_order_no.setChecked(false);
                                    pre_delvery_no.setChecked(false);
                                }



                                if (catering_order_option.equalsIgnoreCase("1")) {
                                    pre_catering_yes.setChecked(true);
                                    pre_catering_no.setChecked(false);
                                } else if (catering_order_option.equalsIgnoreCase("0")) {
                                    pre_catering_no.setChecked(true);
                                    pre_catering_yes.setChecked(false);

                                } else if (catering_order_option.equalsIgnoreCase("")) {
                                    pre_catering_no.setChecked(false);
                                    pre_catering_yes.setChecked(false);
                                }

                                if (catering_option.equalsIgnoreCase("1")) {
                                    catering_yes.setChecked(true);
                                    catering_no.setChecked(false);
                                } else if (catering_option.equalsIgnoreCase("0")) {
                                    catering_no.setChecked(true);
                                    catering_yes.setChecked(false);
                                } else if (catering_option.equalsIgnoreCase("")) {
                                    catering_no.setChecked(false);
                                    catering_yes.setChecked(false);
                                }

                            }
                            JSONArray jsonArray1 = jsonObject.getJSONArray("details");
                            for (int j = 0; j <= jsonArray1.length(); j++) {
                                JSONObject object1 = jsonArray1.getJSONObject(j);
                                String delivery_service = object1.getString("delivery_service");
                                String delivery_vendor_name = object1.getString("delivery_vendor_name");
                                etDeliveryVendor.setText(delivery_vendor_name);
                                if (delivery_service.equalsIgnoreCase("1")) {
                                    service_yes.setChecked(true);
                                    service_no.setChecked(false);
                                } else if (delivery_service.equalsIgnoreCase("0")) {
                                    service_no.setChecked(true);
                                    service_yes.setChecked(false);

                                } else if (delivery_service.equalsIgnoreCase("")) {
                                    service_no.setChecked(false);
                                    service_yes.setChecked(false);
                                }

                            }
                        } else {
                            Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                        }
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


}
