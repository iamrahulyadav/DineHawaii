package com.yberry.dinehawaii.Bussiness.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.customview.CustomCheckBox;

import java.util.ArrayList;
import java.util.HashMap;

import static com.yberry.dinehawaii.Bussiness.Activity.BusiSelectPackageActivity.package_type;

/**
 * Created by PRINCE 9977123453 on 08-02-17.
 */

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    private static final String TAG = "PackageAdapter";
    int amount = 0;
    int selected_count = 0;
    private Context context;
    private ArrayList<CheckBoxPositionModel> list;
    private LayoutInflater inflater;
    private HashMap<String, Double> packageCharge;

    public PackageAdapter(Context context, ArrayList<CheckBoxPositionModel> list, HashMap<String, Double> packageCharge) {
        super();
        this.list = list;
        this.context = context;
        this.packageCharge = packageCharge;
        inflater = LayoutInflater.from(context);
        Log.e(TAG, "PackageAdapter: packageCharge >> " + packageCharge);
    }

    @Override
    public PackageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_pos_check_box, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final PackageAdapter.ViewHolder holder, final int position) {
        final CheckBoxPositionModel model = list.get(position);

        if (model != null) {
            holder.checkBox.setText(model.getName());
            if (model.isChckStatus()) {
                holder.checkBox.setChecked(true);
                selected_count++;
                package_type.setText(String.valueOf(packageCharge.get(String.valueOf(selected_count))));
            } else {
                holder.checkBox.setChecked(false);
            }

            holder.tooltip_help.setVisibility(View.VISIBLE);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    holder.checkBox.setChecked(isChecked);
                    model.setChckStatus(isChecked);

                    if (isChecked) {
                        if (selected_count <= 0) {
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("test"));
                        }
                        selected_count++;
                        Log.e(TAG, "onCheckedChanged: Selected count >> " + selected_count);
                        package_type.setText(String.valueOf(packageCharge.get(String.valueOf(selected_count))));

                    } else if (!isChecked) {
                        selected_count--;
                        package_type.setText(String.valueOf(packageCharge.get(String.valueOf(selected_count))));
                        if (selected_count <= 0)
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("test"));
                        Log.e(TAG, "onCheckedChanged: Selected count >> " + selected_count);
                    }
                }

/*
                        String price =  model.getAmount().replace("$", "").trim();
                        Log.v(TAG, " package optionTotalAmount :- " + price );
                        amount = amount + Integer.parseInt(price);
                        if(amount > 0 ) {
                            Log.v(TAG, "Total package optionTotalAmount :- " + amount);
                            package_type.setText(" " + amount);
                        }else{
                            Toast.makeText(context, "Please select package", Toast.LENGTH_SHORT).show();
                        }*/

            });

            holder.tooltip_help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.isChckStatus()) {
                        Function.bottomToolTipDialogBox(null, context, "This package is already selected by you !!!" /*+ "\n Package Details : " + model.getPackage_detail()*/, holder.tooltip_help, null);
                    } else {
                        Function.bottomToolTipDialogBox(null, context, "This package monthly cost is $" + model.getAmount().replace("$", "") /*+"\nPackage Details :\n" + model.getPackage_detail()*/, holder.tooltip_help, null);
                    }
                }
            });
        }
    }

    public void clearAllChecked() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChckStatus(false);
        }
        //notifyItemRangeChanged(0, list.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<String> getSelectedItem() {
        ArrayList<String> lists = new ArrayList<String>();
        for (CheckBoxPositionModel model : list) {
            if (model.isChckStatus()) {
                lists.add(model.getId());
            }
            Log.d(TAG, "CheckBox Package ids :- " + lists.toString());
        }
        return lists;
    }

    public int getSelected_count() {
        int count = 0;
        for (CheckBoxPositionModel model : list) {
            if (model.isChckStatus()) {
                count++;
            }
        }
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomCheckBox checkBox;
        private ImageView tooltip_help;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CustomCheckBox) itemView.findViewById(R.id.chk_bussineId);
            tooltip_help = (ImageView) itemView.findViewById(R.id.tooltip_help);
            checkBox.setChecked(false);
        }
    }
}