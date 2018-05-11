package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yberry.dinehawaii.Customer.Activity.CartActivity;
import com.yberry.dinehawaii.Customer.Activity.CustomerOrderDetailActivity;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.OrderHistoryItems;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hvantage2 on 6/29/2017.
 */

public class FavouriteFragmentAdapter98 extends RecyclerView.Adapter<FavouriteFragmentAdapter98.ViewHolder> {
    public static OrderFavAdapter adapter;
    Context context;
    List<String> chlidFoodStr = new ArrayList<>();
    ArrayList<OrderHistoryItems> arrayList;
    String OrderIds;
    private List<CustomerModel> repeatOrderList;

    public FavouriteFragmentAdapter98(Context context, List<CustomerModel> repeatOrderList) {
        this.context = context;
        this.repeatOrderList = repeatOrderList;
    }

    @Override
    public FavouriteFragmentAdapter98.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repeat_fav_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavouriteFragmentAdapter98.ViewHolder holder, final int position) {


        final CustomerModel model = repeatOrderList.get(position);
        Log.d("model", String.valueOf(repeatOrderList.get(position)));
        Log.d("model", model.getFood_name());
        holder.orderid.setText(model.getOrder_id());
        OrderIds = model.getId();

        final String[] chlidFood = model.getFood_name().split(",");
        final String[] chlidQuanty = model.getOrder_quantity().split(",");
        final String[] chlidprice = model.getItem_price().split(",");
        final String[] menuI_id = model.getMenu_id().split(",");
        final String[] cat_id = model.getCat_id().split(",");

        arrayList = new ArrayList<OrderHistoryItems>();
        adapter = new OrderFavAdapter(context, arrayList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        holder.reycler_customizationOrder.setLayoutManager(manager);
        holder.reycler_customizationOrder.setAdapter(adapter);
        String dataPrice;
        for (int i = 0; i < chlidFood.length; i++) {

            Log.d("modelFood", "" + chlidFood[i]);
            String dataFood = chlidFood[i];
            String dattaQuanty = chlidQuanty[i];
            if (chlidQuanty[i].equalsIgnoreCase("1/2")) {
                dataPrice = chlidprice[i];
            } else {
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
                    detailsModel.setItemPrice(chlidprice[i]);
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

                    AppPreferences.setBusiID(context, model.getBusiness_id());
                    String avgPrice = model.getAvgPrice();
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

    @Override
    public int getItemCount() {
        return repeatOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomButton viewdetail;
        CustomTextView orderid, orderdate, total_price;
        RecyclerView reycler_customizationOrder;
        CustomButton btnRepeatOrder;
        LinearLayout mainlayout, tabContent;
        View view1;
        ImageView imgArrow;

        public ViewHolder(View itemView) {
            super(itemView);

            reycler_customizationOrder = (RecyclerView) itemView.findViewById(R.id.reycler_customizationData);
            orderid = (CustomTextView) itemView.findViewById(R.id.orderid);
            orderdate = (CustomTextView) itemView.findViewById(R.id.date);
            total_price = (CustomTextView) itemView.findViewById(R.id.total_price);
            btnRepeatOrder = (CustomButton) itemView.findViewById(R.id.repeatOrder);
            viewdetail = (CustomButton) itemView.findViewById(R.id.viewOrder);
            mainlayout = (LinearLayout) itemView.findViewById(R.id.tabMain);
            tabContent = (LinearLayout) itemView.findViewById(R.id.tabContent);
            imgArrow = (ImageView) itemView.findViewById(R.id.imgArrow);
            //   view1 = (View) itemView.findViewById(R.id.view1);

        }
    }
}