package com.yberry.dinehawaii.Customer.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 11-Apr-17.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> {

    private static final String TAG = "CartItemAdapter";
    ArrayList<OrderItemsDetailsModel> orderItemsDetailsModels;
    DatabaseHandler mydb;
    CustomTextView total_amount;
    //MenuCart menuCart = new MenuCart();
    private Context mContext;
    private String temp;

    public CartItemAdapter(Context mContext, ArrayList<OrderItemsDetailsModel> orderItemsDetailsModels, CustomTextView total_amount/*, String str*/) {
        this.mContext = mContext;
        this.orderItemsDetailsModels = orderItemsDetailsModels;
        this.total_amount = total_amount;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder myViewHolder;
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_item_layout, viewGroup, false);
        myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {

        ///      if (temp.equalsIgnoreCase("item")) {}else if(temp.equalsIgnoreCase("manageorder")){}
//        OrderItemsDetailsModel itemsDetailsModel = orderItemsDetailsModels.get(pos);
        final OrderItemsDetailsModel itemsDetailsModel = orderItemsDetailsModels.get(pos);
        // myViewHolder.custom1.setText(AppPreferences.getCustomizationDelivery(mContext));
        myViewHolder.custom1.setText(itemsDetailsModel.getItemCustomiationList().replace("[", "").replace("]", ""));

        myViewHolder.item_Name.setText(itemsDetailsModel.getItemName());
        myViewHolder.item_Price.setText(itemsDetailsModel.getItemTotalCost());
        myViewHolder.item_Quantity.setText(itemsDetailsModel.getItemQuantity());

        if (itemsDetailsModel.getQuantityType() == null) {
            myViewHolder.item_basePrice.setText(itemsDetailsModel.getItemPrice());
        } else if (itemsDetailsModel.getQuantityType().equalsIgnoreCase("half")) {
            myViewHolder.quantLayout.setVisibility(View.GONE);
            myViewHolder.item_basePrice.setText(itemsDetailsModel.getItemPrice());
        } else if (itemsDetailsModel.getQuantityType().equalsIgnoreCase("full")) {
            myViewHolder.item_basePrice.setText(itemsDetailsModel.getItemPrice());
        } else {
            myViewHolder.item_basePrice.setText(itemsDetailsModel.getItemPrice());
        }
        Log.v(TAG, "getquan :- " + itemsDetailsModel.getQuantityType());

        myViewHolder.add_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quantityS = myViewHolder.item_Quantity.getText().toString();
                Log.v(TAG, "Quantity ;;;- " + quantityS);

                int quantity = 0;
                try {
                    quantity = Integer.parseInt(quantityS);
                    Log.v(TAG, "Quantity before Adding :- " + quantity);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                quantity = quantity + 1;
                myViewHolder.item_Quantity.setText("" + quantity);

                Log.v(TAG, "Quantity after Adding :- " + quantity);
                itemsDetailsModel.setItemQuantity(quantity + "");

                //double itemPrice = Double.parseDouble(myViewHolder.item_Price.getText().toString());
                double itemPrice = Double.parseDouble(itemsDetailsModel.getItemPrice());
                Log.v(TAG, "Item Price :- " + itemPrice);

                double totalCost = itemPrice * quantity;
                Log.v(TAG, "Total Cost :- " + totalCost);
                myViewHolder.item_Price.setText(" " + totalCost);
                orderItemsDetailsModels.get(pos).setItemTotalCost(totalCost + "");
                new DatabaseHandler(mContext).insertCartlist(itemsDetailsModel);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("updateTotalprice"));
            }
        });
        myViewHolder.remove_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quantity = Integer.parseInt(myViewHolder.item_Quantity.getText().toString());
                Log.v(TAG, "Quantity before minus :- " + quantity);
                if (quantity == 1) {
                    Toast.makeText(mContext, "Quantity can't be less than 1", Toast.LENGTH_SHORT).show();
                } else {
                    quantity = quantity - 1;
                    myViewHolder.item_Quantity.setText("" + quantity);
                }
                Log.v(TAG, "Quantity after minus :- " + quantity);
                itemsDetailsModel.setItemQuantity(quantity + "");

                // double itemPrice = Double.parseDouble(myViewHolder.item_Price.getText().toString());
                double itemPrice = Double.parseDouble(itemsDetailsModel.getItemPrice());
                Log.v(TAG, "Item Price :- " + itemPrice);
                double totalCost = itemPrice * quantity;
                Log.v(TAG, "Total Cost :- " + totalCost);
                myViewHolder.item_Price.setText(" " + totalCost);
                orderItemsDetailsModels.get(pos).setItemTotalCost(totalCost + "");
                new DatabaseHandler(mContext).insertCartlist(itemsDetailsModel);

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("updateTotalprice"));
            }
        });
        myViewHolder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogToDeletePosition(itemsDetailsModel, pos);
            }
        });

        Log.v(TAG, " Total cost is : -" + itemsDetailsModel.getItemCustomiationList());

        double total_cost = 0;
        for (OrderItemsDetailsModel models : orderItemsDetailsModels) {
            Log.v(TAG, "All items in cart are:- " + models.getItemName() + "\n Price :- " + models.getItemTotalCost());
            double cost = Double.parseDouble(models.getItemTotalCost());
            total_cost = total_cost + cost;
            Log.v(TAG, "Total Cost :- " + total_cost);

        }
    }

    private void openDialogToDeletePosition(final OrderItemsDetailsModel itemsDetailsModel, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setMessage("Do you want to remove this item from list");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mydb = new DatabaseHandler(mContext);
                mydb.deleteCartitem(itemsDetailsModel.getMenu_id());
                orderItemsDetailsModels.remove(pos);
                notifyDataSetChanged();
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("updateTotalprice"));

                Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();

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

        CustomTextView item_Name, item_Price, item_Quantity, custom1, item_basePrice;
        ImageView add_quantity, remove_quantity;
        ImageView item_delete;
        LinearLayout quantLayout;

        public MyViewHolder(View convertView) {

            super(convertView);
            item_Name = (CustomTextView) convertView.findViewById(R.id.item_Name);
            item_Price = (CustomTextView) convertView.findViewById(R.id.item_Price);
            item_Quantity = (CustomTextView) convertView.findViewById(R.id.item_Quantity);
            item_basePrice = (CustomTextView) convertView.findViewById(R.id.item_baseprice);
            add_quantity = (ImageView) convertView.findViewById(R.id.add_Quantity);
            remove_quantity = (ImageView) convertView.findViewById(R.id.remove_Quantity);
            custom1 = (CustomTextView) convertView.findViewById(R.id.custom1);
            item_delete = (ImageView) convertView.findViewById(R.id.itemdelete);
            quantLayout = (LinearLayout) convertView.findViewById(R.id.incQuanLayout);

        }
    }
}
