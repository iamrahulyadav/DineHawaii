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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessPositionActivity;
import com.yberry.dinehawaii.Bussiness.Adapter.EmployeeListAdapter;
import com.yberry.dinehawaii.Model.EmployeeModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeListFragment extends Fragment implements View.OnClickListener {
    String TAG = "EmployeeListFragment";
    EmployeeListAdapter listAdapter;
    ArrayList<EmployeeModel> modelArrayList;
    private Context context;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomTextView addemployee, noemp;
    FragmentIntraction intraction;

    public EmployeeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_list, container, false);
        context = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("Employee List");
        }
        init(view);
        getAllEmployee();
        return view;
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
    private void init(View view) {
        modelArrayList = new ArrayList<EmployeeModel>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_employee);
        addemployee = (CustomTextView) view.findViewById(R.id.addnewEmpl);
        noemp = (CustomTextView) view.findViewById(R.id.noemployee);
        addemployee.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(listAdapter);
        listAdapter = new EmployeeListAdapter(context, modelArrayList);
        mRecyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private void getAllEmployee() {
        JsonObject jsonObjectEmpl = new JsonObject();
        jsonObjectEmpl.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.GETALLEMPLOYEEDETAIL);
        jsonObjectEmpl.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObjectEmpl.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "Request GET ALL EMPLOYEE >> " + jsonObjectEmpl.toString());
        getEmployeDetails(jsonObjectEmpl);
    }

    private void getEmployeDetails(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "Response GET ALL EMPLOYEE >> " + response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EmployeeModel employeeModel = new EmployeeModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            employeeModel.setEmp_id(object.getString("emp_id"));
                            employeeModel.setEmp_dineid(object.getString("dine_hawaii_id"));
                            employeeModel.setEmp_email(object.getString("email_id"));
                            employeeModel.setEmp_jobtitle(object.getString("job_title"));
                            employeeModel.setEmp_firstnm(object.getString("first_name"));
                            employeeModel.setEmp_lastmn(object.getString("last_name"));
                            employeeModel.setEmp_pass(object.getString("password"));
                            employeeModel.setEmp_phoneno(object.getString("smart_phone_number"));
                            employeeModel.setEmp_jobid(object.getString("job_id"));
                            employeeModel.setDuties(object.getString("emp_duties"));
                            employeeModel.setJobduty(object.getString("emp_duties"));
                            employeeModel.setStatus(object.getString("status"));
                            modelArrayList.add(employeeModel);
                        }
                        listAdapter.notifyDataSetChanged();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        noemp.setVisibility(View.VISIBLE);
                        progressHD.dismiss();
                    } else {
                        progressHD.dismiss();
                        noemp.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                noemp.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addnewEmpl:
                Fragment fragment = new SecurityBusinessAccessFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
                break;
                default:
                    break;
        }
    }
}
