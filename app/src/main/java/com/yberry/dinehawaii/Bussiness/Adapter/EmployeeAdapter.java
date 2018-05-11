package com.yberry.dinehawaii.Bussiness.Adapter;

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



public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    Context context;
    private List<ReportEmployee> employeelist;

    public EmployeeAdapter(Context context, List<ReportEmployee> employeelist){
        this.context = context;
        this.employeelist = employeelist;
    }

    public EmployeeAdapter(Context context) {
    }

    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_data, parent, false);
        EmployeeAdapter.ViewHolder viewHolder = new EmployeeAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ReportEmployee model = employeelist.get(position);
     /*   reportemployee.setBusiness_name(object.getString("business_name"));
        reportemployee.setDine_app_id(object.getString("dine_app_id"));
        reportemployee.setAdded_by(object.getString("added_by"));
        reportemployee.setEmail(object.getString("email"));
        reportemployee.setMobile(object.getString("mobile"));
        reportemployee.setName(object.getString("name"));
        reportemployee.setPosiiton(object.getString("posiiton"));
        reportemployee.setStatus(object.getString("status"));

   */
        holder.mbusiness_name.setText("Business Name -" + model.getBusiness_name());
        holder.mbusiness_Id.setText("Id -" + model.getDine_app_id());
        holder.maddded_by.setText("Added by -" + model.getAdded_by());
        holder.memail_id.setText("Email-Id -" + model.getEmail());
        holder.mmobile.setText("Mobile -" + model.getMobile());
        holder.mname.setText("Name -" + model.getName());
        holder.mposition.setText("Position -" + model.getPosiiton());
         holder.mtablename.setText("Status -" + model.getStatus());


    }






    @Override
    public int getItemCount() {
        return employeelist.size();

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

        }}}
/*
public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {
    Context context;

    private List<CustomerModel> employeelist;



    public EmployeeAdapter(Context context, List<CustomerModel> employeelist){
        this.context = context;
        this.employeelist = employeelist;

    }



    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_data, parent, false);
        EmployeeAdapter.ViewHolder viewHolder = new EmployeeAdapter.ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       // CustomTextView  business_name,business_Id,addded_by,email_id,mobile,name,position,status;
ReportEmployee model = ReportEmployee.get(position);
     */
/*   final ReportEmployee model = ReportEmployee.get(position);
        holder.title.setText("Title -" + AppPreferences.getCustomerCoupon(context));
        holder.business_name.setText("Name -" + model.getBusiness_name());
        holder.coupon_code.setText("Code -" + model.getCoupon_code());
        holder.amt.setText("Amount -" + model.getAmount());
        holder.d_start.setText("Start Date -" + model.getStart_date());
        holder.d_end.setText("Expire -" + model.getEnd_date());*//*



    }










    @Override
    public int getItemCount() {
        return 9;

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView  business_name,business_Id,addded_by,email_id,mobile,name,position,status;

        public ViewHolder(View itemView) {
            super(itemView);
            business_name = (CustomTextView)itemView.findViewById(R.id.business_name);

            business_Id = (CustomTextView)itemView.findViewById(R.id.business_Id);
            addded_by = (CustomTextView)itemView.findViewById(R.id.addded_by);
            email_id = (CustomTextView)itemView.findViewById(R.id.email_id);
            mobile = (CustomTextView)itemView.findViewById(R.id.mobile);
            name = (CustomTextView)itemView.findViewById(R.id.name);
            position = (CustomTextView) itemView.findViewById(R.id.position);
            status = (CustomTextView) itemView.findViewById(R.id.status);



        }


    }}
*/
