package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
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
import com.yberry.dinehawaii.Bussiness.Adapter.CheckBoxOptionAdapter;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* ~~~~~~~~~~~~~~~~~~~~~~~~  Screen No 42 B ~~~~~~~~~~~~~~~~~~~~~~~~*/

public class EmployeesPosition extends AppCompatActivity {
    private static final String TAG = "EmployeesPosition";
    Context context;
    public static List<CheckBoxPositionModel> list = new ArrayList<>();
    CheckBoxOptionAdapter ad;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    private ImageView back;
    private String[] position_ids;
    CustomTextView tvaddpostion;
    Dialog popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_position);
        ButterKnife.bind(this);
        if (getIntent().getAction().equalsIgnoreCase("udpate")) ;
        {
            String position_id = getIntent().getStringExtra("position_id");
            Log.e(TAG, "onCreate: emp_duties>> " + position_id);
            if (!position_id.equalsIgnoreCase("")) {
                position_ids = position_id.split(",");
                Log.e(TAG, "onCreate: duty_list >> " + position_ids.length);
            }
        }
        init();
        getallpos();


    }

    private void getallpos() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLPOSTITLE);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(EmployeesPosition.this));
        Log.e("", "Json Request :- " + jsonObject.toString());
        getAllJobTitlesTask(jsonObject);
    }

    private void init() {
        context = this;
        setToolbar();
        context = this;
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        tvaddpostion = (CustomTextView) findViewById(R.id.tvaddpostion);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(EmployeesPosition.this));
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) list, new CheckBoxOptionAdapter.setOnClickListener() {
            @Override
            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

            }
        });
        recyclerView.setAdapter(ad);
        
        tvaddpostion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPosDialog();
            }
        });
    }

    private void showAddPosDialog() {
             popup = new Dialog(context);
            popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popup.setCancelable(false);
            popup.setCanceledOnTouchOutside(false);
            popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popup.setContentView(R.layout.food_category_dialog);
            final CustomEditText foodtype = (CustomEditText) popup.findViewById(R.id.new_category);
            final CustomTextView subtitle = (CustomTextView) popup.findViewById(R.id.addcategory);
            final CustomTextView title = (CustomTextView) popup.findViewById(R.id.foodTitle);
            title.setText("Employee Position");
            subtitle.setText("Add Employee Position");
        foodtype.setHint("Enter position");
            popup.findViewById(R.id.popupclose).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();
                }
            });
            popup.findViewById(R.id.cat_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(foodtype.getText().toString())) {
                        foodtype.setError("Enter Position");
                    } else {
                        if (Util.isNetworkAvailable(context)) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.GENERALAPI.ADDEMPPOS);
                            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
                            jsonObject.addProperty("position", foodtype.getText().toString());
                            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
                            Log.e(TAG, "add emppos json" + jsonObject.toString());
                            addNewPosApi(jsonObject);
                        } else {
                            Toast.makeText(context, "Please Connect Internet", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            popup.show();
    }

    private void addNewPosApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_add_pos_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "get addpos response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        popup.dismiss();
                        list.clear();
                       getallpos();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        popup.dismiss();
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        popup.dismiss();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Employee Positions");
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

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("employee_position", checkBoxOptionList);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllJobTitlesTask(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(EmployeesPosition.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e("", "onResponse" + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        CheckBoxPositionModel model = new CheckBoxPositionModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        model.setId(object.getString("job_tile_id"));
                        model.setName(object.getString("job_tile"));
                        model.setChckStatus(false);
                        if (position_ids != null)
                            if (position_ids.length > 0) {
                                for (int j = 0; j < position_ids.length; j++) {
                                    if (position_ids[j].equalsIgnoreCase(model.getId())) {
                                        model.setChckStatus(true);
                                        Log.e(TAG, "onResponse: model >> " + model);
                                    }
                                }
                            }

                        list.add(model);

                        CheckBoxOptionAdapter ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) list, new CheckBoxOptionAdapter.setOnClickListener() {
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
                Log.e("", "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }
}
