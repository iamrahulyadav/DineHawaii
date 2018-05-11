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

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.FragmentIntraction;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackByCustomerFragment extends Fragment {
    FragmentIntraction intraction;

    public FeedbackByCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_by_customer, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Customer Feedbacks");
        }
        init(view);
        return view;
    }

    private void init(View view) {
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.containerFeedback);
        viewPager.setOffscreenPageLimit(3);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Order Feedback");
        tabLayout.getTabAt(1).setText("Reservation Feedback");
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
                    return new OrderFeedbackFragment();
                case 1:
                    return new ReservFeedbackFragment();
            }
            return null;
        }
    }
}
