package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectJobTitleActivity extends AppCompatActivity {

    private static final String TAG = "SelectJobTitleActivity";
    CheckBoxPositionModel radioList;
    private ArrayList<CheckBoxPositionModel> list;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_job_title);

        setToolbar();
        backButtonListener();
        init();
        getAllJobTitles();
        setListener();
    }

    private void init() {
        list = new ArrayList<CheckBoxPositionModel>();
        radioGroup = (RadioGroup) findViewById(R.id.radio_jobTitle);
        radioList = new CheckBoxPositionModel();
    }

    private void setListener() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) findViewById(id);
                Log.d(TAG, "Selected Radio Button text :- " + button.getText().toString() + "\n And its ID is :- " + id);
                radioList.setId(id + "");
                radioList.setName(button.getText().toString());
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

    private void getAllJobTitles() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLPOSTITLE);
        Log.e(TAG, "Json Request :- " + jsonObject.toString());
        getAllJobTitlesTask(jsonObject);
    }

    private void getAllJobTitlesTask(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(SelectJobTitleActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "onResponse" + response.body().toString());
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
                        list.add(model);

                        Log.d(TAG, "Selcted Job Title :- " + String.valueOf(list.size()));


                        RadioButton button = new RadioButton(SelectJobTitleActivity.this);
                        button.setHighlightColor(Color.GREEN);
                        button.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.gray)));

                        button.setId(Integer.parseInt(object.getString("job_tile_id")));
                        button.setText(object.getString("job_tile"));
                        radioGroup.addView(button);
                        Log.d(TAG, "Selcted Job Title :- " + list.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Select Job");
        // mToolbar.setNavigationIcon(R.mipmap.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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

            if (radioList != null) {

                Intent intent = new Intent();
                intent.putExtra("jobTitle", radioList);

                Log.d(TAG, "Radio List Name :- " + radioList.getName() + " Id :- " + radioList.getId());
                setResult(RESULT_OK, intent);
                finish();
                Log.d(TAG, "ListValue :- " + radioList.toString());

            } else {
                Toast.makeText(getApplicationContext(), "Please select at least one Job Title!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
