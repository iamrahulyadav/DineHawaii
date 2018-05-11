package com.yberry.dinehawaii.Customer.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yberry.dinehawaii.R;

public class EGiftApproved extends AppCompatActivity {

    private static final String TAG = "EGiftApproved";
    String send_UserEmailId ;
    TextView emailTextView;
    int TIMEOUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egift_approved);

        if (!getIntent().getExtras().getString("send_UserEmailId").equalsIgnoreCase("") ){
            send_UserEmailId = getIntent().getExtras().getString("send_UserEmailId");
        }else {

        }

        setToolbar();
        initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(EGiftApproved.this, CustomerNaviDrawer.class));
                finish();
            }
        },TIMEOUT);
    }

    private void initView() {

        emailTextView = (TextView) findViewById(R.id.emailTextView);
        emailTextView.setText(send_UserEmailId);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("E-Gift Approved");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }
}
