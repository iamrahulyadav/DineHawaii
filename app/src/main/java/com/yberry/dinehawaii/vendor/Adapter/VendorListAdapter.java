package com.yberry.dinehawaii.vendor.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VendorListAdapter extends RecyclerView.Adapter<VendorListAdapter.ViewHolder> {
    private static final String TAG = "VendorListAdapter";
    Context context;
    ArrayList<VendorMasterData> vendorModelArrayList;

    public VendorListAdapter(Context context, ArrayList<VendorMasterData> reservList) {
        this.context = context;
        this.vendorModelArrayList = reservList;
    }

    @Override
    public VendorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_user_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VendorMasterData model = vendorModelArrayList.get(position);
        if (model.getSub_vendor_categ().equalsIgnoreCase("Delivery Vendor")) {
            holder.favBorder.setVisibility(View.VISIBLE);
            if (model.getFavorite_del_status().equalsIgnoreCase("0")) {
                holder.favBorder.setVisibility(View.VISIBLE);
                holder.favVendor.setVisibility(View.GONE);
            } else if (model.getFavorite_del_status().equalsIgnoreCase("1")) {
                holder.favBorder.setVisibility(View.GONE);
                holder.favVendor.setVisibility(View.VISIBLE);
            } else {
                holder.favBorder.setVisibility(View.VISIBLE);
                holder.favVendor.setVisibility(View.GONE);
            }
        } else
            holder.favBorder.setVisibility(View.GONE);
        holder.tvName.setText(model.getSub_vendor_fn() + " " + model.getSub_vendor_ln());
        holder.tvBusname.setText(model.getSub_vendor_busname());
        holder.tvVendorcontact.setText(model.getSub_vendor_contact());
        holder.tvtitle.setText(model.getSub_vendor_email());
        if (model.getVendor_assign_status().equalsIgnoreCase("0")) {
            holder.switchCompat.setText("Not Added");
            holder.switchCompat.setChecked(false);
        } else if (model.getVendor_assign_status().equalsIgnoreCase("1")) {
            holder.switchCompat.setChecked(true);
            holder.switchCompat.setText("Added");
        } else {
            holder.switchCompat.setText("Not Added");
            holder.switchCompat.setChecked(false);
        }
        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    addVendorApi(model);
                } else {
                    removeVendorApi(model);
                }
            }
        });
        holder.favBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFavStatus(model.getSub_vendor_id(), holder,"1");
            }
        });
        holder.favVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFavStatus(model.getSub_vendor_id(), holder,"0");
            }
        });
    }

    private void changeFavStatus(String sub_vendor_id, ViewHolder holder, String status) {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.FAVORITE_DELIVERY_VENDOR);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
            jsonObject.addProperty("vendor_id", sub_vendor_id);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
            jsonObject.addProperty("favorite_status", status);
            Log.e(TAG, "addfav request>>" + jsonObject.toString());
            final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.n_business_new_api(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "changeFavStatus: response" + response.body().toString());
                    String resp = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = resultJsonArray.getJSONObject(0);
                            notifyDataSetChanged();
                            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("updateData"));
                            Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = resultJsonArray.getJSONObject(0);
                            Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                    progressHD.dismiss();
                    Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void addVendorApi(VendorMasterData model) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_VENDOR_API.ASSIGN_VENDOR_IN_BUSINESS);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        jsonObject.addProperty("vendor_id", model.getSub_vendor_id());
        Log.e(TAG, "addVendorApi: request>>>" + jsonObject.toString());


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "addVendorApi onResponse: " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("updateData"));
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

    private void removeVendorApi(VendorMasterData model) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_VENDOR_API.REMOVE_VENDOR_IN_BUSINESS);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        jsonObject.addProperty("vendor_id", model.getSub_vendor_id());
        Log.e(TAG, "removeVendorApi: Request>>>> " + jsonObject.toString());


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "removeVendorApi: Response>>>>> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("updateData"));
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

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvName, tvBusname, tvVendorcontact, tvtitle;
        SwitchCompat switchCompat;
        ImageView favBorder, favVendor;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvBusname = (CustomTextView) itemView.findViewById(R.id.tvBusname);
            tvVendorcontact = (CustomTextView) itemView.findViewById(R.id.tvVendorcontact);
            tvtitle = (CustomTextView) itemView.findViewById(R.id.tvtitle);
            switchCompat = (SwitchCompat) itemView.findViewById(R.id.switchAdd);
            favBorder = (ImageView) itemView.findViewById(R.id.favBorder);
            favVendor = (ImageView) itemView.findViewById(R.id.favVendor);
        }
    }
}