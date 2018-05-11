package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;

import java.util.ArrayList;


public class ManageVendorAdapter extends RecyclerView.Adapter<ManageVendorAdapter.ViewHolder> {
    private static final String TAG = "ManageVendorAdapter";
    Context context;
    ArrayList<VendorMasterData> vendorModelArrayList;

    public ManageVendorAdapter(Context context, ArrayList<VendorMasterData> reservList) {
        this.context = context;
        this.vendorModelArrayList = reservList;
    }

    @Override
    public ManageVendorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_cat_list_layout, parent, false);
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
        holder.vendorName.setText((position + 1) + ". " + model.getMaster_vendor_name());
       /* holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorListActivity.class);
                intent.putExtra("category_id", model.getMaster_vendor_id());
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView vendorName, vendorBusName, vendorContact;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            vendorName = (CustomTextView) itemView.findViewById(R.id.tvName);
            vendorBusName = (CustomTextView) itemView.findViewById(R.id.tvBusname);
            vendorContact = (CustomTextView) itemView.findViewById(R.id.tvVendorcontact);
            cardView = (CardView) itemView.findViewById(R.id.cardView_Vendor);
        }
    }
}