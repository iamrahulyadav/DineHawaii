package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.ItemAdapter;
import com.yberry.dinehawaii.Bussiness.model.OrderDetailItemData;
import com.yberry.dinehawaii.Bussiness.model.OrderDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressBarAnimation;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.yberry.dinehawaii.Util.Util.context;

public class OrderDetailActivty extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OrderDetailActivty";
    public ItemAdapter itemAdapter;
    LinearLayout llBasic, llItems, llDelivery, llloyalty, llOthers, lleamt, llecode, llccode, llcamt;
    ArrayList<OrderDetailItemData> itemList;
    View view;
    ArrayList<VendorModel> vendorList;
    private ImageView back;
    private RecyclerView mrecycler;
    private CustomTextView tvOrderId, tvDateTime, tvOrderStatus, tvOrderType, tvCustomerName, tvContactNo, tvDeliveryName,
            tvDeliveryAddress, tvPickupName, tvPickUpTime, tvTotalAmount, tvloyaltypt, tvegiftamt, tvcouponamt, tvegiftcode, tvcouponcode;
    private CardView cardTakeout, cardDelivery;
    private FloatingActionButton fabPending, fabInProgress, fabCompleted, fabDelPick, fabPrepared;
    private ProgressBar orderProgress;
    private String order_id, status = "", order_type = "", new_status = "";
    private CustomTextView tvFabText;
    private String selectedVendorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_activty);
        setToolbar();
        init();
        setAdapter();

        order_id = getIntent().getStringExtra("order_id");
        Log.e(TAG, "onCreate: order_id >> " + order_id);
        new getOrderDetails().execute();
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
        tvDateTime = (CustomTextView) findViewById(R.id.tvDateTime);
        tvOrderStatus = (CustomTextView) findViewById(R.id.tvOrderStatus);
        tvOrderType = (CustomTextView) findViewById(R.id.tvOrderType);
        tvCustomerName = (CustomTextView) findViewById(R.id.tvCustomerName);
        tvContactNo = (CustomTextView) findViewById(R.id.tvContactNo);
        tvDeliveryName = (CustomTextView) findViewById(R.id.tvDeliveryName);
        tvDeliveryAddress = (CustomTextView) findViewById(R.id.tvDeliveryAddress);
        tvPickupName = (CustomTextView) findViewById(R.id.tvPickupName);
        tvPickUpTime = (CustomTextView) findViewById(R.id.tvPickUpTime);
        tvTotalAmount = (CustomTextView) findViewById(R.id.tvTotalAmount);
        tvFabText = (CustomTextView) findViewById(R.id.tvFabText);

        llBasic = (LinearLayout) findViewById(R.id.llBasic);
        llItems = (LinearLayout) findViewById(R.id.llItems);
        llDelivery = (LinearLayout) findViewById(R.id.llDelivery);
        llOthers = (LinearLayout) findViewById(R.id.llOthers);
        llcamt = (LinearLayout) findViewById(R.id.llcouponamt);
        llccode = (LinearLayout) findViewById(R.id.llcoupon);
        lleamt = (LinearLayout) findViewById(R.id.llegiftamt);
        llecode = (LinearLayout) findViewById(R.id.llegift);
        llloyalty = (LinearLayout) findViewById(R.id.llloyaltypt);
        cardTakeout = (CardView) findViewById(R.id.cardTakeout);
        cardDelivery = (CardView) findViewById(R.id.cardDelivery);

        ((CustomTextView) findViewById(R.id.basicinfo)).setOnClickListener(OrderDetailActivty.this);
        ((CustomTextView) findViewById(R.id.items)).setOnClickListener(OrderDetailActivty.this);
        ((CustomTextView) findViewById(R.id.delivery)).setOnClickListener(OrderDetailActivty.this);
        ((CustomTextView) findViewById(R.id.others)).setOnClickListener(OrderDetailActivty.this);

        orderProgress = (ProgressBar) findViewById(R.id.orderProgress);

        fabPending = (FloatingActionButton) findViewById(R.id.fabPending);
        fabInProgress = (FloatingActionButton) findViewById(R.id.fabInProgress);
        fabPrepared = (FloatingActionButton) findViewById(R.id.fabPrepared);
        fabCompleted = (FloatingActionButton) findViewById(R.id.fabCompleted);
        fabDelPick = (FloatingActionButton) findViewById(R.id.fabDelPick);

        fabPending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabInProgress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
        fabPrepared.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
        fabCompleted.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
        fabDelPick.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));

        orderProgress.setProgress(0);

        if (!getIntent().getAction().equalsIgnoreCase("customer")) {
            fabPending.setOnClickListener(this);
            fabInProgress.setOnClickListener(this);
            fabPrepared.setOnClickListener(this);
            fabCompleted.setOnClickListener(this);
            fabDelPick.setOnClickListener(this);
        }
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivty.this);
        builder.setTitle("CHANGE ORDER STATUS");
        builder.setMessage("ALERT : If you change the order status, a notification will be sent to customer. This can't be undone. Do you want to change status?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new CompleteOrderTask().execute();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void setAdapter() {
        itemList = new ArrayList<OrderDetailItemData>();
        mrecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mrecycler.setLayoutManager(new LinearLayoutManager(OrderDetailActivty.this));
        mrecycler.setHasFixedSize(true);
        itemAdapter = new ItemAdapter(context, itemList);
        mrecycler.setAdapter(itemAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.basicinfo) {
            basicChildInfo();
        } else if (view.getId() == R.id.items) {
            itemChildinfo();
        } else if (view.getId() == R.id.delivery) {
            deliveryChildInfo();
        } else if (view.getId() == R.id.others) {
            otherChildInfo();
        } else if (view.getId() == R.id.fabInProgress) {
            new_status = "In-Progress";
            showDialog();
        } else if (view.getId() == R.id.fabPrepared) {
            //new_status = "Prepared";
            //showDialog();
            if (order_type.equalsIgnoreCase("delivery"))
                showDeliveryVendor();
            else if (order_type.equalsIgnoreCase("pickup")) {
                new_status = "Prepared";
                showDialog();
            }
        } else if (view.getId() == R.id.fabDelPick) {
            Log.e(TAG, "onClick: order_type >> " + order_type);
            if (order_type.equalsIgnoreCase("delivery"))
                new_status = "Delivered";
            else if (order_type.equalsIgnoreCase("pickup"))
                new_status = "Picked-up";
            showDialog();
        } else if (view.getId() == R.id.fabCompleted) {
            new_status = "Completed";
            showDialog();
        }
    }

    private void showDeliveryVendor() {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(OrderDetailActivty.this);
        dialog.setTitle("Select Delivery Vendor");
        final RadioGroup group = new RadioGroup(this);
        for (int i = 0; i < vendorList.size(); i++) {
            RadioButton button = new RadioButton(OrderDetailActivty.this);
            button.setId(Integer.parseInt(vendorList.get(i).getVendorId()));
            button.setText(vendorList.get(i).getVendorBusName());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 0);
            button.setLayoutParams(params);
            group.addView(button);
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                Log.e(TAG, "onClick: vendorText" + radioButton.getText().toString());

            }
        });
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "onClick: " + group.getCheckedRadioButtonId());
                selectedVendorId = String.valueOf(group.getCheckedRadioButtonId());
            }
        });

        dialog.setView(group);
        dialog.show();


    }

    private void setCompleted() {
        ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(orderProgress, 700);
        mProgressAnimation.setProgress(400);
        fabPending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabInProgress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabPrepared.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabDelPick.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabCompleted.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
    }

    private void setDeliveredPicked() {
        ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(orderProgress, 700);
        mProgressAnimation.setProgress(300);
        fabPending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabInProgress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabPrepared.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabDelPick.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabCompleted.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
    }

    private void setPrepared() {
        ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(orderProgress, 700);
        mProgressAnimation.setProgress(200);
        fabPending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabInProgress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabPrepared.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabDelPick.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
        fabCompleted.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
    }

    private void setInProgress() {
        ProgressBarAnimation mProgressAnimation = new ProgressBarAnimation(orderProgress, 700);
        mProgressAnimation.setProgress(100);
        fabPending.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabInProgress.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        fabPrepared.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
        fabDelPick.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
        fabCompleted.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab_gray)));
    }


    private void basicChildInfo() {
        if (llBasic.getVisibility() == View.VISIBLE) {
            llBasic.setVisibility(View.GONE);
        } else {
            llBasic.setVisibility(View.VISIBLE);
        }
    }

    private void itemChildinfo() {
        if (llItems.getVisibility() == View.VISIBLE) {
            llItems.setVisibility(View.GONE);
        } else {
            llItems.setVisibility(View.VISIBLE);
        }
    }

    private void deliveryChildInfo() {
        if (llDelivery.getVisibility() == View.VISIBLE) {
            llDelivery.setVisibility(View.GONE);
        } else {
            llDelivery.setVisibility(View.VISIBLE);
        }
    }

    private void otherChildInfo() {
        if (llOthers.getVisibility() == View.VISIBLE) {
            llOthers.setVisibility(View.GONE);
        } else {
            llOthers.setVisibility(View.VISIBLE);
        }
    }

    class getOrderDetails extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            itemList.clear();
            progressHD = ProgressHUD.show(OrderDetailActivty.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.ORDER_DETAIL);
            jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferencesBuss.getUserId(OrderDetailActivty.this)); //7
            jsonObject.addProperty("order_id", order_id);

            Log.e(TAG, "Request GET ALL ORDERDETAIL" + jsonObject.toString());

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(AppConstants.BASEURL.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);

            Call<JsonObject> call = apiService.today_orders(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Request GET ORDER DETAIL >>  " + call.request().toString());
                    Log.e(TAG, "Response GET ORDER DETAIL >> " + response.body().toString());
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("200")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject result = jsonArray.get(i).getAsJsonObject();
                            Log.e("listItemresult", String.valueOf(result));
                            OrderDetails listItem = new Gson().fromJson(result, OrderDetails.class);

                            if (listItem.getOrder_status().equalsIgnoreCase("Pending")) {
                                fabPending.setEnabled(false);
                            } else if (listItem.getOrder_status().equalsIgnoreCase("In-Progress")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(false);
                                setInProgress();
                            } else if (listItem.getOrder_status().equalsIgnoreCase("Prepared")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(false);
                                fabPrepared.setEnabled(false);
                                setPrepared();
                            } else if (listItem.getOrder_status().equalsIgnoreCase("Delivered") || listItem.getOrder_status().equalsIgnoreCase("Picked-up")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(false);
                                fabPrepared.setEnabled(false);
                                fabDelPick.setEnabled(false);
                                setDeliveredPicked();
                            } else if (listItem.getOrder_status().equalsIgnoreCase("Completed")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(false);
                                fabPrepared.setEnabled(false);
                                fabDelPick.setEnabled(false);
                                fabCompleted.setEnabled(false);
                                setCompleted();
                            }
                            if (listItem.getLoyalty_points().equalsIgnoreCase(""))
                                llloyalty.setVisibility(View.GONE);
                            else {
                                llloyalty.setVisibility(View.VISIBLE);
                                view.setVisibility(View.VISIBLE);
                                tvloyaltypt.setText(listItem.getLoyalty_points());
                            }
                            if (listItem.getE_gift_code().equalsIgnoreCase("")) {
                                llecode.setVisibility(View.GONE);
                                lleamt.setVisibility(View.GONE);
                            } else {
                                view.setVisibility(View.VISIBLE);
                                llecode.setVisibility(View.VISIBLE);
                                lleamt.setVisibility(View.VISIBLE);
                                tvegiftamt.setText(listItem.getE_gift_amount());
                                tvegiftcode.setText(listItem.getE_gift_code());
                            }
                            if (listItem.getCoupon_code().equalsIgnoreCase("")) {
                                llcamt.setVisibility(View.GONE);
                                llccode.setVisibility(View.GONE);
                            } else {
                                view.setVisibility(View.VISIBLE);
                                llcamt.setVisibility(View.VISIBLE);
                                llccode.setVisibility(View.VISIBLE);
                                tvcouponcode.setText(listItem.getCoupon_code());
                                tvcouponamt.setText(listItem.getCoupon_amount());
                            }

                            switch (listItem.getOrder_type()) {
                                case "In-House":
                                    order_type = "delivery";
                                    tvFabText.setText("Delivered");
                                case "Home Delivery":
                                    order_type = "delivery";
                                    tvFabText.setText("Delivered");
                                    cardDelivery.setVisibility(View.VISIBLE);
                                    break;
                                case "Take-Out":
                                    tvFabText.setText("Picked-up");
                                    order_type = "pickup";
                                    cardTakeout.setVisibility(View.VISIBLE);
                                    break;
                                case "Catering":
                                    order_type = "pickup";
                                    tvFabText.setText("Picked-up");
                                    cardTakeout.setVisibility(View.VISIBLE);
                                    break;
                            }

                            //basic info
                            tvOrderId.setText(listItem.getOrder_id());
                            tvDateTime.setText(listItem.getDate());
                            tvCustomerName.setText(listItem.getUser_name());
                            tvOrderType.setText(listItem.getOrder_type());
                            tvOrderStatus.setText(listItem.getOrder_status());
                            //delivery info
                            tvDeliveryName.setText(listItem.getDelivery_name());
                            tvDeliveryAddress.setText(listItem.getDelivery_adderess());
                            tvContactNo.setText(listItem.getDelivery_contact_no());

                            //takeout
                            tvPickupName.setText(listItem.getUser_name());
                            tvPickUpTime.setText(listItem.getDue_time());

                            tvTotalAmount.setText("$" + listItem.getTotal_price());

                            JsonArray jsonArrayItem = jsonObject.getAsJsonArray("menu_detail");
                            for (int j = 0; j < jsonArrayItem.size(); j++) {
                                Gson gson = new Gson();
                                JsonObject itemDetail = jsonArrayItem.get(j).getAsJsonObject();
                                OrderDetailItemData model = gson.fromJson(itemDetail, OrderDetailItemData.class);
                                Log.e(TAG, i + ", itemDetail: >> " + model);
                                itemList.add(model);
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        JsonObject result = jsonObject.getAsJsonObject("result");
                        publishProgress(400, result.get("msg").getAsString());
                    }
                    publishProgress(200, "");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("Response: request 1", call.request().toString());
                    Log.e("Response: onFailure 1", t.toString());
                    Toast.makeText(OrderDetailActivty.this, "Server not responding .... please try later", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }

        private void publishProgress(int i, String msg) {
            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            if (i == 400) {
                progressHD.dismiss();
                Toast.makeText(OrderDetailActivty.this, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }


    class CompleteOrderTask extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHD = ProgressHUD.show(OrderDetailActivty.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.UPDATE_ORDER);
            jsonObject.addProperty("order_id", order_id);
            jsonObject.addProperty("order_status", new_status);
            jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferencesBuss.getUserId(OrderDetailActivty.this));
            Log.e(TAG, "Request UPDATE ORDER >> " + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.today_orders(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response UPDATE ORDER >> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            publishProgress(200);
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            publishProgress(400);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        publishProgress(400);
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, " Error :- " + Log.getStackTraceString(t));
                    publishProgress(400);
                }
            });
            return null;
        }

        private void publishProgress(int status) {
            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            if (status == 200) {
                Toast.makeText(OrderDetailActivty.this, "Order Updated", Toast.LENGTH_SHORT).show();
                new getOrderDetails().execute();
            } else if (status == 400) {
                Toast.makeText(OrderDetailActivty.this, "Failed, try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
