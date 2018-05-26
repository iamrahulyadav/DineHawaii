package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class CustomizationItemsAdapter extends RecyclerView.Adapter<CustomizationItemsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> itemsList;

    public CustomizationItemsAdapter(Context context, ArrayList<String> chairDataList) {
        this.context = context;
        this.itemsList = chairDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customization_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvItemNo.setText(position + 1 + ".");
        holder.tv_ItemName.setText(itemsList.get(position));
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvItemNo,tv_ItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_ItemName = (CustomTextView) itemView.findViewById(R.id.tv_ItemName);
            tvItemNo = (CustomTextView) itemView.findViewById(R.id.tvItemNo);
        }
    }
}


