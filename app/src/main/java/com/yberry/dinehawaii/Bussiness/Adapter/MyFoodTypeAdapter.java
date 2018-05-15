package com.yberry.dinehawaii.Bussiness.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by abc on 2/26/2018.
 */

public class MyFoodTypeAdapter extends RecyclerView.Adapter<MyFoodTypeAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "MyFoodTypeAdapter";
    public ArrayList<CheckBoxPositionModel> listValue = new ArrayList<CheckBoxPositionModel>();
    setOnClickListener setOnClickListener;
    String edit_category_id, del_category_id;
    private Context mCotnext;
    private ArrayList<CheckBoxPositionModel> list;
    private Dialog popup;

    public MyFoodTypeAdapter(Context mCotnext, ArrayList<CheckBoxPositionModel> list, setOnClickListener setOnClickListener) {
        this.mCotnext = mCotnext;
        this.list = list;
        this.setOnClickListener = setOnClickListener;
    }

    @Override
    public MyFoodTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_food_type_adapter, parent, false);
        return new MyFoodTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CheckBoxPositionModel optionData = list.get(position);

        if (optionData.getEdit_status().equalsIgnoreCase("0")) {
            holder.edit_layout.setVisibility(View.GONE);
        } else if (optionData.getEdit_status().equalsIgnoreCase("1")) {
            holder.edit_layout.setVisibility(View.VISIBLE);
        } else {
            holder.edit_layout.setVisibility(View.GONE);
        }
        if (optionData != null) {
            holder.checkBoxPositionModel = optionData;
            holder.checkBox.setText(optionData.getName());
            if (optionData.isChckStatus()) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }

            holder.editFoodType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit_category_id = optionData.getId();
                    showEditDialog(optionData.getName());
                    Log.d(TAG, "edit items id :- " + optionData.getFood_ctagory_id() + optionData.getId());
                }
            });
            holder.deleteFoodType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    del_category_id = optionData.getId();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mCotnext);
                    alertDialog.setMessage("Are you sure you want to delete this?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteFoodType(del_category_id);
                        }
                    });

                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                    Log.d(TAG, "delete items id :- " + optionData.getId());
                }
            });

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    optionData.setChckStatus(isChecked);
                    if (isChecked) {
                        listValue.add(optionData);
                        Log.d("DataList", listValue.toString());
                    } else {
                        int pos = listValue.indexOf(optionData);
                        listValue.remove(pos);
                        Log.d("DataListReomve", listValue.toString());
                    }
                }
            });
        }

    }

    private void deleteFoodType(String del_category_id) {
        if (Util.isNetworkAvailable(mCotnext)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.KEY_DEL_FOOD_CAT);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(mCotnext));
            jsonObject.addProperty("food_category_id", del_category_id);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(mCotnext));
            Log.e(TAG, "delete food type json" + jsonObject.toString());
            deleteFoodTypeApi(jsonObject);
        } else {
            Toast.makeText(mCotnext, "Please Connect Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteFoodTypeApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mCotnext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "delete food menu response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        notifyDataSetChanged();
                        Intent intent = new Intent("refreshActivity");
                        LocalBroadcastManager.getInstance(mCotnext).sendBroadcast(intent);
                        Toast.makeText(mCotnext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        Toast.makeText(mCotnext, jsonObject.getString("Already deleted this category name"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mCotnext, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mCotnext, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showEditDialog(String name) {
        popup = new Dialog(mCotnext);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setCancelable(false);
        popup.setCanceledOnTouchOutside(false);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.food_category_dialog);
        CustomTextView title = (CustomTextView) popup.findViewById(R.id.addcategory);
        title.setText("Edit Food Type");
        CustomButton submit = (CustomButton) popup.findViewById(R.id.cat_submit);
        submit.setText("Save");
        final CustomEditText foodtype = (CustomEditText) popup.findViewById(R.id.new_category);
        foodtype.setText(name);
        popup.findViewById(R.id.popupclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(foodtype.getText().toString())) {
                    foodtype.setError("Enter Food Type");
                } else {
                    if (Util.isNetworkAvailable(mCotnext)) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.KEY_EDIT_FOOD_CAT);
                        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(mCotnext));
                        jsonObject.addProperty("food_category", foodtype.getText().toString());
                        jsonObject.addProperty("food_category_id", edit_category_id);
                        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(mCotnext));
                        Log.e(TAG, "add food type json" + jsonObject.toString());
                        editFoodTypeApi(jsonObject);
                    } else {
                        Toast.makeText(mCotnext, "Please Connect Internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        popup.show();
    }

    private void editFoodTypeApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mCotnext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "edit food menu response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        popup.dismiss();
                        notifyDataSetChanged();
                        Intent intent = new Intent("refreshActivity");
                        LocalBroadcastManager.getInstance(mCotnext).sendBroadcast(intent);
                        Toast.makeText(mCotnext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        popup.dismiss();
                        Toast.makeText(mCotnext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        popup.dismiss();
                        Toast.makeText(mCotnext, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mCotnext, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<CheckBoxPositionModel> getSelectedItem() {
        ArrayList<CheckBoxPositionModel> lists = new ArrayList<CheckBoxPositionModel>();
        for (CheckBoxPositionModel model : list) {
            if (model.isChckStatus()) {
                // lists.add(model.getId());
                lists.add(model);
            }
            Log.d(TAG, "CheckBox model " + lists.toString());
        }
        return lists;
    }

    public ArrayList<String> getSelectedItemIds() {

        ArrayList<String> lists = new ArrayList<String>();
        for (CheckBoxPositionModel model : list) {
            if (model.isChckStatus()) {
                // lists.add(model.getId());
                lists.add(model.getId());
            }
            Log.d(TAG, "CheckBox items id :- " + lists.toString());
        }
        return lists;
    }


    @Override
    public void onClick(View view) {
    }

    public interface setOnClickListener {
        public void onItemClick(CheckBoxPositionModel checkBoxPositionModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout edit_layout;
        CheckBoxPositionModel checkBoxPositionModel;
        ImageView editFoodType, deleteFoodType;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            checkBox = (CheckBox) itemView.findViewById(R.id.chk_bussineId);
            edit_layout = (LinearLayout) itemView.findViewById(R.id.ed_layout);
            editFoodType = (ImageView) itemView.findViewById(R.id.edit_food_type);
            deleteFoodType = (ImageView) itemView.findViewById(R.id.delete_food_type);
            this.setIsRecyclable(false);
            // checkBox.setChecked(false);
        }

        @Override
        public void onClick(View view) {
            setOnClickListener.onItemClick(checkBoxPositionModel);

        }
    }
}