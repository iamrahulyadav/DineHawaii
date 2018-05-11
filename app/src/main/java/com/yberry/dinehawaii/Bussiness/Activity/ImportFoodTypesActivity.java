package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.adapter.CheckBoxOptionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportFoodTypesActivity extends AppCompatActivity {
    private static final String TAG = "Import_food_activity";
    public static List<CheckBoxPositionModel> foodlist = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    CheckBoxOptionAdapter ad;
    ArrayAdapter<CheckBoxPositionModel> adapter;
    RelativeLayout layoutLinear;
    private String food_catagory_id_list="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_food_types);
        setToolbar();
        init();
        getFoodTypesData();
    }
    private void getFoodTypesData() {
        if (Util.isNetworkAvailable(ImportFoodTypesActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.FOODTYPE);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ImportFoodTypesActivity.this));// AppPreferencesBuss.getUserId(context));
            Log.d(TAG, "Request GET ALL FOODTYPE >> " + jsonObject);
            getFoodTypeApi(jsonObject);
        } else {
            Toast.makeText(ImportFoodTypesActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getFoodTypeApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(ImportFoodTypesActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            CheckBoxPositionModel model = new CheckBoxPositionModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            model.setName(object.getString("food_name"));
                            model.setId(object.getString("id"));
                            model.setChckStatus(false);
                            foodlist.add(model);
                            for (int k = 0; k < foodlist.size(); k++) {
                                System.out.println(TAG + " VAlue :" + foodlist.get(k).getName());
                            }
                        }

                        CheckBoxOptionAdapter ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) foodlist, new CheckBoxOptionAdapter.setOnClickListener() {
                            @Override
                            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

                            }
                        });

                        recyclerView.setAdapter(ad);
                        ad.notifyDataSetChanged();
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
                Toast.makeText(ImportFoodTypesActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Import Food Type");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        layoutLinear = (RelativeLayout) findViewById(R.id.layoutLinear);
        for (CheckBoxPositionModel model : foodlist) {
            String name, id;
            name = model.getFood_name();
            id = model.getId();
            Log.v(TAG, "Id :- " + id + "\n Name :- " + name);
        }

        foodlist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(ImportFoodTypesActivity.this));
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) foodlist, new CheckBoxOptionAdapter.setOnClickListener() {
            @Override
            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

            }
        });
        recyclerView.setAdapter(ad);
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
            food_catagory_id_list="";
            ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
            for (CheckBoxPositionModel listValue : checkBoxOptionList) {
                if (food_catagory_id_list.equalsIgnoreCase("")){
                    food_catagory_id_list = listValue.getId();
                }else{
                    food_catagory_id_list = food_catagory_id_list + "," + listValue.getId();
                }
            }
            importFoodTypes();
            Log.d(TAG, "selected check box :- " + food_catagory_id_list);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void importFoodTypes() {
        if (Util.isNetworkAvailable(ImportFoodTypesActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.KEY_IMPORT_FOOD);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ImportFoodTypesActivity.this));// AppPreferencesBuss.getUserId(context));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ImportFoodTypesActivity.this));// AppPreferencesBuss.getUserId(context));
            jsonObject.addProperty("food_cat_id", food_catagory_id_list);// AppPreferencesBuss.getUserId(context));
            Log.d(TAG, "import FOODTYPE request " + jsonObject);
            importFoodTypesApi(jsonObject);
        } else {
            Toast.makeText(ImportFoodTypesActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void importFoodTypesApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(ImportFoodTypesActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.addeditmenu(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "import food menu response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        String msg = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            msg = jsonObj.getString("msg");
                        }
                        finish();
                        Toast.makeText(ImportFoodTypesActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            msg = jsonObj.getString("msg");
                        }
                        Toast.makeText(ImportFoodTypesActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ImportFoodTypesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ImportFoodTypesActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
