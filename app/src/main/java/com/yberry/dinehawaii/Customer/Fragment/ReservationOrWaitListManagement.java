package com.yberry.dinehawaii.Customer.Fragment;


import android.content.Context;
import android.os.Bundle;
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

public class ReservationOrWaitListManagement extends Fragment {
    FragmentIntraction intraction;
    private Context mContext;
    private ViewPager viewPager;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_reservation_or_wait_list_management, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Reservations");
        }
        initCommponent();
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
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Current");
        tabLayout.getTabAt(1).setText("History");
        tabLayout.getTabAt(2).setText("Wait List");


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
                    return new CurrentReservationFragment();
                case 1:
                    return new ReservationHistoryFragment();
                case 2:
                    return new WaitListFragment();
            }

            return null;
        }


    }


}









/*
package com.yberry.dinehawaii.Customer.Fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yberry.dinehawaii.R;

public class ReservationOrWaitListManagement extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reservation_or_wait_list_management, container, false);
        return view;
    }


}
*/
