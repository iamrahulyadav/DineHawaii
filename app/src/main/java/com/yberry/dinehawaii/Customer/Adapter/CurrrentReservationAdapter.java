package com.yberry.dinehawaii.Customer.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Customer.Activity.CustomerResDetailActivity;
import com.yberry.dinehawaii.Model.ReservationDataModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;


/**
 * Created by PRINCE 9977123453 on 24-10-2016.
 */

/**
 * Created by PRINCE 9977123453 on 24-10-2016.
 */

public class CurrrentReservationAdapter extends RecyclerView.Adapter<CurrrentReservationAdapter.ViewHolder> {

    private static final String TAG = "Current Reservation";
    Context context;
    ArrayList<ReservationDataModel> reservationData = new ArrayList<ReservationDataModel>();
    String reserve_pre_amt = "";

    public CurrrentReservationAdapter(Context context) {
        this.context = context;


    }

    public CurrrentReservationAdapter(Context context, ArrayList<ReservationDataModel> reservationData) {
        this.context = context;
        this.reservationData = reservationData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_current_reservation, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ReservationDataModel model = reservationData.get(position);
        reserve_pre_amt = model.getPre_amount();
        holder.textViewReservationId.setText(model.getReservation_id());
        holder.textViewBusiName.setText(model.getBusiness_name());
        holder.textViewDate.setText(model.getDate());
        holder.textViewTime.setText(model.getTime());

        holder.reservCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerResDetailActivity.class);
                intent.setAction("current_reserv");
                intent.putExtra("reserv_id", model.getId());
                intent.putExtra("edit_status", model.getEdit_status());
                intent.putExtra("confirm_status", model.getConfirm_status());
                intent.putExtra("cancel_status", model.getDelete_status());
                intent.putExtra("buss_id", model.getBusiness_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reservationData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView textViewReservationId, textViewBusiName, textViewDate, textViewTime;
        CardView reservCard;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewReservationId = (CustomTextView) itemView.findViewById(R.id.textViewReservationId);
            textViewBusiName = (CustomTextView) itemView.findViewById(R.id.textViewBusiName);
            textViewDate = (CustomTextView) itemView.findViewById(R.id.textViewDate);
            textViewTime = (CustomTextView) itemView.findViewById(R.id.textViewTime);
            reservCard = (CardView) itemView.findViewById(R.id.reservcardview);

        }


    }
}