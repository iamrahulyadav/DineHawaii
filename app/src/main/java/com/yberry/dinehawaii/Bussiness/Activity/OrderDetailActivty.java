package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.yberry.dinehawaii.common.activity.OrderMapActivity;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Model.VendorModel;

import org.json.JSONArray;
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


public class OrderDetailActivty extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OrderDetailActivty";
    public ItemAdapter itemAdapter;
    LinearLayout llVendor, llBasic, llItems, llDelivery, llloyalty, llOthers, lleamt, llecode, llccode, llcamt;
    ArrayList<OrderDetailItemData> itemList = new ArrayList<OrderDetailItemData>();
    View view;
    ArrayList<VendorModel> vendorList;
    private ImageView back;
    private RecyclerView mrecycler;
    private CustomTextView tvOrderId, tvDateTime, tvOrderStatus, tvOrderType, tvCustomerName, tvContactNo, tvDeliveryName,
            tvDeliveryAddress, tvPickupName, tvPickUpTime, tvTotalAmount, tvloyaltypt, tvegiftamt, tvcouponamt, tvegiftcode, tvcouponcode;
    private CardView cardTakeout, cardDelivery, cardVendor;
    private FloatingActionButton fabPending, fabInProgress, fabCompleted, fabDelPick, fabPrepared;
    private ProgressBar orderProgress;
    private String order_id, status = "", order_type = "", new_status = "";
    private CustomTextView tvFabText, tvVendorContact, tvVendorBusiness, tvVendorName, tvDriverName, tvDriverCall;
    private String selectedVendorId = "", orderAssignStatus = "", vendorAssignStatus = "", driverAssignStatus = "";
    private Context context;
    private CustomTextView tvWalletAmt;
    private CustomButton btnTrackOrder;
    private OrderDetails orderData = null;
    private CardView cardDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_activty);
        setToolbar();
        context = this;
        vendorList = new ArrayList<VendorModel>();
        init();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel) {
            new AlertDialog.Builder(context)
                    .setMessage("Do you want to cancel this order?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new CancelOrder().execute();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        view = (View) findViewById(R.id.view_total);
        tvegiftamt = (CustomTextView) findViewById(R.id.tvegiftamt);
        tvegiftcode = (CustomTextView) findViewById(R.id.tvegiftcode);
        tvloyaltypt = (CustomTextView) findViewById(R.id.tvLoyaltyPoints);
        tvcouponamt = (CustomTextView) findViewById(R.id.tvcouponamt);
        tvcouponcode = (CustomTextView) findViewById(R.id.tvcouponcode);
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
        tvVendorBusiness = (CustomTextView) findViewById(R.id.tvVendorBusiness);
        tvVendorName = (CustomTextView) findViewById(R.id.tvVendorName);
        tvDriverName = (CustomTextView) findViewById(R.id.tvDriverName);
        tvDriverCall = (CustomTextView) findViewById(R.id.tvDriverCall);
        tvVendorContact = (CustomTextView) findViewById(R.id.tvVendorContact);
        tvWalletAmt = (CustomTextView) findViewById(R.id.tvWalletAmt);

        llVendor = (LinearLayout) findViewById(R.id.llVendor);
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
        cardVendor = (CardView) findViewById(R.id.cardVendor);
        cardDriver = (CardView) findViewById(R.id.cardDriver);
        btnTrackOrder = findViewById(R.id.btnTrackOrder);
        cardVendor.setVisibility(View.GONE);
        cardDriver.setVisibility(View.GONE);
        btnTrackOrder.setVisibility(View.GONE);


        btnTrackOrder.setOnClickListener(this);
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

        fabPending.setOnClickListener(this);
        fabInProgress.setOnClickListener(this);
        fabPrepared.setOnClickListener(this);
        fabCompleted.setOnClickListener(this);
        fabDelPick.setOnClickListener(this);
    }

    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivty.this);
        builder.setTitle("CHANGE ORDER STATUS");
        builder.setCancelable(false);
        builder.setMessage("ALERT : If you change the order status, a notification will be sent to customer. This can't be undone. Do you want to change status?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (order_type.equalsIgnoreCase("delivery") && status.equalsIgnoreCase("Pending")) {
                    getDeliveryVendors();
                } else
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
        mrecycler = (RecyclerView) findViewById(R.id.recycler_view);
        mrecycler.setLayoutManager(new LinearLayoutManager(OrderDetailActivty.this));
        mrecycler.setHasFixedSize(true);
        itemAdapter = new ItemAdapter(context, itemList, order_type);
        mrecycler.setAdapter(itemAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnTrackOrder) {
            Intent intent = new Intent(context, OrderMapActivity.class);
            intent.putExtra("cust_lat", orderData.getCustLat());
            intent.putExtra("cust_long", orderData.getCustLong());
            intent.putExtra("cust_address", orderData.getCustAddress());
            intent.putExtra("busi_lat", orderData.getBusLat());
            intent.putExtra("busi_long", orderData.getBusLong());
            intent.putExtra("busi_address", orderData.getBusAddress());
            intent.putExtra("order_id", orderData.getId());
            startActivity(intent);
        } else if (view.getId() == R.id.basicinfo) {
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
            new_status = "Prepared";
            showDialog();
         /*   if (order_type.equalsIgnoreCase("delivery")) {
                getDeliveryVendors();

            } else if (order_type.equalsIgnoreCase("pickup")) {
                new_status = "Prepared";
                showDialog();
            }*/
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

    private void showDeliveryVendor(final ArrayList<VendorModel> vendorList) {
        Log.e(TAG, "showDeliveryVendor: vendorList.size() >> " + this.vendorList.size());
       /* vendorList = new ArrayList<VendorModel>();
        VendorModel data1 = new VendorModel();
        data1.setVendorId("1");
        data1.setVendorBusName("Ashu Delivery Vendor");
        data1.setVendorName("Ashutosh khare");
        vendorList.add(data1);*/

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(OrderDetailActivty.this);
        dialog.setTitle("Select Delivery Vendor");
        dialog.setCancelable(false);
        final RadioGroup group = new RadioGroup(this);
        for (int i = 0; i < this.vendorList.size(); i++) {

            if (this.vendorList.get(i).getVendorId() != null) {
                RadioButton button = new RadioButton(OrderDetailActivty.this);
                button.setId(Integer.parseInt(this.vendorList.get(i).getVendorId()));
                button.setText(this.vendorList.get(i).getBusinessName());
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(80, 10, 0, 0);
                button.setLayoutParams(params);
                group.addView(button);
            }
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                Log.e(TAG, "onClick: vendorText" + radioButton.getText().toString());

            }
        });
        dialog.setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "onClick: " + group.getCheckedRadioButtonId());
                String vendorId = String.valueOf(group.getCheckedRadioButtonId());
                assignDelivery(vendorId);
                new CompleteOrderTask().execute();

            }
        });
        dialog.setNegativeButton("Request to all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<String> items = new ArrayList<>();
                for (int j = 0; j < vendorList.size(); j++) {
                    items.add(vendorList.get(j).getVendorId());
                }
                Log.e(TAG, "onClick: all>>" + items.toString());
                String separatedList = items.toString().replace("[", "").replace("]", "");
//                String separatedList = TextUtils.join(",", items);
                Log.e(TAG, "onClick: separatedList" + separatedList);
                assignDelivery(separatedList);
                new CompleteOrderTask().execute();
            }
        });
        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.setView(group);
        dialog.show();


    }

    private void assignDelivery(String vendorId) {
        final ProgressHUD progressHD = ProgressHUD.show(OrderDetailActivty.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.ASSIGN_ORDER_DELIVERY);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(OrderDetailActivty.this));
        jsonObject.addProperty("order_id", order_id);
        jsonObject.addProperty("vendor_id", vendorId);
        Log.e(TAG, "assignDelivery: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "assignDelivery: Response >> " + resp);
                try {
                    vendorList.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        new getOrderDetails().execute();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(OrderDetailActivty.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                    progressHD.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "server not responding", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void getDeliveryVendors() {
        final ProgressHUD progressHD = ProgressHUD.show(OrderDetailActivty.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.ALL_DELIVERY_VENDORS);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(OrderDetailActivty.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(OrderDetailActivty.this));
        Log.e(TAG, "getDeliveryVendors: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getDeliveryVendors: Response >> " + resp);
                try {
                    vendorList.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (progressHD != null) progressHD.dismiss();
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Gson gson = new Gson();
                            VendorModel model = gson.fromJson(String.valueOf(jsonObject1), VendorModel.class);
                            vendorList.add(model);
                        }
                        if (orderAssignStatus.equalsIgnoreCase("1"))
                            showDeliveryVendor(vendorList);
                        else {
                            ArrayList<String> items = new ArrayList<>();
                            for (int j = 0; j < vendorList.size(); j++) {
                                items.add(vendorList.get(j).getVendorId());
                            }
                            Log.e(TAG, "getDeliveryVendors: allvendorIds>>" + items.toString());
                            String separatedList = TextUtils.join(",", items);
                            Log.e(TAG, "getDeliveryVendors: separatedList" + separatedList);
                            assignDelivery(separatedList);
                            new CompleteOrderTask().execute();
                        }
                    } else {
                        Toast.makeText(OrderDetailActivty.this, "No delivery vendors available", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressHD != null) progressHD.dismiss();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (progressHD != null) progressHD.dismiss();
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
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
            jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferencesBuss.getUserId(OrderDetailActivty.this));
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
                    Log.e(TAG, "Response GET ORDER DETAIL >> " + response.body().toString());
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("200")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject result = jsonArray.get(i).getAsJsonObject();
                            Log.e("listItemresult", String.valueOf(result));
                            orderData = new Gson().fromJson(result, OrderDetails.class);
                            Log.e(TAG, "onResponse: orderData >> " + orderData);
                            status = orderData.getOrderStatus();
                            if (orderData.getOrderStatus().equalsIgnoreCase("Pending")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(true);
                                fabPrepared.setEnabled(false);
                                fabDelPick.setEnabled(false);
                                fabCompleted.setEnabled(false);
                            } else if (orderData.getOrderStatus().equalsIgnoreCase("In-Progress")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(false);
                                fabPrepared.setEnabled(true);
                                fabDelPick.setEnabled(true);
                                fabCompleted.setEnabled(true);
                                setInProgress();
                            } else if (orderData.getOrderStatus().equalsIgnoreCase("Prepared")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(false);
                                fabPrepared.setEnabled(true);
                                fabDelPick.setEnabled(true);
                                fabCompleted.setEnabled(true);
                                setPrepared();
                            } else if (orderData.getOrderStatus().equalsIgnoreCase("Delivered") || orderData.getOrderStatus().equalsIgnoreCase("Picked-up")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(false);
                                fabPrepared.setEnabled(false);
                                fabDelPick.setEnabled(false);
                                setDeliveredPicked();
                            } else if (orderData.getOrderStatus().equalsIgnoreCase("Completed")) {
                                fabPending.setEnabled(false);
                                fabInProgress.setEnabled(false);
                                fabPrepared.setEnabled(false);
                                fabDelPick.setEnabled(false);
                                fabCompleted.setEnabled(false);
                                setCompleted();
                            }
                            if (orderData.getLoyaltyPoints().equalsIgnoreCase("") || orderData.getLoyaltyPoints().equalsIgnoreCase("0"))
                                llloyalty.setVisibility(View.GONE);
                            else {
                                llloyalty.setVisibility(View.VISIBLE);
                                view.setVisibility(View.VISIBLE);
                                tvloyaltypt.setText(orderData.getLoyaltyPoints());
                            }
                            if (orderData.getEGiftCode().equalsIgnoreCase("") || orderData.getEGiftCode().equalsIgnoreCase("0")) {
                                llecode.setVisibility(View.GONE);
                                lleamt.setVisibility(View.GONE);
                            } else {
                                view.setVisibility(View.VISIBLE);
                                llecode.setVisibility(View.VISIBLE);
                                lleamt.setVisibility(View.VISIBLE);
                                tvegiftamt.setText(orderData.getEGiftAmount());
                                tvegiftcode.setText(orderData.getEGiftCode());
                            }
                            if (orderData.getCouponCode().equalsIgnoreCase("") || orderData.getCouponCode().equalsIgnoreCase("0")) {
                                llcamt.setVisibility(View.GONE);
                                llccode.setVisibility(View.GONE);
                            } else {
                                view.setVisibility(View.VISIBLE);
                                llcamt.setVisibility(View.VISIBLE);
                                llccode.setVisibility(View.VISIBLE);
                                tvcouponcode.setText(orderData.getCouponCode());
                                tvcouponamt.setText(orderData.getCouponAmount());
                            }

                            switch (orderData.getOrderType()) {
                                case "In-House":
                                    order_type = "inhouse";
                                    tvFabText.setText("Delivered");
                                    cardDelivery.setVisibility(View.VISIBLE);
                                    break;
                                case "Home Delivery":
                                    order_type = "delivery";
                                    tvFabText.setText("Delivered");
                                    cardDelivery.setVisibility(View.VISIBLE);
                                    vendorAssignStatus = orderData.getVenderAssignStatus();
                                    driverAssignStatus = orderData.getDriverAssignStatus();
                                    if (!orderData.getOrderStatus().equalsIgnoreCase("In-Progress") && !orderData.getOrderStatus().equalsIgnoreCase("Pending") && !orderData.getOrderStatus().equalsIgnoreCase("Picked-up")) {
                                        cardVendor.setVisibility(View.VISIBLE);
                                        tvVendorBusiness.setText(orderData.getVendorBusinessName());
                                        tvVendorName.setText(orderData.getVendorName());
                                        tvVendorContact.setText(orderData.getVendorContactNo());
                                    } else
                                        cardVendor.setVisibility(View.GONE);

                                    if (vendorAssignStatus.equalsIgnoreCase("0") || vendorAssignStatus.equalsIgnoreCase(""))
                                        cardVendor.setVisibility(View.GONE);
                                    else {
                                        cardVendor.setVisibility(View.VISIBLE);
                                        tvVendorBusiness.setText(orderData.getVendorBusinessName());
                                        tvVendorContact.setText(orderData.getVendorContactNo());
                                        tvVendorName.setText(orderData.getVendorBusinessName());
                                    }
                                    if (driverAssignStatus.equalsIgnoreCase("0") || driverAssignStatus.equalsIgnoreCase("")) {
                                        cardDriver.setVisibility(View.GONE);
                                        btnTrackOrder.setVisibility(View.GONE);
                                    } else {
                                        btnTrackOrder.setVisibility(View.VISIBLE);
                                        cardDriver.setVisibility(View.VISIBLE);
                                        tvDriverName.setText(orderData.getDriverName());
                                        tvDriverCall.setText(orderData.getDriverContactNo());
                                    }


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
                            tvOrderId.setText(orderData.getOrderId());
                            tvDateTime.setText(orderData.getDate());
                            tvCustomerName.setText(orderData.getUserName());
                            tvOrderType.setText(orderData.getOrderType());
                            tvOrderStatus.setText(orderData.getOrderStatus());
                            //delivery info
                            tvDeliveryName.setText(orderData.getDeliveryName());
                            tvDeliveryAddress.setText(orderData.getDeliveryAdderess());
                            tvContactNo.setText(orderData.getDeliveryContactNo());
                            orderAssignStatus = orderData.getOrderAssignStatus();

                            //takeout
                            tvPickupName.setText(orderData.getUserName());
                            tvPickUpTime.setText(orderData.getDueTime());
                            tvWalletAmt.setText(orderData.getWalletAmt());

                            tvTotalAmount.setText("$" + orderData.getTotalPrice());

                            try {
                                JsonArray jsonArrayItem = jsonObject.getAsJsonArray("menu_detail");
                                for (int j = 0; j < jsonArrayItem.size(); j++) {
                                    Gson gson = new Gson();
                                    JsonObject itemDetail = jsonArrayItem.get(j).getAsJsonObject();
                                    OrderDetailItemData model = gson.fromJson(itemDetail, OrderDetailItemData.class);
                                    Log.e(TAG, i + ", itemDetail: >> " + model);
                                    itemList.add(model);
                                }
                                setAdapter();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        JsonObject result = jsonObject.getAsJsonObject("result");
                        publishProgress(400, result.get("msg").getAsString());
                    }
                    publishProgress(200, "");
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressHD.dismiss();
                    Log.e("Response: request 1", call.request().toString());
                    Log.e("Response: onFailure 1", t.toString());
                    publishProgress(400, "Server not responding...");

                }
            });
            return null;
        }

        private void publishProgress(int i, String msg) {
            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            if (i == 400) {
                new AlertDialog.Builder(OrderDetailActivty.this)
                        .setMessage(msg)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new getOrderDetails().execute();
                            }
                        }).setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
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

    class CancelOrder extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHD = ProgressHUD.show(OrderDetailActivty.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.CANCELORDER);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(OrderDetailActivty.this));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(OrderDetailActivty.this));
            jsonObject.addProperty("order_id", order_id);


            Log.e(TAG, "CancelOrder: Request >> " + jsonObject.toString());

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
                    Log.e(TAG, "CancelOrder: Response >> " + response.body().toString());
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("200")) {
                        publishProgress(200, "Cancelled");
                    } else {
                        JsonObject result = jsonObject.getAsJsonObject("result");
                        publishProgress(400, result.get("msg").getAsString());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressHD.dismiss();
                    Log.e("Response: onFailure 1", t.toString());
                    publishProgress(400, "Server not responding...");

                }
            });
            return null;
        }

        private void publishProgress(int i, String msg) {
            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            if (i == 400) {
                new AlertDialog.Builder(OrderDetailActivty.this)
                        .setMessage(msg)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new getOrderDetails().execute();
                            }
                        }).setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        }
    }
}
