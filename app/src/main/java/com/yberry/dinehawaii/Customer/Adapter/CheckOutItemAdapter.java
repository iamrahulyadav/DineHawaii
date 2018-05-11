package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 11-Apr-17.
 */

public class CheckOutItemAdapter extends RecyclerView.Adapter<CheckOutItemAdapter.MyViewHolder> {

    private static final String TAG = "CartItemAdapter";
    ArrayList<OrderItemsDetailsModel> orderItemsDetailsModels;
    DatabaseHandler mydb;
    CustomTextView total_amount;
    //MenuCart menuCart = new MenuCart();
    private Context mContext;
    private String temp;

    public CheckOutItemAdapter(Context mContext, ArrayList<OrderItemsDetailsModel> orderItemsDetailsModels) {
        this.mContext = mContext;
        this.orderItemsDetailsModels = orderItemsDetailsModels;
        this.total_amount = total_amount;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        MyViewHolder myViewHolder;
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.checkout_cart_item_layout, viewGroup, false);

        myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
        final OrderItemsDetailsModel itemsDetailsModel = orderItemsDetailsModels.get(pos);
        myViewHolder.tvItem.setText(itemsDetailsModel.getItemName());
        myViewHolder.tvUnitPrice.setText("$" + itemsDetailsModel.getItemPrice());
        myViewHolder.tvQty.setText(itemsDetailsModel.getItemQuantity());
        try {
            int quantity = Integer.parseInt(itemsDetailsModel.getItemQuantity());
            int itemPrice = Integer.parseInt(itemsDetailsModel.getItemPrice());
            double totalCost = itemPrice * quantity;
            myViewHolder.tvTotal.setText("$" + String.valueOf(totalCost));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return orderItemsDetailsModels.size();
    }

    public List<OrderItemsDetailsModel> getListValue() {
        return orderItemsDetailsModels;
    }

    public ArrayList<OrderItemsDetailsModel> getCartItems() {
        return orderItemsDetailsModels;
    }


    public double getTotalCost() {
        double total_cost = 0;
        for (OrderItemsDetailsModel models : orderItemsDetailsModels) {
            Log.v(TAG, "All items in cart are:- " + models.getItemName() + "\n Price :- " + models.getItemTotalCost());
            double cost = Double.parseDouble(models.getItemTotalCost());
            total_cost = total_cost + cost;
            Log.v(TAG, "Total Cost1 :- " + cost + ">>" + total_cost);
        }
        return total_cost;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CustomTextView tvItem, tvQty, tvUnitPrice, tvTotal;

        public MyViewHolder(View convertView) {

            super(convertView);
            tvItem = (CustomTextView) convertView.findViewById(R.id.tvItem);
            tvQty = (CustomTextView) convertView.findViewById(R.id.tvQty);
            tvUnitPrice = (CustomTextView) convertView.findViewById(R.id.tvUnitPrice);
            tvTotal = (CustomTextView) convertView.findViewById(R.id.tvTotal);
        }
    }
}
