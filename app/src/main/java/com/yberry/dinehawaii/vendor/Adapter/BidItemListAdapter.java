package com.yberry.dinehawaii.vendor.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorBidItemModel;

import java.util.ArrayList;

public class BidItemListAdapter extends RecyclerView.Adapter<BidItemListAdapter.ViewHolder> {
    private static final String TAG = "ManageVendorAdapter";
    Context context;
    ArrayList<VendorBidItemModel> vendorModelArrayList;

    public BidItemListAdapter(Context context, ArrayList<VendorBidItemModel> reservList, String vendor_id) {
        this.context = context;
        this.vendorModelArrayList = reservList;
    }

    @Override
    public BidItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_bid_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VendorBidItemModel model = vendorModelArrayList.get(position);

        holder.itemName.setText(model.getItem_name());
        holder.itemPrice.setText("$" + model.getVendor_item_price());
        holder.tvVendorName.setText("by " + model.getVendor_name());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.llOthers.getVisibility() == View.VISIBLE)
                    holder.llOthers.setVisibility(View.GONE);
                else if (holder.llOthers.getVisibility() == View.GONE)
                    holder.llOthers.setVisibility(View.VISIBLE);
            }
        });


        if (model.getOther_vendors_list().size() == 0) {
            holder.noalternative.setVisibility(View.VISIBLE);
            holder.llcolumns.setVisibility(View.GONE);
        } else {
            holder.noalternative.setVisibility(View.GONE);
            holder.llcolumns.setVisibility(View.VISIBLE);
            holder.recycler_view.setLayoutManager(new LinearLayoutManager(context));
            holder.recycler_view.setNestedScrollingEnabled(false);
            OtherVendorAdapter adapter = new OtherVendorAdapter(context, model.getOther_vendors_list());
            holder.recycler_view.setAdapter(adapter);
        }

        holder.btnplacebid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<VendorBidItemModel> tempList = model.getOther_vendors_list();

                VendorBidItemModel tempModel = new VendorBidItemModel();

                tempModel.setProduct_id(model.getProduct_id());
                tempModel.setItem_id(model.getItem_id());
                tempModel.setItem_name(model.getItem_name());
                tempModel.setVendor_item_price(model.getVendor_item_price());
                tempModel.setVendor_id(model.getVendor_id());
                tempModel.setVendor_name(model.getVendor_name());
                tempModel.setVendor_item_total_cost(model.getVendor_item_price());
                tempModel.setVendor_item_qty("1");

                if (tempList == null || tempList.isEmpty()) {
                    tempList = new ArrayList<VendorBidItemModel>();
                    tempList.add(model);
                } else {
                    tempList.set(0, tempModel);
                }
                showVendorDialog(tempList);


               /* detailsModel.setItemId(model.getMaster_item_id());
                detailsModel.setItemName(model.getMaster_item_name());
                detailsModel.setItemPrice("0");
                detailsModel.setItemTotalCost("0");
                detailsModel.setItemQuan("1");
                detailsModel.setVendorId(vendor_id);
                new VendorOrderDBHandler(context).insertVendorOrderCartItem(detailsModel);
                new VendorOrderDBHandler(context).insertVendorOrderCartItem(detailsModel);*/
                //Toast.makeText(context, model.getMaster_item_name() + " Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showVendorDialog(ArrayList<VendorBidItemModel> other_vendors_list) {
        if (vendorModelArrayList != null && !vendorModelArrayList.isEmpty()) {
            final VendorMultipleChoiceAdapter adapter = new VendorMultipleChoiceAdapter(context, other_vendors_list);
            new AlertDialog.Builder(context).setTitle("Add To Cart")
                    .setAdapter(adapter, null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
        }
    }

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView btnplacebid;
        CustomTextView itemName, tvVendorName, itemPrice, noalternative;
        LinearLayout llOthers, llcolumns;
        RecyclerView recycler_view;
        RelativeLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);
            btnplacebid = (CardView) itemView.findViewById(R.id.btnplacebid);
            itemName = (CustomTextView) itemView.findViewById(R.id.tvName);
            tvVendorName = (CustomTextView) itemView.findViewById(R.id.tvVendorName);
            itemPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
            llOthers = (LinearLayout) itemView.findViewById(R.id.llotherVendors);
            llcolumns = (LinearLayout) itemView.findViewById(R.id.llcolumns);
            noalternative = (CustomTextView) itemView.findViewById(R.id.noalternative);
            recycler_view = (RecyclerView) itemView.findViewById(R.id.listview);
        }
    }


}