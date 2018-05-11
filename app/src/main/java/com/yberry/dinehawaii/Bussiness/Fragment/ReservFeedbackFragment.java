package com.yberry.dinehawaii.Bussiness.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservFeedbackFragment extends Fragment {


    public ReservFeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_reserv_feedback, container, false);
        return view;
    }

}
