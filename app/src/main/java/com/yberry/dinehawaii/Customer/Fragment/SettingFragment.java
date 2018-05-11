package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.HomeScreenActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;

import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingFragment extends Fragment {

    private static final String TAG = "SettngActivity";
    SwitchCompat email_switch, privacy_switch, notify_switch, appUpdate_switch;
    CustomTextView logout;
    DatabaseHandler mydb;
    String email = "0", privacy = "0", notify = "0", appUpdate = "0";
    FragmentIntraction intraction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_setting, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Settings");
        }
      //  setHasOptionsMenu(true);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "Raleway-Regular.ttf");
        email_switch = (SwitchCompat) view.findViewById(R.id.emailSwitchButton);
        email_switch.setText("Email");
        email_switch.setTypeface(typeface);

        notify_switch = (SwitchCompat) view.findViewById(R.id.notifySwitchButton);
        notify_switch.setText("Notification");
        notify_switch.setTypeface(typeface);

        privacy_switch = (SwitchCompat) view.findViewById(R.id.privacySwitchButton);
        privacy_switch.setText("Privacy");
        privacy_switch.setTypeface(typeface);

        appUpdate_switch = (SwitchCompat) view.findViewById(R.id.autoSwitchButton);
        appUpdate_switch.setText("Auto App Updates");
        appUpdate_switch.setTypeface(typeface);


        if (! AppPreferences.getEmailSetting(getActivity().getApplicationContext()).equalsIgnoreCase("")){
            email_switch.setChecked(true);
        } else if (! AppPreferences.getPrivacySetting(getActivity().getApplicationContext()).equalsIgnoreCase("")){
            privacy_switch.setChecked(true);
        } else if (! AppPreferences.getNotificationSetting(getActivity().getApplicationContext()).equalsIgnoreCase("")){
            notify_switch.setChecked(true);
        } else if (! AppPreferences.getAppUpdateSetting(getActivity().getApplicationContext()).equalsIgnoreCase("")){
            appUpdate_switch.setChecked(true);
        }


        email_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    email = "1";
                    email_switch.setChecked(true);
                } else {
                    email = "0";
                }
            }
        });

        notify_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    notify = "1";
                    notify_switch.setChecked(true);
                } else {
                    notify = "0";
                }
            }
        });

        privacy_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    privacy = "1";
                    privacy_switch.setChecked(true);
                } else {
                    privacy = "0";
                }
            }
        });

        appUpdate_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    appUpdate = "1";
                    appUpdate_switch.setChecked(true);
                } else {
                    appUpdate = "0";
                }
            }
        });

        Log.v(TAG, "Email :- " + email + " Privacy :- " + privacy  + " Notify:- "+notify + " appUpdate :- " + appUpdate);

        logout = (CustomTextView) view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutData();
            }
        });
        return view;
    }

    private void logoutData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method",AppConstants.GENERALAPI.LOGOUT);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));
        jsonObject.addProperty("user_type","3");
        Log.e(TAG+"logjson",jsonObject.toString());
        logoutApi(jsonObject);
    }

    private void logoutApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.request(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "logout response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        AppPreferences.clearPreference(getActivity());
                        AppPreferencesBuss.clearPreference(getActivity());
                        mydb = new DatabaseHandler(getActivity());
                        mydb.deleteCartitem();
                        Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();

                    }
                    else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
       /* getActivity().getMenuInflater().inflate(R.menu.setting_menu, menu);

        MenuItem menuItem1, menuItem2, menuItem3, menuItem4;
        menuItem1 = menu.findItem(R.id.action_group);
        menuItem2 = menu.findItem(R.id.action_search);
        menuItem3 = menu.findItem(R.id.action_notification);
        menuItem4 = menu.findItem(R.id.action_submit);
        menuItem1.setVisible(false);
        menuItem2.setVisible(false);
        menuItem3.setVisible(false);
        menuItem4.setVisible(false);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_submit) {

            submitSetting();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void submitSetting() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.PROFILE_SETTING);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));
        jsonObject.addProperty("email_setting", email);
        jsonObject.addProperty("notification_setting", notify);
        jsonObject.addProperty("privacy_setting", privacy);
        jsonObject.addProperty("auto_app_setting", appUpdate);

        Log.e(TAG, " Request Setting :- " + jsonObject.toString());

        getSetting(jsonObject);

    }

    private void getSetting(JsonObject jsonObject) {

        Log.e(TAG, " Request Setting getSetting :- " + jsonObject.toString());

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestGeneral(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response Setting :- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray result = jsonObject.getJSONArray("result");

                        for (int i =0 ; i< result.length() ;i++){

                            JSONObject jsonObject1 = result.getJSONObject(i);

                            String email_setting = jsonObject1.getString("email_setting");
                            String notification_setting = jsonObject1.getString("notification_setting");
                            String privacy_setting = jsonObject1.getString("privacy_setting");
                            String auto_app_setting = jsonObject1.getString("auto_app_setting");

                            AppPreferences.setEmailSetting(getActivity(),email_setting);
                            AppPreferences.setNotificationSetting(getActivity(), notification_setting);
                            AppPreferences.setPrivacySetting(getActivity(), privacy_setting);
                            AppPreferences.setAppUpdateSetting(getActivity(), auto_app_setting);

                        }

                        Toast.makeText(getActivity().getApplicationContext(), "Setting Changed Successfully !!!", Toast.LENGTH_SHORT).show();
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
}
