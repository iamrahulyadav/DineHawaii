package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.Model.MenuDetail;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.List;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    Context context;
    private List<String> menulist;

    public MenuListAdapter(Context context, List<String> menulist) {
        this.context = context;
        this.menulist = menulist;
    }

    @Override
    public MenuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menulist_activity, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!menulist.get(position).equalsIgnoreCase(""))
            holder.menulit.setText(menulist.get(position));
    }

    @Override
    public int getItemCount() {
        return menulist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView menulit;

        public ViewHolder(View itemView) {
            super(itemView);
            menulit = (CustomTextView) itemView.findViewById(R.id.menulist);
        }
    }
}