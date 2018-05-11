package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.MenuAdapter;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadMenuList extends AppCompatActivity {

    private static final String TAG = "UploadMenuList";
    JsonObject itemList1, itemList2, itemList3, itemList4, itemList5, itemList6, itemList7, itemList8, itemList9, itemList10;
    MenuAdapter menuAdapter;
    RecyclerView recyclerView;
    ArrayList<CheckBoxPositionModel> menuList;
    CheckBoxPositionModel positionModel;
    String deliveryType, vanderName;
    JsonObject otherDetails;
    int count = 1;
    Button addMoreItem;
    TableRow tableRow1, tableRow2, tableRow3, tableRow4, tableRow5, tableRow6, tableRow7, tableRow8, tableRow9, tableRow10;
    EditText itemName1, itemName2, itemName3, itemName4, itemName5, itemName6, itemName7, itemName8, itemName9, itemName10;
    EditText itemPrice1, itemPrice2, itemPrice3, itemPrice4, itemPrice5, itemPrice6, itemPrice7, itemPrice8, itemPrice9, itemPrice10;
    JsonArray menuDetailsJsonArray = new JsonArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_menu_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Price List");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuList = new ArrayList<CheckBoxPositionModel>();
        positionModel = new CheckBoxPositionModel();
//        menuList = getIntent().getParcelableArrayListExtra("listFoodService");

        otherDetails = new JsonObject();
        if (getIntent().getAction().equalsIgnoreCase("FoodFragment45B")) {

            otherDetails.addProperty("business_id", AppPreferencesBuss.getBussiId(UploadMenuList.this));
            otherDetails.addProperty("user_id", AppPreferencesBuss.getUserId(UploadMenuList.this));
            otherDetails.addProperty("food_type_id", getIntent().getStringExtra("food_type_id"));
            otherDetails.addProperty("delivery_status", getIntent().getStringExtra("deliveryType"));
            otherDetails.addProperty("vendor_name", getIntent().getStringExtra("vandername"));

        } else if (getIntent().getAction().equalsIgnoreCase("SuppliesVendorFragment46")) {

            otherDetails.addProperty("business_id", AppPreferencesBuss.getBussiId(UploadMenuList.this));
            otherDetails.addProperty("user_id", AppPreferencesBuss.getUserId(UploadMenuList.this));
            otherDetails.addProperty("food_type_id", getIntent().getStringExtra("food_type_id"));
            otherDetails.addProperty("delivery_status", getIntent().getStringExtra("deliveryType"));
            otherDetails.addProperty("vendor_name", getIntent().getStringExtra("vendor_name"));

        }

        /* recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UploadMenuList.this));

        menuAdapter = new MenuAdapter(UploadMenuList.this,menuList);
        recyclerView.setAdapter(menuAdapter); */
        initViews();
        setListener();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(itemName1.getText().toString()) && !TextUtils.isEmpty(itemPrice1.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName1.getText().toString());
                    itemList.addProperty("price", itemPrice1.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 1:- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }
                if (!TextUtils.isEmpty(itemName2.getText().toString()) && !TextUtils.isEmpty(itemPrice2.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName2.getText().toString());
                    itemList.addProperty("price", itemPrice2.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 2 :- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }
                if (!TextUtils.isEmpty(itemName3.getText().toString()) && !TextUtils.isEmpty(itemPrice3.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName3.getText().toString());
                    itemList.addProperty("price", itemPrice3.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 3 :- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }
                if (!TextUtils.isEmpty(itemName4.getText().toString()) && !TextUtils.isEmpty(itemPrice4.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName4.getText().toString());
                    itemList.addProperty("price", itemPrice4.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 4 :- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }
                if (!TextUtils.isEmpty(itemName5.getText().toString()) && !TextUtils.isEmpty(itemPrice5.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName5.getText().toString());
                    itemList.addProperty("price", itemPrice5.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 5 :- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }
                if (!TextUtils.isEmpty(itemName6.getText().toString()) && !TextUtils.isEmpty(itemPrice6.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName6.getText().toString());
                    itemList.addProperty("price", itemPrice6.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 6 :- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                } else if (!TextUtils.isEmpty(itemName7.getText().toString()) && !TextUtils.isEmpty(itemPrice7.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName7.getText().toString());
                    itemList.addProperty("price", itemPrice7.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 7 :- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }
                if (!TextUtils.isEmpty(itemName8.getText().toString()) && !TextUtils.isEmpty(itemPrice8.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName8.getText().toString());
                    itemList.addProperty("price", itemPrice8.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 8:- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }
                if (!TextUtils.isEmpty(itemName9.getText().toString()) && !TextUtils.isEmpty(itemPrice9.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName9.getText().toString());
                    itemList.addProperty("price", itemPrice9.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 9:- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }
                if (!TextUtils.isEmpty(itemName10.getText().toString()) && !TextUtils.isEmpty(itemPrice10.getText().toString())) {

                    JsonObject itemList = new JsonObject();
                    itemList.addProperty("food_name", itemName10.getText().toString());
                    itemList.addProperty("price", itemPrice10.getText().toString());
                    menuDetailsJsonArray.add(itemList);
                    Log.v(TAG, "Json Element 10 :- " + itemList);
                    Log.v(TAG, "Json Menu Details :- " + menuDetailsJsonArray);

                }

                if (getIntent().getAction().equalsIgnoreCase("FoodFragment45B"))
                    sendFoodDistributionRequest(AppConstants.BUSINESS_FOOD_VENDOR_API.ADD_FOOD_DISTRIBUTION);
                else if (getIntent().getAction().equalsIgnoreCase("SuppliesVendorFragment46")) {
                    sendFoodDistributionRequest(AppConstants.BUSINESS_FOOD_VENDOR_API.ADD_SUPPLIER_VENDOR);
                }
            }
        });
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

    private void sendFoodDistributionRequest(String methodName) {

        /*for (int i = 0; i < menuList.size(); i++) {
            CheckBoxPositionModel model = menuList.get(i);
            JsonObject itemList = new JsonObject();
            itemList.addProperty("food_name", model.getFood_name());
            itemList.addProperty("price", model.getAmount());
            menuDetailsJsonArray.add(itemList);
        }*/

        Log.v(TAG, "Json array inside method:- " + menuDetailsJsonArray);
        Log.v(TAG, "Json otherDetails :- " + otherDetails);
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("method", methodName);
//                    jsonObject.add("menuDetails", new Gson().toJsonTree(menuAdapter.getList()));
        jsonObject.add("menuDetails", new Gson().toJsonTree(menuDetailsJsonArray));
        jsonObject.add("otherDetails", new Gson().toJsonTree(otherDetails));
        uploadPriceList(jsonObject);
    }

    private void setListener() {

        addMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;

                Log.v(TAG, "Add button  clicked :- " + count);


                if (TextUtils.isEmpty(itemName1.getText().toString()) && TextUtils.isEmpty(itemPrice1.getText().toString())) {

                    itemName1.setError("Please enter Name");
                    itemPrice1.setError("Please enter Price");
                } else {

                    tableRow2.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(itemName2.getText().toString()) && TextUtils.isEmpty(itemPrice2.getText().toString())) {


                } else {

                    tableRow3.setVisibility(View.VISIBLE);
                    Log.v(TAG, "Array List after adding 1 item :- " + menuList.toString());
                }
                if (TextUtils.isEmpty(itemName3.getText()) && TextUtils.isEmpty(itemPrice3.getText())) {
                } else {

                    tableRow4.setVisibility(View.VISIBLE);
                }


                if (TextUtils.isEmpty(itemName4.getText()) && TextUtils.isEmpty(itemPrice4.getText())) {
                } else {
                    tableRow5.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(itemName5.getText()) && TextUtils.isEmpty(itemPrice5.getText())) {
                } else {
                    tableRow6.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(itemName6.getText()) && TextUtils.isEmpty(itemPrice6.getText())) {
                } else {
                    tableRow7.setVisibility(View.VISIBLE);
                }


                if (TextUtils.isEmpty(itemName7.getText()) && TextUtils.isEmpty(itemPrice7.getText())) {
                } else {
                    tableRow8.setVisibility(View.VISIBLE);
                }

                if (TextUtils.isEmpty(itemName8.getText()) && TextUtils.isEmpty(itemPrice8.getText())) {
                } else {
                    tableRow9.setVisibility(View.VISIBLE);
                }


                if (TextUtils.isEmpty(itemName9.getText()) && TextUtils.isEmpty(itemPrice9.getText())) {
                } else {
                    tableRow10.setVisibility(View.VISIBLE);
                }

                if (count == 11)
                    Toast.makeText(getApplicationContext(), "You can add only 10 items at once !!!", Toast.LENGTH_SHORT).show();

            }
        });
        Log.v(TAG, "Add button clicked outside :- " + count);
    }

    private void initViews() {

        tableRow1 = (TableRow) findViewById(R.id.tableRow1);
        tableRow2 = (TableRow) findViewById(R.id.tableRow2);
        tableRow3 = (TableRow) findViewById(R.id.tableRow3);
        tableRow4 = (TableRow) findViewById(R.id.tableRow4);
        tableRow5 = (TableRow) findViewById(R.id.tableRow5);
        tableRow6 = (TableRow) findViewById(R.id.tableRow6);
        tableRow7 = (TableRow) findViewById(R.id.tableRow7);
        tableRow8 = (TableRow) findViewById(R.id.tableRow8);
        tableRow9 = (TableRow) findViewById(R.id.tableRow9);
        tableRow10 = (TableRow) findViewById(R.id.tableRow10);

        itemName1 = (EditText) findViewById(R.id.item1);
        itemName2 = (EditText) findViewById(R.id.item2);
        itemName3 = (EditText) findViewById(R.id.item3);
        itemName4 = (EditText) findViewById(R.id.item4);
        itemName5 = (EditText) findViewById(R.id.item5);
        itemName6 = (EditText) findViewById(R.id.item6);
        itemName7 = (EditText) findViewById(R.id.item7);
        itemName8 = (EditText) findViewById(R.id.item8);
        itemName9 = (EditText) findViewById(R.id.item9);
        itemName10 = (EditText) findViewById(R.id.item10);

        itemPrice1 = (EditText) findViewById(R.id.price1);
        itemPrice2 = (EditText) findViewById(R.id.price2);
        itemPrice3 = (EditText) findViewById(R.id.price3);
        itemPrice4 = (EditText) findViewById(R.id.price4);
        itemPrice5 = (EditText) findViewById(R.id.price5);
        itemPrice6 = (EditText) findViewById(R.id.price6);
        itemPrice7 = (EditText) findViewById(R.id.price7);
        itemPrice8 = (EditText) findViewById(R.id.price8);
        itemPrice9 = (EditText) findViewById(R.id.price9);
        itemPrice10 = (EditText) findViewById(R.id.price10);
        addMoreItem = (Button) findViewById(R.id.addMoreItem_btn);
    }

    private void uploadPriceList(JsonObject jsonObject) {

        Log.d(TAG, jsonObject + "\n" + jsonObject.getAsJsonObject());

        final ProgressHUD progressHD = ProgressHUD.show(UploadMenuList.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                progressHD.dismiss();
                Log.e("onResponse", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        Toast.makeText(getApplicationContext(), " Record Added Succesfully !!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressHD.dismiss();
                Log.e("UploadMenuList", "Error On failure :- " + Log.getStackTraceString(t));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
