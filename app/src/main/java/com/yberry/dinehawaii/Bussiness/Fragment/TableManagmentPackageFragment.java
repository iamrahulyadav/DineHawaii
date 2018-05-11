package com.yberry.dinehawaii.Bussiness.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.MultipleServiceType_52B;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 04-02-17.
 */

public class TableManagmentPackageFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TableManagment";
    public List<CheckBoxPositionModel> servicelist = new ArrayList<CheckBoxPositionModel>();
    JsonArray jsonArrayCombineTABLE;
    FragmentIntraction intraction;
    private ArrayList<CheckBoxPositionModel> listFoodService;
    private List<String> stringsList = new ArrayList<String>();
    private ArrayAdapter<CharSequence> reservTableAdapter, reservTableAdapter2, reservTableAdapter3, reservTableAdapter4, reservTableAdapter5, reservTableAdapter6;
    private ArrayAdapter<String> spinnerC1table1Adapter, spinnerC1table2Adapter, spinnerC2table3Adapter, spinnerC2table4Adapter, spinnerC3table5Adapter, spinnerC3table6Adapter;
    private JsonArray jsonArray;
    private ArrayList<Integer> integerList = new ArrayList<>();
    private ImageView imageView;
    private Spinner spinerReservTable, spinerReservA2, spinerReservA3, spinerReservA4, spinerReservA5, spinerReservA6,
            spinnerC1table1, spinnerC1table2, spinnerC2table3, spinnerC2table4, spinnerC3table5, spinnerC3table6;
    private CustomTextView textService, tvServiceA2, tvServiceA3, tvServiceA4, tvServiceA5, tvServiceA6;
    private String listValueNew, service_list, s2, s3, s4, s5, s6, item, spinnerItem2, spinnerItem3, spinnerItem4,
            spinnerItem5, spinnerItem6;
    private Button btnSubmit, btnSubmitCombine, btnAddMore, btnAddMoreCombineTable;
    private CustomEditText etTableA1, etTableA2, etTableA3, etTableA4, etTableA5, etTableA6,
            etCapacity1, etCapacity2, etCapacity3, etCapacity4, etCapacity5, etCapacity6,
            etCobineTableC1, etCobineTableC2, etCobineTableC3;
    private TableRow tableRow1, tableRow2, tableRow3, tableRow4, tableRow5, tableRow6;
    private int count = 1;
    private int home = 1;
    private int where = 0;
    private int tableCombineItem = 0;
    private ImageView imageview;
    private String tableID1, tableID2, tableID3, tableID4, tableID5, tableID6;
    private LinearLayout lltableCombineC2, lltableCombineC3;
    private CustomTextView textViewC1TotalCapacity, textViewC2TotalCapacity, textViewC3TotalCapacity;
    private ArrayList<String> tableList = new ArrayList<String>();
    private ArrayList<String> tableID = new ArrayList<String>();
    private ArrayList<String> tableCapacity = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_table_mangnmet_package, container, false);
//        headText.setText("TABLE MANAGMENT PACKAGE");
        init(view);
        if (intraction != null) {
            intraction.actionbarsetTitle("Table Managment Package");
        }
        apiCalling();
        apiGettingTables();
        onClickListner();
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
        service_list = new String("");
        imageView = (ImageView) view.findViewById(R.id.imageview);
        spinerReservTable = (Spinner) view.findViewById(R.id.spinerReserv);
        spinerReservA2 = (Spinner) view.findViewById(R.id.spinerReservA2);
        spinerReservA3 = (Spinner) view.findViewById(R.id.spinerReservA3);
        spinerReservA4 = (Spinner) view.findViewById(R.id.spinerReservA4);
        spinerReservA5 = (Spinner) view.findViewById(R.id.spinerReservA5);
        spinerReservA6 = (Spinner) view.findViewById(R.id.spinerReservA6);

        spinnerC1table1 = (Spinner) view.findViewById(R.id.spinnerC1table1);
        spinnerC1table2 = (Spinner) view.findViewById(R.id.spinnerC1table2);
        spinnerC2table3 = (Spinner) view.findViewById(R.id.spinnerC2table3);
        spinnerC2table4 = (Spinner) view.findViewById(R.id.spinnerC2table4);
        spinnerC3table5 = (Spinner) view.findViewById(R.id.spinnerC3table5);
        spinnerC3table6 = (Spinner) view.findViewById(R.id.spinnerC3table6);

        textService = (CustomTextView) view.findViewById(R.id.tvService);
        tvServiceA2 = (CustomTextView) view.findViewById(R.id.tvServiceA2);
        tvServiceA3 = (CustomTextView) view.findViewById(R.id.tvServiceA3);
        tvServiceA4 = (CustomTextView) view.findViewById(R.id.tvServiceA4);
        tvServiceA5 = (CustomTextView) view.findViewById(R.id.tvServiceA5);
        tvServiceA6 = (CustomTextView) view.findViewById(R.id.tvServiceA6);

        textViewC1TotalCapacity = (CustomTextView) view.findViewById(R.id.textViewC1TotalCapacity);
        textViewC2TotalCapacity = (CustomTextView) view.findViewById(R.id.textViewC2TotalCapacity);
        textViewC3TotalCapacity = (CustomTextView) view.findViewById(R.id.textViewC3TotalCapacity);

        reservTableAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.ReserveTable, R.layout.support_simple_spinner_dropdown_item);
        reservTableAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinerReservTable.setAdapter(reservTableAdapter);

        reservTableAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.ReserveTable, R.layout.support_simple_spinner_dropdown_item);
        reservTableAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinerReservA2.setAdapter(reservTableAdapter2);

        reservTableAdapter3 = ArrayAdapter.createFromResource(getActivity(), R.array.ReserveTable, R.layout.support_simple_spinner_dropdown_item);
        reservTableAdapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinerReservA3.setAdapter(reservTableAdapter3);

        reservTableAdapter4 = ArrayAdapter.createFromResource(getActivity(), R.array.ReserveTable, R.layout.support_simple_spinner_dropdown_item);
        reservTableAdapter4.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinerReservA4.setAdapter(reservTableAdapter4);

        reservTableAdapter5 = ArrayAdapter.createFromResource(getActivity(), R.array.ReserveTable, R.layout.support_simple_spinner_dropdown_item);
        reservTableAdapter5.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinerReservA5.setAdapter(reservTableAdapter5);

        reservTableAdapter6 = ArrayAdapter.createFromResource(getActivity(), R.array.ReserveTable, R.layout.support_simple_spinner_dropdown_item);
        reservTableAdapter6.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinerReservA6.setAdapter(reservTableAdapter6);


        //Combile Spinner Adapter
        spinnerC1table1Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableList);
        spinnerC1table1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC1table1.setAdapter(spinnerC1table1Adapter);


        spinnerC1table2Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableList);
        spinnerC1table2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC1table2.setAdapter(spinnerC1table2Adapter);

        spinnerC2table3Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableList);
        spinnerC2table3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC2table3.setAdapter(spinnerC2table3Adapter);

        spinnerC2table4Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableList);
        spinnerC2table4Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC2table4.setAdapter(spinnerC2table4Adapter);

        spinnerC3table5Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableList);
        spinnerC3table5Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC3table5.setAdapter(spinnerC3table5Adapter);

        spinnerC3table6Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableList);
        spinnerC3table6Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC3table6.setAdapter(spinnerC3table6Adapter);


        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmitCombine = (Button) view.findViewById(R.id.btnSubmitCombineTable);
        btnAddMoreCombineTable = (Button) view.findViewById(R.id.btnAddMoreCombineTable);
        btnAddMore = (Button) view.findViewById(R.id.btnAddmore);
        etTableA1 = (CustomEditText) view.findViewById(R.id.etTableA1);
        etTableA2 = (CustomEditText) view.findViewById(R.id.etTableA2);
        etTableA3 = (CustomEditText) view.findViewById(R.id.etTableA3);
        etTableA4 = (CustomEditText) view.findViewById(R.id.etTableA4);
        etTableA5 = (CustomEditText) view.findViewById(R.id.etTableA5);
        etTableA6 = (CustomEditText) view.findViewById(R.id.etTableA6);
        etCapacity1 = (CustomEditText) view.findViewById(R.id.etCapacity1);
        etCapacity2 = (CustomEditText) view.findViewById(R.id.etCapacity2);
        etCapacity3 = (CustomEditText) view.findViewById(R.id.etCapacity3);
        etCapacity4 = (CustomEditText) view.findViewById(R.id.etCapacity4);
        etCapacity5 = (CustomEditText) view.findViewById(R.id.etCapacity5);
        etCapacity6 = (CustomEditText) view.findViewById(R.id.etCapacityA6);


        etCobineTableC1 = (CustomEditText) view.findViewById(R.id.etCobineTableC1);
        etCobineTableC2 = (CustomEditText) view.findViewById(R.id.etCobineTableC2);
        etCobineTableC3 = (CustomEditText) view.findViewById(R.id.etCobineTableC3);

        tableRow1 = (TableRow) view.findViewById(R.id.tableRow1);
        tableRow2 = (TableRow) view.findViewById(R.id.tableRow2);
        tableRow3 = (TableRow) view.findViewById(R.id.tableRow3);
        tableRow4 = (TableRow) view.findViewById(R.id.tableRow4);
        tableRow5 = (TableRow) view.findViewById(R.id.tableRow5);
        tableRow6 = (TableRow) view.findViewById(R.id.tableRow6);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        lltableCombineC2 = (LinearLayout) view.findViewById(R.id.lltableCombineC2);
        lltableCombineC3 = (LinearLayout) view.findViewById(R.id.lltableCombineC3);

        jsonArray = new JsonArray();

        jsonArrayCombineTABLE = new JsonArray();

    }

    private void onClickListner() {
        imageView.setOnClickListener(this);
        textService.setOnClickListener(this);
        tvServiceA2.setOnClickListener(this);
        tvServiceA3.setOnClickListener(this);
        tvServiceA4.setOnClickListener(this);
        tvServiceA5.setOnClickListener(this);
        tvServiceA6.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnSubmitCombine.setOnClickListener(this);
        btnAddMore.setOnClickListener(this);
        btnAddMoreCombineTable.setOnClickListener(this);
//        lltableCombineC2.setOnClickListener(this);
//        lltableCombineC3.setOnClickListener(this);
        spinerReservTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getSelectedItem().toString();
                Log.d(TAG + "Spinner", item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinerReservA2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerItem2 = adapterView.getSelectedItem().toString();
                Log.d(TAG + "Spinner2", spinnerItem2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinerReservA3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerItem3 = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinerReservA4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerItem4 = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinerReservA5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerItem5 = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinerReservA6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerItem6 = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerC1table1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int total_cap = Integer.parseInt(tableCapacity.get(spinnerC1table2.getSelectedItemPosition())) + Integer.parseInt(tableCapacity.get(i));
                textViewC1TotalCapacity.setText("" + total_cap);
                Log.e("selected table", tableList.get(i));
                Log.e("selected table id", tableID.get(i));
                Log.e("selected table capacity", tableCapacity.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerC1table2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int total_cap = Integer.parseInt(tableCapacity.get(spinnerC1table1.getSelectedItemPosition())) + Integer.parseInt(tableCapacity.get(i));
                textViewC1TotalCapacity.setText("" + total_cap);
                Log.e("selected table", tableList.get(i));
                Log.e("selected table id", tableID.get(i));
                Log.e("selected table capacity", tableCapacity.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerC2table3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int total_cap = Integer.parseInt(tableCapacity.get(spinnerC2table4.getSelectedItemPosition())) + Integer.parseInt(tableCapacity.get(i));
                textViewC2TotalCapacity.setText("" + total_cap);
                Log.e("selected table", tableList.get(i));
                Log.e("selected table id", tableID.get(i));
                Log.e("selected table capacity", tableCapacity.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerC2table4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int total_cap = Integer.parseInt(tableCapacity.get(spinnerC2table3.getSelectedItemPosition())) + Integer.parseInt(tableCapacity.get(i));
                textViewC2TotalCapacity.setText("" + total_cap);
                Log.e("selected table", tableList.get(i));
                Log.e("selected table id", tableID.get(i));
                Log.e("selected table capacity", tableCapacity.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerC3table5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int total_cap = Integer.parseInt(tableCapacity.get(spinnerC3table6.getSelectedItemPosition())) + Integer.parseInt(tableCapacity.get(i));
                textViewC3TotalCapacity.setText("" + total_cap);
                Log.e("selected table", tableList.get(i));
                Log.e("selected table id", tableID.get(i));
                Log.e("selected table capacity", tableCapacity.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerC3table6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int total_cap = Integer.parseInt(tableCapacity.get(spinnerC3table5.getSelectedItemPosition())) + Integer.parseInt(tableCapacity.get(i));
                textViewC3TotalCapacity.setText("" + total_cap);
                Log.e("selected table", tableList.get(i));
                Log.e("selected table id", tableID.get(i));
                Log.e("selected table capacity", tableCapacity.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void apiCalling() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLSERVICES);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));//AppPreferencesBuss.getUserId(getApplicationContext()));
            Log.e(TAG, jsonObject.toString());
            JsonCallService(jsonObject);

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void JsonCallService(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponse", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            CheckBoxPositionModel model = new CheckBoxPositionModel();
                            JSONObject object = jsonArray1.getJSONObject(i);
                            model.setName(object.getString("service_name"));
                            model.setId(object.getString("id"));
                            model.setChckStatus(false);
                            servicelist.add(model);
                            Log.d("FoodService", String.valueOf(servicelist.size()));
                        }

                        //startActivity(intent);
                    }

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


    private void getValueTableValue() {
        /*if(etTableA1.getText().toString().equalsIgnoreCase(""))
        {
            etTableA1.setError("Enter Table Name");
            return;
        }
        if(etCapacity1.getText().toString().equalsIgnoreCase(""))
        {
            etCapacity1.setError("Enter Capacity");
            return;
        }*/
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etTableA1.getText().toString().trim());
        jsonObject.addProperty("capacity", etCapacity1.getText().toString().trim());
        jsonObject.addProperty("service_type_id", service_list.replace("[", "").replace("]", ""));
        jsonObject.addProperty("reser_priority", item);
        jsonArray.add(jsonObject);
        if (etTableA1.getText().toString().trim() != null)
            stringsList.add(etTableA1.getText().toString());

        /**********Spinner notify*********/

        spinnerC1table1Adapter.notifyDataSetChanged();
        spinnerC1table2Adapter.notifyDataSetChanged();
        spinnerC2table3Adapter.notifyDataSetChanged();
        spinnerC2table4Adapter.notifyDataSetChanged();
        spinnerC3table5Adapter.notifyDataSetChanged();
        spinnerC3table6Adapter.notifyDataSetChanged();
        if (where == 7) {
            editTextValue2();
        }
        if (where == 8) {
            editTextValue2();
            editTextValue3();
        } else if (where == 9) {
            editTextValue2();
            editTextValue3();
            editTextValue4();
        } else if (where == 10) {
            editTextValue2();
            editTextValue3();
            editTextValue4();
            editTextValue5();
        } else if (where == 11) {
            editTextValue2();
            editTextValue3();
            editTextValue4();
            editTextValue5();
            editTextValue6();
        }
        Log.d(TAG + "ETVA", etCapacity1.getText().toString().trim());


    }

    private void editTextValue2() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etTableA2.getText().toString().trim());
        jsonObject.addProperty("capacity", etCapacity2.getText().toString().trim());
        jsonObject.addProperty("service_type_id", s2);
        jsonObject.addProperty("reser_priority", spinnerItem2);
        jsonArray.add(jsonObject);
        Log.d("JSONOBJECT", jsonObject.toString());
        if (etTableA2.getText().toString().trim() != null)
            stringsList.add(etTableA2.getText().toString().trim());
    }

    private void editTextValue3() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etTableA3.getText().toString().trim());
        jsonObject.addProperty("capacity", etCapacity3.getText().toString().trim());
        jsonObject.addProperty("service_type_id", s3);
        jsonObject.addProperty("reser_priority", spinnerItem3);
        jsonArray.add(jsonObject);
        Log.d("JSONOBJECT", jsonObject.toString());
        if (etTableA3.getText().toString().trim() != null)
            stringsList.add(etTableA3.getText().toString().trim());

    }

    private void editTextValue4() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etTableA4.getText().toString().trim());
        jsonObject.addProperty("capacity", etCapacity4.getText().toString().trim());
        jsonObject.addProperty("service_type_id", s4);
        jsonObject.addProperty("reser_priority", spinnerItem4);
        jsonArray.add(jsonObject);
        Log.d("JSONOBJECT", jsonObject.toString());
        if (etTableA4.getText().toString().trim() != null)
            stringsList.add(etTableA4.getText().toString().trim());

    }

    private void editTextValue5() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etTableA5.getText().toString().trim());
        jsonObject.addProperty("capacity", etCapacity5.getText().toString().trim());
        jsonObject.addProperty("service_type_id", s5);
        jsonObject.addProperty("reser_priority", spinnerItem5);
        jsonArray.add(jsonObject);
        Log.d("JSONOBJECT", jsonObject.toString());

        if (etTableA5.getText().toString().trim() != null)
            stringsList.add(etTableA5.getText().toString().trim());

    }

    private void editTextValue6() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etTableA6.getText().toString().trim());
        jsonObject.addProperty("capacity", etCapacity6.getText().toString().trim());
        jsonObject.addProperty("service_type_id", s6);
        jsonObject.addProperty("reser_priority", spinnerItem6);
        jsonArray.add(jsonObject);
        Log.d("JSONOBJECT", jsonObject.toString());
        if (etTableA6.getText().toString().trim() != null)
            stringsList.add(etTableA6.getText().toString().trim());

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageview) {
            //  apiCallingCombineTable();     (App is crashing)
            Fragment fragment = new TableManagmentPackageNextFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


            fragmentTransaction.add(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();


        } else if (view.getId() == R.id.tvService) {
            textService.setText("");
            Intent intent = new Intent(getActivity(), MultipleServiceType_52B.class);
            intent.putParcelableArrayListExtra("ServiceType", (ArrayList<? extends Parcelable>) servicelist);
            startActivityForResult(intent, 12);

        } else if (view.getId() == R.id.tvServiceA2) {
            tvServiceA2.setText("");
            Intent intent = new Intent(getActivity(), MultipleServiceType_52B.class);
            intent.putParcelableArrayListExtra("ServiceType", (ArrayList<? extends Parcelable>) servicelist);
            startActivityForResult(intent, 13);

        } else if (view.getId() == R.id.tvServiceA3) {
            tvServiceA3.setText("");
            Intent intent = new Intent(getActivity(), MultipleServiceType_52B.class);
            intent.putParcelableArrayListExtra("ServiceType", (ArrayList<? extends Parcelable>) servicelist);
            startActivityForResult(intent, 14);

        } else if (view.getId() == R.id.tvServiceA4) {
            tvServiceA4.setText("");
            Intent intent = new Intent(getActivity(), MultipleServiceType_52B.class);
            intent.putParcelableArrayListExtra("ServiceType", (ArrayList<? extends Parcelable>) servicelist);
            startActivityForResult(intent, 15);

        } else if (view.getId() == R.id.tvServiceA5) {
            tvServiceA5.setText("");
            Intent intent = new Intent(getActivity(), MultipleServiceType_52B.class);
            intent.putParcelableArrayListExtra("ServiceType", (ArrayList<? extends Parcelable>) servicelist);
            startActivityForResult(intent, 16);

        } else if (view.getId() == R.id.tvServiceA6) {
            tvServiceA6.setText("");
            Intent intent = new Intent(getActivity(), MultipleServiceType_52B.class);
            intent.putParcelableArrayListExtra("ServiceType", (ArrayList<? extends Parcelable>) servicelist);
            startActivityForResult(intent, 17);

        } else if (view.getId() == R.id.btnSubmit) {
            Log.e("service_list", service_list);
            if (service_list == "") {
                Toast.makeText(getActivity(), "Please select service type !!!", Toast.LENGTH_SHORT).show();
            } else {
                apiAddTable();
                apiGettingTables();

            }
        } else if (view.getId() == R.id.btnSubmitCombineTable) {
            /*getValueTableCombineValue();
            Log.e("combine table data",jsonArrayCombineTABLE.toString());*/
            //combine api here
            apiAddCombineTable();


        } else if (view.getId() == R.id.btnAddmore) {
            for (int i = 0; i <= count; i++) {
                if (count == 1) {
                    tableRow2.setVisibility(View.VISIBLE);
                    where = 7;
                } else if (count == 2) {
                    tableRow3.setVisibility(View.VISIBLE);
                    where = 8;
                } else if (count == 3) {
                    tableRow4.setVisibility(View.VISIBLE);
                    where = 9;
                } else if (count == 4) {
                    tableRow5.setVisibility(View.VISIBLE);
                    where = 10;
                } else if (count == 5) {
                    tableRow6.setVisibility(View.VISIBLE);
                    btnAddMore.setVisibility(View.INVISIBLE);
                    where = 11;
                }
                count++;
                break;
            }

        } else if (view.getId() == R.id.btnAddMoreCombineTable) {
            Log.d(TAG + "", "btnAddMoreCombineTable");
            for (int j = 0; j <= home; j++) {
                if (home == 1) {
                    Log.d(TAG + "COUNT1", String.valueOf(home));
                    lltableCombineC2.setVisibility(View.VISIBLE);
                    tableCombineItem = 1;
                } else if (home == 2) {
                    Log.d(TAG + "COUNT", String.valueOf(home));
                    lltableCombineC3.setVisibility(View.VISIBLE);
                    tableCombineItem = 2;
                    btnAddMoreCombineTable.setVisibility(View.GONE);


                }
                home++;
                Log.d(TAG + "CouFinal", String.valueOf(home));
                break;
            }
        }

    }

    private void apiCallingCombineTable() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonElement jsonElement = jsonArrayCombineTABLE.getAsJsonArray();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_TABLE_COMBINATION);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            getValueTableCombineValue();
            jsonObject.add("tableComboDetails", jsonElement);// jsonArray.getAsJsonArray().toString().replaceAll("\"", ""));
            combineTableValueJson(jsonObject);
            Log.d(TAG + "CombineTb", jsonObject.toString());

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void getValueTableCombineValue() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etCobineTableC1.getText().toString().trim());
        jsonObject.addProperty("capacity", textViewC1TotalCapacity.getText().toString().trim());
        String table_id1 = tableID.get(spinnerC1table1.getSelectedItemPosition());
        String table_id2 = tableID.get(spinnerC1table2.getSelectedItemPosition());
        jsonObject.addProperty("table_id", table_id1 + "," + table_id2);
        jsonArrayCombineTABLE.add(jsonObject);
        Log.d(TAG + "CombineJs", jsonObject.toString());
        if (tableCombineItem == 1) {
            getValueCombineTable2();
        }
        if (tableCombineItem == 2) {
            getValueCombineTable2();
            getValueCombineTable3();
        }

    }

    private void getValueCombineTable3() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etCobineTableC3.getText().toString().trim());
        jsonObject.addProperty("capacity", textViewC3TotalCapacity.getText().toString().trim());
        String table_id1 = tableID.get(spinnerC3table5.getSelectedItemPosition());
        String table_id2 = tableID.get(spinnerC3table6.getSelectedItemPosition());
        jsonObject.addProperty("table_id", table_id1 + "," + table_id2);
        jsonArrayCombineTABLE.add(jsonObject);

    }

    private void getValueCombineTable2() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("table_name", etCobineTableC2.getText().toString().trim());
        jsonObject.addProperty("capacity", textViewC2TotalCapacity.getText().toString().trim());
        String table_id1 = tableID.get(spinnerC2table3.getSelectedItemPosition());
        String table_id2 = tableID.get(spinnerC2table4.getSelectedItemPosition());
        jsonObject.addProperty("table_id", table_id1 + "," + table_id2);
        jsonArrayCombineTABLE.add(jsonObject);

    }

    private void combineTableValueJson(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG + "onResponseTableCombine", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d(TAG, "Service id Result :" + s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Fragment fragment = new TableManagmentPackageNextFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


                        fragmentTransaction.add(R.id.frame, fragment, fragment.getTag());
                        fragmentTransaction.commitAllowingStateLoss();
                    }

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

    private void apiAddTable() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonElement jsonElement = jsonArray.getAsJsonArray();
            Log.d(TAG, "Json Array tabel :- " + jsonElement);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_TABLE);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            /*jsonObject.addProperty("business_id", "1");
            jsonObject.addProperty("user_id", "13");*/
            getValueTableValue();
            jsonObject.add("tableDetails", jsonElement /*jsonArray.getAsJsonArray().toString().replaceAll("\"", "")*/);
            addTableJson(jsonObject);
            Log.e(TAG, "Request :- " + jsonObject.toString());
            Log.e(TAG, "listValue :- " + stringsList.toString());

            //Log.e("final Data submitted", jsonArray.toString());

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void addTableJson(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG + "onResponseTable", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        //JSONObject jobj = jsonObject.getJSONObject("msg");
                        //String msg= jobj.getString("msg");
                        Toast.makeText(getActivity(), "Table Added", Toast.LENGTH_SHORT).show();

                    } else {
                        //   Toast.makeText(getActivity(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                    }

                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, new TableManagmentPackageFragment());
                    transaction.commit();

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

    private void apiGettingTables() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonElement jsonElement = jsonArray.getAsJsonArray();
            Log.d(TAG, "Json Array tabel :- " + jsonElement);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.GET_BUSINESS_TABLE);
            /*jsonObject.addProperty("business_id", "1");
            jsonObject.addProperty("user_id", "13");
            */
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));


            Log.e(TAG, "Request :- " + jsonObject.toString());
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
                Log.e(TAG + "onResponseTable", response.body().toString());
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
                            tableCapacity.add(jsonObject1.getString("capacity"));

                        }
                        spinnerC1table1Adapter.notifyDataSetChanged();
                        spinnerC1table2Adapter.notifyDataSetChanged();
                        spinnerC2table3Adapter.notifyDataSetChanged();
                        spinnerC2table4Adapter.notifyDataSetChanged();
                        spinnerC3table5Adapter.notifyDataSetChanged();
                        spinnerC3table6Adapter.notifyDataSetChanged();
                    }

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

    private void apiAddCombineTable() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonElement jsonElement = jsonArrayCombineTABLE.getAsJsonArray();
            Log.d(TAG, "Json Array tabel :- " + jsonElement);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_COMBINE_BUSINESS_TABLE);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            //jsonObject.addProperty("business_id", "1");
            //jsonObject.addProperty("user_id", "13");
            getValueTableCombineValue();
            jsonObject.add("tableDetails", jsonElement /*jsonArray.getAsJsonArray().toString().replaceAll("\"", "")*/);
            addCombineTableJson(jsonObject);
            Log.e(TAG, "Request :- " + jsonObject.toString());
            Log.e(TAG, "listValue :- " + stringsList.toString());

            //Log.e("final Data submitted", jsonArray.toString());

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }

    private void addCombineTableJson(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_combine_table_system_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseTable", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        //JSONObject jobj = jsonObject.getJSONObject("msg");
                        //String msg= jobj.getString("msg");
                        Toast.makeText(getActivity(), "Table Added", Toast.LENGTH_SHORT).show();

                    } else {
                        //   Toast.makeText(getActivity(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                    }

                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame, new TableManagmentPackageFragment());
                    transaction.commit();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 12) {
                Log.d("OnActivtyServiceType", "OnActivtyResultServiceType");
                listFoodService = data.getParcelableArrayListExtra("servicetype");
                Log.d(TAG, "StringArrayList :- " + String.valueOf(listFoodService));
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");

                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(textService.getText().toString())) {
                        textService.setText(listValue.getName());
                        integerList.add(Integer.valueOf(listValue.getId()));
                    } else {
                        textService.setText(textService.getText().toString() + "," + listValue.getName());
                        integerList.add(Integer.valueOf(listValue.getId()));
                        Log.d(TAG + "ListV", integerList.toString());
                    }
                }
                Log.v(TAG, "List values :-  " + listValueNew);
                service_list = integerList.toString();
                Log.d(TAG, "List of services needed :- " + service_list.replace("[", "").replace("]", ""));
            }
            if (requestCode == 13) {
                listFoodService = data.getParcelableArrayListExtra("servicetype");
                Log.d("StringArrayList", String.valueOf(listFoodService));
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");
                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(tvServiceA2.getText().toString())) {
                        tvServiceA2.setText(listValue.getName());
                    } else {
                        tvServiceA2.setText(tvServiceA2.getText().toString() + "," + listValue.getName());
                        integerList.add(Integer.valueOf(listValue.getId()));

                    }
                }
                s2 = integerList.toString();
                Log.d(TAG + "ListV2", s2);
            }
            if (requestCode == 14) {
                listFoodService = data.getParcelableArrayListExtra("servicetype");
                Log.d("StringArrayList", String.valueOf(listFoodService));
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");
                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(tvServiceA3.getText().toString())) {
                        tvServiceA3.setText(listValue.getName());
                    } else {
                        tvServiceA3.setText(tvServiceA3.getText().toString() + "," + listValue.getName());
                        integerList.add(Integer.valueOf(listValue.getId()));
                    }
                }
                s3 = integerList.toString();
                Log.d(TAG + "ListV3", s3);
            }
            if (requestCode == 15) {
                listFoodService = data.getParcelableArrayListExtra("servicetype");
                Log.d("StringArrayList", String.valueOf(listFoodService));
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");
                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(tvServiceA4.getText().toString())) {
                        tvServiceA4.setText(listValue.getName());
                    } else {
                        tvServiceA4.setText(tvServiceA4.getText().toString() + "," + listValue.getName());
                        //integerList=listValue.getId();
                        integerList.add(Integer.valueOf(listValue.getId()));

                    }
                }
                s4 = integerList.toString();
                Log.d(TAG + "ListV4", s4);
            }
            if (requestCode == 16) {
                listFoodService = data.getParcelableArrayListExtra("servicetype");
                Log.d("StringArrayList", String.valueOf(listFoodService));
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");
                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(tvServiceA5.getText().toString())) {
                        tvServiceA5.setText(listValue.getName());
                    } else {
                        tvServiceA5.setText(tvServiceA5.getText().toString() + "," + listValue.getName());
                        integerList.add(Integer.valueOf(listValue.getId()));
                    }
                }
                s5 = integerList.toString();
                Log.d(TAG + "ListV5", s5);

            }
            if (requestCode == 17) {
                listFoodService = data.getParcelableArrayListExtra("servicetype");
                Log.d("StringArrayList", String.valueOf(listFoodService));
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");
                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(tvServiceA6.getText().toString())) {
                        tvServiceA6.setText(listValue.getName());
                    } else {
                        tvServiceA6.setText(tvServiceA6.getText().toString() + "," + listValue.getName());
                        integerList.add(Integer.valueOf(listValue.getId()));
                    }
                }
                s6 = integerList.toString();
                Log.d(TAG + "ListV6", s6);
            }
        }
    }

}
