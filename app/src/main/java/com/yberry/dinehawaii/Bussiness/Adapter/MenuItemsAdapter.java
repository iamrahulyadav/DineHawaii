package com.yberry.dinehawaii.Bussiness.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.EditMenuItemActivity;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<OrderItemsDetailsModel> menudetils;
    String TAG = "MenuItemsAdapter";
    private String delete_item_id = "";

    public MenuItemsAdapter(Context context, ArrayList<OrderItemsDetailsModel> details) {
        this.context = context;
        this.menudetils = details;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_menu_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderItemsDetailsModel model = menudetils.get(position);
        String alottedDuty = AppPreferencesBuss.getAllottedDuties(context);
        if (!TextUtils.isEmpty(alottedDuty)) {
            if (alottedDuty.contains("4") && alottedDuty.contains("5")) {
                holder.itemSoldOut.setVisibility(View.VISIBLE);
            } else if (alottedDuty.contains("4")) {
                holder.itemSoldOut.setVisibility(View.GONE);
            } else {
                holder.itemSoldOut.setVisibility(View.VISIBLE);
            }

        }
        if (model.getItemName() == null)
            holder.name.setText("NA");
        else
            holder.name.setText(model.getItemName());

        if (model.getHalf_price().equalsIgnoreCase(""))
            holder.halfprice.setText("00");
        else
            holder.halfprice.setText(model.getHalf_price());

        if (model.getItemPrice().equalsIgnoreCase(""))
            holder.price.setText("00");
        else
            holder.price.setText(model.getItemPrice());

        if (model.getMenuItemStatus().equalsIgnoreCase("0")) {
            holder.itemSoldOut.setText("Sold Out");
            holder.itemSoldOut.setChecked(true);
        } else if (model.getMenuItemStatus().equalsIgnoreCase("1")) {
            holder.itemSoldOut.setText("Make Sold Out");
            holder.itemSoldOut.setChecked(false);
        } else {
            holder.itemSoldOut.setText("Make Sold Out");
            holder.itemSoldOut.setChecked(false);
        }


        holder.itemSoldOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getMenuItemStatus().equalsIgnoreCase("0")) {
                    holder.itemSoldOut.setChecked(true);
                    soldOutData("1", model.getItemEditId(), holder.itemSoldOut, position);
                } else if (model.getMenuItemStatus().equalsIgnoreCase("1")) {
                    holder.itemSoldOut.setChecked(false);
                    soldOutData("0", model.getItemEditId(), holder.itemSoldOut, position);
                } else {
                    holder.itemSoldOut.setChecked(false);
                }
            }
        });
        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditMenuItemActivity.class);
                intent.setAction("edit_menu");
                intent.putExtra("item_name", model.getItemName());
                intent.putExtra("item_price", model.getItemPrice());
                intent.putExtra("half_price", model.getHalf_price());
                intent.putExtra("item_cat", model.getItem_category());
                intent.putExtra("details", model.getDetails());
                intent.putExtra("item_image", model.getItemImage());
                intent.putExtra("service_type", model.getServiceType());
                intent.putExtra("edit_id", model.getItemEditId());
                intent.putExtra("foodtype_id", model.getFood_type_id());
                intent.putExtra("service_id", model.getService_type_id());
                intent.putExtra("area_id", model.getBusAreaId());
                intent.putExtra("area_name", model.getBusAreaName());
                context.startActivity(intent);
            }
        });

        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_item_id = model.getCat_id();
                Log.e(TAG, "model" + model.getCat_id());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Are you sure?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFoodMenuItem(delete_item_id);
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

    }

    private void soldOutData(String soldvalue, String itemEditId, final Switch itemSoldOut, final int position) {
        if (Util.isNetworkAvailable(context)) {
            final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.SOLDMENUITEM);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
            jsonObject.addProperty("menu_id", itemEditId);
            jsonObject.addProperty("menu_status", soldvalue);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
            Log.e(TAG, "solditem json" + jsonObject.toString());


            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "solditem response" + response.body().toString());
                    String resp = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            notifyDataSetChanged();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = jsonArray.getJSONObject(0);
                            if (menudetils.get(position).getMenuItemStatus().equalsIgnoreCase("0")) {
                                menudetils.get(position).setMenuItemStatus("1");
                            } else if (menudetils.get(position).getMenuItemStatus().equalsIgnoreCase("1")) {
                                menudetils.get(position).setMenuItemStatus("0");
                            } else {
                                menudetils.get(position).setMenuItemStatus("0");
                            }
                            Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            menudetils.get(position).setMenuItemStatus("0");
                            itemSoldOut.setChecked(false);
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject object = jsonArray.getJSONObject(0);
                            Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            menudetils.get(position).setMenuItemStatus("0");
                            itemSoldOut.setChecked(false);
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        menudetils.get(position).setMenuItemStatus("0");
                        itemSoldOut.setChecked(false);
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                    menudetils.get(position).setMenuItemStatus("0");
                    itemSoldOut.setChecked(false);
                    progressHD.dismiss();
                    Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }


    private void deleteFoodMenuItem(String delete_item_id) {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.KEY_DELETE_FOOD_ITEM);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
            jsonObject.addProperty("food_type_id", delete_item_id);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
            Log.e(TAG, "delete food item json" + jsonObject.toString());
            deleteFoodMenuItemApi(jsonObject);
        } else {
            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteFoodMenuItemApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.addeditmenu(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "delete food item response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        notifyDataSetChanged();
                        Intent intent = new Intent("menuItemRefresh");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public int getItemCount() {
        return menudetils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemlayout;
        CustomTextView halfprice, name, price;
        ImageView delete_item;
        Switch itemSoldOut;

        public ViewHolder(View itemView) {
            super(itemView);
            itemlayout = (RelativeLayout) itemView.findViewById(R.id.itemsMenu);
            halfprice = (CustomTextView) itemView.findViewById(R.id.item_halfprice);
            name = (CustomTextView) itemView.findViewById(R.id.item_name);
            price = (CustomTextView) itemView.findViewById(R.id.item_price);
            delete_item = (ImageView) itemView.findViewById(R.id.deleteFood);
            itemSoldOut = (Switch) itemView.findViewById(R.id.switchSoldOut);
        }
    }
}
