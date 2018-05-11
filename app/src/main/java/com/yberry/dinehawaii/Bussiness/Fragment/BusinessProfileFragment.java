package com.yberry.dinehawaii.Bussiness.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yberry.dinehawaii.BuildConfig;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.Customer.Fragment.CustomerMyProfile;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class BusinessProfileFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "BusinessProfile";
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_ALBUM = 2;
    private static final int REQUEST_IMAGE_CROP = 3;
    private Context context;
    private File file;
    private Uri file_uri;
    private String tempPath, imageString, name, email_id,phone,image;
    CircleImageView userImage;
    EditText fullname,email,mobileno;
    View view;
    CustomTextView txt_userNmae;
    Button submit,update;
    RelativeLayout relativeLayout;
    //  public static String image;
    String encoded_String;



    public BusinessProfileFragment() {
        // Required empty public constructor
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=container.getContext();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{ Manifest.permission.CAMERA,  Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }
        }

        view = inflater.inflate(R.layout.fragment_business_profile, container, false);

        init();

        return view;
    }

    @SuppressLint("LongLogTag")
    private void init() {

        fullname = (EditText) view.findViewById(R.id.fullname);
        fullname.setText(AppPreferencesBuss.getfirstname(getActivity()));

        Log.d("name", AppPreferencesBuss.getfirstname(getActivity()));
        // setBussiName
        email = (EditText) view.findViewById(R.id.email);
        email.setText(AppPreferencesBuss.getBussiEmilid(getActivity()));
        email.setClickable(true);
        email.setOnClickListener(this);

        txt_userNmae = (CustomTextView) view.findViewById(R.id.txt_userNmae);
        txt_userNmae.setText(AppPreferencesBuss.getfirstname(getActivity()));

        mobileno = (EditText) view.findViewById(R.id.mobileno);
        mobileno.setText(AppPreferencesBuss.getBussiPhoneNo(getActivity()));
        userImage = (CircleImageView) view.findViewById(R.id.img_circle);

        if(!AppPreferencesBuss.getProfileImage(getActivity()).equalsIgnoreCase("")) {
            Log.d("checkingImage", AppPreferencesBuss.getProfileImage (getActivity()));
            Picasso.with(context).load(AppPreferencesBuss.getProfileImage(getActivity())).placeholder(R.drawable.pic).into(userImage);
        }else
            userImage.setImageResource(R.drawable.pic);


        ((CustomButton) view.findViewById(R.id.update)).setOnClickListener(this);
        ((CustomButton) view.findViewById(R.id.btn_change_photo)).setOnClickListener(this);

        relativeLayout = (RelativeLayout)view.findViewById(R.id.relativeBusProfile);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v != null){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
    }




    private String openDialogToChosePic() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Take picture using");
        //  final boolean result = Util.checkPermission(MainActivity.this);

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

    private void getFileUri() throws IOException {
        String image_name = "img" + System.currentTimeMillis();
        File image = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file = File.createTempFile(

                image_name,  // prefix
                ".jpeg",         // suffix
                image      // directory
        );
        tempPath = "file:" + file.getAbsolutePath();
        // file_uri = Uri.fromFile(file);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            file_uri = Uri.fromFile(file);

        } else {
            Log.e("iMAGE", file.toString());
            file_uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", file);
        }
    }


    public Bitmap getResizedBitmap(Bitmap scaledBitmap, String source) {

        ExifInterface exif;
        try {
            exif = new ExifInterface(source);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif : " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif : " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif : " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }

    private void performCrop1(Uri uri) {
        Uri picUri = uri;
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setType("image");
            cropIntent.setData(picUri);
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 11);//11
            cropIntent.putExtra("aspectY", 11);//6
            cropIntent.putExtra("outputX", 410);//980
            cropIntent.putExtra("outputY", 410);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            // Toast.makeText(getActivity(), "errorMessage", Toast.LENGTH_LONG).show();

        }
    }
/*=============================================== Condition =================================================================================*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;


        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CODE_CAMERA) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),
                            Uri.parse(tempPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userImage.setVisibility(View.VISIBLE);
                Bitmap bb = getResizedBitmap(bitmap, file.getAbsolutePath());
                userImage.setImageBitmap(bb);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    bb.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                byte[] byteArray = outputStream.toByteArray();
                encoded_String = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e(TAG, "Camera Encode String " + encoded_String);


                String path1 = MediaStore.Images.Media.insertImage(getActivity().getApplicationContext().getContentResolver(), bb, "Title", "display.jpg");
                Uri uri1 = Uri.parse(path1);
                performCrop1(uri1);


            } else if (requestCode == REQUEST_CODE_ALBUM) {
                Bitmap bm = null;
                String filename = "";
                Uri photoUri = data.getData();
                String filePath = "";
                if (photoUri != null) {
                    try {
                        //We get the file path from the media info returned by the content resolver
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(photoUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        filePath = cursor.getString(columnIndex);
                        filename = filePath;
                        cursor.close();
                    } catch (Exception e) {
                    }
                }

                if (data != null) {
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                userImage.setVisibility(View.VISIBLE);


                Bitmap bb = getResizedBitmap(bm, filename);
                userImage.setImageBitmap(bb);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bb.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                byte[] b = baos.toByteArray();

                try {
                    System.gc();
                    encoded_String = Base64.encodeToString(b, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (OutOfMemoryError e) {
                    baos = new ByteArrayOutputStream();
                    bb.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                    b = baos.toByteArray();
                    encoded_String = Base64.encodeToString(b, Base64.DEFAULT);
                    Log.e("EWN", "Out of memory error catched");
                }
                String path1 = MediaStore.Images.Media.insertImage(getActivity().getApplicationContext().getContentResolver(), bb, "Title", "display.jpg");
                Uri uri1 = Uri.parse(path1);
                performCrop1(uri1);

                //encoded_String = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e(TAG, "Gallery Encode String " + encoded_String);

            } else if (requestCode == REQUEST_IMAGE_CROP) {
                if (data.getData() != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    userImage.setImageBitmap(bitmap);

                } else {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    userImage.setImageBitmap(bitmap);

                }


                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                byte[] byteArray = outputStream.toByteArray();
                // imageString = Util.encodeTobase64(bitmap);

                imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("image_string_crop", imageString);
                //    AppPreferencesBuss.setProfileImage(getActivity(), imageString);

                ///  AppPreferences.setPic(getActivity(),imageString);


            }
        }

    }





    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update) {
            dataServer();
        } else if (v.getId() == R.id.btn_change_photo) {
            openDialogToChosePic();
        }else if (v.getId()  == R.id.email){
            Toast.makeText(getContext(), "The Email id cannot be edited", Toast.LENGTH_SHORT).show();
        }
    }

    public void dataServer(){
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.REGISTRATION.UPDATE_PROFILE);
            jsonObject.addProperty("name",fullname.getText().toString() );
            jsonObject.addProperty("email", email.getText().toString());
            jsonObject.addProperty("phone_number", mobileno.getText().toString());
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
            jsonObject.addProperty("user_image",imageString);
            Log.e(TAG , "Business DATA :- " + jsonObject.toString());
            JsonCallMethod(jsonObject);

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }

    private void JsonCallMethod(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.businessprofile(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG , "Business PROFILE :- " + response.body().toString());
                String s=response.body().toString();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if(jsonObject.getString("status").equalsIgnoreCase("200")){
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            name = object.getString("name");
                            email_id = object.getString("email");
                            phone = object.getString("phone_number");
                            image = object.getString("user_image");
                            AppPreferencesBuss.setfirstname(getActivity(),name);
                            AppPreferencesBuss.setBussiPhoneNo(getActivity(),phone);
                            Intent intent = new Intent("updated_profile_bus");
                            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                AppPreferencesBuss.setProfileImage(getActivity(), object.getString("user_image"));

                            if(!AppPreferencesBuss.getProfileImage(getActivity()).equalsIgnoreCase("")) {
                                Picasso.with(context).load(AppPreferencesBuss.getProfileImage(getActivity())).placeholder(R.drawable.pic).into(userImage);
                            }
                        }
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(BusinessProfileFragment.this).attach(BusinessProfileFragment.this).commit();

                    }else {
                        String msg=jsonObject.getString("status");
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Business error:- ",t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });
    }

}
