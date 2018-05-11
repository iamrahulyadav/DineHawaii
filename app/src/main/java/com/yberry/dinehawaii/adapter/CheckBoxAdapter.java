package com.yberry.dinehawaii.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRINCE 9977123453 on 08-02-17.
 */

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ViewHolder> {
    private static final String TAG = "PackageAdapter";
    private Context context;
    private ArrayList<CheckBoxPositionModel> listFoodService;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;

    public  CheckBoxAdapter(Context context, ArrayList<CheckBoxPositionModel> listFoodService, OnItemClickListener mOnItemClickListener) {
        this.context = context;
        this.listFoodService = listFoodService;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public CheckBoxAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_box_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final CheckBoxAdapter.ViewHolder holder, final int position) {
        final CheckBoxPositionModel checkBoxPositionModel = listFoodService.get(position);

//        CheckBoxPositionModel checkBoxPositionModel = listValue.get(position);
        holder.tvlistItem.setText(checkBoxPositionModel.getDish_name());
        holder.etlistPrice.setText(checkBoxPositionModel.getAmount());
        holder.chkSelected.setSelected(checkBoxPositionModel.isSelected());

        holder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkBoxPositionModel.setSelected(true);
                    holder.etlistPrice.setFocusable(true);
                    holder.etlistPrice.setFocusableInTouchMode(true);
                    holder.etlistPrice.setClickable(true);
                    holder.etlistPrice.setActivated(true);
                    holder.etlistPrice.setEnabled(true);
                }else {
                    checkBoxPositionModel.setSelected(false);
                    holder.etlistPrice.setFocusable(false);
                    holder.etlistPrice.setFocusableInTouchMode(false);
                    holder.etlistPrice.setClickable(false);
                    holder.etlistPrice.setActivated(false);
                    holder.etlistPrice.setEnabled(false);
                }
            }
        });

        holder.etlistPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listFoodService.get(position).setAmount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void clearAllChecked() {
        for (int i = 0; i < listFoodService.size(); i++) {
            listFoodService.get(i).setChckStatus(false);
        }
        //notifyItemRangeChanged(0, list.size());
    }

    @Override
    public int getItemCount() {
        return listFoodService.size();
    }

    public ArrayList<String> getSelectedItem() {
        ArrayList<String> lists = new ArrayList<String>();
        for (CheckBoxPositionModel model : listFoodService) {
            if (model.isChckStatus()) {
                lists.add(model.getId());
            }
        }
        return lists;
    }

    public interface OnItemClickListener {
        public void onItemClick(int view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvlistItem;
        public EditText etlistPrice;
        public CheckBox chkSelected;

        public ViewHolder(View view) {
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
        return listFoodService;
    }

}
