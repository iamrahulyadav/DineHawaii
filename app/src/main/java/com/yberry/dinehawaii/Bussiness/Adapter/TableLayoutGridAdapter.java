package com.yberry.dinehawaii.Bussiness.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Bussiness.model.TableLayoutData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;


public class TableLayoutGridAdapter extends RecyclerView.Adapter<TableLayoutGridAdapter.ViewHolder> {
    private static final String TAG = "TableLayoutGridAdapter";
    Context context;
    ArrayList<TableLayoutData> reservList;
    private Activity activity;

    public TableLayoutGridAdapter(Activity activity, Context context, ArrayList<TableLayoutData> reservList) {
        this.activity = activity;
        this.context = context;
        this.reservList = reservList;
    }

    @Override
    public TableLayoutGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_layout_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final TableLayoutGridAdapter.ViewHolder holder, final int position) {
        final TableLayoutData model = reservList.get(position);
        holder.tvTitle.setText(model.getTableName() + "(Cp. " + model.getTableSize() + ")");
        if (model.getStatus().equalsIgnoreCase("Booked")) {
            holder.item_view.setCardBackgroundColor(context.getResources().getColor(R.color.red));
            holder.tvTime.setText(model.getBookingTime().toLowerCase());
        } else if (model.getStatus().equalsIgnoreCase("Free")) {
            holder.item_view.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvTime.setText(model.getSeatingTime().toLowerCase());
        }
    }


    @Override
    public int getItemCount() {
        return reservList.size();
    }


    public void removeAt(int position) {
        reservList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, reservList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvTitle, tvTime;
        CardView item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (CustomTextView) itemView.findViewById(R.id.tvTitle);
            tvTime = (CustomTextView) itemView.findViewById(R.id.tvTime);
            item_view = itemView.findViewById(R.id.item_view);

        }
    }
}