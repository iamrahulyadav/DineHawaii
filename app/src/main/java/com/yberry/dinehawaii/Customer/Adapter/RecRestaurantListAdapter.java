package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class RecRestaurantListAdapter extends RecyclerView.Adapter<RecRestaurantListAdapter.MyViewHolder> {
    Context context;
    ArrayList<ListItem> listItems;
    OnItemClickListener itemClickListener;
    int lastPosition = -1;
    private MyViewHolder holder;

    public RecRestaurantListAdapter(Context context, ArrayList<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_list_item, parent, false);

        myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        this.holder = holder;
        ListItem listItem = listItems.get(position);
        Log.e("check0909", String.valueOf(listItems.get(position)));
        holder.name.setText(listItem.getBusinessName());
        holder.address.setText(listItem.getBusinessAddress());
        holder.myRatingBar.setRating(Float.parseFloat(listItem.getRating()));

        if (listItem.getBusinessOption() != null) {
            Log.e("OPTIONOFBUSINESSS", listItem.getBusinessOption());
            String business_option = listItem.getBusinessOption();
            if (business_option.contains("2")) {
                listItem.setDisplayLogo(true);
            }
        }

        holder.mDistance.setText("(" + listItem.getDistance() + " miles away)");
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(ListItem listItem);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CustomTextView name, address, mDistance;
        RatingBar myRatingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            name = (CustomTextView) itemView.findViewById(R.id.mName);
            address = (CustomTextView) itemView.findViewById(R.id.mAddress);
            mDistance = (CustomTextView) itemView.findViewById(R.id.mDistance);
            myRatingBar = (RatingBar) itemView.findViewById(R.id.myRatingBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(listItems.get(getAdapterPosition()));
        }
    }

}
