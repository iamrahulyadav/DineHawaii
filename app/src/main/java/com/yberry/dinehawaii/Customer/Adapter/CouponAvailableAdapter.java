package com.yberry.dinehawaii.Customer.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.List;


public class CouponAvailableAdapter extends RecyclerView.Adapter<CouponAvailableAdapter.ViewHolder> {

    Context context;
    private List<CustomerModel> availablecouponlist;

    public CouponAvailableAdapter(Context context, List<CustomerModel> availablecouponlist) {
        this.context = context;
        this.availablecouponlist = availablecouponlist;
    }

    public CouponAvailableAdapter(Context context) {
    }

    @Override
    public CouponAvailableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.avail_coupon, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CouponAvailableAdapter.ViewHolder holder, int position) {
        final CustomerModel model = availablecouponlist.get(position);
        if (model.getCoupon_code().equalsIgnoreCase("")) {
            holder.coupon_code.setText("NA");
        } else {
            holder.coupon_code.setText(model.getCoupon_code());
        }
        if (model.getAmount().equalsIgnoreCase("")) {
            holder.amount.setText("NA");
        } else {
            holder.amount.setText(model.getAmount());
        }
        if (model.getEnd_date().equalsIgnoreCase("")) {
            holder.d_end.setText("NA");
        } else {
            holder.d_end.setText(model.getEnd_date());
        }
        if (model.getCoupon_status().equalsIgnoreCase("")) {
            holder.status.setText("NA");
        } else {
            holder.status.setText(model.getCoupon_status());
        }
    }


    @Override
    public int getItemCount() {
        return availablecouponlist.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView coupon_code, amount,  d_end, status;

        public ViewHolder(View itemView) {
            super(itemView);
            coupon_code = (CustomTextView) itemView.findViewById(R.id.cpnCode);
            amount = (CustomTextView) itemView.findViewById(R.id.cpnAmount);
            d_end = (CustomTextView) itemView.findViewById(R.id.cpnEnd);
            status = (CustomTextView) itemView.findViewById(R.id.cpnStatus);
        }


    }
}