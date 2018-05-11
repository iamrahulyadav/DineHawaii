package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.adapter.CheckBoxOptionAdapter;

import java.util.ArrayList;

/**
 * Created by Peter on 04-Apr-17.
 */

public class SelectFoodTypeAdapter extends RecyclerView.Adapter<SelectFoodTypeAdapter.ViewHolder> implements View.OnClickListener {

    private static final String TAG = "SelectFoodTypeAdapter";
    private Context mCotnext;
    private ArrayList<CheckBoxPositionModel> list;
    public ArrayList<CheckBoxPositionModel> listValue = new ArrayList<CheckBoxPositionModel>();
    SelectFoodTypeAdapter.setOnClickListener setOnClickListener;

    public SelectFoodTypeAdapter(Context mCotnext, ArrayList<CheckBoxPositionModel> list, SelectFoodTypeAdapter.setOnClickListener setOnClickListener) {
        this.mCotnext = mCotnext;
        this.list = list;
        this.setOnClickListener=setOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.radio_food_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final CheckBoxPositionModel optionData = list.get(i);
        if (optionData != null) {
            viewHolder.checkBoxPositionModel = optionData;
            viewHolder.radioButton.setText(optionData.getName());
            ///holder.checkBox.setText(optionData.getFood_name());

            if (optionData.isChckStatus()) {
                viewHolder.radioButton.setChecked(true);
            } else {
                viewHolder.radioButton.setChecked(false);
            }

            viewHolder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    optionData.setChckStatus(isChecked);
                    if(isChecked) {
                        listValue.add(optionData);
                        Log.d(TAG, "DataList :- " +listValue.toString());
                    }
                    else
                    {
                        int pos =listValue.indexOf(optionData);
                        listValue.remove(pos);
                        Log.d("DataListReomve", listValue.toString());
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
            return list.size();
    }

    @Override
    public void onClick(View v) {

    }

    public ArrayList<CheckBoxPositionModel> getSelectedItem() {
        ArrayList<CheckBoxPositionModel> lists = new ArrayList<CheckBoxPositionModel>();
        for (CheckBoxPositionModel model : list) {
            if (model.isChckStatus()) {
                // lists.add(model.getId());
                lists.add(model);
            }
            Log.d("CheckBox",lists.toString());
        }
        return lists;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RadioButton radioButton;
        CheckBoxPositionModel checkBoxPositionModel;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            radioButton = (RadioButton) itemView.findViewById(R.id.chk_bussineId);
            radioButton.setChecked(false);
        }


        @Override
        public void onClick(View v) {
            setOnClickListener.onItemClick(checkBoxPositionModel);
        }
    }

    public interface setOnClickListener{
        public void onItemClick(CheckBoxPositionModel checkBoxPositionModel);
    }
}
