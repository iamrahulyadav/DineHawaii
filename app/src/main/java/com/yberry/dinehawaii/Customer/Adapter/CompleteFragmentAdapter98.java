package com.yberry.dinehawaii.Customer.Adapter;

/**
 * Created by Hvantage2 on 6/28/2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Activity.CartActivity;
import com.yberry.dinehawaii.Customer.Activity.CustomerOrderDetailActivity;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.OrderHistoryItems;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompleteFragmentAdapter98 extends RecyclerView.Adapter<CompleteFragmentAdapter98.ViewHolder> {
    public static OrderAdapter adapter;
    Context context;
    List<String> chlidFoodStr = new ArrayList<>();
    ArrayList<OrderHistoryItems> arrayList;
    String OrderIds = "", TAG = "historyfrag";
    private List<CustomerModel> repeatOrderList;

    public CompleteFragmentAdapter98(Context context, List<CustomerModel> repeatOrderList) {
        this.context = context;
        this.repeatOrderList = repeatOrderList;
    }

    @Override
    public CompleteFragmentAdapter98.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repeat_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CompleteFragmentAdapter98.ViewHolder holder, final int position) {


        final CustomerModel model = repeatOrderList.get(position);
        holder.orderid.setText(model.getOrder_id());
        OrderIds = model.getId();

        if (model.getFav_status().equalsIgnoreCase("0")) {
            holder.fav_img.setVisibility(View.GONE);
            holder.fav_border_img.setVisibility(View.VISIBLE);
        } else if (model.getFav_status().equalsIgnoreCase("1")) {
            holder.fav_img.setVisibility(View.VISIBLE);
            holder.fav_border_img.setVisibility(View.GONE);
        } else {
            holder.fav_img.setVisibility(View.GONE);
            holder.fav_border_img.setVisibility(View.VISIBLE);
        }
        final String[] chlidFood = model.getFood_name().split(",");
        final String[] chlidQuanty = model.getOrder_quantity().split(",");
        final String[] chlidprice = model.getItem_price().split(",");
        final String[] menuI_id = model.getMenu_id().split(",");
        final String[] cat_id = model.getCat_id().split(",");

        arrayList = new ArrayList<OrderHistoryItems>();
        adapter = new OrderAdapter(context, arrayList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        holder.reycler_customizationOrder.setLayoutManager(manager);
        holder.reycler_customizationOrder.setAdapter(adapter);
        String dataPrice = "0";
        for (int i = 0; i < chlidFood.length; i++) {

            Log.d("modelFood", "" + chlidFood[i]);
            String dataFood = chlidFood[i];
            String dattaQuanty = chlidQuanty[i];

            if (chlidQuanty[i].equalsIgnoreCase("1/2")) {
                dataPrice = chlidprice[i];
            } else {
                if (!chlidprice[i].equalsIgnoreCase("") && !chlidQuanty[i].equalsIgnoreCase(""))
                    dataPrice = String.valueOf(Double.parseDouble(chlidprice[i]) * Double.parseDouble(chlidQuanty[i]));
            }
            arrayList.add(new OrderHistoryItems(dataFood, dattaQuanty, dataPrice));
        }
        adapter.notifyDataSetChanged();
        Log.d("model", "" + chlidFoodStr.size());
        holder.orderid.setText(model.getOrder_id());
        holder.orderdate.setText(model.getOrder_date());
        holder.total_price.setText(model.getTotal_price());


        holder.btnRepeatOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatabaseHandler(context).deleteCartitem();


                ArrayList<OrderItemsDetailsModel> detailsModels = new ArrayList<>();


                for (int i = 0; i < chlidFood.length; i++) {
                    OrderItemsDetailsModel detailsModel = new OrderItemsDetailsModel();
                    detailsModel.setItemName(chlidFood[i]);
                    detailsModel.setItemQuantity(chlidQuanty[i]);
                    detailsModel.setOrderType(model.getOrder());
                    detailsModel.setItemCustomiationList(model.getOrder_ItemCustomization());
                    detailsModel.setMenu_id(menuI_id[i]);
                    detailsModel.setCat_id(cat_id[i]);
                    detailsModel.setBuss_id(model.getBusiness_id());
                    if (chlidQuanty[i].equalsIgnoreCase("1/2")) {
                        detailsModel.setQuantityType("half");
                        detailsModel.setItemPrice(String.valueOf(Double.parseDouble(chlidprice[i]) * 2));
                        detailsModel.setItemTotalCost(String.valueOf(Double.parseDouble(chlidprice[i])
                        ));
                    } else {
                        detailsModel.setQuantityType("full");
                        detailsModel.setItemPrice(chlidprice[i]);
                        detailsModel.setItemTotalCost(String.valueOf(Double.parseDouble(chlidprice[i]) * Double.parseDouble(chlidQuanty[i])));
                    }
                    detailsModel.setMessage(model.getIteam_message());
                    detailsModel.setAvgPrice(model.getAvgPrice());

                    String avgPrice = model.getAvgPrice();
                    AppPreferences.setBusiID(context, model.getBusiness_id());
                    AppPreferencesBuss.setAveragePrice(context, avgPrice);


                    detailsModels.add(detailsModel);
                    new DatabaseHandler(context).insertCartlist(detailsModel);

                }
                notifyDataSetChanged();
                Intent intent = new Intent(context, CartActivity.class);
                context.startActivity(intent);
            }
        });
        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tabContent.getVisibility() == View.VISIBLE) {
                    holder.tabContent.setVisibility(View.GONE);
                    holder.imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_green_24dp);

                } else {
                    holder.tabContent.setVisibility(View.VISIBLE);
                    holder.imgArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_green_24dp);


                }
            }
        });

        holder.fav_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(context)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.REMOVEFAV);
                    jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
                    jsonObject.addProperty("order_id", model.getId());
                    Log.e(TAG, "remove fav json" + jsonObject.toString());
                    removeFavApi(jsonObject);
                } else {
                    Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.fav_border_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(context)) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.ADDTOFAV);
                    jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
                    jsonObject.addProperty("order_id", model.getId());
                    Log.e(TAG, "add to fav json" + jsonObject.toString());
                    addTOFavApi(jsonObject);
                } else {
                    Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
                }

            }

        });
        holder.viewdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail = new Intent(context, CustomerOrderDetailActivity.class);
                detail.setAction("customer");
                detail.putExtra("order_id", model.getId());
                context.startActivity(detail);
            }
        });
    }

    private void removeFavApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.wait(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "remove fav response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = resultJsonArray.getJSONObject(0);
                        notifyDataSetChanged();
                        Intent intent = new Intent("markedFav");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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
    }

    private void addTOFavApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserBusinessApi(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "add to fav response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = resultJsonArray.getJSONObject(0);
                        notifyDataSetChanged();
                        Intent intent = new Intent("markedFav");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
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
    }

    @Override
    public int getItemCount() {
        return repeatOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView orderid, orderdate, total_price;
        RecyclerView reycler_customizationOrder;
        CustomButton btnRepeatOrder, viewdetail;
        LinearLayout tabContent, mainlayout;
        View view1;
        ImageView imgArrow, fav_border_img, fav_img;

        public ViewHolder(View itemView) {
            super(itemView);

            reycler_customizationOrder = (RecyclerView) itemView.findViewById(R.id.reycler_customizationData);
            orderid = (CustomTextView) itemView.findViewById(R.id.orderid);
            orderdate = (CustomTextView) itemView.findViewById(R.id.date);
            total_price = (CustomTextView) itemView.findViewById(R.id.total_price);
            viewdetail = (CustomButton) itemView.findViewById(R.id.viewOrder);
            btnRepeatOrder = (CustomButton) itemView.findViewById(R.id.repeatOrder);
            mainlayout = (LinearLayout) itemView.findViewById(R.id.tabMain);
            tabContent = (LinearLayout) itemView.findViewById(R.id.tabContent);
            imgArrow = (ImageView) itemView.findViewById(R.id.imgArrow);
            fav_border_img = (ImageView) itemView.findViewById(R.id.favBorder);
            fav_img = (ImageView) itemView.findViewById(R.id.favorder);
            // view1 = (View) itemView.findViewById(R.id.view1);

        }


    }
}