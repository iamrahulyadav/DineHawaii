package com.yberry.dinehawaii.Bussiness.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.ReviewModel;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.Function.fieldRequired;


/**
 * Created by abc on 8/22/2017.
 */

/*public class CustomerResturantAdapter  {
}*/



public class BusinessReviewAdapter extends RecyclerView.Adapter<BusinessReviewAdapter.ViewHolder> {

    final static String TAG = "OrderFeedbackAdapter";
    Context context;
    private List<ReviewModel> reviewlist;
    private AlertDialog deliveryDialog;

    public BusinessReviewAdapter(Context context, List<ReviewModel> reviewlist) {
        this.context = context;
        this.reviewlist = reviewlist;

    }

    @Override
    public BusinessReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_review_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BusinessReviewAdapter.ViewHolder holder, final int position) {
        final ReviewModel model = reviewlist.get(position);

        if (!model.getRating().equalsIgnoreCase(""))
            holder.ratingBar.setRating(Float.parseFloat(model.getRating()));
        holder.customer.setText("by " + model.getReview_question());
        holder.title.setText(model.getReview_message());
        holder.tvSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogReply(model);
            }
        });
        holder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogReject(model, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return reviewlist.size();

    }

    @SuppressLint("RestrictedApi")
    public void dialogReply(final ReviewModel orderDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater _inflater = LayoutInflater.from(context);
        View view = _inflater.inflate(R.layout.reply_dialog_review, null);
        builder.setView(view, 50, 50, 50, 50);
        builder.setCancelable(true);
        deliveryDialog = builder.create();
        deliveryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deliveryDialog.setCanceledOnTouchOutside(false);

        final EditText etText = (CustomEditText) view.findViewById(R.id.etText);
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
                    deliveryDialog.hide();
                    if (Util.isNetworkAvailable(context)) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.SEND_REPLY_TO_REVIEW);
                        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));// AppPreferencesBuss.getUserId(context)
                        jsonObject.addProperty("business_user_id", AppPreferencesBuss.getUserId(context));// AppPreferencesBuss.getUserId(context)
                        jsonObject.addProperty("user_id", orderDetails.getCustomer_id());
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

    public void dialogReject(ReviewModel model, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater _inflater = LayoutInflater.from(context);
        View view = _inflater.inflate(R.layout.dialog_review_reject, null);
        builder.setView(view, 50, 50, 50, 50);
        builder.setCancelable(true);
        deliveryDialog = builder.create();
        deliveryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deliveryDialog.setCanceledOnTouchOutside(false);

        final RadioGroup rgReason = (RadioGroup) view.findViewById(R.id.rgReason);
        CustomButton dsubmit = (CustomButton) view.findViewById(R.id.dsubmit);

        ImageView close = (ImageView) view.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deliveryDialog.dismiss();
            }
        });

        dsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryDialog.hide();
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
        CardView user_post;
        CustomTextView title, tvSendReply, tvReject, tvAccept, customer;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            user_post = (CardView) itemView.findViewById(R.id.user_post);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            title = (CustomTextView) itemView.findViewById(R.id.title);
            customer = (CustomTextView) itemView.findViewById(R.id.customer);
            tvSendReply = (CustomTextView) itemView.findViewById(R.id.tvSendReply);
            tvAccept = (CustomTextView) itemView.findViewById(R.id.tvAccept);
            tvReject = (CustomTextView) itemView.findViewById(R.id.tvReject);

        }
    }
}