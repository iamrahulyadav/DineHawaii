package com.yberry.dinehawaii.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yberry.dinehawaii.R;

public class PackagesMarketingOptActivity extends AppCompatActivity {

    ImageView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages_marketing_opt);
        ((TextView) findViewById(R.id.headet_text)).setText("Reservation Package");
        init();

        LinearLayout mainView = (LinearLayout) findViewById(R.id.mainView);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void init() {
        next = (ImageView) findViewById(R.id.imageview);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(PackagesMarketingOptActivity.this,TableManagmentActivity.class);
                startActivity(i1);
            }
        });
    }
}