package com.yberry.dinehawaii.vendor.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.HomeScreenActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorLoginWebViewActivity extends AppCompatActivity {
    String TAG = "VendorWebView";
    String url;
    Context context;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login_web_view);
        setToolbar();
        init();
        if (getIntent().hasExtra("vendor_url"))
            url = getIntent().getStringExtra("vendor_url");
        Log.e(TAG, "onCreate: url >>>>>" + url);
        startWebView(url);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        ((TextView) findViewById(R.id.headet_text)).setText(AppPreferencesBuss.getfirstname(VendorLoginWebViewActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_icon_menu, menu);
        MenuItem menuItem1 = menu.findItem(R.id.action_filter);
        menuItem1.setVisible(false);
        MenuItem menuItem2 = menu.findItem(R.id.action_logout);
        menuItem2.setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                vendorLogoutMethod();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void vendorLogoutMethod() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.LOGOUT);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
        jsonObject.addProperty("user_type", AppPreferencesBuss.getUsertypeid(context));
        Log.e(TAG + "json", jsonObject.toString());
        logoutApi(jsonObject);
    }

    private void logoutApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.request(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "logout response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        AppPreferences.clearPreference(context);
                        AppPreferencesBuss.clearPreference(context);
                        AppPreferences.clearPreference(context);
                        AppPreferencesBuss.clearPreference(context);
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, HomeScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        context = this;
        webView = (WebView) findViewById(R.id.webView);
    }

    private void startWebView(String url) {
//Create new webview Client to show progress dialog
//When opening a url or click on link
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(VendorLoginWebViewActivity.this);
            alertDialog.setMessage("Do you want to exit?");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        }
    }

    private class MyBrowser extends WebViewClient {
        final ProgressHUD progressHD = ProgressHUD.show(VendorLoginWebViewActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                progressHD.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressHD != null) {
                progressHD.dismiss();
            }
        }
    }
}
