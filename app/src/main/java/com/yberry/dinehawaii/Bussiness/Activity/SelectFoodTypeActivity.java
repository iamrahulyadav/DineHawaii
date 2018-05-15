package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.MyFoodTypeAdapter;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelectFoodTypeActivity extends AppCompatActivity {

    private static final String TAG = "SelectFoodTypeActivity";
    public static List<CheckBoxPositionModel> foodlist = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    MyFoodTypeAdapter ad;
    ArrayAdapter<CheckBoxPositionModel> adapter;
    RelativeLayout layoutLinear;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_food_type);
        setToolbar();
        LocalBroadcastManager.getInstance(SelectFoodTypeActivity.this).registerReceiver(new MyRefereshReceiver(), new IntentFilter("refreshActivity"));
        init();
        foodlistdata();
    }

    private void foodlistdata() {
        if (Util.isNetworkAvailable(SelectFoodTypeActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.KEY_MY_FOODS);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(SelectFoodTypeActivity.this));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(SelectFoodTypeActivity.this));// AppPreferencesBuss.getUserId(context));
            Log.d(TAG, "Request GET MY FOODTYPES >> " + jsonObject);
            foodServicesData(jsonObject);
        } else {
            Toast.makeText(SelectFoodTypeActivity.this, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void foodServicesData(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(SelectFoodTypeActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_food_vendor_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET MY FOODTYPES >> " + response.body().toString());
                String s = response.body().toString();

                try {
                    foodlist.clear();
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            CheckBoxPositionModel model = new CheckBoxPositionModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            model.setName(object.getString("food_name"));
                            model.setId(object.getString("id"));
                            model.setEdit_status(object.getString("edit_status"));
                            model.setChckStatus(false);
                            foodlist.add(model);
                            for (int k = 0; k < foodlist.size(); k++) {
                                System.out.println("VAlue :" + foodlist.get(k).getName());
                            }
                        }

                        MyFoodTypeAdapter ad = new MyFoodTypeAdapter(SelectFoodTypeActivity.this, (ArrayList<CheckBoxPositionModel>) foodlist, new MyFoodTypeAdapter.setOnClickListener() {
                            @Override
                            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

                            }
                        });
                        recyclerView.setAdapter(ad);
                        ad.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equals("400")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SelectFoodTypeActivity.this);
                        alertDialog.setMessage("No Food Types found.");
                        alertDialog.setIcon(R.drawable.ic_launcher_app);
                        alertDialog.setPositiveButton("Yes,Import it!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(SelectFoodTypeActivity.this, ImportFoodTypesActivity.class));
                                finish();
                            }
                        });

                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();

                    } else {
                        Toast.makeText(SelectFoodTypeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SelectFoodTypeActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ad.notifyDataSetChanged();
    }

    private void init() {
        layoutLinear = (RelativeLayout) findViewById(R.id.layoutLinear);
        for (CheckBoxPositionModel model : foodlist) {
            String name, id;
            name = model.getFood_name();
            id = model.getId();
            Log.v(TAG, "Id :- " + id + "\n Name :- " + name);
        }

        context = this;
        foodlist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectFoodTypeActivity.this));
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        ad = new MyFoodTypeAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) foodlist, new MyFoodTypeAdapter.setOnClickListener() {
            @Override
            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

            }
        });
        recyclerView.setAdapter(ad);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Select Food Type");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.action_filter) {
            ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("foodtype", checkBoxOptionList);
            setResult(-1, intent);
            finish();
            Log.d(TAG, "ListValue :- " + checkBoxOptionList.toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyRefereshReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            foodlist.clear();
            foodlistdata();
        }
    }
}
