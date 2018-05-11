package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yberry.dinehawaii.Customer.Activity.CustomerConfirmreservationActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;

import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PRINCE 9977123453 on 02-02-17.
 */

public class FeedbackAndReview_92Fragment extends Fragment {
    private static final String TAG = "Making_Reservation_Act";
    Button complete, submit;
    String business_id, business_name;
    CustomEditText partySize;
    CustomTextView datePicker, timePicker, businessName;
    CheckBox child_high_chair, child_booster_chair;
    String child_booster = "0", child_high = "0";
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Context context;
    View view;

    public FeedbackAndReview_92Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_making_reservation, container, false);
        setHasOptionsMenu(true);
        context = getContext();
        initViews();

        if (!getActivity().getIntent().getStringExtra("business_id").equalsIgnoreCase("")) {

            business_id = getActivity().getIntent().getStringExtra("business_id");
            Log.v(TAG, "Business Id :- " + business_id);
        }

        if (!getActivity().getIntent().getStringExtra("business_name").equalsIgnoreCase("")) {

            business_name = getActivity().getIntent().getStringExtra("business_name");
            Log.v(TAG, "Business Name :- " + business_name);
        }
        return view;
    }

    private void initViews() {

        complete = (Button) view.findViewById(R.id.complete);
        submit = (Button) view.findViewById(R.id.submit1);
        businessName = (CustomTextView) view.findViewById(R.id.businessName);
        partySize = (CustomEditText) view.findViewById(R.id.partySize);
        datePicker = (CustomTextView) view.findViewById(R.id.datePicker);
        timePicker = (CustomTextView) view.findViewById(R.id.timePicker);
        child_high_chair = (CheckBox) view.findViewById(R.id.child_high_chair);
        child_booster_chair = (CheckBox) view.findViewById(R.id.child_booster_chair);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        (DatePickerDialog.OnDateSetListener) getActivity(),
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setCancelColor(getResources().getColor(R.color.colorPrimary));
                dpd.setOkColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        (TimePickerDialog.OnTimeSetListener) getActivity(),
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false);

                timePickerDialog.show(getActivity().getFragmentManager(), "TimePickerDialog");
                timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
                timePickerDialog.setCancelColor(getResources().getColor(R.color.colorPrimary));
                timePickerDialog.setOkColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (child_booster_chair.isChecked()) {
                    child_booster = "1";
                }
                if (child_high_chair.isChecked()) {
                    child_high = "1";
                }

                Log.v(TAG, "child_high :- " + child_high + " child_booster :- " + child_booster);

                String partySizeS = partySize.getText().toString().trim();
                String dateString = datePicker.getText().toString().trim();
                String timeString = timePicker.getText().toString().trim();

                submitRequest(partySizeS, dateString, timeString);

            }
        });
    }

    private void submitRequest(String partySizeS, String date, String time) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.MAKE_BUSINESS_RESERVATION);
        jsonObject.addProperty("business_id", business_id);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
        jsonObject.addProperty("date", date);
        jsonObject.addProperty("time", time);
        jsonObject.addProperty("party_size", partySizeS);
        jsonObject.addProperty("child_high_chair", child_high);
        jsonObject.addProperty("child_booster_chair", child_booster);

        Log.e(TAG, "Request Making Reservation :- " + jsonObject.toString());
        make_business_reservation(jsonObject);

    }

    private void make_business_reservation(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, " Response Making Reservation :- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        String reservation_id = null, date = "", time = null;
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            reservation_id = jsonObject1.getString("reservation_id");
                            date = jsonObject1.getString("date");
                            time = jsonObject1.getString("time");

                            Log.v(TAG, "RESERVATION_ID :- " + reservation_id);

                        }

                       // Intent in = new Intent(context, MakingReservationNextOne.class);
                        Intent in = new Intent(context, CustomerConfirmreservationActivity.class);
                        in.putExtra("order_id", reservation_id);
                        in.putExtra("date", date);
                        in.putExtra("time", time);
                        startActivity(in);

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
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        Log.v(TAG, "You picked the following Date: " + date);
        datePicker.setText(date);
    }


    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = hourOfDay + ":" + minute + ":" + second;
        Log.v(TAG, "You picked the following time: " + time);
        timePicker.setText(time);
    }

}
