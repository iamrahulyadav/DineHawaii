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
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;

    ArrayList<ReservationDataModel> reservationDataModels = new ArrayList<ReservationDataModel>();

    public HistoryAdapter(Context context) {
        this.context = context;

    }

    public HistoryAdapter(Context context, ArrayList<ReservationDataModel> reservationDataModels) {
        this.context = context;
        this.reservationDataModels = reservationDataModels;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_reservation_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final HistoryAdapter.ViewHolder holder, int position) {

        final ReservationDataModel model = reservationDataModels.get(position);

        holder.textViewReservationId.setText(model.getReservation_id());
        holder.textViewBusiName.setText(model.getBusiness_name());
        holder.textViewDate.setText(model.getDate());
        holder.textViewTime.setText(model.getTime());


        holder.cardViewHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerResDetailActivity.class);
                intent.setAction("history_reserv");
                intent.putExtra("reserv_id", model.getId());
                intent.putExtra("buss_id", model.getBusiness_id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewHist;
        CustomTextView textViewReservationId, textViewBusiName, textViewDate, textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            cardViewHist = (CardView) itemView.findViewById(R.id.cardhistory);
            textViewReservationId = (CustomTextView) itemView.findViewById(R.id.textViewReservationId);
            textViewBusiName = (CustomTextView) itemView.findViewById(R.id.textViewBusiName);
            textViewDate = (CustomTextView) itemView.findViewById(R.id.textViewDate);
            textViewTime = (CustomTextView) itemView.findViewById(R.id.textViewTime);


        }


    }
}