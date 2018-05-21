package com.yberry.dinehawaii.vendor.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.BidDetailsModel;

import java.util.ArrayList;

/**
 * Created by Peter on 11-Apr-17.
 */

public class VendorBidDetailsAdapter extends RecyclerView.Adapter<VendorBidDetailsAdapter.MyViewHolder> {

    private static final String TAG = "VendorBidDetails";
    ArrayList<BidDetailsModel> list;
    private Context mContext;
    private Dialog popup;

    public VendorBidDetailsAdapter(Context mContext, ArrayList<BidDetailsModel> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vendor_bid_item_detail_layout, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
        final BidDetailsModel model = list.get(pos);
        myViewHolder.tvItemName.setText(model.getItemName());
        myViewHolder.tvVendorName.setText("(" + model.getVendorName() + ")");
        myViewHolder.tvItemQty.setText(model.getItemQuantity());
        myViewHolder.tvItemTotalPrice.setText(model.getVendorBidAmount());
        myViewHolder.tvyourPrice.setText("$" + model.getBusinessBidAmt());
        if (model.getVendorBidFinalAmount().equalsIgnoreCase("0")) {
            myViewHolder.tvFinalPrice.setText("$" + model.getVendorBidFinalAmount());
            myViewHolder.tvFinalPrice.setClickable(false);
        } else {
            myViewHolder.tvFinalPrice.setText("$" + model.getVendorBidFinalAmount());
            myViewHolder.tvFinalPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit_black_18dp, 0);
            myViewHolder.tvFinalPrice.setCompoundDrawablePadding(5);
            myViewHolder.tvFinalPrice.setClickable(true);
        }

        myViewHolder.tvFinalPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateBidDialog(model.getItemName(), model.getBusinessBidAmt());
            }
        });
    }

    private void showUpdateBidDialog(String itemName, String businessBidAmt) {
        popup = new Dialog(mContext);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.food_category_dialog);
        final CustomEditText foodtype = (CustomEditText) popup.findViewById(R.id.new_category);
        final CustomTextView foodTitle = (CustomTextView) popup.findViewById(R.id.foodTitle);
        final CustomTextView addcategory = (CustomTextView) popup.findViewById(R.id.addcategory);
        foodTitle.setText("Update Bid");
        addcategory.setText(itemName + "(Previous Bid Quote : $" + businessBidAmt + ")");
        foodtype.setHint("Enter bid amount");
        popup.findViewById(R.id.popupclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.findViewById(R.id.cat_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvItemName, tvVendorName, tvItemTotalPrice, tvItemQty, tvyourPrice, tvFinalPrice;

        public MyViewHolder(View convertView) {

            super(convertView);
            tvItemName = (CustomTextView) convertView.findViewById(R.id.tvItemName);
            tvVendorName = (CustomTextView) convertView.findViewById(R.id.tvVendorName);
            tvItemQty = (CustomTextView) convertView.findViewById(R.id.tvItemQty);
            tvyourPrice = (CustomTextView) convertView.findViewById(R.id.tvyourPrice);
            tvItemTotalPrice = (CustomTextView) convertView.findViewById(R.id.tvItemTotalPrice);
            tvFinalPrice = (CustomTextView) convertView.findViewById(R.id.tvFinalPrice);

        }
    }
}
