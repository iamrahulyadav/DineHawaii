package com.yberry.dinehawaii.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SelectFoodTypeActivity extends AppCompatActivity {

    private static final String TAG = "Select_Food_Type";
    public static List<CheckBoxPositionModel> foodlist = new ArrayList<>();
    public static List<String> listAdd;
    CheckBoxPositionModel radioList;
    RadioGroup radioGroup;
    private Context context;

    CustomEditText seachButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food_type);
        setToolbar();
        backButtonListener();
        init();

        if (getIntent().getStringExtra("Foodtype") != null) {

            String data = getIntent().getStringExtra("Foodtype");
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {

                    CheckBoxPositionModel model = new CheckBoxPositionModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    model.setName(object.getString("food_name"));
                    model.setId(object.getString("id"));
                    model.setChckStatus(false);

                    foodlist.add(model);
                    Log.d("FoodService", String.valueOf(foodlist.size()));

                    RadioButton button = new RadioButton(SelectFoodTypeActivity.this);
                    button.setId(Integer.parseInt(object.getString("id")));
                    button.setText(object.getString("food_name"));
                    radioGroup.addView(button);
                    Log.d("FoodService", foodlist.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setListener();
    }

    private void setListener() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) findViewById(id);
                Log.d("radio", button.getText().toString() + id);
                radioList.setId(id + "");
                radioList.setName(button.getText().toString());

            }
        });
    }

    private void init() {
        context = this;
        foodlist = new ArrayList<>();
        radioGroup = (RadioGroup) findViewById(R.id.chk_serviceId);
        radioList = new CheckBoxPositionModel();
      //  seachButton = (CustomEditText) findViewById(R.id.seachButton);
        //seachButton.clearFocus();

    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Food Type");
        // mToolbar.setNavigationIcon(R.mipmap.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

            if (radioList != null) {

                Intent intent = new Intent();
                intent.putExtra("foodtype", radioList);
                Log.d(TAG, "Radio List Name :- " + radioList.getName() + " Id :- " + radioList.getId() );
                setResult(-1, intent);
                finish();
                Log.d(TAG, "ListValue :- " + radioList.toString());

            } else {
                Toast.makeText(getApplicationContext(), "Please select at least on service!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

