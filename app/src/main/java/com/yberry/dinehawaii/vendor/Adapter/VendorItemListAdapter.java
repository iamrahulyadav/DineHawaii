package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.VendorOrderDBHandler;
import com.yberry.dinehawaii.vendor.Activity.BidCartActivity;
import com.yberry.dinehawaii.vendor.Activity.VendorCartActivity;
import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;
import com.yberry.dinehawaii.vendor.Model.VendorlistDataModel;

import java.util.ArrayList;


public class VendorItemListAdapter extends RecyclerView.Adapter<VendorItemListAdapter.ViewHolder> {
    private static final String TAG = "OrderItemListAdapter";
    Context context;
    ArrayList<VendorlistDataModel> vendorModelArrayList;
    String vendor_id;

    public VendorItemListAdapter(Context context, ArrayList<VendorlistDataModel> reservList, String vendor_id) {
        this.context = context;
        this.vendorModelArrayList = reservList;
        this.vendor_id = vendor_id;
    }

    public static boolean isViewClicked(View view, MotionEvent e) {
        Rect rect = new Rect();

        view.getGlobalVisibleRect(rect);

        return rect.contains((int) e.getX(), (int) e.getY());
    }

    @Override
    public VendorItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_cat_itemlist_layout, parent, false);
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
        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.textViewOptions);
                popup.inflate(R.menu.vendor_menu_options);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_order:
                                detailsModel.setItemId(model.getItemId());
                                detailsModel.setItemName(model.getItemName());
                                detailsModel.setItemPrice(model.getPrice().trim());
                                detailsModel.setItemTotalCost(model.getPrice().trim());
                                detailsModel.setItemQuan("1");
                                detailsModel.setVendorId(vendor_id);
                                detailsModel.setProductId(model.getProductId());
                                new VendorOrderDBHandler(context).insertVendorOrderCartItem(detailsModel);
                                Toast.makeText(context, model.getItemName() + " Added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, VendorCartActivity.class);
                                intent.putExtra("vendor_id",vendor_id);
                                context.startActivity(intent);
                                return true;
                            case R.id.menu_bid:
                                context.startActivity(new Intent(context, BidCartActivity.class));
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView itemName, itemPrice, textViewOptions;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (CustomTextView) itemView.findViewById(R.id.tvName);
            itemPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
            textViewOptions = (CustomTextView) itemView.findViewById(R.id.textViewOptions);
        }
    }
}