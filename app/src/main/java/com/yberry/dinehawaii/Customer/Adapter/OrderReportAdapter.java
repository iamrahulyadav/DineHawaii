package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yberry.dinehawaii.Bussiness.model.ReportEmployee;
import com.yberry.dinehawaii.Customer.Activity.ConfirmCoupon;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.List;

/**
 * Created by abc on 8/11/2017.
 */

public class OrderReportAdapter extends RecyclerView.Adapter<OrderReportAdapter.ViewHolder> {
    Context context;
    private List<ReportEmployee> orderlistlist;


    public OrderReportAdapter(Context context,  List<ReportEmployee> orderlistlist){
        this.context = context;
        this.orderlistlist = orderlistlist;

    }



    @Override
    public OrderReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_data, parent, false);
        OrderReportAdapter.ViewHolder viewHolder = new OrderReportAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ReportEmployee model = orderlistlist.get(position);
        //  mbusiness_name,mbusiness_Id,maddded_by,memail_id,mmobile,mname,mposition,mdate,mtime,mpartsize,mtablename;

        holder.mbusiness_name.setText("Business Name -" + model.getBusiness_name());
        holder.mbusiness_Id.setText("Id -" + model.getDine_app_id());
        holder.maddded_by.setText("Added by -" + model.getAdded_by());
        holder.memail_id.setText("Total Price -" + model.getTotal_prcie());
        holder.mmobile.setText("Delivery Address -" + model.getDelivery_adderess());
        holder.mname.setText("Delivery Contact -" + model.getDelivery_contact_no());
        holder.mposition.setText("Delivery Name -" + model.getDelivery_name());
        holder.mdate.setText("Food Name-" + model.getFood_name());
        holder.mtime.setText("Status-" + model.getStatus());

    }







    @Override
    public int getItemCount() {
        return orderlistlist.size();
        //return 2;


    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView  mbusiness_name,mbusiness_Id,maddded_by,memail_id,mmobile,mname,mposition,mdate,mtime,mpartsize,mtablename;

        public ViewHolder(View itemView) {
            super(itemView);
            mbusiness_name = (CustomTextView)itemView.findViewById(R.id.mbusiness_name);
            mbusiness_Id = (CustomTextView)itemView.findViewById(R.id.mbusiness_Id);
            maddded_by = (CustomTextView)itemView.findViewById(R.id.maddded_by);
            memail_id = (CustomTextView)itemView.findViewById(R.id.memail_id);
            mmobile = (CustomTextView)itemView.findViewById(R.id.mmobile);
            mname = (CustomTextView)itemView.findViewById(R.id.mname);
            mposition = (CustomTextView)itemView.findViewById(R.id.mposition);
            mdate = (CustomTextView)itemView.findViewById(R.id.mdate);
            mtime = (CustomTextView)itemView.findViewById(R.id.mtime);
            mpartsize = (CustomTextView)itemView.findViewById(R.id.mpartsize);
            mtablename = (CustomTextView)itemView.findViewById(R.id.mtablename);


        }


    }}




















/*
package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

*/
/**
 * Created by Hvantage2 on 6/28/2017.
 *//*


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<String> list = new ArrayList<>();
    List<String> list1 = new ArrayList<>();
    Context context;
    private ArrayList<HashMap<String, String>> repeatOrderList;

    public OrderAdapter(Context context, ArrayList<HashMap<String, String>> repeatOrderList) {
        this.context = context;
        this.repeatOrderList = repeatOrderList;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customization, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.ViewHolder holder, int position) {
        list1.clear();
        list.clear();
        final HashMap<String, String> model = repeatOrderList.get(position);

        if (model != null) {


            String data = model.get("FoodName");
            String FoodQaunty = model.get("FoodQaunty");

            list.add(data);
            list1.add(FoodQaunty);

            for (String d : list) {
                holder.foodname.setText(d);
                Log.d("model", d);
            }
            for (String d : list1) {

                holder.order_quantity.setText(d);
            }

        }

    }

    @Override
    public int getItemCount() {
        return repeatOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  foodname, order_customization, order_quantity;

        public ViewHolder(View itemView) {
            super(itemView);




            foodname = (TextView) itemView.findViewById(R.id.foodname);
            order_customization = (TextView) itemView.findViewById(R.id.customization);
            order_quantity = (TextView) itemView.findViewById(R.id.quantity);

        }


    }
}*/
