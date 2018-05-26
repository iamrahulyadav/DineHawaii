package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorOrderHistoryModel;

import java.util.ArrayList;

/**
 * Created by Peter on 05-May-17.
 */

public class VendorOrderHistoryAdapter extends RecyclerView.Adapter<VendorOrderHistoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<VendorOrderHistoryModel> orderDetails;



    public VendorOrderHistoryAdapter(Context context, ArrayList<VendorOrderHistoryModel> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_orderhist_adapter_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        VendorOrderHistoryModel model = orderDetails.get(position);
        holder.dateTextView.setText(model.getDateTime());
        holder.nameTextView.setText(model.getVendorName());
        holder.tvOrderItems.setText(model.getItemName());
        holder.totalAmountTextView.setText("$"+model.getAmount());
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        CustomTextView dateTextView, nameTextView, totalAmountTextView, statusTextView,tvOrderItems;
        LinearLayout recycler_item;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            dateTextView = (CustomTextView) itemView.findViewById(R.id.dateTextView);
            nameTextView = (CustomTextView) itemView.findViewById(R.id.nameTextView);
            totalAmountTextView = (CustomTextView) itemView.findViewById(R.id.totalAmountTextView);
            statusTextView = (CustomTextView) itemView.findViewById(R.id.statusTextView);
            tvOrderItems = (CustomTextView) itemView.findViewById(R.id.tvOrderItems);
            recycler_item = (LinearLayout) itemView.findViewById(R.id.recycler_item);
        }
    }
}
