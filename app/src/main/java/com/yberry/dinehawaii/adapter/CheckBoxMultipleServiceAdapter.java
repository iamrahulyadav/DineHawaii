package com.yberry.dinehawaii.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;

import java.util.ArrayList;

/**
 * Created by JAI on 2/9/2017.
 */

public class CheckBoxMultipleServiceAdapter extends RecyclerView.Adapter<CheckBoxMultipleServiceAdapter.ViewHolder> implements View.OnClickListener{
    private Context mCotnext;
    private ArrayList<CheckBoxPositionModel> list;
    public ArrayList<CheckBoxPositionModel> listValue = new ArrayList<CheckBoxPositionModel>();
    setOnClickListener setOnClickListener;
    public CheckBoxMultipleServiceAdapter(Context mCotnext, ArrayList<CheckBoxPositionModel> list, setOnClickListener setOnClickListener) {
        this.mCotnext = mCotnext;
        this.list = list;
        this.setOnClickListener=setOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_pos_check_box, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CheckBoxPositionModel optionData = list.get(position);
        if (optionData != null) {
            holder.checkBoxPositionModel = optionData;
            holder.checkBox.setText(optionData.getName());
            ///holder.checkBox.setText(optionData.getFood_name());

            if(optionData.isChckStatus()){
                holder.checkBox.setChecked(true);
            }else {
                holder.checkBox.setChecked(false);
            }


            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    optionData.setChckStatus(isChecked);
                    if(isChecked) {
                        listValue.add(optionData);
                        Log.d("DataList", listValue.toString());
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

    public ArrayList<CheckBoxPositionModel> getSelectedItem() {
        ArrayList<CheckBoxPositionModel> lists = new ArrayList<CheckBoxPositionModel>();
        for (CheckBoxPositionModel model : list) {
            if (model.isChckStatus()) {
               // lists.add(model.getId());
                lists.add(model);

                Log.d("CheckBoxId", model.getId());
            }
            Log.d("CheckBox",lists.toString());

        }
        return lists;
    }

    @Override
    public void onClick(View view) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox checkBox;
        CheckBoxPositionModel checkBoxPositionModel;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            checkBox = (CheckBox) itemView.findViewById(R.id.chk_bussineId);
            checkBox.setChecked(false);
        }

        @Override
        public void onClick(View view) {
            setOnClickListener.onItemClick(checkBoxPositionModel);

        }
    }
    public interface setOnClickListener{
        public void onItemClick(CheckBoxPositionModel checkBoxPositionModel);
    }
}
