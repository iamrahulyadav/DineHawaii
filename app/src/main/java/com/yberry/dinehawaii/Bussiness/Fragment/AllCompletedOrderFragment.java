package com.yberry.dinehawaii.Bussiness.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;

import java.util.ArrayList;
import java.util.List;


public class AllCompletedOrderFragment extends Fragment {
    String TAG = "AllCompletedOrderFragment";
    private Context context;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    PagerAdapter adapter;
    private String packages = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_open_close, container, false);
        packages = AppPreferencesBuss.getBussiPackagelist(getActivity());
        Log.e(TAG, "onCreateView >> " + packages);
        setTabLayout(view);
        return view;
    }


    private void setTabLayout(View view) {
        adapter = new PagerAdapter(getChildFragmentManager());
        //manage tabs
        if (packages.contains("3")) {
            adapter.addFragment(new CompletedInHouseFragment(), "In-House");
            adapter.addFragment(new CompletedTakeoutFragment(), "Take-Out");
            adapter.addFragment(new CompletedDeliveryOrdersFragment(), "Delivery");
            adapter.addFragment(new CompletedCateringFragment(), "Catering");
        } else
            adapter.addFragment(new CompletedDeliveryOrdersFragment(), "Delivery");


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> tabTitles = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

        public void addFragment(Fragment fragment, String tabTitle) {
            fragments.add(fragment);
            tabTitles.add(tabTitle);
        }
    }

}
