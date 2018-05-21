package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.vendor.Model.BidListItemModel;

import java.util.ArrayList;

public class BidListItemAdapter extends RecyclerView.Adapter<BidListItemAdapter.ViewHolder> {
    private static final String TAG = "BidListItemAdapter";
    Context context;
    ArrayList<BidListItemModel> vendorModelArrayList;

    public BidListItemAdapter(Context context, ArrayList<BidListItemModel> reservList) {
        this.context = context;
        this.vendorModelArrayList = reservList;
    }

    @Override
    public BidListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_list_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BidListItemModel model = vendorModelArrayList.get(position);

        holder.tvItemId.setText("#"+model.getBid_unique_id());
        holder.tvStatus.setText(model.getBidStatus());
        holder.tvDateTime.setText(model.getDateTime());
    }


    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemId, tvStatus, tvDateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemId = itemView.findViewById(R.id.tvItemId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
        }
    }


}