package com.yberry.dinehawaii.Customer.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yberry.dinehawaii.Customer.Activity.CustomerResDetailActivity;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.List;


public class WaitlistAdapter extends RecyclerView.Adapter<WaitlistAdapter.ViewHolder> {

    Context context;
    private List<CustomerModel> waitlist;


    public WaitlistAdapter(Context context, List<CustomerModel> waitlist) {
        this.context = context;
        this.waitlist = waitlist;

    }

    @Override
    public WaitlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_wait_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WaitlistAdapter.ViewHolder holder, int position) {
        final CustomerModel model = waitlist.get(position);
        Log.d("model", String.valueOf(waitlist.get(position)));
        holder.reservation_id.setText(model.getWaitlist_reservation_id());
        holder.business_name.setText(model.getWaitlist_business_name());
        holder.date.setText(model.getWaitlist_date());
        holder.time.setText(model.getWaitlist_time());
    }

    @Override
    public int getItemCount() {
        return waitlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView reservation_id, business_name,date, time;
        CardView cardViewWaitList;

        public ViewHolder(View itemView) {
            super(itemView);
            reservation_id = (CustomTextView) itemView.findViewById(R.id.reservation_id);
            business_name = (CustomTextView) itemView.findViewById(R.id.business_name);
            date = (CustomTextView) itemView.findViewById(R.id.date);
            time = (CustomTextView) itemView.findViewById(R.id.time);
            cardViewWaitList = (CardView) itemView.findViewById(R.id.cardWaitlist);

        }


    }
}