package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Adapter.VendorBidDetailsAdapter;
import com.yberry.dinehawaii.vendor.Model.BidDetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    String bid_id = "";
    ArrayList<BidDetailsModel> modelsList;
    String TAG = "BidItemDetailsActivity";
    CustomTextView tvbidUid, tvBidStatus, tvBidDate, items, basicinfo;
    LinearLayout llBasic, llitems;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private VendorBidDetailsAdapter bidadapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_item_details);

        if (getIntent().hasExtra("bid_id"))
            bid_id = getIntent().getStringExtra("bid_id");
        initView();
        setToolbar();

        if (Util.isNetworkAvailable(context))
            getBidDetails();
        else
            Toast.makeText(context, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();

        setCartAdapter();
    }


    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Bid Details");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void basicChildInfo() {
        if (llBasic.getVisibility() == View.VISIBLE) {
            llBasic.setVisibility(View.GONE);
        } else {
            llBasic.setVisibility(View.VISIBLE);
        }
    }


    private void itemChildinfo() {
        if (llitems.getVisibility() == View.VISIBLE) {
            llitems.setVisibility(View.GONE);
        } else {
            llitems.setVisibility(View.VISIBLE);
        }
    }

    private void setCartAdapter() {
        bidadapter = new VendorBidDetailsAdapter(context, modelsList);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(bidadapter);
    }

    private void initView() {
        context = this;
        modelsList = new ArrayList<>();
        tvBidDate = (CustomTextView) findViewById(R.id.tvDateTime);
        tvBidStatus = (CustomTextView) findViewById(R.id.tvBidStatus);
        llBasic = (LinearLayout) findViewById(R.id.llBasic);
        llitems = (LinearLayout) findViewById(R.id.llitems);
        basicinfo = (CustomTextView) findViewById(R.id.basicinfo);
        items = (CustomTextView) findViewById(R.id.items);
        tvbidUid = (CustomTextView) findViewById(R.id.tvBidId);

        basicinfo.setOnClickListener(this);
        items.setOnClickListener(this);
    }


    private void getBidDetails() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.GETBIDDETAILS);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("bid_id", bid_id);
        Log.e(TAG, "getBidDetails: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.n_business_new_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getBidDetails: Response >> " + resp);
                try {
                    modelsList.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONArray jsonArraymain = jsonObject.getJSONArray("result_main_bid");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            BidDetailsModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), BidDetailsModel.class);
                            modelsList.add(model);
                        }
                        JSONObject object = jsonArraymain.getJSONObject(0);
                        tvBidDate.setText(object.getString("date_time"));
                        tvbidUid.setText(object.getString("bid_unique_id"));
                        tvBidStatus.setText(object.getString("bid_status"));
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        modelsList.clear();
                        Toast.makeText(context, "no record found", Toast.LENGTH_SHORT).show();
                    }
                    bidadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "getBidDetails error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.basicinfo:
                basicChildInfo();
                break;
            case R.id.items:
                itemChildinfo();
                break;
            default:
                break;
        }
    }
}
