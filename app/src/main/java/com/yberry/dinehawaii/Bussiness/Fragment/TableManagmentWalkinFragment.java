package com.yberry.dinehawaii.Bussiness.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yberry.dinehawaii.R;

public class TableManagmentWalkinFragment extends Fragment {
    View view;
    Context context;
    ViewPager viewPager;

    public
    TableManagmentWalkinFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_table_managnment, container, false);
        context=getActivity();
        init();
        return view;
    }

    private void init() {
        context = getActivity();
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Current(31/12/16)");
        tabLayout.getTabAt(1).setText("Future");
    }

    class PagerAdapter extends FragmentPagerAdapter {
        Context context;
        public PagerAdapter(FragmentManager fm) {
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
                    return new CurrentWalkIn();
                case 1:
                    return new FutureWalkIn();
            }
            return null;
        }
    }
}


