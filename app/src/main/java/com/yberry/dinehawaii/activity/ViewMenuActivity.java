package com.yberry.dinehawaii.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.adapter.TextViewWithEditTextAdapter;
import com.yberry.dinehawaii.adapter.ViewMenuAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewMenuActivity extends AppCompatActivity {

    private static final String TAG = "ViewMenuActivity";
    public static List<String> listValueFoodType;
    TextViewWithEditTextAdapter adapter;
    ViewMenuAdapter viewMenuAdaper;
    JsonObject otherdetails;
    ArrayList<CheckBoxPositionModel> listFoodService = new ArrayList<>();
    String food_type_id_list, food_type_id, food_name,food_price,food_type,food_id ;
    ArrayList food_ctagory_id_list = new ArrayList();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);
        setToolbar();
        backButtonListener();
        init();

    }

    private void backButtonListener() {
        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recylerViewUploadMenu);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (getIntent().getAction().equalsIgnoreCase("FoodServiceFragment45")) {
            String s = getIntent().getStringExtra("other");
            otherdetails = new Gson().fromJson(s, JsonObject.class);
            food_type_id_list = getIntent().getExtras().getString("food_type_id");
            Log.v(TAG, "food_type_id_list :- " + food_type_id_list);
            getAllFoodCategories_Modify(AppConstants.BUSSINES_USER_BUSINESSAPI.ALL_FOOD_MODIFY_CATEGORIES);
        } else if (getIntent().getAction().equalsIgnoreCase("FoodDistributorFragment45B")) {
            food_type_id = getIntent().getExtras().getString("food_type_id");
            ArrayList<HashMap<String, String>> arrayListMap = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("food_prices");
            for(int i=0;i<arrayListMap.size();i++){
                HashMap<String, String> hashMap = arrayListMap.get(i);
                CheckBoxPositionModel model=new CheckBoxPositionModel();
                model.setDish_name(hashMap.get("food_name"));
                model.setAmount(hashMap.get("price"));
                listFoodService.add(model);
            }
            viewMenuAdaper=new ViewMenuAdapter(this,listFoodService);
            mRecyclerView.setAdapter(viewMenuAdaper);
//              for(int i=0;i<arrayListMap.size();i++){
//                  HashMap<String, String> hashMap = arrayListMap.get(i);
//                  hashMap.get("");
//              }

//            Log.e("neelesh",arrayListMap.get(0).get("price"));
//            ArrayList<HashMap<String, String>> arrayListMap1 = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("food_name");
//            Log.e("neelesh1",arrayListMap1.get(0).get("food_name"));

          /*  food_id = getIntent().getExtras().getString("food_id");
            food_name = getIntent().getExtras().getString("food_name");
            food_price = getIntent().getExtras().getString("food_price");
            food_type = getIntent().getExtras().getString("food_type");
            Log.d("food_name",food_name);
            Log.d("food_price",food_price);
            Log.d("food_type",food_type);*/
            getAllFoodCategories_Modify(AppConstants.BUSINESS_FOOD_VENDOR_API.VIEW_FOOD_DISTRIBUTOR_MENU_PRICE);
        } else if (getIntent().getAction().equalsIgnoreCase("SuppliesVendorFragment46")) {
            food_type_id =  getIntent().getExtras().getString("food_type_id");
            getAllFoodCategories_Modify(AppConstants.BUSINESS_FOOD_VENDOR_API.VIEW_FOOD_SUPPLIER_MENU_PRICE);
        }
    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("View Menu");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getAllFoodCategories_Modify(String methodName) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", methodName);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(ViewMenuActivity.this)); // AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(ViewMenuActivity.this));

        if (getIntent().getAction().equalsIgnoreCase("FoodServiceFragment45")) {

            jsonObject.addProperty("food_category_id", food_type_id_list);

        } else if (getIntent().getAction().equalsIgnoreCase("FoodDistributorFragment45B")) {
            jsonObject.addProperty("food_types_id", food_type_id );
        } else if (getIntent().getAction().equalsIgnoreCase("SuppliesVendorFragment46")) {
            jsonObject.addProperty("food_types_id", food_type_id );
        }

        getAllFoodCategoriesTask_Modify(jsonObject);
        Log.d(TAG, "View Menu Json resquest from the Class - "+ methodName + " :- " + jsonObject);
    }

    private void getAllFoodCategoriesTask_Modify(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(ViewMenuActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });

        Call<JsonObject> call = null;
        if (getIntent().getAction().equalsIgnoreCase("FoodServiceFragment45")) {

            MyApiEndpointInterface apiService =
                    ApiClient.getClient().create(MyApiEndpointInterface.class);
            call = apiService.businessUserBusinessApi(jsonObject);

        } else if (getIntent().getAction().equalsIgnoreCase("FoodDistributorFragment45B")) {

            MyApiEndpointInterface apiService =
                    ApiClient.getClient().create(MyApiEndpointInterface.class);
            call = apiService.business_food_vendor_api(jsonObject);

        }  else if (getIntent().getAction().equalsIgnoreCase("SuppliesVendorFragment46")) {

            MyApiEndpointInterface apiService =
                    ApiClient.getClient().create(MyApiEndpointInterface.class);
            call = apiService.business_food_vendor_api(jsonObject);
        }

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "View Menu Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                CheckBoxPositionModel model;
                try {
                    JSONObject jsonObject = new JSONObject(resp);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new CheckBoxPositionModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (getIntent().getAction().equalsIgnoreCase("FoodServiceFragment45")) {

                                Log.v(TAG, "Inside FoodServiceFragment45");

                                String food_cat_id = jsonObject1.getString("food_cat_id");
                                String dish_name = jsonObject1.getString("dish_name");
                                String food_name = jsonObject1.getString("food_name");
                                String food_type_id = jsonObject1.getString("food_type_id");
                                String amount = jsonObject1.getString("price");

                                model.setFood_ctagory_id(food_cat_id);
                                model.setDish_name(dish_name);
                                model.setFood_name(food_name);
                                model.setId(food_type_id);
                                model.setAmount(amount);
                                listFoodService.add(model);

                            } else if (getIntent().getAction().equalsIgnoreCase("FoodDistributorFragment45B")) {

                                Log.v(TAG, "Inside FoodDistributorFragment45B");

                                String dish_name = jsonObject1.getString("food_name");   // Dish Name
                                String food_name = jsonObject1.getString("food_type_name");   // Main Menu Name
                                String food_type_id = jsonObject1.getString("food_type_id");
                                String amount = jsonObject1.getString("price");

                                model.setDish_name(dish_name);
                                model.setFood_name(food_name);
                                model.setId(food_type_id);
                                model.setAmount(amount);
                                listFoodService.add(model);

                            } else if (getIntent().getAction().equalsIgnoreCase("SuppliesVendorFragment46")) {

                                Log.v(TAG, "Inside SuppliesVendorFragment46");

                                String dish_name = jsonObject1.getString("food_name");
                                String food_name = jsonObject1.getString("food_type_name");
                                String food_type_id = jsonObject1.getString("food_type_id");
                                String amount = jsonObject1.getString("price");

                                model.setDish_name(dish_name);
                                model.setFood_name(food_name);
                                model.setId(food_type_id);
                                model.setAmount(amount);
                                listFoodService.add(model);
                            }
                        }

                        String food_cat_list = food_ctagory_id_list.toString().replace("[", "").replace("]", "");
                        Log.v(TAG, "food_ctagory_id_list :- " + food_cat_list);
                        Log.v(TAG, "dataList" + listFoodService);

                        setAdapterCheck(listFoodService);

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
                Toast.makeText(ViewMenuActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapterCheck(ArrayList<CheckBoxPositionModel> listFoodService) {

        viewMenuAdaper = new ViewMenuAdapter(ViewMenuActivity.this, listFoodService);
        mRecyclerView.setAdapter(viewMenuAdaper);
        viewMenuAdaper.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
