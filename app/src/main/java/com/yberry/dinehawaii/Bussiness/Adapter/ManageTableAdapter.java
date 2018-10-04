package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yberry.dinehawaii.Bussiness.Activity.AddNewTableActivity;
import com.yberry.dinehawaii.Model.TableData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;


public class ManageTableAdapter extends RecyclerView.Adapter<ManageTableAdapter.ViewHolder> {
    private final String TAG = "ManageTableAdapter";
    private final Context context;
    private final ArrayList<TableData> tabledetails;
    private ManageTableAdapterListener listener;
    private String type = "";

    public ManageTableAdapter(Context context, ArrayList<TableData> details, String type, ManageTableAdapterListener listener) {
        this.context = context;
        this.tabledetails = details;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_table_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TableData tableData = tabledetails.get(position);
        if (type.equalsIgnoreCase("single")) {
            holder.tvBlock.setVisibility(View.VISIBLE);
            holder.tvBlock.setText("BLOCK");
            holder.tvBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.blockTable(view, position, true);
                }
            });
        } else if (type.equalsIgnoreCase("Combined")) {
            holder.tvBlock.setVisibility(View.VISIBLE);
            holder.tvBlock.setText("BLOCK");
            holder.tvBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.blockTable(view, position, true);
                }
            });
        } else if (type.equalsIgnoreCase("blocked")) {
            holder.tvBlock.setVisibility(View.VISIBLE);
            holder.tvBlock.setText("UNBLOCK");
            holder.tvBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.blockTable(view, position, false);
                }
            });
        }
        if (tableData.getTable_name().equalsIgnoreCase("")) {
            holder.tableno.setText("NA");
        } else {
            holder.tableno.setText(tableData.getTable_name());
        }
        if (tableData.getService_name().equalsIgnoreCase("")) {
            holder.table_service.setText("NA");
        } else {
            holder.table_service.setText(tableData.getService_name());
        }
        if (tableData.getTable_capacity().equalsIgnoreCase("")) {
            holder.capacity.setText("00");
        } else {
            holder.capacity.setText(tableData.getTable_capacity());
        }
        if (tableData.getReserv_priority().equalsIgnoreCase("")) {
            holder.reserv.setText("00");
        } else {
            holder.reserv.setText(tableData.getReserv_priority());
        }
     /*   if (tableData.getAlot_hours().equalsIgnoreCase("")) {
            holder.hours.setText("00");
        } else {
            holder.hours.setText(tableData.getAlot_hours());
        }*/
        if (tableData.getAlot_min().equalsIgnoreCase("")) {
            holder.mins.setText("00");
        } else {
            holder.mins.setText(tableData.getAlot_min() + " mins");
        }

        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNewTableActivity.class);
                if (tableData.getTable_type().equalsIgnoreCase("combine"))
                    intent.setAction("combine_edit");
                else
                    intent.setAction("single_edit");
                intent.putExtra("table_data", tableData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tabledetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemlayout;
        CustomTextView tableno, capacity, reserv, mins, table_service, tvBlock;

        public ViewHolder(View itemView) {
            super(itemView);
            itemlayout = (RelativeLayout) itemView.findViewById(R.id.itemsMenu);
            tableno = (CustomTextView) itemView.findViewById(R.id.table_name);
            tvBlock = (CustomTextView) itemView.findViewById(R.id.tvBlock);
            capacity = (CustomTextView) itemView.findViewById(R.id.table_capacity);
            reserv = (CustomTextView) itemView.findViewById(R.id.table_reser);
            table_service = (CustomTextView) itemView.findViewById(R.id.table_service);
//            hours = (CustomTextView) itemView.findViewById(R.id.table_hours);
            mins = (CustomTextView) itemView.findViewById(R.id.table_mins);
        }
    }

    public interface ManageTableAdapterListener {
        void blockTable(View view, int position, boolean block_status);
    }

}
