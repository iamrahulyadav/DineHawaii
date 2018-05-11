package com.yberry.dinehawaii.vendor.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomEditText;

public class DeliveryVendorDetailActivity extends AppCompatActivity implements View.OnClickListener {
    CustomEditText etDeliveryArea, etDriverArrivalTime, etFlatamt, etPercent;
    LinearLayout llRange;
    RadioGroup rgDeliveryCost, rgDriverTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_vendor_detail);
        setToolbar();
        initViews();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Delivery Details");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        etDeliveryArea = (CustomEditText) findViewById(R.id.etdeliveryArea);
        etDriverArrivalTime = (CustomEditText) findViewById(R.id.etArrivalTime);
        etFlatamt = (CustomEditText) findViewById(R.id.etFlatAmount);
        etPercent = (CustomEditText) findViewById(R.id.etPercentage);
        llRange = (LinearLayout) findViewById(R.id.llRange);

        rgDeliveryCost = (RadioGroup) findViewById(R.id.rgrpCost);
        rgDriverTip = (RadioGroup) findViewById(R.id.rgrpTip);
        setRadioGrpListners();
    }

    private void setRadioGrpListners() {
        rgDeliveryCost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdFlatAmount) {
                    etFlatamt.setVisibility(View.VISIBLE);
                    etPercent.setVisibility(View.GONE);
                    llRange.setVisibility(View.GONE);
                } else if (checkedId == R.id.rdPercent) {
                    etPercent.setVisibility(View.VISIBLE);
                    etFlatamt.setVisibility(View.GONE);
                    llRange.setVisibility(View.GONE);
                } else if (checkedId == R.id.rdRange) {
                    llRange.setVisibility(View.VISIBLE);
                    etPercent.setVisibility(View.GONE);
                    etFlatamt.setVisibility(View.GONE);
                }
            }
        });

        rgDriverTip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
              /*  if (checkedId==R.id.tipYes)

                else if (checkedId==R.id.tipNo)*/

            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
