package com.yberry.dinehawaii.vendor.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.BidDetailsModel;

import java.util.ArrayList;

/**
 * Created by Peter on 11-Apr-17.
 */

public class VendorBidDetailsAdapter extends RecyclerView.Adapter<VendorBidDetailsAdapter.MyViewHolder> {

    private static final String TAG = "VendorBidDetails";
    ArrayList<BidDetailsModel> list;
    private Context mContext;
    private Dialog popup;

    public VendorBidDetailsAdapter(Context mContext, ArrayList<BidDetailsModel> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vendor_bid_item_detail_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
        final BidDetailsModel model = list.get(pos);
        myViewHolder.tvItemName.setText(model.getItemName());
        myViewHolder.tvVendorName.setText("(" + model.getVendorName() + ")");
        myViewHolder.tvItemQty.setText(model.getItemQuantity());
        myViewHolder.tvItemTotalPrice.setText(model.getVendorBidAmount());
        myViewHolder.tvyourPrice.setText("$" + model.getBusinessBidAmt());
        myViewHolder.tvFinalPrice.setText("$" + model.getVendorBidFinalAmount());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvItemName, tvVendorName, tvItemTotalPrice, tvItemQty, tvyourPrice, tvFinalPrice;

        public MyViewHolder(View convertView) {

            super(convertView);
            tvItemName = (CustomTextView) convertView.findViewById(R.id.tvItemName);
            tvVendorName = (CustomTextView) convertView.findViewById(R.id.tvVendorName);
            tvItemQty = (CustomTextView) convertView.findViewById(R.id.tvItemQty);
            tvyourPrice = (CustomTextView) convertView.findViewById(R.id.tvyourPrice);
            tvItemTotalPrice = (CustomTextView) convertView.findViewById(R.id.tvItemTotalPrice);
            tvFinalPrice = (CustomTextView) convertView.findViewById(R.id.tvFinalPrice);

        }
    }
}
