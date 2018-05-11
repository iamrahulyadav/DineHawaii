package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yberry.dinehawaii.Bussiness.model.OrderDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hvantage2 on 7/5/2017.
 */

public class OrderPrepationAdapter extends RecyclerView.Adapter<OrderPrepationAdapter.ViewHolder> {

    private Context context;
    ArrayList<String> arl;
    private List<OrderDetails> prepationList;


    public OrderPrepationAdapter(Context context, List<OrderDetails> prepationList) {
        this.context = context;
        this.prepationList = prepationList;

    }

    @Override
    public OrderPrepationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderprepationdetail_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderPrepationAdapter.ViewHolder holder, int position) {
        final OrderDetails model = prepationList.get(position);
        holder.foodMenuCategory.setText(model.getFood_name());
        holder.foodCategory.setText(model.getFood_name());
        holder.foodDesc.setText(model.getDescription());
        holder.foodCustom.setText(model.getCustomization());
        holder.orderType.setText(model.getOrder_type());
        holder.orderStatus.setText(model.getOrder_status());
        holder.order_Qty.setText(model.getQuantity());

/*
        holder.recycler_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderPurchesingDetailVini.class);
                context.startActivity(intent);
            }
        });
*/

    }

    @Override
    public int getItemCount() {

        return prepationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView foodMenuCategory, foodCategory, foodDesc, foodCustom, orderType, orderStatus;
        TextView order_Qty;
        RelativeLayout recycler_item;


        public ViewHolder(View itemView) {
            super(itemView);
            foodMenuCategory = (CustomTextView) itemView.findViewById(R.id.foodMenucategory);
            foodCategory = (CustomTextView) itemView.findViewById(R.id.foodCategory);
            foodDesc = (CustomTextView) itemView.findViewById(R.id.description);
            foodCustom = (CustomTextView) itemView.findViewById(R.id.custom);
            orderType = (CustomTextView) itemView.findViewById(R.id.order_type);
            orderStatus = (CustomTextView) itemView.findViewById(R.id.status);
            order_Qty = (TextView) itemView.findViewById(R.id.qty);
            //  recycler_item = (RelativeLayout) itemView.findViewById(R.id.recycler_item);


        }


    }
}