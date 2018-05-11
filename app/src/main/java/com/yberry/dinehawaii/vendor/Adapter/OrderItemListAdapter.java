package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.VendorOrderDBHandler;
import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;
import com.yberry.dinehawaii.vendor.Model.VendorlistDataModel;

import java.util.ArrayList;


public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.ViewHolder> {
    private static final String TAG = "OrderItemListAdapter";
    Context context;
    ArrayList<VendorlistDataModel> vendorModelArrayList;
    private String vendor_id = "";

    public OrderItemListAdapter(Context context, ArrayList<VendorlistDataModel> reservList, String vendor_id) {
        this.context = context;
        this.vendorModelArrayList = reservList;
        this.vendor_id = vendor_id;
    }

    @Override
    public OrderItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VendorlistDataModel model = vendorModelArrayList.get(position);
        holder.itemName.setText(model.getItemName());
        holder.itemPrice.setText("$"+model.getPrice());
        final VendorOrderItemsDetailsModel detailsModel = new VendorOrderItemsDetailsModel();
        holder.btnplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsModel.setItemId(model.getItemId());
                detailsModel.setItemName(model.getItemName());
                detailsModel.setItemPrice(model.getPrice().trim());
                detailsModel.setItemTotalCost(model.getPrice().trim());
                detailsModel.setItemQuan("1");
                detailsModel.setVendorId(vendor_id);
                detailsModel.setProductId(model.getProductId());
                new VendorOrderDBHandler(context).insertVendorOrderCartItem(detailsModel);
                Toast.makeText(context, model.getItemName() + " Added", Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("update_counter"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView itemName, itemPrice;
        CustomButton btnplaceorder;
        LinearLayout llprice;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (CustomTextView) itemView.findViewById(R.id.tvName);
            itemPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
            btnplaceorder = (CustomButton) itemView.findViewById(R.id.btnplaceorder);
        }
    }
}