package com.yberry.dinehawaii.Bussiness.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessLoginError;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.Bussiness.model.OtherBusinessModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.ViewHolder> {
    private static final String TAG = "CouponAdapter";
    Context context;
    ArrayList<OtherBusinessModel> reservList;
    private Activity activity;

    public BusinessListAdapter(Activity activity, Context context, ArrayList<OtherBusinessModel> reservList) {
        this.activity = activity;
        this.context = context;
        this.reservList = reservList;
    }

    @Override
    public BusinessListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final BusinessListAdapter.ViewHolder holder, final int position) {
        final OtherBusinessModel model = reservList.get(position);
        holder.tvBussName.setText(model.getBusinessName());
        holder.tvBussAddr.setText("Address : " + model.getBusinessAddress());
        holder.tvEmail.setText("Email: " + model.getBusinessEmail());
        holder.tvContactNo.setText("Contact No : " + model.getBusinessContactNo());

        holder.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLogin(model);
            }
        });
        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getStatus().equalsIgnoreCase("A"))
                    dialogDisable(model, position);
                else
                    dialogEnable(model, position);
            }
        });

        if (model.getStatus().equalsIgnoreCase("A")) {
            holder.tvLogin.setVisibility(View.VISIBLE);
            holder.tvRemove.setText("Make Inactive");
        } else {
            holder.tvLogin.setVisibility(View.GONE);
            holder.tvRemove.setText("Make Active");
        }

        if (!model.getBusinessLogo().equalsIgnoreCase("")) {
            Glide.with(context).load(model.getBusinessName())
                    .thumbnail(0.5f)
                    .crossFade()
//                    .override(50, 50)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.restLogo);
        }
    }

    private void dialogDisable(final OtherBusinessModel model, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Disable " + model.getBusinessName());
        builder.setMessage("Do you want to disable this business?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                disableBusiness(model.getId(), position);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void dialogEnable(final OtherBusinessModel model, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enable " + model.getBusinessName());
        builder.setMessage("Do you want to enable this business?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enableBusiness(model.getId(), position );
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void dialogLogin(final OtherBusinessModel model) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Switch Business");
        builder.setMessage("Do you want to login into " + model.getBusinessName()
                + ", you will be logged out from your current account."
                + "\n\nEnter password");
        builder.setCancelable(false);
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.text_input_password, (ViewGroup) activity.findViewById(android.R.id.content), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton("Continue Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pass = input.getText().toString();
                Log.e(TAG, "onClick: pass >> " + pass);
                loginApi(model.getBusinessEmail(), pass);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
        input.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (input.length() > 2) {
                    alertDialog.getButton(AlertDialog.BUTTON1).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON1).setEnabled(false);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return reservList.size();
    }

    private void loginApi(final String email, final String password) {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.REGISTRATION.BUSINESSUSERLOGIN);
            jsonObject.addProperty("email_id", email);
            jsonObject.addProperty("password", password);
            jsonObject.addProperty("fcm_id", FirebaseInstanceId.getInstance().getToken());
            Log.e(TAG, "Request BUSINESS LOGIN >> " + jsonObject.toString());
            final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            MyApiEndpointInterface apiService =
                    ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.requestBusinessUrl(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response BUSINESS LOGIN >> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                            AppPreferencesBuss.setIsSubBusiness(context, true);
                            AppPreferencesBuss.setMainBusinessEmail(context, AppPreferencesBuss.getBussiEmilid(context));

                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                            //AppPreferences.setCustomerid(context, jsonObject1.getString("user_id"));
                            AppPreferencesBuss.setUserId(context, jsonObject1.getString("user_id"));
                            AppPreferencesBuss.setBussiEmilid(context, jsonObject1.getString("email_id"));
                            AppPreferencesBuss.setBussiName(context, jsonObject1.getString("bussiness_name"));
                            AppPreferencesBuss.setBussiFein(context, jsonObject1.getString("bussiness_fein"));
                            AppPreferencesBuss.setBussiId(context, jsonObject1.getString("bussiness_id"));
                            AppPreferencesBuss.setBussiPackagelist(context, jsonObject1.getString("package_id"));
                            AppPreferencesBuss.setBussiOptionlist(context, jsonObject1.getString("option_id"));
                            AppPreferencesBuss.setfirstname(context, jsonObject1.getString("first_name"));
                            AppPreferencesBuss.setBussiPhoneNo(context, jsonObject1.getString("contact_no"));
                            AppPreferencesBuss.setUsertypeid(context, jsonObject1.getString("user_type"));

                            if (jsonObject1.getString("multisite").equalsIgnoreCase("1"))
                                AppPreferencesBuss.setIsMultisite(context, true);
                            else
                                AppPreferencesBuss.setIsMultisite(context, false);

                            AppPreferences.setUserType(context, AppConstants.BUSS_LOGIN_TYPE.BUSINESS_USER);

                            if (jsonObject1.getString("admin_approval").equalsIgnoreCase("1"))
                                AppPreferencesBuss.setVerified_status(context, true);
                            else
                                AppPreferencesBuss.setVerified_status(context, false);

                            if (jsonObject1.getString("user_image").length() == 0) {
                                AppPreferencesBuss.setProfileImage(context, "");
                            } else {
                                AppPreferencesBuss.setProfileImage(context, jsonObject1.getString("user_image"));
                            }

                            String admin_approval = jsonObject1.getString("admin_approval");
                            if (admin_approval.equalsIgnoreCase("0")) {
                                Toast.makeText(context, "After admin approval, your business details will be displayed to the users", Toast.LENGTH_LONG).show();
                            } else {
                            }

                            AppPreferencesBuss.setSaveIdPass(context, email, password);
                            Intent intent1 = new Intent(context, BusinessNaviDrawer.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                        } else if (jsonObject.getString("status").equalsIgnoreCase("300")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            Log.e(TAG, "onResponse: " + jsonArray);
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            AppPreferencesBuss.setfirstname(context, jsonObject1.getString("emp_name"));
                            AppPreferencesBuss.setBussiId(context, jsonObject1.getString("emp_business"));
                            AppPreferencesBuss.setAllottedDuties(context, jsonObject1.getString("emp_duties"));
                            AppPreferencesBuss.setBussiPackagelist(context, jsonObject1.getString("package_id"));
                            AppPreferencesBuss.setBussiOptionlist(context, jsonObject1.getString("package_id"));
                            AppPreferencesBuss.setUserId(context, jsonObject1.getString("user_id"));
                            AppPreferencesBuss.setBussiEmilid(context, jsonObject1.getString("emp_email"));
                            AppPreferencesBuss.setBussiPhoneNo(context, jsonObject1.getString("emp_contact"));
                            AppPreferencesBuss.setBussiFein(context, jsonObject1.getString("emp_dine_app_id"));
                            AppPreferences.setUserType(context, AppConstants.BUSS_LOGIN_TYPE.BUSSINESS_LOCAL_USER);
                            AppPreferencesBuss.setEmpPosition(context, jsonObject1.getString("emp_position_name"));
                            AppPreferencesBuss.setEmpPositionId(context, jsonObject1.getString("emp_position"));
                            AppPreferencesBuss.setUsertypeid(context, jsonObject1.getString("user_type"));
                            AppPreferencesBuss.setSaveIdPass(context, email, password);
                            Intent intent1 = new Intent(context, BusinessNaviDrawer.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject12 = jsonArray.getJSONObject(0);
                            Log.d("onResponse", jsonObject12.getString("msg"));
                            Toast.makeText(context, jsonObject12.getString("msg"), Toast.LENGTH_SHORT).show();
                        } /*else if (jsonObject.getString("status").equalsIgnoreCase("700")) {
                        AppPreferences.setUserType(context, AppConstants.BUSS_LOGIN_TYPE.VENDOR_USER);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        AppPreferencesBuss.setfirstname(context, jsonObject1.getString("first_name") + " " + jsonObject1.getString("last_name"));
                        AppPreferencesBuss.setUsertypeid(context, jsonObject1.getString("user_type"));
                        AppPreferencesBuss.setVendorUrl(context, jsonObject1.getString("VENDOR_ADMIN_Url"));
                        AppPreferencesBuss.setUserId(context, jsonObject1.getString("user_id"));
                        Intent intent = new Intent(getApplicationContext(), VendorLoginWebViewActivity.class);
                        intent.putExtra("vendor_url", jsonObject1.getString("VENDOR_ADMIN_Url"));
                        startActivity(intent);
                    } */ else {
                            Intent intent = new Intent(context, BusinessLoginError.class);
                            context.startActivity(intent);
                        }
                    } catch (JSONException e) {
                        progressHD.dismiss();
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, " Error :- " + Log.getStackTraceString(t));
                    progressHD.dismiss();
                }
            });

        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void disableBusiness(String id, final int position) {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.DISABLE_MY_BUSSINESS);
            jsonObject.addProperty("other_business_id", id);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
            Log.e(TAG, "disableBusiness: Request >> " + jsonObject.toString());
            final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "disableBusiness: Response >> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            removeAt(position);
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject12 = jsonArray.getJSONObject(0);
                            Log.d("onResponse", jsonObject12.getString("msg"));
                            Toast.makeText(context, jsonObject12.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressHD.dismiss();
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, " Error :- " + Log.getStackTraceString(t));
                    progressHD.dismiss();
                }
            });

        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableBusiness(String id, final int position) {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ENABLE_MY_BUSSINESS);
            jsonObject.addProperty("other_business_id", id);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
            Log.e(TAG, "disableBusiness: Request >> " + jsonObject.toString());
            final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "disableBusiness: Response >> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            removeAt(position);
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject12 = jsonArray.getJSONObject(0);
                            Log.d("onResponse", jsonObject12.getString("msg"));
                            Toast.makeText(context, jsonObject12.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressHD.dismiss();
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, " Error :- " + Log.getStackTraceString(t));
                    progressHD.dismiss();
                }
            });

        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeAt(int position) {
        reservList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, reservList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvBussName, tvBussAddr, tvEmail, tvContactNo, tvLogin, tvRemove;
        ImageView restLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBussName = (CustomTextView) itemView.findViewById(R.id.tvBussName);
            tvBussAddr = (CustomTextView) itemView.findViewById(R.id.tvBussAddr);
            tvEmail = (CustomTextView) itemView.findViewById(R.id.tvEmail);
            tvContactNo = (CustomTextView) itemView.findViewById(R.id.tvContactNo);
            tvLogin = (CustomTextView) itemView.findViewById(R.id.tvLogin);
            tvRemove = (CustomTextView) itemView.findViewById(R.id.tvRemove);
            restLogo = (ImageView) itemView.findViewById(R.id.restLogo);
        }
    }
}