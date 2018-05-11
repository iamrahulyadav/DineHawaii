package com.yberry.dinehawaii.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yberry.dinehawaii.R;

public class DeliveryServiceActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    private DrawerLayout drawerLayout;
    ImageView back, imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_service);
        init();
    }
    private void init() {
        context = this;
        drawerLayout = (DrawerLayout) findViewById(R.id.mydrawerlayout);
        imageView = (ImageView)findViewById(R.id.imageview);
        imageView.setOnClickListener(this);
     //   setToolbar();
    }
    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Delivery Service-Distributor & Food Suppliers");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    OpenLeft();
            }
        });
    }

    public void OpenLeft() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT))
            drawerLayout.closeDrawer(Gravity.LEFT);
        else
            drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.imageview){
            Intent intent = new Intent(context, DeliveryServicesNextActivity.class);
            startActivity(intent);
        }
    }
}