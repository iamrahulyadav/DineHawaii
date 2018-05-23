package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Activity.VendorItemListActivity;
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;

import java.util.ArrayList;


public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.ViewHolder> {
    private static final String TAG = "ManageVendorAdapter";
    Context context;
    ArrayList<VendorMasterData> vendorModelArrayList;

    public VendorListAdapter(Context context, ArrayList<VendorMasterData> reservList) {
        this.context = context;
        this.vendorModelArrayList = reservList;
    }

    @Override
    public VendorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_user_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VendorMasterData model = vendorModelArrayList.get(position);
        holder.tvName.setText(model.getSub_vendor_fn() + " " + model.getSub_vendor_ln());
        holder.tvBusname.setText(model.getSub_vendor_busname());
        holder.tvVendorcontact.setText(model.getSub_vendor_contact());
        holder.tvtitle.setText(model.getSub_vendor_email());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getSub_vendor_categ().equalsIgnoreCase("Delivery Vendor")) {

                } else {
                    Intent intent = new Intent(context, VendorItemListActivity.class);
                    intent.putExtra("vendor_id", model.getSub_vendor_id());
                    intent.putExtra("vendor_name", model.getSub_vendor_busname());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvName, tvBusname, tvVendorcontact,  tvtitle;
        LinearLayout llemail, llcontact, llbusnm;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvBusname = (CustomTextView) itemView.findViewById(R.id.tvBusname);
            tvVendorcontact = (CustomTextView) itemView.findViewById(R.id.tvVendorcontact);
            tvtitle = (CustomTextView) itemView.findViewById(R.id.tvtitle);
            cardView = (CardView) itemView.findViewById(R.id.cardView_Vendor);
        }
    }
}