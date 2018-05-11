package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.OrderHistoryItems;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hvantage2 on 6/29/2017.
 */

public class OrderFavAdapter extends RecyclerView.Adapter<OrderFavAdapter.ViewHolder> {
    List<String> list = new ArrayList<>();
    Context context;

    ArrayList<OrderHistoryItems> repeatOrderList = new ArrayList<>();


    public OrderFavAdapter(Context context, ArrayList<OrderHistoryItems> repeatOrderList) {
        this.context = context;
        this.repeatOrderList = repeatOrderList;
    }

    @Override
    public OrderFavAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customization, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderFavAdapter.ViewHolder holder, int position) {

        OrderHistoryItems item = repeatOrderList.get(position);
        if (item.getItem().equalsIgnoreCase("")) {
            holder.foodname.setText("NA");
        } else {
            holder.foodname.setText(item.getItem());
        }
        if (item.getQty().equalsIgnoreCase("")) {
            holder.order_quantity.setText("(Qty : 0)");
        } else {
            holder.order_quantity.setText("(Qty : " + item.getQty() + ")");
        }
        if (item.getPrice().equalsIgnoreCase("")) {
            holder.item_price.setText("00");

        } else {
            holder.item_price.setText(item.getPrice());

        }


    }


    @Override
    public int getItemCount() {
        return repeatOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView foodname, order_customization, order_quantity, item_price;

        public ViewHolder(View itemView) {
            super(itemView);

            foodname = (CustomTextView) itemView.findViewById(R.id.foodname);
            order_customization = (CustomTextView) itemView.findViewById(R.id.customization);
            order_quantity = (CustomTextView) itemView.findViewById(R.id.quantity);
            item_price = (CustomTextView) itemView.findViewById(R.id.itemPrice);
        }


    }
}
