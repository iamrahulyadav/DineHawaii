package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Bussiness.model.OrderDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

/**
 * Created by abc on 05-Apr-18.
 */

public class ResrvFeedbackAdapter extends RecyclerView.Adapter<ResrvFeedbackAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderDetails> feedbackData = new ArrayList<OrderDetails>();

    public ResrvFeedbackAdapter(Context context, ArrayList<OrderDetails> reservationData) {
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
        OrderDetails orderDetails = feedbackData.get(position);
        holder.orderid.setText("#"+orderDetails.getOrder_id());
        holder.name.setText(orderDetails.getUser_name());
        holder.message.setText(orderDetails.getReviewmsg());
    }

    @Override
    public int getItemCount() {
        return feedbackData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView name,message,orderid;
        public ViewHolder(View itemView) {
            super(itemView);
            name  = (CustomTextView)itemView.findViewById(R.id.username);
            orderid  = (CustomTextView)itemView.findViewById(R.id.orderId);
            message  = (CustomTextView)itemView.findViewById(R.id.msg);
        }


    }
}
