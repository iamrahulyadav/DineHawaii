package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Peter on 05-May-17.
 */

public class PackageMarketingAdapter extends RecyclerView.Adapter<PackageMarketingAdapter.ViewHolder> {

    private static final String TAG = "PackageMarketingAdapter";
    private Context context;
    private ArrayList<CheckBoxPositionModel> packageList;

    public PackageMarketingAdapter(Context context, ArrayList<CheckBoxPositionModel> packageList) {
        this.context = context;
        this.packageList = packageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_package_marketing, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        CheckBoxPositionModel model = packageList.get(position);
        holder.packageNameTextView.setText(model.getName());
        holder.packageDetailsTextView.setText(model.getPackage_detail());

        holder.plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.packageDetailsTextView.getVisibility() == View.VISIBLE) {
                    holder.plusImageView.setImageResource(R.drawable.ic_add_black_24dp);
                    holder.packageDetailsTextView.setVisibility(View.GONE);
                } else {
                    holder.plusImageView.setImageResource(R.drawable.ic_remove_black_24dp);
                    holder.packageDetailsTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView plusImageView;
        CustomTextView packageNameTextView, packageDetailsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            packageNameTextView = (CustomTextView) itemView.findViewById(R.id.packageNameTextView);
            packageDetailsTextView = (CustomTextView) itemView.findViewById(R.id.packageDetailsTextView);
            plusImageView = (ImageView) itemView.findViewById(R.id.plusImageView);
        }
    }
}
