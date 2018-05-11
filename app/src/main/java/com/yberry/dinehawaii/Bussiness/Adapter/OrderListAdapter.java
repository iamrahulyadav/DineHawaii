package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Customer.Adapter.ReservationAdapter;
import com.yberry.dinehawaii.R;

/**
 * Created by abc on 8/11/2017.
 */
/*
public class OrderListAdapter {
}*/

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    Context context;


    public OrderListAdapter(Context context){
        this.context = context;

    }



    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_data, parent, false);
        OrderListAdapter.ViewHolder viewHolder = new OrderListAdapter.ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }










    @Override
    public int getItemCount() {
        return 9;

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);


        }


    }}
