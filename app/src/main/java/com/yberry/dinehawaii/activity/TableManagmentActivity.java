package com.yberry.dinehawaii.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yberry.dinehawaii.Bussiness.Activity.ReservationDeailsActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Bussiness.Fragment.ResvWaitListFragment;
import com.yberry.dinehawaii.Bussiness.Fragment.ResvFutureFragment;

public class TableManagmentActivity extends AppCompatActivity  implements View.OnClickListener {

    private Context context;
    private DrawerLayout drawerLayout;
    private ImageView back;
    private ImageView text_continue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_managment);
        init();
    }
    private void init() {
        context = this;
        text_continue = (ImageView) findViewById(R.id.imageview);
        text_continue.setOnClickListener(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), TableManagmentActivity.this);

        viewPager.setAdapter(pagerAdapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.getTabAt(0).setText("Current(31/12/16)");
        tabLayout.getTabAt(1).setText("Future");
        //setToolbar();
    }
    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Table Managment Check-In");
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLeft();
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
        if(v.getId() == R.id.imageview){
            Intent i = new Intent(TableManagmentActivity.this,ReservationDeailsActivity.class);
            startActivity(i);
        }
    }
    class PagerAdapter extends FragmentPagerAdapter {
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
        }   //size

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new ResvWaitListFragment();
                case 1:
                    return new ResvFutureFragment();
            }
            return null;
        }
    }
}
