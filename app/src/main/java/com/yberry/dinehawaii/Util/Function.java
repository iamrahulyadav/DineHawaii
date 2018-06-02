package com.yberry.dinehawaii.Util;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yberry.dinehawaii.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Created by MAX on 15/Jan/2017.
 */
public class Function {

    public static EditText EditTextPointer;
    public static String errorMessage;
    public static String fieldRequired = "This field is required!";

    public static boolean isEmailNotValid(EditText tv) {
        //add your own logic
        if (TextUtils.isEmpty(tv.getText())) {
            EditTextPointer = tv;
            errorMessage = "This field can't be empty.!";
            return true;
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(tv.getText()).matches()) {
                return false;
            } else {
                EditTextPointer = tv;
                errorMessage = "Invalid Email Id";
                return true;
            }
        }
    }

    public static boolean validatePass(String pass1, String pass2) {

        if (pass1.length() < 1 || pass2.length() < 1) {
            errorMessage = "This field can't be empty.!";
            return false;
        }

        if (pass1 != null && pass2 != null) {

            if (pass1.equals(pass2)) {


                pass1 = pass2;
                boolean hasUppercase = !pass1.equals(pass1.toLowerCase());
                boolean hasLowercase = !pass1.equals(pass1.toUpperCase());
                boolean hasNumber = pass1.matches(".*\\d.*");
                boolean noSpecialChar = pass1.matches("[a-zA-Z0-9 ]*");

                if (pass1.length() < 6) {

                    errorMessage = ("Password is too short. Needs to have 6 characters");
                    return false;
                } else if (!hasNumber) {

                    errorMessage = ("Password needs a number ");
                    return false;
                } else if (noSpecialChar) {

                    errorMessage = ("Password needs a special character i.e. !,@,#, etc.");
                    return false;
                } else {
                    return true;
                }

                /*if (!hasUppercase) {
                    logger.info(pass1 + " <-- needs uppercase");
                    retVal.append("Password needs an upper case <br>");
                }

                if (!hasLowercase) {
                    logger.info(pass1 + " <-- needs lowercase");
                    retVal.append("Password needs a lowercase <br>");
                }*/


            } else {

                errorMessage = ("Passwords don't match");
                return false;
            }
        } else {
            //logger.info("Passwords = null");
            errorMessage = ("Passwords Null ");
            return false;
        }

/*
        if(retVal.length() == 0){
            //logger.info("Password validates");
            errorMessage = ("Success");
        }*/

    }

    public static boolean confirmPassword(String pass1, String pass2) {
        if (pass2.length() < 1) {
            errorMessage = "This field can't be empty.!";
            return false;
        }

        if (pass1.equals(pass2)) {
            return true;
        } else {
            errorMessage = ("Passwords don't match");
            return false;
        }
    }

    public static boolean passwordValidation(String pass1) {

        if (pass1.length() < 1) {
            errorMessage = "This field can't be empty.!";
            return false;
        }


        boolean hasNumber = pass1.matches(".*\\d.*");
        boolean noSpecialChar = pass1.matches("[a-zA-Z0-9 ]*");

        if (pass1.length() < 6) {

            errorMessage = ("Password is too short. Needs to have 6 characters");
            return false;
        } else if (!hasNumber) {

            errorMessage = ("Password needs a number");
            return false;
        } else if (noSpecialChar) {

            errorMessage = ("Password needs a special character i.e. !,@,#, etc.");
            return false;
        } else {
            return true;
        }
    }

    public static final boolean isPhoneNumberNotValid(EditText tv) {

        if (tv.getText() == null || TextUtils.isEmpty(tv.getText())) {
            errorMessage = "This field can't be empty.!";
            return true;
        } else {

            Pattern pattern;
            Matcher matcher;
            final String MOBILE_PATTERN = "^[4-9][0-9]{9}$";
            pattern = Pattern.compile(MOBILE_PATTERN);
            matcher = pattern.matcher(tv.getText().toString());
            boolean isMatch = matcher.matches();
            if (isMatch) {
                return false;
            } else {
                EditTextPointer = tv;
                errorMessage = "Invalid Mobile No.";
                return true;
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }

    public static String generateUniqueId() {

        String uniqueid = "";
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZ";
        int string_length = 4;

        for (int i = 0; i < string_length; i++) {
            int rnum = (int) Math.floor(Math.random() * chars.length());
            uniqueid += chars.substring(rnum, rnum + 1);
        }
        return uniqueid;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }

    public static boolean isConnectingToInternet(Activity context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }

    public static boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return ni != null && ni.isConnected();
    }

    public static boolean isServiceRunning(Context context, String serviceClassName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    public static String getDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

  public static String getCurrentDateNew() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }


    public static ArrayList<String> getFutureDate(int days){
        ArrayList<String> dateList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String strDate = "";

        int value_add=1;
        for (int i = 0; i < days ; i++){
            c.add(Calendar.DATE, value_add);
            strDate = sdf.format(c.getTime());
            Log.v("Function", "S.no. -" + i + " -- Date :- " + strDate);
            dateList.add(strDate);
        }

        return dateList;
    }

    public static String getAddressFromLatlng(double latitude, double longitude) {

        String result = null;
        try {
            String sensor = "sensor=true";
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&" + sensor;
            Log.d("address", url);
            result = url;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Addresss::", e.getMessage());
        }
        return result;
    }

    



   
    /*public static String getAddressFromLatlng(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(" ");
                }
                sb.append(address.getLocality()).append(" ");
                sb.append(address.getPostalCode()).append(" ");
                sb.append(address.getCountryName());
                result = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Addresss::",e.getMessage());
        }
        return result;
    }*/

    public static String encodeImg1Tobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    public static String getDateFormate(String datetime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
            String setDate = sdf.format(date.getTime());
            return (setDate);
        } catch (Exception e) {
            return ("");
        }

    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static boolean isForeground(Context context) {//String PackageName
        // Get the Activity Manager
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // Get a list of running tasks, we are only interested in the last one,
        // the top most so we give a 1 as parameter so we only get the topmost.
        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(1);
        // Get the info we need for comparison.
        ComponentName componentInfo;
        try {
            componentInfo = task.get(0).topActivity;
        } catch (Exception e) {
            componentInfo = null;
        }
        // Check if it matches our package name.
        if (componentInfo != null && componentInfo.getPackageName().equals("com.bike4everything"))
            return true;
        // If not then our app is not on the foreground.
        return false;
    }

    public static void callPermisstion(Activity activity, int request) {

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CALL_PHONE)) {

            } else {


                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CALL_PHONE},
                        request);

            }
        }
    }

    public static void requestLocationPermission(Activity activity) {

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(activity, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(activity, "GPS permission not allows.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    public static boolean CheckGpsEnableOrNot(Context context) {
        boolean gpsStatus = false;
        try {
            LocationManager locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            gpsStatus = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            gpsStatus = false;
        }
        return gpsStatus;
    }

    public static String loadJSONFromAsset(Context context, String assetName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(assetName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static boolean checkPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    public static void requestPermission(Activity activity, String permission, int PERMISSION_REQUEST_CODE) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

            //Toast.makeText(this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    public static void bottomToolTipDialogBox(Button toolTipShowButton, Context context, String text, ImageView imageView, View view) {
        Color color ;
//        final TypedArray a = context.obtainStyledAttributes(R.styleable.TooltipLayout);
//        color = a.getColorStateList(R.styleable.);
        if (toolTipShowButton != null) {

            Tooltip.make(context,
                    new Tooltip.Builder(101)
                            .anchor(toolTipShowButton, Tooltip.Gravity.TOP)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 2000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text(text)
                            .maxWidth(600)
                            .toggleArrow(true)
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .withOverlay(true).build()
            ).show();

        } else if (imageView != null) {

            Tooltip.make(context,
                    new Tooltip.Builder(101)
                            .anchor(imageView, Tooltip.Gravity.BOTTOM)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 4000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text(text)
                            .maxWidth(600)
                            .toggleArrow(true)
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .withOverlay(true).build()
            ).show();
        } else {

            Tooltip.make(context,
                    new Tooltip.Builder(101)
                            .anchor(view, Tooltip.Gravity.BOTTOM)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 4000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text(text)
                            .maxWidth(600)
                            .toggleArrow(true)
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .withOverlay(true).build()
            ).show();

        }
        }

    public static void rightToolTipPopupWindow(Button toolTipShowButton, Context context, String text, ImageView imageView) {

        if (toolTipShowButton != null) {
            Tooltip.make(context,
                    new Tooltip.Builder(101)
                            .anchor(toolTipShowButton, Tooltip.Gravity.RIGHT)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 2000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text(text)
                            .maxWidth(600)
                            .withArrow(true)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .withOverlay(true).build()
            ).show();

        } else {
            Tooltip.make(context,
                    new Tooltip.Builder(101)
                            .anchor(imageView, Tooltip.Gravity.BOTTOM)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 4000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text(text)
                            .maxWidth(600)
                            .withArrow(true)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .withOverlay(true).build()
            ).show();
        }
    }

    public static void leftToolTipPopupWindow(Button toolTipShowButton, Context context, String text, ImageView imageView) {

        if (toolTipShowButton != null) {

            Tooltip.make(context,
                    new Tooltip.Builder(101)
                            .anchor(toolTipShowButton, Tooltip.Gravity.TOP)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 4000)
                            .activateDelay(700)
                            .showDelay(200)
                            .text(text)
                            .maxWidth(600)
                            .withArrow(true)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .withOverlay(true).build()
            ).show();

        } else {
            Tooltip.make(context,
                    new Tooltip.Builder(101)
                            .anchor(imageView, Tooltip.Gravity.BOTTOM)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 4000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text(text)
                            .maxWidth(600)
                            .withArrow(true)
                            .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                            .withStyleId(R.style.ToolTipLayoutCustomStyle)
                            .withOverlay(true).build()
            ).show();
        }

    }

    public void test() {
    }

}
