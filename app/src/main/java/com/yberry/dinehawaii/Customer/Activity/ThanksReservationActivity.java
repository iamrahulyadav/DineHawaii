package com.yberry.dinehawaii.Customer.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferences;

public class ThanksReservationActivity extends AppCompatActivity {
    private static final String TAG = "Activity";
    String business_id;
    Button gotoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_screen_customer);
        Log.d("business02", AppPreferences.getBusiID(ThanksReservationActivity.this));
        init();
    }

    private void init() {
        gotoHome = (Button) findViewById(R.id.gotoHome);
        gotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThanksReservationActivity.this, CustomerNaviDrawer.class));
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ThanksReservationActivity.this, CustomerNaviDrawer.class));
        finishAffinity();
    }
}
