package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {
    Context context;
    ArrayList<ListItem> listItems;
    OnItemClickListener itemClickListener;
    int lastPosition = -1;
    private MyViewHolder holder;

    public RestaurantListAdapter(Context context, ArrayList<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

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

        if (listItem.getDisplayLogo() == true)
            holder.dineLogo.setVisibility(View.VISIBLE);
        else holder.dineLogo.setVisibility(View.GONE);

        holder.mDistance.setText("(" + listItem.getDistance() + " miles away)");

        // new SetImageTask().execute(listItem.getCoverImage(), listItem.getLogoImg());

        if (!listItem.getLogoImg().equalsIgnoreCase("")) {
            Glide.with(context).load(listItem.getLogoImg())
                    .thumbnail(0.5f)
                    .crossFade()
//                    .override(50, 50)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.restLogo);
        }

        /*if (listItem.getCoverImage() != null)
            if (!listItem.getCoverImage().equalsIgnoreCase("")) {
                Picasso.with(context)
                        .load(listItem.getCoverImage())
                        .placeholder(R.drawable.ic_restaurant_grey_24dp)
                        .error(R.drawable.ic_restaurant_grey_24dp)
                        .into(holder.restLogo);
            }
*/
       /* if(position >lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }*/
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
        ImageView dineLogo, restLogo;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            name = (CustomTextView) itemView.findViewById(R.id.mName);
            address = (CustomTextView) itemView.findViewById(R.id.mAddress);
            mDistance = (CustomTextView) itemView.findViewById(R.id.mDistance);
            myRatingBar = (RatingBar) itemView.findViewById(R.id.myRatingBar);
            dineLogo = (ImageView) itemView.findViewById(R.id.dinelogo);
//            restCover = (ImageView) itemView.findViewById(R.id.restCover);
            restLogo = (ImageView) itemView.findViewById(R.id.restLogo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(listItems.get(getAdapterPosition()));
        }
    }

    class SetImageTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            publishProgress(strings[1], strings[0]);
            return null;
        }

        private void publishProgress(String logo, String cover) {

        }
    }
}
