package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportsFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = "ReportsFragment";
    private CustomTextView tvCustomerFilled, tvTotalNoShows, tvTotalPending, tvTotalCompleted, tvPointsEarned, tvPointsIssued, tvPointsTrans, tvEgiftUsed;
    private CustomTextView tvAmtEgiftUsed, tvTotalFeedback, tvFeedbackResolved, tvAvgRating, tvRating1to2, tvRating3, tvRating4to5;
    private Context mContext;
    private CustomTextView tvTotalInHouse, tvTotalTakeout, tvTotalCatering, tvTotalDelivery, tvTotalReservations, tvReservationCanceled, tvReservationFilled;
    private CustomTextView tvTotalReservationsAmt, tvReservationCanceledAmt, tvReservationFilledAmt, tvTotalPendingAmt, tvTotalCompletedAmt, tvTotalInHouseAmt, tvTotalTakeoutAmt, tvTotalCateringAmt, tvTotalDeliveryAmt, tvPointsEarnedAmt, tvPointsIssuedAmt, tvPointsTransAmt;
    private String days = "0";
    private LinearLayout llCustomFilter;
    private Spinner spinnerFilter;
    private CustomTextView tvApply;
    private CustomEditText etDays;
    private CustomTextView tvCustomerFilledAmt;

    public ReportsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_report_business, container, false);
        mContext = getActivity();
        init(view);
        getData();
        return view;
    }

    private void init(View view) {
        tvTotalReservations = (CustomTextView) view.findViewById(R.id.tvTotalReservations);
        tvReservationCanceled = (CustomTextView) view.findViewById(R.id.tvReservationCanceled);
        tvReservationFilled = (CustomTextView) view.findViewById(R.id.tvReservationFilled);
        tvCustomerFilledAmt = (CustomTextView) view.findViewById(R.id.tvCustomerFilledAmt);
        tvCustomerFilled = (CustomTextView) view.findViewById(R.id.tvCustomerFilled);
        tvTotalNoShows = (CustomTextView) view.findViewById(R.id.tvTotalNoShows);
        tvTotalPending = (CustomTextView) view.findViewById(R.id.tvTotalPending);
        tvTotalCompleted = (CustomTextView) view.findViewById(R.id.tvTotalCompleted);
        tvTotalInHouse = (CustomTextView) view.findViewById(R.id.tvTotalInHouse);
        tvTotalTakeout = (CustomTextView) view.findViewById(R.id.tvTotalTakeout);
        tvTotalCatering = (CustomTextView) view.findViewById(R.id.tvTotalCatering);
        tvTotalDelivery = (CustomTextView) view.findViewById(R.id.tvTotalDelivery);
        tvPointsEarned = (CustomTextView) view.findViewById(R.id.tvPointsEarned);
        tvPointsIssued = (CustomTextView) view.findViewById(R.id.tvPointsIssued);
        tvPointsTrans = (CustomTextView) view.findViewById(R.id.tvPointsTrans);
        tvEgiftUsed = (CustomTextView) view.findViewById(R.id.tvEgiftUsed);
        tvAmtEgiftUsed = (CustomTextView) view.findViewById(R.id.tvAmtEgiftUsed);
        tvTotalFeedback = (CustomTextView) view.findViewById(R.id.tvTotalFeedback);
        tvFeedbackResolved = (CustomTextView) view.findViewById(R.id.tvFeedbackResolved);
        tvAvgRating = (CustomTextView) view.findViewById(R.id.tvAvgRating);
        tvRating1to2 = (CustomTextView) view.findViewById(R.id.tvRating1to2);
        tvRating3 = (CustomTextView) view.findViewById(R.id.tvRating3);
        tvRating4to5 = (CustomTextView) view.findViewById(R.id.tvRating4to5);

        tvTotalReservationsAmt = (CustomTextView) view.findViewById(R.id.tvTotalReservationsAmt);
        tvReservationCanceledAmt = (CustomTextView) view.findViewById(R.id.tvReservationCanceledAmt);
        tvReservationFilledAmt = (CustomTextView) view.findViewById(R.id.tvReservationFilledAmt);
        tvTotalPendingAmt = (CustomTextView) view.findViewById(R.id.tvTotalPendingAmt);
        tvTotalCompletedAmt = (CustomTextView) view.findViewById(R.id.tvTotalCompletedAmt);
        tvTotalInHouseAmt = (CustomTextView) view.findViewById(R.id.tvTotalInHouseAmt);
        tvTotalTakeoutAmt = (CustomTextView) view.findViewById(R.id.tvTotalTakeoutAmt);
        tvTotalCateringAmt = (CustomTextView) view.findViewById(R.id.tvTotalCateringAmt);
        tvTotalDeliveryAmt = (CustomTextView) view.findViewById(R.id.tvTotalDeliveryAmt);
        tvPointsEarnedAmt = (CustomTextView) view.findViewById(R.id.tvPointsEarnedAmt);
        tvPointsIssuedAmt = (CustomTextView) view.findViewById(R.id.tvPointsIssuedAmt);
        tvPointsTransAmt = (CustomTextView) view.findViewById(R.id.tvPointsTransAmt);

        spinnerFilter = (Spinner) view.findViewById(R.id.spinnerFilter);
        llCustomFilter = (LinearLayout) view.findViewById(R.id.llCustomFilter);
        tvApply = (CustomTextView) view.findViewById(R.id.tvApply);
        etDays = (CustomEditText) view.findViewById(R.id.etDays);
        tvApply.setOnClickListener(this);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                if (index == 1) {
                    days = "1";
                    getData();
                } else if (index == 7) {
                    days = "7";
                    getData();
                } else if (index == 15) {
                    days = "15";
                    getData();
                } else if (index == 30) {
                    days = "30";
                    getData();
                } else if (index == 5) {
                    llCustomFilter.setVisibility(View.VISIBLE);
                } else {
                    llCustomFilter.setVisibility(View.GONE);
                    getData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvApply:
                if (TextUtils.isEmpty(etDays.getText().toString()))
                    etDays.setError("Enter Days");
                else {
                    days = etDays.getText().toString();
                    Log.e(TAG, "onClick: days >> " + days);
                    getData();
                }
                break;
        }
    }

    private void getData() {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ALLBUSINESSREPORT);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("days", days);
        Log.e(TAG, "getData: Request >> " + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_reports_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();
                Log.e(TAG, "getData: Response >> " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject total_users_obj = jsonObject.getJSONObject("total_users");
                        tvTotalReservations.setText(jsonObject.getString("total_reservation"));
                        tvTotalReservationsAmt.setText(total_users_obj.getString("total_reservation_amt"));
                        tvReservationCanceled.setText(jsonObject.getString("total_reservation_cancelled"));
                        tvReservationCanceledAmt.setText(total_users_obj.getString("total_reservation_cancelled_amt"));
                        tvReservationFilled.setText(jsonObject.getString("total_reservation_filled"));
                        tvReservationFilledAmt.setText(total_users_obj.getString("total_reservation_filled_amt"));
                        tvCustomerFilled.setText(jsonObject.getString("total_customer_filled"));
                        tvCustomerFilledAmt.setText(total_users_obj.getString("total_customer_filled_amt"));
                        tvTotalNoShows.setText(jsonObject.getString("total_no_shows"));
                        tvTotalPending.setText(jsonObject.getString("orders_pending"));
                        tvTotalPendingAmt.setText(total_users_obj.getString("orders_pending_amt"));
                        tvTotalCompleted.setText(jsonObject.getString("orders_completed"));
                        tvTotalCompletedAmt.setText(total_users_obj.getString("orders_completed_amt"));
                        tvTotalInHouse.setText(jsonObject.getString("total_order_inhouse"));
                        tvTotalInHouseAmt.setText(total_users_obj.getString("total_order_inhouse_amt"));
                        tvTotalTakeout.setText(jsonObject.getString("total_order_takeout"));
                        // tvTotalTakeoutAmt.setText(total_users_obj.getString("total_order_takeout_amt"));
                        tvTotalCatering.setText(jsonObject.getString("total_order_catering"));
                        tvTotalCateringAmt.setText(total_users_obj.getString("total_order_catering_amt"));
                        tvTotalDelivery.setText(jsonObject.getString("total_order_home_delivery"));
                        tvTotalDeliveryAmt.setText(total_users_obj.getString("total_order_home_delivery_amt"));
                        tvPointsEarned.setText(jsonObject.getString("loyalty_point_earned"));
                        tvPointsEarnedAmt.setText(total_users_obj.getString("loyalty_point_earned_amt"));
                        tvPointsIssued.setText(jsonObject.getString("loyalty_point_issued"));
                        tvPointsIssuedAmt.setText(total_users_obj.getString("loyalty_point_issued_amt"));
                        tvPointsTrans.setText(jsonObject.getString("loyalty_point_transferred"));
                        tvPointsTransAmt.setText(total_users_obj.getString("loyalty_point_transferred_amt"));
                        tvEgiftUsed.setText(jsonObject.getString("no_of_egift_used"));
                        tvAmtEgiftUsed.setText(jsonObject.getString("amount_of_total_egift_used"));
                        tvTotalFeedback.setText(jsonObject.getString("feedback_total"));
                        tvFeedbackResolved.setText(jsonObject.getString("feedback_resolved"));
                        tvAvgRating.setText(jsonObject.getString("rating_average"));
                        tvRating1to2.setText(jsonObject.getString("rating_range_1_to_2"));
                        tvRating3.setText(jsonObject.getString("rating_range_3"));
                        tvRating4to5.setText(jsonObject.getString("rating_range_4_to_5"));
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}