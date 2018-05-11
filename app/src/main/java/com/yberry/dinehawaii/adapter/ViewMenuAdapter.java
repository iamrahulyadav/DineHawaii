package com.yberry.dinehawaii.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;

import java.util.List;

/**
 * Created by Peter on 18-Apr-17.
 */

public class ViewMenuAdapter extends RecyclerView.Adapter<ViewMenuAdapter.MyViewHolder> {

    private Context mContext;
    private List<CheckBoxPositionModel> listValue;


    public ViewMenuAdapter(Context mContext, List<CheckBoxPositionModel> listValue) {
        this.mContext = mContext;
        this.listValue = listValue;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_text_view_with_edit_text_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CheckBoxPositionModel checkBoxPositionModel = listValue.get(position);
        holder.tvlistItem.setText(checkBoxPositionModel.getDish_name());
        holder.etlistPrice.setText(checkBoxPositionModel.getAmount());

    }

    @Override
    public int getItemCount() {
        return listValue.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvlistItem;
        public EditText etlistPrice;
        CheckBox chkSelected;


        public MyViewHolder(View view) {
            super(view);
            tvlistItem = (TextView) view.findViewById(R.id.tvlistitem);
            etlistPrice = (EditText) view.findViewById(R.id.etlistPrice);
            etlistPrice.setFocusable(false);
            etlistPrice.setFocusableInTouchMode(false);
            etlistPrice.setClickable(false);
            etlistPrice.setActivated(false);
            etlistPrice.setEnabled(false);

            chkSelected = (CheckBox) view.findViewById(R.id.chkSelected);
        }

    }

    public List<CheckBoxPositionModel> getListValue(){
        return listValue;
    }
}
