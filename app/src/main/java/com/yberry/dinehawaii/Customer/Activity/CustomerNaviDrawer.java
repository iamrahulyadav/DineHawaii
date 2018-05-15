package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yberry.dinehawaii.Customer.Fragment.CouponOffersFragment;
import com.yberry.dinehawaii.Customer.Fragment.CustomerHomeFragment;
import com.yberry.dinehawaii.Customer.Fragment.CustomerMyProfile;
import com.yberry.dinehawaii.Customer.Fragment.EGiftAndCoupons;
import com.yberry.dinehawaii.Customer.Fragment.LoyaltyPointFragment;
import com.yberry.dinehawaii.Customer.Fragment.MyReviews;
import com.yberry.dinehawaii.Customer.Fragment.NotificationCustomerFragment;
import com.yberry.dinehawaii.Customer.Fragment.OrderHistory;
import com.yberry.dinehawaii.Customer.Fragment.ReservationOrWaitListManagement;
import com.yberry.dinehawaii.Customer.Fragment.RestaurantListFragment;
import com.yberry.dinehawaii.Customer.Fragment.SendEGiftVoucher;
import com.yberry.dinehawaii.HomeScreenActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.GPSTracker;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerNaviDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentIntraction {
    public static ImageView mUserImage;
    ImageView headLogo;
    View headerView;
    GPSTracker gpsTracker;
    String fcm_id;
    TextView customerName;
    private CustomTextView headText;
    private String TAG = "CustomerNav";
    private DatabaseHandler mydb;

    @Override
    public void actionbarsetTitle(String title) {
        headText.setText(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_navi_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        headText = (CustomTextView) findViewById(R.id.headText);
        headLogo = (ImageView) findViewById(R.id.headLogo);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        customerName = (TextView) headerView.findViewById(R.id.customerName);
        customerName.setText(AppPreferences.getCustomername(CustomerNaviDrawer.this));
        mUserImage = (ImageView) headerView.findViewById(R.id.mUserImage);
        LocalBroadcastManager.getInstance(this).registerReceiver(new MyReceiver(), new IntentFilter("updateprofile"));

        if (!AppPreferences.getCustomerPic(CustomerNaviDrawer.this).equalsIgnoreCase(""))
            Picasso.with(CustomerNaviDrawer.this).load(AppPreferences.getCustomerPic(CustomerNaviDrawer.this)).placeholder(R.drawable.pic).into(mUserImage);

        navigationView.setCheckedItem(R.id.nav_home);
        fcm_id = FirebaseInstanceId.getInstance().getToken();
        callDefaultFragment();
        sendFcmToken();
        getLocation();
    }

    private void sendFcmToken() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.FCMUPDATE.SENFCMTOKEN);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(this));
        jsonObject.addProperty("fcm_id", fcm_id);
        Log.e("Home FCM token", jsonObject.toString());
        sendFcmTokenApi(jsonObject);
    }

    private void sendFcmTokenApi(JsonObject jsonObject) {
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.request(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response != null)
                    Log.e("Home FCM token", response.body().toString());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getLocation() {
        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            AppPreferences.setLatitude(this, String.valueOf(latitude));
            AppPreferences.setLongitude(this, String.valueOf(longitude));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void callDefaultFragment() {
        Fragment fragment = new CustomerHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            getSupportFragmentManager().popBackStack();
            //exitApplication();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerNaviDrawer.this);
            alertDialog.setIcon(R.drawable.ic_launcher_app);
            alertDialog.setMessage("Do you want to exit?");
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finishAffinity();

                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_navi_drawer, menu);
        return true;
    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            headText.setText("HOME");
            fragment = new CustomerHomeFragment();
        } else if (id == R.id.nav_profile) {
            headText.setText("My Profile");
            fragment = new CustomerMyProfile();
        } else if (id == R.id.nav_orderhistory) {
            headText.setText("Orders");
            Bundle bundle = new Bundle();
            bundle.putString("to", "pen");
            fragment = new OrderHistory();
            fragment.setArguments(bundle);
        } else if (id == R.id.nav_e_gift) {
            headText.setText("E-Gifts");
            fragment = new SendEGiftVoucher();
        } else if (id == R.id.nav_coupons) {
            headText.setText("E-Gift Cards");
            fragment = new EGiftAndCoupons();
        } else if (id == R.id.nav_mycoupons) {
            headText.setText("Coupons");
            fragment = new CouponOffersFragment();
        } else if (id == R.id.nav_myreview) {
            headText.setText("My Reviews");
            fragment = new MyReviews();
        } else if (id == R.id.nav_neworder) {
            headText.setText("New Order");
            fragment = new RestaurantListFragment();
        } else if (id == R.id.nav_edit_reservation) {
            headText.setText("Reservations");
            fragment = new ReservationOrWaitListManagement();
        } else if (id == R.id.nav_repeatorder) {
            headText.setText("REPEAT ORDER");
            fragment = new OrderHistory();
        } else if (id == R.id.nav_loyalityprogram) {
            headText.setText("Loyalty Points");
            fragment = new LoyaltyPointFragment();
        } else if (id == R.id.nav_notifications) {
            headText.setText("Notifications");
            fragment = new NotificationCustomerFragment();
        } else if (id == R.id.nav_setting) {
           /* headText.setText("Settings");
            fragment = new SettingFragment();*/
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerNaviDrawer.this);
            alertDialog.setMessage("Do you want to logout?");
            alertDialog.setIcon(R.drawable.ic_launcher_app);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    logoutData();
                }
            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
        return true;
    }

    private void logoutData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.LOGOUT);
        jsonObject.addProperty("user_id", AppPreferences.getCustomerid(CustomerNaviDrawer.this));
        jsonObject.addProperty("user_type", "3");
        Log.e(TAG + "logjson", jsonObject.toString());
        logoutApi(jsonObject);
    }

    private void logoutApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(CustomerNaviDrawer.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
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
                        AppPreferences.clearPreference(CustomerNaviDrawer.this);
                        AppPreferencesBuss.clearPreference(CustomerNaviDrawer.this);
                        mydb = new DatabaseHandler(CustomerNaviDrawer.this);
                        mydb.deleteCartitem();
                        Intent intent = new Intent(CustomerNaviDrawer.this, HomeScreenActivity.class);
                        Toast.makeText(CustomerNaviDrawer.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        CustomerNaviDrawer.this.finish();

                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(CustomerNaviDrawer.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CustomerNaviDrawer.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            customerName.setText(AppPreferences.getCustomername(CustomerNaviDrawer.this));
            if (!AppPreferences.getCustomerPic(CustomerNaviDrawer.this).equalsIgnoreCase(""))
                Picasso.with(context).load(AppPreferences.getCustomerPic(CustomerNaviDrawer.this)).placeholder(R.drawable.pic).into(mUserImage);
        }
    }
}
