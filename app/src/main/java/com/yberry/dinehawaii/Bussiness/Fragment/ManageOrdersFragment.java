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
import android.widget.TextView;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.FragmentIntraction;

//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 02-02-17.
 */

public class ManageOrdersFragment extends Fragment {
    TextView tvSearchBuss;
    private ImageView imageView;
    FragmentIntraction intraction;

    public ManageOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_managing_order, container, false);
//        headText.setText("MANAGE ORDERS");

        if (intraction != null) {
            intraction.actionbarsetTitle("Orders");
        }
        initViews(view);
        return view;
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


    private void initViews(View view)
    {
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.containerHome);
        viewPager.setOffscreenPageLimit(3);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("PENDING");
        tabLayout.getTabAt(1).setText("COMPLETED");
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
                    return new AllPendingOrderFragment();
                case 1:
                    return new AllCompletedOrderFragment();
            }
            return null;
        }
    }
}