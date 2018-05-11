package com.yberry.dinehawaii.Customer.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.yberry.dinehawaii.Model.ReviewModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class MyReviewsAdapter extends RecyclerView.Adapter<MyReviewsAdapter.ViewHolder>{
    Context context;
    ArrayList<ReviewModel> arrayList;
    public MyReviewsAdapter(Context context, ArrayList<ReviewModel> list) {
        this.context = context;
        this.arrayList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myreview_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        ReviewModel reviewModel = arrayList.get(i);
        holder.busname.setText(reviewModel.getBussName());
        holder.date.setText(reviewModel.getDate());
        holder.ratingtext.setText(reviewModel.getRating()+"/5");
        holder.ratingBar.setRating(Float.parseFloat(reviewModel.getRating()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView date,busname,ratingtext;
        RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            date = (CustomTextView)itemView.findViewById(R.id.tvDate);
            busname = (CustomTextView)itemView.findViewById(R.id.businessName);
            ratingtext = (CustomTextView)itemView.findViewById(R.id.overallRatting);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rattingbar);
        }
    }
}
