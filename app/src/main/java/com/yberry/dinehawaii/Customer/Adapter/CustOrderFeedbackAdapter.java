package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.common.models.FeedbackData;
import com.yberry.dinehawaii.common.models.FeedbackResponseData;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;
import java.util.List;


public class CustOrderFeedbackAdapter extends RecyclerView.Adapter<CustOrderFeedbackAdapter.ViewHolder> {
    final static String TAG = "OrderFeedbackAdapter";
    private View view;
    private MyListener myListener;
    Context context;
    ArrayList<FeedbackData> feedbackData = new ArrayList<FeedbackData>();
    private List<FeedbackResponseData> list;

    public CustOrderFeedbackAdapter(Context context, ArrayList<FeedbackData> reservationData, MyListener myListener) {
        this.context = context;
        this.feedbackData = reservationData;
        this.myListener = myListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_feedback_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FeedbackData orderDetails = feedbackData.get(position);
        holder.orderid.setText("#" + orderDetails.getOrderId() + " (" + orderDetails.getDate() + ")");
        holder.name.setText(orderDetails.getUser());
        holder.message.setText(orderDetails.getTitle());
        holder.tvRemark.setText(orderDetails.getReviewMessage());
        holder.tvSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.onReplyClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbackData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView name, message, orderid, tvSendReply, tvRemark;
        RecyclerView recycler_view;
        LinearLayout llHideShow;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = (CustomTextView) itemView.findViewById(R.id.username);
            orderid = (CustomTextView) itemView.findViewById(R.id.orderId);
            message = (CustomTextView) itemView.findViewById(R.id.msg);
            tvSendReply = (CustomTextView) itemView.findViewById(R.id.tvSendReply);
            tvRemark = (CustomTextView) itemView.findViewById(R.id.tvRemark);
        }
    }


    public interface MyListener {
        public void onItemClick(View view, int position);

        public void onReplyClick(View view, int position);
    }
}
