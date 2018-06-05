package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;
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
    CustomTextView tvbidUid, tvBidStatus, tvBidDate, items, basicinfo,tvBidFreq,tvStartDate,tvEndDate,tvTermsCondt;
    LinearLayout llBasic, llitems;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private VendorBidDetailsAdapter bidadapter;
    private Context context;
    private Dialog popup;
    private AlertDialog.Builder alertdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_item_details);

        if (getIntent().hasExtra("bid_id"))
            bid_id = getIntent().getStringExtra("bid_id");
        initView();
        setToolbar();
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.isNetworkAvailable(context)) {
            modelsList.clear();
            getBidDetails();
        } else
            Toast.makeText(context, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
    }

    private void showbidDialog(final String bidRowId, final BidDetailsModel model) {
        String[] options = {"Approve Bid", "Reject Bid", "Update Bid"};
        String[] optionsId = {"0", "1", "2"};
        alertdialog = new AlertDialog.Builder(context);
        alertdialog.setTitle("Choose an Option");
        alertdialog.setCancelable(false);
        final RadioGroup group = new RadioGroup(this);
        for (int i = 0; i < options.length; i++) {
            RadioButton button = new RadioButton(context);
            button.setId(Integer.parseInt(optionsId[i]));
            button.setText(options[i]);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 0);
            params.setMarginStart(50);
            button.setLayoutParams(params);
            group.addView(button);
        }
        if (model.getVendorBidFinalAmount().equalsIgnoreCase("")|| model.getVendorBidFinalAmount().equalsIgnoreCase("0"))
            group.removeViewAt(0);
       if (model.getBidStatus().equalsIgnoreCase("Completed") || model.getBidStatus().equalsIgnoreCase("Approved")) {
           group.removeViewAt(0);
       }

        alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "onClick: " + group.getCheckedRadioButtonId());
                int groupId = group.getCheckedRadioButtonId();
                Log.e(TAG, "onClick: groupId" + groupId);
                if (groupId == 0) {
                    if (Util.isNetworkAvailable(context)) {
                        acceptVendorBidApi(bidRowId);
                        dialogInterface.cancel();
                    } else
                        Toast.makeText(context, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
                } else if (groupId == 1) {
                    if (Util.isNetworkAvailable(context)) {
                        rejectVendorBidApi(bidRowId);
                        dialogInterface.cancel();
                    } else
                        Toast.makeText(context, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
                } else if (groupId == 2) {
                    dialogInterface.cancel();
                    showUpdateBidDialog(model);
                }
            }
        });

        alertdialog.setView(group);
        alertdialog.show();

    }

    private void rejectVendorBidApi(String bidRowId) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.REJECTBID);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("bid_row_id", bidRowId);
        Log.e(TAG, "rejectVendorBidApi: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendorOrderUrl(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "rejectVendorBidApi: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject objresult = jsonArray.getJSONObject(0);
                        Toast.makeText(context, objresult.getString("msg"), Toast.LENGTH_SHORT).show();
                        getBidDetails();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject objresult = jsonArray.getJSONObject(0);
                        Toast.makeText(context, objresult.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "rejectVendorBidApi error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptVendorBidApi(String bidRowId) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.APPROVEBID);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("bid_row_id", bidRowId);
        Log.e(TAG, "acceptVendorBidApi: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendorOrderUrl(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "acceptVendorBidApi: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject objresult = jsonArray.getJSONObject(0);
                        Toast.makeText(context, objresult.getString("msg"), Toast.LENGTH_SHORT).show();
                        getBidDetails();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject objresult = jsonArray.getJSONObject(0);
                        Toast.makeText(context, objresult.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "acceptVendorBidApi error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateBidDialog(final BidDetailsModel bidModel) {
        Log.e(TAG, "showUpdateBidDialog: bidModel" + bidModel.toString());
        popup = new Dialog(context);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.update_bid_dialog);
        final CustomTextView tvItemName = (CustomTextView) popup.findViewById(R.id.tvItemName);
        final CustomTextView tvItemPrice = (CustomTextView) popup.findViewById(R.id.tvItemPrice);
        final CustomTextView tvItemVendorPrice = (CustomTextView) popup.findViewById(R.id.tvItemVendorPrice);
        final CustomTextView tvItemTotalPrice = (CustomTextView) popup.findViewById(R.id.tvItemTotalPrice);
        final CustomEditText etYourPrice = (CustomEditText) popup.findViewById(R.id.etYourPrice);
        final CustomEditText etQuantity = (CustomEditText) popup.findViewById(R.id.etQuantity);


        tvItemName.setText(bidModel.getItemName());
        tvItemPrice.setText(bidModel.getItemAmount());
        tvItemVendorPrice.setText(bidModel.getVendorBidAmount());

        if (!bidModel.getVendorBidFinalAmount().equalsIgnoreCase("") && !bidModel.getVendorBidFinalAmount().equalsIgnoreCase("0"))
            etYourPrice.setText(bidModel.getVendorBidFinalAmount());

        tvItemTotalPrice.setText(bidModel.getBusinessBidAmt());
        etQuantity.setText(bidModel.getItemQuantity());
        Log.e(TAG, "showUpdateBidDialog: quantity>>>>>" + bidModel.getItemQuantity());
        popup.findViewById(R.id.popupclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        popup.findViewById(R.id.btnsubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etYourPrice.getText().toString()))
                    Toast.makeText(context, "Enter Price", Toast.LENGTH_SHORT).show();
                else if (etYourPrice.getText().toString().equalsIgnoreCase(bidModel.getVendorBidFinalAmount()))
                    Toast.makeText(context, "Price is same as vendor price you can approve the bid", Toast.LENGTH_LONG).show();
                else {
                    if (Util.isNetworkAvailable(context)) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.BID_UPDATE);
                        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
                        jsonObject.addProperty("bid_row_id", bidModel.getBidRowId());
                        jsonObject.addProperty("bid_amount", etYourPrice.getText().toString());
                        jsonObject.addProperty("item_quantity", etQuantity.getText().toString());
                        jsonObject.addProperty("vendor_bid_amount", tvItemVendorPrice.getText().toString());
                        Log.e(TAG, "UpdateBid: Request >> " + jsonObject);
                        updateBidApi(jsonObject);
                    } else
                        Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!etQuantity.getText().toString().equalsIgnoreCase("") && !etQuantity.getText().toString().equalsIgnoreCase("0")) {
                    String qty = etQuantity.getText().toString();
                    String price = bidModel.getItemAmount();
                    int itemTotal = Integer.parseInt(qty) * Integer.parseInt(price);
                    Log.e(TAG, "onTextChanged: itemTotal >> " + itemTotal);
                    Log.e(TAG, "onTextChanged: itemTotal qty>> " + qty + "<<<<<price>>>>" + price);
                    tvItemVendorPrice.setText(String.valueOf(itemTotal));

                } else
                    tvItemVendorPrice.setText(bidModel.getVendorBidAmount());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        popup.show();
    }

    private void updateBidApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendorOrderUrl(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "UpdateBid: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    popup.dismiss();
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject objresult = jsonArray.getJSONObject(0);
                        Toast.makeText(context, objresult.getString("msg"), Toast.LENGTH_SHORT).show();
                        getBidDetails();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject objresult = jsonArray.getJSONObject(0);
                        Toast.makeText(context, objresult.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "acceptVendorBidApi error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void setAdapter() {
        bidadapter = new VendorBidDetailsAdapter(context, modelsList);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(bidadapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BidDetailsModel detailsModel = modelsList.get(position);
                if (!detailsModel.getBidStatus().equalsIgnoreCase("Rejected"))
                    showbidDialog(detailsModel.getBidRowId(), modelsList.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void initView() {
        context = this;
        modelsList = new ArrayList<>();
        tvBidDate = (CustomTextView) findViewById(R.id.tvDateTime);
        tvBidFreq = (CustomTextView) findViewById(R.id.tvBidFreq);
        tvStartDate = (CustomTextView) findViewById(R.id.tvStartDate);
        tvTermsCondt = (CustomTextView) findViewById(R.id.tvTermsCondt);
        tvEndDate = (CustomTextView) findViewById(R.id.tvEndDate);
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
                        tvBidFreq.setText(object.getString("bid_frequency"));
                        tvStartDate.setText(object.getString("start_date"));
                        tvEndDate.setText(object.getString("end_date"));
                        tvTermsCondt.setText(object.getString("terms_conditions"));
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
