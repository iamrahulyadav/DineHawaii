package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.yberry.dinehawaii.Customer.Activity.EGiftApproved;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendEGiftVoucher extends Fragment {

    public static final int PAYPAL_REQUEST_CODE = 123;
    private static final String TAG = "SendEGiftVoucher";
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConstants.PAYPAL_CLIENT_ID);
    String amount, message, transaction_ID, intent, createTime, paymentState;
    Button submitPayment, search;
    String send_UserId = "0",send_user_type = "";;
    CustomEditText amountEditText, usernameEditText, messageEditText;
    LinearLayout giftLinear;
    FragmentIntraction intraction;
    private CustomTextView tvTotalBal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_send_egift_voucher, container, false);
        LinearLayout mainView = (LinearLayout) view.findViewById(R.id.activity_send_egift_voucher);
        if (intraction != null) {
            intraction.actionbarsetTitle("E-Gifts");
        }
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

        Intent intent = new Intent(getActivity(), PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        getActivity().startService(intent);
        giftLinear = (LinearLayout) view.findViewById(R.id.giftAmountLayout);
        amountEditText = (CustomEditText) view.findViewById(R.id.amount);
        usernameEditText = (CustomEditText) view.findViewById(R.id.username);
        messageEditText = (CustomEditText) view.findViewById(R.id.message);
        tvTotalBal = (CustomTextView) view.findViewById(R.id.tvTotalBal);
        submitPayment = (Button) view.findViewById(R.id.submitPayment);
        search = (Button) view.findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Function.isEmailNotValid(usernameEditText)) {
                    usernameEditText.setError("Enter Email Id");
                } else {
                    searchUser();
                }

            }
        });

        submitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount = amountEditText.getText().toString();
                if (TextUtils.isEmpty(amountEditText.getText())) {
                    Toast.makeText(getActivity(), "Please enter Amount !!", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmationAlert("Are you sure you want to send E-Gift Card");

                }
            }
        });
        return view;
    }

    private void showConfirmationAlert(String msg) {
        AlertDialog.Builder confirm_alert = new AlertDialog.Builder(getActivity());
        confirm_alert.setMessage(msg);
        confirm_alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                giftLinear.setVisibility(View.VISIBLE);
                openDialog();
            }
        });
        confirm_alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        confirm_alert.show();
    }

    private void openDialog() {
        final String grpname[] = {"Use EGift Balance", "Purchase via PayPal"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
        alt_bld.setTitle("Send E-GIFT");
        alt_bld.setSingleChoiceItems(grpname, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (item == 0) {
                    if (tvTotalBal.getText().toString().equalsIgnoreCase("0")) {
                        showAlert("You dont have egift balance");
                    } else if (Integer.parseInt(tvTotalBal.getText().toString()) < Integer.parseInt(amount)) {
                        int remaining_amount = Integer.parseInt(amount) - Integer.parseInt(tvTotalBal.getText().toString());
                        showAlert1("You dont have sufficient egift balance.\n\nNeed $" + remaining_amount + " more.", remaining_amount);
                    }
                } else if (item == 1) {
                    getPayment(new BigDecimal(String.valueOf(amountEditText.getText().toString())));
                }
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void showAlert(String msg) {
        new AlertDialog.Builder(getActivity())
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void showAlert1(String msg, final int remaining_amount) {
        new AlertDialog.Builder(getActivity())
                .setMessage(msg)
                .setPositiveButton("PAYPAl", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getPayment(new BigDecimal(remaining_amount));
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void showBalanceAlert() {
        new AlertDialog.Builder(getActivity())
                .setMessage("We are unable to fetch your EGift balance.Please try again.")
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getBalance();
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void getBalance() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.CHECK_EMAILID);
        jsonObject.addProperty(AppConstants.KEY_EMAILID, usernameEditText.getText().toString());

        Log.e(TAG, "getBalance json" + jsonObject.toString());

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserSendGift(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, " getBalance response" + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray result = jsonObject.getJSONArray("result");
                        JSONObject res = result.getJSONObject(0);

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong !!", Toast.LENGTH_SHORT).show();
                    }

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
                Toast.makeText(getActivity().getApplicationContext(), "Server not responding!!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void searchUser() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.CHECK_EMAILID);
        jsonObject.addProperty(AppConstants.KEY_EMAILID, usernameEditText.getText().toString());

        Log.e(TAG, "egift check user json :- " + jsonObject.toString());

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserSendGift(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, " egift check user response:- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray result = jsonObject.getJSONArray("result");
                        JSONObject res = result.getJSONObject(0);
                        send_UserId = res.getString("id");
                        send_user_type = "reg";
                        //showConfirmationAlert("Are you sure you want to send an E-Gift card");
//                        showConfirmationAlert("Are you sure you want to send an E-Gift card to "+res.getString("") );
                        giftLinear.setVisibility(View.VISIBLE);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        send_user_type = "non";
                        // showConfirmationAlert("The entered email id isn't registered with us do you want to send an E-Gift card to them they can use it whenever they register");
                        giftLinear.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong !!", Toast.LENGTH_SHORT).show();
                    }

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
                Toast.makeText(getActivity().getApplicationContext(), "Server not responding!!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getPayment(BigDecimal bigDecimal) {
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(bigDecimal, "USD", "Purchase Fee\n",
                PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(getActivity(), com.paypal.android.sdk.payments.PaymentActivity.class);
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

            if (resultCode == Activity.RESULT_OK) {

                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                Log.e(TAG, confirm.toJSONObject().toString());
                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e(TAG, paymentDetails);

                        try {
                            JSONObject jsonDetails = new JSONObject(paymentDetails);
                            JSONObject jsonResponse = jsonDetails.getJSONObject("response");
                            transaction_ID = jsonResponse.getString("id");
                            createTime = jsonResponse.getString("create_time");
                            intent = jsonResponse.getString("intent");
                            paymentState = jsonResponse.getString("state");
                            Log.e(TAG, "RESPO :- \n" + "Trans_ID :- " + transaction_ID + "\nCreate Time :- " + createTime +
                                    "\nIntent :- " + intent + "\nPayment State :- " + paymentState);
                            amount = amountEditText.getText().toString().trim();
                            message = messageEditText.getText().toString().trim();
                            submitEGift(amount, message);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Sorry The Payment has been terminated", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
                Toast.makeText(getActivity(), "Sorry The Payment has been terminated", Toast.LENGTH_SHORT).show();


            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }

    }


    private void submitEGift(String amount, String message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.KEY_EGIFT_NEW);
        jsonObject.addProperty("send_user_id", send_UserId);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("coupon_amount", amount);
        jsonObject.addProperty("txn_id", transaction_ID);
        jsonObject.addProperty("payment_status", paymentState);
        jsonObject.addProperty("currency_code", "$");
        jsonObject.addProperty("email_id", usernameEditText.getText().toString());
        jsonObject.addProperty("gift_send_type", send_user_type);

        Log.e(TAG, "egift submit json :- " + jsonObject.toString());

        submit_EGiftTask(jsonObject);

    }

    private void submit_EGiftTask(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserSendGift(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "egift submit response :- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray result = jsonObject.getJSONArray("result");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject res = result.getJSONObject(i);
                            send_UserId = res.getString("send_user_id");
                        }
                        Intent intent = new Intent(getActivity(), EGiftApproved.class);
                        intent.putExtra("send_UserEmailId", send_UserId);
                        startActivity(intent);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Error in sending e-Gift !!!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Server not responding..");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        amount = amountEditText.getText().toString().trim();
                        message = messageEditText.getText().toString().trim();
                        submitEGift(amount, message);
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                progressHD.dismiss();
            }
        });

    }


}
