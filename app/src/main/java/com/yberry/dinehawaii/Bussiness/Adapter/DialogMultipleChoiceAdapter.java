package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.yberry.dinehawaii.Model.TableData;
import com.yberry.dinehawaii.R;

import java.util.ArrayList;
import java.util.List;


public class DialogMultipleChoiceAdapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    List<TableData> mItemList;

    public DialogMultipleChoiceAdapter(Context context, List<TableData> itemList) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemList = itemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public TableData getItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public List<String> getSelectedItemIds() {
        List<String> checkedItemList = new ArrayList<>();
        for (TableData item : mItemList) {
            if (item.isSeleted()) {
                checkedItemList.add(item.getTable_id());
            }
        }
        return checkedItemList;
    }

    public List<String> getSelectedItemNames() {
        int counter = 0;
        List<String> checkedItemList = new ArrayList<>();
        for (TableData item : mItemList) {
            if (item.isSeleted()) {
                checkedItemList.add(item.getTable_name());
            }
        }
        return checkedItemList;
    }

    public int getTotalCapacity() {
        int total = 0;
        for (TableData item : mItemList) {
            if (item.isSeleted()) {
                total=total+Integer.parseInt(item.getTable_capacity());
            }
        }
        return total;
    }

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
        if (mItemList.get(position).isSeleted())
            holder.checkbox.setChecked(true);
        else
            holder.checkbox.setChecked(false);
        holder.checkbox.setText(mItemList.get(position).getTable_name()+"( Cap. "+mItemList.get(position).getTable_capacity()+")");
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemList.get(position).isSeleted()) {
                        mItemList.get(position).setSeleted(false);
                    holder.checkbox.setChecked(false);
                } else {
                    mItemList.get(position).setSeleted(true);
                    holder.checkbox.setChecked(true);
                }
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