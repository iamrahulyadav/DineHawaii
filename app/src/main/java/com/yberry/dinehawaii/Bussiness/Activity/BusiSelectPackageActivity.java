package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.OptionAdapter;
import com.yberry.dinehawaii.Bussiness.Adapter.PackageAdapter;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusiSelectPackageActivity extends AppCompatActivity {
    private static final String TAG = "BusiSelectPackage";
    public static CustomTextView option_type;
    public static CustomTextView package_type;
    ImageView back;
    boolean isRegistration = false;
    private Context mContext;
    private Dialog dialog;
    private ImageView ivHelp;
    private ArrayList<CheckBoxPositionModel> pakage_list, option_list;
    private RecyclerView pakage_mRecyclerView, option_mRecyclerView;
    private PackageAdapter packageAdapter;
    private OptionAdapter optionAdapter;
    private HashMap<String, Double> packageCharge;
    private HashMap<String, Double> optionCharge;
    private double single_option_charge;
    private ProgressHUD progressHD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busi_select_package);
        setToolbar();
        LocalBroadcastManager.getInstance(this).registerReceiver(new MyReciever(), new IntentFilter("test"));
        if (getIntent().getAction() != null)
            if (getIntent().getAction().equalsIgnoreCase("register"))
                isRegistration = true;

        packageCharge = new HashMap<String, Double>();
        packageCharge.put("0", 0.0);
        optionCharge = new HashMap<String, Double>();
        optionCharge.put("0", 0.0);
        pakage_list = new ArrayList<>();
        option_list = new ArrayList<>();
        init();

        if (Util.isNetworkAvailable(mContext)) {
            progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            getAmounts();
        } else
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
    }

    private void setAdapters() {
        pakage_mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        pakage_mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        packageAdapter = new PackageAdapter(mContext, pakage_list, packageCharge);
        pakage_mRecyclerView.setAdapter(packageAdapter);

        option_mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        option_mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        optionAdapter = new OptionAdapter(mContext, option_list, optionCharge, single_option_charge);
        option_mRecyclerView.setAdapter(optionAdapter);
    }

    private void getAmounts() {
        if (!progressHD.isShowing())
            progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.GETALLSELECTIONAMOUNT);
        Log.e(TAG, "Request GET ALL AMOUNTS >> " + jsonObject.toString());
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET ALL AMOUNTS >> " + response.body().toString());
                String s = response.body().toString();
                pakage_list.clear();
                int totalAmount = 0;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray arrayResultPackage = jsonObject.getJSONArray("result_package");
                        JSONArray arrayResultOption = jsonObject.getJSONArray("result_option");
                        for (int i = 0; i < arrayResultPackage.length(); i++) {
                            JSONObject jsonObject1 = arrayResultPackage.getJSONObject(i);
                            String sel_quantity = jsonObject1.getString("sel_quantity");
                            String amount = jsonObject1.getString("Amount");
                            Log.e(TAG, "onResponse: arrayResultPackage sel_quantity " + sel_quantity);
                            Log.e(TAG, "onResponse: arrayResultPackage amount " + amount);
                            packageCharge.put(sel_quantity, Double.parseDouble(amount));
                        }

                        for (int i = 0; i < arrayResultOption.length(); i++) {
                            JSONObject jsonObject1 = arrayResultOption.getJSONObject(i);
                            String sel_quantity = jsonObject1.getString("sel_quantity_op");
                            String amount = jsonObject1.getString("Amount_op");
                            optionCharge.put(sel_quantity, Double.parseDouble(amount));
                        }
                        single_option_charge = Double.parseDouble(jsonObject.getString("single_option"));
                    }

                    Log.e(TAG, "onResponse: packageCharge >> " + packageCharge.toString());
                    Log.e(TAG, "onResponse: optionCharge >> " + optionCharge.toString());
                    Log.e(TAG, "onResponse: single_option_charge >> " + single_option_charge);
                    getAllPackage();
                    getOptions();
                    setAdapters();
                } catch (JSONException e) {
                    if (progressHD.isShowing())
                        progressHD.dismiss();
                    e.printStackTrace();
                }
                if (progressHD.isShowing())
                    progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                if (progressHD.isShowing())
                    progressHD.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isRegistration) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(BusiSelectPackageActivity.this);
            alertDialog.setMessage("Are you sure to cancel the registration process?");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        } else
            finish();
    }

    private void backButtonListener() {
        ImageView backImageView = (ImageView) findViewById(R.id.back);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mContext = this;
        package_type = (CustomTextView) findViewById(R.id.package_type);
        package_type.setText("0.0");
        option_type = (CustomTextView) findViewById(R.id.option_type);
        option_type.setText("0.0");
        ivHelp = (ImageView) findViewById(R.id.help);
        customDialog(mContext);
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  dialog.show();
            }
        });
    }

    private void customDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        dialog.setContentView(R.layout.activity_monthly_package_coast);
        ImageView button = (ImageView) dialog.findViewById(R.id.cross);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });
    }

    @SuppressLint("LongLogTag")
    private void getAllPackage() {
        if (!progressHD.isShowing())
            progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.GETALLPACKAGE);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(BusiSelectPackageActivity.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(BusiSelectPackageActivity.this));
        Log.e(TAG, "Requesst GET ALL PACKAGE >> " + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET ALL PACKAGE >> " + response.body().toString());
                String s = response.body().toString();
                pakage_list.clear();
                double totalAmount = 0;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CheckBoxPositionModel model = new CheckBoxPositionModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        model.setId(object.getString("package_id"));
                        model.setName(object.getString("package_name"));
                        model.setAmount(object.getString("amount"));
                        model.setType(object.getString("package_type"));
                        model.setPackage_detail(object.getString("package_detail"));
                        model.setChckStatus(object.getString("package_status").equalsIgnoreCase("1") ? true : false);
                        if (object.getString("package_status").equalsIgnoreCase("1")) {
                            model.setChckStatus(true);
                            //totalAmount = totalAmount + Double.parseDouble(object.getString("amount").replace("$", "").trim());
                            Log.v(TAG, "Selected option amount :-" + totalAmount);
                        } else {
                            model.setChckStatus(false);
                        }
                        model.setTotalPackageAmount("");
                        pakage_list.add(model);
                        packageAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    if (progressHD.isShowing())
                        progressHD.dismiss();
                    e.printStackTrace();
                }
                if (progressHD.isShowing())
                    progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                if (progressHD.isShowing())
                    progressHD.dismiss();

            }
        });
    }

    @SuppressLint("LongLogTag")
    private void getOptions() {
        if (!progressHD.isShowing())
            progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.REGISTRATION.GETALLOPTION);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(BusiSelectPackageActivity.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(BusiSelectPackageActivity.this));
        Log.e(TAG, "Request GET ALL OPTIONS >> " + jsonObject.toString());

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GET ALL OPTIONS >> " + response.body().toString());
                String s = response.body().toString();
                option_list.clear();
                double totalAmount = 0;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CheckBoxPositionModel model = new CheckBoxPositionModel();
                        model.setId(jsonObject1.getString("option_id"));
                        model.setName(jsonObject1.getString("option_name"));
                        model.setAmount(jsonObject1.getString("amount"));
                        model.setType(jsonObject1.getString("option_type"));

                        if (jsonObject1.getString("option_status").equalsIgnoreCase("1")) {
                            model.setChckStatus(true);
                            totalAmount = totalAmount + Double.parseDouble(jsonObject1.getString("amount").replace("$", "").trim());
                            Log.v(TAG, "Selected option amount :-" + totalAmount);
                        } else {
                            model.setChckStatus(false);
                        }
                        model.setTotalOptionAmount("" + totalAmount);
                        option_list.add(model);
                    }

                    optionAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    if (progressHD.isShowing())
                        progressHD.dismiss();
                    e.printStackTrace();
                }

                if (progressHD.isShowing())
                    progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                if (progressHD.isShowing())
                    progressHD.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_icon_menu, menu);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            ArrayList<String> checkBoxPackageList = packageAdapter.getSelectedItem();
            ArrayList<String> checkBoxOptionList = optionAdapter.getSelectedItem();
            String packagelist = checkBoxPackageList.toString().replace("[", "").replace("]", "");
            String optionlist = checkBoxOptionList.toString().replace("[", "").replace("]", "");
            if (package_type.getText().toString().equalsIgnoreCase("0.0") || package_type.getText().toString().equalsIgnoreCase("0")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BusiSelectPackageActivity.this);
                builder.setMessage("No package selected, minimun one package is required.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else if (packagelist.contains("1") && !packagelist.contains("2")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BusiSelectPackageActivity.this);
                builder.setMessage("Package 2 is required if Package 1 has choosen.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                double totalAmount = 0;
                String packageAmount = package_type.getText().toString().trim();
                String optionAmount = option_type.getText().toString().trim();

                double packageamt = Double.parseDouble(packageAmount);
                double optionamt = Double.parseDouble(optionAmount);

                Log.v(TAG, "Package amountToBePaid dsv :- " + packageamt + " Option amountToBePaid dvs :- " + optionamt);
                Log.v(TAG, "Package amountToBePaid :- " + packageAmount + " Option amountToBePaid :- " + optionAmount);
                if (packageAmount.equalsIgnoreCase("") && optionAmount.equalsIgnoreCase(""))
                    totalAmount = 0;
                else
                    totalAmount = Double.parseDouble(packageAmount) + Double.parseDouble(optionAmount);
                // totalAmount = ""+ packageamt + optionamt;
                Log.e(TAG, "Total Amount :- " + totalAmount);


                if (Util.isNetworkAvailable(mContext)) {

                    Log.e(TAG, "packagelist :- " + packagelist);
                    Log.e(TAG, "optionlist :- " + optionlist);

               /* String packageId = "1, "+packagelist;
                Log.e("msg00",packageId);
*/
                    AppPreferencesBuss.setBussiPackagelist(BusiSelectPackageActivity.this, packagelist);
                    AppPreferencesBuss.setBusinessOptionList(BusiSelectPackageActivity.this, optionlist);

                    if (checkBoxPackageList.size() > 0) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("method", AppConstants.REGISTRATION.GETCHECKAMOUNT);
                        jsonObject.addProperty("pacakge_id", packagelist);
                        jsonObject.addProperty("no_of_package", checkBoxPackageList.size());
                        jsonObject.addProperty("option_id", optionlist);
                        jsonObject.addProperty("no_of_option", checkBoxOptionList.size());
                        Log.e(TAG, "Request PACKAGE UDPATE >> " + jsonObject.toString());
                        JsonCallGetMethod(packagelist, optionlist, jsonObject, String.valueOf(totalAmount), packageamt, optionamt);


                    } else {

                        Toast.makeText(mContext, "Please Select Packages", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                }
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void JsonCallGetMethod(final String packageList, final String optionList, JsonObject jsonObject, final String TOTALAMOUNT, final double pakageAm, final double optionAm) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, response.body().toString());
                String s = response.body().toString();
                Log.e(TAG, "Response PACKAGE UDPATE >> " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Log.e(TAG, "DineHawaiiobjectDa amountToBePaid" + jsonObject1.toString());
                    JSONObject object = new JSONObject();
                    object.put("pacakges", packageList);
                    object.put("options", optionList);
                    object.put("total_amount", jsonObject1.getInt("amount"));
                    Intent intent = new Intent(getApplicationContext(), BusiPackagePaymentActivity.class);
                    intent.putExtra("data", object.toString());
                    intent.putExtra("totalAmount", TOTALAMOUNT);
                    intent.putExtra("pakageAmount", String.valueOf(pakageAm));
                    intent.putExtra("optionAmount", String.valueOf(optionAm));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //  Log.e(TAG+"error",t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });

    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Packages");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    class MyReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (optionAdapter != null) {
                ArrayList<CheckBoxPositionModel> new_option_list = new ArrayList<>();
                for (int i = 0; i < option_list.size(); i++) {
                    CheckBoxPositionModel model = option_list.get(i);
                    model.setChckStatus(false);
                    new_option_list.add(model);
                }
                Log.e(TAG, "onReceive: new option list >> " + new_option_list);
                optionAdapter = new OptionAdapter(mContext, new_option_list, optionCharge, single_option_charge);
                option_mRecyclerView.setAdapter(optionAdapter);
                option_type.setText("0.0");
            }
        }
    }


}