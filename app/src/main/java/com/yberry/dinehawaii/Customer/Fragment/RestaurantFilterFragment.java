package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.FoodTypeActivity;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomCheckBox;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class RestaurantFilterFragment extends Fragment {
    public static final String TAG = "RestaurantFilterFragment";
    CustomButton btnSearch;
    TextView tvDate, tvTime;
    CustomEditText mbussinessName, mLocation, mServiceType, mFoodType, distancetext;
    String distance = "";
    ArrayList<String> service_list, food_list;
    ArrayList<String> service_id_list, food_id_list;
    double latitude, longitude;
    ArrayList<CheckBoxPositionModel> listFoodService, listService;
    LinearLayout linearLayout;
    String priceRange = "0";
    private View rootView;
    private FragmentIntraction intraction;
    private CustomTextView text_food;
    private String food_catagory_id_list = "", serv_catagory_id_list = "";
    private CustomTextView tvSelectService;
    private RadioGroup rgDistance, rgPriceRange;
    private CustomCheckBox checkboxManualDistance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_filter, container, false);
        setHasOptionsMenu(true);
        if (intraction != null) {
            intraction.actionbarsetTitle("Filter");
        }
        tvSelectService = ((CustomTextView) rootView.findViewById(R.id.tvSelectService));
        text_food = (CustomTextView) rootView.findViewById(R.id.spinSelectFoodType);
        mbussinessName = (CustomEditText) rootView.findViewById(R.id.mbussinessName);
        distancetext = (CustomEditText) rootView.findViewById(R.id.distancetext);
        mLocation = (CustomEditText) rootView.findViewById(R.id.search_location);
        mServiceType = (CustomEditText) rootView.findViewById(R.id.mServiceType);
        mFoodType = (CustomEditText) rootView.findViewById(R.id.mFoodType);
        btnSearch = (CustomButton) rootView.findViewById(R.id.btnSearch);
        tvDate = (TextView) rootView.findViewById(R.id.date);
        tvTime = (TextView) rootView.findViewById(R.id.tvTime);
        rgDistance = (RadioGroup) rootView.findViewById(R.id.rgDistance);
        rgPriceRange = (RadioGroup) rootView.findViewById(R.id.rgPriceRange);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.searchmain);
        checkboxManualDistance = (CustomCheckBox) rootView.findViewById(R.id.checkboxManualDistance);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = getActivity().getCurrentFocus();
                if (v != null) {
                    hideKeyboard();
                }
                return false;
            }
        });

        listFoodService = new ArrayList<>();
        listService = new ArrayList<>();
        setCheckboxListners();
        service_list = new ArrayList<>();
        service_id_list = new ArrayList<>();

        food_list = new ArrayList<>();
        food_id_list = new ArrayList<>();

        text_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_food.setText("");
                getSearchFoodType();
            }
        });
        tvSelectService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelectService.setText("");
                getSearchServiceType();
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tvTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDate = c.get(Calendar.DATE); // current date


                DatePickerDialog mdiDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        tvDate.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDate);
                mdiDialog.show();

            }

        });

        latitude = Double.parseDouble(AppPreferences.getLatitude(getActivity()));
        longitude = Double.parseDouble(AppPreferences.getLongitude(getActivity()));
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

        return rootView;
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        if (intraction != null) {
            intraction.actionbarsetTitle("Restaurants");
        }
        intraction = null;
    }

    private void sendData() {
        Bundle bundle = new Bundle();
        if (checkboxManualDistance.isChecked()) {
            bundle.putString("distance", distancetext.getText().toString());
        } else {
            bundle.putString("distance", distance);

        }

        bundle.putString("latitude", String.valueOf(latitude));
        bundle.putString("longitutde", String.valueOf(longitude));
        bundle.putString("busname", mbussinessName.getText().toString());
        bundle.putString("location", mLocation.getText().toString());
        bundle.putString("foodid", food_catagory_id_list);
        bundle.putString("serviceid", serv_catagory_id_list);
        bundle.putString("price_range", priceRange);


        Fragment fragment = new RestaurantListFilterFragment();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameSearch, fragment, fragment.getTag());
        fragmentTransaction.commit();
    }

    private void setCheckboxListners() {

        distancetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        checkboxManualDistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((RadioButton) rootView.findViewById(R.id.rb05Mile)).setEnabled(false);
                    ((RadioButton) rootView.findViewById(R.id.rb1Mile)).setEnabled(false);
                    ((RadioButton) rootView.findViewById(R.id.rb5Mile)).setEnabled(false);
                    ((RadioButton) rootView.findViewById(R.id.rb10Mile)).setEnabled(false);
                    distancetext.setEnabled(true);
                } else {
                    ((RadioButton) rootView.findViewById(R.id.rb05Mile)).setEnabled(true);
                    ((RadioButton) rootView.findViewById(R.id.rb1Mile)).setEnabled(true);
                    ((RadioButton) rootView.findViewById(R.id.rb5Mile)).setEnabled(true);
                    ((RadioButton) rootView.findViewById(R.id.rb10Mile)).setEnabled(true);
                    distancetext.setEnabled(false);
                    distancetext.setText("");
                }
            }
        });
        rgDistance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkboxManualDistance.setChecked(false);
                int selectedRadioId = rgDistance.getCheckedRadioButtonId();
                switch (selectedRadioId) {
                    case R.id.rb05Mile:
                        distance = "0.5";
                        break;
                    case R.id.rb1Mile:
                        distance = "1";
                        break;
                    case R.id.rb5Mile:
                        distance = "5";
                        break;
                    case R.id.rb10Mile:
                        distance = "10";
                        break;
                    default:
                        distance = "";
                }
            }
        });

        rgPriceRange.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedRadioId = rgPriceRange.getCheckedRadioButtonId();
                switch (selectedRadioId) {
                    case R.id.rbUnder10:
                        priceRange = "$";
                        break;
                    case R.id.rbUnder20:
                        priceRange = "$$";
                        break;
                    case R.id.rbUnder30:
                        priceRange = "$$$";
                        break;
                    case R.id.rbOver30:
                        priceRange = "$$$$";
                        break;
                    default:
                        distance = "";
                }
            }
        });


    }

    private void getSearchFoodType() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.FOODTYPE);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        Log.e("filter food json", jsonObject + "");
        getFoodTypeApi(jsonObject);
    }

    private void getFoodTypeApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponse", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(getActivity(), FoodTypeActivity.class);
                        intent.setAction("FoodFragment45");
                        intent.putExtra("Foodtype", s);
                        startActivityForResult(intent, 12);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "food_list error :- " + Log.getStackTraceString(t));
                if (progressHD != null && progressHD.isShowing())
                    progressHD.dismiss();
            }
        });
    }


    private void getSearchServiceType() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.ALLSERVICES);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));
        getServiceTypeApi(jsonObject);
        Log.e("filter service json", jsonObject + "");
    }

    private void getServiceTypeApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponse", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(getActivity(), FoodTypeActivity.class);
                        intent.setAction("serviceType");
                        intent.putExtra("service", s);
                        startActivityForResult(intent, 101);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "service_list error :- " + Log.getStackTraceString(t));
                if (progressHD != null && progressHD.isShowing())
                    progressHD.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 12) {
                listFoodService = data.getParcelableArrayListExtra("foodtype");
                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(text_food.getText().toString())) {
                        text_food.setText(listValue.getName());
                        food_catagory_id_list = listValue.getId();
                    } else {
                        text_food.setText(text_food.getText().toString() + "," + listValue.getName());
                        food_catagory_id_list = food_catagory_id_list + "," + listValue.getId();
                    }
                }
            } else if (requestCode == 101) {
                listService = data.getParcelableArrayListExtra("serviceType");
                for (CheckBoxPositionModel listValue : listService) {
                    if (TextUtils.isEmpty(tvSelectService.getText().toString())) {
                        tvSelectService.setText(listValue.getName());
                        serv_catagory_id_list = listValue.getId();
                    } else {
                        tvSelectService.setText(tvSelectService.getText().toString() + "," + listValue.getName());
                        serv_catagory_id_list = serv_catagory_id_list + "," + listValue.getId();
                    }
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.filter_list);
        item.setVisible(false);
        MenuItem cancel = menu.findItem(R.id.filter_cancel);
        cancel.setVisible(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_cancel:
                closeFragment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeFragment() {
        getFragmentManager().popBackStack();
    }

}



