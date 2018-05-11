package com.yberry.dinehawaii.Customer.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Fragment.LoyaltyPointFragment;
import com.yberry.dinehawaii.Model.LoyaltyPointModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
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

public class LoyaltyPointAdapter extends RecyclerView.Adapter<LoyaltyPointAdapter.ViewHolder> {

    Context context;
    ArrayList<LoyaltyPointModel> loyaltyList;
    int checkboxId = 0;
    private CustomTextView total;
    private CustomEditText coupon;
    private CustomEditText et_email;
    private String point;
    private String recipents_email, recipents_point;
    private int busi_id;

    public LoyaltyPointAdapter(Context context, ArrayList<LoyaltyPointModel> loyaltyList) {
        this.context = context;
        this.loyaltyList = loyaltyList;
    }

    @Override
    public LoyaltyPointAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loyalty_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LoyaltyPointAdapter.ViewHolder holder, int position) {

        final LoyaltyPointModel model = loyaltyList.get(position);
        holder.businessName.setText(model.getBussName());
        holder.totalPoint.setText(model.getTotalPoints());
        //holder.date.setText(model.getDate());
        holder.transferPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("idddddddddd bus id",model.getBussId()+"");
                Log.e("idddddddddd 1",checkboxId+"");
                busi_id = Integer.parseInt(model.getBussId());
                openCheckDialog(checkboxId,model.getTotalPoints());
            }
        });
    }

    private void openCheckDialog(final int checkboxId, final String points) {
        final Dialog popup = new Dialog(context);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.reedem_loyalty_points);
        coupon = (CustomEditText) popup.findViewById(R.id.lpoints);
        total = (CustomTextView) popup.findViewById(R.id.totalPoints);
        et_email = (CustomEditText) popup.findViewById(R.id.et_email);
        total.setText(points);
        popup.findViewById(R.id.popupclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.findViewById(R.id.apply_points).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coupon.getText().toString().equalsIgnoreCase("")||coupon.getText().toString().equalsIgnoreCase("0")){
                    coupon.setError("Enter points");
                }  else if (TextUtils.isEmpty(et_email.getText().toString())) {
                    et_email.setError("Enter Email Id");
                }else{

                    recipents_point=coupon.getText().toString();
                    recipents_email=et_email.getText().toString();
                    popup.dismiss();
                    checkUserEmail();
                   // LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                   // Toast.makeText(context, "Now you can send points!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        coupon.setFilters(new InputFilter[]{new InputFilterMinMax("1", "" + points)});
        popup.show();
    }
    public class InputFilterMinMax implements InputFilter {

        private double min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Double.parseDouble(min);
            this.max = Double.parseDouble(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                double input = Double.parseDouble(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(double a, double b, double c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    @Override
    public int getItemCount() {
        return loyaltyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CustomTextView businessName, totalPoint;
        public ImageView transferPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            businessName = (CustomTextView) itemView.findViewById(R.id.businessName);
            totalPoint = (CustomTextView) itemView.findViewById(R.id.totalPoint);
           // date = (CustomTextView) itemView.findViewById(R.id.date);
            transferPoint = (ImageView) itemView.findViewById(R.id.transfer);
            transferPoint = (ImageView) itemView.findViewById(R.id.transfer);
        }
    }


    private void checkUserEmail() {
            if (Util.isNetworkAvailable(context)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.CHECK_EMAILID);
                jsonObject.addProperty(AppConstants.KEY_EMAILID, recipents_email);
                Log.e("LoyaltyAdapter", "check user loyalty json :- " + jsonObject.toString());
                checkEmailApi(jsonObject);
            }
    }

    private void checkEmailApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e("LoyaltyAdapter", " egift check user response:- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String send_UserId;
                    String useremail_type;
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray result = jsonObject.getJSONArray("result");
                        JSONObject res = result.getJSONObject(0);
                        send_UserId = res.getString("id");
                        useremail_type = "reg";
                        sendLoayaltyPoints(send_UserId, useremail_type);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        send_UserId = "0";
                        useremail_type = "non";
                        sendLoayaltyPoints(send_UserId , useremail_type);
                    } else {
                        Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("LoyaltyAdapter", "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server not responding!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendLoayaltyPoints(String send_userId, String useremail_type) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.SENDLOYALTYPOINTS);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
        jsonObject.addProperty("send_user_id", send_userId);
        jsonObject.addProperty("business_id", busi_id);
        jsonObject.addProperty("points", recipents_point);
        jsonObject.addProperty("email_id", recipents_email);
        jsonObject.addProperty("loyal_send_type", useremail_type);
        Log.e("LoyaltyAdapter", " points send  user json:- " + jsonObject.toString());
        sendPointsApi(jsonObject);
    }

    private void sendPointsApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("LoyaltyAdapter", " points send  user response:- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray result = jsonObject.getJSONArray("result");
                        JSONObject res = result.getJSONObject(0);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("loyaltyPoints"));
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray result = jsonObject.getJSONArray("result");
                        JSONObject res = result.getJSONObject(0);
                        Toast.makeText(context, res.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("LoyaltyAdapter", "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server not responding!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}