package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.yberry.dinehawaii.Model.SelectedCustomizationModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.interfacese.SelectedChbkInterface;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by hvantage3 on 7/4/2017.
 */

public class SelectedCustamizationAdapter extends RecyclerView.Adapter<SelectedCustamizationAdapter.SelectedCustomizattion> {

    private ArrayList<SelectedCustomizationModel> selectedList;
    private Context mContext;
    SelectedChbkInterface  anInterface;
    ArrayList<String> list1=new ArrayList<String>();

    public SelectedCustamizationAdapter(Context mContext, ArrayList<SelectedCustomizationModel> list, SelectedChbkInterface  anInterface){
    this.mContext=mContext;
    this.selectedList=list;
    this.anInterface=anInterface;


    }
    @Override
    public SelectedCustomizattion onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_selected_customization, parent, false);
        SelectedCustomizattion viewHolder = new SelectedCustomizattion(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SelectedCustomizattion holder, final int position) {
        if(selectedList!=null){
            SelectedCustomizationModel model=selectedList.get(position);
            holder.chk.setText(model.getName());
            holder.chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        list1.add(holder.chk.getText().toString());
                         notifyDataSetChanged();
                        anInterface.setOnSelectedItems(list1.toString());
                    }else {
                        list1.remove(position);
                        //Toast.makeText(mContext,"Please Select one items",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return selectedList.size();
    }

    public class SelectedCustomizattion extends RecyclerView.ViewHolder{
        private CheckBox chk;

        public SelectedCustomizattion(View itemView) {
            super(itemView);
            chk=(CheckBox)itemView.findViewById(R.id.cb1);

        }

    }
}
