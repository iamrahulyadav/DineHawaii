package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hvantage2 on 7/21/2017.
 */

public class NotificationCustomerAdapter extends RecyclerView.Adapter<NotificationCustomerAdapter.ViewHolder> {
    private static final String TAG = "NotificationcustAdapter";
    Context context;
    private List<CustomerModel> notificationList;


    public NotificationCustomerAdapter(Context context, List<CustomerModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;

    }

    @Override
    public NotificationCustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        NotificationCustomerAdapter.ViewHolder viewHolder = new NotificationCustomerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NotificationCustomerAdapter.ViewHolder holder, final int position) {

        final CustomerModel model = notificationList.get(position);

        holder.noti_msg.setText(model.getNotification_msg());
        holder.noti_tym.setText(model.getNotification_time());
        if (!model.getNotification_image().equalsIgnoreCase(""))
            Picasso.with(context).load(model.getNotification_image()).placeholder(R.drawable.pic).into(holder.noti_img);
    }

    @Override
    public int getItemCount() {

        return notificationList == null ? 0 : notificationList.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noti_msg, noti_tym;
        ImageView noti_img;

        public ViewHolder(View itemView) {
            super(itemView);

            noti_msg = (TextView) itemView.findViewById(R.id.message);
            noti_tym = (TextView) itemView.findViewById(R.id.time);
            noti_img = (ImageView) itemView.findViewById(R.id.noti_image);
        }


    }
}