package com.yberry.dinehawaii;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.applinks.AppLinkData;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessLoginActivity;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessRestFirstSearchBusiness;
import com.yberry.dinehawaii.Customer.Activity.GuestCustRegisterActivity;
import com.yberry.dinehawaii.Customer.Activity.GuestCustomerLoginActivity;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.GPSTracker;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.instagram_integration.ApplicationData;
import com.yberry.dinehawaii.instagram_integration.InstagramApp;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import bolts.AppLinks;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "HomeScreenActivity";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static SharedPreferences mSharedPreferences;
    private Context context;
    private GPSTracker gpsTracker;
    private LoginButton loginButton;
    private ImageView helpDialog;

    private ImageView btnFbLogin;
    private CallbackManager callbackManager;
    private ImageView instagram_btn;
    //for instagram integration
    private InstagramApp mApp;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(HomeScreenActivity.this, "Check your network.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_home_screen);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        init();
        initFacebookLogin();
        gpsTracker = new GPSTracker(HomeScreenActivity.this);
        //MobileAds.initialize(this, "ca-app-pub-6112310070932606~3563171881");
       /* mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        if (!Function.checkPermission(HomeScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Function.requestPermission(HomeScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE);
        }
    }

    private void init() {
        context = this;
        ((LinearLayout) findViewById(R.id.GuestOrCustomer)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.GuestOrCustomerRegister)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.llBussinessOrRestaurant)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.llBussinessOrRestaurantRegister)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.instagram_btn)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.imgRegister)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.tweeter_btn)).setOnClickListener(this);
        //btnConnect = (ImageView) findViewById(R.id.btnConnect);
        instagram_btn = (ImageView) findViewById(R.id.instagram_btn);
        btnFbLogin = (ImageView) findViewById(R.id.btn_login_fb);
        btnFbLogin.setOnClickListener(this);
        helpDialog = ((ImageView) findViewById(R.id.helpdialog));
        helpDialog.setOnClickListener(this);

        //  btnConnect.setOnClickListener(this);
        Log.d("printkeyhash", printKeyHash(HomeScreenActivity.this));

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                //btnConnect.setText("Disconnect");
                //llAfterLoginView.setVisibility(View.VISIBLE);
                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(HomeScreenActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        bindEventHandlers();

        if (mApp.hasAccessToken()) {

            mApp.fetchUserName(handler);
        }

    }

    private void bindEventHandlers() {
        instagram_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.GuestOrCustomer) {

            Intent intent = new Intent(context, GuestCustomerLoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        } else if (v.getId() == R.id.GuestOrCustomerRegister) {
            Intent intent = new Intent(context, GuestCustRegisterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.imgRegister) {
            Intent intent = new Intent(context, GuestCustRegisterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.llBussinessOrRestaurant) {
            Intent intent = new Intent(context, BusinessLoginActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.llBussinessOrRestaurantRegister) {
            Intent intent = new Intent(context, BusinessRestFirstSearchBusiness.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btn_login_fb) {
            loginButton.performClick();
        } else if (v.getId() == R.id.instagram_btn) {
            connectOrDisconnectUser();
        } else if (v.getId() == R.id.tweeter_btn) {

        } else if (v.getId() == R.id.helpdialog) {
            Function.bottomToolTipDialogBox(null, HomeScreenActivity.this, "Welcome to Dine Hawaii, proceed ahead by login or register.", helpDialog, null);
        }
    }

    private void initFacebookLogin() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile,email,user_birthday"));
        //loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess(): " + loginResult);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Vix.error", response.toString());
                                try {
                                    Log.d(TAG, "FACBOOK LOGIN SUCCESS" + response.toString());
                                    String userId = "", email = "", name = "", pic = "";
                                    //    getdatafromfacebuk();
                                    if (object.has("id")) {
                                        userId = Util.validateString(object.getString("id"));
                                    }
                                    if (object.has("email")) {

                                        userId = Util.validateString(object.getString("email"));
                                        //     fbEmail1 = userId;
                                        Log.d("007fbemail", userId);
                                    }
                                    if (object.has("first_name") && object.has("last_name")) {
                                        name = (Util.validateString(object.getString("first_name")) + " " + Util.validateString(object.getString("last_name"))).trim();
                                    }
                                    if (!userId.equals("")) {
                                        pic = "http://graph.facebook.com/" + userId + "/picture?type=large";
                                    }
                                    Uri targetUrl =
                                            AppLinks.getTargetUrlFromInboundIntent(HomeScreenActivity.this, getIntent());
                                    if (targetUrl != null) {
                                        Log.d("Activity", "App Link Target URL: " + targetUrl.toString());
                                    } else {
                                        AppLinkData.fetchDeferredAppLinkData(HomeScreenActivity.this,
                                                new AppLinkData.CompletionHandler() {
                                                    @Override
                                                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                                                        //process applink data
                                                    }
                                                });
                                    }


                                    String appLinkUrl, previewImageUrl;

                                    appLinkUrl = "https://fb.me/1727101414226450";
//          previewImageUrl = "https://www.mydomain.com/my_invite_image.jpg";
                                    previewImageUrl = "http://static.tumblr.com/fvzwa1s/rOsm29huz/chingon_logo_1.jpg";


                                    if (AppInviteDialog.canShow()) {
                                        AppInviteContent content = new AppInviteContent.Builder()
                                                .setApplinkUrl(appLinkUrl)
                                                .setPreviewImageUrl(previewImageUrl)
                                                .build();
                                        AppInviteDialog.show(HomeScreenActivity.this, content);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
                /*Intent intent2 = new Intent(context, MainActivity.class);
                startActivity(intent2);*/
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel()");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "onError(): " + e);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                gpsTracker.getLocation();
                break;
            }
        }
    }


    private void connectOrDisconnectUser() {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
            builder.setMessage("Disconnect from Instagram?").setCancelable(false).setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            mApp.resetAccessToken();
                            //llAfterLoginView.setVisibility(View.GONE);
                            //btnConnect.setText("Connect");

                        }
                    })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }


}
