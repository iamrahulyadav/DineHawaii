package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hvantage2 on 8/2/2017.
 */

public class NotificationBusinessAdapter extends RecyclerView.Adapter<NotificationBusinessAdapter.ViewHolder> {
    private static final String TAG = "NotificationBusAdapter";
    Context context;
    private List<CustomerModel> notificationList;


    public NotificationBusinessAdapter(Context context, List<CustomerModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;

    }

    public NotificationBusinessAdapter(Context context) {
    }

    @Override
    public NotificationBusinessAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        NotificationBusinessAdapter.ViewHolder viewHolder = new NotificationBusinessAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NotificationBusinessAdapter.ViewHolder holder, final int position) {

        final CustomerModel model = notificationList.get(position);

        holder.noti_msg.setText(model.getNotification_msg());
        holder.noti_tym.setText(model.getNotification_time());
        if(!model.getNotification_image().equalsIgnoreCase(""))
        Picasso.with(context).load(model.getNotification_image()).placeholder(R.drawable.pic).into(holder.noti_img);
//        new AQuery(context).id(holder.noti_img).image(model.getNotification_image(), true, true, 0, 0);
    }

    @Override
    public int getItemCount() {

        return notificationList == null ? 0 : notificationList.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noti_msg, noti_tym;
        CircleImageView noti_img;

        public ViewHolder(View itemView) {
            super(itemView);

            noti_msg = (TextView) itemView.findViewById(R.id.message);
            noti_tym = (TextView) itemView.findViewById(R.id.time);
            noti_img = (CircleImageView) itemView.findViewById(R.id.noti_image);
        }


    }
}
