package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Bussiness.model.BusinessAreaModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;


public class BusinessAreaAdapter extends RecyclerView.Adapter<BusinessAreaAdapter.ViewHolder> {
    private static final String TAG = "BusinessAreaAdapter";
    Context context;
    ArrayList<BusinessAreaModel> areaList;

    public BusinessAreaAdapter(Context context, ArrayList<BusinessAreaModel> areaList) {
        this.context = context;
        this.areaList = areaList;
    }

    @Override
    public BusinessAreaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_cat_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final BusinessAreaAdapter.ViewHolder holder, final int position) {
        final BusinessAreaModel model = areaList.get(position);
        holder.tvAreaName.setText(model.getAreaName());
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvAreaName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAreaName = (CustomTextView) itemView.findViewById(R.id.tvName);
        }
    }
}