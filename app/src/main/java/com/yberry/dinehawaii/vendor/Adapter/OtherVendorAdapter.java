package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorBidItemModel;

import java.util.ArrayList;


public class OtherVendorAdapter extends RecyclerView.Adapter<OtherVendorAdapter.ViewHolder> {
    private static final String TAG = "ManageVendorAdapter";
    Context context;
    ArrayList<VendorBidItemModel> vendorModelArrayList;

    public OtherVendorAdapter(Context context, ArrayList<VendorBidItemModel> reservList) {
        this.context = context;
        this.vendorModelArrayList = reservList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.othervendors_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        VendorBidItemModel model = vendorModelArrayList.get(position);
        holder.txtName.setText((position + 1) + ". " + model.getVendor_name());
        holder.txtprice.setText("$" + model.getVendor_item_price());
    }

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtName, txtprice;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (CustomTextView) itemView.findViewById(R.id.vendorname);
            txtprice = (CustomTextView) itemView.findViewById(R.id.vendprice);
        }
    }
}