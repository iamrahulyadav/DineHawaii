package com.yberry.dinehawaii.Bussiness.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.model.OrderDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.Function.fieldRequired;

/**
 * Created by abc on 05-Apr-18.
 */

public class OrderFeedbackAdapter extends RecyclerView.Adapter<OrderFeedbackAdapter.ViewHolder> {
    final static String TAG = "OrderFeedbackAdapter";
    Context context;
    ArrayList<OrderDetails> feedbackData = new ArrayList<OrderDetails>();
    private AlertDialog deliveryDialog;

    public OrderFeedbackAdapter(Context context, ArrayList<OrderDetails> reservationData) {
        this.context = context;
        this.feedbackData = reservationData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_feedback_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OrderDetails orderDetails = feedbackData.get(position);
        holder.orderid.setText("#" + orderDetails.getOrder_id());
        holder.name.setText(orderDetails.getUser_name());
        holder.message.setText(orderDetails.getReviewmsg());
        holder.tvSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogReply(orderDetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbackData.size();
    }

    @SuppressLint("RestrictedApi")
    public void dialogReply(final OrderDetails orderDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater _inflater = LayoutInflater.from(context);
        View view = _inflater.inflate(R.layout.reply_dialog, null);
        builder.setView(view, 50, 50, 50, 50);
        builder.setCancelable(true);
        deliveryDialog = builder.create();
        deliveryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deliveryDialog.setCanceledOnTouchOutside(false);

        final EditText etText = (CustomEditText) view.findViewById(R.id.etText);
        final RadioGroup rgStatus = (RadioGroup) view.findViewById(R.id.rgStatus);
        CustomButton dsubmit = (CustomButton) view.findViewById(R.id.dsubmit);

        ImageView close = (ImageView) view.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryDialog.dismiss();
            }
        });

        dsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etText.getText().toString())) {
                    etText.setError(fieldRequired);
                } else {
                    String status = "";
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    switch (rgStatus.getCheckedRadioButtonId()) {
                        case R.id.rbNone:
                            status = "";
                            break;
                        case R.id.rbSolution:
                            status = "Solution";
                            break;
                        case R.id.rbResolved:
                            status = "Resolved";
                            break;
                    }
                    deliveryDialog.hide();
                    if (Util.isNetworkAvailable(context)) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.SEND_REPLY_TO_ORDERS);
                        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));// AppPreferencesBuss.getUserId(context)
                        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
                        jsonObject.addProperty("order_id", orderDetails.getOrder_id());
                        jsonObject.addProperty("status", status);
                        jsonObject.addProperty("text_reply", etText.getText().toString());
                        Log.e(TAG, "Send Reply: Request >> " + jsonObject.toString());
                        replyApi(jsonObject);
                    } else {
                        Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        deliveryDialog.show();

    }

    private void replyApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Send Reply: Response >> " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressHD.dismiss();
                    Toast.makeText(context, "Server Not Responding", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server Not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView name, message, orderid, tvSendReply;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (CustomTextView) itemView.findViewById(R.id.username);
            orderid = (CustomTextView) itemView.findViewById(R.id.orderId);
            message = (CustomTextView) itemView.findViewById(R.id.msg);
            tvSendReply = (CustomTextView) itemView.findViewById(R.id.tvSendReply);
        }
    }
}
