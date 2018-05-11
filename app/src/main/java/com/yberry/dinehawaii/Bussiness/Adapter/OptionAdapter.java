package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
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

import static com.yberry.dinehawaii.Bussiness.Activity.BusiSelectPackageActivity.option_type;
import static com.yberry.dinehawaii.Bussiness.Activity.BusiSelectPackageActivity.package_type;

/**
 * Created by JAI on 2/9/2017.
 */


public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "OptionAdapter";
    public ArrayList<CheckBoxPositionModel> listValue = new ArrayList<CheckBoxPositionModel>();
    double optionTotalAmount = 0;
    private Context mCotnext;
    private ArrayList<CheckBoxPositionModel> list;
    HashMap<String, Double> optionCharge;
    double single_option_charge;
    private int selected_count = 0;


    public OptionAdapter(Context mCotnext, ArrayList<CheckBoxPositionModel> list, HashMap<String, Double> optionCharge, double single_option_charge) {
        this.mCotnext = mCotnext;
        this.list = list;
        this.optionCharge = optionCharge;
        this.single_option_charge = single_option_charge;
    }

    public OptionAdapter(ArrayList<CheckBoxPositionModel> option_list) {
        this.list = option_list;
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
            holder.checkBox.setText(optionData.getName());
            holder.tooltip_help.setVisibility(View.VISIBLE);
            if (optionData.isChckStatus()) {
                holder.checkBox.setChecked(true);
                selected_count++;
                if (package_type.getText().toString().equalsIgnoreCase("0.0")) {
                    optionTotalAmount = optionTotalAmount + single_option_charge;
                    option_type.setText(String.valueOf(optionTotalAmount));
                } else {
                    option_type.setText(String.valueOf(optionCharge.get(String.valueOf(selected_count))));
                }
            } else {
                holder.checkBox.setChecked(false);
            }

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    optionData.setChckStatus(isChecked);
                    Log.e(TAG, "onCheckedChanged: single option charge >> " + single_option_charge);
                    if (isChecked) {
                        selected_count++;
                        if (selected_count > 0) {
                            Log.e(TAG, "onCheckedChanged: package total >> " + package_type.getText().toString());
                            if (package_type.getText().toString().equalsIgnoreCase("0.0")) {
                                optionTotalAmount = optionTotalAmount + single_option_charge;
                                option_type.setText(String.valueOf(optionTotalAmount));
                            } else {
                                option_type.setText(String.valueOf(optionCharge.get(String.valueOf(selected_count))));
                            }
                        } else {
                            optionTotalAmount = 0;
                            selected_count = 0;
                            option_type.setText("0");
                        }


                        Log.e(TAG, "onCheckedChanged: Selected count >> " + selected_count);
                        /*String price = optionData.getAmount().replace("$", "").trim();
                        Log.v(TAG, "option optionTotalAmount :- " + price);
                        optionTotalAmount = optionTotalAmount + Integer.parseInt(price);
                        if(optionTotalAmount > 0 ) {
                            Log.v("OptionAdapter", "Total option optionTotalAmount :- " + optionTotalAmount);
                            option_type.setText("" + optionTotalAmount);
                        }*/
                    } else if (!isChecked) {
                        selected_count--;
                        if (selected_count > 0) {
                            Log.e(TAG, "onCheckedChanged: package total >> " + package_type.getText().toString());

                            if (package_type.getText().toString().equalsIgnoreCase("0.0")) {
                                optionTotalAmount = optionTotalAmount - single_option_charge;
                                option_type.setText(String.valueOf(optionTotalAmount));
                            } else {
                                option_type.setText(String.valueOf(optionCharge.get(String.valueOf(selected_count))));
                            }
                        } else {
                            optionTotalAmount = 0;
                            selected_count = 0;
                            option_type.setText("0");

                        }
                        Log.e(TAG, "onCheckedChanged: Selected count >> " + selected_count);

                        /*String price = optionData.getAmount().replace("$", "").trim();
                        optionTotalAmount = optionTotalAmount - Integer.parseInt(price);
                        if(optionTotalAmount > 0 ) {

                            option_type.setText("" + optionTotalAmount);
                        }
                        Log.v("OptionAdapter", "Total option optionTotalAmount :- " + optionTotalAmount);*/
                    }
                }
            });

            holder.tooltip_help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (optionData.isChckStatus()) {
                        Function.bottomToolTipDialogBox(null, mCotnext, "This option is already selected by you !!!" /*+ "\n Package Details : " + datalist.getPackage_detail()*/, holder.tooltip_help, null);
                    } else {
                        Function.bottomToolTipDialogBox(null, mCotnext, "Its monthly cost is $" + optionData.getAmount().replace("$", "") /*+"\nPackage Details :\n" + datalist.getPackage_detail()*/, holder.tooltip_help, null);
                    }
                }
            });

        }
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
            Log.d(TAG, "CheckBox option ids :- " + lists.toString());
        }
        return lists;
    }

    @Override
    public void onClick(View view) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CustomCheckBox checkBox;
        private ImageView tooltip_help;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tooltip_help = (ImageView) itemView.findViewById(R.id.tooltip_help);
            checkBox = (CustomCheckBox) itemView.findViewById(R.id.chk_bussineId);
            checkBox.setChecked(false);
        }

        @Override
        public void onClick(View view) {


        }
    }
}