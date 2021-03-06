package com.yberry.dinehawaii.Customer.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yberry.dinehawaii.Model.LIstItemAlternate;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;


public class AlternateRestListAdapter extends RecyclerView.Adapter<AlternateRestListAdapter.ViewHolder> {
    private static final String TAG = "CouponAdapter";
    Context context;
    ArrayList<LIstItemAlternate> reservList;
    private Activity activity;

    public AlternateRestListAdapter(Activity activity, Context context, ArrayList<LIstItemAlternate> reservList) {
        this.activity = activity;
        this.context = context;
        this.reservList = reservList;
    }

    @Override
    public AlternateRestListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.altername_rest_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final AlternateRestListAdapter.ViewHolder holder, final int position) {
        final LIstItemAlternate model = reservList.get(position);

        Log.d(TAG, "onBindViewHolder: model >> " + model);
        holder.tvBussName.setText(model.getBusinessName());
       /* if (holder.tvBussAddr != null)
            if (model.getBusinessAddress().equalsIgnoreCase(""))
                holder.tvBussAddr.setVisibility(View.GONE);
            else*/
        holder.tvBussAddr.setText("Address : " + model.getBusinessAddress());
        holder.tvDistance.setText("Distance : " + model.getDistance() + " miles away");
        holder.tvTable.setText("Available Table : " + model.getTableName());

        /*if (model.getDisplayLogo() != null)
            if (model.getDisplayLogo() == true)
                holder.restLogo.setVisibility(View.VISIBLE);
            else holder.restLogo.setVisibility(View.GONE);*/

        //holder.tvDistance.setText("(" + model.getDistance() + " miles away)");


        if (model.getLogoImg() != null)
            if (!model.getLogoImg().equalsIgnoreCase("")) {
                Glide.with(context).load(model.getLogoImg())
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.logo)
//                    .override(50, 50)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.restLogo);
            }
    }

    @Override
    public int getItemCount() {
        return reservList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvBussName, tvBussAddr, tvDistance, tvTable;
        ImageView restLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBussName = (CustomTextView) itemView.findViewById(R.id.tvBussName);
            tvBussAddr = (CustomTextView) itemView.findViewById(R.id.tvBussAddr);
            tvDistance = (CustomTextView) itemView.findViewById(R.id.tvDistance);
            tvTable = (CustomTextView) itemView.findViewById(R.id.tvTable);
            restLogo = (ImageView) itemView.findViewById(R.id.restLogo);
        }
    }
}