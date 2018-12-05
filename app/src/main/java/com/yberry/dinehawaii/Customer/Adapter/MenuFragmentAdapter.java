package com.yberry.dinehawaii.Customer.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.yberry.dinehawaii.Model.MenuCart;
import com.yberry.dinehawaii.Model.MenuDetail;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.Model.SelectedCustomizationModel;
import com.yberry.dinehawaii.MyApplication;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;
import com.yberry.dinehawaii.interfacese.SelectedChbkInterface;

import java.util.ArrayList;
import java.util.Arrays;

import static com.yberry.dinehawaii.Customer.Activity.PlaceAnOrder.itemsDetailsModels;

public class MenuFragmentAdapter extends RecyclerView.Adapter<MenuFragmentAdapter.MyViewHolder> {

    private static final String TAG = "Adapter_Chineseee";
    MenuCart menuCart = new MenuCart();
    Dialog dialog;
    OnItemClickListener itemClickListener;
    MyApplication appControler;
    ProgressHUD progressHD;
    String cat_id, menu_id;
    String quant_type = "";
    ArrayList<SelectedCustomizationModel> getCheck = new ArrayList<>();
    SelectedChbkInterface chbkInterface;
    ArrayList<OrderItemsDetailsModel> models = new ArrayList<OrderItemsDetailsModel>();
    private Context mContext;
    private ArrayList<MenuDetail> arrayList;
    private OrderItemsDetailsModel orderesItem;
    private String stateChbk = "";

    public MenuFragmentAdapter(Context mContext, ArrayList<MenuDetail> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        appControler = (MyApplication) mContext.getApplicationContext();
    }

    @Override
    public int getItemCount() {
        int size = arrayList.size();
        return size;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder myViewHolder;
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chines_fragment, viewGroup, false);

        progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {
        //final MenuDetail menuDetail =   arrayList.get(position);
        if (arrayList.isEmpty()) {
            myViewHolder.llMain.setVisibility(View.GONE);
            myViewHolder.ViewText_noRecord.setVisibility(View.VISIBLE);
        } else {
            orderesItem = new OrderItemsDetailsModel();
            final MenuDetail menuDetail = arrayList.get(position);
            cat_id = menuDetail.getItem_cat_id();
            menu_id = menuDetail.getItemId();

            Log.e(TAG + "<<<<< MENU ID >>>>>", menu_id);
            Log.e(TAG + "<<<<< CART ID >>>>>", cat_id);
            progressHD.dismiss();
            if (menuDetail.getItem_price().length() == 0) {
                myViewHolder.llMain.setVisibility(View.GONE);
                myViewHolder.ViewText_noRecord.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.llMain.setVisibility(View.VISIBLE);
                myViewHolder.tv_ItemName.setText(menuDetail.getItemName());
                myViewHolder.tv_ItemPrice.setText(menuDetail.getItem_price());
                myViewHolder.tvServiceType.setText("For : " + menuDetail.getService_type());
            }
            myViewHolder.btn_AddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialog(menuDetail, mContext, myViewHolder.tv_ItemName.getText().toString(), myViewHolder.tv_ItemPrice.getText().toString());
                    dialog.show();
                }
            });

            myViewHolder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialog(menuDetail, mContext, myViewHolder.tv_ItemName.getText().toString(), myViewHolder.tv_ItemPrice.getText().toString());
                    dialog.show();
                    /*if (myViewHolder.other_info.getVisibility() == View.VISIBLE) {
                        myViewHolder.other_info.setVisibility(View.GONE);
                    } else
                        myViewHolder.other_info.setVisibility(View.VISIBLE);*/
                }
            });
        }
    }

    @SuppressLint("LongLogTag")
    private void customDialog(final MenuDetail menuDetail, final Context context, final String itemName, final String itemPrice) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_customization);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView cancel = ((ImageView) dialog.findViewById(R.id.btnCancel));
        ImageView minusImageView = ((ImageView) dialog.findViewById(R.id.minusImageView));
        ImageView plusImageView = ((ImageView) dialog.findViewById(R.id.plusImageView));
        final CustomEditText enterMessage = (CustomEditText) dialog.findViewById(R.id.enterMessage);
        RecyclerView recyler_customization = (RecyclerView) dialog.findViewById(R.id.recyler_customization);
        RecyclerView.LayoutManager manager = new GridLayoutManager(context, 2);
        recyler_customization.setLayoutManager(manager);

        final CustomTextView tv_ItemName, tv_ItemPrice, tv_totalCost, tv_itemQuantity, tv_half_price, text_price, item_details;
        RadioButton halfRadio, fullRadio;
        final LinearLayout halfLayout, quant_linear;
        item_details = (CustomTextView) dialog.findViewById(R.id.itemdetails);
        tv_ItemName = (CustomTextView) dialog.findViewById(R.id.tv_ItemName);
        tv_ItemPrice = (CustomTextView) dialog.findViewById(R.id.tv_ItemPrice);
        tv_totalCost = (CustomTextView) dialog.findViewById(R.id.tv_totalCost);
        tv_itemQuantity = (CustomTextView) dialog.findViewById(R.id.textView_quantity);
        tv_half_price = (CustomTextView) dialog.findViewById(R.id.halfPriceCust);
        text_price = (CustomTextView) dialog.findViewById(R.id.tv_Price);
        halfLayout = (LinearLayout) dialog.findViewById(R.id.linearHalf);
        quant_linear = (LinearLayout) dialog.findViewById(R.id.linear2);
        halfRadio = (RadioButton) dialog.findViewById(R.id.radioHalf);
        fullRadio = (RadioButton) dialog.findViewById(R.id.radioFull);

        if (menuDetail.getDetails().equalsIgnoreCase("")) {
            item_details.setText("No Details Available");
        } else {
            item_details.setText(menuDetail.getDetails());
        }
        halfRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    quant_type = "half";
                    quant_linear.setVisibility(View.GONE);
                    tv_totalCost.setText(menuDetail.getItem_half_price());
                } else {
                    quant_type = "full";
                    quant_linear.setVisibility(View.VISIBLE);
                    tv_totalCost.setText(itemPrice);
                }
            }
        });
        if (fullRadio.isChecked()) {
            quant_type = "full";
            tv_totalCost.setText(itemPrice);
        }
        tv_ItemName.setText(itemName);
        tv_ItemPrice.setText(itemPrice);
        if (!menuDetail.getItem_half_price().equalsIgnoreCase("0.0") && menuDetail.getItem_half_price().length() != 0) {
            halfLayout.setVisibility(View.VISIBLE);
            tv_half_price.setText(menuDetail.getItem_half_price());
        } else {
            text_price.setText("Price - $ ");
            halfLayout.setVisibility(View.GONE);
            fullRadio.setVisibility(View.GONE);
            halfRadio.setVisibility(View.GONE);
        }
        CustomButton tvdone = (CustomButton) dialog.findViewById(R.id.done);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_itemQuantity.setText("" + 1);
        double itemPrice1 = Double.parseDouble(tv_ItemPrice.getText().toString());
        double totalCost = itemPrice1 * 1;
        tv_totalCost.setText(" " + totalCost);

        plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityS = tv_itemQuantity.getText().toString();
                Log.v(TAG, "Quantity ;;;- " + quantityS);
                int quantity = 1;
                try {
                    quantity = Integer.parseInt(quantityS);
                    Log.v(TAG, "Quantity before Adding :- " + quantity);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                quantity = quantity + 1;
                tv_itemQuantity.setText("" + quantity);
                double itemPrice = Double.parseDouble(tv_ItemPrice.getText().toString());
                double totalCost = itemPrice * quantity;
                tv_totalCost.setText(" " + totalCost);
            }
        });

        minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(tv_itemQuantity.getText().toString());
                if (quantity == 1) {
                    Toast.makeText(mContext, "Quantity can't be less than 1", Toast.LENGTH_SHORT).show();
                } else {
                    quantity = quantity - 1;
                    tv_itemQuantity.setText("" + quantity);
                }
                double itemPrice = Double.parseDouble(tv_ItemPrice.getText().toString());
                double totalCost = itemPrice * quantity;
                tv_totalCost.setText(" " + totalCost);
            }
        });
        CustomTextView noCustomizations = (CustomTextView) dialog.findViewById(R.id.noCustomizations);
        getCheck.clear();
        if (!menuDetail.getCustomizations().equalsIgnoreCase("") && !menuDetail.getCustomizations().equalsIgnoreCase("null")) {
            noCustomizations.setVisibility(View.GONE);
            ArrayList<String> cust_items = new ArrayList<>(Arrays.asList(menuDetail.getCustomizations().split("\\s*,\\s*")));
            Log.e(TAG, "customDialog: cust_items >>>" + cust_items);
            for (int i = 0; i < cust_items.size(); i++) {
                SelectedCustomizationModel model = new SelectedCustomizationModel();
                model.setId(String.valueOf(i));
                model.setName(cust_items.get(i));
                getCheck.add(model);
            }
            chbkInterface = new SelectedChbkInterface() {
                @Override
                public void setOnSelectedItems(String cbhk) {
                    stateChbk = cbhk;
                    notifyDataSetChanged();
                }
            };
            SelectedCustamizationAdapter selectedCustamizationAdapter = new SelectedCustamizationAdapter(mContext, getCheck, chbkInterface);
            recyler_customization.setAdapter(selectedCustamizationAdapter);
            selectedCustamizationAdapter.notifyDataSetChanged();
        } else
            noCustomizations.setVisibility(View.VISIBLE);

        tvdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enter_mesage = enterMessage.getText().toString().trim();
                AppPreferences.setCustomizationMsg(context, enter_mesage);
                Log.d(TAG + "Bussiness Id", menuDetail.getItem_bus_id());
                orderesItem.setItemCustomiationList(stateChbk);
                Log.e(TAG, "onClick: stateChbk>>>>>>>>" + stateChbk);
                orderesItem.setMenu_id(menuDetail.getItemId());
                orderesItem.setCat_id(menuDetail.getItem_cat_id());
                orderesItem.setBuss_id(menuDetail.getItem_bus_id());
                orderesItem.setItemName(tv_ItemName.getText().toString());
                if (quant_type.equalsIgnoreCase("half")) {
                    orderesItem.setItemPrice(tv_half_price.getText().toString());
                    orderesItem.setItemQuantity("1/2");
                } else if (quant_type.equalsIgnoreCase("full")) {
                    orderesItem.setItemPrice(tv_ItemPrice.getText().toString());
                    orderesItem.setItemQuantity(tv_itemQuantity.getText().toString());
                }
                orderesItem.setItemTotalCost(tv_totalCost.getText().toString());
                orderesItem.setMessage(enterMessage.getText().toString());
                orderesItem.setOrderType("New Order");
                orderesItem.setQuantityType(quant_type);
                Log.v(TAG, "Items Custom :- " + menuDetail.getItem_bus_id() + ">>>" + orderesItem.getBuss_id() + "Selection" + stateChbk);
                itemsDetailsModels.add(orderesItem);
                new DatabaseHandler(context).insertCartlist(orderesItem);
                menuCart.setProducts(orderesItem);
                appControler.setProducts(orderesItem);
                Toast.makeText(mContext.getApplicationContext(), orderesItem.getItemName() + " added to cart", Toast.LENGTH_SHORT).show();
                models.add(orderesItem);
                notifyDataSetChanged();
                Intent intent = new Intent("Add_to_cart");
                intent.putExtra("array_model", itemsDetailsModels);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("update_cart_count"));

                AppPreferences.setCatId(mContext, menuDetail.getItem_cat_id());
                AppPreferences.setMenuId(mContext, menuDetail.getItemId());

                AppPreferences.setQty(mContext, tv_itemQuantity.getText().toString());
                AppPreferences.setPrice(mContext, tv_totalCost.getText().toString());
                AppPreferences.setBusiID(mContext, menuDetail.getItem_bus_id());

                for (OrderItemsDetailsModel itemsDetailsModel : itemsDetailsModels) {
                    Log.v(TAG, "Item Name :- " + itemsDetailsModel.getBuss_id() + "Item cost :- " + itemsDetailsModel.getItemName() + "Item cost :- " + itemsDetailsModel.getItemTotalCost());
                }

                dialog.dismiss();
            }
        });
    }

    public ArrayList<OrderItemsDetailsModel> getCartItems() {
        Log.d("itemsDetailsModels 111", itemsDetailsModels.size() + "");
        return itemsDetailsModels;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(OrderItemsDetailsModel listItem);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public ImageView btn_AddItem;
        LinearLayout llMain, halfpLinear;
        //        RelativeLayout other_info;
        CustomTextView tv_ItemName, tv_ItemPrice, ViewText_noRecord, tvServiceType, tvhalfprice;

        public MyViewHolder(View convertView) {
            super(convertView);
            llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
            halfpLinear = (LinearLayout) convertView.findViewById(R.id.halfLinear);
            btn_AddItem = (ImageView) convertView.findViewById(R.id.btn_AddItem);
            tv_ItemName = (CustomTextView) convertView.findViewById(R.id.tvPizza);
            tv_ItemPrice = (CustomTextView) convertView.findViewById(R.id.tvPizzaPrice);
            tvServiceType = (CustomTextView) convertView.findViewById(R.id.tvServiceType);
            ViewText_noRecord = (CustomTextView) convertView.findViewById(R.id.ViewText_noRecord);
            tvhalfprice = (CustomTextView) convertView.findViewById(R.id.tvhalfprice);
//            other_info = (RelativeLayout) convertView.findViewById(R.id.other_info);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(itemsDetailsModels.get(getAdapterPosition()));
        }
    }
}