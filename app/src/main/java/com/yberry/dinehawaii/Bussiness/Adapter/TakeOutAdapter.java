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

public class TakeOutAdapter extends RecyclerView.Adapter<TakeOutAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetails> orderDetails;

    public TakeOutAdapter(Context context) {
        this.context = context;
    }

    public TakeOutAdapter(Context context, ArrayList<OrderDetails> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @Override
    public TakeOutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_today_take_out, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderDetails model = orderDetails.get(position);
        String date = model.getDate();
        String newDate = date.replaceAll("M", "");
        holder.dateTextView.setText(newDate);
        holder.tvDateTimePickup.setText(model.getDueTime());
        holder.tvOrderId.setText(model.getOrderId());
        holder.nameTextView.setText(model.getUserName());
        holder.totalAmountTextView.setText(model.getTotalPrice());
        holder.statusTextView.setText(model.getOrderStatus());
        holder.tvSource.setText(model.getOrderAddedBy());

       /* if (model.getOrder_time().contains("AM")) {
            String replace = model.getOrder_time().replace("AM", "a");
            holder.orderTimeTextView.setText(replace);
        }
        else if (model.getOrder_time().contains("PM")) {
            String replace1 = model.getOrder_time().replace("PM", "p");
            holder.orderTimeTextView.setText(replace1);
        }
        else*/
        holder.orderTimeTextView.setText(model.getDate());

        holder.dueTimeTextView.setText(model.getDueTime());

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
        CustomTextView tvSource, dateTextView, tvDateTimePickup, tvOrderId, nameTextView, orderTimeTextView, dueTimeTextView, totalAmountTextView, statusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            dateTextView = (CustomTextView) itemView.findViewById(R.id.dateTextView);
            tvDateTimePickup = (CustomTextView) itemView.findViewById(R.id.tvDateTimePickup);
            tvOrderId = (CustomTextView) itemView.findViewById(R.id.tvOrderId);
            nameTextView = (CustomTextView) itemView.findViewById(R.id.nameTextView);
            orderTimeTextView = (CustomTextView) itemView.findViewById(R.id.orderTimeTextView);
            dueTimeTextView = (CustomTextView) itemView.findViewById(R.id.dueTimeTextView);
            totalAmountTextView = (CustomTextView) itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = (CustomTextView) itemView.findViewById(R.id.statusTextView);
            tvSource = (CustomTextView) itemView.findViewById(R.id.tvSource);

            recycler_item = (LinearLayout) itemView.findViewById(R.id.recycler_item);
        }
    }

}


