package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.customview.CustomAutoCompleteTextView;
import com.yberry.dinehawaii.customview.CustomButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessRestFirstSearchBusiness extends AppCompatActivity {
    private static final String TAG = "BusiRestFirstSearch";
    CustomButton submitButton;
    CustomAutoCompleteTextView spinSelectBusiness;
    private ImageView back;
    private ScrollView mainView;
    private ArrayList<String> businessNameArrayList, permitList;
    private boolean isFound = false;
    private String selected_permit_no = "";
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busi_search);
        setToolbar();
        init();
        setListener();
        setAdapter();

    }

    private void init() {
        mainView = (ScrollView) findViewById(R.id.mainView);
        submitButton = (CustomButton) findViewById(R.id.submitButton);
        businessNameArrayList = new ArrayList<>();
        permitList = new ArrayList<>();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: isFound " + isFound);
                if (!spinSelectBusiness.getText().toString().equalsIgnoreCase("")) {
                    if (isFound == true) {
                        Intent intent = new Intent(BusinessRestFirstSearchBusiness.this, BusiFirstReg_20A_1.class);
                        intent.putExtra("business_name", spinSelectBusiness.getText().toString());
                        intent.putExtra("permit_no", selected_permit_no);
                        startActivity(intent);
                    } else {
                        showNotFoundAlert();
                    }
                } else
                    spinSelectBusiness.setError("Enter business name");
            }
        });
    }

    private void showNotFoundAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BusinessRestFirstSearchBusiness.this);
        builder.setTitle(R.string.busi_not_found_title);
        builder.setMessage("The business name you have entered is not found, Do you want to register new one?");
        builder.setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(BusinessRestFirstSearchBusiness.this, BusiFirstReg_20A_2.class);
                intent.putExtra("business_name", spinSelectBusiness.getText().toString());
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setListener() {
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });


    }

    private void setAdapter() {
        spinSelectBusiness = (CustomAutoCompleteTextView) findViewById(R.id.spinSelectBusiness);
        spinSelectBusiness.setThreshold(2);


        spinSelectBusiness.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "onTextChanged: isFound >> " + isFound);
                count = s.length();
                if (count > 1) {
                    String business_name = s.toString();
                    new SearchTask().execute(business_name);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinSelectBusiness.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.v(TAG, "spinSelectBusiness hasFocus working");
                    spinSelectBusiness.setText("");
                }
            }
        });

        spinSelectBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isFound = true;
                selected_permit_no = permitList.get(position);
                Log.e(TAG, "onItemClick: selected business >> " + businessNameArrayList.get(position));
                Log.e(TAG, "onItemClick: isFalse >> " + isFound);
                Log.e(TAG, "onItemClick: selected permit no >> " + selected_permit_no);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Search Businesss");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class SearchTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String search_text = strings[0];
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.SEARCH_BUSINESS);
            jsonObject.addProperty("business_name", search_text);
            Log.e(TAG, "REQUEST SEARCH BUSINESS >> " + jsonObject.toString());

            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "RESPONSE SEARCH BUSINESS >> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray resultJsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < resultJsonArray.length(); i++) {
                                JSONObject jsonObject1 = resultJsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String bussiness_name = jsonObject1.getString("bname");
                                String permit_no = jsonObject1.getString("permit_no");

                                Log.v(TAG, "Business Name :- " + bussiness_name);
                                Log.v(TAG, "Business name after replace :- " + bussiness_name.replaceAll("amp;", "").trim());

                                permitList.add(permit_no);
                                businessNameArrayList.add(bussiness_name.replaceAll("amp;", "").trim());
//                                arrayAdapter.notifyDataSetChanged();
                            }
                            Log.e(TAG, "onResponse: businessNameArrayList >> " + businessNameArrayList);
                            arrayAdapter = new ArrayAdapter<String>(BusinessRestFirstSearchBusiness.this, android.R.layout.simple_dropdown_item_1line, businessNameArrayList);
                            spinSelectBusiness.setAdapter(arrayAdapter);

                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            String msg = jsonObject.getString("message");
                            isFound = false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "error" + t.getMessage());
                }
            });
            return null;
        }
    }
}
