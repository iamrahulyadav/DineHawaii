package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.database.VendorBidDBHandler;
import com.yberry.dinehawaii.vendor.Model.VendorBidItemModel;

import java.util.ArrayList;
import java.util.List;


public class VendorMultipleChoiceAdapter extends BaseAdapter {
    private static String TAG = "VendorMultipleChoiceAdapter";

    LayoutInflater mLayoutInflater;
    List<VendorBidItemModel> mItemList;
    private Context context;

    public VendorMultipleChoiceAdapter(Context context, List<VendorBidItemModel> itemList) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItemList = itemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public VendorBidItemModel getItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public List<String> getSelectedItemIds() {
        List<String> checkedItemList = new ArrayList<>();
        for (VendorBidItemModel item : mItemList) {
            if (item.isSelected()) {
                checkedItemList.add(item.getItem_id());
            }
        }
        return checkedItemList;
    }

    public List<String> getSelectedItemNames() {
        int counter = 0;
        List<String> checkedItemList = new ArrayList<>();
        for (VendorBidItemModel item : mItemList) {
            if (item.isSelected()) {
                checkedItemList.add(item.getItem_name());
            }
        }
        return checkedItemList;
    }

   /* public int getTotalCapacity() {
        int total = 0;
        for (VendorBidItemModel item : mItemList) {
            if (item.isSelected()) {
                total=total+Integer.parseInt(item.get());
            }
        }
        return total;
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mItemList.get(position).isSelected())
            holder.checkbox.setChecked(true);
        else
            holder.checkbox.setChecked(false);
        holder.checkbox.setText(mItemList.get(position).getVendor_name() + " - $" + mItemList.get(position).getVendor_item_price());
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemList.get(position).isSelected()) {
                    mItemList.get(position).setSelected(false);
                    holder.checkbox.setChecked(false);
                    new VendorBidDBHandler(context).deleteCartOrderTtem(mItemList.get(position).getItem_id(), mItemList.get(position).getVendor_id());
                    //delete item from cart
                } else {
                    mItemList.get(position).setSelected(true);
                    holder.checkbox.setChecked(true);
                    new VendorBidDBHandler(context).insertVendorOrderCartItem(mItemList.get(position));
                    //add item to cart
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("update_cart"));
            }
        });

        return convertView;
    }

    private void updateItemState(ViewHolder holder, boolean checked) {
        holder.checkbox.setChecked(checked);
    }

    private static class ViewHolder {
        View root;
        CheckBox checkbox;

        ViewHolder(View view) {
            root = view;
            checkbox = view.findViewById(R.id.checkbox);
        }
    }
}