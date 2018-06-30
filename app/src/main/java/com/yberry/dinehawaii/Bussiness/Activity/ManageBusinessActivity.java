package com.yberry.dinehawaii.Bussiness.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yberry.dinehawaii.Bussiness.Fragment.ManageBusinessActive;
import com.yberry.dinehawaii.Bussiness.Fragment.ManageBusinessInactive;
import com.yberry.dinehawaii.R;

import java.util.ArrayList;
import java.util.List;

public class ManageBusinessActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_business);

        setToolbar();

        ViewPager viewPager = findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), ManageBusinessActivity.this);
        pagerAdapter.addFragment(new ManageBusinessActive());
        pagerAdapter.addFragment(new ManageBusinessInactive());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Active");
        tabLayout.getTabAt(1).setText("Inactive");
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tvTitle = (TextView) findViewById(R.id.headet_text);
        tvTitle.setText("Manage Business");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    class PagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
        Context context;
        List<Fragment> managerList = new ArrayList<Fragment>();

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return managerList.size();
        }

        public void addFragment(Fragment fragment) {
            managerList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return managerList.get(position);
        }
    }
}
