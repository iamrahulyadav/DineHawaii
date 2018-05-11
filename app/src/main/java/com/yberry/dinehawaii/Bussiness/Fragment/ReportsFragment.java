package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportsFragment extends Fragment implements View.OnClickListener {
    private String TAG = "ReportsFragment";
    private Context mContext;
    private LinearLayout llTotalUsers, llTotalOrders, llTotalReservations, llTotalPayments;
    private CustomTextView tvTotalUsers, tvTotalOrders, tvTotalReservations, tvTotalPayments;
    private CustomTextView tvActiveUsers, tvInActiveUsers, tvTotalInHouse, tvTotalTakeout, tvTotalCatering, tvTotalDelivery, tvTodayReserv, tvFutureReserv, tvPastReserv, tvWaitList, tvTotalOrdersPay, tvTotalReservPay;
    private ImageView imgArrow1;

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
        llTotalUsers = (LinearLayout) view.findViewById(R.id.llTotalUsers);
        llTotalOrders = (LinearLayout) view.findViewById(R.id.llTotalOrders);
        llTotalReservations = (LinearLayout) view.findViewById(R.id.llTotalReservations);
        llTotalPayments = (LinearLayout) view.findViewById(R.id.llTotalPayments);

        tvTotalUsers = (CustomTextView) view.findViewById(R.id.tvTotalUsers);
        tvTotalOrders = (CustomTextView) view.findViewById(R.id.tvTotalOrders);
        tvTotalReservations = (CustomTextView) view.findViewById(R.id.tvTotalReservations);
        tvTotalPayments = (CustomTextView) view.findViewById(R.id.tvTotalPayments);

        tvActiveUsers = (CustomTextView) view.findViewById(R.id.tvActiveUsers);
        tvInActiveUsers = (CustomTextView) view.findViewById(R.id.tvInActiveUsers);
        tvTotalInHouse = (CustomTextView) view.findViewById(R.id.tvTotalInHouse);
        tvTotalTakeout = (CustomTextView) view.findViewById(R.id.tvTotalTakeout);
        tvTotalCatering = (CustomTextView) view.findViewById(R.id.tvTotalCatering);
        tvTotalDelivery = (CustomTextView) view.findViewById(R.id.tvTotalDelivery);
        tvTodayReserv = (CustomTextView) view.findViewById(R.id.tvTodayReserv);
        tvFutureReserv = (CustomTextView) view.findViewById(R.id.tvFutureReserv);
        tvPastReserv = (CustomTextView) view.findViewById(R.id.tvPastReserv);
        tvWaitList = (CustomTextView) view.findViewById(R.id.tvWaitList);
        tvTotalOrdersPay = (CustomTextView) view.findViewById(R.id.tvTotalOrdersPay);
        tvTotalReservPay = (CustomTextView) view.findViewById(R.id.tvTotalReservPay);

//        imgArrow1 = (ImageView) view.findViewById(R.id.imgArrow1);

        tvTotalUsers.setOnClickListener(this);
        tvTotalOrders.setOnClickListener(this);
        tvTotalReservations.setOnClickListener(this);
        tvTotalPayments.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvTotalUsers:
                if (llTotalUsers.getVisibility() == View.VISIBLE) {
                    llTotalUsers.setVisibility(View.GONE);
                } else if (llTotalUsers.getVisibility() == View.GONE) {
                    llTotalUsers.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tvTotalOrders:
                if (llTotalOrders.getVisibility() == View.VISIBLE)
                    llTotalOrders.setVisibility(View.GONE);
                else if (llTotalOrders.getVisibility() == View.GONE)
                    llTotalOrders.setVisibility(View.VISIBLE);
                break;
            case R.id.tvTotalReservations:
                if (llTotalReservations.getVisibility() == View.VISIBLE)
                    llTotalReservations.setVisibility(View.GONE);
                else if (llTotalReservations.getVisibility() == View.GONE)
                    llTotalReservations.setVisibility(View.VISIBLE);
                break;
            case R.id.tvTotalPayments:
                if (llTotalPayments.getVisibility() == View.VISIBLE)
                    llTotalPayments.setVisibility(View.GONE);
                else if (llTotalPayments.getVisibility() == View.GONE)
                    llTotalPayments.setVisibility(View.VISIBLE);
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
                        JSONObject jsonObject_total_users = jsonObject.getJSONObject("total_users");
                        String active_user = jsonObject_total_users.getString("active_user");
                        String inactive_user = jsonObject_total_users.getString("inactive_user");


                        JSONObject jsonObject_total_orders = jsonObject.getJSONObject("total_orders");
                        String delivery = jsonObject_total_orders.getString("Delivery");
                        String in_house = jsonObject_total_orders.getString("In-House");
                        String take_out = jsonObject_total_orders.getString("Take-out");
                        String catering = jsonObject_total_orders.getString("Catering");

                        JSONObject jsonObject_total_reservation = jsonObject.getJSONObject("total_reservation");
                        String today = jsonObject_total_reservation.getString("Today");
                        String future = jsonObject_total_reservation.getString("Future");
                        String wait_list = jsonObject_total_reservation.getString("Wait-List");
                        String past = jsonObject_total_reservation.getString("Past");

                        JSONObject jsonobject_total_payments = jsonObject.getJSONObject("Total_Payments");
                        String order_amount = jsonobject_total_payments.getString("Order_Amount");
                        String reservation_amount = jsonobject_total_payments.getString("Reservation_Amount");

                        tvActiveUsers.setText(active_user);
                        tvInActiveUsers.setText(inactive_user);
                        tvTotalDelivery.setText(delivery);
                        tvTotalInHouse.setText(in_house);
                        tvTotalTakeout.setText(take_out);
                        tvTotalCatering.setText(catering);
                        tvTodayReserv.setText(today);
                        tvFutureReserv.setText(future);
                        tvPastReserv.setText(past);
                        tvWaitList.setText(wait_list);
                        tvTotalOrdersPay.setText("$" + order_amount);
                        tvTotalReservPay.setText("$" + reservation_amount);

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