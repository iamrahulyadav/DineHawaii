package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Bussiness.model.CouponModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;


public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {
    private static final String TAG = "CouponAdapter";
    Context context;
    ArrayList<CouponModel> reservList;

    public CouponAdapter(Context context, ArrayList<CouponModel> reservList) {
        this.context = context;
        this.reservList = reservList;
    }

    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_coupon_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final CouponAdapter.ViewHolder holder, final int position) {
        final CouponModel model = reservList.get(position);

        holder.tvTitle.setText(model.getCouponTitle());
        holder.tvCode.setText(model.getCouponCode());
        holder.tvValidFrom.setText(model.getCouponSDate());
        holder.tvValidTo.setText(model.getCouponEDate());
        holder.tvCouponType.setText(model.getCouponType());
        if (model.getCouponType().equalsIgnoreCase("Discount Amount"))
            holder.tvValueText.setText("$ : ");
        else if (model.getCouponType().equalsIgnoreCase("Discount Percentage"))
            holder.tvValueText.setText("% : ");
        holder.tvValue.setText(model.getCouponAmtPer());

    }

    @Override
    public int getItemCount() {
        return reservList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvTitle, tvCode, tvValidFrom, tvValidTo, tvValue, tvValueText, tvCouponType;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (CustomTextView) itemView.findViewById(R.id.tvTitle);
            tvCode = (CustomTextView) itemView.findViewById(R.id.tvCode);
            tvValidFrom = (CustomTextView) itemView.findViewById(R.id.tvValidFrom);
            tvValidTo = (CustomTextView) itemView.findViewById(R.id.tvValidTo);
            tvValue = (CustomTextView) itemView.findViewById(R.id.tvValue);
            tvValueText = (CustomTextView) itemView.findViewById(R.id.tvValueText);
            tvCouponType = (CustomTextView) itemView.findViewById(R.id.tvCouponType);
        }
    }
}