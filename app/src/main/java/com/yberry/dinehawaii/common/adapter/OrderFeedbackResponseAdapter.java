package com.yberry.dinehawaii.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.common.models.FeedbackResponseData;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.List;


public class OrderFeedbackResponseAdapter extends RecyclerView.Adapter<OrderFeedbackResponseAdapter.ViewHolder> {
    final static String TAG = "FeedbackResponseAdapter";
    Context context;
    List<FeedbackResponseData> feedbackData;

    public OrderFeedbackResponseAdapter(Context context, List<FeedbackResponseData> reservationData) {
        this.context = context;
        this.feedbackData = reservationData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_feedback_response_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FeedbackResponseData orderDetails = feedbackData.get(position);
        holder.tvTitle.setText(orderDetails.getResponseText());
        holder.tvTitle1.setText(orderDetails.getResponseText());
        holder.tvUserName.setText("by " + orderDetails.getResponseByUserName());
        holder.tvUserName1.setText("by " + orderDetails.getResponseByUserName());
        Log.e(TAG, "onBindViewHolder: rrr " + position % 2);
        if (position % 2 == 0) {
            holder.llLeft.setVisibility(View.VISIBLE);
            holder.llright.setVisibility(View.GONE);
            holder.tvUserName.setGravity(Gravity.RIGHT | Gravity.END);
            holder.tvTitle.setGravity(Gravity.RIGHT | Gravity.END);
        } else {
            holder.llLeft.setVisibility(View.GONE);
            holder.llright.setVisibility(View.VISIBLE);
            holder.tvUserName1.setGravity(Gravity.RIGHT | Gravity.END);
            holder.tvTitle1.setGravity(Gravity.RIGHT | Gravity.END);
        }
    }

    @Override
    public int getItemCount() {
        return feedbackData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvUserName, tvUserName1, tvTitle, tvTitle1;
        LinearLayout llLeft, llright;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = (CustomTextView) itemView.findViewById(R.id.tvUserName);
            tvUserName1 = (CustomTextView) itemView.findViewById(R.id.tvUserName1);
            tvTitle = (CustomTextView) itemView.findViewById(R.id.tvTitle);
            tvTitle1 = (CustomTextView) itemView.findViewById(R.id.tvTitle1);
            llLeft = itemView.findViewById(R.id.llLeft);
            llright = itemView.findViewById(R.id.llright);

        }
    }
}
