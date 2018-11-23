package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorOrderDetailItem;

import java.util.List;

/**
 * Created by Hvantage2 on 2018-02-13.
 */

public class VendorItemAdapter extends RecyclerView.Adapter<VendorItemAdapter.ViewHolder> {
    private final String TAG = "VendorItemAdapter";
    private Context context;
    private List<VendorOrderDetailItem> orderDetails;

    public VendorItemAdapter(Context context, List<VendorOrderDetailItem> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;

    }

    @Override
    public VendorItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_order_detail_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        double total = 0;
        VendorOrderDetailItem model = orderDetails.get(position);
        Log.e("ItemAdapter", "onBindViewHolder: model >> " + model);
        holder.tvItemName.setText(model.getItem_name() + " (Qty : " + model.getItemQuantity() + ")");
        total = Double.parseDouble(model.getItemAmount()) * Integer.parseInt(model.getItemQuantity());
        holder.tvPrice.setText("$" + String.valueOf(total));
    }


    @Override
    public int getItemCount() {
        return orderDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvItemName, tvCustomization, tvPrice;
        ImageView imageStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = (CustomTextView) itemView.findViewById(R.id.tvItemName);
            tvCustomization = (CustomTextView) itemView.findViewById(R.id.tvCustomization);
            tvPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
            imageStatus = (ImageView) itemView.findViewById(R.id.imageStatus);
        }
    }
}


