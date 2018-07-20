package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yberry.dinehawaii.Bussiness.model.OrderDetailItemData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Hvantage2 on 2018-02-13.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final String TAG = "ItemAdapter";
    private String order_type;
    private Context context;
    private ArrayList<OrderDetailItemData> orderDetails;

    public ItemAdapter(Context context, ArrayList<OrderDetailItemData> orderDetails, String order_type) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.order_type = order_type;

    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        double total = 0;
        OrderDetailItemData model = orderDetails.get(position);
        Log.e("ItemAdapter", "onBindViewHolder: model >> " + model);
        Log.e("ItemAdapter", "onBindViewHolder: order_type >> " + order_type);
        holder.tvItemName.setText(model.getFoodName() + " (Qty : " + model.getQuantity() + ")" + "\n" + "from " + model.getFood_area_name());
        if (model.getQuantity().equalsIgnoreCase("1/2"))
            total = Double.parseDouble(model.getItemPrice());
        else
            total = Double.parseDouble(model.getItemPrice()) * Integer.parseInt(model.getQuantity());

        if (model.getItemCustomization().equalsIgnoreCase(""))
            holder.tvCustomization.setText("");
        else holder.tvCustomization.setText(model.getItemCustomization());
        holder.tvPrice.setText("$" + String.valueOf(total));
        holder.imageStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "showDialog: order_type >> " + order_type);
                String order_type_status = "Delivered";
                if (order_type.equalsIgnoreCase("pickup"))
                    order_type_status = "Picked-up";
                Log.e(TAG, "showDialog: order_type_status >> " + order_type_status);

                final CharSequence[] items = {"Pending", "In-Progress", "Prepared", order_type_status, "Completed"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Pending")) {
                        } else if (items[item].equals("In-Progress")) {
                        } else if (items[item].equals("Prepared")) {
                        } else if (items[item].equals("Delivered")) {
                        } else if (items[item].equals("Picked-up")) {
                        } else if (items[item].equals("Completed")) {
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void showDialog() {

    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvItemName, tvCustomization, tvPrice;
        ImageView imageStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = (CustomTextView) itemView.findViewById(R.id.tvItemName);
            tvCustomization = (CustomTextView) itemView.findViewById(R.id.tvCustomization);
            tvPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
            imageStatus = (ImageView) itemView.findViewById(R.id.imageStatus);
        }
    }
}


