package com.yberry.dinehawaii.Bussiness.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.UploadMenuList;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.activity.FoodTypeUploadMenu;
import com.yberry.dinehawaii.activity.SelectFoodTypeActivity;
import com.yberry.dinehawaii.activity.ViewMenuActivity;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 03-02-17.
 */

public class SuppliesVendorFragment extends BaseFragment {
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_ALBUM = 2;
    String imageString;
    Button btnUploadMenu, btnViewMenu , btnModifyMenu;
    CustomTextView spinSelectFoodType;
    RadioGroup deliveryService;
    ArrayList<CheckBoxPositionModel> listFoodService;
    String food_type_id = "";
    RadioButton yesRadioButton, noRadioButton;
    CustomEditText tvVndorName;
    private int where = 5;
    private Uri file_uri;
    private String tempPath;
    private File file;
    private Button btnBussinessPhoto, btnLogo, btnPhoto;
    private String TAG = "SUPPLIESVENDORFRAGMENT";

    View.OnClickListener btnLogoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openDialogToChosePic();
            where = 7;

        }
    };

    View.OnClickListener btnPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openDialogToChosePic();
            where = 1;

        }
    };

    private Context mContext;
    private ImageView imageView;
    private String deliveryType = "1";

    public SuppliesVendorFragment() {
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
        View view = inflater.inflate(R.layout.activity_supplies_vendor, container, false);
//        headText.setText("SUPPLIER-VENDOR");
        if (intraction != null) {
            intraction.actionbarsetTitle("Supplier-Vendor");
        }
        mContext = getActivity();
        init(view);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }
        }

        listner();
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
        listFoodService = new ArrayList<CheckBoxPositionModel>();
        imageView = (ImageView) view.findViewById(R.id.imageview);
        btnLogo = (Button) view.findViewById(R.id.btnLogo);
        btnBussinessPhoto = (Button) view.findViewById(R.id.btnBussinessPhoto);
        btnUploadMenu = (Button) view.findViewById(R.id.btnUploadMenu);
        btnModifyMenu = (Button) view.findViewById(R.id.btnModifyMenu);
        spinSelectFoodType = (CustomTextView) view.findViewById(R.id.spinSelectFoodType);
        deliveryService = (RadioGroup) view.findViewById(R.id.radioGroup);
        yesRadioButton = (RadioButton) view.findViewById(R.id.yesRadioButton);
        noRadioButton = (RadioButton) view.findViewById(R.id.noRadioButton);
        tvVndorName = (CustomEditText) view.findViewById(R.id.tvVndorName);
        btnViewMenu = (Button) view.findViewById(R.id.btnViewMenu);
    }

    private void listner() {
        btnLogo.setOnClickListener(btnLogoListener);
        btnBussinessPhoto.setOnClickListener(btnPhotoListener);

        deliveryService.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (checkedId == R.id.yesRadioButton) {
                    deliveryType = "1";
                } else if (checkedId == R.id.noRadioButton) {
                    deliveryType = "0";
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new DeliveryServiceFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);



                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        spinSelectFoodType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(getActivity())) {

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("method", AppConstants.GENERALAPI.FOODTYPE);
                    jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                    foodServicesData(jsonObject);
                } else {
                    showToast("Please check internet connection!");
                }
            }
        });

        btnUploadMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listFoodService.isEmpty()) {
                    showToast("please select food services");
                } else if (TextUtils.isEmpty(tvVndorName.getText().toString())) {
                    showToast("please enter your vender name");
                } else {
                    Intent intent = new Intent(getActivity(), UploadMenuList.class);
                    intent.putParcelableArrayListExtra("listFoodService", listFoodService);
                    intent.setAction("SuppliesVendorFragment46");
                    intent.putExtra("deliveryType", deliveryType);
                    intent.putExtra("vendor_name", tvVndorName.getText().toString());
                    intent.putExtra("food_type_id", food_type_id);
                    Log.d("JsonObject", String.valueOf(listFoodService));
                    startActivity(intent);
                }
            }
        });

        btnViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewMenuActivity.class);
                intent.setAction("SuppliesVendorFragment46");
                intent.putExtra("food_type_id", food_type_id);
                startActivity(intent);
            }
        });

        btnModifyMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, FoodTypeUploadMenu.class);
                intent.setAction("Fragment46_Modify_Menu");
                intent.putExtra("deliveryType", deliveryType);
                intent.putExtra("vendor_name", tvVndorName.getText().toString());
                intent.putExtra("food_type_id", food_type_id);
                startActivity(intent);
            }
        });

    }

    private void foodServicesData(JsonObject jsonObject) {
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
                Log.e("onResponse", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(getActivity(), SelectFoodTypeActivity.class);
                        intent.setAction("SuppliesVendorFragment");
                        intent.putExtra("Foodtype", s);
                        startActivityForResult(intent, 12);
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
                progressHD.dismiss();
            }
        });
    }

    /**************************
     * camera Code
     ************************************/

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

    /***************************************
     * API Hit
     ***********************/
    private void hitApiBussiness() {
        if (where == 7) {
            bussinessLogo(imageString);
            Log.d("adytfWYE", imageString);
        } else if (where == 1) {
            bussinessPhoto(imageString);

        }
    }

    /********************************************PHOTO*********************************************/
    private void bussinessPhoto(String imageString) {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_FOOD_VENDOR_API.UPLOADDISTRIPHOTO);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("food_type_id", food_type_id);
            jsonObject.addProperty("photo", imageString.trim());
            Log.d("iamgeStringResponce", imageString);
            Log.e("response", jsonObject.toString());
            uploadBusinessPhoto(jsonObject);
        } else {
            showToast("Please check internet connection!");
        }
    }

    private void uploadBusinessPhoto(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_food_vendor_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("onResponseBusiness", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("ResBusinessPhoto:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String message = jsonObject1.getString("msg");
                        Log.d("MESSAGE:", message);

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
                progressHD.dismiss();
            }
        });
    }

    /*********************************************LOGO************************************/
    private void bussinessLogo(String imageString) {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSINESS_FOOD_VENDOR_API.UPLOADDISTRILOGO);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));// AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("business_id",  AppPreferencesBuss.getBussiId(getActivity()));//, AppPreferencesBuss.getBussiId(getActivity()));
            jsonObject.addProperty("food_type_id", food_type_id);//AppPreferencesBuss.getUserId(getApplicationContext()));
            jsonObject.addProperty("business_logo", imageString.trim());//AppPreferencesBuss.getUserId(getApplicationContext()));
            Log.d("iamgeStringResponce", imageString);
            Log.e("response", jsonObject.toString());
            BussinessLogoUpload(jsonObject);
        }
    }

    private void BussinessLogoUpload(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_food_vendor_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();

                Log.e(TAG, "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONObject jsonArray = jsonObject.getJSONObject("result");
                        //  JSONObject jsonObject1=jsonArray.getJSONObject("msg");
                        String message = jsonArray.getString("msg");
                        Log.d("message45", message);
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
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }


    /***********************************OnActivityResult*************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 12) {

                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("foodtype");
//                Log.d("FoodServiceIntentValue", checkBoxPositionModel.getName());
                spinSelectFoodType.setText(checkBoxPositionModel.getName());
                food_type_id = checkBoxPositionModel.getId();
                Log.v(TAG, "Food id type :- " + food_type_id);
                listFoodService.add(checkBoxPositionModel);
                Log.d("StringArrayList", String.valueOf(listFoodService));

            } else if (requestCode == REQUEST_CODE_CAMERA) {
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

}
