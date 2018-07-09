package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.yberry.dinehawaii.Model.ReviewModel;
import com.yberry.dinehawaii.R;
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

    public CustomerResturantAdapter(Context context, List<ReviewModel> reviewlist) {
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

        if (!model.getRating().equalsIgnoreCase(""))
            holder.ratingBar.setRating(Float.parseFloat(model.getRating()));
        holder.customer.setText("by " + model.getReview_question());
        holder.title.setText(model.getReview_message());
        if (!model.getBusiness_reply().equalsIgnoreCase("")) {
            holder.tvResponse.setVisibility(View.VISIBLE);
            holder.tvResponse.setText("(Reply: " + model.getBusiness_reply()+")");
        }
    }


    @Override
    public int getItemCount() {
        return reviewlist.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView user_post;
        CustomTextView title, customer, tvResponse;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            user_post = (CardView) itemView.findViewById(R.id.user_post);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            title = (CustomTextView) itemView.findViewById(R.id.title);
            customer = (CustomTextView) itemView.findViewById(R.id.customer);
            tvResponse = (CustomTextView) itemView.findViewById(R.id.tvResponse);
        }
    }
}