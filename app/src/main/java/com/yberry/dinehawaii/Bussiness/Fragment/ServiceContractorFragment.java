package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.Bussiness.Activity.SelectServiceTypeActivity;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 03-02-17.
 */

public class ServiceContractorFragment extends Fragment {
    private static final String TAG = "ServiceContFrag_48";
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_ALBUM = 2;
    private static boolean UPLOAD_PHOTO = false;
    private static boolean MODIFY_PHOTO = false;
    private static boolean BUSINEESS_PHOTO = false;
    private static boolean BUSINEESS_LOGO = false;
    private static boolean BUSINEESS_VIDEO = false;
    Button btnUpload, btnDownload, btnViewMenu, btnModifyMenu, btnBussinessPhoto, btnLogo, btnVideo;
    String service_id;
    JsonObject jsonObject;
    private ImageView imageView, help_imageView_bussPhoto, help_imageView_bussLogo, help_imageView_bussvideo, help_uploadRateSchedule, help_downloadRateSchedule, help_viewRate, help_modifyRate;
    private CustomTextView spinSelectService;
    private String tempPath;
    private File file;
    private Uri file_uri;
    private String EncodedImageString;

    public ServiceContractorFragment() {
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
        View view = inflater.inflate(R.layout.activity_service_contractor, container, false);
//        headText.setText("SERVICE-CONTRACTOR");
        if (intraction != null) {
            intraction.actionbarsetTitle("Service-Contractor");
        }
        jsonObject = new JsonObject();

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

        spinSelectService = (CustomTextView) view.findViewById(R.id.spinSelectService);

        btnUpload = (Button) view.findViewById(R.id.btnUpload);
        btnDownload = (Button) view.findViewById(R.id.btnDownload);
        btnViewMenu = (Button) view.findViewById(R.id.btnViewMenu);
        btnModifyMenu = (Button) view.findViewById(R.id.btnModifyMenu);
        btnBussinessPhoto = (Button) view.findViewById(R.id.btnBussinessPhoto);
        btnLogo = (Button) view.findViewById(R.id.btnLogo);
        btnVideo = (Button) view.findViewById(R.id.btnVideo);

        imageView = (ImageView) view.findViewById(R.id.imageview);
        help_imageView_bussPhoto = (ImageView) view.findViewById(R.id.help_imageView_bussPhoto);
        help_imageView_bussLogo = (ImageView) view.findViewById(R.id.help_imageView_bussLogo);
        help_imageView_bussvideo = (ImageView) view.findViewById(R.id.help_imageView_bussvideo);

        help_uploadRateSchedule = (ImageView) view.findViewById(R.id.help_uploadRateSchedule);
        help_downloadRateSchedule = (ImageView) view.findViewById(R.id.help_downloadRateSchedule);
        help_viewRate = (ImageView) view.findViewById(R.id.help_viewRate);
        help_modifyRate = (ImageView) view.findViewById(R.id.help_modifyRate);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PackageAndMarketingFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });


        help_imageView_bussPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        spinSelectService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.isNetworkAvailable(getActivity())) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLSERVICES);
                    jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                    jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));//AppPreferencesBuss.getUserId(getApplicationContext()));
                    Log.d(TAG, "Userid :- " + AppPreferencesBuss.getUserId(getActivity()));
                    Log.e(TAG, jsonObject.toString());
                    JsonCallMethod(jsonObject);

                } else {
                    Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();
                }

            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UPLOAD_PHOTO = true;
                openDialogToChosePic();
                Log.v(TAG, " Encoded Image in onclick listerner Upload button :- " + EncodedImageString);
            }
        });

        btnModifyMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MODIFY_PHOTO = true;
                openDialogToChosePic();
                Log.v(TAG, " Encoded Imgage in onclick listerner Modify button :- " + EncodedImageString);

            }
        });

        btnBussinessPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUSINEESS_PHOTO = true;
                openDialogToChosePic();
                Log.v(TAG, " Encoded Imgage in onclick listerner Business Photo button :- " + EncodedImageString);
            }
        });

        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUSINEESS_LOGO = true;
                openDialogToChosePic();
                Log.v(TAG, " Encoded Imgage in onclick listerner Business Logo button :- " + EncodedImageString);

            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        help_imageView_bussLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        help_imageView_bussvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        help_uploadRateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        help_downloadRateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        help_viewRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        help_modifyRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

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

    private void JsonCallMethod(JsonObject jsonObject) {
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
                Log.e(TAG + "onResponseFoodType", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(getActivity(), SelectServiceTypeActivity.class);
                        intent.putExtra("ServerType", s);
                        startActivityForResult(intent, 101);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 101) {
                Log.e(TAG, "ServiceType :- " + "OnActivtyResultFoodType");
                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("serviceFoodType");
                Log.d(TAG, "Service name :- " + checkBoxPositionModel.getName());
                spinSelectService.setText(checkBoxPositionModel.getName());
                jsonObject.addProperty("service_id", checkBoxPositionModel.getId());
                service_id = checkBoxPositionModel.getId();
                Log.v(TAG, "Selected  Service id :- " + service_id);

            }
            if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {

                Log.d(TAG, "ResultOk");
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(tempPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (UPLOAD_PHOTO == true) {

                    EncodedImageString = Util.encodeTobase64(bitmap);
                    Log.d(TAG, "bitmapCamera :- " + String.valueOf(bitmap));
                    Log.d(TAG, "Image String UPLOAD_PHOTO :-" + EncodedImageString);
                    UPLOAD_PHOTO = false;

                } else if (MODIFY_PHOTO == true) {

                    EncodedImageString = Util.encodeTobase64(bitmap);
                    Log.d(TAG, "Image String MODIFY_PHOTO :- " + EncodedImageString);
                    MODIFY_PHOTO = false;

                } else if (BUSINEESS_PHOTO == true) {

                    EncodedImageString = Util.encodeTobase64(bitmap);
                    Log.d(TAG, "Image String BUSINEESS_PHOTO :- " + EncodedImageString);
                    BUSINEESS_PHOTO = false;

                } else if (BUSINEESS_LOGO == true) {

                    EncodedImageString = Util.encodeTobase64(bitmap);
                    Log.d(TAG, "Image String BUSINEESS_LOGO :- " + EncodedImageString);
                    BUSINEESS_LOGO = false;
                }

            } else if (requestCode == REQUEST_CODE_ALBUM) {
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (UPLOAD_PHOTO == true) {

                    EncodedImageString = Util.encodeTobase64(bitmap);
                    Log.d(TAG, "iamgeStringGallary :- " + EncodedImageString);
                    UPLOAD_PHOTO = false;

                } else if (MODIFY_PHOTO == true) {

                    EncodedImageString = Util.encodeTobase64(bitmap);
                    Log.d(TAG, "Image String Gallary :- " + EncodedImageString);
                    Log.d(TAG, "Bitmap Gallary MODIFY_PHOTO Gallery:- " + String.valueOf(bitmap));
                    MODIFY_PHOTO = false;

                } else if (BUSINEESS_PHOTO == true) {

                    EncodedImageString = Util.encodeTobase64(bitmap);
                    Log.d(TAG, "Image String BUSINEESS_PHOTO :- " + EncodedImageString);
                    BUSINEESS_PHOTO = false;

                } else if (BUSINEESS_LOGO == true) {

                    EncodedImageString = Util.encodeTobase64(bitmap);
                    Log.d(TAG, "Image String BUSINEESS_LOGO :- " + EncodedImageString);
                    BUSINEESS_LOGO = false;

                }
            }
        }
    }



}
