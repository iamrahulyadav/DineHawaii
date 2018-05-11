package com.yberry.dinehawaii.Bussiness.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

/**
 * Created by PRINCE 9977123453 on 08-02-17.
 */

public class PackageAdapterPayment extends RecyclerView.Adapter<PackageAdapterPayment.ViewHolder> {

    private static final String TAG = "PackageAdapter";
    private Context context;
    private ArrayList<CheckBoxPositionModel> list;

    public PackageAdapterPayment(Context context, ArrayList<CheckBoxPositionModel> list) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public PackageAdapterPayment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_pos_check_box2, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final PackageAdapterPayment.ViewHolder holder, final int position) {
        final CheckBoxPositionModel datalist = list.get(position);

        if (datalist != null) {
            holder.tvTitle.setText("Package " + (position + 1) + ": " + datalist.getName());

            if (datalist.isChckStatus())
                holder.imageView.setVisibility(View.VISIBLE);
            else
                holder.imageView.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView tvTitle;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (CustomTextView) itemView.findViewById(R.id.tvTitle);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setVisibility(View.GONE);
        }
    }
}