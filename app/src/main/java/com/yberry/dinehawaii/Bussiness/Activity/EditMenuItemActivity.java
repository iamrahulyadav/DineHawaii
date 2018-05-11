package com.yberry.dinehawaii.Bussiness.Activity;

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
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yberry.dinehawaii.BuildConfig;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomCheckBox;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMenuItemActivity extends AppCompatActivity implements View.OnClickListener {
    public CustomTextView tvSelectService, text_food;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_ALBUM = 2;
    private static final int REQUEST_IMAGE_CROP = 3;
    private File file;
    private Uri file_uri;
    CustomEditText foodname, halfprice, fullprice, ingredients;
    CustomCheckBox check_half, check_full;
    private String TAG = "EditMenuItemActivity", finalHalf, finalFull;
    private String service_id = "", food_catagory_id_list = "", listValueNew = "", tempPath = "", encoded_String = "", imageString = "", edit_id = "";
    ArrayList<CheckBoxPositionModel> listFoodService;
    LinearLayout mainLayout;
    ImageView addFoodImage;
    MenuItem add, edit;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);
        setToolbar();
        init();
        setCheckBox();
    }

    private void setCheckBox() {
        check_half.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    halfprice.setVisibility(View.VISIBLE);
                } else {
                    halfprice.setVisibility(View.GONE);
                }
            }
        });

        check_full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    fullprice.setVisibility(View.VISIBLE);
                } else {
                    fullprice.setVisibility(View.GONE);
                }
            }
        });

    }

    private void init() {
        if (ContextCompat.checkSelfPermission(EditMenuItemActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(EditMenuItemActivity.this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(EditMenuItemActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }
        }
        listFoodService = new ArrayList<>();
        tvSelectService = ((CustomTextView) findViewById(R.id.tvSelectService));
        text_food = (CustomTextView) findViewById(R.id.spinSelectFoodType);
        mainLayout = (LinearLayout) findViewById(R.id.mainView);
        foodname = (CustomEditText) findViewById(R.id.edfoodname);
        halfprice = (CustomEditText) findViewById(R.id.edhalfprice);
        fullprice = (CustomEditText) findViewById(R.id.edfullprice);
        ingredients = (CustomEditText) findViewById(R.id.edingredients);
        check_half = (CustomCheckBox) findViewById(R.id.rdhalf);
        check_full = (CustomCheckBox) findViewById(R.id.rdfull);
        addFoodImage = (ImageView) findViewById(R.id.food_image);
        addFoodImage.setOnClickListener(this);
        tvSelectService.setOnClickListener(this);
        text_food.setOnClickListener(this);

        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view = EditMenuItemActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) EditMenuItemActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        getDataFromIntent();
    }

    private void getDataFromIntent() {
        if (getIntent().getAction().equals("edit_menu")) {
            tvTitle.setText("Edit Food");
            tvSelectService.setEnabled(false);
            text_food.setEnabled(false);
            foodname.setEnabled(false);
            halfprice.setEnabled(false);
            fullprice.setEnabled(false);
            check_half.setEnabled(false);
            check_full.setEnabled(false);
            addFoodImage.setEnabled(false);
            ingredients.setEnabled(false);

            if (!getIntent().getStringExtra("item_name").equalsIgnoreCase("")) {
                foodname.setText(getIntent().getStringExtra("item_name"));
            }
            if (!getIntent().getStringExtra("item_price").equalsIgnoreCase("")) {
                fullprice.setText(getIntent().getStringExtra("item_price"));
            }
            if (!getIntent().getStringExtra("half_price").equalsIgnoreCase("")) {
                check_half.setChecked(true);
                halfprice.setVisibility(View.VISIBLE);
                halfprice.setText(getIntent().getStringExtra("half_price"));
            }
            if (!getIntent().getStringExtra("item_cat").equalsIgnoreCase("")) {
                text_food.setText(getIntent().getStringExtra("item_cat"));
            }
            if (!getIntent().getStringExtra("details").equalsIgnoreCase("")) {
                ingredients.setText(getIntent().getStringExtra("details"));
            }
            if (!getIntent().getStringExtra("service_type").equalsIgnoreCase("")) {
                tvSelectService.setText(getIntent().getStringExtra("service_type"));
            }
            if (!getIntent().getStringExtra("edit_id").equalsIgnoreCase("")) {
                edit_id = getIntent().getStringExtra("edit_id");
            }
            if (!getIntent().getStringExtra("foodtype_id").equalsIgnoreCase("")) {
                food_catagory_id_list = getIntent().getStringExtra("foodtype_id");
            }  if (!getIntent().getStringExtra("service_id").equalsIgnoreCase("")) {
                service_id = getIntent().getStringExtra("service_id");
            }
            if (getIntent().hasExtra("item_image")) {
                if (!getIntent().getStringExtra("item_image").equalsIgnoreCase("")) {
                    String image = getIntent().getStringExtra("item_image");
                    Picasso.with(EditMenuItemActivity.this)
                            .load(image)
                            .placeholder(R.drawable.ic_add_a_photo_black_24dp)
                            .error(R.drawable.ic_add_a_photo_black_24dp)
                            .into(addFoodImage);
                }

            }
            Snackbar.make(findViewById(android.R.id.content), "Click on edit icon to edit food detail", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelectService:
                selectServiceData();
                break;
            case R.id.spinSelectFoodType:
                //selectFoodData();
                text_food.setText("");
                Intent intent = new Intent(EditMenuItemActivity.this, SelectFoodTypeActivity.class);
                startActivityForResult(intent, 12);
                break;
            case R.id.food_image:
                openDialogToChosePic();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_food, menu);
        add = menu.findItem(R.id.addfood);
        edit = menu.findItem(R.id.editfood);
        if (getIntent().getAction().equals("edit_menu")) {
            add.setVisible(false);
            edit.setVisible(true);
        } else if (getIntent().getAction().equals("addmenu")) {
            add.setVisible(true);
            edit.setVisible(false);
        } else {
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addfood:
                validateData();
                break;
            case R.id.editfood:
                editFoodData();
                add.setVisible(true);
                edit.setVisible(false);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editFoodData() {
        tvSelectService.setEnabled(true);
        text_food.setEnabled(true);
        foodname.setEnabled(true);
        halfprice.setEnabled(true);
        fullprice.setEnabled(true);
        check_half.setEnabled(true);
        check_full.setEnabled(true);
        ingredients.setEnabled(true);
        addFoodImage.setEnabled(true);
    }

    private void validateData() {
        if (TextUtils.isEmpty(tvSelectService.getText().toString())) {
            Toast.makeText(this, "Select Service Type", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(text_food.getText().toString())) {
            Toast.makeText(this, "Select Food Type", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(foodname.getText().toString())) {
            Toast.makeText(this, "Enter Food Name", Toast.LENGTH_SHORT).show();
        } else if (!check_half.isChecked() && !check_full.isChecked()) {
            Toast.makeText(this, "Select Quantity", Toast.LENGTH_SHORT).show();
        } else if (check_half.isChecked() && TextUtils.isEmpty(halfprice.getText().toString())) {
            Toast.makeText(this, "Enter Half Price", Toast.LENGTH_SHORT).show();
        } else if (check_full.isChecked() && TextUtils.isEmpty(fullprice.getText().toString())) {
            Toast.makeText(this, "Enter Full Price", Toast.LENGTH_SHORT).show();

        } else {
            if (Util.isNetworkAvailable(EditMenuItemActivity.this)) {
                if (halfprice.getText().toString().equals("")) {
                    finalHalf = "0.0";
                } else {
                    finalHalf = halfprice.getText().toString();
                }
                if (fullprice.getText().toString().equals("")) {
                    finalFull = "0.0";
                } else {
                    finalFull = fullprice.getText().toString();
                }
                if (!edit_id.equalsIgnoreCase("") || !edit_id.isEmpty()) {
                    editFoodMenuData();
                } else {
                    addFoodMenuData();
                }

            } else {
                Toast.makeText(EditMenuItemActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void editFoodMenuData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.KEY_EDIT_MENU);
        jsonObject.addProperty("edit_id", edit_id);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(EditMenuItemActivity.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(EditMenuItemActivity.this));
        jsonObject.addProperty("food_service_id", service_id);
        jsonObject.addProperty("food_category_id", food_catagory_id_list);
        jsonObject.addProperty("food_name", foodname.getText().toString());
        jsonObject.addProperty("price", finalFull);
        jsonObject.addProperty("half_price", finalHalf);
        jsonObject.addProperty("detail", ingredients.getText().toString());
        jsonObject.addProperty("food_image", imageString);
        Log.e(TAG, "edit Json" + jsonObject.toString());
        addFoodMenuApi(jsonObject);
    }

    private void addFoodMenuData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.KEY_ADD_MENU);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(EditMenuItemActivity.this));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(EditMenuItemActivity.this));
        jsonObject.addProperty("food_service_id", service_id);
        jsonObject.addProperty("food_category_id", food_catagory_id_list);
        jsonObject.addProperty("food_name", foodname.getText().toString());
        jsonObject.addProperty("price", finalFull);
        jsonObject.addProperty("half_price", finalHalf);
        jsonObject.addProperty("detail", ingredients.getText().toString());
        jsonObject.addProperty("food_image", imageString);
        Log.e(TAG, "add Json" + jsonObject.toString());
        addFoodMenuApi(jsonObject);
    }

    private void addFoodMenuApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(EditMenuItemActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.addeditmenu(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();
                Log.e(TAG, "Add / Edit response " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        String msg = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            msg = jsonObj.getString("msg");
                        }
                        finish();
                        Toast.makeText(EditMenuItemActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        String msg = "";
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            msg = jsonObj.getString("msg");
                        }
                        Toast.makeText(EditMenuItemActivity.this, msg, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditMenuItemActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditMenuItemActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle= (TextView) findViewById(R.id.headet_text);
        tvTitle.setText("Add Food");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private String openDialogToChosePic() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditMenuItemActivity.this);
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

    private void selectFoodData() {
        if (Util.isNetworkAvailable(EditMenuItemActivity.this)) {
            text_food.setText("");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.FOODTYPE);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(EditMenuItemActivity.this));// AppPreferencesBuss.getUserId(context));
            Log.d(TAG, "Request GET ALL FOODTYPE >> " + jsonObject);
            selectFoodApi(jsonObject);
        } else {
            Toast.makeText(EditMenuItemActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
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
            file_uri = FileProvider.getUriForFile(EditMenuItemActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
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
            // Toast.makeText(EditMenuItemActivity.this, "errorMessage", Toast.LENGTH_LONG).show();

        }
    }

    private void selectFoodApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(EditMenuItemActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("Res:", s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        Intent intent = new Intent(EditMenuItemActivity.this, SelectFoodTypeActivity.class);
                        intent.setAction("FoodFragment45");
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
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(EditMenuItemActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectServiceApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(EditMenuItemActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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
                        Intent intent = new Intent(EditMenuItemActivity.this, SelectServiceTypeActivity.class);
                        intent.putExtra("ServerType", s);
                        startActivityForResult(intent, 101);
                    }else{
                        Toast.makeText(EditMenuItemActivity.this, "No services available please import services", Toast.LENGTH_LONG).show();
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
                Toast.makeText(EditMenuItemActivity.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectServiceData() {
        if (Util.isNetworkAvailable(EditMenuItemActivity.this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLSERVICES);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(EditMenuItemActivity.this));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(EditMenuItemActivity.this));//AppPreferencesBuss.getUserId(getApplicationContext()));
            Log.e(TAG, "Request GET ALL SERVICES >> " + jsonObject.toString());
            selectServiceApi(jsonObject);
        } else {
            Toast.makeText(EditMenuItemActivity.this, "Please Connect Your Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                CheckBoxPositionModel checkBoxPositionModel = data.getParcelableExtra("serviceFoodType");
                tvSelectService.setText(checkBoxPositionModel.getName());
                service_id = checkBoxPositionModel.getId();
            } else if (requestCode == 12) {
                listFoodService = data.getParcelableArrayListExtra("foodtype");
                listValueNew = listFoodService.toString().replace("[", "").replace("]", "");
                for (CheckBoxPositionModel listValue : listFoodService) {
                    if (TextUtils.isEmpty(text_food.getText().toString())) {
                        text_food.setText(listValue.getName());
                        food_catagory_id_list = listValue.getId();
                    } else {
                        text_food.setText(text_food.getText().toString() + "," + listValue.getName());
                        food_catagory_id_list = food_catagory_id_list + "," + listValue.getId();
                    }
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(EditMenuItemActivity.this.getApplicationContext().getContentResolver(),
                            Uri.parse(tempPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addFoodImage.setVisibility(View.VISIBLE);
                Bitmap bb = getResizedBitmap(bitmap, file.getAbsolutePath());
                addFoodImage.setImageBitmap(bb);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    bb.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                byte[] byteArray = outputStream.toByteArray();
                encoded_String = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e(TAG, "Camera Encode String " + encoded_String);


                String path1 = MediaStore.Images.Media.insertImage(EditMenuItemActivity.this.getApplicationContext().getContentResolver(), bb, "Title", "display.jpg");
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
                        Cursor cursor = EditMenuItemActivity.this.getApplicationContext().getContentResolver().query(photoUri, filePathColumn, null, null, null);
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
                        bm = MediaStore.Images.Media.getBitmap(EditMenuItemActivity.this.getApplicationContext().getContentResolver(), data.getData());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                addFoodImage.setVisibility(View.VISIBLE);


                Bitmap bb = getResizedBitmap(bm, filename);
                addFoodImage.setImageBitmap(bb);

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
                String path1 = MediaStore.Images.Media.insertImage(EditMenuItemActivity.this.getApplicationContext().getContentResolver(), bb, "Title", "display.jpg");
                Uri uri1 = Uri.parse(path1);
                performCrop1(uri1);

                //encoded_String = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e(TAG, "Gallery Encode String " + encoded_String);

            } else if (requestCode == REQUEST_IMAGE_CROP) {
                if (data.getData() != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(EditMenuItemActivity.this.getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    addFoodImage.setImageBitmap(bitmap);

                } else {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    addFoodImage.setImageBitmap(bitmap);

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
                //    AppPreferencesBuss.setProfileImage(EditMenuItemActivity.this, imageString);

                ///  AppPreferences.setPic(EditMenuItemActivity.this,imageString);


            }
        }
    }

}
