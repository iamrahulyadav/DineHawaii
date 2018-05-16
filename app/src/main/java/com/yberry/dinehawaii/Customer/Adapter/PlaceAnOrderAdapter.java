package com.yberry.dinehawaii.Customer.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.yberry.dinehawaii.Model.MenuDetail;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;
import java.util.List;

public class PlaceAnOrderAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private Context mContext;

    public PlaceAnOrderAdapter(FragmentManager manager, Context mContext) {
        super(manager);
        this.mContext = mContext;

    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }



    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String string) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(string);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}