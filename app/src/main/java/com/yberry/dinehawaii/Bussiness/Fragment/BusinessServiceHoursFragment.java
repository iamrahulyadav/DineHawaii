package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomRadioButton;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessServiceHoursFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BusinessInfoDayFragment";
    RadioGroup radioGroup, radio1, grpDelAssign;
    LinearLayout llYesNo;
    JsonObject jsonObject;
    CustomRadioButton checkYes, checkNo, checkYes1, checkNo1, rbdelAuto, rbDelManual;
    CustomEditText hoursEditTextValue;
    View rootView;
    FragmentIntraction intraction;
    private Button submitButton;
    private CustomTextView sunAD;
    private CustomTextView sunBF;
    private CustomTextView sunL;
    private CustomTextView sunD;
    private CustomTextView sunOther;
    private CustomTextView monAD;
    private CustomTextView monBF;
    private CustomTextView monL;
    private CustomTextView monD;
    private CustomTextView monOther;
    private CustomTextView tueAD;
    private CustomTextView tueBF;
    private CustomTextView tueL;
    private CustomTextView tueD;
    private CustomTextView tueOther;
    private CustomTextView wedAD;
    private CustomTextView wedBF;
    private CustomTextView wedL;
    private CustomTextView wedD;
    private CustomTextView wedOther;
    private CustomTextView thusAD;
    private CustomTextView thusBF;
    private CustomTextView thusL;
    private CustomTextView thusD;
    private CustomTextView thusOther;
    private CustomTextView friAD;
    private CustomTextView friBF;
    private CustomTextView friL;
    private CustomTextView friD;
    private CustomTextView friOther;
    private CustomTextView satAD;
    private CustomTextView satBF;
    private CustomTextView satL;
    private CustomTextView satD;
    private CustomTextView satOther;
    private CustomEditText amountEtDay;
    private Dialog dialog;
    private Context mContext;
    private int mHour, mMinute;
    private String next, is_reservation_deposit = "1", is_loyalalty_program = "1", assign_del_status = "1";
    private CustomEditText etAvgPrice;
    private Spinner spinnerPriceRange;

    public BusinessServiceHoursFragment() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_business_information_day, container, false);
        mContext = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("Service Hours");
        }
        initCommponent(rootView);
        setSpinner();


        RelativeLayout mainView = (RelativeLayout) rootView.findViewById(R.id.mainView);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        showdata();
        return rootView;


    }

    private void setSpinner() {
        ArrayList<String> priceRangeListString = new ArrayList<String>();
        priceRangeListString.add("$(min $10)");
        priceRangeListString.add("$$(min $20)");
        priceRangeListString.add("$$$(min $30)");
        priceRangeListString.add("$$$$(over $30)");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.simple_list_item_1, priceRangeListString);
        spinnerPriceRange.setAdapter(adapter1);
        spinnerPriceRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0)
                    etAvgPrice.setText("$");
                else if (position == 1)
                    etAvgPrice.setText("$$");
                else if (position == 2)
                    etAvgPrice.setText("$$$");
                else if (position == 3)
                    etAvgPrice.setText("$$$$");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initCommponent(final View rootView) {
        submitButton = (Button) rootView.findViewById(R.id.btn_submit_days);
        sunAD = ((CustomTextView) rootView.findViewById(R.id.txt_s1));
        sunBF = ((CustomTextView) rootView.findViewById(R.id.txt_s2));
        sunL = ((CustomTextView) rootView.findViewById(R.id.txt_s3));
        sunD = ((CustomTextView) rootView.findViewById(R.id.txt_s4));
        sunOther = ((CustomTextView) rootView.findViewById(R.id.txt_s5));
        monAD = ((CustomTextView) rootView.findViewById(R.id.txt_m1));
        monBF = ((CustomTextView) rootView.findViewById(R.id.txt_m2));
        monL = ((CustomTextView) rootView.findViewById(R.id.txt_m3));
        monD = ((CustomTextView) rootView.findViewById(R.id.txt_m4));
        monOther = ((CustomTextView) rootView.findViewById(R.id.txt_m5));
        tueAD = ((CustomTextView) rootView.findViewById(R.id.txt_tue1));
        tueBF = ((CustomTextView) rootView.findViewById(R.id.txt_tue2));
        tueL = ((CustomTextView) rootView.findViewById(R.id.txt_tue3));
        tueD = ((CustomTextView) rootView.findViewById(R.id.txt_tue4));
        tueOther = ((CustomTextView) rootView.findViewById(R.id.txt_tue5));
        wedAD = ((CustomTextView) rootView.findViewById(R.id.txt_w1));
        wedBF = ((CustomTextView) rootView.findViewById(R.id.txt_w2));
        wedL = ((CustomTextView) rootView.findViewById(R.id.txt_w3));
        wedD = ((CustomTextView) rootView.findViewById(R.id.txt_w4));
        wedOther = ((CustomTextView) rootView.findViewById(R.id.txt_w5));

        thusAD = ((CustomTextView) rootView.findViewById(R.id.txt_th1));
        thusBF = ((CustomTextView) rootView.findViewById(R.id.txt_th2));
        thusL = ((CustomTextView) rootView.findViewById(R.id.txt_th3));
        thusD = ((CustomTextView) rootView.findViewById(R.id.txt_th4));
        thusOther = ((CustomTextView) rootView.findViewById(R.id.txt_th5));

        friAD = ((CustomTextView) rootView.findViewById(R.id.txt_fri1));
        friBF = ((CustomTextView) rootView.findViewById(R.id.txt_fri2));
        friL = ((CustomTextView) rootView.findViewById(R.id.txt_fri3));
        friD = ((CustomTextView) rootView.findViewById(R.id.txt_fri4));
        friOther = ((CustomTextView) rootView.findViewById(R.id.txt_fri5));

        satAD = ((CustomTextView) rootView.findViewById(R.id.txt_sat1));
        satBF = ((CustomTextView) rootView.findViewById(R.id.txt_sat2));
        satL = ((CustomTextView) rootView.findViewById(R.id.txt_sat3));
        satD = ((CustomTextView) rootView.findViewById(R.id.txt_sat4));
        satOther = ((CustomTextView) rootView.findViewById(R.id.txt_sat5));

        radioGroup = (RadioGroup) rootView.findViewById(R.id.radiogroup);
        radio1 = (RadioGroup) rootView.findViewById(R.id.radio1);
        grpDelAssign = (RadioGroup) rootView.findViewById(R.id.grpDelAssign);


        rbdelAuto = (CustomRadioButton) rootView.findViewById(R.id.rbdelAuto);
        rbDelManual = (CustomRadioButton) rootView.findViewById(R.id.rbDelManual);
        checkYes = (CustomRadioButton) rootView.findViewById(R.id.checkYes);
        checkNo = (CustomRadioButton) rootView.findViewById(R.id.checkNo);
        checkYes1 = (CustomRadioButton) rootView.findViewById(R.id.checkYes1);
        checkNo1 = (CustomRadioButton) rootView.findViewById(R.id.checkNo1);
        amountEtDay = (CustomEditText) rootView.findViewById(R.id.amountEt);
        etAvgPrice = (CustomEditText) rootView.findViewById(R.id.etAvgPrice);
        hoursEditTextValue = (CustomEditText) rootView.findViewById(R.id.hoursEditTextValue);
        hoursEditTextValue.setFilters(new InputFilter[]{new InputFilters(0, 50)});
        llYesNo = (LinearLayout) rootView.findViewById(R.id.llYesNo);
        spinnerPriceRange = (Spinner) rootView.findViewById(R.id.spinnerPriceRange);

        checkYes1.setChecked(true);

        llYesNo.setVisibility(View.VISIBLE);

        checkYes1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkYes1.isChecked()) {
                    llYesNo.setVisibility(View.VISIBLE);
                } else {
                    llYesNo.setVisibility(View.GONE);
                }
            }
        });

        checkNo1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkNo1.isChecked()) {
                    llYesNo.setVisibility(View.GONE);
                } else {
                    llYesNo.setVisibility(View.VISIBLE);
                }
            }
        });

        checkNo1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkNo1.isChecked()) {
                    llYesNo.setVisibility(View.GONE);
                } else {
                    llYesNo.setVisibility(View.VISIBLE);
                }
            }
        });

        sunAD.setOnClickListener(this);
        sunBF.setOnClickListener(this);
        sunL.setOnClickListener(this);
        sunD.setOnClickListener(this);
        sunOther.setOnClickListener(this);
        monAD.setOnClickListener(this);
        monBF.setOnClickListener(this);
        monL.setOnClickListener(this);
        monD.setOnClickListener(this);
        monOther.setOnClickListener(this);
        tueAD.setOnClickListener(this);
        tueBF.setOnClickListener(this);
        tueL.setOnClickListener(this);
        tueD.setOnClickListener(this);
        tueOther.setOnClickListener(this);
        wedAD.setOnClickListener(this);
        wedBF.setOnClickListener(this);
        wedL.setOnClickListener(this);
        wedD.setOnClickListener(this);
        wedOther.setOnClickListener(this);
        thusAD.setOnClickListener(this);
        thusBF.setOnClickListener(this);
        thusL.setOnClickListener(this);
        thusD.setOnClickListener(this);
        thusOther.setOnClickListener(this);
        friAD.setOnClickListener(this);
        friBF.setOnClickListener(this);
        friL.setOnClickListener(this);
        friD.setOnClickListener(this);
        thusD.setOnClickListener(this);
        satAD.setOnClickListener(this);
        satBF.setOnClickListener(this);
        satL.setOnClickListener(this);
        satD.setOnClickListener(this);
        satOther.setOnClickListener(this);
        jsonObject = new JsonObject();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (radioGroup.isSelected()) {
                } else {
                    jsonObject.addProperty("Loyalty_Program", "1");
                    jsonObject.addProperty("Reservation_Deposit", "1");
                }*/
                if (llYesNo.getVisibility() == View.VISIBLE && amountEtDay.getText().toString().isEmpty()) {
                    amountEtDay.setText("0");
                    Toast.makeText(getContext(), "Amount per guest_icon is required", Toast.LENGTH_SHORT).show();
                } else if (llYesNo.getVisibility() == View.VISIBLE && hoursEditTextValue.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Cancellation lead time is required", Toast.LENGTH_SHORT).show();
                } else if (etAvgPrice.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Price range per cusomer is required", Toast.LENGTH_SHORT).show();
                } else {
                    isValidation();
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                CustomRadioButton button = (CustomRadioButton) rootView.findViewById(id);
                Log.d("radio", button.getText().toString() + id);
                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    //jsonObject.addProperty("Loyalty_Program", "1");
                    is_loyalalty_program = "1";
                } else {
                    is_loyalalty_program = "0";
                    //jsonObject.addProperty("Loyalty_Program", "0");
                }
            }
        });

        radio1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                CustomRadioButton button = (CustomRadioButton) rootView.findViewById(id);
                Log.d("radio", button.getText().toString() + id);

                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    //jsonObject.addProperty("Reservation_Deposit", "1");
                    is_reservation_deposit = "1";
                } else {
                    is_reservation_deposit = "0";
                    jsonObject.addProperty("Reservation_Deposit", "0");
                }
            }
        });
        grpDelAssign.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbdelAuto)
                    assign_del_status = "0";
                else if (radioGroup.getCheckedRadioButtonId() == R.id.rbDelManual)
                    assign_del_status = "1";
                else
                    assign_del_status = "1";
            }
        });

    }

    @SuppressLint("LongLogTag")

    private void customDialog(final TextView textView) {


        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_showpopup);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        final ImageView closeButton = (ImageView) dialog.findViewById(R.id.btnCancel);
        final TextView txt_selectTimeTo = (TextView) dialog.findViewById(R.id.txt_selectTimeTo);
        final TextView txt_selectTimeFrom = (TextView) dialog.findViewById(R.id.txt_selectTimeFrom);

        if (TextUtils.isEmpty(txt_selectTimeTo.getText().toString())) {
            textView.setText(txt_selectTimeFrom.getText().toString());
        }

        txt_selectTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar now = Calendar.getInstance();
                mHour = now.get(Calendar.HOUR);
                mMinute = now.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                SimpleDateFormat _24HourSDF = null, _12HourSDF = null;
                                Date _24HourDt = null;
                                String time = hourOfDay + ":" + minute + ":" + second;
                                try {
                                    _24HourSDF = new SimpleDateFormat("HH:mm");
                                    _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    _24HourDt = _24HourSDF.parse(time);
                                    Log.v(TAG, "24 hour format :- " + _24HourDt.toString());
                                    Log.v(TAG, "12 hour format :- " + _12HourSDF.format(_24HourDt).toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Log.v(TAG, "You picked the following time from: " + _12HourSDF.format(_24HourDt).toString());

                                txt_selectTimeTo.setText(_12HourSDF.format(_24HourDt).toString());
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false);

                timePickerDialog.show(getActivity().getFragmentManager(), "TimePickerDialog");
                timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
                timePickerDialog.setCancelColor(getResources().getColor(R.color.colorPrimary));
                timePickerDialog.setOkColor(getResources().getColor(R.color.colorPrimary));

            }
        });

        txt_selectTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar now = Calendar.getInstance();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                SimpleDateFormat _24HourSDF = null, _12HourSDF = null;
                                Date _24HourDt = null;
                                String time = hourOfDay + ":" + minute + ":" + second;

                                try {
                                    _24HourSDF = new SimpleDateFormat("HH:mm");
                                    _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    _24HourDt = _24HourSDF.parse(time);
                                    Log.v(TAG, "24 hour format :- " + _24HourDt.toString());
                                    Log.v(TAG, "12 hour format :- " + _12HourSDF.format(_24HourDt).toString());

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Log.v(TAG, "You picked the following time from: " + _12HourSDF.format(_24HourDt).toString());

                                txt_selectTimeFrom.setText(_12HourSDF.format(_24HourDt).toString());

                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false);

                timePickerDialog.show(getActivity().getFragmentManager(), "TimePickerDialog");
                timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
                timePickerDialog.setCancelColor(getResources().getColor(R.color.colorPrimary));
                timePickerDialog.setOkColor(getResources().getColor(R.color.colorPrimary));

                /*timePickerDialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        String time = hourOfDay + ":" + minute + ":" + second;

                        try {
                            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm:ss a");
                            Date _24HourDt = _24HourSDF.parse(time);
                            Log.v(TAG, "24 hour format :- " + _24HourDt.toString());
                            Log.v(TAG, "12 hour format :- " + _12HourSDF.format(_24HourDt));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.v(TAG, "You picked the following time from: " + time);
                        txt_selectTimeFrom.setText(time);
                    }
                });*/
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button button = (Button) dialog.findViewById(R.id.tvdone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_selectTimeTo.getText().toString().trim() != "" && txt_selectTimeFrom.getText().toString().trim() != "") {
                    textView.setText(txt_selectTimeFrom.getText().toString().trim() + "-" + txt_selectTimeTo.getText().toString().trim());
                    textView.setTextColor(getResources().getColor(R.color.gray));

                } else {
                    Toast.makeText(getContext(), "Select time", Toast.LENGTH_SHORT).show();
                }
                //                if(TextUtils.isEmpty(txt_selectTimeTo.getText().toString())) {
//                    Toast.makeText(mContext,"please select time",Toast.LENGTH_SHORT).show();
//                }
                dialog.cancel();
            }
        });
        dialog.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_s1:
                customDialog(sunAD);
                break;
            case R.id.txt_s2:
                customDialog(sunBF);
                break;
            case R.id.txt_s3:
                customDialog(sunL);
                break;
            case R.id.txt_s4:
                customDialog(sunD);
                break;
            case R.id.txt_s5:
                customDialog(sunOther);
                break;
            case R.id.txt_m1:
                customDialog(monAD);
                break;
            case R.id.txt_m2:
                customDialog(monBF);
                break;
            case R.id.txt_m3:
                customDialog(monL);
                break;
            case R.id.txt_m4:
                customDialog(monD);
                break;
            case R.id.txt_m5:
                customDialog(monOther);
                break;
            case R.id.txt_tue1:
                customDialog(tueAD);
                break;
            case R.id.txt_tue2:
                customDialog(tueBF);
                break;
            case R.id.txt_tue3:
                customDialog(tueL);
                break;
            case R.id.txt_tue4:
                customDialog(tueD);
                break;
            case R.id.txt_tue5:
                customDialog(tueOther);
                break;
            case R.id.txt_w1:
                customDialog(wedAD);
                break;
            case R.id.txt_w2:
                customDialog(wedBF);
                break;
            case R.id.txt_w3:
                customDialog(wedL);
                break;
            case R.id.txt_w4:
                customDialog(wedD);
                break;
            case R.id.txt_w5:
                customDialog(wedOther);
                break;
            case R.id.txt_th1:
                customDialog(thusAD);
                break;
            case R.id.txt_th2:
                customDialog(thusBF);
                break;
            case R.id.txt_th3:
                customDialog(thusL);
                break;
            case R.id.txt_th4:
                customDialog(thusD);
                break;
            case R.id.txt_th5:
                customDialog(thusOther);
                break;
            case R.id.txt_fri1:
                customDialog(friAD);
                break;
            case R.id.txt_fri2:
                customDialog(friBF);
                break;
            case R.id.txt_fri3:
                customDialog(friL);
                break;
            case R.id.txt_fri4:
                customDialog(friD);
                break;
            case R.id.txt_fri5:
                customDialog(friOther);
                break;
            case R.id.txt_sat1:
                customDialog(satAD);
                break;
            case R.id.txt_sat2:
                customDialog(satBF);
                break;
            case R.id.txt_sat3:
                customDialog(satL);
                break;
            case R.id.txt_sat4:
                customDialog(satD);
                break;
            case R.id.txt_sat5:
                customDialog(satOther);
                break;
            default:
                break;
        }
    }

    private void isValidation() {
        if (Util.isNetworkAvailable(mContext)) {
            JsonObject object = new JsonObject();
            object.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.UPDATEBUSINESS42F);
            object.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            object.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            object.addProperty("sun", "1");
            object.addProperty("sun_all", sunAD.getText().toString());
            object.addProperty("sun_breakfast", sunBF.getText().toString());
            object.addProperty("sun_lunch", sunL.getText().toString());
            object.addProperty("sun_dinner", sunD.getText().toString());
            object.addProperty("sun_other", sunOther.getText().toString());
            object.addProperty("mon", "1");
            object.addProperty("mon_all", monAD.getText().toString());
            object.addProperty("mon_breakfast", monBF.getText().toString());
            object.addProperty("mon_lunch", monL.getText().toString());
            object.addProperty("mon_dinner", monD.getText().toString());
            object.addProperty("mon_other", monOther.getText().toString());
            object.addProperty("tue", "1");
            object.addProperty("tue_all", tueAD.getText().toString());
            object.addProperty("tue_breakfast", tueBF.getText().toString());
            object.addProperty("tue_lunch", tueL.getText().toString());
            object.addProperty("tue_dinner", tueD.getText().toString());
            object.addProperty("tue_other", tueOther.getText().toString());
            object.addProperty("wed", "1");
            object.addProperty("wed_all", wedAD.getText().toString());
            object.addProperty("wed_breakfast", wedBF.getText().toString());
            object.addProperty("wed_lunch", wedL.getText().toString());
            object.addProperty("wed_dinner", wedD.getText().toString());
            object.addProperty("wed_other", wedOther.getText().toString());
            object.addProperty("thus", "1");
            object.addProperty("thu_all", thusAD.getText().toString());
            object.addProperty("thu_breakfast", thusBF.getText().toString());
            object.addProperty("thu_lunch", thusL.getText().toString());
            object.addProperty("thu_dinner", thusD.getText().toString());
            object.addProperty("thu_other", thusOther.getText().toString());
            object.addProperty("fri", "1");
            object.addProperty("fri_all", friAD.getText().toString());
            object.addProperty("fri_breakfast", friBF.getText().toString());
            object.addProperty("fri_lunch", friL.getText().toString());
            object.addProperty("fri_dinner", friD.getText().toString());
            object.addProperty("fri_other", friOther.getText().toString());
            object.addProperty("sat", "1");
            object.addProperty("sat_all", satAD.getText().toString());
            object.addProperty("sat_breakfast", satBF.getText().toString());
            object.addProperty("sat_lunch", satL.getText().toString());
            object.addProperty("sat_dinner", satD.getText().toString());
            object.addProperty("sat_other", satOther.getText().toString());
            object.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
            object.addProperty("reservation_price", amountEtDay.getText().toString().trim());
            object.addProperty("Price_Range", "0");
            object.addProperty("Canclelation_Time", hoursEditTextValue.getText().toString().trim());
            object.addProperty("Amount_Per_Guest", amountEtDay.getText().toString().trim());
            object.addProperty("Reservation_Deposit", is_reservation_deposit);
            object.addProperty("Loyalty_Program", is_loyalalty_program);
            object.addProperty("avgPrice", etAvgPrice.getText().toString());
            object.addProperty("order_assign_status", assign_del_status);


            Log.e(TAG, "updateData request>>>>>:" + object.toString());
            updateData(object);

        } else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }

    private void updateData(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "updateData Response>>>>>>> " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject sundayJsonObject = jsonObject1.getJSONObject("sunday");
                            JSONObject mondayJsonObject = jsonObject1.getJSONObject("monday");
                            JSONObject tuesdayJsonObject = jsonObject1.getJSONObject("tuesday");
                            JSONObject wednesdayJsonObject = jsonObject1.getJSONObject("wednesday");
                            JSONObject thursdayJsonObject = jsonObject1.getJSONObject("thursday");
                            JSONObject fridayJsonObject = jsonObject1.getJSONObject("friday");
                            JSONObject saturdayJsonObject = jsonObject1.getJSONObject("saturday");


                            sundayJsonObject.getString("dayKey");
                            sundayJsonObject.getString("sun_all");
                            sundayJsonObject.getString("sun_breakfast");
                            sundayJsonObject.getString("sun_lunch");
                            sundayJsonObject.getString("sun_dinner");
                            sundayJsonObject.getString("sun_other");

                            mondayJsonObject.getString("dayKey");
                            mondayJsonObject.getString("monday_all");
                            mondayJsonObject.getString("monday_breakfast");
                            mondayJsonObject.getString("monday_lunch");
                            mondayJsonObject.getString("monday_dinner");
                            mondayJsonObject.getString("monday_other");

                            tuesdayJsonObject.getString("dayKey");
                            tuesdayJsonObject.getString("tue_all");
                            tuesdayJsonObject.getString("tue_breakfast");
                            tuesdayJsonObject.getString("tue_lunch");
                            tuesdayJsonObject.getString("tue_dinner");
                            tuesdayJsonObject.getString("tue_other");

                            wednesdayJsonObject.getString("dayKey");
                            wednesdayJsonObject.getString("wed_all");
                            wednesdayJsonObject.getString("wed_breakfast");
                            wednesdayJsonObject.getString("wed_lunch");
                            wednesdayJsonObject.getString("wed_dinner");
                            wednesdayJsonObject.getString("wed_other");

                            thursdayJsonObject.getString("dayKey");
                            thursdayJsonObject.getString("thu_all");
                            thursdayJsonObject.getString("thu_breakfast");
                            thursdayJsonObject.getString("thu_lunch");
                            thursdayJsonObject.getString("thu_dinner");
                            thursdayJsonObject.getString("thu_other");

                            fridayJsonObject.getString("dayKey");
                            fridayJsonObject.getString("fri_all");
                            fridayJsonObject.getString("fri_breakfast");
                            fridayJsonObject.getString("fri_lunch");
                            fridayJsonObject.getString("fri_dinner");
                            fridayJsonObject.getString("fri_other");


                            saturdayJsonObject.getString("dayKey");
                            saturdayJsonObject.getString("sat_all");
                            saturdayJsonObject.getString("sat_breakfast");
                            saturdayJsonObject.getString("sat_lunch");
                            saturdayJsonObject.getString("sat_dinner");
                            saturdayJsonObject.getString("sat_other");


                            next = jsonObject1.getString("next_screen");
                            Log.d("next21", next);


                        }
                      /*  AppPreferencesBuss.setRegistrationSno42F(getActivity(), "Submit");
                        AppPreferencesBuss.setRegistrationSno42D(getActivity(), "");
                      */
                        if (next.equalsIgnoreCase("1")) {
                            Log.d("next02", next);
                            Fragment fragment = new BusinessInformationSubmitFragment();
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                            fragmentTransaction.commitAllowingStateLoss();
                        } else if (next.equalsIgnoreCase("0")) {
                            Log.d("next03", next);
                            startActivity(new Intent(getActivity(), BusinessNaviDrawer.class));
                        }
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

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showdata() {
        if (Util.isNetworkAvailable(mContext)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", "updateBusinessView42F");
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            Log.e(TAG, "showdata: Request >> " + jsonObject.toString());
            showDataFromServer(jsonObject);
        } else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void showDataFromServer(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.bussines_service_view(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "showdata: Response >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String monday_break = object.getString("monday_break");
                                String monday_lunch = object.getString("monday_lunch");
                                String monday_dinner = object.getString("monday_dinner");

                                monBF.setText(monday_break);
                                monL.setText(monday_lunch);
                                monD.setText(monday_dinner);
                                /*========================================================================*/
                                String tuesday_start_break = object.getString("tuesday_break");
                                String tuesday_start_lunch = object.getString("tuesday_lunch");
                                String tuesday_start_dinner = object.getString("tuesday_dinner");
                                tueBF.setText(tuesday_start_break);
                                tueL.setText(tuesday_start_lunch);
                                tueD.setText(tuesday_start_dinner);
                                /*========================================================================*/
                                String wednesday_start_break = object.getString("wednesday_break");
                                String wednesday_start_lunch = object.getString("wednesday_lunch");
                                String wednesday_start_dinner = object.getString("wednesday_dinner");
                                wedBF.setText(wednesday_start_break);
                                wedL.setText(wednesday_start_lunch);
                                wedD.setText(wednesday_start_dinner);
                                /*========================================================================*/
                                String thursday_start_break = object.getString("thursday_break");
                                String thursday_start_lunch = object.getString("thursday_lunch");
                                String thursday_start_dinner = object.getString("thursday_dinner");
                                thusBF.setText(thursday_start_break);
                                thusL.setText(thursday_start_lunch);
                                thusD.setText(thursday_start_dinner);
                                /*========================================================================*/
                                String friday_start_break = object.getString("friday_break");
                                String friday_start_lunch = object.getString("friday_lunch");
                                String friday_start_dinner = object.getString("friday_dinner");
                                friBF.setText(friday_start_break);
                                friL.setText(friday_start_lunch);
                                friD.setText(friday_start_dinner);
                                /*========================================================================*/
                                String saturday_start_break = object.getString("saturday_break");
                                String saturday_start_lunch = object.getString("saturday_lunch");
                                String saturday_start_dinner = object.getString("saturday_dinner");
                                satBF.setText(saturday_start_break);
                                satL.setText(saturday_start_lunch);
                                satD.setText(saturday_start_dinner);
                                /*========================================================================*/
                                String sunday_start_break = object.getString("sunday_break");
                                String sunday_start_lunch = object.getString("sunday_lunch");
                                String sunday_start_dinner = object.getString("sunday_dinner");
                                sunBF.setText(sunday_start_break);
                                sunL.setText(sunday_start_lunch);
                                sunD.setText(sunday_start_dinner);
                                /*========================================================================*/
                                String monday_all = object.getString("monday_all_break");
                                String tuesday_all = object.getString("tuesday_all_break");
                                String wednesday_all = object.getString("wednesday_all_break");
                                String thursday_all = object.getString("thursday_all_break");
                                String friday_all = object.getString("friday_all_break");
                                String saturday_all = object.getString("saturday_all_break");
                                String sun_all = object.getString("sunday_all_break");

                                sunAD.setText(sun_all);
                                monAD.setText(monday_all);
                                tueAD.setText(tuesday_all);
                                wedAD.setText(wednesday_all);
                                thusAD.setText(thursday_all);
                                friAD.setText(friday_all);
                                satAD.setText(saturday_all);
                                /*========================================================================*/
                                String mon_other_b = object.getString("monday_other_break");
                                String tues_other_b = object.getString("tuesday_other_break");
                                String wednes_other_b = object.getString("wednesday_other_break");
                                String thurs_other_b = object.getString("thursday_other_break");
                                String fri_other_b = object.getString("friday_other_break");
                                String sat_other_b = object.getString("saturday_other_break");
                                String sun_other_b = object.getString("sunday_other_break");
                                sunOther.setText(sun_other_b);
                                monOther.setText(mon_other_b);
                                tueOther.setText(tues_other_b);
                                wedOther.setText(wednes_other_b);
                                thusOther.setText(thurs_other_b);
                                satOther.setText(sat_other_b);
                                friOther.setText(fri_other_b);
                                /*========================================================================*/
                                String layalty_prrogram = object.getString("layalty_prrogram");
                                String check_price_range = object.getString("check_price_range");
                                String reservation_deposit = object.getString("reservation_deposit");
                                String amount_per_guest = object.getString("amount_per_guest");
                                String refund_customer_hours = object.getString("refund_customer_hours");
                                String avgPrice = object.getString("avgPrice");

                                if (reservation_deposit.equalsIgnoreCase("1")) {
                                    checkYes1.setChecked(true);
                                    checkNo1.setChecked(false);
                                    llYesNo.setVisibility(View.VISIBLE);
                                } else if (reservation_deposit.equalsIgnoreCase("0")) {
                                    checkYes1.setChecked(false);
                                    checkNo1.setChecked(true);
                                    llYesNo.setVisibility(View.GONE);
                                } else {
                                    checkYes1.setChecked(false);
                                    checkNo1.setChecked(false);
                                }

                                amountEtDay.setText(amount_per_guest);
                                hoursEditTextValue.setText(refund_customer_hours);
                                etAvgPrice.setText(avgPrice);

                                if (layalty_prrogram.equalsIgnoreCase("1")) {
                                    checkYes.setChecked(true);
                                    checkNo.setChecked(false);
                                } else if (layalty_prrogram.equalsIgnoreCase("0")) {
                                    checkYes.setChecked(false);
                                    checkNo.setChecked(true);
                                } else if (layalty_prrogram.equalsIgnoreCase("")) {
                                    checkYes.setChecked(false);
                                    checkNo.setChecked(false);
                                }

                                if (avgPrice.equalsIgnoreCase("$"))
                                    spinnerPriceRange.setSelection(0);
                                else if (avgPrice.equalsIgnoreCase("$$"))
                                    spinnerPriceRange.setSelection(1);
                                else if (avgPrice.equalsIgnoreCase("$$$"))
                                    spinnerPriceRange.setSelection(2);
                                else if (avgPrice.equalsIgnoreCase("$$$$"))
                                    spinnerPriceRange.setSelection(3);

                                if (object.getString("order_assign_status").equalsIgnoreCase("0"))
                                    rbdelAuto.setChecked(true);
                                else if (object.getString("order_assign_status").equalsIgnoreCase("1"))
                                    rbDelManual.setChecked(true);
                              /*  else
                                    rbDelManual.setChecked(true);*/
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Data found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG + "error", t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });
    }


    public class InputFilters implements InputFilter {

        private double min, max;

        public InputFilters(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilters(String min, String max) {
            this.min = Double.parseDouble(min);
            this.max = Double.parseDouble(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(double a, double b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}