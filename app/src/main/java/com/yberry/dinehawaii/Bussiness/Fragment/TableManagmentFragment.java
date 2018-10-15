package com.yberry.dinehawaii.Bussiness.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Bussiness.Activity.TableLayoutActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.FragmentIntraction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TableManagmentFragment extends Fragment {
    private static final String TAG = "TableManagmentFragment";
    View view;
    ViewPager viewPager;
    FragmentIntraction intraction;
    FloatingActionButton fabBtn;

    public TableManagmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_table_managment, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Reservations");
        }
        init();
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


    private void init() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        fabBtn = view.findViewById(R.id.fabBtn);
        PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        pagerAdapter.addFragment(new ResvCurrentFragment());
        pagerAdapter.addFragment(new ResvFutureFragment());
        pagerAdapter.addFragment(new ResvWaitListFragment());
        pagerAdapter.addFragment(new ResvPreviousFragment());
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String today = df.format(date);
        Log.v(TAG, "Todays Date :- " + today);
        tabLayout.getTabAt(0).setText(today + " TODAY");
        tabLayout.getTabAt(1).setText("FUTURE");
        tabLayout.getTabAt(2).setText("WAIT LIST");
        tabLayout.getTabAt(3).setText("PREVIOUS");
        viewPager.setCurrentItem(0);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TableLayoutActivity.class));
            }
        });
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

   /* private void sendLocationAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SendLocation.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//      alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60 * 1000, alarmIntent);
        // alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), alarmIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                1 * 60 * 1000,  // After five minute
                1 * 60 * 1000,  // Every five minute
                alarmIntent);
    }*/
}
