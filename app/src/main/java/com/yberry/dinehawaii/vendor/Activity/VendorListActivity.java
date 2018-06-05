package com.yberry.dinehawaii.vendor.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Adapter.SelectVendorTypeAdapter;
import com.yberry.dinehawaii.vendor.Adapter.VendorListAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "VendorListActivity";
    Context context;
    ArrayList<VendorMasterData> masterlist;
    ArrayList<VendorMasterData> sublist;
    String category_id;
    CustomButton addvendorbtn;
    CustomTextView noData;
    private RecyclerView recycler_view;
    private VendorListAdapter adapter;
    private FloatingActionMenu floatingActionMenu;
    private RecyclerView mRecyclerView;
    private String selectedVendorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_list_by_cat);
        setToolbar();
        if (getIntent().hasExtra("category_id"))
            category_id = getIntent().getStringExtra("category_id");


        masterlist = new ArrayList<VendorMasterData>();
        sublist = new ArrayList<VendorMasterData>();

        init();
        setAdapter();
        setFloatingControls();

//        getAllVendorsList();

    }

    private void setFloatingControls() {
        new FloatingButton().showFloatingButton(context);
        new FloatingButton().setFloatingButtonControls(context);
    }

    private void getAllVendorsList() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.GETVENDORCAT);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        jsonObject.addProperty("category_id", category_id);
        Log.e(TAG, "getAllVendorsFood: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendors_list_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "onResponse: Response >> " + resp);
                try {
                    sublist.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noData.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            VendorMasterData model = new VendorMasterData();
                            model.setSub_vendor_id(jsonObject1.getString("user_id"));
                            model.setSub_vendor_email(jsonObject1.getString("email_id"));
                            model.setSub_vendor_busname(jsonObject1.getString("business_name"));
                            model.setSub_vendor_fn(jsonObject1.getString("first_name"));
                            model.setSub_vendor_ln(jsonObject1.getString("last_name"));
                            model.setSub_vendor_locality(jsonObject1.getString("locality"));
                            model.setSub_vendor_categ(jsonObject1.getString("vendor_category"));
                            model.setSub_vendor_contact(jsonObject1.getString("contact_number"));
                            sublist.add(model);
                            if (jsonObject1.getString("vendor_category").equalsIgnoreCase("Delivery Vendor"))
                                floatingActionMenu.setVisibility(View.GONE);
                            else
                                floatingActionMenu.setVisibility(View.VISIBLE);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        sublist.clear();
                        floatingActionMenu.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    noData.setVisibility(View.VISIBLE);
                    sublist.clear();
                    floatingActionMenu.setVisibility(View.GONE);
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

    private void init() {
        context = this;
        noData = (CustomTextView) findViewById(R.id.noData);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        addvendorbtn = (CustomButton) findViewById(R.id.addvendorbtn);
        addvendorbtn.setOnClickListener(this);
    }

    private void setAdapter() {
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new VendorListAdapter(context, sublist);
        recycler_view.setAdapter(adapter);
        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VendorMasterData model = sublist.get(position);
                if (!model.getSub_vendor_categ().equalsIgnoreCase("Delivery Vendor")) {
                    Intent intent = new Intent(context, VendorItemListActivity.class);
                    intent.putExtra("vendor_id", model.getSub_vendor_id());
                    intent.putExtra("vendor_name", model.getSub_vendor_busname());
                    context.startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        CustomTextView textView = (CustomTextView) findViewById(R.id.headet_text);
        textView.setText("Vendor List");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().hasExtra("category_name"))
            textView.setText(getIntent().getStringExtra("category_name"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        floatingActionMenu.close(true);
        if (Util.isNetworkAvailable(context)) {
            getAllVendorsList();
            getAllMasterVendor();
        } else
            Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addvendorbtn:
                startActivity(new Intent(context, AddNewVendorActivity.class));
                break;
            default:
                break;
        }
    }

    private void openVendorsDialog() {
        final Dialog mDialog;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.vendor_list_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        LinearLayoutManager mLayoutManager;
        CustomTextView dialog_title = (CustomTextView) mDialog.findViewById(R.id.title);
        dialog_title.setText("Select Vendor");
        mRecyclerView = (RecyclerView) mDialog.findViewById(R.id.offers_recycler);
        ImageView cancel = (ImageView) mDialog.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        SelectVendorTypeAdapter selectVendor = new SelectVendorTypeAdapter(context, sublist, "sub_category");
        mRecyclerView.setAdapter(selectVendor);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VendorMasterData model = sublist.get(position);
                floatingActionMenu.close(true);
                Intent intent = new Intent(context, OrderItemListActivity.class);
                intent.putExtra("vendor_id", model.getSub_vendor_id());
                startActivity(intent);
                Log.e(TAG, "onItemClick: " + model.getSub_vendor_id());
                Log.e(TAG, "onItemClick: " + model.getSub_vendor_busname());
                mDialog.cancel();

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        mDialog.show();
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);
        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                floatingActionMenu.getMenuIconView().setImageResource(floatingActionMenu.isOpened()
                        ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_add_white_24dp);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        floatingActionMenu.setIconToggleAnimatorSet(set);

    }

    private void getAllMasterVendor() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.MASTERVENDORLIST);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        Log.e(TAG, "getAllVendors: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendors_list_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "onResponse: Response >> " + resp);
                try {
                    masterlist.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objresult = jsonArray.getJSONObject(i);
                            VendorMasterData model = new VendorMasterData();
                            model.setMaster_vendor_id(objresult.getString("cat_id"));
                            model.setMaster_vendor_addedon(objresult.getString("addedOn"));
                            model.setMaster_vendor_name(objresult.getString("name"));
                            if (!objresult.getString("name").equalsIgnoreCase("Delivery Vendor"))
                                masterlist.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {

                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openVendorCatDialog() {
        final Dialog mDialog;
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.vendor_list_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.gravity = Gravity.CENTER;
        mDialog.getWindow().setAttributes(lp);
        LinearLayoutManager mLayoutManager;
        CustomTextView dialog_title = (CustomTextView) mDialog.findViewById(R.id.title);
        dialog_title.setText("Select Vendor Category");
        mRecyclerView = (RecyclerView) mDialog.findViewById(R.id.offers_recycler);
        ImageView cancel = (ImageView) mDialog.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        SelectVendorTypeAdapter selectVendor = new SelectVendorTypeAdapter(context, masterlist, "category");
        mRecyclerView.setAdapter(selectVendor);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                VendorMasterData model = masterlist.get(position);
                floatingActionMenu.close(true);

                Intent intent = new Intent(context, BidItemListActivity.class);
                intent.putExtra("vendor_id", model.getMaster_vendor_id());
                startActivity(intent);
                mDialog.cancel();

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        mDialog.show();
    }

    public class FloatingButton {
        FrameLayout bckgroundDimmer;
        FloatingActionButton button1, button2, button3, button4;

        public void showFloatingButton(final Context activity) {

            floatingActionMenu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
            button1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_orders);
            button2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_reservations);
            button3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_create_order);
            button4 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_create_bid);
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button4.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            createCustomAnimation();

            button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (sublist != null && !sublist.isEmpty())
                        openVendorsDialog();
                    else
                        Snackbar.make(findViewById(android.R.id.content), "Vendors list not available", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (masterlist != null && !masterlist.isEmpty())
                        openVendorCatDialog();
                    else
                        Snackbar.make(findViewById(android.R.id.content), "Vendors category not available", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();

                }
            });

        }

        public void setFloatingButtonControls(Context activity) {
            bckgroundDimmer = (FrameLayout) findViewById(R.id.background_dimmer);
            floatingActionMenu = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
            floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                @Override
                public void onMenuToggle(boolean opened) {
                    if (opened) {
                        bckgroundDimmer.setVisibility(View.VISIBLE);
                    } else {
                        bckgroundDimmer.setVisibility(View.GONE);
                    }
                }
            });
            bckgroundDimmer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (floatingActionMenu.isOpened()) {
                        floatingActionMenu.close(true);
                        bckgroundDimmer.setVisibility(View.GONE);
                        //menu opened
                    }
                }
            });
        }
    }
}
