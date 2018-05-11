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

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    Context context;
    private List<ReportEmployee> reservationlist;


    public ReservationAdapter(Context context, List<ReportEmployee> reservationlist){
        this.context = context;
       this.reservationlist = reservationlist;

    }



    @Override
    public ReservationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_data, parent, false);
        ReservationAdapter.ViewHolder viewHolder = new ReservationAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

      final ReportEmployee model = reservationlist.get(position);
      //  mbusiness_name,mbusiness_Id,maddded_by,memail_id,mmobile,mname,mposition,mdate,mtime,mpartsize,mtablename;

       holder.mbusiness_name.setText("Business Name -" + model.getBusiness_name());
       holder.mbusiness_Id.setText("Id -" + model.getDine_app_id());
       holder.maddded_by.setText("Added by -" + model.getAdded_by());
       holder.memail_id.setText("Email-Id -" + model.getEmail());
       holder.mmobile.setText("Mobile -" + model.getMobile());
       holder.mname.setText("Name -" + model.getName());
       holder.mposition.setText("Business Name -" + model.getPosiiton());
       holder.mdate.setText("Date-" + model.getDate());
       holder.mtime.setText("Time-" + model.getTime());
       holder.mpartsize.setText("Party Size-" + model.getParty_size());
       holder.mtablename.setText("Table Name -" + model.getTable_name());

    }








    @Override
    public int getItemCount() {
       return reservationlist.size();
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
