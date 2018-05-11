package com.yberry.dinehawaii.Bussiness.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
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
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceChargesAndPaymentFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ServiceCharges";
    FragmentIntraction intraction;
    CustomTextView tvInhouse, tvtakeout, tvdelivery, tvtakeorder, tvtakedelivery, tvcatering, tvcatpaymnt;
    Button submit;
    private CustomEditText etInHouse, etTakeOut, etCatering, etDelivery, etTakeoutOrder, etTakeoutDelivery,
            etCatringPayment,etmininhouse,etmintake,etmindelivery,etmincatering;
    private RadioGroup rgInHouse, rgTakeOut, rgCatering, rgDelivery, rgTakeOutOrder, rgTakeOutDeliveryOrder, rgCatringPayment;
    private Context mContext;
    private String radioInHouseValue, radioTakeOut, radioCatring, radioDelivery, radioTakeOutOrder, radioTakeOutDeliveryOrder,
            radioCatringPayment;
    private RadioButton pre_order_no, pre_order_yes, pre_delvery_no, pre_delvery_yes, pre_catering_no,
            pre_catering_yes, service_yes, service_no, catering_yes, catering_no, take_out_yes, take_in_no,
            noradioMultiSiteBusiness, yesradioMultiSiteBusiness,deliveryyes,deliveryno;
    private Dialog dialog;


    public ServiceChargesAndPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_charges_and_payment, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Service charges & Payment");
        }
        mContext = getActivity();
        ///showdata();
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
        RelativeLayout mainView = (RelativeLayout) view.findViewById(R.id.mainView);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getActivity().getCurrentFocus();
                if (view != null) {
                    hideKeyboard();
                }
                return false;
            }
        });


        etInHouse = (CustomEditText) view.findViewById(R.id.etInHouse);
        etTakeOut = (CustomEditText) view.findViewById(R.id.etTakeOut);
        etCatering = (CustomEditText) view.findViewById(R.id.etCatering);
        etDelivery = (CustomEditText) view.findViewById(R.id.etDelivery);
        etTakeoutOrder = (CustomEditText) view.findViewById(R.id.etTakeoutOrder);
        etTakeoutDelivery = (CustomEditText) view.findViewById(R.id.etTakeoutDelivery);
        etCatringPayment = (CustomEditText) view.findViewById(R.id.etCatringPayment);

        etmindelivery = (CustomEditText) view.findViewById(R.id.mindelivery);
        etmininhouse = (CustomEditText) view.findViewById(R.id.mininhouse);
        etmintake = (CustomEditText) view.findViewById(R.id.mintakeout);
        etmincatering = (CustomEditText) view.findViewById(R.id.mincatering);


        tvInhouse = (CustomTextView) view.findViewById(R.id.tvInhouse);
        tvdelivery = (CustomTextView) view.findViewById(R.id.tvdel);
        tvcatering = (CustomTextView) view.findViewById(R.id.tvcatering);
        tvtakedelivery = (CustomTextView) view.findViewById(R.id.tvtakedel);
        tvtakeorder = (CustomTextView) view.findViewById(R.id.tvtakeorder);
        tvtakeout = (CustomTextView) view.findViewById(R.id.tvtkeot);
        tvcatpaymnt = (CustomTextView) view.findViewById(R.id.tvtakecater);
        submit = (Button) view.findViewById(R.id.submitService);
        submit.setOnClickListener(this);
        rgInHouse = (RadioGroup) view.findViewById(R.id.rgInHouse);
        rgTakeOut = (RadioGroup) view.findViewById(R.id.rgTakeOut);
        rgCatering = (RadioGroup) view.findViewById(R.id.rgCatering);
        rgDelivery = (RadioGroup) view.findViewById(R.id.rgDelivery);
        rgTakeOutOrder = (RadioGroup) view.findViewById(R.id.rgTakeOutOrder);
        rgTakeOutDeliveryOrder = (RadioGroup) view.findViewById(R.id.rgTakeOutDeliveryOrder);
        rgCatringPayment = (RadioGroup) view.findViewById(R.id.rgCatringPayment);
        service_yes = (RadioButton) view.findViewById(R.id.service_yes);
        service_no = (RadioButton) view.findViewById(R.id.service_no);
        noradioMultiSiteBusiness = (RadioButton) view.findViewById(R.id.noradioMultiSiteBusiness);
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
        deliveryno = (RadioButton) view.findViewById(R.id.deliver_out_no);
        deliveryyes = (RadioButton) view.findViewById(R.id.deliverout_yes);

    }

    private void onClickListenerRadioGroup(final View view) {
        rgInHouse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgInHouse.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    etInHouse.setVisibility(View.VISIBLE);
                    tvInhouse.setVisibility(View.VISIBLE);
                    radioInHouseValue = "1";
                } else {
                    etInHouse.setVisibility(View.GONE);
                    tvInhouse.setVisibility(View.GONE);
                    radioInHouseValue = "0";
                }
            }
        });
        rgTakeOut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgTakeOut.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    etTakeOut.setVisibility(View.VISIBLE);
                    tvtakeout.setVisibility(View.VISIBLE);
                    radioTakeOut = "1";
                } else {
                    etTakeOut.setVisibility(View.GONE);
                    tvtakeout.setVisibility(View.GONE);
                    radioTakeOut = "0";
                }
            }
        });
        rgCatering.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgCatering.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    etCatering.setVisibility(View.VISIBLE);
                    tvcatering.setVisibility(View.VISIBLE);
                    radioCatring = "1";
                } else {
                    etCatering.setVisibility(View.GONE);
                    tvcatering.setVisibility(View.GONE);
                    radioCatring = "0";
                }
            }
        });
        rgDelivery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgDelivery.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    etDelivery.setVisibility(View.VISIBLE);
                    tvdelivery.setVisibility(View.VISIBLE);
                    radioDelivery = "1";
                } else {
                    etDelivery.setVisibility(View.GONE);
                    tvdelivery.setVisibility(View.GONE);
                    radioDelivery = "0";
                }

            }
        });
        rgTakeOutOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgTakeOutOrder.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    etTakeoutOrder.setVisibility(View.VISIBLE);
                    tvtakeorder.setVisibility(View.VISIBLE);
                    radioTakeOutOrder = "1";
                } else {
                    etTakeoutOrder.setVisibility(View.GONE);
                    tvtakeorder.setVisibility(View.GONE);
                    radioTakeOutOrder = "0";
                }
            }
        });

        rgTakeOutDeliveryOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgTakeOutDeliveryOrder.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    etTakeoutDelivery.setVisibility(View.VISIBLE);
                    tvtakedelivery.setVisibility(View.VISIBLE);
                    radioTakeOutDeliveryOrder = "1";
                } else {
                    etTakeoutDelivery.setVisibility(View.GONE);
                    tvtakedelivery.setVisibility(View.GONE);
                    radioTakeOutDeliveryOrder = "0";
                }
            }
        });

        rgCatringPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rgCatringPayment.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(id);
                String s = radioButton.getText().toString();
                if (s.equalsIgnoreCase("Yes")) {
                    etCatringPayment.setVisibility(View.VISIBLE);
                    tvcatpaymnt.setVisibility(View.VISIBLE);
                    radioCatringPayment = "1";
                } else {
                    etCatringPayment.setVisibility(View.GONE);
                    tvcatpaymnt.setVisibility(View.GONE);
                    radioCatringPayment = "0";
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitService:
                apiCalling();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showdata();
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void apiCalling() {
        if (Util.isNetworkAvailable(mContext)) {

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
            jsonObject.addProperty("inhouse_min", etmininhouse.getText().toString());
            jsonObject.addProperty("takeout_del_min", etmindelivery.getText().toString());
            jsonObject.addProperty("catering_min", etmincatering.getText().toString());
            jsonObject.addProperty("takeout_min", etmintake.getText().toString());

            Log.e(TAG, "service json" + jsonObject.toString());
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
                Log.e(TAG, "service json" + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
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
        if (Util.isNetworkAvailable(mContext)) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.GETSERVICEDATA);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG, "get req" + jsonObject.toString());
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
        Call<JsonObject> call = apiService.view_service(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "get data response", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        final int numberOfItemsInResp = jsonArray.length();
                        for (int i = 0; i < numberOfItemsInResp; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String inhouse_option = object.getString("sc_inhouse_option");
                            String inhouse_percent = object.getString("sc_inhouse_cost_percent");
                            String takeout_option = object.getString("sc_takeout_option");
                            String takeout_cost = object.getString("sc_takeout_cost");
                            String catering_option = object.getString("sc_catering_option");
                            String catering_cost = object.getString("sc_catering_cost");
                            String delivery_cost = object.getString("sc_delivery_cost");
                            String delivery_option = object.getString("sc_delivery_option");
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
                            etmincatering.setText(object.getString("catering_min"));
                            etmindelivery.setText(object.getString("takeout_del_min"));
                            etmintake.setText(object.getString("takeout_min"));
                            etmininhouse.setText(object.getString("inhouse_min"));

                            if (delivery_option.equalsIgnoreCase("1")) {
                                deliveryyes.setChecked(true);
                                deliveryno.setChecked(false);
                            } else if (delivery_option.equalsIgnoreCase("0")) {
                                deliveryno.setChecked(true);
                                deliveryyes.setChecked(false);

                            } else if (delivery_option.equalsIgnoreCase("")) {
                                deliveryyes.setChecked(false);
                                deliveryno.setChecked(false);
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

                            if (takeout_order_option.equalsIgnoreCase("1")) {
                                pre_order_yes.setChecked(true);
                                pre_order_no.setChecked(false);
                            } else if (takeout_order_option.equalsIgnoreCase("0")) {
                                pre_order_no.setChecked(true);
                                pre_order_yes.setChecked(false);

                            } else if (takeout_order_option.equalsIgnoreCase("")) {
                                pre_order_no.setChecked(false);
                                pre_order_yes.setChecked(false);
                            }


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
                    } else {
                        Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
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
