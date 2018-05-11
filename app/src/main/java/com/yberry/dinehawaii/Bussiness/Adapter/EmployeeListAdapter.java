package com.yberry.dinehawaii.Bussiness.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Fragment.SecurityBusinessAccessFragment;
import com.yberry.dinehawaii.Model.EmployeeModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by abc on 3/10/2018.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> {
    private final String TAG = "ManageTableAdapter";
    Context context;
    ArrayList<EmployeeModel> arrayList;

    public EmployeeListAdapter(Context context, ArrayList<EmployeeModel> details) {
        this.context = context;
        this.arrayList = details;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final EmployeeModel empData = arrayList.get(position);

        if (empData.getStatus().equalsIgnoreCase("Enable")) {
            holder.switchCompat.setChecked(true);
        } else if (empData.getStatus().equalsIgnoreCase("Disable")) {
            holder.switchCompat.setChecked(false);
        }
        if (empData.getEmp_firstnm().equalsIgnoreCase("")) {
            holder.empname.setText("NA");
        } else {
            holder.empname.setText(empData.getEmp_firstnm() + " " + empData.getEmp_lastmn());
        }
        if (empData.getEmp_jobtitle().equalsIgnoreCase("")) {
            holder.empjobtitle.setText("NA");
        } else {
            holder.empjobtitle.setText(empData.getEmp_jobtitle());
        }
        if (empData.getEmp_phoneno().equalsIgnoreCase("")) {
            holder.empphn.setText("NA");
        } else {
            holder.empphn.setText(empData.getEmp_phoneno());
        }
        holder.emplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callApi(empData);
                if (isChecked) {
                    holder.switchCompat.setText("Active");
                } else {
                    holder.switchCompat.setText("In-Active");
                }

            }
        });

        holder.emplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("employfnm", empData.getEmp_firstnm());
                bundle.putString("employlnm", empData.getEmp_lastmn());
                bundle.putString("employpos", empData.getEmp_jobtitle());
                bundle.putString("employpw", empData.getEmp_pass());
                bundle.putString("employphn", empData.getEmp_phoneno());
                bundle.putString("employem", empData.getEmp_email());
                bundle.putString("employdineid", empData.getEmp_dineid());
                bundle.putString("employid", empData.getEmp_jobid());
                bundle.putString("jobduty", empData.getJobduty());
                bundle.putString("emp_duties", empData.getDuties());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new SecurityBusinessAccessFragment();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame, myFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void callApi(EmployeeModel empData) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObjectEmpl = new JsonObject();
        jsonObjectEmpl.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.CHANGEEMPLOYEESTATUS);
        jsonObjectEmpl.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObjectEmpl.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        jsonObjectEmpl.addProperty("employee_id", empData.getEmp_id());
        Log.e(TAG, "Request CHANGE EMPLOYEE STATUS>> " + jsonObjectEmpl.toString());


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessSecurityLevelApi(jsonObjectEmpl);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response CHANGE EMPLOYEE STATUS>> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                    } else {
                        Toast.makeText(context, "Failed, try again", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Failed, try again", Toast.LENGTH_SHORT).show();
                    progressHD.dismiss();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout emplayout;
        CustomTextView empname, empphn, empjobtitle;
        SwitchCompat switchCompat;

        public ViewHolder(View itemView) {
            super(itemView);
            emplayout = (RelativeLayout) itemView.findViewById(R.id.emplayout);
            empname = (CustomTextView) itemView.findViewById(R.id.emp_name);
            empphn = (CustomTextView) itemView.findViewById(R.id.emp_phn);
            empjobtitle = (CustomTextView) itemView.findViewById(R.id.job_title);
            switchCompat = (SwitchCompat) itemView.findViewById(R.id.empswitch);
        }
    }

}
