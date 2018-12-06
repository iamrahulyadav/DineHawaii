package com.yberry.dinehawaii.Customer.Fragment;


import android.content.Context;
import android.os.Bundle;
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
import com.yberry.dinehawaii.customview.CustomButton;

public class EGiftAndCoupons extends Fragment implements View.OnClickListener {
    FragmentIntraction intraction;
    CustomButton sendGiftCard;
    private Context mContext;
    private ViewPager viewPager;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_coupon, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("E-Gift Cards");
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
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        sendGiftCard = (CustomButton) rootView.findViewById(R.id.sendEgift);
        sendGiftCard.setOnClickListener(this);
        // PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), mContext);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("My E-Gifts");
        tabLayout.getTabAt(1).setText("Sent E-Gifts");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendEgift:
                Fragment fragment = new SendEGiftVoucher();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
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
                    return new AvailableCoupon();
                case 1:
                    return new SentCoupons();
            }
            return null;
        }
    }
}
