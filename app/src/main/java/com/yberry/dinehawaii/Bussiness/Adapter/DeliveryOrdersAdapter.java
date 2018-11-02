package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yberry.dinehawaii.Bussiness.Activity.OrderDetailActivty;
import com.yberry.dinehawaii.Bussiness.model.OrderDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Peter on 05-May-17.
 */

public class DeliveryOrdersAdapter extends RecyclerView.Adapter<DeliveryOrdersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetails> orderDetails;

    public DeliveryOrdersAdapter(Context context) {
        this.context = context;
    }

    public DeliveryOrdersAdapter(Context context, ArrayList<OrderDetails> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_delivery, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderDetails model = orderDetails.get(position);
        String date = model.getDate();
        String newDate = date.replaceAll("M", "");
        holder.dateTextView.setText(newDate);
        holder.tvOrderId.setText(model.getOrderId());
        holder.tvTableNo.setText("");
        holder.tvTableNoText.setText("");
        holder.nameTextView.setText(model.getDeliveryName());
        holder.totalAmountTextView.setText(model.getTotalPrice());
        holder.statusTextView.setText(model.getOrderStatus());
        holder.tvSource.setText(model.getOrderAddedBy());

        holder.recycler_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetails model = orderDetails.get(position);
                Intent intent = new Intent(context, OrderDetailActivty.class);
                intent.setAction("buss");
                intent.putExtra("order_id", model.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        CustomTextView tvSource, tvOrderId, tvTableNo, tvTableNoText, dateTextView, nameTextView, totalAmountTextView, statusTextView;
        LinearLayout recycler_item;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            tvOrderId = (CustomTextView) itemView.findViewById(R.id.tvOrderId);
            tvTableNoText = (CustomTextView) itemView.findViewById(R.id.tvTableNoText);
            tvTableNo = (CustomTextView) itemView.findViewById(R.id.tvTableNo);
            dateTextView = (CustomTextView) itemView.findViewById(R.id.dateTextView);
            nameTextView = (CustomTextView) itemView.findViewById(R.id.nameTextView);
            totalAmountTextView = (CustomTextView) itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = (CustomTextView) itemView.findViewById(R.id.statusTextView);
            tvSource = (CustomTextView) itemView.findViewById(R.id.tvSource);
            recycler_item = (LinearLayout) itemView.findViewById(R.id.recycler_item);
        }
    }
}
