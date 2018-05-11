package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.yberry.dinehawaii.Bussiness.Adapter.OptionAdapterPayment;
import com.yberry.dinehawaii.Bussiness.Adapter.PackageAdapterPayment;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.JustifiedTextView;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomCheckBox;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusiPackagePaymentActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static final String TAG = "BussinessRest_StepTwo";
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConstants.PAYPAL_CLIENT_ID);
    JSONObject object;
    String totalAmount, totalpackageamt, totaloptionant;
    CustomTextView packageAmount, mkt_optionAmount, perctDiscount, taxAmount, total_amount, optionAmount, subTotalCost;
    String amountToBePaid = "0.1";

    private ImageView back;
    private CustomCheckBox checkBox;
    private RecyclerView pakage_mRecyclerView, option_mRecyclerView;
    private ArrayList<CheckBoxPositionModel> pakage_list, option_list;
    private OptionAdapterPayment optionAdapter;
    private PackageAdapterPayment packageAdapter;
    private BusiPackagePaymentActivity context;
    private String sel_pacakges = "";
    private String sel_options = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_restaurent_resturant_step_two);
        context = this;
        setToolbar();
        pakage_list = new ArrayList<CheckBoxPositionModel>();
        option_list = new ArrayList<CheckBoxPositionModel>();
        initViews();
        setAdapters();
        try {
            object = new JSONObject(getIntent().getStringExtra("data"));
            sel_pacakges = object.getString("pacakges");
            sel_options = object.getString("options");
            Log.e(TAG, "object >> " + object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (getIntent().getExtras() != null) {
            totalpackageamt = getIntent().getExtras().getString("pakageAmount");
            totaloptionant = getIntent().getExtras().getString("optionAmount");
            totalAmount = getIntent().getExtras().getString("totalAmount");
            Log.e(TAG, "totalAmount >> " + totalAmount);
            Log.e(TAG, "totalpackageamt >> " + totalpackageamt);
            Log.e(TAG, "totaloptionant >> " + totaloptionant);
            amountToBePaid = totalAmount;
            packageAmount.setText("$" + totalpackageamt);
            optionAmount.setText("$" + totaloptionant);
            subTotalCost.setText("$" + totalAmount);
        }
        getGETax();

        getAllPackage();
        getOptions();

        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    private void setAdapters() {
        pakage_mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        pakage_mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        pakage_mRecyclerView.setNestedScrollingEnabled(false);
        packageAdapter = new PackageAdapterPayment(context, pakage_list);
        pakage_mRecyclerView.setAdapter(packageAdapter);

        option_mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        option_mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        option_mRecyclerView.setNestedScrollingEnabled(false);
        optionAdapter = new OptionAdapterPayment(context, option_list);
        option_mRecyclerView.setAdapter(optionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_icon_menu, menu);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            showAlert();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("LongLogTag")
    private void getAllPackage() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.GETALLPACKAGE);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        Log.e(TAG, "Requesst GET ALL PACKAGE >> " + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET ALL PACKAGE >> " + response.body().toString());
                String s = response.body().toString();
                pakage_list.clear();
                double totalAmount = 0;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CheckBoxPositionModel model = new CheckBoxPositionModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        model.setId(object.getString("package_id"));
                        model.setName(object.getString("package_name"));
                        model.setAmount(object.getString("amount"));
                        model.setType(object.getString("package_type"));
                        model.setPackage_detail(object.getString("package_detail"));
                        if (sel_pacakges.contains(object.getString("package_id")))
                            model.setChckStatus(true);
                        else
                            model.setChckStatus(false);
                        pakage_list.add(model);
                        packageAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    progressHD.dismiss();
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

    @SuppressLint("LongLogTag")
    private void getOptions() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.GETALLOPTION);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        Log.e(TAG, "Request GET ALL OPTIONS >> " + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET ALL OPTIONS >> " + response.body().toString());
                String s = response.body().toString();
                option_list.clear();
                double totalAmount = 0;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CheckBoxPositionModel model = new CheckBoxPositionModel();
                        model.setId(jsonObject1.getString("option_id"));
                        model.setName(jsonObject1.getString("option_name"));
                        model.setAmount(jsonObject1.getString("amount"));
                        model.setType(jsonObject1.getString("option_type"));

                        if (sel_options.contains(jsonObject1.getString("option_id")))
                            model.setChckStatus(true);
                        else
                            model.setChckStatus(false);
                        option_list.add(model);
                    }

                    optionAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    progressHD.dismiss();
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

    private void initViews() {
        total_amount = (CustomTextView) findViewById(R.id.total_amount);

        packageAmount = (CustomTextView) findViewById(R.id.packageAmount);
        optionAmount = (CustomTextView) findViewById(R.id.mkt_optionAmount);

        mkt_optionAmount = (CustomTextView) findViewById(R.id.mkt_optionAmount);
        subTotalCost = (CustomTextView) findViewById(R.id.subtotalpackg);

        perctDiscount = (CustomTextView) findViewById(R.id.perctDiscount);
        taxAmount = (CustomTextView) findViewById(R.id.taxAmount);

    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Confirm Payment");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getGETax() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.TAX);
        geTaxApi(jsonObject);
        Log.d(TAG, " Request Tax :- " + jsonObject.toString());

    }

    private void geTaxApi(JsonObject jsonObject) {
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Log.e(TAG, "Inside response 200 ");
                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject object = resultJsonArray.getJSONObject(i);
                            double geTaxTotal = Double.parseDouble(object.getString("geTax").replace("%", ""));
                            taxAmount.setText(String.valueOf(geTaxTotal) + "%");
                            double totaltax = Double.parseDouble(totalAmount) * geTaxTotal / 100;
                            double finaltotaltax = totaltax;


                            double admin_amount = finaltotaltax + Double.parseDouble(totalAmount);
                            double admin_per = admin_amount;
                            double admin_total = admin_per;

                            DecimalFormat twoDForm = new DecimalFormat("#.##");
                            double grand_amount = Double.valueOf(twoDForm.format(admin_total));
                            double totaltax1 = Double.valueOf(twoDForm.format(totaltax));
                            total_amount.setText("$" + grand_amount);
                            taxAmount.setText("$" + totaltax1);
                            amountToBePaid = String.valueOf(grand_amount);
                            AppPreferencesBuss.setPackageAmount(context, String.valueOf(grand_amount));
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        /* pay.setVisibility(View.GONE);*/
                        Toast.makeText(context, "Failed to get Tax,Try Again Later!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error" + t.getMessage());
                Toast.makeText(context, "Server Not responding", Toast.LENGTH_SHORT).show();
//                    t.getMessage();
            }
        });
    }

    private void getPayment(String paymentAmount) {
        //Getting the amountToBePaid from editText

//        paymentAmount = price;
        Log.e(TAG, "~~~~~~~~~~~~~~~~~~~~ Song Price outside iff :- " + paymentAmount);

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "USD", "Purchase Fee\n",
                PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(context, com.paypal.android.sdk.payments.PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        //Puting paypal payment to the intent
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment

            /*
            {
                "client": {
                "environment": "sandbox",
                        "paypal_sdk_version": "2.0.0",
                        "platform": "iOS",
                        "product_name": "PayPal iOS SDK;"
            },
                "response": {
                "create_time": "2014-02-12T22:29:49Z",
                        "id": "PAY-564191241M8701234KL57LXI",
                        "intent": "sale",
                        "state": "approved"
            },
                "response_type": "payment"
            }
        */

            if (resultCode == Activity.RESULT_OK) {

                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);
                            JSONObject jsonResponse = jsonDetails.getJSONObject("response");
                            String transaction_ID = jsonResponse.getString("id");
                            String createTime = jsonResponse.getString("create_time");
                            String intent = jsonResponse.getString("intent");
                            AppPreferencesBuss.setBusinessTranctionId(context, jsonResponse.getString("id"));
                            AppPreferencesBuss.setBusinessTranctionStatus(context, jsonResponse.getString("state"));

                            String paymentState = jsonResponse.getString("state");
                            Log.i("paymentExample", "RESPONSE :- \n" + "Transaction_ID :- " + transaction_ID + "\nCreate Time :- " + createTime +
                                    "\nIntent :- " + intent + "\nPayment State :- " + paymentState);

                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
//                        showAlertDialog(paymentAmount);


                        Intent intent = new Intent(getApplicationContext(), PaypalApprovedActivity.class);
                        intent.putExtra("data", object.toString());
                        startActivity(intent);

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
                Toast.makeText(context, "Sorry!! Payment cancelled by User", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), BusiSelectPackageActivity.class);
                intent.putExtra("data", object.toString());
                startActivity(intent);

            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay) {


        }

    }

    private void showAlert() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.paypal_payment_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        JustifiedTextView justifytxt = (JustifiedTextView) dialog.findViewById(R.id.justifytxt);
        final CustomButton pay = (CustomButton) dialog.findViewById(R.id.pay);
        checkBox = (CustomCheckBox) dialog.findViewById(R.id.checkbox);
        justifytxt.setText(getResources().getString(R.string.text_21B));

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkAvailable(context)) {
                    if (checkBox.isChecked()) {
                        try {
                            object.put("terms_condition", "1");
                            getPayment(amountToBePaid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else
                        Snackbar.make(findViewById(android.R.id.content), "Please Check terms & conditions", Snackbar.LENGTH_LONG).show();
                } else
                    Toast.makeText(context, "Please Connect to Internet", Toast.LENGTH_SHORT).show();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    pay.setVisibility(View.VISIBLE);
                else
                    pay.setVisibility(View.GONE);
            }
        });

        if (!BusiPackagePaymentActivity.this.isFinishing())
            dialog.show();
    }
}
