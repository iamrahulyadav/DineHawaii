package com.yberry.dinehawaii;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.Customer.Activity.CustomerNaviDrawer;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.vendor.Activity.VendorLoginWebViewActivity;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    int SPLASHTIMEOUT = 2000;
    private Thread mThread;
    private boolean isFinish = false;
    private Context context;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0 && (!isFinish)) {
                stop();
            }
            super.handleMessage(msg);
        }
    };
    private Runnable mRunnable = new Runnable() {

        public void run() {
            SystemClock.sleep(3000);
            mHandler.sendEmptyMessage(0);
        }
    };

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        }, SPLASHTIMEOUT);
    }

    private void checkLogin() {

        if (AppPreferences.getUserType(SplashActivity.this).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.BUSINESS_USER)) {
            Intent intent = new Intent(SplashActivity.this, BusinessNaviDrawer.class);
            startActivity(intent);

        } else if (AppPreferences.getUserType(SplashActivity.this).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.BUSSINESS_LOCAL_USER)) {
            Intent intent = new Intent(SplashActivity.this, BusinessNaviDrawer.class);
            startActivity(intent);

        } else if (AppPreferences.getUserType(SplashActivity.this).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.CUSTOMER_USER)) {
            Intent intent = new Intent(SplashActivity.this, CustomerNaviDrawer.class);
            startActivity(intent);

        } else if (AppPreferences.getUserType(SplashActivity.this).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.VENDOR_USER)) {
            Intent intent = new Intent(SplashActivity.this, VendorLoginWebViewActivity.class);
            intent.putExtra("vendor_url", AppPreferencesBuss.getVendorUrl(SplashActivity.this));
            startActivity(intent);

        } else {
            init();
            // setContentView(R.layout.activity_splash);

        }

    }

    private void init() {
        context = this;
        mThread = new Thread(mRunnable);
        mThread.start();
    }

    @Override
    protected void onDestroy() {
        try {
            mThread.interrupt();
            mThread = null;
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    private void stop() {
        isFinish = true;
        Intent i = new Intent(context, HomeScreenActivity.class);
        startActivity(i);
        finish();
    }
}
