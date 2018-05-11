package com.yberry.dinehawaii.Customer.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.FragmentIntraction;

public class OrderHistory extends Fragment {
    String TAG = "OrderHistoryOrRese";
    String to = "";
    FragmentIntraction intraction;
    private Context mContext;
    private ViewPager viewPager;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_order_history_or_reservation, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Orders");
        }
        try {
            Bundle bundle = this.getArguments();
            to = bundle.getString("to");
            initCommponent();
        } catch (Exception e) {
            initCommponent();
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentIntraction) {
            intraction = (FragmentIntraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        intraction = null;
    }

    private void initCommponent() {
        mContext = getActivity();
        ((FloatingActionButton) rootView.findViewById(R.id.fab_new)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RestaurantListFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        // PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), mContext);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Pending");
        tabLayout.getTabAt(1).setText("Completed");
        tabLayout.getTabAt(2).setText("Favorites");

        if (to.equalsIgnoreCase("pen"))
            viewPager.setCurrentItem(0);
        else if (to.equalsIgnoreCase("com"))
            viewPager.setCurrentItem(1);
        else if (to.equalsIgnoreCase("fav"))
            viewPager.setCurrentItem(2);
        else
            viewPager.setCurrentItem(0);


    }

    class PagerAdapter extends FragmentPagerAdapter {
        Context context;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return 3;
        }   //size

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PendingOrderFragment();
                case 1:
                    return new CompletedOrderFragment();
                case 2:
                    return new FavouriteOrdersFragment();
            }
            return null;
        }
    }
}
