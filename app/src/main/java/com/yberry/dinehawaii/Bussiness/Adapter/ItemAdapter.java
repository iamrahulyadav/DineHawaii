package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Bussiness.model.OrderDetailItemData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Hvantage2 on 2018-02-13.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OrderDetailItemData> orderDetails;

    public ItemAdapter(Context context, ArrayList<OrderDetailItemData> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        double total = 0;
        OrderDetailItemData model = orderDetails.get(position);
        Log.e("ItemAdapter", "onBindViewHolder: model >> " + model);
        holder.tvItemName.setText(model.getFoodName() + "(Qty : " + model.getQuantity() + ")" + "\n" + "from " + model.getFood_area_name());
        if (model.getQuantity().equalsIgnoreCase("1/2"))
            total = Double.parseDouble(model.getItemPrice());
        else
            total = Double.parseDouble(model.getItemPrice()) * Integer.parseInt(model.getQuantity());

        if (model.getItemCustomization().equalsIgnoreCase(""))
            holder.tvCustomization.setText("-");
        else holder.tvCustomization.setText(model.getItemCustomization());
        holder.tvPrice.setText("$" + String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvItemName, tvCustomization, tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = (CustomTextView) itemView.findViewById(R.id.tvItemName);
            tvCustomization = (CustomTextView) itemView.findViewById(R.id.tvCustomization);
            tvPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
        }
    }
}


