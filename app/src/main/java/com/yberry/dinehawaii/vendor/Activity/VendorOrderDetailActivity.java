package com.yberry.dinehawaii.vendor.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Adapter.VendorItemAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorOrderDetailItem;
import com.yberry.dinehawaii.vendor.Model.VendorOrderDetails;

import java.util.List;


public class VendorOrderDetailActivity extends AppCompatActivity {
    private static final String TAG = "VendorOrderDetail";
    public VendorItemAdapter itemAdapter;
    List<VendorOrderDetailItem> itemList;
    View view;
    private ImageView back;
    private RecyclerView mrecycler;
    private LinearLayoutManager mLayoutManager;
    private CustomTextView tvOrderId, tvDateTime, tvDeliveryDate, tvOrderStatus, tvOrderType, tvVendorBusiness, tvVendorContact;
    private CustomTextView tvRemark, tvRepeat, tvSubtotal, tvDelChargeAmount, tvGETaxAmount, tvTotalAmount;
    private VendorOrderDetailActivity context;
    private VendorOrderDetails data;
    private LinearLayout llRepeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_order_detail_activty);
        context = this;
        setToolbar();
        init();
        data = (VendorOrderDetails) getIntent().getParcelableExtra("data");
        Log.e(TAG, "onCreate: data >> " + data);
        if (data != null) {
            tvOrderId.setText(data.getOrderUniqueId());
            tvDateTime.setText(data.getDateTime());//ordered date
            tvDeliveryDate.setText(data.getDeliveryDate());//ordered date
            tvOrderStatus.setText("-");
            tvOrderType.setText(data.getOrderType());
            tvSubtotal.setText("$" + data.getSubTotal() + "");
            tvDelChargeAmount.setText("$" + data.getDeliveryFee() + "");
            tvGETaxAmount.setText("$" + data.getGeTax() + "");
            tvTotalAmount.setText("$" + data.getTotalAmount() + "");
            tvVendorContact.setText(data.getVendorContactNo());
            tvVendorBusiness.setText(data.getVendorName());
            tvRemark.setText(data.getRemark());
            itemList = data.getOrderDetails();
            Log.e(TAG, "onCreate: itemList >> " + itemList.size());
            if (data.getOrderType().equalsIgnoreCase("repeat")) {
                tvOrderType.setText("Repeating Order");
                tvRepeat.setText(data.getOrderFrequency());
                llRepeats.setVisibility(View.VISIBLE);
            } else {
                tvOrderType.setText("One Time Order");
                llRepeats.setVisibility(View.GONE);
            }
            if (itemList != null)
                setAdapter();
        }
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Order Details");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        tvOrderId = (CustomTextView) findViewById(R.id.tvOrderId);
        tvRemark = (CustomTextView) findViewById(R.id.tvRemark);
        tvRepeat = (CustomTextView) findViewById(R.id.tvRepeat);
        tvSubtotal = (CustomTextView) findViewById(R.id.tvSubtotal);
        tvDelChargeAmount = (CustomTextView) findViewById(R.id.tvDelChargeAmount);
        tvGETaxAmount = (CustomTextView) findViewById(R.id.tvGETaxAmount);
        tvTotalAmount = (CustomTextView) findViewById(R.id.tvTotalAmount);
        tvDateTime = (CustomTextView) findViewById(R.id.tvDateTime);
        tvDeliveryDate = (CustomTextView) findViewById(R.id.tvDeliveryDate);
        tvOrderStatus = (CustomTextView) findViewById(R.id.tvOrderStatus);
        tvOrderType = (CustomTextView) findViewById(R.id.tvOrderType);
        view = (View) findViewById(R.id.view_total);

        tvVendorBusiness = (CustomTextView) findViewById(R.id.tvVendorBusiness);
        tvVendorContact = (CustomTextView) findViewById(R.id.tvVendorContact);
        llRepeats = findViewById(R.id.llRepeats);
    }

    private void setAdapter() {
        mrecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(VendorOrderDetailActivity.this);
        mrecycler.setLayoutManager(mLayoutManager);
        mrecycler.setHasFixedSize(true);
        itemAdapter = new VendorItemAdapter(context, itemList);
        mrecycler.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();
    }
}
