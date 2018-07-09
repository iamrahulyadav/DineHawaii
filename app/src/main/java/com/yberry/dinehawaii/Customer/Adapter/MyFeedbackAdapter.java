package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yberry.dinehawaii.Model.ReviewModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

/**
 * Created by abc on 05-Apr-18.
 */

public class MyFeedbackAdapter extends RecyclerView.Adapter<MyFeedbackAdapter.ViewHolder> {
    Context context;
    ArrayList<ReviewModel> feedbackData = new ArrayList<ReviewModel>();

    public MyFeedbackAdapter(Context context, ArrayList<ReviewModel> reservationData) {
        this.context = context;
        this.feedbackData = reservationData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_feedback_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewModel orderDetails = feedbackData.get(position);
        holder.tvRestName.setText("Restaurant : " + orderDetails.getBussName());
        holder.tvFeedback.setText("Feedback : " + orderDetails.getReview_message());
        holder.tvDate.setText(orderDetails.getDate());
    }

    @Override
    public int getItemCount() {
        return feedbackData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvRestName, tvFeedback, tvDate, orderId;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRestName = (CustomTextView) itemView.findViewById(R.id.tvRestName);
            tvDate = (CustomTextView) itemView.findViewById(R.id.tvDate);
            tvFeedback = (CustomTextView) itemView.findViewById(R.id.tvFeedback);
            orderId = (CustomTextView) itemView.findViewById(R.id.orderId);
        }


    }
}
