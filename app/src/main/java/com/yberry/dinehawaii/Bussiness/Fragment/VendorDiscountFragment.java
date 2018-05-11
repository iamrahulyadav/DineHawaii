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
import android.widget.ImageView;
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

public class VendorDiscountFragment extends Fragment implements View.OnClickListener {
    String valueRadio1, valueRadio2, valueRadio3, valueRadio4, valueRadio5;
    RadioButton pre_payment_yes, pre_payment_no, delivery_yes, delivery_no, invoice_yes, invoice_no, mail_invoce_yes, mail_invoce_no, avail_yes, avail_no;
    private ImageView imageView;
    private RadioGroup radioDeli, radioPre, radioCOD, radioInvo, radioMail;
    private Context mContext;
    private String TAG = "VENDOR DISCOUNT FRAGMENT ";


    public VendorDiscountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentIntraction intraction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vendor_discount, container, false);
      //  headText.setText("VENDOR DISCOUNT PURCHASING SERVICE");
//        headText.setText("VENDOR  SERVICE");
        if (intraction != null) {
            intraction.actionbarsetTitle("Vendor Service");
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
        mContext = getActivity();
        imageView = (ImageView) view.findViewById(R.id.imageview);
        radioDeli = (RadioGroup) view.findViewById(R.id.radioDelivery);
        radioPre = (RadioGroup) view.findViewById(R.id.radioPrePayment);
        radioCOD = (RadioGroup) view.findViewById(R.id.radioCODPayment);
        radioInvo = (RadioGroup) view.findViewById(R.id.radioInvoice);
        radioMail = (RadioGroup) view.findViewById(R.id.radioMailInvoice);


        pre_payment_yes = (RadioButton) view.findViewById(R.id.pre_payment_yes);
        pre_payment_no = (RadioButton) view.findViewById(R.id.pre_payment_no);
        delivery_yes = (RadioButton) view.findViewById(R.id.delivery_yes);
        delivery_no = (RadioButton) view.findViewById(R.id.delivery_no);
        invoice_yes = (RadioButton) view.findViewById(R.id.invoice_yes);
        invoice_no = (RadioButton) view.findViewById(R.id.invoice_no);
        mail_invoce_yes = (RadioButton) view.findViewById(R.id.mail_invoce_yes);
        mail_invoce_no = (RadioButton) view.findViewById(R.id.mail_invoce_no);
        avail_yes = (RadioButton) view.findViewById(R.id.avail_yes);
        avail_no = (RadioButton) view.findViewById(R.id.avail_no);
        imageView.setOnClickListener(this);

        radioDeli.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) view.findViewById(id);
                //jsonObjectOther = new JsonObject();

                Log.d("radioVALUE", button.getText().toString());
                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    valueRadio1 = "1";

                } else if (button.getText().toString().equalsIgnoreCase("No")) {
                    valueRadio1 = "0";
                }


            }
        });

        radioPre.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) view.findViewById(id);
                //jsonObjectOther = new JsonObject();

                Log.d("radioVALUE", button.getText().toString());
                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    valueRadio2 = "1";

                } else if (button.getText().toString().equalsIgnoreCase("No")) {
                    valueRadio2 = "0";
                }


            }
        });
        radioCOD.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) view.findViewById(id);
                //jsonObjectOther = new JsonObject();

                Log.d("radioVALUE", button.getText().toString());
                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    valueRadio3 = "1";

                } else if (button.getText().toString().equalsIgnoreCase("No")) {
                    valueRadio3 = "0";
                }


            }
        });

        radioInvo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) view.findViewById(id);
                //jsonObjectOther = new JsonObject();

                Log.d("radioVALUE", button.getText().toString());
                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    valueRadio4 = "1";

                } else if (button.getText().toString().equalsIgnoreCase("No")) {
                    valueRadio4 = "0";
                }


            }
        });

        radioMail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) view.findViewById(id);
                //jsonObjectOther = new JsonObject();

                Log.d("radioVALUE", button.getText().toString());
                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    valueRadio5 = "1";

                } else if (button.getText().toString().equalsIgnoreCase("No")) {
                    valueRadio5 = "0";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.imageview) {

            if (Util.isNetworkAvailable(mContext)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_VENDOR_DISCOUNT_SERVICE);
                jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                jsonObject.addProperty("delivery_aval", valueRadio1);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("customer_pre_payment", valueRadio2);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("cod_payment", valueRadio3);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("generate_dine_invoice", valueRadio4);//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("buss_mail_invoice", valueRadio5);//AppPreferencesBuss.getUserId(getApplicationContext()));
                Log.d("JSONOBJECT:", jsonObject.toString());

                //Log.e(TAG, jsonObject.toString());
                JsonCallMethod(jsonObject);

                Fragment fragment = new VendorDiscountNextFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();

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

                        Fragment fragment = new VendorDiscountNextFragment();
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
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    private void showdata() {
        if (Util.isNetworkAvailable(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", "BusinessView54");
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id",  AppPreferencesBuss.getUserId(getActivity()));
            //jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_ORDER_PAYMENT_PACKAGE);

           /* jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
           */  //   Log.e(TAG + "Response", jsonObject.toString());
            showDataFromServer(jsonObject);
        } else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }


    private void showDataFromServer(JsonObject jsonObject) {
     /*   final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String delivery_option = object.getString("delivery_available_option");
                            String pre_payment_option = object.getString("customer_pre_payment_option");
                            String payment_delv_option = object.getString("cod_payment_delv_option");
                            String invoice_customer_option = object.getString("generate_dine_invoice_for_customer_option");
                            String business_invoice_option = object.getString("business_mail_invoice_tocustomer_option");
                            if (delivery_option.equalsIgnoreCase("1")) {
                                avail_yes.setChecked(true);
                                avail_no.setChecked(false);
                            } else if (delivery_option.equalsIgnoreCase("0")) {
                                avail_yes.setChecked(false);
                                avail_no.setChecked(true);
                            } else if (delivery_option.equalsIgnoreCase("")) {
                                avail_yes.setChecked(false);
                                avail_no.setChecked(false);
                            }


                            if (pre_payment_option.equalsIgnoreCase("1")) {
                                pre_payment_yes.setChecked(true);
                                pre_payment_no.setChecked(false);
                            } else if (pre_payment_option.equalsIgnoreCase("0")) {
                                pre_payment_yes.setChecked(false);
                                pre_payment_no.setChecked(true);
                            } else if (pre_payment_option.equalsIgnoreCase("")) {
                                pre_payment_yes.setChecked(false);
                                pre_payment_no.setChecked(false);
                            }


                            if (payment_delv_option.equalsIgnoreCase("1")) {
                                delivery_yes.setChecked(true);
                                delivery_no.setChecked(false);

                            } else if (payment_delv_option.equalsIgnoreCase("0")) {
                                delivery_yes.setChecked(false);
                                delivery_no.setChecked(true);

                            } else if (payment_delv_option.equalsIgnoreCase("")) {
                                delivery_yes.setChecked(false);
                                delivery_no.setChecked(false);

                            }

                            if (invoice_customer_option.equalsIgnoreCase("1")) {
                                invoice_yes.setChecked(false);
                                invoice_no.setChecked(false);

                            } else if (invoice_customer_option.equalsIgnoreCase("0")) {
                                invoice_yes.setChecked(false);
                                invoice_no.setChecked(false);

                            } else if (invoice_customer_option.equalsIgnoreCase("")) {
                                invoice_yes.setChecked(false);
                                invoice_no.setChecked(false);

                            }

                            if (business_invoice_option.equalsIgnoreCase("1")) {
                                mail_invoce_yes.setChecked(true);
                                mail_invoce_no.setChecked(false);

                            } else if (business_invoice_option.equalsIgnoreCase("0")) {
                                mail_invoce_yes.setChecked(false);
                                mail_invoce_no.setChecked(true);

                            } else if (business_invoice_option.equalsIgnoreCase("")) {
                                mail_invoce_yes.setChecked(true);
                                mail_invoce_no.setChecked(true);

                            }

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
//                Log.e(TAG + "error", t.getMessage());
                t.getMessage();
              //  progressHD.dismiss();
            }
        });
    }


}
