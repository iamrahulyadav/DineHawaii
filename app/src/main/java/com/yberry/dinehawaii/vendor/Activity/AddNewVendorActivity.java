package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.view.ViewGroup;
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
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewVendorActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    ArrayList<VendorMasterData> masterlist;
    CustomEditText etname, etcity, etaddress, etcontact, etemail, etbusname, etLastNm, etVendorType;
    String TAG = "AddVendorActivity";
    String vendorText;
    LinearLayout mainView;
    private String selectedVendorIds = "";
    private RadioGroup radioGroup;
    private String allow_bid = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vendor);
        setToolbar();
        initViews();
        getAllMasterVendor();
    }

    private void getAllMasterVendor() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.MASTERVENDORLIST);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        Log.e(TAG, "getAllVendors: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendors_list_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "onResponse: Response >> " + resp);
                try {
                    masterlist.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objresult = jsonArray.getJSONObject(i);
                            VendorMasterData model = new VendorMasterData();
                            model.setMaster_vendor_id(objresult.getString("cat_id"));
                            model.setMaster_vendor_addedon(objresult.getString("addedOn"));
                            model.setMaster_vendor_name(objresult.getString("name"));
                            masterlist.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {

                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        context = this;
        masterlist = new ArrayList<VendorMasterData>();
        etname = (CustomEditText) findViewById(R.id.etVendorName);
        etLastNm = (CustomEditText) findViewById(R.id.etLastNm);
        etcity = (CustomEditText) findViewById(R.id.etCity);
        etcontact = (CustomEditText) findViewById(R.id.etVendorphoneno);
        etaddress = (CustomEditText) findViewById(R.id.etAddress);
        etemail = (CustomEditText) findViewById(R.id.etVendorEmail);
        etbusname = (CustomEditText) findViewById(R.id.etVendorBus);
        etVendorType = (CustomEditText) findViewById(R.id.etVendorType);
        mainView = (LinearLayout) findViewById(R.id.mainView);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        etVendorType.setOnClickListener(this);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getCurrentFocus();
                if (view != null)
                    hideKeyboard();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_icon_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        if (TextUtils.isEmpty(etname.getText().toString()))
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(etbusname.getText().toString()))
            Toast.makeText(this, "Enter Business Name", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(etemail.getText().toString()))
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        else if (selectedVendorIds.equalsIgnoreCase("") || selectedVendorIds.equalsIgnoreCase("-1"))
            Toast.makeText(this, "Select Vendor Type", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(etcontact.getText().toString()))
            Toast.makeText(this, "Enter Contact No", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(etcity.getText().toString()))
            Toast.makeText(this, "Enter City", Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(etaddress.getText().toString()))
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
        else {
            if (Util.isNetworkAvailable(this)) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbYes)
                    allow_bid = "1";
                else if (radioGroup.getCheckedRadioButtonId() == R.id.rbNo)
                    allow_bid = "0";
                AddVendor();
            } else
                Toast.makeText(this, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
        }

    }

    private void AddVendor() {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSINESS_VENDOR_API.AddVENDOR);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(context));
        jsonObject.addProperty("category_id", selectedVendorIds);
        jsonObject.addProperty("vendor_fname", etname.getText().toString());
        jsonObject.addProperty("vendor_lname", etLastNm.getText().toString());
        jsonObject.addProperty("vendor_email", etemail.getText().toString());
        jsonObject.addProperty("vendor_number", etcontact.getText().toString());
        jsonObject.addProperty("vendor_city", etcity.getText().toString());
        jsonObject.addProperty("vendor_address", etaddress.getText().toString());
        jsonObject.addProperty("vendor_buss_name", etbusname.getText().toString());
        jsonObject.addProperty("allow_bid", allow_bid);
        Log.e(TAG, "getAllVendors: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendors_list_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    String resp = response.body().toString();
                    Log.e(TAG, "onResponse: Response >> " + resp);
                    masterlist.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject objresult = jsonArray.getJSONObject(0);
                        Toast.makeText(context, objresult.getString("msg"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject objresult = jsonArray.getJSONObject(0);
                        Toast.makeText(context, objresult.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressHD.dismiss();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    progressHD.dismiss();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Add Vendor");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etVendorType:
                selectedVendorIds = "";
                etVendorType.setText("");
                showVendorTypeDialog();
                break;
            default:
                break;
        }
    }

    private void showVendorTypeDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddNewVendorActivity.this);
        dialog.setTitle("Select Vendor");
        final RadioGroup group = new RadioGroup(this);
        for (int i = 0; i < masterlist.size(); i++) {
            RadioButton button = new RadioButton(AddNewVendorActivity.this);
            button.setId(Integer.parseInt(masterlist.get(i).getMaster_vendor_id()));
            button.setText(masterlist.get(i).getMaster_vendor_name());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(40, 10, 0, 0);
            button.setLayoutParams(params);
            group.addView(button);
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                vendorText = radioButton.getText().toString();
                Log.e(TAG, "onClick: vendorText" + vendorText);

            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "onClick: " + group.getCheckedRadioButtonId());
                selectedVendorIds = String.valueOf(group.getCheckedRadioButtonId());
                etVendorType.setText(vendorText);
            }
        });

        dialog.setView(group);
        dialog.show();

    }

}
