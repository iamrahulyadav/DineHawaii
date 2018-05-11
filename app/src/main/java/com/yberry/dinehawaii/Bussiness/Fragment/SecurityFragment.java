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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.SelectJobTitleActivity;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessLoginActivity;
import com.yberry.dinehawaii.Bussiness.Activity.FoodTypeActivity;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 02-02-17.
 */

public class SecurityFragment extends Fragment {

    private static final String TAG = "SecurityFragment";
    CustomTextView addNextEmp, jobTitle, jobDuties,emp_JobTitile1,emp_JobTitile;
    JsonObject otherDetailsJson, employeeDetailsJson, employeeDetailsJson1;
    JsonArray employeeArray;
    Button submitLocation, regiterNewLocation, editIdPwdButton, securityEdit,empEdit;
    LinearLayout addEmpLinearLayout,mainLayout;
    LinearLayout busidpass, basicInfo1, basicInfo2, NewbasicInfo1, newbasicInfo2, security, buttonstab;
    private CustomEditText firstName, firstName1, lastName, lastName1,  dineHawaiID, dineHawaiID1, mobileNo, mobileNo1, emailAddress, emailAddress1, buss_emailId, password, password2, password1;
    private ArrayList<CheckBoxPositionModel> listJobDuties;
    String job_duty_IDs, jobTitle_id,emp1jobid,emp2jobid;


    public SecurityFragment() {
        // Required empty public constructor
    }
    FragmentIntraction intraction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_security_level, container, false);
//        headText.setText("SECURITY");
        if (intraction != null) {
            intraction.actionbarsetTitle("Security");
        }

        employeeDetailsJson = new JsonObject();
        employeeDetailsJson1 = new JsonObject();
        otherDetailsJson = new JsonObject();
        employeeArray = new JsonArray();

//        getAllJobTitle();

        initViews(view);
        mainLayout = (LinearLayout)view.findViewById(R.id.securityMainLayout);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v!= null){
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

    private void setChildViews(View view) {
        busidpass = (LinearLayout) view.findViewById(R.id.basicInfotab);
        basicInfo1 = (LinearLayout) view.findViewById(R.id.employeeInfotab);
        basicInfo2 = (LinearLayout) view.findViewById(R.id.employeeTab2);
        NewbasicInfo1 = (LinearLayout) view.findViewById(R.id.empTabNextBasic);
        newbasicInfo2 = (LinearLayout) view.findViewById(R.id.empTabNext);
        security = (LinearLayout) view.findViewById(R.id.securityTab);
        buttonstab = (LinearLayout) view.findViewById(R.id.buttonsTab);
        securityEdit = (Button) view.findViewById(R.id.securitybtn);
        empEdit = (Button) view.findViewById(R.id.employeeBtn);
        editIdPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (busidpass.getVisibility() == View.VISIBLE) {
                    editIdPwdButton.setText("edit");
                    busidpass.setVisibility(View.GONE);
                } else {
                    editIdPwdButton.setText("cancel");
                    busidpass.setVisibility(View.VISIBLE);
                }
            }
        });
        securityEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (security.getVisibility() == View.VISIBLE){
                    securityEdit.setText("edit");
                    security.setVisibility(View.GONE);
                }else {
                    securityEdit.setText("cancel");
                    security.setVisibility(View.VISIBLE);
                }

            }
        });
        empEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (basicInfo1.getVisibility() == View.VISIBLE && basicInfo2.getVisibility() == View.VISIBLE){
                    empEdit.setText("edit");
                    basicInfo1.setVisibility(View.GONE);
                    basicInfo2.setVisibility(View.GONE);
                    newbasicInfo2.setVisibility(View.GONE);
                    NewbasicInfo1.setVisibility(View.GONE);
                }else{
                    empEdit.setText("cancel");
                    basicInfo1.setVisibility(View.VISIBLE);
                    basicInfo2.setVisibility(View.VISIBLE);
                    addNextEmp.setVisibility(View.VISIBLE);
                }
            }

        });

      /*  if (busidpass.getVisibility() == View.VISIBLE || basicInfo1.getVisibility() == View.VISIBLE && basicInfo2.getVisibility() == View.VISIBLE
                || security.getVisibility() == View.VISIBLE){
            buttonstab.setVisibility(View.VISIBLE);
        }else {
            buttonstab.setVisibility(View.GONE);
        }*/
    }

    private void initViews(View view) {

        String user_id = AppPreferencesBuss.getSaveid(getActivity());
        String passwordS = AppPreferencesBuss.getSavepass(getActivity());

        jobTitle = (CustomTextView) view.findViewById(R.id.jobTitle);
        jobDuties = (CustomTextView) view.findViewById(R.id.jobDuties);
        buss_emailId = (CustomEditText) view.findViewById(R.id.buss_emailId);
        buss_emailId.setText(AppPreferencesBuss.getSaveid(getActivity()));
        password = (CustomEditText) view.findViewById(R.id.password);
        password.setText(passwordS);
        firstName = (CustomEditText) view.findViewById(R.id.firstName);
        lastName = (CustomEditText) view.findViewById(R.id.lastName);
        emp_JobTitile = (CustomTextView) view.findViewById(R.id.emp_JobTitile);
        dineHawaiID = (CustomEditText) view.findViewById(R.id.dineHawaiID);
        mobileNo = (CustomEditText) view.findViewById(R.id.mobileNo);
        emailAddress = (CustomEditText) view.findViewById(R.id.emailAddress);
        firstName1 = (CustomEditText) view.findViewById(R.id.firstName1);
        lastName1 = (CustomEditText) view.findViewById(R.id.lastName1);
        emp_JobTitile1 = (CustomTextView) view.findViewById(R.id.emp_JobTitile1);
        dineHawaiID1 = (CustomEditText) view.findViewById(R.id.dineHawaiID1);
        mobileNo1 = (CustomEditText) view.findViewById(R.id.mobileNo1);
        emailAddress1 = (CustomEditText) view.findViewById(R.id.emailAddress1);
        addEmpLinearLayout = (LinearLayout) view.findViewById(R.id.addEmpLinearLayout);
        addNextEmp = (CustomTextView) view.findViewById(R.id.addNextEmp);
        submitLocation = (Button) view.findViewById(R.id.submitLocation);
        regiterNewLocation = (Button) view.findViewById(R.id.regiterNewLocation);
        editIdPwdButton = (Button) view.findViewById(R.id.editIdPwdButton);
        password1 = (CustomEditText) view.findViewById(R.id.password1);
        password2 = (CustomEditText) view.findViewById(R.id.password2);
        editIdPwdButton = (Button) view.findViewById(R.id.editIdPwdButton);


        setChildViews(view);
        setOnClickViews();
        AddEmployees();
    }

    private void AddEmployees() {
        addNextEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNextEmp.setVisibility(View.GONE);
                NewbasicInfo1.setVisibility(View.VISIBLE);
                newbasicInfo2.setVisibility(View.VISIBLE);
            }
        });

        submitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String first_name = firstName.getText().toString().trim();
                String last_name = lastName.getText().toString().trim();
                String job_title = emp_JobTitile.getText().toString().trim();
                String dine_hawaii_id = dineHawaiID.getText().toString().trim();
                String smart_phone_number = mobileNo.getText().toString().trim();
                String email_id = emailAddress.getText().toString().trim();

                employeeDetailsJson.addProperty("first_name", first_name);
                employeeDetailsJson.addProperty("last_name", last_name);
                employeeDetailsJson.addProperty("job_title", job_title);
                employeeDetailsJson.addProperty("dine_hawaii_id", dine_hawaii_id);
                employeeDetailsJson.addProperty("smart_phone_number", smart_phone_number);
                employeeDetailsJson.addProperty("email_id", email_id);
                employeeDetailsJson.addProperty("password", password1.getText().toString().trim());
                employeeArray.add(employeeDetailsJson);

                if (addEmpLinearLayout.getVisibility() == View.VISIBLE) {

                    String first_name1 = firstName1.getText().toString().trim();
                    String last_name1 = lastName1.getText().toString().trim();
                    String job_title1 = emp_JobTitile1.getText().toString().trim();
                    String dine_hawaii_id1 = dineHawaiID1.getText().toString().trim();
                    String smart_phone_number1 = mobileNo1.getText().toString().trim();
                    String email_id1 = emailAddress1.getText().toString().trim();

                    employeeDetailsJson1.addProperty("first_name", first_name1);
                    employeeDetailsJson1.addProperty("last_name", last_name1);
                    employeeDetailsJson1.addProperty("job_title", job_title1);
                    employeeDetailsJson1.addProperty("dine_hawaii_id", dine_hawaii_id1);
                    employeeDetailsJson1.addProperty("smart_phone_number", smart_phone_number1);
                    employeeDetailsJson1.addProperty("email_id", email_id1);
                    employeeDetailsJson1.addProperty("password", password2.getText().toString().trim());
                    employeeArray.add(employeeDetailsJson1);
                }

                addSerurityEmpDetail(employeeArray);
            }

        });



    }

    private void setOnClickViews() {
        emp_JobTitile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectJobTitleActivity.class);
                startActivityForResult(intent, 102);
            }
        });
        emp_JobTitile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectJobTitleActivity.class);
                startActivityForResult(intent, 103);
            }
        });
        regiterNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(getActivity(), BusinessLoginActivity.class);
                //  i1.putExtra("cameFrom","security_fragment");
                startActivity(i1);
            }
        });

        jobTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectJobTitleActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        jobDuties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jobTitle.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Please Select Job Title First!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), FoodTypeActivity.class);
                    intent.setAction("SecurityFragment");
                    intent.putExtra("jobTitle_id", jobTitle_id);
                    startActivityForResult(intent, 12);
                }
            }
        });

        buss_emailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "The Email id cannot be edited", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addSerurityEmpDetail(JsonArray employeeArray) {

        Log.v(TAG, "Employee Array :- " + employeeArray);
//        String jDuties = jobDuties.getText().toString().trim();
//        String jTitle = jobTitle.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String email_add = buss_emailId.getText().toString().trim();

        otherDetailsJson.addProperty("job_duties", job_duty_IDs);
        otherDetailsJson.addProperty("job_title", jobTitle_id);
        otherDetailsJson.addProperty("password", pwd);
        otherDetailsJson.addProperty("buss_emailId", email_add);
        otherDetailsJson.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity())); // AppPreferencesBuss.getUserId(context));
        otherDetailsJson.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));

        JsonObject mainObject = new JsonObject();
        mainObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ADD_SERURITY_EMP_DETAIL);
        mainObject.add("employeeDetails", new Gson().toJsonTree(employeeArray));
        mainObject.add("otherDetailsJson", new Gson().toJsonTree(otherDetailsJson));

        addSerurityEmpDetailTask(mainObject);
    }

    private void addSerurityEmpDetailTask(JsonObject jsonObject) {

        Log.d(TAG, " Security Business Json :- " + jsonObject.toString());

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
                progressHD.dismiss();
                Log.e(TAG, "Security Business Json :- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d(TAG, "Res :- " + s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        Toast.makeText(getActivity().getApplicationContext(), " Record Added Succesfully !!!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressHD.dismiss();
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 12) {
                Log.v(TAG, "OnActivtyResultServiceType");

                job_duty_IDs = data.getStringExtra("job_id");
                listJobDuties = data.getParcelableArrayListExtra("all_job_duty");
                Log.d(TAG, "All job selected item  :- " + String.valueOf(listJobDuties));
                Log.d(TAG, "All job ids :- " + job_duty_IDs);


                for (CheckBoxPositionModel listValue : listJobDuties) {
                    if (TextUtils.isEmpty(jobDuties.getText().toString())) {
                        jobDuties.setText(listValue.getName());
                    } else {
                        jobDuties.setText(jobDuties.getText().toString() + "\n" + listValue.getName());
                    }
                }

            } else if (requestCode == 101) {
                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("jobTitle");
                jobTitle.setText(checkBoxPositionModel.getName());
                jobTitle_id = checkBoxPositionModel.getId();
            } else if (requestCode == 102) {
                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("jobTitle");
                emp_JobTitile.setText(checkBoxPositionModel.getName());
                emp1jobid = checkBoxPositionModel.getId();
            }else if (requestCode == 103) {
                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("jobTitle");
                emp_JobTitile1.setText(checkBoxPositionModel.getName());
                emp2jobid = checkBoxPositionModel.getId();
            }
        }
    }
}
