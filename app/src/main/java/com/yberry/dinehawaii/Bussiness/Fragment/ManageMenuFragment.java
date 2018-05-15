package com.yberry.dinehawaii.Bussiness.Fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.EditMenuItemActivity;
import com.yberry.dinehawaii.Bussiness.Activity.SelectFoodTypeActivity;
import com.yberry.dinehawaii.Bussiness.Adapter.MenuItemsAdapter;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
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

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageMenuFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageMenuFragment";
    public static ArrayList<OrderItemsDetailsModel> itemslist = new ArrayList<OrderItemsDetailsModel>();
    Context context;
    CustomButton addfood, addfoodtype;
    MenuItemsAdapter menuItemsAdapter;
    ArrayList<CheckBoxPositionModel> listFoodService;
    Dialog popup;
    RelativeLayout foodLayout;
    FragmentIntraction intraction;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private String food_type_id_list = "0", listValueNew, listName;
    private CustomTextView text_food;

    public ManageMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_menu, container, false);
        context = getActivity();
//        headText.setText("Manage Menu");
        if (intraction != null) {
            intraction.actionbarsetTitle("Manage Menu");
        }
        foodLayout = (RelativeLayout) view.findViewById(R.id.foodLayout);
        checkDuties();
        itemslist.clear();
        text_food = (CustomTextView) view.findViewById(R.id.spinSelectFoodType);
        text_food.setOnClickListener(this);
        addfood = (CustomButton) view.findViewById(R.id.addNewfood);
        addfoodtype = (CustomButton) view.findViewById(R.id.addfoodtype);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new MyBroadcast(), new IntentFilter("menuItemRefresh"));
        mRecyclerView.setAdapter(menuItemsAdapter);
        menuItemsAdapter = new MenuItemsAdapter(context, itemslist);
        mRecyclerView.setAdapter(menuItemsAdapter);
        menuItemsAdapter.notifyDataSetChanged();

        addfood.setOnClickListener(this);
        addfoodtype.setOnClickListener(this);

        return view;
    }

    private void checkDuties() {
        String alottedDuty = AppPreferencesBuss.getAllottedDuties(getActivity());
        if (!TextUtils.isEmpty(alottedDuty)) {
            if (alottedDuty.contains("4") && alottedDuty.contains("5")) {
                foodLayout.setVisibility(View.VISIBLE);
            } else if (alottedDuty.contains("5")) {
                foodLayout.setVisibility(View.GONE);
            } else {
                foodLayout.setVisibility(View.VISIBLE);
            }
        }
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
    public void onResume() {
        super.onResume();
        if (food_type_id_list.equalsIgnoreCase("0") || food_type_id_list.equalsIgnoreCase("")) {
            text_food.setText("");
            itemslist.clear();
        } else {
            text_food.setText(listName);
            getMenuItems();
        }
    }

    private void showCategoryDialog() {
        popup = new Dialog(context);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.food_category_dialog);
        final CustomEditText foodtype = (CustomEditText) popup.findViewById(R.id.new_category);
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
                    foodtype.setError("Enter Food Type");
                } else {
                    if (Util.isNetworkAvailable(getContext())) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.KEY_ADD_FOOD_TYPE);
                        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getContext()));
                        jsonObject.addProperty("food_category", foodtype.getText().toString());
                        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getContext()));
                        Log.e(TAG, "add food type json" + jsonObject.toString());
                        addNewFoodApi(jsonObject);
                    } else {
                        Toast.makeText(getContext(), "Please Connect Internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        popup.show();

    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void addNewFoodApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_food_vendor_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "get menu json" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        popup.dismiss();
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        popup.dismiss();
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void getMenuItems() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.ALL_FOOD_MODIFY_CATEGORIES);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("food_category_id", food_type_id_list);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "get menu json" + jsonObject.toString());
        getMenuItemsApi(jsonObject);
    }

    private void getMenuItemsApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessUserBusinessApi(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "get menu resp" + response.body().toString());
                String resp = response.body().toString();
                try {
                    itemslist.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            OrderItemsDetailsModel menuModel = new OrderItemsDetailsModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            menuModel.setCat_id(object.getString("food_cat_id"));
                            menuModel.setItemName(object.getString("dish_name"));
                            menuModel.setHalf_price(object.getString("half_price"));
                            menuModel.setItemPrice(object.getString("price"));
                            menuModel.setItem_category(object.getString("food_name"));
                            menuModel.setFood_type_id(object.getString("food_type_id"));
                            menuModel.setDetails(object.getString("detail"));
                            menuModel.setItemImage(object.getString("food_image"));
                            menuModel.setServiceType(object.getString("service_name"));
                            menuModel.setItemEditId(object.getString("edit_id"));
                            menuModel.setService_type_id(object.getString("service_id"));
                            menuModel.setMenuItemStatus(object.getString("status"));
                            menuModel.setBusAreaId(object.getString("area_id"));
                            menuModel.setBusAreaName(object.getString("area_name"));
                            itemslist.add(menuModel);

                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(getActivity(), "no record found", Toast.LENGTH_SHORT).show();
                    }
                    menuItemsAdapter.notifyDataSetChanged();

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spinSelectFoodType:
                food_type_id_list = "0";
                listName = "";
                text_food.setText("");
                Intent intent1 = new Intent(getContext(), SelectFoodTypeActivity.class);
                startActivityForResult(intent1, 19);
                break;
            case R.id.addNewfood:
                Intent intent = new Intent(getActivity(), EditMenuItemActivity.class);
                intent.setAction("addmenu");
                getContext().startActivity(intent);
                break;
            case R.id.addfoodtype:
                showCategoryDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 19) {
                listFoodService = data.getParcelableArrayListExtra("foodtype");
                Log.e(TAG, "onActivityResult: listFoodService >> " + listFoodService.toString());
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");
                Log.v(TAG, " " + listValueNew);

                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(text_food.getText().toString())) {
                        listName = listValue.getName();
                        text_food.setText(listValue.getName());
                        food_type_id_list = listValue.getId();
                        if (Util.isNetworkAvailable(getContext())) {
                            getMenuItems();
                        } else {
                            Toast.makeText(getContext(), "Please Connect Internet", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        listName = text_food.getText().toString() + "," + listValue.getName();
                        text_food.setText(text_food.getText().toString() + "," + listValue.getName());
                        food_type_id_list = food_type_id_list + "," + listValue.getId();
                        if (Util.isNetworkAvailable(getContext())) {
                            getMenuItems();
                        } else {
                            Toast.makeText(getContext(), "Please Connect Internet", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        } else {
            food_type_id_list = "0";
            Log.e(TAG, "error on activity result");
        }
    }

    class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (food_type_id_list.equalsIgnoreCase("0") || food_type_id_list.equalsIgnoreCase("")) {
                text_food.setText("");
                itemslist.clear();
            } else {
                text_food.setText(listName);
                getMenuItems();
            }
        }
    }

}

