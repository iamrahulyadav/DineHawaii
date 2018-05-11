package com.yberry.dinehawaii.Bussiness.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectServiceTypeActivity extends AppCompatActivity implements View.OnClickListener {

    String radioItem;
    RadioGroup radioGroup;
    CheckBoxPositionModel checkBoxOptionList;
    private ArrayList<CheckBoxPositionModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);
        init();
        checkBoxOptionList = new CheckBoxPositionModel();
        getIntentData();
    }

    private void getIntentData() {

        if (getIntent().getStringExtra("ServerType") != null) {
            String data = getIntent().getStringExtra("ServerType");
            Log.d("ServiceType", data);
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    CheckBoxPositionModel model = new CheckBoxPositionModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    model.setName(object.getString("service_name"));
                    model.setId(object.getString("id"));
                    model.setChckStatus(false);
                    list.add(model);

                    RadioButton button = new RadioButton(SelectServiceTypeActivity.this);
                    button.setId(Integer.parseInt(object.getString("id")));
                    button.setText(object.getString("service_name"));
                    button.setTextColor(getResources().getColor(R.color.blue));
                    RadioGroup.LayoutParams params= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,10, 0,0);
                    button.setLayoutParams(params);
                    //button.setGravity(Gravity.CENTER_VERTICAL);
                    Typeface font = Typeface.createFromAsset(getAssets(), "Raleway-Regular.ttf");
                    button.setTypeface(font);
                    radioGroup.addView(button);

                    Log.d("FoodService", list.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) findViewById(id);
                Log.d("radio", button.getText().toString() + id);
                checkBoxOptionList.setId(id + "");
                checkBoxOptionList.setName(button.getText().toString());

            }
        });

    }


    private void init() {
        setToolbar();
        list = new ArrayList<CheckBoxPositionModel>();
        radioGroup = (RadioGroup) findViewById(R.id.chk_bussineId);

    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Select Service Type");

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
        if (id == R.id.action_filter) {
            // Intent os=new Intent(getApplicationContext(),FoodService.class);
            //startActivity(os);

            if (checkBoxOptionList != null) {
                Intent intent = new Intent();
                intent.putExtra("serviceFoodType", checkBoxOptionList);
//                Log.d("CheckBoxModelValue", checkBoxOptionList.getName());
                setResult(-1, intent);
                finish();

            } else {
                Toast.makeText(SelectServiceTypeActivity.this, "Please select at least on service!", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        /*if (view.getId() == R.id.recyclerView) {

            Log.d("DineRadio", radioItem);
        }*/
    }
}
