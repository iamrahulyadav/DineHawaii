package com.yberry.dinehawaii.Bussiness.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Bussiness.Activity.AddNewTableActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;
import java.util.List;


public class ManageTableFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageTableFragment";
    View view;
    ViewPager viewPager;
    FragmentIntraction intraction;
    private CustomTextView addNewTable;

    public ManageTableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage_table, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Manage Tables");
        }
        init();
        return view;
    }

    private void init() {
        addNewTable = (CustomTextView) view.findViewById(R.id.addNewTable);
        addNewTable.setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        pagerAdapter.addFragment(new TableSingleFragment());
        pagerAdapter.addFragment(new TableCombineFragment());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("SINGLE");
        tabLayout.getTabAt(1).setText("COMBINED");
        viewPager.setCurrentItem(0);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewTable:
                openDialog();
                break;
            default:
                break;
        }
    }

    private void openDialog() {
        final String grpname[] = {"Add Single Table", "Add Combined Table"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
        alt_bld.setTitle("Select Table Type");
        alt_bld.setSingleChoiceItems(grpname, -1, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (item == 0) {
                    startActivity(new Intent(getActivity(), AddNewTableActivity.class).setAction("single_add"));
                } else if (item == 1) {
                    startActivity(new Intent(getActivity(), AddNewTableActivity.class).setAction("combine_add"));
                }
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
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
        }   //size

        public void addFragment(Fragment fragment) {
            managerList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return managerList.get(position);
        }
    }
}
