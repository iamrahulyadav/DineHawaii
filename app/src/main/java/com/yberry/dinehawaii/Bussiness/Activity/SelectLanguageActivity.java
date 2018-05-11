package com.yberry.dinehawaii.Bussiness.Activity;


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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomRadioButton;

/* ~~~~~~~~~~~~~~~~~~~~~~~~  Screen No 42 E ~~~~~~~~~~~~~~~~~~~~~~~~ */

public class SelectLanguageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BussinesInforLangAct";
    CustomRadioButton radioButton;
    String selected_language;
    private Context context;
    private RadioGroup radioGroup;
    private ImageView back;
    private String buttonValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_information_language);

        init();
        listner();
        setToolbar();

    }

    private void listner() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = (CustomRadioButton) findViewById(id);
                buttonValue = radioButton.getText().toString();
                Log.d(TAG, "RadiobuttonValue :-" + radioButton.getText().toString());
            }
        });
    }

    private void init() {

        context = this;
        radioGroup = ((RadioGroup) findViewById(R.id.radioGroup));

    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Select Language");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // mToolbar.setNavigationIcon(R.mipmap.back);
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

            selected_language = radioButton.getText().toString();
            Log.d(TAG, "Selected Language : -" + selected_language);
            if (!selected_language.equalsIgnoreCase("")) {
                Log.d(TAG, "Selected Language inside if :-" + selected_language);

                Intent intent = new Intent();
                intent.putExtra("language", selected_language);
                setResult(100, intent);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Please select a language !!!!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.radioGroup) {
        }
    }
}
