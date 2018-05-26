package com.yberry.dinehawaii.Bussiness.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yberry.dinehawaii.Bussiness.model.CustomizationModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class CustomizationItemsAdapter extends RecyclerView.Adapter<CustomizationItemsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CustomizationModel> itemsList;

    public CustomizationItemsAdapter(Context context, ArrayList<CustomizationModel> chairDataList) {
        this.context = context;
        this.itemsList = chairDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customization_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CustomizationModel model = itemsList.get(position);

        holder.tvItemNo.setText(position + 1 + "");
        holder.tv_ItemName.setText(model.getItemName());
        holder.tv_ItemName.setEnabled(true);
        holder.buttonAdd.setVisibility(View.VISIBLE);
        holder.removeItem.setVisibility(View.GONE);
        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("addedData");
                intent.putExtra("position", position);
                intent.putExtra("itemName", holder.tv_ItemName.getText().toString());
                intent.putExtra("added", true);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                holder.buttonAdd.setVisibility(View.GONE);
                holder.removeItem.setVisibility(View.VISIBLE);
                holder.tv_ItemName.setEnabled(false);
                Toast.makeText(context, "ITEM ADDED", Toast.LENGTH_SHORT).show();
            }
        });

        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemDialog(position);
            }
        });
    }

    private void deleteItemDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Dine Hawaii");
        builder.setIcon(R.drawable.ic_launcher_app);
        builder.setMessage("Do you want to remove this item from list");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemsList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomEditText tv_ItemName;
        CustomButton buttonAdd, removeItem;
        CustomTextView tvItemNo;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_ItemName = (CustomEditText) itemView.findViewById(R.id.tv_ItemName);
            tvItemNo = (CustomTextView) itemView.findViewById(R.id.tvItemNo);
            removeItem = (CustomButton) itemView.findViewById(R.id.removeItem);
            buttonAdd = (CustomButton) itemView.findViewById(R.id.buttonAdd);
        }
    }
}


