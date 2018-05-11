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
 * Created by Peter on 08-May-17.
 */

public class CateringOrderAdapter extends RecyclerView.Adapter<CateringOrderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetails> orderDetails;

    public CateringOrderAdapter(Context context) {
        this.context = context;
    }

    public CateringOrderAdapter(Context context, ArrayList<OrderDetails> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @Override
    public CateringOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_today_take_out, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderDetails model = orderDetails.get(position);
        String date = model.getDate();
        String newDate = date.replaceAll("M","");
        holder.dateTextView.setText(newDate);
        holder.nameTextView.setText(model.getUser_name());
        holder.totalAmountTextView.setText(model.getTotal_price());
        holder.statusTextView.setText(model.getOrder_status());
        holder.orderTimeTextView.setText(model.getOrder_time());
        holder.dueTimeTextView.setText(model.getDue_time());

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
        LinearLayout recycler_item;
        CustomTextView dateTextView, nameTextView, orderTimeTextView, dueTimeTextView, totalAmountTextView, statusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            dateTextView = (CustomTextView) itemView.findViewById(R.id.dateTextView);
            nameTextView = (CustomTextView) itemView.findViewById(R.id.nameTextView);
            orderTimeTextView = (CustomTextView) itemView.findViewById(R.id.orderTimeTextView);
            dueTimeTextView = (CustomTextView) itemView.findViewById(R.id.dueTimeTextView);
            totalAmountTextView = (CustomTextView) itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = (CustomTextView) itemView.findViewById(R.id.statusTextView);
            recycler_item = (LinearLayout) itemView.findViewById(R.id.recycler_item);
        }
    }

}


