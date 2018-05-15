package com.yberry.dinehawaii.Bussiness.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Adapter.BusinessAreaAdapter;
import com.yberry.dinehawaii.Bussiness.model.BusinessAreaModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
import com.yberry.dinehawaii.Util.Util;
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

public class BusinessAreaFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageCouponsFragment";
    Context context;
    ArrayList<BusinessAreaModel> list;
    FragmentIntraction intraction;
    CustomTextView noAreas;
    private RecyclerView recycler_view;
    private BusinessAreaAdapter adapter;
    private Dialog popup;
    CustomButton addNewArea;

    public BusinessAreaFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_coupons, container, false);
        context = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("Business Areas");
        }
        list = new ArrayList<BusinessAreaModel>();
        init(view);
        setAdapter();
        return view;
    }

    private void setAdapter() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BusinessAreaAdapter(context, list);
        recycler_view.setAdapter(adapter);
    }

    private void init(View view) {
        addNewArea=   ((CustomButton) view.findViewById(R.id.addNewCoupon));
        addNewArea.setText("Add Area");
        addNewArea.setOnClickListener(this);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        noAreas = (CustomTextView) view.findViewById(R.id.noCoupons);
        noAreas.setText("No Areas Available");

        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BusinessAreaModel areaModel = list.get(position);
                showEditAreaDialog(areaModel.getAreaId(),areaModel.getAreaName());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void showEditAreaDialog(final String areaId, String areaName) {
        popup = new Dialog(context);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.food_category_dialog);
        final CustomEditText foodtype = (CustomEditText) popup.findViewById(R.id.new_category);
        final CustomTextView foodTitle = (CustomTextView) popup.findViewById(R.id.foodTitle);
        final CustomTextView addcategory = (CustomTextView) popup.findViewById(R.id.addcategory);
        foodTitle.setText("Business Area");
        foodtype.setText(areaName);
        foodtype.setHint("Enter Business Area");
        addcategory.setText("Edit Business Area");
        popup.findViewById(R.id.popupclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                popup.dismiss();
            }
        });
        popup.findViewById(R.id.cat_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(foodtype.getText().toString())) {
                    foodtype.setError("Enter Area");
                } else {
                    if (Util.isNetworkAvailable(getContext())) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.EDITBUSAREA);
                        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getContext()));
                        jsonObject.addProperty("area_id", areaId);
                        jsonObject.addProperty("area_name", foodtype.getText().toString());
                        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getContext()));
                        Log.e(TAG, "add area json>>>" + jsonObject.toString());
                        addNewAreaApi(jsonObject);
                    } else {
                        Toast.makeText(getContext(), "Please Connect Internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        popup.show();
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

    private void getAllAreas() {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.ALLBUSAREA);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "getAllAreas: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_area_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "getAllAreas: Response >> " + resp);
                try {
                    list.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noAreas.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            BusinessAreaModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), BusinessAreaModel.class);
                            list.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        noAreas.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    noAreas.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                noAreas.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isNetworkAvailable(getActivity())) {
            getAllAreas();
        } else
            Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewCoupon:
                addNewAreaDialog();
                break;
            default:
                break;
        }
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void addNewAreaDialog() {
        popup = new Dialog(context);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.food_category_dialog);
        final CustomEditText foodtype = (CustomEditText) popup.findViewById(R.id.new_category);
        final CustomTextView foodTitle = (CustomTextView) popup.findViewById(R.id.foodTitle);
        final CustomTextView addcategory = (CustomTextView) popup.findViewById(R.id.addcategory);
        foodTitle.setText("Business Area");
        foodtype.setHint("Enter Business Area");
        addcategory.setText("Add Business Area");
        popup.findViewById(R.id.popupclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                popup.dismiss();
            }
        });
        popup.findViewById(R.id.cat_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(foodtype.getText().toString())) {
                    foodtype.setError("Enter Area");
                } else {
                    if (Util.isNetworkAvailable(getContext())) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.ADDBUSAREA);
                        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getContext()));
                        jsonObject.addProperty("area_name", foodtype.getText().toString());
                        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getContext()));
                        Log.e(TAG, "add area json>>>" + jsonObject.toString());
                        addNewAreaApi(jsonObject);
                    } else {
                        Toast.makeText(getContext(), "Please Connect Internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        popup.show();
    }

    private void addNewAreaApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_area_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "add area response>>" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        popup.dismiss();
                        list.clear();
                        getAllAreas();
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                    } else {
                        popup.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
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


}

