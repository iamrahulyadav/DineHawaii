package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 04-02-17.
 */

public class TableManagmentPackageNextFragment extends Fragment implements View.OnClickListener {
    String valueRadio = "No";
    JsonArray arrayelements;
    private ImageView ivContinue;
    private Button btnAdd;
    private RadioGroup radioGroup;
    private LinearLayout linearNo3, linearNo4, linearNo5, linearNo6, linearNo7, linearNo8, linearNo9;
    private CustomEditText etBreakfast1, etLunch1, etDinner1, etOther1, etBreakfast2, etLunch2, etDinner2, etOther2, etBreakfast3, etLunch3, etDinner3, etOther3, etBreakfast4, etLunch4, etDinner4, etOther4, etBreakfast5, etLunch5, etDinner5, etOther5, etBreakfast6, etLunch6, etDinner6, etOther6, etBreakfast7, etLunch7, etDinner7, etOther7, etBreakfast8, etLunch8, etDinner8, etOther8;
    private Context mContext;
    private String TAG = "TABLEMANAGMENT PACKAGE NEXT", data1;
    private int Count = 1;
    private int where = 3;

    public TableManagmentPackageNextFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentIntraction intraction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_table_managnmet_package, container, false);
//        headText.setText("TABLE MANAGMENT PACKAGE");
        if (intraction != null) {
            intraction.actionbarsetTitle("Table Managment Package");
        }
        RelativeLayout mainView = (RelativeLayout) view.findViewById(R.id.relativeMain);
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

        init(view);
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


    private void init(final View view) {
        mContext = getActivity();
        arrayelements = new JsonArray();
        ivContinue = (ImageView) view.findViewById(R.id.ivContinue);
        btnAdd = (Button) view.findViewById(R.id.btnAddmore);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        etBreakfast1 = (CustomEditText) view.findViewById(R.id.etbreakfast1);
        etLunch1 = (CustomEditText) view.findViewById(R.id.etlunch1);
        etDinner1 = (CustomEditText) view.findViewById(R.id.etdinner1);
        etOther1 = (CustomEditText) view.findViewById(R.id.etother1);

        etBreakfast2 = (CustomEditText) view.findViewById(R.id.etbreakfast2);
        etLunch2 = (CustomEditText) view.findViewById(R.id.etlunch2);
        etDinner2 = (CustomEditText) view.findViewById(R.id.etdinner2);
        etOther2 = (CustomEditText) view.findViewById(R.id.etother2);

        etBreakfast3 = (CustomEditText) view.findViewById(R.id.etbreakfast3);
        etLunch3 = (CustomEditText) view.findViewById(R.id.etlunch3);
        etDinner3 = (CustomEditText) view.findViewById(R.id.etdinner3);
        etOther3 = (CustomEditText) view.findViewById(R.id.etother3);

        etBreakfast4 = (CustomEditText) view.findViewById(R.id.etbreakfast4);
        etLunch4 = (CustomEditText) view.findViewById(R.id.etlunch4);
        etDinner4 = (CustomEditText) view.findViewById(R.id.etdinner4);
        etOther4 = (CustomEditText) view.findViewById(R.id.etother4);

        etBreakfast5 = (CustomEditText) view.findViewById(R.id.etbreakfast5);
        etLunch5 = (CustomEditText) view.findViewById(R.id.etlunch5);
        etDinner5 = (CustomEditText) view.findViewById(R.id.etdinner5);
        etOther5 = (CustomEditText) view.findViewById(R.id.etother5);

        etBreakfast6 = (CustomEditText) view.findViewById(R.id.etbreakfast6);
        etLunch6 = (CustomEditText) view.findViewById(R.id.etlunch6);
        etDinner6 = (CustomEditText) view.findViewById(R.id.etdinner6);
        etOther6 = (CustomEditText) view.findViewById(R.id.etother6);

        etBreakfast7 = (CustomEditText) view.findViewById(R.id.etbreakfast7);
        etLunch7 = (CustomEditText) view.findViewById(R.id.etlunch7);
        etDinner7 = (CustomEditText) view.findViewById(R.id.etdinner7);
        etOther7 = (CustomEditText) view.findViewById(R.id.etother7);

        etBreakfast8 = (CustomEditText) view.findViewById(R.id.etbreakfast8);
        etLunch8 = (CustomEditText) view.findViewById(R.id.etlunch8);
        etDinner8 = (CustomEditText) view.findViewById(R.id.etdinner8);
        etOther8 = (CustomEditText) view.findViewById(R.id.etother8);


        linearNo3 = (LinearLayout) view.findViewById(R.id.linearNumber3);
        linearNo4 = (LinearLayout) view.findViewById(R.id.linearNumber4);
        linearNo5 = (LinearLayout) view.findViewById(R.id.linearNumber5);
        linearNo6 = (LinearLayout) view.findViewById(R.id.linearNumber6);
        linearNo7 = (LinearLayout) view.findViewById(R.id.linearNumber7);
        linearNo8 = (LinearLayout) view.findViewById(R.id.linearNumber8);
        linearNo9 = (LinearLayout) view.findViewById(R.id.linearNumber9);


        ivContinue.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) view.findViewById(id);
                //jsonObjectOther = new JsonObject();

                Log.d("radioVALUE", button.getText().toString());
                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    valueRadio = "Yes";

                } else if (button.getText().toString().equalsIgnoreCase("No")) {
                    valueRadio = "No";
                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivContinue) {
          PacakgeData();
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("size", "2");
            jsonObject1.addProperty("breakfast_time", etBreakfast1.getText().toString().trim());
            jsonObject1.addProperty("lunch_time", etLunch1.getText().toString().trim());
            jsonObject1.addProperty("dinner_time", etDinner1.getText().toString().trim());
            jsonObject1.addProperty("other_time", etOther1.getText().toString().trim());
            arrayelements.add(jsonObject1);
            Log.d("JSONOBJECT:", jsonObject1.toString());
            Log.d("JSONARRAYVALUE", arrayelements.toString());

            if (Util.isNetworkAvailable(mContext)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_TABLE_TIME);
                getEditTextValue();
                jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                jsonObject.addProperty("wait_list_option", valueRadio);
                jsonObject.add("tableSizeTime", arrayelements.getAsJsonArray());
                Log.e("OBJECT", jsonObject.toString());
                JsonCallMethod(jsonObject);

            } else {
                Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            }
        }

        if (v.getId() == R.id.btnAddmore) {
            for (int i = 0; i <= Count; i++) {
                if (Count == 1) {
                    linearNo3.setVisibility(View.VISIBLE);
                    where = 7;

                } else if (Count == 2) {
                    linearNo4.setVisibility(View.VISIBLE);
                    where = 8;
                } else if (Count == 3) {
                    linearNo5.setVisibility(View.VISIBLE);
                    where = 9;
                } else if (Count == 4) {
                    linearNo6.setVisibility(View.VISIBLE);
                    where = 10;

                } else if (Count == 5) {
                    linearNo7.setVisibility(View.VISIBLE);
                    where = 11;

                } else if (Count == 6) {
                    linearNo8.setVisibility(View.VISIBLE);
                    where = 12;

                } else if (Count == 7) {
                    linearNo9.setVisibility(View.VISIBLE);
                    btnAdd.setVisibility(View.GONE);
                    where = 13;

                }
                Count++;
                break;
            }

        }

    }

    private void getEditTextValue() {
        if (where == 7) {
            getEditTextValueFirstLayout();

        } else if (where == 8) {
            getEditTextValueFirstLayout();
            getEditTextValueSecondLayout();

        } else if (where == 9) {
            getEditTextValueFirstLayout();
            getEditTextValueSecondLayout();
            getEditTextValueThirdLayout();

        } else if (where == 10) {
            getEditTextValueFirstLayout();
            getEditTextValueSecondLayout();
            getEditTextValueThirdLayout();
            getEditTextValueFourthLayout();

        } else if (where == 11) {
            getEditTextValueFirstLayout();
            getEditTextValueSecondLayout();
            getEditTextValueThirdLayout();
            getEditTextValueFourthLayout();
            getEditTextValueFiveLayout();

        } else if (where == 12) {
            getEditTextValueFirstLayout();
            getEditTextValueSecondLayout();
            getEditTextValueThirdLayout();
            getEditTextValueFourthLayout();
            getEditTextValueFiveLayout();
            getEditTextValueSixLayout();

        } else if (where == 13) {
            getEditTextValueFirstLayout();
            getEditTextValueSecondLayout();
            getEditTextValueThirdLayout();
            getEditTextValueFourthLayout();
            getEditTextValueFiveLayout();
            getEditTextValueSixLayout();
            getEditTextValueSevenLayout();

        }

    }

    private void getEditTextValueSevenLayout() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("size", "9");
        jsonObject1.addProperty("breakfast_time", etBreakfast8.getText().toString().trim());
        jsonObject1.addProperty("lunch_time", etLunch8.getText().toString().trim());
        jsonObject1.addProperty("dinner_time", etDinner8.getText().toString().trim());
        jsonObject1.addProperty("other_time", etOther8.getText().toString().trim());
        arrayelements.add(jsonObject1);
        Log.d("JSONOBJECT:", jsonObject1.toString());

    }

    private void getEditTextValueSixLayout() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("size", "8");
        jsonObject1.addProperty("breakfast_time", etBreakfast7.getText().toString().trim());
        jsonObject1.addProperty("lunch_time", etLunch7.getText().toString().trim());
        jsonObject1.addProperty("dinner_time", etDinner7.getText().toString().trim());
        jsonObject1.addProperty("other_time", etOther7.getText().toString().trim());
        arrayelements.add(jsonObject1);
        Log.d("JSONOBJECT:", jsonObject1.toString());

    }

    private void getEditTextValueFiveLayout() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("size", "7");
        jsonObject1.addProperty("breakfast_time", etBreakfast6.getText().toString().trim());
        jsonObject1.addProperty("lunch_time", etLunch6.getText().toString().trim());
        jsonObject1.addProperty("dinner_time", etDinner6.getText().toString().trim());
        jsonObject1.addProperty("other_time", etOther6.getText().toString().trim());
        arrayelements.add(jsonObject1);
        Log.d("JSONOBJECT:", jsonObject1.toString());
    }

    private void getEditTextValueFourthLayout() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("size", "6");
        jsonObject1.addProperty("breakfast_time", etBreakfast5.getText().toString().trim());
        jsonObject1.addProperty("lunch_time", etLunch5.getText().toString().trim());
        jsonObject1.addProperty("dinner_time", etDinner5.getText().toString().trim());
        jsonObject1.addProperty("other_time", etOther5.getText().toString().trim());
        arrayelements.add(jsonObject1);
        Log.d("JSONOBJECT:", jsonObject1.toString());

    }

    private void getEditTextValueThirdLayout() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("size", "5");
        jsonObject1.addProperty("breakfast_time", etBreakfast4.getText().toString().trim());
        jsonObject1.addProperty("lunch_time", etLunch4.getText().toString().trim());
        jsonObject1.addProperty("dinner_time", etDinner4.getText().toString().trim());
        jsonObject1.addProperty("other_time", etOther4.getText().toString().trim());
        arrayelements.add(jsonObject1);
        Log.d("JSONOBJECT:", jsonObject1.toString());
    }

    private void getEditTextValueSecondLayout() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("size", "4");
        jsonObject1.addProperty("breakfast_time", etBreakfast3.getText().toString().trim());
        jsonObject1.addProperty("lunch_time", etLunch3.getText().toString().trim());
        jsonObject1.addProperty("dinner_time", etDinner3.getText().toString().trim());
        jsonObject1.addProperty("other_time", etOther3.getText().toString().trim());
        arrayelements.add(jsonObject1);
        Log.d("JSONOBJECT:", jsonObject1.toString());

    }

    private void getEditTextValueFirstLayout() {
        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("size", "3");
        jsonObject1.addProperty("breakfast_time", etBreakfast2.getText().toString().trim());
        jsonObject1.addProperty("lunch_time", etLunch2.getText().toString().trim());
        jsonObject1.addProperty("dinner_time", etDinner2.getText().toString().trim());
        jsonObject1.addProperty("other_time", etOther2.getText().toString().trim());
        arrayelements.add(jsonObject1);
        Log.d("JSONOBJECT:", jsonObject1.toString());

    }

    private void JsonCallMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_table_system_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseDeliverService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                     //   PacakgeData();
                    }

                    //  adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }

    @SuppressLint("LongLogTag")
    public void PacakgeData() {
        String packageNo = AppPreferencesBuss.getBussiPackagelist(getActivity());
      //  String[] data = packageNo.replace("").split(",");
        String[] data = packageNo.replace(" ", "").split(",");
        List<String> list = Arrays.asList(data);

        //34
        Log.d("package_no",list.toString());
        if (list.contains("3")) {
            Log.d("package_no",list.toString());
            // OnlineOrderingAndPaymentFragment
            Fragment fragment = new OnlineOrderingAndPaymentFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        } else if (list.contains("4")) {
//            Log.d("package_no4",data1);
            Fragment fragment = new FoodServiceFragment45();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
            //break;
        }else{
            Intent intent = new Intent(getActivity(), BusinessNaviDrawer.class);
            startActivity(intent);
        }


    }

}
