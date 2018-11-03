package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yberry.dinehawaii.Bussiness.model.CouponModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomRadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewCouponActivity extends AppCompatActivity {

    private static final String TAG = "AddNewCouponActivity";
    private CustomEditText etCouponTitle, etCouponCode, etCouponDesc, etSDate, etEDate, etAmount, etMinOrderAmt, etUsage, etPercentage;
    private CustomRadioButton rbFlatAmount, rbPercentage;
    private RadioGroup rbGroup, rgSend;
    private MenuItem add;
    private MenuItem edit;
    private int coupon_type = 100;
    private TextView tvTitle;
    private CouponModel data;
    private String coupon_id = "";
    private String send_to = "all_dine_customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_coupon);
        setToolbar();
        init();

        if (getIntent().getAction().equalsIgnoreCase("edit_coupon")) {
            tvTitle.setText("Edit Coupon");
            data = (CouponModel) getIntent().getSerializableExtra("data");
            coupon_id = data.getCouponId();
            Log.e(TAG, "onCreate: data >> " + data);
            setData();
        }
    }

    private void setData() {
        etCouponTitle.setText(data.getCouponTitle());
        etCouponTitle.setEnabled(false);
        etCouponCode.setText(data.getCouponCode());
        etCouponCode.setEnabled(false);
        etCouponDesc.setText(data.getCouponDescription());
        etCouponDesc.setEnabled(false);
        etAmount.setText(data.getCouponAmtPer());
        etAmount.setEnabled(false);
        etPercentage.setText(data.getCouponAmtPer());
        etPercentage.setEnabled(false);
        etSDate.setText(data.getCouponSDate());
        etSDate.setEnabled(false);
        etEDate.setText(data.getCouponEDate());
        etEDate.setEnabled(false);
        etMinOrderAmt.setText(data.getMinOrderAmt());
        etMinOrderAmt.setEnabled(false);
        etUsage.setText(data.getUsagPerUser());
        etUsage.setEnabled(false);
        if (data.getCouponType().equalsIgnoreCase("Discount Amount"))
            rbFlatAmount.setChecked(true);
        else if (data.getCouponType().equalsIgnoreCase("Discount Percentage"))
            rbPercentage.setChecked(true);
        rbFlatAmount.setEnabled(false);
        rbPercentage.setEnabled(false);
    }

    private void init() {
        etCouponTitle = (CustomEditText) findViewById(R.id.etCouponTitle);
        etCouponCode = (CustomEditText) findViewById(R.id.etCouponCode);
        etCouponDesc = (CustomEditText) findViewById(R.id.etCouponDesc);
        etAmount = (CustomEditText) findViewById(R.id.etAmount);
        etPercentage = (CustomEditText) findViewById(R.id.etPercentage);
        rbGroup = (RadioGroup) findViewById(R.id.rbGroup);
        rgSend = (RadioGroup) findViewById(R.id.rgSend);
        rbFlatAmount = (CustomRadioButton) findViewById(R.id.rbFlatAmount);
        rbPercentage = (CustomRadioButton) findViewById(R.id.rbPercentage);
        etUsage = (CustomEditText) findViewById(R.id.etUsage);
        etMinOrderAmt = (CustomEditText) findViewById(R.id.etMinOrderAmt);
        etSDate = (CustomEditText) findViewById(R.id.etSDate);
        etEDate = (CustomEditText) findViewById(R.id.etEDate);

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbFlatAmount) {
                    etAmount.setVisibility(View.VISIBLE);
                    etPercentage.setVisibility(View.GONE);
                } else if (i == R.id.rbPercentage) {
                    etPercentage.setVisibility(View.VISIBLE);
                    etAmount.setVisibility(View.GONE);
                }
            }
        });

        etSDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                etSDate.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "startdate");
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setCancelColor(getResources().getColor(R.color.colorPrimary));
                dpd.setOkColor(getResources().getColor(R.color.colorPrimary));
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis() - 1000);
                dpd.setMinDate(c);
            }
        });

        etEDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                etEDate.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "enddate");
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setCancelColor(getResources().getColor(R.color.colorPrimary));
                dpd.setOkColor(getResources().getColor(R.color.colorPrimary));
               /* Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis() - 1000);
                dpd.setMinDate(c);*/
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_food, menu);
        add = menu.findItem(R.id.addfood);
        edit = menu.findItem(R.id.editfood);
        if (getIntent().getAction().equals("edit_coupon")) {
            add.setVisible(false);
            edit.setVisible(true);
        } else if (getIntent().getAction().equals("add_coupon")) {
            add.setVisible(true);
            edit.setVisible(false);
        } else {
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addfood:
                validateData();
                break;
            case R.id.editfood:
                setEdit();
                add.setVisible(true);
                edit.setVisible(false);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEdit() {
        etCouponTitle.setEnabled(true);
        etCouponCode.setEnabled(false);
        etCouponDesc.setEnabled(true);
        etAmount.setEnabled(true);
        etPercentage.setEnabled(true);
        etSDate.setEnabled(true);
        etEDate.setEnabled(true);
        rbFlatAmount.setEnabled(true);
        rbPercentage.setEnabled(true);
        etUsage.setEnabled(true);
        etMinOrderAmt.setEnabled(true);
    }

    private void validateData() {
        if (TextUtils.isEmpty(etCouponTitle.getText().toString())) {
            Toast.makeText(this, "Enter coupon title", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etCouponCode.getText().toString())) {
            Toast.makeText(this, "Enter coupon code", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etCouponDesc.getText().toString())) {
            Toast.makeText(this, "Enter coupon description", Toast.LENGTH_SHORT).show();
        } else if (etAmount.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etAmount.getText().toString())) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
        } else if (etPercentage.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etPercentage.getText().toString())) {
            Toast.makeText(this, "Enter percentage", Toast.LENGTH_SHORT).show();
        } else if (etUsage.getText().toString().equalsIgnoreCase("") || etUsage.getText().toString().equalsIgnoreCase("0")) {
            Toast.makeText(this, "Enter usage per user", Toast.LENGTH_SHORT).show();
        } else if (etMinOrderAmt.getText().toString().equalsIgnoreCase("") || etMinOrderAmt.getText().toString().equalsIgnoreCase("0")) {
            Toast.makeText(this, "Enter min order", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etSDate.getText().toString())) {
            Toast.makeText(this, "Select valid from date", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etEDate.getText().toString())) {
            Toast.makeText(this, "Select valid to date", Toast.LENGTH_SHORT).show();
        } else {
            if (Util.isNetworkAvailable(AddNewCouponActivity.this)) {
                if (rbGroup.getCheckedRadioButtonId() == R.id.rbFlatAmount)
                    coupon_type = 100;
                else if (rbGroup.getCheckedRadioButtonId() == R.id.rbPercentage)
                    coupon_type = 200;
                if (rgSend.getCheckedRadioButtonId() == R.id.rbDineCustomer)
                    send_to = "all_dine_customer";
                else if (rgSend.getCheckedRadioButtonId() == R.id.rbMyCustomer)
                    send_to = "my_customer";
                if (!coupon_id.equalsIgnoreCase("") || !coupon_id.isEmpty()) {
                    editCoupon();
                } else {
                    addCoupon();
                }
            } else {
                Toast.makeText(AddNewCouponActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void editCoupon() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.EDITCOUPON);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(AddNewCouponActivity.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(AddNewCouponActivity.this));
        jsonObject.addProperty("coupon_id", coupon_id);
        jsonObject.addProperty("coupon_title", etCouponTitle.getText().toString());
        jsonObject.addProperty("coupon_code", etCouponCode.getText().toString());
        jsonObject.addProperty("coupon_type", coupon_type);
        if (etAmount.getVisibility() == View.VISIBLE)
            jsonObject.addProperty("coupon_amt_per", etAmount.getText().toString());
        else if (etPercentage.getVisibility() == View.VISIBLE)
            jsonObject.addProperty("coupon_amt_per", etAmount.getText().toString());
        jsonObject.addProperty("coupon_description", etCouponDesc.getText().toString());
        jsonObject.addProperty("coupon_s_date", etSDate.getText().toString());
        jsonObject.addProperty("coupon_e_date", etEDate.getText().toString());
        jsonObject.addProperty("min_order_amt", etMinOrderAmt.getText().toString());
        jsonObject.addProperty("usage_per_user", etUsage.getText().toString());
        jsonObject.addProperty("send_to", send_to);
        Log.e(TAG, "editCoupon: Request >> " + jsonObject);

        callApi(jsonObject);
    }

    private void addCoupon() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ADDNEWCOUPON);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(AddNewCouponActivity.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(AddNewCouponActivity.this));
        jsonObject.addProperty("coupon_title", etCouponTitle.getText().toString());
        jsonObject.addProperty("coupon_code", etCouponCode.getText().toString());
        jsonObject.addProperty("coupon_type", coupon_type);
        if (etAmount.getVisibility() == View.VISIBLE)
            jsonObject.addProperty("coupon_amt_per", etAmount.getText().toString());
        else if (etPercentage.getVisibility() == View.VISIBLE)
            jsonObject.addProperty("coupon_amt_per", etPercentage.getText().toString());
        jsonObject.addProperty("coupon_description", etCouponDesc.getText().toString());
        jsonObject.addProperty("coupon_s_date", etSDate.getText().toString());
        jsonObject.addProperty("coupon_e_date", etEDate.getText().toString());
        jsonObject.addProperty("min_order_amt", etMinOrderAmt.getText().toString());
        jsonObject.addProperty("usage_per_user", etUsage.getText().toString());
        jsonObject.addProperty("send_to", send_to);
        Log.e(TAG, "addCoupon: Request >> " + jsonObject);

        callApi(jsonObject);
    }

    private void callApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(AddNewCouponActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_coupons_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();
                Log.e(TAG, "addCoupon/editCoupon Response >> " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                        Toast.makeText(AddNewCouponActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddNewCouponActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddNewCouponActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle = (TextView) findViewById(R.id.headet_text);
        tvTitle.setText("Add Coupon");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
