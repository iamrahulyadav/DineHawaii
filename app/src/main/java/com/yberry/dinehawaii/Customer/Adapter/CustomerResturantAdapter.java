package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yberry.dinehawaii.Model.ReviewModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.List;


/**
 * Created by abc on 8/22/2017.
 */

/*public class CustomerResturantAdapter  {
}*/



public class CustomerResturantAdapter extends RecyclerView.Adapter<CustomerResturantAdapter.ViewHolder> {

    Context context;
    private List<ReviewModel> reviewlist;

    public CustomerResturantAdapter(Context context,List<ReviewModel> reviewlist) {
        this.context = context;
        this.reviewlist = reviewlist;

    }

    @Override
    public CustomerResturantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.resuratant_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final CustomerResturantAdapter.ViewHolder holder, int position) {
        final ReviewModel model = reviewlist.get(position);
        holder.title.setText(model.getReview_question());
        holder.business_name.setText(model.getReview_message());
        holder.coupon_code.setText(model.getTime());

        if (!model.getImage().equalsIgnoreCase(""))
            Picasso.with(context).load(model.getImage()).placeholder(R.drawable.pic).into(holder.resturant_logo);
    }


    @Override
    public int getItemCount() {
        return reviewlist.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView user_post;
        CustomTextView title,business_name,coupon_code;
        ImageView resturant_logo;

        public ViewHolder(View itemView) {
            super(itemView);
            user_post = (CardView)itemView.findViewById(R.id.user_post);
            title = (CustomTextView)itemView.findViewById(R.id.title);
            business_name = (CustomTextView)itemView.findViewById(R.id.business_name);
            coupon_code = (CustomTextView)itemView.findViewById(R.id.coupon_code);
            resturant_logo = (ImageView)itemView.findViewById(R.id.resturant_logo);
        }
    }}