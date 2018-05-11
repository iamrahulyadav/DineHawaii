package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.Bussiness.Adapter.WalkInCurrentAdapter;
import com.yberry.dinehawaii.Model.ReservationDetails;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.Util.context;

/**
 * Created by Hvantage2 on 7/14/2017.
 */

public class CurrentWalkIn extends android.support.v4.app.Fragment implements View.OnClickListener {
    public WalkInCurrentAdapter adapter;
    ImageView imgCont;
    TextView tvSubmit;
    CustomTextView tvTableNo;
    CustomEditText etEmailId, etCustomerName, etTime, etPartySize, etDate, etSmartPhno;
    ArrayList<ReservationDetails> reservList;
    View rootView;
    private RecyclerView mRecyclerView;
    private ArrayAdapter<String> spinnerTableAdapter;
    private Spinner spinnerTable;
    private ArrayList<String> tableList = new ArrayList<String>();
    private ArrayList<String> tableID = new ArrayList<String>();
    private LinearLayoutManager mLayoutManager;
    private String tableId = "0", formattedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.walkin_recyclerview, container, false);
        LinearLayout mainView = (LinearLayout) rootView.findViewById(R.id.mainView);

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
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        Log.d("DATECURRENT", formattedDate);
        init();
        apiGettingTables();


        return rootView;
    }


    private void init() {
        etEmailId = (CustomEditText) rootView.findViewById(R.id.dineEmail);
        etCustomerName = (CustomEditText) rootView.findViewById(R.id.customerName);
        etTime = (CustomEditText) rootView.findViewById(R.id.time);
        etPartySize = (CustomEditText) rootView.findViewById(R.id.partySize);
        etDate = (CustomEditText) rootView.findViewById(R.id.date);
        etSmartPhno = (CustomEditText) rootView.findViewById(R.id.smartPhno);
        tvSubmit = (TextView) rootView.findViewById(R.id.tvSubmit);
        spinnerTable = (Spinner) rootView.findViewById(R.id.spinnerC1table1);
        tvTableNo = (CustomTextView) rootView.findViewById(R.id.tableno);

        spinnerTableAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableList);
        spinnerTableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTable.setAdapter(spinnerTableAdapter);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCustomerDetail();
            }
        });
        spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("selected table", tableList.get(i));
                Log.e("selected table id", tableID.get(i));
                tableId = tableID.get(i);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        reservList = new ArrayList<ReservationDetails>();
        getFreeTableData();
/*
        if(getArguments() != null) {
            final Bundle bdl = getArguments();
            try {
                reservList = bdl.getParcelableArrayList("LIStRESERV");
                Log.d("ListReserv", reservList + "");
            } catch (final Exception e){}
        }
*/

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        imgCont = (ImageView) rootView.findViewById(R.id.imageview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new WalkInCurrentAdapter(context, reservList, CurrentWalkIn.this);
        mRecyclerView.setAdapter(adapter);
        imgCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checking();

            }
        });
    }

    private void getFreeTableData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.TABLE_MANAGEMENT_52A);
        jsonObject.addProperty("business_id", "8");//AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("date", "07/10/2017");   //formattedDate
        Log.d("TABLEJSON11", "Current walkin Json Request :- " + jsonObject.toString());
        availableTable(jsonObject);
    }

    @SuppressLint("LongLogTag")
    private void availableTable(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.normalUserBusinessApi(jsonObject);

        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("REsCurrent", "CurrentRespose :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Log.d("ghg", "shg");

                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        Log.e("Json Result", jsonArray.toString());


                        JSONArray listJson = jsonArray.getJSONArray(0);
                        // reservList.clear();


                        for (int j = 0; j < listJson.length(); j++) {
                            ReservationDetails reservationDataModel = new ReservationDetails();
                            JSONObject object = listJson.getJSONObject(j);
                            reservationDataModel.setTableNumber(object.getString("tableNo"));
                            reservationDataModel.setPartySize(object.getString("capacits"));
                            reservationDataModel.setSeatingTime(object.getString("time"));
                            reservList.add(reservationDataModel);

                        }


                    }


                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("CURRENTWALKIN", "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checking() {
        String packageNo = AppPreferencesBuss.getBussiPackagelist(getActivity());
        //  String[] data = packageNo.replace("").split(",");
        String[] data = packageNo.replace(" ", "").split(",");
        List<String> list = Arrays.asList(data);
        //34
        Log.d("package_no", list.toString());
        if (list.contains("2")) {
            ((FrameLayout) rootView.findViewById(R.id.table_frame)).setVisibility(View.VISIBLE);
            Fragment fragment = new TableManagmentPackageFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();

        } else if (list.contains("3")) {
            Log.d("package3", list.toString());
            // OnlineOrderingAndPaymentFragment
            Fragment fragment = new OnlineOrderingAndPaymentFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (list.contains("4")) {
            Log.d("package4", list.toString());
            Fragment fragment = new FoodServiceFragment45();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
            //break;
        } else {
            Intent intent = new Intent(getActivity(), BusinessNaviDrawer.class);
            startActivity(intent);
        }
     /*   String pack = String.valueOf(AppPreferencesBuss.getBussiPackagelist(getActivity()));
        Log.d("checking", pack);
        String[] s = pack.split(",");

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < s.length; i++) {
            list.add(s[i]);
        }
        if ((list.contains("1"))) {
            //((FrameLayout) rootView.findViewById(R.id.info_fram)).setVisibility(View.VISIBLE);
// TableManagmentWalkinFragment
            Log.d(TAG, "PacakgeData: " + "package 2");
            Fragment fragment = new TableManagmentFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();

            //MakingReservationActivity
            Intent intent = new Intent(getActivity(), MakingReservationActivity.class);
           intent.putExtra("business_id", AppPreferencesBuss.getBussiId(getActivity()));
           intent.putExtra("business_name", AppPreferencesBuss.getBussiName(getActivity()));
            startActivity(intent);

        } else if (list.contains("2")) {
            ((FrameLayout) rootView.findViewById(R.id.info_fram)).setVisibility(View.VISIBLE);
// TableManagmentWalkinFragment
            Log.d(TAG, "PacakgeData: " + "package 2");
            Fragment fragment = new TableManagmentWalkinFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();


        } else if (list.contains("3")) {
            ((FrameLayout) rootView.findViewById(R.id.info_fram)).setVisibility(View.VISIBLE);
            // OnlineOrderingAndPaymentFragment
            Fragment fragment = new OnlineOrderingAndPaymentFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();

        } else if (list.contains("4")) {
            ((FrameLayout) rootView.findViewById(R.id.info_fram)).setVisibility(View.VISIBLE);
            //  FoodServiceFragment45
            Fragment fragment = new FoodServiceFragment45();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }






        else if (list.contains(" ")) {
            Log.d(TAG, "PacakgeData: " + "else");
            Intent intent = new Intent(context, BusinessNaviDrawer.class);
            startActivity(intent);
        }
*/

    }

    private void getCustomerDetail() {
        if (Util.isNetworkAvailable(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.CREATE_TABLE_BOOKING);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));  //13
            jsonObject.addProperty("date", etDate.getText().toString().trim());
            jsonObject.addProperty("time", etTime.getText().toString().trim());
            jsonObject.addProperty("party_size", etPartySize.getText().toString().trim());
            jsonObject.addProperty("customer_name", etCustomerName.getText().toString().trim());
            jsonObject.addProperty("customer_hawaii_id", etEmailId.getText().toString().trim());
            jsonObject.addProperty("customer_smartphone_no", etSmartPhno.getText().toString().trim());
            jsonObject.addProperty("table_id", tableId);
            Log.e("JSONOBEJECTCURRENT:", jsonObject.toString());
            ReportJsonCalling(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void ReportJsonCalling(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_table_reaservation_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("RESPONCE", "JsonResponse :- " + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        ((FrameLayout) rootView.findViewById(R.id.table_frame)).setVisibility(View.VISIBLE);
                        Fragment fragment = new TableManagmentPackageFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.table_frame, fragment, fragment.getTag());
                        fragmentTransaction.commitAllowingStateLoss();


                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Log.e("status", jsonObject.getString("status"));
                        JSONObject jobject = jsonObject.getJSONObject("result");
                        String msg = jobject.getString("msg");
                        Log.e("msgCurrent", msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                    // adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ERROR", "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void apiGettingTables() {
        if (Util.isNetworkAvailable(getActivity())) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.GET_BUSINESS_TABLE);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));//1
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));//13

            Log.e("TABLEJSON", "Request :- " + jsonObject.toString());
            getTableJson(jsonObject);


        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void getTableJson(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.get_business_table(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("RESPONSETABLE" + "onResponseTable", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");
                        JSONArray jsonArray2 = jsonArray1.getJSONArray(0);
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
                            Log.e("table id", jsonObject1.getString("id"));
                            Log.e("table name", jsonObject1.getString("name"));
                            Log.e("table capacity", jsonObject1.getString("capacity"));

                            tableList.add(jsonObject1.getString("name"));
                            tableID.add(jsonObject1.getString("id"));

                        }
                        tableList.add(0, "select");
                        tableID.add(0, "0");
                        spinnerTableAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TABLEJSON", "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

        int pos = Integer.parseInt(String.valueOf(view.getTag()));
        ReservationDetails resverModel = reservList.get(pos);
        setReservationFields(resverModel);

    }

    private void setReservationFields(ReservationDetails resverModel) {
        etPartySize.setText(resverModel.getPartySize());
        etTime.setText(resverModel.getSeatingTime());
        tvTableNo.setText(resverModel.getTableNumber());
        etDate.setText(formattedDate);
        // etTime.setEnabled(false);
        // etPartySize.setEnabled(false);


    }
}