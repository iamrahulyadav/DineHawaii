package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessPositionActivity;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.Bussiness.Activity.EmployeesPosition;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SecurityBusinessAccessFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SecurityBusiAccessFrag";
    ArrayList<CheckBoxPositionModel> checkBoxOptionList, jobpositionlist;
    JsonObject otherDetailsJson, employeeDetailsJson, employeeDetailsJson1;
    JsonArray employeeArray;
    FragmentIntraction intraction;
    CustomButton assignDuty, addempl;
    boolean isEditable = false;
    private CustomTextView employeePosition;
    private Context mContext;
    private CustomEditText firstName, lastName, dineHawaiID, mobileNo, emailAddress, passw;
    private ArrayList<CheckBoxPositionModel> listJobDuties;
    private String position_id_list = "";
    private String jobs_id_list = "";

    public SecurityBusinessAccessFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_security_access, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Security Access");
        }

        mContext = getActivity();
        employeePosition = (CustomTextView) view.findViewById(R.id.edittext_pos);
        checkBoxOptionList = new ArrayList<>();
        jobpositionlist = new ArrayList<>();

        init(view);
        LinearLayout mainView = (LinearLayout) view.findViewById(R.id.mainView);
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
        employeeDetailsJson = new JsonObject();
        employeeDetailsJson1 = new JsonObject();
        otherDetailsJson = new JsonObject();
        employeeArray = new JsonArray();
        firstName = (CustomEditText) view.findViewById(R.id.firstName);
        passw = (CustomEditText) view.findViewById(R.id.password1);
        lastName = (CustomEditText) view.findViewById(R.id.lastName);
        dineHawaiID = (CustomEditText) view.findViewById(R.id.dineHawaiID);
        mobileNo = (CustomEditText) view.findViewById(R.id.mobileNo);
        emailAddress = (CustomEditText) view.findViewById(R.id.emailAddress);
        assignDuty = (CustomButton) view.findViewById(R.id.assignduty);
        addempl = (CustomButton) view.findViewById(R.id.addemployee);

        Bundle bundle = this.getArguments();
        if (bundle != null && !bundle.equals("")) {
            isEditable = true;
            position_id_list = bundle.getString("employid");
            jobs_id_list = bundle.getString("jobduty");
            dineHawaiID.setText(bundle.getString("employdineid"));
            emailAddress.setEnabled(false);
            dineHawaiID.setEnabled(false);
            firstName.setText(bundle.getString("employfnm"));
            lastName.setText(bundle.getString("employlnm"));
            emailAddress.setText(bundle.getString("employem"));
            mobileNo.setText(bundle.getString("employphn"));
            passw.setText(bundle.getString("employpw"));
            employeePosition.setText(bundle.getString("employpos"));
            addempl.setText("Update Details");
            jobs_id_list = bundle.getString("emp_duties");
            Log.e(TAG, "init: duties" + jobs_id_list);

        } else {
            isEditable = false;
        }

        assignDuty.setOnClickListener(this);
        addempl.setOnClickListener(this);
        employeePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmployeesPosition.class);
                intent.setAction("udpate");
                intent.putExtra("position_id", position_id_list);
                startActivityForResult(intent, 10);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            checkBoxOptionList = data.getParcelableArrayListExtra("employee_position");
            employeePosition.setText("");
            for (CheckBoxPositionModel listValue : checkBoxOptionList) {
                if (TextUtils.isEmpty(employeePosition.getText().toString())) {
                    employeePosition.setText(listValue.getName());
                    position_id_list = listValue.getId();
                } else {
                    employeePosition.setText(employeePosition.getText().toString() + "," + listValue.getName());
                    position_id_list = position_id_list + "," + listValue.getId();
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == 102) {
            jobpositionlist = data.getParcelableArrayListExtra("jobduties");
            for (CheckBoxPositionModel listValue : jobpositionlist) {
                if (TextUtils.isEmpty(jobs_id_list)) {
                    jobs_id_list = listValue.getId();
                } else {
                    jobs_id_list = jobs_id_list + "," + listValue.getId();
                }
            }

        }
    }


    private void saveData(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessSecurityLevelApi(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response GETUPDATEDUTIES >> " + response.body().toString());
                String s = response.body().toString();
                Log.e(TAG, "add empl json" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        jobs_id_list = "";
                        position_id_list = "";
                            dineHawaiID.setText("");
                            emailAddress.setEnabled(true);
                            dineHawaiID.setEnabled(true);
                            firstName.setText("");
                            lastName.setText("");
                            emailAddress.setText("");
                            mobileNo.setText("");
                            passw.setText("");
                            employeePosition.setText("");
                            addempl.setText("Add Employee");

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        progressHD.dismiss();
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_SHORT).show();
                progressHD.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assignduty:
                //jobs_id_list = "";
                Intent intent = new Intent(getActivity(), BusinessPositionActivity.class);
                intent.setAction("update_duties");
                intent.putExtra("emp_duties", jobs_id_list);
                Log.e("BUSPO",jobs_id_list);
                startActivityForResult(intent, 102);
                break;
            case R.id.addemployee:

                if (isEditable) {
                    checkEditEmpDetails();
                } else {
                    checkEmpDetails();
                }
                break;
            default:
                break;
        }
    }

    private void checkEditEmpDetails() {
        if (TextUtils.isEmpty(employeePosition.getText().toString())) {
            Toast.makeText(mContext, "Select Employee Position", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError("Enter first name");
        } /*else if (TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError("Enter last name");
        } */else if (TextUtils.isEmpty(passw.getText().toString())) {
            passw.setError("Enter password");
        } else if (TextUtils.isEmpty(mobileNo.getText().toString())) {
            mobileNo.setError("Enter mobile number");
        } else {
            if (Util.isNetworkAvailable(getActivity())) {
                editEmployee();
            } else {
                Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkEmpDetails() {
       if (TextUtils.isEmpty(employeePosition.getText().toString())) {
            Toast.makeText(mContext, "Select Employee Position", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError("Enter first name");
        } /*else if (TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError("Enter last name");
        } */else if (TextUtils.isEmpty(emailAddress.getText().toString())) {
            emailAddress.setError("Enter email id");
        } else if (TextUtils.isEmpty(dineHawaiID.getText().toString())) {
            dineHawaiID.setError("Enter Dine Id");
        } else if (TextUtils.isEmpty(passw.getText().toString())) {
            passw.setError("Enter password");
        } else if (TextUtils.isEmpty(mobileNo.getText().toString())) {
            mobileNo.setError("Enter mobile number");
        } /*else if (position_id_list.equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Select Employee Duties", Toast.LENGTH_SHORT).show();
        } */else {
            if (Util.isNetworkAvailable(getActivity())) {
                addEmployee();
            } else {
                Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void editEmployee() {
        employeeDetailsJson.addProperty("first_name", firstName.getText().toString());
        employeeDetailsJson.addProperty("last_name", lastName.getText().toString());
        employeeDetailsJson.addProperty("job_title", position_id_list);
        employeeDetailsJson.addProperty("dine_hawaii_id", dineHawaiID.getText().toString());
        employeeDetailsJson.addProperty("smart_phone_number", mobileNo.getText().toString());
        employeeDetailsJson.addProperty("email_id", emailAddress.getText().toString());
        employeeDetailsJson.addProperty("password", passw.getText().toString().trim());
        employeeArray.add(employeeDetailsJson);

        otherDetailsJson.addProperty("job_duties", jobs_id_list);
        otherDetailsJson.addProperty("job_title", position_id_list);
        otherDetailsJson.addProperty("password", passw.getText().toString());
        otherDetailsJson.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity())); // AppPreferencesBuss.getUserId(context));
        otherDetailsJson.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));


        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ADD_SERURITY_EMP_DETAIL);
        mainObject.add("employeeDetails", new Gson().toJsonTree(employeeArray));
        mainObject.add("otherDetails", new Gson().toJsonTree(otherDetailsJson));
        // mainObject.addProperty("emp_id",employeeid);
        Log.e(TAG, "edit empl json" + mainObject.toString());
        saveData(mainObject);
    }

    private void addEmployee() {
        employeeDetailsJson.addProperty("first_name", firstName.getText().toString());
        employeeDetailsJson.addProperty("last_name", lastName.getText().toString());
        employeeDetailsJson.addProperty("job_title", position_id_list);
        employeeDetailsJson.addProperty("dine_hawaii_id", dineHawaiID.getText().toString());
        employeeDetailsJson.addProperty("smart_phone_number", mobileNo.getText().toString());
        employeeDetailsJson.addProperty("email_id", emailAddress.getText().toString());
        employeeDetailsJson.addProperty("password", passw.getText().toString().trim());
        employeeArray.add(employeeDetailsJson);

        otherDetailsJson.addProperty("job_duties", jobs_id_list);
        otherDetailsJson.addProperty("job_title", position_id_list);
        otherDetailsJson.addProperty("password", passw.getText().toString());
        //otherDetailsJson.addProperty("buss_emailId", emailAddress.getText().toString());
        otherDetailsJson.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity())); // AppPreferencesBuss.getUserId(context));
        otherDetailsJson.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));


        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ADD_SERURITY_EMP_DETAIL);
        mainObject.add("employeeDetails", new Gson().toJsonTree(employeeArray));
        mainObject.add("otherDetails", new Gson().toJsonTree(otherDetailsJson));
        Log.e(TAG, "add empl json" + mainObject.toString());
        saveData(mainObject);
    }
}












