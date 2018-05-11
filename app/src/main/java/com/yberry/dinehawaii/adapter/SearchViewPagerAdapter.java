package com.yberry.dinehawaii.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yberry.dinehawaii.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAI on 12/30/2016.
 */

public class SearchViewPagerAdapter  extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private Context mContext;
    int[] imageResId = new int[]{R.drawable.dropd, R.drawable.dropd, R.drawable.dropd, R.drawable.dropd, R.drawable.dropd};
    String [] title = new String[]{ "Filter Prices","Food Type", "Distance","Take out","Other"};

    public SearchViewPagerAdapter(FragmentManager manager , Context mContext) {
        super(manager);
        this.mContext=mContext;

    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
      mFragmentList.add(fragment);
       // switch (fragment){

     //   }


    }

//    @Override
//    public CharSequence getPageTitle(int position) {
////        Drawable image = ContextCompat.getDrawable(mContext, imageResId[position]);
////        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
////        SpannableString sb = new SpannableString(" "+title[position]);
////        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
////        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        String title1 = customView(position);
//        return title1;
//    }
    public View  customView(int position){
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView title1=(TextView)v.findViewById(R.id.txt_tabTitle);
        ImageView imageView=(ImageView)v.findViewById(R.id.imgV_tab);
        title1.setText(title[position]);
        imageView.setImageResource(imageResId[position]);
        return v;

    }
}