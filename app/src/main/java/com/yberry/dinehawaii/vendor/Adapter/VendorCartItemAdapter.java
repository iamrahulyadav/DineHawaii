package com.yberry.dinehawaii.vendor.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.VendorDBHandler;
import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 11-Apr-17.
 */

public class VendorCartItemAdapter extends RecyclerView.Adapter<VendorCartItemAdapter.MyViewHolder> {

    private static final String TAG = "CartItemAdapter";
    ArrayList<VendorOrderItemsDetailsModel> orderItemsDetailsModels;
    VendorDBHandler mydb;
    CustomTextView total_amount;
    //MenuCart menuCart = new MenuCart();
    private Context mContext;
    private String temp;

    public VendorCartItemAdapter(Context mContext, ArrayList<VendorOrderItemsDetailsModel> orderItemsDetailsModels, CustomTextView total_amount/*, String str*/) {
        this.mContext = mContext;
        this.orderItemsDetailsModels = orderItemsDetailsModels;
        this.total_amount = total_amount;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        MyViewHolder myViewHolder;
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vendor_cart_item_layout, viewGroup, false);

        myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
        final VendorOrderItemsDetailsModel itemsDetailsModel = orderItemsDetailsModels.get(pos);
        myViewHolder.tvItemName.setText(itemsDetailsModel.getItemName());
        myViewHolder.tvItemUnitPrice.setText(itemsDetailsModel.getItemPrice());
        myViewHolder.tvItemTotalPrice.setText(String.valueOf(Integer.parseInt(itemsDetailsModel.getItemQuan()) * Integer.parseInt(itemsDetailsModel.getItemPrice())));
        myViewHolder.etQuantity.setText(itemsDetailsModel.getItemQuan());
        myViewHolder.etQuantity.setFilters(new InputFilter[]{new InputFilters(1, 500)});

        myViewHolder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogToDeletePosition(itemsDetailsModel, pos);
            }
        });

        myViewHolder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!myViewHolder.etQuantity.getText().toString().equalsIgnoreCase("") && !myViewHolder.etQuantity.getText().toString().equalsIgnoreCase("0")) {
                    String qty = myViewHolder.etQuantity.getText().toString();
                    String price = itemsDetailsModel.getItemPrice();
                    int itemTotal = Integer.parseInt(qty) * Integer.parseInt(price);
                    Log.e(TAG, "onTextChanged: itemTotal >> " + itemTotal);
                    new VendorDBHandler(mContext).updateOrderItemQty(qty, itemsDetailsModel.getItemId(), String.valueOf(itemTotal));
                    myViewHolder.tvItemTotalPrice.setText(String.valueOf(itemTotal));
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("updateTotalprice"));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void openDialogToDeletePosition(final VendorOrderItemsDetailsModel itemsDetailsModel, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setMessage("Do you want to remove this item from cart");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mydb = new VendorDBHandler(mContext);
                mydb.deleteCartOrderTtem(itemsDetailsModel.getItemId());
                orderItemsDetailsModels.remove(pos);
                notifyDataSetChanged();
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("updateTotalprice"));
                Toast.makeText(mContext, "Item Removed", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return orderItemsDetailsModels.size();
    }

    public List<VendorOrderItemsDetailsModel> getListValue() {
        return orderItemsDetailsModels;
    }

    public ArrayList<VendorOrderItemsDetailsModel> getCartItems() {
        return orderItemsDetailsModels;
    }

    public double getTotalCost() {
        double total_cost = 0;
        for (VendorOrderItemsDetailsModel models : orderItemsDetailsModels) {
            double cost = Double.parseDouble(models.getItemTotalCost());
            total_cost = total_cost + cost;
        }
        Log.e(TAG, "getTotalCost: total_cost >> " + total_cost);
        return total_cost;
    }

    public class InputFilters implements InputFilter {

        private double min, max;

        public InputFilters(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilters(String min, String max) {
            this.min = Double.parseDouble(min);
            this.max = Double.parseDouble(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(double a, double b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvItemName, tvItemUnitPrice, tvItemTotalPrice;
        ImageView item_delete;
        LinearLayout quantLayout;
        CustomEditText etQuantity;

        public MyViewHolder(View convertView) {

            super(convertView);
            tvItemName = (CustomTextView) convertView.findViewById(R.id.tvItemName);
            etQuantity = (CustomEditText) convertView.findViewById(R.id.etQuantity);
            tvItemUnitPrice = (CustomTextView) convertView.findViewById(R.id.tvItemUnitPrice);
            tvItemTotalPrice = (CustomTextView) convertView.findViewById(R.id.tvItemTotalPrice);
            item_delete = (ImageView) convertView.findViewById(R.id.itemdelete);
            quantLayout = (LinearLayout) convertView.findViewById(R.id.incQuanLayout);

        }
    }
}
