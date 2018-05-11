package com.yberry.dinehawaii.Bussiness.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.SelectServiceTypeActivity;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.Bussiness.Activity.FoodTypeActivity;
import com.yberry.dinehawaii.activity.FoodTypeUploadMenu;
import com.yberry.dinehawaii.activity.ViewMenuActivity;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FoodServiceFragment45 extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_ALBUM = 2;
    private static final String TAG = "FOODSERVICE";
    public CustomTextView tvSelectService;
    JsonObject jsonObjectOther;
    RadioGroup radioGroup;
    ArrayList<CheckBoxPositionModel> listFoodService;
    String listValueNew, imageString;
    String food_catagory_id_list;
    String valueRadio = "1";
    String gatePassFileName;
    String service_id = "";
    ImageView help_menuImageView, help_modifyMenuImageView, help_uploadMenuImageView, help_viewMenuImageView, help_photoImageView, help_logoMenuImageView, help_busPhotoMenuImageView;
    LinearLayout llPhotoLinearLayout;
    View view1;
    FragmentIntraction intraction;
    private int where = 5;
    private Uri file_uri;
    private String tempPath;
    private File file;
    private ImageView next;
    private Context mContext;
    private CustomButton btnUploadMenu, btnBussinessPhoto, btnLogo, btnPhoto, btnModify, btnDownload, btnViewMenu;
    private CustomTextView text_food;

    public FoodServiceFragment45() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.activity_food_service, container, false);
        ((ScrollView) view1.findViewById(R.id.scroll_fragment45)).setVisibility(View.VISIBLE);
        ((FrameLayout) view1.findViewById(R.id.container_fragemnt45)).setVisibility(View.GONE);

//        headText.setText("FOOD SERVICE");
        if (intraction != null) {
            intraction.actionbarsetTitle("Food Service");
        }
        jsonObjectOther = new JsonObject();
        mContext = getActivity();
        initComponent(view1);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }
        }

        if (!AppPreferencesBuss.getBussiOptionlist(getActivity()).equalsIgnoreCase("")) {
            String optionlist = AppPreferencesBuss.getBussiOptionlist(getActivity());
            if (optionlist.contains("3")) {
                llPhotoLinearLayout.setVisibility(View.VISIBLE);
            }
        }

        jsonObjectOther.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObjectOther.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        return view1;
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

    private void initComponent(final View view) {
        llPhotoLinearLayout = (LinearLayout) view.findViewById(R.id.llPhoto);
        text_food = (CustomTextView) view.findViewById(R.id.spinSelectFoodType);
        text_food.setOnClickListener(this);
        tvSelectService = ((CustomTextView) view.findViewById(R.id.tvSelectService));
        next = (ImageView) view.findViewById(R.id.imageview);
        radioGroup = ((RadioGroup) view.findViewById(R.id.radioGroup));
        btnUploadMenu = (CustomButton) view.findViewById(R.id.btnUploadMenu);
        btnBussinessPhoto = (CustomButton) view.findViewById(R.id.btnBussinessPhoto);
        btnLogo = (CustomButton) view.findViewById(R.id.btnLogo);
        btnPhoto = (CustomButton) view.findViewById(R.id.btnPhoto);
        btnModify = (CustomButton) view.findViewById(R.id.btnModify);
        btnDownload = (CustomButton) view.findViewById(R.id.btnDownload);
        btnViewMenu = (CustomButton) view.findViewById(R.id.btnViewMenu);
        next.setOnClickListener(this);
        btnUploadMenu.setOnClickListener(this);
        tvSelectService.setOnClickListener(this);
        btnBussinessPhoto.setOnClickListener(this);
        btnLogo.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        btnViewMenu.setOnClickListener(this);
        help_menuImageView = (ImageView) view.findViewById(R.id.help_menuImageView);
        help_modifyMenuImageView = (ImageView) view.findViewById(R.id.help_modifyMenuImageView);
        help_uploadMenuImageView = (ImageView) view.findViewById(R.id.help_uploadMenuImageView);
        help_viewMenuImageView = (ImageView) view.findViewById(R.id.help_viewMenuImageView);
        help_photoImageView = (ImageView) view.findViewById(R.id.help_photoImageView);
        help_logoMenuImageView = (ImageView) view.findViewById(R.id.help_logoMenuImageView);
        help_busPhotoMenuImageView = (ImageView) view.findViewById(R.id.help_busPhotoMenuImageView);

        help_modifyMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Function.bottomToolTipDialogBox(null, getActivity(), "sold-out, single changes, and new or revised photos", null, view);
            }
        });

        help_uploadMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Function.bottomToolTipDialogBox(null, getActivity(), "New or revise", null, view);
            }
        });

        help_busPhotoMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Function.bottomToolTipDialogBox(null, getActivity(), "Will load to Home page, qty: 1", null, view);
            }
        });


        help_logoMenuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Function.bottomToolTipDialogBox(null, getActivity(), "Will load to Home page, qty: 1", null, view);
            }
        });

        help_photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Function.bottomToolTipDialogBox(null, getActivity(), "Will load to Home page, length: 30 seconds", null, view);
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton) view.findViewById(id);
                //jsonObjectOther = new JsonObject();
                Log.d("radio", button.getText().toString());
                if (button.getText().toString().equalsIgnoreCase("Yes")) {
                    valueRadio = "1";
                } else if (button.getText().toString().equalsIgnoreCase("No")) {
                    valueRadio = "0";
                }
            }
        });

        jsonObjectOther.addProperty("delivery_status", valueRadio);
        String packageid = "";
        if (AppPreferencesBuss.getBussiPackagelist(getActivity()) != null) {
            packageid = AppPreferencesBuss.getBussiPackagelist(getActivity());
            Log.d(TAG, "Business Packageid :- " + packageid);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvSelectService) {
            if (Util.isNetworkAvailable(mContext)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLSERVICES);
                jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));//AppPreferencesBuss.getUserId(getApplicationContext()));
                Log.e(TAG, "Request GET ALL SERVICES >> " + jsonObject.toString());
                JsonCallMethod(jsonObject);
            } else {
                Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.spinSelectFoodType) {
            if (Util.isNetworkAvailable(mContext)) {
                text_food.setText("");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.GENERALAPI.FOODTYPE);
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));// AppPreferencesBuss.getUserId(context));
                Log.d(TAG, "Request GET ALL FOODTYPE >> " + jsonObject);
                foodServicesData(jsonObject);
            } else {
                Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            }
        } else if (view.getId() == R.id.btnUploadMenu) {

            if (TextUtils.isEmpty(tvSelectService.getText())) {
                Toast.makeText(getActivity(), "Please select service type", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(text_food.getText())) {
                Toast.makeText(getActivity(), "Please select food type", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(mContext, FoodTypeUploadMenu.class);
                intent.setAction("Upload_Menu");
                intent.putExtra("food_type_id", food_catagory_id_list);
                intent.putParcelableArrayListExtra("foodtypeuploadmenu", listFoodService);
                intent.putExtra("other", new Gson().toJson(jsonObjectOther));
                Log.d("JsonObject", String.valueOf(listFoodService));
                startActivity(intent);
            }
        } else if (view.getId() == R.id.btnModify) {
            if (TextUtils.isEmpty(tvSelectService.getText())) {
                Toast.makeText(getActivity(), "Please Select Service Type..!", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(text_food.getText())) {
                Toast.makeText(getActivity(), "Please Select Food Type..!", Toast.LENGTH_SHORT).show();
            } else {
                modifyMenu();
            }
        } else if (view.getId() == R.id.btnDownload) {
            getAllFoodCategories_Modify();
        } else if (view.getId() == R.id.btnViewMenu) {
            Intent intent = new Intent(mContext, ViewMenuActivity.class);
            intent.setAction("FoodServiceFragment45");
            intent.putExtra("food_type_id", food_catagory_id_list);
            intent.putExtra("other", new Gson().toJson(jsonObjectOther));
            Log.d("JsonObject", String.valueOf(listFoodService));
            startActivity(intent);
        } else if (view.getId() == R.id.imageview) {
            Fragment fragment = new FoodDistributorFragment45B();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();

          /*  String packageid = AppPreferencesBuss.getBussiPackagelist(getActivity());
            Log.d("packageid", packageid);
            Fragment fragment = null;

            if (packageid.contains("2")) {
                fragment = new FoodDistributorFragment45B();
            } else if (packageid.contains("1")) {
                fragment = new BusinessHomeFragment41();
            } else if (packageid.equalsIgnoreCase("4")) {
                fragment = new FoodDistributorFragment45B();
            }

            *//*else if(packageid.contains("2")){
                fragment = new FoodDistributorFragment45B();
            }else if(packageid.contains("3")){
                fragment = new FoodDistributorFragment45B();
            }*//*

            if (fragment != null) {
                ((ScrollView)view1.findViewById(R.id.scroll_fragment45)).setVisibility(View.GONE);
                ((FrameLayout)view1.findViewById(R.id.container_fragemnt45)).setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);


                fragmentTransaction.replace(R.id.container_fragemnt45, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }*/

        } else if (view.getId() == R.id.btnBussinessPhoto) {
            openDialogToChosePic();
            where = 6;
        } else if (view.getId() == R.id.btnLogo) {
            openDialogToChosePic();
            where = 7;
        } else if (view.getId() == R.id.btnPhoto) {
            openDialogToChosePic();
            where = 8;
        }
    }

    private void modifyMenu() {
        Log.e(TAG, "modifyMenu: jsonObjectOther >> " + jsonObjectOther.toString());
        Log.e(TAG, "modifyMenu: listFoodService >> " + listFoodService.toString());
        Log.e(TAG, "modifyMenu: food_catagory_id_list >> " + food_catagory_id_list.toString());

        Intent intent = new Intent(mContext, FoodTypeUploadMenu.class);
        intent.setAction("Fragment45_Modify_Menu");
        intent.putExtra("food_type_id", food_catagory_id_list);
        intent.putParcelableArrayListExtra("foodtypeuploadmenu", listFoodService);
        intent.putExtra("other", new Gson().toJson(jsonObjectOther));
        startActivity(intent);
    }

    private void foodServicesData(JsonObject jsonObject) {

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
                Log.e(TAG, "Response GET ALL FOODTYPE >> " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(mContext, FoodTypeActivity.class);
                        intent.setAction("FoodFragment45");
                        intent.putExtra("Foodtype", s);
                        startActivityForResult(intent, 12);
                        //startActivity(intent);
                    }
                    // adapter.notifyDataSetChanged();
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

    private void JsonCallMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                Log.e(TAG, "Response GET ALL SERVICES >> " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(mContext, SelectServiceTypeActivity.class);
                        intent.putExtra("ServerType", s);
                        startActivityForResult(intent, 101);
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
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("serviceFoodType");
                Log.e(TAG, "onActivityResult: checkBoxPositionModel >> " + checkBoxPositionModel.toString());
                tvSelectService.setText(checkBoxPositionModel.getName());
                jsonObjectOther.addProperty("service_id", checkBoxPositionModel.getId());
                //jsonObject.put("serviceName", checkBoxPositionModel.getName());
                service_id = checkBoxPositionModel.getId();
            } else if (requestCode == 12) {
                listFoodService = data.getParcelableArrayListExtra("foodtype");
                Log.e(TAG, "onActivityResult: listFoodService >> " + listFoodService.toString());
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");
                Log.v(TAG, " " + listValueNew);

                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(text_food.getText().toString())) {
                        text_food.setText(listValue.getName());
                        food_catagory_id_list = listValue.getId();
                    } else {
                        text_food.setText(text_food.getText().toString() + "," + listValue.getName());
                        food_catagory_id_list = food_catagory_id_list + "," + listValue.getId();
                    }
                }
                Log.v(TAG, "Alll ~~~~~~~~ food_type_id :- " + food_catagory_id_list);

            } else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
                Log.d("ResultOk", "ResultOk");
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(tempPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageString = Util.encodeTobase64(bitmap);
                hitApiBussiness();
                Log.d("bitmapCamera", String.valueOf(bitmap));
                Log.d("iamgeString", imageString);

            } else if (requestCode == REQUEST_CODE_ALBUM) {
                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    imageString = Util.encodeTobase64(bitmap);
                    hitApiBussiness();

                    Log.d("iamgeStringGallary", imageString);

                    // Log.d(TAG, String.valueOf(bitmap));
                    Log.d("bitmapGallary", String.valueOf(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void hitApiBussiness() {
        if (where == 6) {
            bussinessPhoto(imageString);
        } else if (where == 7) {
            bussinessLogo(imageString);
        } else if (where == 8) {
            bussinessPhotoOnly(imageString);
        }
    }

    private void bussinessPhotoOnly(String imageString) {
        if (Util.isNetworkAvailable(getActivity())) {//UPLOADSERVICELOGO
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.UPLOADSERVICEPHOTO);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("service_id", jsonObjectOther.get("service_id").getAsString());//AppPreferencesBuss.getUserId(getApplicationContext()));
            jsonObject.addProperty("photo", imageString.trim());//AppPreferencesBuss.getUserId(getApplicationContext()));
            Log.e("response", jsonObject.toString());
            //        JsonCallMethod(jsonObject);
            JsonCallMethodsApiPhoto(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void JsonCallMethodsApiPhoto(JsonObject jsonObject) {
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
                Log.e("Respnse" + "onResponseFoodType", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonobject = jsonObject.getJSONObject("result");
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

    private void bussinessLogo(String imageString) {
        if (Util.isNetworkAvailable(getActivity())) {//UPLOADSERVICELOGO
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.UPLOADSERVICELOGO);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("service_id", jsonObjectOther.get("service_id").getAsString());//AppPreferencesBuss.getUserId(getApplicationContext()));
            jsonObject.addProperty("business_logo", imageString.trim());//AppPreferencesBuss.getUserId(getApplicationContext()));
            Log.e("response", jsonObject.toString());
            JsonCallMethodsLogo(jsonObject);
        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    private void bussinessPhoto(String imageString) {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.UPLOADBUSSPHOTO);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("service_id", jsonObjectOther.get("service_id").getAsString());//AppPreferencesBuss.getUserId(getApplicationContext()));
            jsonObject.addProperty("buss_photo", imageString.trim());//AppPreferencesBuss.getUserId(getApplicationContext()));
            Log.d("iamgeStringResponce", imageString);
            Log.e("response", jsonObject.toString());
            JsonCallMethods(jsonObject);
        }
    }

    private void JsonCallMethods(JsonObject jsonObject) {
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
                Log.e("Respnse" + "onResponseFoodType", response.body().toString());
                String s = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonArray = jsonObject.getJSONObject("result");
                        //  JSONObject jsonObject1=jsonArray.getJSONObject("msg");
                        String jshg = jsonArray.getString("msg");
                        Log.d("DSgshf", jshg);
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
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void JsonCallMethodsLogo(JsonObject jsonObject) {
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
                Log.e("Respnse" + "onResponseFoodType", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonArray = jsonObject.getJSONObject("result");
                        String jshg = jsonArray.getString("msg");
                        Log.d("Logo", jshg);
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

    /*************************
     * camera
     ********************/

    private String openDialogToChosePic() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Take picture using");

        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSelectCamera();
            }
        });
        builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSelectAlbum();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return null;
    }

    private void onSelectAlbum() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_ALBUM);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, REQUEST_CODE_ALBUM);
        }
    }

    private void onSelectCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            getFileUri();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void getFileUri() throws IOException {
        //String image_name = vNameString + vDateString + ".jpg";
        String image_name = "img" + System.currentTimeMillis();
        File image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file = File.createTempFile(

                image_name,  // prefix
                ".jpeg",         // suffix
                image      // directory
        );
        tempPath = "file:" + file.getAbsolutePath();
        file_uri = Uri.fromFile(file);
        Log.e(TAG, " 2. Inside Method getFileUri ");
    }

    public boolean onBackPressed() {
        Log.d("backpessed", "backpessed");
        return false;
    }

    private void getAllFoodCategories_Modify() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.ALL_FOOD_MODIFY_CATEGORIES);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));// AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("food_category_id", food_catagory_id_list);
        getAllFoodCategoriesTask_Modify(jsonObject);
        Log.d(TAG, "allFoodCategories Json resquest :- " + jsonObject);
    }

    @SuppressLint("LongLogTag")
    private void getAllFoodCategoriesTask_Modify(JsonObject jsonObject) {

        listFoodService = new ArrayList<>();
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
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "allFoodCategories Json Response :- " + response.body().toString());
                String s = response.body().toString();
                CheckBoxPositionModel model;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            model = new CheckBoxPositionModel();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String food_cat_id = jsonObject1.getString("food_cat_id");
                            String dish_name = jsonObject1.getString("dish_name");
                            String food_name = jsonObject1.getString("food_name");
                            String food_type_id = jsonObject1.getString("food_type_id");
                            String amount = jsonObject1.getString("price");

                            model.setFood_ctagory_id(food_cat_id);
                            model.setDish_name(dish_name);
                            model.setFood_name(food_name);
                            model.setId(food_type_id);
                            model.setAmount(amount);
                            listFoodService.add(model);
                        }

                        model = new CheckBoxPositionModel();

                        model.setFood_ctagory_id("101");
                        model.setDish_name("test dish name");
                        model.setFood_name("test food name");
                        model.setId("test id");
                        model.setAmount("250");
                        listFoodService.add(model);
                        listFoodService.add(model);

//                        createGateListPdf(listFoodService);
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONObject object = jsonObject.getJSONObject("result");
                        String message = object.getString("msg");
                        // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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

//    public void createGateListPdf(ArrayList<CheckBoxPositionModel> arrayLisrMenu) {
//
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat sTimeF = new SimpleDateFormat("EEE_MM_dd");
//        String todaysDate = sTimeF.format(calendar.getTime());
//        Log.e("Current Data : ", "" + todaysDate);
//        doc = new Document();
//
//        try {
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Dir";
//
//            File dir = new File(path);
//            if (!dir.exists())
//                dir.mkdirs();
//
//            Log.d("PDFCreator", "PDF Path: " + path);
//
//            gatePassFileName = "Food_Menu_List" + todaysDate + ".pdf";
//            Log.e(TAG, "Menu FILE NAME : " + gatePassFileName);
//
//            File file = new File(dir, gatePassFileName);
//            FileOutputStream fOut = new FileOutputStream(file);
//
//            PdfWriter.getInstance(doc, fOut);
//
//            //open the document
//            doc.open();
//
//            doc.addTitle("Menu List");
//            /* Create Paragraph and Set Font */
//            Paragraph p1 = new Paragraph("******************* Price Menu *******************");
//            /* Create Set Font and its Size */
//            Font paraFont = new Font(Font.FontFamily.HELVETICA);
//            paraFont.setSize(20);
//            p1.setAlignment(Paragraph.ALIGN_CENTER);
//            p1.setFont(paraFont);
//            p1.add("\n");
//            p1.add("\n");
//            p1.add("\n");
//            p1.add("\n");
//
//            //add paragraph to document
//            doc.add(p1);
//
//            // create a table
//
//            PdfPTable table = new PdfPTable(4);
//            table.setWidthPercentage(100);
//            table.getDefaultCell().setUseAscender(true);
//            table.getDefaultCell().setUseDescender(true);
//            PdfPCell cell = new PdfPCell();
//
//            table.addCell("S_No.");
//            table.addCell("FOOD Name");
//            table.addCell("DISH Name");
//            table.addCell("Price ");
//            table.setSpacingAfter(4);
//            table.setSpacingBefore(4);
//
//            SimpleDateFormat sTimeFDb = new SimpleDateFormat("dd-MM-yyyy");
//            String tDate_EntryFrom = sTimeFDb.format(calendar.getTime());
//            //List<GatePassModel> gatePassList = databaseHandler.getAllGatePass(tDate_EntryFrom);
//
////            List<GatePassModel> gatePassList = databaseHandler.getAllGatePass(tDate_EntryFrom);
//            int sNoCount = 0;
//            try {
//                for (CheckBoxPositionModel gatePassModel : arrayLisrMenu) {
////                    String printVisitorDetails = "Visitor Name: " + gatePassModel.getName() + ", Image: " + gatePassModel.getVisitorImage() + " ,Color: " + gatePassModel.getMobNo();
//                    sNoCount++;
//                    table.addCell("" + sNoCount);
//                    table.addCell(gatePassModel.getFood_name());
//                    table.addCell(gatePassModel.getDish_name());
//                    table.addCell("$ " + gatePassModel.getAmount());
//                }
//                doc.add(table);
//                doc.close();
//                //  Toast.makeText(getApplicationContext(), "Created...", Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                Log.e(TAG, "Exception in generating PDF" + e);
//            }
//
//
//        } catch (DocumentException de) {
//            Log.e("PDFCreator", "DocumentException:" + de);
//        } catch (IOException e) {
//            Log.e("PDFCreator", "ioException:" + e);
//        } finally {
//            doc.close();
//        }
//        viewPdf(gatePassFileName, "Dir");
//    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        Uri path = Uri.fromFile(pdfFile);
        Log.d("path420", path.toString());

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
