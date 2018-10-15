package com.yberry.dinehawaii.Bussiness.Adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.yberry.dinehawaii.Bussiness.Activity.ReservationDetailsActivity;
import com.yberry.dinehawaii.Model.ReservationDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;


public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ViewHolder> {
    private static final String TAG = "ReservationListAdapter";
    private static int REPEAT_COUT = 10;
    private final String listType;
    Context context;
    ArrayList<ReservationDetails> reservList;
    String options[] = {"Add 10 Min", "Close Reservation"};
    private ObjectAnimator anim;

    public ReservationListAdapter(Context context, ArrayList<ReservationDetails> reservList, String listType) {
        this.context = context;
        this.reservList = reservList;
        this.listType = listType;
    }

    @Override
    public ReservationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_table_managment_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ReservationListAdapter.ViewHolder holder, final int position) {
        if (listType.equalsIgnoreCase("waiting")) {
            holder.llBottom.setVisibility(View.GONE);
            holder.tvTable.setText("00");
        }
        final ReservationDetails model = reservList.get(position);
        Log.d(TAG, "onBindViewHolder: position >> " + position + " >> " + model.isBlinked());

        if (model.isBlinked()) {
            holder.tvNoShow.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvNoShow.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
        }

        holder.fNameTextView.setText(model.getName());
        holder.tvMobileNo.setText(model.getMobile());
        holder.tvResvTime.setText(model.getTime().replaceAll("M", "").toLowerCase());
        holder.tvPartySize.setText(model.getPartySize());
        holder.tvTable.setText(model.getTableNumber());
        holder.tvStatus.setText(model.getReservationStatus());

        if (model.getReservationStatus().equalsIgnoreCase("Cancelled")) {
            holder.tvCancel.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvCancel.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvStatus.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
        } else if (model.getReservationStatus().equalsIgnoreCase("Closed")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvStatus.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
        }
        if (model.getReservationStatus().contains("Confirm")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvConfirmed.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvConfirmed.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
        } else if (model.getReservationStatus().contains("Booked")) {
            holder.tvCancel.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvCancel.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvStatus.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            holder.tvConfirmed.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvConfirmed.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            holder.tvCheckin.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvCheckin.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            holder.tvWaitTime.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvWaitTime.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            holder.tvTableReady.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvTableReady.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            holder.tvSeatedBy.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvSeatedBy.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            holder.tvDeposit.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvDeposit.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
            holder.tvNoShow.setTextColor(context.getResources().getColor(R.color.unactiveEvent));
            holder.tvNoShow.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Regular.ttf"));
        }


        if (!listType.equalsIgnoreCase("waiting")) {
            if (model.getSetCheckedIn() == 1) {
                holder.tvCheckin.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.tvCheckin.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
            } else {

            }
            if (!model.getSetWaitTime().equalsIgnoreCase("")) {
                holder.tvWaitTime.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.tvWaitTime.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
            } else {

            }
            if (model.getSetTableReady() == 1) {
                holder.tvTableReady.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.tvTableReady.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
            } else {

            }
            if (!model.getSetSeatedBy().equalsIgnoreCase("")) {
                holder.tvSeatedBy.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.tvSeatedBy.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
            } else {

            }
            if (!model.getSetReschedule().equalsIgnoreCase("")) {
                holder.tvReschedule.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.tvReschedule.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
            } else {

            }
            if (!model.getReservation_amount().equalsIgnoreCase("") && !model.getReservation_amount().equalsIgnoreCase("0")) {
                holder.tvDeposit.setText("Deposit-$" + model.getReservation_amount());
                holder.tvDeposit.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.tvDeposit.setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Bold.ttf"));
            } else {

            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setPressed(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReservationDetails model = reservList.get(position);
                            Intent intent = new Intent(context, ReservationDetailsActivity.class);
                            intent.putExtra("reservation_id", model.getReservId());
                            if (model.isBlinked())
                                intent.setAction("NOSHOW");
                            else
                                intent.setAction("SHOW");
                            context.startActivity(intent);
                        }
                    }, 400);
                }
            });
        }
    }

    private void showAlert(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select an action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return reservList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout llBottom;
        RadioGroup rgAllValue;
        CustomTextView tvPartySize, fNameTextView, tvTable, tvResvTime, tvMobileNo, tvStatus;
        CustomTextView tvConfirmed, tvCheckin, tvWaitTime, tvSeatedBy, tvDeposit, tvNoShow, tvReschedule, tvCancel, tvTableReady;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            tvTable = (CustomTextView) itemView.findViewById(R.id.tvTable);
            tvPartySize = (CustomTextView) itemView.findViewById(R.id.tvPartySize);
            fNameTextView = (CustomTextView) itemView.findViewById(R.id.fNameTextView);
            tvResvTime = (CustomTextView) itemView.findViewById(R.id.tvResvTime);
            tvMobileNo = (CustomTextView) itemView.findViewById(R.id.tvMobileNo);
            tvStatus = (CustomTextView) itemView.findViewById(R.id.tvStatus);
            rgAllValue = (RadioGroup) itemView.findViewById(R.id.radioGroup);
            llBottom = (LinearLayout) itemView.findViewById(R.id.llBottom);

            tvConfirmed = (CustomTextView) itemView.findViewById(R.id.tvConfirmed);
            tvCheckin = (CustomTextView) itemView.findViewById(R.id.tvCheckin);
            tvWaitTime = (CustomTextView) itemView.findViewById(R.id.tvWaitTime);
            tvTableReady = (CustomTextView) itemView.findViewById(R.id.tvTableReady);
            tvSeatedBy = (CustomTextView) itemView.findViewById(R.id.tvSeatedBy);
            tvDeposit = (CustomTextView) itemView.findViewById(R.id.tvDeposit);
            tvNoShow = (CustomTextView) itemView.findViewById(R.id.tvNoShow);
            tvReschedule = (CustomTextView) itemView.findViewById(R.id.tvReschedule);
            tvCancel = (CustomTextView) itemView.findViewById(R.id.tvCancel);
            anim = ObjectAnimator.ofFloat(tvNoShow, "alpha", 0.1f, 1f);
            //  anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(ValueAnimator.INFINITE);
            anim.setDuration(250);
        }
    }
}