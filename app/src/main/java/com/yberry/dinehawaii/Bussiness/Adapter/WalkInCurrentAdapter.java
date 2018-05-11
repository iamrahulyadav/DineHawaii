package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.yberry.dinehawaii.Model.ReservationDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

/**
 * Created by Hvantage2 on 6/28/2017.
 */


/**
 * Created by PRINCE 9977123453 on 24-10-2016.
 */

/**
 * Created by PRINCE 9977123453 on 24-10-2016.
 */

public class WalkInCurrentAdapter extends RecyclerView.Adapter<WalkInCurrentAdapter.ViewHolder>  {
    private static final String TAG = "WalkInadapterAdapter";

    Context context;
    ArrayList<ReservationDetails> reservList;
    View.OnClickListener clickListener;

    public WalkInCurrentAdapter(Context context, ArrayList<ReservationDetails> reservList, View.OnClickListener clickListener) {
        this.context = context;
        this.reservList = reservList;
        this.clickListener=clickListener;
    }

    @Override
    public WalkInCurrentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.walikin_list, parent, false);
        WalkInCurrentAdapter.ViewHolder viewHolder = new WalkInCurrentAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WalkInCurrentAdapter.ViewHolder holder, final int position) {


        final ReservationDetails reservationDetails = reservList.get(position);
        Log.v(TAG, "Customer name :- " + reservationDetails.getName());
        Log.v(TAG, "Customer  :- " + reservList.size());

        holder.tableNo.setText(reservationDetails.getTableNumber());
        holder.partySize.setText(reservationDetails.getPartySize());
        // holder.startTym.setText(reservationDetails.getCheckInTime());
        holder.untillTym.setText(reservationDetails.getSeatingTime());
        // holder.dineId.setText(reservationDetails.getDine_id());
        holder.chkData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    holder.chkData.setOnClickListener(clickListener);
                    holder.chkData.setTag(position);
                    holder.chkData.setChecked(b);
                }else {
                    holder.chkData.setChecked(b);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return reservList == null ? 0 : reservList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tableNo, partySize, startTym, untillTym, dineId;
        CheckBox chkData;

        public ViewHolder(View itemView) {
            super(itemView);


            tableNo = (CustomTextView) itemView.findViewById(R.id.tableNo);
            partySize = (CustomTextView) itemView.findViewById(R.id.partySize);
            // startTym = (CustomTextView) itemView.findViewById(R.id.startTym);
            untillTym = (CustomTextView) itemView.findViewById(R.id.untillTym);
            chkData = (CheckBox) itemView.findViewById(R.id.chkList);
            // dineId = (CustomTextView) itemView.findViewById(R.id.DineId);

        }


    }
}