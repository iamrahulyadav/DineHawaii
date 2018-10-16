package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FEEDBACK Customer";
    CustomButton btnSub;
    CustomEditText feedback_buiness, feedbackMsg, feedback_remark, feedback_title;
    private ImageView back;
    AppCompatSpinner spinner;
    private CustomAdapter adapter;
    private String selectedBusiId = "";
    private List<ListItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        list = new ArrayList<ListItem>();
        setToolbar();
        getBusiness();
        init();
    }


    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Feedback");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        btnSub = (CustomButton) findViewById(R.id.btnSub);
        feedbackMsg = (CustomEditText) findViewById(R.id.feedback_msg);
        feedback_remark = (CustomEditText) findViewById(R.id.feedback_remark);
        feedback_title = (CustomEditText) findViewById(R.id.feedback_title);
        feedback_buiness = (CustomEditText) findViewById(R.id.feedback_buiness);
        spinner = findViewById(R.id.spinner);
        feedback_buiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });
        LinearLayout mainView = (LinearLayout) findViewById(R.id.main_view);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = FeedbackActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) FeedbackActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        btnSub.setOnClickListener(this);
        setCatAdapter();
    }

    private void setCatAdapter() {
        adapter = new CustomAdapter(this, R.layout.spinner_item_layout, R.id.tvTitle, list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedBusiId = list.get(position).getId();
                Log.e(TAG, "onItemSelected: selectedBusiId >> " + selectedBusiId);
                feedback_buiness.setText(list.get(position).getBusinessName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void feedbackData(JsonObject jsonObject) {

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSub) {
            if (TextUtils.isEmpty(feedback_buiness.getText().toString())) {
                feedback_buiness.setError("Select Business");
            } else if (TextUtils.isEmpty(feedback_title.getText().toString())) {
                feedbackMsg.setError("Enter Title");
            } else if (TextUtils.isEmpty(feedbackMsg.getText().toString())) {
                feedbackMsg.setError("Enter Message");
            } else {
                if (Util.isNetworkAvailable(FeedbackActivity.this)) {
                    feedbackApi();
                } else {
                    Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void feedbackApi() {
        if (Util.isNetworkAvailable(FeedbackActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(FeedbackActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                    // TODO Auto-generated method stub
                }
            });
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.ALL_FEEDBACK);
            jsonObject.addProperty("business_id", selectedBusiId);
            jsonObject.addProperty("title", feedback_title.getText().toString());
            jsonObject.addProperty("remark", feedback_remark.getText().toString());
            jsonObject.addProperty("review_message", feedbackMsg.getText().toString());
            jsonObject.addProperty("order_id", "0");
            jsonObject.addProperty("type", "none");
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(FeedbackActivity.this));
            Log.e(TAG, "feedbackApi: Request >> " + jsonObject.toString());


            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.order_details(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "feedbackApi: Response >> " + response.body().toString());
                    String resp = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            Toast.makeText(FeedbackActivity.this, jsonObject.getJSONArray("result").getJSONObject(0).getString("msg"), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else if (jsonObject.getString("status").equals("400")) {
                            Toast.makeText(FeedbackActivity.this, jsonObject.getJSONArray("result").getJSONObject(0).getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                        }
                        progressHD.dismiss();
                    } catch (JSONException e) {
                        progressHD.dismiss();
                        e.printStackTrace();
                        Toast.makeText(FeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                    progressHD.dismiss();
                    Toast.makeText(FeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(FeedbackActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getBusiness() {
        if (Util.isNetworkAvailable(FeedbackActivity.this)) {
            final ProgressHUD progressHD = ProgressHUD.show(FeedbackActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_FEEDBACK_BUSINESS);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(FeedbackActivity.this));
            Log.e(TAG, "getBusiness: Request >> " + jsonObject.toString());
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.order_details(jsonObject);

            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "getBusiness: Response >> " + response.body().toString());
                    String resp = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ListItem item = new ListItem();
                                item.setBusinessName(jsonObject1.getString("business_name"));
                                item.setId(jsonObject1.getString("business_id"));
                                list.add(item);
                            }
                        } else if (jsonObject.getString("status").equals("400")) {
                            Toast.makeText(FeedbackActivity.this, jsonObject.getJSONArray("result").getJSONObject(0).getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                        progressHD.dismiss();
                    } catch (JSONException e) {
                        adapter.notifyDataSetChanged();
                        progressHD.dismiss();
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                    progressHD.dismiss();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(FeedbackActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(FeedbackActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    public class CustomAdapter extends ArrayAdapter<ListItem> {
        LayoutInflater flater;

        public CustomAdapter(Activity context, int resouceId, int textviewId, List<ListItem> list) {
            super(context, resouceId, textviewId, list);
            flater = context.getLayoutInflater();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView, position);
        }

        private View rowview(View convertView, int position) {

            ListItem rowItem = getItem(position);

            viewHolder holder;
            View rowview = convertView;
            if (rowview == null) {
                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowview = flater.inflate(R.layout.spinner_item_layout, null, false);
                holder.txtTitle = (AppCompatTextView) rowview.findViewById(R.id.tvTitle);
                rowview.setTag(holder);
            } else {
                holder = (viewHolder) rowview.getTag();
            }
            holder.txtTitle.setText(rowItem.getBusinessName());
            return rowview;
        }

        private class viewHolder {
            AppCompatTextView txtTitle;
        }
    }
}
