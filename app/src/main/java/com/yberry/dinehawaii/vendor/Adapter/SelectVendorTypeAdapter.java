package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;

import java.util.ArrayList;

public class SelectVendorTypeAdapter extends RecyclerView.Adapter<SelectVendorTypeAdapter.ViewHolder> {
    Context context;
    ArrayList<VendorMasterData> arrayList;
    String listType;

    public SelectVendorTypeAdapter(Context context, ArrayList<VendorMasterData> list,String listType) {
        this.context = context;
        this.arrayList = list;
        this.listType= listType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VendorMasterData model = arrayList.get(position);
        if (listType.equalsIgnoreCase("sub_category"))
            holder.offercode.setText(model.getSub_vendor_busname());
        else if (listType.equalsIgnoreCase("category"))
            holder.offercode.setText(model.getMaster_vendor_name());
        // holder.vendor_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.vendor));
        holder.vendor_icon.setBackgroundResource(R.drawable.vendor);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView offercode, amount, descrp;
        ImageView vendor_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            offercode = (CustomTextView) itemView.findViewById(R.id.tvOffername);
            amount = (CustomTextView) itemView.findViewById(R.id.offerdamt);
            descrp = (CustomTextView) itemView.findViewById(R.id.offerdesc);
            vendor_icon = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}