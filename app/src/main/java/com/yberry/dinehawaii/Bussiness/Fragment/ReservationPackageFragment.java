package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 03-02-17.
 */

public class ReservationPackageFragment extends Fragment {
    private static final String TAG = "ReservationPackFragment";
    CustomEditText nameEditText, dine_idEdiText;
    RadioButton radio1_yes, radio1_no, radio2_yes, radio2_no, radio3_yes, radio3_no;
    String name, din_id, priority_in, refund, wait_list;
    private ImageView imageView;
    private RadioGroup radioGrpWalkin, radioGrpWaitList, radioGrpRefund;
    private String radiogrpWalkin, radiogrpRefund, radiogrpGrpWaitList;

    public ReservationPackageFragment() {
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
        View view = inflater.inflate(R.layout.activity_packages_marketing_opt, container, false);
//        headText.setText("RESERVATION PAKCAGE");
        if (intraction != null) {
            intraction.actionbarsetTitle("Reservation Package");
        }
        showJsonData();
        initViews(view);

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


    private void initViews(View view) {

        imageView = (ImageView) view.findViewById(R.id.imageview);
        radioGrpWalkin = (RadioGroup) view.findViewById(R.id.radioGrpWalkin);
        radio2_yes = (RadioButton) view.findViewById(R.id.radio2_yes);
        radio3_yes = (RadioButton) view.findViewById(R.id.radio3_yes);
        radio3_no = (RadioButton) view.findViewById(R.id.radio3_no);
        radio2_no = (RadioButton) view.findViewById(R.id.radio2_no);
        radio1_yes = (RadioButton) view.findViewById(R.id.radio1_yes);
        radio1_no = (RadioButton) view.findViewById(R.id.radio1_no);
        radioGrpRefund = (RadioGroup) view.findViewById(R.id.radioGrpRefund);
        radioGrpWaitList = (RadioGroup) view.findViewById(R.id.radioGrpWaitList);
        nameEditText = (CustomEditText) view.findViewById(R.id.et_name);
        dine_idEdiText = (CustomEditText) view.findViewById(R.id.dine_id);
        radioGrpWalkin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.radio1_yes) {
                    radiogrpWalkin = "1";
                } else if (checkedId == R.id.radio1_no) {
                    radiogrpWalkin = "0";
                }
            }
        });
        radioGrpRefund.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.radio2_yes) {
                    radiogrpRefund = "1";
                } else if (checkedId == R.id.radio2_no) {
                    radiogrpRefund = "0";
                }
            }
        });
        radioGrpWaitList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.radio3_yes) {
                    radiogrpGrpWaitList = "1";
                } else if (checkedId == R.id.radio3_no) {
                    radiogrpGrpWaitList = "0";
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationPackage();
                Intent intent = new Intent(getActivity(), BusinessNaviDrawer.class);
                startActivity(intent);

               /* Fragment fragment = new TableManagmentFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();*/
            }
        });
    }

    private void reservationPackage() {
        String name = nameEditText.getText().toString().trim();
        String dineId = dine_idEdiText.getText().toString().trim();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_FEEDBACK_AND_MARKETING_API.BUSINESS_RESERV_PACKAGE);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("priority_walk_in", radiogrpWalkin);
        jsonObject.addProperty("refund_deposit", radiogrpRefund);
        jsonObject.addProperty("allow_wait_list", radiogrpGrpWaitList);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("email_dine_id", dineId);
        Log.v(TAG, "Request json object :- " + jsonObject.toString());
        reservationPackageTask(jsonObject);
    }

    private void reservationPackageTask(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_feedback_and_marketing_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response json :- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String priority_walk_in = jsonObject1.getString("priority_walk_in");
                            String refund_deposit = jsonObject1.getString("refund_deposit");
                            String allow_wait_list = jsonObject1.getString("allow_wait_list");
                            String name = jsonObject1.getString("name");

                            String email_dine_id = jsonObject1.getString("email_dine_id");


                        }
                       // Fragment fragment = new TableManagmentFragment();
                        /*Fragment fragment = new TableManagmentFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                        fragmentTransaction.commitAllowingStateLoss();*/

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(getActivity(), "Email Id doesnot exists !!! Choose another one.", Toast.LENGTH_LONG).show();
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


    private void showJsonData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "BusinessView50");
       /* jsonObject.addProperty("user_id", "13");
        jsonObject.addProperty("business_id","1");
      */  jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.v(TAG, "Request json object :- " + jsonObject.toString());
        showJsonDataDetails(jsonObject);
    }

    private void showJsonDataDetails(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.bussines_service_view(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "Response json :- " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("name");
                            String din_id = object.getString("dine_id");
                            String priority_in = object.getString("priority_walk_in");
                            String refund = object.getString("refund_deposit");
                            String wait_list = object.getString("wait_list");
                            nameEditText.setText(name);
                            dine_idEdiText.setText(din_id);
                            if (priority_in.equalsIgnoreCase("1")) {
                                radio1_yes.setChecked(true);
                                radio1_no.setChecked(false);
                            } else if (priority_in.equalsIgnoreCase("0")) {
                                radio1_no.setChecked(true);
                                radio1_yes.setChecked(false);
                            } else if (priority_in.equalsIgnoreCase("")) {
                                radio1_no.setChecked(false);
                                radio1_yes.setChecked(false);
                            } else if (refund.equalsIgnoreCase("1")) {
                                radio2_yes.setChecked(true);
                                radio2_no.setChecked(false);
                            } else if (refund.equalsIgnoreCase("0")) {
                                radio2_no.setChecked(true);
                                radio2_yes.setChecked(false);
                            } else if (refund.equalsIgnoreCase("")) {
                                radio2_no.setChecked(false);
                                radio2_yes.setChecked(false);
                            } else if (wait_list.equalsIgnoreCase("1")) {
                                radio3_yes.setChecked(true);
                                radio3_no.setChecked(false);
                            } else if (wait_list.equalsIgnoreCase("0")) {
                                radio3_no.setChecked(true);
                                radio3_yes.setChecked(false);
                            } else if (wait_list.equalsIgnoreCase("")) {
                                radio3_no.setChecked(false);
                                radio3_yes.setChecked(false);
                            }
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        Toast.makeText(getActivity(), jsonObject1.getString("msg"), Toast.LENGTH_LONG).show();
                        Log.d("onResponse", jsonObject1.getString("msg"));


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