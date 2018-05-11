package com.yberry.dinehawaii.vendor.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.VendorDBHandler;
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;
import com.yberry.dinehawaii.vendor.Model.VendorOrderItemsDetailsModel;

import java.util.ArrayList;


public class BidItemListAdapter extends RecyclerView.Adapter<BidItemListAdapter.ViewHolder> {
    private static final String TAG = "ManageVendorAdapter";
    Context context;
    ArrayList<VendorMasterData> vendorModelArrayList;
    private String vendor_id = "";

    public BidItemListAdapter(Context context, ArrayList<VendorMasterData> reservList, String vendor_id) {
        this.context = context;
        this.vendorModelArrayList = reservList;
        this.vendor_id = vendor_id;
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
        final VendorMasterData model = vendorModelArrayList.get(position);
        holder.itemName.setText(model.getMaster_item_name());
        holder.otherVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.llOthers.getVisibility() == View.VISIBLE)
                    holder.llOthers.setVisibility(View.GONE);
                else if (holder.llOthers.getVisibility() == View.GONE)
                    holder.llOthers.setVisibility(View.VISIBLE);
            }
        });
        if (model.getOther_vendors_list().size() == 0)
            holder.noalternative.setVisibility(View.VISIBLE);
        else {
            holder.noalternative.setVisibility(View.GONE);
            holder.recycler_view.setLayoutManager(new LinearLayoutManager(context));
            holder.recycler_view.setNestedScrollingEnabled(false);
            OtherVendorAdapter adapter = new OtherVendorAdapter(context, model.getOther_vendors_list());
            holder.recycler_view.setAdapter(adapter);
        }

        final VendorOrderItemsDetailsModel detailsModel = new VendorOrderItemsDetailsModel();
        holder.btnplacebid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsModel.setItemId(model.getMaster_item_id());
                detailsModel.setItemName(model.getMaster_item_name());
                detailsModel.setItemPrice("0");
                detailsModel.setItemTotalCost("0");
                detailsModel.setItemQuan("1");
                detailsModel.setVendorId(vendor_id);
                new VendorDBHandler(context).insertVendorOrderCartItem(detailsModel);
                new VendorDBHandler(context).insertVendorOrderCartItem(detailsModel);
                Toast.makeText(context, model.getMaster_item_name() + " Added", Toast.LENGTH_SHORT).show();
            }
        });
         holder.itemPrice.setText("$" +model.getMaster_item_price());
    }

    @Override
    public int getItemCount() {
        return vendorModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomButton btnplacebid;
        CustomTextView itemName, itemPrice, noalternative, otherVendor;
        LinearLayout llOthers;
        RecyclerView recycler_view;

        public ViewHolder(View itemView) {
            super(itemView);
            btnplacebid = (CustomButton) itemView.findViewById(R.id.btnplacebid);
            itemName = (CustomTextView) itemView.findViewById(R.id.tvName);
            itemPrice = (CustomTextView) itemView.findViewById(R.id.tvPrice);
            otherVendor = (CustomTextView) itemView.findViewById(R.id.tvOtherVendors);
            llOthers = (LinearLayout) itemView.findViewById(R.id.llotherVendors);
            noalternative = (CustomTextView) itemView.findViewById(R.id.noalternative);
            recycler_view = (RecyclerView) itemView.findViewById(R.id.listview);
        }
    }


}