package com.yberry.dinehawaii.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yberry.dinehawaii.R;

public class PackageAndMarkatingOptions extends AppCompatActivity {
ImageView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_and_markating_options);
        setToolbar();
        next = (ImageView) findViewById(R.id.imageview);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intr = new Intent(PackageAndMarkatingOptions.this,PackagesMarketingOptActivity.class);
                startActivity(intr);
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ((TextView) findViewById(R.id.headet_text)).setText("Packages & Markating Options");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}
