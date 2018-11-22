package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.VendorOrderDBHandler;
import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;
import com.yberry.dinehawaii.vendor.Model.VendorlistDataModel;

import java.util.ArrayList;


public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.ViewHolder> {
    private static final String TAG = "OrderItemListAdapter";
    private MyClickListener listener;
    Context context;
    ArrayList<VendorlistDataModel> vendorModelArrayList;
    private String vendor_id = "";

    public OrderItemListAdapter(Context context, ArrayList<VendorlistDataModel> reservList, String vendor_id, MyClickListener listener) {
        this.context = context;
        this.vendorModelArrayList = reservList;
        this.vendor_id = vendor_id;
        this.listener = listener;
    }

    @Override
    public OrderItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VendorlistDataModel model = vendorModelArrayList.get(position);
        holder.itemName.setText(model.getItemName());
        holder.itemPrice.setText("$" + model.getPrice());
        final VendorOrderItemsDetailsModel detailsModel = new VendorOrderItemsDetailsModel();
        holder.btnplaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsModel.setItemId(model.getItemId());
                detailsModel.setItemName(model.getItemName());
                detailsModel.setItemPrice(model.getPrice().trim());
                detailsModel.setItemTotalCost(model.getPrice().trim());
                detailsModel.setItemQuan(holder.item_Quantity.getText().toString());
                detailsModel.setVendorId(vendor_id);
                detailsModel.setProductId(model.getProductId());
                new VendorOrderDBHandler(context).insertVendorOrderCartItem(detailsModel);
                Toast.makeText(context, model.getItemName() + " Added", Toast.LENGTH_SHORT).show();
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("update_counter"));
            }
        });

        holder.item_Quantity.setText(model.getMinimum_vol());

        holder.add_Quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(holder.item_Quantity.getText().toString());
                if (qty == Integer.parseInt(model.getMaximum_vol())) {
                    Toast.makeText(context, "You can't add more than " + model.getMaximum_vol() + " quantity.", Toast.LENGTH_SHORT).show();
                } else if (qty >= Integer.parseInt(model.getMinimum_vol()) && qty < Integer.parseInt(model.getMaximum_vol())) {
                    qty++;
                    holder.item_Quantity.setText(String.valueOf(qty));
                }
            }
        });

        holder.remove_Quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(holder.item_Quantity.getText().toString());
                if (qty == Integer.parseInt(model.getMinimum_vol())) {
                    Toast.makeText(context, "Minimum " + model.getMinimum_vol() + " quantity is needed.", Toast.LENGTH_SHORT).show();
                } else if (qty >= Integer.parseInt(model.getMinimum_vol()) && qty <= Integer.parseInt(model.getMaximum_vol())) {
                    qty--;
                    holder.item_Quantity.setText(String.valueOf(qty));
                }
//                listener.removeQuantity(view, position, Integer.parseInt(holder.item_Quantity.getText().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView itemName, itemPrice;
        CustomButton btnplaceorder;
        LinearLayout llprice;
        CustomTextView item_Quantity;
        ImageView add_Quantity, remove_Quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (CustomTextView) itemView.findViewById(R.id.tvName);
            itemPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
            btnplaceorder = (CustomButton) itemView.findViewById(R.id.btnplaceorder);
            item_Quantity = itemView.findViewById(R.id.item_Quantity);
            add_Quantity = itemView.findViewById(R.id.add_Quantity);
            remove_Quantity = itemView.findViewById(R.id.remove_Quantity);
        }
    }

    public interface MyClickListener {
        public void addQuantity(View view, int position, int i);

        public void removeQuantity(View view, int position, int i);
    }
}