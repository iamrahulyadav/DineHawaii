package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Adapter.BidListItemAdapter;
import com.yberry.dinehawaii.vendor.Model.BidListItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidListActivity extends AppCompatActivity {

    private static final String TAG = "BidListActivity";
    private RecyclerView recycler_view;
    private BidListItemAdapter adapter;
    private ArrayList<BidListItemModel> list;
    private Context context;
    private CustomTextView noData;
    SwipeRefreshLayout refreshBids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_list);
        setToolbar();
        context = BidListActivity.this;
        list = new ArrayList<BidListItemModel>();
        noData = (CustomTextView) findViewById(R.id.noData);
        refreshBids = (SwipeRefreshLayout) findViewById(R.id.refreshBids);
        refreshBids.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBids.setRefreshing(false);
                getBidList();
            }
        });
        setRecyclerView();
        // setStaticData();
        getBidList();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Bid History");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void setStaticData() {
        BidListItemModel model1 = new Gson().fromJson("{\"bid_id\":\"1\",\"item\":\"item 1, item2, item 3\",\"date_time\":\"15/04/2018\",\"bid_status\":\"Unaccepted\"}", BidListItemModel.class);
        BidListItemModel model2 = new Gson().fromJson("{\"bid_id\":\"1\",\"item\":\"food_test\",\"date_time\":\"19/05/2018\",\"bid_status\":\"Unaccepted\"}", BidListItemModel.class);
        list.add(model1);
        list.add(model2);
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new BidListItemAdapter(context, list);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);

        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BidListItemModel model =list.get(position);
                Intent intent = new Intent(context,BidItemDetailsActivity.class);
                intent.putExtra("bid_id",model.getBidId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void getBidList() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.GET_BID_LIST);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        Log.e(TAG, "getBidList: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_new_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getBidList: Response >> " + resp);
                try {
                    list.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noData.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            BidListItemModel model = new BidListItemModel();
                            model.setBidId(jsonObject1.getString("bid_id"));
                            model.setBid_unique_id(jsonObject1.getString("bid_unique_id"));
                            model.setDateTime(jsonObject1.getString("date_time"));
                            model.setBidStatus(jsonObject1.getString("bid_status"));
                            list.add(model);
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        noData.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    noData.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                noData.setVisibility(View.VISIBLE);
                progressHD.dismiss();
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
