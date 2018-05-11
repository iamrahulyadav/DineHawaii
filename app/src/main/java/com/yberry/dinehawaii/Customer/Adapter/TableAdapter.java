package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yberry.dinehawaii.R;




public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    Context context;
    LayoutInflater mInflater;


    public TableAdapter(Context context) {
        this.context = context;
//        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public TableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.walikin_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TableAdapter.ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {


        public ViewHolder(View itemView) {
            super(itemView);


        }


    }

}


/*public class TableAdapter {
}*/
