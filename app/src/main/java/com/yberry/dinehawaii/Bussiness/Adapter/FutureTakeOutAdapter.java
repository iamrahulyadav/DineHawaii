package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by Peter on 08-May-17.
 */

public class FutureTakeOutAdapter extends RecyclerView.Adapter<FutureTakeOutAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetails> orderDetails;

    public FutureTakeOutAdapter(Context context) {
        this.context = context;
    }

    public FutureTakeOutAdapter(Context context, ArrayList<OrderDetails> orderDetails) {

        this.context = context;
        this.orderDetails = orderDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_future_take_out, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderDetails model = orderDetails.get(position);
        holder.dateTextView.setText(model.getDate());
        holder.nameTextView.setText(model.getUser_name());
        holder.totalAmountTextView.setText(model.getTotal_price());
        holder.statusTextView.setText(model.getOrder_status());
        holder.pickupNameNoTextView.setText(model.getDelivery_adderess());
        holder.tvSource.setText(model.getOrder_source());
        Log.e("order_data", model.toString());

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
        CustomTextView tvSource, dateTextView, nameTextView, pickupNameNoTextView, orderTimeTextView, dueTimeTextView, totalAmountTextView, statusTextView;
        LinearLayout recycler_item;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            dateTextView = (CustomTextView) itemView.findViewById(R.id.dateTextView);
            nameTextView = (CustomTextView) itemView.findViewById(R.id.nameTextView);
            pickupNameNoTextView = (CustomTextView) itemView.findViewById(R.id.pickupNameNoTextView);
            orderTimeTextView = (CustomTextView) itemView.findViewById(R.id.orderTimeTextView);
            dueTimeTextView = (CustomTextView) itemView.findViewById(R.id.dueTimeTextView);
            totalAmountTextView = (CustomTextView) itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = (CustomTextView) itemView.findViewById(R.id.statusTextView);
            recycler_item = (LinearLayout) itemView.findViewById(R.id.recycler_item);
            tvSource = (CustomTextView) itemView.findViewById(R.id.tvSource);

        }
    }

}
