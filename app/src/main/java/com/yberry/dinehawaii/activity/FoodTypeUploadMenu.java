package com.yberry.dinehawaii.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.adapter.CheckBoxAdapter;
import com.yberry.dinehawaii.adapter.TextViewWithEditTextAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodTypeUploadMenu extends AppCompatActivity {

    private static final String TAG = "FoodTypeUploadMenu";
    public static List<String> listValueFoodType;
    TextViewWithEditTextAdapter adapter;
    CheckBoxAdapter checkBoxAdapter;
    JsonObject otherdetails;
    ArrayList<CheckBoxPositionModel> listFoodService = new ArrayList<>();
    String food_type_id_list;
    ArrayList food_ctagory_id_list = new ArrayList();
    private RecyclerView mRecyclerView;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type_upload_menu);
        setToolbar();
        backButtonListener();
        init();

        if (getIntent().getAction().equalsIgnoreCase("Fragment45_Modify_Menu")) {
            String s = getIntent().getStringExtra("other");
            food_type_id_list = getIntent().getExtras().getString("food_type_id");
            Log.e(TAG, "onCreate: other >> "+s);
            Log.e(TAG, "onCreate: food_type_id_list >>"+ food_type_id_list );
            otherdetails = new Gson().fromJson(s, JsonObject.class);
            ((TextView) findViewById(R.id.headet_text)).setText("Modify Menu");
            getAllFoodCategories_Modify(AppConstants.BUSSINES_USER_BUSINESSAPI.ALL_FOOD_MODIFY_CATEGORIES);

        } else if (getIntent().getAction().equalsIgnoreCase("Fragment45B_Modify_Menu")) {

            ((TextView) findViewById(R.id.headet_text)).setText("Modify Menu");
            getAllFoodCategories_Modify(AppConstants.BUSINESS_FOOD_VENDOR_API.VIEW_FOOD_DISTRIBUTOR_MENU_PRICE);

        } else if (getIntent().getAction().equalsIgnoreCase("Fragment46_Modify_Menu")) {

            ((TextView) findViewById(R.id.headet_text)).setText("Modify Menu");
            getAllFoodCategories_Modify(AppConstants.BUSINESS_FOOD_VENDOR_API.VIEW_FOOD_SUPPLIER_MENU_PRICE);

        } else if (getIntent().getAction().equalsIgnoreCase("upload_Menu")) {

            ((TextView) findViewById(R.id.headet_text)).setText("Upload Menu");

            String s = getIntent().getStringExtra("other");
            otherdetails = new Gson().fromJson(s, JsonObject.class);
            food_type_id_list = getIntent().getExtras().getString("food_type_id");
            Log.v(TAG, "food_type_id_list :- " + food_type_id_list);
            getAllFoodCategories(food_type_id_list);

        }

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFoodService();
            }
        });
    }





    @SuppressLint("LongLogTag")
    private void addFoodService() {

        JsonObject jsonObject = new JsonObject();
        JsonArray menudetail = new JsonArray();

        if (getIntent().getAction().equalsIgnoreCase("Fragment45_Modify_Menu")) {

            String food_cat_list = food_ctagory_id_list.toString().replace("[", "").replace("]", "");
            Log.v(TAG, "food_ctagory_id_list :- " + food_cat_list);
            otherdetails.addProperty("food_ctagory_id", food_cat_list);

            JsonElement jsonElement = otherdetails.getAsJsonObject();

            jsonObject.addProperty("method", AppConstants.BUSINESS_FOOD_VENDOR_API.ADD_FOOD_SERVICE);
            jsonObject.add("otherDetails", jsonElement);
            setAdapterCheck(listFoodService);
            if(checkBoxAdapter.getListValue().isEmpty()){
                Toast.makeText(FoodTypeUploadMenu.this, "No Data found",Toast.LENGTH_SHORT).show();
            }else{
                for (int i = 0; i < checkBoxAdapter.getListValue().size(); i++) {
                    Log.d(TAG, i + "===" + checkBoxAdapter.getListValue().get(i).getAmount());

                    if (checkBoxAdapter.getListValue().get(i).isSelected()) {
                        JsonObject object = new JsonObject();
                        object.addProperty("food_ctagory_id", checkBoxAdapter.getListValue().get(i).getFood_ctagory_id());
                        object.addProperty("price", checkBoxAdapter.getListValue().get(i).getAmount());
                        object.addProperty("food_type_id", checkBoxAdapter.getListValue().get(i).getId());
                        menudetail.add(object);
                    }
                }}

          /*  for (int i = 0; i < checkBoxAdapter.getListValue().size(); i++) {
                Log.d(TAG, i + "===" + checkBoxAdapter.getListValue().get(i).getAmount());

                if (checkBoxAdapter.getListValue().get(i).isSelected()) {
                    JsonObject object = new JsonObject();
                    object.addProperty("food_ctagory_id", checkBoxAdapter.getListValue().get(i).getFood_ctagory_id());
                    object.addProperty("price", checkBoxAdapter.getListValue().get(i).getAmount());
                    object.addProperty("food_type_id", checkBoxAdapter.getListValue().get(i).getId());
                    menudetail.add(object);
                }
            }*/

        } else if (getIntent().getAction().equalsIgnoreCase("Fragment45B_Modify_Menu") ) {

            jsonObject.addProperty("method", AppConstants.BUSINESS_FOOD_VENDOR_API.ADD_FOOD_DISTRIBUTION);

            JsonObject otherDetails = new JsonObject();
            otherDetails.addProperty("business_id", AppPreferencesBuss.getBussiId(FoodTypeUploadMenu.this));
            otherDetails.addProperty("user_id", AppPreferencesBuss.getUserId(FoodTypeUploadMenu.this));
            otherDetails.addProperty("food_type_id", getIntent().getExtras().getString("food_type_id"));
            otherDetails.addProperty("delivery_status", getIntent().getStringExtra("deliveryType"));
            otherDetails.addProperty("vendor_name", getIntent().getStringExtra("vandername"));

            jsonObject.add("otherDetails", otherDetails);
            setAdapterCheck(listFoodService);
             if(checkBoxAdapter.getListValue().isEmpty()){
                Toast.makeText(FoodTypeUploadMenu.this, "No Data found",Toast.LENGTH_SHORT).show();

            }else{
                for (int i = 0; i < checkBoxAdapter.getListValue().size(); i++) {
                    Log.d(TAG, i + "===" + checkBoxAdapter.getListValue().get(i).getAmount());

                    if (checkBoxAdapter.getListValue().get(i).isSelected()) {
                        Log.d("data " + i + "===", checkBoxAdapter.getListValue().get(i).getAmount());
                        JsonObject object = new JsonObject();
                        object.addProperty("food_name", checkBoxAdapter.getListValue().get(i).getDish_name());
                        object.addProperty("price", checkBoxAdapter.getListValue().get(i).getAmount());
                        menudetail.add(object);
                    }
                }}



        } else if ( getIntent().getAction().equalsIgnoreCase("Fragment46_Modify_Menu") ) {
            JsonObject otherDetails = new JsonObject();

            jsonObject.addProperty("method", AppConstants.BUSINESS_FOOD_VENDOR_API.ADD_SUPPLIER_VENDOR);

            otherDetails.addProperty("business_id", AppPreferencesBuss.getBussiId(FoodTypeUploadMenu.this));
            otherDetails.addProperty("user_id", AppPreferencesBuss.getUserId(FoodTypeUploadMenu.this));
            otherDetails.addProperty("food_type_id",getIntent().getExtras().getString("food_type_id"));
            otherDetails.addProperty("delivery_status", getIntent().getStringExtra("deliveryType"));
            otherDetails.addProperty("vendor_name", getIntent().getStringExtra("vendor_name"));

            jsonObject.add("otherDetails", otherDetails);
            setAdapterCheck(listFoodService);
            if(checkBoxAdapter.getListValue().isEmpty()){
                Toast.makeText(FoodTypeUploadMenu.this, "No Data found",Toast.LENGTH_SHORT).show();

            }else{
                for (int i = 0; i < checkBoxAdapter.getListValue().size(); i++) {
                    Log.d(TAG, i + "===" + checkBoxAdapter.getListValue().get(i).getAmount());

                    if (checkBoxAdapter.getListValue().get(i).isSelected()) {
                        Log.d("data " + i + "===", checkBoxAdapter.getListValue().get(i).getAmount());
                        JsonObject object = new JsonObject();
                        object.addProperty("food_name", checkBoxAdapter.getListValue().get(i).getDish_name());
                        object.addProperty("price", checkBoxAdapter.getListValue().get(i).getAmount());
                        menudetail.add(object);
                    }
                }}

          /*  for (int i = 0; i < checkBoxAdapter.getListValue().size(); i++) {
                Log.d(TAG, i + "===" + checkBoxAdapter.getListValue().get(i).getAmount());

                if (checkBoxAdapter.getListValue().get(i).isSelected()) {
                    Log.d("data " + i + "===", checkBoxAdapter.getListValue().get(i).getAmount());
                    JsonObject object = new JsonObject();
                    object.addProperty("food_name", checkBoxAdapter.getListValue().get(i).getDish_name());
                    object.addProperty("price", checkBoxAdapter.getListValue().get(i).getAmount());
                    menudetail.add(object);
                }
            }
*/

        } else if (getIntent().getAction().equalsIgnoreCase("upload_Menu")) {

            String food_cat_list = food_ctagory_id_list.toString().replace("[", "").replace("]", "");
            Log.v(TAG, "food_ctagory_id_list :- " + food_cat_list);
            otherdetails.addProperty("food_ctagory_id", food_cat_list);
            Log.e(TAG+"<<<<<< HVANATGE >>>>>>>>>>>>",food_cat_list.toString());
            JsonElement jsonElement = otherdetails.getAsJsonObject();

            jsonObject.addProperty("method", AppConstants.BUSINESS_FOOD_VENDOR_API.ADD_FOOD_SERVICE);
            jsonObject.add("otherDetails", jsonElement);
            setAdapter(listFoodService);
            adapter.notifyDataSetChanged();
            if(adapter.getListValue().isEmpty()){
                Toast.makeText(FoodTypeUploadMenu.this, "No Data found",Toast.LENGTH_SHORT).show();
             }
            } else{
             for (int i = 0; i < adapter.getListValue().size(); i++) {
                Log.d("data " + i + "===", adapter.getListValue().get(i).getAmount());
                JsonObject object = new JsonObject();
                object.addProperty("food_ctagory_id", adapter.getListValue().get(i).getFood_ctagory_id());
                object.addProperty("price", adapter.getListValue().get(i).getAmount());
                object.addProperty("food_type_id", adapter.getListValue().get(i).getId());
                menudetail.add(object);
                 }
        }
            jsonObject.add("menuDetails", menudetail);
            Log.d(TAG, "Add Food Service Json Request :- " + jsonObject);
            addFoodServiceTask(jsonObject);
        }

    private void addFoodServiceTask(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(FoodTypeUploadMenu.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
            }
        });
        Call<JsonObject> call = null;
        MyApiEndpointInterface apiService =
        ApiClient.getClient().create(MyApiEndpointInterface.class);

        if (getIntent().getAction().equalsIgnoreCase("Fragment45_Modify_Menu")) {

            call = apiService.businessUserBusinessApi(jsonObject);

        } else if (getIntent().getAction().equalsIgnoreCase("Fragment45B_Modify_Menu")) {

            call = apiService.business_food_vendor_api(jsonObject);

        } else if (getIntent().getAction().equalsIgnoreCase("Fragment46_Modify_Menu")) {

            call = apiService.business_food_vendor_api(jsonObject);

        } else if (getIntent().getAction().equalsIgnoreCase("upload_Menu")) {

            call = apiService.businessUserBusinessApi(jsonObject);
        }

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String jsonResponse = response.body().toString();
                Log.e(TAG, "Add Food Service Json Response :- " + jsonResponse);
                CheckBoxPositionModel model;
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        Toast.makeText(getApplicationContext(), "Record Added Succesfully !!!", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(FoodTypeUploadMenu.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllFoodCategories_Modify(String MEHTOD_NAME) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", MEHTOD_NAME);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(FoodTypeUploadMenu.this));// AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(FoodTypeUploadMenu.this));

        if (getIntent().getAction().equalsIgnoreCase("Fragment45_Modify_Menu")) {
            jsonObject.addProperty("food_category_id",food_type_id_list );

        } else if (getIntent().getAction().equalsIgnoreCase("Fragment45B_Modify_Menu")) {
            jsonObject.addProperty("food_types_id", getIntent().getExtras().getString("food_type_id") );
        }

        getAllFoodCategoriesTask_Modify(jsonObject);
        Log.d(TAG, "allFoodCategories Json resquest :- " + jsonObject);

    }

    private void getAllFoodCategoriesTask_Modify(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(FoodTypeUploadMenu.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });

        Call<JsonObject> call = null;
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);

        if (getIntent().getAction().equalsIgnoreCase("Fragment45_Modify_Menu")) {

            call = apiService.businessUserBusinessApi(jsonObject);

        } else if (getIntent().getAction().equalsIgnoreCase("Fragment45B_Modify_Menu")) {

            call = apiService.business_food_vendor_api(jsonObject);

        } else if (getIntent().getAction().equalsIgnoreCase("Fragment46_Modify_Menu")) {

            call = apiService.business_food_vendor_api(jsonObject);
        }

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "allFoodCategories Json Response :- " + response.body().toString());
                String s = response.body().toString();
                CheckBoxPositionModel model;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new CheckBoxPositionModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (getIntent().getAction().equalsIgnoreCase("Fragment45_Modify_Menu")) {
                                String food_cat_id = jsonObject1.getString("food_cat_id");
                                String dish_name = jsonObject1.getString("dish_name");
                                String food_name = jsonObject1.getString("food_name");
                                String food_type_id = jsonObject1.getString("food_type_id");
                                String amount = jsonObject1.getString("price");
                                food_ctagory_id_list.add(food_cat_id);
                                model.setFood_ctagory_id(food_cat_id);
                                model.setDish_name(dish_name);
                                model.setFood_name(food_name);
                                model.setId(food_type_id);
                                model.setAmount(amount);
                                listFoodService.add(model);
                            } else if (getIntent().getAction().equalsIgnoreCase("Fragment45B_Modify_Menu")) {
                                Log.v(TAG, "Inside FoodDistributorFragment45B");
                                String dish_name = jsonObject1.getString("food_name");
                                String food_name = jsonObject1.getString("food_type_name");
                                String food_type_id = jsonObject1.getString("food_type_id");
                                String amount = jsonObject1.getString("price");
                                model.setDish_name(dish_name);
                                model.setFood_name(food_name);
                                model.setId(food_type_id);
                                model.setAmount(amount);
                                listFoodService.add(model);
                            } else if (getIntent().getAction().equalsIgnoreCase("Fragment46_Modify_Menu")) {
                                Log.v(TAG, "Inside Fragment46_Modify_Menu");
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
                        Log.v(TAG, "dataList" + listFoodService);
                        setAdapterCheck(listFoodService);

                        /*if (getIntent().getAction().equalsIgnoreCase("Fragment45_Modify_Menu")) {
                        } else if (getIntent().getAction().equalsIgnoreCase("Fragment45B_Modify_Menu")) {
                            setAdapterCheck(listFoodService);
                        }*/
                    }else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(FoodTypeUploadMenu.this, "No record available for the selected food and service", Toast.LENGTH_LONG).show();
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
                Toast.makeText(FoodTypeUploadMenu.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setAdapterCheck(ArrayList<CheckBoxPositionModel> listFoodService) {

        checkBoxAdapter = new CheckBoxAdapter(FoodTypeUploadMenu.this, listFoodService, new CheckBoxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int view, int position) {

            }
        });
        mRecyclerView.setAdapter(checkBoxAdapter);
        checkBoxAdapter.notifyDataSetChanged();
    }

    private void setAdapter(ArrayList<CheckBoxPositionModel> listFoodService) {
        adapter = new TextViewWithEditTextAdapter(FoodTypeUploadMenu.this, listFoodService);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_filter) {

//            addFoodService();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_icon_menu, menu);
        return true;
    }

    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recylerViewUploadMenu);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Menus");
        ((ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
       /* mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
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

    private void getAllFoodCategories(String food_type_id_list) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.ALL_FOOD_CATEGORIES);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(FoodTypeUploadMenu.this));// AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("food_type_id", food_type_id_list);

        getAllFoodCategoriesTask(jsonObject);
        Log.d(TAG, "allFoodCategories Modifyy Json resquest :- " + jsonObject);

    }



    private void getAllFoodCategoriesTask(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(FoodTypeUploadMenu.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Modifyy allFoodCategories Json Response :- " + response.body().toString());
                String s = response.body().toString();
                CheckBoxPositionModel model;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new CheckBoxPositionModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String food_cat_id = jsonObject1.getString("food_cat_id");
                            String dish_name = jsonObject1.getString("dish_name");
                            String food_name = jsonObject1.getString("food_name");
                            String food_type_id = jsonObject1.getString("food_type_id");

                            food_ctagory_id_list.add(food_cat_id);

                            model.setFood_ctagory_id(food_cat_id);
                            model.setDish_name(dish_name);
                            model.setFood_name(food_name);
                            model.setId(food_type_id);
                            listFoodService.add(model);

                        }

                        String food_cat_list = food_ctagory_id_list.toString().replace("[", "").replace("]", "");
                        Log.v(TAG, "food_ctagory_id_list :- " + food_cat_list);

                        Log.v(TAG, "dataList" + listFoodService);

                        if (getIntent().getAction().equalsIgnoreCase("upload_Menu")) {
                            setAdapter(listFoodService);
                        }
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
                Toast.makeText(FoodTypeUploadMenu.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
