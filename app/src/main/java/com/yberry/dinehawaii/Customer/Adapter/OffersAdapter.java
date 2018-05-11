package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Customer.Activity.CheckOutActivity;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class OffersAdapter extends  RecyclerView.Adapter<OffersAdapter.ViewHolder>{
    Context context;
    ArrayList<CustomerModel> arrayList;

    public OffersAdapter(Context context, ArrayList<CustomerModel> list) {
        this.context = context;
        this.arrayList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CustomerModel model = arrayList.get(position);
        holder.offercode.setText(model.getOffer_name());
        holder.amount.setText(model.getOffer_amount());
        holder.descrp.setText(model.getOffer_decp());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView offercode,amount,descrp;

        public ViewHolder(View itemView) {
            super(itemView);
            offercode = (CustomTextView)itemView.findViewById(R.id.tvOffername);
            amount = (CustomTextView)itemView.findViewById(R.id.offerdamt);
            descrp = (CustomTextView)itemView.findViewById(R.id.offerdesc);
        }
    }
}