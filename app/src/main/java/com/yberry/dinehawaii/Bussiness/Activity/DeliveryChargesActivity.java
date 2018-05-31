package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryChargesActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "";
    CustomEditText etDeliveryArea, etDriverArrivalTime, etFlatamt, etPercent, etminrangeamt, etlessthn_amnt, etlesssthn_value, etbtwn1, etbtwn2, etbtwnamt, etDriverTip;
    LinearLayout llRange, mainLayout;
    RadioGroup rgDeliveryCost, rgDriverTip;
    RadioButton radiodFlatAmt, radioPrecent, radioRange, rdTipYes, rdTipNo;
    String rdflat = "0", rdpercent = "0", rdrange = "0", selectedTip = "0";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_vendor_detail);
        setToolbar();
        initViews();
        if (Util.isNetworkAvailable(context))
            getAllCharges();
        else
            Toast.makeText(context, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
        setRadioGrpListners();
    }

    private void getAllCharges() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.GETDELIVERYCHARGES);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        Log.e(TAG, "getAllCharges: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_new_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getAllCharges: Response >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        etDeliveryArea.setText(object.getString("delivery_area"));
                        etDriverArrivalTime.setText(object.getString("driver_arrival_time"));

                        if (!object.getString("percent_amt").equalsIgnoreCase("0"))
                            etPercent.setText(object.getString("percent_amt"));
                        if (!object.getString("min_food_cost").equalsIgnoreCase("0"))
                            etminrangeamt.setText(object.getString("min_food_cost"));
                        if (!object.getString("lessthan_amt").equalsIgnoreCase("0"))
                            etlessthn_amnt.setText(object.getString("lessthan_amt"));
                        if (!object.getString("lessthan_value").equalsIgnoreCase("0"))
                            etlesssthn_value.setText(object.getString("lessthan_value"));
                        if (!object.getString("between_val1").equalsIgnoreCase("0"))
                            etbtwn1.setText(object.getString("between_val1"));
                        if (!object.getString("between_val2").equalsIgnoreCase("0"))
                            etbtwn2.setText(object.getString("between_val2"));
                        if (!object.getString("between_amt").equalsIgnoreCase("0"))
                            etbtwnamt.setText(object.getString("between_amt"));
                        if (!object.getString("flat_amt").equalsIgnoreCase("0"))
                            etFlatamt.setText(object.getString("flat_amt"));

                        if (object.getString("driver_tip").equalsIgnoreCase("1")) {
                            selectedTip = "1";
                            etDriverTip.setVisibility(View.VISIBLE);
                            rdTipYes.setChecked(true);
                        } else {
                            selectedTip = "0";
                            etDriverTip.setVisibility(View.GONE);
                            rdTipNo.setChecked(true);
                        }

                        if (object.getString("cost_flat").equalsIgnoreCase("1"))
                            radiodFlatAmt.setChecked(true);
                        if (object.getString("cost_percent").equalsIgnoreCase("1"))
                            radioPrecent.setChecked(true);
                        if (object.getString("cost_range").equalsIgnoreCase("1"))
                            radioRange.setChecked(true);

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
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
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Delivery Charges");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        context = this;
        etDeliveryArea = (CustomEditText) findViewById(R.id.etdeliveryArea);
        etDriverTip = (CustomEditText) findViewById(R.id.etDriverTip);
        etDriverArrivalTime = (CustomEditText) findViewById(R.id.etArrivalTime);
        etFlatamt = (CustomEditText) findViewById(R.id.etFlatAmount);
        etPercent = (CustomEditText) findViewById(R.id.etPercentage);
        etminrangeamt = (CustomEditText) findViewById(R.id.etRangeCost1);
        etlessthn_amnt = (CustomEditText) findViewById(R.id.etRangeCost2);
        etlesssthn_value = (CustomEditText) findViewById(R.id.etMinCostRange);
        etbtwnamt = (CustomEditText) findViewById(R.id.etRangeCost3);
        etbtwn1 = (CustomEditText) findViewById(R.id.etRangeBetween1);
        etbtwn2 = (CustomEditText) findViewById(R.id.etRangeBetween2);
        llRange = (LinearLayout) findViewById(R.id.llRange);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        radiodFlatAmt = (RadioButton) findViewById(R.id.rdFlatAmount);
        radioPrecent = (RadioButton) findViewById(R.id.rdPercent);
        radioRange = (RadioButton) findViewById(R.id.rdRange);
        rdTipYes = (RadioButton) findViewById(R.id.tipYes);
        rdTipNo = (RadioButton) findViewById(R.id.tipNo);
        rgDeliveryCost = (RadioGroup) findViewById(R.id.rgrpCost);
        rgDriverTip = (RadioGroup) findViewById(R.id.rgrpTip);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v = getCurrentFocus();
                if (v != null)
                    hideKeyboard();
                return false;
            }
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                checkData();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkData() {
        if (TextUtils.isEmpty(etDeliveryArea.getText().toString()))
            etDeliveryArea.setError("Enter Area");
        else if (TextUtils.isEmpty(etDriverArrivalTime.getText().toString()))
            etDriverArrivalTime.getText().toString();
        else if (rgDeliveryCost.getCheckedRadioButtonId() == -1)
            Toast.makeText(context, "Select Delivery Cost Type", Toast.LENGTH_SHORT).show();
        else {
            if (Util.isNetworkAvailable(context)) {
                setChargesData();
            } else
                Toast.makeText(context, getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void setChargesData() {
        String fltamttext, percenttext, mintext, lessvaltext, lessamttext, btw1, btw2, btwamt;
        if (TextUtils.isEmpty(etFlatamt.getText().toString()))
            fltamttext = "0";
        else
            fltamttext = etFlatamt.getText().toString();
        if (TextUtils.isEmpty(etPercent.getText().toString()))
            percenttext = "0";
        else
            percenttext = etPercent.getText().toString();
        if (TextUtils.isEmpty(etminrangeamt.getText().toString()))
            mintext = "0";
        else
            mintext = etminrangeamt.getText().toString();
        if (TextUtils.isEmpty(etlessthn_amnt.getText().toString()))
            lessamttext = "0";
        else
            lessamttext = etlessthn_amnt.getText().toString();
        if (TextUtils.isEmpty(etbtwnamt.getText().toString()))
            btwamt = "0";
        else
            btwamt = etbtwnamt.getText().toString();
        if (TextUtils.isEmpty(etbtwn1.getText().toString()))
            btw1 = "0";
        else
            btw1 = etbtwn1.getText().toString();
        if (TextUtils.isEmpty(etbtwn2.getText().toString()))
            btw2 = "0";
        else
            btw2 = etbtwn2.getText().toString();
        if (TextUtils.isEmpty(etlesssthn_value.getText().toString()))
            lessvaltext = "0";
        else
            lessvaltext = etlesssthn_value.getText().toString();
        setDeliveryCharges(fltamttext, percenttext, mintext, lessvaltext, lessamttext, btw1, btw2, btwamt);
    }

    private void setRadioGrpListners() {
        rgDeliveryCost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdFlatAmount) {
                    rdflat = "1";
                    rdrange = "0";
                    rdpercent = "0";
                    etPercent.setText("");
                    etlesssthn_value.setText("");
                    etminrangeamt.setText("");
                    etlessthn_amnt.setText("");
                    etbtwnamt.setText("");
                    etbtwn1.setText("");
                    etbtwn2.setText("");
                    etFlatamt.setVisibility(View.VISIBLE);
                    etPercent.setVisibility(View.GONE);
                    llRange.setVisibility(View.GONE);
                } else if (checkedId == R.id.rdPercent) {
                    rdpercent = "1";
                    rdflat = "0";
                    rdrange = "0";
                    etFlatamt.setText("");
                    etlesssthn_value.setText("");
                    etminrangeamt.setText("");
                    etlessthn_amnt.setText("");
                    etbtwnamt.setText("");
                    etbtwn1.setText("");
                    etbtwn2.setText("");
                    etPercent.setVisibility(View.VISIBLE);
                    etFlatamt.setVisibility(View.GONE);
                    llRange.setVisibility(View.GONE);
                } else if (checkedId == R.id.rdRange) {
                    rdrange = "1";
                    rdpercent = "0";
                    rdflat = "0";
                    etFlatamt.setText("");
                    etPercent.setText("");
                    llRange.setVisibility(View.VISIBLE);
                    etPercent.setVisibility(View.GONE);
                    etFlatamt.setVisibility(View.GONE);
                }
            }
        });

        rgDriverTip.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tipYes) {
                    selectedTip = "1";
                    etDriverTip.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.tipNo) {
                    selectedTip = "0";
                    etDriverTip.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

    }


    private void setDeliveryCharges(String fltamttext, String percenttext, String mintext, String lessvaltext, String lessamttext, String btw1, String btw2, String btwamt) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ADDDELIVERYCHARGES);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        jsonObject.addProperty("delivery_area", etDeliveryArea.getText().toString());
        jsonObject.addProperty("driver_arrival_time", etDriverArrivalTime.getText().toString());
        jsonObject.addProperty("cost_flat", rdflat);
        jsonObject.addProperty("cost_percent", rdpercent);
        jsonObject.addProperty("cost_range", rdrange);
        jsonObject.addProperty("flat_amt", fltamttext);
        jsonObject.addProperty("percent_amt", percenttext);
        jsonObject.addProperty("min_food_cost", mintext);
        jsonObject.addProperty("lessthan_amt", lessamttext);
        jsonObject.addProperty("lessthan_value", lessvaltext);
        jsonObject.addProperty("between_amt", btwamt);
        jsonObject.addProperty("between_val1", btw1);
        jsonObject.addProperty("between_val2", btw2);
        jsonObject.addProperty("driver_tip", selectedTip);
        Log.e(TAG, "setDeliveryCharges Json" + jsonObject.toString());
        setDeliveryChargesApi(jsonObject);
    }

    private void setDeliveryChargesApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_new_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();
                Log.e(TAG, "setDeliveryCharges response " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        String msg = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObj = jsonArray.getJSONObject(0);
                        msg = jsonObj.getString("msg");

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObj = jsonArray.getJSONObject(0);
                        msg = jsonObj.getString("msg");

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
